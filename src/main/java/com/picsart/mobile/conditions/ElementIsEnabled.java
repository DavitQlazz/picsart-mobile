package com.picsart.mobile.conditions;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ElementIsEnabled implements ExpectedCondition<Boolean> {
    private final WebElement element;

    public ElementIsEnabled(WebElement element) {
        this.element = element;
    }

    @Override
    public Boolean apply(org.openqa.selenium.WebDriver driver) {
        return element.isEnabled();
    }
}
