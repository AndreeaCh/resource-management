/*
 * COPYRIGHT: FREQUENTIS AG. All rights reserved.
 *            Registered with Commercial Court Vienna,
 *            reg.no. FN 72.115b.
 */
package com.resource.management.controller;

import com.resource.management.SubUnits;
import com.resource.management.api.lock.SubUnitUnlockedNotification;
import com.resource.management.model.SubUnit;
import com.resource.management.model.SubUnitsRepository;
import com.resource.management.service.NotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionDisconnectedControllerTest {
    @MockBean
    private SubUnitsRepository repository;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private SessionDisconnectedController sut;

    @Test
    public void handleAddSubUnitRequest_sut_publishesDeletedSubUnit() {
        //given
        SubUnit subUnit = SubUnits.internal();
        String sessionId = "SessionId";
        when(repository.findByLockedBy(sessionId)).thenReturn(Optional.of(subUnit));
        SessionDisconnectEvent event = new SessionDisconnectEvent(mock(Object.class), mock(Message.class), sessionId, CloseStatus.GOING_AWAY);

        //when
        this.sut.onDisconnectEvent(event);

        //then
        ArgumentCaptor<SubUnitUnlockedNotification> captor = ArgumentCaptor.forClass(SubUnitUnlockedNotification.class);
        verify(notificationService).publishUnlockedSubUnitNotification(captor.capture());
        assertThat(
                "Expected that the notification contains the deleted subunit name",
                captor.getValue().getSubUnitName(),
                equalTo(subUnit.getName()));
    }
}