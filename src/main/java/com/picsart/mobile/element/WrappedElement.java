package com.picsart.mobile.element;

import io.appium.java_client.AppiumDriver;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class WrappedElement implements WebElement {
    private final ElementLocator locator;
    private final WebDriverWait wait;
    private final AppiumDriver driver;
    private final String xpath;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration POLLING_TIME = Duration.ofMillis(500);

    public WrappedElement(ElementLocator locator, AppiumDriver driver, String xpath) {
        this.locator = locator;
        this.driver = driver;
        this.xpath = xpath;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT, POLLING_TIME);
    }

    private void clickWithRetry() {
        try {
            waitForElement();
            WebElement element = wait.until(elementToBeClickable(locator.findElement()));
            element.click();
        } catch (ElementClickInterceptedException e) {
            throw new RuntimeException("Failed to click element after retries");
        }
    }

    private WebElement waitForElement() {
        return wait.until(numberOfElementsToBeMoreThan(By.xpath(xpath), 0)).getFirst();
    }

    private boolean isDisplayed(WebElement element) {
        try {
            return wait.until(visibilityOf(element)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    private String getTextWithWait(WebElement element) {
        wait.until(visibilityOf(element));
        return element.getText();
    }

    private void scrollIntoView(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript(
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
        scrollIntoView(waitForElement());
    }

    @Override
    public void click() {
        clickWithRetry();
    }

    public WebElement getByIndex(final int index) {
        waitForElement();
        return locator.findElements().get(index);
    }

    public void clickIfExists() {
        if (isElementExists()) {
            locator.findElement().click();
        }
    }

    public Integer count() {
        waitForElement();
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
        waitForElement().sendKeys(keysToSend);
    }

    @Override
    public void clear() {
        waitForElement().clear();
    }

    @Override
    public String getTagName() {
        return waitForElement().getTagName();
    }

    @Override
    public @Nullable String getAttribute(String name) {
        return "";
    }

    @Override
    public boolean isSelected() {
        return waitForElement().isSelected();
    }

    @Override
    public boolean isEnabled() {
        return waitForElement().isEnabled();
    }

    @Override
    public String getText() {
        waitForElement();
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
        waitForElement();
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
