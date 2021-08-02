package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage {
    @FindBy(className = "note-title")
    private List<WebElement> noteTitles;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public int getNoteCount() {
        return noteTitles.size();
    }
}
