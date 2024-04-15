package org.example;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SampleCreator {

    public void createFile(String path) throws IOException {
        String filePath = path;
        int numberOfSheets = new Random().nextInt(10) + 1; // Рандомное количество листов от 1 до 10
        Workbook workbook = new XSSFWorkbook();
        for (int i = 0; i < numberOfSheets; i++) {
            XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Sheet" + (i + 1));
            createRandomColumns(sheet);
        }
        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        workbook.close();
        fileOut.close();
    }

    private static void createRandomColumns(XSSFSheet sheet) {
        Random random = new Random();
        int numberOfColumns = random.nextInt(25) + 5; // Рандомное количество колонок от 5 до 30
        int numberOfRows = random.nextInt(25) + 5; // Рандомное количество строк от 5 до 30

        Set<String> usedHeaders = new HashSet<>();
        for (int i = 0; i < numberOfRows; i++){
            XSSFRow row = ((XSSFSheet) sheet).createRow(i);
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
        int headerLength = random.nextInt(3) + 1; // Длина заголовка от 1 до 3 символов
        for (int i = 0; i < headerLength; i++) {
            char randomChar = (char) (random.nextInt(26) + 'A'); // Генерация большой буквы латинского алфавита
            header.append(randomChar);
        }
        return header.toString();
    }
}
