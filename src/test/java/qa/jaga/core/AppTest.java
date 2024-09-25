package qa.jaga.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppTest {

    @Test(enabled = false)
    public static void pageScreenshotTest() {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.mercurytravels.co.in/flights");
        new ScreenshotUtility().takePageScreenshot(driver, "pageImage");
        driver.close();
    }

    @Test(enabled = false)
    public static void elementScreenshotTest() {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.mercurytravels.co.in/flights");
        WebElement logo = driver.findElement(By.xpath("//div[@class='container']//img[@class='mercurylogo']"));
        new ScreenshotUtility().takeElementScreenshot(driver, "elementImage", logo);
        driver.close();
    }

    @Test(enabled = false)
    public static void compareImagesHappyPathTest() {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.mercurytravels.co.in/flights");
        new ScreenshotUtility().takePageScreenshot(driver, "scrHomePage");
        Assert.assertTrue(new ScreenshotUtility().areImagesEqual("homepage", "scrHomePage"));
        driver.close();
    }

    @Test
    public static void compareImagesNegativeTest() {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.mercurytravels.co.in/flights");
        WebElement fromCity = driver.findElement(By.xpath("//input[@name='fromCity']"));
        fromCity.sendKeys("Chennai");
        new ScreenshotUtility().takePageScreenshot(driver, "scrHomePage");
        Assert.assertTrue(new ScreenshotUtility().areImagesEqual("homepage", "scrHomePage"));
        driver.close();
    }
}
