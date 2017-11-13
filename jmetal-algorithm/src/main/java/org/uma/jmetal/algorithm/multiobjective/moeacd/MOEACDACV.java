package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2017/1/2.
 */
public class MOEACDACV extends MOEACD {
    protected int numberOfDirectionsForACV = 0;
    protected int para_e = 10;
    protected List<List<double[]>> ACVDirections = null;
    protected List<List<Double>> ACVParameters = null;

    public MOEACDACV(Problem<DoubleSolution> problem,
                    int[] arrayH,
                    double[] integratedTaus,
                    int populationSize,
                    int maxEvaluations,
                    int neighborhoodSize,
                    double neighborhoodSelectionProbability,
                    SBXCrossover sbxCrossoverOperator,
                    DifferentialEvolutionCrossover deCrossoverOperator,
                    MutationOperator<DoubleSolution> mutation
    ) {
        super(problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
    }

    public MOEACDACV(Measurable measureManager, Problem<DoubleSolution> problem,
                    int[] arrayH,
                    double[] integratedTaus,
                    int populationSize,
                    int maxEvaluations,
                    int neighborhoodSize,
                    double neighborhoodSelectionProbability,
                    SBXCrossover sbxCrossoverOperator,
                    DifferentialEvolutionCrossover deCrossoverOperator,
                    MutationOperator<DoubleSolution> mutation
    ) {
        super(measureManager,problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
    }


    /**
     * Initialize subproblems
     */

    protected void initializeConeSubRegions() {
        subRegionManager.generateConeSubRegionList();
        subRegionManager.initializingSubRegionsNeighbors(neighborhoodSize);
        findD0();
        randomlyGeneratedDirections();
        initializedACVParameters();
    }

    protected void randomlyGeneratedDirections(){
        numberOfDirectionsForACV = para_e * populationSize * (problem.getNumberOfObjectives() - 1);
        ACVDirections = new ArrayList<>(subRegionManager.getConeSubRegionsNum());
        for (int i=0;i<subRegionManager.getConeSubRegionsNum();i++){
            List<double[]> subSet = new ArrayList<>();
            subSet.add(subRegionManager.getConeSubRegion(i).getRefDirection());
            ACVDirections.add(subSet);
        }
        List<Integer> idx = new ArrayList<>();
        for (int i=0;i<problem.getNumberOfObjectives();i++)
            idx.add(i);

        for (int i=0;i<numberOfDirectionsForACV;i++){
            Collections.shuffle(idx);
            double[] direction = new double[problem.getNumberOfObjectives()];
            double left = 1.0;
            for (int j = 0;j < problem.getNumberOfObjectives() - 1;j++){
                direction[idx.get(j)] = randomGenerator.nextDouble(0.0,left);
                left -= direction[idx.get(j)];
            }
            if(problem.getNumberOfObjectives() == 1)
                direction[idx.get(0)] = randomGenerator.nextDouble(0.0,left);
            else
                direction[idx.get(problem.getNumberOfObjectives() -1)] = left;
            ConeSubRegion coneSubRegion = subRegionManager.locateSubRegion(direction);
            int index = coneSubRegion.getIdxConeSubRegion();

            ACVDirections.get(index).add(direction);
        }
    }

    protected void initializedACVParameters(){
        ACVParameters = new ArrayList<>(subRegionManager.getConeSubRegionsNum());
        for (int i=0;i<subRegionManager.getConeSubRegionsNum();i++){
            List<Double> parameterSet = new ArrayList<>();
            for (int j = 0;j<ACVDirections.get(i).size();j++){
                double[] direction = ACVDirections.get(i).get(j);
                double parameter = 0.0;
                for (int k = 0;k<problem.getNumberOfObjectives();k++) {
                    parameter += Math.pow(direction[k], 2.0);
                }
                parameterSet.add(parameter);
            }
            ACVParameters.add(parameterSet);
        }
    }


    protected DoubleSolution getBetterSolutionForNeighborUpdate(DoubleSolution newSolution,DoubleSolution storedSolution,ConeSubRegion neighborSubRegion, double[] utopianPoint, double[] normIntercepts){
        ConeSubRegion idealSubRegion = locateConeSubRegion(storedSolution,utopianPoint,normIntercepts);
        if(idealSubRegion != neighborSubRegion) {
            return getBetterSolutionByIndicator(newSolution, storedSolution, neighborSubRegion,utopianPoint,normIntercepts);
        }
        return storedSolution;
    }

    protected  DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion, double[] utopianPoint, double[] normIntercepts) {
        int domination = MOEACDUtils.dominateCompare(newSolution,storedSolution);
        if(domination == -1 ){
            return newSolution;
        }
        else if(domination == 1|| domination == 2){
            return storedSolution;
        }
        else {
            int index = coneSubRegion.getIdxConeSubRegion();
            double ACVolNewSolution = ACVol(newSolution, ACVDirections.get(index), ACVParameters.get(index),utopianPoint,normIntercepts);
            double ACVolStoredSolution = ACVol(storedSolution, ACVDirections.get(index), ACVParameters.get(index),utopianPoint,normIntercepts);

            if (ACVolNewSolution < ACVolStoredSolution)
                return newSolution;
            else
                return storedSolution;
        }
    }

    protected double ASF(double[] objectives, double[] direction) {
        double max_ratio = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            double weight = direction[i];
            if(weight < 1.0e-6)
                weight = 1.0e-6;
            max_ratio = Math.max(max_ratio, objectives[i]/weight);
        }
        return max_ratio;
    }

    protected double ACVol(DoubleSolution solution, List<double[]> directionSet, List<Double> parameterSet, double[] utopianPoint, double[] normIntercepts){
        double[] normObjectives = MOEACDUtils.normalize(solution,utopianPoint,normIntercepts);
        double acvol = 0.0;
        for (int i=0;i<directionSet.size();i++){
            acvol += ASF(normObjectives,directionSet.get(i))*parameterSet.get(i);
        }
        acvol /= directionSet.size();
        return acvol;
    }

    protected DoubleSolution tourmentSelectionUnConstraint(ConeSubRegion subRegion1, DoubleSolution solution1, ConeSubRegion subRegion2, DoubleSolution solution2,double[] utopianPoint,double[] normIntercepts){

        ConeSubRegion idealSubregion1 = locateConeSubRegion(solution1,utopianPoint,normIntercepts);
        boolean isInPlace1 = (idealSubregion1 == subRegion1);
        ConeSubRegion idealSubregion2 = locateConeSubRegion(solution2,utopianPoint,normIntercepts);
        boolean isInPlace2 = (idealSubregion2 == subRegion2);
        if(isInPlace1 && !isInPlace2)
            return solution1;
        else if(!isInPlace1 && isInPlace2)
            return solution2;

        double ACVol1 = ACVol(solution1,ACVDirections.get(subRegion1.getIdxConeSubRegion()),ACVParameters.get(subRegion1.getIdxConeSubRegion()),utopianPoint,normIntercepts);
        double ACVol2 = ACVol(solution2,ACVDirections.get(subRegion2.getIdxConeSubRegion()),ACVParameters.get(subRegion2.getIdxConeSubRegion()),utopianPoint,normIntercepts);

        if(ACVol1 < ACVol2)
            return solution1;
        else if (ACVol1 > ACVol2)
            return solution2;
        else if(randomGenerator.nextDouble(0.0,1.0) < 0.5)
            return solution1;
        else
            return solution2;
    };
}
