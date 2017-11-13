package org.uma.jmetal.algorithm.singleobjective.evolutionstrategy;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;

import java.util.List;
import java.util.Random;

/**
 * Created by X250 on 2017/1/1.
 */
public class CovarianceMatrixAdaptationEvolutionStrategyMeasure extends  CovarianceMatrixAdaptationEvolutionStrategy implements Measurable {
    protected MyAlgorithmMeasures<DoubleSolution> measure;
    protected int gen;


    public CovarianceMatrixAdaptationEvolutionStrategyMeasure (DoubleProblem problem,int lambda,int maxEvaluations,int popSize,double[] typicalX,double sigma) {
        super(problem,lambda,maxEvaluations,typicalX,sigma);
        setMaxPopulationSize(popSize);
        measure = new MyAlgorithmMeasures<DoubleSolution>();
        measure.initMeasures();
    }

    @Override
    public MeasureManager getMeasureManager() {
        return measure.getMeasureManager();

    }

    @Override protected void initProgress() {
        evaluations = 0;
        gen = 0;
    }

    @Override protected void updateProgress() {
        super.updateProgress();
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
