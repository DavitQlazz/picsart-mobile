package com.picsart.mobile.pages;

import com.picsart.mobile.element.WrappedElement;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.FindBy;

public class LandingPage extends BasePage {

    @FindBy(xpath = "//*[@text='Accept All Cookies']")
    private WrappedElement acceptAllCookies;

    @FindBy(xpath = "//*[@text='Log in']")
    private WrappedElement loginBtn;

    public LandingPage(AppiumDriver driver) {
        super(driver);
    }

    public void acceptCookiesIfExists() {
        acceptAllCookies.clickIfExists();
    }

    public void clickLoginButton() {
        loginBtn.click();
    }
}
