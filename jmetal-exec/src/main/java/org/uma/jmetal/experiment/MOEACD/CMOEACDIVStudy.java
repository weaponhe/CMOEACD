package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/6.
 */
public class CMOEACDIVStudy extends AbstractMOEACDStudy {
    public CMOEACDIVStudy(){
        super("C-MOEACD-IV");
        this.algType = MOEACDBuilder.Variant.CMOEACDIV;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDIVMeasure;
    }
}