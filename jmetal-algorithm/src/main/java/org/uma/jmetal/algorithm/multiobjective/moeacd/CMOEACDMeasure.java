package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;

/**
 * Created by weaponhe on 2018/1/20.
 */
public class CMOEACDMeasure extends CMOEACD implements Measurable {
    public CMOEACDMeasure(Measurable measureManager, Problem<DoubleSolution> problem,
                          int[] arrayH,
                          double[] integratedTaus,
                          int populationSize,
                          int constraintLayerSize,
                          int maxEvaluations,
                          int maxGen,
                          int neighborhoodSize,
                          double neighborhoodSelectionProbability,
                          SBXCrossover sbxCrossoverOperator,
                          DifferentialEvolutionCrossover deCrossoverOperator,
                          MutationOperator<DoubleSolution> mutation,
                          AbstractMOEAD.FunctionType functionType,
                          double[] delta
    ) {
        super(measureManager,
                problem,
                arrayH,
                integratedTaus,
                populationSize,
                constraintLayerSize,
                maxEvaluations,
                maxGen,
                neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,
                deCrossoverOperator,
                mutation,
                functionType,
                delta);
    }


    @Override
    public MeasureManager getMeasureManager() {
        return measureManager.getMeasureManager();
    }

    @Override
    public void run() {
        measureManager.durationMeasure.start();
        initializeConeSubRegions();
        initializePopulation(this.populationSize * this.constraintLayerSize);
        int gen = 0;

        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        associateSubRegion(population, utopianPoint, normIntercepts);

        do {
            calcEvolvingSubproblemList();
            for (int i = 0; i < populationSize; i++) {
                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
                DoubleSolution child = children.get(0);
                problem.evaluate(child);
                if (problem instanceof ConstrainedProblem) {
                    ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child);
                }
                if (updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)) {
                    updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                    associateSubRegion(population, utopianPoint, normIntercepts);
                }
                boolean isUpdated = updatePopulation(child, idealPoint, utopianPoint, normIntercepts);
                collectForAdaptiveCrossover(isUpdated);
            }
            initializeNadirPoint(population, nadirPoint);
            if (gen % updateInterval == 0)
                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
            updateAdaptiveCrossover();

            gen++;
        } while (gen < maxGen);
        measureManager.durationMeasure.stop();
    }
}
