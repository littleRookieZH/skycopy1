package com.processingresult;

/**
 * @author xzbz
 * @create 2023-08-30 0:52
 */

import com.twodimension.GetFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrintfResultAOA {
    List<Integer> heightList = new ArrayList<>();
    List<Double> rateList = new ArrayList<>();
    public void readData(String path) throws FileNotFoundException {
        File file = new File(path);
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(path))));
        cin.nextLine();
        cin.nextLine();
        cin.nextLine();
        substr = cin.nextLine().split("\\s+");
        // 高度
        int height = Integer.parseInt(substr[1]);
        heightList.add(height);
        //先读一行
        int nums = cin.nextInt();
        while(nums >= 0){
            substr = cin.nextLine().split("\\s+");
            --nums;
        }
        double rate = cin.nextDouble();
        rateList.add(rate);
//        System.out.println(height + "\t\t" + rate);
    }

    public static void main(String[] args) throws FileNotFoundException {
        GetFile getFile = new GetFile();
//        File file = new File("C:\\Users\\浩\\Desktop\\ResultData02\\skyLine\\ORI");
        File file = new File("C:\\Users\\浩\\Desktop\\ResultData02\\skyLine\\ORI16");
        List<File> allFile = getFile.getAllFile(file);
        PrintfResultAOA readRes = new PrintfResultAOA();
        for (File value : allFile) {
            String absolutePath = value.getAbsolutePath();
            readRes.readData(absolutePath);
        }

        double mediateHeight = 0;
        double sumHeight = 0;
        double minHeight = readRes.heightList.get(0);
        double maxHeight = readRes.heightList.get(0);
        for (int i = 0; i < readRes.heightList.size(); i++) {
            minHeight = Math.min(readRes.heightList.get(i),minHeight);
            maxHeight = Math.max(readRes.heightList.get(i), maxHeight);
            sumHeight += readRes.heightList.get(i);
        }
        mediateHeight = sumHeight / readRes.heightList.size();
        System.out.println("minHeight = " + minHeight);
        System.out.println("maxHeight = " + maxHeight);
        System.out.println("mediateHeight = " + mediateHeight);
    }
}
