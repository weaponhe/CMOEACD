package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by X250 on 2016/9/14.
 */
public class UCarSideImpact extends AbstractDoubleProblem {

    // defining the lower and upper limits
    public static final Double [] LOWERLIMIT = {0.5, 0.45, 0.5, 0.5, 0.875, 0.4, 0.4};
    public static final Double [] UPPERLIMIT = {1.5, 1.35, 1.5, 1.5, 2.625, 1.2, 1.2};


    /**
     * Constructor
     */
    public UCarSideImpact() {
        setNumberOfVariables(7);
        setNumberOfObjectives(9);
        setNumberOfConstraints(0);
        setName("UCarSideImpact");

        List<Double> lowerLimit = Arrays.asList(LOWERLIMIT) ;
        List<Double> upperLimit = Arrays.asList(UPPERLIMIT) ;

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

    }

    /** Evaluate() method */
    @Override
    public void evaluate(DoubleSolution solution) {
        double[] fx = new double[solution.getNumberOfObjectives()];
        double[] x = new double[solution.getNumberOfVariables()];
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            x[i] = solution.getVariableValue(i);
        }

        double F = 4.72 - 0.5 * x[3] - 0.19 * x[1] * x[2];
        double Vmbp = 10.58 - 0.674 * x[0] * x[1] - 0.67275 * x[1];
        double Vfd = 16.45 - 0.489 * x[2] * x[6] - 0.843 * x[4] * x[5];
        fx[0] = 1.98 + 4.9 * x[0] + 6.67 * x[1] + 6.98 * x[2] + 4.01 * x[3] + 1.78 * x[4] + 0.00001 * x[5] + 2.73 * x[6];


        //converted by constraint violation
        double[] g = new double[10];
        g[0] = 1.16 - 0.3717*x[1]*x[3] - 0.0092928*x[2];
        g[1] = 0.261 - 0.0159*x[0]*x[1] - 0.06486*x[0] - 0.019*x[1]*x[6] +0.0144*x[2]*x[4] + 0.0154464*x[5]         ;
        g[2] = 0.214 + 0.00817*x[4] - 0.045195*x[0] - 0.0135168*x[0] + 0.03099*x[1]*x[5] - 0.018*x[1]*x[6] + 0.007176*x[2] + 0.023232*x[2] - 0.00364*x[4]*x[5] - 0.018*x[1]*x[1] ;
        g[3] = 0.74 - 0.61*x[1] - 0.031296*x[2] - 0.031872*x[6] + 0.227*x[1]*x[1]      ;
        g[4] = 28.98 + 3.818*x[2] - 4.2*x[0]*x[1] + 1.27296*x[5] - 2.68065*x[6]     ;
        g[5] = 33.86 + 2.95*x[2] - 5.057*x[0]*x[1] - 3.795*x[1] - 3.4431*x[6] + 1.45728       ;
        g[6] = 46.36 - 9.9*x[1] - 4.4505*x[0] ;
        g[7] = F;
        g[8] = Vmbp;
        g[9] = Vfd;

//        fx[1] = Math.max(0.0,g[0] - 1.0);
//        fx[2] = Math.max(0.0,g[1] - 0.32);
//        fx[3] = Math.max(0.0,g[2] - 0.32);
//        fx[4] = Math.max(0.0,g[3] - 0.32);
//        fx[5] = (Math.max(0.0,g[4] - 32.0) + Math.max(0.0,g[5] - 32.0) + Math.max(0.0,g[6] - 32.0))/3.0;
//        fx[6] = Math.max(0.0,g[7] - 4.0);
//        fx[7] = Math.max(0.0,g[8] - 9.9);
//        fx[8] = Math.max(0.0,g[9] - 15.7);
        fx[1] = g[0] - 1.0;
        fx[2] = g[1] - 0.32;
        fx[3] = g[2] - 0.32;
        fx[4] = g[3] - 0.32;
        fx[5] = ((g[4] - 32.0) + (g[5] - 32.0) + (g[6] - 32.0))/3.0;
        fx[6] = g[7] - 4.0;
        fx[7] = g[8] - 9.9;
        fx[8] = g[9] - 15.7;


        for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
            solution.setObjective(i, fx[i]);
        }
    }
}
