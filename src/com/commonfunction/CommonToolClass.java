package com.commonfunction;

import com.twodimension.TargetData;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author xzbz  工具类
 * @create 2023-11-21 10:42
 */
public class CommonToolClass {
    /**
     * 拷贝二维数组
     *
     * @param rectangle
     * @return
     */
    public int[][] assistArrayRec(int[][] rectangle) {
        if (rectangle.length == 0) {
            return null;
        }
        int[][] temp = new int[rectangle.length][rectangle[0].length];
        for (int i = 0; i < rectangle.length; i++) {
            for (int j = 0; j < rectangle[0].length; j++) {
                temp[i][j] = rectangle[i][j];
            }
        }
        return temp;
    }

    /**
     * 创建天际线的辅助数组
     *
     * @param rectangle
     * @return
     */
    public int[][] assistArray(int[][] rectangle) {
        int[][] temp = new int[rectangle.length][9];
        for (int i = 0; i < rectangle.length; i++) {
//            // 用于重置 初始化序列的索引
            temp[i][0] = i;
            for (int j = 1; j < 5; j++) {
                temp[i][j] = rectangle[i][j];
            }
            // 用于存储按 key 排序的下标
            temp[i][5] = i;
            // 用于存储按 宽度 排序的下标
            temp[i][6] = i;
            // 用于存储按 高度 排序的下标
            temp[i][7] = i;
            // 标记位，用于标记矩形块是否被放置
            temp[i][8] = 0;
        }
        return temp;
    }

