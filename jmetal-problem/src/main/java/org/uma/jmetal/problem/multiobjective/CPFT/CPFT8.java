package org.uma.jmetal.problem.multiobjective.CPFT;

/**
 * Created by X250 on 2016/11/13.
 */
public class CPFT8 extends CPFT {

    protected int k;
    /**
     * Constructor.
     */
    public CPFT8(int k)  {
        this(10,k);
    }

    /**
     * @param numberOfVariables Number of variables.
     */
    public CPFT8(int numberOfVariables,int k) {
        super(numberOfVariables);
        setName("CPFT8") ;
        this.k = k;

    }
    @Override
    protected double[] evaluatePartA(double[] x){
        double[] partA = new double[getNumberOfObjectives()];
        partA[0] = 2.0*x[0];
        partA[1] = 2.0*x[0];
        double a  = 6.217210009329e-2;
        double b = 2.0/3.0;
        double s = a + 4.0*b*x[0];
        partA[2] = 0.2*Math.cos(3.0*Math.PI*s) - s;
        return partA;
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
            p = 1.0 - 0.5*Math.sin(4.0*x[0]*Math.PI);
        else
            p = 1.0 - 0.5*Math.sin(4.0*(1.0 - x[0])*Math.PI);
        partB[1] = Math.pow(Math.abs(2.0*(k*x[1] - Math.floor(k*x[1])) - 1.0),p);
        partB[2] = 0.0;
        return partB;
    }
}