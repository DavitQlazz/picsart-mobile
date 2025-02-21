package com.picsart.mobile.element;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.Widget;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

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


    private String getTextWithWait(WebElement element) {
        wait.until(visibilityOf(element));
        return element.getText();
    }

    private void scrollIntoView(WebElement element) {
        try {
            driver.executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            Thread.sleep(500);
        } catch (Exception e) {
            // Ignore scroll errors
        }
    }

    // Utility methods
    public void waitForAttributeValue(String attribute, String value) {
        wait.until(driver -> {
            WebElement element = locator.findElement();
            return Objects.equals(element.getDomAttribute(attribute), value);
        });
    }

    public void waitForTextPresent(String text) {
        wait.until(driver -> {
            WebElement element = locator.findElement();
            return element.getText().contains(text);
        });
    }

    public boolean waitForElementToDisappear() {
        try {
            wait.until(invisibilityOf(locator.findElement()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollTo() {
        scrollIntoView(element);
    }

    @Override
    public void click() {
        log.info("Click if Exists");
        getElement().click();
    }

    public WebElement getElement() {
        FluentWait<AppiumDriver> wait = new FluentWait<>(driver)
                .withTimeout(DEFAULT_TIMEOUT)
                .pollingEvery(POLLING_TIME)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class);

        return wait.until(driver -> {
            try {
                WebElement el = visibilityOf(element).apply(driver);
                log.info("Element found: {}", element);
                return el;
            } catch (StaleElementReferenceException e) {
                log.info("Encountered StaleElementReferenceException. Retrying...");
                return null; // Returning null forces FluentWait to retry
            }
        });
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
            wait.until(elementToBeClickable(locator.findElement()));
            return true;
        } catch (TimeoutException | NoSuchElementException ignored) {
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
        return element.getTagName();
    }

    @Override
    public @Nullable String getAttribute(String name) {
        return "";
    }

    @Override
    public boolean isSelected() {
        return element.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return element.isEnabled();
    }


    public boolean isClickable() {
        return wait.until(elementToBeClickable(element)).isDisplayed();
    }

    @Override
    public String getText() {
        return getTextWithWait(locator.findElement());
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
        return null;
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Rectangle getRect() {
        return null;
    }

    @Override
    public String getCssValue(String propertyName) {
        return "";
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return null;
    }
}
