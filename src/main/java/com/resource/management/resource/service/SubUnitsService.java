package com.resource.management.resource.service;

import com.resource.management.resource.model.*;
import com.resource.management.resource.model.configuration.SubUnitsConfiguration;
import com.resource.management.resource.model.configuration.SubUnitsConfigurationRepository;
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

import static com.resource.management.resource.model.configuration.SubUnitsConfiguration.ID;

@Service
public class SubUnitsService {

    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private SubUnitsConfigurationRepository configurationRepository;

    @Autowired
    private MongoTemplate template;

    @Autowired
    private LogEntryWriter historyWriter;

    public Optional<SubUnit> findSubUnitById(final String id) {
        return repository.findById(id);
    }


    public void addSubUnit(final String id, final String subUnitName) {
        SubUnit subUnit = createSubUnit(id, subUnitName);
        saveSubUnit(subUnit);

        Optional<SubUnitsConfiguration> subUnitsConfiguration = configurationRepository.findById(ID);
        subUnitsConfiguration.ifPresent(configuration -> {
            configuration.getOrderedSubUnitIds().add(subUnit.getId());
            configurationRepository.save(configuration);
        });
    }

      subUnit.setLastUpdateReserveResource(lastUpdate);

    @Transactional
    public Optional<SubUnit> updateSubUnit(final SubUnit subUnit, final String ipAddress) {
        SubUnit updatedUnit = null;

        Optional<SubUnit> existingSubUnitOptional = findSubUnitById(subUnit.getId());
        if (existingSubUnitOptional.isPresent()) {
            SubUnit existingSubUnit = existingSubUnitOptional.get();
            updatedUnit = updateFirstInterventionResources(subUnit, existingSubUnit);
            updatedUnit = updateOtherResources(subUnit, updatedUnit, existingSubUnit);
            updatedUnit = updateEquipment(subUnit, updatedUnit, existingSubUnit);
            updatedUnit = updateReserves(subUnit, updatedUnit, existingSubUnit);
        }

        if (updatedUnit != null && existingSubUnitOptional.isPresent()) {
            writeLogEntry(existingSubUnitOptional.get(), updatedUnit, ipAddress);
        }

        return Optional.ofNullable(updatedUnit);
    }


