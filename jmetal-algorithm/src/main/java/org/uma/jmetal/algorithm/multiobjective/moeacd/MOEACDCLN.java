package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;

/**
 * Created by weaponhe on 2017/11/28.
 */
public class MOEACDCLN extends MOEACD {
    public MOEACDCLN(Problem<DoubleSolution> problem,
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

    protected int queryConstraitLayer(DoubleSolution solution, double[] utopianPoint, double[] normIntercepts) {
        ConeSubRegion subproblem = locateConeSubRegion(solution, utopianPoint, normIntercepts);
        int subproblemIndex = subproblem.getIdxConeSubRegion();
        double x = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
        double y = fitnessFunction(solution, subproblem.getRefDirection());
        List<Double> subUtopianPoint = this.subPlaneIdealPointList.get(subproblemIndex);
        double xTrans = x - subUtopianPoint.get(0);
        double yTrans = y - subUtopianPoint.get(1);
        int k = (int) Math.floor(((constraintLayerSize - 1) * xTrans / (xTrans + yTrans)) + 0.5);
        return k;
    }

    @Override
    public String getName() {
        return "MOEA/CD-CLN";
    }

    @Override
    public String getDescription() {
        return "MOEA/CD Variant: Constraint Layer Normalization";
    }
}
