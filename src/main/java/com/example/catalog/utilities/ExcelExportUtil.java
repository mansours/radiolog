package com.example.catalog.utilities;

import com.example.catalog.dto.ArtistDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import static com.example.catalog.utilities.ExcelUtil.getNumeric;
import static java.util.Objects.requireNonNullElse;
import static org.apache.poi.ss.usermodel.CellType.*;
import static org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType.BETWEEN;
import static org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType.GREATER_OR_EQUAL;

@Slf4j
public class ExcelExportUtil {

    public byte[] getArtists(List<ArtistDTO> artists) {

        byte[] array = new byte[0];

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("Artist Data"));

            int rowCount = 0;

            //Create header row
            Row headerRow = sheet.createRow(rowCount++);

            //TODO: Angela add headers here
            headerRow.createCell(0, STRING).setCellValue("Action");
            headerRow.createCell(1, STRING).setCellValue("Effective Year");
            headerRow.createCell(2, STRING).setCellValue("Header 1");
            headerRow.createCell(3, STRING).setCellValue("Header 2");

            //Create Styles
            Font font = workbook.createFont();
            font.setBold(true);

            CellStyle boldLockedStyle = workbook.createCellStyle();
            boldLockedStyle.setFont(font);

            //Set header row to bold and locked
            //TODO: Adjust the range here:
            IntStream.rangeClosed(0, 27).forEach(x -> headerRow.getCell(x).setCellStyle(boldLockedStyle));

            //TODO: Replace with artist model
            for (ArtistModel row : export) {
                Row dataRow = sheet.createRow(rowCount);
                dataRow.createCell(0, STRING).setCellValue("No Change");
                dataRow.createCell(1, NUMERIC).setCellValue(getNumeric(row.getYear(), 0));
                dataRow.createCell(2, STRING).setCellValue(row.getName());
                dataRow.createCell(9, BOOLEAN).setCellValue(requireNonNullElse(row.getBoolean(), false));
                ++rowCount;
            }


            //Add Validation Rules
            DataValidationHelper dvHelper = sheet.getDataValidationHelper();
            DataValidation dv;

            DataValidationConstraint actionDv = dvHelper.createExplicitListConstraint(new String[]{"No Change", "Overwrite Year"});
            DataValidationConstraint gtzDv = dvHelper.createIntegerConstraint(GREATER_OR_EQUAL, "0", "0");
            DataValidationConstraint gtzdDv = dvHelper.createDecimalConstraint(GREATER_OR_EQUAL, "0", "0");
            DataValidationConstraint tfDv = dvHelper.createExplicitListConstraint(new String[]{"TRUE", "FALSE"});

            dv = dvHelper.createValidation(actionDv, new CellRangeAddressList(1, rowCount, 0, 0));
            dv.createErrorBox("Invalid String", "Action must be an option from the drop down list provided.");
            dv.setShowErrorBox(true);
            dv.setEmptyCellAllowed(false);
            sheet.addValidationData(dv);

            dv = dvHelper.createValidation(dvHelper.createIntegerConstraint(BETWEEN, "2000", "2100"), new CellRangeAddressList(1, rowCount, 1, 1));
            dv.createErrorBox("Invalid Integer", "Year must be whole number between 2000 and 2100.");
            dv.setShowErrorBox(true);
            dv.setEmptyCellAllowed(false);
            sheet.addValidationData(dv);

            dv = dvHelper.createValidation(dvHelper.createTextLengthConstraint(BETWEEN, "1", "100"), new CellRangeAddressList(1, rowCount, 4, 4));
            dv.createErrorBox("Invalid String", "fasdfdasfs length must be between one and one hundred characters.");
            dv.setShowErrorBox(true);
            dv.setEmptyCellAllowed(false);
            sheet.addValidationData(dv);

            dv = dvHelper.createValidation(gtzDv, new CellRangeAddressList(1, rowCount, 7, 7));
            dv.createErrorBox("Invalid Integer", "fasdfdsf must be a number greater than zero.");
            dv.setShowErrorBox(true);
            dv.setEmptyCellAllowed(false);
            sheet.addValidationData(dv);

            dv = dvHelper.createValidation(gtzdDv, new CellRangeAddressList(1, rowCount, 8, 8));
            dv.createErrorBox("Invalid Decimal", "fsdfadfasdf must be a number greater than zero.");
            dv.setShowErrorBox(true);
            dv.setEmptyCellAllowed(false);
            sheet.addValidationData(dv);

            dv = dvHelper.createValidation(tfDv, new CellRangeAddressList(1, rowCount, 9, 9));
            dv.createErrorBox("Invalid Boolean", "afsdfs must be either TRUE or FALSE.");
            dv.setShowErrorBox(true);
            dv.setEmptyCellAllowed(false);
            sheet.addValidationData(dv);

            sheet.createFreezePane(0, 1);

            workbook.write(out);
            array = out.toByteArray();
        } catch (IOException e) {
            log.error("Could not create Excel sheet in export.", e);
        }

        return array;
    }
}
