package exactsolution.onedimensionalcontiguous;// package exactsolution.onedimensionalcontiguous;
//
// import org.junit.Test;
//
//
// import java.util.*;
//
// public class TestClass {
//
//     /**
//      * 已知两个集合：一个靠左放置的点集、一个靠右放置的点集
//      */
//     public List<Integer> getTboundary(int[] leftPoints, int[] rightPoints, int lengthSize) {
//         int minValue = leftPoints.length;
//         int index = 0;
//         for (int i = 0; i < lengthSize + 1; i++) {
//             int k = 0;
//             for (int j = 0; j < leftPoints.length; j++) {
//                 if (leftPoints[j] >= i) {
//                     break;
//                 }
//                 k++;
//             }
//
//             for (int m = rightPoints.length - 1; m > 0; m--) {
//                 if (leftPoints[m] <= i) {
//                     break;
//                 }
//                 k++;
//             }
//             if (contains(leftPoints, i) || contains(rightPoints, i)) {
//                 k++;
//             }
//             System.out.println("个数为" + k);
//             if (minValue > k) {
//                 index = i;
//                 minValue = k;
//             }
//         }
//         System.out.println(index);
//         List<Integer> arrayList = new ArrayList<>();
//         for (int a1 : leftPoints) {
//             if (a1 < index) {
//                 arrayList.add(a1);
//             }
//         }
//         for (int a2 : rightPoints) {
//             if (a2 > index) {
//                 arrayList.add(a2);
//             }
//         }
//         if (contains(leftPoints, index) || contains(rightPoints, index)) {
//             arrayList.add(index);
//         }
//         return arrayList;
//     }
//
//     public boolean contains(int[] array, int num) {
//         for (int i : array) {
//             if (i == num) {
//                 return true;
//             }
//         }
//         return false;
//     }
//
//     public static void main(String[] args) {
//
//     }
//
//     @Test
//     public void test() {
//         MultiValueMap<String, String> stringMultiValueMap = new LinkedMultiValueMap<>();
//
//         stringMultiValueMap.add("早班 9:00-11:00", "周一");
//         stringMultiValueMap.add("早班 9:00-11:00", "周一");
//         stringMultiValueMap.add("早班 9:00-11:00", "周二");
//         stringMultiValueMap.add("中班 13:00-16:00", "周三");
//         stringMultiValueMap.add("早班 9:00-11:00", "周四");
//         stringMultiValueMap.add("测试1天2次 09:00 - 12:00", "周五");
//         stringMultiValueMap.add("测试1天2次 09:00 - 12:00", "周六");
//         stringMultiValueMap.add("中班 13:00-16:00", "周日");
//         //打印所有值
//         Set<String> keySet = stringMultiValueMap.keySet();
//         for (String key : keySet) {
//             List<String> values = stringMultiValueMap.get(key);
//             for (String value : values) {
//                 System.out.println("key " + key + "value:" + value);
//             }
//         }
//     }
//
//     @Test
//     public void test01() {
//         String target = "I:\1CBP测试集\test\1CBP\beng\beng01.txt";
//         String pre = "I:\1CBP测试集\test\1CBP\beng";
//         String result = target.replaceAll(pre, "");
//         System.out.println(result);
//         System.out.println(target);
//         System.out.println(pre);
//     }
//
//     // 动态规划
//     @Test
//     public void test02() {
//         int[] arr = new int[40];
//         Random random = new Random();
//         for (int i = 0; i < arr.length; i++) {
//             arr[i] = random.nextInt(30);
//         }
//         System.out.println(Arrays.toString(arr));
//         int maxLength = 200;
//         long start = System.currentTimeMillis();
//         List<List<Integer>> lists = getCombinations1(arr, maxLength);
//         for (List<Integer> list1 : lists) {
//             System.out.println(list1);
//         }
//         long end = System.currentTimeMillis();
//         System.out.println("时间花销：    " + (start - end));
//     }
//
//     public List<List<Integer>> generateCombinations(int[] arr, int maxLength) {
//         List<List<Integer>>[] dp = new List[maxLength + 1];
// //        HashSet<Integer> objects = new HashSet<>();
//         dp[0] = new ArrayList<>();
//         dp[0].add(new ArrayList<>());
//
//         for (int num : arr) {
//             for (int i = maxLength; i >= 1; i--) {
//                 if (dp[i - 1] != null) {
//                     if (dp[i] == null) {
//                         dp[i] = new ArrayList<>();
//                     }
//                     for (List<Integer> combination : dp[i - 1]) {
//                         List<Integer> newCombination = new ArrayList<>(combination);
//                         newCombination.add(num);
//                         dp[i].add(newCombination);
//                     }
//                 }
//             }
//         }
//
//         if (dp[maxLength] != null) {
//             return dp[maxLength];
//         } else {
//             return new ArrayList<>();
//         }
//     }
//
//     public static List<Integer> getCombinations(int[] arr, int limit) {
//         boolean[] dp = new boolean[limit + 1];
//         dp[0] = true; // 空组合值
//         for (int num : arr) {
//             boolean[] temp = new boolean[limit + 1];
//             for (int j = 0; j <= limit; j++) {
//                 if (dp[j] && j + num <= limit) {
//                     temp[j + num] = true;
//                 }
//             }
//             for (int j = 0; j <= limit; j++) {
//                 dp[j] |= temp[j];
//             }
//         }
//         List<Integer> combinations = new ArrayList<>();
//         for (int i = 0; i <= limit; i++) {
//             if (dp[i]) {
//                 combinations.add(i);
//             }
//         }
//         return combinations;
//     }
//
//     public static List<List<Integer>> getCombinations1(int[] arr, int limit) {
//         List<List<Integer>> combinations = new ArrayList<>();
//         Arrays.sort(arr); // 排序数组，以确保不重复组合值
//         backtrack(combinations, new ArrayList<>(), arr, limit, 0);
//         return combinations;
//     }
//
//     private static void backtrack(List<List<Integer>> combinations, List<Integer> current, int[] arr, int limit, int start) {
//         if (limit < 0) {
//             return; // 超过限制长度，不继续递归
//         }
//         if (limit == 0) {
//             combinations.add(new ArrayList<>(current)); // 找到符合条件的组合值
//             return;
//         }
//         for (int i = start; i < arr.length; i++) {
//             if (i > start && arr[i] == arr[i - 1]) {
//                 continue; // 跳过重复的元素，避免重复计算
//             }
//             current.add(arr[i]);
//             backtrack(combinations, current, arr, limit - arr[i], i + 1);
//             current.remove(current.size() - 1);
//         }
//     }
//
//     @Test
//     public void test03() {
//         int[] arr = new int[40];
//         Random random = new Random();
//         for (int i = 0; i < arr.length; i++) {
//             arr[i] = random.nextInt(30) + 1;
//         }
//         System.out.println(Arrays.toString(arr));
//         int maxLength = 200;
//         long start = System.currentTimeMillis();
//         getWidth(arr, maxLength);
//         long end = System.currentTimeMillis();
//         System.out.println("时间花销：    " + (start - end));
// //        List<List<Integer>> list1 = new ArrayList<>();
// //        List<Integer> list2 = new ArrayList<>();
// //        for (int i = 0; i < rectangles.length; i++) {
// //            list2.add(0);
// //            list2.add(1);
// //            list1.add(list2);
// //        }
// //        noDefRightPoints(list1, 0, rectangles, 0, 20);
// //        System.out.println(pointArray);
//     }
//
//     public void getWidth(int[] arr, int width){
// //        boolean[][] normalPatternW = new boolean[arr.length][width];
// //        int[] leftW = new int[width+ 1];
// //        int[] rightW = new int[width+ 1];
//         boolean[] RNPatternW = new boolean[width + 1];
//         for (int i = 0; i < arr.length; i++) {
//             boolean[] CW = new boolean[width];
//             boolean[] indexW = new boolean[width];
// //            normalPatternW[i][0] = true;
//             CW[0] = true;
//             indexW[0] = true;
//             int indexw = 0;
//             for (int k = 0; k < arr.length; k++) {
//                 if (k != i) {
//                     try {
//                         // CW[j]表示当前位置是否放置
//                         for (int j = 0; j <= width - arr[i]; j++) {
//                             if (CW[j]) {
//                                 if (j + arr[k] <= width - arr[i]) {
//                                     indexW[j + arr[k]] = true;
//                                     indexw = j + arr[k];
//                                 }
//                             }
//                         }
//                         System.out.println(555);
//                     } catch (RuntimeException e) {
//                         throw new RuntimeException(e);
//                     }
//                     for (int t = 0; t <= indexw; t++) {
//
//                         if (indexW[t]) {
//                             System.out.println(t);
// //                            leftW[t] = 1;
// //                            rightW[width - arr[i] - t] = 1;
//                             CW[t] = true;
//                             RNPatternW[t] = true;
// //                            normalPatternW[i][t] = true;
//                         }
//                     }
//                 }
//             }
//         }
//         for (int i = 0; i < RNPatternW.length; i++) {
//             if(RNPatternW[i]){
//                 System.out.println(i);
//             }
//         }
//     }
//
//     int total;
//     List<Integer> pointArray = new ArrayList<>();
//     public void noDefRightPoints(List<List<Integer>> dataCombinations, int group, int[] combinationArray, int index, int width) {
//         //得到一个不同类型排列的组合
//         if (group == dataCombinations.size()) {
//             int j = 0;
//             total = 0;
//
//             for (int i : combinationArray) {
//                 //矩形组合的长度
//                 total = total + i * width;
//                 if(total > width){
//                     break;
//                 }
//                 j++;
//             }
//             // 保证 长度 小于等于 最大有效值，结果不能重复
//             int var1 = width - total;
//             if (total > 0 && total <=  width && !pointArray.contains(var1)) {
//                 // 右侧离散点： W\H　－　求和wi
//                 pointArray.add(var1);
//             }
//             return;
//         }
//         //递归生成组合数组
//         for (int num : dataCombinations.get(group)) {
//             if (total < width) {
//                 combinationArray[group] = num;
//                 noDefRightPoints(dataCombinations, group + 1, combinationArray, index, width);
//             }
//             //表示在上一次循环中，求得的total已经大于等于总宽；此时同组目标块个数不必再递增
//             if (total >= width) {
//                 total = 0;
//                 return;
//             }
//         }
//     }
// }
