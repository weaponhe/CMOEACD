package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.udea.CUDEA;
import org.uma.jmetal.algorithm.multiobjective.udea.SubRegion;
import org.uma.jmetal.algorithm.multiobjective.udea.UDEA;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/5.
 */
public class CMOEACDAOD extends CMOEACDAO {
    //Ideal Point
    protected double[] idealPointU;
    //utopian point
    protected double[] utopianPointU;
    //nadir point
    protected double[] nadirPointU;
    //Reference Point
    protected double[] referencePointU;
    //intercepts of constructed hyper-plane
    protected double[] interceptsU;
    //for normalization
    protected double[] normInterceptsU;

    protected List<DoubleSolution> initialPop;

    public CMOEACDAOD(Problem<DoubleSolution> problem,
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
        idealPointU = new double[problem.getNumberOfObjectives()];
        utopianPointU = new double[problem.getNumberOfObjectives()];
        nadirPointU = new double[problem.getNumberOfObjectives()];
        referencePointU = new double[problem.getNumberOfObjectives()];
        interceptsU = new double[problem.getNumberOfObjectives()];
        normInterceptsU = new double[problem.getNumberOfObjectives()];
    }

    public CMOEACDAOD(Measurable measureManager, Problem<DoubleSolution> problem,
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
        this(problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
        this.measureManager = (MyAlgorithmMeasures<DoubleSolution>) measureManager;
        this.measureManager.initMeasures();
    }

    @Override public void run() {

        initializeConeSubRegions();

        generatePopulation();

        initializeArchives(initialPop);
        initializeOptima(initialPop);
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(archives,utopianPoint,idealPoint,nadirPoint,referencePoint);
        if(archives.isEmpty())
            initializeExtremePoints(optima, utopianPoint, idealPoint, nadirPoint, referencePoint);
        else
            initializeExtremePoints(archives, utopianPoint, idealPoint, nadirPoint, referencePoint);

        initializePopulation(initialPop);
        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        initializeIntecepts(optima,interceptsU,utopianPointU,nadirPointU);
        initializeNormIntecepts(normInterceptsU,utopianPointU,interceptsU);

        violationThresholdComparator.updateThreshold(population);

        associateSubRegion(population,utopianPoint,normIntercepts);
        associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
        associateSubRegionWithOptima(optima,utopianPointU,normInterceptsU);

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
                boolean isUpdated = false;
                if(isFeasible(child) || isInFeasibleAttainableSpace(child)) {
                    if (updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)) {
                        updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                    }

                    ConeSubRegion subRegion = locateConeSubRegion(child, utopianPoint, normIntercepts);

                    isUpdated |= coneUpdate(child, subRegion, utopianPoint, normIntercepts);
                    if (isFeasible(child)) {
                        isUpdated |= coneUpdateArchives(child, subRegion, utopianPoint, normIntercepts);
                    }
                }
                if(updateExtremePoints(child,utopianPointU,idealPointU,nadirPointU,referencePointU)){
                        updateNormIntercepts(normInterceptsU,utopianPointU,interceptsU);
                }
                ConeSubRegion subRegion = locateConeSubRegion(child, utopianPointU, normInterceptsU);

                isUpdated |= coneUpdateOptimum(child,subRegion,utopianPoint,normIntercepts);

                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();
            violationThresholdComparator.updateThreshold(population);

            initializeNadirPoint(population,nadirPoint);
            initializeNadirPoint(optima,nadirPointU);

            if(gen%updateInterval==0) {
                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
                updateIntercepts(optima,interceptsU,utopianPointU,nadirPointU);
            }
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
            updateNormIntercepts(normInterceptsU,utopianPointU,interceptsU);
//            associateSubRegion(population,utopianPoint,normIntercepts);
//            associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
//            associateSubRegionWithOptima(optima,utopianPoint,normIntercepts);



        } while (evaluations < maxEvaluations);
    }

    public void measureRun() {
        //Start
        measureManager.durationMeasure.start();
        initializeConeSubRegions();

        generatePopulation();

        initializeArchives(initialPop);
        initializeOptima(initialPop);
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(archives,utopianPoint,idealPoint,nadirPoint,referencePoint);
        if(archives.isEmpty())
            initializeExtremePoints(optima, utopianPoint, idealPoint, nadirPoint, referencePoint);
        else
            initializeExtremePoints(archives, utopianPoint, idealPoint, nadirPoint, referencePoint);

        initializePopulation(initialPop);
        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        initializeIntecepts(optima,interceptsU,utopianPointU,nadirPointU);
        initializeNormIntecepts(normInterceptsU,utopianPointU,interceptsU);

        violationThresholdComparator.updateThreshold(population);

        associateSubRegion(population,utopianPoint,normIntercepts);
        associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
        associateSubRegionWithOptima(optima,utopianPointU,normInterceptsU);

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

                boolean isUpdated = false;
                if(isFeasible(child) || isInFeasibleAttainableSpace(child)) {
                    if (updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)) {
                        updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                    }

                    ConeSubRegion subRegion = locateConeSubRegion(child, utopianPoint, normIntercepts);

                    isUpdated |= coneUpdate(child, subRegion, utopianPoint, normIntercepts);
                    if (isFeasible(child)) {
                        isUpdated |= coneUpdateArchives(child, subRegion, utopianPoint, normIntercepts);
                    }
                }
                if(updateExtremePoints(child,utopianPointU,idealPointU,nadirPointU,referencePointU)){
                    updateNormIntercepts(normInterceptsU,utopianPointU,interceptsU);
                }
                ConeSubRegion subRegion = locateConeSubRegion(child, utopianPointU, normInterceptsU);

                isUpdated |= coneUpdateOptimum(child,subRegion,utopianPoint,normIntercepts);
                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();
            violationThresholdComparator.updateThreshold(population);

            initializeNadirPoint(population,nadirPoint);
            initializeNadirPoint(optima,nadirPointU);

            if(gen%updateInterval==0) {
                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
                updateIntercepts(optima,interceptsU,utopianPointU,nadirPointU);
            }
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
            updateNormIntercepts(normInterceptsU,utopianPointU,interceptsU);
