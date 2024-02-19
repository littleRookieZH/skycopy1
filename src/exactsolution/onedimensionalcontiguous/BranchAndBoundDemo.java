package exactsolution.onedimensionalcontiguous;

import ilog.concert.*;
import ilog.cplex.IloCplex;

import java.util.Comparator;
import java.util.PriorityQueue;

//调用cplex实现分支定界算法
//整数规划示例：
//max z=40x_1+90x_2
//9x_1+7x_2<=56
//7x_1+20x_2<=70
//x_1,x_2>=0且为整数
//最优值：x_1=4,x_2=2,z=340

//数据参数定义
class ModelData {
    //目标系数
    double[] objectiveCoefficient = {40, 90};
    //约束系数
    double[][] constraintCoefficient = {{9, 7}, {7, 20}};
    //约束值
    double[] constraintValue = {56, 70};
    //变量数量
    int variableNumber = 2;
    //约束数量
    int constrainNumber = 2;
    //模型下界
    double lowBound = Double.MIN_VALUE;
}

//节点记录类
class Node {
    //模型数据
    ModelData data;
    //模型目标值
    double nodeObj;
    //模型解
    double[] nodeResult;

    //构造函数
    public Node(ModelData data) {
        this.data = data;
        nodeObj = data.lowBound;
        nodeResult = new double[data.variableNumber];
    }

    //复制节点
    public Node nodeCopy() {
        Node newNode = new Node(data);
        newNode.nodeObj = nodeObj;
        newNode.nodeResult = nodeResult.clone();
        return newNode;
    }
}

