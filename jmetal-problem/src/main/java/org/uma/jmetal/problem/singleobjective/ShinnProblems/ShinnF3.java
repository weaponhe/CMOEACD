package org.uma.jmetal.problem.singleobjective.ShinnProblems;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class ShinnF3 extends AbstractDoubleProblem {
    /**
     * Constructor
     *
     * @param numberOfVariables Number of variables of the problem
     */
    //optimum 0.0
    public ShinnF3(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(1);
        setNumberOfConstraints(0) ;
        setName("ShinnF3_"+ numberOfVariables);

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-100.00);
            upperLimit.add(100.00);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }

    /** Evaluate() method */
    @Override
    public void evaluate(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables() ;

        double[] x = new double[numberOfVariables] ;

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double f = 0.0;
        for (int i=0;i<numberOfVariables;i++){
            f += Math.pow(x[i] + 0.5,2.0);
        }

        solution.setObjective(0, f);
    }
}

