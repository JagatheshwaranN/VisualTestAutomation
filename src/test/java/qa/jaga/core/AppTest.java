package qa.jaga.core;

import com.google.common.util.concurrent.Uninterruptibles;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

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
        Assert.assertTrue(new ScreenshotUtility().areImagesEqual("subscribe", "subscribe"));
       // driver.close();
    }

    @Test(dataProvider = "dataSupply")
    public static void testFireTest(String url, String imgName) {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().to(url);
        new ScreenshotUtility().takePageScreenshot(driver, imgName);
    }

    @DataProvider
    public Object[][] dataSupply() {
        return new Object[][] {
                {"https://demo.testfire.net/index.jsp?content=inside_about.htm", "about_us"},
                {"https://demo.testfire.net/index.jsp?content=inside_contact.htm", "contact_us"},
                {"https://demo.testfire.net/index.jsp?content=inside_investor.htm", "investor"},
                {"https://demo.testfire.net/index.jsp?content=inside_press.htm", "press"},
                {"https://demo.testfire.net/index.jsp?content=inside_careers.htm", "careers"},
                {"https://demo.testfire.net/subscribe.jsp", "subscribe"}
        };
    }
}
