package com.uniovi.sdi.sdi2425entrega153;

import com.uniovi.sdi.sdi2425entrega153.pageobjects.PO_LoginView;
import com.uniovi.sdi.sdi2425entrega153.pageobjects.PO_PrivateView;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2425Entrega153ApplicationTests {

	static String PathFirefox = "C:\\Users\\Usuario\\AppData\\Local\\Mozilla Firefox\\firefox.exe";
	static String Geckodriver = "C:\\Users\\Usuario\\Desktop\\uni\\SDI\\PL-SDI-Sesión6-material\\geckodriver-v0.30.0-win64.exe";
	static WebDriver driver = getDriver(PathFirefox, Geckodriver);
	static String URL = "http://localhost:8100";

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
}
