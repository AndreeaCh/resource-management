package com.resource.management.services.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceRepository extends MongoRepository<Service, String>
{
   void deleteByDay( String day );
}
