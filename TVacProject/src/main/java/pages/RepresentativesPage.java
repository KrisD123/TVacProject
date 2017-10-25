package pages;

import org.openqa.selenium.WebDriver;

/**
 * Created by kdodonov on 24.10.2017.
 */
public class RepresentativesPage extends TVacMainPage {
    public RepresentativesPage(WebDriver driver) {
        super(driver);
    }

    public boolean checkRepresentativesPageIsOpen() {
        return driver.getTitle().equals("Представители");
    }
}
