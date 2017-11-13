package org.uma.jmetal.experiment;

import org.uma.jmetal.experiment.MOEACD.*;
import org.uma.jmetal.experiment.dataAnalysis.analysisQuality;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by X250 on 2016/9/4.
 */
public class MyExperimentEngineer {

    public void executeUnConstrainedEngineer(String baseDir,int maxRun){
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 20.0;
        double crossoverDistributionIndexNSGAIII = 20.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes = 20;
        int neighborhoodSize = 20;
        double neighborhoodSelectionProbability = 0.9;
        int extraExtremeSize = 2;
        double pbi_theta = 5.0;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new CrashWorthiness(),
                new UCarSideImpact()
                ,
                new CarCabDesign()
        );
        int[] popsList = {
                153,
                210
                ,
                210
        };
        int[] maxIterationsList = {
                200,
                2000
                ,
                2000
        };
        int[][] divisionConfigList = {
                {16},
                {2,3}
                ,
                {2,3}
        };
        double[][] tauConfigList = {
                {1.0},
                {0.5,1.0}
                ,
                {0.5,1.0}
        };
//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new CrashWorthiness(), new UnconstrainedCarSideImpact()
//        );
//        int[] popsList = {
////                10011,
//                43758
//        };
//        int[] maxIterationsList = {
////                2000,
//                3000
//        };
//        int[][] divisionConfigList = {
////                {140},
//                {10}
//        };
//        double[][] tauConfigList = {
////                {1.0},
//                {1.0}
//        };

//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                 new UnconstrainedCarSideImpact()
//        );
//        int[] popsList = {
//                24310
//        };
//        int[] maxIterationsList = {
//                2000
//        };
//        int[][] divisionConfigList = {
//                {9}
//        };
//        double[][] tauConfigList = {
//                {1.0}
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
                point3D,point5D, point8D, point10D,point15D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);

