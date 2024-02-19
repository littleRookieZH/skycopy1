package com.twodimensiondata;

import com.twodimension.TargetData;

import java.util.HashMap;
import java.util.List;

public class PipeiTest {
    public static void main(String[] args) {
        DefEdgeMatching defEdgeMatching = new DefEdgeMatching();
        int[][] defPoints = new int[][]{{2,2,6,4},{4,6,8,10},{8,2,12,4}};
        int[][] tempPoints = new int[][]{{10,10,12,14}};
        int[] rectangle = new int[] {6,4,8,6};

        HashMap<String, List<int[]>> edgeMatching = new HashMap<>();
        defEdgeMatching.initDefSide(defPoints, edgeMatching);

//        HashMap<String, List<int[]>> tempMatching = new HashMap<>();
//        defEdgeMatching.initDefSide(tempPoints, tempMatching);
//
//        boolean matching = defEdgeMatching.isMatching(edgeMatching, tempMatching);
//        System.out.println(matching);

        // 检查边是否匹配
        DefEdgeMatching defEdgeMatching1 = new DefEdgeMatching();
        // 顶边
        HashMap<String, List<int[]>> topSideHashMap = defEdgeMatching1.initRecTopSide(rectangle);
        // 匹配
        boolean matching1 = defEdgeMatching1.isMatching(edgeMatching, topSideHashMap);
        if(matching1){
            System.out.println("匹配成功！");
        }

    }
}
