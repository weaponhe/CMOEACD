package org.uma.jmetal.experiment;

import org.uma.jmetal.experiment.MOEACD.*;
import org.uma.jmetal.experiment.dataAnalysis.analysisQuality;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.cdtlz.*;
import org.uma.jmetal.problem.multiobjective.cec2009Competition.*;

import org.uma.jmetal.problem.singleobjective.CEC2005Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by X250 on 2016/9/3.
 */
public class MyExperimentConstraints {
    public void executeDTLZConstraints(String baseDir,int maxRun) {
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
//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new C2_DTLZ2(12,3)
//                ,
//                new ConvexC2_DTLZ2(12,3)
//        );
//        int[] popsList = {
//
//                91,
//                91
//        };
//        int[] maxIterationsList = {
//                250
//                ,
//                250
//        };
//        int[][] divisionConfigList = {
//
//                {12},
//
//                {12}
//        };
//        double[][] tauConfigList = {
//                {1.0},
//                {1.0}
//        };

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new C1_DTLZ1(7,3),
                new C1_DTLZ1(9, 5),
                new C1_DTLZ1(12, 8),
                new C1_DTLZ1(14, 10)
                ,
                new C1_DTLZ1(19, 15)
                ,
                new C1_DTLZ3(12,3),new C1_DTLZ3(14,5),new C1_DTLZ3(17,8),new C1_DTLZ3(19,10),new C1_DTLZ3(24,15)
                ,
                new C2_DTLZ2(12,3)
                ,
                new C2_DTLZ2(14, 5),
                new C2_DTLZ2(17,8),
                new C2_DTLZ2(19, 10),
                new C2_DTLZ2(24,15)
                ,
                new ConvexC2_DTLZ2(12,3)
                ,new ConvexC2_DTLZ2(14,5), new ConvexC2_DTLZ2(17,8),
                new ConvexC2_DTLZ2(19,10),new ConvexC2_DTLZ2(24,15)
                ,
                new C3_DTLZ1(7,3,3), new C3_DTLZ1(9,5,5),new C3_DTLZ1(17,8,8),new C3_DTLZ1(19,10,10),new C3_DTLZ1(24,15,15)
                ,
                new C3_DTLZ4(12,3,3)
                , new C3_DTLZ4(14, 5,5), new C3_DTLZ4(17,8,8), new C3_DTLZ4(19,10,10), new C3_DTLZ4(24,15,15)
        );
        int[] popsList = {
                91,
                210,
                156,
                275
                ,
                135
                ,
                91, 210, 156, 275,135,
                91,
                210,
                156,
                275,
                135
                ,
                91
                , 210, 156,275,135
                ,
                91, 210, 156, 275,135,
                91
                ,210,156,275,135
        };
        int[] maxIterationsList = {
                500,
                600,
                800,
                1000
                ,
                1500
                ,
                1000,1500,2500,3500,5000,
                250
                ,
                350,
                500,
                750,
                1000
                ,
                250
                ,750,1500,2500,3500
                ,
                750,1250,2000,3000,4000
                ,
                750
                ,1250,2000,3000,4000
        };
        int[][] divisionConfigList = {
                {12},
                {6},
                {2, 3},
                {2, 3}
                ,
                {1,2}
                ,
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12}
                ,
                {6},
                {2, 3},
                {2, 3},
                {1,2}
                ,
                {12}
                ,{6}, {2, 3},{2, 3},{1,2}
                ,
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12}
                ,{6}, {2, 3}, {2, 3},{1,2}
        };
        double[][] tauConfigList = {
                {1.0},
                {1.0},
                {0.5, 1.0},
                {0.5, 1.0}
                ,
                {0.5,1.0}
                ,
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0},
                {1.0},
                {0.5, 1.0},
                {0.5, 1.0},
                {0.5,1.0},
                {1.0}
                ,{1.0}, {0.5, 1.0},{0.5, 1.0},{0.5,1.0}
                ,
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}
                ,{1.0},{0.5, 1.0}, {0.5, 1.0},{0.5,1.0}
        };

