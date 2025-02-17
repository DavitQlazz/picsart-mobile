package com.picsart.mobile.element;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;


public class WrappedElementDecorator extends AppiumFieldDecorator {
    private final AppiumDriver driver;
    private final DefaultElementLocatorFactory factory;

    public WrappedElementDecorator(AppiumDriver driver) {
        super(driver);
        this.driver = driver;
        this.factory = new DefaultElementLocatorFactory(driver);
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!WebElement.class.isAssignableFrom(field.getType())) {
            return null;
        }

        ElementLocator locator = factory.createLocator(field);
        return new WrappedElement(locator, driver, field.getAnnotation(FindBy.class).xpath());
    }
}
