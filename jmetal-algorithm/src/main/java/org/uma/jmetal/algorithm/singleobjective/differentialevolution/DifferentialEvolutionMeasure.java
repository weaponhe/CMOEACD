package org.uma.jmetal.algorithm.singleobjective.differentialevolution;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.DifferentialEvolutionSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2017/1/1.
 */
public class DifferentialEvolutionMeasure extends DifferentialEvolution implements Measurable {
    protected MyAlgorithmMeasures<DoubleSolution> measure;

    /**
     * Constructor
     *
     * @param problem Problem to solve
     * @param maxEvaluations Maximum number of evaluations to perform
     * @param populationSize
     * @param crossoverOperator
     * @param selectionOperator
     * @param evaluator
     */
    public DifferentialEvolutionMeasure(DoubleProblem problem, int maxEvaluations, int populationSize,
                                 DifferentialEvolutionCrossover crossoverOperator,
                                 DifferentialEvolutionSelection selectionOperator, SolutionListEvaluator<DoubleSolution> evaluator) {
        super(problem,maxEvaluations,populationSize,crossoverOperator,selectionOperator,evaluator);
        measure = new MyAlgorithmMeasures<DoubleSolution>();
        measure.initMeasures();
    }

    @Override
    public MeasureManager getMeasureManager() {
        return measure.getMeasureManager();
    }


    @Override protected void initProgress() {
        evaluations = populationSize;
        //calculate measure
        measure.updateMeasureProgress(getMeasurePopulation());
    }

    @Override protected void updateProgress() {
        evaluations += populationSize;
        //calculate measure
        measure.updateMeasureProgress(getMeasurePopulation());
    }


    @Override public void run() {
        //Start
        measure.durationMeasure.start();

        List<DoubleSolution> offspringPopulation;
        List<DoubleSolution> matingPopulation;

        setPopulation(createInitialPopulation());
        setPopulation(evaluatePopulation(getPopulation()));
        initProgress();
        while (!isStoppingConditionReached()) {
            matingPopulation = selection(getPopulation());
            offspringPopulation = reproduction(matingPopulation);
            offspringPopulation = evaluatePopulation(offspringPopulation);
            setPopulation(replacement(getPopulation(), offspringPopulation));
            updateProgress();
        }

        measure.durationMeasure.stop();
    }

}
