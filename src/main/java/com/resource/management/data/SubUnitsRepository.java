package com.resource.management.data;


import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubUnitsRepository extends MongoRepository<SubUnit, String> {

    Optional<SubUnit> findByName(String name);

    Optional<SubUnit> findByLockedBy(String sessionId);
}
