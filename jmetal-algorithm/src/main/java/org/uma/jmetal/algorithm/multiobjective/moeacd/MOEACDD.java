package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.IntegerDoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.KDTree;

import java.util.*;

/**
 * Created by X250 on 2016/9/19.
 */
public class MOEACDD extends MOEACD{
    protected double D0Min0;
    protected double D0Mean0;
    public MOEACDD(Problem<DoubleSolution> problem,
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
        super(problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
    }

    public MOEACDD(Measurable measureManager, Problem<DoubleSolution> problem,
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
        super(measureManager, problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
    }


    @Override public void run() {

        initializeConeSubRegions();
        initializePopulation();

        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        associateSubRegion(population,utopianPoint,normIntercepts);



        do {
            calcEvolvingSubproblemList();

            for (int i = 0; i < populationSize; i++) {

                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
                DoubleSolution child = children.get(0);

                problem.evaluate(child);
                if(problem instanceof ConstrainedProblem){
                    ((ConstrainedProblem<DoubleSolution>)problem).evaluateConstraints(child);
                }

                evaluations += 1;

                if(updateExtremePoints(child,utopianPoint,idealPoint,nadirPoint,referencePoint)) {
                    updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                }

                ConeSubRegion subRegion = locateConeSubRegion(child,utopianPoint,normIntercepts);

                boolean isUpdated = coneUpdate(child, subRegion,utopianPoint,normIntercepts);

                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();


            if (gen % updateInterval == 0 ) {
                adaptiveRefDirections();
            }
            initializeNadirPoint(population,nadirPoint);
            if(gen%updateInterval==0){
                updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
                updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
                associateSubRegion(population,utopianPoint,normIntercepts);
            }else
                updateNormIntercepts(normIntercepts,utopianPoint,intercepts);



        } while (evaluations < maxEvaluations);
    }

    public void measureRun() {
        //Start
        measureManager.durationMeasure.start();
        initializeConeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        associateSubRegion(population,utopianPoint,normIntercepts);



        //calculate measure
        measureManager.updateMeasureProgress(getMeasurePopulation());
        do {
            calcEvolvingSubproblemList();

            for (int i = 0; i < populationSize; i++) {

                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
                DoubleSolution child = children.get(0);

                problem.evaluate(child);
                if(problem instanceof ConstrainedProblem){
                    ((ConstrainedProblem<DoubleSolution>)problem).evaluateConstraints(child);
                }

                evaluations += 1;

                if(updateExtremePoints(child,utopianPoint,idealPoint,nadirPoint,referencePoint)) {
                    updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                }

                ConeSubRegion subRegion = locateConeSubRegion(child,utopianPoint,normIntercepts);
                boolean isUpdated = coneUpdate(child, subRegion,utopianPoint,normIntercepts);

                collectForAdaptiveCrossover(isUpdated);
            }
            gen++;

            if (gen % updateInterval == 0 ) {
                adaptiveRefDirections();
            }
            initializeNadirPoint(population,nadirPoint);
            if(gen%updateInterval==0){
                updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
                updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
                associateSubRegion(population,utopianPoint,normIntercepts);
            }else
                updateNormIntercepts(normIntercepts,utopianPoint,intercepts);

//            associateSubRegion(population,utopianPoint,normIntercepts);

            updateAdaptiveCrossover();
            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }

    /**
     * Initialize subproblems
     */

    protected void initializeConeSubRegions() {
        super.initializeConeSubRegions();
        subRegionManager.setOriginalSubRegionsNum(subRegionManager.getConeSubRegionsNum());
        D0Min0 = D0Min;
        D0Mean0 = D0Mean;
    }

    protected int updateExtraEvolvingSize(){
        double maxES = Math.min(subRegionManager.getOriginalSubRegionsNum()/5.0,5.0*subRegionManager.getMarginalSubRegionIdxList().size());
        return  extraEvolvingSize = (int) Math.ceil(MOEACDUtils.sigmoid(1.0*evaluations/maxEvaluations,15.0,0.382)*maxES);

    }

    protected List<List<Integer>> countAssociation(int sizeDirections, KDTree kdTree,List<DoubleSolution> populations) {
        List<List<Integer>> result = new ArrayList<>(sizeDirections);
        for (int i=0;i<sizeDirections;i++){
            List<Integer> empty = new ArrayList<>();
            result.add(empty);
        }
        for (int i=0;i<populations.size();i++){
            double[] observation = MOEACDUtils.calObservation(MOEACDUtils.normalize(populations.get(i),utopianPoint,normIntercepts));
            int index = kdTree.queryIndex(observation);
            result.get(index).add(i);
        }
        return result;
    }


    protected void adaptiveRefDirections() {

        List<ConeSubRegion> currentConeSubRegionList = subRegionManager.getConeSubRegionsList();
        List<List<Integer>> currentAssociation = countAssociation(currentConeSubRegionList.size(), subRegionManager.getKdTree(), population);

        //
        boolean needUpdateDirections = false;
        for (int i=0;i<currentAssociation.size();i++){
            if(currentAssociation.get(i).size()>1) {
                needUpdateDirections = true;
                break;
            }
        }
        if(!needUpdateDirections)
            return;


        List<double[]> newRefDirections = new ArrayList<>(population.size() * 2);
        //adaptive reference vectors
        for (int i = 0; i < subRegionManager.getOriginalSubRegionsNum(); i++) {
            double[] direction = currentConeSubRegionList.get(i).getRefDirection();
            newRefDirections.add(direction);
        }

        for (int i = 0; i < currentAssociation.size(); i++) {
            double[] direction = currentConeSubRegionList.get(i).getRefDirection();
            if(!currentAssociation.get(i).isEmpty()) {
                if(i>= subRegionManager.getOriginalSubRegionsNum())
                    newRefDirections.add(direction);

                if (currentAssociation.get(i).size() > 1) {
                    //add reference vector
                    generateRefDirections(i,currentConeSubRegionList,currentAssociation,population,newRefDirections);
                }
            }
        }

        //Filtering the same reference vector
        for (int i = newRefDirections.size() - 1; i >= subRegionManager.getOriginalSubRegionsNum(); i--) {
            boolean hasSame = false;
            for (int j = i - 1; j >= 0; j--) {
                if (isSameVector(newRefDirections.get(i), newRefDirections.get(j))) {
                    hasSame = true;
                    break;
                }
            }
            if (hasSame) {
                newRefDirections.remove(i);
            }
        }

        KDTree tmpKDTree = KDTree.build(newRefDirections);

        List<List<Integer>> tmpAssociation = countAssociation(newRefDirections.size(), tmpKDTree, population);

        //remove extra individual
        List<Integer> discardSolutions = null;
        List<Integer> saveSolutions = null;
        if (population.size() > populationSize) {
            discardSolutions = new ArrayList<>(population.size() - populationSize);
            saveSolutions = new ArrayList<>(population.size() - populationSize);
            for (int i = 0; i < newRefDirections.size(); i++) {
                if (!tmpAssociation.get(i).isEmpty()) {
                    int betterIdx = tmpAssociation.get(i).get(0);
                    DoubleSolution betterOne = population.get(betterIdx);

                    if (tmpAssociation.get(i).size() > 1) {
                        for (int j = 1; j < tmpAssociation.get(i).size(); j++) {
                            int solutionIdx = tmpAssociation.get(i).get(j);
                            DoubleSolution solution = population.get(solutionIdx);
                            betterOne = getBetterSolutionByIndicator(betterOne, solution, newRefDirections.get(i),utopianPoint,normIntercepts,beta_ConeUpdate);
                            if (betterOne == solution) {
                                discardSolutions.add(betterIdx);
                                betterIdx = solutionIdx;
                            } else {
                                discardSolutions.add(solutionIdx);
                            }
                        }
                    }
                    saveSolutions.add(betterIdx);
                }
            }

            int availableSize = population.size() - discardSolutions.size();
            if (availableSize < populationSize) {

                for (int i = 0; i < discardSolutions.size(); i++) {
                    double[] observation = MOEACDUtils.calObservation(MOEACDUtils.normalize(population.get(discardSolutions.get(i)), utopianPoint, normIntercepts));
                    int index = tmpKDTree.queryIndex(observation);
                    int j = tmpAssociation.get(index).size() - 1;
                    while (j>=0){
                        if (tmpAssociation.get(index).get(j).equals(discardSolutions.get(i))) {
                            tmpAssociation.get(index).remove(j);
                            break;
                        }
                        j--;
                    }
                }

                List<Integer> nonAssociatedDirections = new ArrayList<>();
                for (int t = 0;t <tmpAssociation.size();t++){
                    if(tmpAssociation.get(t).isEmpty())
                        nonAssociatedDirections.add(t);
                }

                randomSortIdx(discardSolutions,population,newRefDirections,tmpKDTree);
                MOEACDUtils.invert(discardSolutions);
                int numSave = populationSize - availableSize;
                int i=discardSolutions.size() - 1;
                while (i>=0 && numSave > 0) {
                    DoubleSolution solution = population.get(discardSolutions.get(i));
                    int nearestDirectionIdx = nearestNonAssociatedDirection(nonAssociatedDirections,newRefDirections,solution);
                    tmpAssociation.get(nearestDirectionIdx).add(discardSolutions.get(i));
                    discardSolutions.remove(i);
                    numSave--;
                    i--;
                }
            }
            else if (availableSize > populationSize) {

                randomSortIdx(saveSolutions,population,newRefDirections,tmpKDTree);
                int i = saveSolutions.size() -1;
                int numKidOff = availableSize - populationSize;
                while (numKidOff > 0 && i >= 0) {
                    discardSolutions.add(saveSolutions.get(i));
                    saveSolutions.remove(i);
                    numKidOff--;
                    i--;
                }

                for (i = 0; i < discardSolutions.size(); i++) {
                    double[] observation = MOEACDUtils.calObservation(MOEACDUtils.normalize(population.get(discardSolutions.get(i)), utopianPoint, normIntercepts));
                    int index = tmpKDTree.queryIndex(observation);
                    if(index >= subRegionManager.getOriginalSubRegionsNum()) {
                        int j = tmpAssociation.get(index).size() - 1;
                        while (j>=0){
                            if (tmpAssociation.get(index).get(j).equals(discardSolutions.get(i))) {
                                tmpAssociation.get(index).remove(j);
                                break;
                            }
                            j--;
                        }
                    }
                }
            }

            Collections.sort(discardSolutions);
            for (int i = discardSolutions.size() - 1; i >= 0; i--) {
                population.remove((int) discardSolutions.get(i));
            }
        }

        //remove non-associated reference vectors

        for (int i = newRefDirections.size() - 1; i >= subRegionManager.getOriginalSubRegionsNum(); i--) {
            if (tmpAssociation.get(i).isEmpty())
                newRefDirections.remove(i);
        }

        subRegionManager.reconstructConeSubRegionList(newRefDirections);
        subRegionManager.recalculateSubRegionsNeighbors(neighborhoodSize);
        findD0();
    }

    protected int nearestNonAssociatedDirection(List<Integer> nonAssociatedDirection, List<double[]> directions, DoubleSolution solution){
        double[] observation = MOEACDUtils.calObservation(MOEACDUtils.normalize(solution,utopianPoint,normIntercepts));
        double dis = MOEACDUtils.distance2(observation,directions.get(nonAssociatedDirection.get(nonAssociatedDirection.size()-1)));
        for (int i = nonAssociatedDirection.size() - 2; i >= 0;i--){
            double tmp = MOEACDUtils.distance2(observation,directions.get(nonAssociatedDirection.get(i)));
            if(tmp < dis){
                int idx = nonAssociatedDirection.get(nonAssociatedDirection.size() -1);
                nonAssociatedDirection.set(nonAssociatedDirection.size()-1,nonAssociatedDirection.get(i));
                nonAssociatedDirection.set(i,idx);
            }
        }
        int result = nonAssociatedDirection.get(nonAssociatedDirection.size()-1);
        nonAssociatedDirection.remove(nonAssociatedDirection.size()-1);
        return result;
    }

    protected void generateRefDirections(int idx, List<ConeSubRegion> coneSubRegionList,List<List<Integer>> associateInfo, List<DoubleSolution> populations, List<double[]> newRefDirections) {

        ConeSubRegion coneSubRegion = coneSubRegionList.get(idx);
        double[] direction = coneSubRegion.getRefDirection();

//        double kt = DdMin0 / Math.sqrt(2.0);
//        double vm = 1.0 / problem.getNumberOfObjectives();
//        double k1 = kt * (1.0 - vm);
//        double k2 = kt * vm;
//        List<double[]> candidateRefs = new ArrayList<>(2*problem.getNumberOfObjectives());
//        candidateRefs.add(referenceVector);
//
//        for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
//                double[] newReference1 = new double[problem.getNumberOfObjectives()];
//                System.arraycopy(referenceVector, 0, newReference1, 0, problem.getNumberOfObjectives());
//                boolean isVaild1 = true;
//                double[] newReference2 = new double[problem.getNumberOfObjectives()];
//                System.arraycopy(referenceVector, 0, newReference2, 0, problem.getNumberOfObjectives());
//                boolean isVaild2 = true;
//                for (int k = 0; k < problem.getNumberOfObjectives(); k++) {
//                    if (isVaild1) {
//                        if (k == j) {
//                            newReference1[k] +=  k1;
//                        } else {
//                            newReference1[k] -=  k2;
//                        }
//                        if (newReference1[k] > 1.0 || newReference1[k] < 0.0) {
//                            isVaild1 = false;
//                            break;
//                        }
//                    }
//                    if (isVaild2) {
//                        if (k == j) {
//                            newReference2[k] -= k1;
//                        } else {
//                            newReference2[k] +=  k2;
//                        }
//                        if (newReference2[k] > 1.0 || newReference2[k] < 0.0) {
//                            isVaild2 = false;
//                            break;
//                        }
//                    }
//                }
//                if (isVaild1) {
//                    candidateRefs.add(newReference1);
//                }
//                if (isVaild2) {
//                    candidateRefs.add(newReference2);
//                }
//            }
//
//        double per = DdMin0 / (2.0*Math.sqrt(2.0) );
//        double[] newReference = new double[problem.getNumberOfObjectives()];
//        System.arraycopy(referenceVector, 0, newReference, 0, problem.getNumberOfObjectives());
//            for (int p = 0; p < problem.getNumberOfObjectives(); p++) {
//                newReference[p] += per ;
//                if (newReference[p] <= 1.0) {
//                    for (int q = p + 1; q < problem.getNumberOfObjectives(); q++) {
//                        newReference[q] -= per ;
//                        if (newReference[q] >= 0.0) {
//                            double[] validReference = new double[problem.getNumberOfObjectives()];
//                            System.arraycopy(newReference, 0, validReference, 0, problem.getNumberOfObjectives());
//                            candidateRefs.add(validReference);
//                        }
//                        newReference[q] += per ;
//                    }
//                }
//                newReference[p] -= per ;
//
//                newReference[p] -= per ;
//                if (newReference[p] >= 0.0) {
//                    for (int q = p + 1; q < problem.getNumberOfObjectives(); q++) {
//                        newReference[q] += per ;
//                        if (newReference[q] <= 1.0) {
//                            double[] validReference = new double[problem.getNumberOfObjectives()];
//                            System.arraycopy(newReference, 0, validReference, 0, problem.getNumberOfObjectives());
//                            candidateRefs.add(validReference);
//                        }
//                        newReference[q] -= per ;
//                    }
//                }
//                newReference[p] += per ;
//            }
//
//        for (int i=0;i<associateInfo.get(idx).size();i++){
//            DoubleSolution ind  = populations.get(associateInfo.get(idx).get(i));
//            double[] observation = MOEACDUtils.calObservation(MOEACDUtils.normalize(ind,utopianPoint,normIntercepts));
//            int nearestIdx = 0;
//            double nearestDis = MOEACDUtils.distance2(observation,candidateRefs.get(0));
//            for(int j=1;j < candidateRefs.size();j++){
//                double dis = MOEACDUtils.distance2(observation,candidateRefs.get(j));
//                if(dis < nearestDis){
//                    nearestDis = dis;
//                    nearestIdx = j;
//                }
//            }
////            if(nearestIdx != 0)
//            {
//                double[] selectedRef = new double[problem.getNumberOfObjectives()];
//                for (int j =0;j<problem.getNumberOfObjectives();j++){
//                    selectedRef[j] = 0.5 * (candidateRefs.get(nearestIdx)[j] + observation[j]);
//                }
//                newReferenceVectors.add(selectedRef);
//            }
//        }
//
//        List<Integer> neighbors = coneSubRegion.getNeighbors();
//        for (int i=1;i<neighbors.size();i++){
//            double[] neighborRef = coneSubRegionList.get(neighbors.get(i)).getRefObservation();
//            double dist = MOEACDUtils.distance(referenceVector,neighborRef);
//            if(dist > 2.0*DdMin || dist >= DdMin0){
//                double[] newReferenceVector = new double[problem.getNumberOfObjectives()];
//                for (int k = 0;k<problem.getNumberOfObjectives();k++){
//                    newReferenceVector[k] = 0.5*(referenceVector[k] + neighborRef[k]);
//                }
//                newReferenceVectors.add(newReferenceVector);
//            }
//        }
//
//
//        double kt = DdMin0 / Math.sqrt(2.0);
//        double vm = 1.0 / problem.getNumberOfObjectives();
//        double k1 = kt * (1.0 - vm);
//        double k2 = kt * vm;
//        int p = (int) Math.ceil(1.0 * associateInfo.get(idx).size() / (problem.getNumberOfObjectives()));
//        for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
//            for (int q = 1; q <= p; q++) {
//                double[] newReference1 = new double[problem.getNumberOfObjectives()];
//                System.arraycopy(referenceVector, 0, newReference1, 0, problem.getNumberOfObjectives());
//                boolean isVaild1 = true;
//                double[] newReference2 = new double[problem.getNumberOfObjectives()];
//                System.arraycopy(referenceVector, 0, newReference2, 0, problem.getNumberOfObjectives());
//                boolean isVaild2 = true;
//                double r = 1.0 * q / p;
//                for (int k = 0; k < problem.getNumberOfObjectives(); k++) {
//                    if (isVaild1) {
//                        if (k == j) {
//                            newReference1[k] += r * k1;
//                        } else {
//                            newReference1[k] -= r * k2;
//                        }
//                        if (newReference1[k] > 1.0 || newReference1[k] < 0.0) {
//                            isVaild1 = false;
//                            break;
//                        }
//                    }
//                    if (isVaild2) {
//                        if (k == j) {
//                            newReference2[k] -= r * k1;
//                        } else {
//                            newReference2[k] += r * k2;
//                        }
//                        if (newReference2[k] > 1.0 || newReference2[k] < 0.0) {
//                            isVaild2 = false;
//                            break;
//                        }
//                    }
//                }
//                if (isVaild1) {
//                    newReferenceVectors.add(newReference1);
//                }
//                if (isVaild2) {
//                    newReferenceVectors.add(newReference2);
//                }
//            }
//        }

        int partition =(int) Math.ceil(1.0 * associateInfo.get(idx).size() / (problem.getNumberOfObjectives()));

        double per = D0Mean0 / Math.sqrt(2.0) * partition/(1.0 + partition);
//        double per = D0Mean /(2.0 * Math.sqrt(2.0));
        double[] newDirection = new double[problem.getNumberOfObjectives()];
        System.arraycopy(direction, 0, newDirection, 0, problem.getNumberOfObjectives());
        for (int k = 1; k <= partition; k++) {
            double r = 1.0 * k / partition;
            for (int p = 0; p < problem.getNumberOfObjectives(); p++) {
                newDirection[p] += per * r;
                if (newDirection[p] <= 1.0) {
                    for (int q = p + 1; q < problem.getNumberOfObjectives(); q++) {
                        newDirection[q] -= per * r;
//                        if (newDirection[q] >= 0.0) {
                            double[] validDirection = new double[problem.getNumberOfObjectives()];
                            System.arraycopy(newDirection, 0, validDirection, 0, problem.getNumberOfObjectives());
                            if(validDirection[q] < 0.0)
                                validDirection[q] = 0.0;
                            newRefDirections.add(validDirection);
//                        }
                        newDirection[q] += per * r;
                    }
                }
                newDirection[p] -= per * r;

                newDirection[p] -= per * r;
                if (newDirection[p] >= 0.0) {
                    for (int q = p + 1; q < problem.getNumberOfObjectives(); q++) {
                        newDirection[q] += per * r;
//                        if (newDirection[q] <= 1.0) {
                            double[] validDirection = new double[problem.getNumberOfObjectives()];
                            System.arraycopy(newDirection, 0, validDirection, 0, problem.getNumberOfObjectives());
                            if(validDirection[q] > 1.0)
                                validDirection[q] = 1.0;
                        newRefDirections.add(validDirection);
//                        }
                        newDirection[q] -= per * r;
                    }
                }
                newDirection[p] += per * r;
            }
        }
    }

    protected void randomSortIdx(List<Integer> idx,List<DoubleSolution> population, List<double[]> directionsList , KDTree kdTree){
        Collections.shuffle(idx);
//
//        int ib = 0;
//        int ik = idx.size()-1;
//
//        while (ib < ik){
//            while (ib < idx.size()){
//                DoubleSolution ind = population.get(idx.get(ib));
//                int index = kdTree.queryIndex(MOEACDUtils.calObservation(MOEACDUtils.normalize(ind,idealPoint,normIntercepts)));
//                if(index >= subRegionManager.getOriginalSubRegionsNum())
//                    break;
//                ib++;
//            }
//            while (ik >=0){
//                DoubleSolution ind = population.get(idx.get(ik));
//                int index = kdTree.queryIndex(MOEACDUtils.calObservation(MOEACDUtils.normalize(ind,idealPoint,normIntercepts)));
//                if(index < subRegionManager.getOriginalSubRegionsNum())
//                    break;
//
//                ik--;
//            }
//            if(ib < ik){
//                int idxTmp = idx.get(ib);
//                idx.set(ib, idx.get(ik));
//                idx.set(ik, idxTmp);
//            }
//        }

        List<Integer> indexList = new ArrayList<>(idx.size());
        for (int i=0;i<idx.size();i++){
            DoubleSolution ind = population.get(idx.get(i));
            int index = kdTree.queryIndex(MOEACDUtils.calObservation(MOEACDUtils.normalize(ind,utopianPoint,normIntercepts)));
            indexList.add(index);
        }

        int ib = 0;
        int ik = idx.size()-1;

        while (ib < ik){
            while (ib < idx.size()){
                if(indexList.get(ib) >= subRegionManager.getOriginalSubRegionsNum())
                    break;
                ib++;
            }
            while (ik >=0){
                if(indexList.get(ik) < subRegionManager.getOriginalSubRegionsNum())
                    break;
                ik--;
            }
            if(ib < ik){
                int idxTmp = idx.get(ib);
                idx.set(ib, idx.get(ik));
                idx.set(ik, idxTmp);
                int indexTmp = indexList.get(ib);
                indexList.set(ib,indexList.get(ik));
                indexList.set(ik,indexTmp);
            }
        }

        int partition = ib;

        List<Double> crowdingList = new ArrayList<>(idx.size());
        for (int i=0;i<idx.size();i++){
            double[] direction = directionsList.get(indexList.get(i));
            List<double[]> nearestCoordinates = kdTree.queryKNearestCoordinates(direction,2);
            double crowding = MOEACDUtils.distance2(direction,nearestCoordinates.get(1));
            crowdingList.add(crowding);
        }

        MOEACDUtils.sortIdxByData(crowdingList,idx,0,partition-1,false);
        MOEACDUtils.sortIdxByData(crowdingList,idx,partition,idx.size()-1,false);
//        MOEACDUtils.sortIdxByData(crowdingList,idx,0,idx.size()-1,false);
    }

    protected void calcEvolvingSubproblemList(){

        extraEvolvingSize = updateExtraEvolvingSize();
        int evolvingSize =subRegionManager.getConeSubRegionsNum() + extraEvolvingSize;

        evolvingIdxList = new ArrayList<>(evolvingSize);
        for (int i=0;i<subRegionManager.getOriginalSubRegionsNum();i++) {
            evolvingIdxList.add(i);
        }
        for (int i=subRegionManager.getOriginalSubRegionsNum();i<subRegionManager.getConeSubRegionsNum();i++) {
            if(subRegionManager.getConeSubRegion(i).getIdxSolution() >= 0) {
                evolvingIdxList.add(i);
            }
        }

        if(extraEvolvingSize > 0) {
            List<Integer> marginalIdx = subRegionManager.getMarginalSubRegionIdxList();
            List<Integer> avaiableExtraIdx = new ArrayList<>(marginalIdx.size());
            for (int i=0;i<marginalIdx.size();i++){
                if(subRegionManager.getConeSubRegion(marginalIdx.get(i)).getIdxSolution() >= 0) {
                    avaiableExtraIdx.add(marginalIdx.get(i));
                }
            }

            Collections.shuffle(avaiableExtraIdx);
            if(avaiableExtraIdx.size()>0) {
                int idx = 0;
                while (evolvingIdxList.size() < evolvingSize) {
                    if (idx == avaiableExtraIdx.size())
                        idx = 0;
                    evolvingIdxList.add(avaiableExtraIdx.get(idx));
                    idx++;
                }
            }
        }

        Collections.shuffle(evolvingIdxList);
    }

    protected boolean isSameVector(double[] v1,double[] v2){
        for (int i=0;i<v1.length;i++)
            if(Math.abs(v1[i] - v2[i]) > Constant.TOLERATION)
                return false;
        return true;
    }

    protected boolean isSameInObjectiveSpace(DoubleSolution solution1,DoubleSolution solution2){
        boolean isSame = true;
        for (int i=0;i<problem.getNumberOfObjectives();i++){
            if(Math.abs(solution1.getObjective(i) - solution2.getObjective(i)) > Constant.TOLERATION){
                isSame = false;
                break;
            }
        }
        return isSame;
    }

    protected List<DoubleSolution> parentSelection(int idxSubRegion,int parentPoolSize) {
        List<DoubleSolution> parents = new ArrayList<>(parentPoolSize);

        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
        int idxSolution = coneSubRegion.getIdxSolution();
        if(idxSolution >= 0) {
            DoubleSolution solution = population.get(idxSolution);
            ConeSubRegion targetSubRegion = locateConeSubRegion(solution,utopianPoint,normIntercepts);
            if (targetSubRegion == coneSubRegion)
                parents.add(solution);
        }

        List<Integer> neighbors = coneSubRegion.getNeighbors();

        while (parents.size() < parentPoolSize) {

            int idxSubRegion1;
            int idxSubRegion2;
            if(matingType == MatingType.NEIGHBOR) {
                int idx1 = randomGenerator.nextInt(0, neighbors.size()  - 1);
                idxSubRegion1 = neighbors.get(idx1);

                int idx2 = randomGenerator.nextInt(0, neighbors.size()  - 1);
                idxSubRegion2 = neighbors.get(idx2);

                while (idxSubRegion1 == idxSubRegion2) {
                    idx2 = randomGenerator.nextInt(0, neighbors.size() - 1);
                    idxSubRegion2 = neighbors.get(idx2);
                }
            }else{
                idxSubRegion1 = randomGenerator.nextInt(0,subRegionManager.getConeSubRegionsNum()-1);
                idxSubRegion2 = randomGenerator.nextInt(0,subRegionManager.getConeSubRegionsNum()-1);

                while (idxSubRegion1 == idxSubRegion2) {
                    idxSubRegion2 = randomGenerator.nextInt(0,subRegionManager.getConeSubRegionsNum()-1);
                }
            }

            DoubleSolution selectedSolution = tourmentSelection(idxSubRegion1,idxSubRegion2);

            if(selectedSolution==null)
                continue;

            boolean flag = true;
            for (DoubleSolution solution : parents) {
                if (solution == selectedSolution) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                parents.add(selectedSolution);
            }
        }
        return parents ;
    }

    protected DoubleSolution tourmentSelection(int idx1,int idx2){
        ConeSubRegion subRegion1 = subRegionManager.getConeSubRegion(idx1);
        ConeSubRegion subRegion2 = subRegionManager.getConeSubRegion(idx2);
        int idxSolution1 = subRegion1.getIdxSolution();
        int idxSolution2 = subRegion2.getIdxSolution();

        if(idxSolution1 < 0 && idxSolution2 < 0)
            return null;
        else if(idxSolution1 < 0)
            return population.get(idxSolution2);
        else if(idxSolution2 < 0)
            return population.get(idxSolution1);
        else
            return tourmentSelection(subRegion1,population.get(idxSolution1),subRegion2,population.get(idxSolution2),utopianPoint,normIntercepts);
    };


    protected  boolean coneUpdate(DoubleSolution _solution,ConeSubRegion targetSubRegion, double[] utopianPoint, double[] normIntercepts) {
        int idxStoreInPop = targetSubRegion.getIdxSolution();
        if(idxStoreInPop < 0){
            population.add(_solution);
            targetSubRegion.setIdxSolution(population.size()-1);
            return true;
        }

        DoubleSolution storedSolution = population.get(idxStoreInPop);
        ConeSubRegion storeSubRegion = locateConeSubRegion(storedSolution,utopianPoint,normIntercepts);
        DoubleSolution betterS = null;
        boolean isUpdated = false;
        if(targetSubRegion == storeSubRegion){
            DoubleSolution worserS = _solution;
            betterS = getBetterSolutionByIndicator(_solution,population.get(idxStoreInPop),targetSubRegion,utopianPoint,normIntercepts);
            if(betterS == _solution){
                //has updated
                isUpdated = true;
                population.set(idxStoreInPop,_solution);
                worserS = storedSolution;
            }

            isUpdated |= coneNeighborUpdate(worserS,storeSubRegion,utopianPoint,normIntercepts);

        }
        else{
            isUpdated = true;
            population.set(idxStoreInPop,_solution);
            //cone update recursively
            coneUpdate(storedSolution,storeSubRegion,utopianPoint,normIntercepts);
        }

        return isUpdated;
    }

    protected  boolean coneNeighborUpdate(DoubleSolution solution,ConeSubRegion targetSubRegion, double[] utopianPoint, double[] normIntercepts) {
        List<Integer> neighbors = targetSubRegion.getNeighbors();
        int idxNeighborSubRegion = neighbors.get(randomGenerator.nextInt(0,neighbors.size()-1));
        ConeSubRegion neighborSubRegion = subRegionManager.getConeSubRegion(idxNeighborSubRegion);

        int idxNeighbor = neighborSubRegion.getIdxSolution();
        if(idxNeighbor < 0){
            population.add(solution);
            neighborSubRegion.setIdxSolution(population.size()-1);
            return true;
        }

        DoubleSolution storeNeighbor = population.get(idxNeighbor);

       DoubleSolution betterOne = getBetterSolutionForNeighborUpdate(solution,storeNeighbor,neighborSubRegion,utopianPoint,normIntercepts);
        if(betterOne == solution){
            population.set(idxNeighbor,solution);
            return true;
        }

        return false;
    }

    @Override public String getName() {
        return "MOEA/CD-D" ;
    }

    @Override public String getDescription() {
        return "MOEA/CD  with directions adaptation " ;
    }
}
