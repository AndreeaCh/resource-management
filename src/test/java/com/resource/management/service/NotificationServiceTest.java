package com.resource.management.service;

import com.resource.management.api.SubUnitDeletedNotification;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotificationServiceTest {
    @MockBean
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationService sut;

    @Test
    public void publishSubUnitNotification_sut_callsOnMessagingTemplate() {
        //given
        SubUnitDeletedNotification notification = new SubUnitDeletedNotification("CJ");

        //when
        sut.publishSubUnitNotification(notification);

        //then
        verify(messagingTemplate).convertAndSend("/topic/subunit", notification);
    }
}