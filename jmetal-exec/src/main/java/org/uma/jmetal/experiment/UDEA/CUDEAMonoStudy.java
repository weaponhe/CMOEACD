package org.uma.jmetal.experiment.UDEA;

import org.uma.jmetal.algorithm.multiobjective.udea.UDEABuilder;

/**
 * Created by X250 on 2017/1/4.
 */
public class CUDEAMonoStudy extends UDEAMonoStudy {
    public CUDEAMonoStudy() {
        this.algName = "CUDEA";
        this.algType = UDEABuilder.Variant.CUDEA;
        this.measureAlgType = UDEABuilder.Variant.CUDEAMeasure;
    }
}
