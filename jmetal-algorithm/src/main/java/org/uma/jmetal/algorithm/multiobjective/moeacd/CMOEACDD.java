package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.KDTree;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.comparator.impl.ViolationThresholdComparator;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2016/9/21.
 */
public class CMOEACDD extends MOEACDD {
    protected ViolationThresholdComparator<DoubleSolution> violationThresholdComparator ;
    protected NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

    public CMOEACDD(Problem<DoubleSolution> problem,
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
        violationThresholdComparator = new ViolationThresholdComparator<>() ;
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<>();
    }

    public CMOEACDD(Measurable measureManager, Problem<DoubleSolution> problem,
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
        violationThresholdComparator = new ViolationThresholdComparator<>() ;
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<>();
    }

    @Override public void run() {

        initializeConeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;
        int maxGen = maxEvaluations/populationSize;


        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        violationThresholdComparator.updateThreshold(population);

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
            violationThresholdComparator.updateThreshold(population);

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
        int maxGen = maxEvaluations/populationSize;


        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        violationThresholdComparator.updateThreshold(population);

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
            updateAdaptiveCrossover();


            if (gen % updateInterval == 0 ) {
                adaptiveRefDirections();
            }
            violationThresholdComparator.updateThreshold(population);

            initializeNadirPoint(population,nadirPoint);
            if(gen%updateInterval==0){
                updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
                updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
                associateSubRegion(population,utopianPoint,normIntercepts);
            }else
                updateNormIntercepts(normIntercepts,utopianPoint,intercepts);

            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }

    protected DoubleSolution getBetterSolutionForNeighborUpdate(DoubleSolution newSolution,DoubleSolution storedSolution,ConeSubRegion neighborSubRegion,double[] utopianPoint,double[] normIntercepts){
        ConeSubRegion idealSubRegion = locateConeSubRegion(storedSolution,utopianPoint,normIntercepts);
        if((idealSubRegion != neighborSubRegion) || ((idealSubRegion == neighborSubRegion) && !violationThresholdComparator.underViolationEp(storedSolution))) {
            return getBetterSolutionByIndicator(newSolution,storedSolution,neighborSubRegion.getRefDirection(),utopianPoint,normIntercepts, beta_NeighborUpdate);
        }
        return storedSolution;
    }

    protected  DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution,DoubleSolution storedSolution,double[] referenceVector,double[] utopianPoint,double[] normIntercepts, double beta) {

        if (violationThresholdComparator.needToCompare(storedSolution,newSolution)) {
            int flag = violationThresholdComparator.compare(storedSolution, newSolution);
            if (flag == 1) {
                return newSolution;
            } else if (flag == -1) {
                return storedSolution;
            }
        }

        return super.getBetterSolutionByIndicator(newSolution,storedSolution,referenceVector,utopianPoint,normIntercepts,beta);
    }


