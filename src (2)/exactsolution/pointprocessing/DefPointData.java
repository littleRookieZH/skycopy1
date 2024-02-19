package exactsolution.pointprocessing;

/**
 * @author hao
 * @description: 储存离散点信息的类
 * @date 2023/7/18 16:27
 */
public class DefPointData {
    // 宽度离散点
    public int[] widthPoints;
    // 宽度分界线
    public int widthBoundary;
    // 宽度：靠左放置点
    boolean[][] leftModelW;
    // 宽度：靠右放置点
    boolean[][] rightModelW;
    // 宽度：每一个离散点的具体可放置情况
    public boolean[][] widthPlacedPoints;
    // 高度离散点
    public int[] heightPoints;
    // 高度分界线
    public int heightBoundary;
    // 高度：靠左放置点
    boolean[][] lowerModelH;
    // 高度：靠右放置点
    boolean[][] upperModelH;
    // 高度：每一个离散点的具体可放置情况
    public boolean[][] heightPlacedPoints;
    public DefPointData() {
    }

    public void widthInfo(int boundary, int[] points, boolean[][] leftModel, boolean[][] rightModel, boolean[][] placedPoints) {
        this.widthPoints = points;
        this.widthBoundary = boundary;
        this.leftModelW = leftModel;
        this.rightModelW = rightModel;
        this.widthPlacedPoints = placedPoints;
    }
    public void heightInfo(int boundary, int[] points, boolean[][] leftModel, boolean[][] rightModel, boolean[][] placedPoints) {
        this.heightPoints = points;
        this.heightBoundary = boundary;
        this.lowerModelH = leftModel;
        this.upperModelH = rightModel;
        this.heightPlacedPoints = placedPoints;
    }
    public static int minSize(int[][] blocks, int index) {
        if (blocks.length == 0 || blocks == null) {
            return -1;
        }
        int min = blocks[0][index];
        for (int[] arr1 : blocks) {
            min = Math.min(min, arr1[index]);
        }
        return min;
    }

    public static int maxSize(int[][] blocks, int index) {
        if (blocks.length == 0 || blocks == null) {
            return -1;
        }
        int max = blocks[0][index];
        for (int[] arr1 : blocks) {
            max = Math.max(max, arr1[index]);
        }
        return max;
    }
    /**
     * @description
     * DefPointData：widthPlacedPoints（每个点的放置情况）、widthPoints、leftModelW、rightModelW 被赋初值
     * @author  hao
     * @date    2023/7/24 15:48
     * @param Width
     * @param minWidth
     * @param blockSize
     * @param defSize
     * @return DefPointData
    */
    public DefPointData widthSet(int Width, int minWidth, int[][] blockSize, int[][] defSize){
        DefectWidthPoints defectWidthPoints = new DefectWidthPoints();
        return defectWidthPoints.getWidthPoints(Width, minWidth, blockSize, defSize, this);
    }
    public DefPointData heightSet(int Height, int minHeight, int[][] blockSize, int[][] defSize){
        DefectHeightPoints defectHeightPoints = new DefectHeightPoints();
        return defectHeightPoints.getHeightPoints(Height, minHeight, blockSize, defSize, this);
    }
}
