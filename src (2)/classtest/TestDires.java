package classtest;

import java.util.Arrays;
import java.util.TreeMap;

/**
 * @author xzbz
 * @create 2023-09-06 14:30
 */
public class TestDires{
    public static void main(String[] args) {
        double[] inputVector = {4.5, 2.0, 1.0, 3.5, 5.0}; // 输入的连续型解向量
        int[] orderVector = new int[inputVector.length]; // 用于存储顺序值的数组
        int[] rankVector = new int[inputVector.length];  // 用于存储排名的数组

        // 使用TreeMap来进行排序并计算排名
        TreeMap<Double, Integer> map = new TreeMap<>();
        for (int i = 0; i < inputVector.length; i++) {
            map.put(inputVector[i], i);
        }

        int rank = 1;
        for (int index : map.values()) {
            rankVector[index] = rank++;
        }

        // 为每个项目分配顺序值
        for (int i = 0; i < inputVector.length; i++) {
            orderVector[i] = i + 1;
        }

        // 输出排名和顺序值
        System.out.println("Rank Vector: " + Arrays.toString(rankVector));
        System.out.println("Order Vector: " + Arrays.toString(orderVector));
    }
}

