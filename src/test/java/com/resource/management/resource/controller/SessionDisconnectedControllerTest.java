package com.resource.management.resource.controller;

import com.resource.management.SubUnits;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionDisconnectedControllerTest {
    @MockBean
    private SubUnitsService subUnitsService;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private SessionDisconnectedController sut;

    @Test
    public void handleAddSubUnitRequest_sut_unlocksSubUnit() {
        //given
        SubUnit subUnit = SubUnits.internal();
        String sessionId = "SessionId";
        when(subUnitsService.unlockSubUnitsLockedBySession(sessionId)).thenReturn(Collections.singletonMap(subUnit, ResourceType.FIRST_INTERVENTION));
        SessionDisconnectEvent event = new SessionDisconnectEvent(mock(Object.class), mock(Message.class), sessionId, CloseStatus.GOING_AWAY);

        //when
        this.sut.onDisconnectEvent(event);

        //then
        verify(subUnitsService).unlockSubUnitsLockedBySession(sessionId);
    }

    @Test
    public void handleAddSubUnitRequest_sut_publishesUnlockedSubUnit() {
        //given
        SubUnit subUnit = SubUnits.internal();
        String sessionId = "SessionId";
        when(subUnitsService.unlockSubUnitsLockedBySession(sessionId)).thenReturn(Collections.singletonMap(subUnit, ResourceType.OTHER));
        SessionDisconnectEvent event = new SessionDisconnectEvent(mock(Object.class), mock(Message.class), sessionId, CloseStatus.GOING_AWAY);

        //when
        this.sut.onDisconnectEvent(event);

        //then
        verify(notificationService).publishUnlockedSubUnitNotification(subUnit.getId(), Collections.singleton(ResourceType.OTHER));
    }
}