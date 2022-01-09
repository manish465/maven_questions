package com.mypackege;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

public class TC11_Goibibo {
    static String url = "https://www.goibibo.com/";

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
	driver.manage().deleteAllCookies();
    }

    @AfterClass
    public void teardown() {
	driver.close();
    }

    @Test
    public void test() {
	WebElement fromInput = driver.findElement(By.xpath("//input[@id='gosuggest_inputSrc']"));
	fromInput.sendKeys("Delhi");
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@id='react-autosuggest-1']")));
	fromInput.sendKeys(Keys.chord(Keys.DOWN, Keys.ENTER));

	WebElement destinationInput = driver.findElement(By.xpath("//input[@id='gosuggest_inputDest']"));
	destinationInput.sendKeys("bengaluru");
	wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//ul[@id='react-autosuggest-1']")));
	destinationInput.sendKeys(Keys.chord(Keys.DOWN, Keys.ENTER));

	driver.findElement(By.xpath("//input[@id='gosuggest_inputDest']")).sendKeys("Bengaluru (BLR)");

	driver.findElement(By.xpath("//div[@aria-disabled='false']")).click();
	driver.findElement(By.xpath("//button[@type='submit']")).click();

	List<WebElement> priceList = driver.findElements(By.xpath("//div[contains(@class,'PriceWrap')]"));

	int lowestPriceInNum = Integer.MAX_VALUE;
	WebElement lowestPriceElement = priceList.get(0);

	for (WebElement priceElement : priceList) {
	    String priceString = priceElement.getText().replaceAll(" ", "").replaceAll(",", "");
	    int priceInNum = Integer.parseInt(priceString);
	    if (lowestPriceInNum > priceInNum) {
		lowestPriceInNum = priceInNum;
		lowestPriceElement = priceElement;
	    }
	}

	lowestPriceElement.findElement(By.xpath("./following-sibling::div//button")).click();
	driver.findElement(By.xpath("//input[@type='button']")).click();

	WebElement deatilPageHeading = driver.findElement(By.xpath("//span[@class='flex1']"));
	Assert.assertEquals(deatilPageHeading.getText(), "TICKET DETAILS");
    }
}
