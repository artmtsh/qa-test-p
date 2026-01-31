from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait, Select
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException

class InputsPage:
    FIELD_EMAIL = (By.ID, "dataEmail")
    FIELD_NAME = (By.ID, "dataName")
    SELECT_GENDER = (By.ID, "dataGender")
    CHECKBOX_11 = (By.ID, "dataCheck11")
    CHECKBOX_12 = (By.ID, "dataCheck12")
    RADIO_21 = (By.ID, "dataSelect21")
    RADIO_22 = (By.ID, "dataSelect22")
    RADIO_23 = (By.ID, "dataSelect23")
    BUTTON_ADD = (By.ID, "dataSend")
    # CSS селекторы
    BUTTON_OK = (By.CSS_SELECTOR, ".uk-modal.uk-open .uk-modal-close")
    DATA_TABLE_TR = (By.CSS_SELECTOR, "#dataTable tbody tr")
    DATA_TABLE_TR_LAST_CHILD = (By.CSS_SELECTOR, "#dataTable tbody tr:last-child td")

    ERROR_EMAIL_FORMAT = (By.ID, "emailFormatError")
    ERROR_EMPTY_NAME = (By.ID, "blankNameError")

    def __init__(self, driver):
        self.driver = driver
        self.wait = WebDriverWait(driver, 5)

    def is_inputs_page_opened(self) -> bool:
        try:
            return self.wait.until(EC.visibility_of_element_located(self.FIELD_EMAIL)).is_displayed()
        except TimeoutException:
            return False

    def is_invalid_email_format_visible(self) -> bool:
        try:
            return self.wait.until(EC.visibility_of_element_located(self.ERROR_EMAIL_FORMAT)).is_displayed()
        except TimeoutException:
            return False

    def is_empty_name_visible(self) -> bool:
        try:
            return self.wait.until(EC.visibility_of_element_located(self.ERROR_EMPTY_NAME)).is_displayed()
        except TimeoutException:
            return False

    def fill_in_email(self, email):
        field = self.wait.until(EC.visibility_of_element_located(self.FIELD_EMAIL))
        field.clear()
        field.send_keys(email)

    def fill_in_name(self, name):
        field = self.wait.until(EC.visibility_of_element_located(self.FIELD_NAME))
        field.clear()
        field.send_keys(name)

    def choose_gender(self, gender_text):
        element = self.wait.until(EC.visibility_of_element_located(self.SELECT_GENDER))
        select = Select(element)
        select.select_by_visible_text(gender_text)

    def _toggle_checkbox(self, locator):
        checkbox = self.wait.until(EC.element_to_be_clickable(locator))
        checkbox.click()

    def toggle_option_11(self):
        self._toggle_checkbox(self.CHECKBOX_11)

    def toggle_option_12(self):
        self._toggle_checkbox(self.CHECKBOX_12)

    def _select_radio(self, locator):
        radio = self.wait.until(EC.element_to_be_clickable(locator))
        if not radio.is_selected():
            radio.click()

    def select_variant_21(self):
        self._select_radio(self.RADIO_21)

    def select_variant_22(self):
        self._select_radio(self.RADIO_22)

    def select_variant_23(self):
        self._select_radio(self.RADIO_23)

    def fill_email_name_gender(self, email, name, gender):
        if email:
            self.fill_in_email(email)
        if name:
            self.fill_in_name(name)
        if gender:
            self.choose_gender(gender)

    def submit_form(self):
        self.wait.until(EC.element_to_be_clickable(self.BUTTON_ADD)).click()
        self.wait.until(EC.element_to_be_clickable(self.BUTTON_OK)).click()

    def click_add(self):
        self.wait.until(EC.element_to_be_clickable(self.BUTTON_ADD)).click()

    def get_rows_count(self) -> int:
        return len(self.driver.find_elements(*self.DATA_TABLE_TR))

    def get_last_row_text(self) -> list[str]:
        elements = self.driver.find_elements(*self.DATA_TABLE_TR_LAST_CHILD)
        return [el.text for el in elements]

    def clear_form(self):
        self.wait.until(EC.visibility_of_element_located(self.FIELD_EMAIL)).clear()
        self.wait.until(EC.visibility_of_element_located(self.FIELD_NAME)).clear()

        checkbox11 = self.wait.until(EC.element_to_be_clickable(self.CHECKBOX_11))
        if checkbox11.is_selected():
            checkbox11.click()

        checkbox12 = self.wait.until(EC.element_to_be_clickable(self.CHECKBOX_12))
        if checkbox12.is_selected():
            checkbox12.click()