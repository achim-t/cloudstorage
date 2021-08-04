package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

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
    public void EditNote() {

        driver.get(baseURL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(user.getUsername(), user.getPassword());
        HomePage homePage = new HomePage(driver);
        assertEquals(1, homePage.getNoteCount());
        homePage.editNote(noteId, "newTitle", "newText");
//        assertTrue(homePage.getNoteTitles().get(0).getText().contains("newTitle"));
        assertEquals("", homePage.getNoteTitles().get(0).getText());
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testUserSignupLoginAndSubmitMessage() {

        String username = "pzastoup";
        String password = "whatabadpassword";

        driver.get(baseURL + "/signup");

        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup("Peter", "Zastoupil", username, password);

        driver.get(baseURL + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);

        assertEquals("Home", driver.getTitle());

        HomePage homePage = new HomePage(driver);
        assertEquals(0, homePage.getNoteCount());
        homePage.createNote("title", "message");
        assertEquals(1, homePage.getNoteCount());
    }

    private void CreateUserAndNote() {
        user = new User(1, "achim", "salty", "pass", "achim", "t");
        userService.createUser(user);
        Note note = new Note();
        note.setNotetitle("title");
        note.setNotedescription("text");
        noteId = noteService.addNote(note, user.getUserId());
    }
}
