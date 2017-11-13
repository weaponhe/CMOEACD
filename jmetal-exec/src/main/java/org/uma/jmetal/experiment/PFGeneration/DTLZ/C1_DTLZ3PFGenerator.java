package org.uma.jmetal.experiment.PFGeneration.DTLZ;

/**
 * Created by X250 on 2017/1/1.
 */
public class C1_DTLZ3PFGenerator extends DTLZ3PFGenerator {
    public C1_DTLZ3PFGenerator(int _popSize,int _nObj,int _H){
        super(_popSize,_nObj,_H);
        setInstanceName("C1_DTLZ3");
    }

    public C1_DTLZ3PFGenerator(int _popSize,int _nObj,int[] _arrayH,double[] _arrayTao){
        super(_popSize,_nObj,_arrayH,_arrayTao);
        setInstanceName("C1_DTLZ3");
    }
}
