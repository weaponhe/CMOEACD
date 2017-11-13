package org.uma.jmetal.problem.singleobjective.ShinnProblems;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class ShinnF8 extends AbstractDoubleProblem {
    /**
     * Constructor
     *
     * @param numberOfVariables Number of variables of the problem
     */
    //optimum 0
    public ShinnF8(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(1);
        setNumberOfConstraints(0) ;
        setName("ShinnF8_"+ numberOfVariables);

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-30.0);
            upperLimit.add(30.0);
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
        double tmp1 = 0.0;
        double tmp2 = 0.0;
        for (int i=0;i<numberOfVariables;i++) {
            tmp1 += Math.pow(x[i],2.0);
            tmp2 += Math.cos(2.0*Math.PI*x[i]);
        }
        tmp1 = -0.2*Math.sqrt(tmp1/numberOfVariables);
        tmp2 = tmp2/numberOfVariables;
        f = 20.0 + Math.exp(1.0) - 20.0*Math.exp(tmp1) - Math.exp(tmp2);

        solution.setObjective(0, f);
    }
}

