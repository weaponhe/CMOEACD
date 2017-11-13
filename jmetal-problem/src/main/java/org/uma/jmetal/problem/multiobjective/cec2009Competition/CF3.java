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
public class CF3  extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree ;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints ;

    public CF3(){this(10);}
    /**
     * Constructor
     * @param numberOfVariables
     */
    public CF3(int numberOfVariables) {
        setNumberOfVariables(numberOfVariables) ;
        setNumberOfObjectives(2) ;
        setNumberOfConstraints(1) ;
        setName("CF3") ;

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        lowerLimit.add(0.0);
        upperLimit.add(1.0);
        for (int i = 1; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-2.0);
            upperLimit.add(2.0);
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
        double sum1, sum2, prod1, prod2, yj, pj;


        sum1   = sum2   = 0.0;
        count1 = count2 = 0;
        prod1  = prod2  = 1.0;
        for(j = 2; j <= getNumberOfVariables(); j++)
        {
            yj = x[j-1]-Math.sin(6.0*Math.PI*x[0]+j*Math.PI/getNumberOfVariables());
            pj = Math.cos(20.0*yj*Math.PI/Math.sqrt(j+0.0));
            if (j % 2 == 0)
            {
                sum2  += yj*yj;
                prod2 *= pj;
                count2++;
            }
            else
            {
                sum1  += yj*yj;
                prod1 *= pj;
                count1++;
            }
        }

        fx[0] = x[0]	           + 2.0*(4.0*sum1 - 2.0*prod1 + 2.0) / (double)count1;
        fx[1] = 1.0 - x[0]*x[0] + 2.0*(4.0*sum2 - 2.0*prod2 + 2.0) / (double)count2;

        solution.setObjective(0, fx[0]);
        solution.setObjective(1, fx[1]);
    }

    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        double[] constraint = new double[this.getNumberOfConstraints()];

        double N = 2.0;
        double a = 1.0;

        constraint[0]  = solution.getObjective(1) + Math.pow(solution.getObjective(0),2.0) - a*Math.sin(N*Math.PI*(Math.pow(solution.getObjective(0),2.0)-solution.getObjective(1)+1.0)) - 1.0;

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
