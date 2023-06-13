package com.framework.baseFunctions;

import com.framework.factory.ExtentReportsFactory;
import com.framework.factory.TestFactory;
import com.framework.factory.DriverFactory;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

/**
 * Base Controller class which is give the decission, is Base Function or Assert Function
 */
public class BaseController {
    protected ThreadLocal<String> thrKeyword = new ThreadLocal<>();
    protected ThreadLocal<String> thrObjectName = new ThreadLocal<>();
    protected ThreadLocal<String> thrValue = new ThreadLocal<>();
    protected ThreadLocal<WebElement> thrWebElement = new ThreadLocal<>();
    protected ThreadLocal<String> thrApplication = new ThreadLocal<>();
    protected ThreadLocal<String> thrDescription = new ThreadLocal<>();
    public static ThreadLocal<Media> mediaThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<Boolean> thrInitExtentReports = new ThreadLocal<>();


    public BaseController() {
    }

    /**
     * Constructor for init inheritance class
     *
     * @param thrKeyword
     * @param thrObjectName
     * @param thrValue
     * @param thrWebElement
     * @param thrDescription
     */
    public BaseController(ThreadLocal<String> thrKeyword, ThreadLocal<String> thrObjectName, ThreadLocal<String> thrValue, ThreadLocal<WebElement> thrWebElement, ThreadLocal<String> thrDescription) {
        this.thrKeyword = thrKeyword;
        this.thrObjectName = thrObjectName;
        this.thrValue = thrValue;
        this.thrWebElement = thrWebElement;
        this.thrDescription = thrDescription;
    }

    /**
     * @param strKeyword    Keyword from Datatable
     * @param strObjectName Object Name from Datatable
     * @param strValue      Value from Datatable
     */
    public BaseController(String strKeyword, String strObjectName, String strValue, String strApplication) {
        thrKeyword.set(strKeyword);
        thrObjectName.set(strObjectName);
        thrApplication.set(strApplication);
        thrValue.set(strValue);
        thrInitExtentReports.set(true);
        mediaThreadLocal.set(null);
        dynamicMethod();

    }


    /**
     * Logic for throw what kind functions
     */
    public void dynamicMethod() {

        try {
            Object obj;
            if (thrKeyword.get().contains("assert"))
                obj = new AssertFunction(thrKeyword, thrObjectName, thrValue, thrWebElement, thrDescription);
            else {
                obj = Class.forName("com.application." + thrApplication.get() + ".SpecificFunction")
                        .getDeclaredConstructor(ThreadLocal.class, ThreadLocal.class, ThreadLocal.class, ThreadLocal.class, ThreadLocal.class)
                        .newInstance(thrKeyword, thrObjectName, thrValue, thrWebElement, thrDescription);
            }

            executeMethodDynamicClass(obj);

            if (thrKeyword.get().contains("assert"))
                createReport(Status.PASS);
            else
                createReport(Status.INFO);

        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
            createReport(Status.FAIL, e.getCause());
        } catch (Exception e) {
            e.printStackTrace();
            createReport(Status.FAIL, e.fillInStackTrace());
        }
    }

    /**
     * @param e Exception
     * @return String e.printStackTrace();
     */
    protected String convertException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString().replace("\n", "<br/>");
    }

    /**
     * Load Element by Xpath and put into var local
     */
    protected void loadElement() {
        try {
            WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(20));
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath();

            webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(strXpath)));

            WebElement webElement = DriverFactory.init().get().findElement(By.xpath(strXpath));
            String strDescription = TestFactory.mapXpath.get(thrObjectName.get()).getStrDescription();
            thrWebElement.set(webElement);
            thrDescription.set(strDescription);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("Web Element " + thrObjectName.get() + " Not Found.");
        }
    }

    /**
     * Execute class method in SpecificFunction every application, but if not found there then go to
     * parent class is BaseFunction
     *
     * @param obj
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected void executeMethodDynamicClass(Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            obj.getClass().getDeclaredMethod(thrKeyword.get()).invoke(obj);
        } catch (NoSuchMethodException e) {
            obj.getClass().getSuperclass().getDeclaredMethod(thrKeyword.get()).invoke(obj);
        }

    }

    /**
     * Create extend report for every step execution
     *
     * @param status
     * @param e
     */
    protected void createReport(Status status, Throwable... e) {


        String strDescriptionReport = "Keyword : " + thrKeyword.get() +
                "<br/>Object Name : " + thrObjectName.get() +
                "<br/>Value    : " + thrValue.get() +
                "<br/>Description : " + thrDescription.get();

        if (thrInitExtentReports.get()){
            ExtentReportsFactory.init().get().log(status,
                    strDescriptionReport + (e.length != 0 ? "<br/>Error Message : <br/>" + e[0] : ""), mediaThreadLocal.get());
        }

        if (e.length != 0)
            throw new NullPointerException(strDescriptionReport);
    }


}
