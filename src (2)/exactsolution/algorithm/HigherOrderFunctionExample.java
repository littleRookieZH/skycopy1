package exactsolution.algorithm;

import java.util.ArrayList;
import java.util.List;

public class HigherOrderFunctionExample {

    public static void main(String[] args) {
        HigherOrderFunctionExample example = new HigherOrderFunctionExample();
        List<Integer> numbers = new ArrayList<>();

        // 使用自定义函数式接口，并传递Lambda表达式作为函数参数
        List<Integer> result1 = example.processNumbers(numbers, x -> x * x);
        System.out.println("结果1：" + result1);

        // 使用自定义函数式接口，并传递方法引用作为函数参数
        List<Integer> result2 = example.processNumbers(numbers, HigherOrderFunctionExample::doubleNumber);
        System.out.println("结果2：" + result2);
    }

    // 自定义函数式接口，代表一个数字操作
    @FunctionalInterface
    interface NumberOperation {
        int perform(int number);
    }

    // 方法，接收一个自定义函数式接口作为参数，并使用该接口处理列表中的每个元素
    public List<Integer> processNumbers(List<Integer> list, NumberOperation operation) {
        List<Integer> result = new ArrayList<>();
        for (int number : list) {
            // 调用传入的函数，处理列表中的每个元素，并将结果添加到结果列表中
            result.add(operation.perform(number));
        }
        return result;
    }

    // 数字操作，用于将整数值翻倍
    public static int doubleNumber(int number) {
        return number * 2;
    }
}
