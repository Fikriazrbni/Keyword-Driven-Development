package com.framework.baseFunctions;

import com.framework.DAO.XpathDAO;
import com.framework.baseFunctions.baseInterface.AssertInterface;
import com.framework.factory.TestFactory;
import com.framework.factory.DriverFactory;
import com.framework.services.MyConfig;
import com.framework.services.ScreenshotService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

/**
 * Assert Function class implement AssertInterface
 */
public class AssertFunction extends BaseController implements AssertInterface {



    public AssertFunction() {
    }

    public AssertFunction(ThreadLocal<String> thrKeyword, ThreadLocal<String> thrObjectName, ThreadLocal<String> thrValue, ThreadLocal<WebElement> thrWebElement, ThreadLocal<String> thrDescription) {
        super(thrKeyword, thrObjectName, thrValue, thrWebElement, thrDescription);
    }

    /**
     * used for verify date, generate and manipulate date
     */
    public void assertWebElementDate() {
        loadElement();
        String strValue = thrWebElement.get().getText().substring(0, 10);
        String strDescription = thrDescription.get();
        String format = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String strExpected = simpleDateFormat.format(new Date());

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strValue, strExpected, strDescription);

    }

    /**
     * Used for verify numeric only both from value and webElement text and delete if there "0" in lead.
     */
    public void assertWebElementNumOnly() {
        loadElement();
        String jangka = thrWebElement.get().getText();
        String strValue = StringUtils.getDigits(jangka);
        String strDescription = "Data from UI :" +strValue;
        Long num = Long.parseLong(thrValue.get());
        String strExpected = String.valueOf(num);

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strValue, strExpected, strDescription);

    }
    public void assertWebElementPlafonRea() {
        loadElement();
        Long plafonValue = Long.parseLong(StringUtils.getDigits(thrWebElement.get().getText().trim()))/100;
        String strValue = String.valueOf(plafonValue);
        String strDescription = "Data from UI :" +strValue;
        Long plafon = Long.parseLong(thrValue.get())/100;
        String strExpected = String.valueOf(plafon);

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strValue, strExpected, strDescription);

    }

    /**
     * Return digit only from string
     */
    public void assertWebElementDigitOnly() {
        loadElement();
        String strValue = StringUtils.getDigits(thrWebElement.get().getText().trim());
        String strDescription = "Data from UI :" +strValue;
        String strExpected = thrValue.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strValue, strExpected, strDescription);

    }
    /**
     * Assert Web Element Exist
     */
    @Override
    public void assertWebElementExist() {
        try {
            loadElement();
            if (thrWebElement.get().isDisplayed()) {
                //ScreenshotService.init().screenshot();
            }
        } catch (Exception ex) {
            throw new AssertionError("WebElement : " + thrObjectName.get() + " NOT FOUND");
        }
    }

    /**
     * Assert Web Element not Exist
     */
    @Override
    public void assertWebElementNotExist() {
        try {
            loadElement();
            WebElement webElement = DriverFactory.init().get().findElement(By.xpath(TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath()));
            String strDescription = TestFactory.mapXpath.get(thrObjectName.get()).getStrDescription();
            thrWebElement.set(webElement);
            thrDescription.set(strDescription);

            if (thrWebElement.get().isDisplayed()) {
                throw new AssertionError("WebElement : " + thrObjectName.get() + " FOUND");
            }
        } catch (Exception ex) {
            //ScreenshotService.init().screenshot();
        }

    }

    /**
     * Assert Web Element Text is exact same
     */
    @Override
    public void assertWebElementTextTrue() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = thrValue.get();
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);

    }

    public void assertWebElementTextTrueSaveData() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);

    }

    /**
     * Assert Web Element Text is exact not same
     */
    @Override
    public void assertWebElementTextFalse() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = thrValue.get();
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertNotEquals(strObject, strCompare, strDescription);
    }

    /**
     * Assert Web Element Text is contains text
     */
    @Override
    public void assertWebElementTextContainsTrue() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = thrValue.get();
        String strDescription = thrDescription.get();

        if (strObject.contains(strCompare)) {
            //ScreenshotService.init().screenshot();
        } else {
            throw new AssertionError("Object Name = " + thrObjectName.get() + "</br>Contains text : " + strCompare + " Not Contains " + strObject);
        }
    }

    /**
     * Assert Web Element Text is not contains text
     */
    @Override
    public void assertWebElementTextContainsFalse() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = thrValue.get();
        String strDescription = thrDescription.get();

        if (!strObject.contains(strCompare)) {
            //ScreenshotService.init().screenshot();
        } else {
            throw new AssertionError("Object Name = " + thrObjectName.get() + "</br>Not Contains text : " + strCompare + " Contains " + strObject);
        }
    }

