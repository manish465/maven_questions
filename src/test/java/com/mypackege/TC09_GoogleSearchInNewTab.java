package com.mypackege;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC09_GoogleSearchInNewTab {
    static String url = "https://www.google.co.in/";

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
	WebDriverManager.chromedriver().setup();
	driver = new ChromeDriver();
	wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	driver.manage().window().maximize();
	driver.get(url);
	driver.manage().deleteAllCookies();
    }

    @AfterClass
    public void teardown() {
	driver.close();
    }

    @Test
    public void test() {
	driver.switchTo().activeElement().sendKeys("zip file" + Keys.ENTER);

	List<WebElement> firstTwoSearchLinks = driver.findElements(By.xpath("//h3/ancestor::a")).subList(0, 2);

	for (WebElement links : firstTwoSearchLinks) {
	    links.sendKeys(Keys.chord(Keys.CONTROL, Keys.ENTER));
	}

	List<String> tabs = new ArrayList<String>(driver.getWindowHandles());

	driver.switchTo().window(tabs.get(1));
	WebElement firstTabLogo = wait
		.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@class='navbar-brand']/img")));
	Assert.assertEquals(firstTabLogo.getAttribute("title"), "WinZip");
	driver.close();

	driver.switchTo().window(tabs.get(2));
	WebElement secondTabLogo = wait
		.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='uhfLogo']/img")));
	Assert.assertEquals(secondTabLogo.getAttribute("itemprop"), "logo");
	driver.close();

	driver.switchTo().window(tabs.get(0));

    }
}
