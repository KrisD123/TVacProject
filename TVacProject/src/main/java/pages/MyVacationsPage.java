package pages;

import ObjectModel.Vacation;
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
        WebElement startDate = driver.findElement(By.xpath(START_DATE_FIELD_XPATH));
        chooseAnyDateFromCalendar(startDate);
    }

    public void chooseEndDate() {
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
        startDateField.click();
    }

    public void clickOnEndDateField() {
        endDateField.click();
    }

    public void deleteVacation(String idOfVacationToDelete) {
        clickOnDeleteVacationButton(idOfVacationToDelete);
        wait.until(elementToBeClickable(By.xpath(DELETE_VACATION_CONFIRMATION_XPATH))).click();
    }

    public String getLastAddedVacationId() {
        List<WebElement> vacationIDs = driver.findElements(By.xpath(VACATION_ID_XPATH));
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

    public void addNewVacation(Vacation vacation) {
        clickAddVacationButton();
        Select select = new Select(vacationTypeDropdown);
        select.selectByVisibleText(vacation.getVacationType());

        chooseStartDate();
        chooseEndDate();
        saveButton.click();
    }

    public int getNumberOfVacations() {
        return wait.until(visibilityOfAllElementsLocatedBy(By.xpath(VACATION_LINE_IN_TABLE_XPATH))).size();
    }

    public String getVacationStatus(String vacationId) {
        return wait.until(visibilityOfElementLocated(By.xpath(String.format(VACATION_STATUS_XPATH, vacationId)))).getText();
    }

    public boolean isNotificationInfoDisplayed() {
        return notificationInfoSection.isDisplayed();
    }

    public void openNotificationInfoSection() {
        showNotificationInfoButton.click();
    }

    public boolean isAddVacationButtonEnabled() {
        return addVacationButton.isEnabled();
    }

    public void clickAddVacationButton() {
        addVacationButton.click();
    }

    public boolean isCalendarDisplayed() {
        return calendar.isDisplayed();
    }

    public String getCancelButtonTooltip() {
       return cancelButtonTooltip.getAttribute("title");
    }

    public String getRemoveButtonTooltip() {
        return  removeButtonTooltip.getAttribute("title");
    }

    public String getSaveButtonTooltip() {
        return  saveButtonTooltip.getAttribute("title");
    }

    public void clickOnDeleteVacationButton(String vacationId) {

        wait.until(elementToBeClickable(By.xpath(String.format(DELETE_VACATION_BUTTON_XPATH, vacationId)))).click();
    }

    public void downloadVacation() throws InterruptedException {
        downloadVacationButton.click();
        Thread.sleep(3000);
    }
}
