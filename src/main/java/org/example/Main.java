package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MathManipulation mm = new MathManipulation();
        mm.setData("./data/ДЗ4.xlsx");
        mm.writeResultsToExcel("./data/Result.xlsx");
    }
}