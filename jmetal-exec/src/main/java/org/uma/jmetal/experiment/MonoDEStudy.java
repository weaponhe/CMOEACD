package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADDEMeasure;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADMeasure;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DifferentialEvolution;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DifferentialEvolutionBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DifferentialEvolutionMeasure;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.measure.impl.DurationMeasure;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.MultithreadedSolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/1.
 */
public class MonoDEStudy {
    public void executeMeasure(String baseDir,
                               double crossoverProbability,
                               double f,
                               int maxRun,
                               List<Problem<DoubleSolution>> problemList,
                               int[] popsList,
                               int[] maxIterationsList,
                               double[] optimumList) throws FileNotFoundException {

        DifferentialEvolutionCrossover differentialEvolutionCrossover;
        int maxEvaluations;


        differentialEvolutionCrossover = new DifferentialEvolutionCrossover(crossoverProbability, f, "rand/1/bin");

        for(int iProblem=0;iProblem<problemList.size();++iProblem) {

            maxEvaluations = popsList[iProblem] * maxIterationsList[iProblem];

            String instance = "DE_" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
            MyExperimentMonoAnalysis experimentAnalysis = new MyExperimentMonoAnalysis(baseDir, instance);

            for (int iRun = 0; iRun < maxRun; ++iRun) {
                JMetalLogger.logger.info("[DE--" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")]  Run : " + iRun);
                //configure the algorithm
                DifferentialEvolution algorithm = new DifferentialEvolutionBuilder((DoubleProblem) problemList.get(iProblem), DifferentialEvolutionBuilder.Variant.DEMeasure)
                        .setCrossover(differentialEvolutionCrossover)
                        .setMaxEvaluations(maxEvaluations)
                        .setPopulationSize(popsList[iProblem])
                        .build() ;

                /* Measure management */
                MeasureManager measureManager = ((DifferentialEvolutionMeasure) algorithm).getMeasureManager();
                DurationMeasure currentComputingTime =
                        (DurationMeasure) measureManager.<Long>getPullMeasure("currentExecutionTime");
                BasicMeasure<List<DoubleSolution>> solutionListMeasure =
                        (BasicMeasure<List<DoubleSolution>>) measureManager.<List<DoubleSolution>>getPushMeasure("currentPopulation");

                MyExperimentMonoIndicator experimentIndicator = new MyExperimentMonoIndicator(optimumList[iProblem]);

                MonoIndicatorsListener<DoubleSolution> indicatorsListener = new MonoIndicatorsListener<DoubleSolution>(experimentIndicator, maxIterationsList[iProblem]);
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

    public void execute(String baseDir,
                        double crossoverProbability,
                        double f,
                        int maxRun,
                        List<Problem<DoubleSolution>> problemList,
                        int[] popsList,
                        int[] maxIterationsList,
                        double[] optimumList) throws FileNotFoundException {

        DifferentialEvolutionCrossover differentialEvolutionCrossover;
        int maxEvaluations;

        differentialEvolutionCrossover = new DifferentialEvolutionCrossover(crossoverProbability, f, "rand/1/bin");

        for (int iProblem = 0; iProblem < problemList.size(); ++iProblem) {

            maxEvaluations = popsList[iProblem] * maxIterationsList[iProblem];

            String instance = "DE_" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
            MyExperimentMonoAnalysis experimentAnalysis = new MyExperimentMonoAnalysis(baseDir, instance);

            for (int iRun = 0; iRun < maxRun; ++iRun) {
                JMetalLogger.logger.info("[DE--" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")]  Run : " + iRun);
                //configure the algorithm
                DifferentialEvolution algorithm = new DifferentialEvolutionBuilder((DoubleProblem) problemList.get(iProblem), DifferentialEvolutionBuilder.Variant.DE)
                        .setCrossover(differentialEvolutionCrossover)
                        .setMaxEvaluations(maxEvaluations)
                        .setPopulationSize(popsList[iProblem])
                        .build();


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
