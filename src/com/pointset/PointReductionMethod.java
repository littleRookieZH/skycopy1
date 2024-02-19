package com.pointset;

import com.commonfunction.CommonToolClass;
import com.twodimension.GetFile;
import com.twodimension.TargetData;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hao
 * @description 矩形块减点方法  离散点的减点
 * @date 2023/3/6 15:59
 * @return
 */

public class PointReductionMethod {
    /**
     * @description 宽度分界线
     */
    public int widthDividing;

    /**
     * @description 高度分界线
     */
    public int heightDividing;

    /**
     * @description 记录左侧点集
     */
    public int[] leftPoints;

    /**
     * @description 记录右侧点集
     */
    public int[] rightPoints;
    /**
     * @description 记录下侧点集
     */
    public int[] bottomPoints;

    /**
     * @description 记录上侧点集
     */
    public int[] abovePoints;

    /**
     * @description 宽度点集
     */
    public int[] widthPoints;
    /**
     * @description 高度点集
     */
    public int[] heightPoints;


    public void setWidthDividing(int widthDividing) {
        this.widthDividing = widthDividing;
    }

    public void setHeightDividing(int heightDividing) {
        this.heightDividing = heightDividing;
    }

    public void setLeftPoints(int[] leftPoints) {
        this.leftPoints = leftPoints;
    }

    public void setRightPoints(int[] rightPoints) {
        this.rightPoints = rightPoints;
    }

    public void setBottomPoints(int[] bottomPoints) {
        this.bottomPoints = bottomPoints;
    }

    public void setAbovePoints(int[] abovePoints) {
        this.abovePoints = abovePoints;
    }

    public int[] getLeftPoints() {
        return leftPoints;
    }

    public int[] getRightPoints() {
        return rightPoints;
    }

    public int[] getBottomPoints() {
        return bottomPoints;
    }

    public int[] getAbovePoints() {
        return abovePoints;
    }

    public int getWidthDividing() {
        return widthDividing;
    }

    public int getHeightDividing() {
        return heightDividing;
    }


    public int[] getWidthPoints() {
        return widthPoints;
    }

    public void setWidthPoints(int[] widthPoints) {
        this.widthPoints = widthPoints;
    }

    public int[] getHeightPoints() {
        return heightPoints;
    }

    public void setHeightPoints(int[] heightPoints) {
        this.heightPoints = heightPoints;
    }

    /**
     * @param recData
     * @param isWidth
     * @description 生成X的点集（考虑左右两侧）
     * @author hao
     * @date 2023/6/29 22:51
     */
    public void getXOrYCoordinates(TargetData recData, boolean isWidth) {
        //左侧点集
        DefectLeftPointsWidth nflp = new DefectLeftPointsWidth();
        //这里是源data的地址
        nflp.setRecLayOutData(recData);
        //右侧点集
        DefectRightPointsWidth points = new DefectRightPointsWidth();
        points.setRecLayOutData(recData);
        //确定index是0还是1；0表示计算的是宽度点集，1表示的是高度点集
        int index = isWidth ? 0 : 1;

        //确定矩形的最大宽度或者高度
        // 有问题
        nflp.getMaxLength(index);

        //得到一个待排列组合 比如A组：3个 B组：2个等
        List<List<Integer>> combination = nflp.getCombination(index);

        // 得到左侧（index == 0）或者 下侧（index == 1） ,求左侧传入右上(x2,y2)，需要使用x2
        int[] deLeftPoints = nflp.getDeLeftPoints(recData.defectUpperRight, combination, 0, new int[recData.targetNum], index);

//        System.out.println("得到左侧离散点集    "  + Arrays.toString(deLeftPoints));
//        System.out.println("得到左侧离散点的个数为  " + toArray1.length);

        //得到右侧带缺陷点集 ，求右侧传入右上(x2,y2)，需要使用x2
        int[] defRightPoints = points.getDefRightPoints(recData.defectLowerLeft, combination, 0, new int[recData.targetNum], index);

//        System.out.println("得到右侧离散点集    " + Arrays.toString(defRightPoints));
        //根据t查找最少离散点
        List<Integer> integerList = this.getBoundary(deLeftPoints, defRightPoints, recData, isWidth);
        int[] ints = CommonToolClass.listToArray(integerList);

//        System.out.println("减点之后的点集：    "  + Arrays.toString(ints));
//        System.out.println("减点之后的个数为  " + ints.length);

        if (isWidth) {
            setWidthPoints(ints);
        } else {
            setHeightPoints(ints);
        }

    }

