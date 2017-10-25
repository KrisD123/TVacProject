package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

/**
 * Created by kdodonov on 24.10.2017.
 */
public class LoginPage extends BasePage {
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
        userNameField.clear();
        userNameField.sendKeys(userName);
        passwordField.clear();
        passwordField.sendKeys(userPassword);
        submitButton.click();
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }

    public boolean checkLoginPageOpen() {
        return driver.getTitle().equals("Login");
    }

    public boolean isLoginErrorMessageDisplayed() {
        if(driver.findElements(By.xpath(ERROR_MESSAGE_XPATH)).size()>0) {
            return true;
        } else return false;
    }
}
