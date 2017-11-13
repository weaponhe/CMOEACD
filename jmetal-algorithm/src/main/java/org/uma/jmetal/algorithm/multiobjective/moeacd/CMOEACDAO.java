package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.udea.SubRegion;
import org.uma.jmetal.algorithm.multiobjective.udea.UDEA;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2017/1/4.
 */
public class CMOEACDAO extends CMOEACDA{
    protected List<DoubleSolution> optima;
    public CMOEACDAO(Problem<DoubleSolution> problem,
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

    public CMOEACDAO(Measurable measureManager, Problem<DoubleSolution> problem,
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
        initializeOptima(population);
        evaluations = populationSize;
        int gen = 1;
        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        violationThresholdComparator.updateThreshold(population);

        associateSubRegion(population,utopianPoint,normIntercepts);
        associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
        associateSubRegionWithOptima(optima,utopianPoint,normIntercepts);

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
                isUpdated |= coneUpdateOptimum(child,subRegion,utopianPoint,normIntercepts);
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
//            associateSubRegionWithOptima(optima,utopianPoint,normIntercepts);

        } while (evaluations < maxEvaluations);
    }

    public void measureRun() {
        //Start
        measureManager.durationMeasure.start();
        initializeConeSubRegions();
        initializePopulation();
        initializeArchives(population);
        initializeOptima(population);
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        violationThresholdComparator.updateThreshold(population);

        associateSubRegion(population,utopianPoint,normIntercepts);
        associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
        associateSubRegionWithOptima(optima,utopianPoint,normIntercepts);



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
                isUpdated |= coneUpdateOptimum(child,subRegion,utopianPoint,normIntercepts);
                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();
            violationThresholdComparator.updateThreshold(population);

            initializeNadirPoint(population,nadirPoint);
            if(gen%updateInterval == 0)
                updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
//            associateSubRegion(population,utopianPoint,normIntercepts);
//            associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
//            associateSubRegionWithOptima(optima,utopianPoint,normIntercepts);

            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }


    protected void initializeOptima(List<DoubleSolution> population){
        optima = new ArrayList<>(populationSize);
        for (int i=0;i<population.size();i++){
            optima.add(population.get(i));
        }
    }

