package org.uma.jmetal.experiment.UDEA;

import org.uma.jmetal.algorithm.multiobjective.udea.UDEA;
import org.uma.jmetal.algorithm.multiobjective.udea.UDEABuilder;

/**
 * Created by X250 on 2017/1/4.
 */
public class CUDEAStudy extends UDEAStudy {
    public CUDEAStudy(){
        algName = "CUDEA";
        algType = UDEABuilder.Variant.CUDEA;
        measureAlgType = UDEABuilder.Variant.CUDEAMeasure;
    }
}
