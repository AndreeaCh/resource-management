package com.resource.management.services.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepository extends MongoRepository<Service, String>
{
   void deleteByDay( String day );


   void deleteBySubUnit( String subUnit );


   Iterable<Service> findBySubUnit( String subUnit );


   Iterable<Service> findByDay( String day );
}