//            associateSubRegion(population,utopianPoint,normIntercepts);
//            associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
//            associateSubRegionWithOptima(optima,utopianPoint,normIntercepts);


            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }

    protected void generatePopulation() {
        initialPop = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            DoubleSolution newSolution = (DoubleSolution)problem.createSolution();
            problem.evaluate(newSolution);
            if(problem instanceof ConstrainedProblem){
                ((ConstrainedProblem<DoubleSolution>)problem).evaluateConstraints(newSolution);
            }
            initialPop.add(newSolution);
        }
    }

    protected void initializePopulation(List<DoubleSolution> initialPop) {
        population = new ArrayList<>(subRegionManager.getConeSubRegionsNum());
        for (int i = 0; i < initialPop.size(); i++) {
            if(isInFeasibleAttainableSpace(initialPop.get(i)));
            population.add(initialPop.get(i));
        }
    }

    protected boolean isInFeasibleAttainableSpace(DoubleSolution solution){
        for (int i=0;i<problem.getNumberOfObjectives();i++){
            if(solution.getObjective(i) < utopianPoint[i])
                return false;
        }
        return true;
    }

    protected  boolean coneUpdate(DoubleSolution _solution,ConeSubRegion targetSubRegion, double[] utopianPoint, double[] normIntercepts) {
        int idxStoreInPop = targetSubRegion.getIdxSolution();
        if(idxStoreInPop <0){
            population.add(_solution);
            targetSubRegion.setIdxSolution(population.size() -1);
            return true;
        }

        DoubleSolution storedSolution = population.get(idxStoreInPop);
        ConeSubRegion storedSubRegion = locateConeSubRegion(storedSolution,utopianPoint,normIntercepts);
        DoubleSolution betterS = null;
        boolean isUpdated = false;
        if(targetSubRegion == storedSubRegion){
            DoubleSolution worseS = _solution;
            betterS = getBetterSolutionByIndicator(_solution,population.get(idxStoreInPop),targetSubRegion,utopianPoint,normIntercepts);
            if(betterS == _solution){
                //has updated
                isUpdated = true;
                population.set(idxStoreInPop,_solution);
                worseS = storedSolution;
            }

            isUpdated |= coneNeighborUpdate(worseS,storedSubRegion,utopianPoint,normIntercepts);
        }
        else{
            isUpdated = true;
            population.set(idxStoreInPop,_solution);
            //cone update recursively
            coneUpdate(storedSolution,storedSubRegion,utopianPoint,normIntercepts);

        }

        return isUpdated;
    }

    protected  boolean coneNeighborUpdate(DoubleSolution _solution,ConeSubRegion targetSubRegion, double[] utopianPoint, double[] normIntercepts) {
        List<Integer> neighbors = targetSubRegion.getNeighbors();
        int idxNeighborSubRegion = neighbors.get(randomGenerator.nextInt(0,neighbors.size()-1));
        ConeSubRegion neighborSubRegion = subRegionManager.getConeSubRegion(idxNeighborSubRegion);

        int idxNeighbor = neighborSubRegion.getIdxSolution();
        if(idxNeighbor < 0){
            population.add(_solution);
            neighborSubRegion.setIdxSolution(population.size() - 1);
            return true;
        }
        DoubleSolution storedNeighbor = population.get(idxNeighbor);

        boolean isUpdated = false;
        DoubleSolution discardedOne = _solution;
        DoubleSolution betterOne = getBetterSolutionForNeighborUpdate(_solution,storedNeighbor,neighborSubRegion,utopianPoint,normIntercepts);
        if(betterOne == _solution){
            population.set(idxNeighbor,_solution);
            discardedOne = storedNeighbor;
            isUpdated = true;
        }


        if(population.size() < populationSize){
            population.add(discardedOne);
            ConeSubRegion boundConeSubRegion = nearestUnBoundConeSubRegionWithSolution(discardedOne);
            boundConeSubRegion.setIdxSolution(population.size() - 1);
            isUpdated = true;
        }

        return isUpdated;
    }

    protected ConeSubRegion nearestUnBoundConeSubRegionWithSolution(DoubleSolution solution){
        double[] observation = MOEACDUtils.calObservation(MOEACDUtils.normalize(solution,utopianPoint,normIntercepts));
        double nearestDis = Double.POSITIVE_INFINITY;
        ConeSubRegion nearestSubRegion = null;
        for (int i=0;i<subRegionManager.getConeSubRegionsNum();i++) {
            ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(i);
            if (coneSubRegion.getIdxSolution() < 0) {
                double dis = MOEACDUtils.distance2(observation, coneSubRegion.getRefDirection());
                if(dis < nearestDis){
                    nearestDis = dis;
                    nearestSubRegion = coneSubRegion;
                }
            }
        }
        return nearestSubRegion;
    }

    protected List<DoubleSolution> parentSelection(int idxSubRegion,int parentPoolSize) {
        List<DoubleSolution> parents = new ArrayList<>(parentPoolSize);
        ConeSubRegion subRegion = subRegionManager.getConeSubRegion(idxSubRegion);
        ConeSubRegion optimumSubRegion = subRegion;
        int solutionIdx = subRegion.getIdxSolution();
        if(solutionIdx >= 0) {
            optimumSubRegion = locateConeSubRegion(population.get(solutionIdx), utopianPointU, normInterceptsU);
        }

        selectedPopType = randomSelectedPolutionType();
        if(selectedPopType == POPTYPE.POP){
            if(solutionIdx >= 0) {
                DoubleSolution solution = population.get(solutionIdx);
                ConeSubRegion targetSubRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
                if (violationThresholdComparator.underViolationEp(solution) && targetSubRegion == (subRegion))
                    parents.add(solution);
            }
        }else if(selectedPopType == POPTYPE.ARCHIVE) {
            int archiveIdx = subRegion.getIdxArchive();
            if (archiveIdx >= 0) {
                DoubleSolution archive = archives.get(archiveIdx);
                ConeSubRegion targetSubRegion = locateConeSubRegion(archive, utopianPoint, normIntercepts);
                if (targetSubRegion == (subRegion))
                    parents.add(archive);
            }
        }else if(selectedPopType == POPTYPE.OPTIMUM) {

            DoubleSolution optimum = optima.get(optimumSubRegion.getIdxOptimum());
            ConeSubRegion targetSubRegion = locateConeSubRegion(optimum, utopianPointU, normInterceptsU);
            if (/*violationThresholdComparator.underViolationEp(optimum) && */targetSubRegion == optimumSubRegion)
                parents.add(optimum);
        }

        List<Integer> neighborPOP = new ArrayList<>();
        List<Integer> neighborARCHIVES = new ArrayList<>();
        List<Integer> neighborOPTIMA = new ArrayList<>();
        if(matingType == MatingType.NEIGHBOR) {
            List<Integer> neighborsOrignal = subRegion.getNeighbors();
            for (int i = 0; i < neighborsOrignal.size(); i++) {
                if (subRegionManager.getConeSubRegion(neighborsOrignal.get(i)).getIdxSolution() >= 0)
                    neighborPOP.add(neighborsOrignal.get(i));
            }
            for (int i = 0; i < neighborsOrignal.size(); i++) {
                if (subRegionManager.getConeSubRegion(neighborsOrignal.get(i)).getIdxArchive() >= 0)
                    neighborARCHIVES.add(neighborsOrignal.get(i));
            }

            neighborOPTIMA = optimumSubRegion.getNeighbors();
        }

        while (parents.size() < parentPoolSize) {
            DoubleSolution selectedSolution = null;

            selectedPopType = randomSelectedPolutionType();
            int idxSubRegion1;
            int idxSubRegion2;
            List<Integer> neighbors = null;
            if (matingType == MatingType.NEIGHBOR) {
                if (selectedPopType == POPTYPE.POP)
                    neighbors = neighborPOP;
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
            for (DoubleSolution s : parents) {
                if (s == selectedSolution) {
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
        ConeSubRegion subRegion1 = subRegionManager.getConeSubRegion(idx1);
        ConeSubRegion subRegion2 = subRegionManager.getConeSubRegion(idx2);

//        selectedPopType = randomSelectedPolutionType();
        if(selectedPopType == POPTYPE.POP) {
            int solutionIdx1 = subRegion1.getIdxSolution();
            int solutionIdx2 = subRegion2.getIdxSolution();
            if(solutionIdx1 < 0 && solutionIdx2 < 0)
                return null;
            else if (solutionIdx1 < 0)
                return population.get(solutionIdx2);
            else if (solutionIdx2 < 0)
                return population.get(solutionIdx1);
            return tourmentSelection(subRegion1, population.get(solutionIdx1), subRegion2, population.get(solutionIdx2),utopianPoint,normIntercepts);
        }else if (selectedPopType == POPTYPE.ARCHIVE){
            int archiveIdx1 = subRegion1.getIdxArchive();
            int archiveIdx2 = subRegion2.getIdxArchive();
            if(archiveIdx1 < 0 && archiveIdx2 < 0)
                return null;
            else if (archiveIdx1 < 0)
                return archives.get(archiveIdx2);
            else if (archiveIdx2 < 0)
                return archives.get(archiveIdx1);
            return tourmentSelection(subRegion1, archives.get(archiveIdx1), subRegion2, archives.get(archiveIdx2),utopianPoint,normIntercepts);
        }else if (selectedPopType == POPTYPE.OPTIMUM){
            DoubleSolution optimum1 = optima.get(subRegion1.getIdxOptimum());
            DoubleSolution optimum2 = optima.get(subRegion2.getIdxOptimum());
            return tourmentSelectionUnConstraint(subRegion1, optimum1, subRegion2, optimum2,utopianPointU,normInterceptsU);
        }
        return null;
    };

}
