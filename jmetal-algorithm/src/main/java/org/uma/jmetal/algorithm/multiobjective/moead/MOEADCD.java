package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.UniformWeightUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by X250 on 2016/3/22.
 */
public class MOEADCD extends AbstractMOEAD<DoubleSolution> {
    protected DifferentialEvolutionCrossover differentialEvolutionCrossover;

    protected double[] thetas;
    protected double[] thetasMin;
    protected double[] thetasMax;
    protected double[][] directions;

    protected int kIndex;

    public MOEADCD(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                   CrossoverOperator<DoubleSolution> crossover, MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                   String dataDirectory, double neighborhoodSelectionProbability,
                   int maximumNumberOfReplacedSolutions, int neighborSize, int kIndex) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
        differentialEvolutionCrossover = (DifferentialEvolutionCrossover) crossoverOperator;

        thetas = new double[populationSize];
        thetasMin = new double[populationSize];
        thetasMax = new double[populationSize];
        directions = new double[populationSize][problem.getNumberOfObjectives()];

        this.kIndex = kIndex;
    }
    public MOEADCD(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                   CrossoverOperator<DoubleSolution> crossover, MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                   int[] arrayH,double[] integratedTau, double neighborhoodSelectionProbability,
                   int maximumNumberOfReplacedSolutions, int neighborSize, int kIndex) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                arrayH,integratedTau, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);

        differentialEvolutionCrossover = (DifferentialEvolutionCrossover) crossoverOperator;

        thetas = new double[populationSize];
        thetasMin = new double[populationSize];
        thetasMax = new double[populationSize];
        directions = new double[populationSize][problem.getNumberOfObjectives()];

        this.kIndex = kIndex;
    }


    @Override
    public void run() {
        initializeUniformWeight();
        initializeNeighborhood();
        initializePopulation();

        initializeIdealPoint();

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
                updateNeighborhood(child, subProblemId, neighborType);
            }

            generation++;

        } while (evaluations < maxEvaluations);

    }


    protected void initializeUniformWeight() {
        super.initializeUniformWeight();
        for (int i=0;i<lambda.length;i++){
            for (int j=0;j<lambda[i].length;j++){
                directions[i][j] = lambda[i][j];
            }
        }
        if (MOEAD.FunctionType.TCH.equals(functionType)) {
            for (int i=0;i<directions.length;i++){
                for (int j=0;j<directions[i].length;j++){
                    if(directions[i][j] <= Constant.TOLERATION)
                        directions[i][j] = 1.0 / 0.0001;
                    else
                        directions[i][j] = 1.0/directions[i][j];
                }
            }
        }
    }

    protected void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution) problem.createSolution();

            thetasMin[i] = MOEADUtils.angle(directions[i], directions[neighborhood[i][1]]);
            thetasMax[i] = 2.0 * MOEADUtils.maxAngle2Axis(directions[i]);
            thetas[i] = thetasMin[i] + (1.0 * kIndex / 9.0) * (thetasMax[i] - thetasMin[i]);

            problem.evaluate(newSolution);
            population.add(newSolution);
        }
    }

    /**
     * Update neighborhood method
     * @param individual
     * @param subProblemId
     * @param neighborType
     * @throws JMetalException
     */
    @SuppressWarnings("unchecked")
    protected  void updateNeighborhood(DoubleSolution individual, int subProblemId, NeighborType neighborType) throws JMetalException {
        int size;
        int time;

        time = 0;

        if (neighborType == NeighborType.NEIGHBOR) {
            size = neighborhood[subProblemId].length;
        } else {
            size = population.size();
        }
        int[] perm = new int[size];

        MOEADUtils.randomPermutation(perm, size);

        for (int i = 0; i < size; i++) {
            int k;
            if (neighborType == NeighborType.NEIGHBOR) {
                k = neighborhood[subProblemId][perm[i]];
            } else {
                k = perm[i];
            }
            double f1, f2;

//            f1 = fitnessFunction(population.get(k), lambda[k]);
//            f2 = fitnessFunction(individual, lambda[k]);
            f1 = fitnessFunction(population.get(k),lambda[k],directions[k],thetas[k]);
            f2 = fitnessFunction(individual,lambda[k],directions[k],thetas[k]);

            if (f2 < f1) {
                population.set(k, (DoubleSolution) individual.copy());
                time++;
            }

            if (time >= maximumNumberOfReplacedSolutions) {
                return;
            }
        }
    }

    double fitnessFunction(DoubleSolution individual, double[] lambda , double[] direction,double theta) throws JMetalException {
        double fitness;

        fitness = fitnessFunction(individual,lambda);

        double[] normObjectives = new double[problem.getNumberOfObjectives()];
        for(int j=0;j<problem.getNumberOfObjectives();++j)
            normObjectives[j] = individual.getObjective(j) - idealPoint[j];

        if (MOEADUtils.angle(direction,normObjectives  ) > 0.5*theta)
        {
            fitness = Double.POSITIVE_INFINITY;
        }

        return fitness;
    }

    @Override
    public String getName() {
        return "MOEADCD";
    }

    @Override
    public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition. Version with Constrained Decomposition Approach";
    }
}