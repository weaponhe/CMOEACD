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
//optimum  7049.24802052867
public class g10 extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;
    public MaximumConstraintViolation<DoubleSolution> maximumConstraintViolationDegree;


    /**
     * Constructor
     */
    public g10() {
        setNumberOfVariables(8);
        setNumberOfObjectives(1);
        setNumberOfConstraints(6);
        setName("g10");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());


        lowerLimit.add(100.0);
        upperLimit.add(10000.0);
        lowerLimit.add(1000.0);
        upperLimit.add(10000.0);
        lowerLimit.add(1000.0);
        upperLimit.add(10000.0);
        for (int i=3;i<getNumberOfVariables();i++) {
            lowerLimit.add(10.0);
            upperLimit.add(1000.0);
        }

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

        double f = x[0] + x[1] + x[2];

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

        constraint[0] = -( -1.0 + 0.0025 * (x[3] + x[5]));
        constraint[1] = -( -1.0 + 0.0025 * (x[4] + x[6] - x[3]) );
        constraint[2] = -( -1.0 + 0.01 * (x[7] - x[4]));
        constraint[3] = -( -x[0]*x[5] + 833.33252 * x[3] + 100.0*x[0] - 83333.333);
        constraint[4] = -(-x[1]*x[6] + 1250.0 * x[4] + x[1] * x[3] - 1250.0 * x[3]);
        constraint[5] = -(-x[2]*x[7] + 1250000.0 + x[2]*x[4] - 2500*x[4]);

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


