package com.resource.management;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.data.Detachment;
import com.resource.management.data.DetachmentsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoDbSpringIntegrationTest
{
   @Autowired
   private DetachmentsRepository repository;


   @Test
   public void test()
   {
      final Detachment detachment = new Detachment( "CJ" );
      this.repository.save( detachment );

      Assert.assertEquals( "CJ", this.repository.findAll().get( 0 ).getName() );
   }
}
