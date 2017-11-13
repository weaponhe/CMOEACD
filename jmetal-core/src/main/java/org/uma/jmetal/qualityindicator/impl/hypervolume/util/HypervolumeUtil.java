package org.uma.jmetal.qualityindicator.impl.hypervolume.util;

/**
 * Created by X250 on 2016/5/28.
 */
public class HypervolumeUtil {
    public boolean beats(double x, double y) {
        return x > y;
    }

    public double worse(double x, double y) {
        if (x > y)
            return y;
        else
            return x;
    }
}
