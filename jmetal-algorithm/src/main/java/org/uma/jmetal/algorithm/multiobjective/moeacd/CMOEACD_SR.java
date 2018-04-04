package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.Random;


/**
 * Created by weaponhe on 2017/11/28.
 */
public class CMOEACD_SR extends MOEACD implements Measurable {
    private double pf = 0.05;

    public CMOEACD_SR(Measurable measureManager,
                      Problem<DoubleSolution> problem,
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
        super(measureManager, problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, maxGen, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, deCrossoverOperator, mutation, functionType);
    }

    @Override
    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion, double[] utopianPoint, double[] normIntercepts) {
        return getBetterSolutionByIndicator(newSolution, storedSolution, coneSubRegion);
    }

    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion) {
        double r = randomGenerator.nextDouble(0, 1);
        boolean newFessible = isFessible(newSolution);
        boolean storeFessible = isFessible(storedSolution);
        if ((newFessible && storeFessible) || r < pf) {
            double newFun = fitnessFunction(newSolution, coneSubRegion.getRefDirection());
            double storeFun = fitnessFunction(storedSolution, coneSubRegion.getRefDirection());
            return newFun < storeFun ? newSolution : storedSolution;
        } else {
            double newCV = getOverallConstraintViolationDegree(newSolution);
            double storedCV = getOverallConstraintViolationDegree(storedSolution);
            return newCV < storedCV ? newSolution : storedSolution;
        }
    }

    @Override
    public MeasureManager getMeasureManager() {
        return measureManager.getMeasureManager();
    }

    @Override
    public String getName() {
        return "CMOEA/CD-SR";
    }

    @Override
    public String getDescription() {
        return "MOEA/CD Constraint handle using Stochastic Ranking";
    }
}
