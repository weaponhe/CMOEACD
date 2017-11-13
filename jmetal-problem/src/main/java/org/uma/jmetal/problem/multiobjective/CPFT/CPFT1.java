package org.uma.jmetal.problem.multiobjective.CPFT;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/11/13.
 */
public class CPFT1  extends CPFT {

    /**
     * Constructor.
     */
    public CPFT1()  {
        this(10);
    }

    /**
     * @param numberOfVariables Number of variables.
     */
    public CPFT1(int numberOfVariables) {
        super(numberOfVariables);
        setName("CPFT1") ;
    }
    @Override
    protected double[] evaluatePartA(double[] x){
        double[] partA = new double[getNumberOfObjectives()];
        partA[0] = x[0];
        partA[1] = x[0] + 0.2*Math.sin(4.0*x[0]*Math.PI);
        partA[2] = -Math.pow(2.0*x[0] - 1.0,3.0);
        return partA;
    }
    @Override
    protected double[] evaluatePartB(double[] x){
        double[] partB = new double[getNumberOfObjectives()];
        partB[0] = 2.0*x[1] - 1.0;
        partB[1] = Math.pow(Math.abs(2.0*x[1] - 1.0),0.5*x[0]+0.5);
        partB[2] = 0.0;
        return partB;
    }
}