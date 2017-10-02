import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;


/**
 * Created by kdodonov on 29.09.2017.
 */
public class MyTeamTests extends BaseTest {

    WebElement searchField;

    @BeforeClass
    public void openMyTeamSection() {
        login(userName, userPassword);

        WebElement loggedUserName = driver.findElement(By.xpath("//div[@class='menu-heading btn-brand']"));
        loggedUserName.click();
        driver.findElement(By.linkText("Моя команда")).click();
        searchField = driver.findElement(By.cssSelector(".form-input"));
    }

    @Test
    public void searchEmployeeBySurname() {

        String employeeSurname = "Ещенко";
        searchField.clear();
        searchField.sendKeys(employeeSurname);
        List<WebElement> resultList = driver.findElements(By.xpath(String.format("//span[contains(text(), '%s')]", employeeSurname)));
        Assert.assertTrue(resultList.size() == 1, "Employee with given surname is not found");

    }

    @Test
    public void searchEmployeeByProject() {

        String project = "CCP";
        String employeeSurname = "Вильданова";
        searchField.clear();
        searchField.sendKeys(project);
        List<WebElement> resultList = driver.findElements(By.xpath(String.format("//span[contains(text(), '%s')]//following::td[div[contains(text(), '%s')]]", employeeSurname, project)));
        Assert.assertTrue(resultList.size() == 1, "Employee with given project is not found");

    }

    @Test
    public void searchNotExistingEmployee() {

        String employeeSurname = "SomeEmployee";
        searchField.clear();
        searchField.sendKeys(employeeSurname);
        WebElement emptyTableMessage = driver.findElement(By.className("dataTables_empty"));
        Assert.assertEquals(emptyTableMessage.getText(), "Ничего не найдено");

    }
}
