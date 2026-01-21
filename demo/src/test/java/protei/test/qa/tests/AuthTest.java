package protei.test.qa.tests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import protei.test.qa.pages.AuthPage;
import protei.test.qa.pages.InputsPage;

public class AuthTest extends BaseTest {
  @ParameterizedTest
  @CsvFileSource(resources = "/defaultLoginData.csv", numLinesToSkip = 1)
  void validLoginOpensInputsPage(String email, String password) {
    AuthPage authPage = new AuthPage(webDriver);
    authPage.authenticate(email, password);
    InputsPage inputsPage = new InputsPage(webDriver);
    Assertions.assertTrue(inputsPage.isInputsPageOpened());
  }
  @ParameterizedTest
  @CsvFileSource(resources = "/invalidEmailFormat.csv", numLinesToSkip = 1)
  void invalidEmailShowsFormatError(String email) {
    AuthPage authPage = new AuthPage(webDriver);
    authPage.authenticate(email, "");
    Assertions.assertTrue(authPage.isInvalidEmailFormatVisible());
  }
  @ParameterizedTest
  @CsvFileSource(resources = "/invalidEmailPassword.csv", numLinesToSkip = 1)
  void invalidEmailPasswordShowsAuthError(String email, String password) {
    AuthPage authPage = new AuthPage(webDriver);
    authPage.authenticate(email, password);
    Assertions.assertTrue(authPage.isInvalidEmailPasswordVisible());
  }
}