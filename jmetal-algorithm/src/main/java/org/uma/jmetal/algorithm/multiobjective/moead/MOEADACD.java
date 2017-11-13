package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACD;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;

/**
 * Created by X250 on 2016/3/22.
 */
public class MOEADACD extends MOEADCD {
    public MOEADACD(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                    CrossoverOperator<DoubleSolution> crossover,MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                   String dataDirectory, double neighborhoodSelectionProbability,
                   int maximumNumberOfReplacedSolutions, int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize,5);
    }

    public MOEADACD(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                    CrossoverOperator<DoubleSolution> crossover,MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                    int[] arrayH,double[] integratedTau, double neighborhoodSelectionProbability,
                    int maximumNumberOfReplacedSolutions, int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                arrayH,integratedTau, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize,5);
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

            updateTheta();

        } while (evaluations < maxEvaluations);

    }

    protected void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution) problem.createSolution();

            thetasMin[i] = MOEADUtils.angle(directions[i], directions[neighborhood[i][1]]);
            thetasMax[i] = 2.0 * MOEADUtils.maxAngle2Axis(directions[i]);
            thetas[i] = thetasMin[i];

            problem.evaluate(newSolution);
            population.add(newSolution);
        }
    }

    protected void updateTheta(){
        double[] tmp = new double[populationSize];
        double sumTmp = 0.0;
        double[] normObjectives = new double[problem.getNumberOfObjectives()];
        for(int i=0;i<populationSize;++i){
            for(int j=0;j<problem.getNumberOfObjectives();++j)
                normObjectives[j] = population.get(i).getObjective(j) - idealPoint[j];

            tmp[i] = MOEADUtils.angle(directions[i],normObjectives);
            sumTmp += tmp[i];
        }
        for(int i=0;i<populationSize;++i){
            if(( tmp[i]*populationSize / sumTmp) > 1.0){
                thetas[i] = thetas[i] - thetasMin[i];
                thetas[i] = thetas[i] < thetasMin[i] ? thetasMin[i] : thetas[i];
            }
            else if((populationSize * tmp[i] / sumTmp) < 1.0){
                thetas[i] = thetas[i] + thetasMin[i];
                thetas[i] = thetas[i] > thetasMax[i] ? thetasMax[i] : thetas[i];
            }
        }

    }

    @Override
    public String getName() {
        return "MOEADACD";
    }

    @Override
    public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition. Version with Adaptive Constrained Decomposition Approach";
    }
}
