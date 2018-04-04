package org.uma.jmetal.experiment.data;

import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weaponhe on 2018/3/19.
 */
public class TrussFrontGenerator {
    public static void main(String[] args) {
        int[] objectives = {3, 5, 8, 10, 15};
        int[][] arrayHs = new int[][]{
                {12},
                {6},
                {3, 2},
                {3, 2},
                {2, 1}
        };
//        double[][] integratedTaus = {{1.0},{1.0},{1.0},{1.0}};

        double[][] integratedTaus = new double[][]{
                {1.0},
                {1.0},
                {1.0, 0.5},
                {1.0, 0.5},
                {1.0, 0.5}
        };
    }

    static public List<Point> generateTruss(List<double[]> uniformDirections) {
        int nObj = uniformDirections.get(0).length;
        List<Point> front = new ArrayList<>();
        for (int i = 0; i < uniformDirections.size(); i++) {
            double[] d = uniformDirections.get(i);
            double best = Double.MAX_VALUE;
            for (int j = 0; j < nObj; j++) {
                double co = 1.0 - d[j] + d[j] / 0.5;
                if (co < 0) {
                    System.out.println("!!!");
                }
                best = Math.min(best, co);
            }
            double a = 1.0 / best;
            double[] p = new double[nObj];
            for (int j = 0; j < nObj; j++) {
                p[j] = d[j] * a;
            }
            Point point = new ArrayPoint(p);
            front.add(point);
        }
        return front;
    }
}

