package com.universalalgorithm;


import com.twodimensiondata.TargetAitInfo;

public class RangeMinimumQuery {
    private final SegmentTreeNode root;

    public int getRootMinVal(){
        return root.minVal;
    }

    public int getRootWidth(){
        return root.value1;
    }

    public SegmentTreeNode getRoot() {
        return root;
    }


    public RangeMinimumQuery(int[][] nums, int index, String keyword, boolean isWidth) {
        MaxHeapSort maxHeapSort = new MaxHeapSort();
        int[] sortingMethod = maxHeapSort.switchSortingMethod(keyword);
        if (!isWidth) {
            // 一小一大
            root = buildSegmentTreeHeight(nums, 0, nums.length - 1, index, maxHeapSort.getSortIndex(sortingMethod));
        } else {
            // 两小
            root = buildSegmentTreeWidth(nums, 0, nums.length - 1, index, maxHeapSort.getSortIndex(sortingMethod), 3);
        }
    }

    /**
     * @description  生成高度序列线段树，一个是索引 搜索区间最小值，一个是高度 搜索区间最大值
     * @author  hao
     * @date    2023/5/27 23:11
     * @param nums
     * @param start
     * @param end
     * @param index
     * @param height
     * @return SegmentTreeNode
    */
    private SegmentTreeNode buildSegmentTreeHeight(int[][] nums, int start, int end, int index,int height) {
        if (start > end) {
            return null;
        }
        SegmentTreeNode node = new SegmentTreeNode(start, end);
        if (start == end) {
            node.minVal = nums[start][index];
            node.maxVal = nums[start][height];
        } else {
            int mid = (start + end) / 2;
            node.left = buildSegmentTreeHeight(nums, start, mid,index,height);
            node.right = buildSegmentTreeHeight(nums, mid + 1, end,index,height);
            node.minVal = Math.min(node.left.minVal, node.right.minVal);
            node.maxVal = Math.max(node.left.maxVal, node.right.maxVal);
        }
        return node;
    }

    /**
     * @description  生成宽度序列线段树，一个是索引 搜索区间最小值，一个是宽度 搜索区间最小值,一个高度
     * @author  hao
     * @date    2023/5/27 23:13
     * @param nums
     * @param start
     * @param end
     * @param index
     * @param width
     * @return SegmentTreeNode
    */
    private SegmentTreeNode buildSegmentTreeWidth(int[][] nums, int start, int end, int index,int width,int height) {
        if (start > end) {
            return null;
        }
        SegmentTreeNode node = new SegmentTreeNode(start, end);
        if (start == end) {
            // 其中min对应width的索引 value1对应width
            // 索引 --- 指的是初始序列还没有经过排序的索引，rec[0]
            node.minVal = nums[start][index];
            // 宽度
            node.value1 = nums[start][width];
            // 高度
            node.value2 = nums[start][height];
        } else {
            int mid = (start + end) / 2;
            node.left = buildSegmentTreeWidth(nums, start, mid,index,width,height);
            node.right = buildSegmentTreeWidth(nums, mid + 1, end,index,width,height);
            node.minVal = Math.min(node.left.minVal, node.right.minVal);
            node.value1 = Math.min(node.left.value1, node.right.value1);
            node.value2 = Math.max(node.left.value2, node.right.value2);
        }
        return node;
    }
    /**
     * @description 更新宽度线段树的方法，
     * 对于width，对应的索引和宽度都更新最大值,高度设置为最小
     * @author  hao
     * @date    2023/5/10 22:08
     * @param index
     */
    public void update(int index, int minValue, int value1,int value2) {
        updateWidthTree(root, index, minValue, value1,value2);
    }

    // 将三个数都更新，宽度、索引都是搜索最小值，高度搜索最大值
    private void updateWidthTree(SegmentTreeNode node, int index, int minValue,int value1,int value2) {
        if (node.start == node.end) {
            node.minVal = minValue;
            node.value1 = value1;
            node.value2 = value2;
            return;
        }
        int mid = (node.start + node.end) / 2;
        if (index <= mid) {
            updateWidthTree(node.left, index,minValue,value1,value2);
        } else {
            updateWidthTree(node.right, index,minValue,value1,value2);
        }
        node.minVal = Math.min(node.left.minVal, node.right.minVal);
        node.value1 = Math.min(node.left.value1, node.right.value1);
        node.value2 = Math.max(node.left.value2, node.right.value2);
    }
    /**
     * @description  对于height ，高度更新为最小、索引更新为最大
     * @author  hao
     * @date    2023/5/27 23:14
     */
    public void updateVal(int index, int minValue, int maxValue) {
        updateHeightTree(root, index, minValue, maxValue);
    }
    /**
     * @description 更新线段树的具体方法
     * @author  hao
     * @date    2023/5/10 22:08
     * @param node
     * @param index
     */
    private void updateHeightTree(SegmentTreeNode node, int index, int minValue, int maxValue) {
        if (node.start == node.end) {
            // 原索引
            node.minVal = minValue;
            // 高度
            node.maxVal = maxValue;
            return;
        }
        int mid = (node.start + node.end) / 2;
        if (index <= mid) {
            updateHeightTree(node.left, index,minValue,maxValue);
        } else {
            updateHeightTree(node.right, index,minValue,maxValue);
        }
        node.minVal = Math.min(node.left.minVal, node.right.minVal);
        node.maxVal = Math.max(node.left.maxVal, node.right.maxVal);
    }
    /**
     * @description  查找索引最小值的方法
     * @author  hao
     * @date    2023/5/10 22:10
     * @param start
     * @param end
     * @return int
     */
    public int queryMin(int start, int end) {
        return querySegmentTreeMin(root, start, end);
    }

