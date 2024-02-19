package hybridalgorithm.aoawithskyline;//package main.java.hybridalgorithm.aoawithskyline;
//
///**
// * @author xzbz
// * @create 2023-08-28 10:09
// */
//
//import main.java.com.commonfunction.ParallelTask;
//import main.java.com.pointset.ToolClass;
//import main.java.com.twodimension.IndividualObject;
//import main.java.com.twodimension.SkyLine;
//import main.java.com.twodimension.TargetData;
//import main.java.com.universalalgorithm.AOAMaxHeapSort;
//import main.java.com.universalalgorithm.MaxHeapSort;
//
//import java.io.FileNotFoundException;
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.ForkJoinPool;
//
///**
// * 混合启发式：AOA 与 skyline
// * 根据论文测试 Ci的参考值为：2 6 2 0.5
// * C1  2
// * C2  6
// * C3  2
// * C4  0.5
// */
//public class HybridAlgorithmAOAback {
//
//
//    // Materials_no,Max_iter,fobj, dim,lb,ub,C3,C4
//    // materialsNo：对象个数
//    // maxIter：最大迭代次数
//    // fobj： 计算适应度的函数
//    // dim：维度10
//    // lb：位置下限；--- 0；
//    // ub：位置上限；--- N；目标块的个数 - 1
//    // 位置用目标块的索引表示；
//    public SkyLine algorithmAOA(TargetData targetData, String path) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
//        // AOA 初始化
//        AlgorithmParameter[] algorithmParameters = initAOA(targetData, path);
//
//        int times = 0;
//        int swapLengthLimit = targetData.targetNumber - (int) (targetData.targetNumber * ParameterInfo.Swap_Parameter);
//        int swapTimes = (int) (targetData.targetNumber * ParameterInfo.Swap_Parameter);
//        double transferFactor = 0;
//        double densityFactor = 0;
//        // 调用skyline，求解高度，决定当前解是否接受
//        ParallelTask parallelTask = new ParallelTask();
//        // 开启并行设计
//        ForkJoinPool forkJoinPool = new ForkJoinPool();
//
//        // 初始化解，求解skyline
//        parallelTask.parallelAOA(forkJoinPool, algorithmParameters, targetData);
//        // 迭代
//        while (ParameterInfo.maxIterations > times) {
//            // 拷贝一个临时数组，用于AOA算法，对结果好的接受，结果不好的保持原解
//            AlgorithmParameter[] tempParameters = copyAOAObject(algorithmParameters);
//            int bestFitnessIndex = findBestFitness(algorithmParameters);
//            // 更新体积、密度
//            tempParameters = updateVolumeDensities(tempParameters, bestFitnessIndex);
//            // 计算因子
//            double factor1 = ((double) (times - ParameterInfo.maxIterations)) / ParameterInfo.maxIterations;
//            transferFactor = Math.exp(factor1);
//            double factor2 = ((double) (ParameterInfo.maxIterations - times)) / ParameterInfo.maxIterations;
//            densityFactor = Math.exp(factor2) - (double) (times / ParameterInfo.maxIterations);
//            // 记录最大最小加速度  [0]：最小值；[1]：最大值
//            double[] recordAcc = new double[2];
//            // 根据计算因子更新加速度
//            if (transferFactor <= 0.5) {
//                tempParameters = upDateAccFirstStage(tempParameters, recordAcc);
//            } else {
//                tempParameters = upDateAccSecondStage(tempParameters, recordAcc, bestFitnessIndex);
//            }
//
//            // 标准化加速度
//            tempParameters = standardAcc(tempParameters, recordAcc);
//            // differenceIndex 的信息如下：key：iValue；value：[0]：iIndex, [1]：randValue
//            HashMap<Integer, int[]> differenceIndex = new HashMap<>();
//            //  HashMap<Integer, Integer> numIndex = new HashMap<>();
//            List<int[]> numIndex = new ArrayList<>();
//            // 更新位置信息
//            if (transferFactor <= 0.5) {
//                for (int i = 0; i < tempParameters.length; i++) {
//                    int randomIndex = new Random().nextInt(ParameterInfo.Materials_No);
//                    // DF
//                    int difference = statisticDifferentPoints(tempParameters[randomIndex].skyLine.rectangularSequence, tempParameters[i].skyLine.rectangularSequence, differenceIndex, numIndex);
//                    System.out.println("randomIndex " + randomIndex + "\t" + "i" + i);
//                    // K
//                    double multiple = ParameterInfo.C1 * (new Random().nextDouble()) * tempParameters[i].standardAcc * densityFactor;
//                    if (difference > 0) {
//                        if (multiple >= 1) {
//                            // 随机选择一段矩形序列交换到序列首部
//                            swapSequence(tempParameters[i], swapTimes, swapLengthLimit);
//                            tempParameters[i].setMethodName("交换到序列首部");
//                        } else {
//                            // 随机选择t个矩形交换位置，保证差异性
//                            int swapNumbers = difference - (int) (difference * multiple);
//                            // HashMap<Integer, int[]> differenceIndex, List<int[]> numIndex, int[][] sourceRec, int swapNumbers
//                            reduceDifference(differenceIndex, numIndex, tempParameters[i].skyLine.rectangularSequence, swapNumbers);
//                            tempParameters[i].setMethodName("保证差异性");
//                        }
//                    } else {
//                        // 随机交换两个
//                        exchangedRec(tempParameters[i].skyLine.rectangularSequence);
//                        tempParameters[i].setMethodName("随机交换两个");
//                    }
//                }
//            } else {
//                for (int i = 0; i < tempParameters.length; i++) {
//                    // DF
//                    int difference = statisticDifferentPoints(tempParameters[bestFitnessIndex].skyLine.rectangularSequence, tempParameters[i].skyLine.rectangularSequence, differenceIndex, numIndex);
//                    // K
//                    double multiple = ParameterInfo.C2 * (new Random().nextDouble()) * tempParameters[i].standardAcc * densityFactor;
//                    if (difference > 0) {
//                        if (multiple > 1) {
//                            // 随机选择一段矩形序列发生邻域交换
//                            neighborSwitching(tempParameters[i].skyLine.rectangularSequence);
//                            tempParameters[i].setMethodName("邻域交换");
//                        } else {
//                            // 随机选择t个矩形交换位置，保证差异性
//                            int swapNumbers = difference - (int) (difference * multiple);
//                            // HashMap<Integer, int[]> differenceIndex, List<int[]> numIndex, int[][] sourceRec, int swapNumbers
//                            reduceDifference(differenceIndex, numIndex, tempParameters[i].skyLine.rectangularSequence, swapNumbers);
//                            tempParameters[i].setMethodName("保证差异性");
//                        }
//                    } else {
//                        // 随机交换两个
//                        exchangedRec(tempParameters[i].skyLine.rectangularSequence);
//                        tempParameters[i].setMethodName("随机交换两个");
//                    }
//                }
//            }
//            // 开启并行设计
//            parallelTask.parallelAOA(forkJoinPool, tempParameters, targetData);
//            System.out.println("--------   times  --------" + times);
//            // 更新结果
//            updateResult(algorithmParameters, tempParameters);
//            for (int i = 0; i < tempParameters.length; i++) {
//                System.out.println(tempParameters[i].methodName + "\t\t" + tempParameters[i].skyLine.skyHeight);
//            }
//            ++times;
//        }
//        // 关闭 ForkJoinPool
//        forkJoinPool.shutdown();
//        // 查找最优解
//        int bestFitnessIndex = findBestFitness(algorithmParameters);
//        return algorithmParameters[bestFitnessIndex].skyLine;
//    }
//
//    // AOA算法的初始化
//    public AlgorithmParameter[] initAOA(TargetData targetData, String path) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
//        // 初始化信息
//        targetData.initData(path);
//
//        // 创建一个SkyLine对象用于存储数据
//        List<SkyLine> skyLines = new ArrayList<>();
//        // 创建一个数组用于存放
//        // 根据四种排序规则生成 高度h
//        // 面积规则
//        int[][] rectArea = MaxHeapSort.heapSortAreaAOA(targetData.targetSize);
//        initialRandomSequences(skyLines, rectArea);
//
//        // 高度规则
//        int[][] rectHeight = MaxHeapSort.heapSortHeightAOA(targetData.targetSize);
//        initialRandomSequences(skyLines, rectHeight);
//
//        // 宽度规则
//        int[][] rectWidth = MaxHeapSort.heapSortWitdhAOA(targetData.targetSize);
//        initialRandomSequences(skyLines, rectWidth);
//
//        // 周长规则
//        int[][] rectPerimeter = MaxHeapSort.heapSortPerimeterAOA(targetData.targetSize);
//        initialRandomSequences(skyLines, rectPerimeter);
//
//        AlgorithmParameter[] tempParameters = new AlgorithmParameter[skyLines.size()];
//        for (int i = 0; i < skyLines.size(); i++) {
//            tempParameters[i] = initSingleObject(skyLines.get(i));
//        }
//        return tempParameters;
//    }
//
//    // 根据已有的序列，生成初始随机序列
//    public void initialRandomSequences(List<SkyLine> skyLines, int[][] rectSequences) {
//        for (int i = 0; i < ParameterInfo.Single_Rule_Number - 1; i++) {
//            int[][] resultArraySeries = ToolClass.copyTwoDim(rectSequences);
//            int a = 0;
//            int b = 0;
//            while (a == b) {
//                a = new Random().nextInt(rectSequences.length);
//                b = new Random().nextInt(rectSequences.length);
//            }
//            int[] temp = resultArraySeries[a];
//            resultArraySeries[a] = resultArraySeries[b];
//            resultArraySeries[b] = temp;
//            int[][] resultArrayIndiv = initPositions(resultArraySeries);
//            IndividualObject tempSkyLine = new IndividualObject(resultArrayIndiv, resultArraySeries);
//            skyLines.add(new SkyLine(tempSkyLine));
//        }
//        int[][] resultArrayIndiv1 = initPositions(rectSequences);
//        IndividualObject tempSkyLine1 = new IndividualObject(resultArrayIndiv1, rectSequences);
//        skyLines.add(new SkyLine(tempSkyLine1));
//    }
//
//    // 初始化 skyline、体积、密度、加速度
//    public AlgorithmParameter initSingleObject(SkyLine skyLine) {
//        int length = skyLine.individualObjects.individualPosition.length;
//        double[] den = initDenVol(length);
//        double[] vol = initDenVol(length);
//        double[] acc = initAcc(length);
//        return new AlgorithmParameter(skyLine, den, vol, acc, new double[length]);
//    }
//
//    // 初始化 密度和体积
//    public double[] initDenVol(int length){
//        double[] den = new double[length];
//        for (int i = 0; i < length; i++) {
//            den[i] = new Random().nextDouble();
//        }
//        return den;
//    }
//
//    // 初始化加速度
//    public double[] initAcc(int length){
//        double[] acc = new double[length];
//        for (int i = 0; i < length; i++) {
//            acc[i] = ParameterInfo.Lower_Bound + (new Random()).nextDouble() * (ParameterInfo.Upper_Bound - ParameterInfo.Lower_Bound);
//        }
//        return acc;
//    }
//
//    // 查找最大适应度
//    public int findBestFitness(AlgorithmParameter[] tempParameters) {
//        int minIndex = 0;
//        for (int i = 1; i < tempParameters.length; i++) {
//            minIndex = tempParameters[i].skyLine.skyHeight > tempParameters[minIndex].skyLine.skyHeight ? minIndex : i;
//        }
//        return minIndex;
//    }
//
//    // 更新加速度  TF ≤ 0.5
//    public AlgorithmParameter[] upDateAccFirstStage(AlgorithmParameter[] tempParameters, double[] recordAcc) {
//        int randIndex = new Random().nextInt(ParameterInfo.Materials_No);
//        double[] accTemp = new double[tempParameters.length];
//        for (int i = 0; i < tempParameters.length; i++) {
//            accTemp[i] = (tempParameters[randIndex].den + tempParameters[randIndex].vol * tempParameters[randIndex].acc) / (tempParameters[i].den * tempParameters[i].vol);
//        }
//        // 记录最小值
//        recordAcc[0] = accTemp[0];
//        // 记录最大值
//        recordAcc[1] = accTemp[0];
//        for (int i = 0; i < tempParameters.length; i++) {
//            tempParameters[i].acc = accTemp[i];
//            recordAcc[0] = Math.min(recordAcc[0], accTemp[i]);
//            recordAcc[1] = Math.max(recordAcc[1], accTemp[i]);
//        }
//        return tempParameters;
//    }
//
//    // 更新加速度  TF > 0.5
//    public AlgorithmParameter[] upDateAccSecondStage(AlgorithmParameter[] tempParameters, double[] recordAcc, int bestFitnessIndex) {
//        double[] accTemp = new double[tempParameters.length];
//        for (int i = 0; i < tempParameters.length; i++) {
//            accTemp[i] = (tempParameters[bestFitnessIndex].den + tempParameters[bestFitnessIndex].vol * tempParameters[bestFitnessIndex].acc) / (tempParameters[i].den * tempParameters[i].vol);
//        }
//        // 记录最小值
//        recordAcc[0] = accTemp[0];
//        // 记录最大值
//        recordAcc[1] = accTemp[0];
//        for (int i = 0; i < tempParameters.length; i++) {
//            tempParameters[i].acc = accTemp[i];
//            recordAcc[0] = Math.min(recordAcc[0], accTemp[i]);
//            recordAcc[1] = Math.max(recordAcc[1], accTemp[i]);
//        }
//        return tempParameters;
//    }
//
//    // 更新体积、密度
//    public AlgorithmParameter[] updateVolumeDensities(AlgorithmParameter[] tempParameters, int bestFitnessIndex) {
//        double randomNumber = new Random().nextDouble();
//        double bestDen = tempParameters[bestFitnessIndex].den;
//        double bestVol = tempParameters[bestFitnessIndex].vol;
//        for (int i = 0; i < tempParameters.length; i++) {
//            tempParameters[i].den = tempParameters[i].den + randomNumber * (bestDen - tempParameters[i].den);
//            tempParameters[i].vol = tempParameters[i].vol + randomNumber * (bestVol - tempParameters[i].vol);
//        }
//        return tempParameters;
//    }
//
//    // 标准化加速度
//    public AlgorithmParameter[] standardAcc(AlgorithmParameter[] tempParameters, double[] recordAcc) {
//        for (int i = 0; i < tempParameters.length; i++) {
//            tempParameters[i].standardAcc = ParameterInfo.Normalization_U * (tempParameters[i].acc - recordAcc[0]) / (recordAcc[1] - recordAcc[0]) + ParameterInfo.Normalization_L;
//        }
//        return tempParameters;
//    }
//
//    // 计算不同点
//    public int statisticDifferentPoints(int[][] randomRec, int[][] sourceRec, HashMap<Integer, int[]> differenceIndex, List<int[]> numIndex) {
//        int times = 0;
//        for (int i = 0; i < randomRec.length; i++) {
//            // key：iValue；value：[0]：iIndex, [1]：randValue
//            if (randomRec[i][0] != sourceRec[i][0]) {
//                int[] tempArray = new int[2];
//                // iIndex
//                tempArray[0] = i;
//                // randValue
//                tempArray[1] = randomRec[i][0];
//                differenceIndex.put(sourceRec[i][0], tempArray);
//                numIndex.add(new int[]{(new Random().nextInt(randomRec.length) + 1), sourceRec[i][0]});
//                ++times;
//            }
//        }
//        return times;
//    }
//
//    // 随机选择一段矩形序列交换到序列首部
//    public void swapSequence(AlgorithmParameter tempVar, int swapTimes, int swapLengthLimit) {
//        int randIndex = new Random().nextInt(swapLengthLimit) + 1;
//        int[][] recInfo = tempVar.skyLine.rectangularSequence;
//        int[] tempArray;
//        for (int i = 0; i < swapTimes; i++) {
//            tempArray = recInfo[i];
//            recInfo[i] = recInfo[randIndex + i];
//            recInfo[randIndex + i] = tempArray;
//        }
//    }
//
//    // 交换两个矩形减少差异性
//    public void reduceDifference(HashMap<Integer, int[]> differenceIndex, List<int[]> numIndex, int[][] sourceRec, int swapNumbers) {
//        if (numIndex.size() == 0) {
//            return;
//        }
//        // 将list转为数组，numIndex包括 [0]:产生的随机值，[1]:sourceValue
//        int[][] tempArray = transferArray(numIndex);
//        // 按指定索引排序：以 [0] 为标准，升序排列
//        AOAMaxHeapSort aoaMaxHeapSort = new AOAMaxHeapSort();
//        aoaMaxHeapSort.heapSort(tempArray);
//        for (int i = 0; i < swapNumbers; i++) {
//            // 查找交换的对象,确定交换的索引
//            int sourceKey = tempArray[i][1];
//            int[] sourceValue = differenceIndex.get(sourceKey);
//            int randKey = sourceValue[1];
//            int swapSourceIndex = sourceValue[0];
//            int[] randValue = differenceIndex.get(randKey);
//            int swapRandIndex = randValue[0];
//            if (sourceKey == randKey) {
//                continue;
//            }
//            // 交换rec的指
//            int[] temp = sourceRec[swapSourceIndex];
//            sourceRec[swapSourceIndex] = sourceRec[swapRandIndex];
//            sourceRec[swapRandIndex] = temp;
//            // 更新hash表
//            differenceIndex.put(sourceKey, randValue);
//            differenceIndex.put(randKey, sourceValue);
//        }
//    }
//
//    // list转为二维数组
//    public static int[][] transferArray(List<int[]> numIndex) {
//
//        int[][] resultArray = new int[0][];
//        try {
//            resultArray = new int[numIndex.size()][numIndex.get(0).length];
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        for (int i = 0; i < numIndex.size(); i++) {
//            resultArray[i] = numIndex.get(i);
//        }
//        return resultArray;
//    }
//
//    // 邻域交换
//    public void neighborSwitching(int[][] sourceRec) {
//        int randIndex = new Random().nextInt(sourceRec.length - 2 * ParameterInfo.Neighbor_Exchange_Number + 1);
//        int swapIndex = randIndex + ParameterInfo.Neighbor_Exchange_Number;
//        for (int i = 0; i < ParameterInfo.Neighbor_Exchange_Number; i++) {
//            int[] temp = sourceRec[randIndex];
//            try {
//                sourceRec[randIndex] = sourceRec[swapIndex];
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            sourceRec[swapIndex] = temp;
//            ++swapIndex;
//            ++randIndex;
//        }
//    }
//
//    // 随机选择两个交换
//    public void exchangedRec(int[][] sourceRec) {
//        int a = 0;
//        int b = 0;
//        while (a == b) {
//            a = new Random().nextInt(sourceRec.length);
//            b = new Random().nextInt(sourceRec.length);
//        }
//        int[] temp = sourceRec[a];
//        sourceRec[a] = sourceRec[b];
//        sourceRec[b] = temp;
//    }
//
//    // 拷贝一个临时数组，用于AOA算法
//    public AlgorithmParameter[] copyAOAObject(AlgorithmParameter[] tempParameters) {
//        AlgorithmParameter[] algorithmParameters = new AlgorithmParameter[tempParameters.length];
//        for (int i = 0; i < tempParameters.length; i++) {
//            // SkyLine skyLine, double den, double vol, double acc, double standardAcc
//            algorithmParameters[i] = new AlgorithmParameter(tempParameters[i].skyLine, tempParameters[i].den, tempParameters[i].vol, tempParameters[i].acc, tempParameters[i].standardAcc);
//        }
//        return algorithmParameters;
//    }
//
//    // 更新结果
//    public void updateResult(AlgorithmParameter[] algorithmParameters, AlgorithmParameter[] tempParameters) {
//        for (int i = 0; i < algorithmParameters.length; i++) {
//            if (algorithmParameters[i].skyLine.skyHeight > tempParameters[i].skyLine.skyHeight) {
//                algorithmParameters[i] = tempParameters[i];
//            }
//        }
//    }
//
//    // 生成AOA个体的初始位置
//    public int[][] initPositions(int[][] array) {
//        int[][] tempArray = new int[array.length][2];
//        for (int i = 0; i < array.length; i++) {
//                tempArray[i][0] = array[i][0];
//                tempArray[i][1] = i;
//        }
//        return tempArray;
//    }
//
//    public static void main(String[] args) {
////        int[][] arr = {{1, 2, 3}, {2, 3, 4}};
////        int[] temp = arr[0];
////        arr[0] = arr[1];
////        arr[1] = temp;
////        System.out.println(arr[0][0]);
////        int[] arr1 = {1, 2, 3};
////        int[] arr2 = {2, 3, 4};
////        int[] temp1 = arr1;
////        arr1 = arr2;
////        arr1[0] = 8;
////        System.out.println(temp1[0]);
//     /*   HashMap<Integer, Integer> hash1 = new HashMap<>();
//        hash1.put(6, 1);
//        hash1.put(2, 2);
//        hash1.put(3, 3);
//        hash1.put(4, 3);
//        hash1.put(5, 3);
//        for (Map.Entry<Integer, Integer> entry : hash1.entrySet()) {
//            System.out.println(entry.getKey());
//        }*/
//        ArrayList<int[]> arrayList = new ArrayList<>();
//        arrayList.add(new int[]{1, 2});
//        arrayList.add(new int[]{2, 2});
//        arrayList.add(new int[]{3, 2});
//        arrayList.add(new int[]{4, 2});
//        int[][] ints = transferArray(arrayList);
//        ints[0][0] = 10;
//    }
//}
//
