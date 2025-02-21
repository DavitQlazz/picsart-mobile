package com.picsart.mobile.driver;

import com.picsart.mobile.config.ConfigLoader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.safari.options.SafariOptions;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import static com.picsart.mobile.appium.AppiumServerManager.getAppiumServerUrl;
import static com.picsart.mobile.config.ConfigurationManager.configuration;

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
            e.printStackTrace();
        }
    }

    private static MutableCapabilities getAndroidCapabilities() {
        return new UiAutomator2Options()
                .setPlatformName(Platform.ANDROID.name())
                .setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2)
                .setAutoGrantPermissions(true)
                .withBrowserName(config.androidBrowser())
                .setNewCommandTimeout(Duration.ofSeconds(600))
                .setNativeWebScreenshot(true);
    }

    private static MutableCapabilities getIosCapabilities() {
        XCUITestOptions caps = new XCUITestOptions();
        caps.setAutoWebview(false);
        caps.setFullReset(false);
        caps.setNoReset(false);
        caps.setCapability("webviewConnectRetries", 3);
        caps.withBrowserName(config.iosBrowser());
        caps.setAutomationName("XCUITest");
        caps.safariIgnoreFraudWarning();
        caps.includeSafariInWebviews();
        caps.setCapability("startIWDP", true);
        caps.setDeviceName("iPhone 15 Pro");
        caps.safariIgnoreFraudWarning();
        caps.setPlatformName(Platform.IOS.name());
        caps.setCapability("platformVersion", "17.4");
        caps.setNewCommandTimeout(Duration.ofSeconds(600));
        caps.autoWebview();

        HashMap<String, Object> appiumOptions = new HashMap<>();
        appiumOptions.put("appium:recreateChromeDriverSessions", true);
        caps.setCapability("appium:options", appiumOptions);
        return caps;
    }

    private static MutableCapabilities getCloudCapabilitiesAndroid() {
        MutableCapabilities caps = new MutableCapabilities();
        HashMap<String, Object> bstackOptions = new HashMap<>();
        caps.setCapability("browserName", config.androidBrowser());
        bstackOptions.put("osVersion", "13.0");
        bstackOptions.put("deviceName", "Samsung Galaxy S23");
        bstackOptions.put("userName", "fortesting_AnMoR9");
        bstackOptions.put("accessKey", "yk1hRvjQjjpKSBRxcqpU");
        bstackOptions.put("consoleLogs", "info");
        caps.setCapability("bstack:options", bstackOptions);
        return caps;
    }

    private static MutableCapabilities getCloudCapabilitiesIOS() {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("browserName", config.iosBrowser());
        caps.setCapability("appium:safariOptions", new SafariOptions().setFullReset(true).setNoReset(false));

//        HashMap<String, Object> prefs = new HashMap<>();
//        prefs.put("credentials_enable_service", false);
//        prefs.put("profile.password_manager_enabled", false);
//        caps.setCapability(CHROME_OPTIONS_OPTION, prefs);

        HashMap<String, Object> appiumOptions = new HashMap<>();
        appiumOptions.put("appium:recreateChromeDriverSessions", true);
        appiumOptions.put("appium:userProfile", 2);
        caps.setCapability("appium:options", appiumOptions);

        HashMap<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("osVersion", "17");
        bstackOptions.put("deviceName", "iPhone 15 Pro Max");
        bstackOptions.put("userName", "fortesting_AnMoR9");
        bstackOptions.put("accessKey", "yk1hRvjQjjpKSBRxcqpU");
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
