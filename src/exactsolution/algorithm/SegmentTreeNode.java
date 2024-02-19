package exactsolution.algorithm;

public class SegmentTreeNode {
        int start, end;
        int maxVal;
        SegmentTreeNode left, right;

        public SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
            this.maxVal = Integer.MIN_VALUE;
            this.left = null;
            this.right = null;
        }


    public static void main(String[] args) {
        int[][] array = {{2,2,2,10,4},{3,2,1,30,10},{0,2,4,10,2},{1,2,1,20,4}};
//        RangeMaxQuery rmq = new RangeMaxQuery(array,"width");
//
////      查询区间 [1, 4] 原序列索引的最小值
//        int minVal = rmq.queryMin(0, 2);
//        System.out.println("Min value in range [0, 2]: " + minVal);
//        int maxVal = rmq.queryMax(0, 2);
//        System.out.println("Max value in range [0, 2]: " + maxVal);
//        rmq.update(2,0,0);
//        rmq.updateMaxVal(2,5);
//        System.out.println("11111111111111111");
//        minVal = rmq.queryMin(0, 2);
//        System.out.println("Min value in range [0, 2]: " + minVal);
//        maxVal = rmq.queryMax(0, 2);
//        System.out.println("Max value in range [0, 2]: " + maxVal);
//        // 更新位置 2 的值为 7
////        rmq.update(1, 0);
//        System.out.println(11);
        // 再次查询区间 [1, 4] 的最小值
//        minVal = rmq.query(1, 3);
//        System.out.println("Min value in range [1, 4] after update: " + minVal);
    }
}