//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new C2_DTLZ2(12,3)
//                ,
//                new C2_DTLZ2(14, 5),new C2_DTLZ2(17,8),
//                new C2_DTLZ2(19, 10),
//                new C2_DTLZ2(24,15)
//                ,
//                new ConvexC2_DTLZ2(12,3)
//                ,new ConvexC2_DTLZ2(14,5), new ConvexC2_DTLZ2(17,8),
//                new ConvexC2_DTLZ2(19,10),new ConvexC2_DTLZ2(24,15)
//        );
//        int[] popsList = {
//                91,
//                210, 156, 275,135,
//                91
//                ,210,156,275,135
//        };
//        int[] maxIterationsList = {
//                250
//                ,350,500,
//                750,
//                1000
//                ,
//                250
//                ,750,1500,2500,3500
//        };
//        int[][] divisionConfigList = {
//                {12},
//                {6}, {2, 3}, {2, 3},{1,2},
//                {12}
//                ,{6}, {2, 3}, {2, 3},{1,2}
//        };
//        double[][] tauConfigList = {
//                {1.0},
//                {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {1.0}
//                ,{1.0},{0.5, 1.0}, {0.5, 1.0},{0.5,1.0}
//        };

//        int[] popsList = {
//                10011,10011
//        };
//        int[] maxIterationsList = {
//                2000,2000
//        };
//        int[][] divisionConfigList = {
//                {140},{140}
//        };
//        double[][] tauConfigList = {
//                {1.0},
//                {1.0}
//        };
//
//        int[] popsList = {
//                91, 210,
//                91, 210,
//                91, 210,
//                91, 210
//        };
//        int[] maxIterationsList = {
//                500, 600,
//                250,350,
//                750,1250,
//                750,1250
//        };
//        int[][] divisionConfigList = {
//                {12},{6},
//                {12},{6},
//                {12},{6},
//                {12},{6}
//        };
//        double[][] tauConfigList = {
//                {1.0}, {1.0},
//                {1.0}, {1.0},
//                {1.0}, {1.0},
//                {1.0}, {1.0}
//        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[91]",
                frontDir + "DTLZ1.5D.pf[210]",
                frontDir + "DTLZ1.8D.pf[156]",
                frontDir + "DTLZ1.10D.pf[275]",
                frontDir + "DTLZ1.15D.pf[135]",
                frontDir + "DTLZ2.3D.pf[91]",
                frontDir + "DTLZ2.5D.pf[210]",
                frontDir + "DTLZ2.8D.pf[156]",
                frontDir + "DTLZ2.10D.pf[275]",
                frontDir + "DTLZ2.15D.pf[135]",
                frontDir + "DTLZ3.3D.pf[91]",
                frontDir + "DTLZ3.5D.pf[210]",
                frontDir + "DTLZ3.8D.pf[156]",
                frontDir + "DTLZ3.10D.pf[275]",
                frontDir + "DTLZ3.15D.pf[135]",
                frontDir + "DTLZ4.3D.pf[91]",
                frontDir + "DTLZ4.5D.pf[210]",
                frontDir + "DTLZ4.8D.pf[156]",
                frontDir + "DTLZ4.10D.pf[275]",
                frontDir + "DTLZ4.15D.pf[135]",
                frontDir + "Convex_DTLZ2.3D.pf[91]",
                frontDir + "Convex_DTLZ2.5D.pf[210]",
                frontDir + "Convex_DTLZ2.8D.pf[156]",
                frontDir + "Convex_DTLZ2.10D.pf[275]",
                frontDir + "Convex_DTLZ2.15D.pf[135]",
                frontDir + "Convex_DTLZ2.3D.pf[91]",
                frontDir + "Convex_DTLZ2.5D.pf[210]",
                frontDir + "Convex_DTLZ2.8D.pf[156]",
                frontDir + "Convex_DTLZ2.10D.pf[275]",
                frontDir + "Convex_DTLZ2.15D.pf[135]"
        };

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
                point3D,point5D, point8D, point10D,point15D,
                point3D,point5D, point8D, point10D,point15D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);
