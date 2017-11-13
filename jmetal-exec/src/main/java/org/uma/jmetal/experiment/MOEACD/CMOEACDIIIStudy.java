package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/6.
 */
public class CMOEACDIIIStudy extends AbstractMOEACDStudy {
    public CMOEACDIIIStudy(){
        super("C-MOEACD-III");
        this.algType = MOEACDBuilder.Variant.CMOEACDIII;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDIIIMeasure;
    }
}