package processresult.resultanalyzeheri;

import com.pointset.ToolClass;
import com.twodimension.GetFile;
import com.twodimension.TargetData;
import exactsolution.dualccm1.Ccm1;
import exactsolution.onedimensionalcontiguous.ModeRequiredData;
import exactsolution.pointprocessing.DefPointData;
import ilog.concert.IloException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

// 输出： 文件名、avg_gap、best_gap
public class ResultProcessingAH {

    double[] LB;
    double height;
    double avgHeight;
    double bestHeight;
    int[][] rectangle;
    int[][] defBlack;
    int[] oriSize;

    public double readNowLB(File fileData) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(fileData))));
//        substr = cin.nextLine().split("\\s+");
        // 先读一行
        line = cin.nextLine(); // 空行
        line = cin.nextLine(); // 时间
        substr = cin.nextLine().split("\\s+"); // 物品数量
        int itemsNums = Integer.parseInt(substr[0]);

        substr = cin.nextLine().trim().split("\\s+"); // 条带尺寸
        int[] oriSize = new int[]{Integer.parseInt(substr[0]), Integer.parseInt(substr[1])};
        substr = cin.nextLine().split("\\s+"); // 缺陷块数量
        int defNums = Integer.parseInt(substr[0]);
        // 缺陷块尺寸
        int[][] defBlack = new int[defNums][4];
        double defArea = 0;
        // 读取目标块数据
        for (int i = 0; i < defNums; i++) {
            line = cin.nextLine();
            substr = line.trim().split("\\s+");
            // 宽度
            defBlack[i][0] = Integer.parseInt(substr[3]) - Integer.parseInt(substr[1]);
            // 高度
            defBlack[i][1] = Integer.parseInt(substr[4]) - Integer.parseInt(substr[2]);
            defArea += defBlack[i][0] * defBlack[i][1];
        }

        line = cin.nextLine(); // 利用率

        // 目标块尺寸
        int[][] rectangle = new int[itemsNums][2];
        double area = 0;
        // 读取目标块数据
        for (int i = 0; i < itemsNums; i++) {
            line = cin.nextLine();
            substr = line.trim().split("\\s+");
            // 宽度
            rectangle[i][0] = Integer.parseInt(substr[1]);
            // 高度
            rectangle[i][1] = Integer.parseInt(substr[2]);
            area += rectangle[i][0] * rectangle[i][1];
        }
