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
        numberOfAlreadyAddedVacations = myVacationsPage.getNumberOfVacations();
    }

    @Test
    public void addNewVacation() {
        Vacation vacation = new Vacation();
        vacation.setVacationType("За свой счет");
        vacation.setStatus("Не утвержден");

        //fill in data of new vacation and save changes
        myVacationsPage.addNewVacation(vacation);

        //check that new vacation was added (number of vacations in the table is numberOfAlreadyAddedVacations + 1)
        int newNumberOfVacations = myVacationsPage.getNumberOfVacations();
        Assert.assertEquals(newNumberOfVacations, numberOfAlreadyAddedVacations + 1);

        String IdOfLastAddedVacation = myVacationsPage.getLastAddedVacationId();

        //Check that status of newly added vacation is correct
        Assert.assertEquals(vacation.getStatus(), myVacationsPage.getVacationStatus(IdOfLastAddedVacation));

        //delete last added vacation
        myVacationsPage.deleteVacation(IdOfLastAddedVacation);


    }

    @Test
    public void checkVisibilityOfMyVacationsSection() {

        Assert.assertFalse(myVacationsPage.isNotificationInfoDisplayed());

        myVacationsPage.openNotificationInfoSection();
        Assert.assertTrue(myVacationsPage.isNotificationInfoDisplayed());
    }

    @Test
    public void checkAddNewVacationInterface() {
        //check that 'Add vacation' button is enabled
        Assert.assertTrue(myVacationsPage.isAddVacationButtonEnabled());

        //check that 'Add vacation' button is disabled after it was clicked
        myVacationsPage.clickAddVacationButton();
        Assert.assertFalse(myVacationsPage.isAddVacationButtonEnabled());
        //check that clendar is displayed after clicking on 'Start date' and 'End date' fields
        myVacationsPage.clickOnStartDateField();
        Assert.assertTrue(myVacationsPage.isCalendarDisplayed());

        myVacationsPage.clickOnEndDateField();
        Assert.assertTrue(myVacationsPage.isCalendarDisplayed());

        //check that after some date is chosen in the 'Start date' field the same date is displayed in the 'End date' field
        myVacationsPage.chooseStartDate();

        String startDateValue = myVacationsPage.getValueOfStartDateField();
        String endDateValue = myVacationsPage.getValueOfEndDateField();
        Assert.assertEquals(startDateValue, endDateValue);

        //check that tooltips are correct
        String cancelButtonTooltip = myVacationsPage.getCancelButtonTooltip();
        String removeButtonTooltip = myVacationsPage.getRemoveButtonTooltip();
        String saveButtonTooltip = myVacationsPage.getSaveButtonTooltip();

        Assert.assertEquals(cancelButtonTooltip, "Отменить");
        Assert.assertEquals(removeButtonTooltip, "Удалить");
        Assert.assertEquals(saveButtonTooltip, "Сохранить");

        //check that 'Add vacation' button is enabled after 'Delete' button was clicked
        myVacationsPage.clickOnDeleteVacationButton("");
        Assert.assertTrue(myVacationsPage.isAddVacationButtonEnabled());
    }

    @Test
    public void downloadVacation() throws InterruptedException {
        myVacationsPage.downloadVacation();
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
}
