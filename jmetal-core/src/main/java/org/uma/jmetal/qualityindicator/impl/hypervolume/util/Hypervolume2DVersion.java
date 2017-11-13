package org.uma.jmetal.qualityindicator.impl.hypervolume.util;

import org.uma.jmetal.util.point.util.comparator.PointComparator;

/**
 * Created by X250 on 2016/5/28.
 */
public class Hypervolume2DVersion {
    PointComparator comparator;
    public Hypervolume2DVersion(){
        comparator = new PointComparator();
    }

    public double getHV(WfgHypervolumeFront front) {
        double volume = 0.0;

        front.sort(0,front.getNumberOfPoints(),comparator);

        volume = front.getPoint(0).getDimensionValue(0) * front.getPoint(0).getDimensionValue(1);
        for (int i = 1; i < front.getNumberOfPoints(); i++) {
            volume += (front.getPoint(i).getDimensionValue(0) - front.getPoint(i - 1).getDimensionValue(0)) *
                    front.getPoint(i).getDimensionValue(1);
        }
        return volume;
    }
}
