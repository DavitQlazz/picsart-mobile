package com.picsart.mobile.config;


import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.*;

// Order set by priority
@Sources({
        "system:env",
        "system:properties",
        "classpath:config.properties"
})
@LoadPolicy(LoadType.MERGE)
public interface ConfigLoader extends Config {

    @Key("cloud.server")
    @DefaultValue("https://hub-cloud.browserstack.com/wd/hub")
    String cloudServer();

    @Key("local.server")
    @DefaultValue("http://0.0.0.0:4723/")
    String localServer();

    @Key("base.url")
    @DefaultValue("https://picsart.com")
    String baseUrl();

    @Key("android.browser")
    @DefaultValue("chrome")
    String androidBrowser();

    @Key("ios.browser")
    @DefaultValue("safari")
    String iosBrowser();

    @Key("timeout.long")
    @DefaultValue("30")
    int longTimeout();

    @Key("timeout.short")
    @DefaultValue("8")
    int shortTimeout();

    @Key("timeout.newCommand")
    @DefaultValue("8")
    int newCommandTimeout();

    @Key("platform")
    @DefaultValue("android")
    String platform();

    @Key("runMode")
    @DefaultValue("local")
    String runMode();
}
