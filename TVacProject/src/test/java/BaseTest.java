import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;


/**
 * Created by kdodonov on 20.09.2017.
 */
public class BaseTest {
    WebDriver driver;

    @BeforeClass
    public void init() {
        driver = new ChromeDriver();
        driver.get("https://t-vac-testschool:38443/");
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
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
        driver.quit();
    }
}
