package com.resource.management.resource.service;

import com.resource.management.resource.model.Resource;
import com.resource.management.resource.model.ResourceLog;
import com.resource.management.resource.model.ResourceStatus;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitsRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        subUnit.setLastUpdate(Instant.now().toString());
        saveSubUnit(subUnit);
    }


    @Transactional
    public Optional<SubUnit> updateSubUnit(final SubUnit subUnit) {
        Query query =
                new Query().addCriteria(Criteria.where("name").is(subUnit.getName()))
                           .addCriteria(Criteria.where("resources").ne(subUnit.getResources()));
        Update update =
                new Update().set("lastUpdate", Instant.now().toString()).set("resources", subUnit.getResources());
        SubUnit updatedUnitResources =
                template.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), SubUnit.class);

        query =
                new Query().addCriteria(Criteria.where("name").is(subUnit.getName()))
                           .addCriteria(Criteria.where("equipment").ne(subUnit.getEquipment()));

        update =
                new Update().set("lastUpdate", Instant.now().toString()).set("equipment", subUnit.getEquipment());

        SubUnit updatedUnitEquipment =
                template.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), SubUnit.class);

        if (updatedUnitEquipment != null) { // this returns an updated unit that contains the latest verion of the subunit
            return Optional.of(updatedUnitEquipment);
        } else {
            return Optional.ofNullable(updatedUnitResources);
        }
    }


    public synchronized Map<String, ResourceType> lockSubUnit(final String subUnitName, final ResourceType resourceType, final String sessionId) {
        Map<String, ResourceType> lockedResourceTypeBySessionId = null;
        Optional<SubUnit> subUnitOptional = findSubUnitByName(subUnitName);
        if (subUnitOptional.isPresent()) {
            SubUnit subUnit = subUnitOptional.get();
            HashMap<String, ResourceType> resourceTypeToLockBySessionId = new HashMap<>();
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
                subUnit.getLockedResourceTypeBySessionId()
                       .entrySet()
                       .stream()
                       .filter(entry -> entry.getValue().equals(resourceType))
                       .forEach(entry -> subUnit.getLockedResourceTypeBySessionId().remove(entry.getKey()));
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
