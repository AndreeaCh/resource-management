package com.resource.management.controller;

import com.resource.management.SubUnitsTestDataUtils;
import com.resource.management.api.AddSubUnitRequest;
import com.resource.management.api.AddSubUnitResponse;
import com.resource.management.api.SubUnitUpdatedNotification;
import com.resource.management.data.SubUnitsRepository;
import com.resource.management.service.NotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.resource.management.api.StatusCode.OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddSubUnitControllerTest {
    @MockBean
    private SubUnitsRepository subUnitsRepository;

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
        AddSubUnitRequest request = new AddSubUnitRequest(SubUnitsTestDataUtils.loadRandomSubUnit());

        //when
        AddSubUnitResponse response = this.sut.handle(request);

        //then
        assertThat(
                "Expected status code to be OK.",
                response.getStatusCode(),
                equalTo(OK));
    }

    @Test
    public void handleAddSubUnitRequest_sut_publishesNewSubUnit() {
        //given
        AddSubUnitRequest request = new AddSubUnitRequest(SubUnitsTestDataUtils.loadRandomSubUnit());

        //when
        this.sut.handle(request);

        //then
        ArgumentCaptor<SubUnitUpdatedNotification> captor = ArgumentCaptor.forClass(SubUnitUpdatedNotification.class);
        verify(notificationService).publishSubUnitNotification(captor.capture());
        assertThat(
                "Expected that the notification contains the deleted subunit name",
                captor.getValue().getSubUnit(),
                equalTo(request.getSubUnit()));
    }
}
