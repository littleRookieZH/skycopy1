package exactsolution.algorithm;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsyncForLoop {

    public static void main(String[] args) {
        int iterations = 10;

        // 创建一个CompletableFuture数组
        CompletableFuture<Void>[] futures = new CompletableFuture[iterations];

        // 执行异步操作
        for (int i = 0; i < iterations; i++) {
            final int currentIndex = i;
            futures[i] = CompletableFuture.runAsync(() -> {
                // 执行一些操作，不会发生冲突
                // 可以在这里调用需要在循环中执行的方法
                // ...

                // 打印当前索引，以模拟循环体中的操作
                System.out.println("当前索引：" + currentIndex);
            });
        }

        // 等待所有异步任务完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures);

        try {
            // 阻塞直到所有任务完成
            allFutures.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有异步任务完成！");
    }
}
