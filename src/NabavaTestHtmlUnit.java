import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;


public class NabavaTestHtmlUnit {

	static String testingAcc = "testingAccount@mailinator.com";
	static String testingPass = "Olp0zdUlDT";

	public static void main(String[] args) {

	}

	public static void WishListTest() {
		WebDriver driver = new HtmlUnitDriver();
		((HtmlUnitDriver) driver).setJavascriptEnabled(true);
		
		NabavaLogin(driver, testingAcc, testingPass);
		giveTimeToLoad(5);
		fillWishlist(driver);
		giveTimeToLoad(2);
		emptyWishlist(driver);
	}

	public static void emptyWishlist(WebDriver driver) {
		driver.get("http://www.nabava.net/lista_zelja.php");
		
		assert driver.getCurrentUrl().equals("http://www.nabava.net/lista_zelja.php") : "Nije pronadjena lista zelja";
		
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='content']/form/table/tbody/tr[2]/td[5]/a"))).click();
		driver.navigate().refresh();
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='content']/form/table/tbody/tr[2]/td[5]/a"))).click();
		driver.navigate().refresh();
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='content']/form/table/tbody/tr[2]/td[5]/a"))).click();
		driver.navigate().refresh();
		
		giveTimeToLoad(5);
		String potvrda = driver.findElement(By.xpath(".//*[@id='content']/p[2]")).getText();
		
		assert potvrda.equals("Va�a lista �elja trenutno je prazna.") : "Brisanje nije uspjelo";
		System.out.println("Uspjesno brisanje iz liste!");
	}

	public static void fillWishlist(WebDriver driver) {
		giveTimeToLoad(3);

		assert driver.getCurrentUrl().equals("http://www.nabava.net/login") : "Login nije uspio";

		driver.get("http://www.nabava.net/prijenosna-racunala__30");
		giveTimeToLoad(10);
		
		WebElement proizvod = driver.findElement(By.xpath(".//*[@id='forma']/ul/li[3]/ul[1]/li[2]/div[2]/h3/a"));
		
		String idProizvod = proizvod.toString();
		idProizvod = idProizvod.substring(idProizvod.indexOf("Product name click - "));
		idProizvod = idProizvod.substring(21, 27);

		String fullId = "proizvod" + idProizvod;

		String parametar = "NABAVA.listaZeljaUpdate('proizvod=" + idProizvod + "', '" + fullId + "', 'search');";
		
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(parametar);
		}
		
		driver.navigate().refresh();
		
		String temp = driver.findElement(By.xpath(".//*[@id='headnav2']/li[1]/a/span")).getText();
		assert temp.equals("1") : "Nije unesen proizvod!";
		
		driver.findElement(By.xpath(".//*[@id='forma']/ul/li[3]/ul[1]/li[4]/div[2]/h3/a")).click();
		
		proizvod = driver.findElement(By.xpath(".//*[@id='ponude']/li/ul/li[1]/ul/li[3]/div/ol/li/a"));
		parametar = proizvod.toString();
		parametar = parametar.substring(parametar.indexOf("onclick=") + 9, parametar.indexOf(" title=") - 1);

		if(driver instanceof JavascriptExecutor) {
			((JavascriptExecutor) driver).executeScript(parametar);
		}
		
		driver.navigate().refresh();
		
		temp = driver.findElement(By.xpath(".//*[@id='headnav2']/li[1]/a/span")).getText();
		assert temp.equals("2") : "Nije unesen artikl!";
		
		giveTimeToLoad(3);
		
		driver.findElement(By.id("s")).sendKeys("lenovo g500");
		driver.findElement(By.id("s")).sendKeys(Keys.RETURN);
		
		giveTimeToLoad(10);
		
		String strBrojRezultata = driver.findElement(By.xpath(".//*[@id='forma']/ul/li[2]/p")).getText();
		strBrojRezultata = strBrojRezultata.substring(16);
		int brojRezultata = Integer.parseInt(strBrojRezultata);
		
		String klasa;
		
		for(int i = 1; i <= brojRezultata; i++) {
			klasa = driver.findElement(By.xpath(".//*[@id='forma']/ul/li[3]/ul/li[" + (i * 2) + "]/ul/li[3]/a")).toString();

			if(klasa.contains("invisible")) {
				parametar = driver.findElement(By.xpath(".//*[@id='forma']/ul/li[3]/ul/li[" + ((i * 2) - 1) + "]/div[2]/ol/li/a")).toString();
				parametar = parametar.substring(parametar.indexOf("onclick=") + 9, parametar.indexOf("title=") - 2);
				
				if(driver instanceof JavascriptExecutor) {
					
					((JavascriptExecutor) driver).executeScript(parametar);
				}
				break;
			}
		}
		
		driver.navigate().refresh();
		giveTimeToLoad(5);
		
		driver.get("http://www.nabava.net/lista_zelja.php");
		
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='main']/div[3]/div[2]/ul/li/ul/li[2]/a/span")));
		temp = driver.findElement(By.xpath(".//*[@id='main']/div[3]/div[2]/ul/li/ul/li[2]/a/span")).getText();
		
		assert temp.equals("3") : "Nije unesen nekategorizirani artikl!";
		System.out.println("Uspjesan unos!");
	}

	public static void CreateAccount() {
		WebDriver driver = new HtmlUnitDriver();

		String mail = GetEmail(driver);
		NabavaRegister(driver, mail);
		String pass = GetPassword(driver);
		NabavaLogin(driver, mail, pass);
		CheckLoginSuccess(driver);
	}

	public static String GetEmail(WebDriver driver) {
		driver.get("http://temp-mail.org/");

		String temp = driver.findElement(By.id("mail")).getAttribute("value");
		return temp;
	}

	public static String GetPassword(WebDriver driver) {
		driver.get("http://temp-mail.org/");

		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Nabava.net"))).click();
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/div[1]/div/div/div[2]/div/div/div[3]/p[2]/b")));

		String pass = driver.findElement(By.xpath("html/body/div[1]/div/div/div[2]/div/div/div[3]/p[2]/b")).getText();
		return pass;
	}

	public static void NabavaRegister(WebDriver driver, String mail) {
		driver.get("http://www.nabava.net");
		driver.findElement(By.xpath(".//*[@id='headnav2']/li[2]/a")).click();

		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='loginForm']/form/fieldset/ol[3]/li/strong/a"))).click();

		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
		Date datum = new Date();
		String username = "test";
		username += dateFormat.format(datum);

		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='screenName']"))).sendKeys(username);
		driver.findElement(By.xpath(".//*[@id='email']")).sendKeys(mail);
		driver.findElement(By.xpath(".//*[@id='email']")).sendKeys(Keys.RETURN);

		giveTimeToLoad(5);
	}

	public static void NabavaLogin(WebDriver driver, String mail, String pass) {
		driver.get("http://www.nabava.net");

		driver.findElement(By.xpath(".//*[@id='headnav2']/li[2]/a")).click();

		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='j_username']"))).sendKeys(mail);
		driver.findElement(By.xpath(".//*[@id='j_password']")).sendKeys(pass);

		WebElement chkbxZapamtiMe;
		chkbxZapamtiMe = driver.findElement(By.xpath(".//*[@id='rememberme']"));
		if (chkbxZapamtiMe.isSelected()) {
			chkbxZapamtiMe.click();
		}

		driver.findElement(By.xpath(".//*[@id='submit']")).click();
	}

	public static void CheckLoginSuccess(WebDriver driver) {
		giveTimeToLoad(10);

		try {
			driver.findElement(By.xpath(".//*[@id='headnav2']/li[2]/a")).click();
			System.out.println("Registracija uspjesna!");
		} catch (Exception e) {
			System.out.println("Neuspjesna registracija.");
		}
	}

	public static void giveTimeToLoad(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}