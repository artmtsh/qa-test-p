package protei.test.qa.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import protei.test.qa.pages.AuthPage;
import protei.test.qa.pages.InputsPage;

import java.util.List;

public class InputsTest extends BaseTest {
  final static String BASE_EMAIL = "test@protei.ru";
  final static String BASE_PASSWORD = "test";
  private InputsPage inputPage;

  @BeforeEach
  void login() {
    var authPage = new AuthPage(webDriver);
    authPage.authenticate(BASE_EMAIL, BASE_PASSWORD);
    inputPage = new InputsPage(webDriver);
    Assertions.assertTrue(inputPage.isInputsPageOpened());
  }

  @Test
  public void shouldAddRowToTable_whenFormIsFilledInCorrectly() {
    inputPage.fillEmailNameGender(BASE_EMAIL, "Test", "Мужской");
    inputPage.toggleOption11();
    inputPage.toggleOption12();
    inputPage.selectVariant21();
    var amountOfRowsBeforeAdd = inputPage.getRowsCount();
    inputPage.submitForm();
    var lastRow = inputPage.getLastRowText();
    checkRowAfterAdd(lastRow, "test@protei.ru", "Test", "Мужской", "1.1, 1.2", "2.1");
    Assertions.assertEquals(amountOfRowsBeforeAdd + 1, inputPage.getRowsCount());
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/invalidEmailFormat.csv", numLinesToSkip = 1)
  public void shouldShowEmailFormatError_whenEmailIsInvalid(String email) {
    inputPage.fillEmailNameGender(email, "Test", "Мужской");
    inputPage.toggleOption11();
    inputPage.toggleOption12();
    inputPage.selectVariant21();
    inputPage.clickAdd();
    Assertions.assertTrue(inputPage.isInvalidEmailFormatVisible());
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/invalidEmailFormat.csv", numLinesToSkip = 1)
  public void shouldShowEmailFormatError_whenEmailIsInvalidAndNameIsEmpty(String email) {
    inputPage.fillEmailNameGender(email, "", "Мужской");
    inputPage.toggleOption11();
    inputPage.toggleOption12();
    inputPage.selectVariant21();
    inputPage.clickAdd();
    Assertions.assertTrue(inputPage.isInvalidEmailFormatVisible());
    Assertions.assertFalse(inputPage.isEmptyNameVisible());
  }

  @Test
  public void shouldShowEmptyNameError_whenNameIsBlank() {
    inputPage.fillInEmailField(BASE_EMAIL);
    inputPage.clickAdd();
    Assertions.assertTrue(inputPage.isEmptyNameVisible());
  }

  @Test
  public void shouldAddRowWithGenderFemale_whenGenderFemaleIsSelected() {
    inputPage.fillEmailNameGender(BASE_EMAIL, "Test", "Женский");
    inputPage.toggleOption11();
    inputPage.toggleOption12();
    inputPage.selectVariant21();
    inputPage.submitForm();
    var lastRow = inputPage.getLastRowText();
    checkRowAfterAdd(lastRow, "test@protei.ru", "Test", "Женский", "1.1, 1.2", "2.1");

  }

  @Test
  public void shouldAddRowWithLastSelectedGender_whenFormFilledInCorrectly() {
    inputPage.fillEmailNameGender(BASE_EMAIL, "Test", "Мужской");
    inputPage.chooseGender("Женский");
    inputPage.toggleOption11();
    inputPage.toggleOption12();
    inputPage.selectVariant21();
    inputPage.submitForm();
    var lastRow = inputPage.getLastRowText();
    checkRowAfterAdd(lastRow, "test@protei.ru", "Test", "Женский", "1.1, 1.2", "2.1");

  }

  @Test
  public void shouldAddRowWithLastSelectedRadio_whenFormFilledInCorrectly() {
    inputPage.fillEmailNameGender(BASE_EMAIL, "Test", "Женский");
    inputPage.toggleOption11();
    inputPage.toggleOption12();
    inputPage.selectVariant21();
    inputPage.selectVariant23();
    inputPage.submitForm();
    var lastRow = inputPage.getLastRowText();
    checkRowAfterAdd(lastRow, "test@protei.ru", "Test", "Женский", "1.1, 1.2", "2.3");

  }

  @Test
  public void shouldAddRowOnlyWithSelectedCheckboxes_whenFormFilledInCorrectly() {
    inputPage.fillEmailNameGender(BASE_EMAIL, "Test", "Мужской");
    inputPage.toggleOption11();
    inputPage.toggleOption12();
    inputPage.toggleOption11();
    inputPage.selectVariant21();
    inputPage.submitForm();
    var lastRow = inputPage.getLastRowText();
    checkRowAfterAdd(lastRow, "test@protei.ru", "Test", "Мужской", "1.2", "2.1");

  }

  @Test
  public void shouldAddRowWithoutSelectedCheckboxes_whenFormFilledInCorrectly() {
    inputPage.fillEmailNameGender(BASE_EMAIL, "Test", "Мужской");
    inputPage.selectVariant21();
    inputPage.submitForm();
    var lastRow = inputPage.getLastRowText();
    checkRowAfterAdd(lastRow, "test@protei.ru", "Test", "Мужской", "Нет", "2.1");
  }

  @Test
  public void shouldAddRowWithoutSelectedRadio_whenFormFilledInCorrectly() {
    inputPage.fillEmailNameGender(BASE_EMAIL, "Test", "Мужской");
    inputPage.toggleOption11();
    inputPage.toggleOption12();
    inputPage.submitForm();
    var lastRow = inputPage.getLastRowText();
    checkRowAfterAdd(lastRow, "test@protei.ru", "Test", "Мужской", "1.1, 1.2", "");

  }

  @Test
  public void shouldAddMultipleRows_whenFormFilledCorrectlyMultipleTimes() {
    inputPage.fillEmailNameGender(BASE_EMAIL, "Test1", "Мужской");
    inputPage.toggleOption11();
    inputPage.toggleOption12();
    inputPage.selectVariant21();
    int amountOfRowsBeforeAdd = inputPage.getRowsCount();
    inputPage.submitForm();
    List<String> lastRow = inputPage.getLastRowText();
    checkRowAfterAdd(lastRow, "test@protei.ru", "Test1", "Мужской", "1.1, 1.2", "2.1");

    Assertions.assertEquals(amountOfRowsBeforeAdd + 1, inputPage.getRowsCount());

    inputPage.clearForm();
    inputPage.fillEmailNameGender(BASE_EMAIL, "Test2", "Женский");
    inputPage.toggleOption11();
    inputPage.selectVariant22();
    amountOfRowsBeforeAdd = inputPage.getRowsCount();
    inputPage.submitForm();
    lastRow = inputPage.getLastRowText();
    checkRowAfterAdd(lastRow, "test@protei.ru", "Test2", "Женский", "1.1", "2.2");
    Assertions.assertEquals(amountOfRowsBeforeAdd + 1, inputPage.getRowsCount());
  }

  private void checkRowAfterAdd(List<String> row, String expectedEmail, String expectedName, String expectedGender, String expectedCheckbox, String expectedRadio) {
    Assertions.assertEquals(5, row.size(), "Некорректное количество элементов в строке");
    Assertions.assertEquals(expectedEmail, row.get(0));
    Assertions.assertEquals(expectedName, row.get(1));
    Assertions.assertEquals(expectedGender, row.get(2));
    Assertions.assertEquals(expectedCheckbox, row.get(3));
    Assertions.assertEquals(expectedRadio, row.get(4));
  }
}
