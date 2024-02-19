package com.commonfunction;

import com.twodimension.SkyLine;
import com.twodimension.TargetData;
import com.twodimensiondata.SkyLineResultData;
import com.twodimensiondata.TargetAitInfo;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AsyncExecution {
    public static void main(String[] args) {
        // 创建一个异步任务1，模拟耗时操作
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                // 模拟耗时操作
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task 1 completed";
        });

        // 创建一个异步任务2，模拟耗时操作
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                // 模拟耗时操作
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task 2 completed";
        });

        // 等待所有异步任务完成
        CompletableFuture.allOf(future1, future2).join();

        // 获取异步任务的结果
        String result1 = future1.join();
        String result2 = future2.join();

        // 输出结果
        System.out.println(result1);
        System.out.println(result2);
    }

    /**
     * 使用异步操作，同时执行四种规则，最后返回一个结果最好的
     *
     * @param skyLines
     * @param targetDataList
     * @param hashMap
     * @return
     */
    public SkyLineResultData parallelAsync(Map<String, SkyLineResultData> skyLines, List<TargetData> targetDataList, Map<String, int[][]> hashMap, double startTime, SkyLineResultData bestLine) {

        TargetData targetData = targetDataList.get(0);

        // 创建一个异步任务2，模拟耗时操作 -- 靠右侧放置
        TargetData targetDataRightCorner = targetDataList.get(1);

        // 创建一个异步任务3，模拟耗时操作 -- 检查当前最好的高度，从上往下并靠左放置是否存在更优解。如果存在接受最优解
        TargetData targetDataUpperLeftCorner = targetDataList.get(2);

        // 创建一个异步任务4，模拟耗时操作 -- 检查当前最好的高度，从上往下并靠左放置是否存在更优解。如果存在接受最优解
        TargetData targetDataUpperRightCorner = targetDataList.get(3);

        // 创建一个异步任务1，模拟耗时操作
        // 面积规则
        CompletableFuture<SkyLineResultData> area = CompletableFuture.supplyAsync(() -> {
            SkyLine areaSky = skyLines.get("Area").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            SkyLine finalResult = null;
            try {
                finalResult = areaSky.randomLS(aitInfoArea, areaSky, targetData, hashMap.get("Area"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetData);
        });

        // 高度规则
        CompletableFuture<SkyLineResultData> height = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine heightSky = skyLines.get("Height").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = heightSky.randomLS(aitInfoArea, heightSky, targetData, hashMap.get("Height"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetData);
        });

        // 宽度规则
        CompletableFuture<SkyLineResultData> width = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine widthSky = skyLines.get("Width").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = widthSky.randomLS(aitInfoArea, widthSky, targetData, hashMap.get("Width"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetData);
        });

        // 周长规则
        CompletableFuture<SkyLineResultData> perimeter = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine perimeterSky = skyLines.get("Perimeter").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = perimeterSky.randomLS(aitInfoArea, perimeterSky, targetData, hashMap.get("Perimeter"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetData);
        });

        // 物品靠右放置
        // 面积规则
        CompletableFuture<SkyLineResultData> areaRightCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine areaSky1 = skyLines.get("Area").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = areaSky1.randomLS(aitInfoArea, areaSky1, targetDataRightCorner, hashMap.get("Area"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataRightCorner);
        });

        // 高度规则
        CompletableFuture<SkyLineResultData> heightRightCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine heightSky = skyLines.get("Height").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = heightSky.randomLS(aitInfoArea, heightSky, targetDataRightCorner, hashMap.get("Height"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataRightCorner);
        });

        // 宽度规则
        CompletableFuture<SkyLineResultData> widthRightCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine widthSky = skyLines.get("Width").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = widthSky.randomLS(aitInfoArea, widthSky, targetDataRightCorner, hashMap.get("Width"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataRightCorner);
        });

        // 周长规则
        CompletableFuture<SkyLineResultData> perimeterRightCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine perimeterSky = skyLines.get("Perimeter").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = perimeterSky.randomLS(aitInfoArea, perimeterSky, targetDataRightCorner, hashMap.get("Perimeter"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataRightCorner);
        });

        // 从上往下，靠左放置，检查当前最优高度是否存在更好的解
        // 面积规则
        CompletableFuture<SkyLineResultData> areaUpperLeftCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine areaSky1 = skyLines.get("Area").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = areaSky1.randomLS(aitInfoArea, areaSky1, targetDataUpperLeftCorner, hashMap.get("Area"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataUpperLeftCorner);
        });

        // 高度规则
        CompletableFuture<SkyLineResultData> heightUpperLeftCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine heightSky = skyLines.get("Height").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = heightSky.randomLS(aitInfoArea, heightSky, targetDataUpperLeftCorner, hashMap.get("Height"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataUpperLeftCorner);
        });

        // 宽度规则
        CompletableFuture<SkyLineResultData> widthUpperLeftCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine widthSky = skyLines.get("Width").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = widthSky.randomLS(aitInfoArea, widthSky, targetDataUpperLeftCorner, hashMap.get("Width"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataUpperLeftCorner);
        });

        // 周长规则
        CompletableFuture<SkyLineResultData> perimeterUpperLeftCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine perimeterSky = skyLines.get("Perimeter").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = perimeterSky.randomLS(aitInfoArea, perimeterSky, targetDataUpperLeftCorner, hashMap.get("Perimeter"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataUpperLeftCorner);
        });

        // 从上往下，靠右放置，检查当前最优高度是否存在更好的解
        // 面积规则
        CompletableFuture<SkyLineResultData> areaUpperRightCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine areaSky1 = skyLines.get("Area").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = areaSky1.randomLS(aitInfoArea, areaSky1, targetDataUpperRightCorner, hashMap.get("Area"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataUpperRightCorner);
        });

        // 高度规则
        CompletableFuture<SkyLineResultData> heightUpperRightCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine heightSky = skyLines.get("Height").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = heightSky.randomLS(aitInfoArea, heightSky, targetDataUpperRightCorner, hashMap.get("Height"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataUpperRightCorner);
        });

        // 宽度规则
        CompletableFuture<SkyLineResultData> widthUpperRightCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine widthSky = skyLines.get("Width").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = widthSky.randomLS(aitInfoArea, widthSky, targetDataUpperRightCorner, hashMap.get("Width"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataUpperRightCorner);
        });

        // 周长规则
        CompletableFuture<SkyLineResultData> perimeterUpperRightCorner = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine perimeterSky = skyLines.get("Perimeter").skyLine;
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = perimeterSky.randomLS(aitInfoArea, perimeterSky, targetDataUpperRightCorner, hashMap.get("Perimeter"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new SkyLineResultData(finalResult, targetDataUpperRightCorner);
        });


        // 等待所有异步任务完成  area,, areaRightCorner
        CompletableFuture.allOf(area, height, width, perimeter, areaRightCorner, heightRightCorner, widthRightCorner, perimeterRightCorner).join();
        // CompletableFuture.allOf(areaRightCorner, heightRightCorner, widthRightCorner, perimeterRightCorner).join();
        // CompletableFuture.allOf(height, width, perimeter).join();
        // CompletableFuture.allOf(area, height, width, perimeter).join();
