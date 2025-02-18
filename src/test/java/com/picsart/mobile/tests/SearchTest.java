package com.picsart.mobile.tests;


import com.picsart.mobile.base.BaseTest;
import com.picsart.mobile.pages.LandingPage;
import com.picsart.mobile.pages.SearchPage;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertTrue;

public class SearchTest extends BaseTest {
    SoftAssert softAssert = new SoftAssert();

    @Test
    public void picasrtSearchFilter() {
        LandingPage landingPage = new LandingPage(driver);
        SearchPage searchPage = new SearchPage(driver);

        landingPage.openUrl("https://picsart.com/search");
//        landingPage.acceptCookies();
        searchPage.clickFilterIcon()
                .clickPersonalCheckbox()
                .clickFilterIcon()
                .clickFirstImage();
        softAssert.assertTrue(searchPage.isLikeButtonDisplayed(), "Like button is not displayed");
        softAssert.assertTrue(searchPage.isSaveButtonDisplayed(), "Save button is not displayed");
        softAssert.assertTrue(searchPage.isEditButtonDisplayed(), "Edit button is not displayed");
        softAssert.assertAll();
        searchPage.clickLikeIcon()
                .goBackToSearchPage();
        searchPage.clickFilterIcon()
                .clickPersonalCheckbox()
                .clickFilterIcon()
                .clickPlusAsset();
        assertTrue(searchPage.isGooglePlayStoreDisplayed(), "Google Play Store is not displayed");

    }
}
