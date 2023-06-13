package com.framework.services;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.framework.DynamicTest;
import com.framework.baseFunctions.BaseController;
import com.framework.baseFunctions.BaseFunction;
import com.framework.factory.DriverFactory;
import com.framework.factory.ExtentReportsFactory;
import com.framework.factory.TestFactory;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Base64;
import java.util.List;

public class ScreenshotService {
    private static volatile ScreenshotService instance = null;
    private ThreadLocal<Integer> trdIntCounterData = new ThreadLocal<>();
    private ThreadLocal<String> trdScreenshotPath = new ThreadLocal<>();
    private ThreadLocal<String> trdStrFullPath = new ThreadLocal<>();

    private ScreenshotService() {
        trdScreenshotPath.set(MyConfig.strPathReport + PropertiesService.getDatatableFileName() + "\\");
        initFolderDefault(trdScreenshotPath.get());
    }

    public static ScreenshotService init() {
        if (instance == null) {
            synchronized (ScreenshotService.class) {
                if (instance == null) {
                    instance = new ScreenshotService();
                }
            }
        }
        return instance;
    }

    public synchronized Integer getCounterData() {
        if(trdIntCounterData.get() == null)
            setCounterData(1);

        return trdIntCounterData.get();
    }

    public synchronized void setCounterData(Integer intCounterData) {
        trdIntCounterData.set(intCounterData);
    }

    /**
     * Screenshot current page using method getScreenshotAs from WebDriver
     * Can be used for 'Mobile' and 'Web'
     * @return path of Screenshot
     */
    public synchronized String screenshot() {
        getScreenshotAs();
        putIntoReportByThread();
        DynamicTest.trdIntSSCounter.set(DynamicTest.trdIntSSCounter.get()+1);
        return trdScreenshotPath.get();
    }

    /**
     * Mobile   : screenshot until find the specific object
     * Web      : screenshot until end of specific object size by object class
     * @param strObjectName given object name
     * @return path of Screenshot
     */
    public synchronized String screenshotByObject(String strObjectName) {
        String strDescriptionReport = "Keyword: screenshot_by_object<br/>Object Name : "+strObjectName+"<br/>Value :<br/>Description : Screenshot";
        int intSSNumb = 1;
        getScreenshotAs();
        putIntoReportByDescription(strDescriptionReport);
        DynamicTest.trdIntSSCounter.set(DynamicTest.trdIntSSCounter.get()+1);

        //for Android and iOS
        if (!DriverFactory.init().get().toString().contains("Chrome")) {
            try {
                while ((DriverFactory.init().get().findElements(By.xpath(TestFactory.mapXpath.get(strObjectName).getStrXpath()))).size() == 0) {
                    swipeUpMobile();
                    getScreenshotAs();
                    putIntoReportByDescription(strDescriptionReport+intSSNumb);
                    intSSNumb++;
                    DynamicTest.trdIntSSCounter.set(DynamicTest.trdIntSSCounter.get()+1);
                }
            } catch (Exception e) {
                while (!(DriverFactory.init().get().findElement(By.xpath(TestFactory.mapXpath.get(strObjectName).getStrXpath()))).isDisplayed()) {
                    swipeUpMobile();
                    getScreenshotAs();
                    putIntoReportByDescription(strDescriptionReport+intSSNumb);
                    intSSNumb++;
                    DynamicTest.trdIntSSCounter.set(DynamicTest.trdIntSSCounter.get()+1);
                }
            }
        }
        //for web browser
        else {
            swipeUpWeb_SS(strObjectName,strDescriptionReport);
        }

        return trdScreenshotPath.get();
    }

    /**
     * Screenshot whole page for Web browser only
     * @return path of Screenshot
     */
    public synchronized String screenShotFullWhole() {
        String strFileName = DynamicTest.trdScenario.get()+"_"
                +("000" + DynamicTest.trdIntSSCounter.get()).substring(("000" + DynamicTest.trdIntSSCounter.get()).length() - 3);

        Shutterbug.shootPage(DriverFactory.init().get(), ScrollStrategy.WHOLE_PAGE, 500).withName(strFileName).save(trdScreenshotPath.get());
        putIntoReportByThread();
        DynamicTest.trdIntSSCounter.set(DynamicTest.trdIntSSCounter.get()+1);

        return trdScreenshotPath.get();

    }

    /**
     * Screenshot per section by window/screen size
     * for iPhone still on develop
     * @return path of Screenshot
     */
    public synchronized String screenshotFullPart() {
        String strDescriptionReport = "Keyword: screenshot_full_part<br/>Object Name :<br/>Value :<br/>Description : Screenshot";
        String instanceWebDriver = DriverFactory.init().get().toString();

        getScreenshotAs();
        putIntoReportByDescription(strDescriptionReport);
        DynamicTest.trdIntSSCounter.set(DynamicTest.trdIntSSCounter.get()+1);

        //for Mobile driver
        if (!instanceWebDriver.contains("Chrome")) {

            String strXpath = "";

            if (instanceWebDriver.contains("Android")) { //for Android driver
                strXpath = "//android.widget.ScrollView[@content-desc=\"page content\"]";
            } else if (instanceWebDriver.contains("IOS")) { //for IOS driver
                strXpath = "//XCUIElementTypeScrollView[@index=0]";
            }
            try {
                Boolean assertLayer = false;

                WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(10));
                webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(strXpath)));

