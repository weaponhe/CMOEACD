//package org.uma.jmetal.experiment.data;
//
//import org.apache.commons.math3.ml.clustering.DoublePoint;
//import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
//import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
//import org.uma.jmetal.solution.DoubleSolution;
//import org.uma.jmetal.solution.Solution;
//import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
//import org.uma.jmetal.util.front.Front;
//import org.uma.jmetal.util.front.imp.ArrayFront;
//import org.uma.jmetal.util.point.Point;
//import org.uma.jmetal.util.point.impl.ArrayPoint;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by weaponhe on 2018/2/28.
// */
//public class HVCalculator {
//    public static void main(String[] args) throws IOException {
//        String path = "../jmetal-data/MOEACDStudy/data/CMOEACD/CarSideImpact/FUN0.tsv";
//        //
//        Point referencePoint = new ArrayPoint(new double[]{42.768, 4, 12.453});
//        List solutionList = new ArrayList<DoubleSolution>();
//        solutionList.add(new DefaultDoubleSolution())
//        ArrayFront referenceFront = new ArrayFront(solutionList);
//        WFGHypervolume HV = new WFGHypervolume(referenceFront);
//        WfgHypervolumeFront front = new WfgHypervolumeFront(path);
//        HV.evaluate(front);
//    }
//}
