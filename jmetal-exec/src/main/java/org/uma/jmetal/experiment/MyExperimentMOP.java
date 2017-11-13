package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.multiobjective.moead.MOEADGRSBX;
import org.uma.jmetal.experiment.MOEACD.*;
import org.uma.jmetal.experiment.UDEA.UDEAStudy;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.problem.multiobjective.mop.*;
import org.uma.jmetal.problem.multiobjective.zdt.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by X250 on 2016/5/4.
 */
public class MyExperimentMOP {

    public void executeMOPMeasure(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes = 20;
        int neighborhoodSize = 20;
        double neighborhoodSelectionProbability = 0.9;
        double pbi_theta = 5.0;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new MOP1(), new MOP2(), new MOP3(), new MOP4(), new MOP5(),new MOP6(),new MOP7()
        );

        int[] popsList = {
                100,100,100,100,100,300,300
        };
        int[] maxIterationsList = {
                3000,3000,3000,3000,3000,3000,3000
        };
        int[][] divisionConfigList = {
                {99}, {99}, {99}, {99}, {99},{23},{23}
        };
        double[][] tauConfigList = {
                {1.0}, {1.0}, {1.0}, {1.0}, {1.0},{1.0},{1.0}
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "MOP1.2D.pf[2000]",
                frontDir + "MOP2.2D.pf[2000]",
                frontDir + "MOP3.2D.pf[2000]",
                frontDir + "MOP4.2D.pf[2000]",
                frontDir + "MOP5.2D.pf[2000]",
                frontDir + "MOP6.3D.pf[5050]",
                frontDir + "MOP7.3D.pf[5050]"
        };
        Point refP2D = new ArrayPoint(new double[]{11, 11 });
        Point refP3D = new ArrayPoint(new double[]{11, 11, 11});
        Point[] hvRefPointList = {
                refP2D, refP2D, refP2D, refP2D, refP2D,refP3D,refP3D
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
////
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
//
    }

    public void executeMOP(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 20.0;
        double crossoverDistributionIndexNSGAIII = 20.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes = 20;
        int neighborhoodSize = 20;
        double neighborhoodSelectionProbability = 0.9;
        double pbi_theta = 5.0;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new MOP1(), new MOP2(), new MOP3(), new MOP4(), new MOP5(),new MOP6(),new MOP7()
        );

        int[] popsList = {
                100,100,100,100,100,300,300
        };
        int[] maxIterationsList = {
                3000,3000,3000,3000,3000,3000,3000
        };

        int[][] divisionConfigList = {
                {99}, {99}, {99}, {99}, {99},{23},{23}
        };

