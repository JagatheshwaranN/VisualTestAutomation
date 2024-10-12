package qa.jaga.core.selenium;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TestFireRegressionTest class automates the process of capturing page screenshots,
 * comparing them, and generating ExtentReports for visual test results.
 * It utilizes TestNG for managing test cases and assertions.
 */
public class TestFireRegressionTest {

    // Declares an ExtentReports object to manage the reporting
    private ExtentReports extentReports;

    // Declares an ExtentTest object to manage the individual test logging
    private ExtentTest extentTest;

    // WebDriver instance to control the browser during tests
    private WebDriver driver;

    // Constants to define the directory paths for screenshots, differences, and reports
    private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + "/src/main/resources/selenium/images/screenshot/";
    private static final String DIFFERENCE_DIR = System.getProperty("user.dir") + "/src/main/resources/selenium/images/difference/";
    private static final String EXTENT_DIR = System.getProperty("user.dir") + "/report/ExtentReport.html";

    // Logger for logging errors and messages to the console
    private static final Logger LOGGER = Logger.getLogger(TestFireRegressionTest.class.getName());

    /**
     * setUp method initializes ExtentReports, cleans the screenshot directories,
     * and sets up the WebDriver to open the browser for testing.
     */
    @BeforeClass
    public void setUp() {
        // Initializes ExtentReports for logging test results
        extentReports = new ExtentReports();

        // Initializes ExtentSparkReporter to create a visual HTML report
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(EXTENT_DIR);
        extentReports.attachReporter(sparkReporter);

        try {
            // Cleans the screenshot directory
            FileUtils.cleanDirectory(new File(SCREENSHOT_DIR));

            // Cleans the difference directory (for comparing images)
            FileUtils.cleanDirectory(new File(DIFFERENCE_DIR));
        } catch (IOException ex) {
            // Logs any IOException errors encountered during cleaning directories
            LOGGER.log(Level.SEVERE, "Error cleaning directories: " + ex.getMessage(), ex);
        }

        // Initializes ChromeDriver to automate Chrome browser actions
        this.driver = new ChromeDriver();

        // Maximizes the browser window
        this.driver.manage().window().maximize();

        // Opens the target URL for testing
        this.driver.get("https://demo.testfire.net/index.jsp");
    }

    /**
     * testFireTest method navigates to the given URL, takes a screenshot, compares it with a baseline image,
     * and logs the result in ExtentReports.
     *
     * @param url       The URL to navigate to for the test.
     * @param imageName The name of the image to compare.
     * @param method    The current method being executed.
     * @param context   The ITestContext for passing additional test parameters.
     */
    @Test(dataProvider = "dataSupplier")
    public void testFireTest(String url, String imageName, Method method, ITestContext context) {
        // Adds a parameter "screenshot" with the imageName to the test context for further usage
        context.getCurrentXmlTest().addParameter("screenshot", imageName);

        // Creates a new test log entry with the method name and URL in ExtentReports
        extentTest = extentReports.createTest(method.getName() + " || " + url);

        try {
            // Navigates to the provided URL
            this.driver.navigate().to(url);

            // Takes a screenshot of the page and saves it with the provided image name
            new ScreenshotUtility().takePageScreenshot(this.driver, imageName);

            // Asserts that the current screenshot matches the baseline image, logs failure if not
            Assert.assertTrue(new ScreenshotUtility().areImagesEqual(imageName, imageName),
                    "Images do not match for: " + imageName);
        } catch (Exception ex) {
            // Logs any exceptions that occur during the test execution
            LOGGER.log(Level.SEVERE, "Error during testFireTest execution for " + url + ": " + ex.getMessage(), ex);
        }
    }

    /**
     * dataSupplier method provides URLs and corresponding image names for the testFireTest method.
     *
     * @return A 2D array of URLs and image names for data-driven testing.
     */
    @DataProvider
    public Object[][] dataSupplier() {
        // Returns a set of test data: URL and image name for each test case
        return new Object[][]{
                {"https://demo.testfire.net/index.jsp?content=inside_about.htm", "about_us"},
                {"https://demo.testfire.net/index.jsp?content=inside_contact.htm", "contact_us"},
                {"https://demo.testfire.net/index.jsp?content=inside_investor.htm", "investor"},
                {"https://demo.testfire.net/index.jsp?content=inside_press.htm", "press"},
                {"https://demo.testfire.net/index.jsp?content=inside_careers.htm", "careers"},
                {"https://demo.testfire.net/subscribe.jsp", "subscribe"}
        };
    }

    /**
     * captureResult method logs the result of each test (PASS, FAIL, or SKIP) in ExtentReports,
     * along with a screenshot for failed tests.
     *
     * @param result  The result of the test execution (pass/fail/skip).
     * @param context The test context, used to retrieve screenshot details.
     */
    @AfterMethod
    public void captureResult(ITestResult result, ITestContext context) {
        // Retrieves the screenshot name from the test context
        String imageName = context.getCurrentXmlTest().getParameter("screenshot");

        // Constructs the path for the difference image if any test fails
        String screenshotPath = DIFFERENCE_DIR + imageName + ".png";

        // Logs test as passed if the test status is success
        if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.log(Status.PASS, "Test Passed");
        } else if (result.getStatus() == ITestResult.FAILURE) {

            // Adds the screenshot to the report if the test fails
            String screenshot = extentTest.addScreenCaptureFromPath(screenshotPath).toString();
            extentTest.log(Status.FAIL, "Test Failed. Screenshot: " + screenshot);
            extentTest.log(Status.FAIL, result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {

            // Logs the test as skipped if the status is skipped
            extentTest.log(Status.SKIP, "Test Skipped");
        }
    }

    /**
     * tearDown method closes the WebDriver instance and flushes the ExtentReports log.
     */
    @AfterClass
    public void tearDown() {
        // Closes the WebDriver session if not already closed
        if (this.driver != null) {
            this.driver.quit();
        }

        // Flushes and finalizes the ExtentReports
        if (extentReports != null) {
            extentReports.flush();
        }
    }

}
