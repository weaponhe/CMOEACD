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
//optimum 24.30620906818
public class g07 extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;
    public MaximumConstraintViolation<DoubleSolution> maximumConstraintViolationDegree;


    /**
     * Constructor
     */
    public g07() {
        setNumberOfVariables(10);
        setNumberOfObjectives(1);
        setNumberOfConstraints(8);
        setName("g07");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());


        for (int i=0;i<getNumberOfVariables();i++) {
            lowerLimit.add(-10.0);
            upperLimit.add(10.0);
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

        double f = Math.pow(x[0],2.0) + Math.pow(x[1],2.0) + x[0]*x[1] - 14.0 * x[0] - 16.0 * x[1] + Math.pow(x[2] - 10.0,2.0) + 4.0*Math.pow(x[3] - 5.0,2.0) + Math.pow(x[4] - 3.0,2.0) + 2.0 * Math.pow(x[5] - 1.0,2.0) + 5.0*Math.pow(x[6],2.0) + 7.0 * Math.pow(x[7] - 11.0,2.0) + 2.0 * Math.pow(x[8] - 10.0,2.0) + Math.pow(x[9] - 7.0,2.0) + 45.0;

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

        constraint[0] = -( -105.0 + 4.0*x[0] + 5.0 * x[1] - 3.0 * x[6] + 9.0 *x[7]);
        constraint[1] = -( 10.0 * x[0] - 8.0 * x[1] - 17.0 *x[6]  + 2.0 *x[7] );
        constraint[2] = -( -8.0 * x[0] + 2.0 * x[1] + 5.0 * x[8] - 2.0 *x[9] - 12.0 );
        constraint[3] = -( 3.0 * Math.pow(x[0] - 2.0,2.0) + 4.0 * Math.pow(x[1] - 3.0,2.0) + 2.0 * Math.pow(x[2],2.0) - 7.0 * x[3] - 120.0 );
        constraint[4] = -( 5.0 * Math.pow(x[0],2.0) + 8.0 * x[1] + Math.pow(x[2] - 6.0,2.0) - 2.0 *x[3] - 40.0 );
        constraint[5] = -( Math.pow(x[0],2.0) + 2.0 * Math.pow(x[1] - 2.0,2.0) - 2.0 * x[0] * x[1] + 14.0 * x[4] - 6.0 * x[5] );
        constraint[6] = -(0.5 * Math.pow(x[0] - 8.0,2.0) + 2.0 * Math.pow(x[1] - 4.0,2.0) + 3.0 * Math.pow(x[4],2.0) - x[5] - 30.0);
        constraint[7] = -(-3.0 * x[0] + 6.0 * x[1] + 12.0 * Math.pow(x[8] - 8.0,2.0) - 7.0 * x[9]);

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

