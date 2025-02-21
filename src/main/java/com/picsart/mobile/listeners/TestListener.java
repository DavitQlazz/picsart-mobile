package com.picsart.mobile.listeners;

import com.picsart.mobile.driver.DriverFactory;
import io.qameta.allure.Attachment;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;

public class TestListener extends AllureTestNg {

    @Override
    public void onTestFailure(ITestResult result) {
        saveScreenshot();
    }

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = "png")
    private byte[] saveScreenshot() {
        return DriverFactory.getDriver().getScreenshotAs(OutputType.BYTES);
    }
}
