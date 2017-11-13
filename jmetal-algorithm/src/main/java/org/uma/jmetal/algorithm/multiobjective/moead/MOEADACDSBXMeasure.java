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
 * Created by X250 on 2016/9/10.
 */
public class MOEADACDSBXMeasure  extends MOEADACDSBX implements Measurable {
    protected MyAlgorithmMeasures measure;
    MOEADACDSBXMeasure(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
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

    MOEADACDSBXMeasure(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
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

        evaluations = populationSize;

        //calculate measure
        measure.updateMeasureProgress(getMeasurePopulation());

        int generation = 0;
        evaluations = populationSize;
        do {

            for (int i = 0; i < populationSize; i++) {
                int subProblemId = i;

                NeighborType neighborType = chooseNeighborType();
                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType);

                List<DoubleSolution> children = crossoverOperator.execute(parents);

                DoubleSolution child = children.get(0) ;
                mutationOperator.execute(child);

                problem.evaluate(child);

                evaluations++;

                updateIdealPoint(child);
                updateNeighborhood(child, subProblemId, neighborType);
            }

            generation++;

            updateTheta();

            //calculate measure
            measure.updateMeasureProgress(getMeasurePopulation());
        } while (evaluations < maxEvaluations);



        measure.durationMeasure.stop();
    }
}

