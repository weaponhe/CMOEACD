package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moeacd.ConeSubRegion;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.impl.ViolationThresholdComparator;

/**
 * Created by weaponhe on 2018/3/27.
 */
public class CMOEADSR extends ConstraintMOEAD {
    private double pf = 0.05;

    public CMOEADSR(Problem<DoubleSolution> problem,
                    int populationSize,
                    int resultPopulationSize,
                    int maxEvaluations,
                    int maxGen,
                    MutationOperator<DoubleSolution> mutation,
                    CrossoverOperator<DoubleSolution> crossover,
                    FunctionType functionType,
                    String dataDirectory,
                    double neighborhoodSelectionProbability,
                    int maximumNumberOfReplacedSolutions,
                    int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, maxGen, mutation, crossover, functionType,
                dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
    }

    public CMOEADSR(Problem<DoubleSolution> problem,
                    int populationSize,
                    int resultPopulationSize,
                    int maxEvaluations,
                    int maxGen,
                    MutationOperator<DoubleSolution> mutation,
                    CrossoverOperator<DoubleSolution> crossover,
                    FunctionType functionType,
                    int[] arrayH,
                    double[] integratedTau,
                    double neighborhoodSelectionProbability,
                    int maximumNumberOfReplacedSolutions,
                    int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, maxGen, mutation, crossover, functionType,
                arrayH, integratedTau, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
    }

    protected void updateNeighborhood(DoubleSolution individual, int subProblemId) throws JMetalException {
        int size;

        size = neighborhood[subProblemId].length;

        for (int i = 0; i < size; i++) {
            int k = neighborhood[subProblemId][i];
            DoubleSolution betterS = getBetterSolutionByIndicator(individual, population.get(k), lambda[k]);
            if (betterS == individual) {
                population.set(k, (DoubleSolution) individual.copy());
            }
        }
    }

    @Override
    protected void updateNeighborhood(DoubleSolution individual, int subproblemId, NeighborType neighborType) {
        int size;
        int time;

        time = 0;

        if (neighborType == NeighborType.NEIGHBOR) {
            size = neighborhood[subproblemId].length;
        } else {
            size = population.size();
        }
        int[] perm = new int[size];

        MOEADUtils.randomPermutation(perm, size);

        for (int i = 0; i < size; i++) {
            int k;
            if (neighborType == NeighborType.NEIGHBOR) {
                k = neighborhood[subproblemId][perm[i]];
            } else {
                k = perm[i];
            }
            DoubleSolution betterS = getBetterSolutionByIndicator(individual, population.get(k), lambda[k]);
            if (betterS == individual) {
                population.set(k, (DoubleSolution) individual.copy());
                time++;
            }

            if (time >= maximumNumberOfReplacedSolutions) {
                return;
            }
        }
    }

    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, double[] lambda) {
        double r = randomGenerator.nextDouble(0, 1);
        boolean newFessible = isFessible(newSolution);
        boolean storeFessible = isFessible(storedSolution);
        if ((newFessible && storeFessible) || r < pf) {
            double newFun = fitnessFunction(newSolution, lambda);
            double storeFun = fitnessFunction(storedSolution, lambda);
            return newFun < storeFun ? newSolution : storedSolution;
        } else {
            double newCV = getOverallConstraintViolationDegree(newSolution);
            double storedCV = getOverallConstraintViolationDegree(storedSolution);
            return newCV < storedCV ? newSolution : storedSolution;
        }
    }

    protected double getOverallConstraintViolationDegree(DoubleSolution solution) {
        double cv = (double) solution.getAttribute("overallConstraintViolationDegree");
        return cv >= 0 ? 0 : Math.abs(cv);
    }

    protected boolean isFessible(DoubleSolution solution) {
        return getOverallConstraintViolationDegree(solution) == 0;
    }
}
