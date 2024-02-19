package exactsolution.dualccm1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DualInitial {

    /**
     * @description
     * 类的属性：
     * 原料板的尺寸、目标块种类的数量、目标块尺寸、目标块的价值、目标块的最大数量
     * 缺陷块数量、缺陷块尺寸、缺陷点左下角坐标、缺陷点右上角坐标
     */
    /**
     * @description 原料板的尺寸
     */
    public int[] plateSize;
    /**
     * @description 目标块、缺陷块的数量
     */
    public int targetBlockNum;
    /**
     * @description 目标块尺寸, 需要初始化   w  h
     */
    public int[][] targetBlockSize;
    /**
     * @description 每个目标块种类的数量
     */
    public int[] targetBlockNumber;
    /**
     * @description 缺陷块数量
     */
    public int defectiveBlocksNumber;
    /**
     * @description 缺陷块尺寸：长、宽
     */
    public int[][] defectiveBlocksSize;
    /**
     * @description 缺陷块坐标 (x1,y1)  (x2,y2)
     */
    public int[][] defectPoints;
    /**
     * @description 缺陷点右上角坐标
     */
//    public int[][] defectUpperRight;
    /**
     * @description 记录离散点集合的总宽度
     */
    private int total;
    /**
     * @description 记录离散点集
     */
    private List<Integer> pointArray = new ArrayList<>();

    int maxSize;

    public DualInitial(int[] plateSize, int[][] targetBlockSize, int[] targetBlockNumber, int targetBlockNum) {
        this.plateSize = plateSize;
        this.targetBlockSize = targetBlockSize;
        this.targetBlockNumber = targetBlockNumber;
        this.targetBlockNum = targetBlockNum;
    }

    public DualInitial() {
    }

    /**
     * @param path：矩形块文件路径
     * @description 初始化数据
     * 离散点集如何确定 1、与原料板的宽高有关  2、与减点有关
     * @author hao
     * @date 2023/3/6 20:18
     */
    public void initData(String path) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader(new FileReader(path)));
        //先读取一行
        line = cin.nextLine();
        line = line.trim();
        //以空格为标志进行拆分  得到String类型的原板料宽、高
        substr = line.split("\\s+");
        plateSize = new int[]{Integer.parseInt(substr[0]), Integer.parseInt(substr[1])};
        substr = cin.nextLine().split("\\s+");
        //目标块种类个数
        int kinds = Integer.parseInt(substr[0]);

        //初始化  所有数组在使用的时候都要初始化
        //目标块尺寸
        targetBlockSize = new int[kinds][2];
        //每种目标块数量
        targetBlockNumber = new int[kinds];
        //读取目标块数据
        for (int i = 0; i < kinds; i++) {
            line = cin.nextLine();
            line = line.trim();
            substr = line.split("\\s+");
            //目标块宽、高
            targetBlockSize[i][0] = Integer.parseInt(substr[0]);
            targetBlockSize[i][1] = Integer.parseInt(substr[1]);
            //每种目标块数量
            targetBlockNumber[i] = Integer.parseInt(substr[3]);
            targetBlockNum += targetBlockNumber[i];
        }
        //读取缺陷块
        substr = cin.nextLine().split("\\s+");
        //初始化缺陷块
        defectiveBlocksNumber = Integer.parseInt(substr[0]);
        targetBlockNum += defectiveBlocksNumber;
        defectPoints = new int[defectiveBlocksNumber][4];
        defectiveBlocksSize = new int[defectiveBlocksNumber][2];
        for (int i = 0; i < defectiveBlocksNumber; i++) {
            substr = cin.nextLine().split("\\s+");
            //左下角坐标
            defectPoints[i][0] = Integer.parseInt(substr[0]);
            defectPoints[i][1] = Integer.parseInt(substr[1]);
            //右上角坐标
            defectPoints[i][2] = Integer.parseInt(substr[2]);
            defectPoints[i][3] = Integer.parseInt(substr[3]);
            //缺陷块的宽、高
            defectiveBlocksSize[i][0] = defectPoints[i][2] - defectPoints[i][0];
            defectiveBlocksSize[i][1] = defectPoints[i][3] - defectPoints[i][1];
        }
    }

//    public int[][] getTargetSize(int[][] targetBlockSize, int[][] defectiveBlocksSize ,int[] plateSize) {
//        // 借助生成离散点的方法，获取最大值
//
//    }

    /**
     * @description  先根据缺陷块的位置，预处理（检查是否有交集，如果有合并交集部分）
     * @author  hao 
     * @date    2023/6/29 17:18
     * @param
     
    */
    public void checkDef(int[][] defectPoints){
        // 处理X轴
        // 以x1排序
        sortArray(defectPoints, 0);
        for (int i = 0; i < defectPoints.length; i++) {
            int index = i;
            for (int j = i; j < defectPoints.length; j++) {
                // xi2 > xj1 说明有交集
                if (defectPoints[i][2] > defectPoints[j][0]) {
                    index = j;
                }
            }
            // 找到所有会覆盖i的缺陷块

        }
    }

    public void noDefLeftPoints(AitParameter aitParameter) {
        //得到一个不同类型排列的组合
        if (aitParameter.group == aitParameter.dataCombinations.size()) {
            int j = 0;
            total = 0;
            for (int i : aitParameter.combinationArray) {
                //矩形组合的长度
                total = total + i * targetBlockSize[j][aitParameter.index];
                j++;
            }
            // 保证 长度 小于等于 最大有效值，结果不能重复
            if (total <= aitParameter.maxLength && !pointArray.contains(total)) {
                pointArray.add(total);
                // 记录最大值
                maxSize = total;
            }
            return;
        }
        //递归生成组合数组
        for (int num : aitParameter.dataCombinations.get(aitParameter.group)) {
            if (total < plateSize[aitParameter.index]) {
                aitParameter.combinationArray[aitParameter.group] = num;
                noDefLeftPoints(aitParameter);
            }
            //表示在上一次循环中，求得的total已经大于等于总宽；此时同组目标块个数不必再递增
            if (total >= plateSize[aitParameter.index]) {
                total = 0;
                return;
            }
        }
    }

    static class AitParameter {
        List<List<Integer>> dataCombinations;
        int group;
        int[] combinationArray;
        int index;
        // 这里传入 缺陷块分割的长度
        int maxLength;

        public AitParameter(List<List<Integer>> dataCombinations, int group, int[] combinationArray, int index, int maxLength) {
            this.dataCombinations = dataCombinations;
            this.group = group;
            this.combinationArray = combinationArray;
            this.index = index;
            this.maxLength = maxLength;
        }
    }

    public static void sortArray(int[][] defArray,int index) {
        if (defArray.length == 0) {
            return;
        }
        for (int i = 0; i < defArray.length; i++) {
            int minIndex = i;
            for (int j = i; j < defArray.length; j++) {
                if (defArray[j][index] < defArray[minIndex][index]) {
                    minIndex = j;
                }
            }
            // 交换
            if (i != minIndex) {
                swapArray(defArray, i, minIndex);
            }
        }
    }

    public static void swapArray(int[][] defArray, int i, int j) {
        int[] temp = defArray[i];
        defArray[i] = defArray[j];
        defArray[j] = temp;
    }


    public static void main(String[] args) {
        int[][] arr = {{3, 2}, {2,3}, {1,3}, {3,4}};
        sortArray(arr,0);
        for(int[] a : arr){
            System.out.println(Arrays.toString(a));
        }
    }
}
