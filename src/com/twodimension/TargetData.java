package com.twodimension;

import com.commonfunction.CommonToolClass;
import com.twodimensiondata.TargetAitInfo;
import com.universalalgorithm.MaxHeapSort;
import com.universalalgorithm.RangeMinimumQuery;

import java.io.*;
import java.lang.annotation.Target;
import java.util.*;

/**
 * @author zh15178381496
 * @create 2022-10 15:35
 * @说明：
 * @总结：
 */
public class TargetData implements Serializable {
    /**
     * @description 原料板尺寸，只有宽度和高度
     */
    public int[] oriSize;
    /**
     * @description 目标块种类数量
     */
    public int targetNum;
    /**
     * @description 目标块数量
     */
    public int targetNumber;
    /**
     * @description 目标块尺寸，需要初始化
     * 初始索引、数量、宽度、高度、keyvalue
     */
    public int[][] targetSize;
    /**
     * @description 目标块的价值
     */
    int[] targetArea;
    /**
     * @description 目标块的最大数量
     */
    public int[] tarMaxNum;
    /**
     * @description 缺陷块的数量
     */
    public int defNum;
    /**
     * @description 缺陷块的位置
     */
    public int[][] defPoints;
    /**
     * @description 矩形块的总面积
     */
    int totalArea;

    // 靠左放置的缺陷块，相当于启发式左下放置规则
    /**
     * @description 缺陷块尺寸：长、宽
     */
    public int[][] defectiveBlocksSize;
    /**
     * @description 缺陷块左下角坐标
     */
    public int[][] defectLowerLeft;
    /**
     * @description 缺陷点右上角坐标
     */
    public int[][] defectUpperRight;

    // 靠右放置（将缺陷块的位置左右互换）的缺陷块，相当于启发式右下放置规则
    /**
     * @description 缺陷块尺寸：长、宽
     */
    public int[][] defectiveBlocksSizeRightCorner;
    /**
     * @description 缺陷块左下角坐标
     */
    public int[][] defectLowerLeftRightCorner;
    /**
     * @description 缺陷点右上角坐标
     */
    public int[][] defectUpperRightRightCorner;

    // 从上往下，靠左放置（将缺陷块的位置上下互换）的缺陷块
    /**
     * @description 缺陷块尺寸：长、宽
     */
    public int[][] defectiveBlocksSizeTB;
    /**
     * @description 缺陷块左下角坐标
     */
    public int[][] defectLowerLeftTB;
    /**
     * @description 缺陷点右上角坐标
     */
    public int[][] defectUpperRightTB;

    // 从上往下，靠右放置（将缺陷块的位置上下互换）的缺陷块
    /**
     * @description 缺陷块尺寸：长、宽
     */
    public int[][] defectiveBlocksSizeTBRight;
    /**
     * @description 缺陷块左下角坐标
     */
    public int[][] defectLowerLeftTBRight;
    /**
     * @description 缺陷点右上角坐标
     */
    public int[][] defectUpperRightTBRight;

    /**
     * @description 目标块尺寸, 需要初始化   w  h 数量，这里的物品对应的索引是接下来y方向解集中的索引
     */
    public int[][] targetBlockSize;

    // 文件路径
    public String fileName;
    // 文件名
    public String dicName;
    /**
     * 表示哪一种方式放置
     */
    public String methodPlace;

    /**
     * 用于从上往下检查最优解
     */
    public int checkHeight;

    public int[][] defPointsBack;

    // 对数据集分类
    public HashMap<Integer, ArrayList<Integer>> itemType;

    // 记录缺陷块的四条边对应的线段
    public HashMap<String, List<int[]>> defBoundLines;

    public HashSet<String> improveFitness;

    public TargetData() {

    }

