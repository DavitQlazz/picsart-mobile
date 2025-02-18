package com.picsart.mobile.pages;

import com.picsart.mobile.element.WrappedElement;
import com.picsart.mobile.element.WrappedElementDecorator;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class BasePage {
    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected String baseUrl = "https://www.picsart.com/";

    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        PageFactory.initElements(new WrappedElementDecorator(driver), this);
    }

    public void openUrl(String url) {
        driver.get(url);
    }

    protected void switchToFrame(WrappedElement element) {
        driver.switchTo().frame(element);
    }

    protected void switchToWebViewAndroid() {
        wait.until(d -> {
            Set<String> contextHandles = ((SupportsContextSwitching) d).getContextHandles();
            for (String context : contextHandles) {
                if (context.contains("WEBVIEW")) {
                    ((SupportsContextSwitching) d).context(context); // Switch to WebView context
                    return true;
                }
            }
            return false;
        });
    }
    protected void switchToWebViewIos() {
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

    protected void switchToNativeViewIos() {
        ((IOSDriver) driver).context("NATIVE_APP");
    }

    protected void switchToNativeViewAndroid() {
        ((SupportsContextSwitching) driver).context("NATIVE_APP");
    }
}
