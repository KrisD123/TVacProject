import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.Test;

/**
 * Created by kdodonov on 17.09.2017.
 */
public class BrowsersTests {
    @Test
    public void firefoxTest() {
        WebDriver driver = new FirefoxDriver();
        driver.get("https://t-vac-testschool:38443/");
        driver.quit();
    }

    @Test
    public void chromeTest() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://t-vac-testschool:38443/");
        driver.quit();
    }

    @Test
    public void ieTest() {
        WebDriver driver = new InternetExplorerDriver();
        driver.get("https://t-vac-testschool:38443/");
        driver.quit();
    }
}
