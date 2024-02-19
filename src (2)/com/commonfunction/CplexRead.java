package com.commonfunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CplexRead {

    public static void main(String[] args) {
        // 这里的文件应该是处理过的：缺陷块已经合并到目标块中
        String folderPath = "E:\\EssayTestSet\\2D-SPPActual\\Test\\cplex_test\\part01"; // 指定文件夹路径
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                // 存储高度的数组
                List<Integer> heightValues = new ArrayList<>();
                // 存储目标块最大数量的数组
                List<Integer> numValues = new ArrayList<>();
                // 存储目标块 key 的数组:作用是避免目标块种类重复加入
                List<Integer> keyValues = new ArrayList<>();
                if (file.isFile()) {
                    // 读取文件的第二行数值
                    int secondValue = readValue(file, heightValues, numValues, keyValues);
                    if (secondValue == -1) {
                        System.out.println("读入的数据有错");
                    }
                    // 打印第四个数值的数组
                    System.out.println("File.name: " + file.getName());
                    System.out.println("heightValues: " + heightValues);
                }
            }
        }


    }

    // 根据读取文件的第二行数值，依次读取行数
    private static int readValue(File file, List<Integer> heightValues, List<Integer> numValues, List<Integer> keyValues) {
        int lineLength = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 1;

            while ((line = reader.readLine()) != null) {
                if (lineCount == 2) {
                    lineLength = Integer.parseInt(line.trim());
                    break;
                }
                lineCount++;
            }
            if (lineLength <= 0) {
                lineLength = -1;
            }
            lineCount = 0;
            while ((line = reader.readLine()) != null) {
                if (lineCount < lineLength) {
                    String[] values = line.trim().split("\\s+"); // 假设数值之间使用空格分隔
                    if (values.length >= 4) {
                        int firstValue = Integer.parseInt(values[0]);
                        int secondValue = Integer.parseInt(values[1]);
                        int fourthValue = Integer.parseInt(values[3]);
                        int findVal = (firstValue + secondValue) * (firstValue + secondValue + 1) / 2 + firstValue;
                        numValues.add(secondValue);
                        heightValues.add(fourthValue);
                        keyValues.add(findVal);
                    }
                }
                lineCount++;
            }
            int number = Integer.parseInt(line.trim());
            if (number > 0) {
                lineCount = 0;
                while ((line = reader.readLine()) != null) {
                    if (lineCount < number) {
                        String[] values = line.trim().split("\\s+"); // 假设数值之间使用空格分隔
                        if (values.length >= 4) {
                            int firstValue = Integer.parseInt(values[0]);
                            int secondValue = Integer.parseInt(values[1]);
                            int thirdValue = Integer.parseInt(values[2]);
                            int fourthValue = Integer.parseInt(values[3]);
                            int width = thirdValue - firstValue;
                            int height = fourthValue - secondValue;
                            int findVal = (width + height) * (width + height + 1) / 2 + width;
                            if (!keyValues.contains(findVal)) {

                            }
                            numValues.add(secondValue);
                            heightValues.add(fourthValue);
                        }
                    }
                    lineCount++;
                }
            }

            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineLength;
    }
}
