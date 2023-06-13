package com.application.Channelling;

import com.framework.DAO.XpathDAO;
import com.framework.baseFunctions.BaseFunction;
import com.framework.factory.DriverFactory;
import com.framework.factory.TestFactory;
import com.framework.services.MyConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpecificFunction extends BaseFunction {

    public SpecificFunction(ThreadLocal<String> thrKeyword, ThreadLocal<String> thrObjectName, ThreadLocal<String> thrValue, ThreadLocal<WebElement> thrWebElement, ThreadLocal<String> thrDescription) {
        super(thrKeyword, thrObjectName, thrValue, thrWebElement, thrDescription);
    }
    public static final String resultDataDummyFile = ".//resultDataDummyFile//";
    public static final String scrollIntoview = "arguments[0].scrollIntoView();";

    WebDriver driver = DriverFactory.init().get();

    public void scrollIntoView() {
        loadElement();
        try {
            for (int i = 1; i <= 2; i++) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript(scrollIntoview, thrWebElement.get());
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void assertWebElementIntRate() {
//        loadElement();
//        String strValue = thrWebElement.get().getText()
//                .replace("%","")
//                .replace(".","")
//                .trim();
//        String strDescription = thrDescription.get();
//        Long rate = Long.parseLong(thrValue.get());
//        String strExpected = String.valueOf(rate);
//
//        //ScreenshotService.init().screenshot();
//        Assert.assertEquals(strValue, strExpected, strDescription);
//
//    }
//
//    /**
//     * @throws IOException
//     */
//    public void searchNoApp() throws IOException {
//        loadElement();
//        thrWebElement.get().clear();
//        String[] arr = fileCSVAppForm();
//        switch (thrValue.get()) {
//            case "noapp1":
//                thrWebElement.get().sendKeys(arr[8]);
//            case "noapp2":
//                thrWebElement.get().sendKeys(arr[8*2]);
//            case "noapp3":
//                thrWebElement.get().sendKeys(arr[8*3]);
//            case "noapp4":
//                thrWebElement.get().sendKeys(arr[8*4]);
//            case "noapp5":
//                thrWebElement.get().sendKeys(arr[8*5]);
//            case "noapp6":
//                thrWebElement.get().sendKeys(arr[8*6]);
//            case "noapp7":
//                thrWebElement.get().sendKeys(arr[8*7]);
//            case "noapp8":
//                thrWebElement.get().sendKeys(arr[8*8]);
//        }
//    }
//
//    public String[] fileCSVAppForm() throws IOException {
//
//        List<String> listOfStrings = new ArrayList<>();
//        File folder = new File(resultDataDummyFile); //resultDataDummyFile   resultApprovalFile
//        File[] listOfFiles = folder.listFiles();
//        String fileExcel = null;
//
//        for (int i = 0; i < listOfFiles.length; i++) {
//            if (listOfFiles[i].isFile()) {
//
//                if (listOfFiles[i].getName().contains("APPFILE_L000002") || listOfFiles[i].getName().contains("APPFILE_L000001")) { //APPFILE_L000002
//                    fileExcel = listOfFiles[i].getName();
//                }
//            }
//        }
//        FileReader fr = new FileReader(resultDataDummyFile + fileExcel); //resultDataDummyFile   resultApprovalFile
//        // Created a string to store each character to form word
//        String s = new String();
//        char ch;
//
//        // checking for EOF
//        while (fr.ready()) {
//            ch = (char) fr.read();
//            // Used to specify the delimiters
//            if (ch == '|' || ch == '\n') {
//                // Storing each string in arraylist
//                listOfStrings.add(s.toString());
//                // clearing content in string
//                s = new String();
//            } else {
//                // appending each character to string if the current character is not delimiter
//                s += ch;
//            }
//        }
//        if (s.length() > 0) {
//            // appending last line of strings to list
//            listOfStrings.add(s.toString());
//        }
//        // storing the data in arraylist to array
//        String[] array = listOfStrings.toArray(new String[0]);
//        return array;
//    }
}
