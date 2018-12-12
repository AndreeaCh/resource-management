package com.resource.management.resource.model;

public final class EquipmentMapper {
    public static final Equipment toInternal(final com.resource.management.api.resources.Equipment externalEquipment) {
        Equipment equipment = new Equipment();

        equipment.setEquipmentId(externalEquipment.getEquipmentId());
        equipment.setEquipmentType(externalEquipment.getEquipmentType());
        equipment.setReserves(externalEquipment.getReserves());
        equipment.setUsable(externalEquipment.getUsable());
        equipment.setUnusable(externalEquipment.getUnusable());
        equipment.setResourceType(externalEquipment.getResourceType());
        return equipment;
    }

    public static final com.resource.management.api.resources.Equipment toApi(final Equipment internalEquipment) {
        com.resource.management.api.resources.Equipment equipment = new com.resource.management.api.resources.Equipment();
        equipment.setEquipmentId(internalEquipment.getEquipmentId());
        equipment.setEquipmentType(internalEquipment.getEquipmentType());
        equipment.setReserves(internalEquipment.getReserves());
        equipment.setUsable(internalEquipment.getUsable());
        equipment.setUnusable(internalEquipment.getUnusable());
        equipment.setResourceType(internalEquipment.getResourceType());
        return equipment;
    }
}
