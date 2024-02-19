package exactsolution.onedimensionalcontiguous;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombinationDP {
    public static List<List<Integer>> getCombinations(int[] arr, int maxLength) {
        int n = arr.length;
        boolean[][] dp = new boolean[n + 1][maxLength + 1];
        dp[0][0] = true; // 空组合值

        int maxCombination = 0; // 可达到的最大组合值

        for (int i = 1; i <= n; i++) {
            dp[i][0] = true; // 长度为 0 的组合值
            for (int j = 1; j <= maxCombination; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j >= arr[i - 1]) {
                    dp[i][j] |= dp[i - 1][j - arr[i - 1]];
                }
            }

            maxCombination += arr[i - 1]; // 更新可达到的最大组合值
            maxCombination = Math.min(maxCombination, maxLength); // 限制最大组合值不超过 maxLength
        }

        List<List<Integer>> combinations = new ArrayList<>();
        generateCombinations(arr, maxLength, n, dp, new ArrayList<>(), combinations);
        return combinations;
    }

    private static void generateCombinations(int[] arr, int maxLength, int i, boolean[][] dp, List<Integer> current, List<List<Integer>> combinations) {
        System.out.println(3);
        if (i == 0) {
            combinations.add(new ArrayList<>(current));
            return;
        }

        if (dp[i - 1][maxLength]) {
            generateCombinations(arr, maxLength, i - 1, dp, current, combinations);
        }
        if (maxLength >= arr[i - 1] && dp[i - 1][maxLength - arr[i - 1]]) {
            current.add(arr[i - 1]);
            generateCombinations(arr, maxLength - arr[i - 1], i - 1, dp, current, combinations);
            current.remove(current.size() - 1);
        }
        if (dp[i - 1][maxLength]) {
            generateCombinations(arr, maxLength, i - 1, dp, current, combinations);
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[10];
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = random.nextInt(30);
        }
        int maxLength = 200;

        List<List<Integer>> combinations = getCombinations(arr, maxLength);
        for (List<Integer> combination : combinations) {
            System.out.println(combination);
        }
    }
}
