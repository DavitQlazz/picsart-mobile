package com.picsart.mobile.element;

import io.appium.java_client.AppiumDriver;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class WrappedElement extends RemoteWebElement implements WebElement {
    private final ElementLocator locator;
    private final WebDriverWait wait;
    private final AppiumDriver driver;
    private final WebElement element;
    private final By by;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration POLLING_TIME = Duration.ofMillis(500);

    
    public WrappedElement(WebElement element, ElementLocator locator, AppiumDriver driver, By by) {
        this.element = element;
        this.locator = locator;
        this.driver = driver;
        this.by = by;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT, POLLING_TIME);
    }

    private WebElement getElement() {
        WebElement cachedElement;
            try {
                cachedElement = wait.until(refreshed(presenceOfElementLocated(by)));
                return cachedElement;
            } catch (StaleElementReferenceException | ElementClickInterceptedException | NoSuchElementException e) {
                throw new NoSuchElementException("Cannot find element with locator: " + by, e);
            }
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
        element.click();
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
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(numberOfElementsToBeMoreThan(by, 0));
            return true;
        } catch (TimeoutException ignored) {
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
        return wait.until(visibilityOfElementLocated(by)).isDisplayed();
    }
}
