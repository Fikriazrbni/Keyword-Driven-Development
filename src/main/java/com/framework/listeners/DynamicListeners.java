package com.framework.listeners;

import com.framework.baseFunctions.BaseController;
import com.framework.factory.ExtentReportsFactory;
import com.framework.factory.TestFactory;
import com.framework.factory.DriverFactory;
import com.framework.services.MyConfig;
import com.framework.services.ScreenshotService;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class DynamicListeners implements ITestListener {
    static Boolean isEndRunning;

    static ExtentReports extentReports;

    @Override
    public synchronized void onTestStart(ITestResult result) {
        Class cls = TestFactory.thrDynamicClass.get();
        try {
            int intNo = (int) ((ThreadLocal) cls.getDeclaredField("trdIntNoData").get(null)).get();
            String strScriptID = (String) ((ThreadLocal) cls.getDeclaredField("trdStrScriptID").get(null)).get();
            String strAction = (String) ((ThreadLocal) cls.getDeclaredField("trdStrAction").get(null)).get();
            String strTestGroup = (String) ((ThreadLocal) cls.getDeclaredField("trdTestGroup").get(null)).get();
            String strScenario = (String) ((ThreadLocal) cls.getDeclaredField("trdScenario").get(null)).get();
            String strTestName = intNo + "-" + strScriptID + "-" + strAction;
            ExtentReportsFactory.init().createTest(extentReports, strTestName, strScenario, strTestGroup);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
    //if testSusccesss
    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportsFactory.init().get().log(Status.PASS, "Test Passed");
        DriverFactory.init().tearDown();
        isEndRunning = true;
        extentReports.flush();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ScreenshotService.init().screenshot();

        /** dikomen karena suka error kalau misalnya failed*/
//        if (DriverFactory.init().get().toString().contains("Android") || DriverFactory.init().get().toString().contains("IOS"))
//            ScreenshotService.init().screenshot();
//        else
//            ScreenshotService.init().screenshotFullPart();

        ExtentReportsFactory.init().get().log(Status.FAIL,
                "Test Failed. </br>" +
                        "Last Screen : ", BaseController.mediaThreadLocal.get());
        DriverFactory.init().tearDown();
        isEndRunning = true;
        extentReports.flush();

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportsFactory.init().get().log(Status.SKIP, "Test Skipped");
        DriverFactory.init().tearDown();
        isEndRunning = true;
        extentReports.flush();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {
        if (extentReports == null)
            extentReports = ExtentReportsFactory.init().setupExtentReports();

    }

    public void backupIfSuccess(){
        //Backup output ketika Success
        LocalDateTime now = LocalDateTime.now();
        Path destination = Paths.get(MyConfig.strCurrentPath + "\\outputSuccess\\" + now.getDayOfMonth() + "-" + now.getMonthValue() + "-" + now.getYear() + "-" + now.getHour() + "." + now.getMinute());
        Path targetSs = Path.of(destination + "\\Screenshot");
        Path source = Paths.get(MyConfig.strCurrentPath+"\\Screenshot");
        Path sourceHtml = Paths.get(MyConfig.strCurrentPath+"\\AT-Report_BluExtraCash.html");
        Path targetHtml = Path.of(destination + "\\AT-Report_BluExtraCash.html");

        try {
            Files.createDirectory(destination);
            Files.createDirectory(targetSs);
            FileUtils.copyDirectory(source.toFile(), targetSs.toFile());
            Files.copy(sourceHtml, targetHtml);
            System.out.println("Data di backup " + MyConfig.strCurrentPath + "\\outputSuccess");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //update ReportHtml
    @Override
    public void onFinish(ITestContext context) {
        extentReports.flush();
            if (isEndRunning){
                backupIfSuccess();
                isEndRunning = false;
            }
    }

}