    /**
     * @description 宽度序列查找区间高度最大
     * @author  hao
     * @date    2023/5/27 23:15
     * @param start
     * @param end
     * @return int
    */
    public synchronized int queryHeightMax(TargetAitInfo aitInfo, SegmentTreeNode node, int start, int end) {
        if(root == null){
            System.out.println(555);
        }
        if(start == 1 && end == 1997){
            System.out.println(44);
        }
        if(node == null){
            System.out.println(222);
        }
        try {
            if (node.start == start && node.end == end) {
                // 搜索[min,max]之间高度的最大值
                return node.value2;
            }
        } catch (Exception e) {
            System.out.println(aitInfo.getName());
        }
        int mid = (node.start + node.end) / 2;
        if (end <= mid) {
            try {
                return queryHeightMax(aitInfo,node.left, start, end);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (start > mid) {
            return queryHeightMax(aitInfo,node.right, start, end);
        } else {
            return Math.max(queryHeightMax(aitInfo,node.left, start, mid), queryHeightMax(aitInfo, node.right, mid + 1, end));
        }
    }
    /**
     * @description  高度序列查找区间高度最大
     * @author  hao
     * @date    2023/5/28 10:53
     * @param start
     * @param end
     * @return int
    */
    public int queryHeightMax1(int start, int end) {
        return queryRegionHeightMax1(root, start, end);
    }
    /**
     * @description  查找区间最小索引值的具体方法
     * @author  hao
     * @date    2023/5/10 22:11
     * @param node
     * @param start
     * @param end
     * @return int
     */
    private int querySegmentTreeMin(SegmentTreeNode node, int start, int end) {
        if (node.start == start && node.end == end) {
            return node.minVal;
        }
        int mid = (node.start + node.end) / 2;
        if (end <= mid) {
            return querySegmentTreeMin(node.left, start, end);
        } else if (start > mid) {
            return querySegmentTreeMin(node.right, start, end);
        } else {
            return Math.min(querySegmentTreeMin(node.left, start, mid), querySegmentTreeMin(node.right, mid + 1, end));
        }
    }

    /**
     * @description  查找区间最大高度
     * @author  hao
     * @date    2023/5/27 23:15
     * @param node
     * @param start
     * @param end
     * @return int
    */
    private synchronized int queryRegionHeightMax(TargetAitInfo aitInfo, SegmentTreeNode node, int start, int end) {
        if(node == null){
            System.out.println(222);
        }
        try {
            if (node.start == start && node.end == end) {
                // 搜索[min,max]之间高度的最大值
                return node.value2;
            }
        } catch (Exception e) {
            System.out.println(aitInfo.getName());
        }
        int mid = 0;
        if (end <= mid) {
            return queryRegionHeightMax(aitInfo,node.left, start, end);
        } else if (start > mid) {
            return queryRegionHeightMax(aitInfo,node.right, start, end);
        } else {
            return Math.max(queryRegionHeightMax(aitInfo,node.left, start, mid), queryRegionHeightMax(aitInfo, node.right, mid + 1, end));
        }
    }
    /**
     * @description 高度序列的最大值
     * @author  hao
     * @date    2023/5/28 10:54
     * @param node
     * @param start
     * @param end
     * @return int
    */
    private int queryRegionHeightMax1(SegmentTreeNode node, int start, int end) {
        if (node.start == start && node.end == end) {
            // 搜索[min,max]之间高度的最大值
            return node.maxVal;
        }
        int mid = (node.start + node.end) / 2;
        if (end <= mid) {
            return queryRegionHeightMax1(node.left, start, end);
        } else if (start > mid) {
            return queryRegionHeightMax1(node.right, start, end);
        } else {
            return Math.max(queryRegionHeightMax1(node.left, start, mid), queryRegionHeightMax1(node.right, mid + 1, end));
        }
    }
}
