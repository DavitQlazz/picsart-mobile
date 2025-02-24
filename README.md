# Picsart Mobile Automation Framework

This is a Mobile Automation Framework aimed to mobile web testing (hybrid: Native and Webview) using **Appium, TestNG, and Allure Reporting**. It supports both **Android** and **iOS** testing, with execution on **local devices** and **cloud platforms** (BrowserStack).

## Features
- Supports **Android** and **iOS** platforms
- Supports **local execution** and **cloud execution**
- Uses **TestNG** for test execution
- **Allure Reporting** for test results visualization
- **Dynamic configuration** using `OWNER` library

## Prerequisites
- Install **JDK 19**
- Install **Maven**
- Install **Node.js** (for Appium server)
- Install **Appium**
- Set up **Android SDK** (for Android testing)
- Set up **Xcode** (for iOS testing)

## Note
- Created wrapper decorator for Appium page factory
- Using `@AndroidBy` and `@IOSBy` annotations
- Using xpath locators for both Android and iOS

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/DavitQlazz/picsart-mobile
   cd picsart-mobile
   ```

## Running Tests
### Local Execution
Run tests on a local device:
```sh
mvn test -Dplatform=android
mvn test -Dplatform=ios
```

### Cloud Execution (BrowserStack)
```sh
mvn test -DrunMode=cloud -Dplatform=android
mvn test -DrunMode=cloud -Dplatform=ios
```

## Generating Allure Reports
1. Run tests to generate the Allure results:
   ```sh
   mvn test
   ```
2. Serve the Allure report:
   ```sh
   mvn allure:serve
   ```

## Driver Management
The framework uses `DriverFactory` to initialize and manage Appium drivers.

- **Android Driver** uses `UiAutomator2Options`
- **iOS Driver** uses `XCUITestOptions`
- **Cloud Execution** uses BrowserStack capabilities


## Logging & Reporting
- Uses **SLF4J** for logging
- Generates **Allure Reports** for detailed test insights

