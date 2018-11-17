package com.resource.management.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceLogRepository extends MongoRepository<ResourceLog, String>
{
   List<ResourceLog> findByPlateNumber( String plateNumber );
}
