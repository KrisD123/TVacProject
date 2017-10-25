package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

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
        searchField.clear();
        searchField.sendKeys(searchParameter);
    }

    public boolean checkEmployeeWithSurnameInResultList(String employeeSurname) {
        List<WebElement> resultList = driver.findElements(By.xpath(String.format("//span[contains(text(), '%s')]", employeeSurname)));
        if (resultList.size() == 1) {
            return true;
        } else return false;

    }

    public boolean checkSearchResultByProject(String projectNameToSearch, WebElement resultListItem) {

        //in the line with results find project row
        WebElement projectNameInResults = resultListItem.findElement(By.xpath(".//td[5]/div[@class='projects']"));
        //get visible name of the project
        String projectNameInResultsText = projectNameInResults.getText();
        System.out.println("Project Name in Results = " + projectNameInResultsText);
        //if correct name is visible return true
        if (projectNameInResultsText.contains(projectNameToSearch)) {
            return true;
        }
        // check projects in Popup and if required project is present in popup return true, else return false
        else {
            Actions actions = new Actions(driver);
            actions.moveToElement(projectNameInResults).build().perform();
            List<WebElement> projectNamesInPopup = projectNameInResults.findElements(By.xpath(".//following-sibling::div/div/span"));
            for (WebElement projectNameInPopup : projectNamesInPopup) {
                System.out.println("Project Name in Popup = " + projectNameInPopup.getAttribute("innerText"));
                if (projectNameInPopup.getAttribute("innerText").equals(projectNameToSearch)) {
                    return true;
                }
            } return false;
        }
    }

    public List<WebElement> getSearchResultList() {
        return driver.findElements(By.xpath(EMPLOYEE_TABLE_LINE_XPATH));

    }

    public String getEmptySearchResultMessage() {
        return emptySearchResults.getText();
    }

    public void clickFavouriteForEmployee(String employeeName) {
        //add chosen employee to favourites and check that this employee is marked as favourite
        WebElement favouritesMark = wait.until(visibilityOfElementLocated(By.xpath(String.format(FAVOURITES_MARK_XPATH, employeeName))));
        favouritesMark.click();
    }
public String getRandomEmployeeName() {
    //find all employees on the page
    List<WebElement> employeesList = getSearchResultList();
    //choose random employee for test
    int randomIndex = new Random().nextInt(employeesList.size() - 1);
    WebElement employeeToFavaurites = employeesList.get(randomIndex);
    return employeeToFavaurites.findElement(By.xpath(".//td/span")).getText();
}
    public boolean checkEmployeeIsMarkedAsFavourite(String employeeName) {
        return wait.until(attributeContains(By.xpath(String.format(FAVOURITES_MARK_XPATH, employeeName)), "class", "icon-solid"));
    }

    public String openVacationsOfRandomEmployee() {
        //find all employees on the page
        List<WebElement> employeesList = getSearchResultList();
        //choose random employee for test
        int randomIndex = new Random().nextInt(employeesList.size() - 1);
        WebElement employeeToOpen = employeesList.get(randomIndex);
        String employeeToOpenName = employeeToOpen.findElement(By.xpath(".//td/span")).getText();
        employeeToOpen.click();
        return employeeToOpenName;
    }


}