//        System.out.println("defArea" + defArea + "     " + "area" + area);
       return (area) / oriSize[0];
        // return (defArea + area) / oriSize[0];
    }

    public static void main(String[] args) throws FileNotFoundException {
        ResultProcessingAH resultProcessingAH = new ResultProcessingAH();
        GetFile getFile = new GetFile();
        for (int m = 1; m < 2; m++) {
            // String str1 = "E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\启发式结果集\\ResultData0" + m + "\\AH";
            String str1 = "E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\改进放置位置后的启发式结果集\\ResultData0" + m + "\\AH";
            System.out.println("--------");
            File fileVar = new File(str1);
            File[] files = fileVar.listFiles();
            int fileLength = 10;
            double[][] res = new double[12][fileLength];
            double[][] defLB = new double[12][fileLength];
            int index = 0;
            // 次数
            for (int i = 0; i < files.length; i++) {
                File[] files1 = files[i].listFiles();

                // 按照文件名中的数字部分进行排序
                Arrays.sort(files1, Comparator.comparingInt(file -> Integer.parseInt(file.getName().replaceAll("[^0-9]", ""))));

                // for (File file : files1) {
                //     System.out.println(file.getName());
                // }
                if (files1.length != 360) {
                    continue;
                }

                if (index >= fileLength) {
                    break;
                }
                // 每30个一组，计算高度
                int heightTotal = 0;
                int k = 0;
                for (int j = 1; j <= files1.length; j++) {
                    // 读取高度
                    heightTotal += resultProcessingAH.readHeight(files1[j - 1]);
                    if (j % 30 == 0) {
                        heightTotal /= 30;
                        res[k++][index] = heightTotal;
                        heightTotal = 0;
                    }
                }

                // 30个一组，计算下界
                double nowLB = 0;
                double tempLB1 = 0;

                k = 0;
                if (index == 0) {
                    for (int j = 1; j <= files1.length; j++) {
                        // 读取高度
                        double tempLB = resultProcessingAH.readNowLB(files1[j - 1]);
                        tempLB1 += tempLB;
                        // 下界计算法
                        // double nowLB1 = resultProcessingAH.lbo(files1[j - 1].getAbsolutePath());
                        // tempLB = Math.max(nowLB1, tempLB);
                        nowLB += tempLB;
                        if (j % 30 == 0) {
                            nowLB /= 30;
                            defLB[k++][index] = nowLB;
                            tempLB1 /= 30;
                            // System.out.println(tempLB1);
                            tempLB1 = 0;
                            nowLB = 0;
                        }
                    }
                }
                ++index;
            }
//        System.out.println(111);
            // 计算avg、best
            for (int i = 0; i < res.length; i++) {
                resultProcessingAH.sortArray(res[i]);
                double bestHeight = res[i][0];
                double avgHeight = 0;
                for (int j = 0; j < res[i].length; j++) {
                    avgHeight += res[i][j];
                }
                avgHeight /= res[i].length;
                // 计算LB
                double LB = defLB[i][0];

                double avgGap = (avgHeight - LB) / LB;
                double bestGap = (bestHeight - LB) / LB;
                System.out.println("AH" + (i + 1) + "," + LB + "," + avgGap + "," + bestGap);
            }
        }
    }

    public double[] sortArray(double[] temp) {
        for (int i = 0; i < temp.length; i++) {
            int min = i;
            for (int j = i; j < temp.length; j++) {
                if (temp[min] > temp[j]) {
                    min = j;
                }
            }
            double a = temp[min];
            temp[min] = temp[i];
            temp[i] = a;
        }
        return temp;
    }

    public int readHeight(File fileData) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(fileData))));
//        substr = cin.nextLine().split("\\s+");
        // 先读一行
        line = cin.nextLine(); // 空行
        line = cin.nextLine(); // 时间
        cin.nextLine(); // 物品数量
        substr = cin.nextLine().trim().split("\\s+"); // 条带尺寸
        return Integer.parseInt(substr[1]);
    }

    public double readArea(File fileData) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(fileData))));
//        substr = cin.nextLine().split("\\s+");
        // 先读一行
        substr = cin.nextLine().trim().split("\\s+"); // 条带尺寸
        int[] oriSize = new int[]{Integer.parseInt(substr[0]), Integer.parseInt(substr[1])};
        substr = cin.nextLine().split("\\s+"); // 物品数量
        int itemsNums = Integer.parseInt(substr[0]);
        // 目标块尺寸
        int[][] rectangle = new int[itemsNums][4];
        double area = 0;
        // 读取目标块数据
        for (int i = 0; i < itemsNums; i++) {
            line = cin.nextLine();
            substr = line.trim().split("\\s+");

            rectangle[i][3] = Integer.parseInt(substr[2]);
            area += rectangle[i][3];
        }
        return (area) / oriSize[0];
    }

    public double lbo(String path) throws FileNotFoundException {
        TargetData rLayout = new TargetData();
        rLayout.initDataProcessing(path);
        int[][] blockSize = ModeRequiredData.initBlocks(rLayout);
        int[][] copyBlocks = ToolClass.copyTwoDim(blockSize);
        // 计算下界
        int ccm1 = 0;
        try {
            ccm1 = Ccm1.regionLowerBound(rLayout, rLayout.defectiveBlocksSize);
        } catch (IloException e) {
            throw new RuntimeException(e);
        }
        // 缺陷块的最大高度
        int maxDefHeight = DefPointData.maxSize(rLayout.defPoints, 3);
        // 目标块的最大高度
        int maxBlocksHeight = DefPointData.maxSize(copyBlocks, 1);
        // 确定最低下界
        double l = maxBlocksHeight > maxDefHeight ? Math.max(ccm1, maxBlocksHeight) : Math.max(ccm1, maxDefHeight);
        return l;
    }
}
