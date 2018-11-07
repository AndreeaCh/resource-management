package com.resource.management.data;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DetachmentsRepository extends MongoRepository<Detachment, String> {

    List<Detachment> findByName(String name);
}
