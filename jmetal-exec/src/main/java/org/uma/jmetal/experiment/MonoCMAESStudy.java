package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DifferentialEvolution;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DifferentialEvolutionBuilder;
import org.uma.jmetal.algorithm.singleobjective.differentialevolution.DifferentialEvolutionMeasure;
import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.CovarianceMatrixAdaptationEvolutionStrategyMeasure;
import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.EvolutionStrategyMeasureBuilder;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.measure.impl.DurationMeasure;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class MonoCMAESStudy {
    public void executeMeasure(String baseDir,
                              int[] lambda,
                               double[] typicalX,
                               double[] sigma,
                               int maxRun,
                               List<Problem<DoubleSolution>> problemList,
                               int[] popsList,
                               int[] maxIterationsList,
                               double[] optimumList) throws FileNotFoundException {

        int maxEvaluations;

        for(int iProblem=0;iProblem<problemList.size();++iProblem) {

            maxEvaluations = popsList[iProblem] * maxIterationsList[iProblem];

            String instance = "CMAES_" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
            MyExperimentMonoAnalysis experimentAnalysis = new MyExperimentMonoAnalysis(baseDir, instance);

            for (int iRun = 0; iRun < maxRun; ++iRun) {
                JMetalLogger.logger.info("[CMA--" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")]  Run : " + iRun);
                //configure the algorithm
                CovarianceMatrixAdaptationEvolutionStrategyMeasure algorithm =(CovarianceMatrixAdaptationEvolutionStrategyMeasure) new EvolutionStrategyMeasureBuilder((DoubleProblem) problemList.get(iProblem), EvolutionStrategyMeasureBuilder.EvolutionStrategyVariant.CMAES)
                        .setMaxEvaluations(maxEvaluations)
                        .setMaxPopulationSize(popsList[iProblem])
                        .setLambda(lambda[iProblem])
                        .setSigma(sigma[iProblem])
                        .setTypicalX(typicalX)
                        .build() ;

                /* Measure management */
                MeasureManager measureManager = ((CovarianceMatrixAdaptationEvolutionStrategyMeasure) algorithm).getMeasureManager();
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



}
