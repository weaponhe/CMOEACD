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
//optimum −30665.53867178332
public class g04 extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;
    public MaximumConstraintViolation<DoubleSolution> maximumConstraintViolationDegree;


    /**
     * Constructor
     */

    public g04() {
        setNumberOfVariables(5);
        setNumberOfObjectives(1);
        setNumberOfConstraints(6);
        setName("g04");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());


        lowerLimit.add(78.0);
        upperLimit.add(102.0);
        lowerLimit.add(33.0);
        upperLimit.add(45.0);
        for (int i=2;i<getNumberOfVariables();i++) {
            lowerLimit.add(27.0);
            upperLimit.add(45.0);
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

        double f = 5.3578547 * Math.pow(x[2],2.0) + 0.8356891*x[0]*x[4] + 37.293239 * x[0] - 40792.141;

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

        constraint[0] = -( 85.334407 + 0.0056858*x[1]*x[4] + 0.0006262*x[0]*x[3] - 0.0022053*x[2]*x[4] - 92.0);
        constraint[1] = -( - 85.334407 - 0.0056858 * x[1]*x[4] - 0.0006262*x[0]*x[3] + 0.0022053*x[2]*x[4]);
        constraint[2] = -(80.51249 + 0.0071317*x[1]*x[4] + 0.0029955*x[0]*x[1] + 0.0021813*Math.pow(x[2],2.0) - 110.0);
        constraint[3] = -(-80.51249 - 0.0071317*x[1]*x[4] - 0.0029955*x[0]*x[1] - 0.0021813*Math.pow(x[2],2.0) + 90.0);
        constraint[4] = -(9.300961 + 0.0047026*x[2]*x[4] + 0.0012547*x[0]*x[2] + 0.0019085*x[2]*x[3] - 25.0);
        constraint[5] = - (-9.300961 - 0.0047026*x[2]*x[4] - 0.0012547*x[0]*x[2] - 0.0019085*x[2]*x[3] + 20.0);

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

