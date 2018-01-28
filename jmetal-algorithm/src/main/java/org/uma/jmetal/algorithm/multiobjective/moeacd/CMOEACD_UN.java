package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by weaponhe on 2018/1/9.
 */
public class CMOEACD_UN extends CMOEACD {
    public CMOEACD_UN(Problem<DoubleSolution> problem,
                      int[] arrayH,
                      double[] integratedTaus,
                      int populationSize,
                      int constraintLayerSize,
                      int maxEvaluations,
                      int maxGen,
                      int neighborhoodSize,
                      double neighborhoodSelectionProbability,
                      SBXCrossover sbxCrossoverOperator,
                      DifferentialEvolutionCrossover deCrossoverOperator,
                      MutationOperator<DoubleSolution> mutation,
                      AbstractMOEAD.FunctionType functionType,
                      double[] delta
    ) {
        super(problem,
                arrayH,
                integratedTaus,
                populationSize,
                constraintLayerSize,
                maxEvaluations,
                maxGen,
                neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,
                deCrossoverOperator,
                mutation,
                functionType,
                delta);
    }

    protected boolean updateSubproblem(ConeSubRegion targetSubRegion, DoubleSolution solution) {
        boolean isUpdated = false;
        List<Integer> pop = targetSubRegion.getSubPopulation();
        List<Integer> waitingReplacedLayers = new ArrayList<>();
        int idxTargetSubRegion = targetSubRegion.getIdxConeSubRegion();
        for (int i = 0; i < constraintLayerSize; i++) {
            int idxStoredSolution = pop.get(i);
            int idealSubproblemIndex = locateConeSubRegion(population.get(idxStoredSolution), utopianPoint, normIntercepts).getIdxConeSubRegion();
            if (idealSubproblemIndex != idxTargetSubRegion) {
                waitingReplacedLayers.add(i);
            }
        }
        if (waitingReplacedLayers.size() == 0) {
            int idxTargetLayer = queryConstraitLayerWithNormalization(solution, utopianPoint, normIntercepts);
            if (idxTargetLayer >= constraintLayerSize || idxTargetLayer < 0) {
                idxTargetLayer = 0;
            }
            int idxOldSolution = pop.get(idxTargetLayer);
            DoubleSolution newSolution = solution;
            DoubleSolution storedSolution = population.get(idxOldSolution);
            ComparisonMethod method = ComparisonMethod.CDP;
            DoubleSolution betterSolution = getBetterSolution(newSolution, storedSolution, targetSubRegion, method);
            if (betterSolution == newSolution) {
                population.set(pop.get(idxTargetLayer), solution);
                isUpdated = true;
            }
        } else {
            int idxTargetLayer = waitingReplacedLayers.get(randomGenerator.nextInt(0, waitingReplacedLayers.size() - 1));
            population.set(pop.get(idxTargetLayer), solution);
            isUpdated = true;
        }

        if (isUpdated) {
            classifiedPopulation = classifyPopulation();
            initializeSubExtremePoints(classifiedPopulation);
        }
        return isUpdated;
    }

    @Override
    protected boolean updatePopulation(DoubleSolution solution, double[] idealPoint, double[] utopianPoint, double[] normIntercepts) {
        ConeSubRegion targetSubRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
        List<Integer> neighborIndexes = new ArrayList<>(targetSubRegion.getNeighbors());
        Collections.shuffle(neighborIndexes);
        int nr = 2;
        int counter = 0;
        for (int i = 0; i < neighborIndexes.size() && counter < nr; i++) {
            int targetNeibourIndex = neighborIndexes.get(i);
            boolean subproblemUpdated = updateSubproblem(subRegionManager.getConeSubRegion(targetNeibourIndex), solution);
            if (subproblemUpdated) {
                counter++;
            }
        }
        return counter > 0;
    }

    @Override
    public String getName() {
        return "CMOEA/CD-UN";
    }

    @Override
    public String getDescription() {
        return "CMOEA/CD Update Neibours";
    }
}
