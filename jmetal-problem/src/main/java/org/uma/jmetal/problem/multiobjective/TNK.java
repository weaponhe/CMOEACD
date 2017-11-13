package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/12/29.
 */
public class TNK extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

    /**
     * Constructor
     */
    public TNK() {
        setNumberOfVariables(2);
        setNumberOfObjectives(2);
        setNumberOfConstraints(2);
        setName("TNK");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        for (int i=0;i<getNumberOfVariables();i++) {
            lowerLimit.add(0.0);
            upperLimit.add(Math.PI);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

        overallConstraintViolationDegree = new OverallConstraintViolation<DoubleSolution>();
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>();
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


        f[0] = x[0];
        f[1] = x[1];

        for (int i = 0; i < numberOfObjectives; i++) {
            solution.setObjective(i, f[i]);
        }
    }

    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables();

        double[] x = new double[numberOfVariables];

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double[] constraint  = new double[getNumberOfConstraints()];
        constraint[0] = Math.pow(x[0],2.0) + Math.pow(x[1],2.0) - 1.0 - 0.1*Math.cos(16.0*Math.atan2(x[0],x[1]));
        constraint[1] = -(Math.pow(x[0]-0.5,2.0) + Math.pow(x[1] - 0.5,2.0));


        for (int i=0;i<getNumberOfConstraints();i++)
            solution.setConstraintViolation(i, constraint[i]);

        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            if (constraint[i] < 0.0) {
                overallConstraintViolation += constraint[i];
                violatedConstraints++;
            }
        }

        overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
    }
}
