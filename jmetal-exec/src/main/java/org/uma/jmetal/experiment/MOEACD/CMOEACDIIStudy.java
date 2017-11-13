package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/6.
 */
public class CMOEACDIIStudy extends AbstractMOEACDStudy {
    public CMOEACDIIStudy(){
        super("C-MOEACD-II");
        this.algType = MOEACDBuilder.Variant.CMOEACDII;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDIIMeasure;
    }
}