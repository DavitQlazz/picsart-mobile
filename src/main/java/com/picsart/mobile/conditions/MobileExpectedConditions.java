package com.picsart.mobile.conditions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

@Slf4j
public class MobileExpectedConditions {

    // Prevent instantiation
    private MobileExpectedConditions() {
    }

    /**
     * Wait for element to be present and have specific text
     */
    public static ExpectedCondition<Boolean> textToBePresentInElement(By locator, String text) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement element = driver.findElement(locator);
                    String elementText = element.getText();
                    return elementText.contains(text);
                } catch (StaleElementReferenceException | NoSuchElementException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("text ('%s') to be present in element located by %s", text, locator);
            }
        };
    }

    public static ExpectedCondition<Boolean> isAndroidActivityEquals(String activityName) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                if (!(driver instanceof AndroidDriver androidDriver)) {
                    log.error("Expected AndroidDriver but got: {}", driver.getClass().getSimpleName());
                    throw new IllegalStateException("This condition can only be used with an AndroidDriver.");
                }

                String currentActivity = androidDriver.currentActivity();
                log.info("Current activity: {}", currentActivity);

                boolean isExpectedActivity = currentActivity != null && currentActivity.contains(activityName);
                if (isExpectedActivity) {
                    log.info("App activity matches: {}", currentActivity);
                }
                return isExpectedActivity;
            }

            @Override
            public String toString() {
                return "Activity to contain '" + activityName;
            }
        };
    }


    /**
     * Wait for an iOS app to be in a specific state.
     *
     * @param bundleId the bundle id of the app
     * @param state    the state to wait for
     * @return an ExpectedCondition that returns true if the app is in the given state
     */
    public static ExpectedCondition<Boolean> isIosBundleStateEquals(String bundleId, String state) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                if (!(driver instanceof IOSDriver iosDriver)) {
                    log.error("Expected IOSDriver but got: {}", driver.getClass().getSimpleName());
                    throw new IllegalStateException("AppBackgroundSuspendedCondition can only be used with an IOSDriver.");
                }

                ApplicationState state = iosDriver.queryAppState(bundleId);
                log.info("Current state of app {}: {}", bundleId, state.name());

                boolean isSuspended = state == ApplicationState.RUNNING_IN_BACKGROUND_SUSPENDED;
                if (isSuspended) {
                    log.info("App {} is now in {} state.", bundleId, state);
                }
                return isSuspended;
            }

            @Override
            public String toString() {
                return "App '" + bundleId + "' to be in" + state + " state.";
            }
        };

    }

    /**
     * Wait for element to have specific attribute value
     */
    public static ExpectedCondition<Boolean> attributeToBe(By locator, String attribute, String value) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String elementAttribute = driver.findElement(locator).getDomAttribute(attribute);
                    return elementAttribute != null && elementAttribute.equals(value);
                } catch (StaleElementReferenceException | NoSuchElementException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("element located by %s to have attribute '%s' with value '%s'", locator, attribute, value);
            }
        };
    }

    /**
     * Wait for element to be present and visible in viewport
     */
    public static ExpectedCondition<Boolean> elementToBeVisibleInViewport(By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement element = driver.findElement(locator);
                    Rectangle rect = element.getRect();
                    Dimension viewport = driver.manage().window().getSize();

                    return rect.y >= 0 && rect.y + rect.height <= viewport.height && rect.x >= 0 && rect.x + rect.width <= viewport.width;
                } catch (StaleElementReferenceException | NoSuchElementException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("element located by %s to be visible in viewport", locator);
            }
        };
    }

    /**
     * Wait for element to be clickable and not obscured
     */
    public static ExpectedCondition<WebElement> elementToBeClickableAndNotObscured(By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(WebDriver driver) {
                try {
                    WebElement element = ExpectedConditions.elementToBeClickable(locator).apply(driver);
                    if (element == null) return null;

                    // Check if element is obscured by other elements
                    Point location = element.getLocation();
                    Dimension size = element.getSize();
                    Point center = new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight() / 2);

                    // Use JavaScript to check if element at point is our target element
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    WebElement elementAtPoint = (WebElement) js.executeScript("return document.elementFromPoint(arguments[0], arguments[1]);", center.getX(), center.getY());

                    return element.equals(elementAtPoint) ? element : null;
                } catch (StaleElementReferenceException | NoSuchElementException e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return String.format("element located by %s to be clickable and not obscured", locator);
            }
        };
    }

    /**
     * Wait for alert message to be present with specific text
     */
    public static ExpectedCondition<Boolean> alertToBePresent(String expectedText) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    Alert alert = driver.switchTo().alert();
                    return alert.getText().contains(expectedText);
                } catch (NoAlertPresentException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("alert to be present with text '%s'", expectedText);
            }
        };
    }

    /**
     * Wait for network idle (no active network requests)
     */
    public static ExpectedCondition<Boolean> networkToBeIdle(AppiumDriver driver) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                try {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    return (Boolean) js.executeScript("return window.navigator.onLine && " + "window.performance.getEntriesByType('resource').length === 0;");
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "network to be idle";
            }
        };
    }

    /**
     * Wait for element to have specific attribute containing value
     */
    public static ExpectedCondition<Boolean> attributeToContain(By locator, String attribute, String value) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String elementAttribute = driver.findElement(locator).getDomAttribute(attribute);
                    return elementAttribute != null && elementAttribute.contains(value);
                } catch (StaleElementReferenceException | NoSuchElementException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("element located by %s to have attribute '%s' containing '%s'", locator, attribute, value);
            }
        };
    }

    /**
     * Wait for list of elements to have specific size
     */
    public static ExpectedCondition<Boolean> numberOfElementsToBe(By locator, int expectedSize) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    List<WebElement> elements = driver.findElements(locator);
                    return elements.size() == expectedSize;
                } catch (WebDriverException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("number of elements located by %s to be %d", locator, expectedSize);
            }
        };
    }

    /**
     * Wait for element to be present and have enabled state
     */
    public static ExpectedCondition<Boolean> elementToBeEnabled(By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return driver.findElement(locator).isEnabled();
                } catch (StaleElementReferenceException | NoSuchElementException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("element located by %s to be enabled", locator);
            }
        };
    }

    /**
     * Wait for element to become stale (no longer attached to DOM)
     */
    public static ExpectedCondition<Boolean> elementToBeStale(WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    // If element is still in DOM, this will succeed
                    element.isEnabled();
                    return false;
                } catch (StaleElementReferenceException e) {
                    return true;
                }
            }

            @Override
            public String toString() {
                return "element to become stale";
            }
        };
    }
}
