package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseURL;
    @Autowired
    private UserService userService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private CredentialService credentialService;
    private User user;
    private int noteId;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        baseURL = "http://localhost:" + port;
        CreateUserAndNote();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
        noteService.deleteAllNotes(user.getUserId());
    }

    @Test
    public void unAuthorized() {
        driver.get(baseURL + "/home");
        assertEquals("Login", driver.getTitle());
    }

    @Test
    public void createCredential() {
        login();
        HomePage homePage = new HomePage(driver);
        homePage.activateTab("credentials");
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement addButton = driver.findElement(By.id("credential-add"));
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        WebElement url = driver.findElement(By.id("credential-url"));
        wait.until(ExpectedConditions.elementToBeClickable(url));
        url.sendKeys("www.java.com");
        driver.findElement(By.id("credential-username")).sendKeys("user123");
        driver.findElement(By.id("credential-password")).sendKeys("secret");
        driver.findElement(By.id("credential-submit")).click();


        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("credentialTable"))));
        List<WebElement> webElements = driver.findElements(By.className("credential-url"));
        int lastPosition = webElements.size() - 1;
        assertEquals("www.java.com", webElements.get(lastPosition).getText());
        assertEquals("user123", driver.findElements(By.className("credential-username")).get(lastPosition).getText());
        assertEquals("*********", driver.findElements(By.className("credential-password")).get(lastPosition).getText());
    }

    @Test
    public void createNote() {
        String title = "noteTitle";
        String description = "description";
        login();
        HomePage homePage = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 30);
        homePage.activateTab("notes");

        wait.until(ExpectedConditions.elementToBeClickable(homePage.noteAdd));
        int noteCount = homePage.getNoteCount();
        homePage.noteAdd.click();
        wait.until(ExpectedConditions.elementToBeClickable(homePage.noteTitle));
        homePage.noteTitle.sendKeys(title);
        homePage.noteDescription.sendKeys(description);
        homePage.noteSubmit.click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.className("alert-success"))));

        assertEquals(title, driver.findElements(By.className("note-title")).get(noteCount).getText());
        assertEquals(description, driver.findElements(By.className("note-text")).get(noteCount).getText());

    }

    @Test
    public void updateCredential() {
        login();
        HomePage homePage = new HomePage(driver);
        homePage.activateTab("credentials");
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement editButton = driver.findElement(By.className("credential-edit"));
        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
        WebElement url = driver.findElement(By.id("credential-url"));
        wait.until(ExpectedConditions.elementToBeClickable(url));
        url.clear();
        url.sendKeys("www.test.com");
        WebElement username = driver.findElement(By.id("credential-username"));
        username.clear();
        username.sendKeys("otherUser");
        driver.findElement(By.id("credential-submit")).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.className("credential-url"))));
        assertEquals("www.test.com", driver.findElement(By.className("credential-url")).getText());
        assertEquals("otherUser", driver.findElement(By.className("credential-username")).getText());
    }

    @Test
    public void deleteCredential() {
        login();
        HomePage homePage = new HomePage(driver);
        homePage.activateTab("credentials");
        WebDriverWait wait = new WebDriverWait(driver, 30);
        WebElement deleteButton = driver.findElement(By.className("credential-delete"));
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        int count = driver.findElements(By.className("credential-url")).size();
        deleteButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.className("alert-success"))));
        assertEquals(count - 1, driver.findElements(By.className("credential-url")).size());
    }

    private void login() {
        login(user.getUsername(), user.getPassword());
    }

    @Test
    public void editNote() {
        login();
        HomePage homePage = new HomePage(driver);
        assertEquals(1, homePage.getNoteCount());
        editNote(noteId, "newTitle", "newText");
        assertEquals("newTitle", homePage.getNoteTitles().get(0).getText());
        assertEquals(1, homePage.getNoteCount());
    }

    @Test
    public void deleteNote() {
        login();
        HomePage homePage = new HomePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, 30);
        homePage.activateTab("notes");

        WebElement deleteButton = homePage.notesTable.findElement(By.tagName("a"));
        int noteCount = homePage.getNoteCount();
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
        assertEquals(noteCount - 1, homePage.getNoteCount());
    }

    private void login(String username, String password) {
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
    }

    public void editNote(int noteId, String newTitle, String newText) {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        HomePage homePage = new HomePage(driver);
        homePage.activateTab("notes");

        WebElement editButton = homePage.notesTable.findElement(By.tagName("button"));
        wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();

        wait.until(ExpectedConditions.elementToBeClickable(homePage.noteTitle));

        homePage.noteTitle.clear();
        homePage.noteTitle.sendKeys(newTitle);
        homePage.noteDescription.clear();
        homePage.noteDescription.sendKeys(newText);
        homePage.noteSubmit.click();
    }
    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testUserSignupLogin() {

        String username = "pzastoup";
        String password = "whatabadpassword";

        driver.get(baseURL + "/signup");

        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("Peter", "Zastoupil", username, password);

        login(username, password);

        assertEquals("Home", driver.getTitle());

        driver.findElement(By.id("logout")).click();
        driver.get(baseURL + "/home");
        assertEquals("Login", driver.getTitle());
    }

    private void CreateUserAndNote() {
        user = new User(1, "achim", "salty", "pass", "achim", "t");
        userService.createUser(user);
        Note note = new Note();
        note.setNotetitle("title");
        note.setNotedescription("text");
        noteId = noteService.addNote(note, user.getUserId());
        Credential credential = new Credential();
        credential.setUrl("www.google.com");
        credential.setPassword("password");
        credential.setUsername("userX");
        credentialService.addCredential(credential, user.getUserId());
    }
}
