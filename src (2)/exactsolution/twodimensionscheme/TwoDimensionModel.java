package exactsolution.twodimensionscheme;// package exactsolution.twodimensionscheme;
//
// import com.commonfunction.Print;
// import com.twodimension.GetFile;
// import com.twodimension.TargetData;
// import exactsolution.onedimensionalcontiguous.ModeRequiredData;
// import ilog.concert.IloException;
// import ilog.concert.IloNumExpr;
// import ilog.concert.IloNumVar;
// import ilog.concert.IloNumVarType;
// import ilog.cplex.CpxException;
// import ilog.cplex.IloCplex;
//
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.text.SimpleDateFormat;
// import java.util.Date;
// import java.util.List;
//
// public class TwoDimensionModel {
//     IloCplex model;
//     // R[i][x][y] 表示矩形是否放在离散点(x,y)处
//     IloNumVar[][][] R;
//
//     public boolean masterModel(ModeRequiredData modeRequiredData) throws IloException {
//         //获取原放置区域的宽度
//         int Width = modeRequiredData.oriSize[0];
//         // 获取下界
//         int Height = modeRequiredData.oriSize[1];
//         System.out.println("height  =  " + Height);
//         //获得每种目标块的尺寸信息, w h
//         int[][] blockSize = modeRequiredData.targetBlockSize;
//         // 高度可放置点
//         boolean[][] heightPlacedPoints = modeRequiredData.heightPlacedPoints;
//         boolean[][] widthPlacedPoints = modeRequiredData.widthPlacedPoints;
//         // 高度离散点集
//         int[] heightPoints = modeRequiredData.heightPoints;
//         // 宽度离散点
//         int[] widthPoints = modeRequiredData.widthPoints;
//         // 缺陷块位置 x1,y1,x2,y2
//         int[][] defectSize = modeRequiredData.defectSize;
//
//         // 建立模型
//         model = new IloCplex();
//         // 添加求解模型的时间限制
//         model.setParam(IloCplex.DoubleParam.TiLim, ModeRequiredData.Two_Dimension_Scheme);
//         model.setOut(null);
//
//         // 创建模型变量
//         R = new IloNumVar[blockSize.length][Width + 1][Height + 1];
//         for (int i = 0; i < blockSize.length; i++) {
//             for (int j = 0; j < widthPoints.length; j++) {
//                 for (int k = 0; k < heightPoints.length; k++) {
//                     int xPoint = widthPoints[j];
//                     int yPoint = heightPoints[k];
//                     try {
//                         if (heightPlacedPoints[i][yPoint] && widthPlacedPoints[i][xPoint]) {
//                             //将R[k][i]为0，1变量
//                             R[i][xPoint][yPoint] = model.numVar(0, 1, IloNumVarType.Bool, "R[" + i + "][" + xPoint + "][" + yPoint + "]");
//                         }
//                     } catch (IloException e) {
//                         throw new RuntimeException(e);
//                     }
//                 }
//             }
//         }
//
//         // 所有目标块都要放置
//         for (int i = 0; i < blockSize.length; i++) {
//             IloNumExpr iloNumExpr = model.numExpr();
//             for (int j = 0; j < widthPoints.length; j++) {
//                 int xPoint = widthPoints[j];
//                 for (int k = 0; k < heightPoints.length; k++) {
//                     int yPoint = heightPoints[k];
//                     if(widthPlacedPoints[i][xPoint] && heightPlacedPoints[i][yPoint]){
//                         iloNumExpr = model.sum(iloNumExpr, R[i][xPoint][yPoint]);
//                     }
//                 }
//             }
//             model.addEq(iloNumExpr, 1);
//         }
//
//         // 添加约束：两个目标块不能相互覆盖
//         //先取离散点，对每一个离散点
//         //获得p
//         for (int i = 0; i < widthPoints.length; i++) {
//             int xPoint = widthPoints[i];
//             //获得q
//             for (int j = 0; j < heightPoints.length; j++) //坐标
//             {
//                 int yPoint = heightPoints[j];
//                 IloNumExpr expr1 = model.numExpr();
//                 for (int k = 0; k < blockSize.length; k++) {
//                     //宽度下限，第k个目标块
//                     int minWidth = xPoint - blockSize[k][0] + 1;
//                     if (minWidth < 0) {
//                         minWidth = 0;
//                     }
//                     //高度下限，第k个目标块
//                     int minHeight = yPoint - blockSize[k][1] + 1;
//                     if (minHeight < 0) {
//                         minHeight = 0;
//                     }
//                     //X求和，Y求和
//                     for (int x = minWidth; x <= xPoint; x++) {
//                         //符合条件就添加到数组中
//                         if (widthPlacedPoints[k][x]) {
//                             for (int y = minHeight; y <= yPoint; y++) {
//                                 if (heightPlacedPoints[k][y]) {
//                                     expr1 = model.sum(expr1, R[k][x][y]);
//                                 }
//                             }
//                         }
//                     }
//                 }
//                 model.addLe(expr1, 1);
//             }
//         }
//
//         // 约束二：不能与缺陷块重叠
//         for (int i = 0; i < defectSize.length; i++) {
//             // 创建一个表达式
//             IloNumExpr expr2 = model.numExpr();
//             // 宽度上限 xd2
//             int maxWidth = defectSize[i][2];
//             // 高度上限
//             int maxHeight = defectSize[i][3];
//             // 对每一个目标块
//             for (int j = 0; j < blockSize.length; j++) {
//                 // 宽度下限  xd - wj + 1
//                 int minWidth = defectSize[i][0] - blockSize[j][0] + 1;
//                 // 控制宽度下限
//                 if (minWidth < 0) {
//                     minWidth = 0;
//                 }
//
//                 // 高度下限
//                 int minHeight = defectSize[i][1] - blockSize[j][1] + 1;
//                 // 控制高度下限
//                 if (minHeight < 0) {
//                     minHeight = 0;
//                 }
//
//                 // 对于每一个缺陷块而言，每一个目标块x的范围
//                 for (int x = minWidth; x < maxWidth; x++) {
//                     if (widthPlacedPoints[j][x]) {
//                         for (int y = minHeight; y < maxHeight; y++) {
//                             if (heightPlacedPoints[j][y]) {
//                                 expr2 = model.sum(expr2, R[j][x][y]);
//                             }
//                         }
//                     }
//                 }
//             }
//             model.addEq(expr2, 0);
//         }
//
//         modeRequiredData.resultPoints = new int[blockSize.length][7];
//         try {
//             if (model.solve()) {
//                 for (int i = 0; i < blockSize.length; i++) {
//                     for (int j = 0; j < widthPoints.length; j++) {
//                         int xPoint = widthPoints[j];
//                         for (int k = 0; k < heightPoints.length; k++) {
//                             int yPoint = heightPoints[k];
//                             if(heightPlacedPoints[i][yPoint] && widthPlacedPoints[i][xPoint]){
//                                 double value = 0;
//                                 try {
//                                     value = model.getValue(R[i][xPoint][yPoint]);
//                                 } catch (Exception e) {
//                                     throw new RuntimeException(e);
//                                 }
//                                 if (value > 0.5) {
//                                     modeRequiredData.resultPoints[i][0] = i;
//                                     modeRequiredData.resultPoints[i][1] = blockSize[i][0];
//                                     modeRequiredData.resultPoints[i][2] = blockSize[i][1];
//                                     modeRequiredData.resultPoints[i][3] = xPoint;
//                                     modeRequiredData.resultPoints[i][4] = yPoint;
//                                     modeRequiredData.resultPoints[i][5] = xPoint + blockSize[i][0];
//                                     modeRequiredData.resultPoints[i][6] = yPoint + blockSize[i][1];
//                                 }
//                             }
//                         }
//                     }
//                 }
//                 return true;
//             }
//         } catch (CpxException e) {
//             System.out.println(" out-of-memory status ");
//
//         } catch (Exception e) {
//             System.out.println("  未知错误！  ");
//             throw new RuntimeException(e);
//         }
//         model.end();
//         return false;
//     }
//
//     public boolean rebuildModel(ModeRequiredData modeRequiredData, TargetData rLayout, double time) throws IloException, FileNotFoundException {
//         while (true) {
//             boolean masterModelResult = masterModel(modeRequiredData);
//             if (masterModelResult) {
//                 return true;
//             }
//             double endTime = System.currentTimeMillis() / 1000.0;
//             if ((endTime - time) >= ModeRequiredData.Two_Dimension_Scheme) {
//                 return false;
//             }
//             // 求解失败
//             ++modeRequiredData.oriSize[1];
//             modeRequiredData.improveModel1(modeRequiredData, rLayout);
//             System.out.println("modeRequiredData.lowerBoundHeight :    " + modeRequiredData.oriSize[1]);
//         }
//     }
//
//     public static void main(String[] args) throws Exception {
//         GetFile getFile = new GetFile();
//         File file = new File("C:\\Users\\浩\\Desktop\\test\\parttest");
//         List<File> allFile = getFile.getAllFile(file);
//         for (int i = 0; i < allFile.size(); i++) {
//             File value = allFile.get(i);
//             String path = value.getAbsolutePath();
//             String rePath = value.getName();
//             String parentName = value.getParentFile().getName();
//             System.out.println("value.getName() " + rePath);
//
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
//
//             modeRequiredData = modeRequiredData.initModel1(path, rLayout);
//             TwoDimensionModel twoDimensionModel = new TwoDimensionModel();
//             boolean isSolveModel = false;
//             try {
//                 isSolveModel = twoDimensionModel.rebuildModel(modeRequiredData, rLayout, startTime);
//             }  catch (CpxException e) {
//                 System.out.println(" out-of-memory status ");
//
//             } catch (Exception e) {
//                 throw new RuntimeException(e);
//             }
//             if (!isSolveModel) {
//                 System.out.println("  未找到解法  ");
//                 continue;
//             }
//             double endTime = System.currentTimeMillis() / 1000.0;
//             double time = endTime - startTime;
//             System.out.println("time  " + time);
// //            String outputFilePath = "C:\\Users\\浩\\Desktop\\ResultData 02\\exact\\timenedless02\\" + parentName  + "\\" + rePath + ".pack"; // 指定输出文件的路径
//             String outputFilePath = "C:\\Users\\浩\\Desktop\\ResultData 02\\exact\\twodimension01" + "\\" + rePath + ".pack"; // 指定输出文件的路径
//             Print.printResultsCBP(outputFilePath, time, modeRequiredData);
//         }
//     }
// }
