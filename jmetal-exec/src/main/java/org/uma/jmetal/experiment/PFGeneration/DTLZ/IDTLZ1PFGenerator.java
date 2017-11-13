package org.uma.jmetal.experiment.PFGeneration.DTLZ;

import org.uma.jmetal.util.UniformWeightUtils;

import java.util.ArrayList;

/**
 * Created by X250 on 2016/11/13.
 */
public class IDTLZ1PFGenerator extends DTLZPFGenerator{
    public IDTLZ1PFGenerator(int _popSize,int _nObj,int _H){
        super(_popSize,_nObj,_H);
        setInstanceName("IDTLZ1");
    }
    public IDTLZ1PFGenerator(int _popSize,int _nObj,int[] _arrayH,double[] _arrayTao){
        super(_popSize,_nObj,_arrayH,_arrayTao);
        setInstanceName("IDTLZ1");
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
            double[] f = func1(weights.get(i));
            for (int j=0;j<f.length;j++)
                f[j] = 0.5 - f[j];
            pf.add(f);
        }
    }
}
