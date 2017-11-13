package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/6.
 */
public class CMOEACDVStudy extends AbstractMOEACDStudy {
    public CMOEACDVStudy(){
        super("C-MOEACD-V");
        this.algType = MOEACDBuilder.Variant.CMOEACDV;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDVMeasure;
    }
}