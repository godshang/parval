package com.longingfuture.parval.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.longingfuture.parval.common.ErrorConstant;
import com.longingfuture.parval.core.BeanParser;
import com.longingfuture.parval.excel.ExcelAnalyzer;
import com.longingfuture.parval.excel.ExcelException;
import com.longingfuture.parval.exception.BeanParseException;
import com.longingfuture.parval.util.ClassUtils;

public class ExcelBeanParser implements BeanParser {

    public ExcelBeanParser() {

    }

    @Override
    public <T> List<T> parse(InputStream in, Class<T> clazz) throws BeanParseException {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(in);
        } catch (Exception e) {
            if (ClassUtils.isAssignableValue(InvalidFormatException.class, e)
                    || ClassUtils.isAssignableValue(IOException.class, e)) {
                throw new BeanParseException(ErrorConstant.READ_DATA_ERROR, e);
            }
            throw new BeanParseException(ErrorConstant.UNKNOW_ERROR, e);
        }
        try {
            return ExcelAnalyzer.read(workbook, clazz);
        } catch (ExcelException e) {
            throw new BeanParseException(ErrorConstant.FORMAT_ERROR, e);
        }
    }

    @Override
    public <T> List<T> parse(File file, Class<T> clazz) throws BeanParseException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new BeanParseException(ErrorConstant.READ_DATA_ERROR, e);
        }
        return parse(in, clazz);
    }

}
