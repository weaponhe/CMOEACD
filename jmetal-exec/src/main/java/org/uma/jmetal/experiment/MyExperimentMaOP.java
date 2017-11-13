package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.multiobjective.moead.MOEADGR;
import org.uma.jmetal.algorithm.multiobjective.udea.UDEA;
import org.uma.jmetal.experiment.MOEACD.*;
import org.uma.jmetal.experiment.UDEA.UDEAStudy;
import org.uma.jmetal.experiment.dataAnalysis.analysisQuality;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.hypervolume.ApproximateHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by X250 on 2016/5/5.
 */
public class MyExperimentMaOP {
    public void executeDTLZMeasure(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes =20;
        int neighborhoodSize = 20;
        double neighborhoodSelectionProbability = 0.9;
        double pbi_theta = 5.0;

//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new DTLZ1(7,3), new DTLZ1(9, 5), new DTLZ1(12, 8), new DTLZ1(14, 10),new DTLZ1(19, 15),
//                new DTLZ2(12,3), new DTLZ2(14, 5), new DTLZ2(17, 8), new DTLZ2(19, 10),new DTLZ2(24, 15),
//                new DTLZ3(12,3), new DTLZ3(14, 5), new DTLZ3(17, 8), new DTLZ3(19, 10),new DTLZ3(24, 15)
//                ,
//                new DTLZ4(12,3), new DTLZ4(14, 5), new DTLZ4(17, 8), new DTLZ4(19, 10),new DTLZ4(24, 15),
//                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5), new Convex_DTLZ2(17, 8), new Convex_DTLZ2(19, 10), new Convex_DTLZ2(24, 15)
//        );
//
//        int[] popsList = {
//                91, 210, 156, 275,135,
//                91, 210, 156, 275,135,
//                91, 210, 156, 275,135,
//                91, 210, 156, 275,135,
//                91, 210, 156, 275,135
//        };
//        int[] maxIterationsList = {
//                400,600,750,1000,1500,
//                250,350,500,750,1000,
//                1000,1000,1000,1500,2000
//                ,
//                600,1000,1250,2000,3000,
//                250,750,2000,4000,4500
//        };
//        int[][] divisionConfigList = {
//                {12},{6}, {2, 3}, {2, 3},{1,2},
//                {12},{6}, {2, 3}, {2, 3},{1,2},
//                {12},{6}, {2, 3}, {2, 3},{1,2},
//                {12},{6}, {2, 3}, {2, 3},{1,2},
//                {12},{6}, {2, 3}, {2, 3},{1,2}
//        };
//        double[][] tauConfigList = {
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0}
//        };
        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(7,3), new DTLZ1(9, 5),
                new DTLZ2(12,3), new DTLZ2(14, 5),
                new DTLZ3(12,3), new DTLZ3(14, 5),
                new DTLZ4(12,3), new DTLZ4(14, 5),
                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5)
        );

        int[] popsList = {
                91, 210,
                91, 210,
                91, 210,
                91, 210,
                91, 210
        };
        int[] maxIterationsList = {
                400,600,
                250,350,
                1000,1000,
                600,1000,
                250,750
        };
        int[][] divisionConfigList = {
                {12},{6},
                {12},{6},
                {12},{6},
                {12},{6},
                {12},{6}
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

        NSGAIIIStudy nsgaiiiExperiment = new NSGAIIIStudy();
        try {
            nsgaiiiExperiment.executeMeasure(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndexNSGAIII,
                    mutationDistributionIndex,
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
        MOEADStudy moeadExperiment = new MOEADStudy();
        try {
            moeadExperiment.executeMeasure(baseDir,
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
        MOEADDEStudy moeaddeExperiment = new MOEADDEStudy();
        try {
            moeaddeExperiment.executeMeasure(baseDir,
                    crossoverProbability,
                    f,
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

        MOEADACDStudy moeadacdExperiment = new MOEADACDStudy();
        try {
            moeadacdExperiment.executeMeasure(baseDir,
                    crossoverProbability,
                    f,
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
        MOEADACDSBXStudy moeadacdSBXExperiment = new MOEADACDSBXStudy();
        try {
            moeadacdSBXExperiment.executeMeasure(baseDir,
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

        MOEADAGRStudy moeadagrExperiment = new MOEADAGRStudy();
        try {
            moeadagrExperiment.executeMeasure(baseDir,
                    crossoverProbability,
                    f,
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

        MOEADAGRSBXStudy moeadagrSBXExperiment = new MOEADAGRSBXStudy();
        try {
            moeadagrSBXExperiment.executeMeasure(baseDir,
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

        MOEADDStudy moeaddExperiment = new MOEADDStudy();
        try {
            moeaddExperiment.executeMeasure(baseDir,
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

        MOEACDDEStudy moeacdDEExperiment = new MOEACDDEStudy();
        try {
            moeacdDEExperiment.executeMeasure(baseDir,
                    crossoverProbability,
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

//        MOEACDFStudy moeacdFExperiment = new MOEACDFStudy();
//        try {
//            moeacdFExperiment.executeMeasure(baseDir,
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

        MOEACDSBXStudy moeacdSBXExperiment = new MOEACDSBXStudy();
        try {
            moeacdSBXExperiment.executeMeasure(baseDir,
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
//
//        MOEACDVStudy moeacdVExperiment = new MOEACDVStudy();
//        try {
//            moeacdVExperiment.executeMeasure(baseDir,
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
//        MOEACDEHAStudy moeacdEHAExperiment = new MOEACDEHAStudy();
//        try {
//            moeacdEHAExperiment.executeMeasure(baseDir,
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

    }


    public void executeDTLZScaleMeasure(String baseDir) {
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
        int maxRun = 20;

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
        double[][] tauConfigListMOEACD = {
                {1.0}, {1.0}, {2.0 / 3.0, 1.0}, {2.0 / 3.0, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {2.0 / 3.0, 1.0}, {2.0 / 3.0, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {2.0 / 3.0, 1.0}, {2.0 / 3.0, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {2.0 / 3.0, 1.0}, {2.0 / 3.0, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {2.0 / 3.0, 1.0}, {2.0 / 3.0, 1.0},{0.5,1.0},
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[91]",
//                frontDir + "DTLZ1.5D.pf[210]",
//                frontDir + "DTLZ1.8D.pf[156]",
//                frontDir + "DTLZ2.3D.pf[91]",
//                frontDir + "DTLZ2.5D.pf[210]",
//                frontDir + "DTLZ2.8D.pf[156]",
//                frontDir + "DTLZ3.3D.pf[91]",
//                frontDir + "DTLZ3.5D.pf[210]",
//                frontDir + "DTLZ3.8D.pf[156]",
//                frontDir + "DTLZ4.3D.pf[91]",
//                frontDir + "DTLZ4.5D.pf[210]",
//                frontDir + "DTLZ4.8D.pf[156]",
//                frontDir + "Convex_DTLZ2.3D.pf[91]",
//                frontDir + "Convex_DTLZ2.5D.pf[210]",
//                frontDir + "Convex_DTLZ2.8D.pf[156]"
//        };

        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[5050]",
                frontDir + "DTLZ1.5D.pf[10626]",
                frontDir + "DTLZ1.8D.pf[11440]",
                frontDir + "DTLZ1.10D.pf[24310]",
                frontDir + "DTLZ1.15D.pf[38760]",
                frontDir + "DTLZ2.3D.pf[5050]",
                frontDir + "DTLZ2.5D.pf[10626]",
                frontDir + "DTLZ2.8D.pf[11440]",
                frontDir + "DTLZ2.10D.pf[24310]",
                frontDir + "DTLZ2.15D.pf[38760]",
                frontDir + "DTLZ3.3D.pf[5050]",
                frontDir + "DTLZ3.5D.pf[10626]",
                frontDir + "DTLZ3.8D.pf[11440]",
                frontDir + "DTLZ3.10D.pf[24310]",
                frontDir + "DTLZ3.15D.pf[38760]",
                frontDir + "DTLZ4.3D.pf[5050]",
                frontDir + "DTLZ4.5D.pf[10626]",
                frontDir + "DTLZ4.8D.pf[11440]",
                frontDir + "DTLZ4.10D.pf[24310]",
                frontDir + "DTLZ4.15D.pf[38760]",
                frontDir + "Convex_DTLZ2.3D.pf[5050]",
                frontDir + "Convex_DTLZ2.5D.pf[10626]",
                frontDir + "Convex_DTLZ2.8D.pf[11440]",
                frontDir + "Convex_DTLZ2.10D.pf[24310]",
                frontDir + "Convex_DTLZ2.15D.pf[38760]"
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
                    tauConfigListMOEACD,
                    frontFileList,
                    hvRefPointList,
                    indicatorConfig);
        } catch (FileNotFoundException e) {
        }


        MOEACDNStudy moeacdnExperiment = new MOEACDNStudy();
        try {
            moeacdnExperiment.executeMeasure(baseDir,
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
                    tauConfigListMOEACD,
                    frontFileList,
                    hvRefPointList,
                    indicatorConfig);
        } catch (FileNotFoundException e) {
        }
    }


    public void executeDTLZMeasure(String baseDir,double pbi_theta) {
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
        //double pbi_theta = 5.0;
        int maxRun = 10;

//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new DTLZ1(9, 5), new DTLZ1(12, 8), new DTLZ1(14, 10), new DTLZ1(19, 15),
//                new DTLZ2(14, 5), new DTLZ2(17, 8), new DTLZ2(19, 10), new DTLZ2(24, 15),
//                new DTLZ3(14, 5), new DTLZ3(17, 8), new DTLZ3(19, 10), new DTLZ3(24, 15),
//                new DTLZ4(14, 5), new DTLZ4(17, 8), new DTLZ4(19, 10), new DTLZ4(24, 15),
//                new Convex_DTLZ2(14, 5), new Convex_DTLZ2(17, 8), new Convex_DTLZ2(19, 10), new Convex_DTLZ2(24, 15)
//                );
//        int[] popsList = {
//                210, 156, 275, 135,
//                210, 156, 275, 135,
//                210, 156, 275, 135,
//                210, 156, 275, 135,
//                210, 156, 275, 135
//        };
//        int[] maxIterationsList = {
//                1000, 1500, 2000, 3000,
//                1000, 1500, 2000, 3000,
//                1500, 2000, 2500, 3500,
//                1500, 2000, 2500, 3500,
//                1500, 2000, 2500, 3500
//        };
//        int[][] divisionConfigList = {
//                {6}, {2, 3}, {2, 3}, {1, 2},
//                {6}, {2, 3}, {2, 3}, {1, 2},
//                {6}, {2, 3}, {2, 3}, {1, 2},
//                {6}, {2, 3}, {2, 3}, {1, 2},
//                {6}, {2, 3}, {2, 3}, {1, 2}
//        };
//        double[][] tauConfigList = {
//                {1.0}, {0.5, 1.0}, {0.5, 1.0}, {0.5, 1.0},
//                {1.0}, {0.5, 1.0}, {0.5, 1.0}, {0.5, 1.0},
//                {1.0}, {0.5, 1.0}, {0.5, 1.0}, {0.5, 1.0},
//                {1.0}, {0.5, 1.0}, {0.5, 1.0}, {0.5, 1.0},
//                {1.0}, {0.5, 1.0}, {0.5, 1.0}, {0.5, 1.0}
//        };
//        double[][] tauConfigListMOEACD = {
//                {1.0}, {2 / 3, 1.0}, {2 / 3, 1.0}, {0.5, 1.0},
//                {1.0}, {2 / 3, 1.0}, {2 / 3, 1.0}, {0.5, 1.0},
//                {1.0}, {2 / 3, 1.0}, {2 / 3, 1.0}, {0.5, 1.0},
//                {1.0}, {2 / 3, 1.0}, {2 / 3, 1.0}, {0.5, 1.0},
//                {1.0}, {2 / 3, 1.0}, {2 / 3, 1.0}, {0.5, 1.0}
//        };
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";

//        String[] frontFileList = {
//                frontDir + "DTLZ1.5D.pf[10626]",
//                frontDir + "DTLZ1.8D.pf[11440]",
//                frontDir + "DTLZ1.10D.pf[24310]",
//                frontDir + "DTLZ1.15D.pf[38760]",
//                frontDir + "DTLZ2.5D.pf[10626]",
//                frontDir + "DTLZ2.8D.pf[11440]",
//                frontDir + "DTLZ2.10D.pf[24310]",
//                frontDir + "DTLZ2.15D.pf[38760]",
//                frontDir + "DTLZ3.5D.pf[10626]",
//                frontDir + "DTLZ3.8D.pf[11440]",
//                frontDir + "DTLZ3.10D.pf[24310]",
//                frontDir + "DTLZ3.15D.pf[38760]",
//                frontDir + "DTLZ4.5D.pf[10626]",
//                frontDir + "DTLZ4.8D.pf[11440]",
//                frontDir + "DTLZ4.10D.pf[24310]",
//                frontDir + "DTLZ4.15D.pf[38760]",
//                frontDir + "Convex_DTLZ2.5D.pf[10626]",
//                frontDir + "Convex_DTLZ2.8D.pf[11440]",
//                frontDir + "Convex_DTLZ2.10D.pf[24310]",
//                frontDir + "Convex_DTLZ2.15D.pf[38760]"
//        };
//
//        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
//        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
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
//                point5Dmin, point8Dmin, point10Dmin, point15Dmin,
//                point5D, point8D, point10Dmin, point15Dmin,
//                point5D, point8D, point10Dmin, point15Dmin,
//                point5D, point8D, point10Dmin, point15Dmin,
//                point5D, point8D, point10Dmin, point15Dmin
//        };
//
//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new DTLZ1(7,3), new DTLZ1(9, 5), new DTLZ1(12, 8), new DTLZ1(14, 10),new DTLZ1(19, 15),
//                new DTLZ2(12,3), new DTLZ2(14, 5), new DTLZ2(17, 8), new DTLZ2(19, 10),new DTLZ2(24, 15),
//                new DTLZ3(12,3), new DTLZ3(14, 5), new DTLZ3(17, 8), new DTLZ3(19, 10),new DTLZ3(24, 15),
//                new DTLZ4(12,3), new DTLZ4(14, 5), new DTLZ4(17, 8), new DTLZ4(19, 10),new DTLZ4(24, 15),
//                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5), new Convex_DTLZ2(17, 8), new Convex_DTLZ2(19, 10), new Convex_DTLZ2(24, 15)
//        );
//
//        int[] popsList = {
//                91, 210, 156, 275,135,
//                91, 210, 156, 275,135,
//                91, 210, 156, 275,135,
//                91, 210, 156, 275,135,
//                91, 210, 156, 275,135
//        };
//        int[] maxIterationsList = {
//                400,600,750,1000,1500,
//                250,350,500,750,1000,
//                1000,1000,1000,1500,2000,
//                600,1000,1250,2000,3000,
//                250,750,2000,4000,4500
//        };
//        int[][] divisionConfigList = {
//                {12},{6}, {2, 3}, {2, 3},{1,2},
//                {12},{6}, {2, 3}, {2, 3},{1,2},
//                {12},{6}, {2, 3}, {2, 3},{1,2},
//                {12},{6}, {2, 3}, {2, 3},{1,2},
//                {12},{6}, {2, 3}, {2, 3},{1,2}
//        };
//        double[][] tauConfigList = {
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0}
//        };
//        double[][] tauConfigListMOEACD = {
//                {1.0}, {1.0}, {2.0 / 3.0, 1.0}, {2.0 / 3.0, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {2.0 / 3.0, 1.0}, {2.0 / 3.0, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {2.0 / 3.0, 1.0}, {2.0 / 3.0, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {2.0 / 3.0, 1.0}, {2.0 / 3.0, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {2.0 / 3.0, 1.0}, {2.0 / 3.0, 1.0},{0.5,1.0},
//        };
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";

//
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[5050]",
//                frontDir + "DTLZ1.5D.pf[10626]",
//                frontDir + "DTLZ1.8D.pf[11440]",
//                frontDir + "DTLZ1.10D.pf[24310]",
//                frontDir + "DTLZ1.15D.pf[38760]",
//                frontDir + "DTLZ2.3D.pf[5050]",
//                frontDir + "DTLZ2.5D.pf[10626]",
//                frontDir + "DTLZ2.8D.pf[11440]",
//                frontDir + "DTLZ2.10D.pf[24310]",
//                frontDir + "DTLZ2.15D.pf[38760]",
//                frontDir + "DTLZ3.3D.pf[5050]",
//                frontDir + "DTLZ3.5D.pf[10626]",
//                frontDir + "DTLZ3.8D.pf[11440]",
//                frontDir + "DTLZ3.10D.pf[24310]",
//                frontDir + "DTLZ3.15D.pf[38760]",
//                frontDir + "DTLZ4.3D.pf[5050]",
//                frontDir + "DTLZ4.5D.pf[10626]",
//                frontDir + "DTLZ4.8D.pf[11440]",
//                frontDir + "DTLZ4.10D.pf[24310]",
//                frontDir + "DTLZ4.15D.pf[38760]",
//                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//                frontDir + "Convex_DTLZ2.5D.pf[10626]",
//                frontDir + "Convex_DTLZ2.8D.pf[11440]",
//                frontDir + "Convex_DTLZ2.10D.pf[24310]",
//                frontDir + "Convex_DTLZ2.15D.pf[38760]"
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
//                point3Dmin,point5Dmin, point8Dmin, point10Dmin,point15Dmin,
//                point3D,point5D, point8D, point10D,point15D,
//                point3D,point5D, point8D, point10D,point15D,
//                point3D,point5D, point8D, point10D,point15D,
//                point3D,point5D, point8D, point10D,point15D
//        };
//
        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(7,3), new DTLZ1(9, 5), new DTLZ1(12, 8),
                new DTLZ2(12,3), new DTLZ2(14, 5), new DTLZ2(17, 8),
                new DTLZ3(12,3), new DTLZ3(14, 5), new DTLZ3(17, 8),
                new DTLZ4(12,3), new DTLZ4(14, 5), new DTLZ4(17, 8),
                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5), new Convex_DTLZ2(17, 8)
        );

        int[] popsList = {
                91, 210, 156,
                91, 210, 156,
                91, 210, 156,
                91, 210, 156,
                91, 210, 156
        };
        int[] maxIterationsList = {
                400,600,750,
                250,350,500,
                1000,1000,1000,
                600,1000,1250,
                250,750,2000
        };
        int[][] divisionConfigList = {
                {12},{6}, {2, 3},
                {12},{6}, {2, 3},
                {12},{6}, {2, 3},
                {12},{6}, {2, 3},
                {12},{6}, {2, 3}
        };
        double[][] tauConfigList = {
                {1.0}, {1.0}, {0.5, 1.0},
                {1.0}, {1.0}, {0.5, 1.0},
                {1.0}, {1.0}, {0.5, 1.0},
                {1.0}, {1.0}, {0.5, 1.0},
                {1.0}, {1.0}, {0.5, 1.0}
        };
        double[][] tauConfigListMOEACD = {
                {1.0}, {1.0}, {2.0 / 3.0, 1.0},
                {1.0}, {1.0}, {2.0 / 3.0, 1.0},
                {1.0}, {1.0}, {2.0 / 3.0, 1.0},
                {1.0}, {1.0}, {2.0 / 3.0, 1.0},
                {1.0}, {1.0}, {2.0 / 3.0, 1.0}
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";


        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[5050]",
                frontDir + "DTLZ1.5D.pf[10626]",
                frontDir + "DTLZ1.8D.pf[11440]",
                frontDir + "DTLZ2.3D.pf[5050]",
                frontDir + "DTLZ2.5D.pf[10626]",
                frontDir + "DTLZ2.8D.pf[11440]",
                frontDir + "DTLZ3.3D.pf[5050]",
                frontDir + "DTLZ3.5D.pf[10626]",
                frontDir + "DTLZ3.8D.pf[11440]",
                frontDir + "DTLZ4.3D.pf[5050]",
                frontDir + "DTLZ4.5D.pf[10626]",
                frontDir + "DTLZ4.8D.pf[11440]",
                frontDir + "Convex_DTLZ2.3D.pf[5050]",
                frontDir + "Convex_DTLZ2.5D.pf[10626]",
                frontDir + "Convex_DTLZ2.8D.pf[11440]"
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
                point3Dmin,point5Dmin, point8Dmin,
                point3D,point5D, point8D,
                point3D,point5D, point8D,
                point3D,point5D, point8D,
                point3D,point5D, point8D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);


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
//                    tauConfigListMOEACD,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//     }
    }

    public void executeDTLZ(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes =20;
        int neighborhoodSize = 20;
        double neighborhoodSelectionProbability = 0.9;
        double pbi_theta = 5.0;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(7,3), new DTLZ1(9, 5), new DTLZ1(12, 8), new DTLZ1(14, 10),new DTLZ1(19, 15),
                new DTLZ2(12,3), new DTLZ2(14, 5), new DTLZ2(17, 8), new DTLZ2(19, 10),new DTLZ2(24, 15),
                new DTLZ3(12,3), new DTLZ3(14, 5), new DTLZ3(17, 8), new DTLZ3(19, 10),new DTLZ3(24, 15)
                ,
                new DTLZ4(12,3), new DTLZ4(14, 5), new DTLZ4(17, 8),
                new DTLZ4(19, 10),new DTLZ4(24, 15)
                ,
                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5), new Convex_DTLZ2(17, 8), new Convex_DTLZ2(19, 10), new Convex_DTLZ2(24, 15)
//                new DTLZ2(24, 15),
//                new DTLZ3(17, 8), new DTLZ3(19, 10),new DTLZ3(24, 15)
//                ,
//                new DTLZ4(17, 8),  new DTLZ4(19, 10),new DTLZ4(24, 15)
        );

        int[] popsList = {
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156,
                275,135
//                135,
//                156, 275,135,
//                156,
//                275,135
        };
        int[] maxIterationsList = {
                400,600,750,1000,1500,
                250,350,500,750,1000,
                1000,1000,
                1000,1500,2000
                ,
                600,1000,
                1250,
                2000,3000
                ,
                250,750,2000,4000,4500
//                1250,
//                1250,1750,2500
//                ,
//                1500,
//                2500,3500
        };
        int[][] divisionConfigList = {
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12},{6}, {2, 3},
                {2, 3},{1,2}
//                {1,2},
//                {2, 3}, {2, 3},{1,2},
//                {2, 3},
//                {2, 3},{1,2}
        };
        double[][] tauConfigList = {
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {0.5, 1.0},
                {0.5, 1.0},{0.5,1.0}
//                {0.5,1.0},
//                {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {0.5, 1.0},
//                {0.5, 1.0},{0.5,1.0}
        };

//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new DTLZ1(7,3), new DTLZ1(9, 5),
//                new DTLZ2(12,3), new DTLZ2(14, 5),
//                new DTLZ3(12,3), new DTLZ3(14, 5),
//                new DTLZ4(12,3), new DTLZ4(14, 5),
//                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5)
//        );
//
//        int[] popsList = {
//                91, 210,
//                91, 210,
//                91, 210,
//                91, 210,
//                91, 210
//        };
//        int[] maxIterationsList = {
//                400,600,
//                250,350,
//                1000,1000,
//                600,1000,
//                250,750
//        };
//        int[][] divisionConfigList = {
//                {12},{6},
//                {12},{6},
//                {12},{6},
//                {12},{6},
//                {12},{6}
//        };
//        double[][] tauConfigList = {
//                {1.0}, {1.0},
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
////
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
////
//        MOEADDEStudy moeaddeExperiment = new MOEADDEStudy();
//        try {
//            moeaddeExperiment.execute(baseDir,
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
//            moeadacdExperiment.execute(baseDir,
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
//    }
//        MOEADACDSBXStudy moeadacdSBXExperiment = new MOEADACDSBXStudy();
//        try {
//            moeadacdSBXExperiment.execute(baseDir,
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
//        MOEADAGRStudy moeadagrExperiment = new MOEADAGRStudy();
//        try {
//            moeadagrExperiment.execute(baseDir,
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
//    }
//
//        MOEADAGRSBXStudy moeadagrSBXExperiment = new MOEADAGRSBXStudy();
//        try {
//            moeadagrSBXExperiment.execute(baseDir,
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
//        MOEADDStudy moeaddExperiment = new MOEADDStudy();
//        try {
//            moeaddExperiment.execute(baseDir,
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
//        MOEACDDEStudy moeacdDEExperiment = new MOEACDDEStudy();
//        try {
//            moeacdDEExperiment.execute(baseDir,
//                    crossoverProbability,
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
//        MOEACDFStudy moeacdFExperiment = new MOEACDFStudy();
//        try {
//            moeacdFExperiment.execute(baseDir,
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
        MOEACDSBXStudy moeacdSBXExperiment = new MOEACDSBXStudy();
        try {
            moeacdSBXExperiment.execute(baseDir,
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

//        MOEACDVStudy moeacdVExperiment = new MOEACDVStudy();
//        try {
//            moeacdVExperiment.execute(baseDir,
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
//        MOEACDPEHStudy moeacdPEHExperiment = new MOEACDPEHStudy();
//        try {
//            moeacdPEHExperiment.execute(baseDir,
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
//        MOEACDACVStudy moeacdACVExperiment = new MOEACDACVStudy();
//        try {
//            moeacdACVExperiment.execute(baseDir,
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
//        MOEACDAECVStudy moeacdAECVExperiment = new MOEACDAECVStudy();
//        try {
//            moeacdAECVExperiment.execute(baseDir,
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

//        UDEAStudy UDEAExperiment = new UDEAStudy();
//        try {
//            UDEAExperiment.execute(baseDir,
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
//        UCDEAStudy UCDEAExperiment = new UCDEAStudy();
//        try {
//            UCDEAExperiment.execute(baseDir,
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

    public void executeDTLZScale(String baseDir,int maxRun) {
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
//        int maxRun = 20;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new SDTLZ1(7, 3,10.0), new SDTLZ1(9, 5,10.0), new SDTLZ1(12, 8,3.0),new SDTLZ1(14, 10,2.0),new SDTLZ1(19, 15,1.2),
                new SDTLZ1(7, 3,10.0), new SDTLZ1(9, 5,10.0), new SDTLZ1(12, 8,3.0),
//                new SDTLZ1(14, 10,3.0),new SDTLZ1(19, 15,2.0)
                new SDTLZ1(14, 10,2.0),new SDTLZ1(19, 15,1.2)
                 ,
                new SDTLZ2(12, 3,10.0), new SDTLZ2(14, 5,10.0), new SDTLZ2(17, 8,3.0),new SDTLZ2(19, 10,3.0),new SDTLZ2(24, 15,2.0)
                ,
                new SDTLZ3(12, 3,10.0), new SDTLZ3(14, 5,10.0), new SDTLZ3(17, 8,3.0),new SDTLZ3(19, 10,3.0),new SDTLZ3(24, 15,2.0),
                new SDTLZ4(12, 3,10.0), new SDTLZ4(14, 5,10.0), new SDTLZ4(17, 8,3.0),new SDTLZ4(19, 10,3.0),new SDTLZ4(24, 15,2.0),
                new Convex_SDTLZ2(12, 3,10.0), new Convex_SDTLZ2(14, 5,10.0), new Convex_SDTLZ2(17, 8,3.0),new Convex_SDTLZ2(19, 10,3.0),new Convex_SDTLZ2(24, 15,2.0)
                );

        int[] popsList = {
                91, 210, 156,
                275,135
                ,
                91, 210, 156, 275,135
                ,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135
        };

        int[] maxIterationsList = {
                400,600,750,
                1000,1500
                ,
                250,350,500,750,1000
                ,
                1000,1000,1000,1500,2000,
                600,1000,1250,2000,3000,
                250,750,2000,4000,4500
        };
        int[][] divisionConfigList = {
                {12},{6}, {2, 3},
                {2, 3},{1,2}
                ,
                {12},{6}, {2, 3}, {2, 3},{1,2}
                ,
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12},{6}, {2, 3}, {2, 3},{1,2}
        };
        double[][] tauConfigList = {
                {1.0}, {1.0}, {0.5, 1.0},
                {0.5, 1.0},{0.5,1.0}
                ,
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0}
                ,
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0}
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
                point3D,point5D, point8D, point10D,point15D
                ,
                point3D,point5D, point8D, point10D,point15D,
                point3D,point5D, point8D, point10D,point15D,
                point3D,point5D, point8D, point10D,point15D
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
                frontDir + "DTLZ2.3D.pf[91]",
                frontDir + "DTLZ2.5D.pf[210]",
                frontDir + "DTLZ2.8D.pf[156]",
                frontDir + "DTLZ2.10D.pf[275]",
                frontDir + "DTLZ2.15D.pf[135]",
                frontDir + "DTLZ2.3D.pf[91]",
                frontDir + "DTLZ2.5D.pf[210]",
                frontDir + "DTLZ2.8D.pf[156]",
                frontDir + "DTLZ2.10D.pf[275]",
                frontDir + "DTLZ2.15D.pf[135]",
                frontDir + "DTLZ2.3D.pf[91]",
                frontDir + "DTLZ2.5D.pf[210]",
                frontDir + "DTLZ2.8D.pf[156]",
                frontDir + "DTLZ2.10D.pf[275]",
                frontDir + "DTLZ2.15D.pf[135]"
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);
//////
        NSGAIIIStudy nsgaiiiExperiment = new NSGAIIIStudy();
        try {
            nsgaiiiExperiment.execute(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndexNSGAIII,
                    mutationDistributionIndex,
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
        MOEADNStudy moeadnExperiment = new MOEADNStudy();
        try {
            moeadnExperiment.execute(baseDir,
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
//
////        MOEADACDNStudy moeadacdnExperiment = new MOEADACDNStudy();
////        try {
////            moeadacdnExperiment.execute(baseDir,
////                    crossoverProbability,
////                    f,
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
////
////        MOEADGRNStudy moeadgrnExperiment = new MOEADGRNStudy();
////        try {
////            moeadgrnExperiment.execute(baseDir,
////                    crossoverProbability,
////                    f,
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
////        MOEADAGRNStudy moeadagrnExperiment = new MOEADAGRNStudy();
////        try {
////            moeadagrnExperiment.execute(baseDir,
////                    crossoverProbability,
////                    f,
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
//
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

//
//        MOEACDNDEStudy moeacdNDEExperiment = new MOEACDNDEStudy();
//        try {
//            moeacdNDEExperiment.execute(baseDir,
//                    crossoverProbability,
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
    }


    public void executeDTLZSmallMeasure(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 5;
        int indicatorEvaluatingTimes = 20;
        double neighborhoodSelectionProbability = 0.9;
        int extraExtremeSize = 1;
        double pbi_theta = 5.0;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(7,3), new DTLZ1(9, 5), new DTLZ1(14, 10),
                new DTLZ2(12,3), new DTLZ2(14, 5), new DTLZ2(19, 10),
                new DTLZ3(12,3), new DTLZ3(14, 5), new DTLZ3(19, 10),
                new DTLZ4(12,3), new DTLZ4(14, 5), new DTLZ4(19, 10),
                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5),  new Convex_DTLZ2(19, 10)
        );

        int[] popsList = {
                10, 20, 65,
                10, 20, 65,
                10, 20, 65,
                10, 20, 65,
                10, 20, 65
        };
        int[] maxIterationsList = {
                400,600,1000,
                250,350,750,
                1000,1000,1500,
                600,1000,2000,
                250,750,4000
        };
        int[][] divisionConfigList = {
                {3},{1,2},{1,2},
                {3},{1,2},{1,2},
                {3},{1,2},{1,2},
                {3},{1,2},{1,2},
                {3},{1,2},{1,2}
        };
        double[][] tauConfigList = {
                {1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {0.5, 1.0},{0.5,1.0}
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[10]",
//                frontDir + "DTLZ1.5D.pf[20]",
//                frontDir + "DTLZ1.10D.pf[65]",
//                frontDir + "DTLZ2.3D.pf[10]",
//                frontDir + "DTLZ2.5D.pf[20]",
//                frontDir + "DTLZ2.10D.pf[65]",
//                frontDir + "DTLZ3.3D.pf[10]",
//                frontDir + "DTLZ3.5D.pf[20]",
//                frontDir + "DTLZ3.10D.pf[65]",
//                frontDir + "DTLZ4.3D.pf[10]",
//                frontDir + "DTLZ4.5D.pf[20]",
//                frontDir + "DTLZ4.10D.pf[65]",
//                frontDir + "Convex_DTLZ2.3D.pf[10]",
//                frontDir + "Convex_DTLZ2.5D.pf[20]",
//                frontDir + "Convex_DTLZ2.10D.pf[65]"
//        };

        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[5050]",
                frontDir + "DTLZ1.5D.pf[14950]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ2.3D.pf[5050]",
                frontDir + "DTLZ2.5D.pf[14950]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ3.3D.pf[5050]",
                frontDir + "DTLZ3.5D.pf[14950]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ4.3D.pf[5050]",
                frontDir + "DTLZ4.5D.pf[14950]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.3D.pf[5050]",
                frontDir + "Convex_DTLZ2.5D.pf[14950]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]"
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
//
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
////
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
////
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
//    }
//        MOEADGRStudy moeadgrExperiment = new MOEADGRStudy();
//        try {
//            moeadgrExperiment.executeMeasure(baseDir,
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
//    }

//        MOEADAGRStudy moeadagrExperiment = new MOEADAGRStudy();
//        try {
//            moeadagrExperiment.executeMeasure(baseDir,
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
//    }
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
        MOEACDStudy moeacdExperiment = new MOEACDStudy();
        try {
            moeacdExperiment.executeMeasure(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndex,
                    f,
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

        MOEACDNStudy moeacdnExperiment = new MOEACDNStudy();
        try {
            moeacdnExperiment.executeMeasure(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndex,
                    f,
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

    }


    public void executeDTLZSmall(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 5;
        int indicatorEvaluatingTimes = 20;
        double neighborhoodSelectionProbability = 0.9;
        int extraExtremeSize = 1;
        double pbi_theta = 5.0;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(7,3), new DTLZ1(9, 5), new DTLZ1(14, 10),
                new DTLZ2(12,3), new DTLZ2(14, 5), new DTLZ2(19, 10),
                new DTLZ3(12,3), new DTLZ3(14, 5), new DTLZ3(19, 10),
                new DTLZ4(12,3), new DTLZ4(14, 5), new DTLZ4(19, 10),
                new Convex_DTLZ2(12,3), new Convex_DTLZ2(14, 5),  new Convex_DTLZ2(19, 10)
        );

        int[] popsList = {
                10, 20, 65,
                10, 20, 65,
                10, 20, 65,
                10, 20, 65,
                10, 20, 65
        };
        int[] maxIterationsList = {
                400,600,1000,
                250,350,750,
                1000,1000,1500,
                600,1000,2000,
                250,750,4000
        };
        int[][] divisionConfigList = {
                {3},{1,2},{1,2},
                {3},{1,2},{1,2},
                {3},{1,2},{1,2},
                {3},{1,2},{1,2},
                {3},{1,2},{1,2}
        };
        double[][] tauConfigList = {
                {1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {0.5, 1.0},{0.5,1.0}
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[10]",
//                frontDir + "DTLZ1.5D.pf[20]",
//                frontDir + "DTLZ1.10D.pf[65]",
//                frontDir + "DTLZ2.3D.pf[10]",
//                frontDir + "DTLZ2.5D.pf[20]",
//                frontDir + "DTLZ2.10D.pf[65]",
//                frontDir + "DTLZ3.3D.pf[10]",
//                frontDir + "DTLZ3.5D.pf[20]",
//                frontDir + "DTLZ3.10D.pf[65]",
//                frontDir + "DTLZ4.3D.pf[10]",
//                frontDir + "DTLZ4.5D.pf[20]",
//                frontDir + "DTLZ4.10D.pf[65]",
//                frontDir + "Convex_DTLZ2.3D.pf[10]",
//                frontDir + "Convex_DTLZ2.5D.pf[20]",
//                frontDir + "Convex_DTLZ2.10D.pf[65]"
//        };

        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[5050]",
                frontDir + "DTLZ1.5D.pf[14950]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ2.3D.pf[5050]",
                frontDir + "DTLZ2.5D.pf[14950]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ3.3D.pf[5050]",
                frontDir + "DTLZ3.5D.pf[14950]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ4.3D.pf[5050]",
                frontDir + "DTLZ4.5D.pf[14950]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.3D.pf[5050]",
                frontDir + "Convex_DTLZ2.5D.pf[14950]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]"
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

        NSGAIIIStudy nsgaiiiExperiment = new NSGAIIIStudy();
        try {
            nsgaiiiExperiment.execute(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndexNSGAIII,
                    mutationDistributionIndex,
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

        MOEADStudy moeadExperiment = new MOEADStudy();
        try {
            moeadExperiment.execute(baseDir,
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
        MOEADDEStudy moeaddeExperiment = new MOEADDEStudy();
        try {
            moeaddeExperiment.execute(baseDir,
                    crossoverProbability,
                    f,
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
        MOEADACDStudy moeadacdExperiment = new MOEADACDStudy();
        try {
            moeadacdExperiment.execute(baseDir,
                    crossoverProbability,
                    f,
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
        MOEADGRStudy moeadgrExperiment = new MOEADGRStudy();
        try {
            moeadgrExperiment.execute(baseDir,
                    crossoverProbability,
                    f,
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

//        MOEADAGRStudy moeadagrExperiment = new MOEADAGRStudy();
//        try {
//            moeadagrExperiment.execute(baseDir,
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
        MOEADDStudy moeaddExperiment = new MOEADDStudy();
        try {
            moeaddExperiment.execute(baseDir,
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
//                    mutationDistributionIndex,
//                    neighborSize,
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

//        MOEACDNStudy moeacdnExperiment = new MOEACDNStudy();
//        try {
//            moeacdnExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborSize,
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


    }


    public void executeDTLZMethod(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes =20;
        int neighborhoodSize = 20;
        double neighborhoodSelectionProbability = 0.9;
        double pbi_theta = 5.0;



        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(7,3),
                new DTLZ2(12,3),
                new DTLZ3(12,3),
                new DTLZ4(12,3),
                new Convex_DTLZ2(12,3)
        );

        int[] popsList = {
                91,
                91,
                91,
                91,
                91
        };
        int[] maxIterationsList = {
                400,
                250,
                1000,
                600,
                250
        };
        int[][] divisionConfigList = {
                {12},
                {12},
                {12},
                {12},
                {12}
        };
        double[][] tauConfigList = {
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


//        MOEACDSCVStudy moeacdSCVExperiment = new MOEACDSCVStudy();
//        try {
//            moeacdSCVExperiment.execute(baseDir,
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
//        MOEACDPEHStudy moeacdPEHExperiment = new MOEACDPEHStudy();
//        try {
//            moeacdPEHExperiment.execute(baseDir,
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

        MOEACDACVStudy moeacdACVExperiment = new MOEACDACVStudy();
        try {
            moeacdACVExperiment.execute(baseDir,
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

        MOEACDAECVStudy moeacdAECVExperiment = new MOEACDAECVStudy();
        try {
            moeacdAECVExperiment.execute(baseDir,
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

    public static void main(String[] args) {
        analysisQuality computor = new analysisQuality();

        String[] algorithmNameList = {
//                "NSGAIII",
//                "MOEADD_PBI",
//                "MOEAD_PBI",
//                "MOEADAGRSBX_PBI",
//               "MOEADACDSBX_PBI"
//                ,
//                "MOEADDE_PBI",
//                "MOEADAGR_PBI",
//                "MOEADACD_PBI"
//                ,
//                "MOEACD"
//                ,
//                "MOEACD-DE"
//                ,
                "MOEACD-SBX"
//                ,
//                "MOEACD-F"
//                "MOEACD-V",
//                "MOEACD-PEH",
//                "U-EACD",
//                "U-EACD-ACV",
//                "U-EACD-AECV"
//                "MOEACD"
//                ,
//                "MOEACD-ACV"
//                ,
//                "MOEACD-AECV"
//                "UDEA"
//                "U-CDEA"
        };

        int approximateDim = 10;
        int numOfSample = 1000000;

        int maxRunMOP = 20;
        MyExperimentMOP experimentMOP = new MyExperimentMOP();
        String baseDirMOP = "E:\\ResultsMOPMOEACD_O_B3_0_SB20\\";
//        String baseDirMOP = "E:\\ResultsMOPMeasureTrend\\";
//        String baseDirMOP = "E:\\ResultsMOPFinalMOEADAGR\\";
      String statDirMOP = baseDirMOP + "stat\\";
//        experimentMOP.executeMOP(baseDirMOP,maxRunMOP);
//        computor.executeMOPFinal(baseDirMOP,baseDirMOP,maxRunMOP,algorithmNameList,approximateDim,numOfSample);

//        experimentMOP.executeMOPMeasure(baseDirMOP,maxRunMOP);
//        computor.executeMOPMeasure(baseDirMOP,baseDirMOP,maxRunMOP,algorithmNameList,approximateDim,numOfSample);

        MyExperimentMaOP experimentMaOP = new MyExperimentMaOP();
        int maxRunMaOP = 20;

        String baseDirMaOP = "E:\\ResultsMaOPMOEACD_FFF22\\";
//        String baseDirMaOP = "E:\\ResultsMaOPMOEACDWithEvolving\\";
//        String baseDirMaOP = "E:\\ResultsMaOPMOEACDWithNeighbor\\";
//        String baseDirMaOP = "E:\\ResultsMaOPMeasureTrend\\";
//       String statDirMaOP = baseDirMaOP+"sta  t\\";
//        String baseDirMaOP = "E:\\ResultsMaOPMOEACDACV\\";
        experimentMaOP.executeDTLZ(baseDirMaOP,maxRunMaOP);
        computor.executeMaOPFinal(baseDirMaOP,baseDirMaOP,maxRunMaOP,algorithmNameList,approximateDim,numOfSample);
//        experimentMaOP.executeDTLZMeasure(baseDirMaOP,maxRunMaOP);
//        computor.executeMaOPMeasure(baseDirMaOP,baseDirMaOP,maxRunMaOP,algorithmNameList,approximateDim,numOfSample);

        String[] methodAlgorithmNameList = {
                "MOEACD-V",
                "MOEACD-PEH",
                "MOEACD-ACV",
                "MOEACD-AECV"
        };

        String baseDirMaOPTestMethod = "E:\\ResultsMaOPMethod\\";
//        experimentMaOP.executeDTLZMethod(baseDirMaOPTestMethod,maxRunMaOP);
//        computor.executeMaOPMethodFinal(baseDirMaOPTestMethod,baseDirMaOPTestMethod,maxRunMaOP,methodAlgorithmNameList,approximateDim,numOfSample);

        String[] algorithmNameList1 = {
                "NSGAIII",
                "MOEADN_PBI",
                 "MOEADDN_PBI"
//                ,
//                "MOEACD"
//                ,
//                "MOEACD-N"
//                ,
//                "MOEACD-N-SBX"
//                ,"MOEACD-N-F"
        };

        int maxRunMaOPScale = 20;
//        String baseDirMaOPScale = "E:\\ResultsMaOPScaleCompare\\";
//        String baseDirMaOPScale = "D:\\Experiments\\ExperimentDataThesis\\ScaledDTLZ\\compare\\";

        String baseDirMaOPScale = "E:\\ResultsScaleDTLZCompareNew\\";
//        String baseDirMaOPScale = "E:\\ResultsScaleDTLZMOEACDWithoutEvolving\\";
//        String baseDirMaOPScale = "E:\\ResultsScaleDTLZMOEACDWithoutNeighbor\\";
//  String statDirMaOPScale = baseDirMaOPScale+"stat2\\";
////
//        experimentMaOP.executeDTLZScale(baseDirMaOPScale,maxRunMaOPScale);
////        computor.executeMaOPScale(baseDirMaOPScale,baseDirMaOPScale,maxRunMaOPScale,algorithmNameList1,approximateDim,numOfSample);
//        computor.executeMaOPScaleHVFinal(baseDirMaOPScale,baseDirMaOPScale,maxRunMaOPScale,algorithmNameList1,approximateDim,numOfSample);

        MyExperimentEngineer engineer = new MyExperimentEngineer();
//
        int maxRun = 20;

        String[] algorithmNameListUn = {
                "MOEADN_PBI"
                ,
                "MOEADDN_PBI"
//                ,
//                "MOEACD"
//                ,
//                "MOEACD-N"
//                ,
//                "MOEACD-N-SBX"
//                ,
//                "MOEACD-ND"
        };

        String baseDirUn= "E:\\ResultsEngineerUnConstraintsCompareNew\\";
//        String baseDirUn= "E:\\ResultsEngineerUnConstraintsMOEACDWithoutEvolving\\";
//        String baseDirUn= "E:\\ResultsEngineerUnConstraintsMOEACDWithoutNeighbor\\";
//        String baseDirUn= "E:\\ResultsEngineerUnConstraintsFinalCompare\\";

//        engineer.executeUnConstrainedEngineer(baseDirUn,maxRun);
//        computor.executeUnConstrainedEngineerFinal(baseDirUn,baseDirUn,maxRun,algorithmNameListUn,approximateDim,numOfSample);


        MyExperimentIrregular experimentIrregular = new MyExperimentIrregular();

        int maxRunIrregular = 20;
//        String baseDirIrregular = "D:\\Experiments\\ExperimentDataThesis\\Irregular\\MOEACD\\";
        String baseDirIrregular = "E:\\ResultsIrregularMOEACD50\\";
//        String baseDirIrregular = "E:\\ResultsIrregularMOEACD0\\";
//        experimentIrregular.executeIrregular(baseDirIrregular,maxRunIrregular);
        String[] algorithmNameListIrregular = {
//                "MOEAD_PBI",
//                "MOEADD_PBI"
//                ,
                "MOEACD"
                ,
                "MOEACD-D"
        };
//        computor.executeIrregularFinal(baseDirIrregular,baseDirIrregular,maxRunIrregular,algorithmNameListIrregular,approximateDim,numOfSample);


        String[] algorithmNameListConstraints = {
//                "CMOEAD_PBI",
////                "CMOEADD_PBI"
////                ,
                "C-MOEACD"
                ,
                "C-MOEACD-A"
//                ,
//                "C-MOEACD-D",
//                "C-MOEACD-AD"
//                ,
//                "C-MOEACD-II"
//                ,
//                "C-MOEACD-A-II"
//                "C-MOEACD-III"
//                "C-MOEACD-0"
        };

        MyExperimentConstraints experimentConstraints = new MyExperimentConstraints();

        int maxRunConstraints = 20;
//
//        String baseDirConstraints = "E:\\ResultsConstraintsFinalCompare\\";
//        String baseDirConstraints = "D:\\Experiments\\ExperimentDataThesis\\ConstrainedDTLZ\\compare\\";
//        String baseDirConstraints = "E:\\ResultsConstraintsFinalFinalMOEACDFinal5\\";
//        String baseDirConstraints = "E:\\ResultsConstraintsCompare\\";
        String baseDirConstraints = "E:\\ResultsConstraintsMOEACD50\\";
//         String baseDirConstraints = "E:\\ResultsConstraintsIrregularMOEACD30\\";
//        String baseDirConstraints = "E:\\ResultsIrregularApproximate\\";

//        String statDirConstraints = baseDirConstraints+"stat\\";
//        experimentConstraints.executeDTLZConstraints(baseDirConstraints,maxRunConstraints);
//        computor.executeConstraintsFinal(baseDirConstraints,baseDirConstraints,maxRunConstraints,algorithmNameListConstraints,approximateDim,numOfSample);

    }
}