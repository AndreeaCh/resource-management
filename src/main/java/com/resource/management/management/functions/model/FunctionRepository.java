package com.resource.management.management.functions.model;

import com.resource.management.services.model.Service;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FunctionRepository extends MongoRepository<Function, String> {
}
