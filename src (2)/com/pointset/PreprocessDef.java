package com.pointset;


import com.commonfunction.CommonToolClass;
import com.twodimension.TargetData;

import java.io.FileNotFoundException;
import java.util.*;

public class PreprocessDef {

    /**
     * @param defPoints
     * @param widthPoints
     * @param heightPoints
     * @return int[][]
     * @description 重新确定缺陷点的坐标（x1,y1,x2,y2）
     * @author hao
     * @date 2023/6/30 16:32
     */
    public static int[][] updataDefPoint(int[][] defPoints, int[] widthPoints, int[] heightPoints) {
        try {
            for (int i = 0; i < defPoints.length; i++) {
                // 确定X1
                defPoints[i][0] = detemineCoordLower(defPoints[i][0], widthPoints);
                // 确定Y1
                defPoints[i][1] = detemineCoordLower(defPoints[i][1], heightPoints);
                // 确定X2
                defPoints[i][2] = detemineCoordUpper(defPoints[i][2], widthPoints);
                // 确定Y2
                defPoints[i][3] = detemineCoordUpper(defPoints[i][3], heightPoints);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return defPoints;
    }

    /**
     * @param number
     * @param array  离散点集
     * @return int
     * @description 重新确定缺陷点的  左下坐标
     * 前提：array递增排序
     * @author hao
     * @date 2023/6/30 16:36
     */
    public static int detemineCoordLower(int number, int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (number == array[i]) {
                return number;
            }
            if (array[i] > number) {
                if (i - 1 >= 0) {
                    return array[i - 1];
                } else {
                    return 0;
                }
            }
        }
        return number;
    }

    /**
     * @param number 缺陷块的坐标值
     * @param array  离散点集
     * @return int
     * @description 重新确定缺陷点的  右上坐标
     * 前提：array递增排序
     * @author hao
     * @date 2023/6/30 16:51
     */
    public static int detemineCoordUpper(int number, int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] >= number) {
                return array[i];
            }
        }
        return number;
    }

    /**
     * @return boolean
     * @description 确定有交集的缺陷块集合
     * @author hao
     * @date 2023/6/30 17:23
     * defPoints  传入的是无序的，经过 judgeDef 中的 ToolClass.sortedTwoDim 处理变为有序的
     */
    public List<List<Integer>> judgeDef(int[][] defPoints) {
        CommonToolClass commonToolClass = new CommonToolClass();
        // 先对defPoints排序
        commonToolClass.sortedTwoDim(defPoints, 1);
        List<List<Integer>> lists = new ArrayList<>();
        for (int i = 0; i < defPoints.length; i++) {
            for (int j = i + 1; j < defPoints.length; j++) {
                // 判断当前缺陷块是否与其他缺陷块有交集
                //xd1 yd1 xd2 yd2   xd1 <= totalWidth <= xd2
                int width = defPoints[j][2] - defPoints[j][0];
                int height = defPoints[j][3] - defPoints[j][1];
                double x1 = defPoints[i][0];
                double x2 = defPoints[i][2] + width;
                double y1 = defPoints[i][1];
                double y2 = defPoints[i][3] + height;
                //只有同时满足这四个表达式，才能说明目标块覆盖了缺陷块
                if (defPoints[j][2] > x1 && defPoints[j][2] < x2 && defPoints[j][3] > y1 && defPoints[j][3] < y2) {
                    // 说明缺陷块会产生交集
                    // 首次产生交集
                    if (lists.size() == 0) {
                        List<Integer> listCoord = new ArrayList<>();
                        listCoord.add(i);
                        listCoord.add(j);
                        lists.add(listCoord);
                    } else {
                        // 不是首次，先判断之前有没有加入
                        int k = 0;
                        // 判断是否需要添加新的组
                        boolean isExits = false;
                        for (; k < lists.size(); k++) {
                            boolean contains1 = isContains(lists.get(k), i);
                            boolean contains2 = isContains(lists.get(k), j);
                            // 同假才修改。同真说明已经存在
                            if (!contains1 && !contains2) {
                                isExits = true;
                            }
                            // 一真一假才可以加入
                            if (contains1) {
                                if (!contains2) {
                                    isExits = false;
                                    lists.get(k).add(j);
                                }
                            } else {
                                if (contains2) {
                                    isExits = false;
                                    lists.get(k).add(i);
                                }
                            }
                        }
                        // 说明当前产生的交集是一个新组
                        if (k == lists.size() && isExits) {
                            List<Integer> listCoord = new ArrayList<>();
                            listCoord.add(i);
                            listCoord.add(j);
                            lists.add(listCoord);
                        }
                    }
                }
            }
        }
        return lists;
    }

    /**
     * @param temp      产生交集的矩形集合，n组
     * @param defPoints 缺陷块的坐标点，传入的已经是 以y1递增 排序好的
     * @description 根据交集重新生成新的缺陷块
     * @author hao
     * @date 2023/7/1 13:35
     */
    public List<int[]> defCoord(List<List<Integer>> temp, int[][] defPoints) {
        // 划分 def交集 产生的矩形
        List<int[]> recList = new ArrayList<>();
        // List每一个元素代表一组交集
        List<Map<Integer, List<int[]>>> hashMapsList = new ArrayList<>();
        // 收集每一组的Y
        List<List<Integer>> aitList = new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            Map<Integer, List<int[]>> tempMap = new LinkedHashMap<Integer, List<int[]>>();
            // 获取一组交集的y
            List<Integer> infoYCoord = infoYCoord(temp.get(i), defPoints);
            // 根据每一个y 确定交集x
            for (int j = 0; j < infoYCoord.size(); j++) {
                int yInt = infoYCoord.get(j);
                List<int[]> tempList = new ArrayList<>();
                // 求覆盖当前行的缺陷块的x1,x2
                for (int k = 0; k < defPoints.length; k++) {
                    if (defPoints[k][1] <= yInt && defPoints[k][3] >= yInt) {
                        int[] ints = new int[2];
                        ints[0] = defPoints[k][0];
                        ints[1] = defPoints[k][2];
                        tempList.add(ints);
                    }
                }
                // 对集合 { (x1,x2) } 以 x1 排序
                sortListInt(tempList);
                int times = tempList.size();
                //对交集合并
                for (int k = 0; k < times; k++) {
                    if (k + 1 >= times) {
                        break;
                    }
                    int[] arr1 = tempList.get(k);
                    int[] arr2 = tempList.get(k + 1);
                    // arr1 x2 >= arr2 x1
                    if (arr1[1] >= tempList.get(k + 1)[0] && arr1[1] <= tempList.get(k + 1)[1]) {
                        arr1[1] = arr2[1];
                        tempList.set(k, arr1);
                        tempList.remove(k + 1);
                        --k;
                        --times;
                    } else if (arr1[0] <= tempList.get(k + 1)[0] && arr1[1] >= tempList.get(k + 1)[1]) {
                        tempList.remove(k + 1);
                        --k;
                        --times;
                    }
                }
                tempMap.put(yInt, tempList);
            }
            // 当前组的 y 集合
            aitList.add(infoYCoord);
            // 当前的 x坐标集合
            hashMapsList.add(tempMap);
        }

        // 获取每组集合的y值
        for (int i = 0; i < aitList.size(); i++) {
            // 获取当前组的 y x
            Map<Integer, List<int[]>> integerListMap = hashMapsList.get(i);
            // 对于当前组的每一个y
            List<Integer> list1 = aitList.get(i);
            for (int j = 0; j < list1.size(); j++) {
                if (j + 1 == list1.size()) {
                    break;
                }
                int y1 = list1.get(j);
                int y2 = list1.get(j + 1);
                int height = y2 - y1;
                // 获取 y1 对应的交集 (x1,x2)
                List<int[]> listX1 = integerListMap.get(y1);
                // 获取 y2 对应的交集 (x1,x2)
                List<int[]> listX2 = integerListMap.get(y2);
                for (int k = 0; k < listX1.size(); k++) {
                    int[] arr1 = listX1.get(k);
                    int width = 0;
                    for (int l = 0; l < listX2.size(); l++) {
                        int[] arr2 = listX2.get(l);
                        // arr1 x2 < arr2 x1 说明此后再无交集
                        if (arr1[1] < arr2[0]) {
                            break;
                        }
                        // arr1 x1 > arr2 x2 说明还未产生交集
                        if (arr1[0] > arr2[1]) {
                            continue;
                        }
                        width = Math.min(arr1[1], arr2[1]) - Math.max(arr1[0], arr2[0]);
                    }
                    int[] defRec = {width, height};
                    recList.add(defRec);
                }
            }
        }
        return recList;
    }


    /**
     * @param temp
     * @param defPoints 是已经y1排序的defPoints，与temp中的索引相对应
     * @param recList
     * @description 获取缺陷块集合的长宽，用于对偶可行函数
     * @author hao
     * @date 2023/7/1 21:36
     */
    public List<int[]> combineSize(List<List<Integer>> temp, int[][] defPoints, List<int[]> recList) {
        List<Integer> arrayList = new ArrayList<>();
        for (List<Integer> list1 : temp) {
            for (int var : list1) {
                if (!arrayList.contains(var)) {
                    arrayList.add(var);
                }
            }
        }
        for (int i = 0; i < defPoints.length; i++) {
            if (!arrayList.contains(i)) {
                int width = defPoints[i][2] - defPoints[i][0];
                int height = defPoints[i][3] - defPoints[i][1];
                int[] arr1 = new int[3];
                arr1[0] = width;
                arr1[1] = height;
                // 数量
                arr1[2] = 1;
                recList.add(arr1);
            }
        }

//        System.out.println("最终结果：；；；；；");
//        for (int[] arr : recList) {
//            System.out.println(Arrays.toString(arr));
//        }

        return recList;
    }

    /**
     * @param tempList
     * @description 对tempList排序
     * @author hao
     * @date 2023/7/1 15:55
     */
    public void sortListInt(List<int[]> tempList) {
        Collections.sort(tempList, new ArrayComparator());
    }

    class ArrayComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] p1, int[] p2) {
            // 由小到大排序
            return p1[0] - p2[0];
        }
    }

    /**
     * @return boolean
     * @description 根据交集信息得到y坐标集合，再排序
     * @author hao
     * @date 2023/7/1 14:22
     */
    public List<Integer> infoYCoord(List<Integer> tempList, int[][] defPoints) {
        List<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            int index = tempList.get(i);
            if (!arrayList.contains(defPoints[index][1])) {
                arrayList.add(defPoints[index][1]);
            }
            if (!arrayList.contains(defPoints[index][3])) {
                arrayList.add(defPoints[index][3]);
            }
        }
        Collections.sort(arrayList);
        return arrayList;
    }


    public boolean isContains(List<Integer> lists, int temp) {
        for (int j = 0; j < lists.size(); j++) {
            if (lists.get(j) == temp) {
                return true;
            }
        }
        return false;
    }

