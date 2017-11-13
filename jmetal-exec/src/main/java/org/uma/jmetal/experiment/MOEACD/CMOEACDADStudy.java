package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/11/19.
 */
public class CMOEACDADStudy extends AbstractMOEACDStudy {
    public CMOEACDADStudy(){
        super("C-MOEACD-AD");
        this.algType = MOEACDBuilder.Variant.CMOEACDAD;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDADMeasure;
    }
}