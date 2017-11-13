package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.impl.GeneralViolationThresholdComparator;
import org.uma.jmetal.util.comparator.impl.NormalizedOverallViolationThreholdComparator;

/**
 * Created by X250 on 2017/1/6.
 */
public class CMOEACDIII extends CMOEACD{
    public CMOEACDIII(Problem<DoubleSolution> problem,
                      int[] arrayH,
                      double[] integratedTaus,
                      int populationSize,
                      int maxEvaluations,
                      int neighborhoodSize,
                      double neighborhoodSelectionProbability,
                      SBXCrossover sbxCrossoverOperator,
                      DifferentialEvolutionCrossover deCrossoverOperator,
                      MutationOperator<DoubleSolution> mutation
    ) {
        super(problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
        violationThresholdComparator = new NormalizedOverallViolationThreholdComparator<>();
    }

    public CMOEACDIII(Measurable measureManager, Problem<DoubleSolution> problem,
                      int[] arrayH,
                      double[] integratedTaus,
                      int populationSize,
                      int maxEvaluations,
                      int neighborhoodSize,
                      double neighborhoodSelectionProbability,
                      SBXCrossover sbxCrossoverOperator,
                      DifferentialEvolutionCrossover deCrossoverOperator,
                      MutationOperator<DoubleSolution> mutation
    ) {
        super(measureManager,problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
        violationThresholdComparator = new NormalizedOverallViolationThreholdComparator<>();
    }
}
