package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;

/**
 * Created by X250 on 2016/9/2.
 */
public class MOEADAGRMeasure extends MOEADAGR implements Measurable {
    protected MyAlgorithmMeasures measure;
    MOEADAGRMeasure(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                   CrossoverOperator<DoubleSolution> crossover, MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                   String dataDirectory, double neighborhoodSelectionProbability,
                   int maximumNumberOfReplacedSolutions, int neighborSize){
        super(problem, populationSize, resultPopulationSize, maxEvaluations,
                crossover,mutation,  functionType,
                dataDirectory,  neighborhoodSelectionProbability,
                maximumNumberOfReplacedSolutions, neighborSize);
        measure = new MyAlgorithmMeasures<DoubleSolution>();
        measure.initMeasures();
    }

    MOEADAGRMeasure(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                   CrossoverOperator<DoubleSolution> crossover, MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                   int[] arrayH,double[] integratedTau, double neighborhoodSelectionProbability,
                   int maximumNumberOfReplacedSolutions, int neighborSize){
        super(problem, populationSize, resultPopulationSize, maxEvaluations,
                crossover,mutation,  functionType,
                arrayH,integratedTau,  neighborhoodSelectionProbability,
                maximumNumberOfReplacedSolutions, neighborSize);
        measure = new MyAlgorithmMeasures<DoubleSolution>();
        measure.initMeasures();
    }

    @Override
    public MeasureManager getMeasureManager() {
        return measure.getMeasureManager();
    }

    @Override public void run() {
        //Start
        measure.durationMeasure.start();

        initializeUniformWeight();
        initializeNeighborhood();
        initializePopulation();

        initializeIdealPoint();

        int generation = 0;
        evaluations = populationSize;
        //calculate measure
        measure.updateMeasureProgress(getMeasurePopulation());
        do {
            int[] permutation = new int[populationSize];
            MOEADUtils.randomPermutation(permutation, populationSize);

            for (int i = 0; i < populationSize; i++) {
                int subProblemId = permutation[i];

                NeighborType neighborType = chooseNeighborType();
                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType);

                differentialEvolutionCrossover.setCurrentSolution(population.get(subProblemId));
                List<DoubleSolution> children = differentialEvolutionCrossover.execute(parents);

                DoubleSolution child = children.get(0);
                mutationOperator.execute(child);
                problem.evaluate(child);

                evaluations++;

                updateIdealPoint(child);
                int appropriateSubProblemId = findAppropriateSubproblem(child);
                updateNeighborhood(child, appropriateSubProblemId, neighborType);
            }

            generation++;
            //calculate measure
            measure.updateMeasureProgress(getMeasurePopulation());
        } while (evaluations < maxEvaluations);
        measure.durationMeasure.stop();
    }
}

