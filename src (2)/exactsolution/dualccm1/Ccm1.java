package exactsolution.dualccm1;

import com.twodimension.TargetData;
import ilog.concert.IloException;
import com.pointset.PreprocessDef;
import com.twodimension.GetFile;

import com.universalalgorithm.QuickSortDef;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Ccm1 {
    class TempVar {
        int[] targetSize;
        int rectangleSize;

        public TempVar(int[] targetSize, int rectangleSize) {
            this.targetSize = targetSize;
            this.rectangleSize = rectangleSize;
        }
    }

    static class ClassicalFunctionFk0 {
        public static int reflectionSize(int overallLength, int kValue, int rectangleSize) {
            if (rectangleSize > overallLength || rectangleSize < 0
                    || kValue <= 0 || kValue > 0.5 * overallLength) {
                return -1;
            }

            if (rectangleSize > (overallLength - kValue)) {
                return overallLength;
            }
            if ((overallLength - kValue) >= rectangleSize && rectangleSize >= kValue) {
                return rectangleSize;
            }
            return 0;
        }
    }

    static class ClassicalFunctionFk1 {
        // 获取J
        public static int[][] getAitArrJ(int[][] targetSize, int overallLength, int kValue) {
            int[][] ints = new int[targetSize.length][4];
            for (int i = 0; i < targetSize.length; i++) {
                ints[i][0] = targetSize[i][0];
                ints[i][1] = targetSize[i][1];
                ints[i][2] = targetSize[i][2];
                ints[i][3] = 0;
            }
            // 对符合条件的宽度赋 1
            for (int i = 0; i < ints.length; i++) {
                if (ints[i][0] >= kValue && ints[i][0] <= 0.5 * overallLength) {
                    ints[i][3] = 1;
                }
            }
            return ints;
        }

        /**
         * @param aitArrJ       矩形序列
         * @param overallLength 放置矩形的条宽度
         * @return int
         * @description MC (X, J )是与J和大小X有关的一维1KP的最佳值。
         * 返回 -1 说明出错
         * @author hao
         * @date 2023/6/28 11:28
         */
        public static int placementScale(int overallLength, int[][] aitArrJ) {
            int i = 0;
            int sum = 0;
            int num = 0;
            for (int j = 0; j < aitArrJ.length; j++) {
                // [][] ：宽度、高度、数量、是否在宽度 0.5C J 之间
                if (aitArrJ[j][3] == 1) {
                    while (aitArrJ[j + i][3] == 1) {
                        // 表示当前元素的个数
                        for (int k = 0; k < aitArrJ[i + j][2]; k++) {
                            if (sum <= overallLength && (sum + aitArrJ[i + j][0]) > overallLength) {
                                return num;
                            } else {
                                ++num;
                                sum += aitArrJ[j + i][0];
                            }
                        }
                        ++i;
                        if ((i + j) >= aitArrJ.length) {
                            return num;
                        }
                    }
                    return num;
                }
            }
            //说明没有矩形符合条件
            return 0;
        }

        /**
         * @param overallLength 区域总尺寸
         * @param kValue        变量K
         * @param rectangleSize 矩形尺寸
         * @return int
         * @description 外层是一个for循环
         * @author hao
         * @date 2023/6/28 11:22
         */
        public static int reflectionSize(int overallLength, int kValue, int rectangleSize, int[][] targetSize) {
            if (targetSize == null || rectangleSize > overallLength || rectangleSize < 0
                    || kValue <= 0 || kValue > 0.5 * overallLength) {
                return -1;
            }
            // 获取 J
            int[][] aitArrJ = getAitArrJ(targetSize, overallLength, kValue);
            // 计算 Mc(C, J)
            int total0 = placementScale(overallLength, aitArrJ);
            // 计算 Mc(C − x, J)
            int total1 = placementScale(overallLength - rectangleSize, aitArrJ);
            if (rectangleSize > 0.5 * overallLength) {
                return total0 - total1;
            }
            if (0.5 * overallLength >= rectangleSize && rectangleSize >= kValue) {
                return 1;
            }
            return 0;
        }
    }

    static class ClassicalFunctionFk2 {
        public static int reflectionSize(int overallLength, int kValue, int rectangleSize) {
            if (rectangleSize > overallLength || rectangleSize < 0 || kValue <= 0 || kValue > 0.5 * overallLength) {
                return -1;
            }

            if (rectangleSize > 0.5 * overallLength) {
                int var1 = overallLength / kValue;
                int var2 = (overallLength - rectangleSize) / kValue;
                return 2 * (var1 - var2);
            }
            if (rectangleSize == 0.5 * overallLength) {
                return overallLength / kValue;
            }
            return 2 * (rectangleSize / kValue);
        }
    }

    static class ClassicalFunctionFk3 {
        public static int reflectionSize(int overallLength, int kValue, int rectangleSize) {
            if (rectangleSize > overallLength || rectangleSize < 0 || kValue <= 0 || kValue > 0.5 * overallLength) {
                return -1;
            }
            double X = (kValue + 1.0) * rectangleSize / overallLength;
            if (isInteger(X)) {
                return rectangleSize;
            }
            return (int)X * overallLength / kValue;
        }
        // 判断一个 double 值是否为整数
        public static boolean isInteger(double value) {
            // 将值转为整数，并与原值比较
            return value == (int) value;
        }
    }


    public static int getCCM1Height(int[][] rectangleSize, int[] plateSize) throws IloException {
        int w = (int) (0.5 * plateSize[0]);
        int times = 4;
        int max = 0;
        for (int k = 1; k <= w; k++) {
            for (int u = 0; u < times; u++) {
                    // 重新计算每个矩形的w
                    int tempArea = 0;
                    for (int i = 0; i < rectangleSize.length; i++) {
                        int wi = selectMethod(plateSize[0], u, k, rectangleSize[i][0], rectangleSize);
                        // 单个面积 * 个数
                        tempArea += wi * rectangleSize[i][1] * rectangleSize[i][2];
                    }
                    // 计算矩形区域的宽度
                    int w0 = selectMethod(plateSize[0], u, k, plateSize[0], rectangleSize);
                    int ceil = (int) Math.ceil((double) tempArea / (w0));
                    max = Math.max(max, ceil);
            }
        }
        return max;
    }

    /**
     * @description
     * 选择合适的对偶可行函数
     * @author  hao
     * @date    2023/6/30 14:47
     * @param overallLength
     * @param times
     * @param kValue
     * @param rectangleSize
     * @param targetSize
     * @return int
    */
    @SuppressWarnings("AlibabaSwitchStatement")
    public static int selectMethod(int overallLength, int times, int kValue, int rectangleSize, int[][] targetSize) {
        int reflectSize = 0;
        switch (times) {
            case 0:
                reflectSize = ClassicalFunctionFk0.reflectionSize(overallLength, kValue, rectangleSize);
                break;
            case 1:
                reflectSize = ClassicalFunctionFk1.reflectionSize(overallLength, kValue, rectangleSize, targetSize);
                break;
            case 2:
                reflectSize = ClassicalFunctionFk2.reflectionSize(overallLength, kValue, rectangleSize);
                break;
            case 3:
                reflectSize = ClassicalFunctionFk3.reflectionSize(overallLength, kValue, rectangleSize);
                break;
        }
        return reflectSize;
    }

    /**
     * @description
     * @author  hao
     * @date    2023/7/11 19:46
    */
    public static int[][] combineBlock(int[][] targetBlock , int[][] defBlock){
        int targetLength = targetBlock.length;
        int defLength = defBlock.length;
        int[][] ints1 = new int[targetLength + defLength][3];
        for (int i = 0; i < targetBlock.length; i++) {
            ints1[i][0] = targetBlock[i][0];
            ints1[i][1] = targetBlock[i][1];
            ints1[i][2] = targetBlock[i][2];
        }
        for (int i = 0; i < defLength; i++) {
            ints1[i + targetLength][0] = defBlock[i][0];
            ints1[i + targetLength][1] = defBlock[i][1];
            ints1[i + targetLength][2] = 1;
        }
        return ints1;
    }

    public static int  regionLowerBound(TargetData rLayout, int[][] defList) throws IloException {
        int[][] targetBlock = rLayout.targetBlockSize;
        int[][] tempArray = combineBlock(targetBlock, defList);
        QuickSortDef quickSortDef = new QuickSortDef();
        int[][] ccm1QuickSort1 = quickSortDef.getCcm1QuickSort(tempArray);
        int ccm12 = getCCM1Height(ccm1QuickSort1, rLayout.oriSize);
        // 更新下界
        return ccm12;
    }

    public static void main(String[] args) throws FileNotFoundException, IloException {
        // 测试数据
        GetFile getFile = new GetFile();
        File file = new File("I:\\1CBP测试集\\test01def");
        List<File> allFile = getFile.getAllFile(file);
        for (File value : allFile) {
            String path = value.getAbsolutePath();
            String rePath = value.getName();
            //矩形信息
            TargetData targetData = new TargetData();
            targetData.initData(path);
//            int length = targetData.targetNum + targetData.defNum;
//            int[][] ints = new int[length][3];
//            for (int i = 0; i < targetData.targetNum; i++) {
//                ints[i][0] = targetData.targetBlockSize[i][0];
//                ints[i][1] = targetData.targetBlockSize[i][1];
//                ints[i][2] = targetData.targetBlockSize[i][2];
//            }
//            for (int i = 0; i < targetData.defNum; i++) {
//                ints[i + targetData.targetNum][0] = targetData.defectiveBlocksSize[i][0];
//                ints[i + targetData.targetNum][1] = targetData.defectiveBlocksSize[i][1];
//                ints[i + targetData.targetNum][2] = 1;
//            }
//            int[][] ccm1QuickSort = QuickSortDef.getCcm1QuickSort(ints);
//
//            int ccm11 = getCCM1Height(ccm1QuickSort, targetData.oriSize);
//            System.out.println("rePath   " + rePath + "ccm11   =  " + ccm11);
//
//            int height = LowerBound.averageHeight(ccm1QuickSort, targetData.oriSize[0], null);
//            System.out.println("aveHeight =   " + height);

            System.out.println("rePath   " + rePath );
            PreprocessDef preprocessDef = new PreprocessDef();
            // PointReductionMethod
        }
    }
}
