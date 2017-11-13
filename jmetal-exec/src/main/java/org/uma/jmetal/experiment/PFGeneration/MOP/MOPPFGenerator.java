package org.uma.jmetal.experiment.PFGeneration.MOP;

import org.uma.jmetal.experiment.PFGeneration.PFGenerator;
import org.uma.jmetal.util.Constant;

/**
 * Created by X250 on 2016/7/24.
 */
public abstract class MOPPFGenerator extends PFGenerator {
    public MOPPFGenerator(int _popSize,int _nObj,int _H){
        super(_popSize,_nObj,_H);
    };

    public MOPPFGenerator(int _popSize,int _nObj,int[] _arrayH,double[] _arrayTao){
        super(_popSize,_nObj,_arrayH,_arrayTao);
    };

    protected double[] func1(double[] weight) {
        double t;
        if (weight[1] < Constant.TOLERATION)
            t = 1.0 / Math.sqrt(weight[0]);
        else
            t = (-Math.sqrt(weight[0]) + Math.sqrt(weight[0] + 4.0 * weight[1])) / (2.0 * weight[1]);
        double k = t * t;
        double[] f = new double[nObj];
        for (int j = 0; j<nObj; j++)
            f[j] = k * weight[j];

        return f;
    }

    protected double[] func2(double[] weight) {
        double k;
        if (weight[0] < Constant.TOLERATION)
            k = 1.0 / weight[1];
        else
            k = (-weight[1] + Math.sqrt(weight[1]*weight[1] + 4.0 * weight[0]*weight[0])) / (2.0 * weight[0]*weight[0]);
        double[] f = new double[nObj];
        for (int j = 0; j<nObj; j++)
            f[j] = k * weight[j];
        return f;
    }

    protected double[] func3(double[] weight) {
        double sum = 0.0;
        for (int j = 0; j < nObj; j++)
            sum += weight[j] * weight[j];
        double k = Math.sqrt(1.0 / sum);
        double[] f = new double[nObj];
        for (int j = 0; j < nObj; j++)
            f[j] = k * weight[j];
        return f;
    }
}
