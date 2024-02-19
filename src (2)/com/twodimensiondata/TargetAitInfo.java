package com.twodimensiondata;


import com.universalalgorithm.RangeMinimumQuery;

import java.util.HashSet;

public class TargetAitInfo {

    public double multipleRate = 20 / 1000.0;
    public double multiple = 0;
    /**
     * @description R0（按下标值排序）的辅助序列
     */
    public int[][] indexRectangles;
    /**
     * @description R1（按key值排序）的辅助序列
     */
    public int[][] keyRectangles;

    /**
     * @description R2（按width值排序）的辅助序列
     */
    public int[][] widthRectangles;

    /**
     * @description 根据矩形序列宽度创建的线段树：为了快速找到当前剩余矩形序列中宽度的最小值
     */
    public RangeMinimumQuery widthNodeTree;
    /**
     * @description 根据矩形序列创建的线段树：为了快速找到当前剩余矩形序列中下标的最小值
     */
    public  RangeMinimumQuery heightIndexNodeTree;
    /**
     * @description R3（按height值排序）的辅助序列
     */
    public int[][] heightRectangles;
    /**
     * @description 二分法查找矩形序列得出的 最小索引
     */
    private int minRecIndex;
    /**
     * @description 二分法查找矩形序列得出的 最大索引
     */
    private int maxRecIndex;

    public  int[] aitWidthArray;
    public  int[] aitHeightArray;

    private String name;
    public int[] oriArea;

//    public HashSet<String> improveFitness;

    public TargetAitInfo() {
    }

    public TargetAitInfo(int minRecIndex, int maxRecIndex) {
        this.minRecIndex = minRecIndex;
        this.maxRecIndex = maxRecIndex;
    }
    public TargetAitInfo(int minRecIndex, int maxRecIndex, RangeMinimumQuery NodeTree) {
        this.minRecIndex = minRecIndex;
        this.maxRecIndex = maxRecIndex;
    }

    public int getMinRecIndex() {
        return minRecIndex;
    }

    public void setMinRecIndex(int minRecIndex) {
        this.minRecIndex = minRecIndex;
    }

    public int getMaxRecIndex() {
        return maxRecIndex;
    }

    public void setMaxRecIndex(int maxRecIndex) {
        this.maxRecIndex = maxRecIndex;
    }

    public double getMultiple() {
        return multiple;
    }

    public void setMultiple(double multiple) {
        this.multiple = multiple;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
