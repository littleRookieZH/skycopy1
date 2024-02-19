package com.universalalgorithm;


import com.commonfunction.CommonToolClass;
import com.pointset.ToolClass;
import com.twodimension.TargetData;


import java.util.Arrays;


public class MaxHeapSort {

    // /**
    //  * @description 指定堆排序的排序标准  key width height
    //  */
    // private static int sortIndex;
    //
    // /**
    //  * @description 重置排序的索引 比如 按key重新排序之后 [][5] 需要随之更新
    //  */
    // private static int resetIndex;

    public int getSortIndex(int[] sortingMethod) {
        return sortingMethod[0];
    }

    public int getResetIndex(int[] sortingMethod) {
        return sortingMethod[1];
    }

    public static void main(String[] args) {
        int[][] array = {{1, 2, 3, 4, 4}, {2, 2, 0, 1, 1}, {3, 1, 6, 0, 342}, {4, 1, 6, 9, 132}, {5, 8, 2, 5, 453}, {6, 8, 3, 6, 14}};

//        System.out.println("Original array:");
//        printArray(array);

//        heapSortWithIndex(array,"key");
//        System.out.println("by key and index Array after sorting:");
//        printArray(array);
//        heapSortWithWidth(array,"height");
//        System.out.println("by height and width Array after sorting:");
//        printArray(array);
//         int[][] ints1 = heapSortHeight(array);
//        int[][] ints = heapSortOriIndex(array);
//         printArray(ints1);
    }

    /**
     * @param keyRectangles
     * @param keyword
     * @description int[i][methods]相同时，以宽度递增排序
     * @author hao
     * @date 2023/5/11 21:40
     */
    public void heapSortWithWidth(int[][] keyRectangles, String keyword) {
        int[] sortingMethod = switchSortingMethod(keyword);
        int n = keyRectangles.length;
        // 构建初始大顶堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyWithWidth(sortingMethod, keyRectangles, n, i);
        }

