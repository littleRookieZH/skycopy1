package com.twodimension;

import com.twodimensiondata.Data;
import com.twodimensiondata.FitParameter;
import com.twodimensiondata.FitResultData;
import com.twodimensiondata.TargetAitInfo;
import com.universalalgorithm.RangeMinimumQuery;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author zh15178381496
 * @create 2022-09 11:00
 * @说明：
 * @总结：
 */
public class Fitness {
    /**
     * 在key生成的数组中找到与指定矩形块
     * finValue表示带查找的矩形的value
     * rectangle[][]是有序排列的矩形,rectangle[种类数][下标，数量，宽度，高度,key]；
     * 返回的是矩形块的索引值，如果矩形数组为空返回new arraylist，如果没有找到返回
     * fit = 3 只要找到 key 相同的就可以了。 因为 key 相同表示 矩形完全相同，所以无论怎么选都没有影响
     */
    public TargetAitInfo fitnessSw3(int[][] rectangle, int left, int right, int finValue) {
        // 使用二分法查找
        if (left > right) {
            // 说明递归整个数组，但是没有找到
            return null;
        }
        int mid = left + (right - left) / 2;
        int midValue = rectangle[mid][4];
        if (finValue > midValue) {
            // 说明在右边区域，向右递归
            return fitnessSw3(rectangle, mid + 1, right, finValue);
        } else if (finValue < midValue) {
            // 说明在左侧，向左递归
            return fitnessSw3(rectangle, left, mid - 1, finValue);
        } else {
            // 说明 midValue 就是要找的元素，还要判断是否已经使用
            // 找到key相同的之后，判断是否占用，没有占用直接返回；如果占用，向两侧查找，
            TargetAitInfo indexRange;
            if (rectangle[mid][8] == 0) {
                indexRange = new TargetAitInfo(mid, mid);
                return indexRange;
            }
            // 向mid索引值的左边扫描
            int temp = mid - 1;
            while (temp >= 0 && finValue == rectangle[temp][4]) {
                if (rectangle[temp][8] == 0) {
                    indexRange = new TargetAitInfo(temp, temp);
                    return indexRange;
                }
                temp -= 1;
            }
            temp = mid + 1;
            while (temp < rectangle.length && finValue == rectangle[temp][4]) {
                if (rectangle[temp][8] == 0) {
                    indexRange = new TargetAitInfo(temp, temp);
                    return indexRange;
                }
                temp += 1;
            }
            return null;
        }
    }

    /**
     * @return
     * @description 查找所有fitness = 3的矩形块中索引值最小的矩形，方法同样适用于查找fitness = 1，rw=sw的情况
     * @author hao
     * @date 2023/3/5 17:06
     */
    public int getMinIndex(TargetAitInfo range, RangeMinimumQuery nodeTree) {
        if (range == null || nodeTree.getRootMinVal() == Integer.MAX_VALUE) {
            return -1;
        }

        int minIndex = nodeTree.queryMin(range.getMinRecIndex(), range.getMaxRecIndex());
        if (minIndex != Integer.MAX_VALUE) {
            return minIndex;
        } else {
            return -1;
        }
    }

    /**
     * @return
     * @description 使用二分法查找宽度为sw的一组矩形
     * fitness_1  方法三：使用二分法查找一组宽度为sw的矩形
     * @author hao
     * @date 2023/3/5 17:03
     */
    public TargetAitInfo fitnessRw1(TargetAitInfo aitInfo, int[][] rectangle, int left, int right, int finValue) {
        // 给出判断条件：当left大于right时
        if (left > right) {
            return null;
        }
        int mid = left + (right - left) / 2;
        int midValue = rectangle[mid][2];
        if (finValue > midValue) {
            // 向右递归
            return fitnessRw1(aitInfo, rectangle, mid + 1, right, finValue);
        } else if (finValue < midValue) {
            // 向左递归
            return fitnessRw1(aitInfo, rectangle, left, mid - 1, finValue);
        } else {
            int temp = mid;
            // 向mid索引值的左边扫描
            while (temp >= 0) {
                if (finValue == rectangle[temp][2]) {
                    temp--;
                } else {
                    break;
                }
            }
            int minIndex = temp + 1;
            temp = mid + 1;
            while (temp < rectangle.length) {
                if (finValue == rectangle[temp][2]) {
                    temp++;
                } else {
                    break;
                }
            }
            int maxIndex = temp - 1;
            if (aitInfo.widthNodeTree.queryMin(minIndex, maxIndex) == Integer.MAX_VALUE) {
                return null;
            }
            return new TargetAitInfo(minIndex, maxIndex);
        }
    }

