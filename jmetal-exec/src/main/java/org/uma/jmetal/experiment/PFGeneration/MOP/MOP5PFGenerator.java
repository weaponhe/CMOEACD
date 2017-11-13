package org.uma.jmetal.experiment.PFGeneration.MOP;

import org.uma.jmetal.util.UniformWeightUtils;

import java.util.ArrayList;

/**
 * Created by X250 on 2016/7/24.
 */
public class MOP5PFGenerator extends MOPPFGenerator{
    public MOP5PFGenerator(int _popSize){
        super(_popSize,2,_popSize/2);
        setInstanceName("MOP5");
    }
    public MOP5PFGenerator(int _popSize,int _H){
        super(_popSize,2,new int[]{_H},new double[]{1.0});
        setInstanceName("MOP5");
    }
    public void generatePF(){
        if (type == UNIFORMTYPE.CENTROID)
            weights = uniformWeightsManager.getUniformWeights();
        else if(type == UNIFORMTYPE.SIMPLEX)
            weights = UniformWeightUtils.generateArrayList(arrayH,arrayTao,nObj);
        popSize = weights.size();
        pf = new ArrayList<>(popSize);
        int i ;
        for(i=0;i<popSize;i++){
            double[] f = func3(weights.get(i));
            pf.add(f);
        }
    }
}
