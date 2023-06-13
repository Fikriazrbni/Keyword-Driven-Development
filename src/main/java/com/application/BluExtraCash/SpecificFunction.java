package com.application.BluExtraCash;

import com.framework.DAO.XpathDAO;
import com.framework.DynamicTest;
import com.framework.baseFunctions.BaseFunction;
import com.framework.factory.DriverFactory;
import com.framework.factory.ExcelFactory;
import com.framework.factory.TestFactory;
import com.framework.services.MyConfig;
import com.framework.services.ScreenshotService;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.clipboard.HasClipboard;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.apache.commons.io.FileUtils.deleteDirectory;

public class SpecificFunction extends BaseFunction {

    public SpecificFunction(ThreadLocal<String> thrKeyword, ThreadLocal<String> thrObjectName, ThreadLocal<String> thrValue, ThreadLocal<WebElement> thrWebElement, ThreadLocal<String> thrDescription) {
        super(thrKeyword, thrObjectName, thrValue, thrWebElement, thrDescription);
    }


}

