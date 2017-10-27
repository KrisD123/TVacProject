package pages;

import ObjectModel.Vacation;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

/**
 * Created by kdodonov on 24.10.2017.
 */
public class MyVacationsPage extends TVacMainPage {
    private static final Logger LOGGER = Logger.getLogger(MyVacationsPage.class);
    private static final String VACATION_LINE_IN_TABLE_XPATH = "//tbody[@id='employeeVacations']/tr[contains(@class, 'vacation-row')]";
    private static final String ADD_VACATION_BUTTON_XPATH = "//button[contains(text(), 'новый отпуск')]";
    private static final String DELETE_VACATION_BUTTON_XPATH = "//tbody[@id='employeeVacations']/tr[@data-vacation-id='%s']/td/div/button[@title='Удалить']";
    private static final String DELETE_VACATION_CONFIRMATION_XPATH = "//button[@class='btn btn-default remove-vacation-button']";
    private static final String VACATION_ID_XPATH = "//tbody[@id='employeeVacations']/tr/td/div[@class='text-center vacation-id']";
    private static final String VACATION_STATUS_XPATH = "//tbody[@id='employeeVacations']/tr[@data-vacation-id='%s']/td/div[@class='vacation-status']";
    private static final String START_DATE_FIELD_XPATH = "//input[contains(@class, 'start-date') and not(@disabled)]";
    private static final String END_DATE_FIELD_XPATH = "//input[contains(@class, 'end-date') and not(@disabled)]";

    @FindBy(xpath = ADD_VACATION_BUTTON_XPATH)
    WebElement addVacationButton;
    @FindBy(xpath = "//select[not(@disabled)]")
    WebElement vacationTypeDropdown;
    @FindBy(xpath = "//input[contains(@class, 'start-date') and not(@disabled)]")
    WebElement startDateField;
    @FindBy(xpath = "//input[contains(@class, 'end-date') and not(@disabled)]")
    WebElement endDateField;
    @FindBy(xpath = "//button[@title='Сохранить' and not(contains(@style, 'display: none'))]")
    WebElement saveButton;
    @FindBy(id = "notificationInfo")
    WebElement notificationInfoSection;
    @FindBy(id = "showNotificationInfo")
    WebElement showNotificationInfoButton;
    @FindBy(className = "datepicker-days")
    WebElement calendar;
    @FindBy(xpath = "//button[contains (@class, 'cancel') and not(@disabled)]")
    WebElement cancelButtonTooltip;
    @FindBy(xpath = "//button[@class='btn btn-default btn-icon remove' and not(@disabled)]")
    WebElement removeButtonTooltip;
    @FindBy(xpath = "//button[contains (@class, 'save') and not(@disabled)]")
    WebElement saveButtonTooltip;
    @FindBy(xpath = "//tr/td/div[text()='Заявление принято']/../following-sibling::td/div/a")
    WebElement downloadVacationButton;

