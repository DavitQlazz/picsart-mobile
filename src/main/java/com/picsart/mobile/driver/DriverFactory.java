package com.picsart.mobile.driver;

import com.picsart.mobile.config.ConfigLoader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.safari.options.SafariOptions;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static com.picsart.mobile.appium.AppiumServerManager.getAppiumServerUrl;
import static com.picsart.mobile.config.ConfigurationManager.configuration;
import static io.appium.java_client.remote.AutomationName.ANDROID_UIAUTOMATOR2;
import static io.appium.java_client.remote.AutomationName.IOS_XCUI_TEST;
import static java.time.Duration.ofSeconds;

@Slf4j
public class DriverFactory {
    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    public static ConfigLoader config = configuration();

    public static AppiumDriver getDriver() {
        if (driver.get() == null) {
            setupDriver();
        }
        return driver.get();
    }

    private static void setupDriver() {

        MutableCapabilities caps;
        if (config.platform().equalsIgnoreCase(Platform.IOS.name())) {
            caps = getIosCapabilities();
        } else {
            caps = getAndroidCapabilities();
        }

        if (config.runMode().equals("cloud")) {
            if (config.platform().equalsIgnoreCase(Platform.IOS.name())) {
                caps = getCloudCapabilitiesIOS();
            } else {
                caps = getCloudCapabilitiesAndroid();
            }
        }

        try {
            String serverUrl = config.runMode().equals("cloud") ? config.cloudServer() : getAppiumServerUrl();

            driver.set(config.platform().equals("ios")
                    ? new IOSDriver(new URL(serverUrl), caps)
                    : new AndroidDriver(new URL(serverUrl), caps));
        } catch (MalformedURLException e) {
            log.error("Driver is not created: {}", config.platform(), e);
        }
    }

    private static MutableCapabilities getAndroidCapabilities() {
        return new UiAutomator2Options()
                .setPlatformName(Platform.ANDROID.name())
                .setAutomationName(ANDROID_UIAUTOMATOR2)
                .setAutoGrantPermissions(true)
                .withBrowserName(config.androidBrowser())
                .setNewCommandTimeout(ofSeconds(config.newCommandTimeout()))
                .setNativeWebScreenshot(true);
    }

    private static MutableCapabilities getIosCapabilities() {
        XCUITestOptions caps = new XCUITestOptions();
        caps.setAutoWebview(false);
        caps.setFullReset(false);
        caps.setNoReset(false);
        caps.usePrebuiltWda();
        caps.setCapability("webviewConnectRetries", 3);
        caps.withBrowserName(config.iosBrowser());
        caps.setAutomationName(IOS_XCUI_TEST);
        caps.safariIgnoreFraudWarning();
        caps.includeSafariInWebviews();
        caps.setCapability("startIWDP", true);
        caps.setDeviceName(config.iOSDevice());
        caps.safariIgnoreFraudWarning();
        caps.setPlatformName(Platform.IOS.name());
        caps.setPlatformVersion(config.iOSPlatformVersion());
        caps.setNewCommandTimeout(ofSeconds(config.newCommandTimeout()));

        HashMap<String, Object> appiumOptions = new HashMap<>();
        appiumOptions.put("appium:recreateChromeDriverSessions", true);
        caps.setCapability("appium:options", appiumOptions);
        return caps;
    }

    private static MutableCapabilities getCloudCapabilitiesAndroid() {
        UiAutomator2Options caps = new UiAutomator2Options();
        caps.setAutomationName(ANDROID_UIAUTOMATOR2);
        HashMap<String, Object> bstackOptions = new HashMap<>();
        caps.setCapability("browserName", config.androidBrowser());
        bstackOptions.put("osVersion", "13.0");
        bstackOptions.put("deviceName", "Samsung Galaxy S23 Ultra");
        bstackOptions.put("userName", config.bsUsername());
        bstackOptions.put("accessKey", config.accessKey());
        bstackOptions.put("consoleLogs", "info");
        caps.setCapability("bstack:options", bstackOptions);
        return caps;
    }

    private static MutableCapabilities getCloudCapabilitiesIOS() {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("browserName", config.iosBrowser());
        caps.setCapability("appium:safariOptions", new SafariOptions().setFullReset(true).setNoReset(false));

        HashMap<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("osVersion", config.iOSPlatformVersion());
        bstackOptions.put("deviceName", config.iOSDevice());
        bstackOptions.put("userName", config.bsUsername());
        bstackOptions.put("accessKey", config.accessKey());
        caps.setCapability("bstack:options", bstackOptions);
        return caps;
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
