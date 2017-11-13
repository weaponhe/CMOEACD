package org.uma.jmetal.problem.multiobjective.CPFT;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/11/13.
 */
public class CPFT4   extends CPFT {

    /**
     * Constructor.
     */
    public CPFT4()  {
        this(10);
    }

    /**
     * @param numberOfVariables Number of variables.
     */
    public CPFT4(int numberOfVariables) {
        super(numberOfVariables);
        setName("CPFT4") ;

    }


    @Override
    protected double[] evaluatePartB(double[] x){
        double[] partB = new double[getNumberOfObjectives()];
        partB[0] = 2.0*x[1] - 1.0;
        partB[1] = Math.pow(Math.abs(2.0*x[1] -1.0), 1.0 - 0.5 * Math.sin(4.0*x[0]*Math.PI));
        partB[2] = 0.0;
        return partB;
    }
}