        double[][] tauConfigList = {
                {1.0}, {1.0}, {1.0}, {1.0}, {1.0},{1.0},{1.0}
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "MOP1.2D.pf[2000]",
                frontDir + "MOP2.2D.pf[2000]",
                frontDir + "MOP3.2D.pf[2000]",
                frontDir + "MOP4.2D.pf[2000]",
                frontDir + "MOP5.2D.pf[2000]",
                frontDir + "MOP6.3D.pf[5050]",
                frontDir + "MOP7.3D.pf[5050]"
        };
        Point refP2D = new ArrayPoint(new double[]{11, 11 });
        Point refP3D = new ArrayPoint(new double[]{11, 11, 11});
        Point[] hvRefPointList = {
                refP2D, refP2D, refP2D, refP2D, refP2D,refP3D,refP3D
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
////
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
//        }
//
        MOEADACDSBXStudy moeadacdSBXExperiment = new MOEADACDSBXStudy();
        try {
            moeadacdSBXExperiment.execute(baseDir,
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
        MOEADAGRSBXStudy moeadagrSBXExperiment = new MOEADAGRSBXStudy();
        try {
            moeadagrSBXExperiment.execute(baseDir,
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
////
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
//        MOEACDEHAStudy moeacdEHAExperiment = new MOEACDEHAStudy();
//        try {
//            moeacdEHAExperiment.execute(baseDir,
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

//
//        UDEAStudy udeaExperiment = new UDEAStudy();
//        try {
//            udeaExperiment.execute(baseDir,
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
    }

    public void executeZDTMeasure(String baseDir) {

        double crossoverProbability = 0.9;
        double crossoverDistributionIndex = 20.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes = 20;
        int neighborhoodSize = 20;
        double extremeRegionEnhancingSelectionProbability = 0.1;
        double neighborhoodSelectionProbability = 0.9;
        int extraExtremeSize = 2;
        double pbi_theta = 5.0;
        int maxRun = 20;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new ZDT1(), new ZDT2(), new ZDT3(), new ZDT4(), new ZDT6()
        );
        int[] popsList = {
                100, 100, 100, 100, 100
        };
//        int[] maxIterationsList = {
//                200, 200, 200, 1000, 200
//        };
        int[] maxIterationsList = {
                1000,1000,1000,1000,1000,1000,1000
        };
        int[][] divisionConfigList = {
                {99}, {99}, {99}, {99}, {99}
        };
        double[][] tauConfigList = {
                {1}, {1}, {1}, {1}, {1}
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "ZDT1.2D.pf[2000]",
                frontDir + "ZDT2.2D.pf[2000]",
                frontDir + "ZDT3.2D.pf[2000]",
                frontDir + "ZDT4.2D.pf[2000]",
                frontDir + "ZDT6.2D.pf[2000]"
        };
        Point refP = new ArrayPoint(new double[]{11, 11});
        Point[] hvRefPointList = {
                refP, refP, refP, refP, refP
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
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


    }


    public void executeZDT(String baseDir) {

        double crossoverProbability = 0.9;
        double crossoverDistributionIndex = 20.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes = 20;
        int neighborhoodSize = 20;
        double extremeRegionEnhancingSelectionProbability = 0.1;
        double neighborhoodSelectionProbability = 0.9;
        int extraExtremeSize = 2;
        double pbi_theta = 5.0;
        int maxRun = 20;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new ZDT1(), new ZDT2(), new ZDT3(), new ZDT4(), new ZDT6()
        );
        int[] popsList = {
                100, 100, 100, 100, 100
        };
//        int[] maxIterationsList = {
//                200, 200, 200, 1000, 200
//        };
        int[] maxIterationsList = {
                1000,1000,1000,1000,1000,1000,1000
        };
        int[][] divisionConfigList = {
                {99}, {99}, {99}, {99}, {99}
        };
        double[][] tauConfigList = {
                {1}, {1}, {1}, {1}, {1}
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "ZDT1.2D.pf[2000]",
                frontDir + "ZDT2.2D.pf[2000]",
                frontDir + "ZDT3.2D.pf[2000]",
                frontDir + "ZDT4.2D.pf[2000]",
                frontDir + "ZDT6.2D.pf[2000]"
        };
        Point refP = new ArrayPoint(new double[]{11, 11});
        Point[] hvRefPointList = {
                refP, refP, refP, refP, refP
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
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

    }

    public void executeDTLZMeasure(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes = 20;
        int neighborhoodSize = 20;
        double extremeRegionEnhancingSelectionProbability = 0.1;
        double neighborhoodSelectionProbability = 0.9;
        int extraExtremeSize = 2;
        double pbi_theta = 5.0;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(6, 2),new DTLZ2(11, 2), new DTLZ3(11, 2),new DTLZ4(11, 2),new Convex_DTLZ2(11, 2),
                new DTLZ1(7, 3),new DTLZ2(12, 3), new DTLZ3(12, 3),new DTLZ4(12, 3),new Convex_DTLZ2(12, 3)
        );
        int[] popsList = {
                100, 100, 100, 100, 100,
                300, 300, 300, 300, 300
        };
//        int[] maxIterationsList = {
//                500,500,500,500,500,
//                750,750,750,750,750
//        };
        int[] maxIterationsList = {
                1000,1000,1000,1000,1000,
                1000,1000,1000,1000,1000
        };
        int[][] divisionConfigList = {
                {99}, {99}, {99}, {99}, {99},
                {23}, {23}, {23}, {23}, {23}
        };
        double[][] tauConfigList = {
                {1}, {1}, {1}, {1}, {1},
                {1}, {1}, {1}, {1}, {1}
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "DTLZ1.2D.pf[2000]",
                frontDir + "DTLZ2.2D.pf[2000]",
                frontDir + "DTLZ3.2D.pf[2000]",
                frontDir + "DTLZ4.2D.pf[2000]",
                frontDir + "Convex_DTLZ2.2D.pf[2000]",
                frontDir + "DTLZ1.3D.pf[5050]",
                frontDir + "DTLZ2.3D.pf[5050]",
                frontDir + "DTLZ3.3D.pf[5050]",
                frontDir + "DTLZ4.3D.pf[5050]",
                frontDir + "Convex_DTLZ2.3D.pf[5050]"
        };
        Point refP2D = new ArrayPoint(new double[]{11, 11});
        Point refP3D = new ArrayPoint(new double[]{11, 11, 11});
        Point[] hvRefPointList = {
                refP2D, refP2D, refP2D, refP2D, refP2D,
                refP3D, refP3D, refP3D, refP3D, refP3D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
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

    }

    public void executeDTLZ(String baseDir) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes = 20;
        int neighborhoodSize = 20;
        double extremeRegionEnhancingSelectionProbability = 0.1;
        double neighborhoodSelectionProbability = 0.9;
        int extraExtremeSize = 2;
        double pbi_theta = 5.0;
        int maxRun = 20;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(6, 2),new DTLZ2(11, 2), new DTLZ3(11, 2),new DTLZ4(11, 2),new Convex_DTLZ2(11, 2),
                new DTLZ1(7, 3),new DTLZ2(12, 3), new DTLZ3(12, 3),new DTLZ4(12, 3),new Convex_DTLZ2(12, 3)
        );
        int[] popsList = {
                100, 100, 100, 100, 100,
                300, 300, 300, 300, 300
        };
//        int[] maxIterationsList = {
//                500,500,500,500,500,
//                750,750,750,750,750
//        };
        int[] maxIterationsList = {
                1000,1000,1000,1000,1000,
                1000,1000,1000,1000,1000
        };
        int[][] divisionConfigList = {
                {99}, {99}, {99}, {99}, {99},
                {23}, {23}, {23}, {23}, {23}
        };
        double[][] tauConfigList = {
                {1}, {1}, {1}, {1}, {1},
                {1}, {1}, {1}, {1}, {1}
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "DTLZ1.2D.pf[2000]",
                frontDir + "DTLZ2.2D.pf[2000]",
                frontDir + "DTLZ3.2D.pf[2000]",
                frontDir + "DTLZ4.2D.pf[2000]",
                frontDir + "Convex_DTLZ2.2D.pf[2000]",
                frontDir + "DTLZ1.3D.pf[5000]",
                frontDir + "DTLZ2.3D.pf[5000]",
                frontDir + "DTLZ3.3D.pf[5000]",
                frontDir + "DTLZ4.3D.pf[5000]",
                frontDir + "Convex_DTLZ2.3D.pf[5000]"
        };
        Point refP2D = new ArrayPoint(new double[]{11, 11});
        Point refP3D = new ArrayPoint(new double[]{11, 11, 11});
        Point[] hvRefPointList = {
                refP2D, refP2D, refP2D, refP2D, refP2D,
                refP3D, refP3D, refP3D, refP3D, refP3D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
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
    }


    public static void main(String[] args) {

    }
}