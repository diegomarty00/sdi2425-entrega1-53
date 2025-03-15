package com.uniovi.sdi.sdi2425entrega153.pageobjects;

import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.util.SeleniumUtils;
import org.openqa.selenium.support.ui.Select;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_PrivateView extends PO_NavView {

    static public void registerUser(WebDriver driver) {
        goToUserLink(driver, "register");
        //Ahora vamos a rellenar los datos del usuario
        String checkText = "Lamine";
        fillFormRegisterUser(driver, "99887766A", checkText, "Yamal");
        //Esperamos a que se muestren los enlaces de paginación de la lista de usuarios
        List<WebElement> elements = PO_View.checkElementBy(driver, "free",
                "//a[contains(@class, 'page-link')]");
        //Nos vamos a la última página
        elements.getLast().click();
        //Comprobamos que aparece el usuario en la página
        elements = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, elements.get(0).getText());
    }

    static public void registerUserError(WebDriver driver, String dnip, String namep, String lastNamep,
                                         String expectedError) {
        goToUserLink(driver, "register");
        //Ahora vamos a rellenar los datos del usuario
        fillFormRegisterUser(driver, dnip, namep, lastNamep);
        List<WebElement> elements = PO_View.checkElementBy(driver, "text", expectedError);
        Assertions.assertTrue(elements.getFirst().getText().contains(expectedError));
    }

    static public void listUsers(WebDriver driver, List<User> users) {
        goToUserLink(driver, "list");
        for(User user : users) {
            if(!findUser(driver, user)) Assertions.fail("User not found");
        }
    }

    static public String[] editUser(WebDriver driver, List<User> users) {
        goToUserLink(driver, "list");
        List<WebElement> elements = PO_View.checkElementBy(driver, "free",
                "//tbody/tr[td[.//text()[contains(., '" + users.getFirst().getDni() + "')]]]/td[last()]/a");
        elements.getFirst().click();
        String dnip = "66778899A";
        String namep = "Ilyas";
        String lastNamep = "Chaira";
        int roleOrder = 1;

        fillFormEditUser(driver, dnip, namep, lastNamep, roleOrder);

        elements = PO_View.checkElementBy(driver, "text", dnip);
        Assertions.assertEquals(dnip, elements.getFirst().getText());
        elements = PO_View.checkElementBy(driver, "text", namep);
        Assertions.assertEquals(namep, elements.getFirst().getText());
        elements = PO_View.checkElementBy(driver, "text", lastNamep);
        Assertions.assertEquals(lastNamep, elements.getFirst().getText());

        return new String[]{dnip, namep, lastNamep, "ROLE_ADMIN"};
    }

    static public void editUserError(WebDriver driver, String dniToEdit, String dnip, String namep, String lastNamep,
                                     String... errors) {
        goToUserLink(driver, "list");
        List<WebElement> elements = PO_View.checkElementBy(driver, "free",
                "//tbody/tr[td[.//text()[contains(., '" + dniToEdit + "')]]]/td[last()]/a");
        elements.getFirst().click();

        fillFormEditUser(driver, dnip, namep, lastNamep, 1);

        for(String error : errors) {
            elements = PO_View.checkElementBy(driver, "text", error);
            Assertions.assertTrue(elements.getFirst().getText().contains(error));
        }
    }

    static public void changePassword(WebDriver driver, String oldPassword, String newPassword) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "free",
                "//*[@id='my-navbarColor02']/ul[2]/li[2]/a");
        elements.getFirst().click();
        fillFormChangaPassword(driver, oldPassword, newPassword, newPassword);
    }

    static public void changePasswordError(WebDriver driver, String oldPassword, String newPassword,
                                           String passwordConfirm, String error) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "free",
                "//*[@id='my-navbarColor02']/ul[2]/li[2]/a");
        elements.getFirst().click();
        fillFormChangaPassword(driver, oldPassword, newPassword, passwordConfirm);

        elements = PO_View.checkElementBy(driver, "text", error);
        Assertions.assertTrue(elements.getFirst().getText().contains(error));
    }

    static private boolean findUser(WebDriver driver, User user) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "text", user.getDni());
        if(elements.getFirst().getText().equals(user.getDni())) {
            Assertions.assertEquals(user.getDni(), elements.getFirst().getText());
            return true;
        } else {
            elements = PO_View.checkElementBy(driver, "free",
                    "//div[@class='text-center']/ul[@class='pagination justify-content-center']/li[4]/a");
            elements.getFirst().click();
            return findUser(driver, user);
        }
    }

    static private void fillFormChangaPassword(WebDriver driver, String oldPasswordp, String newPasswordp,
                                               String passwordConfirmp) {
        SeleniumUtils.waitSeconds(driver, 5);
        WebElement oldPassword = driver.findElement(By.name("oldPassword"));
        oldPassword.clear();
        oldPassword.sendKeys(oldPasswordp);
        WebElement newPassword = driver.findElement(By.name("password"));
        newPassword.click();
        newPassword.clear();
        newPassword.sendKeys(newPasswordp);
        WebElement passwordConfirm = driver.findElement(By.name("passwordConfirm"));
        passwordConfirm.click();
        passwordConfirm.clear();
        passwordConfirm.sendKeys(passwordConfirmp);
        By button = By.className("btn");
        driver.findElement(button).click();
    }

    static private void fillFormEditUser(WebDriver driver, String dnip, String namep, String lastNamep, int roleOrder) {
        SeleniumUtils.waitSeconds(driver, 5);
        WebElement dni = driver.findElement(By.name("dni"));
        dni.clear();
        dni.sendKeys(dnip);
        WebElement name = driver.findElement(By.name("name"));
        name.click();
        name.clear();
        name.sendKeys(namep);
        WebElement lastName = driver.findElement(By.name("lastName"));
        lastName.click();
        lastName.clear();
        lastName.sendKeys(lastNamep);
        new Select(driver.findElement(By.name("role"))).selectByIndex(roleOrder);
        By button = By.className("btn");
        driver.findElement(button).click();
    }

    static private void fillFormRegisterUser(WebDriver driver, String dnip, String namep, String lastNamep) {
        //Esperamos 5 segundo a que carge el DOM porque en algunos equipos falla
        SeleniumUtils.waitSeconds(driver, 5);
        //Rellenemos el campo de dni
        WebElement dni = driver.findElement(By.name("dni"));
        dni.clear();
        dni.sendKeys(dnip);
        WebElement name = driver.findElement(By.name("name"));
        name.click();
        name.clear();
        name.sendKeys(namep);
        WebElement lastName = driver.findElement(By.name("lastName"));
        lastName.click();
        lastName.clear();
        lastName.sendKeys(lastNamep);
        By button = By.className("btn");
        driver.findElement(button).click();
    }

    static private void goToUserLink(WebDriver driver, String link) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "free",
                "//*[@id='my-navbarColor02']/ul[1]/li[2]");
        elements.getFirst().click();
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@href, 'user/" + link + "')]");
        elements.getFirst().click();
    }
}