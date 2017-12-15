package org.uma.jmetal.experiment.data;

import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;

/**
 * Created by weaponhe on 2017/12/12.
 */
public class evaluateHV {
    public static void main(String[] args) throws FileNotFoundException{

        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});

        Point[] hvRefPointList = {
                point10Dmin, point10D,point10D,point10D,point10D,
                point15Dmin,point15D,point15D,point15D,point15D
        };

        String basrDir = "jmetal-data/MOEACDStudy/data";
        String algName = "CMOEAD";
        String proName = "C1_DTLZ1_10D";
        int run = 1;

        String solutionPF = String.format("%s/%s/%s/FUN%d.tsv", basrDir, algName, proName, run);
        WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
        WFGHypervolume hvIndicator = new WFGHypervolume();
        double hv = hvIndicator.evaluate(solutionFront, new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}));
        System.out.println(hv);
    }
}
