package com.resource.management.resource.service;

import com.resource.management.management.vehicles.model.VehicleType;
import com.resource.management.resource.model.Equipment;

public class CustomFunctionDemo {
    public Integer getCount(String subUnitName, VehicleType vehicleType, String status) {
        switch (status) {
            case "operational":
                return 1;
            case "rezerva":
                return 2;
            case "neoperational":
                return 3;
        }
        return 0;
    }

    public Integer getCount(String subUnitName, Equipment equipment, String status) {
        switch (status) {
            case "operational":
                return 1;
            case "rezerva":
                return 2;
            case "neoperational":
                return 3;
        }
        return 0;
    }

    public Integer getTotal( VehicleType vehicleType, String status) {
        switch (status) {
            case "operational":
                return 11;
            case "rezerva":
                return 22;
            case "neoperational":
                return 33;
        }
        return 0;
    }

    public Integer getTotal( Equipment equipment, String status) {
        switch (status) {
            case "operational":
                return 11;
            case "rezerva":
                return 22;
            case "neoperational":
                return 33;
        }
        return 0;
    }
}
