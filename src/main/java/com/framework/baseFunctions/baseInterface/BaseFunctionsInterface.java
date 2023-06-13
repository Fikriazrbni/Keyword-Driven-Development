package com.framework.baseFunctions.baseInterface;

/**
 * Interface for BaseFunctionClass
 */
public interface BaseFunctionsInterface {


    void startChromeDriver();


    void saveData();

    void goToValue();

    void anchorGoToValue();

    void goToSheet();

    void startMobileDriver();

    void switchDriver();

    void click();

    void doubleClick();
//    void screenShotFullPage();

//    void screenshotFullViewMobile();

    void screenshotFullPart();

    void screenShotMobile();

    void screenShotChrome();

    void screenshotByObject();

    void waitUntilWebElementExist();

    void goto_url();

    void setText();

    void changeIframe();

    void changeIframeDefault();

    void waitForSeconds() throws InterruptedException;


    void forDataBySheet();

    void endDataForSheet();


    void scrollUntilElement() throws InterruptedException;
}
