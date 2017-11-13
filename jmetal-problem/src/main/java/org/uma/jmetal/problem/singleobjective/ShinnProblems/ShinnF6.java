package org.uma.jmetal.problem.singleobjective.ShinnProblems;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class ShinnF6 extends AbstractDoubleProblem {
    /**
     * Constructor
     *
     * @param numberOfVariables Number of variables of the problem
     */
    //optimum -1.85D
    public ShinnF6(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(1);
        setNumberOfConstraints(0) ;
        setName("ShinnF6_"+ numberOfVariables);

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-1.0);
            upperLimit.add(2.0);
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
            f += (x[i]*Math.sin(10.0*Math.PI*x[i]));
        }

        solution.setObjective(0, -f);
    }
}

