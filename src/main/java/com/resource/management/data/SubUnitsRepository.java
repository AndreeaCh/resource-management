package com.resource.management.data;


import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubUnitsRepository extends MongoRepository<SubUnit, String> {

    Optional<SubUnit> findByName(String name);
}
