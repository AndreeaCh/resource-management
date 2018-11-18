package com.resource.management;

import java.util.List;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.data.SubUnit;
import com.resource.management.data.SubUnitsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoDbSpringIntegrationTest
{
   @Autowired
   private SubUnitsRepository repository;


   @Test
   public void test() throws JSONException
   {
      List<SubUnit> subUnits = SubUnitsTestDataUtils.loadAllSubUnits();
      this.repository.saveAll( subUnits );

      Assert.assertEquals( "CJ", this.repository.findAll().get( 0 ).getName() );
   }
}
