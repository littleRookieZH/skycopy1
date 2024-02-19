package exactsolution.blockpreprocessing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hao
 * @description: 预处理目标块
 * 思想：根据最大可行长度提升目标块高度、宽度
 * @date 2023/7/18 23:10
 */
public class PreprocessBlock {
    /**
     * @param defPoints
     * @param block
     * @param oriSize   放置区域：得使用下界计算
     * @return int[][]
     * @description 预处理：
     * // 1、计算目标块i的高度增加量
     * // 2、计算放置区域的宽度减少量
     * // 3、增加宽度方向block的宽度
     * @author hao
     * @date 2023/7/18 23:57
     */
    public void processBlock(int minHeight, int minWidth, int[] oriSize, int[][] defPoints, int[][] block) {
        // 区域宽度方向的最大可放置值
        int maxWidthLength = 0;
        int height = oriSize[1];
        int width = oriSize[0];
        // 1、计算目标块i的高度增加量
        // 计算去除block i 之后的最大放置高度
        for (int i = 0; i < block.length; ++i) {
            boolean[] points = new boolean[height + 1];
            boolean[] tempPoints = new boolean[height + 1];
            points[0] = true;
            tempPoints[0] = true;
            int index = 0;
            // 去除block i 的高度
            int blockHeight = height - block[i][1];
            // 计算靠下放置点集
            for (int j = 0; j < block.length; ++j) {
                if (i == j) {
                    continue;
                }
                for (int k = 0; k <= blockHeight - minHeight; ++k) {
                    if (points[k]) {
                        if (k + block[j][1] <= blockHeight) {
                            tempPoints[k + block[j][1]] = true;
                            index = k + block[j][1];
                        } else {
                            break;
                        }
                    }
                }
                for (int k = 0; k <= index; ++k) {
                    if (tempPoints[k]) {
                        points[k] = true;
                    }
                }
            }
            // 计算带缺陷的点
            for (int j = 0; j < defPoints.length; ++j) {
                for (int k = 0; k <= blockHeight; ++k) {
                    if (points[k]) {
                        if (k + defPoints[j][3] <= blockHeight) {
                            points[k + defPoints[j][3]] = true;
                        } else {
                            break;
                        }
                    }
                }
            }

            // 计算i的最小可增加高度
            int minIncreaseLength = 0;
            boolean isFirst = true;
            List<Integer> heightList = new ArrayList<>();
            heightList.add(blockHeight);
            for (int j = 0; j < defPoints.length; ++j) {
                if (defPoints[j][1] >= block[i][1]) {
                    heightList.add(defPoints[j][1] - block[i][1]);
                }
            }
            for (int j = 0; j < heightList.size(); ++j) {
                for (int k = heightList.get(j); k >= 0; --k) {
                    try {
                        if (points[k]) {
                            if (isFirst) {
                                isFirst = false;
                                minIncreaseLength = heightList.get(j) - k;
                            } else {
                                minIncreaseLength = Math.min(minIncreaseLength, heightList.get(j) - k);
                            }
                            break;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            block[i][1] += minIncreaseLength;
        }

        // 2、计算放置区域的宽度减少量
        boolean[] pointsW = new boolean[width + 1];
        boolean[] tempPointsW = new boolean[width + 1];
        pointsW[0] = true;
        tempPointsW[0] = true;
        int tempWidth = 0;
        for (int i = 0; i < block.length; ++i) {
            for (int j = 0; j <= width - minWidth; ++j) {
                if (pointsW[j]) {
                    if (j + block[i][0] <= width) {
                        tempPointsW[j + block[i][0]] = true;
                        tempWidth = j + block[i][0];
                    } else {
                        break;
                    }
                }
            }
            // 将 tempPointsW 更新到 pointsW 中
            for (int j = 0; j <= tempWidth; ++j) {
                if (tempPointsW[j]) {
                    pointsW[j] = true;
                }
            }
        }
        // 考虑def的离散点
        for (int i = 0; i < defPoints.length; ++i) {
            for (int j = 0; j <= width; ++j) {
                if (pointsW[j]) {
                    if (j + defPoints[i][2] <= width) {
                        pointsW[j + defPoints[i][2]] = true;
                    } else {
                        break;
                    }
                }
            }
        }
        // 取得宽度方向的最大放置宽度
        try {
            for (int i = width; i >= 0; --i) {
                if (pointsW[i]) {
                    maxWidthLength = i;
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        width = maxWidthLength;

        // 3、增加宽度方向block的宽度
        for (int i = 0; i < block.length; ++i) {
            boolean[] pointsWidth = new boolean[width + 1];
            boolean[] tempPoints = new boolean[width + 1];
            pointsWidth[0] = true;
            tempPoints[0] = true;
            int index = 0;
            // 去除block i 的高度
            int blockWidth = width - block[i][0];
            for (int j = 0; j < block.length; ++j) {
                if (i == j) {
                    continue;
                }
                for (int k = 0; k <= blockWidth - minWidth; ++k) {
                    if (pointsWidth[k]) {
                        if (k + block[j][0] <= blockWidth) {
                            tempPoints[k + block[j][0]] = true;
                            index = k + block[j][0];
                        }
                    }
                }
                for (int k = 0; k <= index; ++k) {
                    if (tempPoints[k]) {
                        pointsWidth[k] = true;
                    }
                }
            }
            // 考虑缺陷块
            for (int j = 0; j < defPoints.length; ++j) {
                for (int k = 0; k <= blockWidth; k++) {
                    if (pointsWidth[k]) {
                        if (k + defPoints[j][2] <= blockWidth) {
                            pointsWidth[k + defPoints[j][2]] = true;
                        } else {
                            break;
                        }
                    }
                }
            }
            // 计算 i 的宽度增加量
            int minIncreaseLength = 0;
            boolean isFirst = true;
            List<Integer> widthList = new ArrayList<>();
            widthList.add(blockWidth);
            for (int j = 0; j < defPoints.length; ++j) {
                // >= ：防止出现增大宽度之后原离散点无法放置的问题
                if (defPoints[j][0] >= block[i][0]) {
                    widthList.add(defPoints[j][0] - block[i][0]);
                }
            }
            // 计算最小增加量（到离散点的距离）
            for (int j = 0; j < widthList.size(); j++) {
//                int integer = ;
                for (int k = widthList.get(j); k >= 0; --k) {
                    if (pointsWidth[k]) {
                        if (isFirst) {
                            minIncreaseLength = widthList.get(j) - k;
                            isFirst = false;
                        } else {
                            minIncreaseLength = Math.min(minIncreaseLength, widthList.get(j) - k);
                        }
                        break;
                    }
                }
            }
            block[i][0] += minIncreaseLength;
        }
    }

    /**
     * @param minHeight
     * @param minWidth
     * @param oriSize
     * @param defPoints
     * @param block
     * @description 提升高度之后不必再进行宽度方向的提升
     * @author hao
     * @date 2023/7/25 16:51
     */
    public void processBlockImprove(int minHeight, int minWidth, int[] oriSize, int[][] defPoints, int[][] block) {
        // 区域宽度方向的最大可放置值
        int maxWidthLength = 0;
        int height = oriSize[1];
        int width = oriSize[0];
        // 1、计算目标块i的高度增加量
        // 计算去除block i 之后的最大放置高度
        for (int i = 0; i < block.length; ++i) {
            boolean[] points = new boolean[height + 1];
            boolean[] tempPoints = new boolean[height + 1];
            points[0] = true;
            tempPoints[0] = true;
            int index = 0;
            // 去除block i 的高度
            int blockHeight = height - block[i][1];
            // 计算靠下放置点集
            for (int j = 0; j < block.length; ++j) {
                if (i == j) {
                    continue;
                }
                for (int k = 0; k <= blockHeight - minHeight; ++k) {
                    if (points[k]) {
                        if (k + block[j][1] <= blockHeight) {
                            tempPoints[k + block[j][1]] = true;
                            index = k + block[j][1];
                        } else {
                            break;
                        }
                    }
                }
                for (int k = 0; k <= index; ++k) {
                    if (tempPoints[k]) {
                        points[k] = true;
                    }
                }
            }
            // 计算带缺陷的点
            for (int j = 0; j < defPoints.length; ++j) {
                for (int k = 0; k <= blockHeight; ++k) {
                    if (points[k]) {
                        if (k + defPoints[j][3] <= blockHeight) {
                            points[k + defPoints[j][3]] = true;
                        } else {
                            break;
                        }
                    }
                }
            }

            // 计算i的最小可增加高度
            int minIncreaseLength = 0;
            boolean isFirst = true;
            List<Integer> heightList = new ArrayList<>();
            heightList.add(blockHeight);
            for (int j = 0; j < defPoints.length; ++j) {
                if (defPoints[j][1] >= block[i][1]) {
                    heightList.add(defPoints[j][1] - block[i][1]);
                }
            }
            for (int j = 0; j < heightList.size(); ++j) {
                for (int k = heightList.get(j); k >= 0; --k) {
                    if (points[k]) {
                        if (isFirst) {
                            isFirst = false;
                            minIncreaseLength = heightList.get(j) - k;
                        } else {
                            minIncreaseLength = Math.min(minIncreaseLength, heightList.get(j) - k);
                        }
                        break;
                    }
                }
            }
            block[i][1] += minIncreaseLength;
        }

//        // 2、计算放置区域的宽度减少量
//        boolean[] pointsW = new boolean[width + 1];
//        boolean[] tempPointsW = new boolean[width + 1];
//        pointsW[0] = true;
//        tempPointsW[0] = true;
//        int tempWidth = 0;
//        for (int i = 0; i < block.length; ++i) {
//            for (int j = 0; j <= width - minWidth; ++j) {
//                if (pointsW[j]) {
//                    if (j + block[i][0] <= width) {
//                        tempPointsW[j + block[i][0]] = true;
//                        tempWidth = j + block[i][0];
//                    } else {
//                        break;
//                    }
//                }
//            }
//            // 将 tempPointsW 更新到 pointsW 中
//            for (int j = 0; j <= tempWidth; ++j) {
//                if (tempPointsW[j]) {
//                    pointsW[j] = true;
//                }
//            }
//        }
//        // 考虑def的离散点
//        for (int i = 0; i < defPoints.length; ++i) {
//            for (int j = 0; j <= width; ++j) {
//                if (pointsW[j]) {
//                    if (j + defPoints[i][2] <= width) {
//                        pointsW[j + defPoints[i][2]] = true;
//                    } else {
//                        break;
//                    }
//                }
//            }
//        }
//        // 取得宽度方向的最大放置宽度
//        try {
//            for (int i = width; i >= 0; --i) {
//                if (pointsW[i]) {
//                    maxWidthLength = i;
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        width = maxWidthLength;
//
//        // 3、增加宽度方向block的宽度
//        for (int i = 0; i < block.length; ++i) {
//            boolean[] pointsWidth = new boolean[width + 1];
//            boolean[] tempPoints = new boolean[width + 1];
//            pointsWidth[0] = true;
//            tempPoints[0] = true;
//            int index = 0;
//            // 去除block i 的高度
//            int blockWidth = width - block[i][0];
//            for (int j = 0; j < block.length; ++j) {
//                if (i == j) {
//                    continue;
//                }
//                for (int k = 0; k <= blockWidth - minWidth; ++k) {
//                    if (pointsWidth[k]) {
//                        if (k + block[j][0] <= blockWidth) {
//                            tempPoints[k + block[j][0]] = true;
//                            index = k + block[j][0];
//                        }
//                    }
//                }
//                for (int k = 0; k <= index; ++k) {
//                    if (tempPoints[k]) {
//                        pointsWidth[k] = true;
//                    }
//                }
//            }
//            // 考虑缺陷块
//            for (int j = 0; j < defPoints.length; ++j) {
//                for (int k = 0; k <= blockWidth; k++) {
//                    if (pointsWidth[k]) {
//                        if (k + defPoints[j][2] <= blockWidth) {
//                            pointsWidth[k + defPoints[j][2]] = true;
//                        } else {
//                            break;
//                        }
//                    }
//                }
//            }
//            // 计算 i 的宽度增加量
//            int minIncreaseLength = 0;
//            boolean isFirst = true;
//            List<Integer> widthList = new ArrayList<>();
//            widthList.add(blockWidth);
//            for (int j = 0; j < defPoints.length; ++j) {
//                // >= ：防止出现增大宽度之后原离散点无法放置的问题
//                if (defPoints[j][0] >= block[i][0]) {
//                    widthList.add(defPoints[j][0] - block[i][0]);
//                }
//            }
//            // 计算最小增加量（到离散点的距离）
//            for (int j = 0; j < widthList.size(); j++) {
////                int integer = ;
//                for (int k = widthList.get(j); k >= 0; --k) {
//                    if(pointsWidth[k]){
//                        if(isFirst){
//                            minIncreaseLength = widthList.get(j) - k;
//                            isFirst = false;
//                        }else{
//                            minIncreaseLength = Math.min(minIncreaseLength, widthList.get(j) - k);
//                        }
//                        break;
//                    }
//                }
//            }
//            block[i][0] += minIncreaseLength;
    }

    public static void test01(int[][] arr) {
        arr[0][0] = 0;
    }

    public static void main(String[] args) {
        int[][] arr = {{2, 3}};
        PreprocessBlock.test01(arr);
        for (int[] a : arr) {
            System.out.println(Arrays.toString(a));
        }
    }

}
