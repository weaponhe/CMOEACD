package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/7.
 */
public class CMOEACDVIStudy extends AbstractMOEACDStudy {
    public CMOEACDVIStudy(){
        super("C-MOEACD-VI");
        this.algType = MOEACDBuilder.Variant.CMOEACDVI;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDVIMeasure;
    }
}