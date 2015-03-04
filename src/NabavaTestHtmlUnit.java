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
		driver.get("http://getairmail.com");
		
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/section[2]/div/section/div/section/section/div/a"))).click();
		String mail = new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='tempemail']"))).getAttribute("value");
		
		return mail;
	}
	
	public static String GetPassword(WebDriver driver) {
		
		driver.get("http://getairmail.com");
		
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='ui_inbox']/table/tbody/tr[2]/td[2]")));
		
		driver.findElement(By.xpath(".//*[@id='ui_inbox']/table/tbody/tr[1]/td[2]")).click();
		String pass = new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='view_mail']/table/tbody/tr[2]/td[1]/b"))).getText();
		
		return pass;
	}

	public static void NabavaRegister(WebDriver driver, String mail) {
		driver.get("http://www.nabava.net");
		driver.findElement(By.xpath(".//*[@id='headnav2']/li[2]/a")).click();
		
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='loginForm']/form/fieldset/ol[3]/li/strong/a"))).click();
		
		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm");
		Date datum = new Date();
		String username = dateFormat.format(datum);
		username += "test";
		
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='screenName']"))).sendKeys(username);
		driver.findElement(By.xpath(".//*[@id='email']")).sendKeys(mail);
		driver.findElement(By.xpath(".//*[@id='email']")).sendKeys(Keys.RETURN);
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
		
		boolean isPresent = driver.findElements(By.xpath(".//*[@id='ui-id-1']")).size() > 0;
		
		if(isPresent) {
			System.out.println("Registracija uspjesna!");
		}
		else {
			System.out.println("Desila se greska pri registraciji!");
		}
	}
}