package com.commonfunction;

import com.commonfunction.Print;
import com.twodimension.GetFile;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

;

public class ReadFileW {
    /**
     * @description 原料板尺寸，只有宽度和高度
     */
    int[] oriSize;
    /**
     * @description 目标块种类数量
     */
    int targetNum;
    /**
     * @description 目标块尺寸，需要初始化
     */
    int[][] targetSize;
    /**
     * @description 缺陷块的数量
     */
    int defNum;
    /**
     * @description 缺陷块的位置信息
     */
    int[][] defPoints;

    public void readData(File file, String printPath) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(file))));
//        substr = cin.nextLine().split("\\s+");
        //先读一行
        line = cin.nextLine();
        line = line.trim();
        //以空格为标志进行拆分，得到String类型的原版料宽和高
        substr = line.split("\\s+");
        try {
            oriSize = new int[]{Integer.parseInt(substr[0]), Integer.parseInt(substr[1])};
        } catch (NumberFormatException e) {
            return;
        }
        substr = cin.nextLine().split("\\s+");
        //目标块数量
        targetNum = Integer.parseInt(substr[0]);
        //初始化数组
        //目标块尺寸
        targetSize = new int[targetNum][4];
        //读取目标块数据
        for (int i = 0; i < targetNum; i++) {
            line = cin.nextLine();
            substr = line.trim().split("\\s+");
            //宽度
            targetSize[i][0] = Integer.parseInt(substr[0]);
            //高度
            targetSize[i][1] = Integer.parseInt(substr[1]);
            //价值
            targetSize[i][2] = Integer.parseInt(substr[2]);
            //数量
            targetSize[i][3] = Integer.parseInt(substr[3]);
        }

        int sum = 0;
        for (int i = 0; i < targetSize.length; i++) {
            sum+= targetSize[i][3];
        }
        // 计算平均宽度 和 高度，使用平均尺寸创建缺陷块
        int aveWidth = 0;
        int aveHeight = 0;
        for (int i = 0; i < targetSize.length; i++) {
            aveWidth += targetSize[i][0];
            aveHeight += targetSize[i][1];
        }
        aveWidth /= sum;
        aveHeight /= sum;


        // 自动生成缺陷块尺寸()
        Random numList = new Random();
        // 个数
        int defNum = 4;
        /*
        if (targetNum <= 50) {
            defNum = 1;
        } else if (targetNum <= 100) {
            defNum = 2;
        } else {
            defNum = 3;
        }*/

      /*   if (targetNum <= 50) {
            defNum = 2;
        } else if (targetNum <= 100) {
            defNum = 3;
        } else {
            defNum = 4;
        } */

        defPoints = new int[defNum][4];
        List<List<Integer>> arrayList = new ArrayList<>();
        for (int i = 0; i < defNum; i++) {
            // 缺陷块的宽高范围
            int numWidthUpper1 = (int) (7 * aveWidth / 10);
            int numWidthLow1 = (int) (4 * aveWidth / 10);
            int numWidthUpper2 = (int) (oriSize[0] / 6);
            int numWidthLow2 = (int) (oriSize[0] / 10);

            int numWidthUpper = Math.min(numWidthUpper1, numWidthUpper2);
            int numWidthLow = Math.min(numWidthLow1, numWidthLow2);
//            if(numWidthLow > aveWidth){
//                numWidthUpper = (int)(aveWidth * 1.1);
//                numWidthLow = (int)(aveWidth * 0.8);
//            }

            int numHeightUpper1 = (int) (7 * aveHeight / 10);
            int numHeightLow1 = (int) (4 * aveHeight / 10);
            int numHeightUpper2 = (int) (oriSize[1] / 6);
            int numHeightLow2 = (int) (oriSize[1] / 10);
            int numHeightUpper = Math.min(numHeightUpper1, numHeightUpper2);
            int numHeightLow = Math.min(numHeightLow1, numHeightLow2);
//            if(numHeightLow > aveHeight){
//                numHeightUpper = (int)(aveWidth * 1.1);
//                numHeightLow = (int)(aveWidth * 0.8);
//            }
            List<Integer> list;
            do {
                int numW = (int) (Math.random() * (numWidthUpper - numWidthLow + 1)) + numWidthLow;
                int numH = (int) (Math.random() * (numHeightUpper - numHeightLow + 1)) + numHeightLow;
                int coordX = numList.nextInt(oriSize[0] - numW);
                int coordY = numList.nextInt(oriSize[1] - numH);
                defPoints[i][0] = coordX;
                defPoints[i][1] = coordY;
                if (numW == 0) {
                    numW++;
                }
                defPoints[i][2] = coordX + numW;
                if (numH == 0) {
                    numH++;
                }
                defPoints[i][3] = coordY + numH;
                list = new ArrayList<Integer>();
                list.add(defPoints[i][0]);
                list.add(defPoints[i][1]);
                list.add(defPoints[i][2]);
                list.add(defPoints[i][3]);
            } while (!judgeDefCoord(arrayList, list));
        }

        // 输出
        //输出位置
        Print name = new Print(printPath);
        System.out.println(oriSize[0] + "\t" + oriSize[1] + "\t");
        System.out.println(targetNum);
        for (int i = 0; i < targetNum; i++) {
            System.out.print(targetSize[i][0] + "\t" + targetSize[i][1] + "\t");
            System.out.println(targetSize[i][2] + "\t" + targetSize[i][3] + "\t");
        }
        System.out.println(defNum);
        for (int i = 0; i < defNum; i++) {
            System.out.print(defPoints[i][0] + "\t" + defPoints[i][1] + "\t");
            System.out.println(defPoints[i][2] + "\t" + defPoints[i][3] + "\t");
        }
