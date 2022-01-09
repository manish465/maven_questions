package com.mypackege;

import java.time.Duration;

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

public class TC02_ImgBB {
    static String url = "https://imgbb.com/";
    static WebDriver driver;
    static WebDriverWait wait;
    static Actions actions;

    @BeforeClass
    public void setup() {
	WebDriverManager.chromedriver().setup();
	driver = new ChromeDriver();
	wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	driver.manage().window().maximize();

	driver.get(url);
    }

    @Test
    public void test() {
	Assert.assertEquals(driver.getTitle(), "ImgBB — Upload Image — Free Image Hosting");

	WebElement fileUploadInput = driver.findElement(By.xpath("//input[@id='anywhere-upload-input']"));
	fileUploadInput.sendKeys("C:\\Users\\manishs\\Downloads\\upload\\random.jpg");

	WebElement uploadSubmitButton = wait
		.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@data-action='upload']")));
	uploadSubmitButton.click();

	WebElement feedback = wait
		.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Upload complete']")));

	Assert.assertEquals(feedback.getText(), "Upload complete");
    }

    @AfterClass
    public void tearDown() {
	driver.close();
    }

}
