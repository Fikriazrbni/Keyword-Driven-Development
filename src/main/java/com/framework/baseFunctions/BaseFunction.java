package com.framework.baseFunctions;

import com.framework.DAO.XpathDAO;
import com.framework.DynamicTest;
import com.framework.baseFunctions.baseInterface.BaseFunctionsInterface;
import com.framework.factory.DriverFactory;
import com.framework.factory.ExcelFactory;
import com.framework.factory.PostgreSQL;
import com.framework.factory.TestFactory;
import com.framework.services.MyConfig;
import com.framework.services.ScreenshotService;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.clipboard.HasClipboard;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.io.FileUtils.deleteDirectory;

/**
 * Base Function class implement AssertInterface
 */
public class BaseFunction extends BaseController implements BaseFunctionsInterface {
    static boolean isRunningFirst = true;

    public static ThreadLocal<Integer> trdIntTempCounterRow = new ThreadLocal<>();
    public static ThreadLocal<Integer> trdIntTempNoData = new ThreadLocal<>();
    public static ThreadLocal<Integer> trdIntTotalLooping = new ThreadLocal<>();
    public static ThreadLocal<Integer> trdIntCounterLooping = new ThreadLocal<>();

    public BaseFunction() {
    }

    public BaseFunction(ThreadLocal<String> thrKeyword, ThreadLocal<String> thrObjectName, ThreadLocal<String> thrValue, ThreadLocal<WebElement> thrWebElement, ThreadLocal<String> thrDescription) {
        super(thrKeyword, thrObjectName, thrValue, thrWebElement, thrDescription);
    }

    /**
     * Start chrome driver
     */
    @Override
    public void startChromeDriver() {
        DriverFactory.init().initWebDriver();
    }

    public void closeChromeDriver(){
        DriverFactory.init().get().quit();
    }

    /**
     * Start mobile driver
     */
    @Override
    public void startMobileDriver() {
        MyConfig.mapSaveData.put("saveResetApp", "false");
        DriverFactory.init().initMobileDriver();
    }

    public void startConnectPostgre(){
        PostgreSQL.connectionPostgreSQL();
    }

    public void resetApp() {
        MyConfig.mapSaveData.put("saveResetApp", "true");
        DriverFactory.init().initMobileDriver();
    }

    @Override
    public void switchDriver() {
        DriverFactory.init().switchDriver();
    }


    /**
     * Click the WebElement
     */
    @Override
    public void click() {
        loadElement();
        thrWebElement.get().click();
    }

    @Override
    public void doubleClick() {
        Actions action = new Actions(DriverFactory.init().get());
        WebElement btn =DriverFactory.init().get().findElement(By.xpath(TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath()));
        action.doubleClick(btn).perform();
    }

    /**
     * Set Text the WebElement
     */
    @Override
    public void setText() throws NullPointerException {
        loadElement();
        thrWebElement.get().clear();
        thrWebElement.get().sendKeys(thrValue.get());
    }


    public void setTextSaveData() throws NullPointerException {
        loadElement();
        thrWebElement.get().clear();
        thrWebElement.get().sendKeys(MyConfig.mapSaveData.get(thrValue.get()));
    }

    /**
     * Put url on WebDriver
     */
    @Override
    public void goto_url() {
        boolean bolIsTimeout = true;
        do{
            try {
                DriverFactory.init().get().get(thrValue.get());
                bolIsTimeout = false;
            }catch (TimeoutException ex){
            }
        }while (bolIsTimeout);
//        ((AndroidDriver)DriverFactory.init().get()).pressKey(new KeyEvent(AndroidKey.DIGIT_5));
    }


    /**
     * Change IFrame
     */
    @Override
    public void changeIframe() {
        loadElement();
        DriverFactory.init().get().switchTo().frame(thrWebElement.get());
    }

    /**
     * Change IFrame to default
     */
    @Override
    public void changeIframeDefault() {
        loadElement();
        DriverFactory.init().get().switchTo().defaultContent();
    }

    /**
     * Wait for Seconds
     *
     * @throws InterruptedException
     */
    @Override
    public void waitForSeconds() throws InterruptedException {
        Thread.sleep(Integer.parseInt(thrValue.get()) * 1000);
    }

