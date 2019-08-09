package com.resource.management.services.model;

public class ServiceMapper
{

   public static final Service toInternal( final com.resource.management.api.resources.Service externalService,
         final String lastUpdate )
   {
      final Service internalService = new Service();
      internalService.setId( externalService.getId() );
      internalService.setName( externalService.getName() );
      internalService.setTitle( externalService.getTitle() );
      internalService.setRole( externalService.getRole() );
      internalService.setSubUnit( externalService.getSubUnit() );
      internalService.setContact( externalService.getContact() );
      internalService.setLastUpdate( lastUpdate );
      internalService.setDay( externalService.getDay() );
      return internalService;
   }


   public static final com.resource.management.api.resources.Service toApi( final Service internalService )
   {
      final com.resource.management.api.resources.Service externalService =
            new com.resource.management.api.resources.Service();
      externalService.setId( internalService.getId() );
      externalService.setTitle( internalService.getTitle() );
      externalService.setRole( internalService.getRole() );
      externalService.setSubUnit( internalService.getSubUnit() );
      externalService.setContact( internalService.getContact() );
      externalService.setDay( internalService.getDay() );
      return externalService;
   }
}
