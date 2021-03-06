package com.resource.management.resource.service;

import static com.resource.management.resource.model.configuration.SubUnitsConfiguration.ID;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

import com.resource.management.resource.model.Equipment;
import com.resource.management.resource.model.Resource;
import com.resource.management.resource.model.ResourceLog;
import com.resource.management.resource.model.ResourceStatus;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitsRepository;
import com.resource.management.resource.model.configuration.SubUnitsConfiguration;
import com.resource.management.resource.model.configuration.SubUnitsConfigurationRepository;
import com.resource.management.services.model.LastUpdatedTimestampRepository;
import com.resource.management.services.model.ServiceRepository;

@Service
public class SubUnitsService
{

   @Autowired
   private SubUnitsRepository repository;

   @Autowired
   private SubUnitsConfigurationRepository configurationRepository;

   @Autowired
   private ServiceRepository serviceRepository;

   @Autowired
   private LastUpdatedTimestampRepository timestampRepository;

   @Autowired
   private MongoTemplate template;

   @Autowired
   private LogEntryWriter historyWriter;


   public Optional<SubUnit> findSubUnitById( final String id )
   {
      return this.repository.findById( id );
   }


   public void addSubUnit( final String id, final String subUnitName )
   {
      final SubUnit subUnit = createSubUnit( id, subUnitName );
      saveSubUnit( subUnit );

      final Optional<SubUnitsConfiguration> subUnitsConfiguration = this.configurationRepository.findById( ID );
      subUnitsConfiguration.ifPresent( configuration ->
      {
         configuration.getOrderedSubUnitIds().add( subUnit.getId() );
         this.configurationRepository.save( configuration );
      } );
   }


   @Transactional
   public List<SubUnit> updateSubUnitsOrder( final List<String> subUnitIds )
   {
      final List<SubUnit> subUnits = this.repository.findAll();
      this.configurationRepository.save( new SubUnitsConfiguration( ID, subUnitIds ) );
      final List<SubUnit> orderedSubUnits =
            subUnits.stream().sorted( subUnitsOrdering( subUnitIds ) ).collect( Collectors.toList() );
      this.repository.deleteAll();
      this.repository.saveAll( orderedSubUnits );
      return orderedSubUnits;
   }


   @Transactional
   public Optional<SubUnit> updateSubUnitName( final String id, final String name )
   {
      SubUnit updatedUnit = null;

      final Optional<SubUnit> existingSubUnitOptional = findSubUnitById( id );
      if ( existingSubUnitOptional.isPresent() )
      {
         updateServices( name, existingSubUnitOptional.get().getName() );
         updatedUnit = existingSubUnitOptional.get();
         updatedUnit.setName( name );
         this.repository.save( updatedUnit );

      }

      return Optional.ofNullable( updatedUnit );
   }


   private void updateServices( final String updatedSubUnitName, final String oldSubUnitName )
   {
      final Iterable<com.resource.management.services.model.Service> services =
            this.serviceRepository.findBySubUnit( oldSubUnitName );
      services.forEach( service ->
      {
         service.setSubUnit( updatedSubUnitName );
         this.serviceRepository.save( service );
         this.timestampRepository.saveTodaysTimestamp();
         this.timestampRepository.saveTomorrowsTimestamp();
      } );
   }


   @Transactional
   public Optional<SubUnit> updateSubUnit( final SubUnit subUnit, final String ipAddress )
   {
      SubUnit updatedUnit = null;

      final Optional<SubUnit> existingSubUnitOptional = findSubUnitById( subUnit.getId() );
      if ( existingSubUnitOptional.isPresent() )
      {
         final SubUnit existingSubUnit = existingSubUnitOptional.get();
         updatedUnit = updateFirstInterventionResources( subUnit, existingSubUnit );
         updatedUnit = updateOtherResources( subUnit, updatedUnit, existingSubUnit );
         updatedUnit = updateEquipment( subUnit, updatedUnit, existingSubUnit );
         updatedUnit = updateReserves( subUnit, updatedUnit, existingSubUnit );
      }

      if ( updatedUnit != null )
      {
         writeLogEntry( existingSubUnitOptional.get(), updatedUnit, ipAddress );
      }
      else
      {
         updatedUnit = existingSubUnitOptional.orElse( null );
      }

      return Optional.ofNullable( updatedUnit );
   }