    /**
     * @description  重新计算高度离散点
     * @author  hao
     * @date    2023/7/12 12:41
     * @param recData
     * @param isWidth
    */
    public void getYCoordinates(TargetData recData, boolean isWidth) {
        //左侧点集
        DefectLeftPointsWidth nflp = new DefectLeftPointsWidth();
        //这里是源data的地址
        nflp.setRecLayOutData(recData);
        //右侧点集
        DefectRightPointsWidth points = new DefectRightPointsWidth();
        points.setRecLayOutData(recData);
        //确定index是0还是1；0表示计算的是宽度点集，1表示的是高度点集
        int index = isWidth ? 0 : 1;
        //确定矩形的最大宽度或者高度
        nflp.getMaxLength(index);
        //得到一个待排列组合 比如A组：3个 B组：2个等
        List<List<Integer>> combination = nflp.getCombination(index);

        // 得到左侧（index == 0）或者 下侧（index == 1） ,求左侧传入右上(x2,y2)，需要使用x2
        int[] deLeftPoints = nflp.getDeLeftPoints(recData.defectUpperRight, combination, 0, new int[recData.targetNum], index);

//        //得到右侧带缺陷点集 ，求右侧传入右上(x2,y2)，需要使用x2
//        int[] defRightPoints = points.getDefRightPoints(recData.defectLowerLeft, combination, 0, new int[recData.targetNum], index);
//
//        //根据t查找最少离散点
//        List<Integer> integerList = this.getBoundary(deLeftPoints, defRightPoints, recData, isWidth);
//        int[] ints = ToolClass.listToArray(integerList);

        if (!isWidth) {
            setHeightPoints(deLeftPoints);
        }
    }


    /**
     * @param arr
     * @return int
     * @description 获取最大尺寸
     * @author hao
     * @date 2023/3/23 17:26
     */
    public int getMaxLength(int[] arr) {
        int max = arr[0];
        for (int i = 0; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
        }
        return max;
    }

    /**
     * @param recData
     * @param isWidth
     * @return int[]
     * @description 计算宽度、高度离散点集（左下）
     * @author hao
     * @date 2023/6/29 23:11
     */
    public int[] getLeftLowerPoints(TargetData recData, boolean isWidth) {
        //左侧点集
        DefectLeftPointsWidth nflp = new DefectLeftPointsWidth();
        //这里是源data的地址
        nflp.setRecLayOutData(recData);
        //确定index是0还是1；0表示计算的是宽度点集，1表示的是高度点集
        int index = isWidth ? 0 : 1;
        //确定矩形的最大宽度
        nflp.getMaxLength(index);
        //得到一个待排列组合
        List<List<Integer>> combination = nflp.getCombination(index);
        //得到左侧（index == 0）带缺陷点集
        int[] deLeftPoints = nflp.getDeLeftPoints(recData.defectUpperRight, combination, 0, new int[recData.targetNum], index);
        return deLeftPoints;
    }

    /**
     * @param recData
     * @return int[]
     * @description 获取靠左侧放置的缺陷点集
     * @author hao
     * @date 2023/3/27 22:25
     */
    public int[] getLeftBottom(TargetData recData) {
        //左侧点集
        DefectLeftPointsWidth nflp = new DefectLeftPointsWidth();
        //这里是源data的地址
        nflp.setRecLayOutData(recData);
        //确定矩形的最大宽度
        nflp.getMaxLength(0);
        //得到一个待排列组合
        List<List<Integer>> combination = nflp.getCombination(0);
        //得到左侧（index == 0）带缺陷点集
        int[] deLeftPoints = nflp.getDeLeftPoints(recData.defectUpperRight, combination, 0, new int[recData.targetNum], 0);
        return deLeftPoints;
    }

    /**
     * @param recData
     * @return int[]
     * @description 获取靠左侧放置的无缺陷点集
     * @author hao
     * @date 2023/6/29 22:54
     */
    public int[] getLeftBottomNoDef(TargetData recData) {
        //左侧点集
        DefectLeftPointsWidth nflp = new DefectLeftPointsWidth();
        //这里是源data的地址
        nflp.setRecLayOutData(recData);
        //确定矩形的最大宽度
        nflp.getMaxLength(0);
        //得到一个待排列组合
        List<List<Integer>> combination = nflp.getCombination(0);
        //得到左侧（index == 0）带无缺陷点集
        nflp.noDefLeftPoints(combination, 0, new int[recData.targetNum], 0);
        //对无缺陷点集进行排序
        int[] noDefectLeftPoints = CommonToolClass.listToArray(nflp.getPointArray());
        return noDefectLeftPoints;
    }

