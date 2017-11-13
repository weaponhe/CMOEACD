package org.uma.jmetal.problem.multiobjective.MCOP;

import org.uma.jmetal.solution.DoubleSolution;

/**
 * Created by X250 on 2016/12/29.
 */
public class MCOP7 extends MCOP {

    /**
     * Constructor
     */
    public MCOP7() {
        super(10,2,1,"MCOP7",1);
    }

    /**
     * Constructor
     */
    public MCOP7(int level) {
        super(10,2,1,"MCOP6",level);
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


        double g = gFun3(x);
        f[0] = 1.0 - Math.exp(-4.0*x[0])*Math.pow(Math.sin(6.0*Math.PI*x[0]),6.0);
        f[1] = g * (1.0 - Math.sqrt(f[0]/g));

        for (int i = 0; i < numberOfObjectives; i++) {
            solution.setObjective(i, f[i]);
        }
    }

}
