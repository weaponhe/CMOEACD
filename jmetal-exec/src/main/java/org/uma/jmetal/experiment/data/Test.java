package org.uma.jmetal.experiment.data;

import org.uma.jmetal.problem.DoubleBinaryProblem;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.IOException;
import java.util.*;

/**
 * Created by weaponhe on 2018/1/8.
 */
public class Test {
    private static String basrDir = "../jmetal-data/MOEACDStudy/data";

    public static void main(String[] args) throws IOException {


        String[] algorithmList = {
//                "C-MOEAD-SR",
                "C-MOEAD-CDP",
//                "C-MOEAD-ACV",
                "C-NSGAIII",
                "C-MOEADD",
                "C-MOEACD"
        };
        Map<String, double[]> hvMap = new HashMap();

        String problem = "Machining";
        Point hvRefPoint = new ArrayPoint(new double[]{3.6, -3.7, -3.3, -0.3});


        int runs = 5;

        for (int i = 0; i < algorithmList.length; i++) {
            String algorithm = algorithmList[i];
            ArrayList<Double> hvs = new ArrayList<>();
            for (int run = 0; run < runs; run++) {
                String solutionPF = String.format("%s/%s/%s/FUN%d.tsv", basrDir, algorithm, problem, run);
                WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                WFGHypervolume hvIndicator = new WFGHypervolume();
                double hv = hvIndicator.evaluate(solutionFront, hvRefPoint);
                hvs.add(hv);
            }
            hvMap.put(algorithm, new double[]{getBest(hvs), getMedian(hvs), getWorst(hvs)});
        }

        for (int i = 0; i < algorithmList.length; i++) {
            String algorithm = algorithmList[i];
            System.out.print(algorithm + "\t");
        }
        System.out.print("\n");
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < algorithmList.length; i++) {
                String algorithm = algorithmList[i];
                System.out.print(hvMap.get(algorithm)[j] + "\t");
            }
            System.out.print("\n");
        }
    }

    static Double getBest(List<Double> array) {
        Collections.sort(array);
        return array.get(array.size() - 1);
    }

    static Double getMedian(List<Double> array) {
        Collections.sort(array);
        if (array.size() % 2 == 0) {
            int index1 = array.size() / 2 - 1;
            int index2 = array.size() / 2;
            return (array.get(index1) + array.get(index2)) / 2;

        } else {
            return array.get(array.size() / 2);
        }
    }

    static Double getWorst(List<Double> array) {
        Collections.sort(array);
        return array.get(0);
    }
}