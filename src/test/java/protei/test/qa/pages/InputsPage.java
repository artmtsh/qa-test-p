package protei.test.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class InputsPage {
  private static final By FIELD_EMAIL = By.id("dataEmail");
  private static final By FIELD_NAME = By.id("dataName");
  private static final By SELECT_GENDER = By.id("dataGender");
  private static final By CHECKBOX_11 = By.id("dataCheck11");
  private static final By CHECKBOX_12 = By.id("dataCheck12");
  private static final By RADIO_21 = By.id("dataSelect21");
  private static final By RADIO_22 = By.id("dataSelect22");
  private static final By RADIO_23 = By.id("dataSelect23");
  private static final By BUTTON_ADD = By.id("dataSend");
  private static final By BUTTON_OK = By.cssSelector(".uk-modal.uk-open .uk-modal-close");

  private static final By ERROR_EMAIL_FORMAT = By.id("emailFormatError");
  private static final By ERROR_EMPTY_NAME = By.id("blankNameError");

  private static final By DATA_TABLE_TR = By.cssSelector("#dataTable tbody tr");
  private static final By DATA_TABLE_TR_LAST_CHILD = By.cssSelector("#dataTable tbody tr:last-child td");

  private final WebDriverWait wait;

  private final WebDriver driver;
  public InputsPage(WebDriver driver) {
    this.driver = driver;
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
  }

  public boolean isInputsPageOpened() {
    try {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_EMAIL)).isDisplayed();
    } catch (TimeoutException e) {
      return false;
    }
  }


  public boolean isInvalidEmailFormatVisible() {
    try {
      return wait.until(
              ExpectedConditions.visibilityOfElementLocated(ERROR_EMAIL_FORMAT)
      ).isDisplayed();
    } catch (TimeoutException e) {
      return false;
    }
  }

  public boolean isEmptyNameVisible() {
    try {
      return wait.until(
              ExpectedConditions.visibilityOfElementLocated(ERROR_EMPTY_NAME)
      ).isDisplayed();
    } catch (TimeoutException e) {
      return false;
    }
  }

  public void fillInEmailField(String email) {
    var fieldEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_EMAIL));
    fieldEmail.clear();
    fieldEmail.sendKeys(email);
  }

  public void fillInName(String name) {
    var fieldName = wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_NAME));
    fieldName.clear();
    fieldName.sendKeys(name);
  }

  public void chooseGender(String gender) {
    var selectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(SELECT_GENDER));
    Select genderSelect = new Select(selectElement);
    genderSelect.selectByVisibleText(gender);
  }

  private void toggleCheckbox(By locator) {
    var checkbox = wait.until(ExpectedConditions.elementToBeClickable(locator));
    checkbox.click();
  }

  public void toggleOption11() {
    toggleCheckbox(CHECKBOX_11);
  }

  public void toggleOption12() {
    toggleCheckbox(CHECKBOX_12);
  }

  private void selectRadio(By locator) {
    var radio = wait.until(ExpectedConditions.elementToBeClickable(locator));
    if (!radio.isSelected()) {
      radio.click();
    }
  }

  public void selectVariant21() {
    selectRadio(RADIO_21);
  }

  public void selectVariant22() {
    selectRadio(RADIO_22);
  }

  public void selectVariant23() {
    selectRadio(RADIO_23);
  }

  public void fillEmailNameGender(String email, String name, String gender) {
    if (email != null) {
      fillInEmailField(email);
    }
    if (name != null) {
      fillInName(name);
    }
    if (gender != null) {
      chooseGender(gender);
    }
  }


  public void clickAdd() {
    wait.until(ExpectedConditions.elementToBeClickable(BUTTON_ADD)).click();
  }

  public void clickOK() {
    wait.until(ExpectedConditions.elementToBeClickable(BUTTON_OK)).click();
  }

  public void submitForm() {
    clickAdd();
    clickOK();
  }

  public int getRowsCount() {
    return driver.findElements(DATA_TABLE_TR).size();
  }

  public List<String> getLastRowText() {
    return driver.findElements(DATA_TABLE_TR_LAST_CHILD).stream().map(WebElement::getText).collect(Collectors.toList());
  }

  public void clearForm() {
    wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_EMAIL)).clear();
    wait.until(ExpectedConditions.visibilityOfElementLocated(FIELD_NAME)).clear();
    var checkbox11 = wait.until(ExpectedConditions.elementToBeClickable(CHECKBOX_11));
    if (checkbox11.isSelected()) {
      checkbox11.click();
    }
    var checkbox12 = wait.until(ExpectedConditions.elementToBeClickable(CHECKBOX_12));
    if (checkbox12.isSelected()) {
      checkbox12.click();
    }
  }
}
