package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/9/21.
 */
public class CMOEACDNDStudy extends AbstractMOEACDStudy {
    public CMOEACDNDStudy(){
        super("C-MOEACD-ND");
        this.algType = MOEACDBuilder.Variant.CMOEACDND;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDNDMeasure;
    }
}
