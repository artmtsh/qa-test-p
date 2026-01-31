import pytest
from pages.auth_page import AuthPage
from pages.inputs_page import InputsPage

BASE_EMAIL = "test@protei.ru"
BASE_PASSWORD = "test"


@pytest.fixture
def inputs_page(driver):
    auth_page = AuthPage(driver)
    auth_page.authenticate(BASE_EMAIL, BASE_PASSWORD)

    page = InputsPage(driver)
    assert page.is_inputs_page_opened(), "Страница ввода не открылась после логина"
    return page


class TestInputs:

    def _check_row_after_add(self, row_data, expected_email, expected_name, expected_gender, expected_check, expected_radio):
        assert len(row_data) == 5, "Некорректное количество элементов в строке"
        assert row_data[0] == expected_email
        assert row_data[1] == expected_name
        assert row_data[2] == expected_gender
        assert row_data[3] == expected_check
        assert row_data[4] == expected_radio

    def test_should_add_row_correctly(self, inputs_page):
        inputs_page.fill_email_name_gender(BASE_EMAIL, "Test", "Мужской")
        inputs_page.toggle_option_11()
        inputs_page.toggle_option_12()
        inputs_page.select_variant_21()

        rows_before = inputs_page.get_rows_count()
        inputs_page.submit_form()

        last_row = inputs_page.get_last_row_text()
        self._check_row_after_add(last_row, BASE_EMAIL, "Test", "Мужской", "1.1, 1.2", "2.1")
        assert inputs_page.get_rows_count() == rows_before + 1

    @pytest.mark.parametrize("invalid_email", [
        "",                 # пусто
        "plainaddress",     # нет @
        "email.example.com",# нет @
        "@example.com",     # нет части до @
        "test@",            # нет части после @
        "-test@protei.ru",  # начинается с '-' (до @ нельзя '-')
        "te-st@protei.ru",  # '-' в части до @ запрещён их regex'ом
    ])
    def test_should_show_email_format_error(self, inputs_page, invalid_email):
        inputs_page.fill_email_name_gender(invalid_email, "Test", "Мужской")
        inputs_page.toggle_option_11()
        inputs_page.toggle_option_12()
        inputs_page.select_variant_21()
        inputs_page.click_add()

        assert inputs_page.is_invalid_email_format_visible()

    def test_should_show_empty_name_error(self, inputs_page):
        inputs_page.fill_in_email(BASE_EMAIL)
        inputs_page.click_add()
        assert inputs_page.is_empty_name_visible()

    def test_gender_selection_logic(self, inputs_page):
        inputs_page.fill_email_name_gender(BASE_EMAIL, "Test", "Мужской")
        inputs_page.choose_gender("Женский")

        inputs_page.toggle_option_11()
        inputs_page.select_variant_21()
        inputs_page.submit_form()

        last_row = inputs_page.get_last_row_text()
        assert last_row[2] == "Женский"

    def test_add_multiple_rows(self, inputs_page):
        inputs_page.fill_email_name_gender(BASE_EMAIL, "Test1", "Мужской")
        inputs_page.toggle_option_11()
        inputs_page.select_variant_21()
        inputs_page.submit_form()

        rows_count = inputs_page.get_rows_count()

        inputs_page.clear_form()
        inputs_page.fill_email_name_gender(BASE_EMAIL, "Test2", "Женский")
        inputs_page.toggle_option_11()
        inputs_page.select_variant_22()
        inputs_page.submit_form()

        last_row = inputs_page.get_last_row_text()
        self._check_row_after_add(last_row, BASE_EMAIL, "Test2", "Женский", "1.1", "2.2")
        assert inputs_page.get_rows_count() == rows_count + 1
