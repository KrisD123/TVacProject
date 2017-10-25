import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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


    @BeforeClass
    public void goToMyTeamSection() {
        login(userName, userPassword);
        tVacMainPage.openMyTeamSection();
    }

    @Test
    public void searchEmployeeBySurname() {
        String employeeSurname = "Ещенко";
        myTeamPage.performSearch(employeeSurname);

        Assert.assertTrue(myTeamPage.checkEmployeeWithSurnameInResultList(employeeSurname), "Employee with given surname is not found");

    }

    @Test
    public void searchEmployeeByProject() {
        String projectNameToSearch = "CIAM Test";
        myTeamPage.performSearch(projectNameToSearch);
        SoftAssert softAssert = new SoftAssert();
        List<WebElement> resultsList = myTeamPage.getSearchResultList();
        for (WebElement searchResultElement : resultsList) {
            softAssert.assertTrue(myTeamPage.checkSearchResultByProject(projectNameToSearch, searchResultElement));
        }
        softAssert.assertAll();

    }

    @Test
    public void searchNotExistingEmployee() {
        String employeeSurname = "SomeEmployee";
        myTeamPage.performSearch(employeeSurname);
        Assert.assertEquals(myTeamPage.getEmptySearchResultMessage(), "Ничего не найдено");

    }

    @Test
    public void addEmployeeToFavourites() {
        String favouriteEmployeeName = myTeamPage.getRandomEmployeeName();
        //add chosen employee to favourites and check that this employee is marked as favourite
        myTeamPage.clickFavouriteForEmployee(favouriteEmployeeName);
        Assert.assertTrue(myTeamPage.checkEmployeeIsMarkedAsFavourite(favouriteEmployeeName));
        //go to the main page and return to the section My Team
        tVacMainPage.openHomePage();
        tVacMainPage.openMyTeamSection();
        //check that chosen employee is still marked as favourite
        Assert.assertTrue(myTeamPage.checkEmployeeIsMarkedAsFavourite(favouriteEmployeeName));
        //Postconditions: delete favoutite mark from employee
        myTeamPage.clickFavouriteForEmployee(favouriteEmployeeName);
    }

    @Test
    public void viewEmployeesVacations() {

        String employeeToViewVacationsName = myTeamPage.openVacationsOfRandomEmployee();
        //check that correct page is open and employee name is displayed
        Assert.assertTrue(employeesVacationPage.checkEmployeeVacationPageIsOpen());
        Assert.assertTrue(employeesVacationPage.checkEmployeeNameIsDisplayedInHeader(employeeToViewVacationsName));

        //if table with vacations is absent check that there is corresponding message, otherwise check that table is present
        Assert.assertTrue(employeesVacationPage.checkBodyOfVacationsSection());
        myTeamPage.openMyTeamSection();
    }
}