    /**
     * 对二维数组中的指定位置排序,从小到大排序
     *
     * @param defPoints
     * @param index
     */
    public void sortedTwoDim(int[][] defPoints, int index) {
        for (int i = 0; i < defPoints.length; i++) {
            int minIndex = i;
            for (int j = i + 1; j < defPoints.length; j++) {
                if (defPoints[minIndex][index] > defPoints[j][index]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int[] temp = defPoints[minIndex];
                defPoints[minIndex] = defPoints[i];
                defPoints[i] = temp;
            }
        }
    }

    /**
     * 深拷贝初始化信息
     *
     * @param targetData
     * @return
     */
    public TargetData deepCopy(TargetData targetData) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream stream = new ObjectOutputStream(outputStream);
            stream.writeObject(targetData);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
            return (TargetData) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 靠右下侧放置。缺陷块左右位置互换
     *
     * @param targetData
     * @return
     */
    public TargetData deepCopyRightCorner(TargetData targetData) {
        TargetData deepCopy = deepCopy(targetData);
        for (int i = 0; i < targetData.defectiveBlocksSize.length; i++) {
            // 靠右侧放置
            // 缺陷块坐标集合
            deepCopy.defPoints[i][0] = targetData.oriSize[0] - targetData.defPoints[i][2];
            deepCopy.defPoints[i][1] = targetData.defPoints[i][1];
            deepCopy.defPoints[i][2] = targetData.oriSize[0] - targetData.defPoints[i][0];
            deepCopy.defPoints[i][3] = targetData.defPoints[i][3];

            // 左下角坐标
            deepCopy.defectLowerLeft[i][0] = deepCopy.defPoints[i][0];
            deepCopy.defectLowerLeft[i][1] = deepCopy.defPoints[i][1];

            // 右上角坐标
            deepCopy.defectUpperRight[i][0] = deepCopy.defPoints[i][2];
            deepCopy.defectUpperRight[i][1] = deepCopy.defPoints[i][3];
        }
        deepCopy.methodPlace = "RightCorner";
        return deepCopy;
    }

    /**
     * 靠左上侧放置。缺陷块上下位置互换
     *
     * @param targetData
     * @return
     */
    public TargetData deepCopyUpperLeftCorner(TargetData targetData, int height) {
        TargetData deepCopy = deepCopy(targetData);
        for (int i = 0; i < targetData.defectiveBlocksSize.length; i++) {
            // 靠右侧放置
            // 缺陷块坐标集合
            deepCopy.defPoints[i][0] = targetData.defPoints[i][0];
            deepCopy.defPoints[i][1] = height - targetData.defPoints[i][3];
            deepCopy.defPoints[i][2] = targetData.defPoints[i][2];
            deepCopy.defPoints[i][3] = height - targetData.defPoints[i][1];

            // 左下角坐标
            deepCopy.defectLowerLeft[i][0] = deepCopy.defPoints[i][0];
            deepCopy.defectLowerLeft[i][1] = deepCopy.defPoints[i][1];

            // 右上角坐标
            deepCopy.defectUpperRight[i][0] = deepCopy.defPoints[i][2];
            deepCopy.defectUpperRight[i][1] = deepCopy.defPoints[i][3];
        }
        deepCopy.methodPlace = "UpperLeftCorner";
        deepCopy.checkHeight = height;
        return deepCopy;
    }

    /**
     * 更新从上往下求解的高度
     * @param targetData
     * @param deepCopy
     * @return
     */
    public void improveUpperLeftCorner(TargetData targetData, TargetData deepCopy) {
        for (int i = 0; i < targetData.defectiveBlocksSize.length; i++) {
            // 靠右侧放置
            // 缺陷块坐标集合
            deepCopy.defPoints[i][0] = targetData.defPoints[i][0];
            deepCopy.defPoints[i][1] = deepCopy.checkHeight - targetData.defPoints[i][3];
            deepCopy.defPoints[i][2] = targetData.defPoints[i][2];
            deepCopy.defPoints[i][3] = deepCopy.checkHeight - targetData.defPoints[i][1];

            // 左下角坐标
            deepCopy.defectLowerLeft[i][0] = deepCopy.defPoints[i][0];
            deepCopy.defectLowerLeft[i][1] = deepCopy.defPoints[i][1];

            // 右上角坐标
            deepCopy.defectUpperRight[i][0] = deepCopy.defPoints[i][2];
            deepCopy.defectUpperRight[i][1] = deepCopy.defPoints[i][3];
        }
        deepCopy.improveFitness = new HashSet<>();
        deepCopy.addMatchingPoints(deepCopy.defPoints, deepCopy.improveFitness);

    }

    /**
     * 更新从上往下，靠右侧放置的求解的高度
     * @param targetData
     * @param deepCopy
     * @return
     */
    public void improveUpperRightCorner(TargetData targetData, TargetData deepCopy) {
        int width = targetData.oriSize[0];
        int height = deepCopy.checkHeight;

        for (int i = 0; i < targetData.defectiveBlocksSize.length; i++) {
            // 靠右侧放置
            // 缺陷块坐标集合
            deepCopy.defPoints[i][0] = width - targetData.defPoints[i][2];
            deepCopy.defPoints[i][1] = height - targetData.defPoints[i][3];
            deepCopy.defPoints[i][2] = width - targetData.defPoints[i][0];
            deepCopy.defPoints[i][3] = height - targetData.defPoints[i][1];

            // 左下角坐标
            deepCopy.defectLowerLeft[i][0] = deepCopy.defPoints[i][0];
            deepCopy.defectLowerLeft[i][1] = deepCopy.defPoints[i][1];

            // 右上角坐标
            deepCopy.defectUpperRight[i][0] = deepCopy.defPoints[i][2];
            deepCopy.defectUpperRight[i][1] = deepCopy.defPoints[i][3];
        }
        deepCopy.improveFitness = new HashSet<>();
        deepCopy.addMatchingPoints(deepCopy.defPoints, deepCopy.improveFitness);
    }

    /**
     * 靠左侧放置。缺陷块上下位置、左右位置互换
     * @param targetData
     * @return
     */
    public TargetData deepCopyUpperRightCorner(TargetData targetData, int width, int height) {
        TargetData deepCopy = deepCopy(targetData);
        for (int i = 0; i < targetData.defectiveBlocksSize.length; i++) {
            // 靠左侧放置
            // 缺陷块坐标集合
            deepCopy.defPoints[i][0] = width - targetData.defPoints[i][2];
            deepCopy.defPoints[i][1] = height - targetData.defPoints[i][3];
            deepCopy.defPoints[i][2] = width - targetData.defPoints[i][0];
            deepCopy.defPoints[i][3] = height - targetData.defPoints[i][1];

            // 左下角坐标
            deepCopy.defectLowerLeft[i][0] = deepCopy.defPoints[i][0];
            deepCopy.defectLowerLeft[i][1] = deepCopy.defPoints[i][1];

            // 右上角坐标
            deepCopy.defectUpperRight[i][0] = deepCopy.defPoints[i][2];
            deepCopy.defectUpperRight[i][1] = deepCopy.defPoints[i][3];
        }
        deepCopy.methodPlace = "UpperRightCorner";
        deepCopy.checkHeight = height;
        // 初始化加分点
        deepCopy.improveFitness = new HashSet<>();
        deepCopy.addMatchingPoints(deepCopy.defPoints, deepCopy.improveFitness);
        return deepCopy;
    }


    /**
     * 输出结果时，将靠右放置结果修改为左下放置方式
     * 修改缺陷块、目标块的位置
     *
     * @param rectangle
     * @return
     */
    public void changeLocationRightCornerDef(int[][] defBlock, int[][] rectangle, int width) {
        for (int i = 0; i < defBlock.length; i++) {
            int x1 = defBlock[i][0];
            int y1 = defBlock[i][1];
            int x2 = defBlock[i][2];
            int y2 = defBlock[i][3];
            defBlock[i][0] = width - x2;
            defBlock[i][2] = width - x1;
        }

        for (int i = 0; i < rectangle.length; i++) {
            int x1 = rectangle[i][2];
            int y1 = rectangle[i][3];
            int x2 = rectangle[i][4];
            int y2 = rectangle[i][5];
            rectangle[i][2] = width - x2;
            rectangle[i][4] = width - x1;
        }
    }

    /**
     * 输出结果时，将从上往下，靠右放置结果修改为原方向左下放置方式
     * 修改缺陷块、目标块的位置
     *
     * @param rectangle
     * @return
     */
    public void changeLocationUpperLeftCornerDef(int[][] defBlock, int[][] rectangle, int height) {
        System.out.println("-----   UpperLeftCorner 输出解的结果  ------");
        System.out.println("-----   缺陷块的求解位置  ------");
        for (int i = 0; i < defBlock.length; i++) {
            System.out.println(Arrays.toString(defBlock[i]));
        }
        System.out.println("-----   目标块的求解位置  ------");
        for (int i = 0; i < rectangle.length; i++) {
            System.out.println(Arrays.toString(rectangle[i]));
        }

        for (int i = 0; i < defBlock.length; i++) {
            int x1 = defBlock[i][0];
            int y1 = defBlock[i][1];
            int x2 = defBlock[i][2];
            int y2 = defBlock[i][3];
            defBlock[i][1] = height - y2;
            defBlock[i][3] = height - y1;
        }

        for (int i = 0; i < rectangle.length; i++) {
            int x1 = rectangle[i][2];
            int y1 = rectangle[i][3];
            int x2 = rectangle[i][4];
            int y2 = rectangle[i][5];
            rectangle[i][3] = height - y2;
            rectangle[i][5] = height - y1;
        }
    }

    /**
     * 输出结果时，将从上往下，靠右放置结果修改为原方向左下放置方式
     * 修改缺陷块、目标块的位置
     *
     * @param rectangle
     * @return
     */
    public void changeLocationUpperRightCornerDef(int[][] defBlock, int[][] rectangle, int width, int height) {
        System.out.println("-----   UpperRightCorner 输出解的结果  ------");
        System.out.println("-----   缺陷块的求解位置  ------");
        for (int i = 0; i < defBlock.length; i++) {
            System.out.println(Arrays.toString(defBlock[i]));
        }
        System.out.println("-----   目标块的求解位置  ------");
        for (int i = 0; i < rectangle.length; i++) {
            System.out.println(Arrays.toString(rectangle[i]));
        }

        for (int i = 0; i < defBlock.length; i++) {
            int x1 = defBlock[i][0];
            int y1 = defBlock[i][1];
            int x2 = defBlock[i][2];
            int y2 = defBlock[i][3];
            defBlock[i][0] = width - x2;
            defBlock[i][1] = height - y2;
            defBlock[i][2] = width - x1;
            defBlock[i][3] = height - y1;
        }

        for (int i = 0; i < rectangle.length; i++) {
            int x1 = rectangle[i][2];
            int y1 = rectangle[i][3];
            int x2 = rectangle[i][4];
            int y2 = rectangle[i][5];
            rectangle[i][2] = width - x2;
            rectangle[i][3] = height - y2;
            rectangle[i][4] = width - x1;
            rectangle[i][5] = height - y1;
        }
    }


    public void locationRestoration(TargetData targetData, int[][] rectangle) {
        int[][] defBlock = targetData.defPoints;
        int[][] defPointsBack = targetData.defPointsBack;
        int width = targetData.oriSize[0];
        int height = targetData.oriSize[1];
        String method = targetData.methodPlace;
        switch(method){
            case "RightCorner":
                changeLocationRightCornerDef(defBlock, rectangle, width);
                break;
            case "UpperLeftCorner":
                changeLocationUpperLeftCornerDef(defPointsBack, rectangle, height);
                targetData.defPoints = defPointsBack;
                break;
            case "UpperRightCorner":
                changeLocationUpperRightCornerDef(defPointsBack, rectangle, width, height);
                targetData.defPoints = defPointsBack;
                break;
            default:
                return ;
        }
    }


    /**
     * @param array
     * @param num
     * @return boolean
     * @description 检查数组中是否包含一个数
     * @author hao
     * @date 2023/3/9 8:44
     */
    public static boolean contains(int[] array, int num) {
        for (int i : array) {
            if (i == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param pointArray 任意 List集合
     * @return int[] 任意 一维数组
     * @description 将List集合转化为一维数组，并排序
     * @author hao
     * @date 2023/3/7 20:57
     */
    public static int[] listToArray(List<Integer> pointArray) {
        // 拷贝数组
        int[] tempArray = listCopyToArray(pointArray);
        // 数组排序
        sortedArray(tempArray);
        return tempArray;
    }

    /**
     * @description 对一维数组排序
     * @author hao
     * @date 2023/3/8 11:01
     */
    public static void sortedArray(int[] tempArray) {
        for (int i = 0; i < tempArray.length; i++) {
            int minIndex = i;
            for (int k = i; k < tempArray.length; k++) {
                if (tempArray[k] <= tempArray[minIndex]) {
                    minIndex = k;
                }
            }
            // 交换i和minIndex的值
            int temp = tempArray[minIndex];
            tempArray[minIndex] = tempArray[i];
            tempArray[i] = temp;
        }
    }

    /**
     * @param arrayList
     * @return int[]
     * @description array转一维数组
     * @author hao
     * @date 2023/3/8 11:01
     */
    public static int[] listCopyToArray(List<Integer> arrayList) {
        if (arrayList == null) {
            return null;
        }
        int[] tempArray = new int[arrayList.size()];
        int j = 0;
        for (int i : arrayList) {
            tempArray[j] = i;
            j++;
        }
        return tempArray;
    }
}
