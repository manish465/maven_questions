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

public class TC01_SwagLabs {
    static String url = "https://www.saucedemo.com/";

    static WebDriver driver;
    static WebDriverWait wait;

    static List<String> shoppingList;

    @BeforeClass
    public void setup() {
	WebDriverManager.chromedriver().setup();
	driver = new ChromeDriver();
	wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	shoppingList = new ArrayList<String>();

	shoppingList.add("Sauce Labs Backpack");
	shoppingList.add("Sauce Labs Fleece Jacket");
	shoppingList.add("Test.allTheThings() T-Shirt (Red)");

	driver.manage().window().maximize();

	driver.get(url);
    }

    @Test
    public void test() {
	driver.findElement(By.xpath("//input[@data-test='username']")).sendKeys("standard_user");
	driver.findElement(By.xpath("//input[@data-test='password']")).sendKeys("secret_sauce" + Keys.ENTER);

	WebElement productTiltElement = wait
		.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='title']")));

	Assert.assertEquals(productTiltElement.getText(), "PRODUCTS");

	List<WebElement> itemList = driver.findElements(By.xpath("//div[@class='inventory_item']"));

	for (WebElement item : itemList) {
	    String nameString = item.findElement(By.xpath(".//div[@class='inventory_item_name']")).getText();
	    if (shoppingList.contains(nameString))
		item.findElement(By.xpath(".//div[@class='inventory_item_price']/following-sibling::button")).click();
	}

	driver.findElement(By.xpath("//a[@class='shopping_cart_link']")).click();

	List<WebElement> verificationList = driver.findElements(By.xpath("//div[@class='inventory_item_name']"));

	for (WebElement element : verificationList) {
	    Assert.assertTrue(shoppingList.contains(element.getText()));
	}

	driver.findElement(By.xpath("//button[@id='checkout']")).click();

	driver.findElement(By.xpath("//input[@id='first-name']")).sendKeys("Manish");
	driver.findElement(By.xpath("//input[@id='last-name']")).sendKeys("Sahu");
	driver.findElement(By.xpath("//input[@id='postal-code']")).sendKeys("689241");

	driver.findElement(By.xpath("//input[@id='continue']")).click();
	driver.findElement(By.xpath("//button[@id='finish']")).click();

	WebElement checkoutElement = wait.until(ExpectedConditions
		.visibilityOfElementLocated(By.xpath("//div[@class='checkout_complete_container']/h2")));

	Assert.assertEquals(checkoutElement.getText(), "THANK YOU FOR YOUR ORDER");
    }

    @AfterClass
    public void tearDown() {
	driver.close();
    }
}
