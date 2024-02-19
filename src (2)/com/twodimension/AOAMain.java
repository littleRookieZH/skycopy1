package com.twodimension;// package com.twodimension;
//
// import com.commonfunction.Print;
//
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.lang.reflect.InvocationTargetException;
// import java.util.List;
//
// /**
//  * @author xzbz
//  * @create 2023-08-29 21:02
//  */
// public class AOAMain {
//     public static void main(String[] args) throws FileNotFoundException, InvocationTargetException, IllegalAccessException {
//         GetFile getFile = new GetFile();
//         File file = new File("C:\\Users\\浩\\Desktop\\test\\AOANoDef");
//         for (int i = 0; i <= 10; i++) {
//             List<File> allFile = getFile.getAllFile(file);
//             for (File value : allFile) {
//                 String path = value.getAbsolutePath();
//                 String rePath = value.getName();
//                 String parentName = value.getParentFile().getName();
//                 double startTime = System.currentTimeMillis() / 1000.0;
//                 //矩形信息
//                 TargetData targetData = new TargetData();
//                 // 天际线
//                 HybridAlgorithmAOA hybridAlgorithmAOA = new HybridAlgorithmAOA();
//                 SkyLine skyLine = hybridAlgorithmAOA.algorithmAOA(targetData, path);
//                 double endtime = System.currentTimeMillis() / 1000.0;
//                 double time = endtime - startTime;
//                 System.out.println(time);
// //                System.out.println("总的覆盖缺陷块的次数：" + SkyLine.totalDef);
// //                System.out.println("总的次数：" + SkyLine.totalTime);
//                 // 输出
// //                String outputFilePath = "C:\\Users\\浩\\Desktop\\ResultData 02\\skyLine\\AOA_Res0\\" + parentName + "\\" + rePath + ".pack"; // 指定输出文件的路径
//                 String outputFilePath = "C:\\Users\\浩\\Desktop\\ResultData02\\aoaparameter\\aoa9\\Test0" + i + "\\" + rePath + ".pack"; // 指定输出文件的路径
//                 Print.printResults(outputFilePath, targetData, time, skyLine);
//             }
//         }
//     }
// }
