/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package com.resource.management.resource.controller;

import com.resource.management.SubUnits;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.service.NotificationService;
import com.resource.management.resource.service.SubUnitsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

import static org.mockito.Mockito.*;

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
        when(subUnitsService.unlockSubUnitLockedBySession(sessionId)).thenReturn(Optional.of(subUnit));
        SessionDisconnectEvent event = new SessionDisconnectEvent(mock(Object.class), mock(Message.class), sessionId, CloseStatus.GOING_AWAY);

        //when
        this.sut.onDisconnectEvent(event);

        //then
        verify(subUnitsService).unlockSubUnitLockedBySession(sessionId);
    }

    @Test
    public void handleAddSubUnitRequest_sut_publishesUnlockedSubUnit() {
        //given
        SubUnit subUnit = SubUnits.internal();
        String sessionId = "SessionId";
        when(subUnitsService.unlockSubUnitLockedBySession(sessionId)).thenReturn(Optional.of(subUnit));
        SessionDisconnectEvent event = new SessionDisconnectEvent(mock(Object.class), mock(Message.class), sessionId, CloseStatus.GOING_AWAY);

        //when
        this.sut.onDisconnectEvent(event);

        //then
        verify(notificationService).publishUnlockedSubUnitNotification(subUnit.getName());
    }
}