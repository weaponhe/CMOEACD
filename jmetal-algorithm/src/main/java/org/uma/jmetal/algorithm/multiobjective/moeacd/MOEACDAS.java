package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * Created by weaponhe on 2017/11/28.
 */
public class MOEACDAS extends MOEACD{
    public MOEACDAS(Problem<DoubleSolution> problem,
                     int[] arrayH,
                     double[] integratedTaus,
                     int populationSize,
                     int constraintLayerSize,
                     int maxEvaluations,
                     int maxGen,
                     int neighborhoodSize,
                     double neighborhoodSelectionProbability,
                     AbstractMOEAD.FunctionType functionType,
                     SBXCrossover sbxCrossoverOperator,
                     DifferentialEvolutionCrossover deCrossoverOperator,
                     MutationOperator<DoubleSolution> mutation,
                     double[] delta
    ) {
        super(problem, arrayH, integratedTaus,
                populationSize, constraintLayerSize, maxEvaluations, maxGen, neighborhoodSize,
                neighborhoodSelectionProbability, functionType,
                sbxCrossoverOperator, deCrossoverOperator, mutation, delta);
    }


    @Override
    protected double[] computeDelta() {
        double[] ret = new double[3];

        return ret;
    }

    @Override
    public String getName() {
        return "MOEA/CD-AS";
    }

    @Override
    public String getDescription() {
        return "MOEA/CD Variant: Adaptive Selection";
    }
}
