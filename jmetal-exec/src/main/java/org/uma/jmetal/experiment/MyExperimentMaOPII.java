package org.uma.jmetal.experiment;

import org.uma.jmetal.experiment.MOEACD.*;
import org.uma.jmetal.experiment.dataAnalysis.analysisQuality;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 */
public class MyExperimentMaOPII {
    public void executeDTLZMeasure(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes = 10;
        int neighborhoodSize = 20;
        double neighborhoodSelectionProbability = 0.9;
        int extraExtremeSize = 2;
        double pbi_theta = 5.0;
        boolean strictlyFinalPopulationFlitting = false;
//        int maxRun = 20;

//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new DTLZ1(7,3), new DTLZ1(9, 5), new DTLZ1(12, 8), new DTLZ1(14, 10),new DTLZ1(19, 15),
//                new DTLZ2(12,3), new DTLZ2(14, 5), new DTLZ2(17, 8), new DTLZ2(19, 10),new DTLZ2(24, 15),
//                new DTLZ3(12,3), new DTLZ3(14, 5), new DTLZ3(17, 8), new DTLZ3(19, 10),new DTLZ3(24, 15),
//                new DTLZ4(12,3), new DTLZ4(14, 5), new DTLZ4(17, 8), new DTLZ4(19, 10),new DTLZ4(24, 15),
//                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5), new Convex_DTLZ2(17, 8), new Convex_DTLZ2(19, 10), new Convex_DTLZ2(24, 15)
//        );
//
//        int[] popsList = {
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136
//        };
//        int[] maxIterationsList = {
//                400,600,750,1000,1500,
//                250,350,500,750,1000,
//                1000,1000,1000,1500,2000,
//                600,1000,1250,2000,3000,
//                250,750,2000,4000,4500
//        };
//        int[][] divisionConfigList = {
//                {8},{5}, {3}, {3},{2},
//                {8},{5}, {3}, {3},{2},
//                {8},{5}, {3}, {3},{2},
//                {8},{5}, {3}, {3},{2},
//                {8},{5}, {3}, {3},{2}
//        };
//        double[][] tauConfigList = {
//                {1.0}, {1.0}, {1.0}, {1.0},{1.0},
//                {1.0}, {1.0}, {1.0}, {1.0},{1.0},
//                {1.0}, {1.0}, {1.0}, {1.0},{1.0},
//                {1.0}, {1.0}, {1.0}, {1.0},{1.0},
//                {1.0}, {1.0}, {1.0}, {1.0},{1.0}
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[5050]",
//                frontDir + "DTLZ1.5D.pf[14950]",
//                frontDir + "DTLZ1.8D.pf[31824]",
//                frontDir + "DTLZ1.10D.pf[43758]",
//                frontDir + "DTLZ1.15D.pf[54264]",
//                frontDir + "DTLZ2.3D.pf[5050]",
//                frontDir + "DTLZ2.5D.pf[14950]",
//                frontDir + "DTLZ2.8D.pf[31824]",
//                frontDir + "DTLZ2.10D.pf[43758]",
//                frontDir + "DTLZ2.15D.pf[54264]",
//                frontDir + "DTLZ3.3D.pf[5050]",
//                frontDir + "DTLZ3.5D.pf[14950]",
//                frontDir + "DTLZ3.8D.pf[31824]",
//                frontDir + "DTLZ3.10D.pf[43758]",
//                frontDir + "DTLZ3.15D.pf[54264]",
//                frontDir + "DTLZ4.3D.pf[5050]",
//                frontDir + "DTLZ4.5D.pf[14950]",
//                frontDir + "DTLZ4.8D.pf[31824]",
//                frontDir + "DTLZ4.10D.pf[43758]",
//                frontDir + "DTLZ4.15D.pf[54264]",
//                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//                frontDir + "Convex_DTLZ2.5D.pf[14950]",
//                frontDir + "Convex_DTLZ2.8D.pf[31824]",
//                frontDir + "Convex_DTLZ2.10D.pf[43758]",
//                frontDir + "Convex_DTLZ2.15D.pf[54264]"
//        };
//
////        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
////        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
////        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
////        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
////        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
////        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
////        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
//        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
//        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
//        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
//        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
//        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});
//
//        Point[] hvRefPointList = {
//                point3Dmin,point5Dmin, point8Dmin, point10Dmin,point15Dmin,
//                point3D,point5D, point8D, point10D,point15D,
//                point3D,point5D, point8D, point10D,point15D,
//                point3D,point5D, point8D, point10D,point15D,
//                point3D,point5D, point8D, point10D,point15D
//        };


//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//
//                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5), new Convex_DTLZ2(17, 8), new Convex_DTLZ2(19, 10), new Convex_DTLZ2(24, 15)
//        );
//
//        int[] popsList = {
//
//                109, 251, 165, 286,136
//        };
//        int[] maxIterationsList = {
//
//                250,750,2000,4000,4500
//        };
//        int[][] divisionConfigList = {
//
//                {8},{5}, {3}, {3},{2}
//        };
//        double[][] tauConfigList = {
//
//                {1.0}, {1.0}, {1.0}, {1.0},{1.0}
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//
//                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//                frontDir + "Convex_DTLZ2.5D.pf[14950]",
//                frontDir + "Convex_DTLZ2.8D.pf[31824]",
//                frontDir + "Convex_DTLZ2.10D.pf[43758]",
//                frontDir + "Convex_DTLZ2.15D.pf[54264]"
//        };
//
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
//
//        Point[] hvRefPointList = {
//                point3D,point5D, point8D, point10D,point15D
//        };

//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new DTLZ1(7,3), new DTLZ1(9, 5), new DTLZ1(12, 8),
//                new DTLZ2(12,3), new DTLZ2(14, 5), new DTLZ2(17, 8),
//                new DTLZ3(12,3), new DTLZ3(14, 5), new DTLZ3(17, 8),
//                new DTLZ4(12,3), new DTLZ4(14, 5), new DTLZ4(17, 8),
//                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5), new Convex_DTLZ2(17, 8)
//        );
//
//        int[] popsList = {
//                109, 251, 165,
//                109, 251, 165,
//                109, 251, 165,
//                109, 251, 165,
//                109, 251, 165
//        };
//        int[] maxIterationsList = {
//                400,600,750,
//                250,350,500,
//                1000,1000,1000,
//                600,1000,1250,
//                250,750,2000
//        };
//        int[][] divisionConfigList = {
//                {8},{5}, {3},
//                {8},{5}, {3},
//                {8},{5}, {3},
//                {8},{5}, {3},
//                {8},{5}, {3}
//        };
//        double[][] tauConfigList = {
//                {1.0}, {1.0}, {1.0},
//                {1.0}, {1.0}, {1.0},
//                {1.0}, {1.0}, {1.0},
//                {1.0}, {1.0}, {1.0},
//                {1.0}, {1.0}, {1.0}
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[5050]",
//                frontDir + "DTLZ1.5D.pf[14950]",
//                frontDir + "DTLZ1.8D.pf[31824]",
//                frontDir + "DTLZ2.3D.pf[5050]",
//                frontDir + "DTLZ2.5D.pf[14950]",
//                frontDir + "DTLZ2.8D.pf[31824]",
//                frontDir + "DTLZ3.3D.pf[5050]",
//                frontDir + "DTLZ3.5D.pf[14950]",
//                frontDir + "DTLZ3.8D.pf[31824]",
//                frontDir + "DTLZ4.3D.pf[5050]",
//                frontDir + "DTLZ4.5D.pf[14950]",
//                frontDir + "DTLZ4.8D.pf[31824]",
//                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//                frontDir + "Convex_DTLZ2.5D.pf[14950]",
//                frontDir + "Convex_DTLZ2.8D.pf[31824]"
//        };
//
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
//
//        Point[] hvRefPointList = {
//                point3Dmin,point5Dmin, point8Dmin,
//                point3D,point5D, point8D,
//                point3D,point5D, point8D,
//                point3D,point5D, point8D,
//                point3D,point5D, point8D
//        };

//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new DTLZ2(12,3), new DTLZ2(14, 5), new DTLZ2(17, 8)
//        );
//
//        int[] popsList = {
//                109, 126, 165,
//        };
//        int[] maxIterationsList = {
//                250,350,500
//        };
//        int[][] divisionConfigList = {
//                {8},{4}, {3}
//        };
//        double[][] tauConfigList = {
//                {1.0}, {1.0}, {1.0}
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ2.3D.pf[5050]",
//                frontDir + "DTLZ2.5D.pf[14950]",
//                frontDir + "DTLZ2.8D.pf[31824]"
//        };
//
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
//
//        Point[] hvRefPointList = {
//                point3D,point5D, point8D
//        };


//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new DTLZ1(9, 5),
//                new DTLZ2(14, 5),
//                new DTLZ3(14, 5),
//                new DTLZ4(14, 5),
//                new Convex_DTLZ2(14, 5)
//        );
//
//        int[] popsList = {
//                251,
//                251,
//                251,
//                251,
//                251
//        };
//        int[] maxIterationsList = {
//                600,
//                350,
//                1000,
//                1000,
//                750
//        };
//        int[][] divisionConfigList = {
//                {5},
//                {5},
//                {5},
//                {5},
//                {5}
//        };
//        double[][] tauConfigList = {
//                {1.0},
//                {1.0},
//                {1.0},
//                {1.0},
//                {1.0}
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//
//                frontDir + "DTLZ1.5D.pf[14950]",
//                frontDir + "DTLZ2.5D.pf[14950]",
//                frontDir + "DTLZ3.5D.pf[14950]",
//                frontDir + "DTLZ4.5D.pf[14950]",
//                frontDir + "Convex_DTLZ2.5D.pf[14950]"
//        };
//
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
//
//        Point[] hvRefPointList = {
//                point5Dmin,
//                point5D,
//                point5D,
//                point5D,
//                point5D
//        };
//
        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(14, 10),new DTLZ1(19, 15),
                new DTLZ2(19, 10),new DTLZ2(24, 15),
                new DTLZ3(19, 10),new DTLZ3(24, 15),
                new DTLZ4(19, 10),new DTLZ4(24, 15),
                new Convex_DTLZ2(19, 10), new Convex_DTLZ2(24, 15)
        );