//        MOEADStudy moeadExperiment = new MOEADStudy();
//        try {
//            moeadExperiment.execute(baseDir,
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

        MOEADNStudy moeadNExperiment = new MOEADNStudy();
        try {
            moeadNExperiment.execute(baseDir,
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
//
//
////        MOEADDStudy moeaddExperiment = new MOEADDStudy();
////        try {
////            moeaddExperiment.execute(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborSize,
////                    neighborhoodSelectionProbability,
////                    indicatorEvaluatingTimes,
////                    maxRun,
////                    problemList,
////                    popsList,
////                    maxIterationsList,
////                    divisionConfigList,
////                    tauConfigList,
////                    frontFileList,
////                    hvRefPointList,
////                    indicatorConfig);
////        } catch (FileNotFoundException e) {
////        }
//////
        MOEADDNStudy moeaddnExperiment = new MOEADDNStudy();
        try {
            moeaddnExperiment.execute(baseDir,
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

//        MOEACDStudy moeacdExperiment = new MOEACDStudy();
//        try {
//            moeacdExperiment.execute(baseDir,
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
//        MOEACDNStudy moeacdnExperiment = new MOEACDNStudy();
//        try {
//            moeacdnExperiment.execute(baseDir,
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
//        MOEACDNFStudy moeacdNFExperiment = new MOEACDNFStudy();
//        try {
//            moeacdNFExperiment.execute(baseDir,
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

        MOEACDNSBXStudy moeacdNSBXExperiment = new MOEACDNSBXStudy();
        try {
            moeacdNSBXExperiment.execute(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndex,
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

//        MOEACDNDStudy moeacdNDExperiment = new MOEACDNDStudy();
//        try {
//            moeacdNDExperiment.execute(baseDir,
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

    public void executeConstrainedEngineer(String baseDir,int maxRun){
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
               new CarSideImpact()
                ,
//                new NCarSideImpact(),
                new Machining()
                ,
                new Water()
//                ,
//                new NWater()
        );
        int[] popsList = {
            91
                ,
//                91,
                165
//                210
                ,
                210
        };
        int[] maxIterationsList = {
            500
                ,
//                500,
                750
//                1000
                ,
                1000
        };
        int[][] divisionConfigList = {
                {12}
                ,
//                {12},
                {8}
//                {6}
                ,
                {6}
        };
        double[][] tauConfigList = {
                {1.0}
                ,
//                 {1.0},
                {1.0}
//                {1.0}
                ,
                {1.0}
        };

//        int[] popsList = {
////                10011,
//                10660
////                ,
////                10626
//        };
//        int[] maxIterationsList = {
////                2000,
//                2500
////                ,3000
//        };
//        int[][] divisionConfigList = {
////                {140},
//                {38}
////                ,
////                {20}
//        };
//        double[][] tauConfigList = {
////                {1.0}
////                ,
//                {1.0}
////                ,
////                {1.0}
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
                point3D,point5D, point8D, point10D,point15D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);
        //
//
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
////
//        CMOEADNStudy cmoeadNExperiment = new CMOEADNStudy();
//        try {
//            cmoeadNExperiment.execute(baseDir,
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
////
////        CMOEADDStudy CmoeaddExperiment = new CMOEADDStudy();
////        try {
////            CmoeaddExperiment.execute(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborSize,
////                    neighborhoodSelectionProbability,
////                    indicatorEvaluatingTimes,
////                    maxRun,
////                    problemList,
////                    popsList,
////                    maxIterationsList,
////                    divisionConfigList,
////                    tauConfigList,
////                    frontFileList,
////                    hvRefPointList,
////                    indicatorConfig);
////        } catch (FileNotFoundException e) {
////        }
//
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
//        }

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
        CMOEACDNStudy CmoeacdnExperiment = new CMOEACDNStudy();
        try {
            CmoeacdnExperiment.execute(baseDir,
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

        CMOEACDNAStudy CmoeacdNAExperiment = new CMOEACDNAStudy();
        try {
            CmoeacdNAExperiment.execute(baseDir,
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

        CMOEACDNDStudy CmoeacdNDExperiment = new CMOEACDNDStudy();
        try {
            CmoeacdNDExperiment.execute(baseDir,
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

        CMOEACDNADStudy CmoeacdNADExperiment = new CMOEACDNADStudy();
        try {
            CmoeacdNADExperiment.execute(baseDir,
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
//        CMOEACDNIIStudy CmoeacdnIIExperiment = new CMOEACDNIIStudy();
//        try {
//            CmoeacdnIIExperiment.execute(baseDir,
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
//        CMOEACDNAIIStudy CmoeacdNAIIExperiment = new CMOEACDNAIIStudy();
//        try {
//            CmoeacdNAIIExperiment.execute(baseDir,
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
//        CMOEACDNDIIStudy CmoeacdNDIIExperiment = new CMOEACDNDIIStudy();
//        try {
//            CmoeacdNDIIExperiment.execute(baseDir,
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
//        CMOEACDNADIIStudy CmoeacdNADIIExperiment = new CMOEACDNADIIStudy();
//        try {
//            CmoeacdNADIIExperiment.execute(baseDir,
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

    public static void main(String[] argv){
        analysisQuality computor = new analysisQuality();

        String[] algorithmNameList = {
//                "CMOEAD_PBI"
//                ,
//                "CMOEADD_PBI"
//                "CMOEAD_PBI",
//                "CMOEADN_PBI",
//////                "CMOEADD_PBI"
//////                ,
//                "CMOEADDN_PBI"
//                ,
//                "C-MOEACD"
//                ,
                "C-MOEACD-N"
                ,
                "C-MOEACD-NA"
                ,
                "C-MOEACD-ND",
                "C-MOEACD-NAD"

//                ,
//                "C-MOEACD-N-II"
//                ,
//                "C-MOEACD-NA-II"
//                ,
//                "C-MOEACD-ND-II",
//                "C-MOEACD-NAD-II"
        };
        int approximateDim = 10;
        int numOfSample = 1000000;

        MyExperimentEngineer engineer = new MyExperimentEngineer();

        int maxRun = 20;
//        String baseDir = "E:\\ResultsApproximateEngineer\\";
//        String baseDir = "E:\\ResultsEngineerConstraintsCompare17\\";
//        String baseDir = "E:\\ResultsApproximate\\";
        String baseDir = "E:\\ResultsEngineerConstraintsMOEACD35\\";
//        String baseDir = "E:\\ResultsEngineerConstraintsFinalCompare\\";
//        String baseDir = "D:\\Experiments\\ExperimentDataThesis\\Engineer\\Constraints\\MOEACD\\";

        String statDir = baseDir+"stat\\";
//        engineer.executeConstrainedEngineer(baseDir,maxRun);
//        computor.executeConstrainedEngineerFinal(baseDir,baseDir,maxRun,algorithmNameList,approximateDim,numOfSample);


        String[] algorithmNameListUn = {
//                "MOEAD_PBI"
//                ,
//                "MOEADD_PBI"
//                "MOEADN_PBI"
//                ,
//                "MOEADDN_PBI"
//                ,
//                "MOEACD"
//                ,
//                "MOEACDN"
//                "MOEACDND"
                "MOEACD-N"
                ,
                "MOEACD-N-SBX"
                ,
//                "MOEACD-N-F"
//                ,
                "MOEACD-ND"
        };
//        String baseDirUn = "E:\\ResultsApproximateEngineer\\";
        String baseDirUn= "E:\\ResultsEngineerUnConstraintsMOEACD46\\";
//        String baseDirUn= "E:\\ResultsEngineerUnConstraintsFinalCompare\\";
//        String baseDirUn = "D://Experiments/ExperimentDataThesis/Engineer/UnConstraints/compare/";
        String statDirUn = baseDir+"stat\\";
        engineer.executeUnConstrainedEngineer(baseDirUn,maxRun);
        computor.executeUnConstrainedEngineerFinal(baseDirUn,baseDirUn,maxRun,algorithmNameListUn,approximateDim,numOfSample);
    }
}
