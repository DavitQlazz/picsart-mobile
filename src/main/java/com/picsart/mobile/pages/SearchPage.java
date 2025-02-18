package com.picsart.mobile.pages;

import com.picsart.mobile.element.WrappedElement;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class SearchPage extends BasePage {

    @AndroidFindBy(xpath = "//*[@resource-id='filter_icon']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Filter']")
    private WrappedElement filterIcon;

    @AndroidFindBy(xpath = "//*[@text='licenses-Personal-checkbox']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSwitch[@name='licenses-Personal-checkbox']")
    private WrappedElement personalCheckbox;

    @AndroidFindBy(xpath = "//*[starts-with(@resource-id, 'base_card_item')]")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeImage[@value='blur']")
    private WrappedElement allAssets;

    @AndroidFindBy(xpath = "//*[starts-with(@resource-id, 'base_card_item')]/android.view.View/android.widget.Image")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeAny[@value='blur']")
    private WrappedElement plusAssets;

    @AndroidFindBy(xpath = "//android.view.View[@resource-id=\"__next\"]" +
            "/android.view.View[2]/android.view.View/android.view.View[1]/android.widget.Button")
    @iOSXCUITFindBy(xpath = "//*[@data-testid='likeComponent']")
    private WrappedElement likeBtn;

    @AndroidFindBy(xpath = "//*[@data-testid='saved-to-collection']")
    @iOSXCUITFindBy(xpath = "//*[@data-testid='saved-to-collection']")
    private WrappedElement saveBtn;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='Edit']")
    @iOSXCUITFindBy(xpath = "//*[@class='heading-editButtonHolder-0-2-66'] /button")
    private WrappedElement editBtn;

    @AndroidFindBy(
            xpath = "//android.widget.Button[starts-with(@text, 'Sign in options title Sign in options description')]")
    @iOSXCUITFindBy(xpath = "//*[@data-testid='registration-overlay']")
    private WrappedElement signInPopup;

    @AndroidFindBy(xpath = "//*[@package='com.android.vending']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeAny[@value='blur']")
    private WrappedElement playStore;

    public SearchPage(AppiumDriver driver) {
        super(driver);
    }

    public SearchPage clickFilterButton() {
        switchToNativeViewIos();
        filterIcon.click();
        return this;
    }

    public SearchPage clickPersonalCheckbox() {
        personalCheckbox.click();
        return this;
    }

    public SearchPage clickFirstImage() {
        allAssets.getByIndex(0).click();
        switchToWebViewIos();
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
        return playStore.isDisplayed();
    }
}
