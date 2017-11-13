package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by Administrator on 2016/11/25.
 */
public class CMOEACDNADStudy  extends AbstractMOEACDStudy {
    public CMOEACDNADStudy(){
        super("C-MOEACD-NAD");
        this.algType = MOEACDBuilder.Variant.CMOEACDNAD;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDNADMeasure;
    }
}