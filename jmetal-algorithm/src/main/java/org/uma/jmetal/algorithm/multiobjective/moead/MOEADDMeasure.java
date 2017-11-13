package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;

/**
 * Created by X250 on 2016/4/9.
 */
public class MOEADDMeasure extends MOEADD implements Measurable {
    protected MyAlgorithmMeasures measure;
    MOEADDMeasure(Problem<DoubleSolution> problem,
                 int populationSize,
                 int resultPopulationSize,
                 int maxEvaluations,
                 CrossoverOperator<DoubleSolution> crossover,
                 MutationOperator<DoubleSolution> mutation,
                 FunctionType functionType,
                 String dataDirectory,
                 double neighborhoodSelectionProbability,
                 int maximumNumberOfReplacedSolutions,
                 int neighborSize){
        super(problem,populationSize,resultPopulationSize, maxEvaluations, crossover,mutation,functionType, dataDirectory, neighborhoodSelectionProbability,maximumNumberOfReplacedSolutions, neighborSize);
        measure = new MyAlgorithmMeasures<DoubleSolution>();
        measure.initMeasures();
    }

    MOEADDMeasure(Problem<DoubleSolution> problem,
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
                   int neighborSize){
        super(problem,populationSize,resultPopulationSize, maxEvaluations, crossover,mutation,functionType, arrayH,integratedTau, neighborhoodSelectionProbability,maximumNumberOfReplacedSolutions, neighborSize);
        measure = new MyAlgorithmMeasures<DoubleSolution>();
        measure.initMeasures();
    }
    @Override
    public MeasureManager getMeasureManager() {
        return measure.getMeasureManager();
    }

    @Override public void run(){
        //Start
        measure.durationMeasure.start();
        initializePopulation() ;
        initializeUniformWeight();
        initializeNeighborhood();
        initializeIdealPoint() ;
        initializeNadirPoint();


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
        //calculate measure
        measure.updateMeasureProgress(getMeasurePopulation());
        int lastEvaluations = evaluations;
        do {
            int[] permutation = new int[populationSize];
            MOEADUtils.randomPermutation(permutation, populationSize);

            for (int i = 0; i < populationSize && evaluations < maxEvaluations; i++) {
                int subProblemId = permutation[i];

                NeighborType neighborType = chooseNeighborType() ;
                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType) ;

                List<DoubleSolution> children = crossoverOperator.execute(parents);

                DoubleSolution child1 = children.get(0) ;
                DoubleSolution child2 = children.get(1);
                mutationOperator.execute(child1);
                mutationOperator.execute(child2);
                problem.evaluate(child1);
                problem.evaluate(child2);

                evaluations += 2;

                updateIdealPoint(child1);
                updateIdealPoint(child2);

                updateNadirPoint(child1);
                updateNadirPoint(child2);

                updateArchive(child1);
                updateArchive(child2);
                if(evaluations >= lastEvaluations + populationSize ){
                    //calculate measure
                    measure.updateMeasureProgress(getMeasurePopulation());
                    lastEvaluations += populationSize;
                }
            }

        } while (evaluations < maxEvaluations);
        measure.durationMeasure.stop();
    }
}

//    @Override public void run(){
//        //Start
//        measure.durationMeasure.start();
//        initializePopulation() ;
//        initializeUniformWeight();
//        initializeNeighborhood();
//        initializeIdealPoint() ;
//        initializeNadirPoint();
//
//
//        // initialize the distance
//        for (int i = 0; i < populationSize; i++) {
//            double distance = calculateDistance2(population.get(i), lambda[i]);
//            subregionDist[i][i] = distance;
//        }
//
//
//        // Non-dominated sorting for feasible solutions
//        ranking = ranking.computeRanking(population);
//        int curRank;
//        for (int i = 0; i < populationSize; i++) {
//            curRank = ranking.getAttribute(population.get(i));
//            rankIdx[curRank][i] = 1;
//        }
//
//        evaluations = populationSize ;
//        //calculate measure
//        measure.updateMeasureProgress(getPopulation());
//        int lastEvaluations = evaluations;
//        do {
//            int[] permutation = new int[populationSize];
//            MOEADUtils.randomPermutation(permutation, populationSize);
//
//            for (int i = 0; i < populationSize; i++) {
//                int subProblemId = permutation[i];
//
//                NeighborType neighborType = chooseNeighborType() ;
//                List<DoubleSolution> parents = parentSelection(subProblemId, neighborType) ;
//
//                List<DoubleSolution> children = crossoverOperator.execute(parents);
//
//                DoubleSolution child1 = children.get(0) ;
//                mutationOperator.execute(child1);
//                problem.evaluate(child1);
//
//                evaluations ++;
//
//                updateIdealPoint(child1);
//
//                updateNadirPoint(child1);
//
//                updateArchive(child1);
//
//            }
//            //calculate measure
//            measure.updateMeasureProgress(getPopulation());
//        } while (evaluations < maxEvaluations);
//        measure.durationMeasure.stop();
//    }
//}

