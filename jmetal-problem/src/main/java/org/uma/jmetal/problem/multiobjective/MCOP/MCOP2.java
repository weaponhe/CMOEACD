package org.uma.jmetal.problem.multiobjective.MCOP;

import org.uma.jmetal.solution.DoubleSolution;

/**
 * Created by X250 on 2016/12/29.
 */
public class MCOP2 extends MCOP {

    /**
     * Constructor
     */
    public MCOP2() {
        super(30,2,1,"MCOP2",1);
    }

    /**
     * Constructor
     */
    public MCOP2(int level) {
        super(30,2,1,"MCOP2",level);
    }

    /**
     * Evaluate() method
     */
    public void evaluate(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables();
        int numberOfObjectives = getNumberOfObjectives();

        double[] f = new double[numberOfObjectives];
        double[] x = new double[numberOfVariables];

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }


        double g = gFun1(x);
        f[0] = g * x[0];
        f[1] = g * (1.0 - Math.pow(f[0]/g,2.0));

        for (int i = 0; i < numberOfObjectives; i++) {
            solution.setObjective(i, f[i]);
        }
    }

}
