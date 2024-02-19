package com.commonfunction;

import com.twodimension.SkyLine;
import com.twodimension.TargetData;
import com.twodimensiondata.SkyLineResultData;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * @author zh15178381496
 * @create 2022-10 20:44
 * @说明：
 * @总结：
 */
public class Print {
    /**
     * @description  使用PrintStream
     * @author  hao
     * @date    2023/3/5 18:09
     * @return
    */
    public Print(String name) throws FileNotFoundException {
        try {
            PrintStream print = new PrintStream(name);
            System.setOut(print);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[][] sortedArray(List<List<Integer>> rectangle) {
        int[][] array = getArray(rectangle);
        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int k = i; k < array.length; k++) {
                if (array[k][6] <= array[minIndex][6]) {
                    minIndex = k;
                }
            }
            //交换i和minIndex的值
            int[] temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }
        return array;
    }
    public static int[][] getArray(List<List<Integer>> rectangle) {
        //如果集合的长度为0，说明没有元素
        if (rectangle.size() == 0) {
            return null;
        }
        int[][] copyArray = new int[rectangle.size()][rectangle.get(0).size()];
        for (int i = 0; i < copyArray.length; i++) {
            for (int j = 0; j < copyArray[0].length; j++) {
                copyArray[i][j] = rectangle.get(i).get(j);
            }
        }
        return copyArray;
    }

    public static void printResults(String outputFilePath, SkyLineResultData skyLineResultData, double time) throws FileNotFoundException {
        SkyLine skyLine = skyLineResultData.skyLine;
        TargetData targetData = skyLineResultData.targetData;
        try {
            // 创建输出流
            File outputFile = new File(outputFilePath);
            if (!outputFile.exists() && !outputFile.isDirectory()) {
                outputFile.getParentFile().mkdirs();
            }
            OutputStream outputStream = new FileOutputStream(outputFile);
            PrintWriter writer = new PrintWriter(outputStream);
            // 间隔
            writer.println();
            // 时间
            writer.println("time: " + time);
            // 输出矩形块个数
            int sum = 0;
            for (int j = 0; j < targetData.targetNum; j++) {
                sum += targetData.targetSize[j][1];
            }
            writer.println(sum);
            // 矩形板的宽度，和最终形成的高度
//            writer.println(targetData.oriSize[0] + "\t" + (skyLineResultData.currentHeight));
            writer.println(targetData.oriSize[0] + "\t" + (skyLineResultData.skyLine.skyHeight));

            if(skyLineResultData.currentHeight <= 247){
                System.out.println(222);
            }

            // 输出缺陷块数目
            writer.println(targetData.defNum);

            int[][] recInfo = sortedArray(skyLine.arrayList);
            // 位置还原
            CommonToolClass commonToolClass = new CommonToolClass();
//            targetData.oriSize[1] = skyLineResultData.currentHeight;
            commonToolClass.locationRestoration(targetData, recInfo);

            // for (int i = 0; i < recInfo.length; i++) {
            //     if(skyLine.skyHeight < recInfo[i][5]){
            //         System.out.println(999);
            //     }
            // }

            // 输出缺陷块尺寸
            for (int j = 0; j < targetData.defNum; j++) {
                writer.print(j + "\t");
                writer.print(targetData.defPoints[j][0] + "\t");
                writer.print(targetData.defPoints[j][1] + "\t");
                writer.print(targetData.defPoints[j][2] + "\t");
                writer.print(targetData.defPoints[j][3] + "\t");
                writer.println();
            }

            // 使用率
            double usingRatio = targetData.getUsingRatio(skyLine.skyHeight);
            writer.printf("%.4f\n", usingRatio);

            // 输出加入框架内目标块和它的x1,y1,x2,y2
            for (int k = 0; k < recInfo.length; k++) {
                writer.print(recInfo[k][6] + "\t");
                writer.print(recInfo[k][0] + "\t");
                writer.print(recInfo[k][1] + "\t");
                writer.print(recInfo[k][2] + "\t");
                writer.print(recInfo[k][3] + "\t");
                writer.print(recInfo[k][4] + "\t");
                writer.print(recInfo[k][5] + "\t");
                writer.println();


            }
            // 关闭流
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//     public static void printResultsCBP(String outputFilePath, double time, ModeRequiredData modeRequiredData) throws FileNotFoundException {
//         try {
//             // 创建输出流
//             File outputFile = new File(outputFilePath);
//             if (!outputFile .exists() && !outputFile .isDirectory())
//             {
//                 outputFile.getParentFile().mkdirs();
//             }
//             OutputStream outputStream = Files.newOutputStream(outputFile.toPath());
//             PrintWriter writer = new PrintWriter(outputStream);
//             // 间隔
//             writer.println();
//             //时间
//             writer.println("time: " + time);
//             //输出矩形块个数
//             writer.println(modeRequiredData.targetNumber);
//             //矩形板的宽度，和最终形成的高度
//             writer.println(modeRequiredData.oriSize[0] + "\t" + (modeRequiredData.oriSize[1]));
//             //输出缺陷块数目
//             writer.println(modeRequiredData.defNum);
//             //输出缺陷块尺寸
//             for (int j = 0; j < modeRequiredData.defNum; j++) {
//                 writer.print(j + "\t");
//                 writer.print(modeRequiredData.defectSize[j][0] + "\t");
//                 writer.print(modeRequiredData.defectSize[j][1] + "\t");
//                 writer.print(modeRequiredData.defectSize[j][2] + "\t");
//                 writer.print(modeRequiredData.defectSize[j][3] + "\t");
//                 writer.println();
//             }
//             // 使用率
// //            double usingRatio = targetData.getUsingRatio(modeRequiredData.lowerBoundHeight);
//             writer.printf("%.4f\n", 0.5);
//             int[][] recInfo = modeRequiredData.resultPoints;
//             //输出加入框架内目标块的宽度、高度 和它的 x1,y1,x2,y2
//             for (int k = 0; k < recInfo.length; k++) {
//                 writer.print(recInfo[k][0] + "\t");
//                 writer.print(recInfo[k][1] + "\t");
//                 writer.print(recInfo[k][2] + "\t");
//                 writer.print(recInfo[k][3] + "\t");
//                 writer.print(recInfo[k][4] + "\t");
//                 writer.print(recInfo[k][5] + "\t");
//                 writer.print(recInfo[k][6] + "\t");
//                 writer.println();
//             }
//             // 关闭流
//             writer.close();
//             outputStream.close();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }


}
