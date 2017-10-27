package listeners;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kdodonov on 27.10.2017.
 */
public class RunListener implements ITestListener {
    private static final Logger LOGGER = Logger.getLogger(RunListener.class);
    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        takeScreenShot(iTestResult);

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }

    private void takeScreenShot(ITestResult iTestResult) {
        LOGGER.info("Taking screenshot of the page");

        //Taking screenshot
        File tempFile = ((TakesScreenshot) iTestResult
                .getTestContext()
                .getAttribute("driver"))
                .getScreenshotAs(OutputType.FILE);

        // Directory for the file
        String finalDir = iTestResult.getTestContext().getOutputDirectory() + File.separator + "Screenshots";

        //Generating file name
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = String.format("%s_%s.png", iTestResult.getMethod().getMethodName(),
                dateFormat.format(new Date()));

        //Path with file name
        String dirWithFile = finalDir + File.separator + fileName;

        // Saving screenshot
        try {
            FileUtils.copyFile(tempFile, new File(dirWithFile));
        } catch (IOException e) {
            LOGGER.error("Error occurred while taking screenshot: " + e.toString());
        }
    }

}
