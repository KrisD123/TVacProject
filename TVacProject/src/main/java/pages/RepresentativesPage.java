package pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Created by kdodonov on 24.10.2017.
 */
public class RepresentativesPage extends TVacMainPage {
    private static final Logger LOGGER = Logger.getLogger(RepresentativesPage.class);
    public RepresentativesPage(WebDriver driver) {
        super(driver);
    }

    public boolean checkRepresentativesPageIsOpen() {
        LOGGER.info("Check that 'Representatives' page is open");
        return driver.getTitle().equals("Представители");
    }
}