        int[] popsList = {
                286,136,
                286,136,
                286,136,
                286,136,
                286,136
        };
        int[] maxIterationsList = {
                1000,1500,
                750,1000,
                1500,2000,
                2000,3000,
                4000,4500
        };
        int[][] divisionConfigList = {
                {3},{2},
                {3},{2},
                {3},{2},
                {3},{2},
                {3},{2}
        };
        double[][] tauConfigList = {
                {1.0}, {1.0},
                {1.0}, {1.0},
                {1.0}, {1.0},
                {1.0}, {1.0},
                {1.0}, {1.0}
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ1.15D.pf[54264]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ2.15D.pf[54264]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ3.15D.pf[54264]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "DTLZ4.15D.pf[54264]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.15D.pf[54264]"
        };

        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});

        Point[] hvRefPointList = {
                point10Dmin,point15Dmin,
                point10D,point15D,
                point10D,point15D,
                point10D,point15D,
                point10D,point15D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);

//        NSGAIIIStudy nsgaiiiExperiment = new NSGAIIIStudy();
//        try {
//            nsgaiiiExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndexNSGAIII,
//                    mutationDistributionIndex,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
//        MOEADStudy moeadExperiment = new MOEADStudy();
//        try {
//            moeadExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborSize,
//                    neighborhoodSelectionProbability,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
//
//        MOEADDEStudy moeaddeExperiment = new MOEADDEStudy();
//        try {
//            moeaddeExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    f,
//                    mutationDistributionIndex,
//                    neighborSize,
//                    neighborhoodSelectionProbability,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
//
//        MOEADACDStudy moeadacdExperiment = new MOEADACDStudy();
//        try {
//            moeadacdExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    f,
//                    mutationDistributionIndex,
//                    neighborSize,
//                    neighborhoodSelectionProbability,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
//
//        MOEADDStudy moeaddExperiment = new MOEADDStudy();
//        try {
//            moeaddExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborSize,
//                    neighborhoodSelectionProbability,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
//
//        MOEACDStudy moeacdExperiment = new MOEACDStudy();
//        try {
//            moeacdExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    extraExtremeSize,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
//
//
//        MOEACDNStudy moeacdnExperiment = new MOEACDNStudy();
//        try {
//            moeacdnExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    extraExtremeSize,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
//

    }

    public void executeDTLZScale(String baseDir,int maxRun) {
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
//        int maxRun = 20;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new SDTLZ1(7, 3,10.0), new SDTLZ1(9, 5,10.0), new SDTLZ1(12, 8,3.0),new SDTLZ1(14, 10,2.0),new SDTLZ1(19, 15,1.2),
                new SDTLZ2(12, 3,10.0), new SDTLZ2(14, 5,10.0), new SDTLZ2(17, 8,3.0),new SDTLZ2(19, 10,3.0),new SDTLZ2(24, 15,2.0)
        );

        int[] popsList = {
                109, 251, 165,286,136,
                109, 251, 165,286,136
        };


        int[] maxIterationsList = {
                400, 600, 750,1000,1500,
                250, 350, 500,750,1000
        };
        int[][] divisionConfigList = {
                {8},{5}, {3}, {3},{2},
                {8},{5}, {3}, {3},{2}
        };
        double[][] tauConfigList = {
                {1.0}, {1.0}, {1.0},{1.0}, {1.0},
                {1.0}, {1.0}, {1.0},{1.0}, {1.0}
        };

        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});

        Point[] hvRefPointList = {
                point3Dmin,point5Dmin, point8Dmin, point10Dmin,point15Dmin,
                point3D,point5D, point8D, point10D,point15D
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
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
                frontDir + "DTLZ2.15D.pf[54264]"
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);

