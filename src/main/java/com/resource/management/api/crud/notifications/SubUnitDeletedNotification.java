package com.resource.management.api.crud.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class SubUnitDeletedNotification {
    private String deletedSubUnitName;
}