package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/9/21.
 */
public class CMOEACDDStudy extends AbstractMOEACDStudy {
    public CMOEACDDStudy(){
        super("C-MOEACD-D");
        this.algType = MOEACDBuilder.Variant.CMOEACDD;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDDMeasure;
    }
}
