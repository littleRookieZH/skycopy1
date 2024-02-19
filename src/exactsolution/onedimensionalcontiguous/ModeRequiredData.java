package exactsolution.onedimensionalcontiguous;

import com.commonfunction.CommonToolClass;
import com.pointset.DefectBlockSize;
import com.pointset.ToolClass;
import com.twodimension.TargetData;
import exactsolution.dualccm1.Ccm1;
import ilog.concert.IloException;
import exactsolution.blockpreprocessing.PreprocessBlock;
import exactsolution.pointprocessing.DefPointData;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author hao
 * @description: 模型数据类
 * @date 2023/7/5 20:07
 */
public class ModeRequiredData {
    /**
     * @description 原料板尺寸，只有宽度和高度
     */
    public int[] oriSize;

//    public int lowerBoundHeight;
    /**
     * @description
     * 预处理之后的尺寸
     * 目标块尺寸, 需要初始化   w  h
     */
    public int[][] targetBlockSize;
    /**
     * @description
     * 原尺寸
     * 目标块尺寸, 需要初始化   w  h
     */
    int[][] blockSize;
    /**
     * @description 目标块数量
     */
    public int targetNumber;
    /**
     * @description 缺陷块的位置
     */
    public int[][] defectSize;
    /**
     * @description 缺陷块的数量
     */
    public int defNum;
    /**
     * @description 宽度离散点集
     */
    public int[] widthPoints;

    /**
     * @description 每一行缺陷块的总长度
     */
    public int[] defectWidth;
    // 宽度：每一个离散点的具体可放置情况
    public boolean[][] widthPlacedPoints;
    /**
     * @description 高度离散点集
     */
    public int[] heightPoints;

    // 高度：每一个离散点的具体可放置情况
    public boolean[][] heightPlacedPoints;
    public int[][] resultPoints;
    //计算高度最小
    public int minHeight;
    // 计算宽度最小
    public int minWidth;
    public CheckCplex check;
    /**
     * @description 不可行解的约束集合
    */
//    MultiValueMap<Integer, IloLinearNumExpr> exprListMap;
    List<int[][]> exprList;
    /**
     * @description  过滤器
     */
    HashSet<Integer> filterHashSet;
    HashSet<Integer> hashSetMinInfeases;
    HashSet<Integer> hashSetNonInfeases;
    public final static double CBP_TIMELIMIT = 600;
    public final static double X_CHECK_TIMELIMIT = 600;
    public final static double INFEASIBLE_TIMELIMIT = 120;
    public final static double LIFTCUT_TIMELIMIT = 60;
    public final static double Two_Dimension_Scheme = 1200;
    public final static long Solution_Times = 1200;



    public ModeRequiredData(DefPointData defPointData, TargetData targetData, DefectBlockSize dBS) {
        oriSize = targetData.oriSize; // 不变
        defNum = targetData.defNum;  // 不变
        targetNumber = targetData.targetNumber; // 不变
        widthPoints = defPointData.widthPoints;  // 不变
        widthPlacedPoints  = defPointData.widthPlacedPoints;
        heightPlacedPoints = defPointData.heightPlacedPoints;
        defectSize = targetData.defPoints; // 不变
        heightPoints = defPointData.heightPoints; // 变化
        defectWidth = dBS.defectWidth;  // 每一行缺陷块的总长度  变化
    }

    public ModeRequiredData() {

    }

