package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/12/21.
 */
public class MOEACDSCVStudy extends AbstractMOEACDStudy{
    public MOEACDSCVStudy(){
        super("MOEACD-SCV");
        this.algType = MOEACDBuilder.Variant.MOEACDSCV;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDSCVMeasure;
    }
}
