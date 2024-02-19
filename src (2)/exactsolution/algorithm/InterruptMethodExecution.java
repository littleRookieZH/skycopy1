package exactsolution.algorithm;//package exactsolution.algorithm;
//
//import com.twodimension.TargetData;
//import exactsolution.onedimensionalcontiguous.MasterModel;
//import exactsolution.onedimensionalcontiguous.ModeRequiredData;
//import ilog.concert.IloException;
//
//import java.io.FileNotFoundException;
//import java.util.concurrent.*;
//
///**
// * @author hao
// * @description: 目前没有用
// * @date 2023/7/28 19:33
// */
//public class InterruptMethodExecution {
//
//    public boolean timeLimit(long limit, MasterModel masterModel, ModeRequiredData modeRequiredData, TargetData rLayout) {
//        long startTimeMillis = System.currentTimeMillis();
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        // 调用方法A
//        MasterModelFunction methodAFunction = masterModel::solveModel;
//
//        Future future = executorService.submit(() -> {
//            try {
//                methodAFunction.solveModel(modeRequiredData, rLayout);
//            } catch (FileNotFoundException e) {
//                throw new RuntimeException(e);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        try {
//            // 设置最大等待时间为10秒
//            future.get(limit, TimeUnit.SECONDS);
//        } catch (TimeoutException e) {
//            future.cancel(true);
//            modeRequiredData.check = null;
//            masterModel = null;
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            future.cancel(true);
//            return false;
//        } finally {
//            executorService.shutdown();
//        }
//        return true;
//    }
//
//    // 定义一个函数式接口，用于传递方法A的函数
//    @FunctionalInterface
//    interface MasterModelFunction {
//        public void solveModel(ModeRequiredData modeRequiredData, TargetData rLayout) throws FileNotFoundException, IloException, RuntimeException;
//    }
//}
