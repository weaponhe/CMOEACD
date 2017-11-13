package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/2.
 */
public class MOEACDAECV extends MOEACDACV {
    protected double c = 1.0;

    protected List<List<Double>> AECVParameters = null;

    public MOEACDAECV(Problem<DoubleSolution> problem,
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

    public MOEACDAECV(Measurable measureManager, Problem<DoubleSolution> problem,
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

    protected double weight(double[] lambda, double[] referenceLambda) {
        double w = 0.0;
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            w += Math.pow(lambda[i] - referenceLambda[i], 2.0);
        }
//        w *= (c * populationSize / Math.pow(problem.getNumberOfObjectives(), 2.0));
        w *= (c * populationSize / problem.getNumberOfObjectives());
        return 1.0 + w;
    }

    protected void initializedACVParameters(){
        ACVParameters = new ArrayList<>(subRegionManager.getConeSubRegionsNum());
        AECVParameters = new ArrayList<>(subRegionManager.getConeSubRegionsNum());
        for (int i=0;i<subRegionManager.getConeSubRegionsNum();i++){
            double[] refDirections = subRegionManager.getConeSubRegion(i).getRefDirection();
            List<Double> parameterSet = new ArrayList<>();
            List<Double> AECVParameterSet = new ArrayList<>();
            for (int j = 0;j<ACVDirections.get(i).size();j++){
                double[] direction = ACVDirections.get(i).get(j);
                double parameter = 0.0;
                for (int k = 0;k<problem.getNumberOfObjectives();k++) {
                    parameter += Math.pow(direction[k], 2.0);
                }
                parameterSet.add(parameter);
                AECVParameterSet.add(parameter*weight(direction,refDirections));
            }
            ACVParameters.add(parameterSet);
            AECVParameters.add(AECVParameterSet);
        }
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
            double ACVolNewSolution = ACVol(newSolution, ACVDirections.get(index), AECVParameters.get(index),utopianPoint,normIntercepts);
            double ACVolStoredSolution = ACVol(storedSolution, ACVDirections.get(index), AECVParameters.get(index),utopianPoint,normIntercepts);

            if (ACVolNewSolution < ACVolStoredSolution)
                return newSolution;
            else
                return storedSolution;
        }
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

        double ACVol1 = ACVol(solution1,ACVDirections.get(subRegion1.getIdxConeSubRegion()),AECVParameters.get(subRegion1.getIdxConeSubRegion()),utopianPoint,normIntercepts);
        double ACVol2 = ACVol(solution2,ACVDirections.get(subRegion2.getIdxConeSubRegion()),AECVParameters.get(subRegion2.getIdxConeSubRegion()),utopianPoint,normIntercepts);

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
