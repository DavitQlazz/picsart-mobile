package com.picsart.mobile.tests;


import com.picsart.mobile.base.BaseTest;
import com.picsart.mobile.pages.LandingPage;
import com.picsart.mobile.pages.SearchPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchTest extends BaseTest {

    @Test
    public void picasrtSearchFilter() {
        LandingPage landingPage = new LandingPage(driver);
        SearchPage searchPage = new SearchPage(driver);

        landingPage.openUrl("https://picsart.com/search");
        landingPage.acceptCookiesIfExists();
        searchPage.clickFilterIcon();
        searchPage.clickPersonalCheckbox();
        searchPage.clickFilterIcon();
        searchPage.clickFirstImage();
        searchPage.clickLikeIcon();
        searchPage.goBackToSearchPage();
        searchPage.clickFilterIcon();
        searchPage.clickPersonalCheckbox();
        searchPage.clickPlusAsset();
        Assert.assertTrue(true);

    }
}