                int intSSNumb = 1;

                while(!assertLayer) {
                    List<WebElement> currentChilds = DriverFactory.init().get().findElements(By.xpath(strXpath+"//*"));
                    swipeUpMobile();
                    List<WebElement> afterSwipeChilds = DriverFactory.init().get().findElements(By.xpath(strXpath+"//*"));
                    if (currentChilds.size()!=afterSwipeChilds.size()){
                        getScreenshotAs();
                        putIntoReportByDescription(strDescriptionReport+intSSNumb);
                        intSSNumb++;
                        DynamicTest.trdIntSSCounter.set(DynamicTest.trdIntSSCounter.get()+1);
                    } else {
                        int intCountNotSame = 0;
                        for (int i=1;i<=currentChilds.size();i++) {
                            if (currentChilds.get(i) != afterSwipeChilds.get(i)) {
                                intCountNotSame++;
                            }
                            if (intCountNotSame!=0) {
                                getScreenshotAs();
                                putIntoReportByDescription(strDescriptionReport+intSSNumb);
                                DynamicTest.trdIntSSCounter.set(DynamicTest.trdIntSSCounter.get()+1);
                                break;
                            }
                        }
                        if (intCountNotSame==0) {
                            assertLayer = true;
                        }
                    }
                }
            } catch (Exception e) {}
        } else {
            //for web browser
            swipeUpWeb_SS("", strDescriptionReport);
        }
        return trdScreenshotPath.get();
    }

    /**
     * Change image into Base64
     * @param strPathScreenShot Full path file screenshot
     * @return encodedString
     */
    private  String changeImageToBase64(String strPathScreenShot) {
        String encodedString = "";
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(new File(strPathScreenShot));
            encodedString = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedString;
    }

    /**
     * Create folder for put the result screenshot
     * @param strPathFolderResultTesting Path Folder file Screenshot
     */
    private void initFolderDefault(String strPathFolderResultTesting){
        File file = new File(strPathFolderResultTesting);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * Calling screenshot function using method getScreenshotAs of WebDriver
     */
    public void getScreenshotAs() {
        trdStrFullPath.set(trdScreenshotPath.get()
                +DynamicTest.trdScenario.get()+"_"
                +("000" + DynamicTest.trdIntSSCounter.get()).substring(("000" + DynamicTest.trdIntSSCounter.get()).length() - 3)
                +".png");
        /*
         * A.1 Step Login Passed_001.png
         * A.1 Step Login Passed = SCENARIO
         * _
         * 001
         * .png
         * */
        try {
            Thread.sleep(500);
            File scrFile = ((TakesScreenshot) DriverFactory.init().get()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(trdStrFullPath.get()));

        } catch (IOException e) {
            ExtentReportsFactory.init().get().log(Status.FAIL, "Path Not Found." + trdStrFullPath.get());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * swipe down current page on mobile
     */

    public void swipeDownMobile() {
        Dimension mobileDimension = DriverFactory.init().get().manage().window().getSize();
        int intDimensionHeight = mobileDimension.height;
        int intDimensionWidth = mobileDimension.width;

        // Define the start and end points for the swipe action
        int starty = (int) (mobileDimension.height * 0.5);
        int endy = (int) (mobileDimension.height * 0.9);
        int startx = mobileDimension.width / 2;

        TouchAction actionSwipeUP = null;

        if (DriverFactory.init().get().toString().contains("Android")) {
            actionSwipeUP = new TouchAction((AndroidDriver) DriverFactory.init().get());
        } else if (DriverFactory.init().get().toString().contains("IOS")) {
            actionSwipeUP = new TouchAction((IOSDriver) DriverFactory.init().get());
        }

        actionSwipeUP.press(PointOption.point(startx,starty)) // (x,y) --> (Middle point of window, 3/4 of window height)
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(300))) //you can change wait durations as per your requirement
                .moveTo(PointOption.point(startx, endy)) // (x,y) --> (Middle point of window, 1/4 of window height)
                .release()
                .perform();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * swipe up current page on mobile
     */

    public void swipeUpMobile() {
        Dimension mobileDimension = DriverFactory.init().get().manage().window().getSize();
        int intDimensionHeight = mobileDimension.height;
        int intDimensionWidth = mobileDimension.width;
        TouchAction actionSwipeUP = null;

        if (DriverFactory.init().get().toString().contains("Android")) {
            actionSwipeUP = new TouchAction((AndroidDriver) DriverFactory.init().get());
        } else if (DriverFactory.init().get().toString().contains("IOS")) {
            actionSwipeUP = new TouchAction((IOSDriver) DriverFactory.init().get());
        }

        actionSwipeUP.press(PointOption.point(intDimensionWidth / 2, intDimensionHeight * 1 / 2 )) // (x,y) --> (Middle point of window, 3/4 of window height)
                .waitAction(new WaitOptions().withDuration(Duration.ofMillis(300))) //you can change wait durations as per your requirement
                .moveTo(PointOption.point(intDimensionWidth / 2, intDimensionHeight / 5)) // (x,y) --> (Middle point of window, 1/4 of window height)
                .release()
                .perform();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void swipeUpWeb_SS(String strObjectName, String strDescriptionReport) {
        DriverFactory.init().get().switchTo().defaultContent();
        JavascriptExecutor JsEx = (JavascriptExecutor) DriverFactory.init().get();

        JsEx.executeScript("document.scrollingElement.scrollTo(0,-document.scrollingElement.scrollHeight);");

        String strScriptClassToExecute = "";
        String strObjectClassName = "";

        if (strObjectName.equalsIgnoreCase("")) {
            strScriptClassToExecute = "return document.body.scrollHeight";
        } else {
            String replaceQuotation = (TestFactory.mapXpath.get(strObjectName).getStrXpath()).replace("\'","\"");
            String savereplaceQuotation = "";
            MyConfig.mapSaveData.put("savereplaceQuotation", replaceQuotation);
            String[] splitXpathByQuotes = replaceQuotation.split("\"");
            strObjectClassName = splitXpathByQuotes[1];

            if(MyConfig.mapSaveData.get("savereplaceQuotation").contains("class")) {
                strScriptClassToExecute = "return document.getElementsByClassName('"+strObjectClassName+"')[0].scrollHeight";
            } else if (MyConfig.mapSaveData.get("savereplaceQuotation").contains("id")) {
                strScriptClassToExecute = "return document.getElementById('"+strObjectClassName+"').scrollHeight";
            }
        }
//        strScriptClassToExecute = "return document.querySelector('div[ng-hide=\"report\"]').scrollHeight";
        long longWindowSize = ((long) JsEx.executeScript("return window.innerHeight"));
        long longClassHeightSize = ((long) JsEx.executeScript(strScriptClassToExecute));

        for (int i = 1; i <= longClassHeightSize/longWindowSize; i++) {
            if (longClassHeightSize<longWindowSize){
                break;
            }
            String savereplaceQuotation = MyConfig.mapSaveData.get("savereplaceQuotation");
            if(savereplaceQuotation.contains("class")) {
                JsEx.executeScript("document.getElementsByClassName('"+strObjectClassName+"')[0].scrollBy(0,window.innerHeight-10)"); //[step_page_down]
            } else if (savereplaceQuotation.contains("id")) {
                //untuk fineract pakai id
                Actions action = new Actions(DriverFactory.init().get());
                action.sendKeys(Keys.PAGE_DOWN).build().perform();
//                JsEx.executeScript("document.getElementById('"+strObjectClassName+"').scrollBy(0,window.innerHeight-10)"); //[step_page_down]
            }
//            else {
//                JsEx.executeScript("document.querySelector('div[ng-hide=\"report\"]').scrollBy(0,window.innerHeight-10)");
//            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            getScreenshotAs();
            putIntoReportByDescription(strDescriptionReport+i);
            DynamicTest.trdIntSSCounter.set(DynamicTest.trdIntSSCounter.get()+1);
        }

        JsEx.executeScript("document.scrollingElement.scrollTo(0,-document.scrollingElement.scrollHeight);");
    }

    /**
     * Put image into Report using Media Thread and delete it
     */
    private void putIntoReportByThread(){
//        BaseController.mediaThreadLocal.set(MediaEntityBuilder.createScreenCaptureFromPath(System.getProperty("user.dir")+"\\"+ trdStrFullPath.get()).build());
        BaseController.mediaThreadLocal.set(MediaEntityBuilder.createScreenCaptureFromPath(trdStrFullPath.get()).build());

//        //displaying image as a base64
//        String encodedString = changeImageToBase64(System.getProperty("user.dir")+"\\"+ trdStrFullPath.get());
//        BaseController.mediaThreadLocal.set(MediaEntityBuilder.createScreenCaptureFromBase64String(encodedString).build());
    }

    /**
     * Put image into Report for multiple times using description value and delete it
     * @param strDescriptionReport description value that will be put in a Report
     */
    public void putIntoReportByDescription(String strDescriptionReport) {
//       ExtentReportsFactory.init().get().log(Status.INFO, strDescriptionReport, MediaEntityBuilder.createScreenCaptureFromPath(System.getProperty("user.dir")+"\\"+ trdStrFullPath.get()).build());
       ExtentReportsFactory.init().get().log(Status.INFO, strDescriptionReport, MediaEntityBuilder.createScreenCaptureFromPath(trdStrFullPath.get()).build());

//        //displaying image as a base64
//        String encodedString = changeImageToBase64(System.getProperty("user.dir")+"\\"+ trdStrFullPath.get());
//        BaseController.mediaThreadLocal.set(MediaEntityBuilder.createScreenCaptureFromBase64String(encodedString).build());

        BaseController.thrInitExtentReports.set(false);
    }
}