    /**
     * Screenshot use Shutterbug for full Port (page) of mobile app and Web
     */
    @Override
    public void screenshotFullPart() {
        WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(5));
        ScreenshotService.init().screenshotFullPart();
    }

    @Override
    public void screenShotMobile() {
        WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(5));
        ScreenshotService.init().screenshot();
    }

    /**
     * Screenshot chrome use WebDriver.TakeScreenshot
     */
    @Override
    public void screenShotChrome() {
        WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(5));
        ScreenshotService.init().screenshot();
    }

    @Override
    public void screenshotByObject(){
        WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(5));
        ScreenshotService.init().screenshotByObject(thrObjectName.get());
    }

    public void swipeUpMobileUntil(){
        //for Android and iOS
        if (!DriverFactory.init().get().toString().contains("Chrome")) {
            try {
                while ((DriverFactory.init().get().findElements(By.xpath(TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath()))).size() == 0) {
                    ScreenshotService.init().swipeUpMobile();

                }
            } catch (Exception e) {
                while (!(DriverFactory.init().get().findElement(By.xpath(TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath()))).isDisplayed()) {
                    ScreenshotService.init().swipeUpMobile();

                }
            }
        }
    }

    public void swipeDownMobileUntil(){
        //for Android and iOS
        if (!DriverFactory.init().get().toString().contains("Chrome")) {
            try {
                while ((DriverFactory.init().get().findElements(By.xpath(TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath()))).size() == 0) {
                    ScreenshotService.init().swipeDownMobile();
                }
            } catch (Exception e) {
                while (!(DriverFactory.init().get().findElement(By.xpath(TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath()))).isDisplayed()) {
                    ScreenshotService.init().swipeDownMobile();

                }
            }
        }
    }

    /**
     * Wait until Element Exist
     */
    @Override
    public void waitUntilWebElementExist() {
        WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(10));
        webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath())));
    }

    /**
     * Save data for process in other keyword
     */
    @Override
    public void saveData() {
        String strTempValue = thrValue.get();
//        if (! strTempValue.isEmpty()) {
            MyConfig.mapSaveData.put(thrObjectName.get(), thrValue.get());
            thrDescription.set("Save data" +
                    "<br/>Key = " + thrObjectName.get() +
                    "<br/>Value = " + thrValue.get());
//        }
    }


    public void putsaveDataToSaveData() {
        String strTempValue = MyConfig.mapSaveData.get(thrValue.get());
//        if (! strTempValue.isEmpty()) {
        MyConfig.mapSaveData.put(thrObjectName.get(), strTempValue);
        thrDescription.set("Save data" +
                "<br/>Key = " + thrObjectName.get() +
                "<br/>Value = " + thrValue.get());
//        }
    }
    public void saveDataFromXpath() {
        WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(10));
        String tempthrObjName = thrValue.get();
        WebElement webElement = DriverFactory.init().get().
                findElement(By.xpath(TestFactory.mapXpath.get(tempthrObjName).getStrXpath()));
        String getValueXpath = webElement.getText();

        MyConfig.mapSaveData.put(thrObjectName.get(), getValueXpath);
        thrDescription.set("Save data" +
                "<br/>Key = " + thrObjectName.get() +
                "<br/>Value = " + getValueXpath);
    }

    public void getTextElement() {
        loadElement();
        thrWebElement.get().getText();
    }


    /**
     * Go To Row when found the anchorGoToValue, Row will be intCounterRow + 1
     * Example, if anchor on row 10, so the next loop intCounterRow will be 11.
     */
    @Override
    public void goToValue() {
        int intCounterRow = getRowByValue(thrValue.get(), "anchor");
        if (intCounterRow != -1)
            DynamicTest.trdIntCounterRow.set(intCounterRow);
    }

    public void exitAutomation() {
        DynamicTest.trdIntCounterRow.set(DynamicTest.trdIntTotalRow.get());
    }

    /**
     * Anchor function GoToValue
     */
    @Override
    public void anchorGoToValue() {

    }

    /**
     * Go To the other sheet by Value.
     * Value is a Sheet Name which is the program will switch the Action Sheet into Value Sheet
     */
    @Override
    public void goToSheet() {
        int intTempCounterRow = DynamicTest.trdIntCounterRow.get();
        String strTempSheet = DynamicTest.trdStrAction.get();
        DynamicTest.trdStrAction.set(thrValue.get());
        DynamicTest.execute(thrValue.get());
        DynamicTest.trdIntCounterRow.set(intTempCounterRow);
        DynamicTest.trdStrAction.set(strTempSheet);
    }

    /**
     * Looping data  for specific sheet
     * Data will counter by intNoData to specific sheet
     */
    @Override
    public void forDataBySheet() {
        int[] intDataLooping = getDataForDataBySheet(DynamicTest.trdIntNoData.get());
        if (intDataLooping[1] == 0) {
            DynamicTest.trdIntCounterRow.set(getRowByValue("", "endDataForSheet"));
            thrDescription.set("No data found");

        } else {
            trdIntTempCounterRow.set(DynamicTest.trdIntCounterRow.get());
            trdIntTotalLooping.set(intDataLooping[1]);
            trdIntTempNoData.set(DynamicTest.trdIntNoData.get());
            trdIntCounterLooping.set(0);
            DynamicTest.trdIntNoData.set(intDataLooping[0]);
            thrDescription.set("Looping for " + intDataLooping[1] + " datas");

        }

    }

    /**
     * Anchor for End For Data By Sheet
     * 1. Set Counter Row to back to anchor for data by sheet
     * 2. Reset after looping end
     */
    @Override
    public void endDataForSheet() {
        trdIntCounterLooping.set(trdIntCounterLooping.get() + 1);
        thrDescription.set("End looping for data - " + (trdIntCounterLooping.get()));

        if (trdIntCounterLooping.get() < trdIntTotalLooping.get()) {
            DynamicTest.trdIntCounterRow.set(trdIntTempCounterRow.get());
            DynamicTest.trdIntNoData.set(DynamicTest.trdIntNoData.get() + 1);
        } else {
            DynamicTest.trdIntNoData.set(trdIntTempNoData.get());
            trdIntTempCounterRow.set(null);
            trdIntTotalLooping.set(null);
            trdIntTempNoData.set(null);
            trdIntCounterLooping.set(null);
        }

    }

    /**
     * Search row counter by Value
     *
     * @param strCompareValue
     * @param strAnchorKeyword
     * @return
     */
    public int getRowByValue(String strCompareValue, String strAnchorKeyword) {
        Sheet shtAction = ExcelFactory.init().getSheetExcel(MyConfig.strPathDatatable, DynamicTest.trdStrAction.get());

        int intColumnValue = ExcelFactory.init().getColumnByName(shtAction, "Value");
        int intColumnKeyword = ExcelFactory.init().getColumnByName(shtAction, "Keyword");

        int intCounterRow = -1;

        for (int j = DynamicTest.trdIntCounterRow.get(); j <= shtAction.getLastRowNum(); j++) {
            try {
                String strKeyword = ExcelFactory.init().getCellValue(shtAction.getWorkbook(), shtAction.getRow(j).getCell(intColumnKeyword));
                String strValue = ExcelFactory.init().getCellValue(shtAction.getWorkbook(), shtAction.getRow(j).getCell(intColumnValue), 1);
                if (strValue.equalsIgnoreCase(strCompareValue) && strKeyword.contains(strAnchorKeyword)) {
                    intCounterRow = j;
                    break;
                }
            } catch (Exception e) {
            }

        }
        return intCounterRow;

    }

    public void isWebElementExist() {
        WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(15));
        String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath();
        webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(strXpath)));

    }

    /**
     * Counter data for forDataBySheet
     *
     * @param intNoData
     * @return int array [Start Row Data,IntTotalData for looping]
     */
    public int[] getDataForDataBySheet(int intNoData) {
        Sheet shtData = ExcelFactory.init().getSheetExcel(MyConfig.strPathDatatable, thrValue.get());
        int intColumnNo = ExcelFactory.init().getColumnByName(shtData, "No");
        int intTotalData = 0;
        int intStartRowData = -1;

        for (int j = 1; j <= shtData.getLastRowNum(); j++) {
            try {
                String strNoData = ExcelFactory.init().getCellValue(shtData.getWorkbook(), shtData.getRow(j).getCell(intColumnNo));
                if (strNoData.equalsIgnoreCase(intNoData + "")) {
                    if (intStartRowData == -1)
                        intStartRowData = j;
                    intTotalData++;
                }
            } catch (NullPointerException ex) {
            }

        }

        return new int[]{intStartRowData, intTotalData};
    }


    public void sliderJumlahPinjaman(int coordinateX) {
        loadElement();
        int x = thrWebElement.get().getLocation().getX();
        int y = thrWebElement.get().getLocation().getY();
        String strJumlahPinjaman = "-1";

        do {
            TouchAction ScrollLogin = new TouchAction((AndroidDriver) DriverFactory.init().get());
            TouchAction perform = ScrollLogin.press(PointOption.point(x, y))
                    .waitAction(new WaitOptions().withDuration(Duration.ofMillis(500)))
                    .moveTo(PointOption.point(x += coordinateX, y))
                    .release()
                    .perform();
            WebElement webElementJumlahPinjaman = DriverFactory.init().get().
                    findElement(By.xpath(TestFactory.mapXpath.get("txtJumlahPinjaman").getStrXpath()));
            strJumlahPinjaman = webElementJumlahPinjaman.getText().replace("Rp ", "").replace(".", "");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } while (!strJumlahPinjaman.equals(thrValue.get()));
    }

    public void sliderTenor(int coordinateX) throws InterruptedException {
        loadElement();
        int x = thrWebElement.get().getLocation().getX();
        int y = thrWebElement.get().getLocation().getY();
        String strTenor = "-1";
        do {
            TouchAction ScrollLogin = new TouchAction((AndroidDriver) DriverFactory.init().get());
            TouchAction perform = ScrollLogin.press(PointOption.point(x, y))
                    .waitAction(new WaitOptions().withDuration(Duration.ofMillis(500)))
                    .moveTo(PointOption.point(x += coordinateX, y))
                    .release()
                    .perform();
            WebElement webElementTenor = DriverFactory.init().get().findElement(By.xpath(TestFactory.mapXpath.get("txtTenor").getStrXpath()));
            strTenor = webElementTenor.getText();
            Thread.sleep(300);

        } while (!strTenor.equalsIgnoreCase(thrValue.get()));
    }

    public void scrollPage(int pointX, int pointY, int moveX, int moveY) {
        TouchAction ScrollLogin = new TouchAction((AndroidDriver) DriverFactory.init().get());
        TouchAction perform = ScrollLogin.press(PointOption.point(pointX, pointY))
                .waitAction(new WaitOptions().withDuration(Duration.ofMillis(1000))) //you can change wait durations as per your requirement (transition)
                .moveTo(PointOption.point(moveX, moveY))
                .release()
                .perform();

//        Thread.sleep(500);
    }

    public void testArray() {
        List<Integer> lstCoordinates = Arrays.stream(thrValue.get().split(";"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        scrollPage(lstCoordinates.get(0), lstCoordinates.get(1), lstCoordinates.get(2), lstCoordinates.get(3));
    }

    @Override
    public void scrollUntilElement() throws InterruptedException {

        Boolean notExist = true;
        WebElement webElementSK = null;
        do {
//            scrollPage(lstCoordinates.get(0), lstCoordinates.get(1), lstCoordinates.get(2), lstCoordinates.get(3));
            scrollPage(300, 1200, 300, 400);
            try {
                isWebElementExist();
                notExist = false;
            } catch (Exception e) {
            }
        } while (notExist);
    }

    public void selectDropdownList() throws InterruptedException {
        String strTempXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath();
        String[] thrValueSplit = thrValue.get().split(";");
        String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("?", thrValue.get());
        TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        //TODO Still development
        try {
            click();
        }catch (Exception e){
            click();
        }
        TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strTempXpath, ""));
    }

    public void selectRadioBtn() throws InterruptedException {
        String strTempXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath();
        String[] thrValueSplit = thrValue.get().split(";");
        String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", thrValue.get());
        TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        //TODO Still development
        try {
            click();
        }catch (Exception e){
            click();
        }
        TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strTempXpath, ""));
    }

    public void selectDropdownListSaveData() throws InterruptedException {
        String strTempXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath();
        String[] thrValueSplit = thrValue.get().split(";");
        String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("?", MyConfig.mapSaveData.get(thrValue.get()));
        TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        //TODO Still development
        try {
            click();
        }catch (Exception e){
            click();
        }
        TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strTempXpath, ""));
    }

    public void ifObjectExist_Gotovalue(){
        try{
            isWebElementExist();
            goToValue();

        } catch (Exception e) {
        }
    }

    public void ifObjectNotExist_Gotovalue(){
        try{
            isWebElementExist();
        } catch (Exception e) {
            goToValue();
        }
    }

    public void scrollUpUntilElement(){
        Boolean notExist = true;
        WebElement webElementSK = null;
        do {
            scrollPage(300, 400, 300, 1200);
            try {
                isWebElementExist();
                notExist = false;
            } catch (Exception e) {
            }
        } while (notExist);


    }
    public void debug() {
        double j = 12.5;
        System.out.println(j);
        String test = MyConfig.mapSaveData.get("");
        String x = "23";
    }

    public void debug2() {
        double j = 12;
        String x = "23";
    }

    public void calculationBlu(){
        String strPathExcel = "src\\main\\resources\\calculationBlu.xlsx";
        Sheet shtCalculation = ExcelFactory.init().getSheetExcel(strPathExcel, "disbursement");
        Workbook wb = shtCalculation.getWorkbook();
        Cell cellPinjaman = shtCalculation.getRow(1).getCell(1);
        Cell cellTenor = shtCalculation.getRow(2).getCell(1);
        Cell cellInterestBCAD = shtCalculation.getRow(2).getCell(4);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String enterKey = System.getProperty("line.separator");


        //need savedata from excel for cellPinjaman dan cellTenor
        if (MyConfig.mapSaveData.get("cellPinjaman") != null && MyConfig.mapSaveData.get("saveTenor") != null){
            String saveCellPinjaman = MyConfig.mapSaveData.get("cellPinjaman").replace(".", "").replace(" Bulan", "").replace("Rp ", "").replace("Rp ", "").split(",")[0];
            String saveCellTenor = MyConfig.mapSaveData.get("saveTenor").replace(".", "").replace(" Bulan", "").replace("Rp ", "").split(",")[0];
            String saveCellInterestBCAD = MyConfig.mapSaveData.get("saveinterestLOS").replace("%","");
            int saveIntPinjaman = Integer.parseInt(saveCellPinjaman);
            int saveIntTenor = Integer.parseInt(saveCellTenor);
            Double saveIntInterestBCAD = Double.parseDouble(saveCellInterestBCAD)/100;
            cellPinjaman.setCellValue(saveIntPinjaman);
            cellTenor.setCellValue(saveIntTenor);
            cellInterestBCAD.setCellValue(saveIntInterestBCAD);
        }else {
            MyConfig.mapSaveData.put("cellPinjaman",String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellPinjaman).getNumericCellValue() +""))).split("\\.")[0]);
            MyConfig.mapSaveData.put("saveTenor",String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellTenor).getNumericCellValue() +""))).split("\\.")[0]);
        }

        for (int i = 1; i <= 3 ; i++) {
            //Cell Interest
            Cell cellInterest = shtCalculation.getRow(14 + i).getCell(2);
            MyConfig.mapSaveData.put("cellInterest" + i,String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellInterest).getNumericCellValue() +""))).split("\\.")[0]);
            cellInterest.setCellFormula("IF($B$3>="+i+",$B$7,0)");

            //Cell Principal
            Cell cellPrincipal = shtCalculation.getRow(14 + i).getCell(3);
            MyConfig.mapSaveData.put("cellPrincipal" + i,String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellPrincipal).getNumericCellValue() +""))).split("\\.")[0]);
            cellPrincipal.setCellFormula("IF($B$3>="+i+",IF($F"+(14+i)+"-$B$8<20,$B$8+$F"+(15+i)+",$B$8),0)" + enterKey);

            //Cell Installment
            Cell cellInstallment = shtCalculation.getRow(14 + i).getCell(4);
            MyConfig.mapSaveData.put("cellInstallment" + i,String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellInstallment).getNumericCellValue() +""))).split("\\.")[0]);
            cellInstallment.setCellFormula("IF($B$3>="+i+",IF($F"+(14+i)+"-$B$8<20,$B$9+$F"+(15+i)+",$B$9),0)" + enterKey);

            //Cell Sisa pokok
            Cell cellSisaPokok = shtCalculation.getRow(14 + i).getCell(5);
            MyConfig.mapSaveData.put("cellSisaPokok" + i,String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellSisaPokok).getNumericCellValue() +""))).split("\\.")[0]);
            cellSisaPokok.setCellFormula("IF($B$3>="+i+",$F"+(14+i)+"-$B$8,0)" + enterKey);
        }

        //Cell provinsi+materai
        Cell provisiDanMaterai = shtCalculation.getRow(14).getCell(4);
        MyConfig.mapSaveData.put("cellProvinsiDanMaterai",String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(provisiDanMaterai).getNumericCellValue() +""))).split("\\.")[0]);
        provisiDanMaterai.setCellFormula("E4+E5" + enterKey);


        //Cell Total interest
        Cell cellTotalInterest = shtCalculation.getRow(19).getCell(2);
        MyConfig.mapSaveData.put("cellTotalInterest", String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellTotalInterest).getNumericCellValue() +""))).split("\\.")[0]);
        cellTotalInterest.setCellFormula("SUM(C16:C18)" + enterKey);


        //Cell Total principal
        Cell cellTotalPrincipal = shtCalculation.getRow(19).getCell(3);
        MyConfig.mapSaveData.put("cellTotalPrincipal", String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellTotalPrincipal).getNumericCellValue() +""))).split("\\.")[0]);
        cellTotalPrincipal.setCellFormula("SUM(D16:D18)" + enterKey);

        //Cell Total Installment
        Cell cellTotalInstallment = shtCalculation.getRow(19).getCell(4);
        MyConfig.mapSaveData.put("cellTotalInstallment", String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellTotalInstallment).getNumericCellValue() +""))).split("\\.")[0]);
        cellTotalInstallment.setCellFormula("SUM(E16:E18)" + enterKey);


        //Cell Total Sisa pokok
        Cell cellTotalSisaPokok = shtCalculation.getRow(19).getCell(5);
        MyConfig.mapSaveData.put("cellTotalSisaPokok", (evaluator.evaluateInCell(cellTotalSisaPokok).getNumericCellValue() +"").split("\\.")[0]);
        cellTotalSisaPokok.setCellFormula("SUM(F16:F18)" + enterKey);

        //cellPovinsiMaterai+cellTotalInstallment
        String TempProvisiMateraiTotalInstallment = String.valueOf(Integer.parseInt(MyConfig.mapSaveData.get("cellProvinsiDanMaterai"))+Integer.parseInt(MyConfig.mapSaveData.get("cellTotalInstallment")));
        MyConfig.mapSaveData.put("cellProvisiMateraiTotalInstallment", TempProvisiMateraiTotalInstallment);

        //Cell Cicilan Perbulan Dengan Bunga
        Cell cellCicilanPerBulanDenganBunga = shtCalculation.getRow(8).getCell(1);
        MyConfig.mapSaveData.put("cellCicilanPerbulanDenganBunga", String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellCicilanPerBulanDenganBunga).getNumericCellValue() +""))).split("\\.")[0]);
        cellCicilanPerBulanDenganBunga.setCellFormula("ROUND(($B$2/$B$3)+$B$7,0)" + enterKey);

        //Cell Nominal yang di kredit
        Cell cellNominalYangDiKredit = shtCalculation.getRow(1).getCell(4);
        MyConfig.mapSaveData.put("cellNominalYangDiKredit", String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellNominalYangDiKredit).getNumericCellValue() +""))).split("\\.")[0]);
        cellNominalYangDiKredit.setCellFormula("$B$2-($E$4+$E$5)" + enterKey);

        //Cell Provisi
        Cell cellProvinsi = shtCalculation.getRow(3).getCell(4);
        MyConfig.mapSaveData.put("cellProvisi", String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellProvinsi).getNumericCellValue() +""))).split("\\.")[0]);
        cellProvinsi.setCellFormula("IF(F4*$B$2<=50000,50000,F4*$B$2)" + enterKey);

        //Cell Materai
        Cell cellMaterai = shtCalculation.getRow(4).getCell(4);
        MyConfig.mapSaveData.put("cellMaterai",String.valueOf(Math.round(Float.parseFloat(evaluator.evaluateInCell(cellMaterai).getNumericCellValue() +""))).split("\\.")[0]);
        cellMaterai.setCellFormula("10000" + enterKey);

        //Cell Bunga Perbulan
        MyConfig.mapSaveData.put("cellBungaPerBulan", "1.0%");


        try {
            FileOutputStream out = new FileOutputStream(strPathExcel);
            wb.write(out);
            out.close();
            wb.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendKeys_pgDown(){
        loadElement();
        thrWebElement.get().sendKeys(Keys.PAGE_DOWN);
    }

    public void goto_URL_ZoomOut() throws InterruptedException {
        JavascriptExecutor executor = (JavascriptExecutor)DriverFactory.init().get();
        DriverFactory.init().get().get("chrome://settings/");
        executor.executeScript("chrome.settingsPrivate.setDefaultZoom(0.67);");
        DriverFactory.init().get().get(thrValue.get());
        Thread.sleep(1000);
    }

    public void swipeUP(){

       WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(5));
       ScreenshotService.init().swipeUpWeb_SS(thrObjectName.get(),thrValue.get() );

    }

    public void clickAndGetClipboardTextToValue(){
        loadElement();
        click();

        // Retrieve the text from the clipboard and store it in a variable
        HasClipboard clipboard = (HasClipboard) DriverFactory.init().get();
        String content = clipboard.getClipboardText();

        MyConfig.mapSaveData.put(thrValue.get(),content);
    }
    public void openNotification() {
        Dimension dimension = DriverFactory.init().get().manage().window().getSize();
        Point upperLeftCorner = DriverFactory.init().get().manage().window().getPosition();
        int topYCoordinate = upperLeftCorner.getY();
        int bottomYCoordinate = upperLeftCorner.getY() + dimension.getHeight();
        scrollPage(300, topYCoordinate, 300, (bottomYCoordinate / 2));
    }

    public void saveToday() {
        String currentTime = new SimpleDateFormat("dd MMMM yyyy").format(Calendar.getInstance().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        Calendar cal = Calendar.getInstance();
        currentTime = sdf.format(cal.getTime());
        MyConfig.mapSaveData.put(thrObjectName.get(), currentTime);
    }

    public void back() throws InterruptedException {
        Thread.sleep(3000);
        DriverFactory.init().get().navigate().back();
    }

    public void deleteAllScreenshot() throws IOException {
        if (isRunningFirst) {
            File directory = new File("Screenshot");
            if (directory.exists()) {
                File[] files = directory.listFiles();
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            isRunningFirst = false;
        }
    }

    public void flushExcelGenerateDataRegistrasi() throws IOException {
        if (isRunningFirst) {
            File sourceFile = new File(MyConfig.strCurrentPath + "\\src\\main\\resources\\Backup Excel\\generateDataRegistrasi.xlsx");
            File targetFile = new File(MyConfig.strCurrentPath + "\\src\\main\\resources\\generateDataRegistrasi.xlsx");
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            isRunningFirst = false;
        }
    }
    public void doLoopClick() {
        Boolean notExist = true;
        do {
            loadElement();
            click();
            try {
                Thread.sleep(3000);
                if (DriverFactory.init().get().findElement(By.xpath(TestFactory.mapXpath.get(thrValue.get()).getStrXpath())).isDisplayed()) {
                    notExist = false;
                }
            } catch (Exception e) {
            }
        } while (notExist);
    }

    public void enterKey() {
        loadElement();
        thrWebElement.get().sendKeys(Keys.ENTER);
    }


}
