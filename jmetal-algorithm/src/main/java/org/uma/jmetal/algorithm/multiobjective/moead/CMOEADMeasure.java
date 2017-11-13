package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;

/**
 * Created by X250 on 2016/11/18.
 */
public class CMOEADMeasure extends CMOEAD implements Measurable {
    protected MyAlgorithmMeasures<DoubleSolution> measure;

    public CMOEADMeasure(Problem<DoubleSolution> problem,
                         int populationSize,
                         int resultPopulationSize,
                         int maxEvaluations,
                         CrossoverOperator<DoubleSolution> crossover,
                         MutationOperator<DoubleSolution> mutation,
                         AbstractMOEAD.FunctionType functionType,
                         String dataDirectory,
                         int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType, dataDirectory, neighborSize);
        measure = new MyAlgorithmMeasures<DoubleSolution>();
        measure.initMeasures();
    }

    public CMOEADMeasure(Problem<DoubleSolution> problem,
                         int populationSize,
                         int resultPopulationSize,
                         int maxEvaluations,
                         CrossoverOperator<DoubleSolution> crossover,
                         MutationOperator<DoubleSolution> mutation,
                         AbstractMOEAD.FunctionType functionType,
                         int[] arrayH,
                         double[] integratedTau,
                         int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType, arrayH, integratedTau, neighborSize);
        measure = new MyAlgorithmMeasures<DoubleSolution>();
        measure.initMeasures();
    }


    @Override
    public MeasureManager getMeasureManager() {
        return measure.getMeasureManager();
    }


    @Override
    public void run() {
        //Start
        measure.durationMeasure.start();
        initializePopulation();
        initializeUniformWeight();
        initializeNeighborhood();
        initializeIdealPoint();

        violationThresholdComparator.updateThreshold(population);

        evaluations = populationSize;
        //calculate measure
        measure.updateMeasureProgress(getMeasurePopulation());
        do {
            for (int i = 0; i < populationSize; i++) {
                int subProblemId = i;

                List<DoubleSolution> parents = parentSelection(subProblemId);

                List<DoubleSolution> children = crossoverOperator.execute(parents);

                DoubleSolution child = children.get(0);
                mutationOperator.execute(child);
                problem.evaluate(child);
                if (problem instanceof ConstrainedProblem) {
                    ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child);
                }
                evaluations++;

                updateIdealPoint(child);
                updateNeighborhood(child, subProblemId);
            }

            violationThresholdComparator.updateThreshold(population);
            //calculate measure
            measure.updateMeasureProgress(getMeasurePopulation());
        } while (evaluations < maxEvaluations);
        measure.durationMeasure.stop();

    }
}