//    protected void calcEvolvingSubproblemList(){
//
//        extraEvolvingSize = updateExtraEvolvingSize();
//        int evolvingSize =subRegionManager.getConeSubRegionsNum() + extraEvolvingSize;
//
//        evolvingIdxList = new ArrayList<>(evolvingSize);
//        for (int i=0;i<subRegionManager.getOriginalSubRegionsNum();i++) {
//            evolvingIdxList.add(i);
//        }
//        for (int i=subRegionManager.getOriginalSubRegionsNum();i<subRegionManager.getConeSubRegionsNum();i++) {
//            int indIdx = subRegionManager.getConeSubRegion(i).getIdxSolution();
//            if(indIdx >=0 ) {
//                DoubleSolution solution = population.get(indIdx);
//                if(violationThresholdComparator.underViolationEp(solution))
//                    evolvingIdxList.add(i);
//            }
//        }
//
//        if(extraEvolvingSize > 0) {
//            List<Integer> marginalIdx = subRegionManager.getMarginalSubRegionIdxList();
//            Collections.shuffle(marginalIdx);
//            List<Integer> avaiableExtraIdx = new ArrayList<>(marginalIdx.size());
//            for (int i=0;i<marginalIdx.size();i++){
//                int IdxSolution= subRegionManager.getConeSubRegion(marginalIdx.get(i)).getIdxSolution();
//                if(IdxSolution >=0 ) {
//                    DoubleSolution solution = population.get(IdxSolution);
//                    if (/*isFeasible(solution) */ violationThresholdComparator.underViolationEp(solution)) {
//                        avaiableExtraIdx.add(marginalIdx.get(i));
//                    }
//                }
//            }
//            if(avaiableExtraIdx.size()>0) {
//                int idx = 0;
//                while (evolvingIdxList.size() < evolvingSize) {
//                    if (idx == avaiableExtraIdx.size())
//                        idx = 0;
//                    evolvingIdxList.add(avaiableExtraIdx.get(idx));
//                    idx++;
//                }
//            }
//        }
//
//        Collections.shuffle(evolvingIdxList);
//    }


    protected List<DoubleSolution> parentSelection(int idxSubRegion,int parentPoolSize) {
        List<DoubleSolution> parents = new ArrayList<>(parentPoolSize);

        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
        int idxInd = coneSubRegion.getIdxSolution();
        if(idxInd >= 0) {
            DoubleSolution solution = population.get(idxInd);
            ConeSubRegion targetSubRegion = locateConeSubRegion(solution,utopianPoint,normIntercepts);
            if (violationThresholdComparator.underViolationEp(solution) && targetSubRegion == coneSubRegion)
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
            if(selectedSolution == null)
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

    protected DoubleSolution tourmentSelection(ConeSubRegion subRegion1,DoubleSolution solution1, ConeSubRegion subRegion2, DoubleSolution solution2,double[] utopianPoint,double[] normIntercepts) {

        if (violationThresholdComparator.needToCompare(solution1, solution2)) {
            int flag = violationThresholdComparator.compare(solution1,solution2);
            if (flag == 1) {
                return solution2;
            } else if (flag == -1) {
                return solution1;
            }
        }
        return super.tourmentSelection(subRegion1,solution1, subRegion2,solution2,utopianPoint,normIntercepts);
    };
//
    protected void adaptiveRefDirections() {

//        int numFeasible = 0;
//
//        for (int i=0;i<population.size();i++) {
//            if (isFeasible(population.get(i))) //feasible solutions
////            if(violationThresholdComparator.underViolationEp(population.get(i)))
//                numFeasible++;
//        }

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


        ArrayList<double[]> newRefDirections = new ArrayList<>(population.size() * 2);
        //adaptive reference vectors
        for (int i = 0; i < subRegionManager.getOriginalSubRegionsNum(); i++) {
            double[] direction = currentConeSubRegionList.get(i).getRefDirection();
            newRefDirections.add(direction);
        }

        for (int i = 0; i < currentAssociation.size(); i++) {
            double[] direction = currentConeSubRegionList.get(i).getRefDirection();

            if (!currentAssociation.get(i).isEmpty()) {
                if(i >= subRegionManager.getOriginalSubRegionsNum())
                    newRefDirections.add(direction);

//                int count = 0;
//                for (int j = 0; j < currentAssociation.get(i).size(); j++) {
//                    int idxSolution = currentAssociation.get(i).get(j);
//                    if (isFeasible(population.get(idxSolution)))
////                    if(violationThresholdComparator.underViolationEp(population.get(idxSolution)))
//                        count++;
//                }
//                if ((numFeasible < 0.9*populationSize && count > 0) || count > 1) {//
                if(currentAssociation.get(i).size()>1){//
//                if(count > 1){
//                if (currentAssociation.get(i).size() > 1) {

                    //add reference vectors
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
                            int idxSolution = tmpAssociation.get(i).get(j);
                            DoubleSolution solution = population.get(idxSolution);
                            betterOne = getBetterSolutionByIndicator(betterOne, solution, newRefDirections.get(i),utopianPoint,normIntercepts,beta_ConeUpdate);
                            if (betterOne == solution) {
                                discardSolutions.add(betterIdx);
                                betterIdx = idxSolution;
                            } else {
                                discardSolutions.add(idxSolution);
                            }
                        }
                    }
//                    if(!isFeasible(betterOne))
//                    if(!violationThresholdComparator.underViolationEp(betterOne))
//                        discardSolutions.add(betterIdx);
//                    else
                        saveSolutions.add(betterIdx);

//                    int betterIdx = -1;
//                    DoubleSolution betterOne = null;
//                    for (int j=0;j<tmpAssociation.get(i).size();j++){
//                        DoubleSolution solution = population.get(tmpAssociation.get(i).get(j));
////                        if(!isFeasible(solution))
//////                        if(!violationThresholdComparator.underViolationEp(solution))
////                            discardSolutions.add(tmpAssociation.get(i).get(j));
////                        else
//                        if(betterIdx == -1){
//                            betterIdx = tmpAssociation.get(i).get(j);
//                            betterOne = ind;
//                        }else {
//                            betterOne = getBetterSolutionByIndicator(betterOne, solution, newRefDirections.get(i));
//                            if (betterOne.equals(solution)) {
//                                discardSolutions.add(betterIdx);
//                                betterIdx = tmpAssociation.get(i).get(j);
//                            } else {
//                                discardSolutions.add(tmpAssociation.get(i).get(j));
//                            }
//                        }
//                    }
//                    if(betterIdx != -1)
//                        saveSolutions.add(betterIdx);
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


                randomSortByCVAndIdx(discardSolutions,population,newRefDirections,tmpKDTree);
//                randomSortIdx(discardSolutions,population,newRefDirections,tmpKDTree);
                int numSave = populationSize - availableSize;
                MOEACDUtils.invert(discardSolutions);
                int i = discardSolutions.size() -1;
                while (i >= 0 && numSave > 0){
                    DoubleSolution ind = population.get(discardSolutions.get(i));
                    int nearestDirectionIdx = nearestNonAssociatedDirection(nonAssociatedDirections,newRefDirections,ind);
                    tmpAssociation.get(nearestDirectionIdx).add(discardSolutions.get(i));
                    discardSolutions.remove(i);
                    numSave--;
                    i--;
                }


            } else if (availableSize > populationSize) {

                randomSortByCVAndIdx(saveSolutions,population,newRefDirections,tmpKDTree);
//                randomSortIdx(saveSolutions,population,newRefDirections,tmpKDTree);
                int numKidOff = availableSize - populationSize;
                int i=saveSolutions.size()-1;
                while (i>=0 && numKidOff > 0){
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


    protected void randomSortByCVAndIdx(List<Integer> idx,List<DoubleSolution> population,List<double[]> directionsList, KDTree kdTree){
//        Collections.shuffle(idx);

        List<Double> cv = new ArrayList<>(idx.size());
        for (int k = 0; k < idx.size(); k++) {
            DoubleSolution solution = population.get(idx.get(k));
            double solutionCV = Math.abs(overallConstraintViolationDegree.getAttribute(solution))*numberOfViolatedConstraints.getAttribute(solution);
            cv.add(solutionCV);
//            cv.add(Math.max(0.0,solutionCV - violationThresholdComparator.getThreshold()));
        }

        int ib = 0;
        int ik = cv.size()-1;
        while (ib < ik){
            while (ib < cv.size() && cv.get(ib)<=0){
                ib++;
            }
            while (ik >= 0 && cv.get(ik) > 0){
                ik--;
            }
            if(ib < ik) {
                double cvTmp = cv.get(ib);
                cv.set(ib, cv.get(ik));
                cv.set(ik, cvTmp);
                int idxTmp = idx.get(ib);
                idx.set(ib, idx.get(ik));
                idx.set(ik, idxTmp);
            }
        }

        int cvPartition = ib;


        if(cvPartition>0) {

//            ib = 0;
//            ik = cvPartition - 1;
//            while (ib < ik) {
//                while (ib < cvPartition) {
//                    DoubleSolution ind = population.get(idx.get(ib));
//                    int index = kdTree.queryIndex(MOEACDUtils.calObservation(MOEACDUtils.normalize(ind, idealPoint, normIntercepts)));
//                    if (index >= subRegionManager.getOriginalSubRegionsNum())
//                        break;
//                    ib++;
//                }
//                while (ik >= 0) {
//                    DoubleSolution solution = population.get(idx.get(ik));
//                    int index = kdTree.queryIndex(MOEACDUtils.calObservation(MOEACDUtils.normalize(solution, idealPoint, normIntercepts)));
//                    if (index < subRegionManager.getOriginalSubRegionsNum())
//                        break;
//                    ik--;
//                }
//                if (ib < ik) {
//                    int idxTmp = idx.get(ib);
//                    idx.set(ib, idx.get(ik));
//                    idx.set(ik, idxTmp);
//                }
//            }
//
            List<Integer> indexList = new ArrayList<>(cvPartition);
            for (int i = 0; i < cvPartition; i++) {
                DoubleSolution solution = population.get(idx.get(i));
                int index = kdTree.queryIndex(MOEACDUtils.calObservation(MOEACDUtils.normalize(solution, utopianPoint, normIntercepts)));
                indexList.add(index);
            }

            ib = 0;
            ik = cvPartition - 1;
            while (ib < ik) {
                while (ib < cvPartition) {
                   if (indexList.get(ib) >= subRegionManager.getOriginalSubRegionsNum())
                        break;
                    ib++;
                }
                while (ik >= 0) {
                    if (indexList.get(ik) < subRegionManager.getOriginalSubRegionsNum())
                        break;
                    ik--;
                }
                if (ib < ik) {
                    int idxTmp = idx.get(ib);
                    idx.set(ib, idx.get(ik));
                    idx.set(ik, idxTmp);
                    int indexTmp = indexList.get(ib);
                    indexList.set(ib,indexList.get(ik));
                    indexList.set(ik,indexTmp);
                }
            }

            int idxPartition = ib;

            List<Double> crowdingList = new ArrayList<>(cvPartition);
            for (int i = 0; i < cvPartition; i++) {
                double[] direction = directionsList.get(indexList.get(i));
                List<double[]> nearest2Coordinates = kdTree.queryKNearestCoordinates(direction, 2);
                double crowding = MOEACDUtils.distance2(direction, nearest2Coordinates.get(1));
                crowdingList.add(crowding);
            }

            MOEACDUtils.sortIdxByData(crowdingList, idx, 0, idxPartition - 1, false);
            MOEACDUtils.sortIdxByData(crowdingList, idx, idxPartition, cvPartition - 1, false);
//            MOEACDUtils.sortIdxByData(crowdingList, idx, 0, cvPartition - 1, false);
        }

//        ib = cvPartition;
//        ik = cv.size()-1;
//        while (ib < ik){
//            while (ib < cv.size()){
//                DoubleSolution solution = population.get(idx.get(ib));
//                int index = kdTree.queryIndex(MOEACDUtils.calObservation(MOEACDUtils.normalize(solution,utopianPoint,normIntercepts)));
//                if(index >= subRegionManager.getOriginalSubRegionsNum())
//                    break;
//                ib++;
//            }
//            while (ik >= 0){
//                DoubleSolution solution = population.get(idx.get(ik));
//                int index = kdTree.queryIndex(MOEACDUtils.calObservation(MOEACDUtils.normalize(solution,utopianPoint,normIntercepts)));
//                if(index < subRegionManager.getOriginalSubRegionsNum())
//                    break;
//                ik--;
//            }
//            if(ib < ik){
//                double cvTmp = cv.get(ib);
//                cv.set(ib, cv.get(ik));
//                cv.set(ik, cvTmp);
//                int idxTmp = idx.get(ib);
//                idx.set(ib, idx.get(ik));
//                idx.set(ik, idxTmp);
//            }
//        }
//
//        int infeasiblePartition = ib;
//
//        MOEACDUtils.sortIdxByData(cv,idx,cvPartition,infeasiblePartition-1);
//        MOEACDUtils.sortIdxByData(cv,idx,infeasiblePartition,cv.size()-1);
        MOEACDUtils.sortIdxByData(cv,idx,cvPartition,cv.size()-1);
    }


    @Override public String getName() {
        return "C-MOEA/CD-D" ;
    }

    @Override public String getDescription() {
        return "MOEA/CD with constraints handling and directions adaptation" ;
    }


}
