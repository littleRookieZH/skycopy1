package com.universalalgorithm;

import java.util.Arrays;

public class QuickSortDef {

    /**
     * @description 对缺陷块排序
     * @author  hao
     * @date    2023/5/30 19:42
     * @param array
    */
    public void quickSortDef(double[][] array) {
        if (array == null || array.length == 0 || array.length == 1) {
            return;
        }
        quickSort(array, 0, array.length - 1);
    }

    private void quickSort(double[][] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    private int partition(double[][] array, int low, int high) {
        double pivot = array[high][0] * array[high][0] + array[high][1] * array[high][1];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            double temp1 = array[j][0] * array[j][0] + array[j][1] * array[j][1];
            if (temp1 < pivot) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    private void swap(double[][] array, int i, int j) {
        double[] temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    private void swap(int[][] array, int i, int j) {
        int[] temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static void main(String[] args) {
        QuickSortDef quickSortDef = new QuickSortDef();
        double[][] array = {{5,1}, {3,1},{4,2},{5,2}};
        quickSortDef.quickSortDef(array);
        for(double[] i: array){
            System.out.println(Arrays.toString(i));
        }
    }

    /**
     * @description 以宽度递减排序
     * @author  hao
     * @date    2023/7/2 10:13
     * @param array
     * @return int[][]
    */
    public int[][] getCcm1QuickSort(int[][] array){
        if (array == null || array.length == 0 || array.length == 1) {
            return null;
        }
        quickSort(array, 0, array.length - 1);
        return array;
    }
    private void quickSort(int[][] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }
    private int partition(int[][] arr, int low, int high) {
        int pivot = arr[high][0];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j][0] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }
}
