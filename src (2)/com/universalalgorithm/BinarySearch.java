package com.universalalgorithm;

public class BinarySearch {
    /**
     * @description  使用二分法查找
     * @author  hao
     * @date    2023/5/11 14:24
     * @param nums
     * @param target
     * @return int
    */
    public static int binarySearch(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target) {
                // 找到目标值，返回索引
                return mid;
            } else if (nums[mid] < target) {
                // 目标值在右半部分，更新左边界
                left = mid + 1;
            } else {
                // 目标值在左半部分，更新右边界
                right = mid - 1;
            }
        }
        // 没有找到目标值，返回-1
        return -1;
    }

    public int findIndex(int[][] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid][2] <= target) {
                if (mid == arr.length - 1 || arr[mid + 1][2] > target) {
                    return mid;
                } else {
                    left = mid + 1;
                }
            } else {
                right = mid - 1;
            }
        }
        System.out.println("target" + target);
        return -1; // 如果没有找到小于等于目标数的索引，返回 -1
    }

    public static void main(String[] args) {
//        int[][] arr = {1,2,3,4,6,8,10};
//        int index = findIndex(arr, 10);
//        System.out.println(index);
    }

}
