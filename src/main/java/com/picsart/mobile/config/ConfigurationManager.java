package com.picsart.mobile.config;

import org.aeonbits.owner.ConfigCache;

public class ConfigurationManager {

    private ConfigurationManager() {}

    public static ConfigLoader configuration() {
        return ConfigCache.getOrCreate(ConfigLoader.class);
    }
}
