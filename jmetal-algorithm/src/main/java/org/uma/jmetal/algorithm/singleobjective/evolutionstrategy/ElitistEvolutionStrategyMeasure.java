package org.uma.jmetal.algorithm.singleobjective.evolutionstrategy;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;

import java.util.List;

/**
 * Created by X250 on 2017/1/1.
 */
public class ElitistEvolutionStrategyMeasure extends ElitistEvolutionStrategy<DoubleSolution> implements Measurable {
    protected MyAlgorithmMeasures<DoubleSolution> measure;
    protected int gen;
    /**
     * Constructor
     */
    public ElitistEvolutionStrategyMeasure(Problem<DoubleSolution> problem, int mu, int lambda, int maxEvaluations,int popSize,
                                    MutationOperator<DoubleSolution> mutation) {
        super(problem,mu,lambda,maxEvaluations,mutation) ;
        setMaxPopulationSize(popSize);
        measure = new MyAlgorithmMeasures<DoubleSolution>();
        measure.initMeasures();
    }

    @Override
    public MeasureManager getMeasureManager() {
        return measure.getMeasureManager();

    }

    @Override protected void initProgress() {
        evaluations = 1;
        gen = 1;
    }

    @Override protected void updateProgress() {
        evaluations += lambda;
        int tmpGen = evaluations/getMaxPopulationSize();
        if(tmpGen > gen){
            //calculate measure
            measure.updateMeasureProgress(getMeasurePopulation());
            gen = tmpGen;
        }
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
