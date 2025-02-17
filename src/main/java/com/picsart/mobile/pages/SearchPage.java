package com.picsart.mobile.pages;

import com.picsart.mobile.element.WrappedElement;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.FindBy;

public class SearchPage extends BasePage {

    @FindBy(xpath = "//*[@resource-id='filter_icon']")
    private WrappedElement filterIcon;

    @FindBy(xpath = "//*[@text='licenses-Personal-checkbox']")
    private WrappedElement personalCheckbox;

    @FindBy(xpath = "//*[starts-with(@resource-id, 'base_card_item')]")
    private WrappedElement allAssets;

    @FindBy(xpath = "//*[starts-with(@resource-id, 'base_card_item')]/android.view.View/android.widget.Image")
    private WrappedElement plusAssets;

    @FindBy(xpath = "//android.view.View[@resource-id=\"__next\"]" +
            "/android.view.View[2]/android.view.View/android.view.View[1]/android.widget.Button")
    private WrappedElement likeBtn;

    @FindBy(xpath = "//android.view.View[@resource-id=\"__next\"]" +
            "/android.view.View[2]/android.view.View/android.view.View[2]/android.widget.Button")
    private WrappedElement saveBtn;

    @FindBy(xpath = "//android.widget.Button[@text='Edit']")
    private WrappedElement editBtn;

    @FindBy(xpath = "//android.widget.Button[starts-with(@text, 'Sign in options title Sign in options description')]")
    private WrappedElement signInPopup;

    public SearchPage(AppiumDriver driver) {
        super(driver);
    }

    public SearchPage clickFilterIcon() {
        filterIcon.click();
        return this;
    }

    public SearchPage clickPersonalCheckbox() {
        personalCheckbox.click();
        return this;
    }

    public SearchPage clickFirstImage() {
        allAssets.getAll().getFirst().click();
        return this;
    }

    public SearchPage clickLikeIcon() {
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
}
