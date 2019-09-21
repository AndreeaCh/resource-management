/************************************************************************
 ** PROJECT:   XVP
 ** LANGUAGE:  Java, J2SE JDK 1.8
 **
 ** COPYRIGHT: FREQUENTIS AG
 **            Innovationsstrasse 1
 **            A-1100 VIENNA
 **            AUSTRIA
 **            tel +43 1 811 50-0
 **
 ** The copyright to the computer program(s) herein
 ** is the property of Frequentis AG, Austria.
 ** The program(s) shall not be used and/or copied without
 ** the written permission of Frequentis AG.
 **
 ************************************************************************/
package com.resource.management.resource.controller;

import com.resource.management.api.resources.lock.LockSubUnitRequest;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitsRepository;
import com.resource.management.resource.service.MissionsXlsCreator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import static com.resource.management.resource.service.MissionsXlsCreator.MISSIONS_REPORT_FILE_NAME;

@Controller
public class GetMissionsReportController {
    private static final Logger LOG = LoggerFactory.getLogger(GetMissionsReportController.class);
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private MissionsXlsCreator xlsCreator;

    @MessageMapping("/getMissionsReport")
    public void getMissionsReportRequest(
            @Payload final LockSubUnitRequest request,
            final SimpMessageHeaderAccessor headerAccessor) {

        List<SubUnit> subUnits = repository.findAll();
        xlsCreator.createXls(subUnits);
        String xlsFileContentAsBase64 = getXLSFileContentAsBase64();
        messagingTemplate.convertAndSendToUser(
                headerAccessor.getSessionId(), "/queue/missionsReport", xlsFileContentAsBase64, headerAccessor.getMessageHeaders());
    }

    private String getXLSFileContentAsBase64() {
        try {
            byte[] input_file = Files.readAllBytes(Paths.get(MISSIONS_REPORT_FILE_NAME));
            byte[] encodedBytes = Base64.encodeBase64(input_file);
            return new String(encodedBytes);
        } catch (IOException ex) {
            LOG.info("Error writing file to output stream. Filename was '{}'", MISSIONS_REPORT_FILE_NAME, ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}
