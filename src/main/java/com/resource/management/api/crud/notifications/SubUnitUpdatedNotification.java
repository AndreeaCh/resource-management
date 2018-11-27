package com.resource.management.api.crud.notifications;

import com.resource.management.api.SubUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class SubUnitUpdatedNotification {
    private SubUnit subUnit;
}
