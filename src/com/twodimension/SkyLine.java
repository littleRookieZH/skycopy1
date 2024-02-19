package com.twodimension;

import com.commonfunction.AsyncExecution;
import com.commonfunction.CommonToolClass;
import com.commonfunction.ParallelDesign;
import com.twodimension.IndividualObject;
import com.twodimension.TargetData;
import com.twodimensiondata.*;
import com.universalalgorithm.BinarySearch;
import com.universalalgorithm.MaxHeapSort;
import com.universalalgorithm.QuickSortDef;
import com.universalalgorithm.TransRule;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author zh15178381496
 * @create 2022-09 20:37
 * @说明： 对于一个si的天际线，如何根据他的sl和sh计算它的key值
 * si需要存储的元素有哪些：sw、shi、slh、srh、targetIndex
 * slh = si-1的
 * 存储结构选择链表LinkedList
 * @总结：
 */
public class SkyLine implements Serializable {
    /**
     * @description 存放矩形位置信息
     */
    public List<List<Integer>> arrayList = new ArrayList<>();
    /**
     * @description 记录天际线的高度
     */
    public int skyHeight;

    public int[][] rectangularSequence;

    /**
     * 种群信息
     */
    public IndividualObject individualObjects;


    public SkyLine() {
    }

    public SkyLine(List<List<Integer>> arrayList, int skyHeight, int[][] rectangularSequence) {
        this.arrayList = arrayList;
        this.skyHeight = skyHeight;
        this.rectangularSequence = rectangularSequence;
    }

    public SkyLine(IndividualObject individualObjects) {
        this.individualObjects = individualObjects;
    }

    public SkyLine(int[][] rectangularSequence) {
        this.rectangularSequence = rectangularSequence;
    }

    /**
     * @return
     * @description 初始化天际线
     * @author hao
     * @date 2023/3/5 18:55
     */
    public LinkedList<Data> oriSky(TargetData td) {
        LinkedList<Data> list = new LinkedList<>();
        list.add(new Data(0, td.oriSize[0], 0, -1, -1, 0, 0));
        return list;
    }

    /*
        //获取天际线第i个元素的slh和srh对应的value值，
        //合并过程中使用链表合并，因此传入链表来获取value值
        //也可以用于计算fitness=2的情况，bl == TRUE 表示矩形的高度(srh)以左边为准
        //更新缺陷块
 */

    // public LinkedList<Data> upDef(double[] list1, LinkedList<Data> linkedList) throws InvocationTargetException, IllegalAccessException {
    //
    //     if (!judgeSkyLineRh(linkedList)) {
    //         System.out.println("oo");
    //     }
    //     if (!judgeSkyLineLh(linkedList)) {
    //         System.out.println("oo");
    //     }
    //
    //     if (linkedList.size() >= 5) {
    //         if (list1[0] == 99 && list1[1] == 77 && list1[2] == 131) {
    //             System.out.println(999);
    //         }
    //     }
    //     // 查找宽度(求和的)可以覆盖x1的天际线下标
    //     int total1 = 0;
    //     int start = 0;
    //     // 直接根据判断x1
    //     for (int i = 0; i < linkedList.size(); i++) {
    //         // 获取天际线宽度
    //         // 如果求和的长度大于等于缺陷块坐标x1
    //         if ((linkedList.get(i).x1 + linkedList.get(i).skyWidth) >= list1[0]) {
    //             start = i;
    //             break;
    //         }
    //     }
    //     if (linkedList.get(start).x1 == list1[0]) {
    //         total1 = linkedList.get(start).x1;
    //     } else {
    //         total1 = linkedList.get(start).x1 + linkedList.get(start).skyWidth;
    //     }
    //     total1 = linkedList.get(start).x1 + linkedList.get(start).skyWidth;
    //
    //     // 查找查找宽度(求和的)可以覆盖x1的天际线下标，从start + 1开始查起
    //     int total2 = total1;
    //     int end = start;
    //     // 终点位置 x2
    //     int defW1 = (int) list1[2];
    //     if (total2 < defW1) {
    //         for (int i = start + 1; i < linkedList.size(); i++) {
    //             int temp = linkedList.get(i).x1 + linkedList.get(i).skyWidth;
    //             if (temp >= list1[2]) {
    //                 end = i;
    //                 total2 = temp;
    //                 break;
    //             }
    //         }
    //     }
    //     // 在修改之前获取原start、end索引值的data信息
    //     Data startdata = linkedList.get(start);
    //     Data endData = linkedList.get(end);
    //     // 先取出end、end+1索引值位置的data数据
    //     Data repair = null;
    //     Data endData2 = endData.copyData();
    //     // 预先判断end是不是最后一个元素，防止取end+1时出现角标越界  TRUE表示是
    //     boolean b1 = end == linkedList.size() - 1;
    //     if (!b1) {
    //         // 说明end不是最后一个元素，保证不会角标越界,
    //         repair = linkedList.get(end + 1);
    //         if (defW1 >= total2) {
    //             repair.slh = (int) list1[3] - repair.skyHeight;
    //         }
    //     }
    //     // 修改，删除，加入新元素
    //     // 修改起点
    //     Data startdata2 = linkedList.get(start);
    //     // 差值 total - x1，如果t1 == 0说明起点与x1重合
    //     int t1 = total1 - (int) list1[0];
    //     if(t1 == 0){
    //         System.out.println(88);
    //     }
    //     // start索引位置对应的天际线宽度 - 差值
    //     int w1 = startdata.skyWidth - t1;
    //     // 检测起终点高度  如果缺陷块在下，最低水平线在上，减少缺陷块尺寸
    //     int defW = 0;
    //     // 比较 start位置天际线的高度与缺陷块的y2坐标
    //     // 起点处：缺陷块在下，最低水平线在上
    //     if (startdata.skyHeight > list1[3]) {
    //         // 被覆盖的尺寸。需要修改start的srh
    //         defW = t1;
    //     }
    //     // 修改终点
    //     // 差值，如果t2 == 0，说明终点与x2重合
    //     int t2 = total2 - defW1;
    //     // 这里end已经被修改，所以需要使用辅助接点的数据(endData)
    //     // 只有在缺陷块在下，最低水平线在上，才考虑w2
    //     int w2 = endData.skyWidth - t2;
    //     // 分为起点、终点相等和不相等
    //     if (start == end) {
    //         // 终点处：缺陷块在下，最低水平线在上
    //         if (endData.skyHeight >= list1[3]) {// 说明缺陷块被原天际线完全覆盖，此时不需要其他操作，只需要删除缺陷块即可
    //             defW += w2;
    //         }
    //         if ((endData.skyHeight < list1[3])) {
    //             // x1 与def[0] 重合
    //             if (w1 == 0) {
    //                 startdata2.skyWidth = (int) list1[2] - (int) list1[0];
    //                 startdata2.skyHeight = (int) list1[3];
    //                 if(t2 != 0){
    //                     startdata2.srh = endData2.skyHeight - (int) list1[3];
    //                 }else{
    //                     // 说明不是尾元素
    //                     if(repair != null){
    //                         startdata2.srh = -1;
    //                     }else{
    //                         startdata2.srh = repair.skyHeight - (int) list1[3];
    //                     }
    //                 }
    //             } else {
    //                 startdata2.skyWidth = w1;
    //                 startdata2.srh = (int) list1[3] - startdata2.skyHeight;
    //             }
    //             // startdata2.skyWidth = w1;
    //             // startdata2.srh = (int) list1[3] - startdata2.skyHeight;
    //
    //             // t2 == 0说明缺陷块的x2与天际线的右侧x坐标相等。不需要其他操作 后续添加了元素
    //             if (t2 != 0) {
    //                 // 说明start=end，并且天际线在缺陷块以下，需要修改start结点，end结点(增加的)
    //                 // 借助一个辅助节点，完成end结点的更新   这里有错误，不能这样写。因为他们都是在更新同一个节点
    //                 endData2.skyWidth = t2;
    //                 endData2.x1 += w2;
    //                 endData2.slh = (int) list1[3] - endData2.skyHeight;
    //                 // 将修改的结点加入到链表中(因为产生了一个新的天际线),位置：start的后一个
    //                 linkedList.add(start + 1, endData2);
    //             }
    //         }
    //     }
    //     if (start != end) {
    //         // 修改终点  终点处：缺陷块在下，最低水平线在上
    //         if (endData.skyHeight > list1[3]) {
    //             defW += w2;
    //             endData2.slh = (int) list1[3] - endData2.skyHeight;
    //             linkedList.set(end, endData2);
    //         } else {// 终点处：缺陷块在上，最低水平线在下
    //             if (t2 == 0) {
    //                 // 终点正好覆盖
    //                 linkedList.remove(end);
    //             } else {
    //                 endData2.skyWidth = t2;
    //                 endData2.slh = (int) list1[3] - endData2.skyHeight;
    //                 endData2.x1 = defW1;
    //                 linkedList.set(end, endData2);
    //             }
    //         }
    //         // if(t1 != 0){
    //         //     // 起点处：缺陷块在上，最低水平线在下
    //         //     startdata2.skyWidth = w1;
    //         //     // 缺陷块的高度 - 天际线的高度
    //         //     startdata2.srh = (int) list1[3] - startdata.skyHeight;
    //         // }
    //
    //         if (startdata.skyHeight > list1[3]) {
    //             startdata2.srh = (int) list1[3] - startdata.skyHeight;
    //         } else {
    //             // 起点处：缺陷块在上，最低水平线在下
    //             startdata2.skyWidth = w1;
    //             // 缺陷块的高度 - 天际线的高度
    //             startdata2.srh = (int) list1[3] - startdata.skyHeight;
    //         }
    //     }
    //     // 删除起点，终点之间的元素
    //     for (int i = start + 1; i < end; i++) {
    //         linkedList.remove(start + 1);
    //     }
    //     // 更新    说明缺陷块被原天际线覆盖，之所以出现这种情况是因为缺陷块的更新次序导致的
    //     if (defW >= (list1[2] - list1[0])) {// 不用更新天际线
    //         return linkedList;
    //     } else {
    //         // 需要更新天际线，将缺陷块更新，更新位置：start + 1
    //         // 需要考虑t2是否等于0
    //         Data defData = null;
    //         if (t2 == 0) {
    //             // 说明end是最后一个元素同时缺陷块的x2 与 end的x2重合，srh = -1
    //             if (b1) {
    //                 defData = new Data(0, (int) (list1[2] - list1[0] - defW), (int) list1[3], (int) (startdata.skyHeight - list1[3]), -1,
    //                         (startdata.x1 + startdata.skyWidth), (int) list1[3]);
    //             } else {
    //                 defData = new Data(0, (int) (list1[2] - list1[0] - defW), (int) list1[3], (int) (startdata.skyHeight - list1[3]), (int) (repair.skyHeight - list1[3]),
    //                         (startdata.x1 + startdata.skyWidth), (int) list1[3]);
    //             }
    //         } else {
    //             // 当t2 != 0时，缺陷块的x2 与 end的x2不会重   t1 != 0时，缺陷块的x1 与start的x1 不会重
    //                 if(w1 != 0){
    //                     defData = new Data(0, (int) (list1[2] - list1[0] - defW), (int) list1[3], (int) (startdata.skyHeight - list1[3]), (int) (endData.skyHeight - list1[3]),
    //                             (startdata.x1 + startdata.skyWidth), (int) list1[3]);
    //                 }
    //
    //         }
    //         if (defData != null) {
    //             linkedList.add(start + 1, defData);
    //         }
    //
    //     }
    //
    //     if (judgeSkyWidth(linkedList)) {
    //         System.out.println(999);
    //     }
    //     // 如果有高度相等的相邻天际线，将他们合并
    //     combineHigh(linkedList);
    //     if (judgeSkyWidth(linkedList)) {
    //         System.out.println(999);
    //     }
    //     if (!judgeSkyLineRh(linkedList)) {
    //         System.out.println("oo");
    //     }
    //     if (!judgeSkyLineLh(linkedList)) {
    //         System.out.println("oo");
    //     }
    //     return linkedList;
    // }
    public LinkedList<Data> upDef(double[] list1, LinkedList<Data> linkedList) throws InvocationTargetException, IllegalAccessException {

        if (!judgeSkyLineRh(linkedList)) {
            System.out.println("oo");
        }
        if (!judgeSkyLineLh(linkedList)) {
            System.out.println("oo");
        }

        // if (linkedList.size() >= 5) {
        //     if (list1[0] == 131 && list1[1] == 8 && list1[2] == 166) {
        //         System.out.println(999);
        //     }
        // }


        // 查找宽度(求和的)可以覆盖x1的天际线下标
        // int total1 = 0;
        int start = 0;
        // 直接根据判断x1
        for (int i = 0; i < linkedList.size(); i++) {
            // 获取天际线宽度
            // 如果求和的长度大于等于缺陷块坐标x1
            if ((linkedList.get(i).x1 + linkedList.get(i).skyWidth) >= list1[0]) {
                start = i;
                break;
            }
        }

        // 查找查找宽度(求和的)可以覆盖x1的天际线下标，从start + 1开始查起
        int total2 = linkedList.get(start).x1 + linkedList.get(start).skyWidth;
        int end = start;
        // 终点位置 x2
        int defX2 = (int) list1[2];
        if (total2 < defX2) {
            for (int i = start + 1; i < linkedList.size(); i++) {
                int temp = linkedList.get(i).x1 + linkedList.get(i).skyWidth;
                if (temp >= list1[2]) {
                    end = i;
                    break;
                }
            }
        }
        // 在修改之前获取原start、end索引值的data信息
        Data startData = linkedList.get(start);
        Data endData = linkedList.get(end);
        // 先取出end、end+1索引值位置的data数据
        Data endNextData = null;
        Data endData2 = endData.copyData();

        // 修改起点
        // 差值 如果t1 == 0 说明起点与x1重合，且是首元素
        int t1 = (int) list1[0] - startData.x1;

        // 修改终点
        // 差值，如果t2 == 0，说明 终点 与 dx2 重合
        int t2 = endData.skyWidth + endData.x1 - defX2;

        // 预先判断end是不是最后一个元素，防止取end+1时出现角标越界  TRUE表示是
        boolean isLastElement = end == linkedList.size() - 1;
        if (!isLastElement) {
            // 说明end不是最后一个元素，保证不会角标越界,
            endNextData = linkedList.get(end + 1);
            if(t2 == 0){
                endNextData.slh = (int) list1[3] - endNextData.skyHeight;
            }
        }

        // 修改起点
        startData.skyWidth = t1;
        startData.srh = (int) list1[3] - startData.skyHeight;

        // 修改终点：分为起点、终点相等和不相等
        if (start == end) {
                // 说明start=end，并且天际线在缺陷块以下，需要修改start结点，end结点(增加的)
                // 借助一个辅助节点，完成end结点的更新   这里有错误，不能这样写。因为他们都是在更新同一个节点
                endData2.skyWidth = t2;
                endData2.x1 = (int) list1[2];
                endData2.slh = (int) list1[3] - endData2.skyHeight;
                // 将修改的结点加入到链表中(因为产生了一个新的天际线),位置：start的后一个
                linkedList.add(start + 1, endData2);
        }
        if (start != end) {
            endData.skyWidth = t2;
            endData.x1 = (int) list1[2];
            endData.slh = (int) list1[3] - endData.skyHeight;
        }
        // 删除起点，终点之间的元素
        for (int i = start + 1; i < end; i++) {
            linkedList.remove(start + 1);
        }

        // 创建新节点
        Data defData = new Data();
        defData.skyWidth = (int) list1[2] - (int) list1[0];
        defData.skyHeight = (int) list1[3];
        defData.x1 = (int) list1[0];
        defData.y1 = (int) list1[3];
        if (t1 == 0) {
            defData.slh = -1;
        } else {
            defData.slh = startData.skyHeight - (int) list1[3];
        }
        if(t2 == 0 && endNextData != null){
            defData.srh = endNextData.skyHeight - (int) list1[3];
        } else if(t2 == 0){
            defData.srh = -1;
        } else {
            defData.srh = endData.skyHeight - (int) list1[3];
        }
        linkedList.add(start + 1, defData);
        if(t2 == 0 ){
            linkedList.remove(start + 2);
        }
        if(t1 == 0){
            linkedList.remove(start);
        }

        // if (judgeSkyWidth(linkedList)) {
        //     System.out.println(999);
        // }

        // 如果有高度相等的相邻天际线，将他们合并
        combineHigh(linkedList);
        // if (judgeSkyWidth(linkedList)) {
        //     System.out.println(999);
        // }
        if (!judgeSkyLineRh(linkedList)) {
            System.out.println("oo");
        }
        if (!judgeSkyLineLh(linkedList)) {
            System.out.println("oo");
        }
        return linkedList;
    }

