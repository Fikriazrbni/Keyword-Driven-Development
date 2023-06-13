package com.framework.factory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.framework.services.MyConfig;

public class ExtentReportsFactory {
    private static volatile ExtentReportsFactory instance = null;

    private ExtentReportsFactory() {
    }

    public static ExtentReportsFactory init() {
        if (instance == null) {
            synchronized (ExtentReportsFactory.class) {
                if (instance == null)
                    instance = new ExtentReportsFactory();
            }
        }
        return instance;
    }

    volatile ThreadLocal<ExtentTest> trdExtentTest = new ThreadLocal<>();

    public ExtentTest get() {
        return trdExtentTest.get();
    }

    public void set(ExtentTest extentTest) {
        trdExtentTest.set(extentTest);
    }

    public void remove() {
        trdExtentTest.remove();
    }

    /**
     * @return setup ExtentReports
     */
    public ExtentReports setupExtentReports() {
        ExtentSparkReporter extentSparkReporter = new ExtentSparkReporter("AT-Report_BluExtraCash.html");
        ExtentReports extentReports = new ExtentReports();

        extentReports.attachReporter(extentSparkReporter);

        //Setting config the spark
        extentSparkReporter.config().setTheme(Theme.DARK);
        extentSparkReporter.config().setDocumentTitle("Dynamic Automation Testing Report - BluExtraCash");
        extentSparkReporter.config().setReportName("Dynamic Automation Testing Report - BluExtraCash");

        //Setting config ExtentReport
        extentReports.setSystemInfo("Execute by ", MyConfig.strExecuteBy);
        extentReports.setSystemInfo("Execute on OS ", "Mobile & Web Framework Automation");

        return extentReports;
    }

    /**
     * Create test and put into Thread Local
     *
     * @param extentReports  which ExtentReports
     * @param strTestName    strTestName for the report
     * @param strDescription strDescription for the report
     * @param strCategory    strCategory is TAG Test Group for the report
     */
    public void createTest(ExtentReports extentReports, String strTestName, String strDescription, String strCategory) {
        set(
                extentReports.createTest(strTestName, strDescription)
                        .assignCategory(strCategory)
        );
    }


}
