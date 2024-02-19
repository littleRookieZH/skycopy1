package exactsolution.onedimensionalcontiguous;// package exactsolution.onedimensionalcontiguous;
//
// import com.commonfunction.Print;
// import com.twodimension.GetFile;
// import com.twodimension.TargetData;
// import ilog.cplex.CpxException;
//
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.text.SimpleDateFormat;
// import java.util.Date;
// import java.util.List;
//
// public class Main {
//     public static void main(String[] args) throws Exception {
//         GetFile getFile = new GetFile();
//         File file = new File("C:\\Users\\浩\\Desktop\\test\\DataTestAll01");
//         List<File> allFile = getFile.getAllFile(file);
//         for (int i = 0; i < allFile.size(); i++) {
//             File value = allFile.get(i);
//             String path = value.getAbsolutePath();
//             String rePath = value.getName();
//             String parentName = value.getParentFile().getName();
//             System.out.println("value.getName() " + rePath);
//             double startTime = System.currentTimeMillis() / 1000.0;
//             Date date = new Date();
//             SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//             System.out.println("开始时间： " + formatter.format(date));
//             TargetData rLayout = new TargetData();
//             ModeRequiredData modeRequiredData = new ModeRequiredData();
//             // 初始化模型
//             rLayout.initData(path);
//             System.out.println(rLayout.targetNum);
//             if(rLayout.targetNum > 40 ){
//                 continue;
//             }
//             modeRequiredData = modeRequiredData.initModel1(path, rLayout);
//             MasterModel masterModel = new MasterModel();
// //            InterruptMethodExecution methodExecution = new InterruptMethodExecution();
// //            boolean isSolveModel = methodExecution.timeLimit(200, masterModel, modeRequiredData, rLayout);
// //                 求解模型
//
//             long modelStartTime =  System.currentTimeMillis();
//             boolean isSolveModel = false;
//             try {
//                 isSolveModel = masterModel.solveModel(modeRequiredData, rLayout, modelStartTime, ModeRequiredData.Solution_Times);
//             } catch (CpxException e) {
//                 System.out.println("模型太大");
//             } catch (FileNotFoundException e) {
//                 throw new RuntimeException(e);
//             } catch (Exception e) {
//                 throw new RuntimeException(e);
//             }
//             if (!isSolveModel) {
//                 System.out.println(" 整个求解超过时间限制 ");
//                 continue;
//             }
//             double endtime = System.currentTimeMillis() / 1000.0;
//             double time = endtime - startTime;
//             System.out.println("time  " + time);
// //            String outputFilePath = "C:\\Users\\浩\\Desktop\\ResultData 02\\exact\\timenedless02\\" + parentName  + "\\" + rePath + ".pack"; // 指定输出文件的路径
//             String outputFilePath = "C:\\Users\\浩\\Desktop\\ResultData 02\\exact\\DeleteArea_continue01" + "\\" + rePath + ".pack"; // 指定输出文件的路径
//             Print.printResultsCBP(outputFilePath, time, modeRequiredData);
//         }
//
//     }
// }
