package org.example;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SampleCreator {

    public void createFile(String path) throws IOException {

        File file = new File(path);

        if (!file.exists()) {

            int numberOfSheets = new Random().nextInt(100) + 1; // Рандомное количество листов от 1 до 100
            Workbook workbook = new XSSFWorkbook();
            for (int i = 0; i < numberOfSheets; i++) {
                XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet" + (i + 1));
                createRandomColumns(sheet);
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();
        }
    }

    private static void createRandomColumns(XSSFSheet sheet) {
        Random random = new Random();
        int numberOfColumns = random.nextInt(100) + 1; // Рандомное количество колонок от 1 до 100
        int numberOfRows = random.nextInt(1000) + 2; // Рандомное количество строк от 2 до 1001

        Set<String> usedHeaders = new HashSet<>();
        for (int i = 0; i < numberOfRows; i++){
            XSSFRow row = sheet.createRow(i);
            for (int j = 0; j < numberOfColumns; j++) {
                if (i == 0) { // обработка заголовка
                    String header;
                    do {
                        header = generateRandomHeader(random);
                    } while (usedHeaders.contains(header));
                    usedHeaders.add(header);
                    row.createCell(j).setCellValue(header);
                } else row.createCell(j).setCellValue(Math.random() * 200 - 100); // случайное double число от -100 до 100
            }
        }
    }

    private static String generateRandomHeader(Random random) {
        StringBuilder header = new StringBuilder();
        int headerLength = random.nextInt(4) + 1; // Длина заголовка от 1 до 4 символов
        for (int i = 0; i < headerLength; i++) {
            char randomChar = (char) (random.nextInt(26) + 'A'); // Генерация большой буквы латинского алфавита
            header.append(randomChar);
        }
        return header.toString();
    }
}
