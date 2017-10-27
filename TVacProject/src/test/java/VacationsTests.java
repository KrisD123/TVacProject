import ObjectModel.Vacation;
import listeners.RunListener;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
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
@Listeners(value = RunListener.class)
public class VacationsTests extends BaseTest {
    private static final Logger LOGGER = Logger.getLogger(VacationsTests.class);

    private int numberOfAlreadyAddedVacations;

    @BeforeClass
    public void goToAddVacationPage() {
        login(userName, userPassword);
        numberOfAlreadyAddedVacations = myVacationsPage.getNumberOfVacations();
    }

    @Test
    public void addNewVacation() {
        LOGGER.info("Test for creation of a new vacation");
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
        LOGGER.info("'Notification Info' is not displayed");
        myVacationsPage.openNotificationInfoSection();
        Assert.assertTrue(myVacationsPage.isNotificationInfoDisplayed());
        LOGGER.info("'Notification Info' is displayed");
    }

    @Test
    public void checkAddNewVacationInterface() {
        //check that 'Add vacation' button is enabled
        Assert.assertTrue(myVacationsPage.isAddVacationButtonEnabled());
        LOGGER.info("'Add' button is enabled");

        //check that 'Add vacation' button is disabled after it was clicked
        myVacationsPage.clickAddVacationButton();
        Assert.assertFalse(myVacationsPage.isAddVacationButtonEnabled());
        LOGGER.info("'Add' button is disabled");
        //check that clendar is displayed after clicking on 'Start date' and 'End date' fields
        myVacationsPage.clickOnStartDateField();
        Assert.assertTrue(myVacationsPage.isCalendarDisplayed());
        LOGGER.info("Calendar is displayed");
        myVacationsPage.clickOnEndDateField();
        Assert.assertTrue(myVacationsPage.isCalendarDisplayed());
        LOGGER.info("Calendar is displayed");
        //check that after some date is chosen in the 'Start date' field the same date is displayed in the 'End date' field
        myVacationsPage.chooseStartDate();

        LOGGER.info("Check that values in Start date and End Date fields are the same");
        String startDateValue = myVacationsPage.getValueOfStartDateField();
        String endDateValue = myVacationsPage.getValueOfEndDateField();
        Assert.assertEquals(startDateValue, endDateValue);
        LOGGER.info("Start date: " + startDateValue + ", End Date: " + endDateValue + ", values are the same");

        //check that tooltips are correct
        LOGGER.info("Check that tooltips have correct text");
        String cancelButtonTooltip = myVacationsPage.getCancelButtonTooltip();
        String removeButtonTooltip = myVacationsPage.getRemoveButtonTooltip();
        String saveButtonTooltip = myVacationsPage.getSaveButtonTooltip();

        Assert.assertEquals(cancelButtonTooltip, "Отменить");
        Assert.assertEquals(removeButtonTooltip, "Удалить");
        Assert.assertEquals(saveButtonTooltip, "Сохранить");

        LOGGER.info("Tooltips have correct text");
        //check that 'Add vacation' button is enabled after 'Delete' button was clicked
        myVacationsPage.clickOnDeleteVacationButton("");
        Assert.assertTrue(myVacationsPage.isAddVacationButtonEnabled());
        LOGGER.info("'Add' button is enabled");
    }

    @Test
    public void downloadVacation() throws InterruptedException {
        myVacationsPage.downloadVacation();
        File directory = new File("C:\\Users\\kdodonov\\Desktop\\WorkDocs\\TA Selenium\\TempForDownload");
        Assert.assertTrue(checkFileWasDownloaded(directory, "Заявление_и_Приказ_о_предоставлении_отпуска.docx"));
        LOGGER.info("File was successfully downloaded");
        emptyFolderAfterTheTest(directory);

    }

    private boolean checkFileWasDownloaded(File directory, String fileName) {
        File[] files = directory.listFiles();
        for (File fil : files) {
            if (fil.getName().equals(fileName)) {
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
