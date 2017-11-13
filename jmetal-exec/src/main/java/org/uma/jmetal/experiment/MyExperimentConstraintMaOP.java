package org.uma.jmetal.experiment;

import org.uma.jmetal.experiment.MOEACD.MOEACDStudy;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by X250 on 2016/8/30.
 */
public class MyExperimentConstraintMaOP {

    public void executeConstraintDTLZ(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes = 20;
        int neighborhoodSize = 20;
        double neighborhoodSelectionProbability = 0.9;
        int extraExtremeSize = 2;
        double pbi_theta = 5.0;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(7,3), new DTLZ1(9, 5), new DTLZ1(12, 8), new DTLZ1(14, 10),new DTLZ1(19, 15),
                new DTLZ2(12,3), new DTLZ2(14, 5), new DTLZ2(17, 8), new DTLZ2(19, 10),new DTLZ2(24, 15),
                new DTLZ3(12,3), new DTLZ3(14, 5), new DTLZ3(17, 8), new DTLZ3(19, 10),new DTLZ3(24, 15),
                new DTLZ4(12,3), new DTLZ4(14, 5), new DTLZ4(17, 8), new DTLZ4(19, 10),new DTLZ4(24, 15),
                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5), new Convex_DTLZ2(17, 8), new Convex_DTLZ2(19, 10), new Convex_DTLZ2(24, 15)
        );

        int[] popsList = {
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135
        };
        int[] maxIterationsList = {
                400,600,750,1000,1500,
                250,350,500,750,1000,
                1000,1000,1000,1500,2000,
                600,1000,1250,2000,3000,
                250,750,2000,4000,4500
        };
        int[][] divisionConfigList = {
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12},{6}, {2, 3}, {2, 3},{1,2}
        };
        double[][] tauConfigList = {
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0}
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[91]",
//                frontDir + "DTLZ1.5D.pf[210]",
//                frontDir + "DTLZ1.8D.pf[156]",
//                frontDir + "DTLZ1.10D.pf[275]",
//                frontDir + "DTLZ1.15D.pf[135]",
//                frontDir + "DTLZ2.3D.pf[91]",
//                frontDir + "DTLZ2.5D.pf[210]",
//                frontDir + "DTLZ2.8D.pf[156]",
//                frontDir + "DTLZ2.10D.pf[275]",
//                frontDir + "DTLZ2.15D.pf[135]",
//                frontDir + "DTLZ3.3D.pf[91]",
//                frontDir + "DTLZ3.5D.pf[210]",
//                frontDir + "DTLZ3.8D.pf[156]",
//                frontDir + "DTLZ3.10D.pf[275]",
//                frontDir + "DTLZ3.15D.pf[135]",
//                frontDir + "DTLZ4.3D.pf[91]",
//                frontDir + "DTLZ4.5D.pf[210]",
//                frontDir + "DTLZ4.8D.pf[156]",
//                frontDir + "DTLZ4.10D.pf[275]",
//                frontDir + "DTLZ4.15D.pf[135]",
//                frontDir + "Convex_DTLZ2.3D.pf[91]",
//                frontDir + "Convex_DTLZ2.5D.pf[210]",
//                frontDir + "Convex_DTLZ2.8D.pf[156]",
//                frontDir + "Convex_DTLZ2.10D.pf[275]",
//                frontDir + "Convex_DTLZ2.15D.pf[135]"
//        };

        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[5050]",
                frontDir + "DTLZ1.5D.pf[14950]",
                frontDir + "DTLZ1.8D.pf[31824]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ1.15D.pf[54264]",
                frontDir + "DTLZ2.3D.pf[5050]",
                frontDir + "DTLZ2.5D.pf[14950]",
                frontDir + "DTLZ2.8D.pf[31824]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ2.15D.pf[54264]",
                frontDir + "DTLZ3.3D.pf[5050]",
                frontDir + "DTLZ3.5D.pf[14950]",
                frontDir + "DTLZ3.8D.pf[31824]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ3.15D.pf[54264]",
                frontDir + "DTLZ4.3D.pf[5050]",
                frontDir + "DTLZ4.5D.pf[14950]",
                frontDir + "DTLZ4.8D.pf[31824]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "DTLZ4.15D.pf[54264]",
                frontDir + "Convex_DTLZ2.3D.pf[5050]",
                frontDir + "Convex_DTLZ2.5D.pf[14950]",
                frontDir + "Convex_DTLZ2.8D.pf[31824]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.15D.pf[54264]"
        };

        //        Point point3Dmin = new ArrayPoint(new double[]{0.6, 0.6, 0.6});
//        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.6, 0.6, 0.6, 0.6, 0.6});
//        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1, 1.1, 1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6});
//        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6});
//        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6});
//        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1});

//        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
//        Point point3D = new ArrayPoint(new double[]{1.18, 1.18, 1.18});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{1.18, 1.18, 1.18, 1.18, 1.18});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
//        Point point8D = new ArrayPoint(new double[]{1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point10D = new ArrayPoint(new double[]{1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18, 1.18});

//        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
//        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
//        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});

        Point[] hvRefPointList = {
                point3Dmin,point5Dmin, point8Dmin, point10Dmin,point15Dmin,
                point3D,point5D, point8D, point10D,point15D,
                point3D,point5D, point8D, point10D,point15D,
                point3D,point5D, point8D, point10D,point15D,
                point3D,point5D, point8D, point10D,point15D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);


        MOEACDStudy moeacdExperiment = new MOEACDStudy();
        try {
            moeacdExperiment.executeMeasure(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndex,
                    f,
                    mutationDistributionIndex,
                    neighborhoodSize,
                    neighborhoodSelectionProbability,
                    indicatorEvaluatingTimes,
                    maxRun,
                    problemList,
                    popsList,
                    maxIterationsList,
                    divisionConfigList,
                    tauConfigList,
                    frontFileList,
                    hvRefPointList,
                    indicatorConfig);
        } catch (FileNotFoundException e) {
        }

    }

}
