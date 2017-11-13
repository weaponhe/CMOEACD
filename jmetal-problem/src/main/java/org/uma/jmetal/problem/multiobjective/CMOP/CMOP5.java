package org.uma.jmetal.problem.multiobjective.CMOP;

import org.uma.jmetal.solution.DoubleSolution;

/**
 * Created by X250 on 2016/12/29.
 */
public class CMOP5 extends CMOP {

    /**
     * Constructor
     */
    public CMOP5() {
        super(30,2,3,"CMOP5");
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


        f[0] = fun1(x);
        f[1] = fun2(x);

        for (int i = 0; i < numberOfObjectives; i++) {
            solution.setObjective(i, f[i]);
        }
    }

    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables();
        int numberOfObjectives = getNumberOfObjectives();

        double[] x = new double[numberOfVariables];


        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double[] constraint  = calcConstraints3(x,0.51,0.5,20.0);

        for (int i=0;i<getNumberOfConstraints();i++)
            solution.setConstraintViolation(i, constraint[i]);

        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;
        double maximumConstraintViolation = 0.0;
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            if (constraint[i] < 0.0) {
                overallConstraintViolation += constraint[i];
                violatedConstraints++;
                maximumConstraintViolation = Math.min(maximumConstraintViolation,constraint[i]);
            }
        }

        overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
        maximumConstraintViolationDegree.setAttribute(solution,maximumConstraintViolation);
    }

}
