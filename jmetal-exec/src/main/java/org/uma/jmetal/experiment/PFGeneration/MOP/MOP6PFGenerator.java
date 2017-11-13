package org.uma.jmetal.experiment.PFGeneration.MOP;

import org.uma.jmetal.util.UniformWeightUtils;

import java.util.ArrayList;

/**
 * Created by X250 on 2016/7/24.
 */
public class MOP6PFGenerator extends MOPPFGenerator{
    public MOP6PFGenerator(int _popSize,int _H){
        super(_popSize,3,_H);
        setInstanceName("MOP6");
    }
    public MOP6PFGenerator(int _popSize,int[] _arrayH,double[] _arrayTao){
        super(_popSize,3,_arrayH,_arrayTao);
        setInstanceName("MOP6");
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
            pf.add(weights.get(i));
        }
    }
}
