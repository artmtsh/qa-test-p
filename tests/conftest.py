import os
import pytest
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager


@pytest.fixture(scope="function")
def driver():
    service = Service(ChromeDriverManager().install())
    driver = webdriver.Chrome(service=service)
    driver.maximize_window()

    # conftest.py лежит в tests/, а html лежит в tests/resources/
    current_dir = os.path.dirname(__file__)
    file_path = os.path.join(current_dir, "resources", "qa-test.html")

    url = "file://" + os.path.abspath(file_path).replace("\\", "/")
    driver.get(url)

    try:
        yield driver
    finally:
        driver.quit()
