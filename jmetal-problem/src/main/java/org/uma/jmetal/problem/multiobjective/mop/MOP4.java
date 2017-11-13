package org.uma.jmetal.problem.multiobjective.mop;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/5/4.
 */
public class MOP4 extends AbstractDoubleProblem {

    public MOP4() {
        this(10, 2);
    }

    public MOP4(Integer numberOfVariables, Integer numberOfObjectives) throws JMetalException {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(numberOfObjectives);
        setName("MOP4");

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

        double x1 = solution.getVariableValue(0);
        f[0] = (1.0 + g) * x1;
        f[1] = (1.0 + g) * (1.0 - Math.sqrt(x1)
                * Math.cos(2.0 * Math.PI * x1)
                * Math.cos(2.0 * Math.PI * x1));;

        for (int i = 0; i < numberOfObjectives; i++) {
            solution.setObjective(i, f[i]);
        }
    }

    private double evalG(DoubleSolution solution, double[] t) throws JMetalException {
        double g = 0.0;

        for (int i = 1; i < getNumberOfVariables(); i++)
            g += (Math.abs(t[i]) / (1.0 + Math.exp(5.0 * Math.abs(t[i]))));

        g = 10.0 * Math.sin(Math.PI * solution.getVariableValue(0)) * g;

        return g;
    } // evalG

    public double[] evalT(DoubleSolution solution) throws JMetalException{
        double[] t = new double[getNumberOfVariables()];

        double temp = Math.sin(0.5 * Math.PI * solution.getVariableValue(0));
        for (int i = 1; i < getNumberOfVariables(); i++)
            t[i] = solution.getVariableValue(i) - temp;

        return t;
    }
}
