package exactsolution.pointprocessing;


import com.universalalgorithm.OneDimQuickSort;

import java.util.ArrayList;
import java.util.List;

public class DefectHeightPoints {

    public DefPointData getHeightPoints(int Height, int minBlockHeight, int[][] arr, int[][] defPoints, DefPointData defPointData) {

        // 计算分界线
        // normalPatternL：无缺陷点集
        boolean[][] normalPatternL = new boolean[arr.length][Height + 1];
        int[] leftL = new int[Height + 1];
        int[] rightL = new int[Height + 1];
        // RNPatternL：记录放置的高度点集
        boolean[] RNPatternL = new boolean[Height + 1];
        for (int i = 0; i < arr.length; i++) {
            boolean[] CL = new boolean[Height + 1];
            boolean[] indexL = new boolean[Height + 1];
            CL[0] = true;
            indexL[0] = true;
            int indexl = 0;
            for (int k = 0; k < arr.length; k++) {
                if (k != i) {
                    for (int j = 0; j <= Height - arr[i][1]; j++) {
                        if (CL[j]) {
                            if (j + arr[k][1] <= Height - arr[i][1]) {
                                indexl = j + arr[k][1];
                                indexL[indexl] = true;
                            }
                        }
                    }
                    for (int t = 0; t <= indexl; t++) {
                        // 表示目标块i的位置
                        if (indexL[t]) {
                            leftL[t] = 1;
                            rightL[Height - arr[i][1] - t] = 1;
                            RNPatternL[t] = true;
                            CL[t] = true;
                            normalPatternL[i][t] = true;
                        }
                    }
                }
            }
        }
        // 考虑缺陷块
        for (int i = 0; i < defPoints.length; i++) {
            for (int j = 0; j < Height; j++) {
                if (RNPatternL[j]) {
                    // 以y2为起点，靠左放置点
                    if (j + defPoints[i][3] <= Height - minBlockHeight) {
                        leftL[j + defPoints[i][3]] = 1;
                    }
                    if (j != 0) {
                        if (defPoints[i][1] - j >= 0) {
                            rightL[defPoints[i][1] - j] = 1;
                        }
                    }
                }
            }
        }

        //增量计算: 通过累加的方式确定当前点的左侧有多少个离散点
        for (int i = 1; i <= Height; i++) {
            leftL[i] = leftL[i] + leftL[i - 1];
            rightL[Height - i] = rightL[Height - i] + rightL[Height - i + 1];
        }
        //确定使放置点最小的t值
        int tL = 1;
        int min = leftL[0] + rightL[1];
        for (int i = 2; i <= Height; i++) {
            if (leftL[i - 1] + rightL[i] < min) {
                min = leftL[i - 1] + rightL[i];
                tL = i;
            }
        }
        // 将bool转为点集
        boolean[] isCombineL = new boolean[Height + 1];
        // 下侧可放置点
        boolean[][] lowerModelH = new boolean[arr.length][Height + 1];
        // 上侧可放置点
        boolean[][] upperModelH = new boolean[arr.length][Height + 1];
        boolean[][] mimH = new boolean[arr.length][Height + 1];
        isCombineL[0] = true;
        //计算MIM可放置点
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j <= Height; j++) {
                if (normalPatternL[i][j]) {
                    // 判断上下是根据可放置点在tL的上下
                    if (j < tL) {
                        // 靠下侧放置
                        isCombineL[j] = true;
                        lowerModelH[i][j] = true;
                        mimH[i][j] = true;
                    }
                    if (Height - arr[i][1] - j >= tL) {
                        // 靠上侧放置
                        isCombineL[Height - arr[i][1] - j] = true;
                        upperModelH[i][Height - arr[i][1] - j] = true;
                        mimH[i][Height - arr[i][1] - j] = true;
                    }
                }
            }
        }

        for (int i = 0; i < defPoints.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                for (int k = 0; k <= Height; k++) {
                    if (normalPatternL[j][k]) {
                        if (k + defPoints[i][3] < tL && ((k + defPoints[i][3] + arr[j][1]) <= Height)) {
                            isCombineL[k + defPoints[i][3]] = true;
                            lowerModelH[j][k + defPoints[i][3]] = true;
                            mimH[j][k + defPoints[i][3]] = true;
                        }
                        if (defPoints[i][1] - k >= tL + arr[j][1]) {
                            isCombineL[defPoints[i][1] - k - arr[j][1]] = true;
                            upperModelH[j][defPoints[i][1] - k - arr[j][1]] = true;
                            mimH[j][defPoints[i][1] - k - arr[j][1]] = true;
                        }
                    }
                }
            }
        }

        //把可放置的点存入链表中
        List<Integer> useFulL = new ArrayList<>();
        for (int j = 0; j <= Height - minBlockHeight; j++) {
            if (isCombineL[j]) {
                useFulL.add(j);
            }
        }

        int[] heightPoints = new int[useFulL.size()];
        for (int i = 0; i < heightPoints.length; ++i) {
            heightPoints[i] = useFulL.get(i);
        }
        OneDimQuickSort.quickSort(heightPoints);
        defPointData.heightInfo(tL, heightPoints, lowerModelH, upperModelH, mimH);
        return defPointData;
    }

    /**
     * @description 预处理可放置点
     * 目的：增大某些位置目标块的高度可以保证计算1CBP添加高度约束时可以更加精确。
     * 其次是在计算约束缺陷块左右两侧目标块时可以减少约束变量
     */
    public boolean[][] preprocessHeightPoints(int Height, int[][] defPoints, int[][] blocks, DefPointData defPointData) {
        boolean[][] pointsWithHeight = defPointData.heightPlacedPoints;
        boolean[][] lowerModelH = defPointData.lowerModelH;
        boolean[][] upperModelH = defPointData.upperModelH;
        // 初始化hip
        // 表示放在j位置的目标块i的高度
        int[][] hip = new int[blocks.length][Height + 1];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j <= Height; j++) {
                if (pointsWithHeight[i][j]) {
                    hip[i][j] = blocks[i][1];
                }
            }
        }
        // 预处理二
        // hip:表示当前目标块i在离散点p的高度大小
        // 主要针对分界线附近的目标块以及缺陷块y1、y2附近的目标块
        for (int i = 0; i < blocks.length; i++) {
            for (int r = 0; r < Height; r++) {
                if (lowerModelH[i][r]) {
                    boolean index = false;
                    for (int j = r + blocks[i][1]; j <= Height; j++) {
                        // 与缺陷块的y1重合
                        for (int d = 0; d < defPoints.length; d++) {
                            if (j == defPoints[d][1]) {
                                index = true;
                                hip[i][r] = j - r;
                                break;
                            }
                        }
                        if (index) {
                            break;
                        }
                        // 与离散点重合
                        for (int t = 0; t < blocks.length; t++) {
                            if (pointsWithHeight[t][j]) {
                                hip[i][r] = j - r;
                                index = true;
                                break;
                            }
                        }
                        if (index) {
                            break;
                        }
                        if (j == Height) {
                            hip[i][r] = Height - r;
                            break;
                        }
                    }
                }
                if (upperModelH[i][r]) {
                    boolean index = false;
                    int q = 0;
                    for (int j = r; j >= 0; j--) {
                        // 与缺陷块的y2重合
                        for (int d = 0; d < defPoints.length; d++) {
                            if (j == defPoints[d][3]) {
                                q = j;
                                index = true;
                                break;
                            }
                        }
                        if (index) {
                            break;
                        }
                        // 与离散点重合
                        for (int t = 0; t < blocks.length; t++) {
                            if (j - blocks[t][1] >= 0) {
                                if (pointsWithHeight[t][j - blocks[t][1]]) {
                                    q = j;
                                    index = true;
                                    break;
                                }
                            }
                        }
                        if (index) {
                            break;
                        }
                    }
                    // i的可放置位置调整
                    // 主要是分界线位置的和缺陷块位置的
                    pointsWithHeight[i][r] = false;
                    pointsWithHeight[i][q] = true;
                    hip[i][q] = r + blocks[i][1] - q;
                }
            }
            // 因为目标块i的高度会增加，如果目标块在两个点（y11 != y21）增大高度后相等（y12 == y22），
            // 说明目标块会出现覆盖情况。此时要避免
            for (int j = 0; j <= Height; j++) {
                if (pointsWithHeight[i][j]) {
                    for (int k = j + 1; k <= Height; k++) {
                        if (pointsWithHeight[i][k]) {
                            if (k + hip[i][k] <= j + hip[i][j]) {
                                pointsWithHeight[i][j] = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return pointsWithHeight;
    }

    public static int[] noDefHeightPoints(int[][] arr, int Height, int minBlockHeight) {
        // 计算高度可放置点
        boolean[][] normalPatternL = new boolean[arr.length][Height + 1];
        int[] leftL = new int[Height + 1];
        int[] rightL = new int[Height + 1];
        boolean[] RNPatternL = new boolean[Height + 1];
        for (int i = 0; i < arr.length; i++) {
            boolean[] CL = new boolean[Height + 1];
            boolean[] indexL = new boolean[Height + 1];
            CL[0] = true;
            indexL[0] = true;
            int indexl = 0;
            for (int k = 0; k < arr.length; k++) {
                if (k != i) {
                    for (int j = 0; j <= Height - arr[i][1]; j++) {
                        if (CL[j]) {
                            if (j + arr[k][1] <= Height - arr[i][1]) {
                                indexL[j + arr[k][1]] = true;
                                indexl = j + arr[k][1];
                            }
                        }
                    }
                    for (int t = 0; t <= indexl; t++) {
                        if (indexL[t]) {
                            leftL[t] = 1;
                            rightL[Height - arr[i][1] - t] = 1;
                            RNPatternL[t] = true;
                            CL[t] = true;
                            normalPatternL[i][t] = true;
                        }
                    }
                }
            }
        }

        //增量计算
        for (int i = 1; i <= Height; i++) {
            leftL[i] = leftL[i] + leftL[i - 1];
            rightL[Height - i] = rightL[Height - i] + rightL[Height - i + 1];
        }
        //确定使放置点最小的t值
        int tL = 1;
        int min = leftL[0] + rightL[1];
        for (int i = 2; i <= Height; i++) {
            if (leftL[i - 1] + rightL[i] < min) {
                min = leftL[i - 1] + rightL[i];
                tL = i;
            }
        }

        System.out.println("tL  " + tL);
        // 将bool转为点集
        boolean[] isCombineL = new boolean[Height + 1];
        isCombineL[0] = true;
        //计算MIM可放置点
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j <= Height; j++) {
                if (normalPatternL[i][j]) {
                    if (j < tL) {
                        // 靠下侧放置
                        isCombineL[j] = true;
                    }
                    if (Height - arr[i][1] - j >= tL) {
                        // 靠上侧放置
                        isCombineL[Height - arr[i][1] - j] = true;
                    }
                }
            }
        }
        List<Integer> useFulL = new ArrayList<>();  //把可放置的点存入链表中
        for (int j = 0; j <= Height - minBlockHeight; j++) {
            if (isCombineL[j]) {
                useFulL.add(j);
            }
        }
        int[] tempArray = new int[useFulL.size()];
        for (int i = 0; i < tempArray.length; ++i) {
            tempArray[i] = useFulL.get(i);
        }
        OneDimQuickSort.quickSort(tempArray);
        return tempArray;
    }
}
