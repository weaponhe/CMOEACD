package org.uma.jmetal.solution.util;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * Created by X250 on 2016/7/20.
 */
public class RepairDoubleSolutionByFold implements RepairDoubleSolution {
    private JMetalRandom randomGenerator ;

    /**
     * Constructor
     */
    public RepairDoubleSolutionByFold() {
        randomGenerator = JMetalRandom.getInstance() ;
    }
    /**
     * Checks if the value is between its bounds; if not, a random value between the limits is returned
     * @param value The value to be checked
     * @param lowerBound
     * @param upperBound
     * @return The same value if it is between the limits or a repaired value otherwise
     */
    public double repairSolutionVariableValue(double value, double lowerBound, double upperBound) {
        if (lowerBound > upperBound) {
            throw new JMetalException("The lower bound (" + lowerBound + ") is greater than the "
                    + "upper bound (" + upperBound+")") ;
        }
        double result = value ;
        double range = upperBound - lowerBound;
        if (value < lowerBound) {
            double spilthRate = (lowerBound - value)/range;
            result = lowerBound + (spilthRate - Math.floor(spilthRate))*range;
        }
        if (value > upperBound) {
            double spilthRate = (value - upperBound)/range;
            result = upperBound - (spilthRate - Math.floor(spilthRate))*range;
        }

        return result ;
    }
}
