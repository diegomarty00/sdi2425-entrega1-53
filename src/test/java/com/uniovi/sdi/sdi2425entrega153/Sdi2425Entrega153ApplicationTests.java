package com.uniovi.sdi.sdi2425entrega153;

import com.uniovi.sdi.sdi2425entrega153.entities.*;
import com.uniovi.sdi.sdi2425entrega153.pageobjects.*;
import com.uniovi.sdi.sdi2425entrega153.repositories.PathRepository;
import com.uniovi.sdi.sdi2425entrega153.services.PathService;
import com.uniovi.sdi.sdi2425entrega153.services.RefuelService;
import com.uniovi.sdi.sdi2425entrega153.services.UsersService;
import com.uniovi.sdi.sdi2425entrega153.services.VehicleService;
import org.hibernate.sql.Select;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2425Entrega153ApplicationTests {

	static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	//static String Geckodriver = "C:\\Users\\Usuario\\Desktop\\SDI\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
	static String Geckodriver = "C:\\Users\\Diego Marty\\Desktop\\Documentos\\Universidad\\SDI\\PL-SDI-Sesión6-material\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
	static WebDriver driver = getDriver(PathFirefox, Geckodriver);
	static String URL = "http://localhost:8100";
	@Autowired
	private PathService pathService;
	@Autowired
	private UsersService usersService;
    @Autowired
    private RefuelService refuelService;
    @Autowired
    private VehicleService vehicleService;

	public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckodriver);
		driver = new FirefoxDriver();
		return driver;
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
	@Order(24)
	void Prueba24() {
		String userDNI = "99999990A";
		PO_LoginView.login(driver, userDNI, "123456");
		//Entramos en el elemento de trayectos ([1]) y luego en el ver trayectos ([2])
		driver.get(URL + "/path/list");

		// Obtener datos esperados de la base de datos
		List<Path> expectedPaths = pathService.getPathsByUserDni(userDNI); // Ajusta según tu lógica de negocio

		// Obtener los elementos de la lista desde la página web
		List<WebElement> pathListRows = driver.findElements(By.xpath("//table[@id='pathsTable']/tbody/tr"));

		// Verificar que el tamaño de la lista coincide
		assertEquals(expectedPaths.size(), pathListRows.size(), "El número de trayectos no coincide");

		String[] parts;
		// Verificar contenido de cada trayecto
		for (int i = 0; i < expectedPaths.size(); i++) {
			parts = pathListRows.get(i).getText().split("\\s+");
			assertEquals(parts[0], expectedPaths.get(i).getOnlyDate(), "No coincide la fecha: " + i);
			assertEquals(parts[1], expectedPaths.get(i).getOnlyTime(), "No coincide la hora: " + i);
			assertEquals(parts[2], expectedPaths.get(i).getVehicleRegistration(), "No coincide la matricula: " + i);
			String expectedVehicle = expectedPaths.get(i).getUserDni();
			assertEquals(userDNI, expectedVehicle, "El empleado no coincide en la el conductor: " + i);
		}

		PO_LoginView.logout(driver);
	}

	@Test
	@Order(25)
	void testInicioTrayectoValido() throws Exception {
		// Login
		driver.get(URL + "/login");
		driver.findElement(By.id("username")).sendKeys("99999990A");
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		// Ir al formulario de inicio de trayecto
		driver.get(URL + "/path/start");
		Thread.sleep(1000);

		// Verifica que la opción "1234BCZ" NO aparece en el desplegable
		List<WebElement> options = driver.findElements(By.xpath("//select[@name='vehicleRegistration']/option"));
		boolean found = false;
		for (WebElement option : options) {
			if (option.getText().trim().equals("1234BCZ")) {
				found = true;
				break;
			}
		}
		Assertions.assertTrue(found, "El vehículo '1234BCZ' se añadio correctamente.");
	}

	@Test
	@Order(26)
	void testInicioTrayectoNoValidoConTrayectoActivo() throws Exception {
		// Preparar: Crear un trayecto activo para el vehículo "1234BCM" para el usuario "99999990A"
		pathService.createActivePathForVehicle("1234BCM", "99999990A");

		// Login con un usuario que ya tiene trayecto activo
		driver.get(URL + "/login");
		driver.findElement(By.id("username")).sendKeys("99999990A");
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		// Ir al formulario de inicio de trayecto
		driver.get(URL + "/path/start");
		Thread.sleep(1000);

		// Verificar que la opción "1234BCM" no aparezca en el desplegable
		List<WebElement> options = driver.findElements(By.xpath("//select[@name='vehicleRegistration']/option"));
		boolean found = false;
		for (WebElement option : options) {
			System.out.println("Opción encontrada: " + option.getText().trim());
			if (option.getText().trim().equals("1234BCM")) {
				found = true;
				break;
			}
		}
		// Aserción: La opción "1234BCM" no debe estar en el desplegable
		assertFalse(found, "La opción '1234BCM' no debería estar en el desplegable, ya que el vehículo está en uso.");

		// Cierra sesión (opcional, si existe el botón o enlace para logout)
		// driver.findElement(By.id("logoutButton")).click();
	}

	@Test
	@Order(27)
	void testInicioTrayectoNoValidoVehiculoEnUso() throws Exception {
		// Configuración: Crear un trayecto activo para el vehículo "1234BCZ"
		// (Simula que el vehículo está en uso, con un DNI diferente al del usuario que se va a loguear)
		pathService.createActivePathForVehicle("1234BCZ", "99999990A");

		// Login con un usuario de prueba (que no tiene trayecto activo)
		driver.get(URL + "/login");
		driver.findElement(By.id("username")).sendKeys("99999990A");
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		// Navega a la página de inicio de trayecto
		driver.get(URL + "/path/start");
		Thread.sleep(1000); // Espera a que se cargue el formulario

		// Obtener todas las opciones del select con name "vehicleRegistration"
		List<WebElement> options = driver.findElements(By.xpath("//select[@name='vehicleRegistration']/option"));
		boolean found = false;
		for (WebElement option : options) {
			System.out.println("Opción encontrada: " + option.getText().trim());
			if (option.getText().trim().equals("1234BCZ")) {
				found = true;
				break;
			}
		}
		// Verifica que la opción "1234BCZ" NO aparezca, ya que el vehículo está en uso.
		assertFalse(found, "La opción '1234BCZ' no debería estar en el desplegable, ya que el vehículo está en uso.");





	}
	@Test
	@Order(28)
	void testRepostajeValido() throws InterruptedException {
		// Preparar estado: Crear un trayecto activo para el usuario 10000001S y el vehículo "1111"
		// (Asegúrate de que "1111" existe en tu base de datos y que createActivePathForVehicle lo marque como activo)
		pathService.createActivePathForVehicle("1111", "99999990A");

		// 1. Iniciar sesión
		driver.get(URL + "/login");
		driver.findElement(By.id("username")).sendKeys("99999990A");
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		// 2. Navegar al formulario de repostaje
		driver.get(URL + "/refuel/new");
		Thread.sleep(1000);

		// 3. Llenar campos del formulario con datos válidos
		driver.findElement(By.id("stationName")).sendKeys("Estación X");
		driver.findElement(By.id("fuelPrice")).sendKeys("1.50");
		driver.findElement(By.id("fuelQuantity")).sendKeys("10");
		driver.findElement(By.id("odometer")).sendKeys("200"); // Debe ser > odómetro inicial del trayecto

		// 4. Enviar el formulario
		driver.findElement(By.id("submitRefuelButton")).click();

		// 5. Verificar que se ha registrado correctamente (p.ej., sin errorMessage)
		//    Dependiendo de tu implementación, podrías verificar la redirección o la ausencia de error:
		List<WebElement> errorElements = driver.findElements(By.id("errorMessage"));
		assertTrue(errorElements.isEmpty(), "No debería mostrarse un mensaje de error en repostaje válido.");

		// Opcional: Cerrar sesión
		// driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
	}
	@Test
	@Order(29)
	void testRepostajeNoHayTrayectoEnCurso() throws InterruptedException {
		// Asegurarnos de que NO hay trayecto activo para el usuario 99999990A
		// Podrías finalizar trayectos previos o no crear ninguno.

		// 1. Iniciar sesión
		driver.get(URL + "/login");
		driver.findElement(By.id("username")).sendKeys("99999990A");
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		// 2. Navegar al formulario de repostaje
		driver.get(URL + "/refuel/new");
		Thread.sleep(1000);

		// 3. Se espera un error directo, por ejemplo, "No tienes un trayecto en curso..."
		//    Dependiendo de tu implementación, quizás ni aparezca el formulario o aparezca un mensaje:
		String errorText = driver.findElement(By.id("errorMessage")).getText();
		assertTrue(errorText.contains("No tienes un trayecto en curso"),
				"Debe mostrarse error indicando que no hay trayecto en curso.");

		// Opcional: Cerrar sesión
		// driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
	}

	@Test
	@Order(30)
	void testRepostajeCamposVacios() throws InterruptedException {
		// Preparar estado: Crear un trayecto activo para 10000001S y vehículo "1111"
		pathService.createActivePathForVehicle("1111", "99999990A");

		// 1. Iniciar sesión
		driver.get(URL + "/login");
		driver.findElement(By.id("username")).sendKeys("99999990A");
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		// 2. Navegar al formulario de repostaje
		driver.get(URL + "/refuel/new");
		Thread.sleep(1000);

		// 3. Enviar el formulario sin llenar campos
		//    (nombre de la estación vacío, precio vacío, cantidad vacía, odómetro vacío)
		driver.findElement(By.id("submitRefuelButton")).click();

		// 4. Verificar que aparecen mensajes de error (o uno genérico) en "errorMessage"
		String errorText = driver.findElement(By.id("errorMessage")).getText();
		assertTrue(errorText.contains("El nombre de la estación es obligatorio.")
						&& errorText.contains("El precio debe ser un número positivo.")
						&& errorText.contains("La cantidad debe ser un número positivo.")
						&& errorText.contains("El odómetro debe ser mayor que el valor inicial del trayecto (0.0)."),
				"Deben mostrarse mensajes de error por campos vacíos.");

		// Opcional: Cerrar sesión
		// driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
	}
	/**
	 * [Prueba33] Registro de fin de trayecto válido.
	 * Se asume que el usuario "userActive" tiene un trayecto activo.
	 * Se rellena el odómetro con un valor mayor al inicio del trayecto.
	 */
	@Test
	@Order(33)
	void testFinTrayectoValido() throws InterruptedException {
		pathService.createActivePathForVehicle("1111", "99999990A");
		// 1. Login con un usuario que tenga un trayecto activo
		driver.get(URL + "/login");
		driver.findElement(By.id("username")).sendKeys("99999990A");
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		// 2. Ir a la vista de fin de trayecto
		driver.get(URL + "/path/end");
		Thread.sleep(1000);

		// 3. Rellenar el campo de odómetro final con un valor válido
		// Ajusta "finalOdometer" al id real del input de tu formulario
		driver.findElement(By.id("finalOdometer")).clear();
		driver.findElement(By.id("finalOdometer")).sendKeys("20000");

		// 4. Hacer click en el botón para finalizar
		// Ajusta "submitEndButton" al id real de tu botón
		driver.findElement(By.xpath("//button[text()='Finalizar Trayecto']")).click();

		// 5. Verificar que se ha redirigido o que aparece un mensaje de éxito
		// Por ejemplo, que la URL contiene "/path/personal"
		Assertions.assertTrue(driver.getCurrentUrl().contains("/path/end"));
	}

	/**
	 * [Prueba34] Registro de fin de trayecto inválido (odómetro vacío).
	 * Se asume que "userActive" tiene un trayecto activo,
	 * pero no se rellena el campo odómetro.
	 */
	@Test
	@Order(34)
	void testFinTrayectoOdometroVacio() throws InterruptedException {
		pathService.createActivePathForVehicle("1111", "99999990A");
		driver.get(URL + "/login");
		driver.findElement(By.id("username")).sendKeys("99999990A");
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		driver.get(URL + "/path/end");
		Thread.sleep(1000);

		// Dejar el campo de odómetro vacío
		driver.findElement(By.id("finalConsumption")).clear();

		driver.findElement(By.xpath("//button[text()='Finalizar Trayecto']")).click();


		// Verificar que sigue en la misma página y/o aparece un mensaje de error
		// Suponiendo que se muestra un elemento con id "errorMessage"
		String errorText = driver.findElement(By.id("errorMessage")).getText();
		Assertions.assertTrue(errorText.contains("El campo no puede ser vacio"));
	}

	/**
	 * [Prueba35] Registro de fin de trayecto inválido (odómetro negativo).
	 * Se asume que "userActive" tiene un trayecto activo,
	 * pero se rellena el campo con un valor negativo.
	 */
	@Test
	@Order(35)
	void testFinTrayectoOdometroNegativo() throws InterruptedException {
		driver.get(URL + "/login");
		driver.findElement(By.id("username")).sendKeys("99999990A");
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		driver.get(URL + "/path/end");
		Thread.sleep(1000);

		driver.findElement(By.id("finalOdometer")).clear();
		driver.findElement(By.id("finalOdometer")).sendKeys("-100");

		driver.findElement(By.xpath("//button[text()='Finalizar Trayecto']")).click();


		// Verificar el error
		String errorText = driver.findElement(By.id("errorMessage")).getText();
		Assertions.assertTrue(errorText.contains("El odómetro final debe ser mayor que el inicial"));
	}

	/**
	 * [Prueba36] Registro de fin de trayecto inválido (no hay trayectos en curso).
	 * Se asume que el usuario "userNoActive" no tiene un trayecto activo.
	 */
	@Test
	@Order(36)
	void testFinTrayectoSinTrayectoActivo() throws InterruptedException {
		driver.get(URL + "/login");
		driver.findElement(By.id("username")).sendKeys("99999990A");
		driver.findElement(By.id("password")).sendKeys("123456");
		driver.findElement(By.xpath("//button[text()='Login']")).click();

		driver.get(URL + "/path/end");
		Thread.sleep(1000);

		// Debería redirigir o mostrar un error indicando que no hay trayecto
		String errorText = driver.findElement(By.id("errorMessage")).getText();
		Assertions.assertTrue(errorText.contains("No tienes un trayecto en curso"));
	}

	@Test
	@Order(37)
	void Prueba37() {
		String userDNI = "99999990A";
		PO_LoginView.login(driver, userDNI, "123456");
		//Entramos en el elemento de trayectos ([1]) y luego en el ver trayectos ([2])
		driver.get(URL + "/vehicle/free");

		// Obtener los elementos de la lista desde la página web
		List<WebElement> vehicleListRows = driver.findElements(By.xpath("//table[@id='vehiclesTable']/tbody/tr"));

		String[] vehiculo;
		// Verificar contenido de cada trayecto
		for (int i = 0; i < vehicleListRows.size(); i++) {
			vehiculo = vehicleListRows.get(i).getText().split("\\s+");
			List<Path> expectedPaths = pathService.getPathsByVehicle(vehiculo[0]); // Ajusta según tu lógica de negocio
			if (!expectedPaths.isEmpty()) {
				driver.get(URL + "/vehicle/paths/"+expectedPaths.get(0).getId());

				List<WebElement> pathListRows = driver.findElements(By.xpath("//table[@id='pathsTable']/tbody/tr"));

				String[] parts;
				for (int j = 0; j < pathListRows.size(); j++) {
					parts = pathListRows.get(j).getText().split("\\s+");
					assertEquals(parts[0], expectedPaths.get(j).getOnlyDate(), "No coincide la fecha: " + j);
					assertEquals(parts[1], expectedPaths.get(j).getOnlyTime(), "No coincide la hora: " + j);
					assertEquals(vehiculo[0], expectedPaths.get(j).getVehicleRegistration(), "No coincide la matricula: " + i);
				}
			}
		}
		PO_LoginView.logout(driver);
	}

	@Test
	@Order(38)
	void Prueba38() {
		String userDNI = "99999990A";
		PO_LoginView.login(driver, userDNI, "123456");
		//Entramos en el elemento de trayectos ([1]) y luego en el ver trayectos ([2])
		driver.get(URL + "/vehicle/free");

		// Obtener los elementos de la lista desde la página web
		List<WebElement> vehicleListRows = driver.findElements(By.xpath("//table[@id='vehiclesTable']/tbody/tr"));

		String[] vehiculo;
		// Verificar contenido de cada trayecto
		for (int i = 0; i < vehicleListRows.size(); i++) {
			vehiculo = vehicleListRows.get(i).getText().split("\\s+");
			List<Refuel> expectedRefuels = refuelService.getRefulsByVehicle(vehiculo[0]); // Ajusta según tu lógica de negocio
			if (!expectedRefuels.isEmpty()) {
				driver.get(URL + "/vehicle/paths/"+expectedRefuels.get(0).getId());

				List<WebElement> refuelListRows = driver.findElements(By.xpath("//table[@id='refuelsTable']/tbody/tr"));

				String[] refuels;
				for (int j = 0; j < refuelListRows.size(); j++) {
					refuels = refuelListRows.get(j).getText().split("\\s+");
					assertEquals(refuels[0], expectedRefuels.get(j).getOnlyDate(), "No coincide la fecha: " + j);
					assertEquals(refuels[1], expectedRefuels.get(j).getOnlyTime(), "No coincide la hora: " + j);
					assertEquals(vehiculo[0], expectedRefuels.get(j).getVehicleRegistration(), "No coincide la matricula: " + i);
				}
			}
		}
		PO_LoginView.logout(driver);
	}

	@Test
	@Order(39)
	void Prueba39() {
		String userDNI = "99999990A";
		PO_LoginView.login(driver, userDNI, "123456");
		//Entramos en el elemento de trayectos ([1]) y luego en el ver trayectos ([2])
		driver.get(URL + "/vehicle/free");

		// Obtener los elementos de la lista desde la página web
		List<WebElement> vehicleListRows = driver.findElements(By.xpath("//table[@id='vehiclesTable']/tbody/tr"));

		driver.get(URL + "/vehicle/free?page=1");
		vehicleListRows.addAll(driver.findElements(By.xpath("//table[@id='vehiclesTable']/tbody/tr")));

		driver.get(URL + "/vehicle/free?page=2");
		vehicleListRows.addAll(driver.findElements(By.xpath("//table[@id='vehiclesTable']/tbody/tr")));

		List<Vehicle> vehicles = vehicleService.findAll();
		List<Vehicle> result = new ArrayList<Vehicle>();
		assertTrue(vehicles.size() > vehicleListRows.size());

		for (int i = 0; i < vehicles.size(); i++) {
			boolean found = false;
			String matricula = vehicleListRows.get(i).getText().split("\\s+")[0];
			for (int j = 0; j < vehicleListRows.size(); j++) {
				if (matricula.equals(vehicles.get(j).getPlate())) {
					found = true;
				}
			}
			if (!found) {
				assertFalse(vehicleService.findByPlate(matricula).isFree());
			}
		}
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
