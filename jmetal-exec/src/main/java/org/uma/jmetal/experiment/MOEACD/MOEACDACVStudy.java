package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/2.
 */
public class MOEACDACVStudy extends MOEACDStudy{
    public MOEACDACVStudy(){
        this.algName = ("MOEACD-ACV");
        this.algType = MOEACDBuilder.Variant.MOEACDACV;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDACVMeasure;
    }
}
