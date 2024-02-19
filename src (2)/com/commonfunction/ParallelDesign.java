package com.commonfunction;

import com.twodimensiondata.Data;
import com.twodimensiondata.FitResultData;
import com.twodimensiondata.TargetAitInfo;
import com.twodimension.Fitness;
import com.twodimension.SkyLine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelDesign {

    /**
     * @description  fit = 1 ：并行计算，返回第一个矩形，如果都没有返回一个合并后的对象
     * @author  hao
     * @date    2023/6/4 9:13
     * @param aitInfo
     * @param data
     * @param defSize1
     * @param defIndex
     * @return FitResultData
    */
/*    public static FitResultData parallelFit1(TargetAitInfo aitInfo,  Data data, double[][] defSize1, int[] defIndex,SkyLine skyLine) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 提交 frw1 的任务
        Future<FitResultData> frw1 = executor.submit(() -> fitRw1(aitInfo, data, defSize1, defIndex, skyLine));

        // 提交 flh1 的任务
        Future<FitResultData> flh1 = executor.submit(() -> fitLh1(aitInfo, data, defSize1, defIndex, skyLine));

        // 提交 frh1 的任务
        Future<FitResultData> frh1 = executor.submit(() -> fitRh1(aitInfo, data, defSize1, defIndex, skyLine));
        FitResultData finalResult = null;
        try {
            // 获取 resultFrw1 、 resultFlh1 和 resultFrh1 的结果
            FitResultData resultFrw1 = frw1.get();
            FitResultData resultFlh1 = flh1.get();
            FitResultData resultFrh1 = frh1.get();

            // 先加入集合
            List<FitResultData> list = new ArrayList<>();
            list.add(resultFrw1);
            list.add(resultFlh1);
            list.add(resultFrh1);
            // 根据方法B和方法C的结果计算最终结果
            finalResult = calculateResultFit(list,skyLine);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
        return finalResult;
    }*/

    // 并行设计2
    public static FitResultData fitLh2(TargetAitInfo aitInfo, Data data, double[][] defSize1, int[] defIndex, SkyLine skyLine) {
        // 实现 fitLh2 的逻辑
        Fitness fitness = new Fitness();
        FitResultData fitLh2 = fitness.getFitLh2(aitInfo, data, defSize1, defIndex, skyLine);
        fitLh2.setLeft(true);
        return fitLh2;
    }

    public static FitResultData fitRh2(TargetAitInfo aitInfo, Data data, double[][] defSize1, int[] defIndex,SkyLine skyLine) {
        // 实现 fitRh2 的逻辑
        Fitness fitness = new Fitness();
        FitResultData fitRh2 = fitness.getFitRh2(aitInfo, data, defSize1, defIndex, skyLine);
        // 靠左靠右是一样的
        fitRh2.setLeft(true);
        return fitRh2;
    }

    // 并行设计1
    public static FitResultData fitRw1(TargetAitInfo aitInfo, Data data, double[][] defSize1, int[] defIndex,SkyLine skyLine) {
        // 实现 fitRw1 的逻辑
        Fitness fitness = new Fitness();
        FitResultData fitRw1 = fitness.getFitRw1(aitInfo, data, defSize1, defIndex, skyLine);
        fitRw1.setLeft(true);
        return fitRw1;
    }

    public static FitResultData fitLh1(TargetAitInfo aitInfo, Data data, double[][] defSize1, int[] defIndex,SkyLine skyLine) {
        // 实现 fitLh1 的逻辑
        Fitness fitness = new Fitness();
        FitResultData fitLh1 = fitness.getFitLh1(aitInfo, data, defSize1, defIndex, skyLine);
        fitLh1.setLeft(true);
        return fitLh1;
    }

  /*  public static FitResultData fitRh1(TargetAitInfo aitInfo,  Data data, double[][] defSize1, int[] defIndex,SkyLine skyLine) {
        // 实现 fitRh1 的逻辑
        Fitness fitness = new Fitness();
        FitResultData fitRh1 = fitness.getFitRh1(aitInfo, data, defSize1, defIndex, skyLine);
        fitRh1.setLeft(false);
        return fitRh1;
    }*/

    /**
     * @description 合并结果并返回
     * @author  hao
     * @date    2023/6/4 9:08
     * @param list
     * @return FitResultData
    */
    public static FitResultData calculateResultFit(List<FitResultData> list, SkyLine skyLine) {
        FitResultData result = null;
        boolean isEmpty = false;
        // 返回不为空的，索引最小的一个
        for (FitResultData frd : list) {
            if(frd.getRecInfo() != null){
                isEmpty = true;
                if (result == null) {
                    result = frd;
                }else{
                    result = result.getRecInfo()[0] > frd.getRecInfo()[0] ? frd : result;
                }
            }
        }
        // 如果三个矩形序列都是空
        if(!isEmpty){
            return skyLine.combineDefInfo(list);
        }
        return result;
    }

    public static FitResultData calculateResultFitNew(List<FitResultData> list, SkyLine skyLine) {
        FitResultData result = null;
        boolean isEmpty = false;
        // 返回不为空的，索引最小的一个
        for (FitResultData frd : list) {
            if(frd.getRecInfo() != null){
                isEmpty = true;
                if (result == null) {
                    result = frd;
                }else{
                    result = result.getRecInfo()[0] > frd.getRecInfo()[0] ? frd : result;
                }
            }
        }
        // 如果三个矩形序列都是空
        if(!isEmpty){
            return skyLine.combineDefInfo(list);
        }
        return result;
    }

    // 随机选择一个靠边的矩形
    public static FitResultData randomSelectSide(List<FitResultData> list, SkyLine skyLine){
        int sum = 0;
        for(FitResultData fitRes : list){
            sum += fitRes.sidePlacement.size();
        }
        int rondom =(int)(Math.random() * sum) + 1;
        sum = 0;
        int frontSum = 0;
        for(FitResultData fitRes : list){
            sum += fitRes.sidePlacement.size();
            if(sum >= rondom){
                fitRes.recInfo = fitRes.sidePlacement.get(rondom - frontSum - 1);
                return fitRes;
            }
            frontSum = sum;
        }
        return skyLine.combineDefInfo(list);
    }

    // 随机选择一个不靠边的矩形
    public static FitResultData randomSelectNoSide(List<FitResultData> list, SkyLine skyLine){
        int sum = 0;
        for(FitResultData fitRes : list){
            sum += fitRes.nonSidePlacement.size();
        }
        int rondom =(int)(Math.random() * sum) + 1;
        sum = 0;
        int frontSum = 0;
        for(FitResultData fitRes : list){
            sum += fitRes.nonSidePlacement.size();
            if(sum >= rondom){
                fitRes.recInfo = fitRes.nonSidePlacement.get(rondom - frontSum - 1);
                return fitRes;
            }
            frontSum = sum;
        }
        return skyLine.combineDefInfo(list);
    }

    public static FitResultData calculateResultFit2(List<FitResultData> list, SkyLine skyLine) {
        FitResultData result = null;
        boolean isEmpty = false;
        // 返回不为空的，索引最小的一个
        for (FitResultData frd : list) {
            if(frd.getRecInfo() != null){
                isEmpty = true;
                if (result == null) {
                    result = frd;
                }else{
                    result = result.getRecInfo()[3] > frd.getRecInfo()[3] ? frd : result;
                }
            }
        }
        // 如果三个矩形序列都是空，收集缺陷快信息
        if(!isEmpty){
            return skyLine.combineDefInfo(list);
        }
        return result;
    }
}
