package org.uma.jmetal.problem.multiobjective.CTP;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.MaximumConstraintViolation;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/12/29.
 */
abstract public class CTP extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public enum CTP_VERSION{CTP_EASY,CTP_MEDIUM,CTP_HARD};
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;
    public MaximumConstraintViolation<DoubleSolution> maximumConstraintViolationDegree;

    private CTP_VERSION ctp_version = CTP_VERSION.CTP_EASY;
    /**
     * Constructor
     */
    public CTP(int numberOfObjectives, int numberOfConstraints, String problemName,CTP_VERSION version) {
        setNumberOfObjectives(numberOfObjectives);
        setNumberOfConstraints(numberOfConstraints);
        setName(problemName);
        ctp_version = version;

        List<Double> lowerLimit = null;
        List<Double> upperLimit = null;
        switch (ctp_version) {
            case CTP_HARD:{
                setNumberOfVariables(10);
                lowerLimit = new ArrayList<>(getNumberOfVariables());
                upperLimit = new ArrayList<>(getNumberOfVariables());

                for (int i = 1; i < getNumberOfVariables(); i++) {
                    lowerLimit.add(0.0);
                    upperLimit.add(1.0);
                }
                break;
            }
            case CTP_MEDIUM: {
                setNumberOfVariables(4);
                lowerLimit = new ArrayList<>(getNumberOfVariables());
                upperLimit = new ArrayList<>(getNumberOfVariables());

                lowerLimit.add(0.0);
                upperLimit.add(1.0);
                for (int i = 1; i < getNumberOfVariables(); i++) {
                    lowerLimit.add(-5.0);
                    upperLimit.add(5.0);
                }
                break;
            }
            default:{
                setNumberOfVariables(1);
                lowerLimit = new ArrayList<>(getNumberOfVariables());
                upperLimit = new ArrayList<>(getNumberOfVariables());

                lowerLimit.add(0.0);
                upperLimit.add(1.0);
            }
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

        overallConstraintViolationDegree = new OverallConstraintViolation<DoubleSolution>();
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>();
        maximumConstraintViolationDegree = new MaximumConstraintViolation<>();
    }

    protected double gFun(double[] x)
    {
        switch (ctp_version) {
            case CTP_HARD:{
                //need to find the right formula
                double g = 31;
                for (int i = 0; i < getNumberOfVariables(); i++) {
                    g += (Math.pow(x[i], 2.0) - 10.0 * Math.cos(4.0 * Math.PI * x[i]));
                }
                return g;
            }
            case CTP_MEDIUM: {
                double g = 31;
                for (int i = 0; i < 3; i++) {
                    g += (Math.pow(x[i], 2.0) - 10.0 * Math.cos(4.0 * Math.PI * x[i]));
                }
                return g;
            }
            default:{
                return 1.0 + x[0];
            }
        }
    }
    protected double[]  constraints2(double[] f, double g,double[] a,double[] b)
    {
        double[] constraints = new double[2];
        for (int i=0;i < getNumberOfConstraints();i++)
            constraints[i]= f[1] - a[i] * Math.exp(-b[i] * f[0]);

        return constraints;
    }
    protected double[] constraint1(double[] f,double g,double theta,double a,double b,double c,double d,double e)
    {
        double[] constraints = new double[1];
        constraints[0] = Math.cos(theta)*(f[1]-e) - Math.sin(theta)*f[0] - a*Math.pow(Math.abs(Math.sin(b*Math.PI*Math.pow(Math.sin(theta)*(f[1] - e)+Math.cos(theta)*f[0],c))),d);
        return constraints;
    }
}
