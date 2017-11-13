package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.multiobjective.moead.*;
import org.uma.jmetal.experiment.IndicatorsListener;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.measure.impl.DurationMeasure;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;

import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by X250 on 2016/4/11.
 */
public class MOEADStudy {

    public void executeMeasure(String baseDir,
                        double crossoverProbability,
                        double crossoverDistributionIndex,
                        double mutationDistributionIndex,
                        int neighborSize,
                        double neighborhoodSelectionProbability,
                        int indicatorEvaluatingTimes,
                        int maxRun,
                        List<Problem<DoubleSolution>> problemList,
                        int[] popsList,
                        int[] maxIterationsList,
                        int[][] divisionConfigList,
                        double[][] tauConfigList,
                        String[] frontFileList,
                        Point[] hvRefPointList,
                        MyExperimentIndicatorConfig indicatorConfig) throws FileNotFoundException{

        SBXCrossover sbxCrossover;
        double mutationProbability;
        MutationOperator<DoubleSolution> mutation;
        int indicatorInterval;
        int maxEvaluations;


        String[] variantName = {
//                "MOEAD_TCH"
// ,
                "MOEAD_PBI"
        };
        AbstractMOEAD.FunctionType[] variantConfig = {
//                AbstractMOEAD.FunctionType.TCH
//                ,
                AbstractMOEAD.FunctionType.PBI
        };

        sbxCrossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

        for(int iProblem=0;iProblem<problemList.size();++iProblem){

            maxEvaluations = popsList[iProblem] * maxIterationsList[iProblem];

            mutationProbability = 1.0 / problemList.get(iProblem).getNumberOfVariables();
            mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

            Front referenceFront  = new ArrayFront(frontFileList[iProblem]);
            indicatorInterval = maxIterationsList[iProblem] / indicatorEvaluatingTimes;

            for(int iV = 0;iV<variantConfig.length;++iV) {
                String instance = variantName[iV]+"_" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                MyExperimentAnalysis experimentAnalysis = new MyExperimentAnalysis(baseDir, instance);

                for (int iRun = 0; iRun < maxRun; ++iRun) {
                    JMetalLogger.logger.info("[" + variantName[iV] +"--"+ problemList.get(iProblem).getName() +"(" + problemList.get(iProblem).getNumberOfObjectives() + ")]  Run : " + iRun);
                    //configure the algorithm
                    AbstractMOEAD<DoubleSolution> algorithm = new MOEADBuilder(problemList.get(iProblem), MOEADBuilder.Variant.MOEADMeasure)
                            .setCrossover(sbxCrossover)
                            .setMutation(mutation)
                            .setMaxEvaluations(maxEvaluations)
                            .setPopulationSize(popsList[iProblem])
                            .setResultPopulationSize(popsList[iProblem])
                            .setNeighborSize(neighborSize)
                            .setNeighborhoodSelectionProbability(neighborhoodSelectionProbability)
                            .setFunctionType(variantConfig[iV])
                            .setNumofDivision(divisionConfigList[iProblem])
                            .setIntegratedTau(tauConfigList[iProblem])
                            .build();

                /* Measure management */
                    MeasureManager measureManager = ((MOEADMeasure) algorithm).getMeasureManager();
                    DurationMeasure currentComputingTime =
                            (DurationMeasure) measureManager.<Long>getPullMeasure("currentExecutionTime");
                    BasicMeasure<List<DoubleSolution>> solutionListMeasure =
                            (BasicMeasure<List<DoubleSolution>>) measureManager.<List<DoubleSolution>>getPushMeasure("currentPopulation");

//                    MyExperimentIndicator experimentIndicator = new MyExperimentIndicator(referenceFront);
//                    WFGHypervolume hvEvaluator = new WFGHypervolume();
//                    hvEvaluator.setReferencePoint(hvRefPointList[iProblem]);
//                    hvEvaluator.setMiniming();
//                    experimentIndicator.addIndicatorEvaluator(hvEvaluator);
//                    experimentIndicator.addIndicatorEvaluator(new InvertedGenerationalDistance());
                    MyExperimentIndicator experimentIndicator = indicatorConfig.generate(referenceFront,hvRefPointList[iProblem]);
                    IndicatorsListener<DoubleSolution> indicatorsListener = new IndicatorsListener<DoubleSolution>(experimentIndicator, maxIterationsList[iProblem],indicatorEvaluatingTimes);//indicatorInterval);
                    solutionListMeasure.register(indicatorsListener);
                /* End of measure management */

                    //run algorithm
                    algorithm.run();

                    //print or save solution or indivcators
                    List<DoubleSolution> population = algorithm.getMeasurePopulation();
                    experimentIndicator = indicatorsListener.getExperimentIndicator();
                    experimentIndicator.setComputingTime(currentComputingTime.get());
                    experimentAnalysis.addIndicator(experimentIndicator);

                    //save final population attained each run
                    experimentAnalysis.printFinalSolutionSet(iRun, population);
                }
                //analizing all statistics atain by algorithm
                experimentAnalysis.analyzingResults();
            }
        }
    }

