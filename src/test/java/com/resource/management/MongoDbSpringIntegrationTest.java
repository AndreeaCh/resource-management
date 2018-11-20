package com.resource.management;

import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;
import java.time.Instant;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoDbSpringIntegrationTest {
    @Autowired
    private SubUnitsRepository repository;


    @Test
    public void test() {
        final SubUnit subUnit = new SubUnit("CJ", Collections.emptyList(), Instant.now().toString(), null, true);
        this.repository.save(subUnit);

        Assert.assertEquals("CJ", this.repository.findAll().get(0).getName());
    }
}
