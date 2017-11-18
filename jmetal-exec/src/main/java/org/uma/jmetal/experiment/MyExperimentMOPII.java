//package org.uma.jmetal.experiment;
//
//import org.uma.jmetal.experiment.MOEACD.*;
//import org.uma.jmetal.experiment.dataAnalysis.analysisQuality;
//import org.uma.jmetal.problem.Problem;
//import org.uma.jmetal.problem.multiobjective.dtlz.*;
//import org.uma.jmetal.problem.multiobjective.mop.*;
//import org.uma.jmetal.problem.multiobjective.zdt.*;
//import org.uma.jmetal.solution.DoubleSolution;
//import org.uma.jmetal.util.point.Point;
//import org.uma.jmetal.util.point.impl.ArrayPoint;
//
//import java.io.FileNotFoundException;
//import java.util.Arrays;
//import java.util.List;
//
///**
// */
//public class MyExperimentMOPII {
//    public void executeMOPMeasure(String baseDir,int maxRun) {
//        double crossoverProbability = 1.0;
//        double crossoverDistributionIndex = 30.0;
//        double crossoverDistributionIndexNSGAIII = 30.0;
//        double f = 0.5;
//        double mutationDistributionIndex = 20.0;
//        int neighborSize = 20;
//        int indicatorEvaluatingTimes = 20;
//        int neighborhoodSize = 20;
//        double neighborhoodSelectionProbability = 0.9;
//        int extraExtremeSize = 2;
//        double pbi_theta = 5.0;
////        int maxRun = 20;
//
//        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new MOP1(), new MOP2(), new MOP3(), new MOP4(), new MOP5(),new MOP6(),new MOP7()
//        );
//
//        int[] popsList = {
//                101,101,101,101,101,316,316
//        };
//        int[] maxIterationsList = {
//                3000,3000,3000,3000,3000,3000,3000
//        };
//        int[][] divisionConfigList = {
//                {50}, {50}, {50}, {50}, {50},{14},{14}
//        };
//        double[][] tauConfigList = {
//                {1}, {1}, {1}, {1}, {1},{1},{1}
//        };
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "MOP1.2D.pf[2000]",
//                frontDir + "MOP2.2D.pf[2000]",
//                frontDir + "MOP3.2D.pf[2000]",
//                frontDir + "MOP4.2D.pf[2000]",
//                frontDir + "MOP5.2D.pf[2000]",
//                frontDir + "MOP6.3D.pf[5050]",
//                frontDir + "MOP7.3D.pf[5050]"
//        };
//        Point refP2D = new ArrayPoint(new double[]{11, 11 });
//        Point refP3D = new ArrayPoint(new double[]{11, 11, 11});
//        Point[] hvRefPointList = {
//                refP2D, refP2D, refP2D, refP2D, refP2D,refP3D,refP3D
//        };
//
//        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
////        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
////        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
////        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
//        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
//        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);
////
////        NSGAIIIStudy nsgaiiiExperiment = new NSGAIIIStudy();
////        try {
////            nsgaiiiExperiment.executeMeasure(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndexNSGAIII,
////                    mutationDistributionIndex,
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
////        MOEADStudy moeadExperiment = new MOEADStudy();
////        try {
////            moeadExperiment.executeMeasure(baseDir,
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
////
//
////        MOEADDEStudy moeaddeExperiment = new MOEADDEStudy();
////        try {
////            moeaddeExperiment.executeMeasure(baseDir,
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
////        MOEADACDStudy moeadacdExperiment = new MOEADACDStudy();
////        try {
////            moeadacdExperiment.executeMeasure(baseDir,
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
////        MOEADDStudy moeaddExperiment = new MOEADDStudy();
////        try {
////            moeaddExperiment.executeMeasure(baseDir,
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
////        MOEACDPBIStudy moeacdpbiExperiment = new MOEACDPBIStudy();
////        try {
////            moeacdpbiExperiment.executeMeasure(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborhoodSize,
////                    neighborhoodSelectionProbability,
////                    extraExtremeSize,
////                    pbi_theta,
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
////        MOEACDPBISStudy moeacdpbisExperiment = new MOEACDPBISStudy();
////        try {
////            moeacdpbisExperiment.executeMeasure(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborhoodSize,
////                    neighborhoodSelectionProbability,
////                    extraExtremeSize,
////                    pbi_theta,
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
////        MOEACDPBIANStudy moeacdpbianExperiment = new MOEACDPBIANStudy();
////        try {
////            moeacdpbianExperiment.executeMeasure(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborhoodSize,
////                    neighborhoodSelectionProbability,
////                    extraExtremeSize,
////                    pbi_theta,
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
////        MOEACDFPBIStudy moeacdfpbiExperiment = new MOEACDFPBIStudy();
////        try {
////            moeacdfpbiExperiment.executeMeasure(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborhoodSize,
////                    neighborhoodSelectionProbability,
////                    extraExtremeSize,
////                    100,
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
////        MOEACDAPBIStudy moeacdapbiExperiment = new MOEACDAPBIStudy();
////        try {
////            moeacdapbiExperiment.executeMeasure(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborhoodSize,
////                    neighborhoodSelectionProbability,
////                    extraExtremeSize,
////                    pbi_theta,
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
////        MOEACDFAPBIStudy moeacdfapbiExperiment = new MOEACDFAPBIStudy();
////        try {
////            moeacdfapbiExperiment.executeMeasure(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborhoodSize,
////                    neighborhoodSelectionProbability,
////                    extraExtremeSize,
////                    pbi_theta,
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
////        MOEACDFBAStudy moeacdfbaExperiment = new MOEACDFBAStudy();
////        try {
////            moeacdfbaExperiment.executeMeasure(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborhoodSize,
////                    neighborhoodSelectionProbability,
////                    extraExtremeSize,
////                    500,
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
////        MOEACDLHVStudy moeacdlhvExperiment = new MOEACDLHVStudy();
////        try {
////            moeacdlhvExperiment.executeMeasure(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborhoodSize,
////                    neighborhoodSelectionProbability,
////                    extraExtremeSize,
////                    500,
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
////        MOEACDStudy moeacdExperiment = new MOEACDStudy();
////        try {
////            moeacdExperiment.executeMeasure(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborhoodSize,
////                    neighborhoodSelectionProbability,
////                    extraExtremeSize,
////                    500,
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
////        MOEACDNStudy moeacdnExperiment = new MOEACDNStudy();
////        try {
////            moeacdnExperiment.executeMeasure(baseDir,
////                    crossoverProbability,
////                    crossoverDistributionIndex,
////                    mutationDistributionIndex,
////                    neighborhoodSize,
////                    neighborhoodSelectionProbability,
////                    extraExtremeSize,
////                    500,
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
//        MOEACDStudy moeacdExperiment = new MOEACDStudy();
//        try {
//            moeacdExperiment.executeMeasure(baseDir,
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
//        MOEACDNStudy moeacdnExperiment = new MOEACDNStudy();
//        try {
//            moeacdnExperiment.executeMeasure(baseDir,
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
//
//    }
//
//    public static void main(String[] args) {
////        MyExperimentMOPII experiment = new MyExperimentMOPII();
////
////        String baseDir = "E:\\ResultsMOPMeasure13\\";
////        int maxRun = 20;
////        experiment.executeMOPMeasure(baseDir,maxRun);
////
////        analysisQuality computor = new analysisQuality();
////        computor.executeMOPMeasure(baseDir,baseDir,maxRun);
//    }
//}