    public void execute(String baseDir,
                        double crossoverProbability,
                        double crossoverDistributionIndex,
                        double mutationDistributionIndex,
                        int neighborSize,
                        double neighborhoodSelectionProbability,
                        int indicatorEvaluatingTimes,
                        int maxRun,
                        List<Problem<DoubleSolution>> problemList,
                        int[] popsList,
                        int[] maxIterationsList,
                        int[][] divisionConfigList,
                        double[][] tauConfigList,
                        String[] frontFileList,
                        Point[] hvRefPointList,
                        MyExperimentIndicatorConfig indicatorConfig) throws FileNotFoundException{

        SBXCrossover sbxCrossover;
        double mutationProbability;
        MutationOperator<DoubleSolution> mutation;
        int maxEvaluations;


        String[] variantName = {
//                "MOEAD_TCH"
////                ,
                "MOEAD_PBI"
        };
        AbstractMOEAD.FunctionType[] variantConfig = {
//                AbstractMOEAD.FunctionType.TCH
//                ,
                AbstractMOEAD.FunctionType.PBI
        };

        sbxCrossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

        for(int iProblem=0;iProblem<problemList.size();++iProblem){

            maxEvaluations = popsList[iProblem] * maxIterationsList[iProblem];

            mutationProbability = 1.0 / problemList.get(iProblem).getNumberOfVariables();
            mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

            Front referenceFront  = new ArrayFront(frontFileList[iProblem]);

            for(int iV = 0;iV<variantConfig.length;++iV) {
                String instance = variantName[iV]+"_" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                MyExperimentAnalysis experimentAnalysis = new MyExperimentAnalysis(baseDir, instance);

                for (int iRun = 0; iRun < maxRun; ++iRun) {
                    JMetalLogger.logger.info("[" + variantName[iV] +"--"+ problemList.get(iProblem).getName() +"(" + problemList.get(iProblem).getNumberOfObjectives() + ")]  Run : " + iRun);
                    //configure the algorithm
                    AbstractMOEAD<DoubleSolution> algorithm = new MOEADBuilder(problemList.get(iProblem), MOEADBuilder.Variant.MOEAD)
                            .setCrossover(sbxCrossover)
                            .setMutation(mutation)
                            .setMaxEvaluations(maxEvaluations)
                            .setPopulationSize(popsList[iProblem])
                            .setResultPopulationSize(popsList[iProblem])
                            .setNeighborSize(neighborSize)
                            .setNeighborhoodSelectionProbability(neighborhoodSelectionProbability)
                            .setFunctionType(variantConfig[iV])
                            .setNumofDivision(divisionConfigList[iProblem])
                            .setIntegratedTau(tauConfigList[iProblem])
                            .build();


                    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                            .execute() ;

                    List<DoubleSolution> population = algorithm.getMeasurePopulation() ;

                    MyExperimentIndicator experimentIndicator = indicatorConfig.generate(referenceFront,hvRefPointList[iProblem]);
                    experimentIndicator.computeQualityIndicators(maxIterationsList[iProblem],population);
                    experimentIndicator.setComputingTime(algorithmRunner.getComputingTime());
                    experimentAnalysis.addIndicator(experimentIndicator);

                    //save final population attained each run
                    experimentAnalysis.printFinalSolutionSet(iRun, population);
                }
                //analizing all statistics atain by algorithm
                experimentAnalysis.analyzingResults();
            }
        }
    }

