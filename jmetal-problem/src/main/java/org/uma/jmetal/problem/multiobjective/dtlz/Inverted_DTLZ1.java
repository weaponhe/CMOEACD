package org.uma.jmetal.problem.multiobjective.dtlz;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

/**
 * Created by X250 on 2016/11/13.
 */
public class Inverted_DTLZ1 extends DTLZ1 {
    public Inverted_DTLZ1() throws ClassNotFoundException, JMetalException {
        this(7, 3);
    }
    public Inverted_DTLZ1(Integer numberOfVariables, Integer numberOfObjectives) throws JMetalException {
        super(numberOfVariables, numberOfObjectives);
        setName("InvertedDTLZ1");
    }
    public void evaluate(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables();
        int numberOfObjectives = getNumberOfObjectives() ;

        double[] f = new double[numberOfObjectives];
        double[] x = new double[numberOfVariables];

        int k = getNumberOfVariables() - getNumberOfObjectives() + 1;

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double g = 0.0;
        for (int i = numberOfVariables - k; i < numberOfVariables; i++) {
            g += (x[i] - 0.5) * (x[i] - 0.5) - Math.cos(20.0 * Math.PI * (x[i] - 0.5));
        }

        g = 100 * (k + g);
        for (int i = 0; i < numberOfObjectives; i++) {
            f[i] = (1.0 + g) * 0.5;
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            for (int j = 0; j < numberOfObjectives - (i + 1); j++) {
                f[i] *= x[j];
            }
            if (i != 0) {
                int aux = numberOfObjectives - (i + 1);
                f[i] *= 1 - x[aux];
            }
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            f[i] = (1.0 + g) * 0.5 - f[i];
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            solution.setObjective(i, f[i]);
        }    }
}
