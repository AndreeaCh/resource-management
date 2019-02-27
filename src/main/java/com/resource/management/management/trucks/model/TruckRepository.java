package com.resource.management.management.trucks.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TruckRepository extends MongoRepository<Trucks, String> {
}
