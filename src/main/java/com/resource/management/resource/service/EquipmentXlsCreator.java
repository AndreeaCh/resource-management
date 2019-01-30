package com.resource.management.resource.service;

import com.resource.management.resource.model.SubUnit;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EquipmentXlsCreator {
    private static final Logger LOG = LoggerFactory.getLogger(EquipmentXlsCreator.class);
    public static final String EQUIPMENT_REPORT_FILE_NAME = "RaportEchipamente.xlsx";

    private static final String TITLE = "TEHNICA DE INTERVENȚIE OPERAȚIONALĂ ȘI NEOPERAȚIONALĂ A I.S.U. CLUJ ÎN DATA DE ";
    private static final String TOTAL = "TOTAL";
    private static final String NEOPERATIONAL = "Neoperațional";
    private static final String REZERVA = "Rezervă";
    private static final String OPERATIONAL = "Operațional";
    private static final String GARDA_DE_INTERVENTIE = "Garda de intervenție";
    private static final String TEHNICA_DE_INTERVENTIE = "Tehnică de intervenție";
    private static final String TOTAL_TEHNICA_DE_INTERVENTIE = "TOTAL Tehnică de intervenție";
    private static final String ECHIPAMENTE = "Echipamente";
    private static final String TOTAL_ECHIPAMENTE = "TOTAL Echipamente";
    private static final String TEHNICA_DE_PRIMA_INTERVENTIE = "Tehnică de primă intervenție";
    private static final String TOTAL_TEHNICA_DE_PRIMA_INTERVENTIE = "TOTAL Tehnică de primă intervenție";
    private static final String ALTE_TIPURI_DE_TEHNICA = "Alte tipuri de tehnică";
    private static final String TOTAL_ALTE_TIPURI_DE_TEHNICA = "TOTAL Alte tipuri de tehnică";


    @Autowired
    private ReportBuilder reportBuilder;

    public EquipmentXlsCreator() throws IOException {
    }

    public void createXls(final List<SubUnit> subUnits) {
        try {
            recreateReportFile();
            Workbook workbook = buildReportFile(subUnits);
            writeReportFile(workbook);
        } catch (IOException e) {
            LOG.error("Could not create XLS file. ", e);
        }
    }

    private Workbook buildReportFile(List<SubUnit> subUnits) {
        AllSubUnitsReport report = reportBuilder.buildReport(subUnits);

        SubUnitReport subUnitReport = report.getSubUnitReports().values().stream().findFirst().orElse(null);
        int firstInterventionResourcesSize = subUnitReport.getFirstInterventionResourceReport().size();
        int otherResourcesSize = subUnitReport.getOtherResourceReport().size();
        int equipmentsSize = subUnitReport.getEquipmentReport().size();

        Workbook workbook = new XSSFWorkbook();
        CellStyle headerStyle = createHeaderStyle(workbook, (short) 0);
        CellStyle rotatedHeaderStyle = createHeaderStyle(workbook, (short) 90);
        CellStyle cellStyle = createCellStyle(workbook, (short) 0);

        Sheet sheet = workbook.createSheet("Raport");
        Row header = sheet.createRow(0);

        int columnStartIndex = 0;
        int columnEndIndex = 1;
        Cell gardaDeInterventieHeader = header.createCell(columnStartIndex);
        gardaDeInterventieHeader.setCellValue(GARDA_DE_INTERVENTIE);
        gardaDeInterventieHeader.setCellStyle(rotatedHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 2, columnStartIndex, columnEndIndex));

        columnStartIndex = columnEndIndex + 1;
        // ad 2 for two total columns
        columnEndIndex = columnStartIndex + firstInterventionResourcesSize + otherResourcesSize + 2 - 1;
        Cell tehnicaDeInterventieHeader = header.createCell(columnStartIndex);
        tehnicaDeInterventieHeader.setCellValue(TEHNICA_DE_INTERVENTIE);
        tehnicaDeInterventieHeader.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, columnStartIndex, columnEndIndex));

        columnStartIndex = columnEndIndex + 1;
        columnEndIndex = columnStartIndex;
        Cell totalTehnicaDeInterventieHeader = header.createCell(columnStartIndex);
        totalTehnicaDeInterventieHeader.setCellValue(TOTAL_TEHNICA_DE_INTERVENTIE);
        totalTehnicaDeInterventieHeader.setCellStyle(rotatedHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 2, columnStartIndex, columnEndIndex));

        columnStartIndex = columnEndIndex + 1;
        columnEndIndex = columnStartIndex + equipmentsSize - 1;
        Cell echipamenteHeader = header.createCell(columnStartIndex);
        echipamenteHeader.setCellValue(ECHIPAMENTE);
        echipamenteHeader.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 1, columnStartIndex, columnEndIndex));

        columnStartIndex = columnEndIndex + 1;
        columnEndIndex = columnStartIndex;
        Cell totalEchipamenteHeader = header.createCell(columnStartIndex);
        totalEchipamenteHeader.setCellValue(TOTAL_ECHIPAMENTE);
        totalEchipamenteHeader.setCellStyle(rotatedHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 2, columnStartIndex, columnEndIndex));

        header = sheet.createRow(1);

        columnStartIndex = 2;
        columnEndIndex = columnStartIndex + firstInterventionResourcesSize - 1;
        Cell tehnicaDePrimaInterventieHeader = header.createCell(columnStartIndex);
        tehnicaDePrimaInterventieHeader.setCellValue(TEHNICA_DE_PRIMA_INTERVENTIE);
        tehnicaDePrimaInterventieHeader.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, columnStartIndex, columnEndIndex));

        columnStartIndex = columnEndIndex + 1;
        columnEndIndex = columnStartIndex;
        Cell totalTehnicaDePrimaInterventieHeader = header.createCell(columnStartIndex);
        totalTehnicaDePrimaInterventieHeader.setCellValue(TOTAL_TEHNICA_DE_PRIMA_INTERVENTIE);
        totalTehnicaDePrimaInterventieHeader.setCellStyle(rotatedHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 2, columnStartIndex, columnEndIndex));

        columnStartIndex = columnEndIndex + 1;
        columnEndIndex = columnStartIndex + otherResourcesSize - 1;
        Cell alteTipuriDeTehnicaHeader = header.createCell(columnStartIndex);
        alteTipuriDeTehnicaHeader.setCellValue(ALTE_TIPURI_DE_TEHNICA);
        alteTipuriDeTehnicaHeader.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, columnStartIndex, columnEndIndex));

        columnStartIndex = columnEndIndex + 1;
        columnEndIndex = columnStartIndex;
        Cell totalAlteTipuriDeTehnicaHeader = header.createCell(columnStartIndex);
        totalAlteTipuriDeTehnicaHeader.setCellValue(TOTAL_ALTE_TIPURI_DE_TEHNICA);
        totalAlteTipuriDeTehnicaHeader.setCellStyle(rotatedHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 2, columnStartIndex, columnEndIndex));

        final Row finalHeaderRow = sheet.createRow(2);

        AtomicInteger finalColumnStartIndex = new AtomicInteger(2);
        subUnitReport.getFirstInterventionResourceReport().keySet().forEach(name -> {
            Cell firstInverventionResources = finalHeaderRow.createCell(finalColumnStartIndex.get());
            firstInverventionResources.setCellValue(name);
            firstInverventionResources.setCellStyle(rotatedHeaderStyle);
            finalColumnStartIndex.getAndIncrement();
        });

        //skip total column
        finalColumnStartIndex.getAndIncrement();
        subUnitReport.getOtherResourceReport().keySet().forEach(name -> {
            Cell firstInverventionResources = finalHeaderRow.createCell(finalColumnStartIndex.get());
            firstInverventionResources.setCellValue(name);
            firstInverventionResources.setCellStyle(rotatedHeaderStyle);
            finalColumnStartIndex.getAndIncrement();
        });

        //skip total columns
        finalColumnStartIndex.getAndIncrement();
        finalColumnStartIndex.getAndIncrement();
        subUnitReport.getEquipmentReport().keySet().forEach(name -> {
            Cell firstInverventionResources = finalHeaderRow.createCell(finalColumnStartIndex.get());
            firstInverventionResources.setCellValue(name);
            firstInverventionResources.setCellStyle(rotatedHeaderStyle);
            finalColumnStartIndex.getAndIncrement();
        });


        final AtomicInteger startIndex = new AtomicInteger(3);
        report.getSubUnitReports()
                .forEach((subUnitName, r) -> {
                    Row contentRow = sheet.createRow(startIndex.getAndIncrement());
                    addSubunitName(sheet, contentRow, subUnitName, headerStyle);
                    addUsableResources(contentRow, r, headerStyle, cellStyle);
                    contentRow = sheet.createRow(startIndex.getAndIncrement());
                    addReserveResources(contentRow, r, headerStyle, cellStyle);
                    contentRow = sheet.createRow(startIndex.getAndIncrement());
                    addUnusableResources(contentRow, r, headerStyle, cellStyle);
                });

        final AtomicInteger totalStartIndex = new AtomicInteger(sheet.getLastRowNum() + 1);
        SubUnitReport reportTotals = report.getTotals();
        Row contentRow = sheet.createRow(totalStartIndex.getAndIncrement());
        addSubunitName(sheet, contentRow, TOTAL, headerStyle);
        addUsableResources(contentRow, reportTotals, headerStyle, headerStyle);
        contentRow = sheet.createRow(totalStartIndex.getAndIncrement());
        addReserveResources(contentRow, reportTotals, headerStyle, headerStyle);
        contentRow = sheet.createRow(totalStartIndex.getAndIncrement());
        addUnusableResources(contentRow, reportTotals, headerStyle, headerStyle);

        for (int i = 0; i < finalHeaderRow.getLastCellNum(); i++) {
            sheet.autoSizeColumn(i, true);
        }

        return workbook;
    }

    private void addUsableResources(Row contentRow, SubUnitReport subUnitReport, CellStyle headerStyle, CellStyle cellStyle) {
        Cell operationalCell = contentRow.createCell(1);
        operationalCell.setCellValue(OPERATIONAL);
        operationalCell.setCellStyle(headerStyle);

        AtomicInteger columnNumber = new AtomicInteger(2);
        subUnitReport.getFirstInterventionResourceReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getUsable());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell firstInterventionTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        firstInterventionTotalCell.setCellValue(subUnitReport.getFirstInterventionTotal().getUsable());
        firstInterventionTotalCell.setCellStyle(headerStyle);

        subUnitReport.getOtherResourceReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getUsable());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell otherTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        otherTotalCell.setCellValue(subUnitReport.getOtherResourcesTotal().getUsable());
        otherTotalCell.setCellStyle(headerStyle);

        Cell resourcesTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        resourcesTotalCell.setCellValue(subUnitReport.getAllResourcesTotal().getUsable());
        resourcesTotalCell.setCellStyle(headerStyle);

        subUnitReport.getEquipmentReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getUsable());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell equipmentTotal = contentRow.createCell(columnNumber.getAndIncrement());
        equipmentTotal.setCellValue(subUnitReport.getEquipmentTotal().getUsable());
        equipmentTotal.setCellStyle(headerStyle);
    }

    private void addReserveResources(Row contentRow, SubUnitReport subUnitReport, CellStyle headerStyle, CellStyle cellStyle) {
        Cell operationalCell = contentRow.createCell(1);
        operationalCell.setCellValue(REZERVA);
        operationalCell.setCellStyle(headerStyle);

        AtomicInteger columnNumber = new AtomicInteger(2);
        subUnitReport.getFirstInterventionResourceReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getReserves());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell firstInterventionTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        firstInterventionTotalCell.setCellValue(subUnitReport.getFirstInterventionTotal().getReserves());
        firstInterventionTotalCell.setCellStyle(headerStyle);

        subUnitReport.getOtherResourceReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getReserves());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell otherTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        otherTotalCell.setCellValue(subUnitReport.getOtherResourcesTotal().getReserves());
        otherTotalCell.setCellStyle(headerStyle);

        Cell resourcesTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        resourcesTotalCell.setCellValue(subUnitReport.getAllResourcesTotal().getReserves());
        resourcesTotalCell.setCellStyle(headerStyle);

        subUnitReport.getEquipmentReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getReserves());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell equipmentTotal = contentRow.createCell(columnNumber.getAndIncrement());
        equipmentTotal.setCellValue(subUnitReport.getEquipmentTotal().getReserves());
        equipmentTotal.setCellStyle(headerStyle);
    }

    private void addUnusableResources(Row contentRow, SubUnitReport subUnitReport, CellStyle headerStyle, CellStyle cellStyle) {
        Cell operationalCell = contentRow.createCell(1);
        operationalCell.setCellValue(NEOPERATIONAL);
        operationalCell.setCellStyle(headerStyle);

        AtomicInteger columnNumber = new AtomicInteger(2);
        subUnitReport.getFirstInterventionResourceReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getUnusable());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell firstInterventionTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        firstInterventionTotalCell.setCellValue(subUnitReport.getFirstInterventionTotal().getUnusable());
        firstInterventionTotalCell.setCellStyle(headerStyle);

        subUnitReport.getOtherResourceReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getUnusable());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell otherTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        otherTotalCell.setCellValue(subUnitReport.getOtherResourcesTotal().getUnusable());
        otherTotalCell.setCellStyle(headerStyle);

        Cell resourcesTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        resourcesTotalCell.setCellValue(subUnitReport.getAllResourcesTotal().getUnusable());
        resourcesTotalCell.setCellStyle(headerStyle);

        subUnitReport.getEquipmentReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getUnusable());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell equipmentTotal = contentRow.createCell(columnNumber.getAndIncrement());
        equipmentTotal.setCellValue(subUnitReport.getEquipmentTotal().getUnusable());
        equipmentTotal.setCellStyle(headerStyle);
    }

    private void addSubunitName(Sheet sheet, Row contentRow, String subUnitName, CellStyle headerStyle) {
        Cell subUnitNameCell = contentRow.createCell(0);
        subUnitNameCell.setCellValue(subUnitName);
        subUnitNameCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(contentRow.getRowNum(), contentRow.getRowNum() + 2, 0, 0));
    }

    private CellStyle createCellStyle(Workbook workbook, short rotation) {
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(false);
        cellStyle.setFont(font);
        cellStyle.setRotation(rotation);
        cellStyle.setWrapText(false);
        return cellStyle;
    }

    private CellStyle createHeaderStyle(Workbook workbook, short rotation) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setRotation(rotation);
        headerStyle.setWrapText(false);
        headerStyle.setShrinkToFit(true);
        return headerStyle;
    }

    private void writeReportFile(final Workbook workbook) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(EQUIPMENT_REPORT_FILE_NAME);
        workbook.write(outputStream);
        workbook.close();
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
}