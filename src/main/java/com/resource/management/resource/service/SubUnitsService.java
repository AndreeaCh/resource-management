package com.resource.management.resource.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.resource.management.resource.model.Resource;
import com.resource.management.resource.model.ResourceLog;
import com.resource.management.resource.model.ResourceStatus;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitsRepository;

@Service
public class SubUnitsService {
    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private MongoTemplate template;

    @Autowired
    private LogEntryWriter historyWriter;

    public Optional<SubUnit> findSubUnitByName(final String name) {
        return repository.findByName(name);
    }


    public void addSubUnit(final SubUnit subUnit) {
        subUnit.setLastUpdate(Instant.now().toString());
        saveSubUnit(subUnit);
    }


    @Transactional
    public Optional<SubUnit> updateSubUnit( final SubUnit subUnit, final String ipAddress )
    {
        Query query =
              new Query().addCriteria( Criteria.where( "name" ).is( subUnit.getName() ) )
                    .addCriteria( Criteria.where( "resources" ).ne( subUnit.getResources() ) );

        Optional<SubUnit> initialSubUnit = template.find( query, SubUnit.class ).stream().findFirst();

        Update update =
              new Update().set( "lastUpdate", Instant.now().toString() ).set( "resources", subUnit.getResources() );
        SubUnit updatedUnitResources =
              template.findAndModify( query, update, new FindAndModifyOptions().returnNew( true ), SubUnit.class );

        query =
              new Query().addCriteria( Criteria.where( "name" ).is( subUnit.getName() ) )
                    .addCriteria( Criteria.where( "equipment" ).ne( subUnit.getEquipment() ) );

        update =
              new Update().set( "lastUpdate", Instant.now().toString() ).set( "equipment", subUnit.getEquipment() );

        SubUnit updatedUnitEquipment =
              template.findAndModify( query, update, new FindAndModifyOptions().returnNew( true ), SubUnit.class );

        Optional<SubUnit> subUnitOptional;
        if (updatedUnitEquipment != null) { // this returns an updated unit that contains the latest verion of the subunit
            subUnitOptional = Optional.of( updatedUnitEquipment );
        } else {
            subUnitOptional = Optional.ofNullable( updatedUnitResources );
        }

        if ( subUnitOptional.isPresent() && initialSubUnit.isPresent() )
        {
            writeLogEntry( initialSubUnit.get(), subUnitOptional.get(), ipAddress );
        }

        return subUnitOptional;
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
                historyWriter.addLogToFile( plateNumber + " - " + resource.getIdentificationNumber(),
                      resourceLog.toString() );
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


    private void writeLogEntry( final SubUnit initialSubUnit, final SubUnit updatedSubUnit, final String ipAddress )
    {
        final Map<String, Resource> initalResourcesMap = initialSubUnit.getResources().stream().collect(
              Collectors.toMap( Resource::getPlateNumber, resource -> resource ) );
        final Map<String, Resource> updatedResourcesMap = updatedSubUnit.getResources().stream().collect(
              Collectors.toMap( Resource::getPlateNumber, resource -> resource ) );

        filterAndLogDeletedResources( ipAddress, initalResourcesMap, updatedResourcesMap );

        filterAndLogAddedResources( ipAddress, initalResourcesMap, updatedResourcesMap );

        filterAndLogUpdatedResources( ipAddress, initalResourcesMap, updatedResourcesMap );
    }


    private void filterAndLogUpdatedResources( final String ipAddress, final Map<String, Resource> initalResourcesMap,
          final Map<String, Resource> updatedResourcesMap )
    {
        final List<String> updatedResources = initalResourcesMap.keySet().stream().filter(
              s -> updatedResourcesMap.keySet().contains( s ) && !updatedResourcesMap.get( s ).equals(
                    initalResourcesMap.get( s ) ) ).collect( Collectors.toList() );

        updatedResources.forEach( plateNumber ->
        {
            final Resource resource = initalResourcesMap.get( plateNumber );
            String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
            historyWriter.addLogToFile( resourceIdentifier,
                  "Data&ora='" + Instant.now() + '\'' + ", IP='" + ipAddress + '\'' + ", " + "Resursa editata!" );
        } );
    }


    private void filterAndLogAddedResources( final String ipAddress, final Map<String, Resource> initalResourcesMap,
          final Map<String, Resource> updatedResourcesMap )
    {
        final List<String> addedResources = updatedResourcesMap.keySet().stream().filter(
              s -> !initalResourcesMap.keySet().contains( s ) ).collect( Collectors.toList() );

        addedResources.forEach( plateNumber ->
        {
            final Resource resource = updatedResourcesMap.get( plateNumber );
            String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
            historyWriter.addLogToFile( resourceIdentifier,
                  "Data&ora='" + Instant.now() + '\'' + ", IP='" + ipAddress + '\'' + ", " + "Resursa adaugata!" );
        } );
    }


    private void filterAndLogDeletedResources( final String ipAddress, final Map<String, Resource> initalResourcesMap,
          final Map<String, Resource> updatedResourcesMap )
    {
        final List<String> deletedResources = initalResourcesMap.keySet().stream().filter(
              s -> !updatedResourcesMap.keySet().contains( s ) ).collect( Collectors.toList() );

        deletedResources.forEach( plateNumber ->
        {
            final Resource resource = initalResourcesMap.get( plateNumber );
            String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
            historyWriter.addLogToFile( resourceIdentifier,
                  "Data&ora='" + Instant.now() + '\'' + ", IP='" + ipAddress + '\'' + ", " + "Resursa stearsa!" );
        } );
    }
}
