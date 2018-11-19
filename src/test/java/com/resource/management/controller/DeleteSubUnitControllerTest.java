package com.resource.management.controller;

import com.resource.management.api.DeleteSubUnitRequest;
import com.resource.management.api.DeleteSubUnitResponse;
import com.resource.management.api.SubUnitDeletedNotification;
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
public class DeleteSubUnitControllerTest {
    @MockBean
    private SubUnitsRepository subUnitsRepository;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private DeleteSubUnitController sut;

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.sut, notNullValue());
    }

    @Test
    public void handleAddSubUnitRequest_sut_publishesDeletedSubUnit() {
        //given
        DeleteSubUnitRequest request = new DeleteSubUnitRequest("CJ");

        //when
        this.sut.handle(request);

        //then
        ArgumentCaptor<SubUnitDeletedNotification> captor = ArgumentCaptor.forClass(SubUnitDeletedNotification.class);
        verify(notificationService).sendSubUnitDeletedNotification(captor.capture());
        assertThat("Expected that the notification contains the deleted subunit name",
                captor.getValue().getDeletedSubUnitName(),
                equalTo(request.getName()));
    }

    @Test
    public void handleAddSubUnitRequest_sut_respondsToRequestWithOK() {
        //given
        DeleteSubUnitRequest request = new DeleteSubUnitRequest("CJ");

        //when
        DeleteSubUnitResponse response = this.sut.handle(request);

        //then
        assertThat("Expected notification to contain the deleted sub-unit deletedSubUnitName.", response.getStatusCode(),
                equalTo(OK));
    }
}