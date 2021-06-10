package pl.stadler.counter.excel;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ExcelMenager {

    public Map<Integer, List<String>> readWorksheet(String fileLocation, String sheetName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileLocation);
        Workbook workbook = new XSSFWorkbook(fileInputStream);


        int amountSheets = workbook.getNumberOfSheets();

        // szukaj odpowiedniej zakładki
        int numberOfSheet = -1;
        for (int j = 0; j < amountSheets; j++) {
            if (workbook.getSheetName(j).toUpperCase().contains(sheetName.toUpperCase())) {

                Sheet sheet = workbook.getSheetAt(j);
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
        }
        return null;
    }
    public Map<Integer, List<String>> getMapFromCSV(String filePath) throws IOException{

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<Integer, List<String>> data = new HashMap<>();
        String read = "";
        ArrayList<String> x;
        ArrayList<String> y;
        int i = 0;
        reader.readLine();
        while((read = reader.readLine())!=null){

            x = new ArrayList<>(Arrays.asList(read.split(";")));
            y = new ArrayList<>();
            for(int j= 0 ; j<x.size(); j++){
                y.add(x.get(j).replace(" ", "").replace("�", ""));
            }

            data.put(i, y);
            i++;
        }

        reader.close();
        return data;
    }


    public Map<Integer, List<String>> readWorksheetE3(String fileLocation, String sheetName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileLocation);
        Workbook workbook = new XSSFWorkbook(fileInputStream);


        int amountSheets = workbook.getNumberOfSheets();

        // szukaj odpowiedniej zakładki
        int numberOfSheet = -1;
        for (int j = 0; j < amountSheets; j++) {
            if (workbook.getSheetName(j).toUpperCase().contains(sheetName.toUpperCase())) {

                Sheet sheet = workbook.getSheetAt(j);
                // pobranie nazw arkuszów
                Map<Integer, List<String>> data = new HashMap<>();
                int i = 0;
                for (Row row : sheet) {
                    if(i > 6){
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
                    }

                    i++;
                }

                fileInputStream.close();
                workbook.close();
                return data;
            }
        }
        return null;
    }
}
