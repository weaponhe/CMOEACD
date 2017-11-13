package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * Created by X250 on 2016/12/26.
 */
public class MOEACDNF extends MOEACDN {
    public MOEACDNF(Problem<DoubleSolution> problem,
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
    }

    public MOEACDNF(Measurable measureManager, Problem<DoubleSolution> problem,
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
    }


    protected void chooseCrossoverType(){
        if (randomGenerator.nextDouble() < 0.5)
            crossoverType = CrossoverType.SBX;
        else {
            crossoverType = CrossoverType.DE;
        }
    }

    @Override public String getName() {
        return "MOEA/CD-N-F" ;
    }

    @Override public String getDescription() {
        return "MOEA/CD with fixed probability for choosing SBX and DE" ;
    }
}
