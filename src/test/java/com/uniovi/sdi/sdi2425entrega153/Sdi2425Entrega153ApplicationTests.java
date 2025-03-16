package com.uniovi.sdi.sdi2425entrega153;

import com.uniovi.sdi.sdi2425entrega153.pageobjects.*;
import com.uniovi.sdi.sdi2425entrega153.services.UsersService;
import com.uniovi.sdi.sdi2425entrega153.services.VehicleService;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.uniovi.sdi.sdi2425entrega153.pageobjects.PO_PrivateView.goToVehicleLink;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2425Entrega153ApplicationTests {

	//static String PathFirefox = "C:\\Users\\Usuario\\AppData\\Local\\Mozilla Firefox\\firefox.exe";
	//static String Geckodriver = "C:\\Users\\Usuario\\Desktop\\uni\\SDI\\PL-SDI-Sesión6-material\\geckodriver-v0.30.0-win64.exe";
	static String Geckodriver = "C:\\Users\\Usuario\\Documents\\ESTUDIOS UNIVERSIDAD\\SDI\\lab5\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";

	static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";

	static WebDriver driver = getDriver(PathFirefox, Geckodriver);
	//static WebDriver driver;
	static String URL = "http://localhost:8100";

	@Autowired
	private UsersService usersService;
    @Autowired
    private VehicleService vehicleService;

	@BeforeAll
	static public void setUpAll() {
		driver = getDriver(PathFirefox, Geckodriver);
	}

	@BeforeEach
	public void setUp() {
		driver = getDriver(PathFirefox, Geckodriver);
		driver.manage().window().maximize();
		driver.navigate().to(URL);
	}

	@AfterEach
	public void tearDown() {
		if (driver != null) {
			driver.manage().deleteAllCookies();
			driver.quit();
		}
	}

	@AfterAll
	static public void tearDownAll() {
		if (driver != null) {
			driver.quit();
		}
	}
	public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
		// Configurar las propiedades del sistema para Firefox y Geckodriver
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckodriver);

		// Crear un perfil limpio de Firefox
		FirefoxProfile profile = new FirefoxProfile();

		// Configurar opciones de Firefox
		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(profile); // Usar el perfil limpio
		options.addArguments("--private"); // Modo incógnito
		options.addArguments("--headless"); // Modo headless (sin interfaz gráfica)
		options.addArguments("--disable-gpu"); // Deshabilitar aceleración por hardware
		options.addArguments("--no-sandbox"); // Deshabilitar sandbox para mayor compatibilidad
		options.addArguments("--disable-dev-shm-usage"); // Evitar problemas de memoria en CI/CD

		// Crear y retornar la instancia de FirefoxDriver con las opciones configuradas
		return new FirefoxDriver(options);
	}
	/*
	public static WebDriver getDriver(String PathFirefox, String Geckodriver) {

		//System.setProperty("webdriver.firefox.bin", PathFirefox);
		//System.setProperty("webdriver.gecko.driver", Geckodriver);
		//driver = new FirefoxDriver();
		//return driver;

		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckodriver);

		// Crear opciones para Firefox
		FirefoxOptions options = new FirefoxOptions();

		// Usar perfil limpio o predeterminado sin datos previos
		FirefoxProfile profile = new FirefoxProfile();
		options.setProfile(profile);

		// Habilitar modo incógnito
		options.addArguments("-private");

		// Ejecutar Firefox en modo headless (sin UI)
		options.addArguments("--headless");

		// Crear y retornar el driver con las opciones configuradas
		return new FirefoxDriver(options);
	}

	@BeforeEach
	public void setUp(){
		driver.navigate().to(URL);
	}

	//Después de cada prueba se borran las cookies del navegador
	@AfterEach
	public void tearDown(){
		driver.manage().deleteAllCookies();

	}


	//Antes de la primera prueba
	@BeforeAll
	static public void begin() {}
	//Al finalizar la última prueba

	@AfterAll
	static public void end() {
		//Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}
	*/


	/*
		[Prueba1] Inicio de sesión con datos válidos (administrador).
	 */
	@Test
	@Order(1)
	void Prueba1() {
		//Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");

		//Rellenamos el formulario
		PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

		//Comprobamos que entramos en la pagina privada de Administrador
		String checkText = "Los usuarios que actualmente figuran en el sistema son los siguientes:";
		List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
		Assertions.assertEquals(checkText, result.get(0).getText());
	}

	/*
	[Prueba2] Inicio de sesión con datos válidos (empleado estándar)
	 */
	@Test
	@Order(2)
	void Prueba2() {
		//Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");

		//Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999990A", "123456");

		//Comprobamos que entramos en la pagina de listar trayectos
		//String checkText = ""; //cambiar mensaje cuando ya este /path/list
		//List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
		//Assertions.assertEquals(checkText, result.get(0).getText());
	}

	/*
	[Prueba3] Inicio de sesión con datos inválidos (empleado estándar, campo dni y contraseña vacíos).
	 */
	@Test
	@Order(3)
	void Prueba3() {
		//Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");

		//Rellenamos el formulario
		PO_LoginView.fillForm(driver, "99999990A", "zzz");

		//Comprobamos que seguimos en la pagina de identificacion
		String checkText = "Identifícate";
		List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
		Assertions.assertEquals(checkText, result.get(0).getText());
	}

	/*
	[Prueba4] Inicio de sesión con datos válidos (empleado estándar, dni existente, pero contraseña
		incorrecta).
	 */
	@Test
	@Order(4)
	void Prueba4() {
		//Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");

		//Rellenamos el formulario
		PO_LoginView.fillForm(driver, "", "");

		//Comprobamos que seguimos en la pagina de identificacion
		String checkText = "Identifícate";
		List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
		Assertions.assertEquals(checkText, result.get(0).getText());
	}

	/*
	[Prueba5] Hacer clic en la opción de salir de sesión y comprobar que se muestra el mensaje “Ha cerrado
		sesión correctamente” y se redirige a la página de inicio de sesión (Login).
	 */
	@Test
	@Order(5)
	void Prueba5() {
		//Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");

		//Rellenamos el formulario
		PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

		PO_LoginView.logout(driver);

		//Comprobamos que volvemos a la pagina de identificacion
		String checkText = "Identifícate";
		List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
		Assertions.assertEquals(checkText, result.get(0).getText());

		checkText = "Has cerrado sesión correctamente.";
		result = PO_View.checkElementBy(driver, "text", checkText);
		Assertions.assertEquals(checkText, result.get(0).getText());

	}

	/*
	[Prueba6] Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
	 */
	@Test
	@Order(6)
	void Prueba6() {
		//Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");

		//Rellenamos el formulario
		PO_LoginView.fillForm(driver, "12345678Z", "@Dm1n1str@D0r");

		//comprobbamos que podemos cerrar sesion al estar autenticados
		String checkText = PO_HomeView.getP().getString("logout.message", PO_Properties.getSPANISH());
		//String checkText = "logout.message";
		List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
		Assertions.assertEquals(checkText, result.get(0).getText());

		PO_LoginView.logout(driver);

		//comprobbamos que podemos iniciar sesion al no estar autenticados
		checkText = PO_HomeView.getP().getString("login.message", PO_Properties.getSPANISH());
		//checkText = "login.message";
		result = PO_View.checkElementBy(driver, "text", checkText);
		Assertions.assertEquals(checkText, result.get(0).getText());

		//checkText = "logout.message";
		checkText = PO_HomeView.getP().getString("logout.message", PO_Properties.getSPANISH());
		result = PO_View.checkElementBy(driver, "text", checkText);
		Assertions.assertNotEquals(checkText, result.get(0).getText()); //mirar que no existe el mensaje logout

	}

	/*
		[Prueba11] Registro de un Vehículos con datos válidos
	 */
	@Test
	@Order(11)
	void Prueba11() {

		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		PO_PrivateView.registerVehicle(driver);
		PO_LoginView.logout(driver);


	}
	/*
	[Prueba12] Registro de un Vehículos con datos inválidos (matrícula vacía, número de bastidor vacío,
		marca y modelo vacíos.
	 */
	@Test
	@Order(12)
	void Prueba12() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		String error = PO_HomeView.getP().getString("Error.empty", PO_Properties.getSPANISH());
		PO_PrivateView.registerVehicleError(driver, "", "", "","", error);
		PO_LoginView.logout(driver);
	}
	/*
	[Prueba13] Registro de un Vehículos con datos inválidos (formato de matrícula inválido).
	 */
	@Test
	@Order(13)
	void Prueba13() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		String error = PO_HomeView.getP().getString("Error.vehicle.plate.format", PO_Properties.getSPANISH());
		PO_PrivateView.registerVehicleError(driver, "1341341341341341", "11111111111111111", "toyota","qwe" ,error);
		PO_LoginView.logout(driver);
	}

	/*
	[Prueba14] Registro de un Vehículos con datos inválidos (longitud del número de bastidor inválido).
	 */
	@Test
	@Order(14)
	void Prueba14() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		String error = PO_HomeView.getP().getString("Error.vehicle.chassisNumber.length", PO_Properties.getSPANISH());
		PO_PrivateView.registerVehicleError(driver, "1234ABC", "1", "toyota","qwe" ,error);
		PO_LoginView.logout(driver);
	}

	/*
	[Prueba15] Registro de un Vehículos con datos inválidos (matrícula existente)

	 */
	@Test
	@Order(15)
	void Prueba15() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		String error = PO_HomeView.getP().getString("Error.vehicle.plate.duplicate", PO_Properties.getSPANISH());
		PO_PrivateView.registerVehicleError(driver, "1234BCD", "11111111111111111", "toyota","qwe" ,error);
		PO_LoginView.logout(driver);
	}

	/*
	[Prueba16] Registro de un Vehículos con datos inválidos (número de bastidor existente).
	 */
	@Test
	@Order(16)
	void Prueba16() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		String error = PO_HomeView.getP().getString("Error.vehicle.chassisNumber.repeated", PO_Properties.getSPANISH());
		PO_PrivateView.registerVehicleError(driver, "1234ABC", "12345678901234111", "toyota","qwe" ,error);
		PO_LoginView.logout(driver);
	}

	/*
	[Prueba20] Mostrar el listado de vehículos y comprobar que se muestran todos los que existen en el
sistema.
	 */
	@Test
	@Order(20)
	void Prueba20() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		PO_PrivateView.listVehicles(driver, vehicleService.getVehicles());
		PO_LoginView.logout(driver);
	}

	/*
	[Prueba21] Ir a la lista de vehículos, borrar el primer vehículo de la lista, comprobar que la lista se actualiza
		y dicho vehículo desaparece.
	 */
	@Test
	@Order(21)
	void Prueba21() {

		PO_PrivateView.goToVehicleLink(driver, "list");

		List<WebElement> vehicles = driver.findElements(By.xpath("//table[@id='vehiclesTable']//tbody//tr"));

		Assertions.assertTrue(vehicles.size() > 0, "No hay vehículos en la lista.");

		String firstVehiclePlate = vehicles.get(0).findElement(By.xpath("td[2]")).getText();

		PO_PrivateView.selectVehicleCheckbox(driver, 0).click();

		PO_PrivateView.deleteVehicle(driver);

		PO_PrivateView.verifyVehicleDisappeared(driver, firstVehiclePlate);
	}


	/*
	[Prueba22] Ir a la lista de vehículos, borrar el último vehículo de la lista, comprobar que la lista se actualiza
		y dicho vehículo desaparece.
	 */
	@Test
	@Order(22)
	void Prueba22() {

		goToVehicleLink(driver, "list");

		List<WebElement> vehicles = driver.findElements(By.xpath("//table[@id='vehiclesTable']//tbody//tr"));

		Assertions.assertTrue(vehicles.size() > 0, "No hay vehículos en la lista.");

		String lastVehiclePlate = vehicles.get(vehicles.size() - 1).findElement(By.xpath("td[2]")).getText();

		PO_PrivateView.selectVehicleCheckbox(driver, vehicles.size() - 1).click();

		PO_PrivateView.deleteVehicle(driver);

		PO_PrivateView.verifyVehicleDisappeared(driver, lastVehiclePlate);
	}

	/*
	[Prueba23] Ir a la lista de vehículos, borrar 3 vehículos, comprobar que la lista se actualiza y dichos
		vehículos desaparecen.
	 */
	@Test
	@Order(23)
	void Prueba23() {
		goToVehicleLink(driver, "list");

		// Obtener la lista de vehículos
		List<WebElement> vehicles = driver.findElements(By.xpath("//table[@id='vehiclesTable']//tbody//tr"));

		Assertions.assertTrue(vehicles.size() >= 3, "No hay suficientes vehículos en la lista.");

		for (int i = 0; i < 3; i++) {
			PO_PrivateView.selectVehicleCheckbox(driver, i).click();
		}

		String firstVehiclePlate = vehicles.get(0).findElement(By.xpath("td[2]")).getText();
		String secondVehiclePlate = vehicles.get(1).findElement(By.xpath("td[2]")).getText();
		String thirdVehiclePlate = vehicles.get(2).findElement(By.xpath("td[2]")).getText();

		// Eliminar los vehículos seleccionados
		PO_PrivateView.deleteVehicle(driver);

		// Verificar que los vehículos han desaparecido de la lista
		PO_PrivateView.verifyVehicleDisappeared(driver, firstVehiclePlate);
		PO_PrivateView.verifyVehicleDisappeared(driver, secondVehiclePlate);
		PO_PrivateView.verifyVehicleDisappeared(driver, thirdVehiclePlate);
	}


	@Test
	@Order(7)
	void Prueba7() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		PO_PrivateView.registerUser(driver);
		PO_LoginView.logout(driver);
	}

	@Test
	@Order(8)
	void Prueba8() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		String error = PO_HomeView.getP().getString("Error.empty", PO_Properties.getSPANISH());
		PO_PrivateView.registerUserError(driver, "", "", "", error);
		PO_LoginView.logout(driver);
	}

	@Test
	@Order(9)
	void Prueba9() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		String error = PO_HomeView.getP().getString("Error.register.dni.format", PO_Properties.getSPANISH());
		PO_PrivateView.registerUserError(driver, "1723098712", "Santiago", "Cazorla", error);
		PO_LoginView.logout(driver);
	}

	@Test
	@Order(10)
	void Prueba10() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		String error = PO_HomeView.getP().getString("Error.register.dni.duplicate", PO_Properties.getSPANISH());
		PO_PrivateView.registerUserError(driver, "12345678Z", "Santiago", "Cazorla", error);
		PO_LoginView.logout(driver);
	}




	@Test
	@Order(17)
	void Prueba17() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		PO_PrivateView.listUsers(driver, usersService.getUsers());
		PO_LoginView.logout(driver);
	}

	@Test
	@Order(18)
	void Prueba18() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		String[] values = PO_PrivateView.editUser(driver, usersService.getStandardUsers());
		PO_LoginView.logout(driver);
		PO_LoginView.login(driver, values[0], "123456");
		PO_PrivateView.listUsers(driver, usersService.getUsers());
		PO_LoginView.logout(driver);
	}

	@Test
	@Order(19)
	void Prueba19() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		String error1 = PO_HomeView.getP().getString("Error.register.dni.duplicate", PO_Properties.getSPANISH());
		String error2 = PO_HomeView.getP().getString("Error.empty", PO_Properties.getSPANISH());
		PO_PrivateView.editUserError(driver, "99887766A", "12345678Z", "", "", error1, error2);
		PO_LoginView.logout(driver);
	}

	@Test
	@Order(40)
	void Prueba40() {
		PO_LoginView.login(driver, "12345678Z", "@Dm1n1str@D0r");
		String newPassword = "a123456789A-";
		PO_PrivateView.changePassword(driver, "@Dm1n1str@D0r", newPassword);
		PO_LoginView.logout(driver);
		PO_LoginView.login(driver, "12345678Z", newPassword);
		PO_PrivateView.listUsers(driver, usersService.getUsers());
		PO_LoginView.logout(driver);
	}

	@Test
	@Order(41)
	void Prueba41() {
		PO_LoginView.login(driver, "12345678Z", "a123456789A-");
		String error = PO_HomeView.getP().getString("Error.password.change.incorrect", PO_Properties.getSPANISH());
		PO_PrivateView.changePasswordError(driver, "@Dm1n1str@D0r", "a123456789A-", "a123456789A-", error);
		PO_LoginView.logout(driver);
	}

	@Test
	@Order(42)
	void Prueba42() {
		PO_LoginView.login(driver, "12345678Z", "a123456789A-");
		String error = PO_HomeView.getP().getString("Error.password.change.weak", PO_Properties.getSPANISH());
		PO_PrivateView.changePasswordError(driver, "a123456789A-", "123456", "123456", error);
		PO_LoginView.logout(driver);
	}

	@Test
	@Order(43)
	void Prueba43() {
		PO_LoginView.login(driver, "12345678Z", "a123456789A-");
		String error = PO_HomeView.getP().getString("Error.password.change.different", PO_Properties.getSPANISH());
		PO_PrivateView.changePasswordError(driver, "a123456789A-", "123456789aA-", "123456789aA#", error);
		PO_LoginView.logout(driver);
	}
}
