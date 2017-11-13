package org.uma.jmetal.problem.singleobjective;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class Schwefel extends AbstractDoubleProblem {
    /**
     * Constructor
     *
     * @param numberOfVariables Number of variables of the problem
     */
    //optimum 0
    public Schwefel(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(1);
        setNumberOfConstraints(0) ;
        setName("Schwefel_"+numberOfVariables);

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-500.0);
            upperLimit.add(500.0);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }

    /** Evaluate() method */
    @Override
    public void evaluate(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables() ;

        double[] f = new double[getNumberOfObjectives()];
        double[] x = new double[numberOfVariables] ;

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double result = 0.0;

        for (int i = 0; i < numberOfVariables; i++) {
            result +=  (x[i]*Math.sin(Math.sqrt(Math.abs(x[i]))));
        }

        result = 418.9829*numberOfVariables - result;

        solution.setObjective(0, result);
    }
}

