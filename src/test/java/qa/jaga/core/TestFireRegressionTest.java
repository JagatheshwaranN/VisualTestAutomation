package qa.jaga.core;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class TestFireRegressionTest {

    public WebDriver driver;

    @BeforeClass
    public void setUp() {
        try{
            FileUtils.cleanDirectory(new File(System.getProperty("user.dir")+"/src/main/resources/images/screenshot"));
            FileUtils.cleanDirectory(new File(System.getProperty("user.dir")+"/src/main/resources/images/difference"));
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
        this.driver.get("https://demo.testfire.net/index.jsp");
    }

    @Test(dataProvider = "dataSupplier")
    public void testFireTest(String url, String imageName) {
        this.driver.navigate().to(url);
        new ScreenshotUtility().takePageScreenshot(this.driver, imageName);
        new ScreenshotUtility().areImagesEqual(imageName, imageName);
    }

    @DataProvider
    public Object[][] dataSupplier() {
        return new Object[][] {
                {"https://demo.testfire.net/index.jsp?content=inside_about.htm", "about_us"},
                {"https://demo.testfire.net/index.jsp?content=inside_contact.htm", "contact_us"},
                {"https://demo.testfire.net/index.jsp?content=inside_investor.htm", "investor"},
                {"https://demo.testfire.net/index.jsp?content=inside_press.htm", "press"},
                {"https://demo.testfire.net/index.jsp?content=inside_careers.htm", "careers"},
                {"https://demo.testfire.net/subscribe.jsp", "subscribe"}
        };
    }

    @AfterClass
    public void tearDown() {
        if(this.driver != null)
            this.driver.quit();
    }

}
