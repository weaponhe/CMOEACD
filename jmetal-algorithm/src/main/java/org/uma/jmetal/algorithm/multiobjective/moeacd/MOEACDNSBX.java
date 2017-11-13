package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * Created by X250 on 2016/12/26.
 */
public class MOEACDNSBX extends MOEACDN {
    public MOEACDNSBX(Problem<DoubleSolution> problem,
                      int[] arrayH,
                      double[] integratedTaus,
                      int populationSize,
                      int maxEvaluations,
                      int neighborhoodSize,
                      double neighborhoodSelectionProbability,
                      SBXCrossover sbxCrossoverOperator,
                      MutationOperator<DoubleSolution> mutation
    ) {
        super(problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, null, mutation);
    }

    public MOEACDNSBX(Measurable measureManager,
                      Problem<DoubleSolution> problem,
                      int[] arrayH,
                      double[] integratedTaus,
                      int populationSize,
                      int maxEvaluations,
                      int neighborhoodSize,
                      double neighborhoodSelectionProbability,
                      SBXCrossover sbxCrossoverOperator,
                      MutationOperator<DoubleSolution> mutation) {
        super(measureManager, problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, null, mutation);
    }

    protected void chooseCrossoverType() {
        crossoverType = CrossoverType.SBX;
    }

    @Override
    public String getName() {
        return "MOEA/CD-N-SBX";
    }

    @Override
    public String getDescription() {
        return "MOEA/CD with SBX only";
    }
}