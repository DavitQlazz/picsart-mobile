package com.picsart.mobile.appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;

import static com.picsart.mobile.driver.DriverFactory.config;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.*;

@Slf4j
public class AppiumServerManager {
    private static AppiumDriverLocalService service;

    public static void startServer() {
        if (config.runMode().equals("local")) {
            service = new AppiumServiceBuilder()
                    .withIPAddress("127.0.0.1")
                    .withAppiumJS(new File(getAppiumFilePath()))
                    .withArgument(ALLOW_INSECURE, "chromedriver_autodownload")
                    .withArgument(RELAXED_SECURITY)
                    .withArgument(USE_DRIVERS, "chromium,uiautomator2,xcuitest")
                    .withArgument(SESSION_OVERRIDE)
                    .withArgument(LOG_LEVEL, "error")
                    .withTimeout(Duration.ofSeconds(config.longTimeout()))
                    .usingAnyFreePort()
                    .build();
            service.start();
            if (service != null && service.isRunning()) {
                log.info("Appium Server Started at: {}", service.getUrl());
            }
        }
    }

    public static void stopServer() {
        if (service != null) {
            service.stop();
            System.out.println("Appium Server Stopped");
        }
    }

    public static String getAppiumServerUrl() {
        if (service != null && service.isRunning()) {
            return service.getUrl().toString();
        }
        return config.localServer();
    }

    private static String getAppiumFilePath() {
        ProcessBuilder builder = new ProcessBuilder("which", "appium");
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine().trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
