package org.uma.jmetal.problem.multiobjective.CTP;

import org.uma.jmetal.solution.DoubleSolution;

/**
 * Created by X250 on 2016/12/29.
 */
public class CTP4 extends CTP2 {

    /**
     * Constructor
     */
    public CTP4() {
        super();
        setName("CTP4");
    }
    /**
     * Constructor
     */
    public CTP4(CTP_VERSION version) {
        super(version);
        setName("CTP4");
    }
    /**
     * Constructor
     */
    public CTP4(String problemName,CTP_VERSION version) {
        super(problemName,version);
    }

    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables();
        int numberOfObjectives = getNumberOfObjectives();

        double[] f = new double[numberOfObjectives];
        double[] x = new double[numberOfVariables];

        for (int i=0;i<numberOfObjectives;i++){
            f[i] = solution.getObjective(i);
        }
        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double[] constraint  = constraint1(f,gFun(x),-0.2*Math.PI,0.75,10,1,0.5,1);

        solution.setConstraintViolation(0, constraint[0]);

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
