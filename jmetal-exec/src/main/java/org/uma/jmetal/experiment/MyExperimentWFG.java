package org.uma.jmetal.experiment;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.SDTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.SDTLZ2;
import org.uma.jmetal.problem.multiobjective.wfg.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.util.Arrays;
import java.util.List;

/**
 * Created by X250 on 2016/9/3.
 */
public class MyExperimentWFG {

    public void executeWFG(String baseDir,int maxRun) {
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

        List<Problem<DoubleSolution>> problemList;
        try {
            problemList = Arrays.<Problem<DoubleSolution>>asList(
                    new WFG1(4, 20, 3), new WFG1(8, 20, 5), new WFG1(14, 20, 8), new WFG1(18, 20, 10),
                    new WFG2(4, 20, 3), new WFG2(8, 20, 5), new WFG2(14, 20, 8),new WFG2(18, 20, 10),
                    new WFG3(4, 20, 3), new WFG3(8, 20, 5), new WFG3(14, 20, 8),new WFG3(18, 20, 10),
                    new WFG4(4, 20, 3), new WFG4(8, 20, 5), new WFG4(14, 20, 8),new WFG4(18, 20, 10),
                    new WFG5(4, 20, 3), new WFG5(8, 20, 5), new WFG5(14, 20, 8),new WFG5(18, 20, 10),
                    new WFG6(4, 20, 3), new WFG6(8, 20, 5), new WFG6(14, 20, 8),new WFG6(18, 20, 10),
                    new WFG7(4, 20, 3), new WFG7(8, 20, 5), new WFG7(14, 20, 8),new WFG7(18, 20, 10),
                    new WFG8(4, 20, 3), new WFG8(8, 20, 5), new WFG8(14, 20, 8),new WFG8(18, 20, 10),
                    new WFG9(4, 20, 3), new WFG9(8, 20, 5), new WFG9(14, 20, 8),new WFG9(18, 20, 10)
            );

        }catch (ClassNotFoundException e){}


        int[] popsList = {
                91, 210, 156, 275,
                91, 210, 156, 275,
                91, 210, 156, 275,
                91, 210, 156, 275,
                91, 210, 156, 275,
                91, 210, 156, 275,
                91, 210, 156, 275,
                91, 210, 156, 275,
                91, 210, 156, 275
        };

        int[] maxIterationsList = {
                400,600,750,1000,1500,
                250,350,500,750,1000
//                ,
//                1000,1000,1000,1500,2000,
//                600,1000,1250,2000,3000,
//                250,750,2000,4000,4500
        };
        int[][] divisionConfigList = {
                {12},{6}, {2, 3}, {2, 3},{1,2},
                {12},{6}, {2, 3}, {2, 3},{1,2}
//                ,
//                {12},{6}, {2, 3}, {2, 3},{1,2},
//                {12},{6}, {2, 3}, {2, 3},{1,2},
//                {12},{6}, {2, 3}, {2, 3},{1,2}
        };
        double[][] tauConfigList = {
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0}
//                ,
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
//                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0}
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
//                ,
//                point3D,point5D, point8D, point10D,point15D,
//                point3D,point5D, point8D, point10D,point15D,
//                point3D,point5D, point8D, point10D,point15D
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
                frontDir + "DTLZ2.15D.pf[135]"
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);
//
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
//
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
//
//        MOEADGRNStudy moeadgrnExperiment = new MOEADGRNStudy();
//        try {
//            moeadgrnExperiment.execute(baseDir,
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
//        MOEADAGRNStudy moeadagrnExperiment = new MOEADAGRNStudy();
//        try {
//            moeadagrnExperiment.execute(baseDir,
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
//        MOEACDStudy moeacdExperiment = new MOEACDStudy();
//        try {
//            moeacdExperiment.execute(baseDir,
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

//        MOEACDNStudy moeacdnExperiment = new MOEACDNStudy();
//        try {
//            moeacdnExperiment.execute(baseDir,
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
}
