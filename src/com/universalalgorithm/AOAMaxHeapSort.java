package com.universalalgorithm;

import java.util.Arrays;

public class AOAMaxHeapSort {
    public void heapSort(int[][] arr) {
        int n = arr.length;

        // 构建初始小顶堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // 逐个将堆顶元素（最小值）移动到数组末尾
        for (int i = n - 1; i >= 0; i--) {
            // 将堆顶元素与当前未排序部分的最后一个元素交换
            int[] temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // 重新构建小顶堆
            heapify(arr, i, 0);
        }
    }

    private void heapify(int[][] arr, int n, int i) {
        int smallest = i; // 假设当前节点为最大值
        int left = 2 * i + 1; // 左子节点的索引
        int right = 2 * i + 2; // 右子节点的索引

        // 如果左子节点小于当前节点，更新最大值索引
        if (left < n && arr[left][0] > arr[smallest][0]) {
            smallest = left;
        }

        // 如果右子节点小于当前节点，更新最大值索引
        if (right < n && arr[right][0] > arr[smallest][0]) {
            smallest = right;
        }

        // 如果最大值不是当前节点，交换当前节点与最大值节点
        if (smallest != i) {
            int[] temp = arr[i];
            arr[i] = arr[smallest];
            arr[smallest] = temp;

            // 递归调整交换后的子树
            heapify(arr, n, smallest);
        }
    }

    public void updateHeap(int[][] arr, int index, int newValue) {
        // 更新指定索引处的元素值
        arr[index][0] = newValue;
        heapSort(arr);
//        // 向上调整，保持小顶堆的性质
//        while (index > 0 && arr[index] < arr[parent(index)]) {
//            int parentIndex = parent(index);
//            int temp = arr[index];
//            arr[index] = arr[parentIndex];
//            arr[parentIndex] = temp;
//            index = parentIndex;
//        }
//
//        // 向下调整，保持小顶堆的性质
//        heapify(arr, arr.length, index);
    }

    private static int parent(int index) {
        return (index - 1) / 2;
    }

    public static void main(String[] args) {
        AOAMaxHeapSort aoaMaxHeapSort = new AOAMaxHeapSort();

        int[][] arr = {{6}, {2}, {8}, {1}, {4}, {9}, {3}, {7}, {5}};

        System.out.println("Original Array: " + Arrays.toString(arr));

        aoaMaxHeapSort.heapSort(arr);

        System.out.println("Sorted Array: " + Arrays.toString(arr));

        aoaMaxHeapSort.updateHeap(arr, 3, 10);

        System.out.println("Updated Array: " + Arrays.toString(arr));

        aoaMaxHeapSort.heapSort(arr);

        for (int[] tempArr : arr) {
            System.out.println("Sorted Array: " + Arrays.toString(tempArr));
        }
    }
}
