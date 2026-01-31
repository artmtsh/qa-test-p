import pytest
from pages.auth_page import AuthPage
from pages.inputs_page import InputsPage


class TestAuth:

    @pytest.mark.parametrize("email, password", [
        ("test@protei.ru", "test"),
    ])
    def test_should_login_correctly(self, driver, email, password):
        auth_page = AuthPage(driver)
        auth_page.authenticate(email, password)

        inputs_page = InputsPage(driver)
        assert inputs_page.is_inputs_page_opened(), "После успешного логина не открылась страница анкеты"

    @pytest.mark.parametrize("email", [
        "invalid_email",
        "test@",
        "@protei.ru",
    ])
    def test_should_show_email_format_error(self, driver, email):
        auth_page = AuthPage(driver)
        auth_page.authenticate(email, "")
        assert auth_page.is_invalid_email_format_visible(), "Не показалась ошибка формата email"

    @pytest.mark.parametrize("email, password", [
        ("wrong@protei.ru", "test"),
        ("test@protei.ru", "wrong_pass"),
        ("admin@protei.ru", "admin"),
    ])
    def test_should_show_auth_error_when_creds_invalid(self, driver, email, password):
        auth_page = AuthPage(driver)
        auth_page.authenticate(email, password)
        assert auth_page.is_invalid_email_password_visible(), "Не показалась ошибка неверного логина/пароля"
