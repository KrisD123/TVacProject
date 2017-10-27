package pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import javax.xml.ws.handler.LogicalHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

/**
 * Created by kdodonov on 24.10.2017.
 */
public class MyTeamPage extends TVacMainPage {
    private static final Logger LOGGER = Logger.getLogger(MyTeamPage.class);
    private static String EMPLOYEE_TABLE_LINE_XPATH = "//table[@id='employee-table']//tbody/tr";
    private static String FAVOURITES_MARK_XPATH = "//table[@id='employee-table']//tbody/tr/td[span[text()='%s']]/preceding-sibling::td/i";

    @FindBy(className = "form-input")
    WebElement searchField;
    @FindBy(className = "dataTables_empty")
    WebElement emptySearchResults;

    public MyTeamPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, 10);
    }

    public void performSearch(String searchParameter) {
        LOGGER.info("Search employee by value: " + searchParameter);
        searchField.clear();
        searchField.sendKeys(searchParameter);
    }

    public boolean checkEmployeeWithSurnameInResultList(String employeeSurname) {
        LOGGER.info("Check that employee with surname: " + employeeSurname + " is present in the results list");
        List<WebElement> resultList = driver.findElements(By.xpath(String.format("//span[contains(text(), '%s')]", employeeSurname)));
        if (resultList.size() == 1) {
            LOGGER.info("Employee with surname " + employeeSurname + " is present in the results list");
            return true;
        } else {
            LOGGER.info("Employee with surname " + employeeSurname + " is not present in the results list");
            return false;
        }

    }

    public boolean checkSearchResultByProject(String projectNameToSearch, WebElement resultListItem) {
        LOGGER.info("Check that employee in the results list is from correct project");
        //in the line with results find project row
        WebElement projectNameInResults = resultListItem.findElement(By.xpath(".//td[5]/div[@class='projects']"));
        //get visible name of the project
        String projectNameInResultsText = projectNameInResults.getText();
        //if correct name is visible return true
        if (projectNameInResultsText.contains(projectNameToSearch)) {
            LOGGER.info("Employee is from project: " + projectNameInResultsText);
            return true;
        }
        // check projects in Popup and if required project is present in popup return true, else return false
        else {
            Actions actions = new Actions(driver);
            actions.moveToElement(projectNameInResults).build().perform();
            List<WebElement> projectNamesInPopup = projectNameInResults.findElements(By.xpath(".//following-sibling::div/div/span"));
            for (WebElement projectNameInPopup : projectNamesInPopup) {
                if (projectNameInPopup.getAttribute("innerText").equals(projectNameToSearch)) {
                    LOGGER.info("Employee is from project: " + projectNameInPopup.getAttribute("innerText"));
                    return true;
                }
            }
            LOGGER.info("Employee is not from correct project");
            return false;
        }
    }

    public List<WebElement> getSearchResultList() {
        return driver.findElements(By.xpath(EMPLOYEE_TABLE_LINE_XPATH));

    }

    public String getEmptySearchResultMessage() {
        String emptySearchResultsText = emptySearchResults.getText();
        LOGGER.info("The message about empty search result is: " + emptySearchResultsText);
        return emptySearchResultsText;
    }

    public void clickFavouriteForEmployee(String employeeName) {
        //add chosen employee to favourites and check that this employee is marked as favourite
        LOGGER.info("Click on 'Favoutite' for employee with name: " + employeeName);
        WebElement favouritesMark = wait.until(visibilityOfElementLocated(By.xpath(String.format(FAVOURITES_MARK_XPATH, employeeName))));
        favouritesMark.click();
    }

    public String getRandomEmployeeName() {
        LOGGER.info("Get random employee name from the list");
        //find all employees on the page
        List<WebElement> employeesList = getSearchResultList();
        //choose random employee for test
        int randomIndex = new Random().nextInt(employeesList.size() - 1);
        WebElement employeeToFavaurites = employeesList.get(randomIndex);
        String randomEmployeeName = employeeToFavaurites.findElement(By.xpath(".//td/span")).getText();
        LOGGER.info("Random employee name from the list ia: " + randomEmployeeName);
        return randomEmployeeName;
    }

    public boolean checkEmployeeIsMarkedAsFavourite(String employeeName) {
        LOGGER.info("Check that employee with name: " + employeeName + " is marked as favourite");
        return wait.until(attributeContains(By.xpath(String.format(FAVOURITES_MARK_XPATH, employeeName)), "class", "icon-solid"));
    }

    public String openVacationsOfRandomEmployee() {
        LOGGER.info("Open vacations of random employee");
        //find all employees on the page
        List<WebElement> employeesList = getSearchResultList();
        //choose random employee for test
        int randomIndex = new Random().nextInt(employeesList.size() - 1);
        WebElement employeeToOpen = employeesList.get(randomIndex);
        String employeeToOpenName = employeeToOpen.findElement(By.xpath(".//td/span")).getText();
        LOGGER.info("Random eployee name: " + employeeToOpenName);
        employeeToOpen.click();
        LOGGER.info("Vacations page was open");
        return employeeToOpenName;
    }


}
