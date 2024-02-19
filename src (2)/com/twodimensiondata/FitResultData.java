package com.twodimensiondata;

import java.util.ArrayList;
import java.util.List;

public class FitResultData {
    // 缺陷块信息
    public List<Integer> defIndex = new ArrayList<>();
    // 矩形信息
    public int[] recInfo;
    // 是否靠左放置
    public boolean isLeft;
    // 靠边放置
    public List<int[]> sidePlacement = new ArrayList<>();
    // 不靠边放置
    public List<int[]> nonSidePlacement = new ArrayList<>();

    public FitResultData() {
    }

    public FitResultData(List<Integer> defIndex, int[] recInfo) {
        this.defIndex = defIndex;
        this.recInfo = recInfo;
    }

    public List<Integer> getDefIndex() {
        return defIndex;
    }

    public void setDefIndex(List<Integer> defIndex) {
        this.defIndex = defIndex;
    }

    public int[] getRecInfo() {
        return recInfo;
    }

    public void setRecInfo(int[] recInfo) {
        this.recInfo = recInfo;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }
}
