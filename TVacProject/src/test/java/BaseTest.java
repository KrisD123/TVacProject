import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
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

    @BeforeClass
    public void init() {
        browser.setBrowserName(PropertyLoader.loadProperty("browser.name"));
        browser.setVersion(PropertyLoader.loadProperty("browser.version"));
        browser.setPlatform(PropertyLoader.loadProperty("browser.platform"));
        driver = WebDriverPool.DEFAULT.getDriver(BrowserCapabilities.getCapabilities(browser));
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
        Assert.assertTrue(checkLoginPageOpen());
    }

    public boolean checkLoginPageOpen() {
        String title = driver.findElement(By.xpath("//title")).getAttribute("innerText");
        if (title.equals("Login")) {
            return true;
        } else {
            return false;
        }
    }

    protected void login(String userName, String userPassword) {
        WebElement username = driver.findElement(By.id("username"));
        username.clear();
        username.sendKeys(userName);

        WebElement password = driver.findElement(By.id("password"));
        password.clear();
        password.sendKeys(userPassword);

        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();
    }

    protected void openMyTeamSection() {
        Actions actions = new Actions(driver);
        actions.moveToElement(wait.until(elementToBeClickable(By.xpath("//div[@class='menu-heading btn-brand']")))).build().perform();
        wait.until(visibilityOfElementLocated(By.linkText("Моя команда"))).click();
    }

    protected void openRepresentativesSectionInNewWindow() {
        Actions actions = new Actions(driver);
        actions.moveToElement(wait.until(elementToBeClickable(By.xpath("//div[@class='menu-heading btn-brand']")))).build().perform();
        actions.keyDown(Keys.CONTROL)
                .click(wait.until(visibilityOfElementLocated(By.linkText("Представители"))))
                .keyUp(Keys.CONTROL)
                .build()
                .perform();
    }

    @AfterClass
    public void stop() {
        WebDriverPool.DEFAULT.dismissAll();
    }

}
