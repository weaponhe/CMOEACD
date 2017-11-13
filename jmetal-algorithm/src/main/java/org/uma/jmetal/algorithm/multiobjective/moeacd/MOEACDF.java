package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/11/28.
 */
public class MOEACDF extends MOEACD {

    public MOEACDF(Problem<DoubleSolution> problem,
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

    public MOEACDF(Measurable measureManager, Problem<DoubleSolution> problem,
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
        return "MOEA/CD-F" ;
    }

    @Override public String getDescription() {
        return "MOEA/CD with fixed probability for choosing SBX and DE" ;
    }
}
