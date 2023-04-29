package org.example;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.interval.ConfidenceInterval;
import org.apache.poi.ss.usermodel.*;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class MathManipulation {
    ArrayList<double[]> samples = new ArrayList<>();

    public void setData(String filePath) throws IOException {
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        ArrayList<Double> z = new ArrayList<>();

        int sheetNumber = 5;

            FileInputStream inputStream = new FileInputStream(filePath);
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
            if(x.isEmpty()||y.isEmpty()||z.isEmpty())throw new IOException("Данных не хватает");
            else {
                samples.add(x.stream().mapToDouble(Double::doubleValue).toArray());
                samples.add(y.stream().mapToDouble(Double::doubleValue).toArray());
                samples.add(z.stream().mapToDouble(Double::doubleValue).toArray());
            }
    }

    //1.	Рассчитать среднее геометрическое для каждой выборки
    public double calculateGeometricMean(double[] array) {
        return StatUtils.geometricMean(array);
    }
    //2.	Рассчитать среднее арифметическое для каждой выборки
    public double calculateArithmeticMean(double[] array) {
        return StatUtils.mean(array);
    }
    //3.	Рассчитать оценку стандартного отклонения для каждой выборки
    public double calculateStandardDeviation(double[] array) {
        StandardDeviation sd = new StandardDeviation();
        return sd.evaluate(array);
    }
    //4.	Рассчитать размах каждой выборки
    public double calculateRange(double[] array) {
        return StatUtils.max(array) - StatUtils.min(array);
    }
    //5.	Рассчитать коэффициенты ковариации для всех пар случайных чисел
    public double[][] calculateCovariance(double[] x, double[] y) {
        if(x.length==y.length){
            Covariance covariance = new Covariance(new double[][] {x, y});
            return covariance.getCovarianceMatrix().getData();
        }
        return null;
    }
    //6.	Рассчитать количество элементов в каждой выборке
    public int calculateArrayLength(double[] array) {
        return array.length;
    }
    //7.	Рассчитать коэффициент вариации для каждой выборки
    public double calculateCoefficientOfVariation(double[] array) {
        StandardDeviation sd = new StandardDeviation();
        double mean =StatUtils.mean(array);
        return sd.evaluate(array) / mean;
    }
    //8.	Рассчитать для каждой выборки построить доверительный интервал для мат. ожидания (Случайные числа подчиняются нормальному закону распределения)
    public static ConfidenceInterval calculateConfidenceInterval(double[] array, double alpha) {
        StandardDeviation sd = new StandardDeviation();
        double mean = StatUtils.mean(array);
        double stdDev = sd.evaluate(array);
        NormalDistribution normalDistribution = new NormalDistribution();
        double z = normalDistribution.inverseCumulativeProbability(1.0 - alpha / 2.0);
        double marginOfError = z * stdDev / Math.sqrt(array.length);
        return new ConfidenceInterval(mean - marginOfError, mean + marginOfError, 1.0 - alpha);
    }
    //9.	Рассчитать оценку дисперсии для каждой выборки
    public static double calculateVariance(double[] array) {
        Variance variance = new Variance();
        return variance.evaluate(array);
    }
    //10.	Рассчитать максимумы и минимумы для каждой выборки
    public static double calculateMinimum(double[] array) {
        return StatUtils.min(array);
    }

    public static double calculateMaximum(double[] array) {
        return StatUtils.max(array);
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
        for (double[] sample : samples) {
            Row dataRow = sheet.createRow(rowIndex.getAndIncrement());
            dataRow.createCell(0).setCellValue(names[nameIndex]);
            dataRow.createCell(1).setCellValue(String.valueOf(calculateGeometricMean(sample)));
            dataRow.createCell(2).setCellValue(calculateArithmeticMean(sample));
            dataRow.createCell(3).setCellValue(calculateStandardDeviation(sample));
            dataRow.createCell(4).setCellValue(calculateRange(sample));
            dataRow.createCell(5).setCellValue(calculateArrayLength(sample));
            dataRow.createCell(6).setCellValue(calculateCoefficientOfVariation(sample));
            dataRow.createCell(7).setCellValue(String.valueOf(calculateConfidenceInterval(sample, 0.05)));
            dataRow.createCell(8).setCellValue(calculateVariance(sample));
            dataRow.createCell(9).setCellValue(calculateMinimum(sample));
            dataRow.createCell(10).setCellValue(calculateMaximum(sample));
            nameIndex++;
        }

        for (int i = 0; i < 11; i++) {
            sheet.autoSizeColumn(i);
        }

        double[][] covXY = calculateCovariance(samples.get(0), samples.get(1));
        if(covXY!=null) writeArrayToExcel(covXY,workbook,"Cov XY");
        double[][] covXZ = calculateCovariance(samples.get(0), samples.get(2));
        if(covXY!=null) writeArrayToExcel(covXZ,workbook,"Cov XZ");
        double[][] covYZ = calculateCovariance(samples.get(1), samples.get(2));
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
