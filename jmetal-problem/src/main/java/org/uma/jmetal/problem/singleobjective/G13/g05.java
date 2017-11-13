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
//optimum 5126.4967140071
public class g05 extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;
    public MaximumConstraintViolation<DoubleSolution> maximumConstraintViolationDegree;

    /**
     * Constructor
     */

    public g05() {
        setNumberOfVariables(4);
        setNumberOfObjectives(1);
        setNumberOfConstraints(5);
        setName("g05");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());


        lowerLimit.add(0.0);
        upperLimit.add(1200.0);
        lowerLimit.add(0.0);
        upperLimit.add(1200.0);
        lowerLimit.add(-0.55);
        upperLimit.add(0.55);
        lowerLimit.add(-0.55);
        upperLimit.add(0.55);


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

        double f = 3.0*x[0] + 0.000001*Math.pow(x[0],3.0) + 2.0 * x[1] + (0.000002/3.0)*Math.pow(x[1],3.0);
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

        constraint[0] = -(-x[3] + x[2] - 0.55);
        constraint[1] = -(-x[2] + x[3] - 0.55);

        constraint[2] = -(Math.abs(1000.0*Math.sin(-x[2] - 0.25) + 1000.0*Math.sin(-x[3] - 0.25) + 894.8 - x[0])  - Constant.EPSILON);//h
        constraint[3] = -(Math.abs(1000.0*Math.sin(x[2] - 0.25) + 1000.0 * Math.sin(x[2] - x[3] - 0.25) + 894.8 - x[1]) - Constant.EPSILON);//h
        constraint[4] = - (Math.abs(1000.0*Math.sin(x[3] - 0.25) + 1000.0*Math.sin(x[3] - x[2] - 0.25) + 1294.8) - Constant.EPSILON);//h

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
