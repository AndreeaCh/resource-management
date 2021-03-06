package com.resource.management.resource.service;

import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.resource.model.SubUnit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Autowired
    private ReportBuilder reportBuilder;

    public EquipmentXlsCreator() throws IOException {
    }

    public void createXls(final List<SubUnit> subUnits, final List<VehicleType> vehicleTypeList) {
        try {
            recreateReportFile();
            Workbook workbook = buildReportFile(subUnits, vehicleTypeList);
            writeReportFile(workbook);
        } catch (IOException e) {
            LOG.error("Could not create XLS file. ", e);
        }
    }

    private Workbook buildReportFile(List<SubUnit> subUnits, final List<VehicleType> vehicleTypeList) {
        AllSubUnitsReport report = reportBuilder.buildReport(subUnits, vehicleTypeList);

        SubUnitReport subUnitReport = report.getSubUnitReports().values().stream().findFirst().orElse(null);
        int interventionTechniqueResourcesSize = subUnitReport.getInterventionTechniqueResourcesReport().size();
        int equipmentsSize = subUnitReport.getEquipmentReport().size();

        Workbook workbook = new XSSFWorkbook();
        CellStyle headerStyle = createHeaderStyle(workbook, (short) 0);
        CellStyle rotatedHeaderStyle = createHeaderStyle(workbook, (short) 90);
        CellStyle cellStyle = createCellStyle(workbook, (short) 0);

        Sheet sheet = workbook.createSheet("Raport");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY").withZone(ZoneId.systemDefault());
        Row title = sheet.createRow(1);
        Cell titleCell = title.createCell(10);
        titleCell.setCellValue(TITLE + formatter.format(Instant.now()));
        titleCell.setCellStyle(createTitleStyle(workbook));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 40));

        int lastRowNumber = 4;
        Row header = sheet.createRow(lastRowNumber);
        int columnStartIndex = 0;
        int columnEndIndex = 1;
        Cell gardaDeInterventieHeader = header.createCell(columnStartIndex);
        gardaDeInterventieHeader.setCellValue(GARDA_DE_INTERVENTIE);
        gardaDeInterventieHeader.setCellStyle(headerStyle);
        CellRangeAddress region = new CellRangeAddress(lastRowNumber, lastRowNumber + 1, columnStartIndex,
                columnEndIndex);
        sheet.addMergedRegion(region);

        columnStartIndex = columnEndIndex + 1;
        // ad 2 for two total columns
        columnEndIndex = columnStartIndex + interventionTechniqueResourcesSize - 1;
        Cell tehnicaDeInterventieHeader = header.createCell(columnStartIndex);
        tehnicaDeInterventieHeader.setCellValue(TEHNICA_DE_INTERVENTIE);
        tehnicaDeInterventieHeader.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(lastRowNumber, lastRowNumber, columnStartIndex, columnEndIndex));

        columnStartIndex = columnEndIndex + 1;
        columnEndIndex = columnStartIndex;
        Cell totalTehnicaDeInterventieHeader = header.createCell(columnStartIndex);
        totalTehnicaDeInterventieHeader.setCellValue(TOTAL_TEHNICA_DE_INTERVENTIE);
        totalTehnicaDeInterventieHeader.setCellStyle(rotatedHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(lastRowNumber, lastRowNumber + 1, columnStartIndex, columnEndIndex));

        columnStartIndex = columnEndIndex + 1;
        columnEndIndex = columnStartIndex + equipmentsSize - 1;
        Cell echipamenteHeader = header.createCell(columnStartIndex);
        echipamenteHeader.setCellValue(ECHIPAMENTE);
        echipamenteHeader.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(lastRowNumber, lastRowNumber, columnStartIndex, columnEndIndex));

        columnStartIndex = columnEndIndex + 1;
        columnEndIndex = columnStartIndex;
        Cell totalEchipamenteHeader = header.createCell(columnStartIndex);
        totalEchipamenteHeader.setCellValue(TOTAL_ECHIPAMENTE);
        totalEchipamenteHeader.setCellStyle(rotatedHeaderStyle);
        sheet.addMergedRegion(new CellRangeAddress(lastRowNumber, lastRowNumber + 1, columnStartIndex, columnEndIndex));

        lastRowNumber = sheet.getLastRowNum()+1 ;
        header = sheet.createRow(lastRowNumber);

        columnStartIndex = columnEndIndex + 1;
        columnEndIndex = columnStartIndex ;

        lastRowNumber = sheet.getLastRowNum();
        final Row finalHeaderRow = sheet.createRow(lastRowNumber);

        AtomicInteger finalColumnStartIndex = new AtomicInteger(2);
        subUnitReport.getInterventionTechniqueResourcesReport().keySet().forEach(name -> {
            Cell firstInverventionResources = finalHeaderRow.createCell(finalColumnStartIndex.get());
            firstInverventionResources.setCellValue(name);
            firstInverventionResources.setCellStyle(rotatedHeaderStyle);
            finalColumnStartIndex.getAndIncrement();
        });

        //skip total columns
        finalColumnStartIndex.getAndIncrement();
        subUnitReport.getEquipmentReport().keySet().forEach(name -> {
            Cell firstInverventionResources = finalHeaderRow.createCell(finalColumnStartIndex.get());
            firstInverventionResources.setCellValue(name);
            firstInverventionResources.setCellStyle(rotatedHeaderStyle);
            finalColumnStartIndex.getAndIncrement();
        });

        final AtomicInteger startIndex = new AtomicInteger(sheet.getLastRowNum() + 1);
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
        subUnitReport.getInterventionTechniqueResourcesReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getUsable());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell firstInterventionTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        firstInterventionTotalCell.setCellValue(subUnitReport.getInterventionTechniqueTotal().getUsable());
        firstInterventionTotalCell.setCellStyle(headerStyle);

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
        subUnitReport.getInterventionTechniqueResourcesReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getReserves());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell firstInterventionTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        firstInterventionTotalCell.setCellValue(subUnitReport.getInterventionTechniqueTotal().getReserves());
        firstInterventionTotalCell.setCellStyle(headerStyle);

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
        subUnitReport.getInterventionTechniqueResourcesReport()
                .forEach((type, r) -> {
                    Cell resourceCell = contentRow.createCell(columnNumber.getAndIncrement());
                    resourceCell.setCellValue(r.getUnusable());
                    resourceCell.setCellStyle(cellStyle);
                });

        Cell firstInterventionTotalCell = contentRow.createCell(columnNumber.getAndIncrement());
        firstInterventionTotalCell.setCellValue(subUnitReport.getInterventionTechniqueTotal().getUnusable());
        firstInterventionTotalCell.setCellStyle(headerStyle);

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
        CellRangeAddress region = new CellRangeAddress(contentRow.getRowNum(), contentRow.getRowNum() + 2, 0, 0);
        sheet.addMergedRegion(region);
    }

    private void setRegionStyle(final Sheet sheet, final CellRangeAddress region) {
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, region, sheet);
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
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setWrapText(false);
        return headerStyle;
    }

    private CellStyle createHeaderStyle(Workbook workbook, short rotation) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setRotation(rotation);
        headerStyle.setWrapText(false);
        headerStyle.setShrinkToFit(true);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
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
