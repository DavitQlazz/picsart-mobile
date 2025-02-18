package com.picsart.mobile.element;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.time.Duration;


public class WrappedElementDecorator extends AppiumFieldDecorator {
    private final AppiumDriver driver;
    private final AppiumElementLocatorFactory factory;
    private final String platform;
    private final String automationName;

    public WrappedElementDecorator(AppiumDriver driver) {
        super(driver);
        this.driver = driver;
        this.platform = driver.getCapabilities().getPlatformName().name();
        this.automationName = driver.getCapabilities().getCapability("automationName").toString();
        this.factory = new AppiumElementLocatorFactory(driver, Duration.ofSeconds(30),
                new DefaultElementByBuilder(platform, automationName));
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!WebElement.class.isAssignableFrom(field.getType())) {
            return null;
        }
        String by;
        if (platform.equalsIgnoreCase("ios")) {
            by = field.getAnnotation(iOSXCUITFindBy.class).xpath();
        } else {
            by = field.getAnnotation(AndroidFindBy.class).xpath();
        }

        ElementLocator locator = factory.createLocator(field);
        return new WrappedElement(locator, driver, by);
    }
}
