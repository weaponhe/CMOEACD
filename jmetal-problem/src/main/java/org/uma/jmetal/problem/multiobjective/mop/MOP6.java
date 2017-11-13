package org.uma.jmetal.problem.multiobjective.mop;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/5/4.
 */
public class MOP6 extends AbstractDoubleProblem {

    public MOP6() {
        this(10, 3);
    }

    public MOP6(Integer numberOfVariables, Integer numberOfObjectives) throws JMetalException {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(numberOfObjectives);
        setName("MOP6");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0.0);
            upperLimit.add(1.0);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }

    /** Evaluate() method */
    public void evaluate(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables();
        int numberOfObjectives = getNumberOfObjectives() ;

        double[] f = new double[numberOfObjectives];
        double[] t = this.evalT(solution);
        double g = this.evalG(solution, t);

        f[0] = (1.0 + g) * solution.getVariableValue(0)  * solution.getVariableValue(1);
        f[1] = (1.0 + g) * solution.getVariableValue(0) * (1.0 - solution.getVariableValue(1));
        f[2] = (1.0 + g) * (1.0 - solution.getVariableValue(0));
        for (int i = 0; i < numberOfObjectives; i++) {
            solution.setObjective(i, f[i]);
        }
    }

    private double evalG(DoubleSolution solution, double[] t) throws JMetalException {
        double g = 0.0;

        for (int i = 2; i < getNumberOfVariables(); i++)
            g += (-0.9 * t[i] * t[i] + Math.pow(Math.abs(t[i]), 0.6));

        g = 2.0 * Math.sin(Math.PI * solution.getVariableValue(0)) * g;

        return g;
    } // evalG

    public double[] evalT(DoubleSolution solution) throws JMetalException{
        double[] t = new double[getNumberOfVariables()];

        double temp = solution.getVariableValue(0) * solution.getVariableValue(1);
        for (int i = 2; i < getNumberOfVariables(); i++)
            t[i] = solution.getVariableValue(i) - temp;

        return t;
    }
}
