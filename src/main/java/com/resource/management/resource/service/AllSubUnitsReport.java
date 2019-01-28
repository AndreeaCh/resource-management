package com.resource.management.resource.service;

import lombok.*;

import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AllSubUnitsReport {
    private Map<String, SubUnitReport> subUnitReports;
    private SubUnitReport totals;
}