   public synchronized Map<String, ResourceType> lockSubUnit( final String subUnitId, final ResourceType resourceType,
         final String sessionId )
   {
      Map<String, ResourceType> lockedResourceTypeBySessionId = null;
      final Optional<SubUnit> subUnitOptional = findSubUnitById( subUnitId );
      if ( subUnitOptional.isPresent() )
      {
         final SubUnit subUnit = subUnitOptional.get();
         if ( !isResourceAlreadyLocked( subUnit, resourceType ) )
         {
            final ConcurrentHashMap<String, ResourceType> resourceTypeToLockBySessionId = new ConcurrentHashMap<>();
            resourceTypeToLockBySessionId.put( sessionId, resourceType );
            if ( subUnit.getLockedResourceTypeBySessionId() == null )
            {
               subUnit.setLockedResourceTypeBySessionId( resourceTypeToLockBySessionId );
            }
            else
            {
               subUnit.getLockedResourceTypeBySessionId().putAll( resourceTypeToLockBySessionId );
            }

            lockedResourceTypeBySessionId = subUnit.getLockedResourceTypeBySessionId();
            saveSubUnit( subUnit );
         }
      }

      return lockedResourceTypeBySessionId;
   }


   private boolean isResourceAlreadyLocked( final SubUnit subUnit, final ResourceType resourceType )
   {
      return subUnit.getLockedResourceTypeBySessionId() != null
            && subUnit.getLockedResourceTypeBySessionId().values().contains( resourceType );
   }


   public Optional<SubUnit> unlockSubUnit( final String subUnitId, final ResourceType resourceType )
   {
      final Optional<SubUnit> subUnitOptional = findSubUnitById( subUnitId );
      if ( subUnitOptional.isPresent() )
      {
         final SubUnit subUnit = subUnitOptional.get();
         if ( subUnit.getLockedResourceTypeBySessionId() != null )
         {
            final Optional<String> keyToBeRemoved =
                  subUnit.getLockedResourceTypeBySessionId().entrySet().stream()
                        .filter( entry -> entry.getValue().equals( resourceType ) ).findFirst()
                        .map( Map.Entry::getKey );
            if ( keyToBeRemoved.isPresent() )
            {
               subUnit.getLockedResourceTypeBySessionId().remove( keyToBeRemoved.get() );
               saveSubUnit( subUnit );
            }
         }
      }
      return subUnitOptional;
   }


   public Map<SubUnit, ResourceType> unlockSubUnitsLockedBySession( final String sessionId )
   {
      final List<SubUnit> lockedSubUnits =
            this.repository.findAll().stream()
                  .filter( s -> s.getLockedResourceTypeBySessionId() != null
                        && s.getLockedResourceTypeBySessionId().get( sessionId ) != null )
                  .collect( Collectors.toList() );

      final Map<SubUnit, ResourceType> lockedResourceTypes = new HashMap<>();
      lockedSubUnits.forEach( s ->
      {
         lockedResourceTypes.put( s, s.getLockedResourceTypeBySessionId().get( sessionId ) );
         s.getLockedResourceTypeBySessionId().remove( sessionId );
         saveSubUnit( s );
      } );

      return lockedResourceTypes;
   }


   public void unlockAllSubUnits()
   {
      this.repository.findAll().forEach( s ->
      {
         s.setLockedResourceTypeBySessionId( null );
         saveSubUnit( s );
      } );
   }


