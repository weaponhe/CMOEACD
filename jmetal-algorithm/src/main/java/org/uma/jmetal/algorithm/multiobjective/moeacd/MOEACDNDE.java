package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * Created by X250 on 2016/12/26.
 */
public class MOEACDNDE extends MOEACDN {
    public MOEACDNDE(Problem<DoubleSolution> problem,
                     int[] arrayH,
                     double[] integratedTaus,
                     int populationSize,
                     int maxEvaluations,
                     int neighborhoodSize,
                     double neighborhoodSelectionProbability,
                     DifferentialEvolutionCrossover deCrossoverOperator,
                     MutationOperator<DoubleSolution> mutation
    ) {
        super(problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                null,deCrossoverOperator, mutation);
    }

    public MOEACDNDE(Measurable measureManager, Problem<DoubleSolution> problem,
                     int[] arrayH,
                     double[] integratedTaus,
                     int populationSize,
                     int maxEvaluations,
                     int neighborhoodSize,
                     double neighborhoodSelectionProbability,
                     DifferentialEvolutionCrossover deCrossoverOperator,
                     MutationOperator<DoubleSolution> mutation
    ) {
        super(measureManager,problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                null,deCrossoverOperator, mutation);
    }

    protected void chooseCrossoverType(){
        crossoverType = CrossoverType.DE;
    }

    @Override public String getName() {
        return "MOEA/CD-N-DE" ;
    }

    @Override public String getDescription() {
        return "MOEA/CD with DE only" ;
    }
}
