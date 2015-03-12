import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;

public class NabavaTestFirefox {

	static String testingAcc = "testingAccount@mailinator.com";
	static String testingPass = "Olp0zdUlDT";
	
	public static void main(String[] args) {
		
		WishListTest();
	}
	
	public static void WishListTest() {
		WebDriver driver = new FirefoxDriver();
		
		NabavaLogin(driver, testingAcc, testingPass);
		fillWishlist(driver);
		giveTimeToLoad(2);
		emptyWishlist(driver);
	}
	
	public static void emptyWishlist(WebDriver driver) {
		driver.get("http://www.nabava.net/lista_zelja.php");
		
		giveTimeToLoad(3);
		
		String tempXpath;
		
		for(int i = 1; i <= 3; i++) {
			tempXpath = ".//*[@id='content']/form/table/tbody/tr[2]/td[5]/a";
			driver.findElement(By.xpath(tempXpath)).click();
			driver.navigate().refresh();
			giveTimeToLoad(5);
		}
		
		
		String temp = driver.findElement(By.xpath(".//*[@id='content']/p[2]/strong")).getText();

		assert temp.equals("Vaša lista želja trenutno je prazna.") : "Greska u praznjenju liste zelja";
		System.out.println("Lista zelja radi ispravno!");
	}
	
	public static void fillWishlist(WebDriver driver) {
		giveTimeToLoad(3);
		
		assert driver.getCurrentUrl().equals("http://www.nabava.net/login") : "Login nije uspio";
		
		driver.findElement(By.xpath(".//*[@id='categoriesMenu']/li[7]/a")).click();
		driver.findElement(By.xpath(".//*[@id='categoriesMenu']/li[7]/a")).click();
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='main']/div[3]/div[2]/ul/li[1]/ul/li[5]/a"))).click();
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='main']/div[3]/div[2]/ul/li[1]/ul/li[1]/a"))).click();
		
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/div[3]/div[3]/div[1]/form/ul/li[3]/ul[1]/li[2]/div[4]/ol/li[4]/a"))).click();
		driver.findElement(By.xpath(".//*[@id='forma']/ul/li[3]/ul[1]/li[2]/div[2]/h3/a")).click();
		
		giveTimeToLoad(3);
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/div[3]/div[3]/div/ul/li/div/ul/li/ol/li/ul/li/ul/li[3]/div[2]/ol/li[1]/a"))).click();
		
		driver.findElement(By.id("s")).sendKeys("lenovo g500");
		driver.findElement(By.id("s")).sendKeys(Keys.RETURN);
		
		giveTimeToLoad(5);
		
		int listItemsCount = driver.findElements(By.className("offerToolbar")).size();
		String tempXpath;
		String className;
		
		for(int i = 1; i <= listItemsCount; i++) {
			tempXpath = ".//*[@id='forma']/ul/li[3]/ul/li[" + i * 2 + "]/ul/li[3]/a";
			className = driver.findElement(By.xpath(tempXpath)).getAttribute("class");
			
			if(className.equals("invisible")) {
				tempXpath = ".//*[@id='forma']/ul/li[3]/ul/li[" + ((i * 2) - 1) + "]/div[2]/ol/li[1]/a";
				driver.findElement(By.xpath(tempXpath)).click();
				break;
			}
		}
	}
	
	public static void CreateAccount() {
		WebDriver driver = new FirefoxDriver();
		
		String mail = GetEmail(driver);
		NabavaRegister(driver, mail);
		String pass = GetPassword(driver);
		NabavaLogin(driver, mail, pass);
		CheckLoginSuccess(driver);
	}
	
	public static String GetEmail(WebDriver driver) {
		driver.get("http://temp-mail.org/");
		
		System.out.println(driver.getCurrentUrl());
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
		if(chkbxZapamtiMe.isSelected()) {
			chkbxZapamtiMe.click();
		}
		
		driver.findElement(By.xpath(".//*[@id='submit']")).click();
	}
	
	public static void CheckLoginSuccess(WebDriver driver) {
		giveTimeToLoad(10);
		
		try {
			driver.findElement(By.xpath(".//*[@id='headnav2']/li[2]/a")).click();
			System.out.println("Registracija uspjesna!");
		}
		catch(Exception e) {
			System.out.println("Neuspjesna registracija.");
		}
	}
	
	public static void giveTimeToLoad(int sec) {
		try {
			Thread.sleep(sec * 1000);
		}
		catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}