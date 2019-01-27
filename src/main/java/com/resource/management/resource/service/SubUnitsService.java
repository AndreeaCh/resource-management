package com.resource.management.resource.service;

import com.resource.management.resource.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SubUnitsService {
    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private MongoTemplate template;

    @Autowired
    private ResourceStatusHistoryWriter historyWriter;

    public Optional<SubUnit> findSubUnitByName(final String name) {
        return repository.findByName(name);
    }

    public void addSubUnit(final SubUnit subUnit) {
        subUnit.setLastUpdateFirstInterventionResource(Instant.now().toString());
        saveSubUnit(subUnit);
    }

    @Transactional
    public Optional<SubUnit> updateSubUnit(final SubUnit subUnit) {
        SubUnit updatedUnit = null;

        Optional<SubUnit> existingSubUnitOptional = findSubUnitByName(subUnit.getName());
        if (existingSubUnitOptional.isPresent()) {
            SubUnit existingSubUnit = existingSubUnitOptional.get();
            updatedUnit = updateFirstInterventionResources(subUnit, existingSubUnit);
            updateOtherResources(subUnit, updatedUnit, existingSubUnit);
            updateEquipment(subUnit, updatedUnit, existingSubUnit);
        }

        return Optional.ofNullable(updatedUnit);
    }

    public synchronized Map<String, ResourceType> lockSubUnit(final String subUnitName, final ResourceType resourceType, final String sessionId) {
        Map<String, ResourceType> lockedResourceTypeBySessionId = null;
        Optional<SubUnit> subUnitOptional = findSubUnitByName(subUnitName);
        if (subUnitOptional.isPresent()) {
            SubUnit subUnit = subUnitOptional.get();
            ConcurrentHashMap<String, ResourceType> resourceTypeToLockBySessionId = new ConcurrentHashMap<>();
            resourceTypeToLockBySessionId.put(sessionId, resourceType);
            if (subUnit.getLockedResourceTypeBySessionId() == null) {
                subUnit.setLockedResourceTypeBySessionId(resourceTypeToLockBySessionId);
            } else {
                subUnit.getLockedResourceTypeBySessionId().putAll(resourceTypeToLockBySessionId);
            }
            lockedResourceTypeBySessionId = subUnit.getLockedResourceTypeBySessionId();
            saveSubUnit(subUnit);
        }
        return lockedResourceTypeBySessionId;
    }

    public Optional<SubUnit> unlockSubUnit(final String subUnitName, final ResourceType resourceType) {
        Optional<SubUnit> subUnitOptional = findSubUnitByName(subUnitName);
        if (subUnitOptional.isPresent()) {
            SubUnit subUnit = subUnitOptional.get();
            if (subUnit.getLockedResourceTypeBySessionId() != null) {
                Optional<String> keyToBeRemoved = subUnit.getLockedResourceTypeBySessionId()
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().equals(resourceType))
                        .findFirst()
                        .map(Map.Entry::getKey);
                if (keyToBeRemoved.isPresent()) {
                    subUnit.getLockedResourceTypeBySessionId().remove(keyToBeRemoved.get());
                    saveSubUnit(subUnit);
                }
            }
        }
        return subUnitOptional;
    }

    public Optional<SubUnit> unlockSubUnitLockedBySession(final String sessionId) {
        Optional<SubUnit> subUnit = repository.findAll()
                .stream()
                .filter(s -> s.getLockedResourceTypeBySessionId().get(sessionId) != null)
                .findFirst();
        subUnit.ifPresent(s ->
        {
            s.getLockedResourceTypeBySessionId().remove(sessionId);
            saveSubUnit(s);
        });

        return subUnit;
    }

    public void unlockAllSubUnits() {
        repository.findAll().forEach(s ->
        {
            s.setLockedResourceTypeBySessionId(null);
            saveSubUnit(s);
        });
    }

    public void deleteSubUnit(final String name) {
        repository.deleteById(name);
    }

    private synchronized void saveSubUnit(final SubUnit subUnit) {
        repository.save(subUnit);
    }

    public List<ResourceLog> getLogForResource(final String plateNumber) {
        List<ResourceLog> resourceLog = new ArrayList<>();
        List<SubUnit> subUnits = repository.findAll();
        final Optional<Resource> resourceOptional =
                subUnits.stream().flatMap(subUnit -> subUnit.getResources().stream()
                        .filter(resource -> resource.getPlateNumber().equals(plateNumber))).findFirst();
        if (resourceOptional.isPresent()) {
            resourceLog = resourceOptional.get().getResourceLogs();
        }

        return resourceLog;
    }

    public Optional<SubUnit> updateResourceStatus(String plateNumber, ResourceStatus resourceStatus, String ipAddress) {
        final Optional<SubUnit> subUnitOptional = getSubUnitWithPlateNumber(plateNumber);
        if (subUnitOptional.isPresent()) {
            final Optional<Resource> resourceOptional = getResourceWithPlateNumber(subUnitOptional.get(), plateNumber);
            if (resourceOptional.isPresent()) {
                Resource resource = resourceOptional.get();
                resource.setStatus(resourceStatus);
                if (resource.getResourceLogs() == null) {
                    resource.setResourceLogs(new ArrayList<>());
                }

                ResourceLog resourceLog = new ResourceLog(UUID.randomUUID(), Instant.now().toString(), ipAddress, resourceStatus);
                resource.getResourceLogs()
                        .add(resourceLog);
                saveSubUnit(subUnitOptional.get());
                historyWriter.addLogToFile(plateNumber, resourceLog);
            }
        }

        return subUnitOptional;
    }

    private SubUnit updateEquipment(SubUnit subUnit, SubUnit updatedUnit, SubUnit existingSubUnit) {
        List<Equipment> existingEquipment = getEquipment(existingSubUnit);
        List<Equipment> updatedEquipment = getEquipment(subUnit);
        if (checkIfEquipmentUpdated(existingEquipment, updatedEquipment)) {
            Query query = new Query().addCriteria(Criteria.where("name").is(subUnit.getName()));
            Update update =
                    new Update().set("lastUpdateEquipment", Instant.now().toString()).set("equipment", subUnit.getEquipment());
            updatedUnit =
                    template.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), SubUnit.class);
        }
        return updatedUnit;
    }

    private SubUnit updateOtherResources(SubUnit subUnit, SubUnit updatedUnit, SubUnit existingSubUnit) {
        List<Resource> existingOtherResources = getOtherResources(existingSubUnit);
        List<Resource> updatedOtherResources = getOtherResources(subUnit);

        if (checkIfResourcesUpdated(existingOtherResources, updatedOtherResources)) {
            Query query = new Query().addCriteria(Criteria.where("name").is(subUnit.getName()));
            Update update =
                    new Update().set("lastUpdateOtherResource", Instant.now().toString()).set("resources", subUnit.getResources());
            updatedUnit =
                    template.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), SubUnit.class);

        }
        return updatedUnit;
    }

    private SubUnit updateFirstInterventionResources(SubUnit subUnit, SubUnit existingSubUnit) {
        SubUnit updatedUnit = null;
        List<Resource> existingFirstInterventionResources = getFirstInterventionResources(existingSubUnit);
        List<Resource> updatedFirstInterventionResources = getFirstInterventionResources(subUnit);

        if (checkIfResourcesUpdated(existingFirstInterventionResources, updatedFirstInterventionResources)) {
            Query query = new Query().addCriteria(Criteria.where("name").is(subUnit.getName()));
            Update update =
                    new Update().set("lastUpdateFirstInterventionResource", Instant.now().toString()).set("resources", subUnit.getResources());
            updatedUnit =
                    template.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), SubUnit.class);
        }

        return updatedUnit;
    }

    private boolean checkIfEquipmentUpdated(List<Equipment> existingEquipment, List<Equipment> updatedEquipment) {
        boolean updated = true;
        if (existingEquipment != null && updatedEquipment != null && existingEquipment.size() == updatedEquipment.size()
                && existingEquipment.containsAll(updatedEquipment)) {
            updated = false;
        } else if (updatedEquipment == null && existingEquipment == null) {
            updated = false;
        }

        return updated;
    }

    private boolean checkIfResourcesUpdated(List<Resource> existingResources, List<Resource> updatedResources) {
        boolean response = true;
        if (existingResources != null && updatedResources != null && existingResources.size() == updatedResources.size()
                && existingResources.containsAll(updatedResources)) {
            response = false;
        } else if (updatedResources == null && existingResources == null) {
            response = false;
        }

        return response;
    }

    private List<Resource> getFirstInterventionResources(SubUnit existingSubUnit) {
        return existingSubUnit.getResources().stream()
                .filter(resource -> resource.getType().equals(ResourceType.FIRST_INTERVENTION)).collect(Collectors.toList());
    }

    private List<Resource> getOtherResources(SubUnit existingSubUnit) {
        return existingSubUnit.getResources().stream()
                .filter(resource -> resource.getType().equals(ResourceType.OTHER)).collect(Collectors.toList());
    }

    private List<Equipment> getEquipment(SubUnit existingSubUnit) {
        return existingSubUnit.getEquipment();
    }


    private Optional<Resource> getResourceWithPlateNumber(final SubUnit subUnit, final String plateNumber) {
        return subUnit.getResources().stream().filter(r -> r.getPlateNumber().equals(plateNumber)).findFirst();
    }


    private Optional<SubUnit> getSubUnitWithPlateNumber(String plateNumber) {
        List<SubUnit> subUnits = repository.findAll();
        return subUnits.stream()
                .filter(s -> s.getResources().stream().anyMatch(r -> r.getPlateNumber().equals(plateNumber)))
                .findFirst();
    }
}
