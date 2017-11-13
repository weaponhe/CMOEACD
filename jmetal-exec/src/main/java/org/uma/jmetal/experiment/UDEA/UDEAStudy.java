package org.uma.jmetal.experiment.UDEA;

import org.uma.jmetal.algorithm.multiobjective.moeacd.AbstractMOEACD;
import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;
import org.uma.jmetal.algorithm.multiobjective.udea.UDEA;
import org.uma.jmetal.algorithm.multiobjective.udea.UDEABuilder;
import org.uma.jmetal.experiment.IndicatorsListener;
import org.uma.jmetal.experiment.MyExperimentAnalysis;
import org.uma.jmetal.experiment.MyExperimentIndicator;
import org.uma.jmetal.experiment.MyExperimentIndicatorConfig;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.measure.impl.DurationMeasure;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolutionAtBounds;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class UDEAStudy {
    protected String algName = "";
    protected UDEABuilder.Variant algType;
    protected UDEABuilder.Variant measureAlgType;

    public UDEAStudy(){
        algName = "UDEA";
        algType = UDEABuilder.Variant.UDEA;
        measureAlgType = UDEABuilder.Variant.UDEAMeasure;
    }
    public UDEAStudy(String algName) {
        this.algName = algName;
    }

    public UDEA createAlgInstance(UDEABuilder.Variant algType,
                                  SBXCrossover sbxCrossover,
                                  DifferentialEvolutionCrossover deCrossover,
                                  MutationOperator<DoubleSolution> mutation,
                                  int neighborhoodSize,
                                  double neighborhoodSelectionProbability,
                                  Problem<DoubleSolution> problem,
                                  int popsize,
                                  int maxEvaluations,
                                  int maxIterations,
                                  int[] H,
                                  double[] Tau) {
        //configure the algorithm
        UDEA algorithm = new UDEABuilder(problem, algType)
                .setPopulationSize(popsize)
                .setMaxEvaluations(maxEvaluations)
                .setNumOfDivision(H)
                .setIntegratedTaus(Tau)
                .setNeighborhoodSize(neighborhoodSize)
                .setSBXCrossover(sbxCrossover)
                .setDECrossover(deCrossover)
                .setMutation(mutation)
                .setNeighborhoodSelectionProbability(neighborhoodSelectionProbability)
                .build();
        return algorithm;
    }

    public void executeMeasure(String baseDir,
                               double crossoverProbability,
                               double crossoverDistributionIndex,
                               double f,
                               double mutationDistributionIndex,
                               int neighborhoodSize,
                               double neighborhoodSelectionProbability,
                               int indicatorEvaluatingTimes,
                               int maxRun,
                               List<Problem<DoubleSolution>> problemList,
                               int[] popsList,
                               int[] maxIterationsList,
                               int[][] H,
                               double[][] Tau,
                               String[] frontFileList,
                               Point[] hvRefPointList,
                               MyExperimentIndicatorConfig indicatorConfig) throws FileNotFoundException {

        SBXCrossover sbxCrossover;
        DifferentialEvolutionCrossover deCrossover;
        double mutationProbability;
        MutationOperator<DoubleSolution> mutation;
        int maxEvaluations;

        RepairDoubleSolution solutionRepair = new RepairDoubleSolutionAtBounds();//new RepairDoubleSolutionByFold();
        sbxCrossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex, solutionRepair);
        deCrossover = new DifferentialEvolutionCrossover(crossoverProbability, f, "rand/1/bin", solutionRepair);

        for (int iProblem = 0; iProblem < problemList.size(); ++iProblem) {

            maxEvaluations = popsList[iProblem] * maxIterationsList[iProblem];
            mutationProbability = 1.0 / problemList.get(iProblem).getNumberOfVariables();
            mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex, solutionRepair);
            Front referenceFront = new ArrayFront(frontFileList[iProblem]);

            String instance = algName + "_" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
            MyExperimentAnalysis experimentAnalysis = new MyExperimentAnalysis(baseDir, instance);

            for (int iRun = 0; iRun < maxRun; ++iRun) {
                JMetalLogger.logger.info("[ " + algName + " --" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")]  Run : " + iRun);
                //configure the algorithm
                UDEA algorithm = createAlgInstance(measureAlgType,
                        sbxCrossover,
                        deCrossover,
                        mutation,
                        neighborhoodSize,
                        neighborhoodSelectionProbability,
                        problemList.get(iProblem),
                        popsList[iProblem],
                        maxEvaluations,
                        maxIterationsList[iProblem],
                        H[iProblem],
                        Tau[iProblem]);

                /* Measure management */
                MeasureManager measureManager = algorithm.getMeasureManager();
                DurationMeasure currentComputingTime =
                        (DurationMeasure) measureManager.<Long>getPullMeasure("currentExecutionTime");
                BasicMeasure<List<DoubleSolution>> solutionListMeasure =
                        (BasicMeasure<List<DoubleSolution>>) measureManager.<List<DoubleSolution>>getPushMeasure("currentPopulation");

//                MyExperimentIndicator experimentIndicator = new MyExperimentIndicator(referenceFront);
//                WFGHypervolume hvEvaluator = new WFGHypervolume();
//                hvEvaluator.setReferencePoint(hvRefPointList[iProblem]);
//                hvEvaluator.setMiniming();
//                experimentIndicator.addIndicatorEvaluator(hvEvaluator);
//                experimentIndicator.addIndicatorEvaluator(new InvertedGenerationalDistance());
                MyExperimentIndicator experimentIndicator = indicatorConfig.generate(referenceFront, hvRefPointList[iProblem]);

                IndicatorsListener<DoubleSolution> indicatorsListener = new IndicatorsListener<DoubleSolution>(experimentIndicator, maxIterationsList[iProblem], indicatorEvaluatingTimes);//indicatorInterval);
                solutionListMeasure.register(indicatorsListener);
                /* End of measure management */

                //run algorithm
                algorithm.measureRun();

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

    public void execute(String baseDir,
                        double crossoverProbability,
                        double crossoverDistributionIndex,
                        double f,
                        double mutationDistributionIndex,
                        int neighborhoodSize,
                        double neighborhoodSelectionProbability,
                        int maxRun,
                        List<Problem<DoubleSolution>> problemList,
                        int[] popsList,
                        int[] maxIterationsList,
                        int[][] H,
                        double[][] Tau,
                        String[] frontFileList,
                        Point[] hvRefPointList,
                        MyExperimentIndicatorConfig indicatorConfig) throws FileNotFoundException {

        SBXCrossover sbxCrossover;
        DifferentialEvolutionCrossover deCrossover;
        double mutationProbability;
        MutationOperator<DoubleSolution> mutation;
        int maxEvaluations;
        RepairDoubleSolution solutionRepair = new RepairDoubleSolutionAtBounds();//new RepairDoubleSolutionByFold();

        sbxCrossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex, solutionRepair);
        deCrossover = new DifferentialEvolutionCrossover(crossoverProbability, f, "rand/1/bin", solutionRepair);

        for (int iProblem = 0; iProblem < problemList.size(); ++iProblem) {

            maxEvaluations = popsList[iProblem] * maxIterationsList[iProblem];
            mutationProbability = 1.0 / problemList.get(iProblem).getNumberOfVariables();
            mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex, solutionRepair);
            Front referenceFront = new ArrayFront(frontFileList[iProblem]);


            String instance = algName + "_" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
            MyExperimentAnalysis experimentAnalysis = new MyExperimentAnalysis(baseDir, instance);

            for (int iRun = 0; iRun < maxRun; ++iRun) {
                JMetalLogger.logger.info("[ " + algName + " --" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")]  Run : " + iRun);
                //configure the algorithm
                UDEA algorithm = createAlgInstance(algType,
                        sbxCrossover,
                        deCrossover,
                        mutation,
                        neighborhoodSize,
                        neighborhoodSelectionProbability,
                        problemList.get(iProblem),
                        popsList[iProblem],
                        maxEvaluations,
                        maxIterationsList[iProblem],
                        H[iProblem],
                        Tau[iProblem]);


                AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                        .execute();

                List<DoubleSolution> population = algorithm.getMeasurePopulation();

                MyExperimentIndicator experimentIndicator = indicatorConfig.generate(referenceFront, hvRefPointList[iProblem]);
                experimentIndicator.computeQualityIndicators(maxIterationsList[iProblem], population);
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
