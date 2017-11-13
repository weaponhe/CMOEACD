package org.uma.jmetal.problem.multiobjective.CPFT;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/11/13.
 */
public class CPFT3   extends CPFT {

    /**
     * Constructor.
     */
    public CPFT3()  {
        this(10);
    }

    /**
     * @param numberOfVariables Number of variables.
     */
    public CPFT3(int numberOfVariables) {
        super(numberOfVariables);
        setName("CPFT3");
    }
    @Override
    protected double[] evaluatePartB(double[] x){
        double[] partB = new double[getNumberOfObjectives()];
        partB[0] = 2.0*x[1] - 1.0;
        partB[1] = Math.pow(Math.abs(2.0*x[1] - 1.0),x[0]+0.5);
        partB[2] = 0.0;
        return partB;
    }
}