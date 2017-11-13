package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2016/9/21.
 */
public class MOEACDDStudy extends AbstractMOEACDStudy{
    public MOEACDDStudy(){
        super("MOEACD-D");
        this.algType = MOEACDBuilder.Variant.MOEACDD;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDDMeasure;
    }
}