////

//        CMOEADStudy cmoeadExperiment = new CMOEADStudy();
//        try {
//            cmoeadExperiment.execute(baseDir,
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

//        CMOEADDStudy CmoeaddExperiment = new CMOEADDStudy();
//        try {
//            CmoeaddExperiment.execute(baseDir,
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

//        CMOEADDNStudy CmoeaddnExperiment = new CMOEADDNStudy();
//        try {
//            CmoeaddnExperiment.execute(baseDir,
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
////        }
//
//        CMOEACDStudy CmoeacdExperiment = new CMOEACDStudy();
//        try {
//            CmoeacdExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CMOEACDAStudy CmoeacdAExperiment = new CMOEACDAStudy();
//        try {
//            CmoeacdAExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//
//        CMOEACDDStudy CmoeacdDExperiment = new CMOEACDDStudy();
//        try {
//            CmoeacdDExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
////
//
//        CMOEACDADStudy CmoeacdADExperiment = new CMOEACDADStudy();
//        try {
//            CmoeacdADExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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


//        CMOEACDAIIStudy CmoeacdAIIExperiment = new CMOEACDAIIStudy();
//        try {
//            CmoeacdAIIExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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

//        CMOEACDIIIStudy CmoeacdIIIExperiment = new CMOEACDIIIStudy();
//        try {
//            CmoeacdIIIExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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

//        CMOEACD0Study Cmoeacd0Experiment = new CMOEACD0Study();
//        try {
//            Cmoeacd0Experiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
    }

    public void executeDTLZConstraintsTest(String baseDir,int maxRun) {
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
                new C1_DTLZ1(7,3),
                new C1_DTLZ3(12,3),
                new C2_DTLZ2(12,3),
                new ConvexC2_DTLZ2(12,3),
                new C3_DTLZ1(7,3,3),
                new C3_DTLZ4(12,3,3)

                ,

                new C1_DTLZ1(9, 5),
                new C1_DTLZ3(14,5),
                new C2_DTLZ2(14, 5),
                new ConvexC2_DTLZ2(14,5),
                new C3_DTLZ1(9,5,5),
                new C3_DTLZ4(14, 5,5)
        );
        int[] popsList = {
                91,
                91,
                91,
                91,
                91,
                91

                ,

                210,
                210,
                210,
                210,
                210,
                210

        };
        int[] maxIterationsList = {
                500,
                1000,
                250,
                250,
                750,
                750

                ,

                600,
                1500,
                350,
                750,
                1250,
                1250
        };
        int[][] divisionConfigList = {
                {12},
                {12},
                {12},
                {12},
                {12},
                {12}
//
                ,

                {6},
                {6},
                {6},
                {6},
                {6},
                {6}


        };
        double[][] tauConfigList = {
                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0}

                ,

                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0}

        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[91]",
                frontDir + "DTLZ1.5D.pf[210]",
                frontDir + "DTLZ1.8D.pf[156]",
                frontDir + "DTLZ1.10D.pf[275]",
                frontDir + "DTLZ1.15D.pf[135]",
                frontDir + "DTLZ2.3D.pf[91]",
                frontDir + "DTLZ2.5D.pf[210]",
                frontDir + "DTLZ2.8D.pf[156]",
                frontDir + "DTLZ2.10D.pf[275]",
                frontDir + "DTLZ2.15D.pf[135]",
                frontDir + "DTLZ3.3D.pf[91]",
                frontDir + "DTLZ3.5D.pf[210]",
                frontDir + "DTLZ3.8D.pf[156]",
                frontDir + "DTLZ3.10D.pf[275]",
                frontDir + "DTLZ3.15D.pf[135]",
                frontDir + "DTLZ4.3D.pf[91]",
                frontDir + "DTLZ4.5D.pf[210]",
                frontDir + "DTLZ4.8D.pf[156]",
                frontDir + "DTLZ4.10D.pf[275]",
                frontDir + "DTLZ4.15D.pf[135]",
                frontDir + "Convex_DTLZ2.3D.pf[91]",
                frontDir + "Convex_DTLZ2.5D.pf[210]",
                frontDir + "Convex_DTLZ2.8D.pf[156]",
                frontDir + "Convex_DTLZ2.10D.pf[275]",
                frontDir + "Convex_DTLZ2.15D.pf[135]",
                frontDir + "Convex_DTLZ2.3D.pf[91]",
                frontDir + "Convex_DTLZ2.5D.pf[210]",
                frontDir + "Convex_DTLZ2.8D.pf[156]",
                frontDir + "Convex_DTLZ2.10D.pf[275]",
                frontDir + "Convex_DTLZ2.15D.pf[135]"
        };

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
                point3D,point5D, point8D, point10D,point15D,
                point3D,point5D, point8D, point10D,point15D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();

