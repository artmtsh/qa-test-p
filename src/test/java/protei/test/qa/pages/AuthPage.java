package protei.test.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AuthPage {
  private static final By FIELD_EMAIL = By.id("loginEmail");
  private static final By FIELD_PASSWORD = By.id("loginPassword");
  private static final By BUTTON_AUTH = By.id("authButton");

  private static final By ERROR_EMAIL_FORMAT = By.id("emailFormatError");
  private static final By ERROR_INVALID_EMAIL_PASSWORD = By.id("invalidEmailPassword");
  private final WebDriverWait wait;

  public AuthPage(WebDriver driver) {
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
  }

  public boolean isInvalidEmailFormatVisible(){
    try {
      return wait.until(
              ExpectedConditions.visibilityOfElementLocated(ERROR_EMAIL_FORMAT)
      ).isDisplayed();
    } catch (TimeoutException e) {
      return false;
    }
  }

  public boolean isInvalidEmailPasswordVisible(){
    try {
      return wait.until(
              ExpectedConditions.visibilityOfElementLocated(ERROR_INVALID_EMAIL_PASSWORD)
      ).isDisplayed();
    } catch (TimeoutException e) {
      return false;
    }
  }

  public void authenticate(String email, String password){
    wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_EMAIL)).sendKeys(email);
    wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_PASSWORD)).sendKeys(password);;
    wait.until(ExpectedConditions.elementToBeClickable(BUTTON_AUTH)).click();
  }

  public boolean isAuthPageOpened(){
    try {
      boolean fieldPasswordExist = wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_PASSWORD)).isDisplayed();
      boolean fieldEmailExist = wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_EMAIL)).isDisplayed();
      boolean buttonAuthExist = wait.until(ExpectedConditions.elementToBeClickable(BUTTON_AUTH)).isDisplayed();
      return fieldPasswordExist && fieldEmailExist && buttonAuthExist;
    } catch (TimeoutException e){
      return false;
    }
  }
}
