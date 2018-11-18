package com.resource.management.api;

import com.resource.management.data.SubUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class SubscribeSubUnitsResponse {
    private List<SubUnit> subUnitsList;
}
