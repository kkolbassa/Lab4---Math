package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class XSLXManipulation {

    private MathManipulation mm = new MathManipulation();
    private SampleCreator sc = new SampleCreator();

    public void setData(String path) throws IOException {
        mm = new MathManipulation();
        InputStream inputStream;
        if(path.equals("ДЗ4.xlsx")){
            inputStream = MathManipulation.class.getClassLoader().getResourceAsStream("ДЗ4.xlsx");
        } else {
            sc.createFile(path);
            inputStream = new FileInputStream(path);
        }

        Workbook workbook = WorkbookFactory.create(inputStream);
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            DataSheet dataSheet = new DataSheet();
            Sheet sheet = workbook.getSheetAt(i);
            TreeMap<String, double[]> sheetData = new TreeMap<>();
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                if (row.getRowNum() == 0) {
                    continue; // Пропускаем первую строку с заголовками
                }
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    String columnHeader = sheet.getRow(0).getCell(columnIndex).getStringCellValue();
                    Double cellValue = cell.getNumericCellValue();
                    if (sheetData.containsKey(columnHeader)) {
                        double[] values = sheetData.get(columnHeader);
                        double[] newValues = new double[values.length + 1];
                        System.arraycopy(values, 0, newValues, 0, values.length);
                        newValues[values.length] = cellValue;
                        sheetData.put(columnHeader, newValues);
                    } else {
                        sheetData.put(columnHeader, new double[]{cellValue});
                    }
                }
            }
            dataSheet.setSheet(sheetData);
            mm.addSheet(dataSheet);
        }
    }

    public void writeResultsToExcel(String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        for (int sheetIndex = 0; sheetIndex < mm.getDataSheets().size(); sheetIndex++) {
            Sheet sheet = workbook.createSheet("Sheet " + (sheetIndex + 1));
            DataSheet dataSheet = mm.getDataSheets(sheetIndex);

            AtomicInteger rowIndex = new AtomicInteger();
            Row headerRow = sheet.createRow(rowIndex.getAndIncrement());
            headerRow.createCell(0).setCellValue("Sample");
            headerRow.createCell(1).setCellValue("Geometric mean");
            headerRow.createCell(2).setCellValue("Arithmetic mean");
            headerRow.createCell(3).setCellValue("Standard deviation");
            headerRow.createCell(4).setCellValue("Range");
            headerRow.createCell(5).setCellValue("Array length");
            headerRow.createCell(6).setCellValue("Coefficient of variation");
            headerRow.createCell(7).setCellValue("Confidence interval(lower bound)");
            headerRow.createCell(8).setCellValue("Confidence interval(upper bound)");
            headerRow.createCell(9).setCellValue("Variance");
            headerRow.createCell(10).setCellValue("Minimum");
            headerRow.createCell(11).setCellValue("Maximum");

            for (String key : dataSheet.getSheet().keySet()) {
                Row row = sheet.createRow(rowIndex.getAndIncrement());
                row.createCell(0).setCellValue(key);
                double[] sample = dataSheet.getSheet().get(key);
                row.createCell(1).setCellValue(String.valueOf(mm.calculateGeometricMean(sample)));
                row.createCell(2).setCellValue(mm.calculateArithmeticMean(sample));
                row.createCell(3).setCellValue(mm.calculateStandardDeviation(sample));
                row.createCell(4).setCellValue(mm.calculateRange(sample));
                row.createCell(5).setCellValue(mm.calculateArrayLength(sample));
                row.createCell(6).setCellValue(mm.calculateCoefficientOfVariation(sample));
                row.createCell(7).setCellValue(MathManipulation.calculateConfidenceInterval(sample, 0.05).getLowerBound());
                row.createCell(8).setCellValue(MathManipulation.calculateConfidenceInterval(sample, 0.05).getUpperBound());
                row.createCell(9).setCellValue(mm.calculateVariance(sample));
                row.createCell(10).setCellValue(mm.calculateMinimum(sample));
                row.createCell(11).setCellValue(mm.calculateMaximum(sample));
            }
            rowIndex.getAndIncrement();
            sheet.createRow(rowIndex.getAndIncrement()).createCell(0).setCellValue("Матрица ковариаций:");

            Row rowHeader = sheet.createRow(rowIndex.getAndIncrement());
            AtomicInteger colIndexHeader = new AtomicInteger();
            colIndexHeader.getAndIncrement();

            for (String key1 : dataSheet.getSheet().keySet()) {
                Row row = sheet.createRow(rowIndex.getAndIncrement());
                double[] values1 = dataSheet.getSheet().get(key1);

                AtomicInteger colIndex = new AtomicInteger();
                row.createCell(colIndex.getAndIncrement()).setCellValue(key1);
                rowHeader.createCell(colIndexHeader.getAndIncrement()).setCellValue(key1);
                for (String key2 : dataSheet.getSheet().keySet()) {
                    double[] values2 = dataSheet.getSheet().get(key2);
                    row.createCell(colIndex.getAndIncrement()).setCellValue(mm.calculateCovariance(values1, values2));
                }
            }

            for (int i = 0; i < 12; i++) {
                sheet.autoSizeColumn(i);
            }
        }
        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
}
