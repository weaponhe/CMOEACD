package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.Random;


/**
 * Created by weaponhe on 2017/11/28.
 */
public class CMOEACD_SR extends MOEACD {
    public CMOEACD_SR(Problem<DoubleSolution> problem,
                      int[] arrayH,
                      double[] integratedTaus,
                      int populationSize,
                      int maxEvaluations,
                      int maxGen,
                      int neighborhoodSize,
                      double neighborhoodSelectionProbability,
                      SBXCrossover sbxCrossoverOperator,
                      DifferentialEvolutionCrossover deCrossoverOperator,
                      MutationOperator<DoubleSolution> mutation,
                      AbstractMOEAD.FunctionType functionType
    ) {
        super(problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, maxGen, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, deCrossoverOperator, mutation, functionType);
    }

    @Override
    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion, double[] utopianPoint, double[] normIntercepts) {
        return this.getBetterSolutionByIndicatorConstraint(newSolution, storedSolution, coneSubRegion.getRefDirection(), utopianPoint, normIntercepts, beta_ConeUpdate);
    }

    protected DoubleSolution getBetterSolutionByIndicatorConstraint(DoubleSolution newSolution, DoubleSolution storedSolution, double[] refDirection, double[] utopianPoint, double[] normIntercepts, double beta) {
        double rand = randomGenerator.nextDouble(0, 1);
        if (isFessible(newSolution) && isFessible(storedSolution) || rand < 0.05) {
            if (fitnessFunction(newSolution, refDirection) < fitnessFunction(storedSolution, refDirection)) {
                return newSolution;
            } else {
                return storedSolution;
            }
        } else {
            return getOverallConstraintViolationDegree(newSolution) < getOverallConstraintViolationDegree(storedSolution) ? newSolution : storedSolution;
        }
    }

    @Override
    public String getName() {
        return "MOEA/CD-SR";
    }

    @Override
    public String getDescription() {
        return "MOEA/CD Constraint handle using Stochastic Ranking";
    }
}
