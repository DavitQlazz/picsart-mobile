package com.picsart.mobile.base;

import com.picsart.mobile.appium.AppiumServerManager;
import com.picsart.mobile.driver.DriverFactory;
import com.picsart.mobile.listeners.TestListener;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.*;

@Listeners(TestListener.class)
public class BaseTest {
    protected AppiumDriver driver;

    @BeforeTest
    public void startAppiumServer() {
        AppiumServerManager.startServer();
    }

    @BeforeMethod
    public void setup() {
        driver = DriverFactory.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    @AfterTest(alwaysRun = true)
    public void stopAppiumServer() {
        AppiumServerManager.stopServer();
    }
}
