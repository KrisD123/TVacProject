package pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Set;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

/**
 * Created by kdodonov on 24.10.2017.
 */
public class TVacMainPage extends BasePage {
    private static final Logger LOGGER = Logger.getLogger(TVacMainPage.class);
    private static final String USER_NAME_LINK_XPATH = "//div[@class='menu-heading btn-brand']";

    @FindBy(xpath = USER_NAME_LINK_XPATH)
    WebElement userNameLink;
    @FindBy(linkText = "Выход")
    WebElement exitLink;
    @FindBy(linkText = "Моя команда")
    WebElement myTeamLink;
    @FindBy(linkText = "Главная")
    WebElement homePageLink;


    public TVacMainPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        LOGGER.info("Perform log out");
        moveToUserNameLink();
        wait.until(visibilityOf(exitLink)).click();
        LOGGER.info("User was logged out");
    }

    public String getTextOfUserNameLink() {
        return userNameLink.getText();
    }

    public void openMyTeamSection() {
        LOGGER.info("Open 'My team' section");
        moveToUserNameLink();
        wait.until(visibilityOf(myTeamLink)).click();
        LOGGER.info("'My team' section was open");
    }

    private void moveToUserNameLink() {
        Actions actions = new Actions(driver);
        actions.moveToElement(wait.until(visibilityOfElementLocated(By.xpath(USER_NAME_LINK_XPATH)))).build().perform();
    }

    public void openHomePage() {
        LOGGER.info("Open Home page");
        homePageLink.click();
    }

    public void openRepresentativesSectionInNewWindow() {
        LOGGER.info("Open 'My representatives' section in a new window");
        Set<String> oldWindowHandles = driver.getWindowHandles();

        Actions actions = new Actions(driver);
        moveToUserNameLink();
        actions.keyDown(Keys.CONTROL)
                .click(wait.until(visibilityOfElementLocated(By.linkText("Представители"))))
                .keyUp(Keys.CONTROL)
                .build()
                .perform();

        Set<String> newWindowHandles = driver.getWindowHandles();
        newWindowHandles.removeAll(oldWindowHandles);
        String newWIndowHandle = newWindowHandles.iterator().next();

        driver.switchTo().window(newWIndowHandle);

    }


}