    protected void associateSubRegionWithOptima(List<DoubleSolution> optima,double[] utopianPoint,double[] normIntercepts) {

        //clearing the associate information
        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); ++i) {
            subRegionManager.getConeSubRegion(i).setIdxOptimum(-1);
        }

        List<Integer> remainingOptimaIdx = new ArrayList<>(optima.size());
        for (int i = 0; i < optima.size(); ++i) {
            //find the cone subregion which the individual belongs to
            ConeSubRegion subRegion = locateConeSubRegion(optima.get(i),utopianPoint,normIntercepts);
            if (subRegion.getIdxOptimum() < 0) {//No individual has been bound to this subregion
                //bind it
                subRegion.setIdxOptimum(i);
            } else {
                int idxBoundOptimum = subRegion.getIdxOptimum();
                int idxWorst = i;
                //choose the better one for subregion by comparing their indicators using in the algorithm
                DoubleSolution betterS = getBetterSolutionByIndicatorUnConstraint(optima.get(i), optima.get(idxBoundOptimum), subRegion,utopianPoint,normIntercepts);
                if (betterS == optima.get(i)) {
                    //replace the bound one
                    subRegion.setIdxOptimum(i);
                    idxWorst = idxBoundOptimum;
                }
                //record the worst one
                remainingOptimaIdx.add(idxWorst);
            }
        }

        List<Integer> unboundSubregion = new ArrayList<>(subRegionManager.getConeSubRegionsNum());
        for (int i=0;i<subRegionManager.getConeSubRegionsNum();i++){
            ConeSubRegion subRegion = subRegionManager.getConeSubRegion(i);
            if(subRegion.getIdxOptimum() < 0){
                unboundSubregion.add(i);
            }
        }

        Collections.shuffle(remainingOptimaIdx);

        for (int i=0;i<remainingOptimaIdx .size()&&(!unboundSubregion.isEmpty());i++){
            int selectedIdx = nearestUnboundSubRegionIdx(optima.get(remainingOptimaIdx.get(i)), unboundSubregion,utopianPoint,normIntercepts);
            ConeSubRegion subRegion = subRegionManager.getConeSubRegion(unboundSubregion.get(selectedIdx));
            subRegion.setIdxOptimum(remainingOptimaIdx.get(i));
            unboundSubregion.remove(selectedIdx);
        }
    }

    protected  boolean coneUpdateOptimum(DoubleSolution _solution,ConeSubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {
        int idxStoreInPop = targetSubRegion.getIdxOptimum();
        DoubleSolution storedOptimum = optima.get(idxStoreInPop);
        ConeSubRegion storedSubRegion = locateConeSubRegion(storedOptimum,utopianPoint,normIntercepts);
        DoubleSolution betterS = null;
        boolean isUpdated = false;
        if(targetSubRegion == storedSubRegion){
            DoubleSolution worseS = _solution;
            betterS = getBetterSolutionByIndicatorUnConstraint(_solution,optima.get(idxStoreInPop),targetSubRegion,utopianPoint,normIntercepts);
            if(betterS == _solution){
                //has updated
                isUpdated = true;
                optima.set(idxStoreInPop,_solution);
                worseS = storedOptimum;
            }
            isUpdated |= coneNeighborUpdateOptimum(worseS,storedSubRegion,utopianPoint,normIntercepts);
        }
        else{
            isUpdated = true;
            optima.set(idxStoreInPop,_solution);
            //cone update recursively
            coneUpdateOptimum(storedOptimum,storedSubRegion,utopianPoint,normIntercepts);

        }
        return isUpdated;
    }

    protected  boolean coneNeighborUpdateOptimum(DoubleSolution _solution,ConeSubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {
        List<Integer> neighbors = targetSubRegion.getNeighbors();
        int idxNeighborSubRegion = neighbors.get(randomGenerator.nextInt(0,neighbors.size()-1));
        ConeSubRegion neighborSubRegion = subRegionManager.getConeSubRegion(idxNeighborSubRegion);

        int idxNeighbor = neighborSubRegion.getIdxOptimum();
        DoubleSolution storedNeighbor = optima.get(idxNeighbor);

        DoubleSolution betterOne = getBetterSolutionForNeighborUpdateUnConstraint(_solution,storedNeighbor,neighborSubRegion,utopianPoint,normIntercepts);
        if(betterOne == _solution){
            optima.set(idxNeighbor,_solution);
            return true;
        }
        return false;
    }


    protected POPTYPE randomSelectedPolutionType(){
        double pp = randomGenerator.nextDouble(0.0,1.0);
        if(pp < 0.5) {
            selectedPopType = POPTYPE.POP;
        }else if(pp < 0.75){
            selectedPopType = POPTYPE.ARCHIVE;
        }else {
            selectedPopType = POPTYPE.OPTIMUM;
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
            if(/*isFeasible(solution) */violationThresholdComparator.underViolationEp(solution) && targetSubRegion ==coneSubRegion)
                parents.add(solution);
        }else if(selectedPopType == POPTYPE.ARCHIVE) {
            int index = coneSubRegion.getIdxArchive();
            if(index >= 0) {
                DoubleSolution archive = archives.get(index);
                ConeSubRegion targetSubRegion = locateConeSubRegion(archive,utopianPoint,normIntercepts);
                if (targetSubRegion==coneSubRegion)
                    parents.add(archive);
            }
        }else if(selectedPopType == POPTYPE.OPTIMUM){
            DoubleSolution optimum = optima.get(coneSubRegion.getIdxOptimum());
            ConeSubRegion targetSubRegion = locateConeSubRegion(optimum,utopianPoint,normIntercepts);
            if(targetSubRegion==coneSubRegion)
                parents.add(optimum);
        }

        List<Integer> neighborsPOP = coneSubRegion.getNeighbors();
        List<Integer> neighborARCHIVES = new ArrayList<>();
        List<Integer> neighborOPTIMA = coneSubRegion.getNeighbors();
        if(matingType == MatingType.NEIGHBOR){
            for (int i=0;i<neighborsPOP.size();i++){
                if (subRegionManager.getConeSubRegion(neighborsPOP.get(i)).getIdxArchive() >= 0)
                    neighborARCHIVES.add(neighborsPOP.get(i));
            }
        }

        while (parents.size() < parentPoolSize) {
            DoubleSolution selectedSolution = null;
            selectedPopType = randomSelectedPolutionType();

            int idxSubRegion1;
            int idxSubRegion2;
            List<Integer> neighbors = null;
            if (matingType == MatingType.NEIGHBOR) {
                if (selectedPopType == POPTYPE.POP)
                    neighbors = neighborsPOP;
                else if (selectedPopType == POPTYPE.ARCHIVE)
                    neighbors = neighborARCHIVES;
                else if (selectedPopType == POPTYPE.OPTIMUM)
                    neighbors = neighborOPTIMA;
            }

            if (matingType == MatingType.NEIGHBOR && neighbors.size() >= parentPoolSize + 1) {
                int idx1 = randomGenerator.nextInt(0, neighbors.size() - 1);
                idxSubRegion1 = neighbors.get(idx1);

                int idx2 = randomGenerator.nextInt(0, neighbors.size() - 1);
                idxSubRegion2 = neighbors.get(idx2);

                while (idxSubRegion1 == idxSubRegion2) {
                    idx2 = randomGenerator.nextInt(0, neighbors.size() - 1);
                    idxSubRegion2 = neighbors.get(idx2);
                }
            } else {
                idxSubRegion1 = randomGenerator.nextInt(0, subRegionManager.getConeSubRegionsNum() - 1);
                idxSubRegion2 = randomGenerator.nextInt(0, subRegionManager.getConeSubRegionsNum() - 1);

                while (idxSubRegion1 == idxSubRegion2) {
                    idxSubRegion2 = randomGenerator.nextInt(0, subRegionManager.getConeSubRegionsNum() - 1);
                }
            }

            selectedSolution = tourmentSelection(idxSubRegion1, idxSubRegion2);

            if(selectedSolution == null)
                continue;

            boolean flag = true;
            for (DoubleSolution solution : parents) {
                if (solution==selectedSolution) {
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
//        selectedPopType = randomSelectedPolutionType();
        if(selectedPopType == POPTYPE.POP){
            ConeSubRegion subRegion1 = subRegionManager.getConeSubRegion(idx1);
            ConeSubRegion subRegion2 = subRegionManager.getConeSubRegion(idx2);
            int idxSolution1 = subRegion1.getIdxSolution();
            int idxSolution2 = subRegion2.getIdxSolution();

            DoubleSolution solution1 = population.get(idxSolution1);
            DoubleSolution solution2 = population.get(idxSolution2);
            return tourmentSelection(subRegion1,solution1,subRegion2,solution2,utopianPoint,normIntercepts);
        }else if(selectedPopType == POPTYPE.ARCHIVE){
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

            return tourmentSelectionUnConstraint(subRegion1, archives.get(idxSolution1), subRegion2, archives.get(idxSolution2),utopianPoint, normIntercepts);
        }else {
            ConeSubRegion subRegion1 = subRegionManager.getConeSubRegion(idx1);
            ConeSubRegion subRegion2 = subRegionManager.getConeSubRegion(idx2);
            int idxSolution1 = subRegion1.getIdxOptimum();
            int idxSolution2 = subRegion2.getIdxOptimum();

            DoubleSolution solution1 = optima.get(idxSolution1);
            DoubleSolution solution2 = optima.get(idxSolution2);
            return tourmentSelectionUnConstraint(subRegion1,solution1,subRegion2,solution2,utopianPoint,normIntercepts);
        }
    };

    @Override public String getName() {
        return "C-MOEA/CD-AO" ;
    }

    @Override public String getDescription() {
        return "MOEA/CD with constraints handling and archives" ;
    }
}