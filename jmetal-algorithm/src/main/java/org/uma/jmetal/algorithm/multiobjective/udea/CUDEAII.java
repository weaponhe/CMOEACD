package org.uma.jmetal.algorithm.multiobjective.udea;

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
public class CUDEAII extends CUDEA{

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

    public CUDEAII(Problem<DoubleSolution> problem,
                 int[] H,
                 double[] Tau,
                 int populationSize,
                 int maxEvaluations,
                 int neighborhoodSize,
                 double neighborhoodSelectionProbability,
                 SBXCrossover sbxCrossoverOperator,
                 DifferentialEvolutionCrossover deCrossoverOperator,
                 MutationOperator<DoubleSolution> mutation){
        super(problem,H,Tau,populationSize,maxEvaluations,
                neighborhoodSize,neighborhoodSelectionProbability,sbxCrossoverOperator,deCrossoverOperator,mutation);
        idealPointU = new double[problem.getNumberOfObjectives()];
        utopianPointU = new double[problem.getNumberOfObjectives()];
        nadirPointU = new double[problem.getNumberOfObjectives()];
        referencePointU = new double[problem.getNumberOfObjectives()];
        interceptsU = new double[problem.getNumberOfObjectives()];
        normInterceptsU = new double[problem.getNumberOfObjectives()];
    }

    public CUDEAII(Measurable measureManager,
                 Problem<DoubleSolution> problem,
                 int[] arrayH,
                 double[] integratedTaus,
                 int populationSize,
                 int maxEvaluations,
                 int neighborhoodSize,
                 double neighborhoodSelectionProbability,
                 SBXCrossover sbxCrossoverOperator,
                 DifferentialEvolutionCrossover deCrossoverOperator,
                 MutationOperator<DoubleSolution> mutation) {
        this(problem, arrayH, integratedTaus, populationSize, maxEvaluations, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, deCrossoverOperator, mutation);
        this.measureManager = (MyAlgorithmMeasures<DoubleSolution>) measureManager;
        this.measureManager.initMeasures();
    }

