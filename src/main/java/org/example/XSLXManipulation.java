package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class XSLXManipulation {
    private MathManipulation mm = new MathManipulation();
    public void setData(String s) throws IOException {
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> z = new ArrayList<>();

        int sheetNumber = 5;

        InputStream inputStream = MathManipulation.class.getClassLoader().getResourceAsStream("ДЗ4.xlsx");
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(sheetNumber);

        Iterator<Row> iterator = sheet.iterator();

        if (iterator.hasNext()) {
            iterator.next();
        }

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Cell cell1 = currentRow.getCell(0);
            Cell cell2 = currentRow.getCell(1);
            Cell cell3 = currentRow.getCell(2);

            if (cell1!=null&&cell1.getCellType() == CellType.NUMERIC) {
                x.add(cell1.getNumericCellValue());
            }

            if (cell2!=null&&cell2.getCellType() == CellType.NUMERIC) {
                y.add(cell2.getNumericCellValue());
            }

            if (cell3!=null&&cell3.getCellType() == CellType.NUMERIC) {
                z.add(cell3.getNumericCellValue());
            }
        }
        ArrayList<double[]> samples = new ArrayList<>();
        if(x.isEmpty()||y.isEmpty()||z.isEmpty())throw new IOException("Данных не хватает");
        else {
            samples.add(x.stream().mapToDouble(Double::doubleValue).toArray());
            samples.add(y.stream().mapToDouble(Double::doubleValue).toArray());
            samples.add(z.stream().mapToDouble(Double::doubleValue).toArray());
        }
        mm.setSamples(samples);
    }

    public void writeResultsToExcel(String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Results");

        AtomicInteger rowIndex = new AtomicInteger();
        Row headerRow = sheet.createRow(rowIndex.getAndIncrement());
        headerRow.createCell(0).setCellValue("Sample");
        headerRow.createCell(1).setCellValue("Geometric mean");
        headerRow.createCell(2).setCellValue("Arithmetic mean");
        headerRow.createCell(3).setCellValue("Standard deviation");
        headerRow.createCell(4).setCellValue("Range");
        headerRow.createCell(5).setCellValue("Array length");
        headerRow.createCell(6).setCellValue("Coefficient of variation");
        headerRow.createCell(7).setCellValue("Confidence interval");
        headerRow.createCell(8).setCellValue("Variance");
        headerRow.createCell(9).setCellValue("Minimum");
        headerRow.createCell(10).setCellValue("Maximum");

        String[] names = {"X","Y","Z"};
        int nameIndex = 0;
        for (double[] sample : mm.getSamples()) {
            Row dataRow = sheet.createRow(rowIndex.getAndIncrement());
            dataRow.createCell(0).setCellValue(names[nameIndex]);
            dataRow.createCell(1).setCellValue(String.valueOf(mm.calculateGeometricMean(sample)));
            dataRow.createCell(2).setCellValue(mm.calculateArithmeticMean(sample));
            dataRow.createCell(3).setCellValue(mm.calculateStandardDeviation(sample));
            dataRow.createCell(4).setCellValue(mm.calculateRange(sample));
            dataRow.createCell(5).setCellValue(mm.calculateArrayLength(sample));
            dataRow.createCell(6).setCellValue(mm.calculateCoefficientOfVariation(sample));
            dataRow.createCell(7).setCellValue(String.valueOf(mm.calculateConfidenceInterval(sample, 0.05)));
            dataRow.createCell(8).setCellValue(mm.calculateVariance(sample));
            dataRow.createCell(9).setCellValue(mm.calculateMinimum(sample));
            dataRow.createCell(10).setCellValue(mm.calculateMaximum(sample));
            nameIndex++;
        }

        for (int i = 0; i < 11; i++) {
            sheet.autoSizeColumn(i);
        }

        double[][] covXY = mm.calculateCovariance(mm.getSamples().get(0), mm.getSamples().get(1));
        if(covXY!=null) writeArrayToExcel(covXY,workbook,"Cov XY");
        double[][] covXZ = mm.calculateCovariance(mm.getSamples().get(0), mm.getSamples().get(2));
        if(covXY!=null) writeArrayToExcel(covXZ,workbook,"Cov XZ");
        double[][] covYZ = mm.calculateCovariance(mm.getSamples().get(1), mm.getSamples().get(2));
        if(covXY!=null) writeArrayToExcel(covYZ,workbook,"Cov YZ");

        FileOutputStream fileOut = new FileOutputStream(filePath);
        workbook.write(fileOut);
        fileOut.close();

        workbook.close();

        if(covXY==null||covXZ==null||covYZ==null) throw new IOException("Неправильные данные для рассчета ковариации");

    }
    public void writeArrayToExcel(double[][] array, Workbook workbook, String sheetname) {
        Sheet sheet = workbook.createSheet(sheetname);
        for (int rowIndex = 0; rowIndex < array.length; rowIndex++) {
            Row row = sheet.createRow(rowIndex);
            for (int colIndex = 0; colIndex < array[rowIndex].length; colIndex++) {
                Cell cell = row.createCell(colIndex);
                cell.setCellValue(array[rowIndex][colIndex]);
            }
        }
    }
}
