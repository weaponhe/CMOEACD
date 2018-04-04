package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.Arrays;
import java.util.List;

/**
 * Created by weaponhe on 2018/3/19.
 */
public class ThreeTruss extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

    // defining the lower and upper limits
    public static final Double[] LOWERLIMIT = {0.1e-4, 0.1e-4, 0.1e-4};
    public static final Double[] UPPERLIMIT = {2e-4, 2e-4, 2e-4};


    public double PI = 3.1415926535897932384626433832795;
    double angle_theta = 0.25 * PI;//45度角
    double angle_alpha = PI / 6;//30度角
    double[] gamma = new double[3];
    double[] delta = new double[2];
    double L = 1;//1米
    double F = 20e3;//牛顿
    double E = 200e9;//pa 扬模系数
    double sigma = 200e6;//pa 最大压力
    double[] rho = {7850, 7850, 7850};//密度 kg/m3 千克每立方米
    double[] V = new double[3];//体积
    double weight;//重量
    double K = 15.0;
    double cost;//费用
    double[] N = new double[3];//应力

    public ThreeTruss() {
        setNumberOfVariables(3);
        setNumberOfObjectives(2);
        setNumberOfConstraints(3);
        setName("ThreeTruss");

        List<Double> lowerLimit = Arrays.asList(LOWERLIMIT);
        List<Double> upperLimit = Arrays.asList(UPPERLIMIT);

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

        overallConstraintViolationDegree = new OverallConstraintViolation<DoubleSolution>();
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>();
    }

    /**
     * Evaluate() method
     */
    @Override
    public void evaluate(DoubleSolution solution) {
        double[] fx = new double[3];
        double[] x = new double[getNumberOfVariables()];
        for (int i = 0; i < getNumberOfVariables(); i++)
            x[i] = solution.getVariableValue(i);

        gamma[0] = x[1] + x[0] * Math.pow(Math.sin(angle_theta), 3) + x[2] * Math.pow(Math.sin(angle_alpha), 3);
        gamma[1] = -x[0] * Math.pow(Math.sin(angle_theta), 2) * Math.cos(angle_theta) + x[2] * Math.pow(Math.sin(angle_alpha), 2) * Math.cos(angle_alpha);
        gamma[2] = x[0] * Math.sin(angle_theta) * Math.pow(Math.cos(angle_theta), 2) + x[2] * Math.sin(angle_alpha) * Math.pow(Math.cos(angle_alpha), 2);
        //形变
        delta[0] = (L * F * (gamma[2] - gamma[1])) / (E * (gamma[2] * gamma[0] - gamma[1] * gamma[1]));
        delta[1] = (L * F * (gamma[1] - gamma[0])) / (E * (gamma[1] * gamma[1] - gamma[0] * gamma[2]));

        V[0] = L * x[0] / Math.sin(angle_theta);
        V[1] = L * x[1];
        V[2] = L * x[2] / Math.sin(angle_alpha);

        //质量
        weight = rho[0] * V[0] + rho[1] * V[1] + rho[2] * V[2];

        //三根杆子的反作用力
        N[0] = E / L * (delta[0] * Math.sin(angle_theta) - delta[1] * Math.cos(angle_theta)) * Math.sin(angle_theta);
        N[1] = E / L * delta[0];
        N[2] = E / L * (delta[0] * Math.sin(angle_alpha) + delta[1] * Math.cos(angle_alpha)) * Math.sin(angle_alpha);

        //费用
        cost = K * weight;
        for (int i = 0; i < x.length; i++) {
            double temp;
            if (x[i] >= 0.1 && x[i] < 0.9) {
                temp = 20 + Math.pow(x[i] - 0.1, 2.0) / -0.128;
            } else if (x[i] < 1.5) {
                temp = 3.33 * x[i] + 9.67;
            } else {
                temp = 28 + Math.pow(x[i] - 3.0, 2.0) / -0.180;
            }
            cost += temp;
        }

        fx[0] = 0.25 * delta[0] + 0.75 * delta[1];  //Deformation
        fx[1] = weight;    //weight
        fx[1] = cost;
        solution.setObjective(0, fx[0]);
        solution.setObjective(1, fx[1]);
        solution.setObjective(2, fx[2]);
    }


    /**
     * EvaluateConstraints() method
     */
    public void evaluateConstraints(DoubleSolution solution) {
        double[] constraint = new double[getNumberOfConstraints()];
        double[] x = new double[getNumberOfVariables()];
        for (int i = 0; i < getNumberOfVariables(); i++)
            x[i] = solution.getVariableValue(i);

        gamma[0] = x[1] + x[0] * Math.pow(Math.sin(angle_theta), 3) + x[2] * Math.pow(Math.sin(angle_alpha), 3);
        gamma[1] = -x[0] * Math.pow(Math.sin(angle_theta), 2) * Math.cos(angle_theta) + x[2] * Math.pow(Math.sin(angle_alpha), 2) * Math.cos(angle_alpha);
        gamma[2] = x[0] * Math.sin(angle_theta) * Math.pow(Math.cos(angle_theta), 2) + x[2] * Math.sin(angle_alpha) * Math.pow(Math.cos(angle_alpha), 2);
        //形变
        delta[0] = (L * F * (gamma[2] - gamma[1])) / (E * (gamma[2] * gamma[0] - gamma[1] * gamma[1]));
        delta[1] = (L * F * (gamma[1] - gamma[0])) / (E * (gamma[1] * gamma[1] - gamma[0] * gamma[2]));

        V[0] = L * x[0] / Math.sin(angle_theta);
        V[1] = L * x[1];
        V[2] = L * x[2] / Math.sin(angle_alpha);

        //质量
        weight = rho[0] * V[0] + rho[1] * V[1] + rho[2] * V[2];

        //三根杆子的反作用力
        N[0] = E / L * (delta[0] * Math.sin(angle_theta) - delta[1] * Math.cos(angle_theta)) * Math.sin(angle_theta);
        N[1] = E / L * delta[0];
        N[2] = E / L * (delta[0] * Math.sin(angle_alpha) + delta[1] * Math.cos(angle_alpha)) * Math.sin(angle_alpha);

        constraint[0] = sigma - Math.abs(N[0]);
        constraint[1] = sigma - Math.abs(N[1]);
        constraint[2] = sigma - Math.abs(N[2]);
//        constraint[0] = 10;
//        constraint[1] = 10;
//        constraint[2] = 10;

        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            if (constraint[i] < 0.0) {
                overallConstraintViolation += constraint[i];
                violatedConstraints++;
            }
        }

        solution.setAttribute("overallConstraintViolationDegree", overallConstraintViolation);
        overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
    }
}