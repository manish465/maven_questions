package com.mypackege;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC08_SeleniumSearch {
    static String url = "https://www.google.co.in/";

    WebDriver driver;

    @BeforeClass
    public void setup() {
	WebDriverManager.chromedriver().setup();

	driver = new ChromeDriver();

	driver.manage().window().maximize();

	driver.get(url);
    }

    @AfterClass
    public void teardown() {
	driver.close();
    }

    @Test
    public void test() {
	driver.switchTo().activeElement().sendKeys("selenium" + Keys.ENTER);

	String pageTitle = driver.getTitle();
	String actualString = "selenium";

	Assert.assertTrue(pageTitle.contains(actualString));

    }
}
