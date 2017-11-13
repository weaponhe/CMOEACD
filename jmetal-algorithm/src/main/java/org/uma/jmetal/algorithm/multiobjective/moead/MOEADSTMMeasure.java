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
 * Created by X250 on 2016/4/9.
 */
public class MOEADSTMMeasure extends  MOEADSTM implements Measurable {
    protected MyAlgorithmMeasures measure;
    MOEADSTMMeasure(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                    MutationOperator<DoubleSolution> mutation, CrossoverOperator<DoubleSolution> crossover,
                    FunctionType functionType, String dataDirectory, double neighborhoodSelectionProbability,
                    int maximumNumberOfReplacedSolutions, int neighborSize){
        super(problem,populationSize,resultPopulationSize, maxEvaluations, mutation, crossover, functionType,  dataDirectory,  neighborhoodSelectionProbability,maximumNumberOfReplacedSolutions, neighborSize);
        measure = new MyAlgorithmMeasures<DoubleSolution>();
        measure.initMeasures();
    }

    MOEADSTMMeasure(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                    MutationOperator<DoubleSolution> mutation, CrossoverOperator<DoubleSolution> crossover,
                    FunctionType functionType, int[] arrayH,double[] integratedTau, double neighborhoodSelectionProbability,
                    int maximumNumberOfReplacedSolutions, int neighborSize){
        super(problem,populationSize,resultPopulationSize, maxEvaluations, mutation, crossover, functionType,  arrayH,integratedTau,  neighborhoodSelectionProbability,maximumNumberOfReplacedSolutions, neighborSize);
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

        initializePopulation();
        initializeUniformWeight();
        initializeNeighborhood();
        initializeIdealPoint();
        initializeNadirPoint();

        int generation = 0;
        evaluations    = populationSize;
        //calculate measure
        measure.updateMeasureProgress(getMeasurePopulation());
        do {
            int[] permutation = new int[populationSize];
            MOEADUtils.randomPermutation(permutation, populationSize);
            offspringPopulation.clear();

            for (int i = 0; i < populationSize; i++) {
                int subProblemId = permutation[i];
                frequency[subProblemId]++;

                NeighborType neighborType = chooseNeighborType();
                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType);

                differentialEvolutionCrossover.setCurrentSolution(population.get(subProblemId));
                List<DoubleSolution> children = differentialEvolutionCrossover.execute(parents);

                DoubleSolution child = children.get(0);
                mutationOperator.execute(child);
                problem.evaluate(child);

                evaluations++;

                updateIdealPoint(child);
                updateNadirPoint(child);
                updateNeighborhood(child, subProblemId, neighborType);

                offspringPopulation.add(child);
            }

            // Combine the parent and the current offspring populations
            jointPopulation.clear();
            jointPopulation.addAll(population);
            jointPopulation.addAll(offspringPopulation);

            // selection process
            stmSelection();

            generation++;
            if (generation % 30 == 0) {
                utilityFunction();
            }

            //calculate measure
            measure.updateMeasureProgress(getMeasurePopulation());
        } while (evaluations < maxEvaluations);
        measure.durationMeasure.stop();
    }
}
