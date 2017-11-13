package org.uma.jmetal.util.comparator.impl;

import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;

/**
 * Created by X250 on 2017/1/6.
 */
public class NormalizedViolationThresholdComparatorII<S extends Solution<?>> extends NormalizedViolationThresholdComparator<S> {
    /**
     * Constructor
     */
    public NormalizedViolationThresholdComparatorII(int numberOfConstraints) {
        super(numberOfConstraints);
    }

    protected double calcNormalizedOverallViolation(S solution) {
        double normalizedOverall = 0.0;
        for (int i = 0; i < solution.getNumberOfConstraints(); i++){
            if (solution.getConstraintViolation(i) < 0.0) {
                normalizedOverall += Math.min(0.0,solution.getConstraintViolation(i)) / constraintNadir.get(i);
            }
        }
        return normalizedOverall*numberOfViolatedConstraints.getAttribute(solution);
    }
}
