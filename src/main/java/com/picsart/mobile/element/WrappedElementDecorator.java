package com.picsart.mobile.element;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumElementLocatorFactory;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.List;


public class WrappedElementDecorator extends AppiumFieldDecorator {
    private final AppiumDriver driver;
    private final String platform;
    private final WrappedElementByBuilder defaultBuilder;

    public WrappedElementDecorator(AppiumDriver driver) {
        super(driver, Duration.ofSeconds(30));
        this.driver = driver;
        String platform = driver.getCapabilities().getPlatformName().name();
        String automationName = driver.getCapabilities().getCapability("automationName").toString();
        this.defaultBuilder = new WrappedElementByBuilder(platform, automationName);
        this.platform = driver.getCapabilities().getPlatformName().name();
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!field.isAnnotationPresent(AndroidBy.class)) {
            return null;  // Ignore fields without the custom annotation
        }

        defaultBuilder.setAnnotated(field);
        AppiumElementLocatorFactory factory = new AppiumElementLocatorFactory(driver, Duration.ofSeconds(30), defaultBuilder);

        AndroidBy annotation = field.getAnnotation(AndroidBy.class);
        By by = getBySelector(annotation);
        ElementLocator elementLocator = factory.createLocator(field);
        if (WebElement.class.isAssignableFrom(field.getType())) {
            // Handling single element
            InvocationHandler handler = new LocatingElementHandler(elementLocator);
            WebElement element = (WebElement) Proxy.newProxyInstance(
                    loader, new Class<?>[]{WebElement.class}, handler);
            return new WrappedElement(element, elementLocator, driver);
        } else if (List.class.isAssignableFrom(field.getType())) {
            // Handling list of elements
            InvocationHandler handler = new LocatingElementListHandler(elementLocator);
            return Proxy.newProxyInstance(loader, new Class<?>[]{List.class}, handler);
        }

        return null;
    }

    private By getBySelector(AndroidBy annotation) {
        if (platform.equalsIgnoreCase("ios") && !annotation.xpath().isEmpty()) {
            return By.xpath(annotation.xpath());
        } else if (platform.equalsIgnoreCase("android") && !annotation.xpath().isEmpty()) {
            return By.xpath(annotation.xpath());
        } else if (!annotation.xpath().isEmpty()) {
            return By.xpath(annotation.xpath());
        }
        throw new IllegalArgumentException("No valid locator found in MobileFindBy annotation.");
    }
}

