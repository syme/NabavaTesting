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

public class NabavaTestHtmlUnit {
	
	public static void main(String[] args) {

		WebDriver driver = new HtmlUnitDriver();
		
		String mail = GetEmail(driver);
		NabavaRegister(driver, mail);
		String pass = GetPassword(driver);
		NabavaLogin(driver, mail, pass);
	}
	
	public static String GetEmail(WebDriver driver) {
		driver.get("http://temp-mail.org/");
		
		System.out.println(driver.getCurrentUrl());
		String temp = driver.findElement(By.id("mail")).getAttribute("value");
		return temp;
	}
	
	public static String GetPassword(WebDriver driver) {
		driver.get("http://temp-mail.org/");
		
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("html/body/div[1]/div/div/div[2]/div/div/table/tbody/tr/td[2]/a"))).click();
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
		
		try {
			Thread.sleep(5000);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
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
		
		try {
			Thread.sleep(10000);
		}
		catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		try {
			driver.findElement(By.xpath(".//*[@id='headnav2']/li[2]/a")).click();
			System.out.println("Registracija uspjesna!");
		}
		catch(Exception e) {
			System.out.println("Neuspjesna registracija.");
		}
	}
}