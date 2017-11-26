package org.uma.jmetal.experiment.data;

import org.uma.jmetal.util.UniformWeightUtils;

import java.util.List;

/**
 * Created by weaponhe on 2017/11/22.
 */
public class C3ParetoGenerator {
    public static void main(String[] args) {
        int[] arrayH = {12};
        double[] integratedTaus = {1.0};
        int nObj = 3;
        List<double[]> uniformDirections = UniformWeightUtils.generateArrayList(arrayH, integratedTaus, nObj);
        System.out.println(uniformDirections);
    }
}
