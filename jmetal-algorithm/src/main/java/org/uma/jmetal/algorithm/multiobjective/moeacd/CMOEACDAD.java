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
import org.uma.jmetal.util.comparator.impl.ViolationThresholdComparator;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2016/11/19.
 */
public class CMOEACDAD extends CMOEACDD {

    protected List<DoubleSolution> archives;

    public CMOEACDAD(Problem<DoubleSolution> problem,
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
        archives = new ArrayList<>(populationSize);
    }

    public CMOEACDAD(Measurable measureManager, Problem<DoubleSolution> problem,
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
        archives = new ArrayList<>(populationSize);

    }

    @Override
    public void run() {

        initializeConeSubRegions();
        initializePopulation();
        initializeArchives(population);
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        violationThresholdComparator.updateThreshold(population);

        associateSubRegion(population,utopianPoint,normIntercepts);
        associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);


        int maxGen = maxEvaluations / populationSize;
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
                if(isFeasible(child)) {
                    isUpdated |= coneUpdateArchives(child, subRegion,utopianPoint,normIntercepts);
                }

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
                associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
            }else {
                updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
            }


        } while (evaluations < maxEvaluations);
    }

    public void measureRun() {
        //Start
        measureManager.durationMeasure.start();
        initializeConeSubRegions();
        initializePopulation();
        initializeArchives(population);
        evaluations = populationSize;
        int gen = 1;


        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        violationThresholdComparator.updateThreshold(population);

        associateSubRegion(population,utopianPoint,normIntercepts);
        associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);


        int maxGen = maxEvaluations / populationSize;
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
                if(isFeasible(child)) {
                    isUpdated |= coneUpdateArchives(child, subRegion,utopianPoint,normIntercepts);
                }

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
                associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
            }else {
                updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
            }

            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }

    protected void initializeArchives(List<DoubleSolution> population){
        for (int i=0;i<population.size();i++){
            if(isFeasible(population.get(i))){
                archives.add((DoubleSolution)population.get(i).copy());
            }
        }
    }
    //update the association between cone subregion and solution
    protected void associateSubRegionWithArchives(List<DoubleSolution> archives,double[] utopianPoint,double[] normIntercepts) {

        //clearing the associate information
        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); ++i) {
            subRegionManager.getConeSubRegion(i).setIdxArchive(-1);
        }

        List<Integer> remainingArchiveIdx = new ArrayList<>(archives.size());
        for (int i = 0; i < archives.size(); ++i) {
            //find the cone subregion which the individual belongs to
            ConeSubRegion subRegion = locateConeSubRegion(archives.get(i),utopianPoint,normIntercepts);
            if (subRegion.getIdxArchive() < 0) {//No individual has been bound to this subregion
                //bind it
                subRegion.setIdxArchive(i);
            } else {
                int idxBoundArchive = subRegion.getIdxArchive();
                int idxWorst = i;
                //choose the better one for subregion by comparing their indicators using in the algorithm
                DoubleSolution betterS = getBetterSolutionByIndicatorUnConstraint(archives.get(i), archives.get(idxBoundArchive), subRegion,utopianPoint,normIntercepts);
                if (betterS == archives.get(i)) {
                    //replace the bound one
                    subRegion.setIdxArchive(i);
                    idxWorst = idxBoundArchive;
                }
                //record the worst one
                remainingArchiveIdx.add(idxWorst);
            }
        }

        List<Integer> unboundSubregion = new ArrayList<>(subRegionManager.getConeSubRegionsNum());
        for (int i=0;i<subRegionManager.getConeSubRegionsNum();i++){
            ConeSubRegion subRegion = subRegionManager.getConeSubRegion(i);
            if(subRegion.getIdxArchive() < 0){
                unboundSubregion.add(i);
            }
        }

        Collections.shuffle(remainingArchiveIdx);

        for (int i=0;i<remainingArchiveIdx.size()&&(!unboundSubregion.isEmpty());i++){
            int selectedIdx = nearestUnboundSubRegionIdx(archives.get(remainingArchiveIdx.get(i)), unboundSubregion,utopianPoint,normIntercepts);
            ConeSubRegion subRegion = subRegionManager.getConeSubRegion(unboundSubregion.get(selectedIdx));
            subRegion.setIdxArchive(remainingArchiveIdx.get(i));
            unboundSubregion.remove(selectedIdx);
        }
    }


    protected  boolean coneNeighborUpdateArchives(DoubleSolution _solution,ConeSubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {
        List<Integer> neighbors = targetSubRegion.getNeighbors();
        int idxNeighborSubRegion = neighbors.get(randomGenerator.nextInt(0,neighbors.size()-1));
        ConeSubRegion neighborSubRegion = subRegionManager.getConeSubRegion(idxNeighborSubRegion);

        int idxNeighbor = neighborSubRegion.getIdxArchive();
        if(idxNeighbor < 0){
            archives.add(_solution);
            neighborSubRegion.setIdxArchive(archives.size()-1);
            return true;
        }
        DoubleSolution storeNeighbor = archives.get(idxNeighbor);

        boolean isUpdated = false;
        DoubleSolution discardedOne = _solution;
        DoubleSolution betterOne = getBetterSolutionForNeighborUpdateUnConstraint(_solution,storeNeighbor,neighborSubRegion,utopianPoint,normIntercepts);
        if(betterOne == _solution){
            archives.set(idxNeighbor,_solution);
            discardedOne = storeNeighbor;
            isUpdated = true;
        }

        if(archives.size() < populationSize){
            archives.add(discardedOne);
            ConeSubRegion boundConeSubRegion = nearestUnBoundConeSubRegionWithArchives(discardedOne);
            boundConeSubRegion.setIdxArchive(archives.size() - 1);
            isUpdated = true;
        }

        return isUpdated;
    }

    protected  boolean coneUpdateArchives(DoubleSolution _solution,ConeSubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {
        int idxStoreInArchives = targetSubRegion.getIdxArchive();
        if(idxStoreInArchives < 0){
            archives.add( _solution);
            targetSubRegion.setIdxArchive(archives.size()-1);
            return true;
        }

        DoubleSolution storedArchive = archives.get(idxStoreInArchives);
        ConeSubRegion storedSubRegion = locateConeSubRegion(storedArchive,utopianPoint,normIntercepts);
        DoubleSolution betterS = null;
        boolean isUpdated = false;
        if(targetSubRegion==storedSubRegion){
            DoubleSolution worseS = _solution;
            betterS = getBetterSolutionByIndicatorUnConstraint(_solution,archives.get(idxStoreInArchives),targetSubRegion,utopianPoint,normIntercepts);
            if(betterS == _solution){
                //has updated
                isUpdated = true;
                archives.set(idxStoreInArchives,_solution);
                worseS = storedArchive;
            }
            isUpdated |= coneNeighborUpdateArchives(worseS,storedSubRegion,utopianPoint,normIntercepts);
        }
        else {
            isUpdated = true;
            archives.set(idxStoreInArchives, _solution);
            //cone update recursively
            coneUpdateArchives(storedArchive,storedSubRegion,utopianPoint,normIntercepts);
        }

        return isUpdated;
    }

    protected ConeSubRegion nearestUnBoundConeSubRegionWithArchives(DoubleSolution solution){
        double[] observation = MOEACDUtils.calObservation(MOEACDUtils.normalize(solution,utopianPoint,normIntercepts));
        double nearestDis = Double.POSITIVE_INFINITY;
        ConeSubRegion nearestSubRegion = null;
        for (int i=0;i<subRegionManager.getConeSubRegionsNum();i++) {
            ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(i);
            if (coneSubRegion.getIdxArchive() < 0) {
                double dis = MOEACDUtils.distance2(observation, coneSubRegion.getRefDirection());
                if(dis < nearestDis){
                    nearestDis = dis;
                    nearestSubRegion = coneSubRegion;
                }
            }
        }
        return nearestSubRegion;
    }


    protected void adaptiveRefDirections() {

        List<ConeSubRegion> currentConeSubRegionList = subRegionManager.getConeSubRegionsList();
        List<List<Integer>> currentAssociation = countAssociation(currentConeSubRegionList.size(), subRegionManager.getKdTree(), population);
        List<List<Integer>> currentArchiveAssociation = countAssociation(currentConeSubRegionList.size(),subRegionManager.getKdTree(),archives);

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

            if (!currentAssociation.get(i).isEmpty() || !currentArchiveAssociation.get(i).isEmpty()) {
                if(i >= subRegionManager.getOriginalSubRegionsNum())
                    newRefDirections.add(direction);

//                int count = 0;
//                for (int j = 0; j < currentAssociation.get(i).size(); j++) {
//                    int idxSolution = currentAssociation.get(i).get(j);
//                    if (isFeasible(population.get(idxSolution)))
////                    if(violationThresholdComparator.underViolationEp(population.get(idxSolution)))
//                        count++;
//                }
//
//                count += currentArchiveAssociation.get(i).size();
////
//                if ((archives.size() < 0.9*populationSize && count > 0) || count > 1) {
                 if(currentAssociation.get(i).size() > 1 || currentArchiveAssociation.get(i).size() > 1
                        || (currentAssociation.get(i).size() == 1 && currentArchiveAssociation.get(i).size() == 1 && !isSameInObjectiveSpace(population.get(currentAssociation.get(i).get(0)),archives.get(currentArchiveAssociation.get(i).get(0))))){//
//                if(count > 1){
//                if (currentAssociation.get(i).size() > 1 || currentArchiveAssociation.get(i).size() > 1) {

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
        List<List<Integer>> tmpArchiveAssociation = countAssociation(newRefDirections.size(),tmpKDTree,archives);

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
//
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

                    DoubleSolution solution = population.get(discardSolutions.get(i));
                    int nearestDirectionIdx = nearestNonAssociatedDirection(nonAssociatedDirections,newRefDirections,solution);
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

        //remove extra archives
        List<Integer> discardArchives = null;
        List<Integer> saveArchives = null;
        if (archives.size() > populationSize) {
            discardArchives = new ArrayList<>(archives.size() - populationSize);
            saveArchives = new ArrayList<>(archives.size() - populationSize);
            for (int i = 0; i < newRefDirections.size(); i++) {
                if (!tmpArchiveAssociation.get(i).isEmpty()) {
                    int betterIdx = tmpArchiveAssociation.get(i).get(0);
                    DoubleSolution betterOne = archives.get(betterIdx);

                    if (tmpArchiveAssociation.get(i).size() > 1) {

                        for (int j = 1; j < tmpArchiveAssociation.get(i).size(); j++) {
                            int archiveIdx = tmpArchiveAssociation.get(i).get(j);
                            DoubleSolution archive = archives.get(archiveIdx);
                            betterOne = getBetterSolutionByIndicator(betterOne, archive, newRefDirections.get(i),utopianPoint,normIntercepts,beta_ConeUpdate);
                            if (betterOne == archive) {
                                discardArchives.add(betterIdx);
                                betterIdx = archiveIdx;
                            } else {
                                discardArchives.add(archiveIdx);
                            }
                        }
                    }
                    saveArchives.add(betterIdx);
                }
            }

            int availableSize = archives.size() - discardArchives.size();
            if (availableSize < populationSize) {

                List<Integer> nonAssociatedDirections = new ArrayList<>();
                for (int t = 0;t <tmpArchiveAssociation.size();t++){
                    if(tmpArchiveAssociation.get(t).isEmpty())
                        nonAssociatedDirections.add(t);
                }

                randomSortIdx(discardArchives,archives,newRefDirections,tmpKDTree);
                int numSave = populationSize - availableSize;
                MOEACDUtils.invert(discardArchives);
                int i = discardArchives.size() -1;
                while (i >= 0 && numSave > 0){

                    DoubleSolution solution = archives.get(discardArchives.get(i));
                    int nearestDirectionIdx = nearestNonAssociatedDirection(nonAssociatedDirections,newRefDirections,solution);
                    tmpAssociation.get(nearestDirectionIdx).add(discardArchives.get(i));

                    discardArchives.remove(i);
                    numSave--;
                    i--;
                }
            } else
            if (availableSize > populationSize) {

                randomSortIdx(saveArchives,archives,newRefDirections,tmpKDTree);
                int numKidOff = availableSize - populationSize;
                int i=saveArchives.size()-1;
                while (i>=0 && numKidOff > 0){
                    discardArchives.add(saveArchives.get(i));
                    saveArchives.remove(i);
                    numKidOff--;
                    i--;
                }
                for (i = 0; i < discardArchives.size(); i++) {
                    double[] observation = MOEACDUtils.calObservation(MOEACDUtils.normalize(archives.get(discardArchives.get(i)), utopianPoint, normIntercepts));
                    int index = tmpKDTree.queryIndex(observation);
                    if(index >= subRegionManager.getOriginalSubRegionsNum()) {
                        int j = tmpArchiveAssociation.get(index).size() - 1;
                        while (j>=0){
                            if (tmpArchiveAssociation.get(index).get(j).equals(discardArchives.get(i))) {
                                tmpArchiveAssociation.get(index).remove(j);
                                break;
                            }
                            j--;
                        }
                    }
                }
            }


            Collections.sort(discardArchives);
            for (int i = discardArchives.size() - 1; i >= 0; i--) {
                archives.remove((int) discardArchives.get(i));
            }
        }

        //remove non-associated reference vectors
        for (int i = newRefDirections.size() - 1; i >= subRegionManager.getOriginalSubRegionsNum(); i--) {
            if (tmpAssociation.get(i).isEmpty() && tmpArchiveAssociation.get(i).isEmpty())
                newRefDirections.remove(i);
        }

        subRegionManager.reconstructConeSubRegionList(newRefDirections);
        subRegionManager.recalculateSubRegionsNeighbors(neighborhoodSize);
        findD0();
    }


    public List<DoubleSolution> getPopulation(){
        return archives;
    }

    @Override public String getName() {
        return "C-MOEA/CD-AD" ;
    }

    @Override public String getDescription() {
        return "MOEA/CD with constraints handling , archives , and directions adaptation" ;
    }

}