   public void deleteSubUnit( final String id, final String ipAddress )
   {
      final Optional<SubUnit> existingSubUnitOptional = findSubUnitById( id );
      existingSubUnitOptional.ifPresent( subUnit -> subUnit.getResources().forEach( resource ->
      {
         String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
         historyWriter.addLogToFile( resourceIdentifier,
               "Data&ora='" + Instant.now() + '\'' + ", IP='" + ipAddress + '\'' + ", " + "Resursa stearsa!" );
      } ) );

      this.repository.deleteById( id );
   }


   private synchronized void saveSubUnit( final SubUnit subUnit )
   {
      this.repository.save( subUnit );
   }


   public List<ResourceLog> getLogForResource( final String resourceId )
   {
      List<ResourceLog> resourceLog = new ArrayList<>();
      final List<SubUnit> subUnits = this.repository.findAll();
      final Optional<Resource> resourceOptional =
            subUnits.stream().flatMap( subUnit -> subUnit.getResources().stream()
                  .filter( resource -> resource.getId().equals( resourceId ) ) ).findFirst();
      if ( resourceOptional.isPresent() )
      {
         resourceLog = resourceOptional.get().getResourceLogs();
      }

      return resourceLog;
   }


   public Optional<SubUnit> updateResourceStatus( final String resourceId, final ResourceStatus resourceStatus,
         final String ipAddress )
   {
      final Optional<SubUnit> subUnitOptional = getSubUnitWithResource( resourceId );
      if ( subUnitOptional.isPresent() )
      {
         final Optional<Resource> resourceOptional = getResourceWithId( subUnitOptional.get(), resourceId );
         if ( resourceOptional.isPresent() )
         {
            final Resource resource = resourceOptional.get();
            final ResourceStatus oldStatus = resource.getStatus();
            resource.setStatus( resourceStatus );
            if ( resource.getResourceLogs() == null )
            {
               resource.setResourceLogs( new ArrayList<>() );
            }
            updateLastUpdatedTimestamp( subUnitOptional.get(), resource.getType() );
            final ResourceLog resourceLog =
                  new ResourceLog( UUID.randomUUID(), Instant.now().toString(), ipAddress, null, resourceStatus );
            resource.getResourceLogs().add( resourceLog );
            saveSubUnit( subUnitOptional.get() );

            final String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
            this.historyWriter.addLogToFile( resourceIdentifier,
                  "Data&ora='" + Instant.now() + '\'' + ", IP='" + ipAddress + '\'' + ", "
                        + "Schimbare status resursă: " + oldStatus.getStatus() + " -> " + resourceStatus.getStatus() );
         }
      }

      return subUnitOptional;
   }


   public Optional<SubUnit> updateResourceType( final String resourceId, final ResourceType resourceType,
         final String ipAddress )
   {
      final Optional<SubUnit> subUnitOptional = getSubUnitWithResource( resourceId );
      if ( subUnitOptional.isPresent() )
      {
         final Optional<Resource> resourceOptional = getResourceWithId( subUnitOptional.get(), resourceId );
         if ( resourceOptional.isPresent() )
         {
            final Resource resource = resourceOptional.get();
            final ResourceType oldType = resource.getType();
            updateLastUpdatedTimestamp( subUnitOptional.get(), oldType );
            resource.setType( resourceType );
            updateLastUpdatedTimestamp( subUnitOptional.get(), resource.getType() );
            if ( resourceType.equals( ResourceType.RESERVE ) )
            {
               resource.setStatus( new ResourceStatus( ResourceStatus.Status.OPERATIONAL ) );
            }
            else
            {
               resource.setStatus( new ResourceStatus( ResourceStatus.Status.AVAILABLE ) );
            }
            if ( resource.getResourceLogs() == null )
            {
               resource.setResourceLogs( new ArrayList<>() );
            }
            final ResourceLog resourceLog =
                  new ResourceLog( UUID.randomUUID(), Instant.now().toString(), ipAddress, resourceType,
                        resource.getStatus() );
            resource.getResourceLogs().add( resourceLog );
            saveSubUnit( subUnitOptional.get() );

            final String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
            this.historyWriter.addLogToFile( resourceIdentifier, "Data&ora='" + Instant.now() + '\'' + ", IP='"
                  + ipAddress + '\'' + ", " + "Resursă mutată: " + oldType + " -> " + resourceType );
         }
      }
      return subUnitOptional;
   }


