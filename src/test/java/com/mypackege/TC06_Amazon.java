package com.mypackege;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC06_Amazon {
    static String url = "https://www.amazon.in/";

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
	driver.manage().deleteAllCookies();
    }

    @AfterClass
    public void teardown() {
	driver.quit();
    }

    @Test
    public void test() {
	driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']")).sendKeys("mobiles" + Keys.ENTER);
	driver.findElement(By.xpath("//span[text()='Smartphones']/ancestor::a")).click();

	Select sortBy = new Select(driver.findElement(By.xpath("//select[@name='s']")));
	sortBy.selectByValue("price-asc-rank");

	WebElement productElementLink = wait.until(ExpectedConditions
		.visibilityOfElementLocated(By.xpath("//span[@data-component-type='s-product-image']/a")));
	productElementLink.click();

	ArrayList<String> tabsList = new ArrayList<String>(driver.getWindowHandles());
	driver.switchTo().window(tabsList.get(1));

	WebElement reviewSection = driver.findElement(By.xpath("//table[@id='histogramTable'][2]"));
	js.executeScript("arguments[0].scrollIntoView(true)", reviewSection);

	List<WebElement> ratingPercentList = reviewSection
		.findElements(By.xpath("//td[@class='a-text-right a-nowrap']"));

	WebElement totalRatingElement = driver.findElement(By.xpath("//div[@data-hook='total-review-count']"));

	int totalRating = getTotalRating(totalRatingElement.getText());

	System.out.println(convertListToMap(ratingPercentList, totalRating));
    }

    private int getTotalRating(String text) {
	return Integer.parseInt(text.substring(0, text.indexOf(" ")));
    }

    private HashMap<String, Integer> convertListToMap(List<WebElement> ratingPercentList, int totalRating) {
	HashMap<String, Integer> ratingMap = new HashMap<String, Integer>();

	int point = 5;
	for (WebElement ratingElement : ratingPercentList) {
	    String ratingText = ratingElement.getText();
	    int ratingPercent = Integer.parseInt(ratingText.substring(0, ratingText.length() - 1));
	    int numRating = (int) Math.round((ratingPercent * totalRating) / 100.0);

	    ratingMap.put(point + " STAR", numRating);
	    point--;
	}

	return ratingMap;
    }
}
