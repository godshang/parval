package com.longingfuture.parval;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.longingfuture.parval.BeanParserBuilder;
import com.longingfuture.parval.annotation.ExcelSheet;
import com.longingfuture.parval.annotation.ExcelTitle;
import com.longingfuture.parval.annotation.Source;
import com.longingfuture.parval.annotation.SourceType;
import com.longingfuture.parval.core.BeanParser;

public class ExcelBeanParserTest extends TestCase {

    @Source(SourceType.EXCEL)
    @ExcelSheet("test-sheet")
    public static class Person {

        @ExcelTitle(names = "列1")
        String name;
        @ExcelTitle(names = "列2")
        int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

    }

    public void test() throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("test-sheet");

        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("列1");
        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("列2");

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue("alan");
        cell = row.createCell(1);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(27);

        OutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        InputStream in = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());

        BeanParser parser = BeanParserBuilder.build();
        List<Person> list = parser.parse(in, Person.class);
        for (Person item : list) {
            System.out.println(item.getName());
            System.out.println(item.getAge());
        }
    }
}
