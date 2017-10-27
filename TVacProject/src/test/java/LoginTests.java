import listeners.RunListener;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.LoginPage;


/**
 * Created by kdodonov on 20.09.2017.
 */
@Listeners(value = RunListener.class)
public class LoginTests extends BaseTest {
    private static final Logger LOGGER = Logger.getLogger(LoginTests.class);


    @BeforeMethod
    public void checkUserLogged() {
        if (!loginPage.checkLoginPageOpen()) {
            tVacMainPage.logout();
        }
    }

    @Test(priority = 1)
    public void unsuccessfulLogin() {
        LOGGER.info("Test for unsuccessful login");
        login(userName, "wrongPassword");

        LOGGER.info("Check that error message with correct text is displayed");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Неверная пара логин/пароль."), "Error message is not displayed");

    }

    @Test(priority = 0)
    public void successfulLogin() {
        LOGGER.info("Test for successful login");
        login(userName, userPassword);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertFalse(loginPage.isLoginErrorMessageDisplayed(), "Error message is displayed");

        LOGGER.info("Check that link with user name is displayed");
        softAssert.assertEquals(tVacMainPage.getTextOfUserNameLink(), "Кристина Додонова", "User name is not displayed, User is not logged in");
        LOGGER.info("Link with user name is displayed");
        softAssert.assertAll();

    }

}
