package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weaponhe on 2017/11/28.
 */
public class MOEACDUP2 extends MOEACD {
    public MOEACDUP2(Problem<DoubleSolution> problem,
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

    protected boolean updatePopulation(DoubleSolution solution, double[] idealPoint, double[] utopianPoint, double[] normIntercepts) {
        boolean isUpdated = false;
        ConeSubRegion targetSubRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
        List<Integer> pop = targetSubRegion.getSubPopulation();
        DoubleSolution firstLayerStoredSolution = population.get(pop.get(0));
        DoubleSolution betterSolution = getBetterSolution(solution, firstLayerStoredSolution, targetSubRegion, ComparisonMethod.CDP);
        if (betterSolution == solution) {
            population.set(pop.get(0), solution);
            isUpdated = true;
        }

        List<Integer> waitingReplacedLayers = new ArrayList<>();
        int idxTargetSubRegion = targetSubRegion.getIdxConeSubRegion();
        for (int i = 1; i < constraintLayerSize; i++) {
            int idxStoredSolution = pop.get(i);
            int idealSubproblemIndex = locateConeSubRegion(population.get(idxStoredSolution), utopianPoint, normIntercepts).getIdxConeSubRegion();
            if (idealSubproblemIndex != idxTargetSubRegion) {
                waitingReplacedLayers.add(i);
            }
        }

        DoubleSolution remainedSolution = null;
        if (waitingReplacedLayers.size() == 0) {
            int idxTargetLayer = queryConstraitLayerWithNormalization(solution, utopianPoint, normIntercepts);
            if (idxTargetLayer != 0) {
                int idxOldSolution = pop.get(idxTargetLayer);
                DoubleSolution storedSolution = population.get(idxOldSolution);
                DoubleSolution betterSolution1 = getBetterSolution(solution, storedSolution, targetSubRegion, ComparisonMethod.CORE_AREA);
                if (betterSolution1 == solution) {
                    population.set(pop.get(idxTargetLayer), solution);
                    isUpdated = true;
                }
            }
        } else {
//            for (int i = 0; i < waitingReplacedLayers.size(); i++) {
//                int idxTargetLayer = waitingReplacedLayers.get(i);
//                population.set(pop.get(idxTargetLayer), newSolution);
//                isUpdated = true;
//            }
            int idxTargetLayer = waitingReplacedLayers.get(randomGenerator.nextInt(0, waitingReplacedLayers.size() - 1));
            remainedSolution = population.get(pop.get(idxTargetLayer));
            population.set(pop.get(idxTargetLayer), solution);
            isUpdated = true;
        }

        if (isUpdated) {
            classifiedPopulation = classifyPopulation();
            initializeSubExtremePoints(classifiedPopulation);
        }

        if (remainedSolution != null) {
            updatePopulation(remainedSolution, idealPoint, utopianPoint, normIntercepts);
        }

        return isUpdated;
    }


    @Override
    public String getName() {
        return "MOEA/CD-UP2";
    }

    @Override
    public String getDescription() {
        return "MOEA/CD Variant: Update first constraint layer firstly";
    }
}
