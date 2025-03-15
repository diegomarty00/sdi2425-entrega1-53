package com.uniovi.sdi.sdi2425entrega153.pageobjects;

import com.uniovi.sdi.sdi2425entrega153.util.SeleniumUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class PO_PrivateView extends PO_NavView {

    static public void registerUser(WebDriver driver) {
        goToUserLink(driver, "register");
        //Ahora vamos a rellenar los datos del usuario
        String checkText = "Lamine";
        fillFormRegisterUser(driver, "99887766A", checkText, "Yamal");
        //Esperamos a que se muestren los enlaces de paginación de la lista de usuarios
        List<WebElement> elements = PO_View.checkElementBy(driver, "free", "//a[contains(@class, 'page-link')]");
        //Nos vamos a la última página
        elements.getLast().click();
        //Comprobamos que aparece el usuario en la página
        elements = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, elements.get(0).getText());
    }

    static public void listUsers(WebDriver driver) {
        goToUserLink(driver, "list");

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