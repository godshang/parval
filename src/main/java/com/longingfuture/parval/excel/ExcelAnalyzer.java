package com.longingfuture.parval.excel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.longingfuture.parval.annotation.ExcelSheet;

public class ExcelAnalyzer {

    public static <T> List<T> read(Workbook workbook, Class<T> clazz) throws ExcelException {
        Workbook wb = workbook;
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

        Sheet sheet = getSheet(workbook, clazz);
        String[] title = getTitle(sheet);

        List<T> list = new ArrayList<T>();
        int sum = sheet.getLastRowNum();
        for (int i = 1; i <= sum; i++) {
            T t = newInstance(clazz);

            Row row = sheet.getRow(i);
            for (int j = 0; j < title.length; j++) {
                Cell cell = row.getCell(j);
                if (null == cell) {
                    continue;
                }
                // 获得单元格内容
                Object value = readCell(cell, evaluator);
                try {
                    // 根据列名设置实体对应的属性值
                    PropertyUtils.setProperty(t, title[j], value);
                } catch (IllegalAccessException e) {
                    throw new ExcelException("set property '" + value + "' for '" + clazz + "' error!");
                }
            }
            list.add(t);
        }
        return list;
    }

    private static <T> Sheet getSheet(Workbook workbook, Class<T> clazz) throws ExcelException {
        ExcelSheet excelSheet = clazz.getAnnotation(ExcelSheet.class);
        if (excelSheet != null) {
            int sheetNum = workbook.getNumberOfSheets();
            for (int i = 0; i < sheetNum; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if (sheet.getSheetName().equals(excelSheet.value())) {
                    return sheet;
                }
            }
        }
        throw new ExcelException("get sheet error!");
    }

    private static String[] getTitle(Sheet sheet) throws ExcelException {
        try {
            Row titleRow = sheet.getRow(sheet.getFirstRowNum());// 标题行
            int colNum = titleRow.getPhysicalNumberOfCells();
            String[] title = new String[colNum];
            for (int i = 0; i < colNum; i++) {
                title[i] = titleRow.getCell(i).getStringCellValue();
            }
            return title;
        } catch (Exception e) {
            throw new ExcelException("get title error!");
        }
    }

    private static <T> T newInstance(Class<T> clazz) throws ExcelException {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new ExcelException("new instance '" + clazz + "' error!");
        }
    }

    private static Object readCell(Cell cell, FormulaEvaluator evaluator) {
        int cellType = cell.getCellType();
        // 如果是公式的话需进行转换
        if (cellType == Cell.CELL_TYPE_FORMULA) {
            cellType = evaluator.evaluateFormulaCell(cell);
        }
        switch (cellType) {
        case Cell.CELL_TYPE_BOOLEAN:
            return cell.getBooleanCellValue();
        case Cell.CELL_TYPE_BLANK:
            return null;
        case Cell.CELL_TYPE_NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            } else {
                // 将数字全部转化成字符串处理
                NumberFormat fomat = NumberFormat.getInstance();
                // 不使用 科学计数法 和 分隔符
                // 如：9.999999999999E12和100,000,000.00等数据转
                // 化成9999999999999和100000000.00
                fomat.setGroupingUsed(false);
                return fomat.format(cell.getNumericCellValue());
            }
        case Cell.CELL_TYPE_STRING:
            return cell.getStringCellValue();
        default:
            return null;
        }
    }
}
