package com.picsart.mobile.pages;

import com.picsart.mobile.element.AndroidBy;
import com.picsart.mobile.element.IOSBy;
import com.picsart.mobile.element.WrappedElement;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;

public class LandingPage extends BasePage {

    @AndroidBy(xpath = "(//*[@text='Accept All Cookies'])[2]")
    @IOSBy(xpath = "//XCUIElementTypeButton[@name='Accept All Cookies']")
    private WrappedElement acceptAllCookies;

    public LandingPage(AppiumDriver driver) {
        super(driver);
    }
    @Step
    public void acceptCookies() {
        switchToNativeView();
        acceptAllCookies.clickIfExists();
    }
}
