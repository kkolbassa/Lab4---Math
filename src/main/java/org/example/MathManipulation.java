package org.example;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.interval.ConfidenceInterval;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.Variance;

import java.util.ArrayList;


public class MathManipulation {
    private ArrayList<DataSheet> dataSheets = new ArrayList<>();

    public ArrayList<DataSheet> getDataSheets() {
        return dataSheets;
    }
    public DataSheet getDataSheets(int index) {
        return dataSheets.get(index);
    }

    public void setDataSheets(ArrayList<DataSheet> dataSheets) {
        this.dataSheets = dataSheets;
    }

    public void addSheet(DataSheet dataSheet) {
        dataSheets.add(dataSheet);
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
    public double calculateCovariance(double[] x, double[] y) {
            Covariance covariance = new Covariance();
            return covariance.covariance(x,y);
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


}
