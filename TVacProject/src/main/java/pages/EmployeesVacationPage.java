package pages;

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
        return driver.getTitle().equals("Список отпусков");
    }

    public boolean checkEmployeeNameIsDisplayedInHeader(String employeeName) {
        String nameTextInHeader = header.getText();
       return nameTextInHeader.contains(employeeName);
    }

    public boolean checkBodyOfVacationsSection() {
        if (driver.findElements(By.xpath("//tbody[@id='employee-vacations']/tr")).size() == 0) {
           return absentVacationsMessage.isDisplayed();
        } else {
           return vacationsTable.isDisplayed();
        }
    }
}
