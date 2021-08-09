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
    public WebElement noteTitle;
    @FindBy(id = "note-description")
    public WebElement noteDescription;
    @FindBy(id = "note-submit")
    public WebElement noteSubmit;
    @FindBy(id = "note-add")
    public WebElement noteAdd;
    @FindBy(id = "nav-notes-tab")
    private WebElement notesTab;
    @FindBy(id = "userTable")
    public WebElement notesTable;
    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTab;
    @FindBy(id = "nav-files-tab")
    private WebElement filesTab;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public List<WebElement> getNoteTitles() {
        return noteTitles;
    }

    public int getNoteCount() {
        return noteTitles.size();
    }

    public void activateTab(String tab) {
        if (tab == "notes") {
            notesTab.click();
        } else if (tab == "files") {
            filesTab.click();
        } else if (tab == "credentials") {
            credentialsTab.click();
        }
    }


}