    /**
     * @description 初始化数据
     * @author hao
     * @date 2023/3/5 19:06
     */
    public void initData(String path) throws FileNotFoundException {
        // 对物品进行分类
        itemType = new HashMap<>();
        fileName = path;
        String line = null;
        String[] substr = null;
        dicName = new File(path).getName().split("\\.")[0];


        Scanner cin = new Scanner(new BufferedReader((new FileReader(path))));
        //先读一行
        line = cin.nextLine();
        line = line.trim();
        //以空格为标志进行拆分，得到String类型的原版料宽和高
        substr = line.split("\\s+");
        oriSize = new int[]{Integer.parseInt(substr[0]), Integer.parseInt(substr[1])};
        substr = cin.nextLine().split("\\s+");
        //目标块数量
        targetNum = Integer.parseInt(substr[0]);
        //初始化数组
        //目标块尺寸
        targetSize = new int[targetNum][5];
        //目标块价值
        targetArea = new int[targetNum];
        tarMaxNum = new int[targetNum];
        targetBlockSize = new int[targetNum][4];

        //读取目标块数据
        for (int i = 0; i < targetNum; i++) {
            line = cin.nextLine();
            substr = line.trim().split("\\s+");
            //每个矩形的初始索引
            targetSize[i][0] = i;
            //数量
            try {
                targetSize[i][1] = Integer.parseInt(substr[3]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //宽度
            targetSize[i][2] = Integer.parseInt(substr[0]);
            //高度
            targetSize[i][3] = Integer.parseInt(substr[1]);
            // key值
            int keyValue = (targetSize[i][3]) * (oriSize[0] + 1) + targetSize[i][2];
            targetSize[i][4] = keyValue;

            //目标块宽、高、数量、key
            targetBlockSize[i][0] = Integer.parseInt(substr[0]);
            targetBlockSize[i][1] = Integer.parseInt(substr[1]);
            targetBlockSize[i][2] = Integer.parseInt(substr[3]);
            targetBlockSize[i][3] = keyValue;

            // 矩形块的面积
            targetArea[i] = targetSize[i][2] * targetSize[i][3];
            //最大数量
            tarMaxNum[i] = Integer.parseInt(substr[3]);

        }
        //读取缺陷块尺寸
        substr = cin.nextLine().trim().split("\\s+");
        defNum = Integer.parseInt(substr[0]);
        defPoints = new int[defNum][4];

        defectLowerLeft = new int[defNum][2];
        defectUpperRight = new int[defNum][2];
        defectiveBlocksSize = new int[defNum][2];
        for (int i = 0; i < defNum; i++) {
            substr = cin.nextLine().trim().split("\\s+");

           // // 从上往下放置，靠右排布
           // // 缺陷块坐标集合
           //  defPoints[i][0] = Integer.parseInt(substr[0]);
           //  defPoints[i][1] = 57 - Integer.parseInt(substr[3]);
           //  defPoints[i][2] = Integer.parseInt(substr[2]);
           //  defPoints[i][3] = 57 - Integer.parseInt(substr[1]);
           // //左下角坐标
           //  defectLowerLeft[i][0] = defPoints[i][0];
           //  defectLowerLeft[i][1] = defPoints[i][1];
           //
           // //右上角坐标
           //  defectUpperRight[i][0] = defPoints[i][2];
           //  defectUpperRight[i][1] = defPoints[i][3];



            // 靠左侧放置
            // 缺陷块坐标集合
            defPoints[i][0] = Integer.parseInt(substr[0]);
            defPoints[i][1] = Integer.parseInt(substr[1]);
            defPoints[i][2] = Integer.parseInt(substr[2]);
            defPoints[i][3] = Integer.parseInt(substr[3]);

            // 左下角坐标
            defectLowerLeft[i][0] = Integer.parseInt(substr[0]);
            defectLowerLeft[i][1] = Integer.parseInt(substr[1]);

            //右上角坐标
            defectUpperRight[i][0] = Integer.parseInt(substr[2]);
            defectUpperRight[i][1] = Integer.parseInt(substr[3]);

            //缺陷块的宽、高
            defectiveBlocksSize[i][0] = defectUpperRight[i][0] - defectLowerLeft[i][0];
            defectiveBlocksSize[i][1] = defectUpperRight[i][1] - defectLowerLeft[i][1];
        }
        //输出矩形块个数
        targetNumber = 0;
        for (int j = 0; j < targetNum; j++) {
            targetNumber += targetSize[j][1];
        }

        methodPlace = "LeftCorner";
        // targetBlockSize 排序
        sortRule(targetBlockSize);

        for (int i = 0; i < targetBlockSize.length; i++) {
            // 根据key值，对物品分类
            if (!itemType.containsKey(targetBlockSize[i][3])) {
                ArrayList<Integer> listIndex = new ArrayList<>();
                listIndex.add(i);
                itemType.put(targetBlockSize[i][3], listIndex);
            } else {
                itemType.get(targetBlockSize[i][3]).add(i);
            }
            // System.out.println("索引 " + i + "  " + Arrays.toString(targetBlockSize[i]));
        }
        // System.out.println(UpperLeftCorner);

        // 记录缺陷块的边，匹配点
        if(defNum != 0){
            defBoundLines = new HashMap<>();
            improveFitness = new HashSet<>();
        }
        initLines(defPoints, defBoundLines);
        addMatchingPoints(defPoints, improveFitness);
    }

    /**
     * @param sortResult
     * @description 对排序规则进行升序排序
     * @author hao
     * @date 2023/5/3 21:58
     */
    public void sortRule(int[][] sortResult) {
        for (int i = 0; i < sortResult.length; i++) {
            int max = sortResult[i][0];
            int maxIndex = i;
            for (int j = i + 1; j < sortResult.length; j++) {
                int tempPer = sortResult[j][0];
                if (max < tempPer || (max == tempPer && sortResult[i][1] < sortResult[j][1])) {
                    //重置max和maxIndex
                    max = tempPer;
                    maxIndex = j;
                }
            }
            //交换索引值为maxIndex和i的矩形
            if (maxIndex != i) {
                int[] a = sortResult[maxIndex];
                sortResult[maxIndex] = sortResult[i];
                sortResult[i] = a;
            }
        }
    }

    public double getUsingRatio(int height) {
        //求矩形块的总面积
        this.totalArea = 0;
        int i = 0;
        for (int area1 : targetArea) {
            this.totalArea += area1 * this.tarMaxNum[i++];
        }
        int defArea = 0;
        for (int j = 0; j < defPoints.length; j++) {
            defArea += (defPoints[j][2] - defPoints[j][0]) * (defPoints[j][3] - defPoints[j][1]);
        }
        int plateArea = oriSize[0] * height;
        double ratio = totalArea / (double) (plateArea - defArea);
        return ratio;
    }

    /**
     * @param rectangle
     * @return int[][]
     * @description 二维数组拷贝
     * @author hao
     * @date 2023/5/12 22:46
     */
    public int[][] assistArrayRec(int[][] rectangle) {
        int[][] temp = new int[rectangle.length][];
        for (int i = 0; i < rectangle.length; i++) {
            int[] row = new int[rectangle[i].length];
            System.arraycopy(rectangle[i], 0, row, 0, rectangle[i].length);
            temp[i] = row;
        }
        return temp;
    }


    /**
     * @param rectangle 随机交换的一个矩形序列 或者是 四种排序规则生成的一个序列
     * @description
     * @author hao
     * @date 2023/5/11 17:07
     */
    public synchronized void initOrders(int[][] rectangle, TargetAitInfo aitInfo) {
        MaxHeapSort maxHeapSort = new MaxHeapSort();
        CommonToolClass commonToolClass = new CommonToolClass();
        // 拷贝一个int[][8] 并重置 初始序列 的索引
        int[][] assistArray = commonToolClass.assistArray(rectangle);
        // 初始化目标块标记：如果目标块覆盖缺陷块，标记为1，否则为0
        aitInfo.aitWidthArray = new int[assistArray.length];
        aitInfo.aitHeightArray = new int[assistArray.length];
        // 使用堆排序 key 的索引是4     生成辅助序列 keyRectangles
        // 按 key 的值进行排序，以递增排序，如果 key 相同，以  索引  递增排序
        maxHeapSort.heapSortWithIndex(assistArray, "key");
        // 按宽度、索引 进行排序   宽度 的索引是2
        maxHeapSort.heapSortWithIndex(assistArray, "width");
        // 按高度、宽度 进行排序   高度 的索引是3
        maxHeapSort.heapSortWithWidth(assistArray, "height");

        // 确定高度辅助序列，拷贝一份
        aitInfo.heightRectangles = assistArrayRec(assistArray);
        // 创建高度序列对应的原序列下标，创建线段树
        aitInfo.heightIndexNodeTree = new RangeMinimumQuery(aitInfo.heightRectangles, 0, "height", false);

        // 确定key辅助序列，拷贝一份
        maxHeapSort.heapSortWithIndex(assistArray, "key");
        aitInfo.keyRectangles = assistArrayRec(assistArray);

        // 确定width辅助序列
        maxHeapSort.heapSortWithIndex(assistArray, "width");
        aitInfo.widthRectangles = assistArray;

        // 创建宽度序列对应的宽度，创建线段树
        aitInfo.widthNodeTree = new RangeMinimumQuery(aitInfo.widthRectangles, 0, "width", true);
        // 确定原索引的辅助序列，拷贝一份
        aitInfo.indexRectangles = maxHeapSort.heapSortOriIndex(assistArray);
    }

    // 数据处理用的
    /**
     * @description 初始化数据
     * @author hao
     * @date 2023/3/5 19:06
     */
    public void initDataProcessing(String path) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(path))));
        cin.nextLine();// 空行
        cin.nextLine();// 时间
        String[] split = cin.nextLine().trim().split("\\s+"); // 目标块数量
        targetNum = Integer.parseInt(split[0]);

        substr = cin.nextLine().trim().split("\\s+"); // 原版料宽和高
        oriSize = new int[]{Integer.parseInt(substr[0]), Integer.parseInt(substr[1])};

        //读取缺陷块尺寸
        substr = cin.nextLine().trim().split("\\s+");
        defNum = Integer.parseInt(substr[0]);
        defPoints = new int[defNum][4];

        defectLowerLeft = new int[defNum][2];
        defectUpperRight = new int[defNum][2];
        defectiveBlocksSize = new int[defNum][2];
        for (int i = 0; i < defNum; i++) {
            substr = cin.nextLine().trim().split("\\s+");

            //左下角坐标
            defectLowerLeft[i][0] = Integer.parseInt(substr[1]);
            defectLowerLeft[i][1] = Integer.parseInt(substr[2]);
            //右上角坐标
            defectUpperRight[i][0] = Integer.parseInt(substr[3]);
            defectUpperRight[i][1] = Integer.parseInt(substr[4]);

            // 缺陷块坐标集合
            defPoints[i][0] = Integer.parseInt(substr[1]);
            defPoints[i][1] = Integer.parseInt(substr[2]);
            defPoints[i][2] = Integer.parseInt(substr[3]);
            defPoints[i][3] = Integer.parseInt(substr[4]);

            //缺陷块的宽、高
            defectiveBlocksSize[i][0] = defectUpperRight[i][0] - defectLowerLeft[i][0];
            defectiveBlocksSize[i][1] = defectUpperRight[i][1] - defectLowerLeft[i][1];
        }
        substr = cin.nextLine().split("\\s+"); // 利用率

        //初始化数组
        //目标块尺寸
        targetSize = new int[targetNum][5];
        //目标块价值
        targetArea = new int[targetNum];
        tarMaxNum = new int[targetNum];
        targetBlockSize = new int[targetNum][3];

        //读取目标块数据
        for (int i = 0; i < targetNum; i++) {
            line = cin.nextLine();
            substr = line.trim().split("\\s+");
            // 每个矩形的初始索引
            targetSize[i][0] = i;
            // 数量
            targetSize[i][1] = 1;
            // 宽度
            targetSize[i][2] = Integer.parseInt(substr[1]);
            // 高度
            targetSize[i][3] = Integer.parseInt(substr[2]);
            // key值
            int keyValue = (targetSize[i][3]) * (oriSize[0] + 1) + targetSize[i][2];
            targetSize[i][4] = keyValue;

            //目标块宽、高、数量
            targetBlockSize[i][0] = Integer.parseInt(substr[1]);
            targetBlockSize[i][1] = Integer.parseInt(substr[2]);
            targetBlockSize[i][2] = 1;

            // 矩形块的面积
            targetArea[i] = targetSize[i][1] * targetSize[i][2];
            //最大数量
            tarMaxNum[i] = 1;
        }
        targetNumber = 0;
        for (int j = 0; j < targetNum; j++) {
            targetNumber += targetSize[j][1];
        }
    }

    public void initLines(int[][] defPoints, HashMap<String, List<int[]>> defBoundLines){
        for (int i = 0; i < defPoints.length; i++) {
            String s1 = "v-" + defPoints[i][0];
            String s2 = "v-" + defPoints[i][2];
            int[] line1 = {defPoints[i][1], defPoints[i][3]};
            String s3 = "l-" + defPoints[i][1];
            String s4 = "l-" + defPoints[i][3];
            int[] line2 = {defPoints[i][0], defPoints[i][2]};
            if(!defBoundLines.containsKey(s1)){
                List<int[]> list = new ArrayList<>();
                list.add(line1);
                defBoundLines.put(s1, list);
            }else{
                defBoundLines.get(s1).add(line1);
            }

            if(!defBoundLines.containsKey(s2)){
                List<int[]> list = new ArrayList<>();
                list.add(line1);
                defBoundLines.put(s2, list);
            }else{
                defBoundLines.get(s2).add(line1);
            }

            if(!defBoundLines.containsKey(s3)){
                List<int[]> list = new ArrayList<>();
                list.add(line2);
                defBoundLines.put(s3, list);
            }else{
                defBoundLines.get(s3).add(line2);
            }

            if(!defBoundLines.containsKey(s4)){
                List<int[]> list = new ArrayList<>();
                list.add(line2);
                defBoundLines.put(s4, list);
            }else{
                defBoundLines.get(s4).add(line2);
            }
        }
    }

    public void addMatchingPoints(int[][] defPoints, HashSet<String> improveFitness){
        for (int i = 0; i < defPoints.length; i++) {
            String s1 = "srh-" + defPoints[i][0] + "-" + defPoints[i][1] + "-" + defPoints[i][3];
            String s2 = "slh-" + defPoints[i][2] + "-" + defPoints[i][1] + "-" + defPoints[i][3];
            improveFitness.add(s1);
            improveFitness.add(s2);
        }
    }

    public static void main(String[] args) {
        int[][] defPoints = new int[][]{{4,4,8,9}};
        TargetData targetData = new TargetData();
        HashSet<String> improveFitness = new HashSet<>();
        targetData.addMatchingPoints(defPoints, improveFitness);
        String s1 = "srh-" + (4) + "-" + (4) + "-" + (9);
        boolean contains = improveFitness.contains(s1);
        System.out.println(contains);
    }

}
