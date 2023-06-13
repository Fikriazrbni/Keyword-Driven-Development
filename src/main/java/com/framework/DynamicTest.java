package com.framework;

import com.framework.baseFunctions.BaseController;
import com.framework.factory.ExcelFactory;
import com.framework.factory.TestFactory;
import com.framework.services.MyConfig;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DynamicTest implements ITest {
    public static ThreadLocal<Integer> trdIntNoData = new ThreadLocal<>();
    public static ThreadLocal<String> trdStrScriptID = new ThreadLocal<>();
    public static ThreadLocal<String> trdStrAction = new ThreadLocal<>();
    public static ThreadLocal<String> trdScenario = new ThreadLocal<>();
    public static ThreadLocal<String> trdTestGroup = new ThreadLocal<>();
    public static ThreadLocal<Integer> trdIntCounterRow = new ThreadLocal<>();
    public static ThreadLocal<Integer> trdIntTotalRow = new ThreadLocal<>();
    public static ThreadLocal<Integer> trdIntSSCounter = new ThreadLocal<>();
    private ThreadLocal<String> testName = new ThreadLocal<>();

    public DynamicTest() {
    }


    @BeforeMethod()
    public void setUp(Method method, ITestContext ctx, Object[] objects) {
        Map<String, Object> mapParam = (HashMap) objects[0];

        int intNo = (int) mapParam.get("intNo");
        String strScriptID = (String) mapParam.get("strScriptID");
        String strAction = (String) mapParam.get("strAction");
        String strScenario = (String) mapParam.get("strScenario");
        String strTestGroup = (String) mapParam.get("strTestGroup");
        String strTestName = intNo + "-" + strScriptID + "-" + strAction;

        //print sedang running data keberapa pada console
        System.out.println("\u001b[32m" + "You are in Datatable No " + mapParam.get("intNo") + "\u001b[0m");

        testName.set(strTestName);
        trdIntNoData.set(intNo);
        trdStrScriptID.set(strScriptID);
        trdStrAction.set(strAction);
        trdTestGroup.set(strTestGroup);
        trdScenario.set(strScenario);
        trdIntSSCounter.set(1);

        TestFactory.thrDynamicClass.set(this.getClass());

    }


    @Test(dataProvider = "init_runner",
            dataProviderClass = TestFactory.class)
    public void DynamicTesting(Map<String, Object> mapParam) {
        execute((String) mapParam.get("strAction"));
    }


    /**
     * Execute the engine Action Sheet.
     *
     * @param strAction which Action
     */
    public static void execute(String strAction) {
        Sheet shtAction = ExcelFactory.init().getSheetExcel(MyConfig.strPathDatatable, strAction);

        int intColumnIsRunning = ExcelFactory.init().getColumnByName(shtAction, "IsRunning");
        int intColumnKeyword = ExcelFactory.init().getColumnByName(shtAction, "Keyword");
        int intColumnObjectName = ExcelFactory.init().getColumnByName(shtAction, "ObjectName");
        int intColumnValue = ExcelFactory.init().getColumnByName(shtAction, "Value");
        int intColumnApplication = ExcelFactory.init().getColumnByName(shtAction, "Application");
        boolean isRunning = false;
        String strApplication = null;
        trdIntTotalRow.set(shtAction.getLastRowNum());

//        trdIntSSCounter.set(1);

        for (trdIntCounterRow.set(1); trdIntCounterRow.get() <= shtAction.getLastRowNum(); trdIntCounterRow.set(trdIntCounterRow.get() + 1)) {

//            try {
                String strIsRunning = ExcelFactory.init().getCellValue(shtAction.getWorkbook(), shtAction.getRow(trdIntCounterRow.get()).getCell(intColumnIsRunning));
                String strTempApplication = ExcelFactory.init().getCellValue(shtAction.getWorkbook(), shtAction.getRow(trdIntCounterRow.get()).getCell(intColumnApplication));
                strApplication = (strTempApplication != "" ? strTempApplication : strApplication);

                isRunning = checkIsRunning(strIsRunning, isRunning);

                if (isRunning) {
                    if (strIsRunning.isBlank()) {
                        //print lokasi row keberapa di sheet pada console
                        System.out.println("You are in row : " + (trdIntCounterRow.get()+1) + " Sheet : " + shtAction.getRow(trdIntCounterRow.get()).getSheet().getSheetName());

                        String strKeyword = ExcelFactory.init().getCellValue(shtAction.getWorkbook(), shtAction.getRow(trdIntCounterRow.get()).getCell(intColumnKeyword));
                        String strObjectName = ExcelFactory.init().getCellValue(shtAction.getWorkbook(), shtAction.getRow(trdIntCounterRow.get()).getCell(intColumnObjectName));
                        String strValue = ExcelFactory.init().getCellValue(shtAction.getWorkbook(), shtAction.getRow(trdIntCounterRow.get()).getCell(intColumnValue), trdIntNoData.get());

                        new BaseController(strKeyword, strObjectName, strValue, strApplication);
                    }
                }
//            }catch (NullPointerException ex){
//            }
        }
        System.out.println("this last");
    }

    /**
     * Check is running or no
     *
     * @param strIsRunning isRunning Yes or No
     * @param isRunning    parameter by Ref
     * @return
     */
    public static boolean checkIsRunning(String strIsRunning, boolean isRunning) {
        if (strIsRunning.equalsIgnoreCase("YES"))
            return true;
        else if (strIsRunning.equalsIgnoreCase("NO"))
            return false;
        return isRunning;
    }

    @Override
    public String getTestName() {
        return testName.get();
    }

}
