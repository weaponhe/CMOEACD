package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/6.
 */
public class CUCDEAStudy  extends UCDEAStudy{
    public CUCDEAStudy(){
        this.algName = "CU-CDEA";
        this.algType = MOEACDBuilder.Variant.CUCDEA;
        this.measureAlgType = MOEACDBuilder.Variant.CUCDEAMeasure;
    }
}