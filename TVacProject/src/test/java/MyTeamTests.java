import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Random;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;


/**
 * Created by kdodonov on 29.09.2017.
 */
public class MyTeamTests extends BaseTest {


    WebElement searchField;
    private static String EMPLOYEE_TABLE_LINE_XPATH = "//table[@id='employee-table']//tbody/tr";
    private static String FAVOURITES_MARK_XPATH = "//table[@id='employee-table']//tbody/tr/td[span[text()='%s']]/preceding-sibling::td/i";

    @BeforeClass
    public void goToMyTeamSection() {
        login(userName, userPassword);
        openMyTeamSection();
    }

    @Test
    public void searchEmployeeBySurname() {
        searchField = wait.until(visibilityOfElementLocated(By.cssSelector(".form-input")));
        String employeeSurname = "Ещенко";
        searchField.clear();
        searchField.sendKeys(employeeSurname);
        List<WebElement> resultList = driver.findElements(By.xpath(String.format("//span[contains(text(), '%s')]", employeeSurname)));
        Assert.assertTrue(resultList.size() == 1, "Employee with given surname is not found");
        searchField.clear();
        searchField.sendKeys(Keys.RETURN);

    }

    @Test
    public void searchEmployeeByProject() {
        searchField = wait.until(visibilityOfElementLocated(By.cssSelector(".form-input")));
        String projectNameToSearch = "CIAM Test";
        searchField.clear();
        searchField.sendKeys(projectNameToSearch);
        SoftAssert softAssert = new SoftAssert();
        List<WebElement> resultList = driver.findElements(By.xpath("//table[@id='employee-table']//tbody/tr"));
        for (WebElement element : resultList) {
            WebElement projectNameInResults = element.findElement(By.xpath(".//td[5]/div[@class='projects']"));
            String projectNameInResultsText = projectNameInResults.getText();
            System.out.println("Project Name in Results = " + projectNameInResultsText);
            if (!projectNameInResultsText.contains(projectNameToSearch)) {
                Actions actions = new Actions(driver);
                actions.moveToElement(projectNameInResults).build().perform();
                List<WebElement> projectNamesInPopup = projectNameInResults.findElements(By.xpath(".//following-sibling::div/div/span"));
                for (WebElement projectNameInPopup : projectNamesInPopup) {
                    System.out.println("Project Name in Popup = " + projectNameInPopup.getAttribute("innerText"));
                    if (projectNameInPopup.getAttribute("innerText").equals(projectNameToSearch)) {
                        projectNameInResultsText = projectNameInPopup.getAttribute("innerText");
                        break;
                    }
                }
            }
            softAssert.assertTrue(projectNameInResultsText.contains(projectNameToSearch));

        }
        softAssert.assertAll();
        searchField.clear();
        searchField.sendKeys(Keys.RETURN);
    }

    @Test
    public void searchNotExistingEmployee() {
        searchField = wait.until(visibilityOfElementLocated(By.cssSelector(".form-input")));
        String employeeSurname = "SomeEmployee";
        searchField.clear();
        searchField.sendKeys(employeeSurname);
        WebElement emptyTableMessage = driver.findElement(By.className("dataTables_empty"));
        Assert.assertEquals(emptyTableMessage.getText(), "Ничего не найдено");
        searchField.clear();
        searchField.sendKeys(Keys.RETURN);

    }

    @Test
    public void addEmployeeToFavourites() {
        //find all employees on the page
        List<WebElement> employeesList = wait.until(visibilityOfAllElementsLocatedBy(By.xpath(EMPLOYEE_TABLE_LINE_XPATH)));
        //choose random employee for test
        int randomIndex = new Random().nextInt(employeesList.size() - 1);
        WebElement employeeToFavaurites = employeesList.get(randomIndex);
        String favouriteEmployeeName = employeeToFavaurites.findElement(By.xpath(".//td/span")).getText();
        //add chosen employee to favourites and check that this employee is marked as favourite
        WebElement favouritesMark = wait.until(visibilityOfElementLocated(By.xpath(String.format(FAVOURITES_MARK_XPATH, favouriteEmployeeName))));
        favouritesMark.click();
        Assert.assertTrue(wait.until(attributeContains(By.xpath(String.format(FAVOURITES_MARK_XPATH, favouriteEmployeeName)), "class", "icon-solid")));
        //go to the main page and return to the section My Team
        wait.until(visibilityOfElementLocated(By.linkText("Главная"))).click();
        openMyTeamSection();
        //check that chosen employee is still marked as favourite
        Assert.assertTrue(wait.until(attributeContains(By.xpath(String.format(FAVOURITES_MARK_XPATH, favouriteEmployeeName)), "class", "icon-solid")));
        wait.until(visibilityOfElementLocated(By.xpath(String.format(FAVOURITES_MARK_XPATH, favouriteEmployeeName)))).click();
    }

    @Test
    public void viewEmployeesVacations() {
        //find all employees on the page
        List<WebElement> employeesList = wait.until(visibilityOfAllElementsLocatedBy(By.xpath(EMPLOYEE_TABLE_LINE_XPATH)));
        //choose random employee for test and open Vacations page for this employee
        int randomIndex = new Random().nextInt(employeesList.size() - 1);
        WebElement employeeToViewVacations = employeesList.get(randomIndex);
        String employeeToViewVacationsName = employeeToViewVacations.findElement(By.xpath(".//td/span")).getText();
        employeeToViewVacations.click();
        //check that correct page is open and employee name is displayed
        Assert.assertTrue(wait.until(titleIs("Список отпусков")));
        WebElement nameTextInHeader = wait.until(visibilityOfElementLocated(By.xpath("//div[@class='breadcrumbs']/div/span")));
        Assert.assertTrue(wait.until(textToBePresentInElement(nameTextInHeader, employeeToViewVacationsName)));

        //if table with vacations is absent check that there is corresponding message, otherwise check that table is present
        if (driver.findElements(By.xpath("//tbody[@id='employee-vacations']/tr")).size()==0) {
            Assert.assertNotNull(wait.until(visibilityOfElementLocated(By.xpath("//div[text()='Отпусков пока нет']"))));
        } else {
            Assert.assertNotNull(wait.until(visibilityOfElementLocated(By.xpath("//tbody[@id='employee-vacations']"))));
        }
        openMyTeamSection();
    }
}
