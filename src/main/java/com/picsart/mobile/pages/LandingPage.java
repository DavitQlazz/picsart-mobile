package com.picsart.mobile.pages;

import com.picsart.mobile.element.AndroidBy;
import com.picsart.mobile.element.IOSBy;
import com.picsart.mobile.element.WrappedElement;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LandingPage extends BasePage {

    @AndroidBy(xpath = "(//*[@text='Accept All Cookies'])[2]")
    @IOSBy(xpath = "//XCUIElementTypeButton[@name='Accept All Cookies']")
    private WrappedElement acceptAllCookies;

    public LandingPage(AppiumDriver driver) {
        super(driver);
    }

    @Step
    public void open() {
        log.info("Opened landing page");
        openUrl(baseUrl + "/search");
    }
    @Step
    public void acceptCookies() {
        log.info("Accepting cookies");
        switchToNativeView();
        acceptAllCookies.clickIfExists();
    }
}
