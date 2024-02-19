package com.pointset;




import com.commonfunction.CommonToolClass;
import com.twodimension.TargetData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hao
 * @description: 右侧带缺陷点集
 * @date 2023/3/8 8:38
 */
public class DefectRightPointsWidth{
    /**
     * @description 引用矩形对象
     */
    private TargetData recLayOutData;
    /**
     * @description 离散点集，用于添加生成的横坐标或者纵坐标
     */
    private List<Integer> pointArray = new ArrayList<>();

    private int total;

    public List<Integer> getPointArray() {
        return pointArray;
    }

    public void setRecLayOutData(TargetData recLayOutData) {
        this.recLayOutData = recLayOutData;
    }

//    /**
//     * @param noDefLeftPoints 左侧无缺陷点集
//     * @param index           索引决定是宽度还是高度  0是宽，1是高
//     * @return int[]
//     * @description 生成一个右侧或者上侧无缺陷点集
//     *  x(左侧放置点) + y（右侧放置点） + wi = W0
//     * @author hao
//     * @date 2023/3/8 8:50
//     */
//    public void noDefRightPoints(int[] noDefLeftPoints, int index) {
//        //总宽/总高
//        int widthLength = recLayOutData.oriSize[index];
//        for (int i = 0; i < noDefLeftPoints.length; i++) {
//            //将无缺陷点集加入到pointArray中
//            pointArray.add(widthLength - noDefLeftPoints[i]);
//        }
//    }
//
//
//    /**
//     * @param defectBlockCoor 缺陷块左下坐标或者右上坐标
//     * @param index           索引决定是宽度还是高度  0是宽，1是高
//     * @description 生成一个右侧或者上侧有缺陷点集
//     * @author hao
//     * @date 2023/3/8 20:25
//     */
//    public void getDefRightPoints(int[][] defectBlockCoor, int index) {
//        //对无缺陷点集进行排序
//        int[] tempArray = ToolClass.listToArray(pointArray);
//        int total = 0;
//        //得到最小宽度
//        int minWidth = getMinLength(index);
//        for (int[] coor : defectBlockCoor) {
//            //pointArray：存放的是无缺陷右侧离散点集
//            for (int noDefectPoint : tempArray) {
//                total = coor[index] - noDefectPoint;
//                //判断：total是否大于矩形最小宽度、是否不存在
//                if (total >= minWidth && !pointArray.contains(total)) {
//                    pointArray.add(total);
//                }
//            }
//        }
//    }
//
//    /**
//     * @description 查找最小矩形宽度
//     * @author hao
//     * @date 2023/3/8 9:31
//     */
//    public int getMinLength(int index) {
//        int[][] tempArray = recLayOutData.targetBlockSize;
//        int temptVar = tempArray[0][index];
//        for (int[] arr : tempArray) {
//            temptVar = Math.min(temptVar, arr[index]);
//        }
//        return temptVar;
//    }

    /**
     * @param dataCombinations n组待组合的数据
     * @param group            组别
     * @param index            索引决定是宽度还是高度  0是宽，1是高
     * @param combinationArray 存放目标块序列
     * @description 生成一个无缺陷块状态的离散点集  在使用之前需要清理一下array数组
     * @author hao
     * @date 2023/3/7 20:53
     */
    public void noDefRightPoints(List<List<Integer>> dataCombinations, int group, int[] combinationArray, int index) {
        //得到一个不同类型排列的组合
        if (group == dataCombinations.size()) {
            int j = 0;
            total = 0;

            for (int i : combinationArray) {
                //矩形组合的长度
                total = total + i * recLayOutData.targetBlockSize[j][index];
                if(total > recLayOutData.oriSize[index]){
                    break;
                }
                j++;
            }
            // 保证 长度 小于等于 最大有效值，结果不能重复
            int var1 = recLayOutData.oriSize[index] - total;
            if (total > 0 && total <=  recLayOutData.oriSize[index] && !pointArray.contains(var1)) {
                // 右侧离散点： W\H　－　求和wi
                pointArray.add(var1);
            }
            return;
        }
        //递归生成组合数组
        for (int num : dataCombinations.get(group)) {
            if (total < recLayOutData.oriSize[index]) {
                combinationArray[group] = num;
                noDefRightPoints(dataCombinations, group + 1, combinationArray, index);
            }
            //表示在上一次循环中，求得的total已经大于等于总宽；此时同组目标块个数不必再递增
            if (total >= recLayOutData.oriSize[index]) {
                total = 0;
                return;
            }
        }
    }

    /**
     * @param defectBlockCoordinates 缺陷块的左下或者右上坐标
     * @param index                  索引决定是宽度还是高度  0是宽，1是高
     * @description 生成一个有缺陷块状态的离散点集
     * @author hao
     * @date 2023/3/9 17:01
     */
    public int[] getDefRightPoints(int[][] defectBlockCoordinates, List<List<Integer>> dataCombinations, int group, int[] combinationArray, int index) {
        // 无缺陷点集
        noDefRightPoints(dataCombinations, group, combinationArray, index);
        // 对无缺陷点集结果排序
        int[] noDefectRightPoints = CommonToolClass.listToArray(pointArray);
        int total = 0;
        for (int[] ints : defectBlockCoordinates) {
            for (int noDefectPoint : noDefectRightPoints) {
                // x1 - 求和结果
                total = ints[index] - noDefectPoint;
                //判断：total是否小于矩形的宽度、是否不存在
                if (total >= 0 && !pointArray.contains(total)) {
                    pointArray.add(total);
                }
            }
        }
        // 对最终结果排序
        return CommonToolClass.listToArray(pointArray);
    }
}