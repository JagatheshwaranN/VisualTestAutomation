package qa.jaga.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SampleTest class contains test cases for capturing screenshots of web pages or elements,
 * comparing images, and navigating to different URLs using WebDriver.
 * Each test case uses ScreenshotUtility for the screenshot-related operations.
 */
public class SampleTest {

    // Logger to log messages and errors to the console
    private static final Logger LOGGER = Logger.getLogger(SampleTest.class.getName());

    /**
     * Initializes a WebDriver instance, sets it up to use Chrome, maximizes the browser window,
     * and returns the WebDriver object.
     *
     * @return WebDriver instance used to interact with the browser.
     */
    private WebDriver initializeDriver() {
        // Initializes the Chrome WebDriver
        WebDriver driver = new ChromeDriver();

        // Maximizes the browser window
        driver.manage().window().maximize();

        // Returns the WebDriver instance
        return driver;
    }

    /**
     * pageScreenshotTest captures a screenshot of the entire "Mercury Travels Flights" page.
     * This test is currently disabled and does not run.
     */
    @Test(enabled = false)
    public void pageScreenshotTest() {
        WebDriver driver = initializeDriver();
        try {
            // Opens the "Mercury Travels Flights" page
            driver.get("https://www.mercurytravels.co.in/flights");

            // Captures a screenshot of the entire page and saves it as "pageImage"
            new ScreenshotUtility().takePageScreenshot(driver, "pageImage");
        } catch (Exception ex) {
            // Logs any exceptions that occur during the test
            LOGGER.log(Level.SEVERE, "Error in pageScreenshotTest: " + ex.getMessage(), ex);
        } finally {
            // Closes the browser after the test completes
            driver.quit();
        }
    }

    /**
     * elementScreenshotTest captures a screenshot of a specific element (logo) on the
     * "Mercury Travels Flights" page. This test is currently disabled and does not run.
     */
    @Test(enabled = false)
    public void elementScreenshotTest() {
        WebDriver driver = initializeDriver();
        try {
            // Opens the "Mercury Travels Flights" page
            driver.get("https://www.mercurytravels.co.in/flights");

            // Finds the logo element using an XPath locator
            WebElement logo = driver.findElement(By.xpath("//div[@class='container']//img[@class='mercurylogo']"));

            // Captures a screenshot of the logo element and saves it as "elementImage"
            new ScreenshotUtility().takeElementScreenshot(driver, "elementImage", logo);
        } catch (Exception ex) {
            // Logs any exceptions that occur during the test
            LOGGER.log(Level.SEVERE, "Error in elementScreenshotTest: " + ex.getMessage(), ex);
        } finally {
            // Closes the browser after the test completes
            driver.quit();
        }
    }

    /**
     * compareImagesHappyPathTest compares the screenshot of the homepage with a baseline image
     * to verify they are identical. This test is currently disabled and does not run.
     */
    @Test(enabled = false)
    public void compareImagesHappyPathTest() {
        WebDriver driver = initializeDriver();
        try {
            // Opens the "Mercury Travels Flights" page
            driver.get("https://www.mercurytravels.co.in/flights");

            // Captures a screenshot of the page and saves it as "scrHomePage"
            new ScreenshotUtility().takePageScreenshot(driver, "scrHomePage");

            // Compares the captured screenshot with the baseline image "homepage"
            boolean imagesAreEqual = new ScreenshotUtility().areImagesEqual("homepage", "scrHomePage");

            // Asserts that the images are equal
            Assert.assertTrue(imagesAreEqual, "Images are not equal in Happy Path Test.");
        } catch (Exception ex) {
            // Logs any exceptions that occur during the test
            LOGGER.log(Level.SEVERE, "Error in compareImagesHappyPathTest: " + ex.getMessage(), ex);
        } finally {
            // Closes the browser after the test completes
            driver.quit();
        }
    }

    /**
     * compareImagesNegativeTest performs a negative comparison of images. It captures a screenshot
     * after interacting with the "Mercury Travels Flights" page and compares it with a baseline image.
     */
    @Test
    public void compareImagesNegativeTest() {
        WebDriver driver = initializeDriver();
        try {
            // Opens the "Mercury Travels Flights" page
            driver.get("https://www.mercurytravels.co.in/flights");

            // Finds the "fromCity" input field and types "Chennai" into it
            WebElement fromCity = driver.findElement(By.xpath("//input[@name='fromCity']"));
            fromCity.sendKeys("Chennai");

            // Captures a screenshot of the page and saves it as "scrHomePage"
            new ScreenshotUtility().takePageScreenshot(driver, "scrHomePage");

            // Compares the captured screenshot with the baseline image "subscribe"
            boolean imagesAreEqual = new ScreenshotUtility().areImagesEqual("subscribe", "scrHomePage");

            // Asserts that the images are equal (this is expected to fail as it's a negative test)
            Assert.assertTrue(imagesAreEqual, "Images are not equal in Negative Test.");
        } catch (Exception ex) {
            // Logs any exceptions that occur during the test
            LOGGER.log(Level.SEVERE, "Error in compareImagesNegativeTest: " + ex.getMessage(), ex);
        } finally {
            // Closes the browser after the test completes
            driver.quit();
        }
    }

    /**
     * testFireTest is a parameterized test that navigates to various URLs and captures screenshots
     * of the pages. The URLs and image names are provided by the data provider.
     *
     * @param url     The URL of the page to navigate to.
     * @param imgName The name of the image file to save.
     */
    @Test(dataProvider = "dataSupply")
    public void testFireTest(String url, String imgName) {
        WebDriver driver = initializeDriver();
        try {
            // Navigates to the specified URL
            driver.navigate().to(url);

            // Captures a screenshot of the page and saves it with the provided image name
            new ScreenshotUtility().takePageScreenshot(driver, imgName);
        } catch (Exception ex) {
            // Logs any exceptions that occur during the test
            LOGGER.log(Level.SEVERE, "Error in testFireTest for URL: " + url + " : " + ex.getMessage(), ex);
        } finally {
            // Closes the browser after the test completes
            driver.quit();
        }
    }

    /**
     * dataSupply provides test data for the testFireTest. Each entry contains a URL and
     * a corresponding image name to use for the screenshot.
     *
     * @return A 2D Object array containing URLs and image names for testing.
     */
    @DataProvider
    public Object[][] dataSupply() {
        return new Object[][]{
                {"https://demo.testfire.net/index.jsp?content=inside_about.htm", "about_us"},
                {"https://demo.testfire.net/index.jsp?content=inside_contact.htm", "contact_us"},
                {"https://demo.testfire.net/index.jsp?content=inside_investor.htm", "investor"},
                {"https://demo.testfire.net/index.jsp?content=inside_press.htm", "press"},
                {"https://demo.testfire.net/index.jsp?content=inside_careers.htm", "careers"},
                {"https://demo.testfire.net/subscribe.jsp", "subscribe"}
        };
    }

}
