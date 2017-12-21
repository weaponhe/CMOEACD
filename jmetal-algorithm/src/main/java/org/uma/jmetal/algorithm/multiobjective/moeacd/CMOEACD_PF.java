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
public class CMOEACD_PF extends MOEACD {
    protected double penaltyFactor;

    public CMOEACD_PF(Problem<DoubleSolution> problem,
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
                      AbstractMOEAD.FunctionType functionType,
                      double penaltyFactor
    ) {
        super(problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, maxGen, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, deCrossoverOperator, mutation, functionType);
        this.penaltyFactor = penaltyFactor;
    }

    @Override
    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion, double[] utopianPoint, double[] normIntercepts) {
        return this.getBetterSolutionByIndicatorConstraint(newSolution, storedSolution, coneSubRegion.getRefDirection(), utopianPoint, normIntercepts, beta_ConeUpdate);
    }

    protected DoubleSolution getBetterSolutionByIndicatorConstraint(DoubleSolution newSolution, DoubleSolution storedSolution, double[] refDirection, double[] utopianPoint, double[] normIntercepts, double beta) {
        double newFun = fitnessFunction(newSolution, refDirection);
        double storedFun = fitnessFunction(storedSolution, refDirection);
        double newPf = getOverallConstraintViolationDegree(newSolution) * penaltyFactor;
        double storedPf = getOverallConstraintViolationDegree(storedSolution) * penaltyFactor;
        double newFitness = newFun + newPf;
        double storedFitness = storedFun + storedPf;
        return newFitness < storedFitness ? newSolution : storedSolution;
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
