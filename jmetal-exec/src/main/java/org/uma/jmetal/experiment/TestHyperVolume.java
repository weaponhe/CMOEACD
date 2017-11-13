package org.uma.jmetal.experiment;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.mop.MOP1;
import org.uma.jmetal.problem.multiobjective.mop.MOP4;
import org.uma.jmetal.qualityindicator.impl.hypervolume.ApproximateHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.QuickHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/4/13.
 */
public class TestHyperVolume {
    public static void main(String [] argc) {
//        try {
            String frontFile = "E:\\ResultsMaOP1\\MOEACDCAPBI_DTLZ1(10)_275_1000R0.pof";//"E:\\ResultsMaOP1\\MOEACDCAPBI_DTLZ1(8)_156_750R0.pof";// "E:\\ResultsMaOP1\\MOEACDCAPBI_DTLZ1(5)_210_600R4.pof";//"E:\\ResultsMaOP1\\MOEACDCAPBI_DTLZ1(3)_91_400R2.pof";//"E:\\ResultsMaOP1\\DTLZ1.2D.pf[2000]";//"E:\\实验室\\ParetoFront\\ParetoFront\\data\\Convex_DTLZ2.10D.pf[275]";//"jmetal-problem/src/test/resources/pareto_fronts/Convex_DTLZ2.15D.pf[135]";// "E:\\实验室\\ParetoFront\\ParetoFront\\data\\Convex_DTLZ2.8D.pf[156]";//"E://Results2/MOEAD_PBI_DTLZ1(3)_91_400R0.pof";//"E://Results2/MOEAD_PBI_DTLZ1(15)_135_1500R0.pof";//"jmetal-problem/src/test/resources/pareto_fronts/DTLZ1.15D.pf[135]";

            ApproximateHypervolume aHV = new ApproximateHypervolume();
            int nobj = 10;
            int numOfSample = 1000;
            Point refP = new ArrayPoint(nobj);
            Point boundP = new ArrayPoint(nobj);
            for(int i=0;i<nobj;++i){
                refP.setDimensionValue(i,1.0);
                boundP.setDimensionValue(i,0.0);
            }
            aHV.setSamplePoints(numOfSample,refP,boundP);
            List<Point> samplePoints = aHV.getSamplePoints();
            BufferedWriter writer = new DefaultFileOutputContext("E:\\points.dat").getFileWriter();
            try {
                for (int i = 0; i < numOfSample; ++i) {
                    Point p = samplePoints.get(i);
                    for (int j = 0; j < nobj; j++) {
                        writer.write("" + p.getDimensionValue(j) + " ");
                    }
                    writer.newLine();
                }
                writer.close();
            }catch (IOException e){}
//            Front referenceFront = new ArrayFront(frontFile);
//            List<DoubleSolution> solutionList = new ArrayList<>(referenceFront.getNumberOfPoints());
//            Problem problem = new DTLZ1(10,10);
//            for (int i = 0; i < referenceFront.getNumberOfPoints(); i++) {
//                DoubleSolution newSolution = (DoubleSolution) problem.createSolution();
//                for(int j=0;j<referenceFront.getPointDimensions();j++) {
//                    newSolution.setObjective(j,referenceFront.getPoint(i).getDimensionValue(j));
//                }
//                solutionList.add(newSolution);
//            }



//                        Point point15D = new ArrayPoint(new double[]{-2, -2, -2,-2, -2, -2, -2, -2, -2, -2,-2,-2,-2,-2,-2});
//            Point point15D = new ArrayPoint(new double[]{-1, -1});
///            Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2,2,2,2,2,2});
           // Point point15D = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//             Point point15D = new ArrayPoint(new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0,0,0,0,0});
//            Point point15D = new ArrayPoint(new double[]{0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6,0.6,0.6,0.6,0.6,0.6});
//            Point point15D = new ArrayPoint(new double[]{0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5,0.5,0.5,0.5,0.5,0.5});
             //Point point15D = new ArrayPoint(new double[]{0.002, 0.002, 0.002, 0.002, 0.002, 0.002, 0.002, 0.002, 0.002, 0.002, 0.002, 0.002, 0.002,0.002,0.002});
//            Point point15D = new ArrayPoint(new double[]{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2});
//            Point point15D = new ArrayPoint(new double[]{1,1,1,1,1,1,1,1});
            //Point point15D = new ArrayPoint(new double[]{1.18,1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18});
//            Point point15D = new ArrayPoint(new double[]{1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18});





//            Point point15D = new ArrayPoint(new double[]{1,1,1,1,1,1,1,1,1,1});
//
//            WFGHypervolume hvIndicator = new WFGHypervolume();
//            hvIndicator.setMiniming();
////            hvIndicator.setMaximizing();
//            hvIndicator.setReferencePoint(point15D);
//
//            JMetalLogger.logger.info("HV : " + hvIndicator.evaluate(solutionList));

//            QuickHypervolume qhvComputor = new QuickHypervolume();
//            qhvComputor.setMaximizing();
//            qhvComputor.setMiniming();
//            qhvComputor.setReferencePoint(point15D);
//            JMetalLogger.logger.info("QHV : " + qhvComputor.evaluate(solutionList));
//            String frontFile = "E://Results/MOEAD_PBI_DTLZ1(3)_91_400R0.pos";//"jmetal-problem/src/test/resources/pareto_fronts/DTLZ1.15D.pf[135]";
//
//            Front referenceFront = new ArrayFront(frontFile);
//            List<DoubleSolution> solutionList = new ArrayList<>(referenceFront.getNumberOfPoints());
//            Problem problem = new DTLZ1(2,3);
//            for (int i = 0; i < referenceFront.getNumberOfPoints(); i++) {
//                DoubleSolution newSolution = (DoubleSolution) problem.createSolution();
//                for(int j=0;j<referenceFront.getPointDimensions();j++) {
//                    newSolution.setObjective(j,referenceFront.getPoint(i).getDimensionValue(j));
//                }
//                solutionList.add(newSolution);
//            }
//            WFGHypervolume hvIndicator = new WFGHypervolume();
////            Point point3D = new ArrayPoint(new double[]{1, 1, 1});
//            Point point3D = new ArrayPoint(new double[]{0, 0, 0});
//
//            //hvIndicator.setMiniming();
//            hvIndicator.setMaximizing();
//            hvIndicator.setReferencePoint(point3D);
//            JMetalLogger.logger.info("HV : " + hvIndicator.evaluate(solutionList));

//        } catch (FileNotFoundException e) {
//        }
    }
}
