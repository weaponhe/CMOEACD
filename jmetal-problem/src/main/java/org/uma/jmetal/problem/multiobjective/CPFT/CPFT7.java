package org.uma.jmetal.problem.multiobjective.CPFT;

/**
 * Created by X250 on 2016/11/13.
 */
public class CPFT7 extends CPFT {

    protected int k;
    /**
     * Constructor.
     */
    public CPFT7(int k)  {
        this(10,k);
    }

    /**
     * @param numberOfVariables Number of variables.
     */
    public CPFT7(int numberOfVariables,int k) {
        super(numberOfVariables);
        setName("CPFT7") ;
        this.k = k;

    }

    @Override
    protected double[] evaluateBeta(double[]x){
        double[] beta = new double[getNumberOfObjectives()];
        for (int j=0;j<getNumberOfObjectives();j++){
            beta[j] = 0.0;
        }
        return beta;
    }
    @Override
    protected double[] evaluatePartB(double[] x){
        double[] partB = new double[getNumberOfObjectives()];
        partB[0] = 2.0*k*x[1];
        double p = 0.0;
        if(Math.floor(k*x[1])%2==0)
            p = 0.5 + x[0];
        else
            p = 1.5 - x[0];
        partB[1] = Math.pow(Math.abs(2.0*(k*x[1] - Math.floor(k*x[1])) - 1.0),p);
        partB[2] = 0.0;
        return partB;
    }
}