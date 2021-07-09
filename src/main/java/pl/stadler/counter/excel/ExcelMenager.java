package pl.stadler.counter.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.stadler.counter.models.ProjectSettings;

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
                                try {
                                    data.get(i).add(String.valueOf((int) cell.getNumericCellValue()));
                                } catch (Exception e) {
                                    data.get(i).add(cell.getRichStringCellValue().getString().replace(" ", ""));
                                }

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

    /////////////////////////
    // zpisanie pliku na dysk
    /////////////////////////

    public void saveFile(MultipartFile multipartFile, String fileLocation) {
        // przepisanie pliku do nowego
        File newFile = new File(fileLocation);
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = multipartFile.getInputStream();
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ///////////////////////////////
    // wczytanie pliku xlsx do mapy
    ///////////////////////////////

    public Map<Integer, List<String>> readWorksheetExcelXLSX(String filelocation, String sheetName) throws IOException, InvalidFormatException {

        Map<Integer, List<String>> data = new HashMap<>();
        FileInputStream fileInputStream = null;
        try {
            File excelFile = new File(filelocation);
            fileInputStream = new FileInputStream(excelFile);

            Workbook workbook = new XSSFWorkbook(fileInputStream);

            int amountSheets = workbook.getNumberOfSheets();


            // szukaj odpowiedniej zakładki
            int numberOfSheet = -1;
            for (int j = 0; j < amountSheets; j++) {
                if (workbook.getSheetName(j).toUpperCase().contains(sheetName.toUpperCase())) {

                    Sheet sheet = workbook.getSheetAt(j);
                    // pobranie nazw arkuszów
                    int i = 0;
                    for (Row row : sheet) {
                        if (i > 6) {
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
                                        try {
                                            data.get(i).add(String.valueOf((int) cell.getNumericCellValue()));
                                        } catch (Exception e) {
                                            data.get(i).add(cell.getRichStringCellValue().getString().replace(" ", ""));
                                        }
                                        break;
                                    default:
                                        data.get(i).add(" ");
                                }
                            }
                        }

                        i++;
                    }
                }
            }
            workbook.close();
            return data;
        } catch (Exception e) {
        } finally {
            assert fileInputStream != null;
            fileInputStream.close();
        }
        return null;
    }

    //////////////////////////////
    // wczytanie pliku csv do mapy
    //////////////////////////////

    public Map<Integer, List<String>> getMapFromCSV(String filePath) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        Map<Integer, List<String>> data = new HashMap<>();
        String read = "";
        ArrayList<String> x;
        ArrayList<String> y;
        int i = 0;
        reader.readLine();
        while ((read = reader.readLine()) != null) {
            if (i == 5160) {
                System.out.println(read);
            }
            x = new ArrayList<>(Arrays.asList(read.split(";")));
            y = new ArrayList<>();
            for (int j = 0; j < x.size(); j++) {
                y.add(x.get(j).replace(" ", "").replace(" ", ""));
            }

            data.put(i, y);
            i++;
        }

        reader.close();
        return data;
    }


//    @EventListener(ApplicationReadyEvent.class)
//
//    public void asda() throws IOException, InvalidFormatException {
//        readWorksheetExcelXLSX2("L:\\05_KIEROWNICTWO\\07_Teamleader\\Okninski Dariusz\\L-4444 FNM Harting extern.xlsx","wagon_A", 8, 13);
//    }


    public Map<Integer, List<String>> readWorksheetExcelXLSX2(String filelocation, Integer moduleA, Integer moduleF) throws IOException{
        Map<Integer, List<String>> data = new HashMap<>();
        FileInputStream fileInputStream = null;
        try {
            File excelFile = new File(filelocation);
            fileInputStream = new FileInputStream(excelFile);

            Workbook workbook = new XSSFWorkbook(fileInputStream);

            int amountSheets = workbook.getNumberOfSheets();
            int i = 0;

            // szukaj odpowiedniej zakładki
            for (int j = 0; j < amountSheets; j++) {
                if (workbook.getSheetName(j).toUpperCase().contains("WAGON") || workbook.getSheetName(j).toUpperCase().contains("JUMPER") ) {

                    //System.out.println(workbook.getSheetName(j));
                    Sheet sheet = workbook.getSheetAt(j);
                    List<List<Integer>> listMerge = new ArrayList<>();

                    for (int l = 0; l < sheet.getNumMergedRegions(); l++) {
                        CellRangeAddress region = sheet.getMergedRegion(l);
                        int lastCol = region.getLastColumn();

                        int rowNum = region.getFirstRow();
                        if (lastCol != region.getFirstColumn() && region.getFirstRow() == region.getLastRow()) {
                            List<Integer> listTmp = new ArrayList<>();
                            listTmp.add(rowNum);
                            listTmp.add(region.getFirstColumn());
                            listTmp.add(region.getNumberOfCells());
                            listMerge.add(listTmp);
                        }
                    }
//                    for (List<Integer> integers : listMerge) {
//                        System.out.println(integers);
//                    }
                    // pobranie nazw arkuszów

                    int z = 0;
                    for (Row row : sheet) {
                        int k =0;

                        data.put(i, new ArrayList<String>());


                        for (Cell cell : row) {

                            boolean orFlag = false;
                            for (List<Integer> integers : listMerge) {
                                if(z == integers.get(0) && k == integers.get(1)){

                                    orFlag = true;

                                    data.get(i).add(String.valueOf(row.getCell(integers.get(1))).replace(" ", ""));
                                }else if(z == integers.get(0) && k > integers.get(1) && k< (integers.get(1) + integers.get(2))){

                                    orFlag = true;

                                    data.get(i).add(String.valueOf(row.getCell(integers.get(1))).replace(" ", ""));

                                }

                            }
                            //System.out.println(orFlag);
                            if(!orFlag){

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
                                        try {
                                            data.get(i).add(String.valueOf((int) cell.getNumericCellValue()));
                                        } catch (Exception e) {
                                            data.get(i).add(cell.getRichStringCellValue().getString().replace(" ", ""));
                                        }
                                        break;
                                    default:
                                        data.get(i).add(" ");
                                }
                            }
                            k++;

                        }
                        z++;
                        i++;
                    }
                }
            }
            workbook.close();
            return data;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            assert fileInputStream != null;
            fileInputStream.close();
        }
        return null;
    }


}
