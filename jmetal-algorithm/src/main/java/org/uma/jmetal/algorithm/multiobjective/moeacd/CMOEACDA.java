package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.impl.ViolationThresholdComparator;

import javax.crypto.AEADBadTagException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2016/11/18.
 */
public class CMOEACDA extends CMOEACD{

    protected List<DoubleSolution> archives;
    public CMOEACDA(Problem<DoubleSolution> problem,
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

    public CMOEACDA(Measurable measureManager, Problem<DoubleSolution> problem,
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

    @Override public void run() {

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
            violationThresholdComparator.updateThreshold(population);

            initializeNadirPoint(population,nadirPoint);
            if(gen%updateInterval==0)
                updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
//            associateSubRegion(population,utopianPoint,normIntercepts);
//            associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);

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
            violationThresholdComparator.updateThreshold(population);

            initializeNadirPoint(population,nadirPoint);
            if(gen%updateInterval==0)
                updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
//            associateSubRegion(population,utopianPoint,normIntercepts);
//            associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);


            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }


    protected void initializeArchives(List<DoubleSolution> population){
        archives = new ArrayList<>(populationSize);
        for (int i=0;i<population.size();i++){
            if(isFeasible(population.get(i))){
                archives.add(population.get(i));
            }
        }
    }


    public List<DoubleSolution> getMeasurePopulation(){
        return archives;
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
        DoubleSolution storedNeighbor = archives.get(idxNeighbor);

        boolean isUpdated = false;
        DoubleSolution discardedOne = _solution;
        DoubleSolution betterOne = getBetterSolutionForNeighborUpdateUnConstraint(_solution,storedNeighbor,neighborSubRegion,utopianPoint,normIntercepts);
        if(betterOne == _solution){
            archives.set(idxNeighbor,_solution);
            discardedOne = storedNeighbor;
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

    protected POPTYPE randomSelectedPolutionType(){
        if(randomGenerator.nextDouble(0.0,1.0) < 0.5) {
            selectedPopType = POPTYPE.POP;
        }else {
            selectedPopType = POPTYPE.ARCHIVE;
        }
        return selectedPopType;
    }

    protected List<DoubleSolution> parentSelection(int idxSubRegion,int parentPoolSize) {
        List<DoubleSolution> parents = new ArrayList<>(parentPoolSize);

        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
        selectedPopType = randomSelectedPolutionType();
        if(selectedPopType == POPTYPE.POP){
            DoubleSolution solution = population.get(coneSubRegion.getIdxSolution());
            ConeSubRegion targetSubRegion = locateConeSubRegion(solution,utopianPoint,normIntercepts);
            if(/*isFeasible(solution) */violationThresholdComparator.underViolationEp(solution) && targetSubRegion == coneSubRegion)
                parents.add(solution);
        }else {
            int index = coneSubRegion.getIdxArchive();
            if(index >= 0) {
                DoubleSolution ind = archives.get(index);
                ConeSubRegion targetSubRegion = locateConeSubRegion(ind,utopianPoint,normIntercepts);
                if (targetSubRegion == coneSubRegion)
                    parents.add(ind);
            }
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

    protected DoubleSolution tourmentSelection(int idx1,int idx2) {
        selectedPopType = randomSelectedPolutionType();
        if(selectedPopType == POPTYPE.POP){
            return super.tourmentSelection(idx1,idx2);
        }else {
            ConeSubRegion subRegion1 = subRegionManager.getConeSubRegion(idx1);
            ConeSubRegion subRegion2 = subRegionManager.getConeSubRegion(idx2);

            int idxSolution1 = subRegion1.getIdxArchive();
            int idxSolution2 = subRegion2.getIdxArchive();

            if(idxSolution1 < 0 && idxSolution2 < 0)
                return null;
            else if(idxSolution1 < 0)
                return archives.get(idxSolution2);
            else if(idxSolution2 < 0)
                return archives.get(idxSolution1);

            return tourmentSelectionUnConstraint(subRegion1, archives.get(idxSolution1), subRegion2, archives.get(idxSolution2),utopianPoint,normIntercepts);
        }
    };

    @Override public String getName() {
        return "C-MOEA/CD-A" ;
    }

    @Override public String getDescription() {
        return "MOEA/CD with constraints handling and archives" ;
    }

}