//        CMOEADStudy cmoeadExperiment = new CMOEADStudy();
//        try {
//            cmoeadExperiment.execute(baseDir,
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

//        CMOEADDStudy CmoeaddExperiment = new CMOEADDStudy();
//        try {
//            CmoeaddExperiment.execute(baseDir,
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

//        CMOEADDNStudy CmoeaddnExperiment = new CMOEADDNStudy();
//        try {
//            CmoeaddnExperiment.execute(baseDir,
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
////        }

//        CMOEACDStudy CmoeacdExperiment = new CMOEACDStudy();
//        try {
//            CmoeacdExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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

        CMOEACDAStudy CmoeacdAExperiment = new CMOEACDAStudy();
        try {
            CmoeacdAExperiment.execute(baseDir,
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

        CMOEACDIIStudy CmoeacdIIExperiment = new CMOEACDIIStudy();
        try {
            CmoeacdIIExperiment.execute(baseDir,
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


        CMOEACDIIIStudy CmoeacdIIIExperiment = new CMOEACDIIIStudy();
        try {
            CmoeacdIIIExperiment.execute(baseDir,
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

        CMOEACDIVStudy CmoeacdIVExperiment = new CMOEACDIVStudy();
        try {
            CmoeacdIVExperiment.execute(baseDir,
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

        CMOEACDVStudy CmoeacdVExperiment = new CMOEACDVStudy();
        try {
            CmoeacdVExperiment.execute(baseDir,
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

//        CMOEACDAOStudy CmoeacdAOExperiment = new CMOEACDAOStudy();
//        try {
//            CmoeacdAOExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
        CMOEACDAOIIStudy CmoeacdAOIIExperiment = new CMOEACDAOIIStudy();
        try {
            CmoeacdAOIIExperiment.execute(baseDir,
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
        CMOEACDAODStudy CmoeacdAODExperiment = new CMOEACDAODStudy();
        try {
            CmoeacdAODExperiment.execute(baseDir,
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
//              CUCDEAStudy CUCDEAExperiment = new CUCDEAStudy();
//        try {
//            CUCDEAExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CUCDEAIIStudy CUCDEAIIExperiment = new CUCDEAIIStudy();
//        try {
//            CUCDEAIIExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CUDEAStudy CUDEAExperiment = new CUDEAStudy();
//        try {
//            CUDEAExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    neighborhoodSelectionProbability,
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
//        CUDEAIIStudy CUDEAIIExperiment = new CUDEAIIStudy();
//        try {
//            CUDEAIIExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    neighborhoodSelectionProbability,
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
//        CMOEACDDStudy CmoeacdDExperiment = new CMOEACDDStudy();
//        try {
//            CmoeacdDExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
////
//
//        CMOEACDADStudy CmoeacdADExperiment = new CMOEACDADStudy();
//        try {
//            CmoeacdADExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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

//        CMOEACDIIStudy CmoeacdIIExperiment = new CMOEACDIIStudy();
//        try {
//            CmoeacdIIExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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

//        CMOEACDAIIStudy CmoeacdAIIExperiment = new CMOEACDAIIStudy();
//        try {
//            CmoeacdAIIExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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

//        CMOEACDIIIStudy CmoeacdIIIExperiment = new CMOEACDIIIStudy();
//        try {
//            CmoeacdIIIExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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

//        CMOEACD0Study Cmoeacd0Experiment = new CMOEACD0Study();
//        try {
//            Cmoeacd0Experiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
    }

    public void executeDTLZConstraintsMeasureTest(String baseDir,int maxRun) {
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
                new C1_DTLZ1(7,3),
                new C1_DTLZ3(12,3),
                new C2_DTLZ2(12,3),
                new ConvexC2_DTLZ2(12,3),
                new C3_DTLZ1(7,3,3),
                new C3_DTLZ4(12,3,3)

//                ,
//
//                new C1_DTLZ1(9, 5),
//                new C1_DTLZ3(14,5),
//                new C2_DTLZ2(14, 5),
//                new ConvexC2_DTLZ2(14,5),
//                new C3_DTLZ1(9,5,5),
//                new C3_DTLZ4(14, 5,5)
        );
        int[] popsList = {
                91,
                91,
                91,
                91,
                91,
                91

//                ,
//
//                210,
//                210,
//                210,
//                210,
//                210,
//                210

        };
        int[] maxIterationsList = {
                500,
                1000,
                250,
                250,
                750,
                750

//                ,
//
//                600,
//                1500,
//                350,
//                750,
//                1250,
//                1250
        };
        int[][] divisionConfigList = {
                {12},
                {12},
                {12},
                {12},
                {12},
                {12}
//
//                ,
//
//                {6},
//                {6},
//                {6},
//                {6},
//                {6},
//                {6}
//

        };
        double[][] tauConfigList = {
                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0}

//                ,
//
//                {1.0},
//                {1.0},
//                {1.0},
//                {1.0},
//                {1.0},
//                {1.0}

        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[91]",
                frontDir + "DTLZ1.5D.pf[210]",
                frontDir + "DTLZ1.8D.pf[156]",
                frontDir + "DTLZ1.10D.pf[275]",
                frontDir + "DTLZ1.15D.pf[135]",
                frontDir + "DTLZ2.3D.pf[91]",
                frontDir + "DTLZ2.5D.pf[210]",
                frontDir + "DTLZ2.8D.pf[156]",
                frontDir + "DTLZ2.10D.pf[275]",
                frontDir + "DTLZ2.15D.pf[135]",
                frontDir + "DTLZ3.3D.pf[91]",
                frontDir + "DTLZ3.5D.pf[210]",
                frontDir + "DTLZ3.8D.pf[156]",
                frontDir + "DTLZ3.10D.pf[275]",
                frontDir + "DTLZ3.15D.pf[135]",
                frontDir + "DTLZ4.3D.pf[91]",
                frontDir + "DTLZ4.5D.pf[210]",
                frontDir + "DTLZ4.8D.pf[156]",
                frontDir + "DTLZ4.10D.pf[275]",
                frontDir + "DTLZ4.15D.pf[135]",
                frontDir + "Convex_DTLZ2.3D.pf[91]",
                frontDir + "Convex_DTLZ2.5D.pf[210]",
                frontDir + "Convex_DTLZ2.8D.pf[156]",
                frontDir + "Convex_DTLZ2.10D.pf[275]",
                frontDir + "Convex_DTLZ2.15D.pf[135]",
                frontDir + "Convex_DTLZ2.3D.pf[91]",
                frontDir + "Convex_DTLZ2.5D.pf[210]",
                frontDir + "Convex_DTLZ2.8D.pf[156]",
                frontDir + "Convex_DTLZ2.10D.pf[275]",
                frontDir + "Convex_DTLZ2.15D.pf[135]"
        };

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
                point3D,point5D, point8D, point10D,point15D,
                point3D,point5D, point8D, point10D,point15D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();

//        CMOEADStudy cmoeadExperiment = new CMOEADStudy();
//        try {
//            cmoeadExperiment.executeMeasure(baseDir,
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
//
//        CMOEADDStudy CmoeaddExperiment = new CMOEADDStudy();
//        try {
//            CmoeaddExperiment.executeMeasure(baseDir,
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
//        CMOEACDStudy CmoeacdExperiment = new CMOEACDStudy();
//        try {
//            CmoeacdExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CMOEACDIIStudy CmoeacdIIExperiment = new CMOEACDIIStudy();
//        try {
//            CmoeacdIIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//
//        CMOEACDIIIStudy CmoeacdIIIExperiment = new CMOEACDIIIStudy();
//        try {
//            CmoeacdIIIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CMOEACDIVStudy CmoeacdIVExperiment = new CMOEACDIVStudy();
//        try {
//            CmoeacdIVExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CMOEACDVStudy CmoeacdVExperiment = new CMOEACDVStudy();
//        try {
//            CmoeacdVExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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

//        CMOEACDVIStudy CmoeacdVIExperiment = new CMOEACDVIStudy();
//        try {
//            CmoeacdVIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//
//        CMOEACDVIIStudy CmoeacdVIIExperiment = new CMOEACDVIIStudy();
//        try {
//            CmoeacdVIIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//
//        CMOEACDVIIIStudy CmoeacdVIIIExperiment = new CMOEACDVIIIStudy();
//        try {
//            CmoeacdVIIIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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

//        CMOEACDAStudy CmoeacdAExperiment = new CMOEACDAStudy();
//        try {
//            CmoeacdAExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CMOEACDAIIStudy CmoeacdAIIExperiment = new CMOEACDAIIStudy();
//        try {
//            CmoeacdAIIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//
//        CMOEACDAIIIStudy CmoeacdAIIIExperiment = new CMOEACDAIIIStudy();
//        try {
//            CmoeacdAIIIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CMOEACDAIVStudy CmoeacdAIVExperiment = new CMOEACDAIVStudy();
//        try {
//            CmoeacdAIVExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CMOEACDAVStudy CmoeacdAVExperiment = new CMOEACDAVStudy();
//        try {
//            CmoeacdAVExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
        CMOEACDAOStudy CmoeacdAOExperiment = new CMOEACDAOStudy();
        try {
            CmoeacdAOExperiment.executeMeasure(baseDir,
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
//
//        CMOEACDAOIIStudy CmoeacdAOIIExperiment = new CMOEACDAOIIStudy();
//        try {
//            CmoeacdAOIIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
        CMOEACDAODStudy CmoeacdAODExperiment = new CMOEACDAODStudy();
        try {
            CmoeacdAODExperiment.executeMeasure(baseDir,
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

//        CUCDEAStudy CUCDEAExperiment = new CUCDEAStudy();
//        try {
//            CUCDEAExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CUCDEAIIStudy CUCDEAIIExperiment = new CUCDEAIIStudy();
//        try {
//            CUCDEAIIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//
//        CMOEACDDStudy CmoeacdDExperiment = new CMOEACDDStudy();
//        try {
//            CmoeacdDExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
////
//
//        CMOEACDADStudy CmoeacdADExperiment = new CMOEACDADStudy();
//        try {
//            CmoeacdADExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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


    public void executeCFConstraintsMeasure(String baseDir,int maxRun) {
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
                new CF1(),
                new CF2(),
                new CF3(),
                new CF4(),
                new CF5(),
                new CF6(),
                new CF7(),
                new CF8(),
                new CF9(),
                new CF10()
        );
        int[] popsList = {
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                300,
                300,
                300
        };
        int[] maxIterationsList = {
                1000,
                1000,
                1000,
                1000,
                1000,
                1000,
                1000,
                1000,
                1000,
                1000
        };
        int[][] divisionConfigList = {
                {99},
                {99},
                {99},
                {99},
                {99},
                {99},
                {99},
                {23},
                {23},
                {23}
        };
        double[][] tauConfigList = {
                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0},
                {1.0}

        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "CF1.2D.pf",
                frontDir + "CF2.2D.pf",
                frontDir + "CF3.2D.pf",
                frontDir + "CF4.2D.pf",
                frontDir + "CF5.2D.pf",
                frontDir + "CF6.2D.pf",
                frontDir + "CF7.2D.pf",
                frontDir + "CF8.3D.pf",
                frontDir + "CF9.3D.pf",
                frontDir + "CF10.3D.pf"
        };


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
                point3D,point5D, point8D, point10D,point15D,
                point3D,point5D, point8D, point10D,point15D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();

        CMOEADStudy cmoeadExperiment = new CMOEADStudy();
        try {
            cmoeadExperiment.executeMeasure(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndex,
                    mutationDistributionIndex,
                    neighborSize,
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


        CMOEADDStudy CmoeaddExperiment = new CMOEADDStudy();
        try {
            CmoeaddExperiment.executeMeasure(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndex,
                    mutationDistributionIndex,
                    neighborSize,
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

        CMOEACDStudy CmoeacdExperiment = new CMOEACDStudy();
        try {
            CmoeacdExperiment.executeMeasure(baseDir,
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

        CMOEACDAStudy CmoeacdAExperiment = new CMOEACDAStudy();
        try {
            CmoeacdAExperiment.executeMeasure(baseDir,
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
//

//        CMOEACDDStudy CmoeacdDExperiment = new CMOEACDDStudy();
//        try {
//            CmoeacdDExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
////
//
//        CMOEACDADStudy CmoeacdADExperiment = new CMOEACDADStudy();
//        try {
//            CmoeacdADExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CMOEACDIIStudy CmoeacdIIExperiment = new CMOEACDIIStudy();
//        try {
//            CmoeacdIIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CMOEACDAIIStudy CmoeacdAIIExperiment = new CMOEACDAIIStudy();
//        try {
//            CmoeacdAIIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//
//        CMOEACDIIIStudy CmoeacdIIIExperiment = new CMOEACDIIIStudy();
//        try {
//            CmoeacdIIIExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
//        CMOEACD0Study Cmoeacd0Experiment = new CMOEACD0Study();
//        try {
//            Cmoeacd0Experiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
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
    }

    public void executeG13Measure(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 5;
        int neighborhoodSize = 5;
        double neighborhoodSelectionProbability = 0.9;
        double pbi_theta = 5.0;

        List<Problem<DoubleSolution>> problemList = new ArrayList<>();
        int[] D = new int[]{10,30,50};
        int problemIDMin = 13;
        int problemIDMax = 17;
        //12 和 25 有问题
        for (int i=0;i<= problemIDMax  -problemIDMin;i++){
            for (int j=0;j<D.length;j++) {
                int problemID = problemIDMin+i;
                Problem<DoubleSolution> problem = new CEC2005Problem(problemID, D[j]);
                problem.setName("CEC" + problemID + "_" + D[j]);
                problemList.add(problem);
            }
        };

        int[] popsList = new int[problemList.size()];
        for (int i = 0;i<problemList.size();i++) {
            popsList[i] = 50;
        };

        int[] maxIterationsList = new int[problemList.size()];
        int c  = 0;
        for (int i=0;i<=problemIDMax - problemIDMin;i++){
            for (int j=0;j<D.length;j++){
                maxIterationsList[c] = D[j]*10000/popsList[c];
                c++;
            }
        };

        double[] allOptimum = new double[]{
                -450.0, //CEC1
                -450.0, //CEC2
                -450.0, //CEC3
                -450.0, //CEC4
                -310.0, //CEC5
                390.0,  //CEC6
                -180.0, //CEC7
                -140.0, //CEC8
                -330.0, //CEC9
                -330.0, //CEC10
                90.0,   //CEC11
                -460.0, //CEC12
                -130.0, //CEC13
                -300.0, //CEC14
                120.0, //CEC15
                120.0,  //CEC16
                120.0,  //CEC17
                10.0,   //CEC18
                10.0,   //CEC19
                10.0,   //CEC20
                360.0,  //CEC21
                360.0,  //CEC22
                360.0,  //CEC23
                260.0,  //CEC24
                260.0   //CEC25
        };

        double[] optimumList = new double[problemList.size()];
        c = 0;
        for (int i=0;i<problemIDMax - problemIDMin;i++){
            int problemID = problemIDMin + i;
            for (int j=0;j<D.length;j++){
                optimumList[c++] = allOptimum[problemID];
            }
        }
//

//        MonoDEStudy monoDEExperiment = new MonoDEStudy();
//        try {
//            monoDEExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    f,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        }catch (FileNotFoundException e){
//        }
    }


    public static void main(String[] argv){
        analysisQuality computor = new analysisQuality();

        String[] algorithmNameList = {
//                "CMOEAD_PBI",
//                "CMOEADD_PBI"
//                ,
//                "C-MOEACD"
//                ,
//                "C-MOEACD-A"
//                , "C-MOEACD-AII"
//                , "C-MOEACD-AIII"
//                , "C-MOEACD-AIV"
//                , "C-MOEACD-AV"
//                ,
////                "C-MOEACD-AO",
//                "C-MOEACD-AOII"
//                ,
//                "C-MOEACD-AOD"
//                ,
//                "C-MOEACD-D",
//                "C-MOEACD-AD"
//                ,
//                "C-MOEACD-II"
//                ,
//                "C-MOEACD-A-II"
//                "C-MOEACD-III"
//                "C-MOEACD-0"
//                "CUDEA",
//                "CUDEA-II"
//                "CU-CDEA"
//                "CU-CDEA-II"

                "C-MOEACD"
                , "C-MOEACD-II"
                , "C-MOEACD-III"
                , "C-MOEACD-IV"
                , "C-MOEACD-V",
                "C-MOEACD-VI",
                "C-MOEACD-VII",
                "C-MOEACD-VIII",
                "C-MOEACD-A",
                "C-MOEACD-AO",
                "C-MOEACD-AOD"
        };

        int approximateDim = 10;
        int numOfSample = 1000000;

        MyExperimentConstraints experimentConstraints = new MyExperimentConstraints();

        int maxRunConstraints = 20;

        String baseDirConstraints = "E:\\ResultsConstraintsMeasureMOEACD70\\";
//        String baseDirConstraints = "D:\\Experiments\\ExperimentDataThesis\\ConstrainedDTLZ\\compare\\";
//        String baseDirConstraints = "E:\\ResultsConstraintsFinalFinalMOEACDFinal5\\";
//        String baseDirConstraints = "E:\\ResultsConstraintsCompare\\";
//         String baseDirConstraints = "E:\\ResultsConstraintsMOEACD39\\";
//         String baseDirConstraints = "E:\\ResultsConstraintsIrregularMOEACD30\\";
//        String baseDirConstraints = "E:\\ResultsIrregularApproximate\\";
//
//        String statDirConstraints = baseDirConstraints+"stat\\";
//        experimentConstraints.executeDTLZConstraints(baseDirConstraints,maxRunConstraints);
//        computor.executeConstraintsFinal(baseDirConstraints,baseDirConstraints,maxRunConstraints,algorithmNameList,approximateDim,numOfSample);


//               experimentConstraints.executeDTLZConstraintsTest(baseDirConstraints,maxRunConstraints);
//        computor.executeDTLZConstraintsTest(baseDirConstraints,baseDirConstraints,maxRunConstraints,algorithmNameList,approximateDim,numOfSample);

//        experimentConstraints.executeDTLZConstraintsMeasureTest(baseDirConstraints,maxRunConstraints);
        computor.executeDTLZConstraintsMeasureTest(baseDirConstraints,baseDirConstraints,maxRunConstraints,algorithmNameList,approximateDim,numOfSample);


//        experimentConstraints.executeDTLZConstraintsTest(baseDirConstraints,maxRunConstraints);
//        computor.executeDTLZConstraintsTest(baseDirConstraints,baseDirConstraints,maxRunConstraints,algorithmNameList,approximateDim,numOfSample);

//        String baseDirConstraints = "E:\\ResultsCFConstraintsMeasure\\";
//
//        experimentConstraints.executeCFConstraintsMeasure(baseDirConstraints,maxRunConstraints);
//        computor.executeCFConstraintsMeasure(baseDirConstraints,baseDirConstraints,maxRunConstraints,algorithmNameList,approximateDim,numOfSample);
    }
}
