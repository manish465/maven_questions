package com.mypackege;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC07_GoogleLinks {
    static String url = "https://www.google.co.in/";

    WebDriver driver;

    @BeforeClass
    public void setup() {
	WebDriverManager.chromedriver().setup();
	driver = new ChromeDriver();

	driver.manage().window().maximize();
	driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

	driver.get(url);
    }

    @AfterClass
    public void teardown() {
	driver.close();
    }

    @Test
    public void test() {
	driver.switchTo().activeElement().sendKeys("zip files" + Keys.ENTER);

	List<WebElement> webLinksList = driver.findElements(By.xpath("//h3[@class='LC20lb MBeuO DKV0Md']"));
	int numOfLinks = 0;

	for (WebElement links : webLinksList) {
	    if (links.getText().length() != 0) {
		System.out.println(links.getText());
		numOfLinks++;
	    }
	}

	System.out.println();
	System.out.println("Total number of links in the page: " + numOfLinks);
    }

}
