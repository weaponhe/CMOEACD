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
public class CMOEACD_CDP extends MOEACD {
    public CMOEACD_CDP(Problem<DoubleSolution> problem,
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
        if (isFessible(newSolution) && isFessible(storedSolution)) {
            if (fitnessFunction(newSolution, refDirection) < fitnessFunction(storedSolution, refDirection)) {
                return newSolution;
            } else {
                return storedSolution;
            }
        } else if (isFessible(newSolution) && !isFessible(storedSolution)) {
            return newSolution;
        } else if (!isFessible(newSolution) && isFessible(storedSolution)) {
            return storedSolution;
        } else {
            double newCV = Math.abs((double) newSolution.getAttribute("overallConstraintViolationDegree"));
            double storeCV = Math.abs((double) storedSolution.getAttribute("overallConstraintViolationDegree"));
            return newCV < storeCV ? newSolution : storedSolution;
        }
    }

    protected boolean isFessible(DoubleSolution solution) {
        return (double) solution.getAttribute("overallConstraintViolationDegree") >= 0;
    }

    @Override
    public String getName() {
        return "MOEA/CD-CDP";
    }

    @Override
    public String getDescription() {
        return "MOEA/CD Constraint handle using Constraint Dominate Principle";
    }
}
