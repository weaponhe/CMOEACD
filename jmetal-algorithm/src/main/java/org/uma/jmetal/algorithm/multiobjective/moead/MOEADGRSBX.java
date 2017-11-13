package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/9/10.
 */
public class MOEADGRSBX extends AbstractMOEAD<DoubleSolution>  {

    public MOEADGRSBX(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                      CrossoverOperator<DoubleSolution> crossover, MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                      String dataDirectory, double neighborhoodSelectionProbability,
                      int maximumNumberOfReplacedSolutions, int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
    }

    public MOEADGRSBX(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                      CrossoverOperator<DoubleSolution> crossover,MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                      int[] arrayH,double[] integratedTau, double neighborhoodSelectionProbability,
                      int maximumNumberOfReplacedSolutions, int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                arrayH,integratedTau, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
    }

    @Override public void run() {
        initializePopulation() ;
        initializeUniformWeight();
        initializeNeighborhood();
        initializeIdealPoint() ;

        evaluations = populationSize ;
        do {

            for (int i = 0; i < populationSize; i++) {
                int subProblemId = i;
                NeighborType neighborType = chooseNeighborType() ;

                List<DoubleSolution> parents = parentSelection(subProblemId,neighborType) ;

                List<DoubleSolution> children = crossoverOperator.execute(parents);

                DoubleSolution child = children.get(0) ;
                mutationOperator.execute(child);
                problem.evaluate(child);

                evaluations++;

                updateIdealPoint(child);
                int appropriateSubProblemId = findAppropriateSubproblem(child);
                updateNeighborhood(child, appropriateSubProblemId);
            }
        } while (evaluations < maxEvaluations);
    }

    protected void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution)problem.createSolution();

            problem.evaluate(newSolution);
            population.add(newSolution);
        }
    }
    protected List<DoubleSolution> parentSelection(int subProblemId, NeighborType neighborType) {
        List<Integer> matingPool = matingSelection(subProblemId, 2,neighborType);

        List<DoubleSolution> parents = new ArrayList<>(2);

        parents.add(population.get(matingPool.get(0)));
        parents.add(population.get(matingPool.get(1)));

        return parents ;
    }

    /**
     *
     * @param subproblemId the id of current subproblem
     * @param neighbourType neighbour type
     */
    protected List<Integer> matingSelection(int subproblemId, int numberOfSolutionsToSelect, NeighborType neighbourType) {
        int neighbourSize;
        int selectedSolution;

        List<Integer> listOfSolutions = new ArrayList<>(numberOfSolutionsToSelect) ;

        neighbourSize = neighborhood[subproblemId].length;
        while (listOfSolutions.size() < numberOfSolutionsToSelect) {
            int random;
            if (neighbourType == NeighborType.NEIGHBOR) {
                random = randomGenerator.nextInt(0, neighbourSize - 1);
                selectedSolution = neighborhood[subproblemId][random];
            } else {
                selectedSolution = randomGenerator.nextInt(0, populationSize - 1);
            }
            boolean flag = true;
            for (Integer individualId : listOfSolutions) {
                if (individualId == selectedSolution) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                listOfSolutions.add(selectedSolution);
            }
        }

        return listOfSolutions ;
    }

    protected int findAppropriateSubproblem(DoubleSolution indiv){
        double minFitness = Double.MAX_VALUE;
        int minIdx = 0;
        for (int i=0;i<population.size();i++){
            double f = fitnessFunction(indiv, lambda[i]);
            if(f < minFitness){
                minFitness = f ;
                minIdx = i;
            }
        }
        return minIdx;
    }

    protected  void updateNeighborhood(DoubleSolution individual, int subProblemId) throws JMetalException {
        int size;

        size = neighborhood[subProblemId].length;

        for (int i = 0; i < size; i++) {
            int k = neighborhood[subProblemId][i];

            double f1, f2;

            f1 = fitnessFunction(population.get(k), lambda[k]);
            f2 = fitnessFunction(individual, lambda[k]);

            if (f2 < f1) {
                population.set(k, (DoubleSolution) individual.copy());
            }
        }
    }

    @Override public String getName() {
        return "MOEAD" ;
    }

    @Override public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition" ;
    }
}