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
public class CF8 extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree ;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints ;

    public CF8(){this(10);}
    /**
     * Constructor
     * @param numberOfVariables
     */
    public CF8(int numberOfVariables) {
        setNumberOfVariables(numberOfVariables) ;
        setNumberOfObjectives(3) ;
        setNumberOfConstraints(1) ;
        setName("CF8") ;

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        lowerLimit.add(0.0);
        lowerLimit.add(0.0);
        upperLimit.add(1.0);
        upperLimit.add(1.0);
        for (int i = 2; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-4.0);
            upperLimit.add(4.0);
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

        int j, count1, count2, count3;
        double sum1, sum2, sum3, yj;


        sum1   = sum2   = sum3   = 0.0;
        count1 = count2 = count3 = 0;
        for(j = 3; j <= getNumberOfVariables(); j++)
        {
            yj = x[j-1] - 2.0*x[1]*Math.sin(2.0*Math.PI*x[0]+j*Math.PI/getNumberOfVariables());
            if(j % 3 == 1)
            {
                sum1  += yj*yj;
                count1++;
            }
            else if(j % 3 == 2)
            {
                sum2  += yj*yj;
                count2++;
            }
            else
            {
                sum3  += yj*yj;
                count3++;
            }
        }
        fx[0] = Math.cos(0.5*Math.PI*x[0])*Math.cos(0.5*Math.PI*x[1]) + 2.0*sum1 / (double)count1;
        fx[1] = Math.cos(0.5*Math.PI*x[0])*Math.sin(0.5*Math.PI*x[1]) + 2.0*sum2 / (double)count2;
        fx[2] = Math.sin(0.5*Math.PI*x[0])                  + 2.0*sum3 / (double)count3;


        solution.setObjective(0, fx[0]);
        solution.setObjective(1, fx[1]);
        solution.setObjective(2, fx[2]);
    }

    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        double[] constraint = new double[this.getNumberOfConstraints()];

        double N = 2.0;
        double a = 4.0;
        constraint[0] = (Math.pow(solution.getObjective(0),2.0)+Math.pow(solution.getObjective(1),2.0))/(1-Math.pow(solution.getObjective(2),2.0)) - a*Math.abs(Math.sin(N*Math.PI*((Math.pow(solution.getObjective(0),2.0)-Math.pow(solution.getObjective(1),2.0))/(1-Math.pow(solution.getObjective(2),2.0))+1.0))) - 1.0;

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
