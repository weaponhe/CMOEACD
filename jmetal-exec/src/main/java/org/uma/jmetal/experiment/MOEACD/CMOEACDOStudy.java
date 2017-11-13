package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/10.
 */
public class CMOEACDOStudy  extends AbstractMOEACDStudy {
    public CMOEACDOStudy(){
        super("C-MOEACD-O");
        this.algType = MOEACDBuilder.Variant.CMOEACDO;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDOMeasure;
    }
}