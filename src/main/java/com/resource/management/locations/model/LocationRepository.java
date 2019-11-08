package com.resource.management.locations.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository extends MongoRepository<Location, String>
{

}
