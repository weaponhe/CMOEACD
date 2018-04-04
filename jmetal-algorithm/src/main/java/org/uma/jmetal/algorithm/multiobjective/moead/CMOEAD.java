package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.impl.ViolationThresholdComparator;

import java.util.List;

/**
 * Created by X250 on 2016/11/18.
 */
public class CMOEAD extends MOEAD {
    protected ViolationThresholdComparator<DoubleSolution> violationThresholdComparator ;

    public CMOEAD(Problem<DoubleSolution> problem,
                  int populationSize,
                  int resultPopulationSize,
                  int maxEvaluations,
                  CrossoverOperator<DoubleSolution> crossover,
                  MutationOperator<DoubleSolution> mutation,
                  FunctionType functionType,
                  String dataDirectory,
                  int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory,neighborSize);
        violationThresholdComparator = new ViolationThresholdComparator<DoubleSolution>() ;
    }

    public CMOEAD(Problem<DoubleSolution> problem,
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
                arrayH,integratedTau,neighborSize);
        violationThresholdComparator = new ViolationThresholdComparator<DoubleSolution>() ;
    }

    @Override public void run() {
        initializePopulation() ;
        initializeUniformWeight();
        initializeNeighborhood();
        initializeIdealPoint() ;

        violationThresholdComparator.updateThreshold(population);

        evaluations = populationSize ;
        do {
            for (int i = 0; i < populationSize; i++) {
                int subProblemId = i;

                List<DoubleSolution> parents = parentSelection(subProblemId) ;

                List<DoubleSolution> children = crossoverOperator.execute(parents);

                DoubleSolution child = children.get(0);
                mutationOperator.execute(child);
                problem.evaluate(child);
                if (problem instanceof ConstrainedProblem) {
                    ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child);
                }
                evaluations++;

                updateIdealPoint(child);
                updateNeighborhood(child, subProblemId);
            }

            violationThresholdComparator.updateThreshold(population);

        } while (evaluations < maxEvaluations);
    }

    public void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution)problem.createSolution() ;

            problem.evaluate(newSolution);
            if (problem instanceof ConstrainedProblem) {
                ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(newSolution);
            }
            population.add(newSolution);
        }
    }

    protected  void updateNeighborhood(DoubleSolution individual, int subProblemId) throws JMetalException {
        int size;

        size = neighborhood[subProblemId].length;

        for (int i = 0; i < size; i++) {
            int k = neighborhood[subProblemId][i];

            double f1, f2;

            f1 = fitnessFunction(population.get(k), lambda[k]);
            f2 = fitnessFunction(individual, lambda[k]);

            if (violationThresholdComparator.needToCompare(population.get(k), individual)) {
                int flag = violationThresholdComparator.compare(population.get(k), individual);
                if (flag == 1) {
                    population.set(k, (DoubleSolution) individual.copy());
                } else if (flag == 0) {
                    if (f2 < f1) {
                        population.set(k, (DoubleSolution) individual.copy());
                    }
                }
            } else {
                if (f2 < f1) {
                    population.set(k, (DoubleSolution) individual.copy());
                }
            }
        }
    }


    @Override public String getName() {
        return "CMOEAD" ;
    }

    @Override public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition with constraints support" ;
    }
}
