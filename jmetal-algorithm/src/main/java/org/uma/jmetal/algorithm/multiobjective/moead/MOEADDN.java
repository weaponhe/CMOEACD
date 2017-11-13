package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.solutionattribute.impl.DistanceToDirectionAttribute;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;
import org.uma.jmetal.util.solutionattribute.impl.LocationAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by X250 on 2016/7/6.
 */
public class MOEADDN extends MOEADD {
    protected double[] intercepts;
    protected double[] normIntercepts;
    public MOEADDN(Problem<DoubleSolution> problem,
                  int populationSize,
                  int resultPopulationSize,
                  int maxEvaluations,
                  CrossoverOperator<DoubleSolution> crossover,
                  MutationOperator<DoubleSolution> mutation,
                  FunctionType functionType,
                  String dataDirectory,
                  double neighborhoodSelectionProbability,
                  int maximumNumberOfReplacedSolutions,
                  int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
        intercepts = new double[problem.getNumberOfObjectives()];
        normIntercepts = new double[problem.getNumberOfObjectives()];
    }

    public MOEADDN(Problem<DoubleSolution> problem,
                  int populationSize,
                  int resultPopulationSize,
                  int maxEvaluations,
                  CrossoverOperator<DoubleSolution> crossover,
                  MutationOperator<DoubleSolution> mutation,
                  FunctionType functionType,
                  int[] arrayH,
                  double[] integratedTau,
                  double neighborhoodSelectionProbability,
                  int maximumNumberOfReplacedSolutions,
                  int neighborSize) {
        super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
                arrayH,integratedTau, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
                neighborSize);
        intercepts = new double[problem.getNumberOfObjectives()];
        normIntercepts = new double[problem.getNumberOfObjectives()];
    }

    @Override public void run() {
        initializePopulation() ;
        initializeUniformWeight();
        initializeNeighborhood();
        initializeIdealPoint() ;
        initializeNadirPoint();
        initializeIntercepts();


        // initialize the distance
        for (int i = 0; i < populationSize; i++) {
            double distance = calculateDistance2(population.get(i), lambda[i]);
            subregionDist[i][i] = distance;
        }


        // Non-dominated sorting for feasible solutions
        ranking = ranking.computeRanking(population);
        int curRank;
        for (int i = 0; i < populationSize; i++) {
            curRank = ranking.getAttribute(population.get(i));
            rankIdx[curRank][i] = 1;
        }

        evaluations = populationSize ;

        do {
            int[] permutation = new int[populationSize];
            MOEADUtils.randomPermutation(permutation, populationSize);

            for (int i = 0; i < populationSize && evaluations < maxEvaluations; i++) {
                int subProblemId = permutation[i];

                NeighborType neighborType = chooseNeighborType() ;
                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType) ;

                List<DoubleSolution> children = crossoverOperator.execute(parents);

                DoubleSolution child1 = children.get(0);
                DoubleSolution child2 = children.get(1);

                mutationOperator.execute(child1);
                mutationOperator.execute(child2);

                problem.evaluate(child1);
                problem.evaluate(child2);

                evaluations+=2;

                updateIdealPoint(child1);
                updateIdealPoint(child2);

                updateNadirPoint(child1);
                updateNadirPoint(child2);

                updateArchive(child1);
                updateArchive(child2);
            }

            updateIntercepts();

        } while (evaluations < maxEvaluations);
    }


    protected void initializeIntercepts(){
        for(int i=0;i<problem.getNumberOfObjectives();i++){
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
        if(isUpdated)
            updateNormIntercepts();
    }


    protected void updateIntercepts(){
        for(int i=0;i<problem.getNumberOfObjectives();++i){
            intercepts[i] = Double.NEGATIVE_INFINITY;
        }
        for(int i=0;i<populationSize;++i){
            for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
                if (population.get(i).getObjective(j) > intercepts[j]) {
                    intercepts[j] = population.get(i).getObjective(j);
                }
            }
        }
        updateNormIntercepts();
    }

    public void updateNormIntercepts(){
        for(int i=0;i<problem.getNumberOfObjectives();++i){
            normIntercepts[i] = intercepts[i] - idealPoint[i];
            if(normIntercepts[i] < Constant.TOLERATION)
                normIntercepts[i] = Constant.TOLERATION;
        }
    }


    double fitnessFunction(DoubleSolution individual, double[] lambda) throws JMetalException {
        double fitness;
        if (MOEAD.FunctionType.TCH.equals(functionType)) {
            double maxFun = -1.0e+30;

            for (int n = 0; n < problem.getNumberOfObjectives(); n++) {

                double diff = Math.abs((individual.getObjective(n) - idealPoint[n])/normIntercepts[n]);

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
        }else if (MOEAD.FunctionType.AGG.equals(functionType)) {
            double sum = 0.0;
            for (int n = 0; n < problem.getNumberOfObjectives(); n++) {
                sum += (lambda[n]) * (individual.getObjective(n) - idealPoint[n])/normIntercepts[n];
            }

            fitness = sum;

        }else if (MOEAD.FunctionType.PBI.equals(functionType)) {
            double d1, d2, nl;
            double theta = 5.0;

            d1 = d2 = nl = 0.0;

            for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
                d1 += (individual.getObjective(i) - idealPoint[i])/normIntercepts[i] * lambda[i];
                nl += Math.pow(lambda[i], 2.0);
            }
            nl = Math.sqrt(nl);
            if(nl < 1e-10)
                nl = 1e-10;
            d1 = Math.abs(d1) / nl;

            for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
                d2 += Math.pow((individual.getObjective(i) - idealPoint[i])/normIntercepts[i] - d1 * (lambda[i] / nl), 2.0);
            }
            d2 = Math.sqrt(d2);

            fitness = (d1 + theta * d2);
        } else {
            throw new JMetalException(" MOEAD.fitnessFunction: unknown type " + functionType);
        }
        return fitness;
    }

    public double calculateDistance2(DoubleSolution _indiv, double[] _lambda) {

        // normalize the weight vector (line segment)
        double nd = norm_vector(_lambda);
        for (int i = 0; i < problem.getNumberOfObjectives(); i++)
            _lambda[i] = _lambda[i] / nd;

        double[] realA = new double[problem.getNumberOfObjectives()];
        double[] realB = new double[problem.getNumberOfObjectives()];

        // difference between current point and reference point
        for (int i = 0; i < problem.getNumberOfObjectives(); i++)
            realA[i] = (_indiv.getObjective(i) - idealPoint[i])/normIntercepts[i];

        // distance along the line segment
        double d1 = Math.abs(innerproduct(realA, _lambda));

        // distance to the line segment
        for (int i = 0; i < problem.getNumberOfObjectives(); i++)
            realB[i] = (realA[i] - (d1 * _lambda[i]));

        double distance = norm_vector(realB);

        return distance;
    }



    @Override public String getName() {
        return "MOEADDN" ;
    }

    @Override public String getDescription() {
        return "Multi-Objective Evolutionary Algorithm based on Decomposition,  Version with Dominance Concept" ;
    }
}

