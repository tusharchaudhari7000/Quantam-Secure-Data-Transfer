package com.dypcoe.qsdta.configuration.propertyutil;

public interface IPropertyOperations {
    public String getStringProperty(String key, String defaultValue);
    public int getIntegerProperty(String key, int defaultValue);
    public float getFloatProperty(String key, float defaultValue);
    public long getLongProperty(String key, long defaultValue);
    public double getDoubleProperty(String key, double defaultValue);
    public boolean getBooleanProperty(String key, boolean defaultValue);
}
