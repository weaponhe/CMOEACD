package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/6.
 */
public class CUCDEAIIStudy extends UCDEAStudy{
    public CUCDEAIIStudy(){
        this.algName = "CU-CDEA-II";
        this.algType = MOEACDBuilder.Variant.CUCDEAII;
        this.measureAlgType = MOEACDBuilder.Variant.CUCDEAIIMeasure;
    }
}