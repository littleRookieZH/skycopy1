package hybridalgorithm.aoawithskyline;

/**
 * @author xzbz
 * @create 2023-08-28 10:15
 */
public class ParameterInfo {
    public static final double C1 = 2;
    public static final double C2 = 6;
    public static final double C3 = 0.9;
    public static final double C4 = 0.5;
    // 迭代次数
    public static final int maxIterations = 1000;

    // 标准化参数上界
    public static final double Normalization_U = 0.9;
    // 标准化参数下界
    public static final double Normalization_L = 0.1;
    // 加速度上界
    public static final double Upper_Bound = 10;
    // 加速度下界
    public static final double Lower_Bound = -10;
    // 序列首部交换系数
    public static final double Swap_Parameter = 0.01;
    // 交换的邻域个数
    public static final int Neighbor_Exchange_Number = 6;
    public static final int Single_Rule_Number = 10;
    // 种群个数
    public static final int Materials_No = Single_Rule_Number * 4;
}
