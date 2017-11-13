package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/2.
 */
public class MOEACDAECVStudy extends MOEACDStudy{
    public MOEACDAECVStudy(){
        this.algName = ("MOEACD-AECV");
        this.algType = MOEACDBuilder.Variant.MOEACDAECV;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDAECVMeasure;
    }
}
