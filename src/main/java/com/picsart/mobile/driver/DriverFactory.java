package com.picsart.mobile.driver;

import com.picsart.mobile.config.ConfigLoader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.SupportsContextSwitching;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import static com.picsart.mobile.appium.AppiumServerManager.getAppiumServerUrl;

public class DriverFactory {
    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();

    public static AppiumDriver getDriver() {
        if (driver.get() == null) {
            setupDriver();
        }
        return driver.get();
    }

    private static void setupDriver() {
        String platform = System.getProperty("platform", "android");
        String runMode = System.getProperty("runMode", "local");

        MutableCapabilities caps;
        if (platform.equals("ios")) {
            caps = getIosCapabilities();
        } else {
            caps = getAndroidCapabilities();
        }

        if (runMode.equals("cloud")) {
            caps = getCloudCapabilities();
        }

        try {
            String serverUrl = runMode.equals("cloud")
                    ? ConfigLoader.getProperty("cloud.url")
                    : getAppiumServerUrl();

            driver.set(platform.equals("ios")
                    ? new IOSDriver(new URL(serverUrl), caps)
                    : new AndroidDriver(new URL(serverUrl), caps));
//            ((SupportsContextSwitching) driver.get()).context("WEBVIEW_chrome");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static MutableCapabilities getAndroidCapabilities() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(Platform.ANDROID.name())
                .setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2)
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(600))
                .setNativeWebScreenshot(true);

        // Additional Chrome-related capabilities
//        options.setCapability("chromedriverAutodownload", true);
//        options.setCapability("recreateChromeDriverSessions", true);
        return options;
    }

    private static MutableCapabilities getIosCapabilities() {
        XCUITestOptions caps = new XCUITestOptions();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("browserName", "Safari");
        caps.setCapability("automationName", "XCUITest");
        caps.setNewCommandTimeout(Duration.ofSeconds(600));
        caps.autoWebview();
        return caps;
    }

    private static MutableCapabilities getCloudCapabilities() {
        MutableCapabilities caps = new MutableCapabilities();
        HashMap<String, Object> bstackOptions = new HashMap<>();
        caps.setCapability("browserName", "chrome");
        bstackOptions.put("osVersion", "13.0");
        bstackOptions.put("deviceName", "Samsung Galaxy S23");
        bstackOptions.put("userName", "fortesting_AnMoR9");
        bstackOptions.put("accessKey", "yk1hRvjQjjpKSBRxcqpU");
        bstackOptions.put("consoleLogs", "info");
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
