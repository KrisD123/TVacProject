import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.RepresentativesPage;

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
        tVacMainPage.openRepresentativesSectionInNewWindow();
        Assert.assertTrue(representativesPage.checkRepresentativesPageIsOpen());

    }
}
