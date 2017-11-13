package org.uma.jmetal.experiment.UDEA;

import org.uma.jmetal.algorithm.multiobjective.udea.UDEABuilder;

/**
 * Created by X250 on 2017/1/5.
 */
public class CUDEAIIMonoStudy extends UDEAMonoStudy {
    public CUDEAIIMonoStudy() {
        this.algName = "CUDEA-II";
        this.algType = UDEABuilder.Variant.CUDEAII;
        this.measureAlgType = UDEABuilder.Variant.CUDEAIIMeasure;
    }
}