    /**
     * @return
     * @description 方法三：对一个排序好的数组，使用二分法确定最小位置和最大位置，返回List数组
     * 在高度相等的情况下，查找所有宽度符合的矩形，
     * 方法三同样适用于高度为srh
     * @author hao
     * @date 2023/3/5 17:29
     */
    public TargetAitInfo fitnessLh1(int[][] rectangle, int left, int right, int findValueH, int findValueW) {
        // 给出判断条件
        if (left > right) {
            return null;
        }
        int mid = left + (right - left) / 2;
        int midValue = rectangle[mid][3];
        boolean isExist = false;
        if (findValueH > midValue) {
            // 向右递归
            return fitnessLh1(rectangle, mid + 1, right, findValueH, findValueW);
        } else if (findValueH < midValue) {
            return fitnessLh1(rectangle, left, mid - 1, findValueH, findValueW);
        } else if (rectangle[mid][2] >= findValueW) {
            // 高度相同，Wmid >= sw  向左递归
            return fitnessLh1(rectangle, left, mid - 1, findValueH, findValueW);
        } else {
            // 高度相同，Wmid < sw 开始向两侧查找
            int temp = mid;
            while (temp >= 0) {
                if (midValue == rectangle[temp][3] && rectangle[temp][2] < findValueW) {
                    if (rectangle[temp][8] == 0 && (!isExist)) {
                        isExist = true;
                    }
                    temp--;
                } else {
                    break;
                }
            }
            int minIndex = temp + 1;
            // 向右查找
            temp = mid + 1;
            while (temp < rectangle.length) {
                if (midValue == rectangle[temp][3] && rectangle[temp][2] < findValueW) {
                    if (rectangle[temp][8] == 0 && (!isExist)) {
                        isExist = true;
                    }
                    temp++;
                } else {
                    break;
                }
            }
            int maxIndex = temp - 1;
            if (!isExist) {
                return null;
            }
            return new TargetAitInfo(minIndex, maxIndex);
        }
    }

    /**
     * @param rectangle
     * @param findValue 传入的是sw
     * @return
     */
    public TargetAitInfo fitNessRw0(TargetAitInfo aitInfo, int[][] rectangle, int left, int right, int findValue) {
        // 给出判断条件：当left大于right时
        if (left > right) {
            return null;
        }
        int mid = left + (right - left) / 2;
        int midValue = rectangle[mid][2];
        if (findValue <= midValue) {
            // 向左递归
            return fitNessRw0(aitInfo, rectangle, left, mid - 1, findValue);
        } else {
            // 说明 findValue > midValue
            int minIndex = 0;
            int temp = mid;
            // 向mid索引值的右边扫描
            while (temp < rectangle.length) {
                if (findValue > rectangle[temp][2]) {
                    temp++;
                } else {
                    break;
                }
            }
            int maxIndex = temp - 1;
            TargetAitInfo indexRange = new TargetAitInfo(minIndex, maxIndex);
            if (aitInfo.widthNodeTree.queryMin(minIndex, maxIndex) == Integer.MAX_VALUE) {
                return null;
            }
            return indexRange;
        }
    }

    /**
     * @return
     * @description 造一个方法, 给一个si可以得到fitness=3的一个矩形，返回矩形的种类，同时适用于fitness=2,srh = lrh
     * @author hao
     * @date 2023/3/5 17:42
     */
    public FitResultData getFit3(TargetAitInfo aitInfo, Data data, double[][] defSize1, int[] defIndex, SkyLine skyLine) {
        // 记录结果
        FitResultData resultData = new FitResultData();
        if (data.slh != data.srh) {
            return resultData;
        }

        // 获取元素内容为：[下标，数量，宽度，高度,key,,,]，并且已经按照key值增大排序了
        int[][] rectangle = aitInfo.keyRectangles;
        // 获取si的key值
        // int findVal = (data.skyWidth + data.srh) * (data.skyWidth + data.srh + 1) / 2 + data.skyWidth;
        int findVal = (data.srh) * (aitInfo.oriArea[0] + 1) + data.skyWidth;
        // 查找 fit=3 的矩形，返回的是一个 TargetData ，或者是 空
        TargetAitInfo range = fitnessSw3(rectangle, 0, rectangle.length - 1, findVal);
        if (range == null) {
            return resultData;
        }

        // 封装参数
        FitParameter fitParameter = new FitParameter(data, rectangle, defSize1, defIndex, skyLine);
        // 加入缺陷块的覆盖判断,返回矩形信息 或者 null
        FitResultData list1 = getNoDef(fitParameter, range, true, resultData);
        // 返回一个fit = 3 的矩形。如果没找到就返回-1，找到就返回 R0的下标
        return list1;
    }

