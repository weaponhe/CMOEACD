package org.uma.jmetal.experiment.UDEA;

import org.uma.jmetal.algorithm.multiobjective.udea.UDEABuilder;

/**
 * Created by X250 on 2017/1/5.
 */
public class CUDEAIIStudy extends UDEAStudy {
    public CUDEAIIStudy(){
        algName = "CUDEA-II";
        algType = UDEABuilder.Variant.CUDEAII;
        measureAlgType = UDEABuilder.Variant.CUDEAIIMeasure;
    }
}
