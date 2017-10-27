import listeners.RunListener;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.RepresentativesPage;

import java.util.Set;

import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

/**
 * Created by kdodonov on 13.10.2017.
 */
@Listeners(value = RunListener.class)
public class RepresentativesTests extends BaseTest {
    private static final Logger LOGGER = Logger.getLogger(RepresentativesTests.class);

    @BeforeClass
    public void performLogin() {
        login(userName, userPassword);
    }

    @Test
    public void openRepresentativesSectionInNewWindowTest() {
        LOGGER.info("Test to open 'My representatives' page in a new window");
        tVacMainPage.openRepresentativesSectionInNewWindow();
        Assert.assertTrue(representativesPage.checkRepresentativesPageIsOpen());
        LOGGER.info("'My representatives' page was open in a new window");

    }
}
