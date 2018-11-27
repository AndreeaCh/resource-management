package com.resource.management.service;

import com.resource.management.SubUnits;
import com.resource.management.api.SubUnit;
import com.resource.management.api.crud.notifications.SubUnitUpdatedNotification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotificationServiceTest {
    @MockBean
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationService sut;

    @Test
    public void publishoNotification_sut_callsOnMessagingTemplate() {
        //given
        SubUnit subUnit = SubUnits.api();

        //when
        sut.publishSubUnitNotification(subUnit);

        //then
        verify(messagingTemplate).convertAndSend(eq("/topic/unitUpdatedNotification"), isA(SubUnitUpdatedNotification.class));
    }
}