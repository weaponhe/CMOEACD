package org.uma.jmetal.experiment.PFGeneration.MOP;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by X250 on 2016/7/24.
 */
public class MOP4PFGenerator extends MOPPFGenerator{
    public MOP4PFGenerator(int _popSize){
        super(_popSize,2,_popSize/2);
        setInstanceName("MOP4");
    }
    public MOP4PFGenerator(int _popSize,int _H){
        super(_popSize,2,new int[]{_H},new double[]{1.0});
        setInstanceName("MOP4");
    }
    public void generatePF(){
//        weights = uniformWeightsManager.getUniformWeights();
//        popSize = weights.size();

        pf = new ArrayList<>(popSize);
        double delta = 1.0 / (popSize - 1);
        int i = 0;
        List<double[]> tmp = new ArrayList<>(popSize);
        for (i = 0; i<popSize; i++)
        {
            double[] f = new double[nObj];
            f[0] = delta * i;
            f[1] = 1.0 - Math.sqrt(f[0])* Math.cos(2.0 * Math.PI * f[0])* Math.cos(2.0 * Math.PI * f[0]);
            tmp.add(f);
        }
        double t = tmp.get(0)[1];
        pf.add(tmp.get(0));
        for (i = 1; i < popSize; ++i) {
            if (t > tmp.get(i)[1]) {
                pf.add(tmp.get(i));
                t = tmp.get(i)[1];
            }
        }
    }
}