    /**
     * @return
     * @description 造一个方法：将高度相同的相邻天际线合并
     * @author hao
     * @date 2023/3/5 18:57
     */
    public LinkedList<Data> combineHigh(LinkedList<Data> upList) {
        int i = 0;
        while (true) {
            if (i == upList.size() - 1) {
                break;
            }
            Data data = upList.get(i);
            Data data1 = upList.get(i + 1);
            if (data.skyHeight == data1.skyHeight) {
                data.skyWidth += data1.skyWidth;
                data.srh = data1.srh;
                upList.remove(i + 1);
            } else {
                i++;
            }
        }
        return upList;
    }

    /**
     * @param linkedList  用于更新的链表
     * @param lowLine     si的位置
     * @param minIndexRec 待放置矩形块的信息
     * @param left        是否靠右放置 仅fit=1,srh=lrh&&siw!=wi
     */
    public void upSkyLine(LinkedList<Data> linkedList, int lowLine, int[] minIndexRec, boolean left) {

        if (!judgeSkyLineRh(linkedList)) {
            System.out.println("oo");
        }
        if (!judgeSkyLineLh(linkedList)) {
            System.out.println("oo");
        }

        // 存放当前更新的矩形块的位置信息 x1、y1、x2、y2。x1应该提前获取，
        List<Integer> list2 = new ArrayList<>();
        int x1 = linkedList.get(lowLine).x1;
        // 目标块的索引
        int indexRec = minIndexRec[0];
        // w
        int wi = minIndexRec[2];
        // h
        int hi = minIndexRec[3];
        // 天际线的宽度
        int skyWidth = linkedList.get(lowLine).skyWidth;
        // 天际线高度
        int skyHeight = linkedList.get(lowLine).skyHeight;
        int sh = hi + skyHeight;
        // 造一个linkedList结点，用于更新
        Data newdata = new Data(indexRec, wi, sh, 0, 0, 0, 0);
        // 获取最低水平线位置的天际线信息
        Data lowdata = linkedList.get(lowLine);
        while (true) {
            // 1、首元素位置更新
            if (lowLine == 0) {
                if (skyWidth == wi) {
                    // 天际线的宽度与矩形块的宽度相等
                    if (lowLine == linkedList.size() - 1) {
                        // 说明原天际线只有一条，并且天际线宽度与矩形块宽度相等，
                        // 直接提高原天际线的高度为sh
                        lowdata.skyHeight = sh;
                        lowdata.y1 = sh;
                        break;
                    }
                    /*靠左放置，原天际线不止一条，lowLine+1才不会越界*/
                    Data laterdata1 = linkedList.get(lowLine + 1);
                    // 1、右边等高，和右边合并，删除原天际线，修改下一条天际线的宽度、slh
                    if (laterdata1.skyHeight == sh) {
                        // 右边天际线的高度等于最低水平线高度+矩形块高度
                        linkedList.remove(lowLine);
                        laterdata1.slh = -1;
                        laterdata1.skyWidth += wi;
                        laterdata1.x1 = 0;
                        break;
                    }
                    // 2、右边不等高，修改lowLine位置的原天际线sh,srh,修改下一条天际线的srh
                    if (laterdata1.skyHeight != sh) {
                        lowdata.skyHeight = sh;
                        lowdata.y1 = sh;
                        // 下一个结点的高度减去当前结点的高度
                        lowdata.srh = laterdata1.skyHeight - sh;
                        laterdata1.slh = -lowdata.srh;
                        break;
                    }
                } else {// lowLine是首元素,但是矩形的宽度小于天际线的宽度，加入新元素h,srh,slh，修改原天际线的宽度slh
                    if (left) {
                        // 如果left为TRUE表示靠左更新
                        newdata.slh = -1;
                        // 等于矩形块高度的负数
                        newdata.srh = -hi;
                        newdata.x1 = 0;
                        newdata.y1 = sh;
                        linkedList.add(lowLine, newdata);
                        // 原天际线宽度减去矩形块宽度
                        lowdata.skyWidth = skyWidth - wi;
                        lowdata.slh = hi;
                        lowdata.x1 = wi;
                        break;
                    } else {
                        // false表示靠右更新,说明右边一定有节点，向右更新只出现在  slh = lrh,wi!=rw  的情况
                        // if(linkedList.size() == 1){
                        //     // 说明当前仅有一条天际线，且需要靠右更新
                        //     newdata.slh = lowdata.skyHeight - sh;
                        //     newdata.srh = -1;
                        //     newdata.x1 = skyWidth - wi;
                        //     newdata.y1 = sh;
                        //     linkedList.add(lowLine + 1, newdata);
                        //     lowdata.skyWidth -= wi;
                        //     lowdata.srh = sh - lowdata.skyHeight;
                        //     break;
                        // }

                        Data laterdata1 = null;
                        try {
                            laterdata1 = linkedList.get(lowLine + 1);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        laterdata1.skyWidth += wi;
                        laterdata1.slh = -hi;
                        laterdata1.x1 -= wi;
                        lowdata.skyWidth = skyWidth - wi;
                        lowdata.srh = hi;
                        break;
                        // 靠右边更新
                        // 1、右边等高，和右边合并，修改原天际线宽度、srh,修改下一个结点的宽度、slh
                        // if (laterdata1.skyHeight == sh) {
                        //     laterdata1.skyWidth += wi;
                        //     laterdata1.slh = -hi;
                        //     laterdata1.x1 -= wi;
                        //     lowdata.skyWidth = skyWidth - wi;
                        //     lowdata.srh = hi;
                        //     break;
                        // }
                        // // 2、右边不等高，加入一个新的天际线，修改当前结点的宽度
                        // if (laterdata1.skyHeight != sh) {
                        //     newdata.slh = lowdata.skyHeight - sh;
                        //     newdata.srh = laterdata1.skyHeight - sh;
                        //     newdata.x1 = laterdata1.x1 - wi;
                        //     newdata.y1 = sh;
                        //     linkedList.add(lowLine + 1, newdata);
                        //     laterdata1.slh = sh - laterdata1.skyHeight;
                        //     lowdata.skyWidth -= wi;
                        //     lowdata.srh = sh - lowdata.skyHeight;
                        //     break;
                        // }
                    }
                }
            }
            // 2、尾元素位置更新
            // 如果lowLine不是天际线的第一个节点，获取lowLine结点的前一个结点，用于更新
            Data frontdata = null;
            if (lowLine != 0) {
                // 说明矩形块不是天际线的第一个结点，slh、srh待定
                frontdata = linkedList.get(lowLine - 1);
            }
            // 最低水平线在尾元素
            if (lowLine == linkedList.size() - 1) {
                // 天际线的宽度与矩形块的宽度相等
                if (skyWidth == wi) {
                    /*靠左放置，原天际线一定不止一条，所以lowLine - 1不会越界*/
                    // frontdata
                    // 1、左边等高，和左边合并，删除原天际线，修改前一条天际线的宽度、srh
                    if (frontdata.skyHeight == sh) {
                        // 左边天际线的高度等于最低水平线高度+矩形块高度
                        linkedList.remove(lowLine);
                        frontdata.srh = -1;
                        frontdata.skyWidth += wi;
                        break;
                    }
                    // 2、左边不等高，修改lowLine位置的原天际线sh,slh,修改前一条天际线的srh
                    if (frontdata.skyHeight != sh) {
                        lowdata.skyHeight = sh;
                        lowdata.y1 = sh;
                        // 前一个结点的高度减去当前结点的高度
                        lowdata.slh = frontdata.skyHeight - sh;
                        frontdata.srh = -lowdata.slh;
                        break;
                    }
                } else {
                    // lowLine是尾元素,但是矩形的宽度小于天际线的宽度，加入新元素,srh,slh，修改原天际线的宽度、slh，修改前一个结点的srh
                    // 1、左边等高，和左边合并
                    if (frontdata.skyHeight == sh) {
                        // 左边天际线的高度等于最低水平线高度+矩形块高度
                        frontdata.srh = -hi;
                        frontdata.skyWidth += wi;
                        lowdata.slh = hi;
                        lowdata.x1 += wi;
                        lowdata.skyWidth -= wi;
                        break;
                    }
                    // 2、左边不等高
                    newdata.slh = frontdata.skyHeight - sh;
                    // 等于矩形块高度的负数
                    newdata.srh = -hi;
                    newdata.x1 = lowdata.x1;
                    newdata.y1 = sh;
                    linkedList.add(lowLine, newdata);
                    frontdata.srh = -newdata.slh;
                    // 原天际线宽度减去矩形块宽度
                    lowdata.skyWidth = skyWidth - wi;
                    lowdata.slh = hi;
                    lowdata.x1 += wi;
                    break;
                }
            }
            // 3、中间位置更新
            // 获取当前节点的前一个结点
            // frontdata = linkedList.get(lowLine - 1);
            // 获取当前节点的下一个结点
            Data laterdata1 = linkedList.get(lowLine + 1);
            if (skyWidth == wi) {
                // 宽度相等，还需要判断左右等高的问题
                // 先判断双边是否相等，在依次判断左边、右边、都不相等
                // 1、两边都等高，一起合并 修改前一个的宽度、srh
                if ((frontdata.skyHeight == sh) && (laterdata1.skyHeight == sh)) {
                    frontdata.skyWidth = wi + frontdata.skyWidth + laterdata1.skyWidth;
                    frontdata.srh = laterdata1.srh;
                    linkedList.remove(lowLine);
                    linkedList.remove(lowLine);
                    break;
                }
                // 2、左边等高，和左边合并 修改前一个的宽度和高度，srh,后一个的slh
                if (frontdata.skyHeight == sh) {
                    // 右边天际线的高度等于最低水平线高度+矩形块高度
                    linkedList.remove(lowLine);
                    // 后一个结点的高度减去前一个结点的高度
                    frontdata.srh = laterdata1.skyHeight - frontdata.skyHeight;
                    frontdata.skyWidth += wi;
                    laterdata1.slh = -frontdata.srh;
                    break;
                }
                // 3、右边等高，和右边合并 修改前一个的srh，后一个的宽度和slh
                if (laterdata1.skyHeight == sh) {
                    // 右边天际线的高度等于最低水平线高度+矩形块高度
                    linkedList.remove(lowLine);
                    laterdata1.slh = frontdata.skyHeight - laterdata1.skyHeight;
                    laterdata1.skyWidth += wi;
                    laterdata1.x1 -= wi;
                    frontdata.srh = -laterdata1.slh;
                    break;
                }
                // 4、两边都不等高，都不合并,修改前一个结点的srh，当前结点的srh\slh，后一个结点的slh
                if ((frontdata.skyHeight != sh) && (laterdata1.skyHeight != sh)) {
                    // 当前-前一个
                    frontdata.srh = sh - frontdata.skyHeight;
                    lowdata.skyHeight = sh;
                    lowdata.slh = -frontdata.srh;
                    lowdata.srh = laterdata1.skyHeight - lowdata.skyHeight;
                    lowdata.y1 = sh;
                    laterdata1.slh = -lowdata.srh;
                    break;
                }
            } else {
                // 宽度不相等，由于是靠左合并，因此需要判断左边是否等高的问题
                if (left) {
                    // 靠左边更新
                    // 1、左边等高，和左边合并，修改前一条天际线的宽度、srh,修改当前结点的宽度，slh
                    if (frontdata.skyHeight == sh) {
                        frontdata.srh = -hi;
                        frontdata.skyWidth += wi;
                        lowdata.skyWidth = skyWidth - wi;
                        lowdata.x1 += wi;
                        lowdata.slh = hi;
                        break;
                    }
                    // 2、左边不等高，修改前一个结点的srh，加入一个新结点，修改当前节点的宽度,slh
                    if (frontdata.skyHeight != sh) {
                        frontdata.srh = sh - frontdata.skyHeight;
                        newdata.slh = -frontdata.srh;
                        newdata.srh = -hi;
                        newdata.x1 = lowdata.x1;
                        newdata.y1 = sh;
                        linkedList.add(lowLine, newdata);
                        lowdata.slh = hi;
                        lowdata.skyWidth = skyWidth - wi;
                        lowdata.x1 += wi;
                        break;
                    }
                } else {
                    // 靠右边更新
                    // 1、右边等高，和右边合并，修改当前天际线的宽度减少，下一个结点的天际线宽度增加
                    if (laterdata1.skyHeight == sh) {
                        lowdata.skyWidth = skyWidth - wi;
                        laterdata1.skyWidth += wi;
                        laterdata1.x1 -= wi;
                        break;
                    }
                    // 2、右边不等高，加入一个新的天际线，修改当前结点的宽度
                    if (laterdata1.skyHeight != sh) {
                        newdata.slh = lowdata.skyHeight - sh;
                        newdata.srh = laterdata1.skyHeight - sh;
                        newdata.x1 = laterdata1.x1 - wi;
                        newdata.y1 = sh;
                        linkedList.add(lowLine + 1, newdata);
                        laterdata1.slh = sh - laterdata1.skyHeight;
                        lowdata.skyWidth -= wi;
                        lowdata.srh = sh - lowdata.skyHeight;
                        break;
                    }
                }
            }
        }
        // 靠右侧更新，x1需要加一段宽度
        if (!left) {
            x1 += linkedList.get(lowLine).skyWidth;
        }
        list2.add(wi);
        list2.add(hi);
        list2.add(x1);
        list2.add(skyHeight);
        list2.add(x1 + wi);
        list2.add(sh);
        list2.add(indexRec);
        arrayList.add(list2);

        // 如果有高度相等的相邻天际线，将他们合并
        combineHigh(linkedList);

        if (!judgeSkyLineRh(linkedList)) {
            System.out.println("oo");
        }
        if (!judgeSkyLineLh(linkedList)) {
            System.out.println("oo");
        }
        // if(linkedList.size() >= 4){
        //     if(linkedList.get(3).skyWidth == 18 && linkedList.get(3).skyHeight == 50 && linkedList.get(3).slh == -10){
        //         System.out.println(99);
        //     }
        // }

    }

    /**
     * 如果是第一个元素，向左合并
     * 如果是最后一个元素，向右合并
     * 如果是中间元素需要比较两边的高度
     */
    public LinkedList<Data> combine(LinkedList<Data> upLink, int lowLine) {
        Data data = upLink.get(lowLine);
        if (lowLine == upLink.size() - 1) {
            // 是不是最后一个元素
            // 左边元素的srh==-1 ，sw更新，删除lowLine位置的元素
            upLink.get(lowLine - 1).srh = -1;
            upLink.get(lowLine - 1).skyWidth += data.skyWidth;
            upLink.remove(lowLine);
        } else if (lowLine == 0) {   // 如果是第一个元素,x1变
            // 获取lowLine的下一个结点
            Data laterdata = upLink.get(lowLine + 1);
            // 宽度合并
            laterdata.skyWidth += data.skyWidth;
            // slh设为lowLine的
            laterdata.slh = data.slh;
            laterdata.x1 = 0;
            // 删除lowLine的元素
            upLink.remove(lowLine);
        } else// 如果是中间元素
        {
            // 判断左右两边的高度
            Data data1 = upLink.get(lowLine - 1);
            Data data2 = upLink.get(lowLine + 1);
            int height1 = data1.skyHeight;
            int height2 = data2.skyHeight;
            if (height1 < height2) {
                // 更新左边sw,srh,index,右边slh,删除lowLine
                data1.skyWidth += data.skyWidth;
                data1.srh = data2.skyHeight - data1.skyHeight;
                data2.slh = -data1.srh;
                upLink.remove(lowLine);
            } else {
                // 更新右边sw,slh,index ,左边srh，删除lowLine
                data2.slh = data1.skyHeight - data2.skyHeight;
                data2.skyWidth += data.skyWidth;
                data2.x1 = upLink.get(lowLine).x1;
                data1.srh = -data2.slh;
                upLink.remove(lowLine);
            }
        }

        // 如果有高度相等的相邻天际线，将他们合并
        combineHigh(upLink);
        return upLink;
    }

    public LinkedList<Data> defLift(LinkedList<Data> upLink, DefData defData, int lowLine) throws InvocationTargetException, IllegalAccessException {
        Data data = upLink.get(lowLine);
        double[] defInfo = defData.defInfo;
        int leftHeight = 0;
        int rightHeight = 0;
        int defHeight = (int) defInfo[3];
        if (lowLine == 0) {   // 如果是首节点
            leftHeight = Integer.MAX_VALUE;
        } else {
            leftHeight = upLink.get(lowLine - 1).skyHeight;
        }
        if (lowLine == (upLink.size() - 1)) {   // 如果是尾节点
            rightHeight = Integer.MAX_VALUE;
        } else {
            rightHeight = upLink.get(lowLine + 1).skyHeight;
        }
        double newHeight = Math.min(leftHeight, rightHeight);
        double newWidth = Math.min(defInfo[2], (data.x1 + data.skyWidth));
        // 虚构一个用于更新的缺陷块
        double[] defTempInfo;
        if (newHeight >= defHeight) {
            // 需要将缺陷块打包到天际线中 -- 以缺陷块的右上角为虚拟矩形的右上角
            defTempInfo = new double[]{data.x1, data.y1, newWidth, defHeight};
            defData.isUsed = true;
        } else {
            defTempInfo = new double[]{data.x1, data.y1, newWidth, newHeight};
            defData.isUsed = false;
        }

        // if (defTempInfo[0] == 0 && defTempInfo[1] == 148 && defTempInfo[2] == 76 && upLink.get(0).skyHeight == 148) {
        //     System.out.println(33);
        // }

        upDef(defTempInfo, upLink);
        return upLink;
    }

    public boolean judgeDef(FitParameter fitParameter, int targetIndex, boolean placeLeft, FitResultData fitResultData) {
        Data data = fitParameter.getData();
        int[][] rectangle = fitParameter.getRectangle();
        double[][] defSize = fitParameter.getDefSize();
        int[] defIndex = fitParameter.getDefIndex();
        // 记录结果的
        List<Integer> defRS = fitResultData.getDefIndex();

        boolean judge = false;
        // 如果defIndex的首元素为0，说明预判断不存在缺陷块会覆盖目标块
        if (defIndex.length == 0) {
            return false;
        }
        // 包含目标块的宽度合
        int totalWidth = 0;
        // 是lowLine左侧的元素宽度合
        int w = data.x1;
        // 高度求和 h_lowLine + hi   y2
        int totalHeight = data.skyHeight + rectangle[targetIndex][3];
        // 求出目标块的右上点(x2,y2)
        // 宽度求和   xd1 < totalWidth < xd2
        if (placeLeft) {
            // x2
            totalWidth = w + rectangle[targetIndex][2];
        } else {
            totalWidth = w + data.skyWidth;
        }

        // 放置的目标块不能和任何一个缺陷块有交集
        for (int i = 0; i < defIndex.length; i++) {
            if (defIndex[i] == 0) {
                break;
            }
            // 在记录缺陷块索引的时候，已经提前 +1
            int index = defIndex[i] - 1;
            // xd1 yd1 xd2 yd2   xd1 <= totalWidth <= xd2
            double x1 = defSize[index][0];
            double x2 = defSize[index][2] + rectangle[targetIndex][2];
            double y1 = defSize[index][1];
            double y2 = defSize[index][3] + rectangle[targetIndex][3];
            // 只有同时满足这四个表达式，才能说明目标块覆盖了缺陷块
            if (totalWidth > x1 && totalWidth < x2 && totalHeight > y1 && totalHeight < y2) {
                judge = true;
                if (!defRS.contains(index)) {
                    defRS.add(index);
                }
            }
        }
        // 如果返回false说明没有覆盖，返回true表示会覆盖
        return judge;
    }

    public boolean judgeW0Def(FitParameter fitParameter, int targetIndex, boolean placeLeft, FitResultData fitResultData) {
        Data data = fitParameter.getData();
        int[][] rectangle = fitParameter.getRectangle();
        double[][] defSize = fitParameter.getDefSize();
        int[] defIndex = fitParameter.getDefIndex();
        // 记录结果的
        List<Integer> defRS = fitResultData.getDefIndex();

        boolean judge = false;
        // 如果defIndex的首元素为0，说明预判断不存在缺陷块会覆盖目标块
        if (defIndex.length == 0) {
            return false;
        }
        // 包含目标块的宽度合
        int totalWidth = 0;
        // 是lowLine左侧的元素宽度合
        int w = data.x1;
        // 高度求和 h_lowLine + hi   y2
        int totalHeight = data.skyHeight + rectangle[targetIndex][3];
        // 求出目标块的右上点(x2,y2)
        // 宽度求和   xd1 < totalWidth < xd2
        if (placeLeft) {
            // x2
            totalWidth = w + rectangle[targetIndex][2];
        } else {
            totalWidth = w + data.skyWidth;
        }

        // 放置的目标块不能和任何一个缺陷块有交集
        for (int i = 0; i < defIndex.length; i++) {
            if (defIndex[i] == 0) {
                break;
            }
            // 在记录缺陷块索引的时候，已经提前 +1
            int index = defIndex[i] - 1;
            // xd1 yd1 xd2 yd2   xd1 <= totalWidth <= xd2
            double x1 = defSize[index][0];
            double x2 = defSize[index][2] + rectangle[targetIndex][2];
            double y1 = defSize[index][1];
            double y2 = defSize[index][3] + rectangle[targetIndex][3];
            // 只有同时满足这四个表达式，才能说明目标块覆盖了缺陷块
            if (totalWidth > x1 && totalWidth < x2 && totalHeight > y1 && totalHeight < y2) {
                judge = true;
                if (!defRS.contains(index)) {
                    defRS.add(index);
                }
            }
        }
        // 如果返回false说明没有覆盖，返回true表示会覆盖
        return judge;
    }

    // 查找lowLine，操作对象是合并数组upLink，返回lowLine的索引值
    public int searchLine(LinkedList<Data> upLink) {
        // int minheight = upLink.get(0).skyHeight;
        // int index = 0;
        // for (int i = 0; i < upLink.size(); i++) {
        //     if (upLink.get(i).skyHeight < minheight) {
        //         minheight = upLink.get(i).skyHeight;
        //         index = i;
        //     }
        // }
        // return index;

        // 迭代器版本
        if (upLink.isEmpty()) {
            return -1;
        }

        int minheight = upLink.getFirst().skyHeight;
        int index = 0;
        ListIterator<Data> iterator = upLink.listIterator();

        while (iterator.hasNext()) {
            Data current = iterator.next();
            if (current.skyHeight < minheight) {
                minheight = current.skyHeight;
                index = iterator.previousIndex();
            }
        }

        return index;
    }

    /**
     * @param lowLine si的序号
     * @param upLink  用于合并的链表
     * 返回一个矩形下标
     * 方法的目的：通过传入一个si(upLink.get(lowLine))，和 辅助数组rectangle，
     * 使用评分规则传入一个合适的矩形，再去判断是否会覆盖缺陷块。如果不会，返回矩形下标，如果会还需要继续查找
     * 最终结果：如果数组找完之后都没有找到合适矩形就返回-1，如果找到就返回下标。
     */


    /**
     *  无缺陷版本
     * @param aitInfo
     * @param targetData
     * @param lowLine
     * @param upLink
     * @param defSize
     * @param skyLine
     * @return
     */
//    public FitResultData select(TargetAitInfo aitInfo, TargetData targetData, int lowLine, LinkedList<Data> upLink, double[][] defSize, SkyLine skyLine) {
//        Data data = upLink.get(lowLine);
//        Fitness fit = new Fitness();
//        // 预判断，确定在当前的天际线上最多可能覆盖多少个缺陷块
//        // 查看一下，当前宽度上可放置的最大高度是多少。
//        int index = BinarySearch.findIndex(aitInfo.widthRectangles, data.skyWidth);
//
//        int maxHeight = data.skyHeight + aitInfo.widthNodeTree.queryHeightMax(aitInfo, aitInfo.widthNodeTree.getRoot(), 0, index);
//        // 得到可能会覆盖目标块的缺陷块索引
//        int[] defIndex = new int[0];
//        if (defSize != null && defSize.length != 0) {
//            defIndex = fit.prejudgmentDef(defSize, data, maxHeight);
//        }
//        /*到此为止，说明存在一类矩形，它的宽度满足天际线的宽度要求。此时如果通过评价函数(包含是否覆盖缺陷块)，没有找到可以放置的矩形,
//         * 那么一定是因为矩形覆盖了缺陷块。
//         * */
//        //思路：由于si的双边是有正负的，因此可以直接通过srh、slh、sw找符合si的矩形
//        // fit=3
//        // 分为四层：fit3、fit2、fit1 和 fit0
//        FitResultData fit3 = fit.getFit3(aitInfo, data, defSize, defIndex, skyLine);
//        if (fit3.getRecInfo() != null) {
//            //如果找到一个合适的矩形，直接更新
//            upSkyLine(upLink, lowLine, fit3.getRecInfo(), true);
//            return fit3;
//        }
//
//        //fit=2,lh
//        FitResultData flh2 = fit.getFitLh2(aitInfo, data, defSize, defIndex, skyLine);
//        flh2.setLeft(true);
//        // fit=2,rh
//        FitResultData frh2 = fit.getFitRh2(aitInfo, data, defSize, defIndex, skyLine);
//        frh2.setLeft(true);
//        // 先加入集合
//        List<FitResultData> listFit2 = new ArrayList<>();
//        listFit2.add(flh2);
//        listFit2.add(frh2);
//        FitResultData resultFit2 = ParallelDesign.calculateResultFit2(listFit2, skyLine);
//
//        if (resultFit2.getRecInfo() != null) {
//            upSkyLine(upLink, lowLine, resultFit2.getRecInfo(), true);
//            return resultFit2;
//        }
////        System.out.println(22222222);
//        // fit=1，sw
//        FitResultData frw1 = fit.getFitRw1New(aitInfo, data, defSize, defIndex, skyLine);
//        frw1.setLeft(true);
//        // fit=1,lh
//        FitResultData flh1 = fit.getFitLh1New(aitInfo, data, defSize, defIndex, skyLine);
//        flh1.setLeft(true);
//        // fit=1,rh
//        FitResultData frh1 = fit.getFitRh1New(aitInfo, data, defSize, defIndex, skyLine);
//        frh1.setLeft(false);
//        // 先加入集合
//        List<FitResultData> list = new ArrayList<>();
//        list.add(frw1);
//        list.add(flh1);
//        list.add(frh1);
//        FitResultData resultFit1 = ParallelDesign.calculateResultFit(list, skyLine);
//        if (resultFit1.getRecInfo() != null) {
//
//            upSkyLine(upLink, lowLine, resultFit1.getRecInfo(), resultFit1.isLeft());
//            return resultFit1;
//        }
//
//        // fit=0
//        FitResultData fit0Left = fit.getFit0LeftNew(aitInfo, data, defSize, defIndex, skyLine);
//        if (fit0Left.getRecInfo() != null) {
//            upSkyLine(upLink, lowLine, fit0Left.getRecInfo(), true);
//            return fit0Left;
//        }
//
//        /*如果程序走到这里说明：虽然存在宽度符合条件的矩形块，但是由于缺陷块的存在导致矩形块无法放置。结论：一定是有矩形块覆盖了缺陷块*/
//        // 汇总各个适应度中缺陷块的信息
//        List<FitResultData> listFinals = new ArrayList<>();
//        listFinals.add(fit3);
//        listFinals.add(resultFit2);
//        listFinals.add(resultFit1);
//        listFinals.add(fit0Left);
//        return combineDefInfo(listFinals);
//    }

    /**
     * 带缺陷版本
     *
     * @param aitInfo
     * @param targetData
     * @param lowLine
     * @param upLink
     * @param defSize
     * @param skyLine
     * @return
     */
    public FitResultData select(TargetAitInfo aitInfo, TargetData targetData, int lowLine, LinkedList<Data> upLink, double[][] defSize, SkyLine skyLine) {
        Data data = upLink.get(lowLine);
        Fitness fit = new Fitness();
        // 预判断，确定在当前的天际线上最多可能覆盖多少个缺陷块
        // 查看一下，当前宽度上可放置的最大高度是多少。
        BinarySearch binarySearch = new BinarySearch();
        int index = binarySearch.findIndex(aitInfo.widthRectangles, data.skyWidth);

        int maxHeight = 0;
        maxHeight = data.skyHeight + aitInfo.widthNodeTree.queryHeightMax(aitInfo, aitInfo.widthNodeTree.getRoot(), 0, index);

        // 得到可能会覆盖目标块的缺陷块索引
        int[] defIndex = new int[0];
        if (defSize != null && defSize.length != 0) {
            defIndex = fit.prejudgmentDef(defSize, data, maxHeight);
        }

        FitParameter fitParameter = new FitParameter(data, null, defSize, defIndex, skyLine);
        /*到此为止，说明存在一类矩形，它的宽度满足天际线的宽度要求。此时如果通过评价函数(包含是否覆盖缺陷块)，没有找到可以放置的矩形,
         * 那么一定是因为矩形覆盖了缺陷块。
         * */
        // 思路：由于si的双边是有正负的，因此可以直接通过srh、slh、sw找符合si的矩形
        // fit=3
        // 分为四层：fit3、fit2、fit1 和 fit0
        FitResultData fit3 = fit.getFit3(aitInfo, data, defSize, defIndex, skyLine);
        if (fit3.getRecInfo() != null) {
            // 如果找到一个合适的矩形，直接更新
            upSkyLine(upLink, lowLine, fit3.getRecInfo(), true);
            return fit3;
        }

        // fit=2,借助缺陷块的位置 fit升至3
        FitResultData flh2Improve = fit.getFitLh2Improve(aitInfo, data, defSize, defIndex, skyLine, targetData);
        flh2Improve.setLeft(true);
        if (flh2Improve.getRecInfo() != null) {
            upSkyLine(upLink, lowLine, flh2Improve.getRecInfo(), true);
            System.out.println("加分了");
            return flh2Improve;
        }

        // fit=2,lh
        FitResultData flh2 = fit.getFitLh2(aitInfo, data, defSize, defIndex, skyLine);
        flh2.setLeft(true);
        // fit=2,rh
        FitResultData frh2 = fit.getFitRh2(aitInfo, data, defSize, defIndex, skyLine);
        frh2.setLeft(true);
        // 先加入集合
        List<FitResultData> listFit2 = new ArrayList<>();
        listFit2.add(flh2);
        listFit2.add(frh2);
        FitResultData resultFit2 = ParallelDesign.calculateResultFit2(listFit2, skyLine);

        if (resultFit2.getRecInfo() != null) {
            upSkyLine(upLink, lowLine, resultFit2.getRecInfo(), true);
            return resultFit2;
        }

        // // fit=2,lh
        // FitResultData flh2 = fit.getFitRh2New(aitInfo, fitParameter, targetData);
        // flh2.setLeft(true);
        // // fit=2,rh
        // FitResultData frh2 = fit.getFitRh2New(aitInfo, fitParameter, targetData);
        // frh2.setLeft(true);
        // // 先加入集合
        // List<FitResultData> listFit2 = new ArrayList<>();
        // listFit2.add(flh2);
        // listFit2.add(frh2);
        // FitResultData selectSide2 = ParallelDesign.randomSelectSide(listFit2, skyLine);
        // FitResultData selectNoSide2 = ParallelDesign.randomSelectNoSide(listFit2, skyLine);
        // if (selectSide2.getRecInfo() != null) {
        //     upSkyLine(upLink, lowLine, selectSide2.getRecInfo(), selectSide2.isLeft());
        //     return selectSide2;
        // } else if (selectNoSide2.getRecInfo() != null) {
        //     upSkyLine(upLink, lowLine, selectNoSide2.getRecInfo(), selectNoSide2.isLeft());
        //     return selectNoSide2;
        // }


        // FitResultData fitRw1New = fit.getFitRw1New(aitInfo, fitParameter, targetData);
        // fitRw1New.setLeft(true);
        // // fit=1,lh
        // FitResultData flh1 = fit.getFitLh1New(aitInfo, fitParameter, targetData);
        // flh1.setLeft(true);
        // // fit=1,rh
        // FitResultData frh1 = fit.getFitRh1New(aitInfo, fitParameter, targetData);
        // frh1.setLeft(false);
        // // 先加入集合
        // List<FitResultData> list = new ArrayList<>();
        // list.add(fitRw1New);
        // list.add(flh1);
        // list.add(frh1);
        // FitResultData selectSide1 = ParallelDesign.randomSelectSide(list, skyLine);
        // FitResultData selectNoSide1 = ParallelDesign.randomSelectNoSide(list, skyLine);
        // if (selectSide1.getRecInfo() != null) {
        //     upSkyLine(upLink, lowLine, selectSide1.getRecInfo(), selectSide1.isLeft());
        //     return selectSide1;
        // } else if (selectNoSide1.getRecInfo() != null) {
        //     upSkyLine(upLink, lowLine, selectNoSide1.getRecInfo(), selectNoSide1.isLeft());
        //     return selectNoSide1;
        // }

        // fit=1，sw
        FitResultData frw1 = fit.getFitRw1(aitInfo, data, defSize, defIndex, skyLine);
        frw1.setLeft(true);
        // fit=1,lh
        FitResultData flh1 = fit.getFitLh1(aitInfo, data, defSize, defIndex, skyLine);
        flh1.setLeft(true);
        // fit=1,rh
        FitResultData frh1 = fit.getFitRh1(aitInfo, data, defSize, defIndex, skyLine, targetData);
        frh1.setLeft(false);
        // 先加入集合
        List<FitResultData> list = new ArrayList<>();
        list.add(frw1);
        list.add(flh1);
        list.add(frh1);
        FitResultData resultFit1 = ParallelDesign.calculateResultFit(list, skyLine);
        if (resultFit1.getRecInfo() != null) {
            upSkyLine(upLink, lowLine, resultFit1.getRecInfo(), resultFit1.isLeft());
            return resultFit1;
        }


        // fit=0
        FitResultData fit0Left = fit.getFit0Left(aitInfo, data, defSize, defIndex, skyLine);
        fit0Left.setLeft(true);
        if (fit0Left.getRecInfo() != null) {
            upSkyLine(upLink, lowLine, fit0Left.getRecInfo(), true);
            return fit0Left;
        }

        // FitResultData fit0Left = fit.getFit0LeftNew(aitInfo, fitParameter, targetData);
        // fit0Left.setLeft(true);
        // // 先加入集合
        // List<FitResultData> list0 = new ArrayList<>();
        // list0.add(fit0Left);
        // FitResultData selectSide0 = ParallelDesign.randomSelectSide(list0, skyLine);
        // FitResultData selectNoSide0 = ParallelDesign.randomSelectNoSide(list0, skyLine);
        // if (selectSide0.getRecInfo() != null) {
        //     System.out.println("起作用了吗");
        //     upSkyLine(upLink, lowLine, selectSide0.getRecInfo(), selectSide0.isLeft());
        //     return selectSide0;
        // } else if (selectNoSide0.getRecInfo() != null) {
        //     upSkyLine(upLink, lowLine, selectNoSide0.getRecInfo(), selectNoSide0.isLeft());
        //     return selectNoSide0;
        // }


        // FitResultData fit0Right = fit.getFit0Right(aitInfo, data, defSize, defIndex, skyLine);
        // if (fit0Right.getRecInfo() != null) {
        //     try {
        //         upSkyLine(upLink, lowLine, fit0Right.getRecInfo(), false);
        //     } catch (Exception e) {
        //         throw new RuntimeException(e);
        //     }
        //     return fit0Right;
        // }

        /*如果程序走到这里说明：虽然存在宽度符合条件的矩形块，但是由于缺陷块的存在导致矩形块无法放置。结论：一定是有矩形块覆盖了缺陷块*/
        // 汇总各个适应度中缺陷块的信息
        List<FitResultData> listFinals = new ArrayList<>();
        listFinals.add(fit3);
        listFinals.add(resultFit2);
        // listFinals.add(selectSide2);
        // listFinals.add(selectNoSide2);
        listFinals.add(resultFit1);
        // listFinals.add(selectSide1);
        // listFinals.add(selectNoSide1);
        // listFinals.add(selectSide0);
        // listFinals.add(selectNoSide0);
        listFinals.add(fit0Left);
        // listFinals.add(fit0Right);
        return combineDefInfo(listFinals);
    }

    /**
     * @param sw
     * @return boolean
     * @description 添加判断，是否有rwi <= sw
     * @author hao
     * @date 2023/5/12 23:16
     */
    public boolean judgeArray(TargetAitInfo aitInfo, int sw) {
        if (aitInfo.widthNodeTree.getRootWidth() <= sw) {
            return true;
        }
        return false;
    }

    /**
     * @return
     * @description 造一个方法：对矩形序列中对应的矩形数量进行删改;修改下标为index的矩形数量，如果修改之后数量为0，删除该种类型的矩形
     * @author hao
     * @date 2023/3/5 18:45
     */
    public void deleteRec(TargetAitInfo aitInfo, int[] index) {
        // 删除的方式就是 将三个辅助序列中对应的矩形信息修改   w h key 都设置为 Integer.Max_Value
        int keyIndex = index[5];
        int widthIndex = index[6];
        int heightIndex = index[7];
        // 判断 num 是否只剩一个；如果不止一个，数量减一；如果只有一个将矩形标记为已经全部放置
        if (aitInfo.keyRectangles[keyIndex][1] != 1) {
            aitInfo.keyRectangles[keyIndex][1]--;
            aitInfo.widthRectangles[widthIndex][1]--;
            aitInfo.heightRectangles[heightIndex][1]--;
            return;
        }
        // 将矩形标记为已经放置
        aitInfo.keyRectangles[keyIndex][8] = 1;
        aitInfo.widthRectangles[widthIndex][8] = 1;
        aitInfo.heightRectangles[heightIndex][8] = 1;
        // 更新宽度序列线段树
        aitInfo.widthNodeTree.update(widthIndex, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
        aitInfo.heightIndexNodeTree.updateVal(heightIndex, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    /**
     * @return
     * @description 需要更新缺陷块，在defList中查找一个y2最小的缺陷块信息，并修改defSize集合信息
     * @author hao
     * @date 2023/3/5 18:44
     */
    public DefData searchDef(List<Integer> defList, double[][] defSize) {
        int index1 = defList.get(0);
        double[] minDef = defSize[index1];
        for (int i = 0; i < defList.size(); i++) {
            double[] temp = defSize[defList.get(i)];
            if (minDef[3] > temp[3]) {
                minDef = temp;
                index1 = defList.get(i);
            }
        }
        /*    // 如果最终这些缺陷块都被删除了，也不用担心出现越界
        defSize[index1][5] = 1;*/
        // 根据defSize的信息，更新缺陷块的区域（minx1,miny1,maxx2,maxy2）
//        TargetData.oriDefRegion(defSize);
        return new DefData(minDef, index1);
    }

    /**
     * @return
     * @description 造一个方法:对每一条天际线要求返回它的最大高度
     * @author hao
     * @date 2023/3/5 17:26
     */
    public int linked_list(LinkedList<Data> upList) {
        int max = upList.get(0).skyHeight;
        for (int i = 0; i < upList.size(); i++) {
            max = Math.max(max, upList.get(i).skyHeight);
        }
        skyHeight = max;

        return max;
    }

    /**
     * @param targetDataList
     * @param path
     * @return int
     * @description 确定排序规则的顺序，skyline求解的入口
     * @author hao
     * @date 2023/5/3 22:22
     */
    public SkyLineResultData fitPack(List<TargetData> targetDataList, String path) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
        // 初始化信息
        TargetData targetData = targetDataList.get(0);
        targetData.initData(path);

        // if (targetData.targetSize.length <= 2000) {
        //     return null;
        // }

        MaxHeapSort maxHeapSort = new MaxHeapSort();
        // 创建一个SkyLine对象用于存储数据
        Map<String, SkyLineResultData> skyLines = new HashMap<>();
        Map<String, int[][]> hashMap = new HashMap<>();

        long startTimes = System.currentTimeMillis() / 1000;

        // 根据四种排序规则生成 高度h
        // 面积规则
        SkyLine skyLineA = new SkyLine();
        // 随机交换 或者 4种规则 生成的序列
        int[][] rectArea = maxHeapSort.heapSortArea(targetData.targetSize);
        TargetAitInfo aitInfoArea = new TargetAitInfo();
        aitInfoArea.setName("Area");

//        for (int i = 0; i < rectArea.length; i++) {
//            System.out.println(rectArea[i][2] * rectArea[i][3]);
//        }

        skyLineA = skyLineA.placeRec(aitInfoArea, rectArea, targetData, skyLineA);
        hashMap.put("Area", rectArea);
        SkyLineResultData skyLineAResultData = new SkyLineResultData(skyLineA, targetData);
        skyLines.put("Area", skyLineAResultData);


        // 高度规则
        SkyLine skyLineH = new SkyLine();
        TargetAitInfo aitInfoHeight = new TargetAitInfo();
        aitInfoHeight.setName("Height");
        int[][] rectHeight = maxHeapSort.heapSortHeight(targetData.targetSize);

       /* for (int i = 0; i < rectHeight.length; i++) {
            System.out.println( rectHeight[i][3]);
        }*/

        skyLineH = skyLineH.placeRec(aitInfoHeight, rectHeight, targetData, skyLineH);
        hashMap.put("Height", rectHeight);
        SkyLineResultData skyLineHResultData = new SkyLineResultData(skyLineH, targetData);
        skyLines.put("Height", skyLineHResultData);

        // 宽度规则
        SkyLine skyLineW = new SkyLine();
        TargetAitInfo aitInfoWidth = new TargetAitInfo();
        aitInfoWidth.setName("Width");
        int[][] rectWidth = maxHeapSort.heapSortWitdh(targetData.targetSize);

      /*  for (int i = 0; i < rectWidth.length; i++) {
            System.out.println( rectWidth[i][2]);
        }*/

        skyLineW = skyLineW.placeRec(aitInfoWidth, rectWidth, targetData, skyLineW);
        hashMap.put("Width", rectWidth);
        SkyLineResultData skyLineWResultData = new SkyLineResultData(skyLineW, targetData);
        skyLines.put("Width", skyLineWResultData);

        // 周长规则
        SkyLine skyLineP = new SkyLine();
        TargetAitInfo aitInfoPerimeter = new TargetAitInfo();
        aitInfoPerimeter.setName("Perimeter");
        int[][] rectPerimeter = maxHeapSort.heapSortPerimeter(targetData.targetSize);

        /*for (int i = 0; i < rectPerimeter.length; i++) {
            System.out.println(rectPerimeter[i][2] + rectPerimeter[i][3]);
        }
*/

        skyLineP = skyLineP.placeRec(aitInfoPerimeter, rectPerimeter, targetData, skyLineP);
        hashMap.put("Perimeter", rectPerimeter);
        SkyLineResultData skyLinePResultData = new SkyLineResultData(skyLineP, targetData);
        skyLines.put("Perimeter", skyLinePResultData);

        SkyLineResultData bestLine = skyLineHResultData;
        for (Map.Entry<String, SkyLineResultData> entry : skyLines.entrySet()) {
            SkyLineResultData currentLine = entry.getValue();
            bestLine = currentLine.skyLine.skyHeight > bestLine.skyLine.skyHeight ? bestLine : currentLine;
        }

        CommonToolClass commonToolClass = new CommonToolClass();
        // 初始化不同方向放置的信息
        // 靠右侧放置
        TargetData copyRightCorner = commonToolClass.deepCopyRightCorner(targetData);
        targetDataList.add(copyRightCorner);

        // 从上往下求，靠左放置
        TargetData copyUpperLeftCorner = commonToolClass.deepCopyUpperLeftCorner(targetData, bestLine.skyLine.skyHeight - 1);
        targetDataList.add(copyUpperLeftCorner);

        TargetData copyUpperRightCorner = commonToolClass.deepCopyUpperRightCorner(targetData, targetData.oriSize[0], bestLine.skyLine.skyHeight - 1);
        targetDataList.add(copyUpperRightCorner);

        AsyncExecution asyncExecution = new AsyncExecution();

        long endTimes = System.currentTimeMillis() / 1000;
        int times = 0;

        while ((endTimes - startTimes) < 60) {
            ++times;
            // 这里换为异步，每次都是新的
//            tempLine = AsyncExecution.parallelAsync(tempLine, targetData, hashMap, sortResult);
//             SkyLineResultData currentSkyData = asyncExecution.parallelAsync(skyLines, targetDataList, hashMap, startTimes, bestLine);
            SkyLineResultData currentSkyData = asyncExecution.parallelAsync(skyLines, targetDataList, hashMap, startTimes, bestLine);
            bestLine = currentSkyData.skyLine.skyHeight > bestLine.skyLine.skyHeight ? bestLine : currentSkyData;
            // if(bestLine.skyLine.skyHeight == 252){
            //     System.out.println(121);
            // }
            commonToolClass.improveUpperLeftCorner(targetDataList.get(0), targetDataList.get(2));
            commonToolClass.improveUpperRightCorner(targetDataList.get(0), targetDataList.get(3));
            endTimes = System.currentTimeMillis() / 1000;
            System.out.println("bestLine   " + bestLine.skyLine.skyHeight + " times：" + times);
        }
        System.out.println("bestLine   " + bestLine.skyLine.skyHeight + " times：" + times);
        return bestLine;
    }

    /**
     * @param targetData
     * @param rec
     * @description 根据排序规则得到n次交换的最优解
     * @author hao
     * @date 2023/5/4 10:12
     */


    public SkyLine randomLS(TargetAitInfo aitInfoArea, SkyLine initialRuleSolution, TargetData targetData, int[][] rec, double startTime) throws InvocationTargetException, IllegalAccessException {

        SkyLine skyLineOri = copyLine(initialRuleSolution);
        CommonToolClass commonToolClass = new CommonToolClass();
        // 先拷贝一份临时变量
        int[][] recCopyOri = commonToolClass.assistArrayRec(rec);
        int[][] recCopy = commonToolClass.assistArrayRec(rec);
        int exchangeTime = 0;
        int multiple = 1;
        // 重置静态变量
        TransRule multipoint = new TransRule();
//        TransRule.temperature = recCopy.length * 0.2;

        while (exchangeTime < multiple * recCopyOri.length) {
            long endTimes = System.currentTimeMillis() / 1000;
            if ((endTimes - startTime) > 60) {
                return skyLineOri;
            }
            // 单点变异
            multipoint.singlePointVariation(recCopy);
            // 多点变异
//            multipoint.multipointVariation(multipoint, recCopy, exchangeTime);
//            System.out.println("TransRule.multipoint_Variation   " + TransRule.multipoint_Variation + "  exchangeTime    " + exchangeTime);
            // 再比较
            // recCopy的信息：索引，矩形块的索引，数量、宽度、高度、key 排序的下标、宽度 排序的下标、高度 排序的下标、矩形块是否被放置
            SkyLine skyLine = new SkyLine(recCopy);
            skyLine = skyLine.placeRec(aitInfoArea, recCopy, targetData, skyLine);

            // if(skyLine.skyHeight <= 241 && targetData.checkHeight == 241){
            //     System.out.println(888);
            // }

            int height = skyLine.skyHeight;
            // System.out.println("      height = " + height);
            if (skyLineOri.skyHeight > height) {
//                System.out.println("initPositions   " + skyLineOri.skyHeight);
//                System.out.println("height  " + height);
//                System.out.println("iteration   " + exchangeTime);
//                tempLine = skyLine;
                skyLineOri = skyLine;
                recCopyOri = recCopy;
            } else {
                recCopy = recCopyOri;
            }
            exchangeTime++;
        }
        // if (skyLineOri.skyHeight <= 241 && targetData.checkHeight == 241) {
        //     System.out.println(888);
        // }
//        System.out.println(" initialRuleSolution.skyHeight   = " + initialRuleSolution.skyHeight + "skyLineOri.skyHeight  = " + skyLineOri.skyHeight);
        return skyLineOri;
    }

    /**
     * @param array
     * @return double[][]
     * @description 缺陷块拷贝
     * @author hao
     * @date 2023/5/31 16:35
     */
    public double[][] copyDef(int[][] array) {
        if (array.length == 0) {
            return null;
        }
        int size1 = array.length;
        int size2 = 6;
        double[][] temp = new double[size1][size2];
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < 4; j++) {
                temp[i][j] = array[i][j];
            }
            temp[i][4] = Math.sqrt(temp[i][0] * temp[i][0] + temp[i][1] * temp[i][1]);
        }
        return temp;
    }

    /**
     * @param fitListData
     * @return FitResultData
     * @description 合并缺陷块的信息
     * @author hao
     * @date 2023/5/31 16:42
     */
    public FitResultData combineDefInfo(List<FitResultData> fitListData) {
        FitResultData fitResultData = new FitResultData();
        List<Integer> defIndex = fitResultData.getDefIndex();
        for (FitResultData frs : fitListData) {
            List<Integer> defIndex1 = frs.getDefIndex();
            for (int index : defIndex1) {
                if (!defIndex.contains(index)) {
                    defIndex.add(index);
                }
            }
        }
        // 有一个问题，get出来的，修改完，原对象信息会有变化吗
        return fitResultData;
    }

    //    public SkyLine copyLine(SkyLine skyLine) {
//        return new SkyLine(skyLine.arrayList, skyLine.skyHeight, skyLine.individualObjects);
//    }
    public SkyLine copyLine(SkyLine skyLine) {
        return new SkyLine(skyLine.arrayList, skyLine.skyHeight, skyLine.rectangularSequence);
    }

    /**
     * @return
     * @description 造一个方法，给一个矩形序列,一个缺陷块序列，一个矩形信息：
     * 1、查找skyline，确定si 对应方法：searchLine
     * 2、使用select选择合适的矩形放置在si上，有三种返回值：-2，-3，矩形下标 对应方法：select
     * 3、返回值为-2，需要合并天际线。对应方法：combine
     * 返回值为-3，需要更新缺陷块，在defList中查找一个y2最小的缺陷块信息。对应方法：upDef
     * 矩形下标，需要对矩形序列中对应的矩形数量进行删改 对应方法：无
     * 4、递归
     * 5、全部放置完成后返回天际线
     * @author hao
     * @date 2023/3/5 18:52
     */
    public SkyLine placeRec(TargetAitInfo aitInfo, int[][] targetSize, TargetData targetData, SkyLine skyLine) throws InvocationTargetException, IllegalAccessException {
        // 根据序列生成 3个辅助序列
        targetData.initOrders(targetSize, aitInfo);
        aitInfo.oriArea = targetData.oriSize;

        // 首先初始化链表       到底是使用递归还是while，优先使用while\for节省空间
        LinkedList<Data> upLink = oriSky(targetData);
        // 初始化缺陷块   每一个序列都要重置缺陷块，使用完标记
        double[][] defArray = copyDef(targetData.defPoints);
        QuickSortDef quickSortDefBlock = new QuickSortDef();
        quickSortDefBlock.quickSortDef(defArray);
        while (true) {
            // 查找一个lowLine
            int lowLine = searchLine(upLink);

//            for (int i = 0; i < defArray.length; i++) {
//                if(defArray[i][0] == 69 && defArray[i][1] == 92 && defArray[i][2] == 96){
//                    // if (upLink.size() >= 3 && upLink.get(0).skyHeight >= 120 && upLink.get(1).skyHeight >= 40) {
//                    //     System.out.println(33);
//                    // }
//                    printfSky(upLink);
//                }
//            }



            // 添加判断，如果找到的si，没有矩形可以放置；那么直接去合并不再进行以下工作     true表示存在sw >= rw,说明放得下,则不合并
            boolean b = judgeArray(aitInfo, upLink.get(lowLine).skyWidth);
            if (!b) {
                // 放不下，直接去合并天际线
                try {
                    upLink = combine(upLink, lowLine);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            // 此时一定有可以放置在最低水平线上的矩形，但是不确定是否会覆盖缺陷块。使用 select（R0的下标 对应：R0[][0]） 去选择合适的矩形进行放置，对返回值进行判断。
            FitResultData select = select(aitInfo, targetData, lowLine, upLink, defArray, skyLine);
            if (select.getRecInfo() == null) {
                // 获取缺陷块的信息，查找一个y2最小的缺陷块信息，
                DefData defData = null;
                try {

                    defData = searchDef(select.getDefIndex(), defArray);

                    // System.out.println(Arrays.toString(defData.defInfo));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                // if (defData.defInfo[0] == 34 && defData.defInfo[1] == 222 && defData.defInfo[2] == 66) {
                //     System.out.println(33);
                // }

                // 原更新方式
                // double[] defInfo = defData.defInfo;
                // Data lowLineData = upLink.get(lowLine);
                // // 判断更新天际线还是更新缺陷块
                // if ((defInfo[3] > lowLineData.skyHeight) &&
                //         (defInfo[2] <= (lowLineData.x1 + lowLineData.skyWidth)
                //                 && (lowLineData.x1 <= defInfo[0]))) {
                //     // defList使用完之后需要清空
                //     defArray[defData.defIndex][5] = 1;
                //     upLink = upDef(defInfo, upLink);
                // } else {
                //     upLink = combine(upLink, lowLine);
                // }

                // 更新缺陷块
                defLift(upLink, defData, lowLine);
                if (defData.isUsed) {
                    defArray[defData.defIndex][5] = 1;
                }

            } else {
                // 目标矩形已经放入成功，需要从 3个辅助矩形序列 和 主序列 中去除 select
                deleteRec(aitInfo, select.getRecInfo());
            }

            // printfSky(upLink);

            // 判断是否所有的矩形都放入  （需要修改）
            if (aitInfo.widthNodeTree.getRootMinVal() == Integer.MAX_VALUE) {
                break;
            }
        }
        skyLine.linked_list(upLink);
        for (int i = 0; i < defArray.length; i++) {
            if(defArray[i][0] == 69 && defArray[i][1] == 92 && defArray[i][2] == 96 && skyLine.skyHeight <= 241){
                System.out.println(43);;
            }
        }
        // if(skyLine.skyHeight <= 241 && targetData.checkHeight == 241){
        //     System.out.println(888);
        // }
        return skyLine;
    }

    // 精确算法
    public SkyLine fitPackExcat(TargetData targetData, String path) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
        if (targetData.targetSize.length > 2000) {
            return null;
        }
        // 创建一个SkyLine对象用于存储数据
        SkyLine tempLine = new SkyLine();
//        List<SkyLine> skyLines = new ArrayList<>();
        Map<String, SkyLine> skyLines = new HashMap<>();
        Map<String, int[][]> hashMap = new HashMap<>();

        MaxHeapSort maxHeapSort = new MaxHeapSort();

        // 根据四种排序规则生成 高度h
        // 面积规则
        SkyLine skyLineA = new SkyLine();
        // 随机交换 或者 4种规则 生成的序列
        int[][] rectArea = maxHeapSort.heapSortArea(targetData.targetSize);
        TargetAitInfo aitInfoArea = new TargetAitInfo();
        aitInfoArea.setName("Area");

//        for (int i = 0; i < rectArea.length; i++) {
//            System.out.println(rectArea[i][2] * rectArea[i][3]);
//        }

        skyLineA = skyLineA.placeRec(aitInfoArea, rectArea, targetData, skyLineA);
//        int areaHeight = skyLineA.skyHeight;
        skyLines.put("Area", skyLineA);
//        sortResult[0][0] = areaHeight;
        hashMap.put("Area", rectArea);

        // 高度规则
        SkyLine skyLineH = new SkyLine();
        TargetAitInfo aitInfoHeight = new TargetAitInfo();
        aitInfoHeight.setName("Height");
        int[][] rectHeight = maxHeapSort.heapSortHeight(targetData.targetSize);

       /* for (int i = 0; i < rectHeight.length; i++) {
            System.out.println( rectHeight[i][3]);
        }*/

        skyLineH = skyLineH.placeRec(aitInfoHeight, rectHeight, targetData, skyLineH);
//        int Height = skyLineH.skyHeight;
        skyLines.put("Height", skyLineH);
//        sortResult[1][0] = Height;
        hashMap.put("Height", rectHeight);

        // 宽度规则
        SkyLine skyLineW = new SkyLine();
        TargetAitInfo aitInfoWidth = new TargetAitInfo();
        aitInfoWidth.setName("Width");
        int[][] rectWidth = maxHeapSort.heapSortWitdh(targetData.targetSize);

      /*  for (int i = 0; i < rectWidth.length; i++) {
            System.out.println( rectWidth[i][2]);
        }*/

        skyLineW = skyLineW.placeRec(aitInfoWidth, rectWidth, targetData, skyLineW);
//        int areaWidth = skyLineW.skyHeight;
        skyLines.put("Width", skyLineW);
//        sortResult[2][0] = areaWidth;
        hashMap.put("Width", rectWidth);

        // 周长规则
        SkyLine skyLineP = new SkyLine();
        TargetAitInfo aitInfoPerimeter = new TargetAitInfo();
        aitInfoPerimeter.setName("Perimeter");
        int[][] rectPerimeter = maxHeapSort.heapSortPerimeter(targetData.targetSize);

        /*for (int i = 0; i < rectPerimeter.length; i++) {
            System.out.println(rectPerimeter[i][2] + rectPerimeter[i][3]);
        }
*/

        skyLineP = skyLineP.placeRec(aitInfoPerimeter, rectPerimeter, targetData, skyLineP);
//        int areaPer = skyLineP.skyHeight;
        skyLines.put("Perimeter", skyLineP);
//        sortResult[3][0] = areaPer;
        hashMap.put("Perimeter", rectPerimeter);
        SkyLine bestLine = skyLineH;
        for (Map.Entry<String, SkyLine> entry : skyLines.entrySet()) {
            SkyLine currentLine = entry.getValue();
            bestLine = currentLine.skyHeight > bestLine.skyHeight ? bestLine : currentLine;
        }

        long startTimes = System.currentTimeMillis() / 1000;
        long endTimes = System.currentTimeMillis() / 1000;
        AsyncExecution asyncExecution = new AsyncExecution();
        while ((endTimes - startTimes) < 0.5) {
            // 这里换为异步，每次都是新的
//            tempLine = AsyncExecution.parallelAsync(tempLine, targetData, hashMap, sortResult);
            SkyLine currentLine = asyncExecution.parallelAsync(skyLines, targetData, hashMap, startTimes);
            bestLine = currentLine.skyHeight > bestLine.skyHeight ? bestLine : currentLine;
            endTimes = System.currentTimeMillis() / 1000;
        }
        return bestLine;
    }

    /**
     * 检查天际线是否正确
     */
    public boolean judgeSkyLineRh(LinkedList<Data> upLink) {
        for (int i = 0; i < upLink.size() - 1; ++i) {
            if (upLink.get(i).skyHeight + upLink.get(i).srh != upLink.get(i + 1).skyHeight) {
                return false;
            }
        }
        return true;
    }

    public boolean judgeSkyLineLh(LinkedList<Data> upLink) {
        for (int i = 0; i < upLink.size() - 1; ++i) {
            if (upLink.get(i).skyHeight - upLink.get(i + 1).slh != upLink.get(i + 1).skyHeight) {
                return false;
            }
        }
        return true;
    }

    public void printfSky(LinkedList<Data> upLink) {
        for (Data data : upLink) {
            System.out.println(data);
        }
        System.out.println("-----------------");
    }

    public boolean judgeSkyWidth(LinkedList<Data> upLink) {
        int sum = 0;
        for (Data data : upLink) {
            sum += data.skyWidth;
        }
        if (sum != 200) {
            return true;
        }
        return false;
    }
}