    public synchronized Map<String, ResourceType> lockSubUnit(final String subUnitId,
                                                              final ResourceType resourceType, final String sessionId) {
        Map<String, ResourceType> lockedResourceTypeBySessionId = null;
        Optional<SubUnit> subUnitOptional = findSubUnitById(subUnitId);
        if (subUnitOptional.isPresent()) {
            SubUnit subUnit = subUnitOptional.get();
            if (!isResourceAlreadyLocked(subUnit, resourceType)) {
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
        }

        return lockedResourceTypeBySessionId;
    }

    private boolean isResourceAlreadyLocked(SubUnit subUnit, ResourceType resourceType) {
        return subUnit.getLockedResourceTypeBySessionId() != null && subUnit.getLockedResourceTypeBySessionId().values().contains(resourceType);
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

    public Map<SubUnit, ResourceType> unlockSubUnitsLockedBySession(final String sessionId) {
        List<SubUnit> lockedSubUnits = repository.findAll()
                .stream()
                .filter(s -> s.getLockedResourceTypeBySessionId() != null
                        && s.getLockedResourceTypeBySessionId().get(sessionId) != null)
                .collect(Collectors.toList());

        Map<SubUnit, ResourceType> lockedResourceTypes = new HashMap<>();
        lockedSubUnits.forEach(s -> {
            lockedResourceTypes.put(s, s.getLockedResourceTypeBySessionId().get(sessionId));
            s.getLockedResourceTypeBySessionId().remove(sessionId);
            saveSubUnit(s);
        });

        return lockedResourceTypes;
    }

    public void unlockAllSubUnits() {
        repository.findAll().forEach(s ->
        {
            s.setLockedResourceTypeBySessionId(null);
            saveSubUnit(s);
        });
    }

    public void deleteSubUnit(final String id) {
        repository.deleteById(id);
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

    public Optional<SubUnit> updateResourceStatus(String plateNumber, ResourceStatus resourceStatus, String
            ipAddress) {
        final Optional<SubUnit> subUnitOptional = getSubUnitWithPlateNumber(plateNumber);
        if (subUnitOptional.isPresent()) {
            final Optional<Resource> resourceOptional = getResourceWithPlateNumber(subUnitOptional.get(), plateNumber);
            if (resourceOptional.isPresent()) {
                Resource resource = resourceOptional.get();
                resource.setStatus(resourceStatus);
                if (resource.getResourceLogs() == null) {
                    resource.setResourceLogs(new ArrayList<>());
                }

                ResourceLog resourceLog = new ResourceLog(UUID.randomUUID(), Instant.now().toString(), ipAddress, null, resourceStatus);
                resource.getResourceLogs()
                        .add(resourceLog);
                saveSubUnit(subUnitOptional.get());
                historyWriter.addLogToFile(
                        plateNumber + " - " + resource.getIdentificationNumber(),
                        resourceLog.toString());
            }
        }

        return subUnitOptional;
    }

    public Optional<SubUnit> updateResourceType(String plateNumber, ResourceType resourceType, String ipAddress) {
        final Optional<SubUnit> subUnitOptional = getSubUnitWithPlateNumber(plateNumber);
        if (subUnitOptional.isPresent()) {
            final Optional<Resource> resourceOptional = getResourceWithPlateNumber(subUnitOptional.get(), plateNumber);
            if (resourceOptional.isPresent()) {
                Resource resource = resourceOptional.get();
                resource.setType(resourceType);
                if(resourceType.equals(ResourceType.RESERVE)) {
                    resource.setStatus(new ResourceStatus(ResourceStatus.Status.OPERATIONAL));
                } else {
                    resource.setStatus(new ResourceStatus(ResourceStatus.Status.AVAILABLE));
                }
                if (resource.getResourceLogs() == null) {
                    resource.setResourceLogs(new ArrayList<>());
                }
                ResourceLog resourceLog = new ResourceLog(UUID.randomUUID(), Instant.now().toString(), ipAddress, resourceType, resource.getStatus());
                resource.getResourceLogs()
                        .add(resourceLog);
                saveSubUnit(subUnitOptional.get());
                historyWriter.addLogToFile(
                        plateNumber + " - " + resource.getIdentificationNumber(),
                        resourceLog.toString());
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

    private SubUnit updateReserves(SubUnit subUnit, SubUnit updatedUnit, SubUnit existingSubUnit) {
        List<Resource> existingReserveResources = getReserveResources(existingSubUnit);
        List<Resource> updatedReserveResources = getReserveResources(subUnit);

        if (checkIfResourcesUpdated(existingReserveResources, updatedReserveResources)) {
            Query query = new Query().addCriteria(Criteria.where("name").is(subUnit.getName()));
            Update update =
                    new Update().set("lastUpdateReserveResource", Instant.now().toString()).set("resources", subUnit.getResources());
            updatedUnit =
                    template.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), SubUnit.class);
        }
        return updatedUnit;
    }

    private boolean checkIfEquipmentUpdated
            (List<Equipment> existingEquipment, List<Equipment> updatedEquipment) {
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

    private List<Resource> getReserveResources(SubUnit existingSubUnit) {
        return existingSubUnit.getResources().stream()
                .filter(resource -> resource.getType().equals(ResourceType.RESERVE)).collect(Collectors.toList());
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


    private void writeLogEntry(final SubUnit initialSubUnit, final SubUnit updatedSubUnit, final String ipAddress) {
        final Map<String, Resource> initalResourcesMap = initialSubUnit.getResources().stream().collect(
                Collectors.toMap(Resource::getPlateNumber, resource -> resource));
        final Map<String, Resource> updatedResourcesMap = updatedSubUnit.getResources().stream().collect(
                Collectors.toMap(Resource::getPlateNumber, resource -> resource));

        filterAndLogDeletedResources(ipAddress, initalResourcesMap, updatedResourcesMap);

        filterAndLogAddedResources(ipAddress, initalResourcesMap, updatedResourcesMap);

        filterAndLogUpdatedResources(ipAddress, initalResourcesMap, updatedResourcesMap);
    }


    private void filterAndLogUpdatedResources(
            final String ipAddress, final Map<String, Resource> initalResourcesMap,
            final Map<String, Resource> updatedResourcesMap) {
        final List<String> updatedResources = initalResourcesMap.keySet().stream().filter(
                s -> updatedResourcesMap.keySet().contains(s) && !updatedResourcesMap.get(s).equals(
                        initalResourcesMap.get(s))).collect(Collectors.toList());

        updatedResources.forEach(plateNumber ->
        {
            final Resource resource = initalResourcesMap.get(plateNumber);
            String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
            historyWriter.addLogToFile(
                    resourceIdentifier,
                    "Data&ora='" + Instant.now() + '\'' + ", IP='" + ipAddress + '\'' + ", " + "Resursa editata!");
        });
    }


    private void filterAndLogAddedResources(
            final String ipAddress, final Map<String, Resource> initalResourcesMap,
            final Map<String, Resource> updatedResourcesMap) {
        final List<String> addedResources = updatedResourcesMap.keySet().stream().filter(
                s -> !initalResourcesMap.keySet().contains(s)).collect(Collectors.toList());

        addedResources.forEach(plateNumber ->
        {
            final Resource resource = updatedResourcesMap.get(plateNumber);
            String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
            historyWriter.addLogToFile(
                    resourceIdentifier,
                    "Data&ora='" + Instant.now() + '\'' + ", IP='" + ipAddress + '\'' + ", " + "Resursa adaugata!");
        });
    }


    private void filterAndLogDeletedResources(
            final String ipAddress, final Map<String, Resource> initalResourcesMap,
            final Map<String, Resource> updatedResourcesMap) {
        final List<String> deletedResources = initalResourcesMap.keySet().stream().filter(
                s -> !updatedResourcesMap.keySet().contains(s)).collect(Collectors.toList());

        deletedResources.forEach(plateNumber ->
        {
            final Resource resource = initalResourcesMap.get(plateNumber);
            String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
            historyWriter.addLogToFile(
                    resourceIdentifier,
                    "Data&ora='" + Instant.now() + '\'' + ", IP='" + ipAddress + '\'' + ", " + "Resursa stearsa!");
        });
    }

    private static Comparator<SubUnit> subUnitsOrdering(List<String> subUnitIds) {
        return Comparator.comparingInt(v -> subUnitIds.indexOf(v.getId()));
    }

    private SubUnit createSubUnit(String id, String subUnitName) {
        String lastUpdate = Instant.now().toString();
        SubUnit subUnit = new SubUnit();
        subUnit.setId(id);
        subUnit.setName(subUnitName);
        subUnit.setLastUpdateFirstInterventionResource(lastUpdate);
        subUnit.setLastUpdateEquipment(lastUpdate);
        subUnit.setLastUpdateOtherResource(lastUpdate);
        subUnit.setLastUpdateReserveResource(lastUpdate);
        subUnit.setResources(new ArrayList<>());
        subUnit.setEquipment(new ArrayList<>());
        subUnit.setLockedResourceTypeBySessionId(new HashMap<>());
        return subUnit;
    }
}
