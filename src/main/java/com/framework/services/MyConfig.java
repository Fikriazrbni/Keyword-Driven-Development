package com.framework.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.framework.DAO.RepaymentSchedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyConfig {
    public static String strCurrentPath = System.getProperty("user.dir");

    public static SimpleDateFormat sdf = new SimpleDateFormat(PropertiesService.readProperties("configuration/excelConfig.properties").getProperty("SimpleFormatDate"));
    public static String strPathDatatable = strCurrentPath + "\\" + PropertiesService.readProperties("configuration/excelConfig.properties").getProperty("strPathDatatable");
    public static String strMainSheet = PropertiesService.readProperties("configuration/excelConfig.properties").getProperty("strMainSheet");
    public static String strDataInfo = PropertiesService.readProperties("configuration/excelConfig.properties").getProperty("strInfoSheet");
    public static Map<String,String> mapSaveData = new HashMap();
    public static Map<String,Integer> mapSaveDataInt = new HashMap();
    public static List<RepaymentSchedule> lstRepaymentSchedule = new ArrayList<>();
    public static String strPathReport = PropertiesService.readProperties("configuration/excelConfig.properties").getProperty("strPathReport");

    public static String strExecuteBy = PropertiesService.readProperties("configuration/excelConfig.properties").getProperty("strExecuteBy");

}