    // 造一个方法，将所有fitness = i的矩形，判断是否覆盖缺陷块，返回一个不会覆盖缺陷块的矩形数组

    /**
     * @param range 通过评分规则得到的矩形下标
     * @return 返回一个不会覆盖缺陷块的数组的下标
     */
    public int getNoDefArrModifyW0(FitParameter fitParameter, TargetAitInfo range, boolean placeLeft, FitResultData fitResultData) {
        int[][] rectangle = fitParameter.getRectangle();
        boolean b1;
        boolean b2;
        // 标记目标块覆盖缺陷块
        boolean isExist = false;
        int tempDef = 0;
        int minIndex = 0;
        for (int i = range.getMinRecIndex(); i <= range.getMaxRecIndex(); i++) {
            if (rectangle[i][8] != 0) {
                continue;
            }

            // 当目标块未被占用时，如果返回false说明没有覆盖，返回true表示会覆盖
            b1 = fitParameter.getSk().judgeDef(fitParameter, i, true, fitResultData);
            b2 = fitParameter.getSk().judgeDef(fitParameter, i, false, fitResultData);
            if (b1) {
                isExist = true;
                // 返回第一个不会覆盖缺陷块的目标块
                if (tempDef != 0) {
                    return minIndex;
                }
            } else if (tempDef == 0) {
                // 如果已经有目标块会覆盖缺陷块,直接返回第一个不会覆盖的目标块
                if (isExist) {
                    return rectangle[i][0];
                }
                // 如果还没有目标块覆盖缺陷块,记录第一个目标块索引
                minIndex = rectangle[i][0];
                tempDef++;
            }
        }
        // 还剩两种情况:一种不存在,一种全可以放
        // 不存在
        if (isExist) {
            return -2;
        }
        // [min,max]之间目标块不会覆盖缺陷块,使用线段树
        return -1;
    }

    public int getNoDefArrModify(FitParameter fitParameter, TargetAitInfo range, boolean placeLeft, FitResultData fitResultData) {
        int[][] rectangle = fitParameter.getRectangle();
        boolean b1;
        // 标记目标块覆盖缺陷块
        boolean isExist = false;
        int tempDef = 0;
        int minIndex = 0;
        for (int i = range.getMinRecIndex(); i <= range.getMaxRecIndex(); i++) {
            if (rectangle[i][8] != 0) {
                continue;
            }
            // 当目标块未被占用时，如果返回false说明没有覆盖，返回true表示会覆盖
            b1 = fitParameter.getSk().judgeDef(fitParameter, i, placeLeft, fitResultData);
            if (b1) {
                isExist = true;
                // 返回第一个不会覆盖缺陷块的目标块
                if (tempDef != 0) {
                    return minIndex;
                }
            } else if (tempDef == 0) {
                // 如果已经有目标块会覆盖缺陷块,直接返回第一个不会覆盖的目标块
                if (isExist) {
                    return rectangle[i][0];
                }
                // 如果还没有目标块覆盖缺陷块,记录第一个目标块索引
                minIndex = rectangle[i][0];
                tempDef++;
            }
        }
        // 还剩两种情况:一种不存在,一种全可以放
        // 不存在
        if (isExist) {
            return -2;
        }
        // [min,max]之间目标块不会覆盖缺陷块,使用线段树
        return -1;
    }

    public void getNoDefArrModify1(FitParameter fitParameter, TargetAitInfo range, boolean placeLeft, FitResultData fitResultData, TargetData targetData) {
        int[][] rectangle = fitParameter.getRectangle();
        for (int i = range.getMinRecIndex(); i <= range.getMaxRecIndex(); i++) {
            if (rectangle[i][8] != 0) {
                continue;
            }
            // 当目标块未被占用时，如果返回false说明没有覆盖，返回true表示会覆盖
            boolean b1 = fitParameter.getSk().judgeDef(fitParameter, i, placeLeft, fitResultData);
            if(!b1){
                // 判断是否靠边  -- true表示靠边
                boolean isSide= isSidePlacement(rectangle[i], targetData.defBoundLines);
                if(isSide){
                    fitResultData.sidePlacement.add(rectangle[i]);
                }else{
                    fitResultData.nonSidePlacement.add(rectangle[i]);
                }
            }
        }
    }

