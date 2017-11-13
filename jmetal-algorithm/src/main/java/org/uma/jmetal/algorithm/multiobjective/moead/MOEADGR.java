package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalException;

import java.util.List;

/**
 * Created by X250 on 2016/8/25.
 */
public class MOEADGR extends MOEADDE {
    public MOEADGR(Problem<DoubleSolution> problem,
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
    }

    public MOEADGR(Problem<DoubleSolution> problem,
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
    }


    @Override public void run() {
        initializePopulation() ;
        initializeUniformWeight();
        initializeNeighborhood();
        initializeIdealPoint() ;

        evaluations = populationSize ;
        do {
            int[] permutation = new int[populationSize];
            MOEADUtils.randomPermutation(permutation, populationSize);

            for (int i = 0; i < populationSize; i++) {
                int subProblemId = permutation[i];

                NeighborType neighborType = chooseNeighborType() ;
                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType) ;

                differentialEvolutionCrossover.setCurrentSolution(population.get(subProblemId));
                List<DoubleSolution> children = differentialEvolutionCrossover.execute(parents);

                DoubleSolution child = children.get(0) ;
                mutationOperator.execute(child);
                problem.evaluate(child);

                evaluations++;

                updateIdealPoint(child);
                int appropriateSubProblemId = findAppropriateSubproblem(child);
                updateNeighborhood(child, appropriateSubProblemId, neighborType);
            }
        } while (evaluations < maxEvaluations);

    }

    protected void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution)problem.createSolution();

            problem.evaluate(newSolution);
            population.add(newSolution);
        }
    }

    protected int findAppropriateSubproblem(DoubleSolution indiv){
        double minFitness = Double.MAX_VALUE;
        int minIdx = 0;
        for (int i=0;i<population.size();i++){
            double f = fitnessFunction(indiv, lambda[i]);
            if(f < minFitness){
                minFitness = f ;
                minIdx = i;
            }
        }
        return minIdx;
    }
    protected  void updateNeighborhood(DoubleSolution individual, int subProblemId) throws JMetalException {
        int size;

        size = neighborhood[subProblemId].length;

        for (int i = 0; i < size; i++) {
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
        return "MOEADGR";
    }

    @Override
    public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition. Version with Global Replacement Approach";
    }
}
