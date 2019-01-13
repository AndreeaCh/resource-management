package com.resource.management.resource.service;

import com.lowagie.text.DocumentException;
import com.resource.management.SubUnits;
import com.resource.management.resource.model.SubUnit;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class EquipmentPdfCreatorTest {
    @Test
    public void test() throws IOException, DocumentException {
        SubUnit subUnit1 = SubUnits.internal();
        SubUnit subUnit2 = SubUnits.otherInternal();

        EquipmentPdfCreator equipmentPdfCreator = new EquipmentPdfCreator();
        equipmentPdfCreator.createPdf(Arrays.asList(subUnit1, subUnit2));
    }

}