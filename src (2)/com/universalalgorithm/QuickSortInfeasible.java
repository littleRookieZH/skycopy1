package com.universalalgorithm;

public class QuickSortInfeasible {

    /**
     * @description 目标块的面积排序
     * @author  hao
     * @date    2023/5/30 19:42
     * @param array
    */
    public static void quickSortInfeasible(int[][] array, int[][] targetBlockSize) {
        if (array == null || array.length == 0 || array.length == 1) {
            return;
        }
        quickSort(array, 0, array.length - 1, targetBlockSize);
    }

    private static void quickSort(int[][] array, int low, int high, int[][] targetBlockSize) {
        if (low < high) {
            int pivotIndex = partition(array, low, high, targetBlockSize);
            quickSort(array, low, pivotIndex - 1, targetBlockSize);
            quickSort(array, pivotIndex + 1, high, targetBlockSize);
        }
    }

    private static int partition(int[][] array, int low, int high, int[][] targetBlockSize) {
        int index = array[high][0];
        int pivot = targetBlockSize[index][0] * targetBlockSize[index][1];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            int index1 = array[j][0];
            int temp1 = targetBlockSize[index1][0] * targetBlockSize[index1][1];
            if (temp1 < pivot) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    private static void swap(int[][] array, int i, int j) {
        int[] temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
