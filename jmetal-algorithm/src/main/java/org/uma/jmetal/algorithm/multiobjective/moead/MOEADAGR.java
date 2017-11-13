package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

import java.util.List;

/**
 * Created by X250 on 2016/9/2.
 */
public class MOEADAGR extends MOEADGR {
    protected double gamma = 0.25;
    protected int neighborSizeForUpdate = 0;
    public MOEADAGR(Problem<DoubleSolution> problem,
                   int populationSize,
                   int resultPopulationSize,
                   int maxEvaluations,
                   CrossoverOperator<DoubleSolution> crossover,
                   MutationOperator<DoubleSolution> mutation,
                   FunctionType functionType,
                   String dataDirectory,
                   double neighborhoodSelectionProbability,
                   int maximumNumberOfReplacedSolutions,
                   int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
        neighborSizeForUpdate = neighborSize;
    }

    public MOEADAGR(Problem<DoubleSolution> problem,
                   int populationSize,
                   int resultPopulationSize,
                   int maxEvaluations,
                   CrossoverOperator<DoubleSolution> crossover,
                   MutationOperator<DoubleSolution> mutation,
                   FunctionType functionType,
                   int[] arrayH,
                   double[] integratedTau,
                   double neighborhoodSelectionProbability,
                   int maximumNumberOfReplacedSolutions,
                   int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                arrayH,integratedTau, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
        neighborSizeForUpdate = neighborSize;
    }


    protected void adaptiveNeighborSizeForUpdate(){
        neighborSizeForUpdate = (int) Math.ceil(1.0*neighborSize/(1.0 + Math.exp(-20.0*(1.0*evaluations/maxEvaluations - gamma))));
    }

    protected  void updateNeighborhood(DoubleSolution individual, int subProblemId) throws JMetalException {

        adaptiveNeighborSizeForUpdate();
        for (int i = 0; i < neighborSizeForUpdate; i++) {
            int k = neighborhood[subProblemId][i];

            double f1, f2;

            f1 = fitnessFunction(population.get(k), lambda[k]);
            f2 = fitnessFunction(individual, lambda[k]);

            if (f2 < f1) {
                population.set(k, (DoubleSolution) individual.copy());
            }
        }
    }


    @Override
    public String getName() {
        return "MOEADAGR";
    }

    @Override
    public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition. Version with Global Replacement Approach";
    }
}
