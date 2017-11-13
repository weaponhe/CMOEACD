package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/12/26.
 */
public class MOEACDNDEStudy extends MOEACDDEStudy {
    public MOEACDNDEStudy() {
        this.algName = "MOEACD-N-DE";
        this.algType = MOEACDBuilder.Variant.MOEACDNDE;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDNDEMeasure;
    }
}
