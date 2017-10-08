import ObjectModel.Vacation;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

/**
 * Created by kdodonov on 06.10.2017.
 */
public class VacationsTests extends BaseTest {

    private static String VACATION_LINE_IN_TABLE_XPATH = "//tbody[@id='employeeVacations']/tr[contains(@class, 'vacation-row')]";

    private int numberOfAlreadyAddedVacations;

    @BeforeClass
    public void goToAddVacationPage() {
        login(userName, userPassword);
        numberOfAlreadyAddedVacations = driver.findElements(By.xpath(VACATION_LINE_IN_TABLE_XPATH)).size();
        wait.until(elementToBeClickable(By.xpath("//button[contains(text(), 'новый отпуск')]"))).click();
    }

    @Test
    public void addNewVacation() {
        Vacation vacation = new Vacation();
        vacation.setVacationType("За свой счет");
        vacation.setStatus("Не утвержден");

        //fill in data of new vacation and save changes
        WebElement typeDropdown = wait.until(visibilityOfElementLocated(By.xpath("//select[not(@disabled)]")));
        Select select = new Select((typeDropdown));
        select.selectByVisibleText(vacation.getVacationType());
        WebElement startDateInput = wait.until(visibilityOfElementLocated(By.xpath("//input[contains(@class, 'start-date') and not(@disabled)]")));
        chooseAnyDateFromCalendar(startDateInput);
        WebElement endDateInput = wait.until(visibilityOfElementLocated(By.xpath("//input[contains(@class, 'end-date') and not(@disabled)]")));
        chooseAnyDateFromCalendar(endDateInput);
        WebElement saveButton = wait.until(elementToBeClickable(By.xpath("//button[@title='Сохранить' and not(contains(@style, 'display: none'))]")));
        saveButton.click();

        //check that new vacation was added (number of vacations in the table is numberOfAlreadyAddedVacations + 1)
        int newNumberOfVacations = wait.until(visibilityOfAllElementsLocatedBy(By.xpath(VACATION_LINE_IN_TABLE_XPATH))).size();
        Assert.assertEquals(newNumberOfVacations, numberOfAlreadyAddedVacations + 1);

        //Check that status of newly added vacation is correct
        WebElement statusOfLastAddedVacation = wait.until(visibilityOfElementLocated(By.xpath("//tbody[@id='employeeVacations']/tr[1]/td/div[@class='vacation-status']")));
        Assert.assertEquals(vacation.getStatus(), statusOfLastAddedVacation.getText());

        //delete last added vacation
        wait.until(elementToBeClickable(By.xpath("//tbody[@id='employeeVacations']/tr[1]/td/div/button[@title='Удалить']"))).click();
        wait.until(elementToBeClickable(By.xpath("//button[@class='btn btn-default remove-vacation-button']"))).click();


    }

    protected void chooseAnyDateFromCalendar(WebElement calendarField) {


        Actions actions = new Actions(driver);
        actions.click(calendarField)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.RETURN)
                .build()
                .perform();
    }
}
