package org.example;

public class Main {
    public static void main(String[] args) {
        MathManipulation mm = new MathManipulation();
        mm.setData("./data/ДЗ4.xlsx");
        mm.calculateAll();
    }
}