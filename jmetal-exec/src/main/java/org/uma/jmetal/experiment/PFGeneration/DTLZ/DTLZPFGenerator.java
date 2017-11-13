package org.uma.jmetal.experiment.PFGeneration.DTLZ;

import org.uma.jmetal.experiment.PFGeneration.PFGenerator;
import org.uma.jmetal.util.Constant;

/**
 * Created by X250 on 2016/7/24.
 */
public abstract class DTLZPFGenerator extends PFGenerator{
    public DTLZPFGenerator(int _popSize,int _nObj,int _H){
        super(_popSize,_nObj,_H);
    };
    public DTLZPFGenerator(int _popSize,int _nObj,int[] _arrayH,double[] _arrayTao){
        super(_popSize,_nObj,_arrayH,_arrayTao);
    };


    protected double[] func1(double[] weight){
        double[] f = new double[nObj];
        for(int j =0;j<nObj;j++)
            f[j] = 0.5 * weight[j];
        return f;
    }

    protected double[] func2(double[] weight){
        double sum = 0.0;
        for(int j =0 ; j < nObj;j++)
            sum += weight[j]*weight[j];
        double k = Math.sqrt(1.0/sum);
        double[] f = new double[nObj];
        for(int j =0 ; j < nObj;j++)
            f[j] = k * weight[j];
        return f;
    }

    protected double[] func3(double[] weight){

        double sum1 = 0.0;
        for(int j =0 ; j<nObj-1;j++)
            sum1 += Math.sqrt(weight[j]);
        double t ;
        if(weight[nObj-1] < Constant.TOLERATION)
            t = 1.0 / sum1;
        else
            t = (-sum1 + Math.sqrt(sum1*sum1 + 4.0 * weight[nObj-1]))/(2.0 * weight[nObj-1]);
        double k = t * t;
        double[] f = new double[nObj];
        for(int j = 0;j<nObj;j++)
            f[j] = k * weight[j];

        return f;
    }
}
