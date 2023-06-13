package com.application.LOS;

import com.framework.DAO.XpathDAO;
import com.framework.baseFunctions.BaseFunction;
import com.framework.factory.DriverFactory;
import com.framework.factory.TestFactory;
import com.framework.services.MyConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.time.Duration;
import java.util.List;

public class SpecificFunction extends BaseFunction {
    public SpecificFunction(ThreadLocal<String> thrKeyword, ThreadLocal<String> thrObjectName, ThreadLocal<String> thrValue, ThreadLocal<WebElement> thrWebElement, ThreadLocal<String> thrDescription) {
        super(thrKeyword, thrObjectName, thrValue, thrWebElement, thrDescription);
    }

    public void cekInqCustData() throws InterruptedException {
//        loadElement();
        WebElement webElementNoBluAcc = null;
        try {
            webElementNoBluAcc = DriverFactory.init().get().findElement(By.xpath(TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath()));
        } catch (Exception e) {

        }
        String strBluAcc = webElementNoBluAcc.getText();
        Thread.sleep(500);

        if (strBluAcc.equalsIgnoreCase(thrValue.get())) {
            WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), 50);
            WebElement webElementInqCustNama = DriverFactory.init().get().
                    findElement(By.xpath(TestFactory.mapXpath.get("LOSInqCust_Nama").getStrXpath()));
            webDriverWait.until(ExpectedConditions.elementToBeClickable(webElementInqCustNama));
            webElementInqCustNama.click();
        }
    }

    public void clickOKAlert() {
        DriverFactory.init().get().switchTo().alert().accept();
//        DriverFactory.init().get().switchTo().alert().dismiss();
    }

    public void ifNoRekeningTrueGoToSheet() {
        String strNoRek = MyConfig.mapSaveData.get(thrValue.get());
        if (!strNoRek.equalsIgnoreCase("111")) {
            thrValue.set("FULL_APPROVE");
            goToSheet();
        } else {
            thrValue.set("FULL_APPROVE_LALA");
            goToSheet();
        }
    }

    public void if_NotResubmit_ExitAutomation() {
        String Status_NasabahDecision = MyConfig.mapSaveData.get(thrObjectName.get());
        if (Status_NasabahDecision.equalsIgnoreCase(("Cancelled"))) {
           exitAutomation();
        }
    }

    public void IfStatusTidakSamaNull() {
        String Status_LOSNasabahDecision = MyConfig.mapSaveData.get(thrObjectName.get());
        if (Status_LOSNasabahDecision.equals("")){
            exitAutomation();
        }
    }


    public void IfStatusCancelled() {
        String Status_LOSAfterPengajuan_Resubmit = MyConfig.mapSaveData.get(thrObjectName.get());
        if ((Status_LOSAfterPengajuan_Resubmit.equalsIgnoreCase(("Cancelled"))) || (Status_LOSAfterPengajuan_Resubmit.equals(("")))){
            goToSheet();
        }
    }

    public void if_NotResubmit_Go_To_Value() {
        String Status_NasabahDecision = MyConfig.mapSaveData.get(thrObjectName.get());
        if (Status_NasabahDecision.equalsIgnoreCase(("Cancelled"))) {
            goToValue();
        }
    }

    public void ifPendingCustDecisionGoToValue(){
        String Status_LOSAfterPengajuan_Resubmit = MyConfig.mapSaveData.get(thrObjectName.get());
        if (Status_LOSAfterPengajuan_Resubmit.equalsIgnoreCase(("Pending Customer Decision"))){
            goToValue();
        }
    }

    public void scrollUntilExist(){
        JavascriptExecutor js;
        js = (JavascriptExecutor) DriverFactory.init().get();
        loadElement();
        String strID = thrWebElement.get().getAttribute("id");

        while(true){
            try {
                thrWebElement.get().click();
                break;
            }catch (Exception e){
                js.executeScript(strID+".scrollBy(0,100)");
            }

        }
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


    public void screenShotUntilObject() throws InterruptedException, AWTException {
        //isWebvalueExist
        WebDriverWait webDriverWait = new WebDriverWait(DriverFactory.init().get(), Duration.ofSeconds(10));
        String strXpath = TestFactory.mapXpath.get(thrValue.get()).getStrXpath();
        webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(strXpath)));

        Boolean notExist = true;
        WebElement webElementSK = null;
        screenShotChrome();
        do {
            Actions action = new Actions(DriverFactory.init().get());
            action.sendKeys(Keys.SPACE).build().perform();
            try {
                isWebElementExist();
                screenShotChrome();
                notExist = false;
            } catch (Exception e) {
            }
        } while (notExist);
    }

    public void cekRadioButton(){
        WebElement webElementNoBluAcc = DriverFactory.init().get().findElement(By.xpath(TestFactory.mapXpath.get(thrObjectName.get()).getStrXpath()));
        Boolean booleanChecked = webElementNoBluAcc.isSelected();
        if (booleanChecked == false){
            goToValue();
        }
    }

    public void getRiskRatingAndInterestLOS(){
        List<WebElement> lstTRWebElements = DriverFactory.init().get().findElements(By.xpath("//table[@id=\"dataTable\"]"));
        for (int i = 1; i < 11; i++) {
            String getcolRRCode = DriverFactory.init().get().findElement(By.xpath("//table[@id=\"dataTable\"]/tbody/tr["+i+"]/td[3]")).getText();
            String getcolinterest = DriverFactory.init().get().findElement(By.xpath("//table[@id=\"dataTable\"]/tbody/tr["+i+"]/td[6]")).getText().replace(" ","");
            String saveRRDataTable= MyConfig.mapSaveData.get("saveRR");
            if (saveRRDataTable.equals(getcolRRCode)){
                MyConfig.mapSaveData.put("saveRRLOS",getcolRRCode);
                MyConfig.mapSaveData.put("saveinterestLOS",getcolinterest);
            }
        }
     }
}
