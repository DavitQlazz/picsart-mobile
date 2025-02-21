package com.picsart.mobile.element;

import org.openqa.selenium.By;
import org.openqa.selenium.support.AbstractFindByBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface IOSBy {
    String xpath() default "";

    class FindByBuilder extends AbstractFindByBuilder {
        @Override
        public By buildIt(Object annotation, Field field) {
            IOSBy findBy = (IOSBy) annotation;
            if (!"".equals(findBy.xpath())) {
                return By.xpath(findBy.xpath());
            }
            throw new IllegalArgumentException("No valid locator found in MobileFindBy annotation.");}
    }
}
