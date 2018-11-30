package com.resource.management.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.api.status.GetResourceLogRequest;
import com.resource.management.api.status.GetResourceLogResponse;
import com.resource.management.model.Resource;
import com.resource.management.model.ResourceLog;
import com.resource.management.model.ResourceLogMapper;
import com.resource.management.model.status.AvailableStatus;
import com.resource.management.service.SubUnitsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetResourceLogControllerTest
{
   private static final String PLATE_NUMBER = "1234";

   @MockBean
   private SubUnitsService subUnitsService;

   @Autowired
   private GetResourceLogController controller;


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
      final GetResourceLogResponse getRes = this.controller.handle( new GetResourceLogRequest( PLATE_NUMBER ) );

      //then
      List<com.resource.management.api.ResourceLog> expected =
            resourceLogsList.stream().map( ResourceLogMapper::toApi ).collect( Collectors.toList() );
      assertThat( "Expected response to contain the list of sub-units.", getRes.getResourceLogs(),
            equalTo( expected ) );
   }


   private List<ResourceLog> prepareResourceLogsInRepository()
   {
      ResourceLog resourceLog =
            new ResourceLog( UUID.randomUUID(), Instant.now().toString(), "10.12.12.12", new AvailableStatus() );
      List<ResourceLog> resourceLogs = Collections.singletonList( resourceLog );
      Resource resource = new Resource();
      resource.setIdentificationNumber( "1" );
      resource.setPlateNumber( PLATE_NUMBER );
      resource.setResourceLogs( resourceLogs );
      when( this.subUnitsService.getLogForResource( PLATE_NUMBER ) ).thenReturn( resourceLogs );
      return resourceLogs;
   }
}