    public MyVacationsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, 10);
    }

    public void chooseAnyDateFromCalendar(WebElement calendarField) {

        Actions actions = new Actions(driver);
        actions.click(calendarField)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys(Keys.RETURN)
                .build()
                .perform();
    }

    public void chooseStartDate() {
        LOGGER.info("Select start date");
        WebElement startDate = driver.findElement(By.xpath(START_DATE_FIELD_XPATH));
        chooseAnyDateFromCalendar(startDate);
    }

    public void chooseEndDate() {
        LOGGER.info("Select end date");
        WebElement endDate = driver.findElement(By.xpath(END_DATE_FIELD_XPATH));
        chooseAnyDateFromCalendar(endDate);
    }

    public String getValueOfStartDateField() {

        return startDateField.getAttribute("value");

    }

    public String getValueOfEndDateField() {

        return endDateField.getAttribute("value");

    }
    public void clickOnStartDateField() {
        LOGGER.info("Click 'Start date'");
        startDateField.click();
    }

    public void clickOnEndDateField() {
        LOGGER.info("Click 'End date'");
        endDateField.click();
    }

    public void deleteVacation(String idOfVacationToDelete) {
        LOGGER.info("Delete vacation with id: " + idOfVacationToDelete);
        clickOnDeleteVacationButton(idOfVacationToDelete);
        wait.until(elementToBeClickable(By.xpath(DELETE_VACATION_CONFIRMATION_XPATH))).click();
    }

    public String getLastAddedVacationId() {
        LOGGER.info("Get id of last added vacation");
        List<WebElement> vacationIDs = driver.findElements(By.xpath(VACATION_ID_XPATH));
        List<String> vacationIDsText = new ArrayList<>();
        //get list of IDs of all vacations to determine the last added one
        for (WebElement element : vacationIDs) {
            vacationIDsText.add(element.getText());
        }
        //sort the list of IDs
        Collections.sort(vacationIDsText, Collections.<String>reverseOrder());

        //Get maximum ID
        LOGGER.info("Id of last added vacaion: " + vacationIDsText.get(0));
        return vacationIDsText.get(0);
    }

    public void addNewVacation(Vacation vacation) {
        LOGGER.info("Add new vacation");
        clickAddVacationButton();
        LOGGER.info("Select vacation type: " + vacation.getVacationType());
        Select select = new Select(vacationTypeDropdown);
        select.selectByVisibleText(vacation.getVacationType());

        chooseStartDate();
        chooseEndDate();
        LOGGER.info("Click 'Save' button");
        saveButton.click();
    }

    public int getNumberOfVacations() {
        return wait.until(visibilityOfAllElementsLocatedBy(By.xpath(VACATION_LINE_IN_TABLE_XPATH))).size();
    }

    public String getVacationStatus(String vacationId) {
        LOGGER.info("Getting status of vacation with id: " + vacationId);
        String vacationStatus = wait.until(visibilityOfElementLocated(By.xpath(String.format(VACATION_STATUS_XPATH, vacationId)))).getText();
        LOGGER.info("Status of vacation: " + vacationStatus);
        return vacationStatus;
    }

    public boolean isNotificationInfoDisplayed() {
        LOGGER.info("Check if 'Notification Info' section is displayed");
        return notificationInfoSection.isDisplayed();
    }

    public void openNotificationInfoSection() {
        LOGGER.info("Open 'Notification Info' section");
        showNotificationInfoButton.click();
    }

    public boolean isAddVacationButtonEnabled() {
        LOGGER.info("Check if 'Add' button is enabled");
        return addVacationButton.isEnabled();
    }

    public void clickAddVacationButton() {
        LOGGER.info("Click 'Add' button");
        addVacationButton.click();
    }

    public boolean isCalendarDisplayed() {
        LOGGER.info("Check if calendar is displayed");
        return calendar.isDisplayed();
    }

    public String getCancelButtonTooltip() {
        LOGGER.info("Get tooltip text of 'Cancel' button");
        String tooltipText = cancelButtonTooltip.getAttribute("title");
        LOGGER.info("Tooltip text: " + tooltipText);
       return tooltipText;
    }

    public String getRemoveButtonTooltip() {
        LOGGER.info("Get tooltip text of 'Remove' button");
        String tooltipText = removeButtonTooltip.getAttribute("title");
        LOGGER.info("Tooltip text: " + tooltipText);
        return  tooltipText;
    }

    public String getSaveButtonTooltip() {
        LOGGER.info("Get tooltip text of 'Save' button");
        String tooltipText = saveButtonTooltip.getAttribute("title");
        LOGGER.info("Tooltip text: " + tooltipText);
        return  tooltipText;
    }

    public void clickOnDeleteVacationButton(String vacationId) {
        LOGGER.info("Click on 'Delete' button");

        wait.until(elementToBeClickable(By.xpath(String.format(DELETE_VACATION_BUTTON_XPATH, vacationId)))).click();
    }

    public void downloadVacation() throws InterruptedException {
        LOGGER.info("Download vacation");
        downloadVacationButton.click();
        Thread.sleep(3000);
    }
}
