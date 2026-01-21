package protei.test.qa.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.nio.file.Paths;

public abstract class BaseTest {
  private static final String BASE_PATH = "src/test/resources/qa-test.html";

  protected WebDriver webDriver;

  @BeforeAll
  static void SetupDriver(){
    WebDriverManager.chromedriver().setup();
  }

  @BeforeEach
  void setUp(){
    webDriver = new ChromeDriver();
    String pathToHtml = Paths.get(BASE_PATH).toAbsolutePath().toUri().toString();
    webDriver.get(pathToHtml);
  }

  @AfterEach
  void tearDown(){
    if (webDriver!= null)
      webDriver.quit();
  }
}
