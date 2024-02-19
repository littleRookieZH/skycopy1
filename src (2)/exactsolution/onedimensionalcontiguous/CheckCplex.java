package exactsolution.onedimensionalcontiguous;

import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pc 检查x是否可行
 */
public class CheckCplex {
    IloCplex checkModel;
    IloNumVar[][] x;
    static int LARGERM = 100000;

    public static boolean xCheck(int[][] step1Solutions, ModeRequiredData modeRequiredData) throws IloException {
        int[] widthPoints = modeRequiredData.widthPoints;
        IloCplex checkModel = new IloCplex();
        modeRequiredData.check.checkModel = checkModel;
        int blockNum = modeRequiredData.targetBlockSize.length;
        int nowBlockNum = step1Solutions.length;
        boolean[][] widthPlacedPoints = modeRequiredData.widthPlacedPoints;

        int Height = modeRequiredData.oriSize[1];

        // 添加求解模型的时间限制
        checkModel.setParam(IloCplex.DoubleParam.TiLim, ModeRequiredData.X_CHECK_TIMELIMIT);
        // 打印宽度可放置点
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < widthPlacedPoints.length; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < widthPlacedPoints[i].length; j++) {
                if (widthPlacedPoints[i][j]) {
                    list.add(j);
                }
            }
            map.put(i, list);
        }
//        for(Map.Entry<Integer, List<Integer>> entry : map.entrySet()){
//            System.out.println("key = " + entry.getKey());
//            System.out.println("value = " + entry.getValue());
//        }
//        System.out.println("===========");

        // lik lki 用来表示i k的相对位置
        IloNumVar[][] leftPos = new IloNumVar[blockNum][blockNum];
        // x[i][x0]  x0:表示X方向离散点
        IloNumVar[][] x = new IloNumVar[blockNum][modeRequiredData.oriSize[0]];
        modeRequiredData.check.x = x;
        // 目标块在X方向只能放一次
        for (int i = 0; i < nowBlockNum; i++) {
            int blockIndex = step1Solutions[i][0];
            IloLinearNumExpr varExpr1 = checkModel.linearNumExpr();
            for (int j = 0; j < widthPoints.length; j++) {
                int xPoint = widthPoints[j];
                if (widthPlacedPoints[blockIndex][xPoint]) {
                    //将 x[i][j]为0，1变量
                    x[blockIndex][xPoint] = checkModel.numVar(0, 1, IloNumVarType.Bool, "x[" + blockIndex + "][" + xPoint + "]");
                    varExpr1.addTerm(x[blockIndex][xPoint], 1);
                }
            }
            checkModel.addEq(varExpr1, 1);
        }

