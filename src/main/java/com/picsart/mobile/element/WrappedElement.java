package com.picsart.mobile.element;

import io.appium.java_client.AppiumDriver;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class WrappedElement implements WebElement {
    private final ElementLocator locator;
    private final WebDriverWait wait;
    private final AppiumDriver driver;
    private final String xpath;
    private WebElement cachedElement;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration POLLING_TIME = Duration.ofMillis(500);
    private static final int MAX_RETRIES = 2;

    public WrappedElement(ElementLocator locator, AppiumDriver driver, String xpath) {
        this.locator = locator;
        this.driver = driver;
        this.xpath = xpath;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT, POLLING_TIME);
    }

//    private WebElement getElement() {
//        return wait.until(numberOfElementsToBeMoreThan(By.xpath(xpath), 0)).getFirst();
//    }

    private WebElement getElement() {
            try {
                for (int attempt = 0; attempt <= MAX_RETRIES; attempt++) {
                    try {
                        // Get fresh element reference to avoid stale element exception
                        cachedElement = wait.until(refreshed(presenceOfElementLocated(By.xpath(xpath))));
                        return cachedElement;
                    } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                        if (attempt == MAX_RETRIES) throw e;
                        resetCachedElement();
                    }
                }
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException(
                        "Cannot find element with locator: " + xpath, e);
            }
        return cachedElement;
    }

    private void resetCachedElement() {
        cachedElement = null;
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
        scrollIntoView(getElement());
    }

    @Override
    public void click() {
        getElement().click();
    }

    public WebElement getByIndex(final int index) {
        getElement();
        return locator.findElements().get(index);
    }

    public void clickIfExists() {
        if (isElementExists()) {
            locator.findElement().click();
        }
    }

    public Integer count() {
        getElement();
        return locator.findElements().size();
    }

    public boolean isElementExists() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(numberOfElementsToBeMoreThan(By.xpath(xpath), 0));
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
        getElement().sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        getElement().clear();
    }

    @Override
    public String getTagName() {
        return getElement().getTagName();
    }

    @Override
    public @Nullable String getAttribute(String name) {
        return "";
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
        getElement();
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
        getElement();
        return driver.findElement(by);
    }

    @Override
    public boolean isDisplayed() {
        return wait.until(visibilityOfElementLocated(By.xpath(xpath))).isDisplayed();
    }

    @Override
    public Point getLocation() {
        return locator.findElement().getLocation();
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
