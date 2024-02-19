package exactsolution.algorithm;

import java.util.ArrayList;
import java.util.List;

public class MethodExecutionWithInterrupt {

    public static void main(String[] args) {
        MethodExecutionWithInterrupt execution = new MethodExecutionWithInterrupt();
        Thread executingThread = Thread.currentThread();

        // 创建一个Runnable任务，在另一个线程中执行方法A
        Thread methodAThread = new Thread(() -> {
            List<Integer> result = execution.methodA();
            System.out.println("方法A执行结果：" + result);
        });

        // 启动线程执行方法A
        methodAThread.start();

        // 等待一段时间后中断方法A的执行
        try {
            Thread.sleep(5000); // 假设在5秒后中断方法A的执行
            executingThread.interrupt(); // 中断方法A的执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> methodA() {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // 执行添加元素动作
                result.add(i);
                i++;
                // 模拟执行时间未知
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("方法A被中断。");
        }
        return result;
    }
}
