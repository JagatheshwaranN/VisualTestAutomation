package qa.jaga.core.playwright;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ScreenshotUtility class provides methods for capturing screenshots of web pages or elements,
 * comparing images, and saving images to specific directories.
 * This utility leverages the AShot library for capturing and comparing screenshots.
 */
public class ScreenshotUtility {

    // Logger to log messages and errors to the console
    private static final Logger LOGGER = Logger.getLogger(ScreenshotUtility.class.getName());

    // Constants for directories where screenshots, baseline images, and differences are stored
    private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + "/src/main/resources/playwright/images/screenshot/";
    private static final String BASELINE_DIR = System.getProperty("user.dir") + "/src/main/resources/playwright/images/baseline/";
    private static final String DIFFERENCE_DIR = System.getProperty("user.dir") + "/src/main/resources/playwright/images/difference/";

    /**
     * Constructor initializes the ScreenshotUtility by ensuring that the necessary
     * directories for screenshots, baselines, and differences exist.
     */
    public ScreenshotUtility() {
        // Ensures the screenshot directory exists or creates it
        createDirectoryIfNotExist(SCREENSHOT_DIR);

        // Ensures the baseline directory exists or creates it
        createDirectoryIfNotExist(BASELINE_DIR);

        // Ensures the difference directory exists or creates it
        createDirectoryIfNotExist(DIFFERENCE_DIR);
    }

    /**
     * takePageScreenshot captures a screenshot of the entire web page, including parts
     * that are off-screen, and saves it to the specified directory.
     *
     * @param page      The Page instance used to control the browser.
     * @param imageName The name of the image file to save.
     */
    public void takePageScreenshot(Page page, String imageName) {
        // Checks if the WebDriver instance is null
        if (page == null) {
            LOGGER.log(Level.WARNING, "Page is null.");
        }
        byte[] screenshotFile = new byte[0];
        BufferedImage screenshot;
        if (page != null) {
            screenshotFile = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(screenshotFile);
        try {
            screenshot = ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Saves the captured screenshot to the specified directory
        saveScreenshot(screenshot, imageName, SCREENSHOT_DIR);
    }

    /**
     * takeElementScreenshot captures a screenshot of a specific web element and saves
     * it to the specified directory.
     *
     * @param page      The Page instance used to control the browser.
     * @param imageName The name of the image file to save.
     * @param element   The WebElement to capture.
     */
    public void takeElementScreenshot(Page page, String imageName, Locator element) {
        // Checks if the Page instance is null
        if (page == null) {
            LOGGER.log(Level.WARNING, "Page is null.");
        }
        // Checks if the WebElement instance is null
        if (element == null) {
            LOGGER.log(Level.WARNING, "Element is null.");
        }
        byte[] screenshotFile = new byte[0];
        BufferedImage screenshot;
        if (element != null) {
            screenshotFile = element.screenshot(new Locator.ScreenshotOptions());
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(screenshotFile);
        try {
            screenshot = ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Saves the captured screenshot to the specified directory
        saveScreenshot(screenshot, imageName, SCREENSHOT_DIR);
    }

    /**
     * areImagesEqual compares two images (baseline and screenshot) and determines whether
     * they are identical. If they are not, it saves the differences to a specified directory.
     *
     * @param baseImage  The name of the baseline image to compare.
     * @param screenshot The name of the screenshot to compare.
     * @return true if the images are identical, false otherwise.
     */
    public boolean areImagesEqual(String baseImage, String screenshot) {
        BufferedImage baseBufferedImage;
        BufferedImage screenshotBufferedImage;
        // Reads the baseline image from the baseline directory
        baseBufferedImage = ImageComparisonUtil.readImageFromResources(BASELINE_DIR + baseImage + ".png");

        // Reads the screenshot image from the screenshot directory
        screenshotBufferedImage = ImageComparisonUtil.readImageFromResources(SCREENSHOT_DIR + screenshot + ".png");

        // Logs an error if either of the images could not be loaded
        if (baseBufferedImage == null || screenshotBufferedImage == null) {
            LOGGER.log(Level.SEVERE, "One or both images could not be loaded.");
            return false;
        }

        // Compares the two images using ImageDiffer
        ImageComparisonResult imageComparisonResult = new ImageComparison(baseBufferedImage, screenshotBufferedImage).compareImages();

        // If a difference is found, marks the differences and saves the result image
        //BufferedImage differentBufferedImage = imageComparisonResult.getResult();

        // Saves the image with differences marked to the difference directory
        File outputFile = new File(DIFFERENCE_DIR + baseImage + ".png");

        ImageComparisonUtil.saveImage(outputFile, imageComparisonResult.getResult());

        return true;
}

/**
 * saveScreenshot saves the given BufferedImage to the specified directory with the
 * provided image name.
 *
 * @param image     The BufferedImage to save.
 * @param imageName The name of the image file.
 * @param directory The directory to save the image.
 */
private void saveScreenshot(BufferedImage image, String imageName, String directory) {
    try {
        // Constructs the file path for the screenshot
        File outputFile = new File(directory + imageName + ".png");

        // Writes the BufferedImage to a file in PNG format
        ImageIO.write(image, "png", outputFile);

        // Logs the location where the screenshot was saved
        LOGGER.log(Level.INFO, "Screenshot saved at: " + outputFile.getAbsolutePath());
    } catch (IOException ex) {
        // Logs any errors that occur while saving the screenshot
        LOGGER.log(Level.SEVERE, "Error while saving screenshot: " + ex.getMessage(), ex);
    }
}

private void saveScreenshotPath(String imageName, String directory) {
    // Constructs the file path for the screenshot
    File outputFile = new File(directory + imageName + ".png");

    // Logs the location where the screenshot was saved
    LOGGER.log(Level.INFO, "Screenshot saved at: " + outputFile.getAbsolutePath());
}


/**
 * createDirectoryIfNotExist ensures that the specified directory exists,
 * creating it if it does not already exist.
 *
 * @param directoryPath The directory path to check or create.
 */
private void createDirectoryIfNotExist(String directoryPath) {
    try {
        // Converts the string directory path into a Path object
        Path path = Paths.get(directoryPath);

        // Checks if the directory exists, and if not, creates it
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            LOGGER.log(Level.INFO, "Directory created at: " + directoryPath);
        }
    } catch (IOException ex) {
        // Logs any errors that occur while creating the directory
        LOGGER.log(Level.SEVERE, "Error while creating directory: " + ex.getMessage(), ex);
    }
}

}