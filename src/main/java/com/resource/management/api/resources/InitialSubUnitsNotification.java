package com.resource.management.api.resources;

import com.resource.management.api.resources.LockedSubUnit;
import com.resource.management.api.resources.SubUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class InitialSubUnitsNotification {
    private List<SubUnit> subUnitsList;
    private List<LockedSubUnit> lockedSubUnits;
}
