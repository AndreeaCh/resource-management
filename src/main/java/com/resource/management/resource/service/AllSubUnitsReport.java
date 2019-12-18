package com.resource.management.resource.service;

import com.resource.management.resource.model.SubUnit;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AllSubUnitsReport {
    private List<SubUnitReport> subUnitReports;
    private SubUnitReport totals;
}
