package processresult.resultanalyzeexact;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


// NTN NTT
// 输出： 文件名、avg_gap、best_gap
public class ResultProcessingExact {

    public double readNowLB(File fileData) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(fileData))));
//        substr = cin.nextLine().split("\\s+");
        //先读一行
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
        //读取目标块数据
        for (int i = 0; i < defNums; i++) {
            line = cin.nextLine();
            substr = line.trim().split("\\s+");
            //宽度
            defBlack[i][0] = Integer.parseInt(substr[3]) - Integer.parseInt(substr[1]);
            //高度
            defBlack[i][1] = Integer.parseInt(substr[4]) - Integer.parseInt(substr[2]);
            defArea += defBlack[i][0] * defBlack[i][1];
        }

        line = cin.nextLine(); // 利用率

        //目标块尺寸
        int[][] rectangle = new int[itemsNums][2];
        double area = 0;
        //读取目标块数据
        for (int i = 0; i < itemsNums; i++) {
            line = cin.nextLine();
            substr = line.trim().split("\\s+");
            //宽度
            rectangle[i][0] = Integer.parseInt(substr[1]);
            //高度
            rectangle[i][1] = Integer.parseInt(substr[2]);
            area += rectangle[i][0] * rectangle[i][1];
        }
        return (defArea + area) / oriSize[0];
    }

    public double readTime(File file) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Map<String, Double> stringDoubleMap = new HashMap<String, Double>();
        Scanner cin = new Scanner(new BufferedReader((new FileReader(file))));
        cin.nextLine();
        substr = cin.nextLine().split("\\s+");
        return Double.parseDouble(substr[1]);
    }

    public int[] readNum(File file) throws FileNotFoundException {
        int[] temp = new int[3];
        String line = null;
        String[] substr = null;
        Map<String, Double> stringDoubleMap = new HashMap<String, Double>();
        Scanner cin = new Scanner(new BufferedReader((new FileReader(file))));
        cin.nextLine();// 空格
        cin.nextLine();// 时间
        substr= cin.nextLine().split("\\s+");// 数量
        temp[0] = Integer.parseInt(substr[0]);
        substr = cin.nextLine().split("\\s+");
        temp[1] = Integer.parseInt(substr[0]);
        temp[2] = Integer.parseInt(substr[1]);
        return temp;
    }

    // 总表需要文件名，个数、平均时间
    // 各个分表 高度 + 时间
    public static void main(String[] args) throws FileNotFoundException {
        ResultProcessingExact resultProcessingExact = new ResultProcessingExact();
        int t = 4;
        String fileName1 = "C:\\Users\\Administrator\\Desktop\\改进后结果集\\DeleteArea_Continue-def01-Test01";
        String[] files = {fileName1};
        HashMap<String, ArrayList<Double>> mapTotal = new HashMap<>();
        HashMap<String, Double> mapTime = new HashMap<>();
        HashMap<String, int[]> mapArray = new HashMap<>();
        for (int n = 0; n < files.length; n++) {
            File file1 = new File(files[n]);
            File[] files1 = file1.listFiles();
            // 不同文件
            for (int i = 0; i < files1.length; i++) {
                File file2 = files1[i];
                File[] files2 = file2.listFiles();
                // 当前文件名下的各个结果集
                for (int j = 0; j < files2.length; j++) {
                    String name = files2[j].getParentFile().getName();

                    // 获取父文件名
                    if (j == 0) {
                        mapTotal.put(name, new ArrayList<>());
                        System.out.println(name);
                    }
                    if(files2[j].isDirectory()){
                        File[] files3 = files2[j].listFiles();
                        for (int k = 0; k < files3.length; k++) {
                            double time1 = resultProcessingExact.readTime(files3[k]);
                            int[] ints = resultProcessingExact.readNum(files3[k]);
                            String name1 = files3[k].getName();
                            mapArray.put(name1,ints);
                            mapTime.put(name1,time1);
                            mapTotal.get(name).add(time1);
                        }
                    }else{
                        // 读取时间
                        String name1 = files2[j].getName();
                        double time1 = resultProcessingExact.readTime(files2[j]);
                        int[] ints = resultProcessingExact.readNum(files2[j]);
                        mapArray.put(name1,ints);
                        mapTotal.get(name).add(time1);
                        mapTime.put(name1,time1);
                    }

                }
            }
        }
        // 计算平均时间
//        for (Map.Entry<String, ArrayList<Double>> entry : mapTotal.entrySet()) {
//            String key = entry.getKey();
//            double avgTime = 0;
//            for (Double var1 : entry.getValue()) {
//                avgTime += var1;
//            }
//            avgTime /= entry.getValue().size();
//            System.out.println(key + "  " + entry.getValue().size() +" " + avgTime);
//        }

        for (Map.Entry<String, Double> entry : mapTime.entrySet()) {
            String key = entry.getKey();
            int[] ints = mapArray.get(key);

            System.out.println(key + "  " + " " + ints[0]+ " " + ints[1]+ " " + ints[2]  +" " + entry.getValue());
        }
    }

    public int readHeight(File fileData) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(fileData))));
        //先读一行
        line = cin.nextLine(); // 空行
        line = cin.nextLine(); // 时间
        cin.nextLine(); // 物品数量
        substr = cin.nextLine().trim().split("\\s+"); // 条带尺寸
        return Integer.parseInt(substr[1]);
    }
}
