package com.picsart.mobile.element;

import io.appium.java_client.pagefactory.DefaultElementByBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

public class WrappedElementByBuilder extends DefaultElementByBuilder {
    public WrappedElementByBuilder(String platform, String automation) {
        super(platform, automation);
    }

    @Override
    protected By buildMobileNativeBy() {
        AnnotatedElement annotatedElement = annotatedElementContainer.getAnnotated();
        By defaultBy = null;
        AndroidBy androidBy = annotatedElement.getAnnotation(AndroidBy.class);
        if (androidBy != null && platform.equalsIgnoreCase(Platform.ANDROID.name())) {
            defaultBy = new AndroidBy.FindByBuilder().buildIt(androidBy, (Field) annotatedElement);
        }

        IOSBy iosBy = annotatedElement.getAnnotation(IOSBy.class);
        if (iosBy != null && platform.equalsIgnoreCase(Platform.IOS.name())) {
            defaultBy = new IOSBy.FindByBuilder().buildIt(iosBy, (Field) annotatedElement);
        }
        return defaultBy;
    }

    @Override
    protected By buildDefaultBy() {
        AnnotatedElement annotatedElement = annotatedElementContainer.getAnnotated();
        By defaultBy = null;
        AndroidBy androidBy = annotatedElement.getAnnotation(AndroidBy.class);
        if (androidBy != null && platform.equalsIgnoreCase(Platform.ANDROID.name())) {
            defaultBy = new AndroidBy.FindByBuilder().buildIt(androidBy, (Field) annotatedElement);
        }

        IOSBy iosBy = annotatedElement.getAnnotation(IOSBy.class);
        if (iosBy != null && platform.equalsIgnoreCase(Platform.IOS.name())) {
            defaultBy = new IOSBy.FindByBuilder().buildIt(iosBy, (Field) annotatedElement);
        }
        return defaultBy;
    }
}
