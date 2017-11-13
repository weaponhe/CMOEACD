package org.uma.jmetal.problem.multiobjective.CPFT;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/11/13.
 */
public abstract class CPFT extends AbstractDoubleProblem {

    protected double[][] A = {
                                {Math.sin(Math.PI/4.0),Math.cos(Math.PI/4.0),0.0},
                                {-Math.cos(Math.PI/4.0),Math.sin(Math.PI/4.0),0.0},
                                {0.0,0.0,0.0}
                            };


    /**
     * Constructor.
     */
    public CPFT()  {
        this(10);
    }

    /**
     * @param numberOfVariables Number of variables.
     */
    public CPFT(int numberOfVariables) {
        setNumberOfVariables(numberOfVariables) ;
        setNumberOfObjectives(3) ;
        setNumberOfConstraints(0) ;
        setName("CPFT") ;

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;


        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0.0);
            upperLimit.add(1.0);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }


    protected double[] evaluatePartA(double[] x){
        double[] partA = new double[getNumberOfObjectives()];
        partA[0] = x[0];
        partA[1] = x[0];
        partA[2] = -Math.pow(2.0*x[0] - 1.0,3.0);
        return partA;
    }

    protected double[] evaluatePartB(double[] x){
        double[] partB = new double[getNumberOfObjectives()];
        for (int i=0;i<getNumberOfObjectives();i++)
            partB[i] = 1;
        return partB;
    }

    protected double[] evaluateAlpha(double[] partA,double[] partB){
        double[] alpha = new double[getNumberOfObjectives()];
        for (int i=0;i<getNumberOfObjectives();i++) {
            alpha[i] = partA[i];
            for (int j=0;j<getNumberOfObjectives();j++){
                alpha[i] += A[i][j]*partB[j];
            }
        }
        return alpha;
    }

    protected double[] evaluateBeta(double[]x){
        double[] beta = new double[getNumberOfObjectives()];
        double tmp = x[1]*Math.sin(2.0*x[0]*Math.PI);
        for (int j=0;j<getNumberOfObjectives();j++){
            beta[j] = 0.0;
            for (int i=2;i<getNumberOfVariables();i++) {
                if((i+1)%3 == j)
                    beta[j] += Math.pow(x[i] - tmp,2.0);
            }
        }
        return beta;
    }

    /** Evaluate() method */
    @Override
    public void evaluate(DoubleSolution solution) {
        double[] fx = new double[getNumberOfObjectives()];
        double[] x = new double[getNumberOfVariables()];
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double[] alpha = evaluateAlpha(evaluatePartA(x),evaluatePartB(x));
        double[] beta = evaluateBeta(x);
        for (int i=0;i<getNumberOfObjectives();i++){
            fx[i] = alpha[i] + beta[i];
            solution.setObjective(i,fx[i]);
        }
    }
}