//        if(Height == 31){
//            System.out.println("111");
//        }

        for (int i = 0; i < nowBlockNum; i++) {
            int blockIndexI = step1Solutions[i][0];
            for (int j = 0; j < i; j++) {
                int blockIndexJ = step1Solutions[j][0];
                // step1Solutions[][] [0]: i ; [1]:  yi ; [2]: yi+hi
                // yi + hi - 1 ≥ yj, yj + hj - 1 ≥ yi
                if (step1Solutions[i][2] - 1 >= step1Solutions[j][1] && step1Solutions[j][2] - 1 >= step1Solutions[i][1]) {
                    leftPos[blockIndexI][blockIndexJ] = checkModel.numVar(0, 1, IloNumVarType.Bool, "leftPos[" + blockIndexI + "][" + blockIndexJ + "]");
                    leftPos[blockIndexJ][blockIndexI] = checkModel.numVar(0, 1, IloNumVarType.Bool, "leftPos[" + blockIndexJ + "][" + blockIndexI + "]");
                    // xk + (1 − lik)M ≥ xi + wi
                    IloNumExpr expr1 = checkModel.numExpr();
                    for (int k = 0; k < widthPoints.length; k++) {
                        if (widthPlacedPoints[blockIndexJ][widthPoints[k]]) {
                            expr1 = checkModel.sum(expr1, checkModel.prod(x[blockIndexJ][widthPoints[k]], widthPoints[k]));
                        }
                        if (widthPlacedPoints[blockIndexI][widthPoints[k]]) {
                            expr1 = checkModel.diff(expr1, checkModel.prod(x[blockIndexI][widthPoints[k]], widthPoints[k]));
                        }
                    }
                    expr1 = checkModel.diff(expr1, checkModel.prod(LARGERM, leftPos[blockIndexI][blockIndexJ]));
                    expr1 = checkModel.sum(expr1, LARGERM);
                    checkModel.addGe(expr1, modeRequiredData.targetBlockSize[blockIndexI][0]);

//                    if (Height == 31) {
//                        System.out.println("111");
//                    }


                    // xi + (1 − lki)M ≥ xk + wk
                    IloNumExpr expr2 = checkModel.numExpr();
                    for (int k = 0; k < widthPoints.length; k++) {
                        if (widthPlacedPoints[blockIndexI][widthPoints[k]]) {
                            expr2 = checkModel.sum(expr2, checkModel.prod(x[blockIndexI][widthPoints[k]], widthPoints[k]));
                        }
                        if (widthPlacedPoints[blockIndexJ][widthPoints[k]]) {
                            expr2 = checkModel.diff(expr2, checkModel.prod(x[blockIndexJ][widthPoints[k]], widthPoints[k]));
                        }
                    }
                    expr2 = checkModel.diff(expr2, checkModel.prod(LARGERM, leftPos[blockIndexJ][blockIndexI]));
                    expr2 = checkModel.sum(expr2, LARGERM);
                    checkModel.addGe(expr2, modeRequiredData.targetBlockSize[blockIndexJ][0]);

//                    if (Height == 31) {
//                        System.out.println("111");
//                    }


                    // lik + lki = 1
                    IloNumExpr expr3 = checkModel.numExpr();
                    expr3 = checkModel.sum(expr3, leftPos[blockIndexI][blockIndexJ], leftPos[blockIndexJ][blockIndexI]);
                    checkModel.addEq(expr3, 1);
                }
            }
        }

        //  lijd，添加关于缺陷块的约束：目标块不能和缺陷块重合
        // 这里应该是有问题
        IloNumVar[][] leftDefectPos = new IloNumVar[blockNum][modeRequiredData.defectSize.length];
        for (int i = 0; i < modeRequiredData.defectSize.length; i++) {
            for (int j = 0; j < nowBlockNum; j++) {
                // yj + hj - 1 ≥ yd1, yd2 - 1 ≥ yj
                if (step1Solutions[j][2] - 1 >= modeRequiredData.defectSize[i][1] && modeRequiredData.defectSize[i][3] - 1 >= step1Solutions[j][1]) {
                    int blockIndexJ = step1Solutions[j][0];
//                    leftDefectPos[blockIndexJ][i] = checkModel.intVar(0, 1);
                    leftDefectPos[blockIndexJ][i] = checkModel.numVar(0, 1, IloNumVarType.Bool, "leftDefectPos[" + blockIndexJ + "][" + i + "]");
                    IloNumExpr expr4 = checkModel.numExpr();
                    IloNumExpr expr5 = checkModel.numExpr();
                    for (int l = 0; l < widthPoints.length; l++) {
                        if (widthPlacedPoints[blockIndexJ][widthPoints[l]]) {
                            expr4 = checkModel.sum(expr4, checkModel.prod(x[blockIndexJ][widthPoints[l]], widthPoints[l]));
                            expr5 = checkModel.sum(expr5, checkModel.prod(x[blockIndexJ][widthPoints[l]], widthPoints[l]));
                        }
                    }
                    expr4 = checkModel.sum(expr4, checkModel.prod(leftDefectPos[blockIndexJ][i], LARGERM));
                    expr4 = checkModel.diff(expr4, modeRequiredData.defectSize[i][2]);
                    checkModel.addGe(expr4, 0);

                    expr5 = checkModel.sum(expr5, checkModel.prod(leftDefectPos[blockIndexJ][i], LARGERM));
                    expr5 = checkModel.diff(expr5, modeRequiredData.defectSize[i][0]);
                    checkModel.addLe(expr5, LARGERM - modeRequiredData.targetBlockSize[blockIndexJ][0]);
//                    if(Height == 32){
//                        System.out.println("111");
//                    }
                }
            }
        }
        checkModel.setOut(null);
        try {
            if (!checkModel.solve()) {
                checkModel.end();
                return false;
            } else {
                // 可行
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
