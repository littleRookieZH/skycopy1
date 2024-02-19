package hybridalgorithm.aoawithskyline;

import com.twodimension.SkyLine;

import java.io.Serializable;

/**
 * @author xzbz
 * @create 2023-08-28 10:35
 */
public class AlgorithmParameter implements Serializable {
    public SkyLine skyLine;
    public double[] den;
    public double[] vol;
    public double[] acc;
    public double[] standardAcc;
    public String methodName;

    public AlgorithmParameter(SkyLine skyLine, double[] den, double[] vol, double[] acc, double[] standardAcc) {
        // 这里不用担心引用对象的问题，因为每一个skyline都是单独的
        this.skyLine = skyLine;
        this.den = den;
        this.vol = vol;
        this.acc = acc;
        this.standardAcc = standardAcc;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
