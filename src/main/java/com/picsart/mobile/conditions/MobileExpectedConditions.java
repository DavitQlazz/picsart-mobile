package com.picsart.mobile.conditions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

@Slf4j
public class MobileExpectedConditions {

    // Prevent instantiation
    private MobileExpectedConditions() {
    }


    /**
     * Wait for the current Android activity to match the specified activity name.
     *
     * @param activityName The name of the activity to match.
     * @return An ExpectedCondition that returns true if the current activity
     * contains the specified activity name.
     * @throws IllegalStateException if the driver is not an instance of AndroidDriver.
     */
    public static ExpectedCondition<Boolean> isAndroidActivityEquals(String activityName) {
        return new ExpectedCondition<>() {
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
     * Wait for the element to be visible. If the element is not visible,
     * this method will wait until the element is visible or the timeout
     * is reached.
     *
     * @param driver     the driver to use
     * @param element    the element to wait for
     * @param timeout    the timeout to wait
     * @param pollingTime the polling time to wait
     * @return the visible element
     */
    public static WebElement waitForElementVisibility(AppiumDriver driver, WebElement element, Duration timeout, Duration pollingTime) {
        FluentWait<AppiumDriver> wait = new FluentWait<>(driver)
                .withTimeout(timeout)
                .pollingEvery(pollingTime)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class)
                .withMessage("Element not found: " + element);

        return wait.until(d -> {
            try {
                WebElement el = ExpectedConditions.visibilityOf(element).apply(d);
                log.info("Element found: {}", element);
                return el;
            } catch (StaleElementReferenceException | TimeoutException e) {
                log.info("Element not found: {}", element);
                return null; // Returning null forces FluentWait to retry
            }
        });
    }

}
