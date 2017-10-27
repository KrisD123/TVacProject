import listeners.RunListener;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Random;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;


/**
 * Created by kdodonov on 29.09.2017.
 */
@Listeners(value = RunListener.class)
public class MyTeamTests extends BaseTest {
private static final Logger LOGGER = Logger.getLogger(MyTeamTests.class);

    @BeforeClass
    public void goToMyTeamSection() {
        login(userName, userPassword);
        tVacMainPage.openMyTeamSection();
    }

    @Test
    public void searchEmployeeBySurname() {
        LOGGER.info("Test for search of employee by surname");
        String employeeSurname = "Ещенко";
        myTeamPage.performSearch(employeeSurname);

        Assert.assertTrue(myTeamPage.checkEmployeeWithSurnameInResultList(employeeSurname), "Employee with given surname is not found");

    }

    @Test
    public void searchEmployeeByProject() {
        LOGGER.info("Test for search of employees by project");
        String projectNameToSearch = "CIAM Test";
        myTeamPage.performSearch(projectNameToSearch);
        SoftAssert softAssert = new SoftAssert();
        List<WebElement> resultsList = myTeamPage.getSearchResultList();
        for (WebElement searchResultElement : resultsList) {
            softAssert.assertTrue(myTeamPage.checkSearchResultByProject(projectNameToSearch, searchResultElement));
        }
        softAssert.assertAll();
        LOGGER.info("All employees in the search results are from the project: " + projectNameToSearch);

    }

    @Test
    public void searchNotExistingEmployee() {
        LOGGER.info("Test for search of non existing employee");
        String employeeSurname = "SomeEmployee";
        myTeamPage.performSearch(employeeSurname);
        Assert.assertEquals(myTeamPage.getEmptySearchResultMessage(), "Ничего не найдено");

    }

    @Test
    public void addEmployeeToFavourites() {
        LOGGER.info("Test for favourite mark for employees");
        String favouriteEmployeeName = myTeamPage.getRandomEmployeeName();
        //add chosen employee to favourites and check that this employee is marked as favourite
        myTeamPage.clickFavouriteForEmployee(favouriteEmployeeName);
        Assert.assertTrue(myTeamPage.checkEmployeeIsMarkedAsFavourite(favouriteEmployeeName));
        LOGGER.info("Employee is marked as favourite");
        //go to the main page and return to the section My Team
        tVacMainPage.openHomePage();
        tVacMainPage.openMyTeamSection();
        //check that chosen employee is still marked as favourite
        Assert.assertTrue(myTeamPage.checkEmployeeIsMarkedAsFavourite(favouriteEmployeeName));
        LOGGER.info("Employee is marked as favourite");
        //Postconditions: delete favoutite mark from employee
        myTeamPage.clickFavouriteForEmployee(favouriteEmployeeName);
    }

    @Test
    public void viewEmployeesVacations() {
        LOGGER.info("Test to verify vacations view of employees");
        String employeeToViewVacationsName = myTeamPage.openVacationsOfRandomEmployee();
        //check that correct page is open and employee name is displayed
        Assert.assertTrue(employeesVacationPage.checkEmployeeVacationPageIsOpen());
        Assert.assertTrue(employeesVacationPage.checkEmployeeNameIsDisplayedInHeader(employeeToViewVacationsName));

        //if table with vacations is absent check that there is corresponding message, otherwise check that table is present
        Assert.assertTrue(employeesVacationPage.checkBodyOfVacationsSection());
        myTeamPage.openMyTeamSection();
    }
}
