package org.uma.jmetal.experiment;

import org.uma.jmetal.experiment.MOEACD.*;
import org.uma.jmetal.experiment.dataAnalysis.analysisQuality;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.CPFT.*;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by X250 on 2016/9/24.
 */
public class MyExperimentIrregular {

    public void executeIrregular(String baseDir,int maxRun) {
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
//                new DTLZ5(12,3),
//                new DTLZ6(12,3),
//                new DTLZ7(12,3),
//                new Inverted_DTLZ1(7,3)
//        );
//
//        int[] popsList = {
//                91,
//                91,
//                91,
//                91
//        };
//
//        int[] maxIterationsList = {
//                500,
//                500,
//                500,
//                500
//        };
//
//        int[][] divisionConfigList = {
//                {12},
//                {12},
//                {12},
//                {12}
//        };
//
//        double[][] tauConfigList = {
//                {1.0},
//                {1.0},
//                {1.0}
//                , {1.0}
//        };

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ5(12,3),
                new DTLZ5(14, 5), new DTLZ5(17, 8), new DTLZ5(19, 10),new DTLZ5(24, 15),
                new DTLZ6(12,3),
                new DTLZ6(14, 5), new DTLZ6(17, 8), new DTLZ6(19, 10),new DTLZ6(24, 15)
                ,
                new DTLZ7(12,3),
                new DTLZ7(14, 5), new DTLZ7(17, 8), new DTLZ7(19, 10),
                new DTLZ7(24, 15)
                ,
                new Inverted_DTLZ1(7,3)
                , new Inverted_DTLZ1(9, 5), new Inverted_DTLZ1(12, 8), new Inverted_DTLZ1(14, 10),new Inverted_DTLZ1(19, 15)
//                new CPFT1(),new CPFT2(),new CPFT3(),new CPFT4(),new CPFT5(),new CPFT6(2),new CPFT7(2),new CPFT8(2)
        );
////
        int[] popsList = {
                91,
                210, 156, 275,135,
                91,
                210, 156, 275,135,
                91,
                210, 156, 275,135,
                91
                , 210, 156, 275,135
//                300,300,300,300,300,300,300,300
        };

        int[] maxIterationsList = {
//                2000,
//                1000,1000,1500,2000,
//                2000,
//                1000,1000,1500,2000,
//                2000
//                ,1000,1000,1500,2000,
//                2000
//                ,1000,1000,1500,2000
                 500,
                750,1000,1500,2000,
                500,
                750,1000,1500,2000,
                500,
                750,1000,1500,2000,
                500
                ,750,1000,1500,2000
//                ,
//                2000,2000,2000,2000,2000,
//                2000,2000,2000,2000,2000,
//                2000,2000,2000,2000,2000,
//                2000,2000,2000,2000,2000
//                2000,2000,2000,2000,2000,2000,2000,2000
        };

        int[][] divisionConfigList = {
                {12},
                {6}, {2, 3}, {2, 3},{1,2},
                {12},
                {6}, {2, 3}, {2, 3},{1,2},
                {12},
                {6}, {2, 3}, {2, 3},{1,2},
                {12}
                ,{6}, {2, 3}, {2, 3},{1,2}
//                {23},{23},{23},{23},{23},{23},{23},{23}
        };

        double[][] tauConfigList = {
                {1.0},
                {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0},
                {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0},
                {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0},
                {1.0}
                , {1.0}, {0.5, 1.0}, {0.5, 1.0},{0.5,1.0}
//                {1.0},{1.0},{1.0},{1.0},{1.0},{1.0},{1.0},{1.0}
        };

//        int[] popsList = {
//                10011,20475,31824,35750,50388,
//                10011,20475,31824,35750,50388,
//                10011,20475,31824,35750,50388,
//                10011,20475,31824,35750,50388
//        };
//        int[] maxIterationsList = {
//                2000,2000,2000,2000,2000,
//                2000,2000,2000,2000,2000,
//                2000,2000,2000,2000,2000,
//                2000,2000,2000,2000,2000
//        };
//        int[][] divisionConfigList = {
//                {140},{26},{11},{7,8},{5,6},
//                {140},{26},{11},{7,8},{5,6},
//                {140},{26},{11},{7,8},{5,6},
//                {140},{26},{11},{7,8},{5,6}
//        };
//        double[][] tauConfigList = {
//                {1.0}, {1.0}, {1.0},{0.5, 1.0}, {0.5, 1.0},
//                {1.0}, {1.0}, {1.0},{0.5, 1.0}, {0.5, 1.0},
//                {1.0}, {1.0}, {1.0},{0.5, 1.0}, {0.5, 1.0},
//                {1.0}, {1.0}, {1.0},{0.5, 1.0}, {0.5, 1.0}
//        };
//        int[] popsList = {
//                10011,10626,11440,16445,14688,
//                10011,10626,11440,16445,14688,
//                10011
//                ,10626,11440,16445,14688,
//                10011,10626,11440,16445,
//                14688
//        };
////        int[] maxIterationsList = {
////                2000,2000,2000,2000,2000,
////                2000,2000,2000,2000,2000,
////                2000
////                ,2000,2000,2000,2000,
////                2000,2000,2000,2000,
////                2000
////        };
//        int[] maxIterationsList = {
//                2000,2500,3000,3500,4000,
//                2000,2500,3000,3500,4000,
//                2000,2500,3000,3500,4000,
//                2000,2500,3000,3500,4000
//        };
//        int[][] divisionConfigList = {
//                {140},{20},{9},{6,7},{4,5},
//                {140},{20},{9},{6,7},{4,5},
//                {140}
//                ,{20},{9},{6,7},{4,5},
//                {140},{20},{9},{6,7},
//                {4,5}
//        };
//        double[][] tauConfigList = {
//                {1.0}, {1.0}, {1.0},{0.5, 1.0}, {0.5, 1.0},
//                {1.0}, {1.0}, {1.0},{0.5, 1.0}, {0.5, 1.0},
//                {1.0}
//                , {1.0}, {1.0},{0.5, 1.0}, {0.5, 1.0},
//                {1.0}, {1.0}, {1.0},{0.5, 1.0},
//                {0.5, 1.0}
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
//
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

        MOEACDStudy moeacdExperiment = new MOEACDStudy();
        try {
            moeacdExperiment.execute(baseDir,
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

        MOEACDDStudy moeacdDExperiment = new MOEACDDStudy();
        try {
            moeacdDExperiment.execute(baseDir,
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
    }

    public static void main(String[] argv){
        int maxRunIrregular = 20;
        MyExperimentIrregular experimentIrregular = new MyExperimentIrregular();

//        String baseDirIrregular = "D:\\Experiments\\ExperimentDataThesis\\Irregular\\MOEACD\\";
        String baseDirIrregular = "E:\\ResultsIrregularMOEACD37\\";
//        String baseDirIrregular = "E:\\ResultsIrregularMOEACD0\\";
        experimentIrregular.executeIrregular(baseDirIrregular,maxRunIrregular);
        analysisQuality computor = new analysisQuality();
        String[] algorithmNameList = {
//                "MOEAD_PBI",
//                "MOEADD_PBI"
//                ,
                "MOEACD"
                ,
                "MOEACD-D"
        };

        int approximateDim = 10;
        int numOfSample = 1000000;
        computor.executeIrregularFinal(baseDirIrregular,baseDirIrregular,maxRunIrregular,algorithmNameList,approximateDim,numOfSample);
    }
}