    /**
     * @param fitParameter
     * @param range
     * @param placeLeft
     * @param fitResultData
     * @return int
     * @description 找到第一个可以放置的目标块
     * @author hao
     * @date 2023/5/31 11:13
     */
    public FitResultData getNoDefSw(FitParameter fitParameter, TargetAitInfo range, boolean placeLeft, FitResultData fitResultData) {
        int[][] rectangle = fitParameter.getRectangle();
        boolean b1;
        for (int i = range.getMinRecIndex(); i <= range.getMaxRecIndex(); i++) {
            if (rectangle[i][8] != 0) {
                continue;
            }
            // 当目标块未被占用时，如果返回false说明没有覆盖，返回true表示会覆盖
            b1 = fitParameter.getSk().judgeDef(fitParameter, i, placeLeft, fitResultData);
            if (!b1) {
                // 返回的是当前宽度序列，rw对应的索引i。不是初始索引
                fitResultData.setRecInfo(rectangle[i]);
                return fitResultData;
            }
        }
        // 没有目标块可以放置
        return null;
    }

    public FitResultData getNoDefSw1(FitParameter fitParameter, TargetAitInfo range, boolean placeLeft, FitResultData fitResultData) {
        int[][] rectangle = fitParameter.getRectangle();
        int i = range.widthNodeTree.queryMin(range.getMinRecIndex(), range.getMaxRecIndex());
        // 返回的是当前宽度序列，rw对应的索引i。不是初始索引
        fitResultData.setRecInfo(rectangle[i]);
        return fitResultData;
    }

    /**
     * @param fitParameter
     * @param range
     * @param placeLeft
     * @param fitResultData
     * @return FitResultData
     * @description 找到第一个没有覆盖缺陷块的目标块
     * @author hao
     * @date 2023/5/31 11:00
     */
    public FitResultData getNoDef(FitParameter fitParameter, TargetAitInfo range, boolean placeLeft, FitResultData fitResultData) {
        int[][] rectangle = fitParameter.getRectangle();
        boolean b1;
        for (int i = range.getMinRecIndex(); i <= range.getMaxRecIndex(); i++) {
            // 当目标块未被占用时，如果返回false说明没有覆盖，返回true表示会覆盖
            b1 = fitParameter.getSk().judgeDef(fitParameter, i, placeLeft, fitResultData);
            if (b1) {
                return fitResultData;
            } else {
                fitResultData.setRecInfo(rectangle[i]);
                return fitResultData;
            }
        }
        return fitResultData;
    }

    /**
     * @return
     * @description 造一个方法，给一个si可以得到fitness=2,srh = lrh的一个矩形
     * @author hao
     * @date 2023/3/5 17:30
     */
    public FitResultData getFitRh2(TargetAitInfo targetData, Data data, double[][] defSize1, int[] defIndex, SkyLine skyLine) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照key值增大排序了
        int[][] rectangle = targetData.keyRectangles;
        // 获取si的key值
        // int findVal = (data.skyWidth + data.srh) * (data.skyWidth + data.srh + 1) / 2 + data.skyWidth;
        int findVal = (data.srh) * (targetData.oriArea[0] + 1) + data.skyWidth;
        // 查找 fit=2 的矩形，返回的是一个 TargetData ，或者是 空
        TargetAitInfo range = fitnessSw3(rectangle, 0, rectangle.length - 1, findVal);
        if (range == null) {
            return resultData;
        }

