import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import selenium.Browser;
import selenium.BrowserCapabilities;
import selenium.PropertyLoader;
import ru.stqa.selenium.factory.WebDriverPool;
import java.util.concurrent.TimeUnit;


/**
 * Created by kdodonov on 20.09.2017.
 */
public class BaseTest {
    WebDriver driver;
    private Browser browser = new Browser();
    String userName;
    String userPassword;

    @BeforeClass
    public void init() {
        browser.setBrowserName(PropertyLoader.loadProperty("browser.name"));
        browser.setVersion(PropertyLoader.loadProperty("browser.version"));
        browser.setPlatform(PropertyLoader.loadProperty("browser.platform"));
        driver = WebDriverPool.DEFAULT.getDriver(BrowserCapabilities.getCapabilities(browser));
        driver.get(PropertyLoader.loadProperty("site.url"));
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
        } else
        {
            return false;
        }
    }

    @AfterClass
    public void stop() {
        WebDriverPool.DEFAULT.dismissAll();
    }

}
