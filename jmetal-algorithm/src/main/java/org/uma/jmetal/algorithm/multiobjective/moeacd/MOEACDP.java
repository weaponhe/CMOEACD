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
 * Created by X250 on 2016/9/24.
 */
public class MOEACDP  extends MOEACD {

    protected List<double[]> predefinedDirections = null;
    public MOEACDP(Problem<DoubleSolution> problem,
                   List<double[]> predefinedDirections,
                   int populationSize,
                   int maxEvaluations,
                   int neighborhoodSize,
                   double neighborhoodSelectionProbability,
                    SBXCrossover sbxCrossoverOperator,
                   DifferentialEvolutionCrossover deCrossoverOperator,
                   MutationOperator<DoubleSolution> mutation
    ) {
        super(problem, new int[]{1}, new double[]{1.0},
                populationSize, maxEvaluations, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
        this.predefinedDirections = predefinedDirections;
    }

    public MOEACDP(Measurable measureManager, Problem<DoubleSolution> problem,
                   List<double[]> predefinedDirections,
                   int populationSize,
                   int maxEvaluations,
                   int neighborhoodSize,
                   double neighborhoodSelectionProbability,
                   SBXCrossover sbxCrossoverOperator,
                   DifferentialEvolutionCrossover deCrossoverOperator,
                   MutationOperator<DoubleSolution> mutation
    ) {
        super(measureManager, problem, new int[]{1}, new double[]{1.0},
                populationSize, maxEvaluations, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
        this.predefinedDirections = predefinedDirections;
    }

    /**
     * Initialize subproblems
     */

    protected void initializeConeSubRegions() {
        subRegionManager.generateConeSubRegionList(predefinedDirections);
        subRegionManager.initializingSubRegionsNeighbors(neighborhoodSize);
        findD0();
    }

    @Override
    public String getName() {
        return "MOEA/CD-P";
    }

    @Override
    public String getDescription() {
        return "MOEA/CD for preference";
    }
}