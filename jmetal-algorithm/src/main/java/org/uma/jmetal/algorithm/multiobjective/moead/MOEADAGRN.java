package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalException;

import java.util.List;

/**
 * Created by X250 on 2016/9/2.
 */
public class MOEADAGRN extends MOEADAGR {
    protected double[] intercepts;
    protected double[] normIntercepts;

    public MOEADAGRN(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                    CrossoverOperator<DoubleSolution> crossover, MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                    String dataDirectory, double neighborhoodSelectionProbability,
                    int maximumNumberOfReplacedSolutions, int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
        intercepts = new double[problem.getNumberOfObjectives()];
        normIntercepts = new double[problem.getNumberOfObjectives()];
    }

    public MOEADAGRN(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                    CrossoverOperator<DoubleSolution> crossover, MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                    int[] arrayH, double[] integratedTau, double neighborhoodSelectionProbability,
                    int maximumNumberOfReplacedSolutions, int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                arrayH, integratedTau, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
        intercepts = new double[problem.getNumberOfObjectives()];
        normIntercepts = new double[problem.getNumberOfObjectives()];
    }

    @Override
    public void run() {
        initializeUniformWeight();
        initializeNeighborhood();
        initializePopulation();

        initializeIdealPoint();
        initializeNadirPoint();
        initializeIntercepts();

        int generation = 0;
        evaluations = populationSize;
        do {
            int[] permutation = new int[populationSize];
            MOEADUtils.randomPermutation(permutation, populationSize);

            for (int i = 0; i < populationSize; i++) {
                int subProblemId = permutation[i];

                NeighborType neighborType = chooseNeighborType();
                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType);

                differentialEvolutionCrossover.setCurrentSolution(population.get(subProblemId));
                List<DoubleSolution> children = differentialEvolutionCrossover.execute(parents);

                DoubleSolution child = children.get(0);
                mutationOperator.execute(child);
                problem.evaluate(child);

                evaluations++;

                updateIdealPoint(child);
                int appropriateSubProblemId = findAppropriateSubproblem(child);
                updateNeighborhood(child, appropriateSubProblemId, neighborType);
            }

            generation++;
            updateIntercepts();

        } while (evaluations < maxEvaluations);
    }


    protected void initializeIntercepts() {
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            intercepts[i] = nadirPoint[i];
        }
        updateNormIntercepts();
    }

    protected void updateIdealPoint(DoubleSolution individual) {
        boolean isUpdated = false;
        for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
            if (individual.getObjective(n) < idealPoint[n]) {
                idealPoint[n] = individual.getObjective(n);
                isUpdated = true;
            }
        }
        if (isUpdated)
            updateNormIntercepts();
    }

    protected void updateIntercepts() {
        for (int i = 0; i < problem.getNumberOfObjectives(); ++i) {
            intercepts[i] = Double.NEGATIVE_INFINITY;
        }
        for (int i = 0; i < populationSize; ++i) {
            for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
                if (population.get(i).getObjective(j) > intercepts[j]) {
                    intercepts[j] = population.get(i).getObjective(j);
                }
            }
        }
        updateNormIntercepts();
    }

    public void updateNormIntercepts() {
        for (int i = 0; i < problem.getNumberOfObjectives(); ++i) {
            normIntercepts[i] = intercepts[i] - idealPoint[i];
            if (normIntercepts[i] < Constant.TOLERATION)
                normIntercepts[i] = Constant.TOLERATION;
        }
    }

    protected double fitnessFunction(DoubleSolution individual, double[] lambda) throws JMetalException {
        double fitness;
        if (MOEAD.FunctionType.TCH.equals(functionType)) {
            double maxFun = -1.0e+30;

            for (int n = 0; n < problem.getNumberOfObjectives(); n++) {

                double diff = Math.abs((individual.getObjective(n) - idealPoint[n]) / normIntercepts[n]);

                double feval;
                if (lambda[n] == 0) {
                    feval = 0.0001 * diff;
                } else {
                    feval = diff * lambda[n];
                }
                if (feval > maxFun) {
                    maxFun = feval;
                }
            }

            fitness = maxFun;
        } else if (MOEAD.FunctionType.AGG.equals(functionType)) {
            double sum = 0.0;
            for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
                sum += (lambda[n]) * (individual.getObjective(n) - idealPoint[n]) / normIntercepts[n];
            }

            fitness = sum;

        } else if (MOEAD.FunctionType.PBI.equals(functionType)) {
            double d1, d2, nl;
            double theta = 5.0;

            d1 = d2 = nl = 0.0;

            for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
                d1 += (individual.getObjective(i) - idealPoint[i]) / normIntercepts[i] * lambda[i];
                nl += Math.pow(lambda[i], 2.0);
            }
            nl = Math.sqrt(nl);
            if (nl < 1e-10)
                nl = 1e-10;
            d1 = Math.abs(d1) / nl;

            for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
                d2 += Math.pow((individual.getObjective(i) - idealPoint[i]) / normIntercepts[i] - d1 * (lambda[i] / nl), 2.0);
            }
            d2 = Math.sqrt(d2);

            fitness = (d1 + theta * d2);
        } else {
            throw new JMetalException(" MOEAD.fitnessFunction: unknown type " + functionType);
        }
        return fitness;
    }


    @Override
    public String getName() {
        return "MOEADAGRN";
    }

    @Override
    public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition. Version with Global Replacement Approach";
    }
}
