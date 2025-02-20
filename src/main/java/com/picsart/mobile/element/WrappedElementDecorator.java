package com.picsart.mobile.element;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumElementLocatorFactory;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.DefaultElementByBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.time.Duration;


public class WrappedElementDecorator extends AppiumFieldDecorator {
    private final AppiumDriver driver;
    private final AppiumElementLocatorFactory factory;
    private final DefaultElementByBuilder defaultBuilder;


    public WrappedElementDecorator(AppiumDriver driver) {
        super(driver, Duration.ofSeconds(30));
        this.driver = driver;
        String platform = driver.getCapabilities().getPlatformName().name();
        String automationName = driver.getCapabilities().getCapability("automationName").toString();
        this.defaultBuilder = new DefaultElementByBuilder(platform, automationName);
        this.factory = new AppiumElementLocatorFactory(driver, Duration.ofSeconds(30), defaultBuilder);
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!WebElement.class.isAssignableFrom(field.getType())) {
            return null;
        }
        defaultBuilder.setAnnotated(field);
        By by = defaultBuilder.buildBy();

        ElementLocator elementLocator = factory.createLocator(field);
        LocatingElementHandler locatingElementHandler = new LocatingElementHandler(elementLocator);
        WebElement element = (WebElement) Proxy.newProxyInstance(
                loader,
                new Class<?>[]{WebElement.class},
                locatingElementHandler
        );
        return new WrappedElement(element,elementLocator, driver, by);
    }
}
