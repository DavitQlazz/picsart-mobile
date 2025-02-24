package com.picsart.mobile.pages;

import com.picsart.mobile.element.AndroidBy;
import com.picsart.mobile.element.IOSBy;
import com.picsart.mobile.element.WrappedElement;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Keys;

@Slf4j
public class SearchPage extends BasePage {

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

    @AndroidBy(xpath = "//*[@data-testid='likeComponent']")
    @IOSBy(xpath = "//*[@data-testid='likeComponent']")
    private WrappedElement likeBtn;

    @AndroidBy(xpath = "//*[@data-testid='saved-to-collection']")
    @IOSBy(xpath = "//*[@data-testid='saved-to-collection']")
    private WrappedElement saveBtn;

    @AndroidBy(xpath = "//button[contains(@class, 'heading-editButton')]")
    @IOSBy(xpath = "//button[contains(@class, 'heading-editButton')]")
    private WrappedElement editBtn;

    @AndroidBy(xpath = "//*[@data-testid='registration-overlay']")
    @IOSBy(xpath = "//*[@data-testid='registration-overlay']")
    private WrappedElement signInPopup;

    @AndroidBy(xpath = "//*[@package='com.android.vending']")
    @IOSBy(xpath = "//XCUIElementTypeAny[@value='blur']")
    private WrappedElement playStore;

    @AndroidBy(xpath = "//*[@id='credential_picker_iframe']")
    @IOSBy(xpath = "//*[@id='credential_picker_iframe']")
    private WrappedElement googleSignInPopupCloseIcon;

    public SearchPage(AppiumDriver driver) {
        super(driver);
    }

    @Step
    public SearchPage clickFilterButton() {
        switchToNativeView();
        filterIcon.click();
        return this;
    }

    @Step
    public SearchPage clickPersonalCheckbox() {
        personalCheckbox.click();
        return this;
    }

    @Step
    public SearchPage clickFirstImage() {
        allAssets.click();
        switchToWebView();
        return this;
    }

    @Step
    public SearchPage clickLikeButton() {
        likeBtn.click();
        return this;
    }

    @Step
    public SearchPage clickPlusAsset() {
        plusAssets.click();
        return this;
    }

    @Step
    public SearchPage closeGoogleSignInPopup() {
        switchToWebView();
        googleSignInPopupCloseIcon.sendKeys(Keys.ESCAPE);
        return this;
    }

    @Step
    public void goBackToSearchPage() {
        driver.navigate().back();
    }

    @Step
    public boolean isSignInPopupDisplayed() {
        return signInPopup.isDisplayed();
    }

    @Step
    public boolean isLikeButtonDisplayed() {
        return likeBtn.isDisplayed();
    }

    @Step
    public boolean isSaveButtonDisplayed() {
        return saveBtn.isDisplayed();
    }

    @Step
    public boolean isEditButtonDisplayed() {
        return editBtn.isDisplayed();
    }


}
