import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


/**
 * Created by kdodonov on 20.09.2017.
 */
public class LoginTests extends BaseTest {


    @BeforeMethod
    public void checkUserLogged() {
        if (!loginPage.checkLoginPageOpen()) {
            tVacMainPage.logout();
        }
    }

    @Test(priority = 1)
    public void unsuccessfulLogin() {
        login(userName, "wrongPassword");

        Assert.assertTrue(loginPage.getErrorMessage().contains("Неверная пара логин/пароль."), "Error message is not displayed");

    }

    @Test(priority = 0)
    public void successfulLogin() {
        login(userName, userPassword);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertFalse(loginPage.isLoginErrorMessageDisplayed(), "Error message is displayed");

        softAssert.assertEquals(tVacMainPage.getTextOfUserNameLink(), "Кристина Додонова", "User name is not displayed, User is not logged in");

        softAssert.assertAll();

    }

}
