package com.pointset;

/**
 * @author hao
 * @description:
 * @date 2023/3/23 10:04
 */
public class DefectBlockSize{

    /**
     * @description 每一行缺陷块的总长度
     */
    public int[] defectWidth;

    /**
     * @description 用来表示预处理之后的缺陷块位置 x1 y1 x2 y2
     */
    public int[][] defectSize;


    /**
     * @description 每一列缺陷块的总长度
     */
    public int[] defectHeight;

    // 缺陷块对应的目标块
//    public List<List<Integer>> defList;

//    /**
//     * @description: 求解缺陷块每一列的高度
//     * @author hao
//     * @date 2023/3/23 10:06
//     */
//    public void defectColumnHeight(int[] widthPoints, int[][] defPoints) {
//        int[] arr = new int[widthPoints.length];
//        for (int i = 0; i < widthPoints.length; i++) {
//            for (int j = 0; j < defPoints.length; j++) {
//                // xd2 > x && xd1 <= x
//                if (defPoints[j][2] > widthPoints[i] && defPoints[j][0] <= widthPoints[i]) {
//                    arr[i] += defPoints[j][3] - defPoints[j][1];
//                }
//            }
//        }
//        defectHeight = arr;
//    }


    /**
     * @description: 求解缺陷块每一行的宽度
     * // 考虑的是缺陷块是标准矩形，不存在相互覆盖的情况
     * @author hao
     * @date 2023/3/23 10:06
     */
    public void defectColumnWidth(int[] heightPoints, int[][] defPoints) {
        //缺陷块的种类
        int defectNumber = defPoints.length;
        //temp的每一个元素对应一个缺陷块的yi1、yi2、wi
        int[][] tempWidth = new int[defectNumber][3];
        for (int i = 0; i < defectNumber; i++) {
            //yi1
            tempWidth[i][0] = defPoints[i][1];
            //yi2
            tempWidth[i][1] = defPoints[i][3];
            //wi
            tempWidth[i][2] = defPoints[i][2] - defPoints[i][0];
        }
        // 以y1 递增排序，y1相同 以y2递增排序
        int[][] sortHeight = sortArray(tempWidth);
        //
        try {
            defectWidth = getTotalWidth(sortHeight, heightPoints);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param tempArray
     * @description 对二维数组进行按照宽度排序
     * @author hao
     * @date 2023/3/23 10:42
     */
    public int[][] sortArray(int[][] tempArray) {
        for (int i = 0; i < tempArray.length; i++) {
            int minIndex = i;
            for (int j = i; j < tempArray.length; j++) {
                if (tempArray[j][0] < tempArray[minIndex][0]) {
                    minIndex = j;
                }
                if (tempArray[j][0] == tempArray[minIndex][0]) {
                    if (tempArray[j][1] < tempArray[minIndex][1]) {
                        minIndex = j;
                    }
                }
            }
            if (minIndex != i) {
                //交换i和minIndex的值
                int[] temp = tempArray[minIndex];
                tempArray[minIndex] = tempArray[i];
                tempArray[i] = temp;
            }
        }
        return tempArray;
    }

    /**
     * @param sortArray    缺陷块的y1 y2 w 这里的缺陷块尺寸应该是预处理之后的缺陷块
     * @param heightPoints 高度离散点
     * @return int[]
     * @description
     * @author hao
     * @date 2023/7/6 16:50
     */
    public int[] getTotalWidth(int[][] sortArray, int[] heightPoints) {
        int[] arr = new int[heightPoints.length];
        // 求每一行，缺陷块的宽度和
        for (int i = 0; i < heightPoints.length; i++) {
            //根据缺陷块位置确定 每一列 的高度
            for (int j = 0; j < sortArray.length; j++) {
                int q = heightPoints[i];
                // yi1 > q
                if (sortArray[j][0] > q) {
                    continue;
                }
                // yi1 <= q < yi2
                if (sortArray[j][0] <= q && sortArray[j][1] > q) {
                    arr[i] += sortArray[j][2];
                }
                // yi2 < q
                if (q > sortArray[j][1]) {
                    break;
                }
            }
        }
        return arr;
    }

//    /**
//     * @return int[]
//     * @description 根据缺陷块的位置信息求出哪些目标块不可以被放置
//     * @author hao
//     * @date 2023/3/28 19:53
//     */
//    public void placementWidth(int[][] blockSize, int width, int[][] defPoints) {
//        //创建辅助数组
//        defList = new ArrayList<>();
//        int max;
//        for (int i = 0; i < defPoints.length; i++) {
//            List<Integer> tempList1 = new ArrayList<>();
//            //比较xd1 和 w - xd2
//            max = Math.max(defPoints[i][0], width - (defPoints[i][2]));
//            for (int j = 0; j < blockSize.length; j++) {
//                //目标块的宽度 大于 最大可行宽度
//                if (blockSize[j][0] > max) {
//                    tempList1.add(j);
//                }
//            }
//            defList.add(tempList1);
//        }
//    }

}