    /**
     * @description
     * 初始化模型数据
     * @author  hao
     * @date    2023/7/24 16:40
     * @param path
     * @param rLayout
     * @return ModeRequiredData
    */
    public ModeRequiredData initModel1(String path, TargetData rLayout) throws IloException, FileNotFoundException {

        int[][] blockSize = ModeRequiredData.initBlocks(rLayout);
        CommonToolClass commonToolClass = new CommonToolClass();
        int[][] copyBlocks = commonToolClass.assistArrayRec(blockSize);
        // 计算下界
        int ccm1 = 0;
        try {
            ccm1 = Ccm1.regionLowerBound(rLayout, rLayout.defectiveBlocksSize);
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
        // 缺陷块的最大高度
        int maxDefHeight = DefPointData.maxSize(rLayout.defPoints, 3);
        // 目标块的最大高度
        int maxBlocksHeight = DefPointData.maxSize(copyBlocks, 1);
        // 确定最低下界
        rLayout.oriSize[1] = maxBlocksHeight > maxDefHeight ? Math.max(ccm1,maxBlocksHeight) : Math.max(ccm1,maxDefHeight);
//        rLayout.oriSize[1] = 31;
        DefPointData discretePoints = new DefPointData();
        // 预处理
        PreprocessBlock preprocessBlock = new PreprocessBlock();
        // 计算宽度最小
        int minWidth = DefPointData.minSize(blockSize, 0);
        //计算高度最小
        int minHeight = DefPointData.minSize(blockSize, 1);
        // 处理：blockSize的宽和高 和 oriSize[0]
        preprocessBlock.processBlock(minHeight, minWidth, rLayout.oriSize, rLayout.defPoints, blockSize);
        // DefPointData
        // 得到X方向离散点
        // DefPointData：widthPlacedPoints（每个点的放置情况）、widthPoints、leftModelW、rightModelW 被赋初值
        discretePoints.widthSet(rLayout.oriSize[0], minWidth, blockSize, rLayout.defPoints);
        // 得到Y方向离散点
        discretePoints.heightSet(rLayout.oriSize[1], minHeight, blockSize, rLayout.defPoints);
        // 初始化 DefectBlockSize
        DefectBlockSize defectSize = new DefectBlockSize();
        // 计算每一行的宽度
        defectSize.defectColumnWidth(discretePoints.heightPoints, rLayout.defPoints);
        // 计算每一列的高度
        ModeRequiredData modeRequiredData = new ModeRequiredData(discretePoints, rLayout, defectSize);
        // 初始化目标块（整合：种类和数量）
        modeRequiredData.targetBlockSize = blockSize;
        modeRequiredData.blockSize = copyBlocks;
        // 确定下界
        modeRequiredData.minWidth = minWidth;
        modeRequiredData.minHeight = minHeight;
        modeRequiredData.check = new CheckCplex();
//        modeRequiredData.exprListMap = new LinkedMultiValueMap<>();
        modeRequiredData.exprList = new ArrayList<>();
        modeRequiredData.filterHashSet = new HashSet<Integer>();
        modeRequiredData.hashSetMinInfeases = new HashSet<Integer>();
        modeRequiredData.hashSetNonInfeases = new HashSet<Integer>();
        return modeRequiredData;
    }

    // 随着高度的变化需要重新更新信息  操作
    public ModeRequiredData improveModel1(ModeRequiredData modeRequiredData, TargetData rLayout) throws IloException, FileNotFoundException {
        // 初始化目标块
        int[][] blockSize = ModeRequiredData.initBlocks(rLayout);
        // DefPointData
        DefPointData discretePoints = new DefPointData();
        // 预处理
        PreprocessBlock preprocessBlock = new PreprocessBlock();
        // 计算宽度最小
        int minWidth = modeRequiredData.minWidth;
        //计算高度最小
        int minHeight = modeRequiredData.minHeight;
        // 处理：blockSize 和 oriSize[0]
        // 应该只用提升高度即可
        preprocessBlock.processBlock(minHeight, minWidth, rLayout.oriSize, rLayout.defPoints, blockSize);
        modeRequiredData.targetBlockSize = blockSize;
        // 得到Y方向离散点
        discretePoints.heightSet(rLayout.oriSize[1], minHeight, modeRequiredData.targetBlockSize, rLayout.defPoints);
        // 初始化 DefectBlockSize
        DefectBlockSize defectSize = new DefectBlockSize();
        // 计算每一行的宽度
        defectSize.defectColumnWidth(discretePoints.heightPoints, rLayout.defPoints);
        modeRequiredData.updataInfo(discretePoints, rLayout, defectSize);
        modeRequiredData.exprList = new ArrayList<>();
        modeRequiredData.filterHashSet = new HashSet<Integer>();
        modeRequiredData.hashSetMinInfeases = new HashSet<Integer>();
        modeRequiredData.hashSetNonInfeases = new HashSet<Integer>();
        return modeRequiredData;
    }

    // 随着高度的变化需要重新更新信息  修改
    public void updataInfo(DefPointData defPointData, TargetData targetData, DefectBlockSize dBS) {
        heightPoints = defPointData.heightPoints; // 变化
        heightPlacedPoints = defPointData.heightPlacedPoints;
        System.out.println("----更新信息---");
        int y = 0;
        for (int i = 0; i < targetBlockSize.length; i++) {
            boolean exist = true;
            for (int j = 0; j < heightPoints.length; j++) {
                y = heightPoints[j];
                if (heightPlacedPoints[i][y]) {
                        exist = false;
                }
            }
            if(exist){
                System.out.println("有的点没有离散点---" + i);
            }
        }
        defectWidth = dBS.defectWidth;  // 变化

    }

    /**
     * @param rLayout
     * @return int[][]
     * @description 目标块集合；宽，长
     * @author hao
     * @date 2023/7/17 10:37
     */
    public static int[][] initBlocks(TargetData rLayout) {
        int[][] rec = new int[rLayout.targetNumber][2];
        int temp = 0;
        for (int i = 0; i < rLayout.targetBlockSize.length; i++) {
            for (int j = 0; j < rLayout.targetBlockSize[i][2]; j++) {
                rec[temp][0] = rLayout.targetBlockSize[i][0];
                rec[temp][1] = rLayout.targetBlockSize[i][1];
                temp++;
            }
        }
        return rec;
    }
}























