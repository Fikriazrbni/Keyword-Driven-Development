package com.framework.factory;

import com.framework.DAO.XpathDAO;
import com.framework.services.MyConfig;
import com.framework.services.PropertiesService;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import java.util.*;

public class TestFactory {
    public static ThreadLocal<Class> thrDynamicClass = new ThreadLocal<>();
    private Map<String, Object> mapParam;
    Properties properties = PropertiesService.readProperties("configuration/excelConfig.properties");

    public static Map<String, XpathDAO> mapXpath;

    /**
     * Create DataProvider for Dynamic Testing which total data in Datatable
     *
     * @param context for get Test Group
     * @return DataProvider for methods DynamicTesting
     */
    @DataProvider(name = "init_runner", parallel = true)
    public Iterator<Object[]> testFactory(ITestContext context) {
        Sheet shtMain = ExcelFactory.init().getSheetExcel(MyConfig.strPathDatatable, MyConfig.strMainSheet);
        Sheet shtInfo = ExcelFactory.init().getSheetExcel(MyConfig.strPathDatatable, MyConfig.strDataInfo);
        String strTestGroupParam = context.getCurrentXmlTest().getParameter("testGroup");


        int intColumnStartData = 0;
        int intColumnEndData = 0;
        List<Object[]> lstObject = new ArrayList<>();
        mapXpath = ExcelFactory.init().getXpath();
        boolean counterDataInfo = true;

        int intColumnStart = ExcelFactory.init().getColumnByName(shtInfo, "Start Data");
        int intColumnEnd = ExcelFactory.init().getColumnByName(shtInfo, "End Data");

        try {
            intColumnStartData = Integer.parseInt(ExcelFactory.init().getCellValue(shtInfo.getWorkbook(), shtInfo.getRow(1).getCell(intColumnStart)));
            intColumnEndData = Integer.parseInt(ExcelFactory.init().getCellValue(shtInfo.getWorkbook(), shtInfo.getRow(1).getCell(intColumnEnd)));
        } catch (Exception e) {
            for (int i = 1; i <= shtInfo.getLastRowNum(); i++) {
                try {
                    int intColumnDataInfo = ExcelFactory.init().getColumnByName(shtInfo, "Choose Data");
                    int intColumnChooseData = Integer.parseInt(ExcelFactory.init().getCellValue(shtInfo.getWorkbook(), shtInfo.getRow(i).getCell(intColumnDataInfo)));
                    int intColumnTestGroup = ExcelFactory.init().getColumnByName(shtMain, properties.getProperty("strTestGroup"));
                    String strTestGroup = ExcelFactory.init().getCellValue(shtMain.getWorkbook(), shtMain.getRow(i).getCell(intColumnTestGroup));
                    if (strTestGroupParam.equalsIgnoreCase(strTestGroup)) {
                        int intColumnAction = ExcelFactory.init().getColumnByName(shtMain, properties.getProperty("strColumnMainSheet"));
                        int intColumnScriptID = ExcelFactory.init().getColumnByName(shtMain, properties.getProperty("strScriptID"));
                        int intColumnScenario = ExcelFactory.init().getColumnByName(shtMain, properties.getProperty("strScenario"));

                        String strScriptID = ExcelFactory.init().getCellValue(shtMain.getWorkbook(), shtMain.getRow(intColumnChooseData).getCell(intColumnScriptID));
                        String strAction = ExcelFactory.init().getCellValue(shtMain.getWorkbook(), shtMain.getRow(intColumnChooseData).getCell(intColumnAction));
                        String strScenario = ExcelFactory.init().getCellValue(shtMain.getWorkbook(), shtMain.getRow(intColumnChooseData).getCell(intColumnScenario));

                        mapParam = new HashMap<>();
                        mapParam.put("intNo", intColumnChooseData);
                        mapParam.put("strScriptID", strScriptID);
                        mapParam.put("strAction", strAction);
                        mapParam.put("strScenario", strScenario);
                        mapParam.put("strTestGroup", strTestGroup);

                        lstObject.add(new Object[]{mapParam});
                    }

                } catch (NullPointerException a) {
                    //TODO Show the error
                    System.out.println("Null pointer");
                    a.printStackTrace();
                } catch (Exception b) {
                    //TODO Show the error
                    System.out.println("Something wrong.");
                    b.printStackTrace();
                }
            }
            counterDataInfo = false;
        }

        if (counterDataInfo) {
            //TODO change counter from DATA_INFO
            for (int i = intColumnStartData; i <= intColumnEndData; i++) {
                try {
                    int intColumnTestGroup = ExcelFactory.init().getColumnByName(shtMain, properties.getProperty("strTestGroup"));
                    String strTestGroup = ExcelFactory.init().getCellValue(shtMain.getWorkbook(), shtMain.getRow(i).getCell(intColumnTestGroup));

                    if (strTestGroupParam.equalsIgnoreCase(strTestGroup)) {
                        int intColumnAction = ExcelFactory.init().getColumnByName(shtMain, properties.getProperty("strColumnMainSheet"));
                        int intColumnScriptID = ExcelFactory.init().getColumnByName(shtMain, properties.getProperty("strScriptID"));
                        int intColumnScenario = ExcelFactory.init().getColumnByName(shtMain, properties.getProperty("strScenario"));

                        String strScriptID = ExcelFactory.init().getCellValue(shtMain.getWorkbook(), shtMain.getRow(i).getCell(intColumnScriptID));
                        String strAction = ExcelFactory.init().getCellValue(shtMain.getWorkbook(), shtMain.getRow(i).getCell(intColumnAction));
                        String strScenario = ExcelFactory.init().getCellValue(shtMain.getWorkbook(), shtMain.getRow(i).getCell(intColumnScenario));

                        mapParam = new HashMap<>();
                        mapParam.put("intNo", i);
                        mapParam.put("strScriptID", strScriptID);
                        mapParam.put("strAction", strAction);
                        mapParam.put("strScenario", strScenario);
                        mapParam.put("strTestGroup", strTestGroup);

                        lstObject.add(new Object[]{mapParam});
                    }

                } catch (NullPointerException e) {
                    //TODO Show the error
                    System.out.println("Null pointer");
                    e.printStackTrace();
                } catch (Exception e) {
                    //TODO Show the error
                    System.out.println("Something wrong.");
                    e.printStackTrace();
                }
            }

        }
        return lstObject.stream().iterator();
    }

}
