package com.framework.factory;

import com.framework.DAO.XpathDAO;
import com.framework.services.MyConfig;
import com.google.common.io.Files;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class ExcelFactory {
    private static volatile ExcelFactory instance = null;
    private static ThreadLocal<Workbook> thrWorkbook = new ThreadLocal<>();

    private ExcelFactory() {

    }

    public static ExcelFactory init() {
        if (instance == null) {
            synchronized (ExcelFactory.class) {
                if (instance == null)
                    instance = new ExcelFactory();
            }
        }
        return instance;
    }

    /**
     * Get Workbook
     *
     * @param strPathExcel path Datatable location
     */
    public Workbook getWorkBook(String strPathExcel) {
        Workbook workbook;
        try {
            File file = new File(strPathExcel);
            FileInputStream inputStream = null;
            inputStream = new FileInputStream(file);
            String fileExtension = Files.getFileExtension(strPathExcel);

            if (fileExtension.equalsIgnoreCase("xlsx") || fileExtension.equalsIgnoreCase("xlsm")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (fileExtension.equalsIgnoreCase("xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                workbook = null;
            }
            return workbook;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found.");
        } catch (IOException e) {
            throw new RuntimeException("Path not found.");
        }

    }

    /**
     * Get specific sheet from workbook
     *
     * @param strPathExcel path Datatable location
     * @param strSheetName which sheet
     * @return
     */
    public Sheet getSheetExcel(String strPathExcel, String strSheetName) {
        try {
//            System.out.println("strPathExcel : "+strPathExcel);
            return getWorkBook(strPathExcel).getSheet(strSheetName);
        } catch (Exception e) {
            throw new RuntimeException("Sheet not found");
        }


    }

    /**
     * Search column in sheet and return the index column
     *
     * @param sheet     which sheet
     * @param strSearch search column
     * @return index column
     */
    public int getColumnByName(Sheet sheet, String strSearch) {
        for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
            String strValue = getCellValue(sheet.getWorkbook(), sheet.getRow(0).getCell(i));
            if (strValue.equalsIgnoreCase(strSearch))
                return sheet.getRow(0).getCell(i).getColumnIndex();
        }
        throw new NullPointerException("Column " + strSearch + " not Found on Sheet " + sheet.getSheetName());
    }

    /**
     * Get cell value
     *
     * @param workbook which workbook
     * @param cell     cell will be checked by type
     * @param intRow   which row. intRow is optional
     * @return value
     */
    public String getCellValue(Workbook workbook, Cell cell, int... intRow) {
        String strValue = "";
        try {
            switch (cell.getCellType()) {
                case STRING:
                    strValue = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    strValue = (cell.getNumericCellValue() + "").replace(".0", "");
                    break;
                case BOOLEAN:
                    strValue = cell.getBooleanCellValue() + "";
                    break;
                case FORMULA:
                    //TODO Check again, we don't effect by
                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    strValue = processFormula(evaluator, cell, intRow[0]);
                    break;
                default:
                    strValue = "";
                    break;
            }
        } catch (NullPointerException e) {
            strValue = "";
        }

        return strValue;

    }


    /**
     * process cell if in the cell is formula
     *
     * @param evaluator evaluator for process the formula
     * @param cell      which cell
     * @param intRow    which row
     * @return String value
     */
    public String processFormula(FormulaEvaluator evaluator, Cell cell, int intRow) {
        String strValue = "";
        String strCellFormula = cell.getCellFormula();
        String strSheetDatatable = ExtractFormulaSheet(strCellFormula);
        int intIndexDatatable = ExtractFormulaAddress(strCellFormula);
        String strColumn = CellReference.convertNumToColString(intIndexDatatable);
        String strFormula = strSheetDatatable + "!" + strColumn + (intRow + 1);
        cell.setCellFormula(strFormula);

        switch (evaluator.evaluateInCell(cell).getCellType()) {
            case STRING:
                strValue = evaluator.evaluateInCell(cell).getStringCellValue();
                break;
            case NUMERIC:
                if (evaluator.evaluateInCell(cell).getNumericCellValue() == 0.0)
                    strValue = (evaluator.evaluateInCell(cell).getNumericCellValue() + "").replace("0.0", "");
                else
                    strValue = (evaluator.evaluateInCell(cell).getNumericCellValue() + "").replace(".0", "");
                break;
            case BOOLEAN:
                strValue = evaluator.evaluateInCell(cell).getBooleanCellValue() + "";
                break;
            default:
                break;
        }
        cell.setCellFormula(strFormula);

        return strValue;
    }

    /**
     * Extract the formula for get Sheet
     *
     * @param strFormula formula will split
     * @return sheet from contains the formula
     */
    public static String ExtractFormulaSheet(String strFormula) {
        String strSheet[] = strFormula.split("!");

        return strSheet[0];
    }

    /**
     * Extract the formula for get Address
     *
     * @param strFormula formula will split
     * @return Address from the formula
     */
    public static int ExtractFormulaAddress(String strFormula) {

        String strSheet[] = strFormula.split("!");
        CellReference cellReference = new CellReference(strSheet[1]);

        return cellReference.getCol();
    }

    /**
     * Get xpath from excel sheet XPATH
     *
     * @return Map(StNameObject, StrXpath)
     */
    public Map getXpath() {
        Map<String, XpathDAO> mapXpath = new HashedMap<>();
        Sheet shtXpath = ExcelFactory.init().getSheetExcel(MyConfig.strPathDatatable, "XPATH");
        for (int i = 1; i <= shtXpath.getLastRowNum(); i++) {
            try {
                int intColumnObject = ExcelFactory.init().getColumnByName(shtXpath, "OBJECT");
                int intColumnXpath = ExcelFactory.init().getColumnByName(shtXpath, "XPATH");
                int intColumnDescription = ExcelFactory.init().getColumnByName(shtXpath, "DESCRIPTION");
                String strObject = ExcelFactory.init().getCellValue(shtXpath.getWorkbook(), shtXpath.getRow(i).getCell(intColumnObject));
                String strXpath = ExcelFactory.init().getCellValue(shtXpath.getWorkbook(), shtXpath.getRow(i).getCell(intColumnXpath));
                String strDescription = ExcelFactory.init().getCellValue(shtXpath.getWorkbook(), shtXpath.getRow(i).getCell(intColumnDescription));
                mapXpath.put(strObject, new XpathDAO(strObject, strXpath, strDescription));
            } catch (NullPointerException e) {
                //TODO Show the error
                System.out.println("Null pointer");
                e.printStackTrace();
                break;
            }
        }
        return mapXpath;
    }
}
