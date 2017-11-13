package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/4.
 */
public class CMOEACDAOIIStudy  extends AbstractMOEACDStudy {
    public CMOEACDAOIIStudy(){
        super("C-MOEACD-AOII");
        this.algType = MOEACDBuilder.Variant.CMOEACDAOII;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDAOIIMeasure;
    }
}