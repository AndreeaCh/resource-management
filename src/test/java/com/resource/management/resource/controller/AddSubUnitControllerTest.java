package com.resource.management.resource.controller;

import com.resource.management.SubUnits;
import com.resource.management.api.management.subunits.AddSubUnitRequest;
import com.resource.management.api.management.subunits.AddSubUnitResponse;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.resource.management.api.resources.StatusCode.OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddSubUnitControllerTest {
    @MockBean
    private SubUnitsService subUnitsService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private AddSubUnitController sut;

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.sut, notNullValue());
    }

    @Test
    public void handleAddSubUnitRequest_sut_respondsWithSuccess() {
        //given
        AddSubUnitRequest request = new AddSubUnitRequest("id", "name");

        //when
        AddSubUnitResponse response = this.sut.handle(request);

        //then
        assertThat(
                "Expected status code to be OK.",
                response.getStatusCode(),
                equalTo(OK));
    }
}
