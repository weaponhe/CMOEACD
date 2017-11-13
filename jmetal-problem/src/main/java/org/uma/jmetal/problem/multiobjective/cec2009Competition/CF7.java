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
public class CF7 extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree ;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints ;

    public CF7(){this(10);}
    /**
     * Constructor
     * @param numberOfVariables
     */
    public CF7(int numberOfVariables) {
        setNumberOfVariables(numberOfVariables) ;
        setNumberOfObjectives(2) ;
        setNumberOfConstraints(2) ;
        setName("CF7") ;

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

        int j;
        double sum1, sum2, yj;

        sum1   = sum2   = 0.0;
        for(j = 2; j <= getNumberOfVariables(); j++)
        {
            if (j % 2 == 1)
            {
                yj     = x[j-1] - Math.cos(6.0*Math.PI*x[0] + j*Math.PI/getNumberOfVariables());
                sum1  += 2.0*yj*yj-Math.cos(4.0*Math.PI*yj)+1.0;
            }
            else
            {
                yj     = x[j-1] - Math.sin(6.0*Math.PI*x[0] + j*Math.PI/getNumberOfVariables());
                if (j==2 || j==4)
                    sum2 += yj*yj;
                else
                    sum2  += 2.0*yj*yj-Math.cos(4.0*Math.PI*yj)+1.0;
            }
        }
        fx[0] = x[0]		                 + sum1;
        fx[1] = (1.0 - x[0])*(1.0 - x[0]) + sum2;

        solution.setObjective(0, fx[0]);
        solution.setObjective(1, fx[1]);
    }

    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        double[] constraint = new double[this.getNumberOfConstraints()];

        constraint[0] = solution.getVariableValue(1)-Math.sin(6.0*solution.getVariableValue(0)*Math.PI+2.0*Math.PI/getNumberOfVariables()) - Math.signum((solution.getVariableValue(0)-0.5)*(1.0-solution.getVariableValue(0)))*Math.sqrt(Math.abs((solution.getVariableValue(0)-0.5)*(1.0-solution.getVariableValue(0))));
        constraint[1] = solution.getVariableValue(3)-Math.sin(6.0*solution.getVariableValue(0)*Math.PI+4.0*Math.PI/getNumberOfVariables()) - Math.signum(0.25*Math.sqrt(1-solution.getVariableValue(0))-0.5*(1.0-solution.getVariableValue(0)))*Math.sqrt(Math.abs(0.25*Math.sqrt(1-solution.getVariableValue(0))-0.5*(1.0-solution.getVariableValue(0))));

        solution.setConstraintViolation(0, constraint[0]);
        solution.setConstraintViolation(1,constraint[1]);

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

