package org.uma.jmetal.util.comparator.impl;

import org.uma.jmetal.solution.DoubleBinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ConstraintViolationComparator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

/**
 * Created by X250 on 2017/1/7.
 */
public class StochasticRankingComparator <S extends Solution<?>>
        implements ConstraintViolationComparator<S> {
    private OverallConstraintViolation<S> overallConstraintViolation ;
    protected double Pf = 0.45;
    public enum Ordering {ASCENDING, DESCENDING} ;
    private int objectiveId;
    private Ordering order;
    private JMetalRandom randomGenerator ;

    /**
     * Constructor
     */
    public StochasticRankingComparator(double Pf,int objectiveId) {
        overallConstraintViolation = new OverallConstraintViolation<S>() ;
        this.Pf = Pf;
        this.objectiveId = objectiveId;
        order = Ordering.ASCENDING;
        randomGenerator = JMetalRandom.getInstance();
    }

    /**
     * Constructor
     */
    public StochasticRankingComparator(double Pf,int objectiveId, Ordering order) {
        overallConstraintViolation = new OverallConstraintViolation<S>() ;
        this.Pf = Pf;
        this.objectiveId = objectiveId;
        this.order = order ;
        randomGenerator = JMetalRandom.getInstance();
    }
    /**
     * Compares two solutions. If the solutions has no constraints the method return 0
     * @param solution1 Object representing the first <code>Solution</code>.
     * @param solution2 Object representing the second <code>Solution</code>.
     * @return -1, or 0, or 1 if o1 is less than, equal, or greater than o2,
     * respectively.
     */
    public int compare(S solution1, S solution2) {
        if (solution1 == null) {
            if (solution2 == null) {
                return 0;
            } else {
                return  1;
            }
        } else if (solution2 == null) {
            return  -1;
        } else if (solution1.getNumberOfObjectives() <= objectiveId) {
            throw new JMetalException("The solution1 has " + solution1.getNumberOfObjectives()+ " objectives "
                    + "and the objective to sort is " + objectiveId) ;
        } else if (solution2.getNumberOfObjectives() <= objectiveId) {
            throw new JMetalException("The solution2 has " + solution2.getNumberOfObjectives()+ " objectives "
                    + "and the objective to sort is " + objectiveId) ;
        } else {
            double violationDegreeSolution1;
            double violationDegreeSolution2;
            if (overallConstraintViolation.getAttribute(solution1) == null) {
                return 0;
            }
            violationDegreeSolution1 = overallConstraintViolation.getAttribute(solution1);
            violationDegreeSolution2 = overallConstraintViolation.getAttribute(solution2);

            if (((violationDegreeSolution1 >= 0) && (violationDegreeSolution2 >= 0)) || (randomGenerator.nextDouble(0.0,1.0) < Pf)) {
                Double objective1 = solution1.getObjective(this.objectiveId);
                Double objective2 = solution2.getObjective(this.objectiveId);
                if (order == Ordering.ASCENDING) {
                    return Double.compare(objective1, objective2);
                } else {
                    return Double.compare(objective2, objective1);
                }
            } else {
                return Double.compare(violationDegreeSolution2, violationDegreeSolution1);
            }
        }
    }

    protected void updateAllowableProbability(double Pf){
        this.Pf = Pf;
    }
}