package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/5.
 */
public class CMOEACDAODStudy  extends AbstractMOEACDStudy {
    public CMOEACDAODStudy(){
        super("C-MOEACD-AOD");
        this.algType = MOEACDBuilder.Variant.CMOEACDAOD;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDAODMeasure;
    }
}