package com.mypackege;

import java.time.Duration;

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

public class TC04_SnapDeal {
    static String url = "https://www.snapdeal.com/";

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setup() {
	WebDriverManager.chromedriver().setup();

	driver = new ChromeDriver();
	wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	driver.manage().window().maximize();

	driver.get(url);
    }

    @AfterClass
    public void teardown() {
	driver.close();
    }

    @Test
    public void test() {
	driver.findElement(By.xpath("//input[@id='inputValEnter']")).sendKeys("apple iphones" + Keys.ENTER);

	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='nnn']")));

	WebElement minPriceElement = driver.findElement(By.xpath("//input[@name='fromVal']"));
	WebElement maxPriceElement = driver.findElement(By.xpath("//input[@name='toVal']"));

	minPriceElement.clear();
	maxPriceElement.clear();

	minPriceElement.sendKeys("1500");
	maxPriceElement.sendKeys("2000");

	driver.findElement(By.xpath("//div[contains(@class,'price-go')]")).click();
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(@class,'clear')]")));

	WebElement singlePriceElement = driver.findElement(By.xpath("//span[contains(@id,'display-price')]"));
	int price = Integer.parseInt(singlePriceElement.getAttribute("data-price"));

	Assert.assertTrue(price >= 1500 && price <= 2000);
    }
}
