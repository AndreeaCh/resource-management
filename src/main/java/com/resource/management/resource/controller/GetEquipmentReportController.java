package com.resource.management.resource.controller;

import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitsRepository;
import com.resource.management.resource.service.EquipmentPdfCreator;
import com.resource.management.resource.service.EquipmentXlsCreator;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.resource.management.resource.service.EquipmentXlsCreator.EQUIPMENT_REPORT_FILE_NAME;

@Controller
public class GetEquipmentReportController {
    private static final Logger LOG = LoggerFactory.getLogger(GetEquipmentReportController.class);
    @Autowired
    private SubUnitsRepository repository;

    @Autowired
    private EquipmentPdfCreator pdfCreator;

    @Autowired
    private EquipmentXlsCreator xlsCreator;

    @MessageMapping("/getEquipmentReport")
    @SendTo("/topic/equipmentReport")
    public String getFile() {
        List<SubUnit> subUnits = repository.findAll();
        pdfCreator.createPdf(subUnits);
        xlsCreator.createXls(subUnits);
        return getXLSFileContentAsBase64();
    }

    private String getXLSFileContentAsBase64() {
        try {
            byte[] input_file = Files.readAllBytes(Paths.get(EQUIPMENT_REPORT_FILE_NAME));
            byte[] encodedBytes = Base64.encodeBase64(input_file);
            return new String(encodedBytes);
        } catch (IOException ex) {
            LOG.info("Error writing file to output stream. Filename was '{}'", "iTextHelloWorld.pdf", ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}