   public List<SubUnit> updateAllResourcesWithVehicleType( final String newVehicleShortName,
         final String oldVehicleShortName )
   {
      return this.repository.findAll().stream().filter( subUnit ->
      {
         final List<Resource> resourcesWithVehicleType = getResourcesWithVehicleType( subUnit, oldVehicleShortName );
         resourcesWithVehicleType.forEach( resource ->
         {
            resource.setVehicleType( newVehicleShortName );
         } );

         saveSubUnit( subUnit );

         return !resourcesWithVehicleType.isEmpty();
      } ).collect( Collectors.toList() );
   }


   private void updateLastUpdatedTimestamp( final SubUnit subunit, final ResourceType resourceType )
   {
      final String lastUpdate = Instant.now().toString();
      switch ( resourceType )
      {
         case FIRST_INTERVENTION:
            subunit.setLastUpdateFirstInterventionResource( lastUpdate );
            break;
         case EQUIPMENT:
            subunit.setLastUpdateEquipment( lastUpdate );
            break;
         case OTHER:
            subunit.setLastUpdateOtherResource( lastUpdate );
            break;
         case RESERVE:
            subunit.setLastUpdateReserveResource( lastUpdate );
            break;
         default:
            throw new RuntimeException( "resource type not supported" );
      }
   }


   private SubUnit updateEquipment( final SubUnit subUnit, SubUnit updatedUnit, final SubUnit existingSubUnit )
   {
      final List<Equipment> existingEquipment = getEquipment( existingSubUnit );
      final List<Equipment> updatedEquipment = getEquipment( subUnit );
      if ( checkIfEquipmentUpdated( existingEquipment, updatedEquipment ) )
      {
         final Query query = new Query().addCriteria( Criteria.where( "name" ).is( subUnit.getName() ) );
         final Update update =
               new Update().set( "lastUpdateEquipment", Instant.now().toString() ).set( "equipment",
                     subUnit.getEquipment() );
         updatedUnit =
               this.template.findAndModify( query, update, new FindAndModifyOptions().returnNew( true ),
                     SubUnit.class );
      }
      return updatedUnit;
   }


   private SubUnit updateOtherResources( final SubUnit subUnit, SubUnit updatedUnit, final SubUnit existingSubUnit )
   {
      final List<Resource> existingOtherResources = getOtherResources( existingSubUnit );
      final List<Resource> updatedOtherResources = getOtherResources( subUnit );

      if ( checkIfResourcesUpdated( existingOtherResources, updatedOtherResources ) )
      {
         final Query query = new Query().addCriteria( Criteria.where( "name" ).is( subUnit.getName() ) );
         final Update update =
               new Update().set( "lastUpdateOtherResource", Instant.now().toString() ).set( "resources",
                     subUnit.getResources() );
         updatedUnit =
               this.template.findAndModify( query, update, new FindAndModifyOptions().returnNew( true ),
                     SubUnit.class );
      }
      return updatedUnit;
   }


   private SubUnit updateFirstInterventionResources( final SubUnit subUnit, final SubUnit existingSubUnit )
   {
      SubUnit updatedUnit = null;
      final List<Resource> existingFirstInterventionResources = getFirstInterventionResources( existingSubUnit );
      final List<Resource> updatedFirstInterventionResources = getFirstInterventionResources( subUnit );

      if ( checkIfResourcesUpdated( existingFirstInterventionResources, updatedFirstInterventionResources ) )
      {
         final Query query = new Query().addCriteria( Criteria.where( "name" ).is( subUnit.getName() ) );
         final Update update =
               new Update().set( "lastUpdateFirstInterventionResource", Instant.now().toString() ).set( "resources",
                     subUnit.getResources() );
         updatedUnit =
               this.template.findAndModify( query, update, new FindAndModifyOptions().returnNew( true ),
                     SubUnit.class );
      }

      return updatedUnit;
   }


