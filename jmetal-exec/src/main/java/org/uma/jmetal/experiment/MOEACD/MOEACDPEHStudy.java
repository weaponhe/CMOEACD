package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/12/22.
 */
public class MOEACDPEHStudy extends AbstractMOEACDStudy{
    public MOEACDPEHStudy(){
        super("MOEACD-PEH");
        this.algType = MOEACDBuilder.Variant.MOEACDPEH;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDPEHMeasure;
    }
}
