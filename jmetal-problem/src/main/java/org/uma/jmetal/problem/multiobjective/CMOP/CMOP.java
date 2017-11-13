package org.uma.jmetal.problem.multiobjective.CMOP;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.MaximumConstraintViolation;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/12/29.
 */
abstract public class CMOP extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;
    public MaximumConstraintViolation<DoubleSolution> maximumConstraintViolationDegree;

    /**
     * Constructor
     */
    public CMOP(int numberOfVariables,int numberOfObjectives, int numberOfConstraints, String problemName) {
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(numberOfObjectives);
        setNumberOfConstraints(numberOfConstraints);
        setName(problemName);

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
        maximumConstraintViolationDegree = new MaximumConstraintViolation<>();
    }

    protected double gFun1(double[] x)
    {//even
        double g1 = 0.0;
        for (int i=1;i<getNumberOfVariables();i++){
            if(i%2==0){
                g1 += Math.pow(x[i] - Math.sin(0.5*Math.PI*x[0]),2.0);
            }
        }
        return g1;
    }

    protected double gFun2(double[] x)
    {//odd
        double g2 = 0.0;
        for (int i=1;i<getNumberOfVariables();i++){
            if(i%2==1){
                g2 += Math.pow(x[i] - Math.cos(0.5*Math.PI*x[0]),2.0);
            }
        }
        return g2;
    }

    protected double fun1(double[] x){
        return x[0] + gFun1(x);
    }

    protected double fun2(double[] x){
        return  1.0 + Math.pow(x[0],2.0) + gFun2(x);
    }

    protected double fun3(double[] x){
        return 1.0 - Math.sqrt(x[0]) + gFun2(x);
    }

    protected double[] calcConstraints1(double[] x,double a){
        double[] constraints = new double[getNumberOfConstraints()];
        constraints[0] = Math.sin(a*Math.PI*x[0]) - 0.5;
        return constraints;
    }

    protected double[] calcConstraints2(double[] x,double a, double b) {
        double[] constraints = new double[getNumberOfConstraints()];
        constraints[0] = (a - gFun1(x)) * (gFun1(x) - b);
        constraints[1] = (a - gFun2(x)) * (gFun2(x) - b);
        return constraints;
    }

    protected double[] calcConstraints3(double[] x,double a, double b,double c) {
        double[] constraints = new double[getNumberOfConstraints()];
        constraints[0] = (a - gFun1(x)) * (gFun1(x) - b);
        constraints[1] = (a - gFun2(x)) * (gFun2(x) - b);
        constraints[2] = Math.sin(c*Math.PI*x[0]) - 0.5;
        return constraints;
    }

    protected double[] calcConstraints4(double[] f, double[] x, double[] p, double[] q,double a2, double b2, double theta, double c){
        double[] constraints = new double[getNumberOfConstraints()];
        for (int i=0;i<getNumberOfConstraints() -1;i++){
            constraints[i] = Math.pow((f[0] - p[i])*Math.cos(theta) - (f[1] - q[i])*Math.sin(theta),2.0)/a2 + Math.pow((f[0] - p[i])*Math.sin(theta) - (f[1] - q[i])*Math.cos(theta),2.0)/b2;
        }
        constraints[getNumberOfConstraints()-1] = Math.sin(c*Math.PI*x[0]) - 0.5;
        return constraints;
    }

}
