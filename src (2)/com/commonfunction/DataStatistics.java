package com.commonfunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataStatistics {
    /**
     * @description 统计次数
    */
    private Map<String, Integer> occurrencesMap;
    /**
     * @description 统计和
    */
    private Map<String, Double> sumMap;

    public DataStatistics() {
        occurrencesMap = new HashMap<>();
        sumMap = new HashMap<>();
    }

    public void processFolder(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        processFolder(file);
                    } else {
                        processFile(file);
                    }
                }
            }
        }
    }

    public void processFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String fileName = file.getName();
            String line;
            int lineIndex = 0;
            int statistic = 0;
            int time = 0;
            while ((line = reader.readLine()) != null) {
                lineIndex++;

                // 只处理第五行数据
                if (lineIndex == 5) {
                    statistic = Integer.parseInt(line);
                }else if(lineIndex > 5){
                    time++;
                }
                if((statistic != 0) && (statistic + 1 == time)){
                    double value = Double.parseDouble(line);
                    // 统计出现次数
                    if (occurrencesMap.containsKey(fileName)) {
                        int count = occurrencesMap.get(fileName) + 1;
                        occurrencesMap.put(fileName, count);
                    } else {
                        occurrencesMap.put(fileName, 1);
                    }

                    // 累加数据
                    if (sumMap.containsKey(fileName)) {
                        double sum = sumMap.get(fileName) + value;
                        sumMap.put(fileName, sum);
                    } else {
                        sumMap.put(fileName, value);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getAverage(String fileName) {
        if (occurrencesMap.containsKey(fileName)) {
            int count = occurrencesMap.get(fileName);
            double sum = sumMap.get(fileName);
            return sum / count;
        }
        return 0.0;
    }

    public int getOccurrences(String fileName) {
        if (occurrencesMap.containsKey(fileName)) {
            return occurrencesMap.get(fileName);
        }
        return 0;
    }

    public void printStatistics() {
        for (String fileName : occurrencesMap.keySet()) {
            double average = getAverage(fileName);
//            System.out.println("File: " + fileName + ", Average: " + average);
            System.out.println(average);
        }
    }

    public static void main(String[] args) {
        DataStatistics statistics = new DataStatistics();
        File folder = new File("E:\\EssayTestSet\\2D-SPPActual\\异步设计part02");
        statistics.processFolder(folder);

        statistics.printStatistics();
    }
}
