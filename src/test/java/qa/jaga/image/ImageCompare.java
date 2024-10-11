package qa.jaga.image;


import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.ImageComparisonUtil;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * https://romankh3.github.io/image-comparison/
 */
public class ImageCompare {

    public static void main(String[] args) {

        //load images to be compared:
        BufferedImage expectedImage = ImageComparisonUtil.readImageFromResources(System.getProperty("user.dir") + "/src/main/resources/selenium/images/baseline/subscribe.png");
        BufferedImage actualImage = ImageComparisonUtil.readImageFromResources(System.getProperty("user.dir") + "/src/main/resources/selenium/images/screenshot/subscribe.png");

        // where to save the result (leave null if you want to see the result in the UI)
        File resultDestination = new File( "result12.png" );

        //Create ImageComparison object with result destination and compare the images.
        ImageComparisonResult imageComparisonResult = new ImageComparison(actualImage, expectedImage).compareImages();

        //Image can be saved after comparison, using ImageComparisonUtil.
        ImageComparisonUtil.saveImage(resultDestination, imageComparisonResult.getResult());

    }


}
