package com.twodimensiondata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefEdgeMatching {
    /**
     * 匹配缺陷块的边数
     *
     * @param defPoints
     * @param edgeMatching
     */
    public void initDefSide(int[][] defPoints, HashMap<String, List<int[]>> edgeMatching) {
        // 加入边
        for (int i = 0; i < defPoints.length; i++) {
            String v1 = "v-" + defPoints[i][0];
            String l1 = "l-" + defPoints[i][1];
            String v2 = "v-" + defPoints[i][2];
//            String l2 = "l-" + defPoints[i][3];
            // v1
            if (!edgeMatching.containsKey(v1)) {
                List<int[]> list1 = new ArrayList<>();
                list1.add(new int[]{defPoints[i][1], defPoints[i][3]});
                edgeMatching.put(v1, list1);
            } else {
                edgeMatching.get(v1).add(new int[]{defPoints[i][1], defPoints[i][3]});
            }
            // l1
            if (!edgeMatching.containsKey(l1)) {
                List<int[]> list1 = new ArrayList<>();
                list1.add(new int[]{defPoints[i][0], defPoints[i][2]});
                edgeMatching.put(l1, list1);
            } else {
                edgeMatching.get(l1).add(new int[]{defPoints[i][0], defPoints[i][2]});
            }
            // v2
            if (!edgeMatching.containsKey(v2)) {
                List<int[]> list1 = new ArrayList<>();
                list1.add(new int[]{defPoints[i][1], defPoints[i][3]});
                edgeMatching.put(v2, list1);
            } else {
                edgeMatching.get(v2).add(new int[]{defPoints[i][1], defPoints[i][3]});
            }
            // l2
//            if (!edgeMatching.containsKey(l2)) {
//                List<int[]> list1 = new ArrayList<>();
//                list1.add(new int[]{defPoints[i][0], defPoints[i][2]});
//                edgeMatching.put(l2, list1);
//            } else {
//                edgeMatching.get(l2).add(new int[]{defPoints[i][0], defPoints[i][2]});
//            }
        }
    }

    public boolean isMatching(HashMap<String, List<int[]>> sourEdgeMatching, HashMap<String, List<int[]>> destEdgeMatching) {
        for (HashMap.Entry<String, List<int[]>> entry : destEdgeMatching.entrySet()) {
            String key = entry.getKey();
            List<int[]> value = entry.getValue();
            if (sourEdgeMatching.containsKey(key)) {
                List<int[]> tempArr = sourEdgeMatching.get(key);
                for (int[] arr1 : tempArr) {
                    if ((arr1[0] >= value.get(0)[0]) && (arr1[1] <= value.get(0)[1])) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public HashMap<String, List<int[]>> initRecTopSide(int[] recPoints) {
        HashMap<String, List<int[]>> edgeMatching = new HashMap<>();
        // 加入边
//            String v1 = "v-" + recPoints[0];
//            String l1 = "l-" + recPoints[1];
//            String v2 = "v-" + recPoints[2];
        String l2 = "l-" + recPoints[3];
        List<int[]> list1 = new ArrayList<>();
        list1.add(new int[]{recPoints[0], recPoints[2]});
        edgeMatching.put(l2, list1);
        return edgeMatching;
    }

}
