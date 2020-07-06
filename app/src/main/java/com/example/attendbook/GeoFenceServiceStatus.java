package com.example.attendbook;

public class GeoFenceServiceStatus {
    private static final GeoFenceServiceStatus ourInstance = new GeoFenceServiceStatus();

    public boolean isLocationEnable() {
        return locationEnable;
    }

    public void setLocationEnable(boolean locationEnable) {
        this.locationEnable = locationEnable;
    }

    private  boolean isInside = false;
    private boolean locationEnable = true;

    public boolean isInside() {
        return isInside;
    }

    public void setInside(boolean inside) {
        isInside = inside;
    }

    public static GeoFenceServiceStatus getInstance() {
        return ourInstance;
    }

    private GeoFenceServiceStatus() {
    }
}