   private SubUnit updateReserves( final SubUnit subUnit, SubUnit updatedUnit, final SubUnit existingSubUnit )
   {
      final List<Resource> existingReserveResources = getReserveResources( existingSubUnit );
      final List<Resource> updatedReserveResources = getReserveResources( subUnit );

      if ( checkIfResourcesUpdated( existingReserveResources, updatedReserveResources ) )
      {
         final Query query = new Query().addCriteria( Criteria.where( "name" ).is( subUnit.getName() ) );
         final Update update =
               new Update().set( "lastUpdateReserveResource", Instant.now().toString() ).set( "resources",
                     subUnit.getResources() );
         updatedUnit =
               this.template.findAndModify( query, update, new FindAndModifyOptions().returnNew( true ),
                     SubUnit.class );
      }
      return updatedUnit;
   }


   private boolean checkIfEquipmentUpdated( final List<Equipment> existingEquipment,
         final List<Equipment> updatedEquipment )
   {
      boolean updated = true;
      if ( existingEquipment != null && updatedEquipment != null && existingEquipment.size() == updatedEquipment.size()
            && existingEquipment.containsAll( updatedEquipment ) )
      {
         updated = false;
      }
      else if ( updatedEquipment == null && existingEquipment == null )
      {
         updated = false;
      }

      return updated;
   }


   private boolean checkIfResourcesUpdated( final List<Resource> existingResources,
         final List<Resource> updatedResources )
   {
      boolean response = true;
      if ( existingResources != null && updatedResources != null && existingResources.size() == updatedResources.size()
            && existingResources.containsAll( updatedResources ) )
      {
         response = false;
      }
      else if ( updatedResources == null && existingResources == null )
      {
         response = false;
      }

      return response;
   }


   private List<Resource> getFirstInterventionResources( final SubUnit existingSubUnit )
   {
      return existingSubUnit.getResources().stream()
            .filter( resource -> resource.getType().equals( ResourceType.FIRST_INTERVENTION ) )
            .collect( Collectors.toList() );
   }


   private List<Resource> getReserveResources( final SubUnit existingSubUnit )
   {
      return existingSubUnit.getResources().stream()
            .filter( resource -> resource.getType().equals( ResourceType.RESERVE ) ).collect( Collectors.toList() );
   }


   private List<Resource> getOtherResources( final SubUnit existingSubUnit )
   {
      return existingSubUnit.getResources().stream()
            .filter( resource -> resource.getType().equals( ResourceType.OTHER ) ).collect( Collectors.toList() );
   }


   private List<Equipment> getEquipment( final SubUnit existingSubUnit )
   {
      return existingSubUnit.getEquipment();
   }


   private Optional<Resource> getResourceWithId( final SubUnit subUnit, final String id )
   {
      return subUnit.getResources().stream().filter( r -> r.getId().equals( id ) ).findFirst();
   }


   private List<Resource> getResourcesWithVehicleType( final SubUnit subUnit, final String vehicleType )
   {
      return subUnit.getResources().stream().filter( r -> r.getVehicleType().equals( vehicleType ) )
            .collect( Collectors.toList() );
   }


   private Optional<SubUnit> getSubUnitWithResource( final String resourceId )
   {
      final List<SubUnit> subUnits = this.repository.findAll();
      return subUnits.stream().filter( s -> s.getResources().stream().anyMatch( r -> r.getId().equals( resourceId ) ) )
            .findFirst();
   }