//        System.out.println(0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t");
    }

    // 获取不包含后缀的文件名
    public static String removeExtension(String fname) {
        int pos = fname.lastIndexOf('.');
        if (pos > -1) {
            return fname.substring(0, pos);
        } else {
            return fname;
        }
    }

    // 判断缺陷块是否符合条件，符合条件就添加
    public boolean judgeDefCoord(List<List<Integer>> lists, List<Integer> tempList) {
        if (lists.size() == 0) {
            lists.add(tempList);
            return true;
        }
        int x11 = tempList.get(0);
        int y11 = tempList.get(1);
        int w11 = tempList.get(2) - x11;
        int h11 = tempList.get(3) - y11;
        for (List<Integer> coordList : lists) {
            int x1 = coordList.get(0);
            int y1 = coordList.get(1);
            int x2 = coordList.get(2);
            int y2 = coordList.get(3);
            if ((x1 - w11 < x11) && (x11 < x2) && (y1 - h11 < y11) && (y11 < y2)) {
                return false;
            }
        }
        // 说明符合条件
        lists.add(tempList);
        return true;
    }

    public int getHeight(int[][] area, int width) {
        int sum = 0;
        for (int temp[] : area) {
            sum += temp[2];
        }
        int heightSize = (int) ((sum / width) * 1.15);
        return heightSize;
    }

    public static void main(String[] args) throws FileNotFoundException {
        GetFile getFile = new GetFile();
        File fileVar = new File("E:\\同步文件\\BaiduSyncdisk\\小论文\\测试集\\part");
        List<File> fileList = getFile.getAllFile(fileVar);
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            if (accept(file)) {
                String rePath = file.getName();
                String name = file.getParentFile().getName();
                ReadFileW readFileZ = new ReadFileW();
                String outputFilePath = "E:\\同步文件\\BaiduSyncdisk\\小论文\\测试集\\part\\DataTestDef04" + "\\"+name + "\\" + rePath;
                // 创建文件夹（如果不存在）
                File folder = new File(outputFilePath).getParentFile();
                if (!folder.exists()) {
                    boolean created = folder.mkdirs();
                    if (!created) {
                        System.out.println("无法创建文件夹：" + folder.getAbsolutePath());
                        return;
                    }
                }
                readFileZ.readData(file, outputFilePath);
            }
        }
    }

    public static boolean accept(File dir) {
        String dirName = dir.getName();
        boolean flag = dirName.endsWith(".txt");
        return flag;
    }
}
