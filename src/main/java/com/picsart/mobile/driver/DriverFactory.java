package com.picsart.mobile.driver;

import com.picsart.mobile.config.ConfigLoader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AutomationName;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;

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
            if (platform.equals("ios")) {
                caps = getCloudCapabilitiesIOS();
            } else {
                caps = getCloudCapabilitiesAndroid();
            }
        }

        try {
            String serverUrl = runMode.equals("cloud")
                    ? ConfigLoader.getProperty("cloud.url")
                    : getAppiumServerUrl();

            driver.set(platform.equals("ios")
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
                .setNewCommandTimeout(Duration.ofSeconds(600))
                .setNativeWebScreenshot(true);
    }

    private static MutableCapabilities getIosCapabilities() {
        XCUITestOptions caps = new XCUITestOptions();
        caps.setAutoWebview(false);
        caps.noReset();
        caps.setCapability("webviewConnectRetries", 3);
        caps.withBrowserName("Safari");
        caps.setAutomationName("XCUITest");
        caps.safariIgnoreFraudWarning();
        caps.includeSafariInWebviews();
        caps.setDeviceName("iPhone 15 Pro");
        caps.setPlatformName("iOS");
        caps.setCapability("platformVersion", "17.4");
        caps.setNewCommandTimeout(Duration.ofSeconds(600));
        caps.autoWebview();
        return caps;
    }

    private static MutableCapabilities getCloudCapabilitiesAndroid() {
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

    private static MutableCapabilities getCloudCapabilitiesIOS() {
        MutableCapabilities caps = new MutableCapabilities();
        HashMap<String, Object> bstackOptions = new HashMap<>();
        caps.setCapability("browserName", "safari");
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
