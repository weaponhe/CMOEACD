package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moeacd.AbstractMOEACD;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/9/8.
 */
public class MOEADACDSBX extends AbstractMOEAD<DoubleSolution> {

    protected double[] thetas;
    protected double[] thetasMin;
    protected double[] thetasMax;
    protected double[][] directions;

    public MOEADACDSBX(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                    CrossoverOperator<DoubleSolution> crossover, MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                    String dataDirectory, double neighborhoodSelectionProbability,
                    int maximumNumberOfReplacedSolutions, int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
        thetas = new double[populationSize];
        thetasMin = new double[populationSize];
        thetasMax = new double[populationSize];
        directions = new double[populationSize][problem.getNumberOfObjectives()];
    }

    public MOEADACDSBX(Problem<DoubleSolution> problem, int populationSize, int resultPopulationSize, int maxEvaluations,
                    CrossoverOperator<DoubleSolution> crossover,MutationOperator<DoubleSolution> mutation, FunctionType functionType,
                    int[] arrayH,double[] integratedTau, double neighborhoodSelectionProbability,
                    int maximumNumberOfReplacedSolutions, int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                arrayH,integratedTau, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);

        thetas = new double[populationSize];
        thetasMin = new double[populationSize];
        thetasMax = new double[populationSize];
        directions = new double[populationSize][problem.getNumberOfObjectives()];
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

            for (int i = 0; i < populationSize; i++) {
                int subProblemId = i;

                NeighborType neighborType = chooseNeighborType();
                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType);

                List<DoubleSolution> children = crossoverOperator.execute(parents);

                DoubleSolution child = children.get(0) ;
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
                        directions[i][j] = 1.0 / Constant.TOLERATION;
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
            if((populationSize * tmp[i] / sumTmp) > 1.0){
                thetas[i] = thetas[i] - thetasMin[i];
                thetas[i] = thetas[i] < thetasMin[i] ? thetasMin[i] : thetas[i];
            }
            else if((populationSize * tmp[i] / sumTmp) < 1.0){
                thetas[i] = thetas[i] + thetasMin[i];
                thetas[i] = thetas[i] > thetasMax[i] ? thetasMax[i] : thetas[i];
            }
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
        return "MOEADACD";
    }

    @Override
    public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition. Version with Adaptive Constrained Decomposition Approach";
    }
}
