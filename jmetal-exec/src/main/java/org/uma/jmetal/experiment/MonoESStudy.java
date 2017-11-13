package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.ElitistEvolutionStrategyMeasure;
import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.EvolutionStrategyMeasureBuilder;
import org.uma.jmetal.algorithm.singleobjective.evolutionstrategy.NonElitistEvolutionStrategyMeasure;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.measure.impl.DurationMeasure;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class MonoESStudy {
    public void executeMeasure(String baseDir,
                               double crossoverProbability,
                               double f,
                               double mutationDistributionIndex,
                               int[] mu,
                               int[] lambda,
                               int maxRun,
                               List<Problem<DoubleSolution>> problemList,
                               int[] popsList,
                               int[] maxIterationsList,
                               double[] optimumList) throws FileNotFoundException {

        double mutationProbability;
        MutationOperator<DoubleSolution> mutation;
        int maxEvaluations;

        for(int iProblem=0;iProblem<problemList.size();++iProblem) {

            maxEvaluations = popsList[iProblem] * maxIterationsList[iProblem];

            mutationProbability = 1.0 / problemList.get(iProblem).getNumberOfVariables();
            mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

            String instance = "ES_" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
            MyExperimentMonoAnalysis experimentAnalysis = new MyExperimentMonoAnalysis(baseDir, instance);

            for (int iRun = 0; iRun < maxRun; ++iRun) {
                JMetalLogger.logger.info("[ES--" + problemList.get(iProblem).getName() + "(" + problemList.get(iProblem).getNumberOfObjectives() + ")]  Run : " + iRun);
                //configure the algorithm
                NonElitistEvolutionStrategyMeasure algorithm =(NonElitistEvolutionStrategyMeasure) new EvolutionStrategyMeasureBuilder((DoubleProblem) problemList.get(iProblem), EvolutionStrategyMeasureBuilder.EvolutionStrategyVariant.NON_ELITIST)
                        .setMaxEvaluations(maxEvaluations)
                        .setMaxPopulationSize(popsList[iProblem])
                        .setMutation(mutation)
                        .setMu(mu[iProblem])
                        .setLambda(lambda[iProblem])
                        .build() ;

                /* Measure management */
                MeasureManager measureManager = ((NonElitistEvolutionStrategyMeasure) algorithm).getMeasureManager();
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