//        CompletableFuture.allOf(area).join();

        // 获取异步任务的结果
        SkyLineResultData resultArea = area.join();
        SkyLineResultData resultWidth = width.join();
        SkyLineResultData resultHeight = height.join();
        SkyLineResultData resultPerimeter = perimeter.join();

        SkyLineResultData resultAreaRightCorner = areaRightCorner.join();
        SkyLineResultData resultWidthRightCorner = widthRightCorner.join();
        SkyLineResultData resultHeightRightCorner = heightRightCorner.join();
        SkyLineResultData resultPerimeterRightCorner = perimeterRightCorner.join();

        SkyLineResultData resultAreaUpperLeftCorner = areaUpperLeftCorner.join();
        SkyLineResultData resultHeightUpperLeftCorner = heightUpperLeftCorner.join();
        SkyLineResultData resultWidthUpperLeftCorner = widthUpperLeftCorner.join();
        SkyLineResultData resultPerimeterUpperLeftCorner = perimeterUpperLeftCorner.join();

        SkyLineResultData resultAreaUpperRightCorner = areaUpperRightCorner.join();
        SkyLineResultData resultHeightUpperRightCorner = heightUpperRightCorner.join();
        SkyLineResultData resultWidthUpperRightCorner = widthUpperRightCorner.join();
        SkyLineResultData resultPerimeterUpperRightCorner = perimeterUpperRightCorner.join();

        // 先加入集合
        List<SkyLineResultData> listResults = new ArrayList<>();

        listResults.add(resultArea);
        listResults.add(resultWidth);
        listResults.add(resultHeight);
        listResults.add(resultPerimeter);

        listResults.add(resultAreaRightCorner);
        listResults.add(resultWidthRightCorner);
        listResults.add(resultHeightRightCorner);
        listResults.add(resultPerimeterRightCorner);

        // SkyLineResultData minHeightSky = resultArea;

        SkyLineResultData minHeightSky = resultAreaRightCorner;

        for (SkyLineResultData skyResultData : listResults) {
            minHeightSky = minHeightSky.skyLine.skyHeight > skyResultData.skyLine.skyHeight ? skyResultData : minHeightSky;
        }

        // 从上往下靠左放置，检查是否存在更优解
        List<SkyLineResultData> checkResults = new ArrayList<>();
        checkResults.add(resultAreaUpperLeftCorner);
        checkResults.add(resultHeightUpperLeftCorner);
        checkResults.add(resultWidthUpperLeftCorner);
        checkResults.add(resultPerimeterUpperLeftCorner);

        checkResults.add(resultAreaUpperRightCorner);
        checkResults.add(resultHeightUpperRightCorner);
        checkResults.add(resultWidthUpperRightCorner);
        checkResults.add(resultPerimeterUpperRightCorner);

        SkyLineResultData minCheckHeightSky = resultAreaUpperLeftCorner;

        System.out.println("minCheckHeightSky.targetData.checkHeight   " + minCheckHeightSky.targetData.checkHeight +
                "  minCheckHeightSky.skyLine.skyHeight " + minCheckHeightSky.skyLine.skyHeight);



        for (SkyLineResultData skyResultData : checkResults) {
            minCheckHeightSky = minCheckHeightSky.skyLine.skyHeight > skyResultData.skyLine.skyHeight ? skyResultData : minCheckHeightSky;
        }

        // if(targetDataUpperLeftCorner.checkHeight == 292){
        //     System.out.println("minCheckHeightSky.skyLine.skyHeight      " + minCheckHeightSky.skyLine.skyHeight);
        //     System.out.println("minHeightSky.skyLine.skyHeight    " + minHeightSky.skyLine.skyHeight);
        //     if(minCheckHeightSky.skyLine.skyHeight == 251){
        //         System.out.println(2313);
        //     }
        // }


        // 判断是否接受最优解
        // 从上往下求的高度小于当前高度

        if (minCheckHeightSky.skyLine.skyHeight <= targetDataUpperLeftCorner.checkHeight) {
            // if((targetDataUpperLeftCorner.checkHeight == 252)){
            //     System.out.println(212);
            // }
            // 下一次检查的高度为本次的最低高度 - 1，必须是减一，不能直接使用最小值替换
            if (minHeightSky.skyLine.skyHeight > targetDataUpperLeftCorner.checkHeight) {
                minCheckHeightSky.skyLine.skyHeight = targetDataUpperLeftCorner.checkHeight;
                minCheckHeightSky.targetData.oriSize[1] = targetDataUpperLeftCorner.checkHeight;
                // 说明最好的解是：从上往下检查的解
                minHeightSky = minCheckHeightSky;
                // 做备份，只保留有效的最优解。defPoints会不断变化
                targetDataUpperLeftCorner.defPointsBack = new CommonToolClass().assistArrayRec(targetDataUpperLeftCorner.defPoints);
                targetDataUpperRightCorner.defPointsBack = new CommonToolClass().assistArrayRec(targetDataUpperRightCorner.defPoints);
                --targetDataUpperLeftCorner.checkHeight;
                --targetDataUpperRightCorner.checkHeight;
            } else {
                targetDataUpperLeftCorner.checkHeight = minHeightSky.skyLine.skyHeight - 1;
                targetDataUpperRightCorner.checkHeight = minHeightSky.skyLine.skyHeight - 1;
            }
        } else {
            // 从上往下没有找到较好的解。
            if(minHeightSky.skyLine.skyHeight <= targetDataUpperLeftCorner.checkHeight){
                targetDataUpperLeftCorner.checkHeight = minHeightSky.skyLine.skyHeight - 1;
                targetDataUpperRightCorner.checkHeight = minHeightSky.skyLine.skyHeight - 1;
            }
        }

        // if(minHeightSky.skyLine.skyHeight <= 252){
        //     System.out.println(9999);
        // }


        return minHeightSky;

        // int currentHeight = minCheckHeightSky.skyLine.skyHeight;
        // int minHeight = minHeightSky.skyLine.skyHeight;
        // int checkHeight = targetDataUpperLeftCorner.checkHeight;
        // int heightBest = checkHeight + 1;
        // if (checkHeight >= currentHeight) {
        //     --heightBest;
        //     minCheckHeightSky.skyLine.skyHeight = targetDataUpperLeftCorner.checkHeight;
        //     minCheckHeightSky.targetData.oriSize[1] = targetDataUpperLeftCorner.checkHeight;
        //     targetDataUpperLeftCorner.defPointsBack = new CommonToolClass().assistArrayRec(targetDataUpperLeftCorner.defPoints);
        //     targetDataUpperRightCorner.defPointsBack = new CommonToolClass().assistArrayRec(targetDataUpperRightCorner.defPoints);
        // }
        // heightBest = Math.min(heightBest, minHeight);
        // targetDataUpperLeftCorner.checkHeight = heightBest - 1;
        // targetDataUpperRightCorner.checkHeight = heightBest - 1;



        // if (minCheckHeightSky.skyLine.skyHeight <= targetDataUpperLeftCorner.checkHeight) {
        //     // 做备份，只保留有效的最优解。defPoints会不断变化
        //     targetDataUpperLeftCorner.defPointsBack = new CommonToolClass().assistArrayRec(targetDataUpperLeftCorner.defPoints);
        //     targetDataUpperRightCorner.defPointsBack = new CommonToolClass().assistArrayRec(targetDataUpperRightCorner.defPoints);
        //     bestLine = minCheckHeightSky;
        // }
        // if (minHeightSky.skyLine.skyHeight <= bestLine.skyLine.skyHeight) {
        //     bestLine = minHeightSky;
        // }
        // targetDataUpperLeftCorner.checkHeight = bestLine.skyLine.skyHeight - 1;
        // targetDataUpperRightCorner.checkHeight = bestLine.skyLine.skyHeight - 1;
        // bestLine.targetData.oriSize[1] = bestLine.skyLine.skyHeight;
        // return bestLine;
    }


    public SkyLine parallelAsync(Map<String, SkyLine> skyLines, TargetData targetData, Map<String, int[][]> hashMap, double startTime) {
//        TargetData targetData = targetDataList.get(0);
        // 创建一个异步任务1，模拟耗时操作
        // 面积规则
        CompletableFuture<SkyLine> area = CompletableFuture.supplyAsync(() -> {
            SkyLine areaSky = skyLines.get("Area");
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            SkyLine finalResult = null;
            try {
                finalResult = areaSky.randomLS(aitInfoArea, areaSky, targetData, hashMap.get("Area"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return finalResult;
        });

        // 高度规则
        CompletableFuture<SkyLine> height = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine heightSky = skyLines.get("Height");
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = heightSky.randomLS(aitInfoArea, heightSky, targetData, hashMap.get("Height"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return finalResult;
        });

        // 宽度规则
        CompletableFuture<SkyLine> width = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine widthSky = skyLines.get("Width");
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = widthSky.randomLS(aitInfoArea, widthSky, targetData, hashMap.get("Width"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return finalResult;
        });

        // 周长规则
        CompletableFuture<SkyLine> perimeter = CompletableFuture.supplyAsync(() -> {
            SkyLine finalResult = null;
            SkyLine perimeterSky = skyLines.get("Perimeter");
            TargetAitInfo aitInfoArea = new TargetAitInfo();
            try {
                finalResult = perimeterSky.randomLS(aitInfoArea, perimeterSky, targetData, hashMap.get("Perimeter"), startTime);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return finalResult;
        });

        // 等待所有异步任务完成
        CompletableFuture.allOf(area, height, width, perimeter).join();
//        CompletableFuture.allOf(area).join();

        // 获取异步任务的结果
        SkyLine resultArea = area.join();
        SkyLine resultWidth = width.join();
        SkyLine resultHeight = height.join();
        SkyLine resultPerimeter = perimeter.join();


        // 先加入集合
        List<SkyLine> listResults = new ArrayList<>();
        listResults.add(resultArea);
        listResults.add(resultWidth);
        listResults.add(resultHeight);
        listResults.add(resultPerimeter);

        SkyLine minKine = resultArea;

        for (SkyLine skyLine : listResults) {
            minKine = minKine.skyHeight > skyLine.skyHeight ? skyLine : minKine;
        }
        // 比较结果，返回一个结果
        return minKine;
    }

    public SkyLine parallelSync(SkyLine tempLine, TargetData targetData, Map<Integer, int[][]> hashMap, int[][] sortResult, double startTime) throws InvocationTargetException, IllegalAccessException {
        // 按规则排名，顺序执行
        TargetAitInfo aitInfoArea = new TargetAitInfo();
        SkyLine finalResult1 = null;
        finalResult1 = tempLine.randomLS(aitInfoArea, tempLine, targetData, hashMap.get(sortResult[0][1]), startTime);
        System.out.println("finalResult1.skyHeight = " + finalResult1.skyHeight);

        // 规则2
        SkyLine finalResult2 = null;
        TargetAitInfo aitInfoHeight = new TargetAitInfo();
        finalResult2 = tempLine.randomLS(aitInfoHeight, finalResult1, targetData, hashMap.get(sortResult[1][1]), startTime);
        System.out.println("finalResult2.skyHeight = " + finalResult2.skyHeight);

        // 规则3
        SkyLine finalResult3 = null;
        TargetAitInfo aitInfoWidth = new TargetAitInfo();
        finalResult3 = tempLine.randomLS(aitInfoWidth, finalResult2, targetData, hashMap.get(sortResult[2][1]), startTime);
        System.out.println("finalResult3.skyHeight = " + finalResult3.skyHeight);

        // 规则4
        SkyLine finalResult4 = null;
        TargetAitInfo aitInfoPara = new TargetAitInfo();
        finalResult4 = tempLine.randomLS(aitInfoPara, finalResult3, targetData, hashMap.get(sortResult[3][1]), startTime);
        System.out.println("finalResult4.skyHeight = " + finalResult4.skyHeight);

        // 比较结果，返回一个结果
        return finalResult4;
    }


}
