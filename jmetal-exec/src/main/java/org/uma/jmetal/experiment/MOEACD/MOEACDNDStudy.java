package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/9/21.
 */
public class MOEACDNDStudy extends AbstractMOEACDStudy{
    public MOEACDNDStudy(){
        super("MOEACD-ND");
        this.algType = MOEACDBuilder.Variant.MOEACDND;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDNDMeasure;
    }
}