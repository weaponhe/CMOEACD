package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/4/10.
 */
public class MOEAD extends AbstractMOEAD<DoubleSolution>  {

    public MOEAD(Problem<DoubleSolution> problem,
                   int populationSize,
                   int resultPopulationSize,
                   int maxEvaluations,
                   CrossoverOperator<DoubleSolution> crossover,
                   MutationOperator<DoubleSolution> mutation,
                   FunctionType functionType,
                   String dataDirectory,
                   int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory, -1, -1,
                neighborSize);
    }

    public MOEAD(Problem<DoubleSolution> problem,
                   int populationSize,
                   int resultPopulationSize,
                   int maxEvaluations,
                   CrossoverOperator<DoubleSolution> crossover,
                   MutationOperator<DoubleSolution> mutation,
                   FunctionType functionType,
                   int[] arrayH,
                   double[] integratedTau,
                   int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                arrayH,integratedTau, -1, -1,
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

                List<DoubleSolution> parents = parentSelection(subProblemId) ;

                List<DoubleSolution> children = crossoverOperator.execute(parents);

                DoubleSolution child = children.get(0) ;
                mutationOperator.execute(child);
                problem.evaluate(child);

                evaluations++;

                updateIdealPoint(child);
                updateNeighborhood(child, subProblemId);
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

    protected List<DoubleSolution> parentSelection(int subProblemId) {
        List<Integer> matingPool = matingSelection(subProblemId, 2);

        List<DoubleSolution> parents = new ArrayList<>(2);

        parents.add(population.get(matingPool.get(0)));
        parents.add(population.get(matingPool.get(1)));

        return parents ;
    }

    /**
     *
     * @param subproblemId the id of current subproblem
     */
    protected List<Integer> matingSelection(int subproblemId, int numberOfSolutionsToSelect) {
        int neighbourSize;
        int selectedSolution;

        List<Integer> listOfSolutions = new ArrayList<>(numberOfSolutionsToSelect) ;

        neighbourSize = neighborhood[subproblemId].length;
        while (listOfSolutions.size() < numberOfSolutionsToSelect) {
            int random = randomGenerator.nextInt(0, neighbourSize - 1);

            selectedSolution = neighborhood[subproblemId][random];

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

    /**
     * Update neighborhood method
     * @param individual
     * @param subProblemId
     * @throws JMetalException
     */
    @SuppressWarnings("unchecked")
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