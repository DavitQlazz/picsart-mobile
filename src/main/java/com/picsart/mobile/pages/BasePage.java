package com.picsart.mobile.pages;

import com.picsart.mobile.element.WrappedElementDecorator;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Platform;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

import static com.picsart.mobile.driver.DriverFactory.config;

@Slf4j
public class BasePage {
    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait shortWait;

    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(config.longTimeout()));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(config.shortTimeout()));
        PageFactory.initElements(new WrappedElementDecorator(driver), this);
    }

    @Step
    public void openUrl(String url) {
        driver.get(url);
    }

    protected void switchToWebView() {
        String platformName = driver.getCapabilities().getPlatformName().name();
        if ("iOS".equalsIgnoreCase(platformName)) {
            switchToWebViewIos();
        } else {
            switchToWebViewAndroid();
        }
    }

    private void switchToWebViewAndroid() {
        wait.until(d -> {
            Set<String> contextHandles = ((SupportsContextSwitching) d).getContextHandles();
            contextHandles.remove("NATIVE_APP");
            log.info("Context handles: {}", contextHandles);
            return ((SupportsContextSwitching) d).context(contextHandles.stream().findFirst().orElseThrow());
        });
    }

    private void switchToWebViewIos() {
        wait.until(d -> {
            Set<String> contextHandles = ((IOSDriver) d).getContextHandles();
            for (String context : contextHandles) {
                if (context.contains("WEBVIEW")) {
                    ((IOSDriver) d).context(context); // Switch to WebView context
                    return true;
                }
            }
            return false;
        });
    }

    protected void switchToNativeView() {
        if (driver instanceof IOSDriver) {
            ((IOSDriver) driver).context("NATIVE_APP");
        } else if (driver instanceof AndroidDriver) {
            ((SupportsContextSwitching) driver).context("NATIVE_APP");
        }
    }

    private boolean isAppStoreDisplayed() {
        return wait.until(driver -> ((IOSDriver) driver)
                .queryAppState("com.apple.AppStore")
                .name()
                .equalsIgnoreCase("RUNNING_IN_BACKGROUND_SUSPENDED"));
    }

    private boolean isPlayStoreDisplayed() {
        return wait.until(driver -> {
            String activity = ((AndroidDriver) driver).currentActivity();
            assert activity != null;
            return activity.contains("com.google.android.finsky");
        });
    }

    public boolean isStoreDisplayed() {
        if (config.platform().equalsIgnoreCase(Platform.IOS.name())) {
            return isAppStoreDisplayed();
        } else {
            return isPlayStoreDisplayed();
        }
    }
}
