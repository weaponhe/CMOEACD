package org.uma.jmetal.problem.multiobjective.cec2009Competition;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.problem.multiobjective.Machining;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/12/28.
 */
public class CF4 extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree ;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints ;

    public CF4(){this(10);}
    /**
     * Constructor
     * @param numberOfVariables
     */
    public CF4(int numberOfVariables) {
        setNumberOfVariables(numberOfVariables) ;
        setNumberOfObjectives(2) ;
        setNumberOfConstraints(1) ;
        setName("CF4") ;

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
        double sum1, sum2, yj, t;

        sum1   = sum2   = 0.0;
        for(j = 2; j <= getNumberOfVariables(); j++)
        {
            yj = x[j-1] - Math.sin(6.0*Math.PI*x[0] + j*Math.PI/getNumberOfVariables());
            if (j % 2 == 1)
            {
                sum1  += yj*yj;
            }
            else
            {
                if (j==2)
                    sum2 += yj < 1.5-0.75*Math.sqrt(2.0) ? Math.abs(yj) : (0.125+(yj-1)*(yj-1));
                else
                    sum2  += yj*yj;
            }
        }
        fx[0] = x[0]		  + sum1;
        fx[1] = 1.0 - x[0] + sum2;

        solution.setObjective(0, fx[0]);
        solution.setObjective(1, fx[1]);
    }

    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        double[] constraint = new double[this.getNumberOfConstraints()];

        double t	 = solution.getVariableValue(1) - Math.sin(6.0*solution.getVariableValue(0)*Math.PI+2.0*Math.PI/getNumberOfVariables()) - 0.5*solution.getVariableValue(0) + 0.25;

        constraint[0] = Math.signum(t)*Math.abs(t)/(1+Math.exp(4.0*Math.abs(t)));

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
