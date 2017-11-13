package org.uma.jmetal.util.comparator.impl;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/12/26.
 */
public class NormalizedViolationThresholdComparator <S extends Solution<?>> extends ViolationThresholdComparator<S> {

    int numberOfConstraints = 0;
    protected List<Double> constraintNadir = null;
    /**
     * Constructor
     */
    public NormalizedViolationThresholdComparator(int numberOfConstraints) {
        super();
        constraintNadir = new ArrayList<>(numberOfConstraints);
        for (int i=0;i<numberOfConstraints;i++){
            constraintNadir.add(1.0);
        }
        this.numberOfConstraints = numberOfConstraints;
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


        double overall1 = calcNormalizedOverallViolation(solution1);
        double overall2 = calcNormalizedOverallViolation(solution2);

        if ((overall1 < 0) && (overall2 < 0)) {
            if (overall1 > overall2) {
                return -1;
            } else if (overall2 > overall1) {
                return 1;
            } else {
                return 0;
            }
        } else if ((overall1 >= 0.0) && (overall2 < 0)) {
            return -1;
        } else if ((overall1 < 0) && (overall2 >=0.0)) {
            return 1;
        } else {
            return 0;
        }
    }

    protected double calcNormalizedOverallViolation(S solution) {
        double normalizedOverall = 0.0;
        for (int i = 0; i < solution.getNumberOfConstraints(); i++){
            if (solution.getConstraintViolation(i) < 0.0) {
                normalizedOverall += Math.min(0.0,solution.getConstraintViolation(i)) / constraintNadir.get(i);
            }
        }
        return normalizedOverall;
    }
    /**
     * Returns true if solutions s1 and/or s2 have an overall constraint
     * violation with value less than 0
     */
    public boolean needToCompare(S solution1, S solution2) {
        boolean needToCompare;
        double overall1 = Math.abs(calcNormalizedOverallViolation(solution1));
        double overall2 = Math.abs(calcNormalizedOverallViolation(solution2));

        needToCompare = (overall1 > this.threshold) || (overall2 > this.threshold);

        return needToCompare;
    }


    public boolean underViolationEp(S solution) {
        double overall  = Math.abs(calcNormalizedOverallViolation(solution));

        return overall < this.threshold;
    }

    /**
     * Computes the feasibility ratio
     * Return the ratio of feasible solutions
     */
    public double feasibilityRatio(List<S> solutionSet) {
        double aux = 0.0;
        for (int i = 0; i < solutionSet.size(); i++) {
//      if (overallConstraintViolation.getAttribute(solutionSet.get(i)) < 0) {
            if (overallConstraintViolation.getAttribute(solutionSet.get(i)) >= 0.0) {
                aux = aux + 1.0;
            }
        }
        return aux / (double) solutionSet.size();
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

    public void updateConstraintNadir(List<S> solutionSet){
        double[] nadir = new double[numberOfConstraints];
        for (int j=0;j<numberOfConstraints;j++){
            nadir[j] = 0.0;
        }
        for (int i=0;i<solutionSet.size();i++){
            for (int j=0;j<numberOfConstraints;j++){
                if(solutionSet.get(i).getConstraintViolation(j)<0.0){
                    nadir[j] = Math.min(nadir[j],solutionSet.get(i).getConstraintViolation(j));
                }
            }
        }
        for (int j=0;j<numberOfConstraints;j++){
            nadir[j] = Math.abs(nadir[j]);
            if(nadir[j] <= Constant.TOLERATION)
                nadir[j] = Constant.TOLERATION;
            constraintNadir.set(j,nadir[j]);
        }
    }

    /**
     * Updates the threshold value using the population
     */
    public void updateThreshold(List<S> set) {
        updateConstraintNadir(set);
        threshold = feasibilityRatio(set) * meanOverallViolation(set);
    }
}