//        NSGAIIIStudy nsgaiiiExperiment = new NSGAIIIStudy();
//        try {
//            nsgaiiiExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndexNSGAIII,
//                    mutationDistributionIndex,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
//        MOEADNStudy moeadnExperiment = new MOEADNStudy();
//        try {
//            moeadnExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborSize,
//                    neighborhoodSelectionProbability,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
////
//        MOEADDENStudy moeaddenExperiment = new MOEADDENStudy();
//        try {
//            moeaddenExperiment.execute(baseDir,
//                    crossoverProbability,
//                    f,
//                    mutationDistributionIndex,
//                    neighborSize,
//                    neighborhoodSelectionProbability,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
////
//        MOEADACDNStudy moeadacdnExperiment = new MOEADACDNStudy();
//        try {
//            moeadacdnExperiment.execute(baseDir,
//                    crossoverProbability,
//                    f,
//                    mutationDistributionIndex,
//                    neighborSize,
//                    neighborhoodSelectionProbability,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
////
//        MOEADDNStudy moeaddnExperiment = new MOEADDNStudy();
//        try {
//            moeaddnExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborSize,
//                    neighborhoodSelectionProbability,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }
//

    }

    public static void main(String[] args) {
        analysisQuality computor = new analysisQuality();
        String[] algorithmNameList = {
//                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"

        };

        int approximateDim = 10;
        int numOfSample = 1000000;
//
//        int maxRunMOP = 2;
//        MyExperimentMOPII experimentMOP = new MyExperimentMOPII();
//        String baseDirMOP = "E:\\ResultsMOPMeasure000421\\";
//        experimentMOP.executeMOPMeasure(baseDirMOP,maxRunMOP);
//        computor.executeMOPMeasure(baseDirMOP,baseDirMOP,maxRunMOP,algorithmNameList,approximateDim,numOfSample);

        int maxRunMaOP = 2;
        MyExperimentMaOPII experimentMaOP = new MyExperimentMaOPII();
        String baseDirMaOP = "E:\\ResultsMaOPMeasure000570\\";
//        String baseDirMaOP = "E:\\ResultsMaOPMeasureCompare000\\";
        String statDirMaOP = baseDirMaOP+"stat2\\";
        experimentMaOP.executeDTLZMeasure(baseDirMaOP,maxRunMaOP);
//        computor.executeMaOPMeasureIIa(baseDirMaOP,baseDirMaOP,maxRunMaOP,algorithmNameList,approximateDim,numOfSample);
//        computor.executeMaOPIIaFinal(baseDirMaOP,statDirMaOP,maxRunMaOP);
        computor.executeMaOPMeasureIIb(baseDirMaOP,baseDirMaOP,maxRunMaOP,algorithmNameList,approximateDim,numOfSample);
//        computor.executeMaOPIIbFinal(baseDirMaOP,statDirMaOP,maxRunMaOP);

//        int maxRunMaOP = 10;
//        MyExperimentMaOPII experimentMaOP = new MyExperimentMaOPII();
//        String baseDirMaOP = "E:\\ResultsMaOPScale000503\\";
//        experimentMaOP.executeDTLZScale(baseDirMaOP,maxRunMaOP);
    }
}