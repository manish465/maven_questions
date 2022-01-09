package com.mypackege;

import java.io.File;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC03_Gmail {
    static String url = "http://mail.google.com/mail/";
    static String username = "userf5282", password = "pass@w0rd", sendUser = "userf5282@gmail.com",
	    message = "maven,selenium,testng";

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    @BeforeClass
    public void setup() {
	WebDriverManager.chromedriver().setup();
	driver = new ChromeDriver();
	wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	js = (JavascriptExecutor) driver;

	driver.manage().window().maximize();

	driver.get(url);
    }

    @AfterClass
    public void tearDown() {
	driver.findElement(By.xpath("//a[contains(@aria-label,'Google Account')]")).click();
	driver.findElement(By.xpath("//a[contains(@aria-label,'Change')]")).click();

	WebElement pfpChangeFrame = wait
		.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[contains(@allow,'camera')]")));
	driver.switchTo().frame(pfpChangeFrame);

	driver.findElement(By.xpath("(//span[text()='Remove'])[last()]")).click();
	WebElement confirmRemoval = wait
		.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Confirm Removal']")));
	js.executeScript("arguments[0].click();", confirmRemoval);

	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[text()='Got it'])[last()]")))
		.click();
	driver.switchTo().defaultContent();

	driver.close();

    }

    @Test
    public void test() {
	login();
	changePFP();
	sendEmail();
    }

    private void login() {
	driver.switchTo().activeElement().sendKeys(username + Keys.ENTER);

	WebElement passwordInput = wait
		.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
	passwordInput.sendKeys(password + Keys.ENTER);

	wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Inbox")));
    }

    private void changePFP() {
	driver.findElement(By.xpath("//a[contains(@aria-label,'Google Account')]")).click();
	driver.findElement(By.xpath("//a[contains(@aria-label,'Change')]")).click();

	WebElement pfpChangeFrame = wait
		.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[contains(@allow,'camera')]")));
	driver.switchTo().frame(pfpChangeFrame);

	driver.findElement(By.xpath("(//span[text()='Add profile photo'])[last()]")).click();

	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[text()='Upload'])[last()]"))).click();
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Choose photo to upload']")));

	WebElement tagrgetElement = driver.findElement(By.xpath("//div[@class='cevIvf']"));

	DropFile(new File("./assets/2.jfif"), tagrgetElement, 0, 0);

	wait.until(
		ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='Save as profile picture'])[last()]")))
		.click();
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Got it']"))).click();

	driver.switchTo().defaultContent();
    }

    private void sendEmail() {
	driver.findElement(By.xpath("//div[text()='Compose']")).click();
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@name='to']")))
		.sendKeys(sendUser);

	WebElement messageBody = driver.findElement(By.xpath("//div[@aria-label='Message Body']"));
	js.executeScript("arguments[0].innerText = '" + message + "'", messageBody);

	driver.findElement(By.xpath("//div[text()='Send']")).click();

	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Message sent']")));
	driver.findElement(By.xpath("//a[@aria-label='Sent']")).click();

	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[@jscontroller='ZdOxDb']"))).click();
	WebElement msgElement = driver.findElement(By.xpath("//div[@class='']"));
	Assert.assertEquals(msgElement.getText(), message);

	driver.findElement(By.xpath("(//div[@title='Delete'])[last()]")).click();
	wait.until(
		ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Conversation moved to Trash.']")));
    }

    public static void DropFile(File filePath, WebElement target, int offsetX, int offsetY) {
	if (!filePath.exists())
	    throw new WebDriverException("File not found: " + filePath.toString());

	WebDriver driver = ((RemoteWebElement) target).getWrappedDriver();
	JavascriptExecutor js = (JavascriptExecutor) driver;
	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

	String JS_DROP_FILE = "var target = arguments[0]," + "    offsetX = arguments[1],"
		+ "    offsetY = arguments[2]," + "    document = target.ownerDocument || document,"
		+ "    window = document.defaultView || window;" + "" + "var input = document.createElement('INPUT');"
		+ "input.type = 'file';" + "input.style.display = 'none';" + "input.onchange = function () {"
		+ "  var rect = target.getBoundingClientRect(),"
		+ "      x = rect.left + (offsetX || (rect.width >> 1)),"
		+ "      y = rect.top + (offsetY || (rect.height >> 1)),"
		+ "      dataTransfer = { files: this.files };" + ""
		+ "  ['dragenter', 'dragover', 'drop'].forEach(function (name) {"
		+ "    var evt = document.createEvent('MouseEvent');"
		+ "    evt.initMouseEvent(name, !0, !0, window, 0, 0, 0, x, y, !1, !1, !1, !1, 0, null);"
		+ "    evt.dataTransfer = dataTransfer;" + "    target.dispatchEvent(evt);" + "  });" + ""
		+ "  setTimeout(function () { document.body.removeChild(input); }, 25);" + "};"
		+ "document.body.appendChild(input);" + "return input;";

	WebElement input = (WebElement) js.executeScript(JS_DROP_FILE, target, offsetX, offsetY);
	input.sendKeys(filePath.getAbsoluteFile().toString());
	wait.until(ExpectedConditions.stalenessOf(input));
    }

}