        // 逐步将堆顶元素（最大值）与最后一个元素交换，并进行堆调整
        for (int i = n - 1; i >= 0; i--) {
            swap(sortingMethod, keyRectangles, 0, i);
            heapifyWithWidth(sortingMethod, keyRectangles, i, 0);
        }
    }

    public void heapifyWithWidth(int[] sortingMethod, int[][] keyRectangles, int heapSize, int rootIndex) {
        int largestIndex = rootIndex;
        int leftChildIndex = 2 * rootIndex + 1;
        int rightChildIndex = 2 * rootIndex + 2;
        // 找到父节点和两个子节点中最大的那一个
        if (leftChildIndex < heapSize && keyRectangles[leftChildIndex][sortingMethod[0]] > keyRectangles[largestIndex][sortingMethod[0]]) {
            largestIndex = leftChildIndex;
        }
        if (leftChildIndex < heapSize && keyRectangles[leftChildIndex][sortingMethod[0]] == keyRectangles[largestIndex][sortingMethod[0]]) {
            if (keyRectangles[leftChildIndex][2] > keyRectangles[largestIndex][2]) {
                largestIndex = leftChildIndex;
            }
        }
        if (rightChildIndex < heapSize && keyRectangles[rightChildIndex][sortingMethod[0]] > keyRectangles[largestIndex][sortingMethod[0]]) {
            largestIndex = rightChildIndex;
        }
        if (rightChildIndex < heapSize && keyRectangles[rightChildIndex][sortingMethod[0]] == keyRectangles[largestIndex][sortingMethod[0]]) {
            if (keyRectangles[rightChildIndex][2] > keyRectangles[largestIndex][2]) {
                largestIndex = rightChildIndex;
            }
        }
        if (largestIndex != rootIndex) {
            swap(sortingMethod, keyRectangles, rootIndex, largestIndex);
            heapifyWithWidth(sortingMethod, keyRectangles, heapSize, largestIndex);
        }
    }

    /**
     * @param keyRectangles
     * @param keyword
     * @description int[i][sortingMethod[0]]相同时，以 索引 递增排序
     * @author hao
     * @date 2023/5/11 21:41
     */
    public void heapSortWithIndex(int[][] keyRectangles, String keyword) {
        int[] sortingMethod = switchSortingMethod(keyword);
        int n = keyRectangles.length;
        // 构建初始大顶堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapWithIndex(sortingMethod, keyRectangles, n, i);
        }

        // 逐步将堆顶元素（最大值）与最后一个元素交换，并进行堆调整
        for (int i = n - 1; i >= 0; i--) {
            swap(sortingMethod, keyRectangles, 0, i);
            heapWithIndex(sortingMethod, keyRectangles, i, 0);
        }
    }

    public void heapWithIndex(int[] sortingMethod, int[][] keyRectangles, int heapSize, int rootIndex) {
        int largestIndex = rootIndex;
        int leftChildIndex = 2 * rootIndex + 1;
        int rightChildIndex = 2 * rootIndex + 2;
        // 找到父节点和两个子节点中最大的那一个
        if (leftChildIndex < heapSize && keyRectangles[leftChildIndex][sortingMethod[0]] > keyRectangles[largestIndex][sortingMethod[0]]) {
            largestIndex = leftChildIndex;
        }
        if (leftChildIndex < heapSize && keyRectangles[leftChildIndex][sortingMethod[0]] == keyRectangles[largestIndex][sortingMethod[0]]) {
            if (keyRectangles[leftChildIndex][0] > keyRectangles[largestIndex][0]) {
                largestIndex = leftChildIndex;
            }
        }
        if (rightChildIndex < heapSize && keyRectangles[rightChildIndex][sortingMethod[0]] > keyRectangles[largestIndex][sortingMethod[0]]) {
            largestIndex = rightChildIndex;
        }
        if (rightChildIndex < heapSize && keyRectangles[rightChildIndex][sortingMethod[0]] == keyRectangles[largestIndex][sortingMethod[0]]) {
            if (keyRectangles[rightChildIndex][0] > keyRectangles[largestIndex][0]) {
                largestIndex = rightChildIndex;
            }
        }
        if (largestIndex != rootIndex) {
            swap(sortingMethod, keyRectangles, rootIndex, largestIndex);
            heapWithIndex(sortingMethod, keyRectangles, heapSize, largestIndex);
        }
    }

    /**
     * @param keyRectangles
     * @description 面积堆排序
     * @author hao
     * @date 2023/5/11 21:42
     */
    public int[][] heapSortArea(int[][] keyRectangles) {
        CommonToolClass commonToolClass = new CommonToolClass();
        int[][] tempArray = commonToolClass.assistArray(keyRectangles);
        int n = tempArray.length;
        // 构建初始大顶堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyArea(tempArray, n, i);
        }

        // 逐步将堆顶元素（最大值）与最后一个元素交换，并进行堆调整
        for (int i = n - 1; i >= 0; i--) {
            fourRulesSwap(tempArray, 0, i);
            heapifyArea(tempArray, i, 0);
        }
        return tempArray;
    }

    public void heapifyArea(int[][] keyRectangles, int heapSize, int rootIndex) {
        int largestIndex = rootIndex;
        int leftChildIndex = 2 * rootIndex + 1;
        int rightChildIndex = 2 * rootIndex + 2;
        int largestArea = keyRectangles[largestIndex][2] * keyRectangles[largestIndex][3];
        // 找到父节点和两个子节点中最大的那一个
        if (leftChildIndex < heapSize) {
            int leftChildArea = keyRectangles[leftChildIndex][2] * keyRectangles[leftChildIndex][3];
            if (leftChildArea < largestArea || (leftChildArea == largestArea && keyRectangles[leftChildIndex][2] < keyRectangles[largestIndex][2])) {
                largestIndex = leftChildIndex;
                largestArea = keyRectangles[largestIndex][2] * keyRectangles[largestIndex][3];
            }
        }
        if (rightChildIndex < heapSize) {
            int rightChildArea = keyRectangles[rightChildIndex][2] * keyRectangles[rightChildIndex][3];
            if (rightChildArea < largestArea || (rightChildIndex == largestArea && keyRectangles[rightChildIndex][2] < keyRectangles[largestIndex][2])) {
                largestIndex = rightChildIndex;
            }
        }
        if (largestIndex != rootIndex) {
            fourRulesSwap(keyRectangles, rootIndex, largestIndex);
            heapifyArea(keyRectangles, heapSize, largestIndex);
        }
    }

    /**
     * @param keyRectangles
     * @description 高度堆排序
     * @author hao
     * @date 2023/5/11 21:42
     */
    public int[][] heapSortHeight(int[][] keyRectangles) {
        CommonToolClass commonToolClass = new CommonToolClass();
        int[][] tempArray = commonToolClass.assistArray(keyRectangles);
        int n = tempArray.length;
        // 构建初始大顶堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyHeight(tempArray, n, i);
        }
        // 逐步将堆顶元素（最大值）与最后一个元素交换，并进行堆调整
        for (int i = n - 1; i > 0; i--) {
            fourRulesSwap(tempArray, 0, i);
            heapifyHeight(tempArray, i, 0);
        }
        return tempArray;
    }

    public void heapifyHeight(int[][] keyRectangles, int heapSize, int rootIndex) {
        // 初始化最大元素为根节点
        int largestIndex = rootIndex;
        int leftChildIndex = 2 * rootIndex + 1;
        int rightChildIndex = 2 * rootIndex + 2;
        // 找到父节点和两个子节点中最大的那一个
        if (leftChildIndex < heapSize) {
            if (keyRectangles[leftChildIndex][3] < keyRectangles[largestIndex][3] || (keyRectangles[leftChildIndex][3] == keyRectangles[largestIndex][3] && keyRectangles[leftChildIndex][2] < keyRectangles[largestIndex][2])) {
                largestIndex = leftChildIndex;
            }
        }
        if (rightChildIndex < heapSize) {
            if (keyRectangles[rightChildIndex][3] < keyRectangles[largestIndex][3] || (keyRectangles[rightChildIndex][3] == keyRectangles[largestIndex][3] && keyRectangles[rightChildIndex][2] < keyRectangles[largestIndex][2])) {
                largestIndex = rightChildIndex;
            }
        }
        if (largestIndex != rootIndex) {
            fourRulesSwap(keyRectangles, rootIndex, largestIndex);
            heapifyHeight(keyRectangles, heapSize, largestIndex);
        }
    }

    /**
     * @param keyRectangles
     * @description 宽度堆排序
     * @author hao
     * @date 2023/5/11 21:42
     */
    public int[][] heapSortWitdh(int[][] keyRectangles) {
        CommonToolClass commonToolClass = new CommonToolClass();
        int[][] tempArray = commonToolClass.assistArray(keyRectangles);
        int n = tempArray.length;
        // 构建初始大顶堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyWitdh(tempArray, n, i);
        }

        // 逐步将堆顶元素（最大值）与最后一个元素交换，并进行堆调整
        for (int i = n - 1; i >= 0; i--) {
            fourRulesSwap(tempArray, 0, i);
            heapifyWitdh(tempArray, i, 0);
        }
        return tempArray;
    }

    public void heapifyWitdh(int[][] keyRectangles, int heapSize, int rootIndex) {
        int largestIndex = rootIndex;
        int leftChildIndex = 2 * rootIndex + 1;
        int rightChildIndex = 2 * rootIndex + 2;
        if (leftChildIndex < heapSize) {
            if (keyRectangles[leftChildIndex][2] < keyRectangles[largestIndex][2] || (keyRectangles[leftChildIndex][2] == keyRectangles[largestIndex][2] && keyRectangles[leftChildIndex][3] < keyRectangles[largestIndex][3])) {
                largestIndex = leftChildIndex;
            }
        }
        if (rightChildIndex < heapSize) {
            if (keyRectangles[rightChildIndex][2] < keyRectangles[largestIndex][2] || (keyRectangles[rightChildIndex][2] == keyRectangles[largestIndex][2] && keyRectangles[rightChildIndex][3] < keyRectangles[largestIndex][3])) {
                largestIndex = rightChildIndex;
            }
        }
        if (largestIndex != rootIndex) {
            fourRulesSwap(keyRectangles, rootIndex, largestIndex);
            heapifyWitdh(keyRectangles, heapSize, largestIndex);
        }
    }

    /**
     * @param keyRectangles
     * @description 半周长堆排序
     * @author hao
     * @date 2023/5/11 21:42
     */
    public int[][] heapSortPerimeter(int[][] keyRectangles) {
        CommonToolClass commonToolClass = new CommonToolClass();
        int[][] tempArray = commonToolClass.assistArray(keyRectangles);
        int n = tempArray.length;
        // 构建初始大顶堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyPerimeter(tempArray, n, i);
        }

        // 逐步将堆顶元素（最大值）与最后一个元素交换，并进行堆调整
        for (int i = n - 1; i >= 0; i--) {
            fourRulesSwap(tempArray, 0, i);
            heapifyPerimeter(tempArray, i, 0);
        }
        return tempArray;
    }

    public void heapifyPerimeter(int[][] keyRectangles, int heapSize, int rootIndex) {
        int largestIndex = rootIndex;
        int leftChildIndex = 2 * rootIndex + 1;
        int rightChildIndex = 2 * rootIndex + 2;
        int largestPerimeter = keyRectangles[largestIndex][2] + keyRectangles[largestIndex][3];
        // 找到父节点和两个子节点中最大的那一个
        if (leftChildIndex < heapSize) {
            int leftChildPerimeter = keyRectangles[leftChildIndex][2] + keyRectangles[leftChildIndex][3];
            if (leftChildPerimeter < largestPerimeter || (leftChildPerimeter == largestPerimeter && keyRectangles[leftChildIndex][2] < keyRectangles[largestIndex][2])) {
                largestIndex = leftChildIndex;
                largestPerimeter = keyRectangles[largestIndex][2] + keyRectangles[largestIndex][3];
            }
        }
        if (rightChildIndex < heapSize) {
            int rightChildPerimeter = keyRectangles[rightChildIndex][2] + keyRectangles[rightChildIndex][3];
            if (rightChildPerimeter < largestPerimeter || (rightChildPerimeter == largestPerimeter && keyRectangles[rightChildIndex][2] < keyRectangles[largestIndex][2])) {
                largestIndex = rightChildIndex;
            }
        }
        if (largestIndex != rootIndex) {
            fourRulesSwap(keyRectangles, rootIndex, largestIndex);
            heapifyPerimeter(keyRectangles, heapSize, largestIndex);
        }
    }

    /**
     * @param keyRectangles
     * @description 原序列下标排序
     * @author hao
     * @date 2023/5/11 21:42
     */
    public int[][] heapSortOriIndex(int[][] keyRectangles) {
        CommonToolClass commonToolClass = new CommonToolClass();
        int[][] tempArray = commonToolClass.assistArrayRec(keyRectangles);
        int n = tempArray.length;
        // 构建初始大顶堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyOriIndex(tempArray, n, i);
        }

        // 逐步将堆顶元素（最大值）与最后一个元素交换，并进行堆调整
        for (int i = n - 1; i >= 0; i--) {
            fourRulesSwap(tempArray, i, 0);
            heapifyOriIndex(tempArray, i, 0);
        }
        return tempArray;
    }

    public void heapifyOriIndex(int[][] keyRectangles, int heapSize, int rootIndex) {
        int largestIndex = rootIndex;
        int leftChildIndex = 2 * rootIndex + 1;
        int rightChildIndex = 2 * rootIndex + 2;
        if (leftChildIndex < heapSize && keyRectangles[leftChildIndex][0] > keyRectangles[largestIndex][0]) {
            largestIndex = leftChildIndex;
        }
        if (rightChildIndex < heapSize && keyRectangles[rightChildIndex][0] > keyRectangles[largestIndex][0]) {
            largestIndex = rightChildIndex;
        }
        if (largestIndex != rootIndex) {
            fourRulesSwap(keyRectangles, rootIndex, largestIndex);
            heapifyOriIndex(keyRectangles, heapSize, largestIndex);
        }
    }

    /**
     * @param keyRectangles
     * @param i
     * @param j
     * @description 基于四种排序规则的交换
     * @author hao
     * @date 2023/5/13 11:23
     */
    public void fourRulesSwap(int[][] keyRectangles, int i, int j) {
        int[] temp = keyRectangles[i];
        keyRectangles[i] = keyRectangles[j];
        keyRectangles[j] = temp;
    }

    /**
     * 基于三种辅助序列的交换， 要求在堆排序的时候重置 resetIndex 下标
     *
     * @param keyRectangles 数组
     * @param i
     * @param j
     */
    public void swap(int[] sortingMethod, int[][] keyRectangles, int i, int j) {
        // 应该可以在交换的时候，重置key
        int[] temp = keyRectangles[i];
        keyRectangles[i] = keyRectangles[j];
        keyRectangles[j] = temp;
        keyRectangles[i][sortingMethod[1]] = i;
        keyRectangles[j][sortingMethod[1]] = j;
    }

    /**
     * @return int
     * @description 根据关键词 返回 堆排序的排序标准
     * @author hao
     * @date 2023/5/13 11:33
     */
    public int[] switchSortingMethod(String keyword) {
        int[] sortIndexes = new int[2];
        switch (keyword) {
            case "key":
                //  sortIndex
                //  resetIndex
                sortIndexes[0] = 4;
                sortIndexes[1] = 5;
                break;
            case "width":
                sortIndexes[0] = 2;
                sortIndexes[1] = 6;
                break;
            case "height":
                sortIndexes[0] = 3;
                sortIndexes[1] = 7;
                break;
        }
        return sortIndexes;
    }


}

