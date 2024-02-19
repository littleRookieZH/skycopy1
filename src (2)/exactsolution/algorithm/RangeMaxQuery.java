package exactsolution.algorithm;

public class RangeMaxQuery {
    private final SegmentTreeNode root;

    public RangeMaxQuery(int[][] nums, int index) {
            // 一小一大
            root = buildSegmentTree(nums, 0, nums.length - 1,index);
   
    }

    /**
     * @description  生成高度序列线段树，搜索区间最大值
     * @author  hao
     * @date    2023/5/27 23:11
     * @param nums
     * @param start
     * @param end
     * @param index
     * @return SegmentTreeNode
    */
    private SegmentTreeNode buildSegmentTree(int[][] nums, int start, int end, int index) {
        if (start > end) {
            return null;
        }
        SegmentTreeNode node = new SegmentTreeNode(start, end);
        if (start == end) {
            node.maxVal = nums[start][index];
        } else {
            int mid = (start + end) / 2;
            node.left = buildSegmentTree(nums, start, mid,index );
            node.right = buildSegmentTree(nums, mid + 1, end,index );
            node.maxVal = Math.max(node.left.maxVal, node.right.maxVal);
        }
        return node;
    }

    /**
     * @description  查找区间高度最大
     * @author  hao
     * @date    2023/5/28 10:53
     * @param start
     * @param end
     * @return int
    */
    public int  queryMax(int start, int end) {
        return queryRegionMax(root, start, end);
    }

    /**
     * @description 区间最大值
     * @author  hao
     * @date    2023/5/28 10:54
     * @param node
     * @param start
     * @param end
     * @return int
    */
    private int queryRegionMax(SegmentTreeNode node, int start, int end) {
        if (node.start == start && node.end == end) {
            // 搜索[min,max]之间高度的最大值
            return node.maxVal;
        }
        int mid = (node.start + node.end) / 2;
        if (end <= mid) {
            return queryRegionMax(node.left, start, end);
        } else if (start > mid) {
            return queryRegionMax(node.right, start, end);
        } else {
            return Math.max(queryRegionMax(node.left, start, mid), queryRegionMax(node.right, mid + 1, end));
        }
    }
}
