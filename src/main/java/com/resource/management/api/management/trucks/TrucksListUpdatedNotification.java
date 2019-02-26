package com.resource.management.api.management.trucks;

import com.resource.management.management.functions.model.Function;
import com.resource.management.management.trucks.model.Truck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrucksListUpdatedNotification {
    private List<Truck> trucks;
}
