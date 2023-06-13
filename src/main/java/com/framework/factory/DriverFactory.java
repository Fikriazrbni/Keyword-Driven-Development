package com.framework.factory;

import com.framework.services.MyConfig;
import com.framework.services.PropertiesService;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public class DriverFactory {
    private static volatile DriverFactory instance = null;
    private Properties propertiesWeb = PropertiesService.readProperties("configuration/webDriverConfig.properties");
    private Properties propertiesMobile = PropertiesService.readProperties("configuration/mobileDriverConfig.properties");
    ;

    private DriverFactory() {

    }

    public static DriverFactory init() {
        if (instance == null) {
            synchronized (DriverFactory.class) {
                if (instance == null)
                    instance = new DriverFactory();
            }
        }
        return instance;
    }

    ThreadLocal<WebDriver> trdMainDriver = new ThreadLocal<>();
    ThreadLocal<WebDriver> trdWebDriver = new ThreadLocal<>();
    ThreadLocal<WebDriver> trdMobileDriver = new ThreadLocal<>();

    public WebDriver get() {
        return trdMainDriver.get();
    }

    public void set(WebDriver webDriver) {
        trdMainDriver.set(webDriver);
    }

    public void remove() {
        trdMainDriver.remove();
    }

    /**
     * @return Return WebDriver by config webDriverProperties
     */
    public void initWebDriver() {
        String strBrowser = propertiesWeb.getProperty("Browser");
        String strSource = propertiesWeb.getProperty("Source");
        WebDriver webDriver = null;
        useLocalorDefault(strSource, strBrowser);

        switch (strBrowser) {
            case "chrome":
                ChromeOptions chromeOptions = null;
                Boolean remoteChrome = Boolean.parseBoolean(propertiesWeb.getProperty("Remote"));
                if (remoteChrome){
                    try {
                        chromeOptions = new ChromeOptions();
                        webDriver = new RemoteWebDriver(URI.create("http://10.121.21.25:4444/").toURL(), chromeOptions);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }else {
                    chromeOptions = remoteChrome(false);
                }

                chromeOptions.addArguments("enable-automation"); // https://stackoverflow.com/a/43840128/1689770
                chromeOptions.addArguments("--no-sandbox"); //https://stackoverflow.com/a/50725918/1689770
                chromeOptions.addArguments("--remote-allow-origins=*"); https://stackoverflow.com/questions/72540689/how-to-help-this-org-openqa-selenium-remote-http-websocketlistener-onerror

                if (propertiesWeb.getProperty("Maximize").equalsIgnoreCase("true"))
                    chromeOptions.addArguments("--start-maximized");

                if (propertiesWeb.getProperty("Headless").equalsIgnoreCase("true")) {
                    chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--disable-dev-shm-usage"); //https://stackoverflow.com/a/50725918/1689770
                    chromeOptions.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
                }

                if (propertiesWeb.getProperty("Incognito").equalsIgnoreCase("true"))
                    chromeOptions.addArguments("--incognito");

                if (propertiesWeb.getProperty("Proxy").equalsIgnoreCase("true")) {
                    String strProxy = propertiesWeb.getProperty("ProxyHost");
                    chromeOptions.addArguments("--proxy-server= " + strProxy);
                }

                int intCounter = 0;
                while (webDriver == null && intCounter < 5) {
                    try {
                        webDriver = new ChromeDriver(chromeOptions);
                    } catch (Exception e) {
                    }
                    intCounter++;
                }

                break;
            case "firefox":
                webDriver = new FirefoxDriver();
                break;

            case "opera":
                //noinspection deprecation
                webDriver = new OperaDriver();
                break;
            case "ie":
                webDriver = new InternetExplorerDriver();
                break;

            case "edge":
                webDriver = new EdgeDriver();
                EdgeOptions edgeOptions = null;
                break;
        }

        Long ObjectWaitTime = Long.parseLong(propertiesWeb.getProperty("WaitTime"));
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ObjectWaitTime));
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ObjectWaitTime * 2));
        webDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(ObjectWaitTime * 2));

        trdWebDriver.set(webDriver);
        set(webDriver);
    }

    /**
     * @return Return mobileDriver by config mobileDriverProperties
     */
    public void initMobileDriver() {
        String resetApp = MyConfig.mapSaveData.get("saveResetApp");
        String strDevice = propertiesMobile.getProperty("Device");
        String strPlatformName = propertiesMobile.getProperty("platformName");
        String strApp = propertiesMobile.getProperty("app");
        String strAppPakage = propertiesMobile.getProperty("appPackage");
        String strAppActivity = propertiesMobile.getProperty("appActivity");
        String strURL = propertiesMobile.getProperty("URL");

        Long newCommandTimeout = Long.parseLong(propertiesMobile.getProperty("newCommandTimeout"));
        Boolean boolNoReset = Boolean.valueOf(propertiesMobile.getProperty("noReset"));

        WebDriver mobileDriver = null;

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", strPlatformName);
        capabilities.setCapability("noReset", boolNoReset);
        capabilities.setCapability("newCommandTimeout", newCommandTimeout);

        /**
         * setCapability using app sourcepath or appPackage-appActivity
         */
        if (strApp.equalsIgnoreCase("null")) {
            if (resetApp.equalsIgnoreCase("true")){
                capabilities.setCapability("appPackage", strAppPakage);
                capabilities.setCapability("appActivity", strAppActivity);
            }
        } else {
            capabilities.setCapability("app", strApp);
        }
        switch (strDevice) {
            case "android":
                try {
                    mobileDriver = new AndroidDriver(new URL(strURL), capabilities);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "iOS":
                capabilities.setCapability("deviceName", propertiesMobile.getProperty("deviceName"));
                capabilities.setCapability("automationName", propertiesMobile.getProperty("automationName"));
                capabilities.setCapability("platformVersion", propertiesMobile.getProperty("platformVersion"));
                capabilities.setCapability("udid", propertiesMobile.getProperty("udid"));
                capabilities.setCapability("xcode0grId", propertiesMobile.getProperty("xcode0grId"));
                capabilities.setCapability("xcodeSigningID", propertiesMobile.getProperty("xcodeSigningID"));
                capabilities.setCapability("autoAcceptAlerts", Boolean.valueOf(propertiesMobile.getProperty("autoAcceptAlerts")));
                capabilities.setCapability("wdaLocalPort", propertiesMobile.getProperty("wdaLocalPort"));
                capabilities.setCapability("forceAppLaunch", propertiesMobile.getProperty("forceAppLaunch"));
                try {
                    mobileDriver = new IOSDriver(new URL(strURL), capabilities);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                break;
        }

        Long ObjectWaitTime = Long.parseLong(propertiesMobile.getProperty("WaitTimeMobile"));
        mobileDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ObjectWaitTime));

        trdMobileDriver.set(mobileDriver);
        set(mobileDriver);
    }

    /**
     * Already start another driver and keeping previous driver
     */
    public void switchDriver() {
        if ((trdMainDriver.get() != trdMobileDriver.get()) && trdWebDriver != null) {
            trdMainDriver = trdMobileDriver;
        } else if ((trdMainDriver.get() != trdWebDriver.get()) && trdMobileDriver != null) {
            trdMainDriver = trdWebDriver;
        }
    }

    /**
     * Close Browser
     */
    public void tearDown() {
        try {
            trdMainDriver.get().quit();
            remove();
        } catch (Exception e) {

        }
    }

    /**
     * @param strSource  Source use local driver or webdrivermanager, because bca use proxy, sometimes we can't connect chromedriver
     * @param strBrowser What browser for the setup
     */
    private void useLocalorDefault(String strSource, String strBrowser) {
        if (strSource.equalsIgnoreCase("driverLocal")) {
            switch (strBrowser) {
                case "chrome":
                    checkVersionChrome();
                    String strdriverLocal = propertiesWeb.getProperty("DriverLocal");
                    System.setProperty("webdriver.chrome.driver", MyConfig.strCurrentPath+"\\src\\main\\resources\\driverLocal\\" + strdriverLocal);
                    break;
                case "firefox":
                    break;
                case "opera":
                    break;
                case "ie":
                    break;
            }
        } else {
            switch (strBrowser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    break;
                case "opera":
                    WebDriverManager.operadriver().setup();
                    break;
                case "ie":
                    WebDriverManager.iedriver().setup();
                    break;
            }
        }
    }

    private void checkVersionChrome() {
        String chromeRegistry = null;
        String strChromeDriver = "";

        //Find Chrome version in Registry Editor
        Runtime rt = Runtime.getRuntime();
        Process proc = null;
        try {
            proc = rt.exec("reg query " + "HKEY_CURRENT_USER\\Software\\Google\\Chrome\\BLBeacon " + "/v version");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader regData = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));
        try {
            while ((chromeRegistry = regData.readLine()) != null) {
                if (chromeRegistry.contains("version")) {
                    //Split values from Chrome registry to get Chrome Vers. (E.g:"   version    REG_SZ    103.0.5060.114")
                    String[] strChromeVersion = chromeRegistry.split("\\s+");
                    //Split Chrome version by the dot
                    String[] splitVersion = strChromeVersion[3].split("\\.");
                    //check if the second value equal to 0 (xxx.0.xxxx.xxx)
                    if (splitVersion[1].equalsIgnoreCase("0")) {
                        strChromeDriver = splitVersion[0];
                    }
//                    else {
//                        strChromeDriver = splitVersion[0]+"."+splitVersion[1];
//                    }
                }
            }
            propertiesWeb.setProperty("DriverLocal", "chromedriver" + strChromeDriver + ".exe");
        } catch (Exception e) {
            try {
                throw new Exception("Chrome version not found in Registry Editor");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Running bat chrome debugger
     * @param isRemote true or false
     */
    public ChromeOptions remoteChrome(boolean isRemote) {
        ChromeOptions chromeOptions = new ChromeOptions();
        try {
            if (isRemote){
                Runtime.getRuntime().exec("src/main/resources/driverLocal/debugging.bat");
                chromeOptions.setExperimentalOption("debuggerAddress","localhost:5555");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return chromeOptions;

    }
}
