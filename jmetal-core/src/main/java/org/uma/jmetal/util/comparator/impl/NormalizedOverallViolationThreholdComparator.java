package org.uma.jmetal.util.comparator.impl;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.List;

/**
 * Created by X250 on 2017/1/4.
 */
public class NormalizedOverallViolationThreholdComparator <S extends Solution<?>>  extends ViolationThresholdComparator<S> {
    protected double maxOverallViolation;
    /**
     * Constructor
     */
    public NormalizedOverallViolationThreholdComparator() {
        super();
        maxOverallViolation = 1.0;
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
        if (overallConstraintViolation.getAttribute(solution1) == null) {
            return 0 ;
        }

        double normalizedOverall1, normalizedOverall2;
        normalizedOverall1 = calcNormalizedOverallViolation(solution1);
        normalizedOverall2 = calcNormalizedOverallViolation(solution2);

        if ((normalizedOverall1 < 0) && (normalizedOverall2 < 0)) {
            if (normalizedOverall1 > normalizedOverall2) {
                return -1;
            } else if (normalizedOverall2 > normalizedOverall1) {
                return 1;
            } else {
                return 0;
            }
        } else if ((normalizedOverall1 == 0) && (normalizedOverall2 < 0)) {
            return -1;
        } else if ((normalizedOverall1 < 0) && (normalizedOverall2 == 0)) {
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
        double normlizedOverall1, normalizedOverall2;
        normlizedOverall1 = Math.abs(calcNormalizedOverallViolation(solution1));
        normalizedOverall2 = Math.abs(calcNormalizedOverallViolation(solution2));

        needToCompare = (normlizedOverall1 > this.threshold) || (normalizedOverall2 > this.threshold);

        return needToCompare;
    }


    public boolean underViolationEp(S solution) {
        double normlizedOverall = Math.abs(calcNormalizedOverallViolation(solution));
        return normlizedOverall < this.threshold;
    }

    protected double calcMaxOverallViolation(List<S> solutionSet){
        maxOverallViolation = 0.0;
        for (int i=0;i<solutionSet.size();i++){
            if(overallConstraintViolation.getAttribute(solutionSet.get(i)) < 0.0){
                maxOverallViolation = Math.max(maxOverallViolation,Math.abs(overallConstraintViolation.getAttribute(solutionSet.get(i))));
            }
        }
        if(maxOverallViolation <= 0.0)
            maxOverallViolation = 1.0;
        return maxOverallViolation;
    }
    /**
     * Computes the feasibility ratio
     * Return the ratio of feasible solutions
     */
    public double meanOverallViolation(List<S> solutionSet) {
        double aux = 0.0;
        for (int i = 0; i < solutionSet.size(); i++) {
            aux += Math.abs(calcNormalizedOverallViolation(solutionSet.get(i)));
        }
        return aux / (double) solutionSet.size();
    }

    public double calcNormalizedOverallViolation(S solution){
        double result = overallConstraintViolation.getAttribute(solution)/maxOverallViolation*numberOfViolatedConstraints.getAttribute(solution);
        return result;
    }

    /**
     * Updates the threshold value using the population
     */
    public void updateThreshold(List<S> set) {
        calcMaxOverallViolation(set);
        threshold = feasibilityRatio(set) * meanOverallViolation(set);
    }
}
