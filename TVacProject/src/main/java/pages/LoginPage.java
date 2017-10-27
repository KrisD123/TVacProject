package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.apache.log4j.Logger;

/**
 * Created by kdodonov on 24.10.2017.
 */
public class LoginPage extends BasePage {
    private static final Logger LOGGER = Logger.getLogger(LoginPage.class);
    private static final String ERROR_MESSAGE_XPATH = ".//div[@class='modal fade login in']//div[@class='modal-body text-center']";

    @FindBy(id = "username")
    WebElement userNameField;
    @FindBy(id = "password")
    WebElement passwordField;
    @FindBy(xpath = "//button[@type='submit']")
    WebElement submitButton;
    @FindBy(xpath = ERROR_MESSAGE_XPATH)
    WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 15), this);
    }

    public void performLogin(String userName, String userPassword) {
        LOGGER.info("Perform login");
        LOGGER.info("Enter user name: " + userName);
        userNameField.clear();
        userNameField.sendKeys(userName);
        LOGGER.info("Enter user password: " + userPassword);
        passwordField.clear();
        passwordField.sendKeys(userPassword);
        LOGGER.info("Click 'Submit' button");
        submitButton.click();
    }

    public String getErrorMessage() {
        String errorMessageText = errorMessage.getText();
        LOGGER.warn("Error message is displayed: " + errorMessageText);
        return errorMessageText;
    }

    public boolean checkLoginPageOpen() {
        return driver.getTitle().equals("Login");
    }

    public boolean isLoginErrorMessageDisplayed() {
        LOGGER.info("Check if login error message is displayed");
        if(driver.findElements(By.xpath(ERROR_MESSAGE_XPATH)).size()>0) {
            LOGGER.info("Error message is displayed");
            return true;
        } else {
            LOGGER.info("Error message is not displayed");
            return false;
        }
    }
}
