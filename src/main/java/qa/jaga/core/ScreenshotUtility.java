package qa.jaga.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenshotUtility {

    public void takePageScreenshot(WebDriver driver, String imageName) {
        Screenshot screenshot = new AShot().takeScreenshot(driver);
        BufferedImage bufferedImage = screenshot.getImage();
        File file = new File(System.getProperty("user.dir")+"/src/main/resources/images/screenshot/"+imageName+".png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void takeElementScreenshot(WebDriver driver, String imageName, WebElement element) {
        //Screenshot screenshot1 = new AShot().coordsProvider(new WebDriverCoordsProvider()).takeScreenshot(driver);
        Screenshot screenshot = new AShot().takeScreenshot(driver, element);
        BufferedImage bufferedImage = screenshot.getImage();
        File file = new File(System.getProperty("user.dir")+"/src/main/resources/images/screenshot/"+imageName+".png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean areImagesEqual(String baseImage, String screenshot) {
        BufferedImage baseBufferedImage = null;
        BufferedImage screenshotBufferedImage = null;
        try {
            baseBufferedImage = ImageIO.read(new File(System.getProperty("user.dir")+"/src/main/resources/images/baseline/"+baseImage+".png"));
            screenshotBufferedImage = ImageIO.read(new File(System.getProperty("user.dir")+"/src/main/resources/images/screenshot/"+screenshot+".png"));
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        ImageDiff imageDiff = new ImageDiffer().makeDiff(baseBufferedImage, screenshotBufferedImage);
        boolean isImageDifferent = imageDiff.hasDiff();
        if(isImageDifferent) {
            BufferedImage differentBufferedImage = imageDiff.getMarkedImage();
            try{
                ImageIO.write(differentBufferedImage, "png", new File(System.getProperty("user.dir")+"/src/main/resources/images/difference/"+baseImage+".png"));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return !isImageDifferent;
    }

}
