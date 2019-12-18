package com.resource.management.resource.service;

import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.resource.model.SubUnit;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class TemplateBasedReportCreator {
    public static final String EQUIPMENT_REPORT_FILE_NAME = "RaportEchipamente.xlsx";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY").withZone(ZoneId.systemDefault());
    @Autowired
    private ReportBuilder reportBuilder;

    public void buildReportFile(List<SubUnit> allSubunits, final List<VehicleType> vehicleTypeList) throws FileNotFoundException {
        AllSubUnitsReport report = reportBuilder.buildReport(allSubunits, vehicleTypeList);
        Collection<VehicleType> firstInterventionResources = report.getSubUnitReports().stream()
                .flatMap(v -> v.getFirstInterventionResourceReport().stream())
                .map(VehicleTypeReport::getVehicleType)
                .filter(distinctByKey(VehicleType::getShortName))
                .collect(Collectors.toSet());

        InputStream resourceAsStream = new FileInputStream("C:\\1-DEV\\resource-management\\src\\main\\resources\\RaportS61Template.xlsx");
        try (InputStream is = resourceAsStream) {
            try (OutputStream os = new FileOutputStream(EQUIPMENT_REPORT_FILE_NAME)) {
                Transformer transformer = TransformerFactory.createTransformer(is, os);

                Context context = new Context();
                context.putVar("date", formatter.format(Instant.now()));
                context.putVar("subUnitReports", report.getSubUnitReports());
                context.putVar("firstInterventionResources", firstInterventionResources);

                JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
                Map<String, Object> functionMap = new HashMap<>();
                functionMap.put("report", new CustomFunctionDemo());
                JexlEngine jexl = new JexlBuilder().namespaces(functionMap).create();
                evaluator.setJexlEngine(jexl);

                AreaBuilder areaBuilder = new XlsCommentAreaBuilder();
                areaBuilder.setTransformer(transformer);
                List<Area> xlsAreaList = areaBuilder.build();
                Area firstArea = xlsAreaList.get(0);

                CellRef targetCellRef = new CellRef("Sheet!A1");
                firstArea.applyAt(targetCellRef, context);
                firstArea.processFormulas();
                String sourceSheetName = firstArea.getStartCellRef().getSheetName();
                transformer.deleteSheet(sourceSheetName);

                transformer.write();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
