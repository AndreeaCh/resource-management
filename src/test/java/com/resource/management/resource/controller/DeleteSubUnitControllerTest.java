package com.resource.management.resource.controller;

import static com.resource.management.api.resources.StatusCode.OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.junit4.SpringRunner;

import com.resource.management.api.management.subunits.DeleteSubUnitRequest;
import com.resource.management.api.management.subunits.DeleteSubUnitResponse;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteSubUnitControllerTest {
    private static final String IP_ADDRESS = "12.2.2.2";

    @MockBean
    private SubUnitsService subUnitsService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private DeleteSubUnitController sut;

    @MockBean
    private SimpMessageHeaderAccessor headerAccessor;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("ip", IP_ADDRESS);
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);

    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(this.sut, notNullValue());
    }

    @Test
    public void handleAddSubUnitRequest_sut_respondsToRequestWithOK() {
        //given
        DeleteSubUnitRequest request = new DeleteSubUnitRequest("CJ");

        //when
        DeleteSubUnitResponse response = this.sut.handle(request, headerAccessor);

        //then
        assertThat("Expected notification to contain the deleted sub-unit deletedSubUnitName.", response.getStatusCode(),
                equalTo(OK));
    }
}
