package exactsolution.onedimensionalcontiguous;

import com.commonfunction.CommonToolClass;
import com.pointset.ToolClass;
import com.universalalgorithm.QuickSortInfeasible;
import ilog.concert.IloException;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.util.*;

public class MinInfeasible {

    /**
     * @param step1Solutions
     * @description 获取 不可行解集
     * @author hao
     * @date 2023/7/9 3:23
     */
    public static List<int[][]> divideSolution(int[][] step1Solutions, ModeRequiredData modeRequiredData) throws IloException {
        // 寻找最小不可行解的第一步：将离散点集划分为几个部分
        // 记录分界线
        List<Integer> list = new ArrayList<>();
        CommonToolClass commonToolClass = new CommonToolClass();
        // 以 yi 排序 （在原数组基础上排序）
        commonToolClass.sortedTwoDim(step1Solutions, 1);
        int max = 0;
        for (int i = 0; i < step1Solutions.length - 1; i++) {
            max = Math.max(max, step1Solutions[i][2]);
            if (step1Solutions[i + 1][1] >= max) {
                // 说明找到分界线
                list.add(i);
            }
        }
        if (list.size() == 0) {
            list.add(step1Solutions.length - 1);
        }
        List<List<int[]>> divList = new ArrayList<>();
        for (int i = 0; i <= list.size(); i++) {
            int start = 0;
            int end = 0;
            List<int[]> listTemp = new ArrayList<>();
            if (i == list.size()) {
                start = list.get(i - 1) + 1;
                end = step1Solutions.length - 1;
            } else {
                if (i == 0) {
                    start = 0;
                } else {
                    start = list.get(i - 1) + 1;
                }
                end = list.get(i);
            }
            for (int j = start; j <= end; j++) {
                listTemp.add(step1Solutions[j]);
            }
            // 检查分段的解集是否是不可行解。如果是，则添加；不是丢弃
            divList.add(listTemp);
        }

        List<int[][]> divList1 = new ArrayList<>();
        for (int i = 0; i < divList.size(); i++) {
            int[][] tempSolutions = new int[divList.get(i).size()][3];
            for (int j = 0; j < divList.get(i).size(); j++) {
                for (int k = 0; k < 3; k++) {
                    tempSolutions[j][k] = divList.get(i).get(j)[k];
                }
            }
            if (!CheckCplex.xCheck(tempSolutions, modeRequiredData)) {
                divList1.add(tempSolutions);
            }
        }

        List<int[][]> infeasibleItems = reducedSolution(divList1, modeRequiredData);
        return infeasibleItems;
//        return divList1;
    }

