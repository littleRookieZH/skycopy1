package com.twodimension;

import java.util.HashMap;
import java.util.List;

/**
 * @author xzbz
 * @create 2024-01-26 19:42
 */
public class TestSide {
    public static void main(String[] args) {
        int[][] ints = {{2,2,5,5}, {5,8,8,10},{10,3,12,8}};
        int[] rec = {8,8,10,13};
        TargetData targetData = new TargetData();
        HashMap<String, List<int[]>> defBoundLines = new HashMap<>();
        targetData.initLines(ints, defBoundLines);
        Fitness fitness = new Fitness();
        boolean sidePlacement = fitness.isSidePlacement(rec, defBoundLines);
        System.out.println(sidePlacement);

    }
}
