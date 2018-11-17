package com.resource.management.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.api.SubscribeResourceLogResponse;
import com.resource.management.data.ResourceLog;
import com.resource.management.data.ResourceLogRepository;
import com.resource.management.data.ResourceStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceLogsControllerTest
{
   private static final String PLATE_NUMBER = "1234";

   @MockBean
   private ResourceLogRepository resourceLogsRepository;

   @Autowired
   private ResourceLogController controller;


   @Test
   public void contextLoads() throws Exception
   {
      assertThat( this.controller, notNullValue() );
   }


   @Test
   public void handleSubscribeRequest_controller_returnsResourceLogsList()
   {
      //given
      final List<ResourceLog> resourceLogsList = prepareResourceLogsInRepository();

      //when
      final SubscribeResourceLogResponse subscribeResourceLogsResponse =
            this.controller.handleResourceLogMessage( PLATE_NUMBER );

      //then
      assertThat( "Expected response to contain the list of sub-units.",
            subscribeResourceLogsResponse.getResourceLogs(), equalTo( resourceLogsList ) );
   }


   private List<ResourceLog> prepareResourceLogsInRepository()
   {
      final List<ResourceLog> resourceLogsList = new ArrayList<>();
      resourceLogsList.add( new ResourceLog( "id1", PLATE_NUMBER, ResourceStatus.AVAILABLE_IN_GARAGE, Instant.now() ) );
      when( this.resourceLogsRepository.findByPlateNumber( PLATE_NUMBER ) ).thenReturn( resourceLogsList );
      return resourceLogsList;
   }
}
