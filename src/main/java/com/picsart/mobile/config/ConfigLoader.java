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

    @Key("ios.device.name")
    @DefaultValue("iPhone 15 Pro")
    String iOSDevice();

    @Key("ios.platform.version")
    @DefaultValue("17")
    String iOSPlatformVersion();

    @Key("timeout.long")
    @DefaultValue("30")
    int longTimeout();

    @Key("timeout.short")
    @DefaultValue("8")
    int shortTimeout();

    @Key("timeout.newCommand")
    @DefaultValue("300")
    int newCommandTimeout();

    @Key("platform")
    @DefaultValue("android")
    String platform();

    @Key("runMode")
    @DefaultValue("local")
    String runMode();

    // Export to your env the Browserstack credentials as "BROWSERSTACK_USERNAME" and "BROWSERSTACK_ACCESSKEY"

    @Key("BROWSERSTACK_USERNAME")
    @DefaultValue("")
    String bsUsername();

    @Key("BROWSERSTACK_ACCESSKEY")
    @DefaultValue("")
    String accessKey();
}
