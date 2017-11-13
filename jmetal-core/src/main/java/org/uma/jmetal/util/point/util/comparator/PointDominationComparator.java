package org.uma.jmetal.util.point.util.comparator;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.point.Point;

import java.util.Comparator;

/**
 * Created by X250 on 2016/4/20.
 */
public class PointDominationComparator extends PointComparator {
    public PointDominationComparator() {
        super();
    }

    @Override
    public int compare(Point pointOne, Point pointTwo) {
        // returns -1 if pointOne dominates pointTwo, 1 if pointTwo dominates pointOne, 2 if pointOne == pointTwo, 0 otherwise

        if (pointOne == null) {
            throw new JMetalException("PointOne is null");
        } else if (pointTwo == null) {
            throw new JMetalException("PointTwo is null");
        } else if (pointOne.getNumberOfDimensions() != pointTwo.getNumberOfDimensions()) {
            throw new JMetalException("Points have different size: "
                    + pointOne.getNumberOfDimensions() + " and "
                    + pointTwo.getNumberOfDimensions());
        }

        for (int i = pointOne.getNumberOfDimensions()-1 ; i >= 0; i--) {
            if (isBetter(pointOne.getDimensionValue(i),pointTwo.getDimensionValue(i))) {
                for (int j = i - 1; j >= 0; j--) {
                    if (isBetter(pointTwo.getDimensionValue(j) , pointOne.getDimensionValue(j))) {
                        return 0;
                    }
                }
                return -1;
            } else if (isBetter(pointTwo.getDimensionValue(i) , pointOne.getDimensionValue(i))) {
                for (int j = i - 1; j >= 0; j--) {
                    if (isBetter(pointOne.getDimensionValue(j) , pointTwo.getDimensionValue(j))) {
                        return 0;
                    }
                }
                return 1;
            }
        }
        return 2;
    }
}