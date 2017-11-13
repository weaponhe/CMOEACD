package org.uma.jmetal.problem.multiobjective.MCOP;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/12/29.
 */
abstract public class MCOP extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

    private double[] cx = new double[]{0,1,0,1,2,0,1,2};
    private double[] cy = new double[]{1.5,0.5,2.5,1.5,0.5,3.5,2.5,1.5,0.5};
    private double theta = -0.25*Math.PI;
    private double a2 = 0.1;
    private double b2 = 0.2;
    private int level = 0;
    /**
     * Constructor
     */
    public MCOP(int numberOfVariables, int numberOfObjectives, int numberOfConstraints, String problemName,int level) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(numberOfObjectives);
        setNumberOfConstraints(numberOfConstraints);
        setName(problemName);
        this.level = level;

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());


        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0.0);
            upperLimit.add(1.0);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

        overallConstraintViolationDegree = new OverallConstraintViolation<DoubleSolution>();
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>();
    }

    protected double gFun1(double[] x)
    {
        double g = 0.0;
        for (int i=1;i<getNumberOfVariables();i++){
            g += x[i];
        }
        g = 1.0 + 9.0*g/(getNumberOfVariables() -1);
        return g;
    }

    protected double gFun2(double[] x)
    {
        double g = 0.0;
        for (int i=1;i<getNumberOfVariables();i++){
            g += x[i];
        }
        g = 1.0 + 9.0*Math.pow(g/(getNumberOfVariables()  -1),0.25);
        return g;
    }

    protected double gFun3(double[] x)
    {
        double g = 0.0;
        for (int i=1;i<getNumberOfVariables();i++){
            g += (x[i]*x[i] - 10.0*Math.cos(4.0*Math.PI*x[i]));
        }
        g += (1.0 + 10.0*(getNumberOfVariables() -1 )) ;
        return g;
    }

    protected double[] calcConstraint(double[] f, double cx, double cy, double theta,  double a2, double b2){
        double[] constraints = new double[getNumberOfConstraints()];
        constraints[0] =  Math.pow((f[0] - cx)*Math.cos(theta) - (f[1] - cy)*Math.sin(theta),2.0)/a2 + Math.pow((f[0] - cx)*Math.sin(theta) + (f[1] - cy)*Math.cos(theta), 2.0)/b2 - 1.0;
        return constraints;
    }


    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables();
        int numberOfObjectives = getNumberOfObjectives();

        double[] f = new double[numberOfObjectives];
        double[] x = new double[numberOfVariables];

        for (int i=0;i<numberOfObjectives;i++){
            f[i] = solution.getObjective(i);
        }
        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double[] constraint  = calcConstraint(f,cx[level],cy[level],theta,a2,b2);

        solution.setConstraintViolation(0, constraint[0]);

        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            if (constraint[i] < 0.0) {
                overallConstraintViolation += constraint[i];
                violatedConstraints++;
            }
        }

        overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
    }
}
