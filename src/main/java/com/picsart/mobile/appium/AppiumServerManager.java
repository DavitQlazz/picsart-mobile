package com.picsart.mobile.appium;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;

@Slf4j
public class AppiumServerManager {
    private static AppiumDriverLocalService service;

    public static void startServer() {
        service = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .withAppiumJS(new File(getAppiumFilePath()))
                .withArgument(GeneralServerFlag.ALLOW_INSECURE, "chromedriver_autodownload")
                .withArgument(GeneralServerFlag.RELAXED_SECURITY)
                .withArgument(GeneralServerFlag.USE_DRIVERS, "chromium,uiautomator2,xcuitest")
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_LEVEL, "info")
                .withTimeout(Duration.ofSeconds(30))
                .usingAnyFreePort()
                .build();
//        service.start();
    }

    public static void stopServer() {
        if (service != null) {
            service.stop();
            System.out.println("Appium Server Stopped");
        }
    }

    public static String getAppiumServerUrl() {
        if (service.isRunning()) {
            return service.getUrl().toString();
        }
        return "http://127.0.0.1:4723/";
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
