package com.twodimensiondata;

import com.commonfunction.CommonToolClass;
import com.twodimension.SkyLine;
import com.twodimension.TargetData;
import com.universalalgorithm.MaxHeapSort;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AdaptiveData {
    public int height;
    public String methodName;

    // 存放解的结果
    public SkyLineResultData skyLineResultData;

    public AdaptiveData() {
    }

    public AdaptiveData(int height, String methodName) {
        this.height = height;
        this.methodName = methodName;
    }

    public AdaptiveData(int height, String methodName, SkyLineResultData skyLineResultData) {
        this.height = height;
        this.methodName = methodName;
        this.skyLineResultData = skyLineResultData;
    }

    @Override
    public String toString() {
        return "AdaptiveData{" +
                "height=" + height +
                ", methodName='" + methodName + '\'' +
                '}';
    }

    /**
     * 对height属性进行排序
     *
     * @param adaptiveData
     */
    public static void sortData(AdaptiveData[] adaptiveData) {
        int i = 0;
        int j = 8;
       Arrays.sort(adaptiveData,i*j, (i++)*j + j, new Comparator<AdaptiveData>() {
           @Override
           public int compare(AdaptiveData a1, AdaptiveData a2) {
               // 按照 height 属性进行降序排序
               return Integer.compare(a2.height, a1.height);
           }
       });
        // Arrays.sort(adaptiveData,i*j, (i++)*j + j, new Comparator<AdaptiveData>() {
        //     @Override
        //     public int compare(AdaptiveData a1, AdaptiveData a2) {
        //         // 按照 height 属性进行降序排序
        //         return Integer.compare(a2.height, a1.height);
        //     }
        // });
        Arrays.sort(adaptiveData,i*j, (i++)*j + j, new Comparator<AdaptiveData>() {
            @Override
            public int compare(AdaptiveData a1, AdaptiveData a2) {
                // 按照 height 属性进行降序排序
                int checkComparison = Integer.compare(a1.skyLineResultData.check, a2.skyLineResultData.check);
                return checkComparison != 0 ? checkComparison : Integer.compare(a2.height, a1.height);
            }
        });
        // Arrays.sort(adaptiveData,i*j, (i++)*j + j, new Comparator<AdaptiveData>() {
        //     @Override
        //     public int compare(AdaptiveData a1, AdaptiveData a2) {
        //         // 按照 height 属性进行降序排序
        //         int checkComparison = Integer.compare(a1.skyLineResultData.check, a2.skyLineResultData.check);
        //         return checkComparison != 0 ? checkComparison : Integer.compare(a2.height, a1.height);
        //     }
        // });
    }

    /**
     * 自适应选择方法
     *
     * @param adaptiveData
     * @return
     */
    public AdaptiveData adaptiveSelection(AdaptiveData[] adaptiveData, int methodNum) {
        methodNum /= 2;
        double t = adaptiveData[7].height > adaptiveData[15].height ? 0.4 : 0.6;

        int i;
        double randomValue1 = Math.random();
        double randomValue2 = Math.random();
        double probability = 0;

        if(randomValue1 < t){
            i = 1;
        }else{
            i = 9;
        }

        int j = 1;
        for (; i <= adaptiveData.length; i++) {
            probability += (double) j * 2 / (methodNum * (methodNum + 1));
            if (probability >= randomValue2) {
                break;
            }
            ++j;
        }
        return adaptiveData[i - 1];
    }

    public SkyLineResultData initialAdaptive(List<TargetData> targetDataList, String path) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
        // 初始化信息
        TargetData targetData = targetDataList.get(0);
        targetData.initData(path);
        MaxHeapSort maxHeapSort = new MaxHeapSort();
        // 保存初始解 -- 用于随机交换时的比较
        Map<String, SkyLine> provideInitialSolution = new HashMap<>();
        // 保存最终解
        Map<String, SkyLineResultData> saveFinalSolution = new HashMap<>();
        // 存储排序规则
        Map<String, int[][]> storageCollation = new HashMap<>();
        // 用于区分是靠上放置还是靠下放置
        Map<String, String> hashMapTargetDataName = new HashMap<>();

        CommonToolClass commonToolClass = new CommonToolClass();

        long startTimes = System.currentTimeMillis() / 1000;

        // 随机交换 或者 4种规则 生成的序列
        int[][] rectArea = maxHeapSort.heapSortArea(targetData.targetSize);
        int[][] rectHeight = maxHeapSort.heapSortHeight(targetData.targetSize);
        int[][] rectWidth = maxHeapSort.heapSortWitdh(targetData.targetSize);
        int[][] rectPerimeter = maxHeapSort.heapSortPerimeter(targetData.targetSize);
        storageCollation.put("Area", rectArea);
        storageCollation.put("Height", rectHeight);
        storageCollation.put("Width", rectWidth);
        storageCollation.put("Perimeter", rectPerimeter);
        addMethodAndRule(hashMapTargetDataName);

        // 添加结果 两组，一组8个
        int methodNum = 16;
        int index = 0;
        AdaptiveData[] adaptiveData = new AdaptiveData[methodNum];

        // 存储靠下放置的解和方法名
        HashMap<Integer, String> mapLower = new HashMap<>();
        // 根据四种排序规则生成
        // 左下面积规则
        adaptiveData[index++] = lowerLeftArea(rectArea, targetData, provideInitialSolution, saveFinalSolution);
        // 左下高度规则
        adaptiveData[index++] = lowerLeftHeight(rectHeight, targetData, provideInitialSolution, saveFinalSolution);
        // 左下宽度规则
        adaptiveData[index++] = lowerLeftWidth(rectWidth, targetData, provideInitialSolution, saveFinalSolution);
        // 左下周长规则
        adaptiveData[index++] = lowerLeftPerimeter(rectPerimeter, targetData, provideInitialSolution, saveFinalSolution);


        // 初始化不同方向放置的信息
        // 靠右侧放置
        TargetData copyRightCorner = commonToolClass.deepCopyRightCorner(targetData);
        targetDataList.add(copyRightCorner);
        // 右下面积规则
        adaptiveData[index++] = lowerRightArea(rectArea, copyRightCorner, provideInitialSolution, saveFinalSolution);
        // 右下高度规则
        adaptiveData[index++] = lowerRightHeight(rectHeight, copyRightCorner, provideInitialSolution, saveFinalSolution);
        // 右下宽度规则
        adaptiveData[index++] = lowerRightWidth(rectWidth, copyRightCorner, provideInitialSolution, saveFinalSolution);
        // 右下周长规则
        adaptiveData[index++] = lowerRightPerimeter(rectPerimeter, copyRightCorner, provideInitialSolution, saveFinalSolution);

        // 获取当前最优解
        // SkyLineResultData bestLine = saveFinalSolution.get("lowerRightArea");
        // for (Map.Entry<String, SkyLineResultData> entry : saveFinalSolution.entrySet()) {
        //     SkyLineResultData currentLine = entry.getValue();
        //     bestLine = currentLine.skyLine.skyHeight > bestLine.skyLine.skyHeight ? bestLine : currentLine;
        // }
        SkyLineResultData bestLine = getOptimalSolution(saveFinalSolution);
        saveFinalSolution.put("bestLine", bestLine);

        // 存储靠上放置的解和方法名
        HashMap<Integer, String> mapUpper = new HashMap<>();
        // // 从上往下求，靠左放置
        TargetData copyUpperLeftCorner = commonToolClass.deepCopyUpperLeftCorner(targetData, bestLine.skyLine.skyHeight - 1);
        targetDataList.add(copyUpperLeftCorner);
        // 左上面积规则
        adaptiveData[index++] = upperLeftArea(rectArea, copyUpperLeftCorner, saveFinalSolution);
        // 左上高度规则
        adaptiveData[index++] = upperLeftHeight(rectHeight, copyUpperLeftCorner, saveFinalSolution);
        // 左上宽度规则
        adaptiveData[index++] = upperLeftWidth(rectWidth, copyUpperLeftCorner, saveFinalSolution);
        // 左上周长规则
        adaptiveData[index++] = upperLeftPerimeter(rectPerimeter, copyUpperLeftCorner, saveFinalSolution);

        // 从上往下求，靠右放置
        TargetData copyUpperRightCorner = commonToolClass.deepCopyUpperRightCorner(targetData, targetData.oriSize[0], bestLine.skyLine.skyHeight - 1);
        targetDataList.add(copyUpperRightCorner);
        // 右上面积规则
        adaptiveData[index++] = upperRightArea(rectArea, copyUpperRightCorner, saveFinalSolution);
        // 右上高度规则
        adaptiveData[index++] = upperRightHeight(rectHeight, copyUpperRightCorner, saveFinalSolution);
        // 右上宽度规则
        adaptiveData[index++] = upperRightWidth(rectWidth, copyUpperRightCorner, saveFinalSolution);
         // 右上周长规则
        adaptiveData[index++] = upperRightPerimeter(rectPerimeter, copyUpperRightCorner, saveFinalSolution);

//        添加元素
//        int i = 0;
//        int j = 8;
//        for (Map.Entry<String, SkyLineResultData> entry : saveFinalSolution.entrySet()) {
//            if (!Objects.equals(entry.getKey(), "bestLine")) {
//                SkyLineResultData skyLineResultData = entry.getValue();
//                if (hashMapTargetDataName.containsKey(entry.getKey())) {
//                    adaptiveData[j++] = new AdaptiveData(skyLineResultData.currentHeight, entry.getKey(), skyLineResultData);
//                } else {
//                    adaptiveData[i++] = new AdaptiveData(skyLineResultData.currentHeight, entry.getKey(), skyLineResultData);
//                }
//            }
//        }

        try {
            sortData(adaptiveData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        SkyLineResultData resultData = getOptimalSolution(saveFinalSolution);
        saveFinalSolution.put("bestLine", resultData);

        AdaptiveData adaptiveSelection = null;
        try {
            adaptiveSelection = adaptiveSelection(adaptiveData, methodNum);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long endTimes = System.currentTimeMillis() / 1000;
        int times = 0;

        while ((endTimes - startTimes) < 60) {
            ++times;

            // System.out.println("adaptiveSelection.methodName  " + adaptiveSelection.methodName);
            // if ("upperRightWidth".equals(adaptiveSelection.methodName)) {
            //     System.out.println(88);
            // }

            SkyLineResultData currentSkyData = adaptiveMethod(hashMapTargetDataName, provideInitialSolution, saveFinalSolution, adaptiveSelection.methodName, startTimes);

            // if (adaptiveSelection.height < currentSkyData.currentHeight) {
            //     System.out.println(9);
            // }

            adaptiveSelection.height = Math.min(adaptiveSelection.height, currentSkyData.currentHeight);
            String targetDataName = hashMapTargetDataName.get(adaptiveSelection.methodName);
            if (Objects.equals(targetDataName, "UpperLeft")) {
                commonToolClass.improveUpperLeftCorner(targetDataList.get(0), targetDataList.get(2));
            } else if (Objects.equals(targetDataName, "UpperRight")) {
                commonToolClass.improveUpperRightCorner(targetDataList.get(0), targetDataList.get(3));
            }
            endTimes = System.currentTimeMillis() / 1000;
            sortData(adaptiveData);

            // for (AdaptiveData element : adaptiveData) {
            //     System.out.println(element);
            // }

            adaptiveSelection = adaptiveSelection(adaptiveData, methodNum);
            // 更新最优解
            SkyLineResultData bestResultData = saveFinalSolution.get("bestLine");
            if(bestResultData.currentHeight > currentSkyData.currentHeight){
                saveFinalSolution.put("bestLine", currentSkyData);
            }
            // System.out.println("bestLine   " + saveFinalSolution.get("bestLine").currentHeight + " times：" + times);
            // System.out.println();
        }
        System.out.println("bestLine   " + saveFinalSolution.get("bestLine").currentHeight + " times：" + times);
        return saveFinalSolution.get("bestLine");
    }

    public void addMethodAndRule(Map<String, String> hashMapTargetDataName) {
        hashMapTargetDataName.put("upperLeftArea", "UpperLeft");
        hashMapTargetDataName.put("upperLeftHeight", "UpperLeft");
        hashMapTargetDataName.put("upperLeftWidth", "UpperLeft");
        hashMapTargetDataName.put("upperLeftPerimeter", "UpperLeft");

        hashMapTargetDataName.put("upperRightWidth", "UpperRight");
        hashMapTargetDataName.put("upperRightPerimeter", "UpperRight");
        hashMapTargetDataName.put("upperRightArea", "UpperRight");
        hashMapTargetDataName.put("upperRightHeight", "UpperRight");
    }

    public SkyLineResultData adaptiveMethod(Map<String, String> hashMapTargetDataName, Map<String, SkyLine> provideInitialSolution, Map<String, SkyLineResultData> saveFinalSolution, String methodName, double startTime) throws InvocationTargetException, IllegalAccessException {
        SkyLineResultData skyLineResultData = saveFinalSolution.get(methodName);
        // 这里是否需要两个？
        TargetAitInfo aitInfo = new TargetAitInfo();
        // 上一次的结果
        SkyLine lastSkyline = skyLineResultData.skyLine;
        lastSkyline.skyHeight = skyLineResultData.currentHeight;
        // 本次的结果
        // initialRuleSolution: 对应规则的初始解

        // if ("upperRightWidth".equals(methodName)) {
        //     System.out.println(99);
        // }

        // 靠上放置
        if (hashMapTargetDataName.containsKey(methodName)) {
            // bestLine 用于更新检查高度
            SkyLineResultData bestLine = saveFinalSolution.get("bestLine");

            SkyLine initialRuleSolution = new SkyLine();
            initialRuleSolution = initialRuleSolution.placeRec(aitInfo, skyLineResultData.sortRule, skyLineResultData.targetData, initialRuleSolution);


            SkyLine currentSkyLine = initialRuleSolution.randomLS(aitInfo, initialRuleSolution, skyLineResultData.targetData, skyLineResultData.sortRule, startTime);

            // System.out.println("currentSkyLine.skyHeight  = " + currentSkyLine.skyHeight + "skyLineResultData.targetData.checkHeight   " + skyLineResultData.targetData.checkHeight);

            updateSkylineRes(skyLineResultData, bestLine, currentSkyLine);

            // System.out.println("currentSkyLine.skyHeight  = " + currentSkyLine.skyHeight + "skyLineResultData.targetData.checkHeight   " + skyLineResultData.targetData.checkHeight);

        } else {
            SkyLine initialRuleSolution = provideInitialSolution.get(methodName);
            SkyLine currentSkyLine = lastSkyline.randomLS(aitInfo, initialRuleSolution, skyLineResultData.targetData, skyLineResultData.sortRule, startTime);

            // 这里如果靠下更新，应该直接比较
            skyLineResultData.skyLine = lastSkyline.skyHeight < currentSkyLine.skyHeight ? lastSkyline : currentSkyLine;
            skyLineResultData.currentHeight = skyLineResultData.skyLine.skyHeight;
        }

        return skyLineResultData;
    }

    public AdaptiveData lowerLeftArea(int[][] rectArea, TargetData targetData, Map<String, SkyLine> provideInitialSolution, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineA = new SkyLine();
        TargetAitInfo aitInfoArea = new TargetAitInfo();
        aitInfoArea.setName("lowerLeftArea");
        skyLineA = skyLineA.placeRec(aitInfoArea, rectArea, targetData, skyLineA);
        SkyLineResultData skyLineAResultData = new SkyLineResultData(skyLineA, targetData, rectArea);
//        skyLines.put("lowerLeftArea", skyLineAResultData);
        skyLineAResultData.currentHeight = skyLineA.skyHeight;
        // 添加最终解
        saveFinalSolution.put("lowerLeftArea", skyLineAResultData);
        // 添加初始解
        provideInitialSolution.put("lowerLeftArea", skyLineA);
        mapLower.put(skyLineAResultData.currentHeight, "lowerLeftArea");
        return new AdaptiveData(skyLineAResultData.currentHeight, "lowerLeftArea", skyLineAResultData);
    }

    public AdaptiveData lowerLeftHeight(int[][] rectHeight, TargetData targetData, Map<String, SkyLine> provideInitialSolution, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineH = new SkyLine();
        TargetAitInfo aitInfoHeight = new TargetAitInfo();
        aitInfoHeight.setName("lowerLeftHeight");
        skyLineH = skyLineH.placeRec(aitInfoHeight, rectHeight, targetData, skyLineH);
        SkyLineResultData skyLineHResultData = new SkyLineResultData(skyLineH, targetData, rectHeight);
//        skyLines.put("lowerLeftHeight", skyLineHResultData);
        skyLineHResultData.currentHeight = skyLineH.skyHeight;
        // 添加最终解
        saveFinalSolution.put("lowerLeftHeight", skyLineHResultData);
        // 添加初始解
        provideInitialSolution.put("lowerLeftHeight", skyLineH);
        mapLower.put(skyLineHResultData.currentHeight, "lowerLeftHeight");
        return new AdaptiveData(skyLineHResultData.currentHeight, "lowerLeftHeight", skyLineHResultData);
    }

    public AdaptiveData lowerLeftWidth(int[][] rectWidth, TargetData targetData, Map<String, SkyLine> provideInitialSolution, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineW = new SkyLine();
        TargetAitInfo aitInfoWidth = new TargetAitInfo();
        aitInfoWidth.setName("lowerLeftWidth");
        skyLineW = skyLineW.placeRec(aitInfoWidth, rectWidth, targetData, skyLineW);
        SkyLineResultData skyLineWResultData = new SkyLineResultData(skyLineW, targetData, rectWidth);
//        skyLines.put("lowerLeftWidth", skyLineWResultData);
        skyLineWResultData.currentHeight = skyLineW.skyHeight;
        // 添加最终解
        saveFinalSolution.put("lowerLeftWidth", skyLineWResultData);
        // 添加初始解
        provideInitialSolution.put("lowerLeftWidth", skyLineW);
        mapLower.put(skyLineWResultData.currentHeight, "lowerLeftWidth");
        return new AdaptiveData(skyLineWResultData.currentHeight, "lowerLeftWidth", skyLineWResultData);
    }

    public AdaptiveData lowerLeftPerimeter(int[][] rectPerimeter, TargetData targetData, Map<String, SkyLine> provideInitialSolution, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineP = new SkyLine();
        TargetAitInfo aitInfoPerimeter = new TargetAitInfo();
        aitInfoPerimeter.setName("lowerLeftPerimeter");
        skyLineP = skyLineP.placeRec(aitInfoPerimeter, rectPerimeter, targetData, skyLineP);
        SkyLineResultData skyLinePResultData = new SkyLineResultData(skyLineP, targetData, rectPerimeter);
        skyLinePResultData.currentHeight = skyLineP.skyHeight;
        // 添加最终解
        saveFinalSolution.put("lowerLeftPerimeter", skyLinePResultData);
        // 添加初始解
        provideInitialSolution.put("lowerLeftPerimeter", skyLineP);
        mapLower.put(skyLinePResultData.currentHeight, "lowerLeftPerimeter");
        return new AdaptiveData(skyLinePResultData.currentHeight, "lowerLeftPerimeter", skyLinePResultData);
    }

    public AdaptiveData lowerRightArea(int[][] rectArea, TargetData targetData, Map<String, SkyLine> provideInitialSolution, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineA = new SkyLine();
        TargetAitInfo aitInfoArea = new TargetAitInfo();
        aitInfoArea.setName("lowerRightArea");
        skyLineA = skyLineA.placeRec(aitInfoArea, rectArea, targetData, skyLineA);
        SkyLineResultData skyLineAResultData = new SkyLineResultData(skyLineA, targetData, rectArea);
        skyLineAResultData.currentHeight = skyLineA.skyHeight;
        // 添加最终解
        saveFinalSolution.put("lowerRightArea", skyLineAResultData);
        // 添加初始解
        provideInitialSolution.put("lowerRightArea", skyLineA);
        mapLower.put(skyLineAResultData.currentHeight, "lowerRightArea");
        return new AdaptiveData(skyLineAResultData.currentHeight, "lowerRightArea", skyLineAResultData);
    }

    public AdaptiveData lowerRightHeight(int[][] rectHeight, TargetData targetData, Map<String, SkyLine> provideInitialSolution, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineH = new SkyLine();
        TargetAitInfo aitInfoHeight = new TargetAitInfo();
        aitInfoHeight.setName("lowerRightHeight");
        skyLineH = skyLineH.placeRec(aitInfoHeight, rectHeight, targetData, skyLineH);
        SkyLineResultData skyLineHResultData = new SkyLineResultData(skyLineH, targetData, rectHeight);
        skyLineHResultData.currentHeight = skyLineH.skyHeight;
        // 添加最终解
        saveFinalSolution.put("lowerRightHeight", skyLineHResultData);
        // 添加初始解
        provideInitialSolution.put("lowerRightHeight", skyLineH);
        mapLower.put(skyLineHResultData.currentHeight, "lowerRightHeight");
        return new AdaptiveData(skyLineHResultData.currentHeight, "lowerRightHeight", skyLineHResultData);
    }

    public AdaptiveData lowerRightWidth(int[][] rectWidth, TargetData targetData, Map<String, SkyLine> provideInitialSolution, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineW = new SkyLine();
        TargetAitInfo aitInfoWidth = new TargetAitInfo();
        aitInfoWidth.setName("lowerRightWidth");
        skyLineW = skyLineW.placeRec(aitInfoWidth, rectWidth, targetData, skyLineW);
        SkyLineResultData skyLineWResultData = new SkyLineResultData(skyLineW, targetData, rectWidth);
        // 添加最终解
        saveFinalSolution.put("lowerRightWidth", skyLineWResultData);
        // 添加初始解
        provideInitialSolution.put("lowerRightWidth", skyLineW);
        return new AdaptiveData(skyLineWResultData.currentHeight, "lowerRightWidth", skyLineWResultData);
    }

    public AdaptiveData lowerRightPerimeter(int[][] rectPerimeter, TargetData targetData, Map<String, SkyLine> provideInitialSolution, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineP = new SkyLine();
        TargetAitInfo aitInfoPerimeter = new TargetAitInfo();
        aitInfoPerimeter.setName("lowerRightPerimeter");
        skyLineP = skyLineP.placeRec(aitInfoPerimeter, rectPerimeter, targetData, skyLineP);
        SkyLineResultData skyLinePResultData = new SkyLineResultData(skyLineP, targetData, rectPerimeter);
        // 添加最终解
        saveFinalSolution.put("lowerRightPerimeter", skyLinePResultData);
        // 添加初始解
        provideInitialSolution.put("lowerRightPerimeter", skyLineP);
        return new AdaptiveData(skyLinePResultData.currentHeight, "lowerRightPerimeter", skyLinePResultData);
    }

    public AdaptiveData upperLeftArea(int[][] rectArea, TargetData targetData, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineA = new SkyLine();
        TargetAitInfo aitInfoArea = new TargetAitInfo();
        aitInfoArea.setName("upperLeftArea");
        skyLineA = skyLineA.placeRec(aitInfoArea, rectArea, targetData, skyLineA);
        SkyLineResultData skyLineAResultData = new SkyLineResultData(null, targetData, rectArea);
        // 更新值
        SkyLineResultData bestLine = saveFinalSolution.get("bestLine");
        updateSkylineRes(skyLineAResultData, bestLine, skyLineA);
        saveFinalSolution.put("upperLeftArea", skyLineAResultData);
        return new AdaptiveData(skyLineAResultData.currentHeight, "upperLeftArea", skyLineAResultData);
    }

    public AdaptiveData upperLeftHeight(int[][] rectHeight, TargetData targetData, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineH = new SkyLine();
        TargetAitInfo aitInfoHeight = new TargetAitInfo();
        aitInfoHeight.setName("upperLeftHeight");
        skyLineH = skyLineH.placeRec(aitInfoHeight, rectHeight, targetData, skyLineH);
        SkyLineResultData skyLineHResultData = new SkyLineResultData(null, targetData, rectHeight);
        SkyLineResultData bestLine = saveFinalSolution.get("bestLine");
        updateSkylineRes(skyLineHResultData, bestLine, skyLineH);
        saveFinalSolution.put("upperLeftHeight", skyLineHResultData);
        return new AdaptiveData(skyLineHResultData.currentHeight, "upperLeftHeight", skyLineHResultData);
    }

    public AdaptiveData upperLeftWidth(int[][] rectWidth, TargetData targetData, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineW = new SkyLine();
        TargetAitInfo aitInfoWidth = new TargetAitInfo();
        aitInfoWidth.setName("upperLeftWidth");
        skyLineW = skyLineW.placeRec(aitInfoWidth, rectWidth, targetData, skyLineW);
        SkyLineResultData skyLineWResultData = new SkyLineResultData(null, targetData, rectWidth);
        SkyLineResultData bestLine = saveFinalSolution.get("bestLine");
        updateSkylineRes(skyLineWResultData, bestLine, skyLineW);
        saveFinalSolution.put("upperLeftWidth", skyLineWResultData);
        return new AdaptiveData(skyLineWResultData.currentHeight, "upperLeftWidth", skyLineWResultData);
    }

    public AdaptiveData upperLeftPerimeter(int[][] rectPerimeter, TargetData targetData, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineP = new SkyLine();
        TargetAitInfo aitInfoPerimeter = new TargetAitInfo();
        aitInfoPerimeter.setName("upperLeftPerimeter");
        skyLineP = skyLineP.placeRec(aitInfoPerimeter, rectPerimeter, targetData, skyLineP);
        SkyLineResultData skyLinePResultData = new SkyLineResultData(null, targetData, rectPerimeter);
        SkyLineResultData bestLine = saveFinalSolution.get("bestLine");
        updateSkylineRes(skyLinePResultData, bestLine, skyLineP);
        saveFinalSolution.put("upperLeftPerimeter", skyLinePResultData);
        return new AdaptiveData(skyLinePResultData.currentHeight, "upperLeftPerimeter", skyLinePResultData);
    }

    public AdaptiveData upperRightArea(int[][] rectArea, TargetData targetData, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineA = new SkyLine();
        TargetAitInfo aitInfoArea = new TargetAitInfo();
        aitInfoArea.setName("upperRightArea");
        skyLineA = skyLineA.placeRec(aitInfoArea, rectArea, targetData, skyLineA);
        SkyLineResultData skyLineAResultData = new SkyLineResultData(null, targetData, rectArea);
        SkyLineResultData bestLine = saveFinalSolution.get("bestLine");
        updateSkylineRes(skyLineAResultData, bestLine, skyLineA);
        saveFinalSolution.put("upperRightArea", skyLineAResultData);
        return new AdaptiveData(skyLineAResultData.currentHeight, "upperRightArea", skyLineAResultData);
    }

    public AdaptiveData upperRightHeight(int[][] rectHeight, TargetData targetData, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineH = new SkyLine();
        TargetAitInfo aitInfoHeight = new TargetAitInfo();
        aitInfoHeight.setName("upperRightHeight");
        skyLineH = skyLineH.placeRec(aitInfoHeight, rectHeight, targetData, skyLineH);
        SkyLineResultData skyLineHResultData = new SkyLineResultData(null, targetData, rectHeight);
        SkyLineResultData bestLine = saveFinalSolution.get("bestLine");
        updateSkylineRes(skyLineHResultData, bestLine, skyLineH);
        saveFinalSolution.put("upperRightHeight", skyLineHResultData);
        return new AdaptiveData(skyLineHResultData.currentHeight, "upperRightHeight", skyLineHResultData);
    }

    public AdaptiveData upperRightWidth(int[][] rectWidth, TargetData targetData, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineW = new SkyLine();
        TargetAitInfo aitInfoWidth = new TargetAitInfo();
        aitInfoWidth.setName("upperRightWidth");
        skyLineW = skyLineW.placeRec(aitInfoWidth, rectWidth, targetData, skyLineW);
        SkyLineResultData skyLineWResultData = new SkyLineResultData(null, targetData, rectWidth);
        SkyLineResultData bestLine = saveFinalSolution.get("bestLine");
        updateSkylineRes(skyLineWResultData, bestLine, skyLineW);
        saveFinalSolution.put("upperRightWidth", skyLineWResultData);
        return new AdaptiveData(skyLineWResultData.currentHeight, "upperRightWidth", skyLineWResultData);
    }

    public AdaptiveData upperRightPerimeter(int[][] rectPerimeter, TargetData targetData, Map<String, SkyLineResultData> saveFinalSolution) throws InvocationTargetException, IllegalAccessException {
        SkyLine skyLineP = new SkyLine();
        TargetAitInfo aitInfoPerimeter = new TargetAitInfo();
        aitInfoPerimeter.setName("upperRightPerimeter");
        skyLineP = skyLineP.placeRec(aitInfoPerimeter, rectPerimeter, targetData, skyLineP);
        SkyLineResultData skyLinePResultData = new SkyLineResultData(null, targetData, rectPerimeter);
        SkyLineResultData bestLine = saveFinalSolution.get("bestLine");
        updateSkylineRes(skyLinePResultData, bestLine, skyLineP);
        saveFinalSolution.put("upperRightPerimeter", skyLinePResultData);
        return new AdaptiveData(skyLinePResultData.currentHeight, "upperRightPerimeter", skyLinePResultData);
    }

    /**
     * @param lastHeightSky 上一轮解
     * @param minHeightSky  当前所有解的最优解
     */
    public void updateSkylineRes(SkyLineResultData lastHeightSky, SkyLineResultData minHeightSky, SkyLine currentSkyLine) {
        TargetData targetData = lastHeightSky.targetData;
        int checkHeight = targetData.checkHeight;
        int currentHeight = currentSkyLine.skyHeight;
        int minHeight = minHeightSky.currentHeight;
        // System.out.println();
        // System.out.println("---------");
        // System.out.println("checkHeight = " + checkHeight + "     currentHeight = " + currentHeight + "     minHeight = " + minHeight);

        // if (currentHeight <= checkHeight) {
        //     // 下一次检查的高度为本次的最低高度 - 1，必须是减一，不能直接使用最小值替换
        //     if (minHeight > checkHeight) {
        //         lastHeightSky.skyLine.skyHeight = checkHeight;
        //         targetData.oriSize[1] = checkHeight;
        //         // 说明最好的解是：从上往下检查的解
        //         // 做备份，只保留有效的最优解。defPoints会不断变化
        //         targetData.defPointsBack = new CommonToolClass().assistArrayRec(targetData.defPoints);
        //         --targetData.checkHeight;
        //     } else {
        //         targetData.checkHeight = minHeight - 1;
        //     }
        // } else if (minHeight <= checkHeight) {
        //     // 从上往下没有找到较好的解。
        //     targetData.checkHeight = minHeight - 1;
        // }

        int height = checkHeight + 1;
        if (checkHeight >= currentHeight) {
            --height;
            // 可能不需要了

            lastHeightSky.skyLine = currentSkyLine;
            lastHeightSky.currentHeight = checkHeight;
            // System.out.println("checkHeight = " + checkHeight);

            // 可能也没用
            targetData.oriSize[1] = checkHeight;

            // 说明最好的解是：从上往下检查的解
            // 做备份，只保留有效的最优解。defPoints会不断变化
            targetData.defPointsBack = new CommonToolClass().assistArrayRec(targetData.defPoints);
        } else {
            if (lastHeightSky.skyLine == null) {
                lastHeightSky.skyLine = currentSkyLine;
                lastHeightSky.currentHeight = currentHeight;
                lastHeightSky.skyLine.skyHeight = currentHeight;
            }
        }
        height = Math.min(height, minHeight);
        targetData.checkHeight = height - 1;
        // System.out.println();
        // System.out.println("checkHeight = " + targetData.checkHeight + "     currentHeight = " + currentHeight + "     minHeight = " + minHeight);
        // System.out.println("---------");
        // if (lastHeightSky.currentHeight == 0) {
        //     System.out.println(99);
        // }
    }

    public static void main(String[] args) {
        AdaptiveData[] data = {
                new AdaptiveData(5, "method1"),
                new AdaptiveData(2, "method2"),
                new AdaptiveData(9, "method3"),
                new AdaptiveData(1, "method4"),
                new AdaptiveData(6, "method5")
        };

        sortData(data);

        for (AdaptiveData item : data) {
            System.out.println(item);
        }
    }

    public boolean isEqual(SkyLineResultData lastHeightSky) {
        int skyHeight = lastHeightSky.skyLine.skyHeight;
        int height = 0;
        for (List<Integer> integers : lastHeightSky.skyLine.arrayList) {
            System.out.println(integers);
        }
        return false;
    }
    public SkyLineResultData getOptimalSolution(Map<String, SkyLineResultData> saveFinalSolution){
        SkyLineResultData bestLine = saveFinalSolution.get("lowerRightArea");
        for (Map.Entry<String, SkyLineResultData> entry : saveFinalSolution.entrySet()) {
            SkyLineResultData currentLine = entry.getValue();
            bestLine = currentLine.currentHeight > bestLine.currentHeight ? bestLine : currentLine;
        }
        return bestLine;
    }
}
