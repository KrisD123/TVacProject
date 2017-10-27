import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pages.*;
import selenium.Browser;
import selenium.BrowserCapabilities;
import selenium.PropertyLoader;
import ru.stqa.selenium.factory.WebDriverPool;

import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;


/**
 * Created by kdodonov on 20.09.2017.
 */
public class BaseTest {
    WebDriver driver;
    private Browser browser = new Browser();
    String userName;
    String userPassword;
    WebDriverWait wait;
    LoginPage loginPage;
    TVacMainPage tVacMainPage;
    MyTeamPage myTeamPage;
    EmployeesVacationPage employeesVacationPage;
    MyVacationsPage myVacationsPage;
    RepresentativesPage representativesPage;

    @BeforeClass
    public void init(ITestContext iTestContext) {
        browser.setBrowserName(PropertyLoader.loadProperty("browser.name"));
        browser.setVersion(PropertyLoader.loadProperty("browser.version"));
        browser.setPlatform(PropertyLoader.loadProperty("browser.platform"));
        driver = WebDriverPool.DEFAULT.getDriver(BrowserCapabilities.getCapabilities(browser));
        iTestContext.setAttribute("driver", driver);
        wait = new WebDriverWait(driver, 10);
        driver.get(PropertyLoader.loadProperty("site.url"));
        if (browser.getBrowserName().equals("chrome")) {
            driver.manage().window().maximize();
        }
        if (browser.getBrowserName().equals("ie")) {
            driver.get("javascript:document.getElementById('overridelink').click();");
        }
        userName = PropertyLoader.loadProperty("user.name");
        userPassword = PropertyLoader.loadProperty("user.password");
        //driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        loginPage = new LoginPage(driver);
        tVacMainPage = new TVacMainPage(driver);
        myTeamPage = new MyTeamPage(driver);
        employeesVacationPage = new EmployeesVacationPage(driver);
        myVacationsPage = new MyVacationsPage(driver);
        representativesPage = new RepresentativesPage(driver);
        Assert.assertTrue(loginPage.checkLoginPageOpen());
    }


    protected void login(String userName, String userPassword) {
        new LoginPage(driver).performLogin(userName, userPassword);
    }

    @AfterClass
    public void stop() {
        WebDriverPool.DEFAULT.dismissAll();
    }

}
