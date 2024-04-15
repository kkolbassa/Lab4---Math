package org.example;

import java.util.TreeMap;

public class DataSheet {
    private TreeMap<String, double[]> sheet;

    public DataSheet() {
        this.sheet = new TreeMap<>();
    }

    public TreeMap<String, double[]> getSheet() {
        return sheet;
    }

    public void setSheet(TreeMap<String, double[]> sheet) {
        this.sheet = sheet;
    }

}