    /**
     * @param leftPoints  一个靠左放置的点集
     * @param rightPoints 一个靠右放置的点集
     * @param isWidth     计算的是宽度分界线 还是 高度分界线
     * @return int 返回的是 t 的位置
     * @description
     * @author hao
     * @date 2023/3/9 8:54
     */
    public List<Integer> getBoundary(int[] leftPoints, int[] rightPoints, TargetData recData, boolean isWidth) {
        //创建两个临时List数组
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        List<Integer> arrayList = new ArrayList<>();
        //确定 计算的是宽度点集 还是 高度点集
        int temp = isWidth ? 0 : 1;
        int lengthSize = recData.oriSize[temp];
        //先假定一个最小长度
        int index1 = leftPoints.length;
        //最小长度对应的值
        int minValue1 = 0;
        try {
            minValue1 = leftPoints[index1 - 1];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < lengthSize + 1; i++) {
            int k = 0;
            for (int leftPoint : leftPoints) {
                if (leftPoint >= i) {
                    break;
                }
                k++;
            }

            for (int m = rightPoints.length - 1; m >= 0; m--) {
                if (rightPoints[m] <= i) {
                    break;
                }
                k++;
            }
            if (CommonToolClass.contains(leftPoints, i) || CommonToolClass.contains(rightPoints, i)) {
                k++;
            }
            if (index1 > k) {
                minValue1 = i;
                index1 = k;
            }
        }

        for (int a1 : leftPoints) {
            if (a1 < minValue1) {
                list1.add(a1);
                arrayList.add(a1);
            }
        }
        for (int a2 : rightPoints) {
            if (a2 > minValue1) {
                list2.add(a2);
                arrayList.add(a2);
            }
        }
        //minValue表示左右点集合并之后的总离散点数，arrayList.size()表示当前离散点数;如果不相等,说明index需要添加
        if (index1 != arrayList.size()) {
            arrayList.add(minValue1);
            //确定分界线处的点，属于哪个集合
            if (CommonToolClass.contains(leftPoints, minValue1)) {
                list1.add(minValue1);
            }
            if (CommonToolClass.contains(rightPoints, minValue1)) {
                list2.add(minValue1);
            }
        }
//        System.out.println("t 的位置在：" + minValue1 + "集合中离散点个数为：" + index1);
        //给分界线赋值，同时 将数组赋给对应的集合
        if (isWidth) {
            widthDividing = minValue1;
            this.leftPoints = CommonToolClass.listCopyToArray(list1);
            this.rightPoints = CommonToolClass.listCopyToArray(list2);
        } else {
            heightDividing = minValue1;
            this.bottomPoints = CommonToolClass.listCopyToArray(list1);
            this.abovePoints = CommonToolClass.listCopyToArray(list2);
        }
        return arrayList;
    }


    @Test
    public void test01() throws FileNotFoundException {

        GetFile getFile = new GetFile();
        File file = new File("E:\\EssayTestSet\\2D-SPPActual\\Test\\cplex_test\\part04");
        List<File> allFile = getFile.getAllFile(file);
        for (File value : allFile) {
            TargetData rectangularLayOutData1 = new TargetData();
            String path = value.getAbsolutePath();
            rectangularLayOutData1.initData(path);
            PointReductionMethod prm1 = new PointReductionMethod();
            prm1.getXOrYCoordinates(rectangularLayOutData1, true);
            System.out.println(Arrays.toString(prm1.getWidthPoints()));

            System.out.println("记录左侧点集 ：" + Arrays.toString(prm1.leftPoints));
            System.out.println("记录右侧点集 ：" + Arrays.toString(prm1.rightPoints));
            System.out.println("宽度方向上的分界线 ： " + prm1.widthDividing);

            prm1.getXOrYCoordinates(rectangularLayOutData1, false);
            System.out.println(Arrays.toString(prm1.getHeightPoints()));
            System.out.println("记录下侧点集 ：" + Arrays.toString(prm1.bottomPoints));
            System.out.println("记录上侧点集 ：" + Arrays.toString(prm1.abovePoints));
            System.out.println("高度方向上的分界线 ： " + prm1.heightDividing);
        }
    }

    @Test
    public void test02() throws FileNotFoundException {
        TargetData rectangularLayOutData1 = new TargetData();
        rectangularLayOutData1.initData("E:\\datatest\\test02.txt");
        PointReductionMethod prm1 = new PointReductionMethod();
        int[] pointLeft = prm1.getLeftBottom(rectangularLayOutData1);
        System.out.println(Arrays.toString(pointLeft));
    }
}
