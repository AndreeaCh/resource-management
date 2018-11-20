package com.resource.management.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.*;

import com.resource.management.api.status.GetResourceLogRequest;
import com.resource.management.data.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.api.status.GetResourceLogResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GetResourceLogControllerTest {
    private static final String PLATE_NUMBER = "1234";

    @MockBean
    private SubUnitsRepository subUnitsRepository;

    @Autowired
    private GetResourceLogController controller;


    @Test
    public void contextLoads() throws Exception {
        assertThat(this.controller, notNullValue());
    }


    @Test
    public void handleSubscribeRequest_controller_returnsResourceLogsList() {
        //given
        final List<ResourceLog> resourceLogsList = prepareResourceLogsInRepository();

        //when
        final GetResourceLogResponse subscribeResourceLogsResponse =
                this.controller.handle(new GetResourceLogRequest(PLATE_NUMBER));

        //then
        assertThat("Expected response to contain the list of sub-units.",
                subscribeResourceLogsResponse.getResourceLogs(), equalTo(resourceLogsList));
    }


    private List<ResourceLog> prepareResourceLogsInRepository() {
        ResourceLog resourceLog = new ResourceLog(UUID.randomUUID(), Instant.now().toString(), "10.12.12.12", ResourceStatus.AVAILABLE_ON_ROUTE);
        Resource resource = new Resource();
        resource.setIdentificationNumber(1);
        resource.setPlateNumber(PLATE_NUMBER);
        resource.setResourceLogs(Collections.singletonList(resourceLog));
        List<Resource> resources = Collections.singletonList(resource);
        SubUnit subUnit = new SubUnit("name", resources, "2081-10-10", "102.2.2.2", false);
        final List<ResourceLog> resourceLogsList = new ArrayList<>();
        resourceLogsList.add(resourceLog);
        when(this.subUnitsRepository.findAll()).thenReturn(Collections.singletonList(subUnit));
        return resourceLogsList;
    }
}
