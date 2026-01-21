package protei.test.qa.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import protei.test.qa.pages.AuthPage;
import protei.test.qa.pages.InputsPage;

public class InputsTest extends BaseTest{
  @ParameterizedTest
  @CsvFileSource(resources = "/defaultLoginData.csv", numLinesToSkip = 1)
  public void validFormFillInAddingRowToTable(String email, String password) throws InterruptedException {
    var authPage = new AuthPage(webDriver);
    authPage.authenticate(email, password);
    var inputPage = new InputsPage(webDriver);
    inputPage.fillInEmailField(email);
    inputPage.fillInName("Test");
    inputPage.chooseGender("Мужской");
    inputPage.setOption11();
    inputPage.setOption12();
    inputPage.selectVariant21();
    inputPage.clickAdd();
    inputPage.clickOK();
    var lastRow = inputPage.getLastRowText();
    Assertions.assertTrue(lastRow.contains("test@protei.ru"));
    Assertions.assertTrue(lastRow.contains("Test"));
    Assertions.assertTrue(lastRow.contains("Мужской"));
    Assertions.assertTrue(lastRow.contains("1.1, 1.2"));
    Assertions.assertTrue(lastRow.contains("2.1"));
  }
}