        // 传递参数
        FitParameter fitParameter = new FitParameter(data, rectangle, defSize1, defIndex, skyLine);
        // 加入缺陷块的覆盖判断,返回一个不会覆盖缺陷块的数组
        FitResultData list1 = getNoDef(fitParameter, range, false, resultData);
        return list1;
    }

    public FitResultData getFitRh2New(TargetAitInfo aitInfo, FitParameter fitParameter, TargetData targetData) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照key值增大排序了
        fitParameter.rectangle = aitInfo.keyRectangles;
        // 获取si的key值
        // int findVal = (data.skyWidth + data.srh) * (data.skyWidth + data.srh + 1) / 2 + data.skyWidth;
        int findVal = (fitParameter.data.srh) * (aitInfo.oriArea[0] + 1) + fitParameter.data.skyWidth;
        // 查找 fit=2 的矩形，返回的是一个 TargetData ，或者是 空
        TargetAitInfo range = fitnessSw3(fitParameter.rectangle, 0, fitParameter.rectangle.length - 1, findVal);
        if (range == null) {
            return resultData;
        }
        getNoDefArrModify1(fitParameter, range, false, resultData, targetData);
        return resultData;
    }

    /**
     * @return
     * @description 造一个方法，给一个si可以得到fitness=2,slh = llh的一个矩形
     * @author hao
     * @date 2023/3/5 17:35
     */
    public FitResultData getFitLh2(TargetAitInfo aitInfo, Data data, double[][] defSize1, int[] defIndex, SkyLine skyLine) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照key值增大排序了
        int[][] rectangle = aitInfo.keyRectangles;
        // 获取si,slh = llh的key值
        // int findVal = (data.skyWidth + data.slh) * (data.skyWidth + data.slh + 1) / 2 + data.skyWidth;
        int findVal = (data.slh) * (aitInfo.oriArea[0] + 1) + data.skyWidth;
        // 查找 fit=2 的矩形，返回的是一个 TargetData ，或者是 空
        TargetAitInfo range = fitnessSw3(rectangle, 0, rectangle.length - 1, findVal);
        if (range == null) {
            return resultData;
        }
        FitParameter fitParameter = new FitParameter(data, rectangle, defSize1, defIndex, skyLine);
        // 加入缺陷块的覆盖判断,返回一个最终结果，包含矩形信息、覆盖的缺陷块信息
        FitResultData list1 = getNoDef(fitParameter, range, true, resultData);
        return list1;
    }

    public FitResultData getFitLh2Improve(TargetAitInfo aitInfo, Data data, double[][] defSize1, int[] defIndex, SkyLine skyLine,TargetData targetData) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照key值增大排序了
        int[][] rectangle = aitInfo.keyRectangles;

        // 检测是否存在可以加分的情况 fit2 --> fit3
        String key;
        int findVal;
        if(data.slh > data.srh){
            key = "srh-" + (data.x1 + data.skyWidth) + "-" + (data.y1 + data.srh) + "-" + (data.y1 + data.slh);
            findVal = (data.slh) * (aitInfo.oriArea[0] + 1) + data.skyWidth;
        }else{
            key = "slh-" + data.x1 + "-" + (data.y1 + data.slh) + "-" + (data.y1 + data.srh);
            findVal = (data.srh) * (aitInfo.oriArea[0] + 1) + data.skyWidth;
        }

        if(!targetData.improveFitness.contains(key)){
            return resultData;
        }
        // 查找 fit=2 的矩形，由于缺陷块的位置，fit=3。返回的是一个 TargetData ，或者是 空
        TargetAitInfo range = fitnessSw3(rectangle, 0, rectangle.length - 1, findVal);
        if (range == null) {
            return resultData;
        }
        FitParameter fitParameter = new FitParameter(data, rectangle, defSize1, defIndex, skyLine);
        // 加入缺陷块的覆盖判断,返回一个最终结果，包含矩形信息、覆盖的缺陷块信息
        FitResultData list1 = getNoDef(fitParameter, range, true, resultData);
        return list1;
    }

    public FitResultData getFitLh2New(TargetAitInfo aitInfo, FitParameter fitParameter, TargetData targetData) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照key值增大排序了
        fitParameter.rectangle = aitInfo.keyRectangles;
        // 获取si,slh = llh的key值
        // int findVal = (data.skyWidth + data.slh) * (data.skyWidth + data.slh + 1) / 2 + data.skyWidth;
        int findVal = (fitParameter.data.slh) * (aitInfo.oriArea[0] + 1) + fitParameter.data.skyWidth;
        // 查找 fit=2 的矩形，返回的是一个 TargetData ，或者是 空
        TargetAitInfo range = fitnessSw3(fitParameter.rectangle, 0, fitParameter.rectangle.length - 1, findVal);
        if (range == null) {
            return resultData;
        }
        getNoDefArrModify1(fitParameter, range, true, resultData, targetData);
        return resultData;
    }

    /**
     * @return
     * @description 造一个方法，给一个si可以得到fitness=1，sw = rw的一个矩形
     * @author hao
     * @date 2023/3/5 17:07
     */
    public FitResultData getFitRw1(TargetAitInfo aitInfo, Data data, double[][] defSize1, int[] defIndex, SkyLine skyLine) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照 width 和 index 值增大排序了
        int[][] rectangle = aitInfo.widthRectangles;
        // 查找所有宽度为sw的矩形的下标
        TargetAitInfo range = fitnessRw1(aitInfo, rectangle, 0, rectangle.length - 1, data.skyWidth);
        if (range == null) {
            return resultData;
        }

        FitParameter fitParameter = new FitParameter(data, rectangle, defSize1, defIndex, skyLine);
        // 加入缺陷块的覆盖判断,返回一个不会覆盖缺陷块的数组
        int list1 = getNoDefArrModify(fitParameter, range, true, resultData);
        if (list1 == -1) {
            // 调用查找线段树的区间最小值方法
            int minIndexWidth = getMinIndex(range, aitInfo.widthNodeTree);
            resultData.setRecInfo(aitInfo.indexRectangles[minIndexWidth]);
            return resultData;
        } else if (list1 == -2) {
            // 说明没有目标块可以放置
            return resultData;
        }
        resultData.setRecInfo(aitInfo.indexRectangles[list1]);
        return resultData;
    }

    public FitResultData getFitRw1New(TargetAitInfo aitInfo, FitParameter fitParameter, TargetData targetData) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照 width 和 index 值增大排序了
        fitParameter.rectangle = aitInfo.widthRectangles;

        // 查找所有宽度为sw的矩形的下标
        TargetAitInfo range = fitnessRw1(aitInfo, fitParameter.rectangle, 0, fitParameter.rectangle.length - 1, fitParameter.data.skyWidth);
        if (range == null) {
            return resultData;
        }
        // FitParameter fitParameter = new FitParameter(data, rectangle, defSize1, defIndex, skyLine);
        getNoDefArrModify1(fitParameter, range, true, resultData, targetData);
        return resultData;
    }

    /**
     * @return
     * @description 造一个方法，给一个si可以得到fitness=1，sw >=rw，srh = lrh的一个矩形
     * int lowLine, LinkedList<Data> upLink 是不是可以省略
     * @author hao
     * @date 2023/3/5 17:16
     */
    public FitResultData getFitRh1(TargetAitInfo aitInfo, Data data, double[][] defSize1, int[] defIndex, SkyLine skyLine, TargetData targetData) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照 height 和 index 值增大排序了
        int[][] rectangle = aitInfo.heightRectangles;
        // 查找所有宽度 sw >=rw，srh = lrh 的矩形的下标
        TargetAitInfo range = fitnessLh1(rectangle, 0, rectangle.length - 1, data.srh, data.skyWidth);
        if (range == null) {
            return resultData;
        }

        FitParameter fitParameter = new FitParameter(data, rectangle, defSize1, defIndex, skyLine);
        // if(targetData.oriSize[1] <= 252 && (targetData.oriSize[1] > 200)&& (data.skyHeight == 86) && (data.srh == 19) && (data.slh == 4)) {
        //     System.out.println(UpperLeftCorner);
        // }
        // 加入缺陷块的覆盖判断,返回一个不会覆盖缺陷块的数组
        int list1 = getNoDefArrModify(fitParameter, range, false, resultData);
        if (list1 == -1) {
            // 调用查找线段树的区间最小值方法（TargetData.heightIndexNodeTree由高度序列的[0]组成）
            int minIndexHeight = getMinIndex(range, aitInfo.heightIndexNodeTree);
            resultData.setRecInfo(aitInfo.indexRectangles[minIndexHeight]);
            return resultData;
        } else if (list1 == -2) {
            // 说明没有目标块可以放置
            return resultData;
        }
        resultData.setRecInfo(aitInfo.indexRectangles[list1]);
        return resultData;
    }

    public FitResultData getFitRh1New(TargetAitInfo aitInfo, FitParameter fitParameter, TargetData targetData) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照 height 和 index 值增大排序了
        fitParameter.rectangle = aitInfo.heightRectangles;
        // 查找所有宽度 sw >=rw，srh = lrh 的矩形的下标
        TargetAitInfo range = fitnessLh1(fitParameter.rectangle, 0, fitParameter.rectangle.length - 1,
                fitParameter.data.srh, fitParameter.data.skyWidth);
        if (range == null) {
            return resultData;
        }
        getNoDefArrModify1(fitParameter, range, false, resultData, targetData);
        return resultData;
    }

    /**
     * @return
     * @description 造一个方法，给一个si可以得到fitness=1，sw >=rw，slh = llh的一个矩形
     * @author hao
     * @date 2023/3/5 17:34
     */
    public FitResultData getFitLh1(TargetAitInfo targetData, Data data, double[][] defSize1, int[] defIndex, SkyLine skyLine) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照 height 和 index 值增大排序了
        int[][] rectangle = targetData.heightRectangles;
        // 查找所有 sw >=rw，slh = llh 矩形的下标
        TargetAitInfo range = fitnessLh1(rectangle, 0, rectangle.length - 1, data.slh, data.skyWidth);
        if (range == null) {
            return resultData;
        }
        FitParameter fitParameter = new FitParameter(data, rectangle, defSize1, defIndex, skyLine);
        // 加入缺陷块的覆盖判断,返回一个不会覆盖缺陷块的数组
        int list1 = getNoDefArrModify(fitParameter, range, true, resultData);
        // 返回一个fit = 1，sw >=rw，slh = llh并且索引值最小的一个矩形。如果没找到就返回-1，找到就返回索引值
        if (list1 == -1) {
            // 调用查找线段树的区间最小值方法
            int minIndexHeight = getMinIndex(range, targetData.heightIndexNodeTree);
            resultData.setRecInfo(targetData.indexRectangles[minIndexHeight]);
            return resultData;
        } else if (list1 == -2) {
            // 说明没有目标块可以放置
            return resultData;
        }
        resultData.setRecInfo(targetData.indexRectangles[list1]);
        return resultData;
    }

    public FitResultData getFitLh1New(TargetAitInfo aitInfo, FitParameter fitParameter, TargetData targetData) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照 height 和 index 值增大排序了
        fitParameter.rectangle = aitInfo.heightRectangles;
        // 查找所有 sw >=rw，slh = llh 矩形的下标
        TargetAitInfo range = fitnessLh1(fitParameter.rectangle, 0, fitParameter.rectangle.length - 1,
                fitParameter.data.slh, fitParameter.data.skyWidth);
        if (range == null) {
            return resultData;
        }
        getNoDefArrModify1(fitParameter, range, true, resultData, targetData);
        return resultData;
    }

    /**
     * @return
     * @description 造一个方法，给一个si可以得到fitness=0的一个矩形
     * @author hao
     * @date 2023/3/5 17:44
     */
    public FitResultData getFit0Left(TargetAitInfo aitInfo, Data data, double[][] defSize1, int[] defIndex, SkyLine skyLine) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照 width 和 index 值增大排序了
        int[][] rectangle = aitInfo.widthRectangles;
        // 找到所有rw < sw的矩形，返回List数组，
        TargetAitInfo range = fitNessRw0(aitInfo, rectangle, 0, rectangle.length - 1, data.skyWidth);
        if (range == null) {
            return resultData;
        }

        FitParameter fitParameter = new FitParameter(data, rectangle, defSize1, defIndex, skyLine);
        // 加入缺陷块的覆盖判断,返回一个不会覆盖缺陷块的数组
        int list1 = getNoDefArrModify(fitParameter, range, true, resultData);
        if (list1 == -1) {
            // 调用查找线段树的区间最小值方法
            int minIndexHeight = getMinIndex(range, aitInfo.widthNodeTree);
            resultData.setRecInfo(aitInfo.indexRectangles[minIndexHeight]);
            return resultData;
        } else if (list1 == -2) {
            // 说明没有目标块可以放置
            return resultData;
        }
        resultData.setRecInfo(aitInfo.indexRectangles[list1]);
        return resultData;
    }

    public FitResultData getFit0Right(TargetAitInfo aitInfo, Data data, double[][] defSize1, int[] defIndex, SkyLine skyLine) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照 width 和 index 值增大排序了
        int[][] rectangle = aitInfo.widthRectangles;
        // 找到所有rw < sw的矩形，返回List数组，
        TargetAitInfo range = fitNessRw0(aitInfo, rectangle, 0, rectangle.length - 1, data.skyWidth);
        if (range == null) {
            return resultData;
        }

        FitParameter fitParameter = new FitParameter(data, rectangle, defSize1, defIndex, skyLine);
        // 加入缺陷块的覆盖判断,返回一个不会覆盖缺陷块的数组
        int list1 = getNoDefArrModify(fitParameter, range, false, resultData);
        if (list1 == -1) {
            // 调用查找线段树的区间最小值方法
            int minIndexHeight = getMinIndex(range, aitInfo.widthNodeTree);
            resultData.setRecInfo(aitInfo.indexRectangles[minIndexHeight]);
            return resultData;
        } else if (list1 == -2) {
            // 说明没有目标块可以放置
            return resultData;
        }
        resultData.setRecInfo(aitInfo.indexRectangles[list1]);
        return resultData;
    }

    public FitResultData getFit0LeftNew(TargetAitInfo aitInfo, FitParameter fitParameter, TargetData targetData) {
        FitResultData resultData = new FitResultData();
        // 获取元素内容为：[下标，数量，宽度，高度,key]，并且已经按照 width 和 index 值增大排序了
        fitParameter.rectangle = aitInfo.widthRectangles;
        // 找到所有rw < sw的矩形，返回List数组，
        TargetAitInfo range = fitNessRw0(aitInfo, fitParameter.rectangle, 0, fitParameter.rectangle.length - 1,
                fitParameter.data.skyWidth);
        if (range == null) {
            return resultData;
        }
        getNoDefArrModify1(fitParameter, range, true, resultData, targetData);
        return resultData;
    }

    /**
     * @description 预判断
     * @author hao
     * @date 2023/5/30 21:01
     */
    public int[] prejudgmentDef(double[][] defArray, Data data, int maxHeight) {
        // 记录每一轮哪些缺陷块被使用
        int size = defArray.length;
        int[] defAuxiliary = new int[size];
        double upperX = data.x1 + data.skyWidth;
        double upperY = maxHeight + data.y1;
        int j = 0;
        double r1 = Math.sqrt(upperX * upperX + upperY * upperY);
        for (int i = 0; i < size; i++) {
            // 缺陷块的(x1,y1)在半径范围之内，并且没有使用过
            if (defArray[i][4] <= r1 && defArray[i][5] == 0) {
                double x1 = defArray[i][0];
                double y1 = defArray[i][1];
                // 如果半径比r1小，说明有可能会覆盖，还需要继续判断
                double lowerX = data.x1 - (defArray[i][2] - defArray[i][0]);
                double lowerY = data.y1 - (defArray[i][3] - defArray[i][1]);
                if ((x1 > lowerX) && (x1 < upperX) && (y1 > lowerY) && (y1 < upperY)) {
                    // 为了避免数字0的混用。将下标都加1
                    defAuxiliary[j++] = i + 1;
                }
            }
        }
        return defAuxiliary;
    }

    // 判断是否物品靠边

    /**
     *
     * @param defBoundLines
     * @param recPlacement 物品的放置位置
     * @return
     */
    public boolean isSidePlacement(int[] recPlacement, HashMap<String, List<int[]>> defBoundLines) {
        // String v1 = "v-" + recPlacement[0];
        // String v2 = "v-" + recPlacement[2];
        String l1 = "l-" + recPlacement[1];
        String l2 = "l-" + recPlacement[3];
        // // 垂直线x1
        // if(defBoundLines.containsKey(v1)){
        //     for (int[] arr1 : defBoundLines.get(v1)) {
        //         if(arr1[0] < recPlacement[3] && arr1[1] > recPlacement[1]){
        //             return true;
        //         }
        //     }
        // }
        // // 垂直线x2
        // if(defBoundLines.containsKey(v2)){
        //     for (int[] arr1 : defBoundLines.get(v2)) {
        //         if(arr1[0] < recPlacement[3] && arr1[1] > recPlacement[1]){
        //             return true;
        //         }
        //     }
        // }
        // 水平线y1
        if(defBoundLines.containsKey(l1)){
            for (int[] arr1 : defBoundLines.get(l1)) {
                if(arr1[0] < recPlacement[2] && arr1[1] > recPlacement[0]){
                    return true;
                }
            }
        }
        // 水平线y2
        if(defBoundLines.containsKey(l2)){
            for (int[] arr1 : defBoundLines.get(l2)) {
                if(arr1[0] < recPlacement[2] && arr1[1] > recPlacement[0]){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isImproveScores(String key, HashSet<String> improveFitness){
        return improveFitness.contains(key);
    }
}
