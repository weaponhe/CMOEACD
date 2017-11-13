package org.uma.jmetal.algorithm.multiobjective.nsgaiii;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.util.ReferencePoint;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.UniformSimplexCentroidWeightsUtils;
import org.uma.jmetal.util.UniformWeightUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by X250 on 2016/4/10.
 */
public class NSGAIIIMeasure<S extends Solution<?>> extends NSGAIII<S> implements Measurable{
    protected MyAlgorithmMeasures<S> measure;
    public NSGAIIIMeasure(NSGAIIIBuilder<S> builder) {
        super(builder);
        measure = new MyAlgorithmMeasures<S>();
        measure.initMeasures();
    }

    @Override
    public MeasureManager getMeasureManager() {
        return measure.getMeasureManager();
    }

    @Override public void run() {
        //Start
        measure.durationMeasure.start();

//            (new ReferencePoint<S>()).generateReferencePoints(referencePoints,getProblem().getNumberOfObjectives() , numberOfDivisions,intergratedTaus);
        Vector<Vector<Double>> weights = UniformWeightUtils.generateVector(numberOfDivisions,intergratedTaus,getProblem().getNumberOfObjectives());
//        UniformSimplexCentroidWeightsUtils uniformWeightsManager = new UniformSimplexCentroidWeightsUtils(getProblem().getNumberOfObjectives(),numberOfDivisions[0]);
//        Vector<Vector<Double>> weights = uniformWeightsManager.getUniformWeightsVector();
//        uniformWeightsManager.free();
        setMaxPopulationSize(weights.size());

        for(int i=0;i<weights.size();i++){
            ReferencePoint<S> refPoint = new ReferencePoint<>(getProblem().getNumberOfObjectives()) ;
            refPoint.position = new ArrayList<>(weights.get(i)) ;
            referencePoints.add(refPoint);
        }

        List<S> offspringPopulation;
        List<S> matingPopulation;

        setPopulation(createInitialPopulation());
        setPopulation(evaluatePopulation(getPopulation()));
        initProgress();

        //calculate measure
        measure.updateMeasureProgress(getMeasurePopulation());

        while (!isStoppingConditionReached()) {
            matingPopulation = selection(getPopulation());
            offspringPopulation = reproduction(matingPopulation);
            offspringPopulation = evaluatePopulation(offspringPopulation);
            setPopulation(replacement(getPopulation(), offspringPopulation));
            updateProgress();
            //calculate measure
            measure.updateMeasureProgress(getMeasurePopulation());
        }

        measure.durationMeasure.stop();
    }
}
