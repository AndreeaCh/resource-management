package com.resource.management.resource.model;

public enum ResourceType {

    FIRST_INTERVENTION, EQUIPMENT, OTHER, RESERVE;

    @Override
    public String toString() {
        switch (this){
            case FIRST_INTERVENTION: return "Tehnică de primă intervenție";
            case EQUIPMENT: return "Echipamente";
            case OTHER: return "Alte tipuri de tehnică";
            case RESERVE: return "Rezerve";
            default: return this.name();
        }
    }
}
