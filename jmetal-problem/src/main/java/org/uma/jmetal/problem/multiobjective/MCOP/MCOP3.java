package org.uma.jmetal.problem.multiobjective.MCOP;

import org.uma.jmetal.solution.DoubleSolution;

/**
 * Created by X250 on 2016/12/29.
 */
public class MCOP3 extends MCOP {

    /**
     * Constructor
     */
    public MCOP3() {
        super(10,2,1,"MCOP3",1);
    }

    /**
     * Constructor
     */
    public MCOP3(int level) {
        super(10,2,1,"MCOP3",level);
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


        double g = gFun2(x);
        f[0] = g * x[0];
        f[1] = g * (1.0 - Math.sqrt(f[0]/g)) - f[0]*Math.sin(10.0*Math.PI*f[0]);

        for (int i = 0; i < numberOfObjectives; i++) {
            solution.setObjective(i, f[i]);
        }
    }

}
