package pl.stadler.counter.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelMenager {

    public Map<Integer, List<String>> readWorksheet(String fileLocation, String nameWorksheet) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileLocation);
        Workbook workbook = new XSSFWorkbook(fileInputStream);


        int amountSheets = workbook.getNumberOfSheets();

        // szukaj odpowiedniej zakładki
        int numberOfSheet = -1;
        for (int i = 0; i < amountSheets; i++) {
            if (workbook.getSheetName(i).equals(nameWorksheet)) {
                numberOfSheet = i;

            }
        }

        if (numberOfSheet > -1) {
            Sheet sheet = workbook.getSheetAt(numberOfSheet);
            // pobranie nazw arkuszów
            Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;
            for (Row row : sheet) {
                data.put(i, new ArrayList<String>());
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            data.get(i).add(cell.getRichStringCellValue().getString().replace(" ", ""));
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                data.get(i).add(String.valueOf(cell.getDateCellValue()));
                            } else {
                                // zrzutowanie na liczbę całkowitą
                                data.get(i).add(String.valueOf((int) cell.getNumericCellValue()));
                            }
                            break;
                        case FORMULA:
                            data.get(i).add(String.valueOf((int) cell.getNumericCellValue()));
                            break;
                        default:
                            data.get(i).add(" ");
                    }
                }
                i++;
            }

            fileInputStream.close();
            workbook.close();
            return data;
        }
        return null;
    }

}
