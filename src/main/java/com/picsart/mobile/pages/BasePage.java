package com.picsart.mobile.pages;

import com.picsart.mobile.element.WrappedElement;
import com.picsart.mobile.element.WrappedElementDecorator;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class BasePage<T extends BasePage<T>> {
    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected WebDriverWait shortWait;
    protected String baseUrl = "https://www.picsart.com/";

    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(new WrappedElementDecorator(driver), this);
    }

    public void openUrl(String url) {
        driver.get(url);
    }

    protected void switchToFrame(WrappedElement element) {
        driver.switchTo().frame(element);
    }

    protected void switchToWebView() {
        String platformName = driver.getCapabilities().getPlatformName().name();
        if ("iOS".equalsIgnoreCase(platformName)) {
            switchToWebViewIos();
        } else {
            switchToWebViewAndroid();
        }
    }

    public WebElement findElementInAllFrames(WebElement element) {
        List<WebElement> frames = driver.findElements(By.tagName("iframe"));

        for (WebElement frame : frames) {
            driver.switchTo().frame(frame);
            try {
                shortWait.until(visibilityOf(element));
                return element; // Found, return immediately
            } catch (NoSuchElementException | TimeoutException e) {
                driver.switchTo().defaultContent(); // Switch back and continue
            }
        }
        throw new NoSuchElementException("Element not found in any frame.");
    }


    private void switchToWebViewAndroid() {
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
}
