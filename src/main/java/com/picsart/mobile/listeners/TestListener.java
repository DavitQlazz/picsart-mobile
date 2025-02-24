package com.picsart.mobile.listeners;

import com.picsart.mobile.driver.DriverFactory;
import io.qameta.allure.Attachment;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;

@Slf4j
public class TestListener implements TestLifecycleListener {

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = "png")
    private byte[] saveScreenshot() {
        return DriverFactory.getDriver().getScreenshotAs(OutputType.BYTES);
    }


    @Override
    public void beforeTestStop(TestResult result) {
        TestLifecycleListener.super.beforeTestStop(result);
        log.info("Taking screenshot for test: {}", result.getName());
        saveScreenshot();
    }
}