    @Override public void run() {

        initializeSubRegions();

        generatePopulation();

        initializeArchives(initialPop);
        initializeOptima(initialPop);

        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(archives,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeExtremePoints(optima,utopianPointU,idealPointU,nadirPointU,referencePointU);

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
                if(isInFeasibleAttainableSpace(child)) {
                    if(updateExtremePoints(child,utopianPoint,idealPoint,nadirPoint,referencePoint)){
                        if(problem.getNumberOfObjectives() == 1)
                            updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
                        else
                            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
                    }
                    SubRegion subRegion = locateSubRegion(child, utopianPoint, normIntercepts);
                    isUpdated |= coneUpdate(child, subRegion, utopianPoint, normIntercepts);
                    if (isFeasible(child)) {
                        isUpdated |= coneUpdateArchives(child, subRegion, utopianPoint, normIntercepts);
                    }
                }

                if(updateExtremePoints(child,utopianPointU,idealPointU,nadirPointU,referencePointU)){
                    if(problem.getNumberOfObjectives() == 1)
                        updateNormIntercepts(normInterceptsU,utopianPointU,nadirPointU);
                    else
                        updateNormIntercepts(normInterceptsU,utopianPointU,interceptsU);
                }
                SubRegion subRegion = locateSubRegion(child, utopianPointU, normInterceptsU);
                isUpdated |= coneUpdateOptima(child,subRegion,utopianPointU,normInterceptsU);

                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();

            population = collectPopulation();
            initializeNadirPoint(population,nadirPoint);
            updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);

            optima = collectOptima();
            initializeNadirPoint(optima,nadirPointU);
            updateIntercepts(optima,interceptsU,utopianPointU,nadirPointU);
            updateNormIntercepts(normInterceptsU,utopianPointU,interceptsU);

            violationThresholdComparator.updateThreshold(population);

        } while (evaluations < maxEvaluations);
    }

    public void measureRun() {
        //Start
        measureManager.durationMeasure.start();
        initializeSubRegions();

        generatePopulation();

        initializeArchives(initialPop);
        initializeOptima(initialPop);

        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(archives,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeExtremePoints(optima,utopianPointU,idealPointU,nadirPointU,referencePointU);

        initializePopulation(initialPop);
        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        initializeIntecepts(optima,interceptsU,utopianPointU,nadirPointU);
        initializeNormIntecepts(normInterceptsU,utopianPointU,interceptsU);


        violationThresholdComparator.updateThreshold(collectOptima());

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

                boolean isUpdated = false;
                if(isInFeasibleAttainableSpace(child)) {
                    if(updateExtremePoints(child,utopianPoint,idealPoint,nadirPoint,referencePoint)){
                        if(problem.getNumberOfObjectives() == 1)
                            updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
                        else
                            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
                    }
                    SubRegion subRegion = locateSubRegion(child, utopianPoint, normIntercepts);
                    isUpdated |= coneUpdate(child, subRegion, utopianPoint, normIntercepts);
                    if (isFeasible(child)) {
                        isUpdated |= coneUpdateArchives(child, subRegion, utopianPoint, normIntercepts);
                    }
                }

                if(updateExtremePoints(child,utopianPointU,idealPointU,nadirPointU,referencePointU)){
                    if(problem.getNumberOfObjectives() == 1)
                        updateNormIntercepts(normInterceptsU,utopianPointU,nadirPointU);
                    else
                        updateNormIntercepts(normInterceptsU,utopianPointU,interceptsU);
                }
                SubRegion subRegion = locateSubRegion(child, utopianPointU, normInterceptsU);
                isUpdated |= coneUpdateOptima(child,subRegion,utopianPointU,normInterceptsU);

                collectForAdaptiveCrossover(isUpdated);
            }
            gen++;

            updateAdaptiveCrossover();

            population = collectPopulation();
            initializeNadirPoint(population,nadirPoint);
            updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);

            optima = collectOptima();
            initializeNadirPoint(optima,nadirPointU);
            updateIntercepts(optima,interceptsU,utopianPointU,nadirPointU);
            updateNormIntercepts(normInterceptsU,utopianPointU,interceptsU);

            violationThresholdComparator.updateThreshold(population);

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
        population = new ArrayList<>(subRegionManager.getSubRegionsNum());
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


    protected  boolean coneUpdate(DoubleSolution solution,SubRegion targetSubRegion,double[] utopianPoint, double[] normIntercepts) {

        DoubleSolution storeSolution = targetSubRegion.getSolution();
        if (storeSolution == null) {
            targetSubRegion.setSolution(solution);
            return true;
        }

        SubRegion storeSubRegion = locateSubRegion(storeSolution, utopianPoint, normIntercepts);
        DoubleSolution betterOne = null;
        boolean isUpdated = false;
        if (targetSubRegion == storeSubRegion) {
            DoubleSolution worserOne = solution;
            betterOne = getBetterSolutionByIndicator(solution, storeSolution, targetSubRegion, utopianPoint, normIntercepts);
            if (betterOne == solution) {
                //has updated
                isUpdated = true;
                targetSubRegion.setSolution(solution);
                worserOne = storeSolution;
            }
            isUpdated |= coneNeighborUpdate(worserOne, targetSubRegion, utopianPoint, normIntercepts);
        } else {
            isUpdated = true;
            //cone update recursively
            targetSubRegion.setSolution(solution);
            coneUpdate(storeSolution, storeSubRegion, utopianPoint, normIntercepts);
        }

        return isUpdated;

    }

    protected  boolean coneNeighborUpdate(DoubleSolution solution,SubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {
        List<Integer> neighbors = targetSubRegion.getNeighbors();
        int idxNeighborSubRegion = neighbors.get(randomGenerator.nextInt(0,neighbors.size()-1));
        SubRegion neighborSubRegion = subRegionManager.getSubRegion(idxNeighborSubRegion);

        DoubleSolution storeNeighbor = neighborSubRegion.getSolution();
        if(storeNeighbor == null){
            neighborSubRegion.setSolution(solution);
            return true;
        }

        boolean isUpdated = false;
        DoubleSolution discardedOne = solution;
        DoubleSolution betterOne = getBetterSolutionForNeighborUpdate(solution,storeNeighbor,neighborSubRegion,utopianPoint,normIntercepts);
        if(betterOne ==(solution)){
            neighborSubRegion.setSolution(solution);
            discardedOne = storeNeighbor;
            isUpdated = true;
        }
        SubRegion boundSubRegion = nearestUnBoundSubRegionForPop(discardedOne,utopianPoint,normIntercepts);
        if(boundSubRegion != null){
            boundSubRegion.setSolution(discardedOne);
            isUpdated = true;
        }

        return isUpdated;
    }

    protected SubRegion nearestUnBoundSubRegionForPop(DoubleSolution solution,double[] utopianPoint,double[] normIntercepts){
        double[] observation = Utils.calObservation(Utils.normalize(solution,utopianPoint,normIntercepts));
        double nearestDis = Double.POSITIVE_INFINITY;
        SubRegion nearestSubRegion = null;
        for (int i=0;i<subRegionManager.getSubRegionsNum();i++) {
            SubRegion subRegion = subRegionManager.getSubRegion(i);
            if (subRegion.getSolution() == null) {
                double dis = Utils.distance2(observation, subRegion.getDirection());
                if(dis < nearestDis){
                    nearestDis = dis;
                    nearestSubRegion = subRegion;
                }
            }
        }
        return nearestSubRegion;
    }

    protected List<DoubleSolution> parentSelection(int idxSubRegion,int parentPoolSize) {
        List<DoubleSolution> parents = new ArrayList<>(parentPoolSize);
        SubRegion subRegion = subRegionManager.getSubRegion(idxSubRegion);
        SubRegion optimumSubRegion = subRegion;
        DoubleSolution solution = subRegion.getSolution();
        if(solution != null) {
            optimumSubRegion = locateSubRegion(solution, utopianPointU, normInterceptsU);
        }

        selectedPopType = randomSelectedPolutionType();
        if(selectedPopType == POPTYPE.POP){
            if(solution != null) {
                SubRegion targetSubRegion = locateSubRegion(solution, utopianPoint, normIntercepts);
                if (violationThresholdComparator.underViolationEp(solution) && targetSubRegion == (subRegion))
                    parents.add(solution);
            }
        }else if(selectedPopType == POPTYPE.ARCHIVE) {
            if(problem.getNumberOfObjectives() == 1){
                if(!archives.isEmpty())
                    parents.add(archives.get(0));
            }else {
                DoubleSolution archive = subRegion.getArchive();
                if (archive != null) {
                    SubRegion targetSubRegion = locateSubRegion(archive,utopianPoint,normIntercepts);
                    if (targetSubRegion == (subRegion))
                        parents.add(archive);
                }
            }
        }else if(selectedPopType == POPTYPE.OPTIMUM) {
            if (problem.getNumberOfObjectives() == 1) {
                if (!optima.isEmpty() && violationThresholdComparator.underViolationEp(optima.get(0)))
                    parents.add(optima.get(0));
            } else {
                DoubleSolution optimum = optimumSubRegion.getOptimum();
                SubRegion targetSubRegion = locateSubRegion(optimum,utopianPointU,normInterceptsU);
                if (/*violationThresholdComparator.underViolationEp(optimum) && */targetSubRegion == optimumSubRegion)
                    parents.add(optimum);
            }
        }

        List<Integer> neighborPOP = new ArrayList<>();
        List<Integer> neighborARCHIVES = new ArrayList<>();
        List<Integer> neighborOPTIMA = new ArrayList<>();
        if(matingType == MatingType.NEIGHBOR) {
            List<Integer> neighborsOrignal = subRegion.getNeighbors();
            for (int i = 0; i < neighborsOrignal.size(); i++) {
                if (subRegionManager.getSubRegion(neighborsOrignal.get(i)).getSolution() != null)
                    neighborPOP.add(neighborsOrignal.get(i));
            }
            if (problem.getNumberOfObjectives() > 1) {
                for (int i = 0; i < neighborsOrignal.size(); i++) {
                    if (subRegionManager.getSubRegion(neighborsOrignal.get(i)).getArchive() != null)
                        neighborARCHIVES.add(neighborsOrignal.get(i));
                }
            }

            neighborOPTIMA = optimumSubRegion.getNeighbors();
        }

        while (parents.size() < parentPoolSize) {
            DoubleSolution selectedSolution = null;

            selectedPopType = randomSelectedPolutionType();
            if(problem.getNumberOfObjectives() == 1 && (selectedPopType == POPTYPE.ARCHIVE || selectedPopType == POPTYPE.OPTIMUM)){
                if(selectedPopType == POPTYPE.ARCHIVE){
                    if(archives.isEmpty())
                        selectedSolution = null;
                    else selectedSolution = archives.get(0);
                }else if(selectedPopType == POPTYPE.OPTIMUM){
                    if(optima.isEmpty())
                        selectedSolution = null;
                    else selectedSolution = optima.get(0);
                }
            }else {
                int idxSubRegion1;
                int idxSubRegion2;
                List<Integer> neighbors = null;
                if(matingType == MatingType.NEIGHBOR) {
                    if (selectedPopType == POPTYPE.POP)
                        neighbors = neighborPOP;
                    else if (selectedPopType == POPTYPE.ARCHIVE)
                        neighbors = neighborARCHIVES;
                    else if(selectedPopType == POPTYPE.OPTIMUM)
                        neighbors = neighborOPTIMA;
                }
                if (matingType == MatingType.NEIGHBOR && neighbors.size() >= parentPoolSize +1) {
                    int idx1 = randomGenerator.nextInt(0, neighbors.size() - 1);
                    idxSubRegion1 = neighbors.get(idx1);

                    int idx2 = randomGenerator.nextInt(0, neighbors.size() - 1);
                    idxSubRegion2 = neighbors.get(idx2);

                    while (idxSubRegion1 == idxSubRegion2) {
                        idx2 = randomGenerator.nextInt(0, neighbors.size() - 1);
                        idxSubRegion2 = neighbors.get(idx2);
                    }
                } else {
                    idxSubRegion1 = randomGenerator.nextInt(0, subRegionManager.getSubRegionsNum() - 1);
                    idxSubRegion2 = randomGenerator.nextInt(0, subRegionManager.getSubRegionsNum() - 1);

                    while (idxSubRegion1 == idxSubRegion2) {
                        idxSubRegion2 = randomGenerator.nextInt(0, subRegionManager.getSubRegionsNum() - 1);
                    }
                }
                selectedSolution = tourmentSelection(idxSubRegion1, idxSubRegion2);
            }

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

    protected DoubleSolution tourmentSelection(int idx1,int idx2){
        SubRegion subRegion1 = subRegionManager.getSubRegion(idx1);
        SubRegion subRegion2 = subRegionManager.getSubRegion(idx2);
        if(selectedPopType == POPTYPE.POP) {
            DoubleSolution solution1 = subRegion1.getSolution();
            DoubleSolution solution2 = subRegion2.getSolution();
            if(solution1==null && solution2 == null)
                return null;
            else if (solution1 == null)
                return solution2;
            else if (solution2 ==null)
                return solution1;
            return tourmentSelection(subRegion1, solution1, subRegion2, solution2,utopianPoint,normIntercepts);
        }else if (selectedPopType == POPTYPE.ARCHIVE){
            DoubleSolution solution1 = subRegion1.getArchive();
            DoubleSolution solution2 = subRegion2.getArchive();
            if(solution1==null && solution2 == null)
                return null;
            else if (solution1 == null)
                return solution2;
            else if (solution2 ==null)
                return solution1;
            return tourmentSelection(subRegion1, solution1, subRegion2, solution2,utopianPoint,normIntercepts);
        }else if (selectedPopType==POPTYPE.OPTIMUM){
            DoubleSolution solution1 = subRegion1.getOptimum();
            DoubleSolution solution2 = subRegion2.getOptimum();
            return tourmentSelectionUnConstraint(subRegion1, solution1, subRegion2, solution2,utopianPointU,normInterceptsU);
        }
        return null;
    };
}
