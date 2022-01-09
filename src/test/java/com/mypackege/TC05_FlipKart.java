package com.mypackege;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC05_FlipKart {
    static String url = "https://www.flipkart.com/";

    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    @BeforeClass
    public void setup() {
	WebDriverManager.chromedriver().setup();
	driver = new ChromeDriver();
	wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	js = (JavascriptExecutor) driver;

	driver.manage().window().maximize();
	driver.get(url);
    }

    @AfterClass
    public void teardown() {
	driver.quit();
    }

    @Test
    public void test() {
	driver.findElement(By.xpath("//button[contains(@class,'_2doB4z')]")).click();
	driver.findElement(By.xpath("//input[@type='text']")).sendKeys("mobiles" + Keys.ENTER);

	driver.navigate().refresh();

	List<WebElement> itemList = wait
		.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//a[@class='_1fQZEK']")));

	WebElement highestRatingElement = getHigestRatingElement(itemList);

	WebElement ratingElement = highestRatingElement.findElement(By.xpath(".//div[@class='_3LWZlK']"));
	WebElement titleElement = highestRatingElement.findElement(By.xpath(".//div[@class='_4rR01T']"));

	double rating = Double.parseDouble(ratingElement.getText());
	System.out.println("name: " + titleElement.getText() + " | rating: " + rating);

	js.executeScript("arguments[0].click();", highestRatingElement);

	ArrayList<String> tabsList = new ArrayList<String>(driver.getWindowHandles());
	driver.switchTo().window(tabsList.get(1));

	List<WebElement> ratingCountElements = driver.findElements(By.xpath("//div[@class='_1uJVNT']"));

	int starCount = 5;
	for (WebElement ratingCountElement : ratingCountElements) {
	    System.out.println(starCount + " STAR: " + ratingCountElement.getText());
	    starCount--;
	}

    }

    private WebElement getHigestRatingElement(List<WebElement> itemList) {
	double higest = 0.0;
	WebElement higestRatedElement = itemList.get(0);
	for (WebElement itemElement : itemList) {
	    WebElement ratingElement = itemElement.findElement(By.xpath(".//div[@class='_3LWZlK']"));
	    double rating = Double.parseDouble(ratingElement.getText());

	    if (higest < rating) {
		higest = rating;
		higestRatedElement = itemElement;
	    }
	}
	return higestRatedElement;
    }
}
