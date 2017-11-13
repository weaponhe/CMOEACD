package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/11/18.
 */
public class CMOEACDAStudy extends AbstractMOEACDStudy {
    public CMOEACDAStudy(){
        super("C-MOEACD-A");
        this.algType = MOEACDBuilder.Variant.CMOEACDA;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDAMeasure;
    }
}