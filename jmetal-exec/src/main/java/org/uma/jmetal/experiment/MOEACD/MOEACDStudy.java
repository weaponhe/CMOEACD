package org.uma.jmetal.experiment.MOEACD;


import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/4/26.
 */
public class MOEACDStudy extends AbstractMOEACDStudy{
    public MOEACDStudy(){
        super("MOEACD");
        this.algType = MOEACDBuilder.Variant.MOEACD;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDMeasure;
    }
}
