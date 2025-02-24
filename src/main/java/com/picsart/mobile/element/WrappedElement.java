package com.picsart.mobile.element;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.Widget;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static com.picsart.mobile.conditions.MobileExpectedConditions.waitForElementVisibility;
import static com.picsart.mobile.driver.DriverFactory.config;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@Slf4j
public class WrappedElement extends Widget implements WebElement {
    private static final Duration DEFAULT_TIMEOUT = ofSeconds(config.longTimeout());
    private static final Duration POLLING_TIME = ofMillis(500);
    private final ElementLocator locator;
    private final WebDriverWait wait;
    private final AppiumDriver driver;
    private final WebElement element;


    public WrappedElement(WebElement element, ElementLocator locator, AppiumDriver driver) {
        super(element);
        this.element = element;
        this.locator = locator;
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT, POLLING_TIME);
    }


    @Override
    public void click() {
        WebElement el = getElement();
        log.info("Clicking element: {}", el);
        el.click();
    }

    public WebElement getElement() {
        return waitForElementVisibility(driver, element, DEFAULT_TIMEOUT, POLLING_TIME);
    }

    public WebElement getByIndex(final int index) {
        return locator.findElements().get(index);
    }

    public void clickIfExists() {
        if (isElementExists()) {
            locator.findElement().click();
        }
    }

    public Integer count() {
        return locator.findElements().size();
    }

    public boolean isElementExists() {
        try {
            wait.until(visibilityOf(locator.findElement()));
            return true;
        } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException ignored) {
            log.info("Element not found {}. Skipping click.", locator);
        }
        return false;
    }

    @Override
    public void submit() {

    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        element.sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        element.clear();
    }

    @Override
    public String getTagName() {
        return getElement().getTagName();
    }

    @Override
    public @Nullable String getAttribute(String name) {
        return getElement().getDomAttribute(name);
    }

    @Override
    public boolean isSelected() {
        return getElement().isSelected();
    }

    @Override
    public boolean isEnabled() {
        return getElement().isEnabled();
    }


    public boolean isClickable() {
        return wait.until(elementToBeClickable(getElement())).isDisplayed();
    }

    @Override
    public String getText() {
        return getElement().getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        ExpectedCondition<List<WebElement>> until = wait.until(driver ->
                numberOfElementsToBeMoreThan(by, 0));
        return until.apply(driver);
    }

    @Override
    public WebElement findElement(By by) {
        return driver.findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        return getElement().isDisplayed();
    }

    @Override
    public Point getLocation() {
        return getElement().getLocation();
    }

    @Override
    public Dimension getSize() {
        return getElement().getSize();
    }

    @Override
    public Rectangle getRect() {
        return getElement().getRect();
    }

    @Override
    public String getCssValue(String propertyName) {
        return Objects.requireNonNull(getElement().getDomProperty(propertyName));
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return null;
    }
}
