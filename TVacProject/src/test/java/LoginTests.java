import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by kdodonov on 20.09.2017.
 */
public class LoginTests extends BaseTest {


    @BeforeMethod
    public void checkUserLogged() {
        if (!checkLoginPageOpen()) {
            WebElement loggedUserName = driver.findElement(By.xpath("//div[@class='menu-heading btn-brand']"));
            loggedUserName.click();
            driver.findElement(By.linkText("Выход")).click();
        }
    }

    @Test(priority = 1)
    public void unsuccessfulLogin() {
        WebElement username = driver.findElement(By.id("username"));
        username.clear();
        username.sendKeys("kdodonov");

        WebElement password = driver.findElement(By.id("password"));
        password.clear();
        password.sendKeys("wrongPassword");

        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        //check that user is not logged in
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        List<WebElement> loggedUserName = driver.findElements(By.xpath("//div[@class='menu-heading btn-brand']"));
        Assert.assertTrue(loggedUserName.size()==0, "User name is displayed, User is logged in");

        //check that correct error message is displayed
        WebElement errorMessage = driver.findElement(By.xpath(".//div[@class='modal fade login in']//div[@class='modal-body text-center']"));
        Assert.assertTrue(errorMessage.getText().contains("Неверная пара логин/пароль."), "Error message is not displayed");

    }

    @Test(priority = 0)
    public void successfulLogin() {
        WebElement username = driver.findElement(By.id("username"));
        username.clear();
        username.sendKeys("insertValidLoginHere");

        WebElement password = driver.findElement(By.id("password"));
        password.clear();
        password.sendKeys("insertValidPwdHere");

        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        SoftAssert softAssert = new SoftAssert();

        List<WebElement> errorMessage = driver.findElements(By.xpath(".//div[@class='modal fade login in']//div[@class='modal-body text-center']"));
        softAssert.assertTrue(errorMessage.size()==0, "Error message is displayed");

        List<WebElement> loggedUserName = driver.findElements(By.xpath("//div[@class='menu-heading btn-brand']"));
        Assert.assertTrue(loggedUserName.size()==1, "User name is not displayed, User is not logged in");

        softAssert.assertAll();

    }

}
