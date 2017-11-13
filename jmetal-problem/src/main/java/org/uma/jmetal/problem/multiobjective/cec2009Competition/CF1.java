package org.uma.jmetal.problem.multiobjective.cec2009Competition;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/12/28.
 */
public class CF1  extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree ;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints ;

    public CF1(){this(10);}
    /**
     * Constructor
     * @param numberOfVariables
     */
    public CF1(int numberOfVariables) {
        setNumberOfVariables(numberOfVariables) ;
        setNumberOfObjectives(2) ;
        setNumberOfConstraints(1) ;
        setName("CF1") ;

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0.0);
            upperLimit.add(1.0);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

        overallConstraintViolationDegree = new OverallConstraintViolation<DoubleSolution>() ;
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>() ;
    }

    /** Evaluate() method */
    @Override
    public void evaluate(DoubleSolution solution) {
        double[] fx = new double[getNumberOfObjectives()];
        double[] x = new double[getNumberOfVariables()];
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        int j, count1, count2;
        double sum1, sum2, yj;

        sum1   = sum2   = 0.0;
        count1 = count2 = 0;
        for(j = 2; j <= getNumberOfVariables(); j++)
        {
            yj = x[j-1]- Math.pow(x[0],0.5*(1.0+3.0*(j-2.0)/(getNumberOfVariables()-2.0)));
            if (j % 2 == 1)
            {
                sum1  += yj*yj;
                count1++;
            }
            else
            {
                sum2  += yj*yj;
                count2++;
            }
        }

        fx[0] = x[0] + 2.0*sum1 / (double)count1;
        fx[1] = 1.0 - x[0] + 2.0*sum2 / (double)count2;

        solution.setObjective(0, fx[0]);
        solution.setObjective(1, fx[1]);
    }

    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        double[] constraint = new double[this.getNumberOfConstraints()];


        double a = 1.0;
        double N = 10.0;
        constraint[0] = solution.getObjective(1) + solution.getObjective(0) - a*Math.abs(Math.sin(N*Math.PI*(solution.getObjective(0) -solution.getObjective(1)+1.0))) - 1.0;

        solution.setConstraintViolation(0, constraint[0]);

        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            if (constraint[i]<0.0){
                overallConstraintViolation+= constraint[i];
                violatedConstraints++;
            }
        }

        overallConstraintViolationDegree.setAttribute(solution,  overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
    }
}
