package exactsolution.onedimensionalcontiguous;

import com.twodimension.TargetData;
import ilog.concert.*;
import ilog.cplex.IloCplex;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hao
 * @description 主模型
 * @date 2023/7/5 19:34
 */
public class MasterModel {
    /**
     * @description 定义cplex内部类的对象
     */
    public IloCplex model;

    /**
     * @description 定义R[y][i]表示小矩形i是否放置在离散点 y 处
     */
    IloNumVar[][] R;
    ArrayList<Integer> integers = new ArrayList<>();
    ArrayList<Integer> integers1 = new ArrayList<>();

    public IloCplex rectangularModel(ModeRequiredData modeRequiredData) throws IloException, RuntimeException {

        //获取原放置区域的宽度
        int Width = modeRequiredData.oriSize[0];
        // 获取下界
        int Height = modeRequiredData.oriSize[1];
        //获得每种目标块的尺寸信息, w h
        int[][] blockSize = modeRequiredData.targetBlockSize;
        //缺陷块每一行的长度
        int[] defectWidth = modeRequiredData.defectWidth;
        int[] heightPoints = modeRequiredData.heightPoints;
        // 缺陷块
        int[][] defectSize = modeRequiredData.defectSize;
        // 高度可放置点
        boolean[][] heightPlacedPoints = modeRequiredData.heightPlacedPoints;
        int minWidth = modeRequiredData.minWidth;
        int minHeight = modeRequiredData.minHeight;
        //建立模型
        model = new IloCplex();
        // 添加求解模型的时间限制
        model.setParam(IloCplex.DoubleParam.TiLim, ModeRequiredData.CBP_TIMELIMIT);
        model.setOut(null);
        // 打印每个目标块的可放置点
//        System.out.println("heightPlacedPoints.length  " + heightPlacedPoints.length);
//        System.out.println("heightPlacedPoints[0].length  " + heightPlacedPoints[0].length);
//        int[][] heightTemp = new int[heightPlacedPoints.length][heightPlacedPoints[0].length];
//        for (int i = 0; i < heightPlacedPoints.length; i++) {
//            for (int j = 0; j < heightPlacedPoints[0].length; j++) {
//                if (heightPlacedPoints[i][j]) {
//                    heightTemp[i][j] = j;
//                }
//            }
//        }

        //创建模型变量
        R = new IloNumVar[blockSize.length][Height + 1];
        for (int j = 0; j < blockSize.length; j++) {
            for (int y = 0; y < heightPoints.length; y++) {
                int yPoint = heightPoints[y];
                try {
                    if (heightPlacedPoints[j][yPoint]) {
                        //将R[y][i]为0，1变量
                        R[j][yPoint] = model.numVar(0, 1, IloNumVarType.Bool, "R[" + j + "][" + yPoint + "]");
                    }
                } catch (IloException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // 添加目标函数
        // 加入约束
        // 约束一：表示所有的目标块必须能切割（如果该离散点可以放置就会创建一个约束表达式）
        for (int i = 0; i < blockSize.length; i++) {
            // 对每一个目标块都创建一个约束表达式
            IloNumExpr expr1 = model.numExpr();
            // 对于一个 目标块 来说，所有离散点 y 的求和应该为1
            for (int k = 0; k < heightPoints.length; k++) {
                int yPoint = heightPoints[k];
                // 以左下放置为标准记录离散点集
                if (heightPlacedPoints[i][yPoint]) {
//                    System.out.print("\t" + yPoint);
                    expr1 = model.sum(expr1, R[i][yPoint]);
                }
            }
            model.addEq(expr1, 1);
        }

        //约束二：表示覆盖第 q 行长条目标块 总长度不超过该长条的可用长度
        for (int i = 0; i < heightPoints.length; i++) {
            //每一行所有靠下侧放置的离散点需要一个约束条件
            IloNumExpr expr2 = model.numExpr();
            for (int j = 0; j < blockSize.length; j++) {
                // 获取目标块靠下侧放置的上下界
                int lowBound1 = Math.max(heightPoints[i] - blockSize[j][1] + 1, 0);
                int upperBound1 = Math.min(heightPoints[i], Height - blockSize[j][1]);
                for (int y = lowBound1; y <= upperBound1; y++) {
                    if (heightPlacedPoints[j][y]) {
                        //对于每一个目标块而言
                        expr2 = model.sum(model.prod(R[j][y], modeRequiredData.targetBlockSize[j][0]), expr2);
                    }
                }
            }
            // 每一行目标块占据的宽度 + 缺陷块的宽度 <= 最小宽度
            expr2 = model.sum(defectWidth[i], expr2);
            model.addLe(expr2, Width);
        }

        // 约束三：Valid inequalities，添加有关缺陷块两侧宽度的约束
        // ∑δdijp wi ≤ W − xd2
        // ∑ξdijp wi ≤ xd1
        IloNumVar[][] leftVar = new IloNumVar[defectSize.length][blockSize.length];
        IloNumVar[][] rightVar = new IloNumVar[defectSize.length][blockSize.length];
        IloNumVar[][][] leftPBinVar = new IloNumVar[defectSize.length][blockSize.length][2];
        IloNumVar[][][] rightPBinVar = new IloNumVar[defectSize.length][blockSize.length][2];
        for (int i = 0; i < defectSize.length; i++) {
            int leftSpace = defectSize[i][0];
            int rightSpace = Width - defectSize[i][2];
            for (int j = 0; j < blockSize.length; j++) {
                // 添加约束ldij + rdij ≥ ∑ziy; y∈ Hd (i, j)
                IloLinearNumExpr expr1 = model.linearNumExpr();
                // eftVar[i][j] = model.intVar(0, 1);
                leftVar[i][j] = model.numVar(0, 1, IloNumVarType.Bool, "leftVar[" + i + "][" + j + "]");
                rightVar[i][j] = model.numVar(0, 1, IloNumVarType.Bool, "rightVar[" + i + "][" + j + "]");
                //  rightVar[i][j] = model.intVar(0, 1);
                // 目标块j 可以覆盖缺陷块i 的条件
                for (int k = defectSize[i][1] - blockSize[j][1] + 1; k < defectSize[i][3]; k++) {
                    // k >= 0 && k + hi <= H && j可以放在k处
                    if (k >= 0 && k + blockSize[j][1] <= Height && heightPlacedPoints[j][k]) {
                        expr1.addTerm(1, R[j][k]);
                    }
                }
                if (leftSpace < blockSize[j][0]) {
                    model.addGe(rightVar[i][j], expr1);
                } else if (rightSpace < blockSize[j][0]) {
                    model.addGe(leftVar[i][j], expr1);
                } else {
                    model.addGe(model.sum(leftVar[i][j], rightVar[i][j]), expr1);
                }
                model.addEq(model.sum(leftVar[i][j], rightVar[i][j]), 1);
            }

            // ξdijp ≥ ldij + ∑ziy − 1 {y∈H (i,p)  i ∈ I, j ∈ D, ydj ≤ p ≤ ydj + hdj − 1}
            int[] pPoints = {defectSize[i][1], defectSize[i][3] - 1};
            //  int[] pPoints = {defectSize[i][1]};
            for (int p = 0; p < pPoints.length; p++) {
                IloLinearNumExpr expr2 = model.linearNumExpr();
                IloLinearNumExpr expr3 = model.linearNumExpr();
                for (int j = 0; j < blockSize.length; j++) {
                    leftPBinVar[i][j][p] = model.numVar(0, 1, IloNumVarType.Bool, "leftPBinVar[" + i + "][" + j + "][" + p + "]");
                    rightPBinVar[i][j][p] = model.numVar(0, 1, IloNumVarType.Bool, "rightPBinVar[" + i + "][" + j + "][" + p + "]");
                    IloLinearNumExpr expr4 = model.linearNumExpr();
                    for (int l = pPoints[p] - blockSize[j][1] + 1; l <= pPoints[p]; l++) {
                        if (blockSize[j][1] + l <= Height && l >= 0 && heightPlacedPoints[j][l]) {
                            //这里的条件是：占据该行，并且高度符合要求
                            expr4.addTerm(1, R[j][l]);

                        }
                    }
                    model.addLe(model.diff(leftPBinVar[i][j][p], leftVar[i][j]), 0);
                    model.addLe(model.diff(rightPBinVar[i][j][p], rightVar[i][j]), 0);
                    model.addLe(model.diff(model.sum(expr4, leftVar[i][j]), leftPBinVar[i][j][p]), 1);
                    model.addLe(model.diff(model.sum(expr4, rightVar[i][j]), rightPBinVar[i][j][p]), 1);

                    if (blockSize[j][0] <= leftSpace) {
                        expr2.addTerm(blockSize[j][0], leftPBinVar[i][j][p]);
                    }
                    if (blockSize[j][0] <= rightSpace) {
                        expr3.addTerm(blockSize[j][0], rightPBinVar[i][j][p]);
                    }
                }
                model.addLe(expr2, leftSpace);
                model.addLe(expr3, rightSpace);
            }
        }

        // 添加额外约束
        for (int i = 0; i < modeRequiredData.exprList.size(); i++) {
            int[][] liftCut = modeRequiredData.exprList.get(i);
            // 向模型添加新的约束
            IloLinearNumExpr expr = model.linearNumExpr();
            for (int j = 0; j < liftCut.length; j++) {
                for (int k = liftCut[j][1]; k <= liftCut[j][2]; k++) {
                    if (modeRequiredData.heightPlacedPoints[liftCut[j][0]][k]) {
                        expr.addTerm(R[liftCut[j][0]][k], 1);
                    }
                }
            }
            model.addLe(expr, liftCut.length - 1);
        }

//        // 添加额外约束
//        Set<Integer> keySet = modeRequiredData.exprListMap.keySet();
//        for (Integer length : keySet) {
//            List<IloLinearNumExpr> exprList = modeRequiredData.exprListMap.get(length);
//            for (int i = 0; i < exprList.size(); i++) {
//                model.addLe(exprList.get(i), length);
//            }
//        }
        return model;
    }

    public boolean solveModel(ModeRequiredData modeRequiredData, TargetData rLayout, long startTime, long endTime) throws IloException, FileNotFoundException, RuntimeException {

        // 记录需要查找的y轴位置  int[][3]  [0]: i   [1]: yi [2]:  yi+hi
        int blockNum = modeRequiredData.targetBlockSize.length;
        int[][] step1Solutions = new int[blockNum][3];
        // 主循环，直到1CBP求解出结果。如果线程没有被中断
        while (true) {
            if(isTimeExceeded(startTime, endTime)){
                modeRequiredData.check = null;
                model.end();
                return false;
            }
            // 加载1CBP模型
            model = rectangularModel(modeRequiredData);
            int[] heightPoints = modeRequiredData.heightPoints;
            // 1CBP
            if (model.solve()) {
                System.out.println("限制时间  解的状态：  " + model.getStatus());
                // 记录矩形的位置信息，i yi yi+hi
                //求得 y 的坐标
                int y = 0;
                integers = new ArrayList<>();
                integers1 = new ArrayList<>();

                for (int i = 0; i < modeRequiredData.targetBlockSize.length; i++) {
                    integers.add(i);
                    boolean exist = true;
                    for (int j = 0; j < heightPoints.length; j++) {
                        y = heightPoints[j];
                        if (modeRequiredData.heightPlacedPoints[i][y]) {

                            double value = model.getValue(R[i][y]);

//                            if(i == 14){
//                                System.out.println("14 对应的高度点    " + y + "model.value ==   " + value);
//                            }

                            exist = false;
                            if (value > 0.5) {
                                integers1.add(i);
                                step1Solutions[i][0] = i;
                                step1Solutions[i][1] = y;
                                step1Solutions[i][2] = y + modeRequiredData.blockSize[i][1];

                                //  step1Solutions[i][2] = y + modeRequiredData.targetBlockSize[i][1];
                            }
                        }
                    }
                    if(exist){
                        System.out.println("有的点没有离散点---" + i);
                    }
                }

//                System.out.println("1CBP模型可解  解的状态：  " + model.getStatus());
//                System.out.println("integers    " + integers);
//                System.out.println("integers1    " + integers1);
                // 打印解
//                for (int[] arr : step1Solutions) {
//                    System.out.println(Arrays.toString(arr));
//                }
//                System.out.println("  1CBP模型可解  ");

                // X-check
                if (CheckCplex.xCheck(step1Solutions, modeRequiredData)) {
                    int[] widthPoints = modeRequiredData.widthPoints;
                    // 输出X信息
                    int xPoints = 0;
                    int[][] xCoord = new int[blockNum][3];
                    for (int i = 0; i < blockNum; i++) {
                        for (int j = 0; j < widthPoints.length; j++) {
                            xPoints = widthPoints[j];
                            double value = 0;
                            try {
                                if (modeRequiredData.widthPlacedPoints[i][xPoints]) {
                                    value = modeRequiredData.check.checkModel.getValue(modeRequiredData.check.x[i][xPoints]);
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            if (value > 0.5) {
                                xCoord[i][0] = i;
                                xCoord[i][1] = xPoints;
                                xCoord[i][2] = xPoints + modeRequiredData.blockSize[i][0];
                                //  xCoord[i][2] = xPoints + modeRequiredData.targetBlockSize[i][0];
                            }
                        }
                    }
                    modeRequiredData.resultPoints = new int[blockNum][7];
                    // 合并得到最终结果
                    for (int i = 0; i < blockNum; i++) {
                        modeRequiredData.resultPoints[i][0] = i;
                        modeRequiredData.resultPoints[i][1] = xCoord[i][2] - xCoord[i][1];
                        modeRequiredData.resultPoints[i][2] = step1Solutions[i][2] - step1Solutions[i][1];
                        modeRequiredData.resultPoints[i][3] = xCoord[i][1];
                        modeRequiredData.resultPoints[i][4] = step1Solutions[i][1];
                        modeRequiredData.resultPoints[i][5] = xCoord[i][2];
                        modeRequiredData.resultPoints[i][6] = step1Solutions[i][2];
                        System.out.println(i);
                    }
                    // X可解；直接输出
                    System.out.println("可以求得最终解~~~   =  " + modeRequiredData.oriSize[1]);
                    return true;
                } else {
//                    System.out.println(222);
                    // 计算最小不可行解 添加了时间限制 (30s)
                    List<int[][]> divideSolution = MinInfeasible.divideSolution(step1Solutions, modeRequiredData);

//                    System.out.println("------divideSolution-----------");
                    // 输出
//                    for(int[][] arr1 :divideSolution){
//                        for(int[] arr2 : arr1){
//                            System.out.println(Arrays.toString(arr2));
//                        }
//                        System.out.println("-----------------");
//                    }

                    // 求解太慢
                    for (int i = 0; i < divideSolution.size(); i++) {
                        if (divideSolution.get(i).length == 0) {
                            continue;
                        }

                        System.out.println("modeRequiredData.oriSize[1]   "+modeRequiredData.oriSize[1]);

                        System.out.println("---------------divideSolution" + i  + "-----------");
                        for(int[] arr3 : divideSolution.get(i)){
                            System.out.println(Arrays.toString(arr3));
                        }

                        // 改善最小不可行解的质量 添加了时间限制，超过时间 (200s)，返回 原不可行解
                        int[][] liftCut = MinInfeasible.liftCut(divideSolution.get(i), step1Solutions, modeRequiredData);

//                        int[][] liftCut = new int[divideSolution.get(i).length][3];
//                        for (int j = 0; j < divideSolution.get(i).length; j++) {
//                            liftCut[j][0] = divideSolution.get(i)[j][0];
//                            liftCut[j][1] = divideSolution.get(i)[j][1];
//                            liftCut[j][2] = divideSolution.get(i)[j][1];
//                        }

//                        System.out.println("..........liftCut..............");
//                        for(int[] arr3 : liftCut){
//                            System.out.println(Arrays.toString(arr3));
//                        }

                        // int[][] liftCut = divideSolution.get(i);
                        if (liftCut.length != 0) {
                            modeRequiredData.exprList.add(liftCut);
                        }
                     /*       // 向模型添加新的约束
                            IloLinearNumExpr expr = model.linearNumExpr();
                            for (int j = 0; j < liftCut.length; j++) {
                                for (int k = liftCut[j][1]; k <= liftCut[j][2]; k++) {
                                    if (modeRequiredData.heightPlacedPoints[liftCut[j][0]][k]) {
                                        expr.addTerm(R[liftCut[j][0]][k], 1);
                                    }
                                }
                            }
                            modeRequiredData.exprListMap.add(liftCut.length - 1, expr);
                        }*/
                    }

                    System.out.println("当前 X-check 模型不可解");
                    // X-check不通过时，需要不断添加约束，直到 Y不可行。说明当前高度确实不可行，则增加高度
                    continue;
                }
            }
            // 检查求解器状态，看是否找到最优解
            if (model.getStatus().equals(IloCplex.Status.Unknown)) {
                System.out.println("  求解超过时间 = " + ModeRequiredData.CBP_TIMELIMIT);
                return false;
            }

            // 求解失败
            ++modeRequiredData.oriSize[1];
            modeRequiredData.improveModel1(modeRequiredData, rLayout);
            System.out.println("modeRequiredData.lowerBoundHeight :    " + modeRequiredData.oriSize[1]);
        }
    }

    public static boolean isTimeExceeded(long startTime, long endTime) {
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - startTime) / 1000;
        return elapsedSeconds >= endTime;
    }
}
