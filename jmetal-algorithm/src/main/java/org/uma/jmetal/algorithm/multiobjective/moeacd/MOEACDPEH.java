package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Jama.Matrix;
import org.uma.jmetal.util.KDTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2016/12/22.
 */
public class MOEACDPEH extends MOEACD {
    public MOEACDPEH(Problem<DoubleSolution> problem,
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

    public MOEACDPEH(Measurable measureManager, Problem<DoubleSolution> problem,
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


    protected  boolean coneUpdate(DoubleSolution _solution,ConeSubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {
        int idxStoreInPop = targetSubRegion.getIdxSolution();
        DoubleSolution storeInd = population.get(idxStoreInPop);
        ConeSubRegion storeSubRegion = locateConeSubRegion(storeInd,utopianPoint,normIntercepts);
        DoubleSolution betterS = null;
        boolean isUpdated = false;
        if(targetSubRegion == storeSubRegion){
            betterS = getBetterSolutionByIndicator(_solution,population.get(idxStoreInPop),targetSubRegion,utopianPoint,normIntercepts);
            if(betterS == _solution){
                //has updated
                isUpdated = true;
            }
        }
        else{
            isUpdated = true;
            betterS = _solution;
        }

        if(isUpdated){
            population.set(idxStoreInPop,betterS);
        }

        if(targetSubRegion != storeSubRegion){
            //cone update recursively
            coneUpdate(storeInd,storeSubRegion,utopianPoint,normIntercepts);
        }

        return isUpdated;
    }

    //update the association between cone subregion and solution
    protected void associateSubRegion(List<DoubleSolution> population,double[] utopianPoint,double[] normIntercepts) {

        //clearing the associate information
        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); ++i) {
            subRegionManager.getConeSubRegion(i).setIdxSolution(-1);
        }

        List<Integer> remainingSolutionIdx = new ArrayList<>(population.size());
        for (int i = 0; i < population.size(); ++i) {
            //find the cone subregion which the individual belongs to
            ConeSubRegion subRegion = locateConeSubRegion(population.get(i),utopianPoint,normIntercepts);
            if (subRegion.getIdxSolution() < 0) {//No individual has been bound to this subregion
                //bind it
                subRegion.setIdxSolution(i);
            } else {
                int idxBoundInd = subRegion.getIdxSolution();
                int idxWorst = i;
                //choose the better one for subregion by comparing their indicators using in the algorithm
                DoubleSolution betterS = getBetterSolutionByIndicator(population.get(i), population.get(idxBoundInd), subRegion.getRefDirection(),utopianPoint,normIntercepts);
                if (betterS == population.get(i)) {
                    //replace the bound one
                    subRegion.setIdxSolution(i);
                    idxWorst = idxBoundInd;
                }
                //record the worst one
                remainingSolutionIdx.add(idxWorst);
            }
        }

        List<Integer> unboundSubregion = new ArrayList<>(subRegionManager.getConeSubRegionsNum());
        for (int i=0;i<subRegionManager.getConeSubRegionsNum();i++){
            ConeSubRegion subRegion = subRegionManager.getConeSubRegion(i);
            if(subRegion.getIdxSolution() < 0){
                unboundSubregion.add(i);
            }
        }

        Collections.shuffle(remainingSolutionIdx);

        for (int i=0;i<remainingSolutionIdx.size()&&(!unboundSubregion.isEmpty());i++){
            int selectedIdx = nearestUnboundSubRegionIdx(population.get(remainingSolutionIdx.get(i)), unboundSubregion,utopianPoint,normIntercepts);
            ConeSubRegion subRegion = subRegionManager.getConeSubRegion(unboundSubregion.get(selectedIdx));
            subRegion.setIdxSolution(remainingSolutionIdx.get(i));
            unboundSubregion.remove(selectedIdx);
        }
    }

    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, double[] refDirection,double[] utopianPoint,double[] normIntercepts) {
        int domination = MOEACDUtils.dominateCompare(newSolution,storedSolution);
        if(domination == -1 ){
            return newSolution;
        }
        else if(domination == 1|| domination == 2){
            return storedSolution;
        }
        else {
            double[] normRef = new double[problem.getNumberOfObjectives()];
            for (int k=0;k<problem.getNumberOfObjectives();k++) {
                normRef[k] = (referencePoint[k] - utopianPoint[k]) / normIntercepts[k];
            }

            List<double[]> normPop = new ArrayList<>(population.size()-1);
            for (int i = 0; i < population.size(); i++) {
                if (population.get(i) == storedSolution)
                    continue;
                double[] norm = MOEACDUtils.normalize(population.get(i), utopianPoint, normIntercepts);
                for (int k = 0; k < problem.getNumberOfObjectives(); k++) {
                    norm[k] = normRef[k] - norm[k];
                }
                normPop.add(norm);
            }

            double[] normNewSolution = MOEACDUtils.normalize(newSolution,utopianPoint,normIntercepts);
            double[] normStoredSolution = MOEACDUtils.normalize(storedSolution,utopianPoint,normIntercepts);
            for (int k=0;k<problem.getNumberOfObjectives();k++){
                normNewSolution[k] = normRef[k] - normNewSolution[k];
                normStoredSolution[k] = normRef[k] - normStoredSolution[k];
            }

            double PEHNewSolution = PEH(normNewSolution, normPop);
            double PEHStoredSolution = PEH(normStoredSolution, normPop);

            if (PEHNewSolution > PEHStoredSolution)
                return newSolution;
            else
                return storedSolution;
        }
    }


    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion,double[] utopianPoint,double[] normIntercepts) {
        int domination = MOEACDUtils.dominateCompare(newSolution,storedSolution);
        if(domination == -1 ){
            return newSolution;
        }
        else if(domination == 1|| domination == 2){
            return storedSolution;
        }
        else {
            double[] normRef = new double[problem.getNumberOfObjectives()];
            for (int k=0;k<problem.getNumberOfObjectives();k++){
                normRef[k] = (referencePoint[k] - utopianPoint[k])/normIntercepts[k];
            }
            List<Integer> neighbors = coneSubRegion.getNeighbors();
            List<double[]> normPop = new ArrayList<>(neighbors.size()-1);
            for (int i = 1; i < neighbors.size(); i++) {
                double[] norm = MOEACDUtils.normalize(population.get(subRegionManager.getConeSubRegion(neighbors.get(i)).getIdxSolution()), utopianPoint, normIntercepts);
                for (int k = 0; k < problem.getNumberOfObjectives(); k++) {
                    norm[k] = normRef[k] - norm[k];
                }
                normPop.add(norm);
            }

            double[] normNewSolution = MOEACDUtils.normalize(newSolution,utopianPoint,normIntercepts);
            double[] normStoredSolution = MOEACDUtils.normalize(storedSolution,utopianPoint,normIntercepts);
            for (int k=0;k<problem.getNumberOfObjectives();k++){
                normNewSolution[k] = normRef[k] - normNewSolution[k];
                normStoredSolution[k] = normRef[k] - normStoredSolution[k];
            }

            double PEHNewSolution = PEH(normNewSolution, normPop);
            double PEHStoredSolution = PEH(normStoredSolution, normPop);

            if (PEHNewSolution > PEHStoredSolution)
                return newSolution;
            else
                return storedSolution;
        }
    }



    public double PEH(double[] normObjective, List<double[]> normPop){
        double ehaTmp1 = 1.0;
        for (int k=0;k<problem.getNumberOfObjectives();k++){
            ehaTmp1 *= normObjective[k];
        }
        double ehaTmp2Max = Double.NEGATIVE_INFINITY;
        for (int i=0;i<normPop.size();i++){
            double ehaTmp2 = 1.0;
            for (int k=0;k<problem.getNumberOfObjectives();k++){
                ehaTmp2 *= Math.min(normObjective[k],normPop.get(i)[k]);
            }
            ehaTmp2Max = Math.max(ehaTmp2Max,ehaTmp2);
        }
        double eha = ehaTmp1 - ehaTmp2Max;
        return eha;
    }

}