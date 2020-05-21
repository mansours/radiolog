package com.example.catalog.utilities;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.math.BigDecimal;

public class ExcelUtil {
    public static String getString(Row row, int cellNum, String defaultIfNull) {
        try {
            return row.getCell(cellNum).getStringCellValue();
        } catch (Exception e) {
            try {
                return row.getCell(cellNum).getRichStringCellValue().getString();
            } catch (Exception e2) {
                try {
                    return String.valueOf((int) Math.round(row.getCell(cellNum).getNumericCellValue()));
                } catch (Exception e3) {
                    return defaultIfNull;
                }
            }
        }
    }

    public static Double getDouble(Row row, int cellNum, Double defaultIfNull) {
        try {
            return (double) Math.round(row.getCell(cellNum).getNumericCellValue());
        } catch (Exception e) {
            try {
                return Double.valueOf(row.getCell(cellNum).getStringCellValue());
            } catch (Exception e2) {
                try {
                    return Double.valueOf(row.getCell(cellNum).getRichStringCellValue().getString());
                } catch (Exception e3) {
                    return defaultIfNull;
                }
            }
        }
    }

    public static Long getLong(Row row, int cellNum, Long defaultIfNull) {
        try {
            return Math.round(row.getCell(cellNum).getNumericCellValue());
        } catch (Exception e) {
            try {
                return Long.valueOf(row.getCell(cellNum).getStringCellValue());
            } catch (Exception e2) {
                try {
                    return Long.valueOf(row.getCell(cellNum).getRichStringCellValue().getString());
                } catch (Exception e3) {
                    return defaultIfNull;
                }
            }
        }
    }

    public static Boolean getBoolean(Row row, int cellNum, Boolean defaultIfNull) {
        try {
            return row.getCell(cellNum).getBooleanCellValue();
        } catch (Exception e) {
            try {
                return Boolean.valueOf(row.getCell(cellNum).getStringCellValue());
            } catch (Exception e2) {
                try {
                    return Boolean.valueOf(row.getCell(cellNum).getRichStringCellValue().getString());
                } catch (Exception e3) {
                    return defaultIfNull;
                }
            }
        }
    }

    public static Row getCreateRow(Sheet sheet, int row) {
        Row _row = sheet.getRow(row);
        if (_row == null)
            return sheet.createRow(row);
        else
            return _row;
    }

    public static double getNumeric(Object number, double defaultIfNull) {
        if (number == null)
            return defaultIfNull;
        else if (number instanceof BigDecimal)
            return ((BigDecimal) number).doubleValue();
        else if (number instanceof Integer)
            return ((Integer) number).doubleValue();
        else if (number instanceof Long)
            return ((Long) number).doubleValue();
        else if (number instanceof Double)
            return (Double) number;
        else if (number instanceof Float)
            return ((Float) number).doubleValue();
        else
            return defaultIfNull;
    }
}
