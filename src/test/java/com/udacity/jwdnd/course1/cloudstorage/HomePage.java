package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage {
    @FindBy(className = "note-title")
    private List<WebElement> noteTitles;

    private final WebDriver driver;
    @FindBy(id = "note-title")
    private WebElement noteTitle;
    @FindBy(id = "note-description")
    private WebElement noteDescription;
    @FindBy(id = "note-submit")
    private WebElement noteSubmit;
    @FindBy(id = "note-add")
    private WebElement noteAdd;
    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public int getNoteCount() {
        return noteTitles.size();
    }

    public void createNote(String title, String text) {
        this.notesTab.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.noteAdd.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.noteTitle.sendKeys(title);
        this.noteDescription.sendKeys(text);
        this.noteSubmit.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
