package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/12/26.
 */
public class MOEACDNFStudy extends MOEACDFStudy {
    public MOEACDNFStudy() {
        this.algName = "MOEACD-N-F";
        this.algType = MOEACDBuilder.Variant.MOEACDNF;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDNFMeasure;
    }
}
