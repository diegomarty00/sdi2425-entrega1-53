package com.uniovi.sdi.sdi2425entrega153.pageobjects;

import com.uniovi.sdi.sdi2425entrega153.entities.User;
import com.uniovi.sdi.sdi2425entrega153.entities.Vehicle;
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

    static public void registerVehicle(WebDriver driver) {
        goToVehicleLink(driver, "register");
        String checkText = "1234ABC";
        fillFormRegisterVehicle(driver, checkText, "11111111111111111", "Toyota","qwe");

        List<WebElement> elements = PO_View.checkElementBy(driver, "free",
                "//a[contains(@class, 'page-link')]");

        elements.getLast().click();
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

    static public void registerVehicleError(WebDriver driver, String plateP, String chassisP, String brandP, String modelP, String expectedError) {
        goToVehicleLink(driver, "register");
        fillFormRegisterVehicle(driver, plateP, chassisP, brandP, modelP);
        List<WebElement> elements = PO_View.checkElementBy(driver, "text", expectedError);
        Assertions.assertTrue(elements.getFirst().getText().contains(expectedError));
    }


    static public void listUsers(WebDriver driver, List<User> users) {
        goToUserLink(driver, "list");
        for(User user : users) {
            if(!findUser(driver, user)) Assertions.fail("User not found");
        }
    }

    static public void listVehicles(WebDriver driver, List<Vehicle> vehicles) {
        goToVehicleLink(driver, "list");
        for(Vehicle vehicle : vehicles) {
            if (!findVehicle(driver, vehicle)) Assertions.fail("Vehicle not found");
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

    static private boolean findVehicle(WebDriver driver, Vehicle vehicle) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "text", vehicle.getPlate());

        if(elements.getFirst().getText().equals(vehicle.getPlate())) {
            Assertions.assertEquals(vehicle.getPlate(), elements.getFirst().getText());
            return true;
        } else {
            elements = PO_View.checkElementBy(driver, "free",
                    "//div[@class='text-center']/ul[@class='pagination justify-content-center']/li[4]/a");
            elements.getFirst().click();
            return findVehicle(driver, vehicle);
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

    static private void fillFormRegisterVehicle(WebDriver driver, String plateP, String chassisP, String brandP, String modelP) {
        SeleniumUtils.waitSeconds(driver, 5);
        WebElement plate = driver.findElement(By.name("plate"));
        plate.clear();
        plate.sendKeys(plateP);
        WebElement chassis = driver.findElement(By.name("chassisNumber"));
        chassis.clear();
        chassis.sendKeys(chassisP);
        WebElement brand = driver.findElement(By.name("brandName"));
        brand.clear();
        brand.sendKeys(brandP);
        WebElement model = driver.findElement(By.name("model"));
        model.clear();
        model.sendKeys(modelP);

        WebElement fuelDropdown = driver.findElement(By.name("fuelType"));
        Select selectFuel = new Select(fuelDropdown);

        // Seleccionar la primera opción después de la opción vacía
        if (selectFuel.getOptions().size() > 1) {
            selectFuel.selectByIndex(1); // La opción en índice 0 es "Seleccione un tipo"
        }

        By button = By.className("btn");
        driver.findElement(button).click();

    }

    public static void goToVehicleLink(WebDriver driver, String link) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "free",
                "//*[@id='my-navbarColor02']/ul[1]/li[3]");
        elements.getFirst().click();
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@href, 'vehicle/" + link + "')]");
        elements.getFirst().click();
    }

    static private void goToUserLink(WebDriver driver, String link) {
        List<WebElement> elements = PO_View.checkElementBy(driver, "free",
                "//*[@id='my-navbarColor02']/ul[1]/li[2]");
        elements.getFirst().click();
        elements = PO_View.checkElementBy(driver, "free", "//a[contains(@href, 'user/" + link + "')]");
        elements.getFirst().click();
    }

    public static WebElement selectVehicleCheckbox(WebDriver driver, int index) {
        return driver.findElements(By.name("selectedVehicles")).get(index);
    }

    public static void deleteVehicle(WebDriver driver) {
        // Click en el botón de eliminar vehículos
        WebElement deleteButton = driver.findElement(By.xpath("//form[@id='deleteForm']/button"));
        deleteButton.click();
    }

    public static void verifyVehicleDisappeared(WebDriver driver, String vehiclePlate) {
        List<WebElement> vehicles = driver.findElements(By.xpath("//table[@id='vehiclesTable']//tbody//tr"));
        boolean found = false;

        for (WebElement vehicle : vehicles) {
            WebElement plateElement = vehicle.findElement(By.xpath("td[2]"));
            if (plateElement.getText().equals(vehiclePlate)) {
                found = true;
                break;
            }
        }
        Assertions.assertFalse(found, "El vehículo con matrícula " + vehiclePlate + " sigue en la lista.");
    }
}