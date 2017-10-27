package pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

/**
 * Created by kdodonov on 24.10.2017.
 */
public class EmployeesVacationPage extends TVacMainPage {
    private static final Logger LOGGER = Logger.getLogger(EmployeesVacationPage.class);
    @FindBy(xpath = "//div[@class='breadcrumbs']/div/span")
    WebElement header;
    @FindBy(xpath = "//div[text()='Отпусков пока нет']")
    WebElement absentVacationsMessage;
    @FindBy(xpath = "//tbody[@id='employee-vacations']")
    WebElement vacationsTable;

    public EmployeesVacationPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, 10);
    }

    public boolean checkEmployeeVacationPageIsOpen() {
        LOGGER.info("Check that employee vacations page is open");
        return driver.getTitle().equals("Список отпусков");
    }

    public boolean checkEmployeeNameIsDisplayedInHeader(String employeeName) {
        LOGGER.info("Check that employee name is displayed in header");
        String nameTextInHeader = header.getText();
       return nameTextInHeader.contains(employeeName);
    }

    public boolean checkBodyOfVacationsSection() {
        LOGGER.info("Check body of vacation section");
        if (driver.findElements(By.xpath("//tbody[@id='employee-vacations']/tr")).size() == 0) {
            LOGGER.info("Check message about absent vacations is displayed");
            String message = absentVacationsMessage.getText();
            LOGGER.info("Message is: " + message);
           return absentVacationsMessage.isDisplayed();
        } else {
            LOGGER.info("Check that vacation table is displayed");
           return vacationsTable.isDisplayed();
        }
    }
}