//    // 预处理缺陷块，并生成长宽， 并且原defpoints 坐标会被更新，会重新 以 y1 排序
//    public static List<int[]> newDefSize( TargetData layOutData, PreprocessDef preprocessDef, PointReductionMethod discretePoints) throws FileNotFoundException {
//        // 预处理缺陷块 预处理之后，原 defPoints 坐标会被更新
//        int[][] ints = updataDefPoint(layOutData.defPoints, discretePoints.getWidthPoints(), discretePoints.getHeightPoints());
//
//        // 得到有交集的缺陷块索引
//        List<List<Integer>> lists = preprocessDef.judgeDef(ints);
//
//        // 对有交集的缺陷块重新划分
//        List<int[]> ints1 = preprocessDef.defCoord(lists, layOutData.defPoints);
//
//        // 获取新的缺陷块的长宽集合
//        List<int[]> defSize = preprocessDef.combineSize(lists, layOutData.defPoints, ints1);
//        return defSize;
//    }

    public static List<int[]> newDefSizes(TargetData layOutData, PreprocessDef preprocessDef, int[] widthPoints, int[] heightPoints) throws FileNotFoundException {
        // 预处理缺陷块 预处理之后，原 defPoints 坐标会被更新
        int[][] ints = updataDefPoint(layOutData.defPoints, widthPoints, heightPoints);

        // 得到有交集的缺陷块索引
        List<List<Integer>> lists = preprocessDef.judgeDef(ints);

        // 对有交集的缺陷块重新划分
        List<int[]> ints1 = preprocessDef.defCoord(lists, layOutData.defPoints);

        // 获取新的缺陷块的长宽集合
        List<int[]> defSize = preprocessDef.combineSize(lists, layOutData.defPoints, ints1);
        return defSize;
    }



    public static void main(String[] args) throws FileNotFoundException {
//        GetFile getFile = new GetFile();
//        File file = new File("E:\\EssayTestSet\\2D-SPPActual\\Test\\cplex_test\\part02");
//        List<File> allFile = getFile.getAllFile(file);
//        for (File value : allFile) {
//            String path = value.getAbsolutePath();
//
//            TargetData layOutData = new TargetData();
//            layOutData.initData(path);
//            List<int[]> recList = PreprocessDef.newDefSize(layOutData);
//
//            System.out.println("最终结果：；；；；；");
//            for (int[] arr : recList) {
//                System.out.println(Arrays.toString(arr));
//            }
//
//            int[][] array = {{1, 1, 1}, {3, 2, 3}};
//            int[][] ints = ToolClass.copyTwoDim(array, recList);
//            for (int[] arr: ints) {
//                System.out.println(Arrays.toString(arr));
//            }
//
//        }
    }
}
