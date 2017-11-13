package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/9/4.
 */
public class CMOEACDNStudy extends  AbstractMOEACDStudy {
    public CMOEACDNStudy() {
        super("C-MOEACD-N");
        this.algType = MOEACDBuilder.Variant.CMOEACDN;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDNMeasure;
    }
}
