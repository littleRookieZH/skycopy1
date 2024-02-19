package hybridalgorithm.aoawithskyline;// package hybridalgorithm.aoawithskyline;
//
// /**
//  * @author xzbz
//  * @create 2023-08-28 10:09
//  */
//
// import com.commonfunction.ParallelTask;
// import com.pointset.ToolClass;
// import com.twodimension.IndividualObject;
// import com.twodimension.SkyLine;
// import com.twodimension.TargetData;
// import com.universalalgorithm.MaxHeapSort;
//
// import java.io.FileNotFoundException;
// import java.lang.reflect.InvocationTargetException;
// import java.util.*;
// import java.util.concurrent.ForkJoinPool;
// /**
//  * 混合启发式：AOA 与 skyline
//  * 根据论文测试 Ci的参考值为：2 6 2 0.5
//  * C1  2
//  * C2  6
//  * C3  2
//  * C4  0.5
//  */
// public class HybridAlgorithmAOA {
//
//
//     // Materials_no,Max_iter,fobj, dim,lb,ub,C3,C4
//     // materialsNo：对象个数
//     // maxIter：最大迭代次数
//     // fobj： 计算适应度的函数
//     // dim：维度10
//     // lb：位置下限；--- 0；
//     // ub：位置上限；--- N；目标块的个数 - 1
//     // 位置用目标块的索引表示；
//     public SkyLine algorithmAOA(TargetData targetData, String path) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
//         // AOA 初始化
//         AlgorithmParameter[] algorithmParameters = initAOA(targetData, path);
//
//         int times = 0;
//
//         double transferFactor = 0;
//         double densityFactor = 0;
//         // 调用skyline，求解高度，决定当前解是否接受
//         ParallelTask parallelTask = new ParallelTask();
//         // 开启并行设计
//         ForkJoinPool forkJoinPool = new ForkJoinPool();
//
//         // 初始化解，求解skyline
//         parallelTask.parallelAOA(forkJoinPool, algorithmParameters, targetData);
//         long startTimes = System.currentTimeMillis();
//
//         // 迭代
//         while ((System.currentTimeMillis() - startTimes) / 30 < 2000) {
//             // 计算因子  TF
//             double factor1 = ((double) (times - ParameterInfo.maxIterations)) / ParameterInfo.maxIterations;
//             transferFactor = Math.exp(factor1);
//             if (transferFactor > 1) {
//                 transferFactor = 1;
//             }
//             // d
//             double factor2 = ((double) (ParameterInfo.maxIterations - times)) / ParameterInfo.maxIterations;
//             densityFactor = Math.exp(factor2) - (double) (times / ParameterInfo.maxIterations);
//
//             // 拷贝一个临时数组，用于AOA算法，对结果好的接受，结果不好的保持原解 -- 有问题
//             // 使用apache commons langs的 SerializationUtils.clone() 深拷贝
//             // AlgorithmParameter[] tempParameters = SerializationUtils.clone(algorithmParameters);
//             int bestFitnessIndex = findBestFitness(algorithmParameters);
//             // 更新体积、密度
//             tempParameters = updateVolumeDensities(tempParameters, bestFitnessIndex);
//             // 记录种群中每个个体的加速度   [0] min    [1] max
//             double[][] recordPopulationAcc = new double[tempParameters.length][2];
//             // 根据计算因子更新加速度
//             if (transferFactor <= 0.75) {
//                 tempParameters = upDateAccFirstStage(recordPopulationAcc, tempParameters);
//             } else {
//                 tempParameters = upDateAccSecondStage(recordPopulationAcc, tempParameters, bestFitnessIndex);
//             }
//
//             // 标准化加速度
//             tempParameters = standardAcc(recordPopulationAcc, tempParameters);
//             // 更新位置信息
//             if (transferFactor <= 0.75) {
//                 for (int i = 0; i < tempParameters.length; i++) {
//                     double[] individualPosition = tempParameters[i].skyLine.individualObjects.individualPosition;
//                     double[] newPosition = new double[individualPosition.length];
//                     double[] standardAcc = tempParameters[i].standardAcc;
//                     for (int j = 0; j < individualPosition.length; j++) {
//                         int randIndex = new Random().nextInt(individualPosition.length);
//                         double rand = new Random().nextDouble();
//                         newPosition[j] = individualPosition[j] + ParameterInfo.C1 * rand * standardAcc[j] * densityFactor * (individualPosition[randIndex] - individualPosition[j]);
//                     }
//                     // 更新位置
//                     tempParameters[i].skyLine.individualObjects.individualPosition = newPosition;
//                     // 连续离散化
//                     tempParameters[i].skyLine.individualObjects.jobPermutation = continuousDiscretization(newPosition);
//                 }
//             } else {
//                 double[] bestPosition = tempParameters[bestFitnessIndex].skyLine.individualObjects.individualPosition;
//                 for (int i = 0; i < tempParameters.length; i++) {
//                     double[] individualPosition = tempParameters[i].skyLine.individualObjects.individualPosition;
//                     double[] newPosition = new double[bestPosition.length];
//                     double[] standardAcc = tempParameters[i].standardAcc;
//                     if (i == bestFitnessIndex) {
//                         newPosition = bestPosition;
//                     } else {
//                         for (int j = 0; j < bestPosition.length; j++) {
//                             // p
//                             double p = 2 * new Random().nextDouble() - ParameterInfo.C4;
//                             // f
//                             int f = (p > 0.5) ? -1 : 1;
//                             // T
//                             double t = (ParameterInfo.C3 * transferFactor < 1) ? ParameterInfo.C3 * transferFactor : 1;
// //                        double t = ParameterInfo.C3 * transferFactor;
//                             double rand = new Random().nextDouble();
//                             newPosition[j] = bestPosition[j] + f * ParameterInfo.C2 * rand * standardAcc[j] * densityFactor * (t * bestPosition[j] - individualPosition[j]);
//                         }
//                     }
//                     // 更新位置
//                     tempParameters[i].skyLine.individualObjects.individualPosition = newPosition;
//                     // 连续离散化
//                     tempParameters[i].skyLine.individualObjects.jobPermutation = continuousDiscretization(newPosition);
//                 }
//             }
//
//             // 开启并行设计
//             parallelTask.parallelAOA(forkJoinPool, tempParameters, targetData);
//             System.out.println("--------   times  --------" + times);
//             // 更新结果
//             updateResult(algorithmParameters, tempParameters);
//             for (int i = 0; i < tempParameters.length; i++) {
//                 System.out.println(tempParameters[i].methodName + "\t\t" + tempParameters[i].skyLine.skyHeight);
//             }
//             ++times;
//         }
//         // 关闭 ForkJoinPool
//         forkJoinPool.shutdown();
//         // 查找最优解
//         int bestFitnessIndex = findBestFitness(algorithmParameters);
//         return algorithmParameters[bestFitnessIndex].skyLine;
//     }
//
//     // AOA算法的初始化
//     public AlgorithmParameter[] initAOA(TargetData targetData, String path) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
//         // 初始化信息
//         targetData.initData(path);
//
//         // 创建一个SkyLine对象用于存储数据
//         List<SkyLine> skyLines = new ArrayList<>();
//         // 创建一个数组用于存放
//         // 根据四种排序规则生成 高度h
//         // 面积规则
//         int[][] rectArea = MaxHeapSort.heapSortAreaAOA(targetData.targetSize);
//         // 初始化种群 和 IndividualObject
//         initialRandomSequences(skyLines, rectArea);
//
//         // 高度规则
//         int[][] rectHeight = MaxHeapSort.heapSortHeightAOA(targetData.targetSize);
//         initialRandomSequences(skyLines, rectHeight);
//
//         // 宽度规则
//         int[][] rectWidth = MaxHeapSort.heapSortWitdhAOA(targetData.targetSize);
//         initialRandomSequences(skyLines, rectWidth);
//
//         // 周长规则
//         int[][] rectPerimeter = MaxHeapSort.heapSortPerimeterAOA(targetData.targetSize);
//         initialRandomSequences(skyLines, rectPerimeter);
//
//         AlgorithmParameter[] tempParameters = new AlgorithmParameter[skyLines.size()];
//         for (int i = 0; i < skyLines.size(); i++) {
//             // 初始化 skyline、体积、密度、加速度
//             tempParameters[i] = initSingleObject(skyLines.get(i));
//         }
//         return tempParameters;
//     }
//
//     // 根据已有的序列，生成初始随机序列
//     public void initialRandomSequences(List<SkyLine> skyLines, int[][] rectSequences) {
//         for (int i = 0; i < ParameterInfo.Single_Rule_Number - 1; i++) {
//             int[][] resultArraySeries = ToolClass.copyTwoDim(rectSequences);
//             int a = 0;
//             int b = 0;
//             while (a == b) {
//                 a = new Random().nextInt(rectSequences.length);
//                 b = new Random().nextInt(rectSequences.length);
//             }
//             int[] temp = resultArraySeries[a];
//             resultArraySeries[a] = resultArraySeries[b];
//             resultArraySeries[b] = temp;
//             // 初始化 位置
//             double[] indivPositions = initPositions(resultArraySeries);
//             // 初始化 permutation
//             int[] indivPermutation = initPermutation(resultArraySeries);
//             IndividualObject tempSkyLine = new IndividualObject(indivPositions, indivPermutation, resultArraySeries);
//             skyLines.add(new SkyLine(tempSkyLine));
//         }
//         // 将原序列也加入
//         // 初始化 位置
//         double[] indivPositions1 = initPositions(rectSequences);
//         // 初始化 permutation
//         int[] indivPermutation1 = initPermutation(rectSequences);
//         IndividualObject tempSkyLine1 = new IndividualObject(indivPositions1, indivPermutation1, rectSequences);
//         skyLines.add(new SkyLine(tempSkyLine1));
//     }
//
//     // 初始化 skyline、体积、密度、加速度
//     public AlgorithmParameter initSingleObject(SkyLine skyLine) {
//         int length = skyLine.individualObjects.individualPosition.length;
//         double[] den = initDenVol(length);
//         double[] vol = initDenVol(length);
//         double[] acc = initAcc(length);
//         return new AlgorithmParameter(skyLine, den, vol, acc, new double[length]);
//     }
//
//     // 初始化 密度和体积
//     public double[] initDenVol(int length) {
//         double[] den = new double[length];
//         for (int i = 0; i < length; i++) {
//             den[i] = new Random().nextDouble();
//         }
//         return den;
//     }
//
//     // 初始化加速度
//     public double[] initAcc(int length) {
//         double[] acc = new double[length];
//         for (int i = 0; i < length; i++) {
//             acc[i] = ParameterInfo.Lower_Bound + (new Random()).nextDouble() * (ParameterInfo.Upper_Bound - ParameterInfo.Lower_Bound);
//         }
//         return acc;
//     }
//
//     // 查找最大适应度
//     public int findBestFitness(AlgorithmParameter[] tempParameters) {
//         int minIndex = 0;
//         for (int i = 1; i < tempParameters.length; i++) {
//             minIndex = tempParameters[i].skyLine.skyHeight > tempParameters[minIndex].skyLine.skyHeight ? minIndex : i;
//         }
//         return minIndex;
//     }
//
//     // 更新加速度  TF ≤ 0.5
//     public AlgorithmParameter[] upDateAccFirstStage(double[][] recordPopulationAcc, AlgorithmParameter[] tempParameters) {
//         int accLength = tempParameters[0].acc.length;
//         for (int i = 0; i < tempParameters.length; i++) {
//             int randIndex = new Random().nextInt(ParameterInfo.Materials_No);
//             double[] accTemp = new double[accLength];
//             for (int j = 0; j < accTemp.length; j++) {
//                 accTemp[j] = (tempParameters[randIndex].den[j] + tempParameters[randIndex].vol[j] * tempParameters[randIndex].acc[j])
//                         / (tempParameters[i].den[j] * tempParameters[i].vol[j]);
//             }
//             tempParameters[i].acc = accTemp;
//             findMinMaxValue(accTemp, recordPopulationAcc[i]);
//         }
//         return tempParameters;
//     }
//
//     // 两个向量相乘
//     public double[] arrayMultiple(double[] temp1, double[] temp2) {
//         double[] res = new double[temp1.length];
//         for (int i = 0; i < temp1.length; i++) {
//             res[i] = temp1[i] * temp2[i];
//         }
//         return res;
//     }
//
//     // 查找个体加速度的最大值和最小值
//     public double[] findMinMaxValue(double[] temp, double[] record) {
//         double minValue = temp[0];
//         double maxValue = temp[0];
//         for (int i = 0; i < temp.length; i++) {
//             minValue = Math.min(minValue, temp[i]);
//             maxValue = Math.max(maxValue, temp[i]);
//         }
//         record[0] = minValue;
//         record[1] = maxValue;
//         return record;
//     }
//
//
//     // 更新加速度  TF > 0.5
//     public AlgorithmParameter[] upDateAccSecondStage(double[][] recordPopulationAcc, AlgorithmParameter[] tempParameters, int bestFitnessIndex) {
//         int accLength = tempParameters[0].acc.length;
//         for (int i = 0; i < tempParameters.length; i++) {
//             double[] accTemp = new double[accLength];
//             for (int j = 0; j < accTemp.length; j++) {
//                 accTemp[j] = (tempParameters[bestFitnessIndex].den[j] + tempParameters[bestFitnessIndex].vol[j] * tempParameters[bestFitnessIndex].acc[j])
//                         / (tempParameters[i].den[j] * tempParameters[i].vol[j]);
//             }
//             tempParameters[i].acc = accTemp;
//             findMinMaxValue(accTemp, recordPopulationAcc[i]);
//         }
//         return tempParameters;
//     }
//
//     // 更新体积、密度
//     public AlgorithmParameter[] updateVolumeDensities(AlgorithmParameter[] tempParameters, int bestFitnessIndex) {
//         double randomNumber = new Random().nextDouble();
//         double[] bestDen = tempParameters[bestFitnessIndex].den;
//         double[] bestVol = tempParameters[bestFitnessIndex].vol;
//         for (int i = 0; i < tempParameters.length; i++) {
//             for (int j = 0; j < bestDen.length; j++) {
//                 tempParameters[i].den[j] = tempParameters[i].den[j] + randomNumber * (bestDen[j] - tempParameters[i].den[j]);
//                 tempParameters[i].vol[j] = tempParameters[i].vol[j] + randomNumber * (bestVol[j] - tempParameters[i].vol[j]);
//             }
//         }
//         return tempParameters;
//     }
//
//     // 标准化加速度
//     public AlgorithmParameter[] standardAcc(double[][] recordAcc, AlgorithmParameter[] tempParameters) {
//         int accLength = tempParameters[0].acc.length;
//         for (int i = 0; i < tempParameters.length; i++) {
//             double[] accTemp = new double[accLength];
//             for (int j = 0; j < accTemp.length; j++) {
//                 accTemp[j] = ParameterInfo.Normalization_U * (tempParameters[i].acc[j] - recordAcc[i][0]) / (recordAcc[i][1] - recordAcc[i][0]) + ParameterInfo.Normalization_L;
//             }
//             tempParameters[i].standardAcc = accTemp;
//         }
//         return tempParameters;
//     }
//
//     // list转为二维数组
//     public static int[][] transferArray(List<int[]> numIndex) {
//
//         int[][] resultArray = new int[0][];
//         try {
//             resultArray = new int[numIndex.size()][numIndex.get(0).length];
//         } catch (Exception e) {
//             throw new RuntimeException(e);
//         }
//         for (int i = 0; i < numIndex.size(); i++) {
//             resultArray[i] = numIndex.get(i);
//         }
//         return resultArray;
//     }
//
//     // 拷贝一个临时数组，用于AOA算法
//     public AlgorithmParameter[] copyAOAObject(AlgorithmParameter[] tempParameters) {
//         AlgorithmParameter[] algorithmParameters = new AlgorithmParameter[tempParameters.length];
//         for (int i = 0; i < tempParameters.length; i++) {
//             // SkyLine skyLine, double den, double vol, double acc, double standardAcc
//             algorithmParameters[i] = new AlgorithmParameter(tempParameters[i].skyLine, tempParameters[i].den, tempParameters[i].vol, tempParameters[i].acc, tempParameters[i].standardAcc);
//         }
//         return algorithmParameters;
//     }
//
//     // 更新结果
//     public void updateResult(AlgorithmParameter[] algorithmParameters, AlgorithmParameter[] tempParameters) {
//         for (int i = 0; i < algorithmParameters.length; i++) {
//             if (algorithmParameters[i].skyLine.skyHeight > tempParameters[i].skyLine.skyHeight) {
//                 algorithmParameters[i] = tempParameters[i];
//             }
//         }
//     }
//
//
//     // 生成AOA个体的初始位置
//     public double[] initPositions(int[][] array) {
//         double[] tempArray = new double[array.length];
//         for (int i = 0; i < array.length; i++) {
//             tempArray[i] = array[i][0];
//         }
//         return tempArray;
//     }
//
//     public int[] initPermutation(int[][] array) {
//         int[] tempArray = new int[array.length];
//         for (int i = 0; i < array.length; i++) {
//             tempArray[i] = array[i][0];
//         }
//         return tempArray;
//     }
//
//
//     // 连续离散化
//     public int[] continuousDiscretization(double[] individualPosition) {
//         // 生成红黑树
//         TreeMap<Double, Integer> temp = new TreeMap<>();
//         for (int i = 0; i < individualPosition.length; i++) {
//             temp.put(individualPosition[i], i);
//         }
//         int[] jobPermutation = new int[individualPosition.length];
//         int i = 0;
//         for (int index : temp.values()) {
//             jobPermutation[index] = i++;
//         }
//         return jobPermutation;
//     }
//
//
//     public static void main(String[] args) {
//         HybridAlgorithmAOA hybridAlgorithmAOA = new HybridAlgorithmAOA();
//         double[] temp = new double[]{1.2, 4.1, 2.1, 8.5, 5.6};
//         int[] doubles = hybridAlgorithmAOA.continuousDiscretization(temp);
//         System.out.println(Arrays.toString(doubles));
//     }
// }
//
