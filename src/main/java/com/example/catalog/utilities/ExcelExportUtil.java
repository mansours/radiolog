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

import static com.example.catalog.utilities.CalendarUtil.getPickerFormat;
import static com.example.catalog.utilities.ExcelUtil.getNumeric;
import static java.util.Objects.requireNonNullElse;
import static org.apache.poi.ss.usermodel.CellType.*;
import static org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType.BETWEEN;
import static org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType.GREATER_OR_EQUAL;

@Slf4j
public class ExcelExportUtil {

    public static byte[] getArtists(List<ArtistDTO> artists) {
    //
        byte[] array = new byte[0];

        try (ByteArrayOutputStream out = new ByteArrayOutputStream(); Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(WorkbookUtil.createSafeSheetName("Artist Data"));

            int rowCount = 0;

            //Create header row
            Row headerRow = sheet.createRow(rowCount++);

            //TODO: Angela add headers here
            headerRow.createCell(0, STRING).setCellValue("ID");
            headerRow.createCell(1, STRING).setCellValue("Name");
            headerRow.createCell(2, STRING).setCellValue("Label");
            headerRow.createCell(3, STRING).setCellValue("Gender");
            headerRow.createCell(4, STRING).setCellValue("Date of Birth");


            //Create Styles
            Font font = workbook.createFont();
            font.setBold(true);

            CellStyle boldLockedStyle = workbook.createCellStyle();
            boldLockedStyle.setFont(font);

            //Set header row to bold and locked
            //TODO: Adjust the range here:
            IntStream.rangeClosed(0, 4).forEach(x -> headerRow.getCell(x).setCellStyle(boldLockedStyle));

            //TODO: Replace with artist model
            for (ArtistDTO artist : artists) {
                Row dataRow = sheet.createRow(rowCount);
                dataRow.createCell(0, NUMERIC).setCellValue(getNumeric(artist.getId(), 0));
                dataRow.createCell(1, STRING).setCellValue(artist.getName());
                dataRow.createCell(2, STRING).setCellValue(artist.getLabel());
                dataRow.createCell(3, STRING).setCellValue(artist.getGender());
                dataRow.createCell(4, STRING).setCellValue(getPickerFormat(artist.getDateOfBirth(), false));
                ++rowCount;
            }


            //Add Validation Rules
            DataValidationHelper dvHelper = sheet.getDataValidationHelper();
            DataValidation dv;

            DataValidationConstraint mfDv = dvHelper.createExplicitListConstraint(new String[]{"Female", "Male"});
            DataValidationConstraint gtzDv = dvHelper.createIntegerConstraint(GREATER_OR_EQUAL, "0", "0");
            DataValidationConstraint gtzdDv = dvHelper.createDecimalConstraint(GREATER_OR_EQUAL, "0", "0");
            DataValidationConstraint tfDv = dvHelper.createExplicitListConstraint(new String[]{"TRUE", "FALSE"});

            dv = dvHelper.createValidation(gtzDv, new CellRangeAddressList(1, rowCount, 0, 0));
            dv.createErrorBox("Invalid Integer", "Artist ID must be greater than 0.");
            dv.setShowErrorBox(true);
            dv.setEmptyCellAllowed(false);
            sheet.addValidationData(dv);

            dv = dvHelper.createValidation(dvHelper.createTextLengthConstraint(BETWEEN, "1", "128"), new CellRangeAddressList(1, rowCount, 1, 1));
            dv.createErrorBox("Invalid String", "Artist name length must be between 1 and 128 characters.");
            dv.setShowErrorBox(true);
            dv.setEmptyCellAllowed(false);
            sheet.addValidationData(dv);

            dv = dvHelper.createValidation(dvHelper.createTextLengthConstraint(BETWEEN, "1", "128"), new CellRangeAddressList(1, rowCount, 2, 2));
            dv.createErrorBox("Invalid String", "Artist label length must be between 1 and 128 characters.");
            dv.setShowErrorBox(true);
            dv.setEmptyCellAllowed(false);
            sheet.addValidationData(dv);


            dv = dvHelper.createValidation(mfDv, new CellRangeAddressList(1, rowCount, 3, 3));
            dv.createErrorBox("Invalid Option", "Gender must be either Female or Male.");
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
