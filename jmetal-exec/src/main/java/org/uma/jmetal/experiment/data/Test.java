package org.uma.jmetal.experiment.data;

import org.uma.jmetal.problem.DoubleBinaryProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by weaponhe on 2018/1/8.
 */
public class Test {
    public static void main(String[] args) {
        List<List<Integer>> list = new ArrayList<>();
        list.add(new ArrayList<Integer>());

        List<Integer> l = list.get(0);
        l.add(1);
        l.add(2);

        List<Integer> s = list.get(0);
        s.set(0, 100);
        System.out.println("asd");

    }

    public static double[] convertDirectionToWeight(double[] refDirection) {
        int length = refDirection.length;
        double[] weights = new double[length];
        double sum = 0;
        int infinityCount = 0;
        for (int i = 0; i < length; i++) {
            weights[i] = 1 / refDirection[i];
            if (weights[i] == Double.POSITIVE_INFINITY) {
                infinityCount++;
            }
            sum += weights[i];
        }
        double[] normlizedWeights = new double[length];
        for (int i = 0; i < length; i++) {
            if (weights[i] == Double.POSITIVE_INFINITY) {
                normlizedWeights[i] = 1.0 / infinityCount;
            } else {
                normlizedWeights[i] = weights[i] / sum;
            }

        }
        return normlizedWeights;
    }
}
