package com.dypcoe.qsdta.configuration.propertyutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ConfigurationProperty implements IPropertyOperations {
    private final Properties properties;
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationProperty.class);

    public ConfigurationProperty(File file) {
        this.properties = new Properties();
        logger.info("Loading properties File : " + (file != null ? file.getName() : "null"));
        loadProperties(file);
    }

    private void loadProperties(File file) {
        if (file == null || !file.exists()) {
            logger.error("Configuration file not found: " + (file != null ? file.getName() : "null"));
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
        } catch (IOException e) {
            logger.error("Error while loading file: " + file.getName(), e);
        }
    }

    @Override
    public String getStringProperty(String key, String defaultValue) {
        return null;
    }

    @Override
    public int getIntegerProperty(String key, int defaultValue) {
        return 0;
    }

    @Override
    public float getFloatProperty(String key, float defaultValue) {
        return 0;
    }

    @Override
    public long getLongProperty(String key, long defaultValue) {
        return 0;
    }

    @Override
    public double getDoubleProperty(String key, double defaultValue) {
        return 0;
    }

    @Override
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        return false;
    }
}
