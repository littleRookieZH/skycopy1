package com.commonfunction;

import com.twodimension.GetFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrintUsing {
    static List<Double> using = new ArrayList<Double>();
    public static void main(String[] args) throws IOException {
        GetFile getFile = new GetFile();
        File file = new File("E:\\EssayTestSet\\2D-SPPActual\\ResultW\\NT_T");
        List<File> allFile = getFile.getAllFile(file);
        for (File value : allFile) {
            String path = value.getAbsolutePath();
            initData(path);
        }
        String outputFilePath = "E:\\EssayTestSet\\2D-SPPActual\\ResultW" + "\\" + "result1" + ".txt";
        File outputFile = new File(outputFilePath);
        OutputStream outputStream = new FileOutputStream(outputFile);
        PrintWriter writer = new PrintWriter(outputStream);
        for (Double integer : using) {
            writer.println(integer);
        }
        writer.close();
        outputStream.close();
    }

    public static void initData(String path) throws FileNotFoundException {
        String line = null;
        String[] substr = null;
        Scanner cin = new Scanner(new BufferedReader((new FileReader(path))));
        //先读一行
        cin.nextLine();
        cin.nextLine();
        cin.nextLine();
        cin.nextLine();
        cin.nextLine();
        substr = cin.nextLine().trim().split("\\s+");
        double usage = Double.parseDouble(substr[0]);
        using.add(usage);
    }
}
