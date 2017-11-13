package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.CMOEADDNMeasure;
import org.uma.jmetal.algorithm.multiobjective.moead.CMOEADNMeasure;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.measure.impl.DurationMeasure;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by X250 on 2016/11/25.
 */
public class CMOEADNStudy {
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
                               MyExperimentIndicatorConfig indicatorConfig) throws FileNotFoundException {

        SBXCrossover sbxCrossover;
        double mutationProbability;
        MutationOperator<DoubleSolution> mutation;
        int indicatorInterval;
        int maxEvaluations;


        String[] variantName = {
//                "CMOEADN_TCH",
                "CMOEADN_PBI",
        };
        AbstractMOEAD.FunctionType[] variantConfig = {
//                AbstractMOEAD.FunctionType.TCHE,
                AbstractMOEAD.FunctionType.PBI
        };

        sbxCrossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

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
                    AbstractMOEAD<DoubleSolution> algorithm = new MOEADBuilder(problemList.get(iProblem), MOEADBuilder.Variant.CMOEADNMeasure)
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
                    MeasureManager measureManager = ((CMOEADNMeasure) algorithm).getMeasureManager();
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
                        MyExperimentIndicatorConfig indicatorConfig) throws FileNotFoundException {

        SBXCrossover sbxCrossover;
        double mutationProbability;
        MutationOperator<DoubleSolution> mutation;
        int maxEvaluations;


        String[] variantName = {
//                "CMOEADN_TCH",
                "CMOEADN_PBI",
        };
        AbstractMOEAD.FunctionType[] variantConfig = {
//                AbstractMOEAD.FunctionType.TCHE,
                AbstractMOEAD.FunctionType.PBI
        };

        sbxCrossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

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
                    AbstractMOEAD<DoubleSolution> algorithm = new MOEADBuilder(problemList.get(iProblem), MOEADBuilder.Variant.CMOEADN)
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
}