//TODO try to catch by AssertionError
//        try{
//            Assert.assertEquals(strObject,strCompare,strDescription);
//            ExtentReportsFactory.init().get().log(Status.PASS,strDescription);
//        }catch (AssertionError  ex){
//            ExtentReportsFactory.init().get().log(Status.FAIL,strDescription + "\n" + ex.getMessage());
//            throw  ex;
//        }

//    @Override
//    public void assertObjectNull(Object strObject, String strDescription) {
//        Assert.assertNull(strObject, strDescription);
//    }
//
//    @Override
//    public void assertObjectNotNull(Object strObject, String strDescription) {
//        Assert.assertNotNull(strObject, strDescription);
//    }
//
//    @Override
//    public void assertSame(Object objObject, Object objObjectCompare, String strDescription) {
//        Assert.assertSame(objObject, objObjectCompare, strDescription);
//    }
//
//    @Override
//    public void assertNotSame(Object objObject, Object objObjectCompare, String strDescription) {
//        Assert.assertNotSame(objObject, objObjectCompare, strDescription);
//    }

    public void assertWebElementTextTruePendTerakhir() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        switch (strCompare) {
            case "Tanpa Gelar":
                strCompare = "Tanpa Gelar";
                break;
            case "Diploma 1":
                strCompare = "Diploma 1";
                break;
            case "Diploma 2":
                strCompare = "Diploma 2";
                break;
            case "Diploma 3":
                strCompare = "Diploma 3";
                break;
            case "Sarjana":
                strCompare = "S-1";
                break;
            case "Master":
                strCompare = "S-2";
                break;
            case "Doktor":
                strCompare = "S-3";
                break;
            case "Lainnya":
                strCompare = "Lainnya";
                break;
        }
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueNamaAlamatPerusahaan() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        if (strCompare.equals("")) {
            strCompare = "N/A";
        }
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueSaveTujuanPinjaman() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        if (strCompare.equals("Lain-lain")) {
            strCompare = "Lain - lain";
        }
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueHubKontakDarurat() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        switch (strCompare) {
            case "Suami/Istri":
                strCompare = "spouse";
                break;
            case "Orang Tua":
                strCompare = "parent";
                break;
            case "Anak":
                strCompare = "child";
                break;
            case "Saudara":
                strCompare = "sibling";
                break;
            case "Lainnya":
                strCompare = "others";
                break;
        }
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueNoHpKontakDarurat() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = MyConfig.mapSaveData.get(thrValue.get()).replace("08", "628");
        String strDescription = thrDescription.get();

        if (strObject.contains(strCompare)) {
            //ScreenshotService.init().screenshot();
        } else {
            throw new AssertionError("Object Name = " + thrObjectName.get() + "</br>Contains text : " + strCompare + " Not Contains " + strObject);
        }
    }

    public void assertElementTextTrueReplace(String target1, String replacement1, String target2, String replacement2, String target3, String replacement3) {
        loadElement();
        String strObject = thrWebElement.get().getText().replace(target1, replacement1).replace(target2, replacement2).replace(target3, replacement3);
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();
        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueTenor() {
        assertElementTextTrueReplace(" ", "", "Bulan", "", "", "");
    }

    public void assertElementTextTrueReplaceKeuangan() {
        assertElementTextTrueReplace("Rp ", "", ".", "", " ", "");
    }

    public void assertElementTextTrueReplaceNoHp() {
        assertElementTextTrueReplace(" ", "", " ", "", "", "");
    }

    public void assertElementTextTrueReplaceTenor() {
        assertElementTextTrueReplace(" Bulan", "", "", "", "", "");
    }

    public void assertElementTextTrueReplacePerkiraanCiciclan() {
        assertElementTextTrueReplace("Rp ", "", "/bulan", "", ".", "");
    }

    public void assertWebElementTextTrueLimit() {
        assertElementTextTrueReplace("Rp ", "", ".", "", ",00", "");
    }

    public void assertElementFromSaveDataReplacement() {
        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "").replace(" Bulan", "").replace("Rp ", "").replace("Rp ", "").replace("Rp0", "").replace(" ", "").split(",")[0];
        String strCompare = MyConfig.mapSaveData.get(thrValue.get()).replace(".", "").replace(" Bulan", "").replace("Rp ", "").replace("Rp ", "").replace(" ", "").split(",")[0];
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertElementFromSaveData() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueSplit() {
        String currentTime = new SimpleDateFormat("dd MMMM yyyy").format(Calendar.getInstance().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        currentTime = sdf.format(cal.getTime());

        loadElement();
        String[] strObjectSplit = thrWebElement.get().getText().split(" ");
        String strObjectSplitDeleted = Arrays.toString(ArrayUtils.removeAll(strObjectSplit, 8, 9, 10)).replace("[", "").replace("]", "").replace(",", "");
        String strObject = strObjectSplitDeleted;
        System.out.println(strObject);
        String strCompare = thrValue.get();
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);

    }

    public void assertCurrentBalance() {
        loadElement();
        String strObject = thrWebElement.get().getText().substring(17).replace(",", "");
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueReplaceKoma() {
        assertElementTextTrueReplace(",", "", "", "", "", "");
    }


    public void assertWebElementTextTrueReplaceSpace() {
        assertElementTextTrueReplace(" ", "", "", "", "", "");
    }

    public void assertFineractRepaymentsAccDetail() {
        loadElement();
        String repayments = thrWebElement.get().getText();
        repayments = String.valueOf(repayments.split(" ")[0]);
        Assert.assertEquals(repayments, MyConfig.mapSaveData.get(thrValue.get()).replace(" Bulan", ""));
    }

    public void assertForCancelledMaaf() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = thrValue.get().replace(";", "\n");
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertForSubTitleCancelled() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String DateCancelled = MyConfig.mapSaveData.get("save7FromTodayMMM");
        String strCompare = thrValue.get() + " " + DateCancelled;
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertSaveElementReplacementTitikDanKoma() {
        loadElement();
        String strObject = thrWebElement.get().getText().replace(",", "");
        String strCompare = MyConfig.mapSaveData.get(thrValue.get()).replace(".", "");
        String strDescription = thrDescription.get();

//        ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }


    public void assertWebElementTextTrueSaveDataReason() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String strTempCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();
        String status = MyConfig.mapSaveData.get("Status_NasabahDecision");
        String strCompare = null;
        if (status.equalsIgnoreCase("Cancelled")) {
            strCompare = strTempCompare;
        } else {
            strCompare = "";
        }
        //        ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueSubTitleCairkan() throws InterruptedException {
        /**  klik New Notif untuk tau nominal yg bisa dicairkan */

        WebElement webElementNewNotif = DriverFactory.init().get().
                findElement(By.xpath(TestFactory.mapXpath.get("Notifikasi_newNotif").getStrXpath()));
        Thread.sleep(300);
        webElementNewNotif.click();

        /**  save jumlah pinjamannya */
        WebElement webElementJumlahPinjaman = DriverFactory.init().get().
                findElement(By.xpath(TestFactory.mapXpath.get("PORTFOLIOCard_nilaiPinjaman").getStrXpath()));
        Thread.sleep(300);
        String getValueXpath = webElementJumlahPinjaman.getText();

        /**  klik home untuk balik ke new notif */
        WebElement webElementBtnHome = DriverFactory.init().get().
                findElement(By.xpath(TestFactory.mapXpath.get("Blu_btnHome").getStrXpath()));
        Thread.sleep(300);
        webElementBtnHome.click();

        /**  klik notif lonceng untuk balik ke new notif */
        WebElement webElementHOME_notifikasi = DriverFactory.init().get().
                findElement(By.xpath(TestFactory.mapXpath.get("HOME_notifikasi").getStrXpath()));
        Thread.sleep(300);
        webElementHOME_notifikasi.click();

        /**  Proses Assert */
        WebElement thrWebElement = DriverFactory.init().get().
                findElement(By.xpath(TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath()));
        String strObject = thrWebElement.getText();
        String strCompare = thrValue.get().replace(";", getValueXpath);
        String strDescription = thrDescription.get();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertForSubTitleCancelledConfirmAlasan() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String DateCancelled = MyConfig.mapSaveData.get("save7FromToday");
        String strCompare = thrValue.get() + " " + DateCancelled;
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }


    public void assertWebElementTextTrueIfEarlyOrManualZwsp() {
        if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("early")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "early");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        } else if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("manual")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "manual");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        }

        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = thrValue.get() + "\u200B";
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);

    }

    public void assertWebElementTextTrueSaveDtDecimalReplaceTititk() {
        if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("early")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "early");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        } else if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("manual")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "manual");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        }

        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "");
        String strCompare = MyConfig.mapSaveData.get(thrValue.get()) + ",00";
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueSaveDtRpDecimalTotalBayar() {
        String installmentDueTotal = MyConfig.mapSaveData.get(thrValue.get());
        String installmentDueHead = MyConfig.mapSaveData.get("installmentDueHead");
        int inAdvanceAfterRepayment = Integer.parseInt(installmentDueTotal) - Integer.parseInt(installmentDueHead);

        loadElement();
        String strObject = thrWebElement.get().getText().replace(".","");
        String strCompare = "Rp " + installmentDueTotal + ",00";
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertSaldoBluBerkurang(){
        int saldoBluBefore = Integer.parseInt(MyConfig.mapSaveData.get("saveSaldoBluAcc").replace(".", "").split(",")[0]);
        int TotalInstallmentTagihan = Integer.parseInt(MyConfig.mapSaveData.get("saveTotalInstallmentTagihan").replace(".", ""));
        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "");
        String strCompare = String.valueOf(saldoBluBefore - TotalInstallmentTagihan);
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertCheckFlag() {
        for (int i = 1; i <= 12; i++) {
            String strObject = MyConfig.mapSaveData.get("CeklisBeforeKe" + i);
            String strCompare = MyConfig.mapSaveData.get("StatusTenorKe" + i);
            String strDescription = thrDescription.get();
            if (strObject == null || strObject == "") {
                continue;
            }
            //ScreenshotService.init().screenshot();
            Assert.assertEquals(strObject, strCompare, strDescription);
        }
    }

    public void assertWebElementTextTrueSaveDtRpDecimal() {
        if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("early")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "early");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        } else if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("manual")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "manual");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        }

        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "");
        String strCompare = "Rp " + MyConfig.mapSaveData.get(thrValue.get()) + ",00";
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueSaveDtDecimal() {
        if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("early")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "early");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        } else if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("manual")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "manual");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        }

        loadElement();
        //String strObject = thrWebElement.get().getText().replace(".","");
        String strObject = thrWebElement.get().getText();
        String strCompare = MyConfig.mapSaveData.get(thrValue.get()) + ",00";
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertElementTextTrueReplaceTitikIfEarlyOrManual() {
        if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("early")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "early");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        } else if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("manual")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "manual");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        }

        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "");
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();
        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertElementTextTrueReplaceKomaIfEarlyOrManual() {
        if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("early")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "early");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        } else if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("manual")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "manual");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        }

        loadElement();
        String strObject = thrWebElement.get().getText().replace(",", "");
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();
        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueSaveDtRp() {
        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "");
        String strCompare = "Rp " + MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueTotalPinjamanPgDetail() {
        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "");
        String strCompare = "dari Rp " + MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTruePokokHutangdiBayar() {
        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "");
        Integer intPrincipalDueTotal = Integer.valueOf(MyConfig.mapSaveData.get("principalDueTotal"));
        Integer intTotalPrincipalDueBelumBayar = Integer.valueOf(MyConfig.mapSaveData.get("totalPrincipalDueBelumBayar"));
        Integer selisih = intPrincipalDueTotal - intTotalPrincipalDueBelumBayar;
        String strCompare = "Rp " + selisih;
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueSaveDtRp0Decimal() {
        if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("early")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "early");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        } else if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("manual")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "manual");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        }

        //xpath cacat
        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "");
        String strCompare = "Rp0" + MyConfig.mapSaveData.get(thrValue.get()) + "0" + ",00";
        String strDescription = thrDescription.get();

//ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }


    public void assertElementFromSaveDataIfEarlyOrManual() {
        if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("early")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "early");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        } else if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("manual")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "manual");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        }

        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertElementJatuhTempoIfEarlyOrManual() {
        String strCompare = null;
        if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("early")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "early");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
            strCompare = thrValue.get();
        } else if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("manual")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "manual");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
            for (int i = 1; i <= Integer.parseInt(MyConfig.mapSaveData.get("saveTenor")); i++) {
                if (MyConfig.mapSaveData.get("saveTglCicilan" + i) != null) {
                    strCompare = MyConfig.mapSaveData.get("saveTglCicilan" + i);
                }
            }
        }
    }

    public void assertWebElementNotifBluExtraCashSejumlah() {
        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "");
        String totalInstallmentDueBelumBayar = MyConfig.mapSaveData.get("totalInstallmentDueBelumBayar");
        String strCompare = thrValue.get().replace(".", "").replace("?", totalInstallmentDueBelumBayar);
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementNotifKamiSudahMenerima() {
        loadElement();
        int intTenor = 1;
        //cekFlag ada berapa di fineract??
        for (int i = 1; i <= 3; i++) {
            if (MyConfig.mapSaveData.get("PaidDateTenorKe" + i) != null) {
                intTenor += 1;
            }
        }

        String tenor = String.valueOf(intTenor);
        String strObject = thrWebElement.get().getText().replace(".", "");
        String totalInstallmentDueBelumBayar = MyConfig.mapSaveData.get("totalInstallmentDueBelumBayar");
        String strCompare = thrValue.get().replace(".", "").replace("*", tenor).replace("?", totalInstallmentDueBelumBayar);
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }


    public void assertNotifBluEarlyRepayment() {
//        String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("2", "3");
//        TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "");
        String totalInstallmentDueBelumBayar = MyConfig.mapSaveData.get("saveTotalInstallmentTagihan");
        String[] strEqual = thrValue.get().replace(".", "").replace("?", totalInstallmentDueBelumBayar).split(";");
        String strCompare = null;
        String strDescription = thrDescription.get();
        if (strObject.equals(strEqual[0])) {
            strCompare = strEqual[0];
        } else if (strObject.equals(strEqual[1])) {
            strCompare = strEqual[1];
        }
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertNotifBluSubtitleEarlyRepayment() {
        loadElement();
        int intTenor = 1;
        //cekFlag
        for (int i = 1; i <= 3; i++) {
            if (MyConfig.mapSaveData.get("PaidDateTenorKe" + i) != null) {
                intTenor += 1;
            }
        }

        String tenor = String.valueOf(intTenor);
        String strObject = thrWebElement.get().getText().replace(".", "");
        String totalInstallmentDueBelumBayar = MyConfig.mapSaveData.get("saveTotalInstallmentTagihan");
        String totalInstallmentDueTanpaFees = MyConfig.mapSaveData.get("installmentDueTotalTanpaFees");
        String[] strEqual = thrValue.get().replace(".", "").replace("*", tenor).replace("?", totalInstallmentDueBelumBayar).replace("+", totalInstallmentDueTanpaFees).split(";");
        String strCompare = null;
        String strDescription = thrDescription.get();
        if (strObject.equals(strEqual[0])) {
            strCompare = strEqual[0];
        } else if (strObject.equals(strEqual[1])) {
            strCompare = strEqual[1];
        }
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementPeriodePinjaman() throws ParseException {
        loadElement();
        String strObject = thrWebElement.get().getText().replace(".", "");
        String maturityDate = MyConfig.mapSaveData.get("saveMaturityDate");
        String disburstmentDate = MyConfig.mapSaveData.get("saveDisburstmentDate");
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yy", new Locale("id", "ID"));
        Date dateMaturity = inputFormat.parse(maturityDate);
        String formattedDateMaturity = outputFormat.format(dateMaturity);
        Date dateDisburse = inputFormat.parse(disburstmentDate);
        String formattedDateDisburse = outputFormat.format(dateDisburse);
        String dateFormat = formattedDateDisburse + " - " + formattedDateMaturity;
        String strCompare = thrValue.get() + dateFormat;
        String strDescription = thrDescription.get();

        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueForDateAndTime() {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String[] parts = strObject.split(":");
        strObject = parts[0] + ":" + parts[1];
        String strCompare = MyConfig.mapSaveData.get(thrValue.get());
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertCicilanTenor() {
        int k = 1;
        for (int i = 1; i <= 12; i++) {
            String maturityDate = MyConfig.mapSaveData.get("CicilanKe" + k);
            String test = "test";
            if (maturityDate != null) {
                String indexXpath = Integer.toString(i + 9);
                String index = Integer.toString(k);
                String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("?", indexXpath);
                TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
                try {
                    WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(5));
                    webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(strXpath)));
                } catch (Exception ex) {
                    String strXpathDefault = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace(indexXpath, "?");
                    TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpathDefault, ""));
                    continue;
                }
                loadElement();
                String strObject = thrWebElement.get().getText().replace(".", "");

                String disburstmentDate = MyConfig.mapSaveData.get("disburstmentDate");
                String totalInstallmentDueBelumBayar = MyConfig.mapSaveData.get("totalInstallmentDueBelumBayar");
                String strCompare = thrValue.get().replace("?", index).replace("*", MyConfig.mapSaveData.get("saveTenor")).replace("^", MyConfig.mapSaveData.get("date" + k));
                String strDescription = thrDescription.get();
                k++;

                Assert.assertEquals(strObject, strCompare, strDescription);
                String strXpathDefault = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace(indexXpath, "?");
                TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpathDefault, ""));
            }
        }
    }

    public void assertWebElementTextTrueStartDateFlex() throws ParseException {
        loadElement();
        String strObject = thrWebElement.get().getText();
        String currentTime = new SimpleDateFormat("dd MMMM yyyy").format(Calendar.getInstance().getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(cal.DATE, -1);
        String currentTimeMin1 = sdf.format(cal.getTime());

        String strCompare = "Start Date Value : " + currentTimeMin1;
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);

    }

    public void assertIfEarlyorManual() {
        if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("early")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "early");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        } else if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("manual")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "manual");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        }

        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = thrValue.get();
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }

    public void assertWebElementTextTrueIfEarlyOrManual() {
        if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("early")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "early");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        } else if (MyConfig.mapSaveData.get("saveTipeRepayment").equals("manual")) {
            String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("#", "manual");
            TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
        }

        loadElement();
        String strObject = thrWebElement.get().getText();
        String strCompare = thrValue.get();
        String strDescription = thrDescription.get();

        //ScreenshotService.init().screenshot();
        Assert.assertEquals(strObject, strCompare, strDescription);
    }


    public void assertCicilanNominal() {
        int k = 1;
        List<Integer> listCompare = new ArrayList<>();
        for (int i = 1; i <= 12; i++){
            String maturityDate = MyConfig.mapSaveData.get("installmentDue" + k);
            String test = "test";
            if (maturityDate != null ){
                String indexXpath = Integer.toString(i+9);
                String index = Integer.toString(k);
                String strXpath = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace("?", indexXpath);
                TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpath, ""));
                try {
                    WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(5));
                    webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(strXpath)));
                } catch (Exception ex) {
                    String strXpathDefault = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace(indexXpath, "?");
                    TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpathDefault, ""));
                    continue;
                }
                loadElement();
                String strObject = thrWebElement.get().getText().replace(".", "").replace(" Bulan", "").replace("Rp ", "").replace("Rp ", "").replace("Rp0", "").replace(" ", "").split(",")[0];
                String strCompare = MyConfig.mapSaveData.get(thrValue.get().replace("?", index));
                listCompare.add(Integer.valueOf(strCompare));
                String strDescription = thrDescription.get();
                k++;

                Assert.assertEquals(strObject, strCompare, strDescription);
                String strXpathDefault = TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath().replace(indexXpath, "?");
                TestFactory.mapXpath.put(thrObjectName.get(), new XpathDAO(thrObjectName.get(), strXpathDefault, ""));
            }
        }
        int total = listCompare.stream().mapToInt(Integer::intValue).sum();
        MyConfig.mapSaveData.put("totalNominalCicilan", String.valueOf(total));
    }
}
