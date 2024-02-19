package com.commonfunction;

import com.twodimension.GetFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ReadFileZ {
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

    public void readData(String path) throws FileNotFoundException {
        File file = new File(path);
        String fileName = file.getName();
        fileName = removeExtension(fileName);
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(path))));
        substr = cin.nextLine().split("\\s+");
        //目标块数量
        targetNum = Integer.parseInt(substr[0]);
        //先读一行
        line = cin.nextLine();
        line = line.trim();
        //以空格为标志进行拆分，得到String类型的原版料宽和高
        substr = line.split("\\s+");
        oriSize = new int[]{Integer.parseInt(substr[0]), Integer.parseInt(substr[1])};
        //初始化数组
        //目标块尺寸
        targetSize = new int[targetNum][4];
        //读取目标块数据
        for (int i = 0; i < targetNum; i++) {
            line = cin.nextLine();
            substr = line.trim().split("\\s+");
            //宽度
            targetSize[i][0] = Integer.parseInt(substr[1]);
            //高度
            targetSize[i][1] = Integer.parseInt(substr[2]);
            //价值
            targetSize[i][2] = targetSize[i][0] * targetSize[i][1];
            //数量
            targetSize[i][3] = Integer.parseInt(substr[4]);
        }
        if (oriSize[1] <= 0) {
            oriSize[1] = getHeight(targetSize, oriSize[0]);
        }
        //自动生成缺陷块尺寸()
        Random numList = new Random();
        // 个数
        int defNum = (int) (targetNum * 0.02) + 1;
        defPoints = new int[defNum][4];
        List<List<Integer>> arrayList = new ArrayList<>();
        for (int i = 0; i < defNum; i++) {
            // 缺陷块的宽高范围
            int numWidthUpper = (int) (oriSize[0] * 0.15);
            int numWidthLow = (int) (oriSize[0] * 0.08);
            int numHeightUpper = (int) (oriSize[1] * 0.12);
            int numHeightLow = (int) (oriSize[0] * 0.04);
            List<Integer> list;
            do {
                int numW = (int) (Math.random() * (numWidthUpper - numWidthLow) + numWidthLow + 1);
                int numH = (int) (Math.random() * (numHeightUpper - numHeightLow) + numHeightLow + 1);
                int coordX = numList.nextInt(oriSize[0] - numW);
                int coordY = numList.nextInt(oriSize[1] - numH);
                defPoints[i][0] = coordX;
                defPoints[i][1] = coordY;
                defPoints[i][2] = coordX + numW;
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
        Print name = new Print("E:\\EssayTestSet\\2D-SPPActual\\ZDF" + "\\" + fileName + ".txt");
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
            System.out.println(222);
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
                System.out.println(11111);
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
        File file = new File("E:\\EssayTestSet\\2D-SPPOri\\ZDF\\ZDF_text");
        List<File> allFile = getFile.getAllFile(file);
        for (File value : allFile) {
            String absolutePath = value.getAbsolutePath();
            ReadFileZ readFileZ = new ReadFileZ();
            readFileZ.readData(absolutePath);
        }
    }
}
