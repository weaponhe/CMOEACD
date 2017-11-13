package org.uma.jmetal.experiment.PFGeneration.DTLZ;

import org.uma.jmetal.util.UniformWeightUtils;

import java.util.ArrayList;

/**
 * Created by X250 on 2016/7/24.
 */
public class ConvexDTLZ2PFGenerator  extends DTLZPFGenerator{
    public ConvexDTLZ2PFGenerator(int _popSize,int _nObj,int _H){
        super(_popSize,_nObj,_H);
        setInstanceName("Convex_DTLZ2");
    }
    public ConvexDTLZ2PFGenerator(int _popSize,int _nObj,int[] _arrayH,double[] _arrayTao){
        super(_popSize,_nObj,_arrayH,_arrayTao);
        setInstanceName("Convex_DTLZ2");
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