package com.resource.management.model;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubUnitsRepository extends MongoRepository<SubUnit, String> {

    Optional<SubUnit> findByName(String name);

    Optional<SubUnit> findByLockedBy(String sessionId);
}
