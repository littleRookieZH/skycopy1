package processresult.resultanalyzeheri;

import com.pointset.ToolClass;
import com.twodimension.GetFile;
import com.twodimension.TargetData;
import exactsolution.dualccm1.Ccm1;
import exactsolution.onedimensionalcontiguous.ModeRequiredData;
import exactsolution.pointprocessing.DefPointData;
import ilog.concert.IloException;

import java.io.*;
import java.util.*;

// 输出： 文件名、avg_gap、best_gap
public class ResultProcessingC0X {

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
        return (area) / oriSize[0];
//        return (defArea + area) / oriSize[0];
    }

    public Map<String, Double> readLB(File file) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Map<String, Double> stringDoubleMap = new HashMap<String, Double>();
        Scanner cin = new Scanner(new BufferedReader((new FileReader(file))));
//        substr = cin.nextLine().split("\\s+");
        while (true) {
            substr = cin.nextLine().split("\\s+");
            if (substr.length == 1) {
                break;
            }
            String name = null;
            name = substr[0] + "_" + substr[1];

            double tempLB = Double.parseDouble(substr[3]);
            stringDoubleMap.put(name, tempLB);
        }
        return stringDoubleMap;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ResultProcessingC0X resultProcessingC0X = new ResultProcessingC0X();
        GetFile getFile = new GetFile();
        File fileVar = new File("E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\改进放置位置后的启发式结果集\\ResultData04\\COX");
        // File fileVar = new File("E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\启发式结果集\\ResultData04\\COX");
        File fileLB = new File("E:\\同步文件\\BaiduSyncdisk\\小论文\\处理结果集\\数据处理\\C0X.txt");

        // 读取LB
        Map<String, Double> stringDoubleMap = resultProcessingC0X.readLB(fileLB);
        File[] files = fileVar.listFiles();
        HashMap<String, ArrayList<Double>> map = new HashMap<>();
        HashMap<String, Double> mapDef = new HashMap<>();

        int index = 0;
        // 输出最终结果
        for (int i = 0; i < files.length; i++) {
            File[] files1 = files[i].listFiles();
            String name = files[i].getName();
            // 读取测试的次数
            // 保存对应次数的值
            double[] heightTotal = new double[files1.length];
            for (int j = 0; j < files1.length; j++) {
                File[] files2 = files1[j].listFiles();

                // 处理一组数据，取平均值作为当前次数的值
                double height1 = 0;
                if (files2.length < 10) {
                    continue;
                }

                if(index == 0){


                }
                for (int k = 0; k < files2.length; k++) {
                    // 读取高度
                    height1 += resultProcessingC0X.readHeight(files2[k]);
                }
                heightTotal[j] = height1 / files2.length;
            }
            // 记录最优值和平均值
            // 排序，取前10次
            resultProcessingC0X.sortArray(heightTotal);

            double avgHeight = 0;
            int n = -1;
            int times = 10;
            while (heightTotal[++n] == 0) {
            }
            for (int j = n; j < times + n; j++) {
                avgHeight += heightTotal[j];
            }
            avgHeight /= times;
            double bestHeight = heightTotal[n];
            // 获取文件名对应的LB
            Double lbDouble = stringDoubleMap.get(name);
            // 获取当前求和后的LB
            double nowLB = 0;
            double tempLB = 0;
            File[] file1 = files1[0].listFiles();
            for (int j = 0; j < file1.length; j++) {
                File file = file1[j];
                tempLB = resultProcessingC0X.readNowLB(file);
                // 下界计算法
                double nowLB1 = resultProcessingC0X.lbo(file.getAbsolutePath());
                nowLB += Math.max(tempLB, nowLB1);
            }
            nowLB /= file1.length;


            if (lbDouble < nowLB) {
                // System.out.println(22);
            }

            lbDouble = Math.max(lbDouble, nowLB);
            double avgGap = (avgHeight - lbDouble) / lbDouble;
            if (bestHeight < lbDouble) {
                // System.out.println(88);
            }
            double bestGap = (bestHeight - lbDouble) / lbDouble;
            System.out.println(name + "," + lbDouble + "," + avgGap + "," + bestGap);
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
