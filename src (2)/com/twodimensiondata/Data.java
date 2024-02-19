package com.twodimensiondata;
/**
 * @author zh15178381496
 * @create 2022-09 20:49
 * @说明：
 * @总结：
 */
public class Data {
    /**
     *   目标块的种类，目前不知道有没有用
     */
    public int index;
    public int skyWidth;
    public int skyHeight;
    /**
     *   si的左边线的高度
     */
    public int slh;
    /**
     *   si右边线的高度
     */
    public int srh;
    public int x1;
    public int y1;

    public Data(int index, int skyWidth, int skyHeight, int slh, int srh,int x1,int y1) {
        this.index = index;
        this.skyWidth = skyWidth;
        this.skyHeight = skyHeight;
        this.slh = slh;
        this.srh = srh;
        this.x1 = x1;
        this.y1 = y1;
    }
    public Data() {
    }
    @Override
    public String toString() {
        return "Data{" +
                "index=" + index +
                ", skyWidth=" + skyWidth +
                ", skyHeight=" + skyHeight +
                ", slh=" + slh +
                ", srh=" + srh +
                '}';
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
    public int getskyWidth() {
        return skyWidth;
    }
    public void getskyWidth(int skyWidth) {
        this.skyWidth = skyWidth;
    }
    public int getSkyHeight() {
        return skyHeight;
    }
    public void setSkyHeight(int skyHeight) {
        this.skyHeight = skyHeight;
    }
    public int getSlh() {
        return slh;
    }
    public void setSlh(int slh) {
        this.slh = slh;
    }
    public int getSrh() {
        return srh;
    }
    public void setSrh(int srh) {
        this.srh = srh;
    }
    /**
     * 深拷贝
     */
    public Data copyData() {
        Data data1 = new Data(index, skyWidth, skyHeight,slh, srh, x1, y1);
        return data1;
    }
}
