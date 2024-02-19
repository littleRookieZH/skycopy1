package com.universalalgorithm;

import com.twodimension.SkyLine;

public class TransRule {
    /**
     * @description 定义初始温度
     */
    private static final double INITIAL_TEMPERATURE = 100.0;
    /**
     * @description 温度下降系数
     */
    private static final double TEMPERATURE_DECAY = 0.2;
    /**
     * @description 定义终止温度
     */
    private static final double TEMPERATURE_BREAK = 0.1;
    /**
     * @description 初始多点变异概率
     */
    private static final double MULTIPOINT_VARIATION = 0.9;
    /**
     * @description 多点变异的变异概率；一般为0.1 -- 0.5
     */
    private static final double ATTENUATION_PARAMETER = 0.2;
    /**
     * @description 定义一个当前温度，初始值100
     */
    public static double temperature;
    /**
     * @description 初始多点变异概率，初始值0.9
     */
    private double multipoint_Variation;

    public TransRule(double multipoint_Variation) {
        this.multipoint_Variation = multipoint_Variation;
    }

    public TransRule() {
    }

    public double getMultipoint_Variation() {
        return multipoint_Variation;
    }

    public void setMultipoint_Variation(double multipoint_Variation) {
        this.multipoint_Variation = multipoint_Variation;
    }

    /**
     * @return int[]
     * @description 以一定概率接受劣解
     * @author hao
     * @date 2023/5/29 23:15
     */
    public static boolean acceptsInferior(SkyLine currentLine, SkyLine newLine, int iteration) {
        double var = 5;
        // 目标函数是求最小，因此差解是高度较大的
        double deltaFitness = var * (newLine.skyHeight - currentLine.skyHeight);
        double probability = Math.exp(-deltaFitness  / temperature);
        System.out.println("iteration   " + iteration + "    temperature     " + temperature + "   deltaFitness     " + deltaFitness + "     probability   " + probability);
//        temperature *= TEMPERATURE_DECAY;
        temperature = INITIAL_TEMPERATURE * Math.exp(-TEMPERATURE_DECAY * iteration / 10);
        // 当前温度大于设定温度，考虑以一定概率接受劣解
        while (temperature > TEMPERATURE_BREAK && deltaFitness <= 60) {
            // 降低温度
//
            if (Math.random() < probability) {
                // 接受劣解--指rec，降低标准，解不再是当前最优解
                return true;
            }
            return false;
        }
        // 不接受劣解，标准不变，解依旧是当前最优解
        return false;
    }

    /**
     * @param currentSolution
     * @return int[]
     * @description 单点变异
     * @author hao
     * @date 2023/5/29 23:15
     */
    public void singlePointVariation(int[][] currentSolution) {
        int size = currentSolution.length;
        int a = (int) (Math.random() * size);
        int b = (int) (Math.random() * size);
        while (b == a) {
            b = (int) (Math.random() * size);
        }
        int[] temp = currentSolution[a];
        currentSolution[a] = currentSolution[b];
        currentSolution[b] = temp;
    }

    /**
     * @description  多点变异
     * @author  hao
     * @date    2023/5/29 23:15
     * @param currentSolution
     * @param iteration
     * @return int[]
     */
    public  void multipointVariation(TransRule multipoint, int[][] currentSolution, int iteration) {
        int times = 0;
        double probability = multipoint.getMultipoint_Variation();
        int size = currentSolution.length;
        if(Math.random() < probability){
            while (times++ < 2) {
                int a = (int)(Math.random() * size);
                int b = (int)(Math.random() * size);
                while(b == a){
                    b = (int)(Math.random() * size);
                }
                int[] temp = currentSolution[a];
                currentSolution[a] = currentSolution[b];
                currentSolution[b] = temp;
            }
        }
        probability = MULTIPOINT_VARIATION * Math.exp(-ATTENUATION_PARAMETER / iteration);
        multipoint.setMultipoint_Variation(probability);
    }
}
