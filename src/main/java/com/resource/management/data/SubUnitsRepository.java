package com.resource.management.data;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubUnitsRepository extends MongoRepository<SubUnit, String> {

    List<SubUnit> findByName(String name);
}
