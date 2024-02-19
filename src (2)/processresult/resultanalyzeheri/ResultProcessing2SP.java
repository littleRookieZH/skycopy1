package processresult.resultanalyzeheri;

import com.pointset.ToolClass;
import com.twodimension.TargetData;
import exactsolution.dualccm1.Ccm1;
import exactsolution.onedimensionalcontiguous.ModeRequiredData;
import exactsolution.pointprocessing.DefPointData;
import ilog.concert.IloException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


// NTN NTT
// 输出： 文件名、avg_gap、best_gap
public class ResultProcessing2SP {

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
        return (defArea + area) / oriSize[0];
    }

    public Map<String, Double> readLB(File file) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Map<String, Double> stringDoubleMap = new HashMap<String, Double>();
        Scanner cin = new Scanner(new BufferedReader((new FileReader(file))));
//        substr = cin.nextLine().split("\\s+");
        while (true) {
            substr = cin.nextLine().split("\\s+");
            if (substr.length == 1 || substr.length == 0) {
                break;
            }
            String name = null;
            name = substr[0].toLowerCase();
//            System.out.println("nameLB   " + name);
            double tempLB = Double.parseDouble(substr[1]);
            stringDoubleMap.put(name, tempLB);
        }
        return stringDoubleMap;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ResultProcessing2SP resultProcessingN = new ResultProcessing2SP();

        for (int m = 1; m < 5; m++) {
            System.out.println("-----------");
            String fileName1 = "E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\改进放置位置后的启发式结果集\\ResultData0" + m + "\\cgcut";
            String fileName2 = "E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\改进放置位置后的启发式结果集\\ResultData0" + m + "\\gcut";
            String fileName3 = "E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\改进放置位置后的启发式结果集\\ResultData0" + m + "\\ngcut";
            String fileName4 = "E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\改进放置位置后的启发式结果集\\ResultData0" + m + "\\beng";

            // String fileName1 = "E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\启发式结果集\\ResultData0" + m + "\\cgcut";
            // String fileName2 = "E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\启发式结果集\\ResultData0" + m + "\\gcut";
            // String fileName3 = "E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\启发式结果集\\ResultData0" + m + "\\ngcut";
            // String fileName4 = "E:\\同步文件\\BaiduSyncdisk\\小论文\\结果集\\启发式算法集\\启发式结果集\\ResultData0" + m + "\\beng";
            String[] files = {fileName1, fileName2, fileName3, fileName4};
            File fileLB = new File("E:\\同步文件\\BaiduSyncdisk\\小论文\\处理结果集\\数据处理\\2SP.txt");
            int[] group = {3, 13, 12, 10};
            for (int n = 0; n < files.length; n++) {
                // 读取LB
                Map<String, Double> stringDoubleMap = resultProcessingN.readLB(fileLB);
                File fileVar = new File(files[n]);
                File[] file1 = fileVar.listFiles();
                HashMap<String, ArrayList<Double>> map = new HashMap<>();
                HashMap<String, Double> mapDef = new HashMap<>();
                int index = 0;
                // 次数
                for (int i = 0; i < file1.length; i++) {
                    File[] files1 = file1[i].listFiles();
                    // 组数 12
                    if (files1.length != group[n]) {
                        continue;
                    }
                    // 输出文件名
                    if (index == 0) {
                        for (int j = 1; j <= files1.length; j++) {
                            String[] split = files1[j - 1].getName().split("\\.");
                            map.put(split[0].toLowerCase(), new ArrayList<Double>());
                            // 1个一组，计算下界
                            // 读取高度
                            double nowLB1 = resultProcessingN.readNowLB(files1[j - 1]);
                            double nowLB = resultProcessingN.lbo(files1[j - 1].getAbsolutePath());
                            nowLB = Math.max(nowLB1, nowLB);
                            mapDef.put(split[0].toLowerCase(), nowLB);
                        }
                    }
                    // 每1个一组，计算高度
                    for (int j = 1; j <= files1.length; j++) {
                        // 读取高度
                        double heightTotal = resultProcessingN.readHeight(files1[j - 1]);
                        String name = files1[j - 1].getName().split("\\.")[0];
                        ArrayList<Double> list = map.get(name.toLowerCase());
                        list.add(heightTotal);
                    }
                    ++index;
                }

                // 计算avg、best
                for (Map.Entry<String, ArrayList<Double>> entry : map.entrySet()) {
                    String key = entry.getKey();

                    double avgHeight = 0;
                    ArrayList<Double> value = entry.getValue();
                    Collections.sort(value);
                    double bestHeight = value.get(0);
                    for (double var : value) {
                        avgHeight += var;
                        if (bestHeight > var) {
                            bestHeight = var;
                        }
                    }
                    avgHeight /= value.size();
                    Double nowLB = mapDef.get(key);
                    Double LB = stringDoubleMap.get(key);
                    LB = Math.max(nowLB, LB);

                    double avgGap = (avgHeight - LB) / LB;
                    double bestGap = (bestHeight - LB) / LB;

                    System.out.println(key + "," + LB + "," + avgGap + "," + bestGap);
                }
            }
        }
    }

    public int readHeight(File fileData) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(fileData))));
        // 先读一行
        line = cin.nextLine(); // 空行
        line = cin.nextLine(); // 时间
        cin.nextLine(); // 物品数量
        substr = cin.nextLine().trim().split("\\s+"); // 条带尺寸
        return Integer.parseInt(substr[1]);
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
