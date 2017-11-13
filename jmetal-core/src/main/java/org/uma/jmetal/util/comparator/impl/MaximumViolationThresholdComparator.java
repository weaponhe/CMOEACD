package org.uma.jmetal.util.comparator.impl;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.solutionattribute.impl.MaximumConstraintViolation;

import java.util.List;

/**
 * Created by X250 on 2017/1/7.
 */
public class MaximumViolationThresholdComparator<S extends Solution<?>> extends ViolationThresholdComparator<S> {
    protected MaximumConstraintViolation<S> maximumConstraintViolationDegree;
    /**
     * Constructor
     */
    public MaximumViolationThresholdComparator() {
        super();
        maximumConstraintViolationDegree = new MaximumConstraintViolation();
    }


    /**
     * Compares two solutions. If the solutions has no constraints the method return 0
     *
     * @param solution1 Object representing the first <code>Solution</code>.
     * @param solution2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
     * respectively.
     */
    @Override
    public int compare(S solution1, S solution2) {
        if (maximumConstraintViolationDegree.getAttribute(solution1) == null) {
            return 0 ;
        }

        double maximumViolation1, maximumViolation2;
        maximumViolation1 = maximumConstraintViolationDegree.getAttribute(solution1);
        maximumViolation2 = maximumConstraintViolationDegree.getAttribute(solution2);

        if ((maximumViolation1 < 0) && (maximumViolation2 < 0)) {
            if (maximumViolation1 > maximumViolation2) {
                return -1;
            } else if (maximumViolation2 > maximumViolation1) {
                return 1;
            } else {
                return 0;
            }
        } else if ((maximumViolation1 == 0) && (maximumViolation2 < 0)) {
            return -1;
        } else if ((maximumViolation1 < 0) && (maximumViolation2 == 0)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns true if solutions s1 and/or s2 have an overall constraint
     * violation with value less than 0
     */
    public boolean needToCompare(S solution1, S solution2) {
        boolean needToCompare;
        double maximumViolation1, maximumViolation2;
        maximumViolation1 = Math.abs(maximumConstraintViolationDegree.getAttribute(solution1));
        maximumViolation2 = Math.abs(maximumConstraintViolationDegree.getAttribute(solution2));

        needToCompare = (maximumViolation1 > this.threshold) || (maximumViolation2 > this.threshold);

        return needToCompare;
    }


    public boolean underViolationEp(S solution) {
        double maximumViolation = Math.abs(maximumConstraintViolationDegree.getAttribute(solution));
        return maximumViolation < this.threshold;
    }

    /**
     * Computes the feasibility ratio
     * Return the ratio of feasible solutions
     */
    public double meanMaximumViolation(List<S> solutionSet) {
        double aux = 0.0;
        for (int i = 0; i < solutionSet.size(); i++) {
            aux += Math.abs(maximumConstraintViolationDegree.getAttribute(solutionSet.get(i)));
        }
        return aux / (double) solutionSet.size();
    }

    /**
     * Updates the threshold value using the population
     */
    public void updateThreshold(List<S> set) {
        threshold = feasibilityRatio(set) * meanMaximumViolation(set);
    }
}
