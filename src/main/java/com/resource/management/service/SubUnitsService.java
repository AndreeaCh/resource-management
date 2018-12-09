package com.resource.management.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.resource.management.model.Resource;
import com.resource.management.model.ResourceLog;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitsRepository;
import com.resource.management.model.ResourceStatus;

@Service
public class SubUnitsService
{
   @Autowired
   private SubUnitsRepository repository;

   @Autowired
   private MongoTemplate template;


   public Optional<SubUnit> findSubUnitByName( final String name )
   {
      return repository.findByName( name );
   }


   public void addSubUnit( final SubUnit subUnit )
   {
      subUnit.setLastUpdate( Instant.now().toString() );
      saveSubUnit( subUnit );
   }


   public Optional<SubUnit> updateSubUnit( final SubUnit subUnit )
   {
      Query query =
            new Query().addCriteria( Criteria.where( "name" ).is( subUnit.getName() ) )
                  .addCriteria( Criteria.where( "resources" ).ne( subUnit.getResources() ) );
      Update update =
            new Update().set( "lastUpdate", Instant.now().toString() ).set( "resources", subUnit.getResources() );
      SubUnit unit =
            template.findAndModify( query, update, new FindAndModifyOptions().returnNew( true ), SubUnit.class );
      return Optional.ofNullable( unit );
   }


   public synchronized boolean lockSubUnit( final String subUnitName, final String sessionId )
   {
      boolean isLocked = false;
      Optional<SubUnit> subUnitOptional = findSubUnitByName( subUnitName );
      if ( subUnitOptional.isPresent() )
      {
         SubUnit subUnit = subUnitOptional.get();
         subUnit.setIsLocked( true );
         subUnit.setLockedBy( sessionId );
         saveSubUnit( subUnit );
         isLocked = true;
      }

      return isLocked;
   }


   public Optional<SubUnit> unlockSubUnit( final String subUnitName )
   {
      Query query = new Query().addCriteria( Criteria.where( "name" ).is( subUnitName ) );
      Update update = new Update().set( "isLocked", false ).set( "lockedBy", null );
      SubUnit unit =
            template.findAndModify( query, update, new FindAndModifyOptions().returnNew( true ), SubUnit.class );
      return Optional.ofNullable( unit );
   }


   public Optional<SubUnit> unlockSubUnitLockedBySession( final String sessionId )
   {
      Optional<SubUnit> subUnit = repository.findByLockedBy( sessionId );
      subUnit.ifPresent( s ->
      {
         s.setIsLocked( false );
         s.setLockedBy( null );
         saveSubUnit( s );
      } );

      return subUnit;
   }


   public void unlockAllSubUnits()
   {
      repository.findAll().forEach( s ->
      {
         s.setIsLocked( false );
         s.setLockedBy( null );
         saveSubUnit( s );
      } );
   }


   public void deleteSubUnit( final String name )
   {
      repository.deleteById( name );
   }


   private synchronized void saveSubUnit( final SubUnit subUnit )
   {
      repository.save( subUnit );
   }


   public List<ResourceLog> getLogForResource( final String plateNumber )
   {
      List<ResourceLog> resourceLog = new ArrayList<>();
      List<SubUnit> subUnits = repository.findAll();
      final Optional<Resource> resourceOptional =
            subUnits.stream().flatMap( subUnit -> subUnit.getResources().stream()
                  .filter( resource -> resource.getPlateNumber().equals( plateNumber ) ) ).findFirst();
      if ( resourceOptional.isPresent() )
      {
         resourceLog = resourceOptional.get().getResourceLogs();
      }

      return resourceLog;
   }


   public Optional<SubUnit> updateResourceStatus( String plateNumber, ResourceStatus resourceStatus, String ipAddress )
   {
      final Optional<SubUnit> subUnitOptional = getSubUnitWithPlateNumber( plateNumber );
      if ( subUnitOptional.isPresent() )
      {
         final Optional<Resource> resourceOptional = getResourceWithPlateNumber( subUnitOptional.get(), plateNumber );
         if ( resourceOptional.isPresent() )
         {
            Resource resource = resourceOptional.get();
            resource.setStatus( resourceStatus );
            if ( resource.getResourceLogs() == null )
            {
               resource.setResourceLogs( new ArrayList<>() );
            }

            resource.getResourceLogs()
                  .add( new ResourceLog( UUID.randomUUID(), Instant.now().toString(), ipAddress, resourceStatus ) );
            saveSubUnit( subUnitOptional.get() );
         }
      }

      return subUnitOptional;
   }


   private Optional<Resource> getResourceWithPlateNumber( final SubUnit subUnit, final String plateNumber )
   {
      return subUnit.getResources().stream().filter( r -> r.getPlateNumber().equals( plateNumber ) ).findFirst();

   }


   private Optional<SubUnit> getSubUnitWithPlateNumber( String plateNumber )
   {
      List<SubUnit> subUnits = repository.findAll();
      return subUnits.stream()
            .filter( s -> s.getResources().stream().anyMatch( r -> r.getPlateNumber().equals( plateNumber ) ) )
            .findFirst();
   }
}
