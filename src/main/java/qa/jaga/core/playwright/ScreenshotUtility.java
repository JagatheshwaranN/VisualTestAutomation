package qa.jaga.core.playwright;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.testng.Assert;

import java.awt.image.BufferedImage;
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
 * It utilizes Playwright and ImageComparison libraries to automate visual testing.
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
        // Log a warning if the page object is null
        if (page == null) {
            LOGGER.log(Level.WARNING, "Page instance is null.");
            return;
        }

        // Capture the element screenshot
        page.screenshot(new Page.ScreenshotOptions().setFullPage(true).setPath(saveScreenshotPath(imageName)));
        LOGGER.log(Level.INFO, "Page screenshot taken: " + imageName);
    }

    /**
     * takeElementScreenshot captures a screenshot of a specific web element and saves
     * it to the specified directory.
     *
     * @param page      The Page instance used to control the browser.
     * @param imageName The name of the image file to save.
     * @param element   The Locator representing the web element to capture.
     */
    public void takeElementScreenshot(Page page, String imageName, Locator element) {
        // Log a warning if the page object is null
        if (page == null) {
            LOGGER.log(Level.WARNING, "Page instance is null.");
            return;
        }
        // Log a warning if the Locator (web element) is null
        if (element == null) {
            LOGGER.log(Level.WARNING, "Element locator is null.");
            return;
        }
        // Capture the element screenshot
        element.screenshot(new Locator.ScreenshotOptions().setPath(saveScreenshotPath(imageName)));
        LOGGER.log(Level.INFO, "Element screenshot taken: " + imageName);
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
        // Reads the baseline image from the baseline directory
        final BufferedImage baseBufferedImage = ImageComparisonUtil.readImageFromResources(BASELINE_DIR + baseImage + ".png");

        // Reads the screenshot image from the screenshot directory
        final BufferedImage screenshotBufferedImage = ImageComparisonUtil.readImageFromResources(SCREENSHOT_DIR + screenshot + ".png");

        // Logs an error if either of the images could not be loaded
        if (baseBufferedImage == null || screenshotBufferedImage == null) {
            LOGGER.log(Level.SEVERE, "One or both images could not be loaded.");
            return false;
        }

        // Compares the two images using ImageComparison
        final ImageComparisonResult imageComparisonResult = new ImageComparison(baseBufferedImage, screenshotBufferedImage).compareImages();

        // Save the image with differences marked to the difference directory if images don't match
        final File outputFile = new File(DIFFERENCE_DIR + baseImage + ".png");

        // If images are not a match, save the result and return false
        if (ImageComparisonState.MATCH != imageComparisonResult.getImageComparisonState()) {
            ImageComparisonUtil.saveImage(outputFile, imageComparisonResult.getResult());
            LOGGER.log(Level.INFO, "Image difference saved for: " + baseImage);
            return false;
        }

        // Assert that images are a match (visual validation)
        Assert.assertEquals(ImageComparisonState.MATCH, imageComparisonResult.getImageComparisonState());
        LOGGER.log(Level.INFO, "Images are identical: " + baseImage + " vs " + screenshot);
        return true;
    }

    /**
     * saveScreenshotPath constructs the file path for saving the screenshot.
     *
     * @param imageName The name of the image file.
     * @return The file path as a Path object.
     */
    private Path saveScreenshotPath(final String imageName) {
        // Constructs the file path for the screenshot
        final File outputFile = new File(ScreenshotUtility.SCREENSHOT_DIR + imageName + ".png");

        // Logs the location where the screenshot was saved
        LOGGER.log(Level.INFO, "Screenshot saved at: " + outputFile.getAbsolutePath());
        return Paths.get(outputFile.toURI());
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
            final Path path = Paths.get(directoryPath);

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
