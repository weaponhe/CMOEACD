package org.uma.jmetal.util.point.util.distance;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.point.Point;

/**
 * Created by X250 on 2016/6/14.
 */
public class MinkowskiDistance implements PointDistance {
    double p = 1;
    public MinkowskiDistance(int _p){p = _p;}

    @Override
    public double compute(Point a, Point b) {
        if (a == null) {
            throw new JMetalException("The first point is null") ;
        } else if (b == null) {
            throw new JMetalException("The second point is null") ;
        } else if (a.getNumberOfDimensions() != b.getNumberOfDimensions()) {
            throw new JMetalException("The dimensions of the points are different: "
                    + a.getNumberOfDimensions() + ", " + b.getNumberOfDimensions()) ;
        }

        double distance = 0.0;

        for (int i = 0; i < a.getNumberOfDimensions(); i++) {
            distance += Math.pow(Math.abs(a.getDimensionValue(i) - b.getDimensionValue(i)),p);
        }
        return Math.pow(distance,1.0/p);
    }
}