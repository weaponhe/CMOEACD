package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by Administrator on 2016/11/25.
 */
public class CMOEACDNAStudy extends AbstractMOEACDStudy {
    public CMOEACDNAStudy(){
        super("C-MOEACD-NA");
        this.algType = MOEACDBuilder.Variant.CMOEACDNA;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDNAMeasure;
    }
}