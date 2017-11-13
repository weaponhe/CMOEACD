package org.uma.jmetal.problem.singleobjective.G13;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.MaximumConstraintViolation;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/1.
 */
//optimum : -15
public class g01 extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;
    public MaximumConstraintViolation<DoubleSolution> maximumConstraintViolationDegree;

    /**
     * Constructor
     */
    public g01() {
        setNumberOfVariables(13);
        setNumberOfObjectives(1);
        setNumberOfConstraints(9);
        setName("g01");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());


        for (int i=0;i<9;i++) {
            lowerLimit.add(0.0);
            upperLimit.add(1.0);
        }
        for (int i = 9; i < 12; i++) {
            lowerLimit.add(0.0);
            upperLimit.add(100.0);
        }
        lowerLimit.add(0.0);
        upperLimit.add(1.0);

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

        overallConstraintViolationDegree = new OverallConstraintViolation<DoubleSolution>();
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>();
        maximumConstraintViolationDegree = new MaximumConstraintViolation<>();
    }

    /** Evaluate() method */
    @Override
    public void evaluate(DoubleSolution solution)  {
        double[] x = new double[solution.getNumberOfVariables()];
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double f = 0.0;
        for (int i=0;i<4;i++){
            f += x[i];
            f -= Math.pow(x[i],2.0);
        }
        f *= 5.0;
        for (int i=4;i<13;i++){
            f -= x[i];
        }

        solution.setObjective(0,f);
    }

    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables();

        double[] x = new double[numberOfVariables];

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double[] constraint = new double[getNumberOfConstraints()];

        constraint[0] = -(2.0 * x[0] + 2.0 * x[1] + x[9] + x[10] - 10.0);
        constraint[1] = -(2.0 * x[0] + 2.0 * x[2] + x[9] + x[11] - 10.0);
        constraint[2] = -(2.0 * x[1] + 2.0 * x[2] + x[10] + x[11] - 10.0);
        constraint[3] = -(-8.0 * x[0] + x[9]);
        constraint[4] = -(-8.0 * x[1] + x[10]);
        constraint[5] = - (-8.0 * x[2] + x[11]);
        constraint[6] = -(-2.0 * x[3] - x[4] + x[9]);
        constraint[7] = -(-2.0 * x[5] - x[6] + x[10]);
        constraint[8] = -(-2.0 * x[7] - x[8] + x[11]);

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
