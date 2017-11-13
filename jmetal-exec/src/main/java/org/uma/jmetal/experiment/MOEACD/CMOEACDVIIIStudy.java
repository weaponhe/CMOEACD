package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/7.
 */
public class CMOEACDVIIIStudy extends AbstractMOEACDStudy {
    public CMOEACDVIIIStudy(){
        super("C-MOEACD-VIII");
        this.algType = MOEACDBuilder.Variant.CMOEACDVIII;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDVIIIMeasure;
    }
}