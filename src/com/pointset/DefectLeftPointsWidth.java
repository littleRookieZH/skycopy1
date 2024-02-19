package com.pointset;

import com.commonfunction.CommonToolClass;
import com.twodimension.TargetData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hao
 * @description: 左侧带缺陷点集
 * @date 2023/3/6 20:29
 */
public class DefectLeftPointsWidth{
    /**
     * @description 引用矩形对象
     */
    private TargetData recLayOutData;
    /**
     * @description 记录宽度组合的总宽度
     */
    private int total;
    /**
     * @description 离散点集，用于添加生成的横坐标或者纵坐标
     */
    private List<Integer> pointArray = new ArrayList<>();
    /**
     * @description 求最大可放置长度
    */
    public int maxLength;

    public int getMaxLength() {
        return maxLength;
    }

    public List<Integer> getPointArray() {
        return pointArray;
    }


    public void setRecLayOutData(TargetData recLayOutData) {
        this.recLayOutData = recLayOutData;
    }

    /**
     * @param index 索引决定是宽度还是高度  0是宽，1是高
     * @description 求宽或高的最大可放置长度；min(w-wi)
     * @author hao
     * @date 2023/3/6 20:55
     */
    public void getMaxLength(int index) {
        int[][] targetBlockSize = recLayOutData.targetBlockSize;
        int minValue = recLayOutData.targetBlockSize[0][index];
        for (int[] ints : targetBlockSize) {
            minValue = Math.min(ints[index], minValue);
        }
        this.maxLength = recLayOutData.oriSize[index] - minValue;
//        System.out.println("最大可放置长度为：" + maxLength);
    }

    /**
     * @param index 索引决定是宽度还是高度  0是宽，1是高
     * @return List<List < Integer>>
     * @description 根据每个目标块的个数，得到n组待组合的数据
     * @author hao
     * @date 2023/3/7 11:00
     */
    public List<List<Integer>> getCombination(int index) {
        //用于存放不同种类的数据集合
        List<List<Integer>> dataCombinations = new ArrayList<>();
        //每一个种类造一个对象，存放一组数据
        int i = 0;
        for (int k : recLayOutData.tarMaxNum) {
            List<Integer> tempList = new ArrayList<>();
            //比较目标块实际可并排放置的数量 与 提供的数据量
            k = Math.min(k, (recLayOutData.oriSize[index] / recLayOutData.targetBlockSize[i][index]));
            for (int j = 0; j < k + 1; j++) {
                tempList.add(j);
            }
            i++;
            dataCombinations.add(tempList);
        }
        return dataCombinations;
    }


    /**
     * @param dataCombinations n组待组合的数据
     * @param group            组别
     * @param index            索引决定是宽度还是高度  0是宽，1是高
     * @param combinationArray 存放目标块序列
     * @description 生成一个无缺陷块状态的离散点集  在使用之前需要清理一下array数组
     * @author hao
     * @date 2023/3/7 20:53
     */
    public void noDefLeftPoints(List<List<Integer>> dataCombinations, int group, int[] combinationArray, int index) {
        //得到一个不同类型排列的组合
        if (group == dataCombinations.size()) {
            int j = 0;
            total = 0;
            for (int i : combinationArray) {
                //矩形组合的长度
                total = total + i * recLayOutData.targetBlockSize[j][index];
                j++;
            }
            // 保证 长度 小于等于 最大有效值，结果不能重复
            if (total <= maxLength && !pointArray.contains(total)) {
                pointArray.add(total);
            }
            return;
        }
        //递归生成组合数组
        for (int num : dataCombinations.get(group)) {
            if (total < recLayOutData.oriSize[index]) {
                combinationArray[group] = num;
                noDefLeftPoints(dataCombinations, group + 1, combinationArray, index);
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
    public int[] getDeLeftPoints(int[][] defectBlockCoordinates, List<List<Integer>> dataCombinations, int group, int[] combinationArray, int index) {
        // 得到无缺陷点集
        noDefLeftPoints(dataCombinations, group, combinationArray, index);
        // 对无缺陷点集结果排序
        int[] noDefectLeftPoints = CommonToolClass.listToArray(pointArray);
        int total = 0;
        for (int[] ints : defectBlockCoordinates) {
            for (int noDefectPoint : noDefectLeftPoints) {
                //求和
                total = ints[index] + noDefectPoint;
                //判断：total是否小于矩形的宽度、是否不存在
                if (total <= maxLength && !pointArray.contains(total)) {
                    pointArray.add(total);
                }
            }
        }
        // 对最终结果排序
        return CommonToolClass.listToArray(pointArray);
    }


}
