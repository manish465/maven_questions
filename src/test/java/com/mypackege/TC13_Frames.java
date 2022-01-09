package com.mypackege;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC13_Frames {
    static String url = "https://www.globalsqa.com/demo-site/frames-and-windows/#iFrame";

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    @BeforeClass
    public void setup() {
	WebDriverManager.chromedriver().setup();

	driver = new ChromeDriver();
	wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	actions = new Actions(driver);

	driver.manage().window().maximize();
	driver.get(url);
    }

    @AfterClass
    public void teardown() {
	driver.close();
    }

    @Test
    public void test() {

	WebElement iframeElement = wait
		.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe[@name='globalSqa']")));
	driver.switchTo().frame(iframeElement);

	List<WebElement> cardList = driver.findElements(By.xpath("//div[starts-with(@class,'one-third')]"));

	for (WebElement card : cardList) {
	    actions.moveToElement(card).perform();
	}

	WebElement titleElement = driver.findElement(By.xpath("//div[@class='logo_img']/a"));
	Assert.assertEquals(titleElement.getAttribute("title"), "GlobalSQA");
	System.out.println(driver.findElement(By.xpath("(//h3)[3]")).getText());
    }
}
