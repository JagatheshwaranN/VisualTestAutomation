package qa.jaga.core;

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


public class TestFireRegressionTest {
    ExtentReports extentReports;
    ExtentTest extentTest;
    public WebDriver driver;

    @BeforeClass
    public void setUp() {
        extentReports = new ExtentReports();
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/report/ExtentReport.html");
        extentReports.attachReporter(sparkReporter);
        try {
            FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "/src/main/resources/images/screenshot"));
            FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "/src/main/resources/images/difference"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
        this.driver.get("https://demo.testfire.net/index.jsp");
    }

    @BeforeMethod
    public void reportInit(Method method) {
        //extentTest = extentReports.createTest(method.getName());
    }

    @Test(dataProvider = "dataSupplier")
    public void testFireTest(String url, String imageName, Method method, ITestContext context) {
        context.getCurrentXmlTest().addParameter("screenshot", imageName);
        extentTest = extentReports.createTest(method.getName() + " || " + url);
        this.driver.navigate().to(url);
        new ScreenshotUtility().takePageScreenshot(this.driver, imageName);
        Assert.assertTrue(new ScreenshotUtility().areImagesEqual(imageName, imageName));
    }

    @DataProvider
    public Object[][] dataSupplier() {
        return new Object[][]{
                {"https://demo.testfire.net/index.jsp?content=inside_about.htm", "about_us"},
                {"https://demo.testfire.net/index.jsp?content=inside_contact.htm", "contact_us"},
                {"https://demo.testfire.net/index.jsp?content=inside_investor.htm", "investor"},
                {"https://demo.testfire.net/index.jsp?content=inside_press.htm", "press"},
                {"https://demo.testfire.net/index.jsp?content=inside_careers.htm", "careers"},
                {"https://demo.testfire.net/subscribe.jsp", "subscribe"}
        };
    }

    @AfterMethod
    public void captureResult(ITestResult result, ITestContext context) {
        String image = context.getCurrentXmlTest().getParameter("screenshot");
        if (result.getStatus() == ITestResult.SUCCESS)
            extentTest.log(Status.PASS, "Test Passed");
        if (result.getStatus() == ITestResult.FAILURE) {
            String difference = extentTest.addScreenCaptureFromPath("../src/main/resources/images/difference/" + image + ".png").toString();
            extentTest.log(Status.FAIL, "Test Failed \n screenshot: \n" + difference);
            extentTest.log(Status.FAIL, result.getThrowable());
        }
        if (result.getStatus() == ITestResult.SKIP) {
            extentTest.log(Status.SKIP, "Test Skipped");
        }
    }

    @AfterClass
    public void tearDown() {
        if (this.driver != null)
            this.driver.quit();
        //extentReports.removeTest(extentTest);
        extentReports.flush();
    }

}
