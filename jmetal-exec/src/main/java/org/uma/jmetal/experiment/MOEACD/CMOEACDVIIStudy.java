package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/7.
 */
public class CMOEACDVIIStudy extends AbstractMOEACDStudy {
    public CMOEACDVIIStudy(){
        super("C-MOEACD-VII");
        this.algType = MOEACDBuilder.Variant.CMOEACDVII;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDVIIMeasure;
    }
}