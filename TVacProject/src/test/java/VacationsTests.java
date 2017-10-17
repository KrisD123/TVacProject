import ObjectModel.Vacation;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

/**
 * Created by kdodonov on 06.10.2017.
 */
public class VacationsTests extends BaseTest {

    private static String VACATION_LINE_IN_TABLE_XPATH = "//tbody[@id='employeeVacations']/tr[contains(@class, 'vacation-row')]";
    private static String ADD_VACATION_BUTTON_XPATH = "//button[contains(text(), 'новый отпуск')]";

    private int numberOfAlreadyAddedVacations;

    @BeforeClass
    public void goToAddVacationPage() {
        login(userName, userPassword);
        numberOfAlreadyAddedVacations = driver.findElements(By.xpath(VACATION_LINE_IN_TABLE_XPATH)).size();
    }

    @Test
    public void addNewVacation() {
        Vacation vacation = new Vacation();
        vacation.setVacationType("За свой счет");
        vacation.setStatus("Не утвержден");

        //fill in data of new vacation and save changes
        wait.until(elementToBeClickable(By.xpath(ADD_VACATION_BUTTON_XPATH))).click();
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

        String IdOfLastAddedVacation = getLastAddedVacationId();

        //Check that status of newly added vacation is correct
        WebElement statusOfLastAddedVacation = wait.until(visibilityOfElementLocated(By.xpath(String.format("//tbody[@id='employeeVacations']/tr[@data-vacation-id='%s']/td/div[@class='vacation-status']", IdOfLastAddedVacation))));
        Assert.assertEquals(vacation.getStatus(), statusOfLastAddedVacation.getText());

        //delete last added vacation
        deleteVacation(IdOfLastAddedVacation);


    }

    @Test
    public void checkVisibilityOfMyVacationsSection() {
        WebElement notificationInfo = driver.findElement(By.id("notificationInfo"));
        Assert.assertFalse(notificationInfo.isDisplayed());

        driver.findElement(By.id("showNotificationInfo")).click();
        Assert.assertTrue(notificationInfo.isDisplayed());
    }

    @Test
    public void checkAddNewVacationInterface() {
        //check that 'Add vacation' button is enabled
        WebElement addVacationButton = wait.until(elementToBeClickable(By.xpath(ADD_VACATION_BUTTON_XPATH)));
        Assert.assertTrue(addVacationButton.isEnabled());
        //check that 'Add vacation' button is disabled after it was clicked
        addVacationButton.click();
        Assert.assertFalse(addVacationButton.isEnabled());
        //check that 'Add vacation' button is enabled after 'Delete' button was clicked
        wait.until(elementToBeClickable(By.xpath(String.format("//tbody[@id='employeeVacations']/tr[@data-vacation-id='%s']/td/div/button[@title='Удалить']", "")))).click();
        Assert.assertTrue(addVacationButton.isEnabled());

        //check that clendar is displayed after clicking on 'Start date' and 'End date' fields
        addVacationButton.click();
        Actions actions = new Actions(driver);
        WebElement startDateInput = wait.until(visibilityOfElementLocated(By.xpath("//input[contains(@class, 'start-date') and not(@disabled)]")));
        actions.click(startDateInput).perform();
        WebElement calendar = wait.until(visibilityOfElementLocated(By.className("datepicker-days")));
        Assert.assertTrue(calendar.isDisplayed());

        WebElement endDateInput = wait.until(visibilityOfElementLocated(By.xpath("//input[contains(@class, 'end-date') and not(@disabled)]")));
        actions.click(startDateInput).perform();
        calendar = wait.until(visibilityOfElementLocated(By.className("datepicker-days")));
        Assert.assertTrue(calendar.isDisplayed());

        //check that after some date is chosen in the 'Start date' field the same date is displayed in the 'End date' field
        chooseAnyDateFromCalendar(startDateInput);

        String startDateValue = startDateInput.getAttribute("value");
        String endDateValue = endDateInput.getAttribute("value");
        Assert.assertEquals(startDateValue, endDateValue);

        //check that tooltips are correct
        String cancelButtonTooltip = driver.findElement(By.xpath("//button[contains (@class, 'cancel') and not(@disabled)]")).getAttribute("title");
        String removeButtonTooltip = driver.findElement(By.xpath("//button[@class='btn btn-default btn-icon remove' and not(@disabled)]")).getAttribute("title");
        String saveButtonTooltip = driver.findElement(By.xpath("//button[contains (@class, 'save') and not(@disabled)]")).getAttribute("title");

        Assert.assertEquals(cancelButtonTooltip, "Отменить");
        Assert.assertEquals(removeButtonTooltip, "Удалить");
        Assert.assertEquals(saveButtonTooltip, "Сохранить");

    }

    @Test
    public void downloadVacation() throws InterruptedException {
        WebElement downloadButton = wait.until(elementToBeClickable(By.xpath("//tr/td/div[text()='Заявление принято']/../following-sibling::td/div/a")));
        downloadButton.click();
        Thread.sleep(3000);
        File directory = new File("C:\\Users\\kdodonov\\Desktop\\WorkDocs\\TA Selenium\\TempForDownload");
        Assert.assertTrue(checkFileWasDownloaded(directory, "Заявление_и_Приказ_о_предоставлении_отпуска.docx"));

        emptyFolderAfterTheTest(directory);

    }

    private boolean checkFileWasDownloaded(File directory, String fileName) {
        File[] files = directory.listFiles();
        for (File fil : files) {
            if (fil.getName().equals(fileName)) {
                System.out.println("File was found");
                return true;
            }
        }
        return false;
    }

    private void emptyFolderAfterTheTest(File directory) {
        File[] allFiles = directory.listFiles();
        for (File file : allFiles) {
            file.delete();
        }


    }


    protected void chooseAnyDateFromCalendar(WebElement calendarField) {


        Actions actions = new Actions(driver);
        actions.click(calendarField)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.RETURN)
                .build()
                .perform();
    }

    protected void deleteVacation(String idOfVacationToDelete) {
        wait.until(elementToBeClickable(By.xpath(String.format("//tbody[@id='employeeVacations']/tr[@data-vacation-id='%s']/td/div/button[@title='Удалить']", idOfVacationToDelete)))).click();
        wait.until(elementToBeClickable(By.xpath("//button[@class='btn btn-default remove-vacation-button']"))).click();
    }

    protected String getLastAddedVacationId() {
        List<WebElement> vacationIDs = driver.findElements(By.xpath("//tbody[@id='employeeVacations']/tr/td/div[@class='text-center vacation-id']"));
        List<String> vacationIDsText = new ArrayList<>();
        //get list of IDs of all vacations to determine the last added one
        for (WebElement element : vacationIDs) {
            vacationIDsText.add(element.getText());
        }
        //sort the list of IDs
        Collections.sort(vacationIDsText, Collections.<String>reverseOrder());

        //Get maximum ID
        return vacationIDsText.get(0);
    }
}
