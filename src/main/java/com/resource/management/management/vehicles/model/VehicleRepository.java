package com.resource.management.management.vehicles.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VehicleRepository extends MongoRepository<VehicleTypes, String> {
}
