package com.mypackege;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC12_POIBrowser {
    static File file;

    Map<String, String> urlBrowswerMap;
    XSSFWorkbook workbook;
    WebDriver driver;

    @BeforeClass
    public void setup() throws InvalidFormatException, IOException {
	file = new File("./assets/poi-automation.xlsx");

	urlBrowswerMap = new HashMap<String, String>();

	workbook = new XSSFWorkbook(file);
	XSSFSheet sheet = workbook.getSheetAt(0);

	for (int r = 1; r <= sheet.getLastRowNum(); r++) {
	    XSSFRow row = sheet.getRow(r);
	    String urlString = row.getCell(0).getStringCellValue();
	    String browserString = row.getCell(1).getStringCellValue();

	    urlBrowswerMap.put(urlString, browserString);
	}
    }

    @AfterClass
    public void teardown() throws IOException {
	workbook.close();
    }

    @Test
    public void test() {
	for (Map.Entry<String, String> entry : urlBrowswerMap.entrySet()) {
	    if (entry.getValue().equals("chrome")) {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		manage(entry);
	    } else if (entry.getValue().equals("firefox")) {
		WebDriverManager.firefoxdriver().setup();
		driver = new FirefoxDriver();
		manage(entry);
	    } else if (entry.getValue().equals("edge")) {
		WebDriverManager.edgedriver().setup();
		driver = new EdgeDriver();
		manage(entry);
	    }
	}
    }

    private void manage(Map.Entry<String, String> entry) {
	driver.manage().window().maximize();
	driver.get(entry.getKey());
	System.out.println(driver.findElement(By.xpath("//h1[@id='firstHeading']")).getText());
	driver.close();
    }
}
