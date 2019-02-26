package com.resource.management.resource.model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubUnitsRepository extends MongoRepository<SubUnit, String> {
}
