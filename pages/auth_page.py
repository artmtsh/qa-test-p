from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException

class AuthPage:
    FIELD_EMAIL = (By.ID, "loginEmail")
    FIELD_PASSWORD = (By.ID, "loginPassword")
    BUTTON_AUTH = (By.ID, "authButton")

    ERROR_EMAIL_FORMAT = (By.ID, "emailFormatError")
    ERROR_INVALID_EMAIL_PASSWORD = (By.ID, "invalidEmailPassword")

    def __init__(self, driver):
        self.driver = driver
        self.wait = WebDriverWait(driver, 5)

    def is_invalid_email_format_visible(self) -> bool:
        try:
            return self.wait.until(
                EC.visibility_of_element_located(self.ERROR_EMAIL_FORMAT)
            ).is_displayed()
        except TimeoutException:
            return False

    def is_invalid_email_password_visible(self) -> bool:
        try:
            return self.wait.until(
                EC.visibility_of_element_located(self.ERROR_INVALID_EMAIL_PASSWORD)
            ).is_displayed()
        except TimeoutException:
            return False

    def authenticate(self, email, password):
        self.wait.until(EC.visibility_of_element_located(self.FIELD_EMAIL)).send_keys(email)
        self.wait.until(EC.visibility_of_element_located(self.FIELD_PASSWORD)).send_keys(password)
        self.wait.until(EC.element_to_be_clickable(self.BUTTON_AUTH)).click()

    def is_auth_page_opened(self) -> bool:
        try:
            field_password = self.wait.until(EC.visibility_of_element_located(self.FIELD_PASSWORD)).is_displayed()
            field_email = self.wait.until(EC.visibility_of_element_located(self.FIELD_EMAIL)).is_displayed()
            button_auth = self.wait.until(EC.element_to_be_clickable(self.BUTTON_AUTH)).is_displayed()
            return field_password and field_email and button_auth
        except TimeoutException:
            return False