//使用cplex求解整数规划
public class BranchAndBoundDemo {
    //定义数据
    ModelData data;
    //定义节点
    Node node1, node2;
    //当前最好解
    double curBest;
    //当前最好方案
    Node curBestNode;
    //定义优先队列
    PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.nodeObj > o2.nodeObj ? -1 : 1;
        }
    });
    //模型对象
    IloCplex model;
    //模型变量
    IloNumVar[] x;
    //变量对应的取值
    double[] xValue;
    //模型目标值
    double modelObj;

    //构造函数
    public BranchAndBoundDemo(ModelData data) {
        this.data = data;
        xValue = new double[data.variableNumber];
    }

    //模型建立
    private void buildModel() throws IloException {
        model = new IloCplex();
        model.setOut(null);
        x = new IloNumVar[data.variableNumber];
        for (int i = 0; i < data.variableNumber; i++) {
            x[i] = model.numVar(0, 1e15, IloNumVarType.Float, "x[" + i + "]");
        }
        //设置目标函数
        IloNumExpr obj = model.numExpr();
        for (int i = 0; i < data.variableNumber; i++) {
            obj = model.sum(obj, model.prod(data.objectiveCoefficient[i], x[i]));
        }
        model.addMaximize(obj);
        //添加约束
        for (int k = 0; k < data.constrainNumber; k++) {
            IloNumExpr expr = model.numExpr();
            for (int i = 0; i < data.variableNumber; i++) {
                expr = model.sum(expr, model.prod(data.constraintCoefficient[k][i], x[i]));
            }
            model.addLe(expr, data.constraintValue[k]);
        }
    }

    //模型求解
    private void solveModel() throws IloException {
        if (model.solve()) {
            modelObj = model.getObjValue();
            System.out.println("模型目标值：" + model.getObjValue());
            System.out.println("模型变量值：");
            for (int i = 0; i < data.variableNumber; i++) {
                xValue[i] = model.getValue(x[i]);
                System.out.print(model.getValue(x[i]) + "\t");
            }
            System.out.println();
        } else {
            System.out.println("模型不可解");
        }
    }

    //模型解复制到节点
    private void modelCopyNode(Node node) {
        node.nodeObj = modelObj;
        node.nodeResult = xValue.clone();
    }

    //分支定界过程
    private void branchAndBoundMethod() throws IloException {
        try {
            // 创建 CPLEX 求解器
            IloCplex cplex = new IloCplex();

            // 创建决策变量 x[i][j]，表示生产线 i 生产产品 j 的数量
            int numLines = 3;
            int numProducts = 4;
            IloNumVar[][] x = new IloNumVar[numLines][numProducts];
            for (int i = 0; i < numLines; i++) {
                for (int j = 0; j < numProducts; j++) {
                    x[i][j] = cplex.intVar(0, Integer.MAX_VALUE, "x[" + i + "][" + j + "]");
                }
            }

            // 设置目标函数，最大化总产量
            IloLinearNumExpr objExpr = cplex.linearNumExpr();
            for (int i = 0; i < numLines; i++) {
                for (int j = 0; j < numProducts; j++) {
                    objExpr.addTerm(1.0, x[i][j]);
                }
            }
            cplex.addMaximize(objExpr);

            // 添加约束条件
            // 生产线工作时间约束
            double[] lineHours = {12.0, 12.0, 12.0};
            for (int i = 0; i < numLines; i++) {
                IloLinearNumExpr timeExpr = cplex.linearNumExpr();
                for (int j = 0; j < numProducts; j++) {
                    double productionTime = getProductionTime(i, j);
                    timeExpr.addTerm(productionTime, x[i][j]);
                }
                cplex.addLe(timeExpr, lineHours[i]);
            }

            // 生产线产量约束
            double[] lineCapacities = {4.0, 3.0, 5.0};

            for (int i = 0; i < 1; i++) {
                IloLinearNumExpr capacityExpr = cplex.linearNumExpr();
                for (int j = 0; j < numProducts; j++) {
                    capacityExpr.addTerm(1.0, x[i][j]);
                }
                cplex.addLe(capacityExpr, lineCapacities[i]);
            }
            IloLinearNumExpr capacityExpr1 = cplex.linearNumExpr();
            capacityExpr1.addTerm(1.0, x[1][1]);
            cplex.addLe(capacityExpr1, lineCapacities[1]);

            IloLinearNumExpr capacityExpr2 = cplex.linearNumExpr();
            capacityExpr2.addTerm(1.0, x[2][2]);
            cplex.addLe(capacityExpr2, lineCapacities[2]);

            // 最低生产量约束
            int[] minProduction = {2, 3, 4, 4};
            for (int j = 0; j < numProducts; j++) {
                IloLinearNumExpr minProductionExpr = cplex.linearNumExpr();
                for (int i = 0; i < numLines; i++) {
                    minProductionExpr.addTerm(1.0, x[i][j]);
                }
                cplex.addGe(minProductionExpr, minProduction[j]);
            }

            // 求解线性规划问题
            if (cplex.solve()) {
                System.out.println("Solution status: " + cplex.getStatus());
                System.out.println("Objective value: " + cplex.getObjValue());
                for (int i = 0; i < numLines; i++) {
                    for (int j = 0; j < numProducts; j++) {
                        System.out.println("x[" + i + "][" + j + "] = " + cplex.getValue(x[i][j]));
                    }
                }
            } else {
                System.out.println("No solution found.");
            }

            // 关闭 CPLEX 求解器
            cplex.end();
        } catch (IloException e) {
            e.printStackTrace();
        }
    }

    //选择分支
    private Node chooseBranch(Node node, int idIndex, boolean leftOrRight) throws IloException {
        Node newNode = new Node(data);
        //复制节点信息-避免返回空值
        newNode = node.nodeCopy();
        //设置变量取值范围
        setVarsBound(node, idIndex, leftOrRight);
        //模型求解
        if (model.solve()) {
            solveModel();
            modelCopyNode(newNode);
        } else {
            System.out.println("模型不可解");
            newNode.nodeObj = Double.MIN_VALUE;
        }
        return newNode;
    }

    private void setVarsBound(Node node, int idIndex, boolean leftOrRight) throws IloException {
        //设置变量分支-左支
        if (leftOrRight) {
            for (int i = 0; i < node.nodeResult.length; i++) {
                if (i == idIndex) {
                    x[idIndex].setLB(0);
                    x[idIndex].setUB((int) node.nodeResult[idIndex]);
                } else {
                    x[i].setLB(node.nodeResult[i]);
                    x[i].setUB(node.nodeResult[i]);
                }
            }
            System.out.println("非整数变量范围：" + 0 + "\t" + (int) node.nodeResult[idIndex]);
            System.out.println("左支模型：");
            System.out.println(model);
            for (int i = 0; i < node.nodeResult.length; i++) {
                if (i == idIndex) {
                    System.out.println("变量" + (i + 1) + "：\t" + (0) + "\t" + ((int) node.nodeResult[idIndex]));
                } else {
                    System.out.println("变量" + (i + 1) + "：\t" + (node.nodeResult[i]) + "\t" + (node.nodeResult[i]));
                }
            }
        }
        //设置变量分支-右支
        else {
            for (int i = 0; i < node.nodeResult.length; i++) {
                if (i == idIndex) {
                    x[idIndex].setLB((int) node.nodeResult[idIndex] + 1);
                    x[idIndex].setUB(Double.MAX_VALUE);
                } else {
                    x[i].setLB(node.nodeResult[i]);
                    x[i].setUB(node.nodeResult[i]);
                }
            }
            System.out.println("非整数变量范围：" + ((int) node.nodeResult[idIndex] + 1) + "\t" + Double.MAX_VALUE);
            System.out.println("右支模型：");
            System.out.println(model);
            for (int i = 0; i < node.nodeResult.length; i++) {
                if (i == idIndex) {
                    System.out.println("变量" + (i + 1) + "：\t" + ((int) node.nodeResult[idIndex] + 1) + "\t" + (Double.MAX_VALUE));
                } else {
                    System.out.println("变量" + (i + 1) + "：\t" + (node.nodeResult[i]) + "\t" + (node.nodeResult[i]));
                }
            }
        }
    }

    private static double getProductionTime(int line, int product) {
        double[][] productionTimes = {
                {2.0, 3.0, 4.0, 2.5},
                {3.0, 2.0, 3.0, 4.0},
                {4.0, 3.0, 1.0, 2.0}
        };
        return productionTimes[line][product];
    }

    public static void main(String[] args) throws IloException {
        ModelData data = new ModelData();
        BranchAndBoundDemo lp = new BranchAndBoundDemo(data);
        lp.branchAndBoundMethod();
    }
}


