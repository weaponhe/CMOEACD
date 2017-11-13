package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/9/4.
 */
public class CMOEACDStudy extends AbstractMOEACDStudy {
    public CMOEACDStudy(){
        super("C-MOEACD");
        this.algType = MOEACDBuilder.Variant.CMOEACD;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDMeasure;
    }
}
