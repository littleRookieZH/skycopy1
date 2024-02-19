package com.twodimensiondata;


import com.twodimension.SkyLine;

public class FitParameter {
    public Data data;
    public double[][] defSize;
    public int[] defIndex;
    public int[][] rectangle;
    public SkyLine sk;

    public FitParameter(Data data,int[][] rectangle, double[][] defSize, int[] defIndex, SkyLine sk) {
        this.data = data;
        this.defSize = defSize;
        this.defIndex = defIndex;
        this.rectangle = rectangle;
        this.sk = sk;
    }

    public Data getData() {
        return data;
    }

    public double[][] getDefSize() {
        return defSize;
    }

    public int[] getDefIndex() {
        return defIndex;
    }

    public int[][] getRectangle() {
        return rectangle;
    }

    public SkyLine getSk() {
        return sk;
    }

    public void setSk(SkyLine sk) {
        this.sk = sk;
    }
}
