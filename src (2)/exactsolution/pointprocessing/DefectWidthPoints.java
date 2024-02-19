package exactsolution.pointprocessing;

import com.universalalgorithm.OneDimQuickSort;

import java.util.ArrayList;
import java.util.List;

public class DefectWidthPoints {
    public DefPointData getWidthPoints(int Width, int minBlockWidth, int[][] arr, int[][] defPoints, DefPointData defPointData ) {
        // normalPatternW：无缺陷点集
        boolean[][] normalPatternW = new boolean[arr.length][Width + 1];
        int[] leftW = new int[Width + 1];
        int[] rightW = new int[Width + 1];
        // RNPatternW：记录放置的宽度点集
        boolean[] RNPatternW = new boolean[Width + 1];
        for (int i = 0; i < arr.length; i++) {
            boolean[] CW = new boolean[Width];
            boolean[] indexW = new boolean[Width];
            normalPatternW[i][0] = true;
            CW[0] = true;
            indexW[0] = true;
            int indexw = 0;
            for (int k = 0; k < arr.length; k++) {
                if (k != i) {
                    // CW[j]表示当前位置是否放置
                    for (int j = 0; j <= Width - arr[i][0]; j++) {
                        if (CW[j]) {
                            if (j + arr[k][0] <= Width - arr[i][0]) {
                                indexw = j + arr[k][0];
                                indexW[indexw] = true;
                            }
                        }
                    }
                    for (int t = 0; t <= indexw; t++) {
                        // 表示目标块i的位置
                        if (indexW[t]) {
                            // 左侧位置
                            leftW[t] = 1;
                            // 右侧位置
                            rightW[Width - arr[i][0] - t] = 1;
                            CW[t] = true;
                            RNPatternW[t] = true;
                            // 表示i可以放置的点集 比如 [1][0]  [1][4] 
                            normalPatternW[i][t] = true;
                        }
                    }
                }
            }
        }
        // 考虑缺陷块
        for (int i = 0; i < defPoints.length; i++) {
            for (int j = 0; j < Width; j++) {
                if (RNPatternW[j]) {
                    // 以x2为起点，靠左放置点
                    if (j + defPoints[i][2] <= Width - minBlockWidth) {
                        leftW[j + defPoints[i][2]] = 1;
                    }
                    // j ！= 0 的原因：靠右放置的起点不是缺陷块的x1（以左侧点记录），而是 max{x1 - wi}
                    if (j != 0) {
                        // 以x1为起点，靠右放置点
                        if (defPoints[i][0] - j >= 0) {
                            rightW[defPoints[i][0] - j] = 1;
                        }
                    }
                }
            }
        }
        //增量计算: 通过累加的方式确定当前点的左侧有多少个离散点
        for (int i = 1; i <= Width; i++) {
            leftW[i] = leftW[i] + leftW[i - 1];
            rightW[Width - i] = rightW[Width - i] + rightW[Width - i + 1];
        }
        //确定使放置点最小的t值
        int tW = 1;
        int min1 = leftW[0] + rightW[1];
        for (int i = 2; i <= Width; i++) {
            if (leftW[i - 1] + rightW[i] < min1) {
                min1 = leftW[i - 1] + rightW[i];
                tW = i;
            }
        }

        boolean[] isCombineW = new boolean[Width + 1];
        // 靠左放置点
        boolean[][] leftModelW = new boolean[arr.length][Width + 1];
        // 靠右放置点
        boolean[][] rightModelW = new boolean[arr.length][Width + 1];
        // 记录所有点的放置位置（true：表示可以放，false：表示不可放）
        boolean[][] mimW = new boolean[arr.length][Width + 1];
        isCombineW[0] = true;
        //计算MIM可放置点
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j <= Width; j++) {
                if (normalPatternW[i][j]) {
                    // 判断左右是根据可放置点在tW的左右
                    if (j < tW) {
                        leftModelW[i][j] = true;
                        mimW[i][j] = true;
                        isCombineW[j] = true;
                    }
                    if (Width - arr[i][0] - j >= tW) {
                        rightModelW[i][Width - arr[i][0] - j] = true;
                        mimW[i][Width - arr[i][0] - j] = true;
                        isCombineW[Width - arr[i][0] - j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < defPoints.length; i++) {
            for (int t = 0; t < arr.length; t++) {
                for (int j = 0; j <= Width; j++) {
                    if (normalPatternW[t][j]) {
                        if (j + defPoints[i][2] < tW) {
                            leftModelW[t][j + defPoints[i][2]] = true;
                            mimW[t][j + defPoints[i][2]] = true;
                            isCombineW[j + defPoints[i][2]] = true;
                        }
                        if (defPoints[i][0] - arr[t][0] - j >= tW) {
                            rightModelW[t][defPoints[i][0] - arr[t][0] - j] = true;
                            mimW[t][defPoints[i][0] - arr[t][0] - j] = true;
                            isCombineW[defPoints[i][0] - arr[t][0] - j] = true;
                        }
                    }
                }
            }
        }

        List<Integer> useFulL = new ArrayList<>();  //把可放置的点存入链表中
        for (int j = 0; j <= Width - minBlockWidth; j++) {
            if (isCombineW[j]) {
                useFulL.add(j);
            }
        }
        int[] widthPoints = new int[useFulL.size()];
        for (int i = 0; i < widthPoints.length; ++i) {
            widthPoints[i] = useFulL.get(i);
        }
        OneDimQuickSort.quickSort(widthPoints);
        defPointData.widthInfo(tW, widthPoints, leftModelW, rightModelW, mimW);
        return defPointData;
    }

    /**
     * @description 预处理可放置点
     * 目的：增大某些位置目标块的宽度可以保证计算1CBP添加宽度约束时可以更加精确。
     */
    public boolean[][] preprocessWidthPoints(int Width, int[][] defPoints, int[][] blocks, DefPointData defPointData) {
        boolean[][] pointsWithWidth = defPointData.widthPlacedPoints;
        boolean[][] leftModelW = defPointData.leftModelW;
        boolean[][] rightModelW = defPointData.rightModelW;
        // 初始化wip
        // 表示放在j位置的目标块i的宽度
        int[][] wip = new int[blocks.length][Width + 1];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j <= Width; j++) {
                if (pointsWithWidth[i][j]) {
                    wip[i][j] = blocks[i][0];
                }
            }
        }
        // 预处理二
        // wip:表示当前目标块i在离散点p的宽度大小
        // 主要针对分界线附近的目标块以及缺陷块x1、x2附近的目标块
        for (int i = 0; i < blocks.length; i++) {
            for (int r = 0; r < Width; r++) {
                if (leftModelW[i][r]) {
                    boolean index = false;
                    for (int j = r + blocks[i][0]; j <= Width; j++) {
                        for (int d = 0; d < defPoints.length; d++) {
                            if (j == defPoints[d][0]) {
                                index = true;
                                wip[i][r] = j - r;
                                break;
                            }
                        }
                        if (index) {
                            break;
                        }
                        for (int t = 0; t < blocks.length; t++) {
                            if (pointsWithWidth[t][j]) {
                                wip[i][r] = j - r;
                                index = true;
                                break;
                            }
                        }
                        if (index) {
                            break;
                        }
                        if (j == Width) {
                            wip[i][r] = Width - r;
                            break;
                        }
                    }
                }
                if (rightModelW[i][r]) {
                    boolean index = false;
                    int q = 0;
                    for (int j = r; j >= 0; j--) {
                        for (int d = 0; d < defPoints.length; d++) {
                            if (j == defPoints[d][2]) {
                                q = j;
                                index = true;
                                break;
                            }
                        }
                        if (index) {
                            break;
                        }
                        // 判断在j左移过程中是否有可能是 t可放置的离散点位置
                        for (int t = 0; t < blocks.length; t++) {
                            if (j - blocks[t][0] >= 0) {
                                if (pointsWithWidth[t][j - blocks[t][0]]) {
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
                    // i的可放置位置调整，向左扩大
                    // 主要是分界线位置的和缺陷块位置的
                    pointsWithWidth[i][r] = false;
                    pointsWithWidth[i][q] = true;
                    wip[i][q] = r + blocks[i][0] - q;
                }
            }
            // 处理的是左侧离散点，右侧不会出现（wip[i][q] + q= r + instance.items[i].w = 定值;）
            // 因为左侧目标块i的宽度会增加，如果目标块在两个点（x11 != x21）增大宽度后相等（x12 == x22），
            // 说明目标块会出现覆盖情况。此时要避免
            for (int j = 0; j <= Width; j++) {
                if (pointsWithWidth[i][j]) {
                    for (int k = j + 1; k <= Width; k++) {
                        if (pointsWithWidth[i][k]) {
                            if (k + wip[i][k] <= j + wip[i][j]) {
                                pointsWithWidth[i][j] = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return pointsWithWidth;
    }

    public static void main(String[] args) {
//        DefectWidthPoints defectWidthPoints = new DefectWidthPoints();
//        int[][] arr = new int[40][2];
//        Random random = new Random();
//        for (int i = 0; i < arr.length; i++) {
//            arr[i][0] = random.nextInt(30) + 1;
//            arr[i][1] = 1;
//        }
//        System.out.println(Arrays.toString(arr));
//        int maxLength = 200;
//        long start = System.currentTimeMillis();
//        int minSize = DefPointData.minSize(arr, 0);
//        //  int[][] defPoints, int minBlockWidth
//        defectWidthPoints.getWidthPoints(arr, maxLength, new int[][]{{1, 1, 1, 1}}, minSize);
//        long end = System.currentTimeMillis();
//        System.out.println("时间花销：    " + (start - end));
    }
}
