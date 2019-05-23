package com.resource.management.resource.controller;

import static com.resource.management.management.vehicles.model.VehicleTypes.ID;
import static com.resource.management.resource.service.EquipmentXlsCreator.EQUIPMENT_REPORT_FILE_NAME;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.resource.management.management.vehicles.model.VehicleRepository;
import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.management.vehicles.model.VehicleTypes;
import com.resource.management.resource.model.SubUnit;
import com.resource.management.resource.model.SubUnitsRepository;
import com.resource.management.resource.service.EquipmentPdfCreator;
import com.resource.management.resource.service.EquipmentXlsCreator;

@RestController
@RequestMapping(path = "/equipmentsReport")
public class GetEquipmentReportRestController
{
    private static final Logger LOG = LoggerFactory.getLogger(GetEquipmentReportRestController.class);
    @Autowired
    private SubUnitsRepository subUnitsRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private EquipmentPdfCreator pdfCreator;

    @Autowired
    private EquipmentXlsCreator xlsCreator;

    @RequestMapping(method = RequestMethod.GET)
    public String getFile() {
        List<SubUnit> subUnits = subUnitsRepository.findAll();
        Optional<VehicleTypes> vehiclesOptional = vehicleRepository.findById(ID);
        List<VehicleType> vehicleTypeList = vehiclesOptional.map(vehicleTypes -> new ArrayList<>(vehicleTypes.getVehicleTypes())).orElseGet(ArrayList::new);
        xlsCreator.createXls(subUnits, vehicleTypeList);
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
