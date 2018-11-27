package com.resource.management.api.crud.notifications;

import com.resource.management.model.SubUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class InitialSubUnitsNotification {
    private List<SubUnit> subUnitsList;
}
