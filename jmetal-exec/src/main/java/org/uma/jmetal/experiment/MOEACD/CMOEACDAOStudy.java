package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;

/**
 * Created by X250 on 2017/1/4.
 */
public class CMOEACDAOStudy extends AbstractMOEACDStudy {
    public CMOEACDAOStudy(){
        super("C-MOEACD-AO");
        this.algType = MOEACDBuilder.Variant.CMOEACDAO;
        this.measureAlgType = MOEACDBuilder.Variant.CMOEACDAOMeasure;
    }
}