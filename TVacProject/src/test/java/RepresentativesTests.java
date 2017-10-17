import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Set;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

/**
 * Created by kdodonov on 13.10.2017.
 */
public class RepresentativesTests extends BaseTest {
    @BeforeClass
    public void performLogin() {
        login(userName, userPassword);
    }

    @Test
    public void openRepresentativesSectionInNewWindowTest() {
        Set<String> oldWindowHandles = driver.getWindowHandles();
        openRepresentativesSectionInNewWindow();
        Set<String> newWindowHandles = driver.getWindowHandles();
        newWindowHandles.removeAll(oldWindowHandles);
        String newWIndowHandle = newWindowHandles.iterator().next();

        driver.switchTo().window(newWIndowHandle);


        Assert.assertTrue(wait.until(titleIs("Представители")));

    }
}
