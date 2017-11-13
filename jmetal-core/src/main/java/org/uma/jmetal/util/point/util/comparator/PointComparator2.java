package org.uma.jmetal.util.point.util.comparator;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.point.Point;

import java.util.Comparator;

/**
 * Created by X250 on 2016/4/12.
 */
public class PointComparator2 extends PointComparator{

    private int idxBegin ;
    public PointComparator2(int idxBegin) {
        setMaximizing();
        this.idxBegin = idxBegin;
    }

    /**
     * Compares two Point objects
     *
     * @param pointOne An object that reference a Point
     * @param pointTwo An object that reference a Point
     * @return -1 if o1 < o2, 1 if o1 > o2 or 0 in other case.
     */
    @Override
    public int compare(Point pointOne, Point pointTwo) {
        if (pointOne == null) {
            throw new JMetalException("PointOne is null");
        } else if (pointTwo == null) {
            throw new JMetalException("PointTwo is null");
        } else if (pointOne.getNumberOfDimensions() != pointTwo.getNumberOfDimensions()) {
            throw new JMetalException("Points have different size: "
                    + pointOne.getNumberOfDimensions() + " and "
                    + pointTwo.getNumberOfDimensions());
        }

        for (int i = idxBegin ; i >= 0; i--) {
            if (isBetter(pointOne.getDimensionValue(i), pointTwo.getDimensionValue(i))) {
                return -1;
            } else if (isBetter(pointTwo.getDimensionValue(i), pointOne.getDimensionValue(i))) {
                return 1;
            }
        }
        return 0;
    }

}
