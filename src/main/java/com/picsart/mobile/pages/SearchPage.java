package com.picsart.mobile.pages;

import com.picsart.mobile.element.AndroidBy;
import com.picsart.mobile.element.IOSBy;
import com.picsart.mobile.element.WrappedElement;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchPage extends BasePage<SearchPage> {

    @AndroidBy(xpath = "//*[@resource-id='filter_icon']")
    @IOSBy(xpath = "//XCUIElementTypeButton[@name='Filter']")
    private WrappedElement filterIcon;

    @AndroidBy(xpath = "//*[@text='licenses-Personal-checkbox']")
    @IOSBy(xpath = "//XCUIElementTypeSwitch[@name='licenses-Personal-checkbox']")
    private WrappedElement personalCheckbox;

    @AndroidBy(xpath = "//*[starts-with(@resource-id, 'base_card_item')]")
    @IOSBy(xpath = "//XCUIElementTypeImage[@value='blur']")
    private WrappedElement allAssets;

    @AndroidBy(xpath = "//*[starts-with(@resource-id, 'base_card_item')]/android.view.View/android.widget.Image")
    @IOSBy(xpath = "//XCUIElementTypeAny[@value='blur']")
    private WrappedElement plusAssets;

    @AndroidBy(
            xpath = "//android.view.View[@resource-id=\"__next\"]"
                    + "/android.view.View[2]/android.view.View/android.view.View[1]/android.widget.Button")
    @IOSBy(xpath = "//*[@data-testid='likeComponent']")
    private WrappedElement likeBtn;

    @AndroidBy(xpath = "//*[@data-testid='saved-to-collection']")
    @IOSBy(xpath = "//*[@data-testid='saved-to-collection']")
    private WrappedElement saveBtn;

    @AndroidBy(xpath = "//android.widget.Button[@text='Edit']")
    @IOSBy(xpath = "//*[@class='heading-editButtonHolder-0-2-66'] /button")
    private WrappedElement editBtn;

    @AndroidBy(
            xpath = "//android.widget.Button[starts-with(@text, 'Sign in options title Sign in options description')]")
    @IOSBy(xpath = "//*[@data-testid='registration-overlay']")
    private WrappedElement signInPopup;

    @AndroidBy(xpath = "//*[@package='com.android.vending']")
    @IOSBy(xpath = "//XCUIElementTypeAny[@value='blur']")
    private WrappedElement playStore;

    @AndroidBy(xpath = "//*[@id='close']")
    @IOSBy(xpath = "//*[@id='credential_picker_iframe']")
    private WrappedElement googleSignInPopupCloseIcon;

    public SearchPage(AppiumDriver driver) {
        super(driver);
    }

    public SearchPage clickFilterButton() {
        switchToNativeView();
        filterIcon.click();
        return this;
    }

    public SearchPage clickPersonalCheckbox() {
        personalCheckbox.click();
        return this;
    }

    public SearchPage clickFirstImage() {
        allAssets.click();
        switchToWebView();
        return this;
    }

    public SearchPage clickLikeButton() {
        likeBtn.click();
        return this;
    }

    public SearchPage clickPlusAsset() {
        plusAssets.click();
        return this;
    }

    public SearchPage closeGoogleSignInPopup() {
        switchToWebView();
        googleSignInPopupCloseIcon.clickIfExists();
        return this;
    }

    public void goBackToSearchPage() {
        driver.navigate().back();
    }

    public boolean isSignInPopupDisplayed() {
        return signInPopup.isDisplayed();
    }

    public boolean isLikeButtonDisplayed() {
        return likeBtn.isDisplayed();
    }

    public boolean isSaveButtonDisplayed() {
        return saveBtn.isDisplayed();
    }

    public boolean isEditButtonDisplayed() {
        return editBtn.isDisplayed();
    }

    public boolean isPlayStoreDisplayed() {
        String name = ((IOSDriver) driver).queryAppState("com.picsart.studio").name();
        log.info("Current activity: {}", name);
        return ((IOSDriver) driver).queryAppState("com.picsart.studio").name().equalsIgnoreCase("RUNNING");
    }
}
