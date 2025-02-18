package com.picsart.mobile.pages;

import com.picsart.mobile.element.WrappedElement;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.support.FindBy;

public class LandingPage extends BasePage {

    @AndroidFindBy(xpath = "//*[@text='Accept All Cookies']")
    @iOSXCUITFindBy(xpath = "//button[@id='onetrust-accept-btn-handler']")
    private WrappedElement acceptAllCookies;

    public LandingPage(AppiumDriver driver) {
        super(driver);
    }

    public void acceptCookies() {
        acceptAllCookies.getByIndex(1).click();
    }
}
