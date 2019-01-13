package com.resource.management.resource.service;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.resource.management.resource.model.Equipment;
import com.resource.management.resource.model.Resource;
import com.resource.management.resource.model.ResourceType;
import com.resource.management.resource.model.SubUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentPdfCreator {
    private static final Logger LOG = LoggerFactory.getLogger(EquipmentPdfCreator.class);
    public static final String EQUIPMENT_REPORT_FILE_NAME = "RaportEchipamente.pdf";
    private static final String ARIAL = "fonts/arial.ttf";
    private static final String ARIAL_BOLD = "fonts/arialbd.ttf";
    private BaseFont bf = BaseFont.createFont(ARIAL, BaseFont.IDENTITY_H, true);
    private BaseFont bfb = BaseFont.createFont(ARIAL_BOLD, BaseFont.IDENTITY_H, true);
    private Font cellFont = new Font(bf, 10);
    private Font cellFontBold = new Font(bfb, 10);
    private Font cellFontTitle = new Font(bfb, 14);

    @Autowired
    private ReportBuilder reportBuilder;

    public EquipmentPdfCreator() throws IOException, DocumentException {
    }

    public void createPdf(final List<SubUnit> subUnits) {
        try {
            AllSubUnitsReport report = reportBuilder.buildReport(subUnits);

            recreateReportFile();
            Document document = new Document(PageSize.A3.rotate(), 0, 0, 40, 20);
            PdfWriter.getInstance(document, new FileOutputStream(EQUIPMENT_REPORT_FILE_NAME));
            document.open();
            addTitle(document);
            PdfPTable table = new PdfPTable(buildNumberOfColumns(subUnits));
            table.setWidthPercentage(90);
            table.setSpacingBefore(0);
            table.setSpacingAfter(0);
            addTableHeader(table, report);
            addDetailsToTable(table, report);
            document.add(table);
            document.close();
        } catch (IOException | DocumentException e) {
            LOG.error("Could not create PDF. ", e);
        }
    }

    private void addTitle(Document document) throws DocumentException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
        Paragraph paragraph = new Paragraph("TEHNICA DE INTERVENȚIE OPERAȚIONALĂ ȘI NEOPERAȚIONALĂ A I.S.U. CLUJ ÎN DATA DE " + dtf.format(now), cellFontTitle);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
    }

    private void addDetailsToTable(PdfPTable table, AllSubUnitsReport report) {
        report.getSubUnitReports()
                .forEach((subUnitName, subUnitReport) -> {
                    addHeaderCell(table, subUnitName, 0, 1, 3);
                    addUsableResources(table, subUnitReport);
                    addReserveResources(table, subUnitReport);
                    addUnusableResources(table, subUnitReport);
                });
    }

    private void addUnusableResources(PdfPTable table, SubUnitReport subUnitReport) {
        addHeaderCell(table, "Neoperațional", 0, 1, 1);
        subUnitReport.getFirstInterventionResourceReport()
                .forEach((type, r) -> {
                    addSimpleTableCell(table, r.getUnusableAsString());
                });
        addTotalCell(table, subUnitReport.getFirstInterventionTotal().getUnusableAsString());
        subUnitReport.getOtherResourceReport()
                .forEach((type, r) -> {
                    addSimpleTableCell(table, r.getUnusableAsString());
                });
        addTotalCell(table, subUnitReport.getOtherResourcesTotal().getUnusableAsString());
        addTotalCell(table, subUnitReport.getAllResourcesTotal().getUnusableAsString());
        subUnitReport.getEquipmentReport()
                .forEach((type, r) -> {
                    addSimpleTableCell(table, r.getUnusableAsString());
                });
        addTotalCell(table, subUnitReport.getEquipmentTotal().getUnusableAsString());
    }

    private void addReserveResources(PdfPTable table, SubUnitReport subUnitReport) {
        addHeaderCell(table, "Rezervă", 0, 1, 1);
        subUnitReport.getFirstInterventionResourceReport()
                .forEach((type, r) -> {
                    addSimpleTableCell(table, r.getReserveAsString());
                });
        addTotalCell(table, subUnitReport.getFirstInterventionTotal().getReserveAsString());
        subUnitReport.getOtherResourceReport()
                .forEach((type, r) -> {
                    addSimpleTableCell(table, r.getReserveAsString());
                });
        addTotalCell(table, subUnitReport.getOtherResourcesTotal().getReserveAsString());
        addTotalCell(table, subUnitReport.getAllResourcesTotal().getReserveAsString());
        subUnitReport.getEquipmentReport()
                .forEach((type, r) -> {
                    addSimpleTableCell(table, r.getReserveAsString());
                });
        addTotalCell(table, subUnitReport.getEquipmentTotal().getReserveAsString());
    }

    private void addUsableResources(PdfPTable table, SubUnitReport subUnitReport) {
        addHeaderCell(table, "Operațional", 0, 1, 1);
        subUnitReport.getFirstInterventionResourceReport()
                .forEach((type, r) -> {
                    addSimpleTableCell(table, r.getUsableAsString());
                });
        addTotalCell(table, subUnitReport.getFirstInterventionTotal().getUsableAsString());
        subUnitReport.getOtherResourceReport()
                .forEach((type, r) -> {
                    addSimpleTableCell(table, r.getUsableAsString());
                });
        addTotalCell(table, subUnitReport.getOtherResourcesTotal().getUsableAsString());
        addTotalCell(table, subUnitReport.getAllResourcesTotal().getUsableAsString());
        subUnitReport.getEquipmentReport()
                .forEach((type, r) -> {
                    addSimpleTableCell(table, r.getUsableAsString());
                });
        addTotalCell(table, subUnitReport.getEquipmentTotal().getUsableAsString());
    }

    private int buildNumberOfColumns(List<SubUnit> subUnits) {
        List<String> equipmentTypes = getEquipmentTypes(subUnits);
        List<String> firstInterventionResourcesTypes = getFirstInterventionResourcesTypes(subUnits);
        List<String> otherResourcesTypes = getOtherResourcesTypes(subUnits);
        return equipmentTypes.size() + firstInterventionResourcesTypes.size() + otherResourcesTypes.size() + 6;
    }

    private void recreateReportFile() throws IOException {
        File f = new File(EQUIPMENT_REPORT_FILE_NAME);
        if (!f.exists()) {
            f.createNewFile();
        } else {
            f.delete();
            f.createNewFile();
        }
    }

    private void addTableHeader(PdfPTable table, AllSubUnitsReport report) {
        SubUnitReport subUnitReport = report.getSubUnitReports().values().stream().findFirst().orElse(null);
        int firstInterventionResources = subUnitReport.getFirstInterventionResourceReport().size();
        int otherResourcesSize = subUnitReport.getOtherResourceReport().size();
        int equipmentsSize = subUnitReport.getEquipmentReport().size();

        addHeaderCell(table, "Garda de intervenție", 90, 2, 3);
        addHeaderCell(table, "Tehnică de intervenție", 0, firstInterventionResources + otherResourcesSize + 2, 1);
        addHeaderCell(table, "TOTAL Tehnică de intervenție", 90, 1, 3);
        addHeaderCell(table, "Echipamente", 0, equipmentsSize, 2);
        addHeaderCell(table, "TOTAL Echipamente", 90, 1, 3);
        //table.completeRow();
        addHeaderCell(table, "Tehnică de primă intervenție", 0, firstInterventionResources, 1);
        addHeaderCell(table, "TOTAL Tehnică de primă intervenție", 90, 1, 2);
        addHeaderCell(table, "Alte tipuri de tehnică", 0, otherResourcesSize, 1);
        addHeaderCell(table, "TOTAL Alte tipuri de tehnică", 90, 1, 2);

        subUnitReport.getFirstInterventionResourceReport().keySet().forEach(r -> {
            addHeaderCell(table, r, 90, 1, 1);
        });

        subUnitReport.getOtherResourceReport().keySet().forEach(r -> {
            addHeaderCell(table, r, 90, 1, 1);
        });

        subUnitReport.getEquipmentReport().keySet().forEach(r -> {
            addHeaderCell(table, r, 90, 1, 1);
        });
    }

    private void addSimpleTableCell(PdfPTable table, String cellContent) {
        addTableCell(table, cellContent, 0, 1, 1, Color.WHITE, cellFont);
    }

    private void addHeaderCell(PdfPTable table, String cellContent, int rotation, int columnSpan, int rowSpan) {
        addTableCell(table, cellContent, rotation, columnSpan, rowSpan, Color.GRAY, cellFontBold);
    }

    private void addTotalCell(PdfPTable table, String cellContent) {
        addTableCell(table, cellContent, 0, 1, 1, Color.LIGHT_GRAY, cellFont);
    }

    private void addTableCell(PdfPTable table, String cellContent, int rotation, int columnSpan, int rowSpan, Color white, Font cellFont) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(white);
        cell.setBorderWidth(2);
        cell.setPhrase(new Phrase(cellContent, cellFont));
        cell.setRotation(rotation);
        cell.setColspan(columnSpan);
        cell.setRowspan(rowSpan);
        cell.setNoWrap(false);
        table.addCell(cell);
    }

    private List<String> getOtherResourcesTypes(List<SubUnit> subUnits) {
        return subUnits.stream()
                .filter(s -> s.getResources() != null)
                .flatMap(s -> s.getResources().stream())
                .filter(resource -> resource.getType().equals(ResourceType.OTHER))
                .map(Resource::getVehicleType)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<String> getFirstInterventionResourcesTypes(List<SubUnit> subUnits) {
        return subUnits.stream()
                .flatMap(s -> s.getResources().stream())
                .filter(resource -> resource.getType().equals(ResourceType.FIRST_INTERVENTION))
                .map(Resource::getVehicleType)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<String> getEquipmentTypes(List<SubUnit> subUnits) {
        return subUnits.stream()
                .filter(s -> s.getEquipment() != null)
                .flatMap(s -> s.getEquipment().stream())
                .map(Equipment::getEquipmentType)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