   private void writeLogEntry( final SubUnit initialSubUnit, final SubUnit updatedSubUnit, final String ipAddress )
   {
      final Map<String, Resource> initalResourcesMap =
            initialSubUnit.getResources().stream().collect( Collectors.toMap( Resource::getId, resource -> resource ) );
      final Map<String, Resource> updatedResourcesMap =
            updatedSubUnit.getResources().stream().collect( Collectors.toMap( Resource::getId, resource -> resource ) );

      filterAndLogDeletedResources( ipAddress, initalResourcesMap, updatedResourcesMap );

      filterAndLogAddedResources( ipAddress, initalResourcesMap, updatedResourcesMap );

      filterAndLogUpdatedResources( ipAddress, initalResourcesMap, updatedResourcesMap );
   }


   private void filterAndLogUpdatedResources( final String ipAddress, final Map<String, Resource> initalResourcesMap,
         final Map<String, Resource> updatedResourcesMap )
   {
      final List<String> updatedResources =
            initalResourcesMap.keySet().stream()
                  .filter( s -> updatedResourcesMap.keySet().contains( s )
                        && !updatedResourcesMap.get( s ).equals( initalResourcesMap.get( s ) ) )
                  .collect( Collectors.toList() );

      updatedResources.forEach( resourceId ->
      {
         final Resource resource = initalResourcesMap.get( resourceId );
         final String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
         this.historyWriter.addLogToFile( resourceIdentifier,
               "Data&ora='" + Instant.now() + '\'' + ", IP='" + ipAddress + '\'' + ", " + "Resursa editata!" );
      } );
   }


   private void filterAndLogAddedResources( final String ipAddress, final Map<String, Resource> initalResourcesMap,
         final Map<String, Resource> updatedResourcesMap )
   {
      final List<String> addedResources =
            updatedResourcesMap.keySet().stream().filter( s -> !initalResourcesMap.keySet().contains( s ) )
                  .collect( Collectors.toList() );

      addedResources.forEach( resourceId ->
      {
         final Resource resource = updatedResourcesMap.get( resourceId );
         final String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
         this.historyWriter.addLogToFile( resourceIdentifier,
               "Data&ora='" + Instant.now() + '\'' + ", IP='" + ipAddress + '\'' + ", " + "Resursa adaugata!" );
      } );
   }


   private void filterAndLogDeletedResources( final String ipAddress, final Map<String, Resource> initalResourcesMap,
         final Map<String, Resource> updatedResourcesMap )
   {
      final List<String> deletedResources =
            initalResourcesMap.keySet().stream().filter( s -> !updatedResourcesMap.keySet().contains( s ) )
                  .collect( Collectors.toList() );

      deletedResources.forEach( resourceId ->
      {
         final Resource resource = initalResourcesMap.get( resourceId );
         final String resourceIdentifier = resource.getPlateNumber() + " - " + resource.getIdentificationNumber();
         this.historyWriter.addLogToFile( resourceIdentifier,
               "Data&ora='" + Instant.now() + '\'' + ", IP='" + ipAddress + '\'' + ", " + "Resursa stearsa!" );
      } );
   }


   private static Comparator<SubUnit> subUnitsOrdering( final List<String> subUnitIds )
   {
      return Comparator.comparingInt( v -> subUnitIds.indexOf( v.getId() ) );
   }


   private SubUnit createSubUnit( final String id, final String subUnitName )
   {
      final String lastUpdate = Instant.now().toString();
      final SubUnit subUnit = new SubUnit();
      subUnit.setId( id );
      subUnit.setName( subUnitName );
      subUnit.setLastUpdateFirstInterventionResource( lastUpdate );
      subUnit.setLastUpdateEquipment( lastUpdate );
      subUnit.setLastUpdateOtherResource( lastUpdate );
      subUnit.setLastUpdateReserveResource( lastUpdate );
      subUnit.setResources( new ArrayList<>() );
      subUnit.setEquipment( new ArrayList<>() );
      subUnit.setLockedResourceTypeBySessionId( new HashMap<>() );
      return subUnit;
   }
}