    public static void main(String[] args) throws IOException{
        double crossoverProbability = 0.9 ;
        double crossoverDistributionIndex = 20.0 ;
        double f = 0.5 ;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        double neighborhoodSelectionProbability =0.9;
        int indicatorEvaluatingTimes = 1;
        int maxRun = 1;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
//                new DTLZ1(7,3),new DTLZ1(9,5),new DTLZ1(12,8),new DTLZ1(14,10),
//                new DTLZ1(19,15)
//                ,
//                new DTLZ2(12,3),new DTLZ2(14,5),new DTLZ2(17,8),new DTLZ2(19,10),
 new DTLZ2(24,15)
//                ,
//                new DTLZ3(12,3),new DTLZ3(14,5),new DTLZ3(17,8),new DTLZ3(19,10),new DTLZ3(24,15),
//                new DTLZ4(12,3),new DTLZ4(14,5),new DTLZ4(17,8),new DTLZ4(19,10),new DTLZ4(24,15)
        );
        int[] popsList = {
//                91,210,156,275,
//                135
//                ,
//                91,210,156,275,
 135
//                ,
//                91,210,156,275,135,
//                91,210,156,275,135,
        };
        int[] maxIterationsList = {
//                400,600,750,1000,
//                1500,
//                250,350,500,750,
 1000
//                ,
//                1000,1000,1000,1000,1000,
//                600,1000,1250,2000,3000
        };
        int[][] divisionConfigList = {
//                {12},{6},{2,3},{2,3},
//                {1,2}
//                ,
//                {12},{6},{2,3},{2,3},
 {1,2}
//                ,
//                {12},{6},{2,3},{2,3},{1,2},
//                {12},{6},{2,3},{2,3},{1,2}
        };
        double[][] tauConfigList = {
//                {1.0},{1.0},{0.5,1.0},{0.5,1.0},
//                {0.5,1.0}
//                ,
//                {1.0},{1.0},{0.5,1.0},{0.5,1.0},
 {0.5,1.0}
//                ,
//                {1.0},{1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0},
//                {1.0},{1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0}
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
//                frontDir+"DTLZ1.3D.pf[91]",
//                frontDir+"DTLZ1.5D.pf[210]",
//                frontDir+"DTLZ1.8D.pf[156]",
//                frontDir+"DTLZ1.10D.pf[275]",
//                frontDir+"DTLZ1.15D.pf[135]"
//                ,
//                frontDir+"DTLZ2.3D.pf[91]",
//                frontDir+"DTLZ2.5D.pf[210]",
//                frontDir+"DTLZ2.8D.pf[156]",
//                frontDir+"DTLZ2.10D.pf[275]",
                frontDir+"DTLZ2.15D.pf[135]"
//                ,
//                frontDir+"DTLZ3.3D.pf[91]",
//                frontDir+"DTLZ3.5D.pf[210]",
//                frontDir+"DTLZ3.8D.pf[156]",
//                frontDir+"DTLZ3.10D.pf[275]",
//                frontDir+"DTLZ3.15D.pf[135]",
//                frontDir+"DTLZ4.3D.pf[91]",
//                frontDir+"DTLZ4.5D.pf[210]",
//                frontDir+"DTLZ4.8D.pf[156]",
//                frontDir+"DTLZ4.10D.pf[275]",
//                frontDir+"DTLZ4.15D.pf[135]",
        };

//        Point point3Dmin = new ArrayPoint(new double[]{0.6,0.6,0.6});
//        Point point3D = new ArrayPoint(new double[]{1.1,1.1,1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.6,0.6,0.6,0.6,0.6});
//        Point point5D = new ArrayPoint(new double[]{1.1,1.1,1.1,1.1,1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6});
//        Point point8D = new ArrayPoint(new double[]{1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6});
//        Point point10D = new ArrayPoint(new double[]{1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6,0.6});
//        Point point15D = new ArrayPoint(new double[]{1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1});
        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        Point[] hvRefPointList = {
//                point3Dmin,point5Dmin,point8Dmin,point10Dmin,
//                point15Dmin
//                ,
//                point3D,point5D,point8D,point10D,
 point15D
//                ,
//                point3D,point5D,point8D,point10D,point15D,
//                point3D,point5D,point8D,point10D,point15D,
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);

        MOEADStudy moeadExperiment = new MOEADStudy();
        try {
            moeadExperiment.execute("E:/Results/",
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
        }catch (FileNotFoundException e){}

    }
}