    /**
     * @param divList
     * @param modeRequiredData
     * @return List<int [ ] [ ]>
     * @description 求最小不可行解
     * @author hao
     * @date 2023/7/27 20:17
     */
    public static List<int[][]> reducedSolution(List<int[][]> divList, ModeRequiredData modeRequiredData) throws IloException {
        HashSet<Integer> filterHashSet = modeRequiredData.filterHashSet;
        HashSet<Integer> hashSetMinInfeases = modeRequiredData.hashSetMinInfeases;
        HashSet<Integer> hashSetNonInfeases = modeRequiredData.hashSetNonInfeases;
        int[][] targetBlockSize = modeRequiredData.targetBlockSize;
        // 拷贝一份
//        List<int[][]> divListTemp = new ArrayList<>(divList);
        List<int[][]> infeasibleItemsStep2 = new ArrayList<>(divList);

//        // 存储的都是可行解  这部分有问题
//        List<int[][]> infeasibleItemsStep2 = new ArrayList<>();
//        // 每一个不可行解的子区域：
//        for (int i = 0; i < divListTemp.size(); i++) {
//            int[][] arrayTemp = divListTemp.get(i);
//            List<int[]> listTemp1 = new ArrayList<>();
//            // 每一个子区域的 离散点y
//            for (int j = 0; j < arrayTemp.length; j++) {
//
////                System.out.println("每一个子区域的 离散点y arrayTemp.length =  " + arrayTemp.length + "   当前 次数 ：" + j);
//
//                int yPoint = arrayTemp[j][1];
//                // 找到不在当前y上的所有目标块
//                for (int[] tempArr : arrayTemp) {
//                    if (tempArr[1] != yPoint) {
//                        listTemp1.add(tempArr);
//                    }
//                }
//                // list转为数组
//                int[][] tempSolution = new int[listTemp1.size()][3];
//                for (int k = 0; k < listTemp1.size(); k++) {
//                    tempSolution[k] = listTemp1.get(k);
//                }
//                // 过滤检查过的解集
//                if (filterHashSet.add(encode(tempSolution))) {
//                    if (tempSolution.length != 0 && !CheckCplex.xCheck(tempSolution, modeRequiredData)) {
//                        // 不可行(非最小不可行解)
//                        if (hashSetNonInfeases.add(encode(tempSolution))) {
////                            divListTemp.add(tempSolution);
//                        }
//                    } else {
//                        // 可行(最小不可行解)
//                        if (hashSetMinInfeases.add(encode(arrayTemp))) {
//                            infeasibleItemsStep2.add(arrayTemp);
//                        }
//                    }
//                } else {
//                    // 不可行(非最小不可行解)
//                    if (!hashSetNonInfeases.contains(encode(tempSolution)) && !hashSetMinInfeases.contains(encode(arrayTemp))) {
//                        infeasibleItemsStep2.add(arrayTemp);
//                    }
//                }
//            }
//        }
        List<int[][]> infeasibleItemsStep3 = new ArrayList<>(divList);
        // 给动态数组添加时间限制
        long startTime = System.currentTimeMillis();
        // 以面积递增排序，逐个删除，判断是否可行(理论上 infeasibleItemsStep2 是包含 divListTemp )
        for (int i = 0; i < infeasibleItemsStep2.size(); i++) {
            int[][] arrayTemp = infeasibleItemsStep2.get(i);
            QuickSortInfeasible.quickSortInfeasible(arrayTemp, targetBlockSize);
        /*    // 每一个子区域的，以面积递增逐个删除目标块
            for (int j = 0; j < arrayTemp.length; j++) {
                // 超过时间直接返回结果

         *//*       if (isTimeExceeded(startTime, ModeRequiredData.INFEASIBLE_TIMELIMIT)) {
                    System.out.println("   reducedSolution 超过时间3s 直接返回结果     " + infeasibleItemsStep3.size());
//                    for (int k = 0; k < infeasibleItemsStep3.size(); k++) {
//                        for(int[] arr : infeasibleItemsStep3.get(k)){
//                            System.out.print(Arrays.toString(arr) + "\t" + "\t");
//                        }
//                        System.out.println();
//                    }
                    return infeasibleItemsStep3;
                }*//*

                int[][] deleteArray = deleteArray(arrayTemp, j);
                if (filterHashSet.add(encode(deleteArray))) {
                    if (deleteArray.length != 0 && !CheckCplex.xCheck(deleteArray, modeRequiredData)) {
                        // 如果不可行，替换 array 为 deleteArray，继续删除
//                        System.out.println(555);
                        try {
                            // 如果当前解是不可行解，将解集加入 infeasibleItemsStep2 中继续判断
//                            if (hashSetNonInfeases.add(encode(deleteArray))) {
//                                infeasibleItemsStep2.add(deleteArray);
//                            }
                            if (hashSetMinInfeases.add(encode(arrayTemp))) {
                                infeasibleItemsStep3.remove(arrayTemp);
                            }
                            // 加入当前的不可行解（此时非最优，防止求解时间太长没找到最小不可行，fin3 = 0）
                            if (hashSetMinInfeases.add(encode(deleteArray))) {
                                infeasibleItemsStep3.add(deleteArray);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            if (hashSetMinInfeases.add(encode(arrayTemp))) {
                                infeasibleItemsStep3.add(arrayTemp);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }*/
            // 改动版
            // 每一个子区域的，以面积递增逐个删除目标块

            while (arrayTemp.length != 0) {
                int[][] deleteArray = deleteArray(arrayTemp, 0);
                if (filterHashSet.add(encode(deleteArray))) {
                    if (deleteArray.length != 0 && CheckCplex.xCheck(deleteArray, modeRequiredData)) {
                        try {
                            if (hashSetMinInfeases.add(encode(arrayTemp))) {
                                infeasibleItemsStep3.add(arrayTemp);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }
                arrayTemp = deleteArray;
                if(deleteArray.length == 0){
                    infeasibleItemsStep3.add(arrayTemp);
                }
            }
        }
        return infeasibleItemsStep3;
    }

    /**
     * @param divList
     * @description 处理两阶段之后的不可行解集，通过 liftcut 进一步增加不可行解
     * 处理的是每一个子集，并返回当前子集的 liftcut 结果
     * @author hao
     * @date 2023/7/10 16:16
     */
    public static int[][] liftCut(int[][] divList, int[][] step1Solutions, ModeRequiredData modeRequiredData) throws IloException {
        IloCplex liftModel = new IloCplex();
        int[] oriSize = modeRequiredData.oriSize;
        int[][] defectSize = modeRequiredData.defectSize;
        // 定义模型变量 并 建立目标块的目标函数
        // upper down
        IloNumVar[] upper = new IloNumVar[step1Solutions.length];
        IloNumVar[] down = new IloNumVar[step1Solutions.length];
        IloNumExpr obj = liftModel.numExpr();

        liftModel.setParam(IloCplex.DoubleParam.TiLim, ModeRequiredData.LIFTCUT_TIMELIMIT);

        for (int j = 0; j < divList.length; j++) {
            int index = divList[j][0];
            int height = divList[j][2] - divList[j][1];
            upper[index] = liftModel.intVar(divList[j][1], oriSize[1] - height, "upper[" + index + "]");
            down[index] = liftModel.intVar(0, divList[j][1], "down[" + index + "]");
            IloNumExpr exprIndex = liftModel.diff(upper[index], down[index]);
            obj = liftModel.sum(obj, exprIndex);
        }
        liftModel.addMaximize(obj);

        // 建立模型
        for (int i = 0; i < divList.length; i++) {
            int jIndex = divList[i][0];
            // 获取与j有交集的目标块集合
            List<int[]> overlapBlock = overlapBlock(divList, i);
            // 建立模型  lsj + hj >= rsk + 1
            for (int k = 0; k < overlapBlock.size(); k++) {
                int[] tempArray = overlapBlock.get(k);
                int kIndex = tempArray[0];
                IloNumExpr expr1 = liftModel.diff(upper[kIndex], down[jIndex]);
                liftModel.addLe(expr1, divList[i][2] - divList[i][1] - 1);
            }
        }

        for (int i = 0; i < defectSize.length; i++) {
            // 与 缺陷块i 有交集的目标块集合
            List<int[]> overlapDefBlock = overlapDefBlock(divList, defectSize[i]);
            for (int j = 0; j < overlapDefBlock.size(); j++) {
                int[] tempArray = overlapDefBlock.get(j);
                IloNumExpr expr2 = liftModel.diff(down[tempArray[0]], defectSize[i][1] - (tempArray[2] - tempArray[1]) + 1);
                liftModel.addGe(expr2, 0);
                IloNumExpr expr3 = liftModel.diff(upper[tempArray[0]], defectSize[i][3] - 1);
                liftModel.addLe(expr3, 0);
            }
        }

        liftModel.setOut(null);
        liftModel.solve();

        int[][] result = new int[divList.length][3];
        System.out.println("liftModel.getStatus()" + liftModel.getStatus());
        if (liftModel.getStatus().equals(IloCplex.Status.Feasible) || liftModel.getStatus().equals(IloCplex.Status.Optimal)) {
            for (int i = 0; i < divList.length; i++) {
                int index = divList[i][0];
                result[i][0] = index;
                result[i][1] = (int) (liftModel.getValue(down[index]) + 0.000001);
                result[i][2] = (int) (liftModel.getValue(upper[index]) + 0.000001);

            }
        }
        // 超过时间限制，返回
        if (liftModel.getStatus().equals(IloCplex.Status.Unknown) || liftModel.getStatus().equals(IloCplex.Status.Infeasible)) {
            System.out.println(" liftcut 求解超时    ");
            liftModel.end();
            for (int i = 0; i < divList.length; i++) {
                result[i][0] = divList[i][0];
                result[i][1] = divList[i][1];
                result[i][2] = divList[i][1];
            }
            return result;
        }
        liftModel.end();
        return result;
    }

    public static List<int[]> overlapDefBlock(int[][] divList, int[] defSize) {
        List<int[]> tempList1 = new ArrayList<>();
        for (int i = 0; i < divList.length; i++) {
            if (defSize[3] - 1 >= divList[i][1] && divList[i][2] - 1 >= defSize[1]) {
                tempList1.add(divList[i]);
            }
        }
        return tempList1;
    }

    public static List<int[]> overlapBlock(int[][] tempArray1, int index) {
        List<int[]> tempList1 = new ArrayList<>();
        for (int i = 0; i < tempArray1.length; i++) {
            if (i != index) {
                if (tempArray1[index][2] > tempArray1[i][1] && tempArray1[i][2] > tempArray1[index][1]) {
                    tempList1.add(tempArray1[i]);
                }
            }
        }
        return tempList1;
    }

    public static int[][] deleteArray(int[][] array, int index) {

        int[][] tempArray = new int[array.length - 1][array[0].length];
        int times = 0;
        try {
            for (int i = 0; i < array.length; i++) {
                if (i != index) {
                    for (int j = 0; j < array[i].length; j++) {
                        tempArray[times][j] = array[i][j];
                    }
                    ++times;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tempArray;
    }

    /**
     * @return int
     * @description 给每一组序列添加唯一的值与之对应
     * @author hao
     * @date 2023/7/9 6:37
     */
    private static int encode(int[][] arrayTemp) {
        int key = 0;
        TreeSet<Integer> set = new TreeSet<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        for (int[] item1 : arrayTemp) {
            // 以索引确定唯一值
            set.add(item1[0]);
        }
        Iterator<Integer> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            key += iterator.next() * (int) (Math.pow(10, i));
            i++;
        }
        return key;
    }

    public static boolean isTimeExceeded(long startTime, double limit) {
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - startTime) / 1000;
        return elapsedSeconds >= limit;
    }

}
