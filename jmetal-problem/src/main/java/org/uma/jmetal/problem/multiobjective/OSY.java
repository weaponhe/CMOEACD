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
public class OSY extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

    /**
     * Constructor
     */
    public OSY() {
        setNumberOfVariables(6);
        setNumberOfObjectives(2);
        setNumberOfConstraints(6);
        setName("OSY");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        lowerLimit.add(0.0);
        lowerLimit.add(0.0);
        lowerLimit.add(1.0);
        lowerLimit.add(0.0);
        lowerLimit.add(1.0);
        lowerLimit.add(0.0);
        upperLimit.add(10.0);
        upperLimit.add(10.0);
        upperLimit.add(5.0);
        upperLimit.add(6.0);
        upperLimit.add(5.0);
        upperLimit.add(10.0);


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


        f[0] = -(25.0*Math.pow(x[0] - 2.0,2.0) + Math.pow(x[1] - 2.0,2.0) + Math.pow(x[2] - 1.0,2.0) + Math.pow(x[3] - 4.0,2.0) + Math.pow(x[4] - 1.0,2.0));
        f[1] = 0.0;
        for (int i=0;i<getNumberOfVariables();i++){
            f[1] += Math.pow(x[i],2.0);
        }

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
        constraint[0] = x[0] + x[1] - 2.0;
        constraint[1] = 6.0 - x[0] - x[1];
        constraint[2] = 2.0 - x[1] + x[0];
        constraint[3] = 2.0 - x[0] + 3.0*x[1];
        constraint[4] = 4.0 - Math.pow(x[2] - 3.0,2.0) - x[3];
        constraint[5] = Math.pow(x[4] - 3.0,2.0) + x[5] - 4.0;

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
