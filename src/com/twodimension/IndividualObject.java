package com.twodimension;

import java.io.Serializable;

/**
 * @author xzbz
 * @create 2023-09-05 18:39
 */
public class IndividualObject implements Serializable {
    // 连续的序列
    public double[] individualPosition;

    // 根据排序得到的离散值
    public int[] jobPermutation;

    public int[][] individualSeries;

    public IndividualObject() {
    }

    public IndividualObject(double[] individualPosition, int[] jobPermutation, int[][] individualSeries) {
        this.individualPosition = individualPosition;
        this.jobPermutation = jobPermutation;
        this.individualSeries = individualSeries;
    }
}
