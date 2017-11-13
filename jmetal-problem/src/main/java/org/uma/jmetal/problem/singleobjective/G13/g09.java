package org.uma.jmetal.problem.singleobjective.G13;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.solutionattribute.impl.MaximumConstraintViolation;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/1.
 */
//optimum 680.630057374402
public class g09 extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;
    public MaximumConstraintViolation<DoubleSolution> maximumConstraintViolationDegree;


    /**
     * Constructor
     */
    public g09() {
        setNumberOfVariables(7);
        setNumberOfObjectives(1);
        setNumberOfConstraints(4);
        setName("g09");

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

        double f = Math.pow(x[0] - 10.0,2.0) + 5.0 * Math.pow(x[1] - 12.0,2.0) + Math.pow(x[2],4.0) + 3.0 * Math.pow(x[3] - 11.0,2.0) + 10.0 * Math.pow(x[4],6.0) + 7.0 * Math.pow(x[5],2.0) + Math.pow(x[6],4.0) - 4.0 * x[5]*x[6] - 10.0 * x[5] - 8.0 * x[6];

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

        constraint[0] = -( -127.0 + 2.0 * Math.pow(x[0],2.0) + 3.0 * Math.pow(x[1],4.0) + x[2] + 4.0 * Math.pow(x[3],2.0) + 5.0 * x[4]);
        constraint[1] = -( -282.0 + 7.0 * x[0] + 3.0 * x[1] + 10.0 * Math.pow(x[2],2.0) + x[3] - x[4] );
        constraint[2] = -(-196.0 + 23.0 * x[0] + Math.pow(x[1],2.0) + 6.0 * Math.pow(x[5],2.0) - 8.0 * x[6]);
        constraint[3] = -(4.0 * Math.pow(x[0],2.0) + Math.pow(x[1],2.0) - 3.0 * x[0] * x[1] + 2.0 * Math.pow(x[2],2.0) + 5.0 * x[5] - 11.0 * x[6]);

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

