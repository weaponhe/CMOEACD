package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/12/26.
 */
public class MOEACDNSBXStudy extends MOEACDSBXStudy {
    public MOEACDNSBXStudy() {
        this.algName = "MOEACD-N-SBX";
        this.algType = MOEACDBuilder.Variant.MOEACDNSBX;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDNSBXMeasure;
    }
}
