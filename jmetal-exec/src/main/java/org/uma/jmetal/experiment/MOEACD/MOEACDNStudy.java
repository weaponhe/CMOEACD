package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/4/26.
 */
public class MOEACDNStudy extends AbstractMOEACDStudy{
    public MOEACDNStudy(){
        super("MOEACD-N");
        this.algType = MOEACDBuilder.Variant.MOEACDN;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDNMeasure;
    }
}