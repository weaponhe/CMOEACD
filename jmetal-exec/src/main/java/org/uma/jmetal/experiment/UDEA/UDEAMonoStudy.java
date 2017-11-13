package org.uma.jmetal.experiment.UDEA;

import org.uma.jmetal.algorithm.multiobjective.moeacd.AbstractMOEACD;
import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;
import org.uma.jmetal.algorithm.multiobjective.udea.UDEA;
import org.uma.jmetal.algorithm.multiobjective.udea.UDEABuilder;
import org.uma.jmetal.experiment.MonoIndicatorsListener;
import org.uma.jmetal.experiment.MyExperimentMonoAnalysis;
import org.uma.jmetal.experiment.MyExperimentMonoIndicator;
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

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class UDEAMonoStudy extends UDEAStudy {
    public UDEAMonoStudy() {
        this.algName = "UDEA";
        this.algType = UDEABuilder.Variant.UDEA;
        this.measureAlgType = UDEABuilder.Variant.UDEAMeasure;
    }

    public void executeMeasure(String baseDir,
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
                               double[] optimumList
    ) throws FileNotFoundException {

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


            String instance = algName + "_" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
            MyExperimentMonoAnalysis experimentAnalysis = new MyExperimentMonoAnalysis(baseDir, instance);

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
                        new int[]{popsList[iProblem] - 1},
                        new double[]{1.0});

                /* Measure management */
                MeasureManager measureManager = algorithm.getMeasureManager();
                DurationMeasure currentComputingTime =
                        (DurationMeasure) measureManager.<Long>getPullMeasure("currentExecutionTime");
                BasicMeasure<List<DoubleSolution>> solutionListMeasure =
                        (BasicMeasure<List<DoubleSolution>>) measureManager.<List<DoubleSolution>>getPushMeasure("currentPopulation");

                MyExperimentMonoIndicator experimentIndicator = new MyExperimentMonoIndicator(optimumList[iProblem]);
                MonoIndicatorsListener<DoubleSolution> indicatorsListener = new MonoIndicatorsListener<DoubleSolution>(experimentIndicator, maxIterationsList[iProblem]);
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
                        double[] optimumList) throws FileNotFoundException {

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

            String instance = algName + "_" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
            MyExperimentMonoAnalysis experimentAnalysis = new MyExperimentMonoAnalysis(baseDir, instance);

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
                        new int[]{popsList[iProblem] - 1},
                        new double[]{1.0});


                AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                        .execute();

                List<DoubleSolution> population = algorithm.getMeasurePopulation();

                MyExperimentMonoIndicator experimentIndicator = new MyExperimentMonoIndicator(optimumList[iProblem]);
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