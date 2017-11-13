package org.uma.jmetal.algorithm.multiobjective.udea;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.impl.MyAlgorithmMeasures;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.comparator.impl.NormalizedOverallViolationThreholdComparator;
import org.uma.jmetal.util.comparator.impl.ViolationThresholdComparator;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2017/1/4.
 */
public class CUDEA extends UDEA{
    enum POPTYPE{POP,ARCHIVE,OPTIMUM};
    protected List<DoubleSolution> archives;
    protected POPTYPE selectedPopType = POPTYPE.POP;
    protected List<DoubleSolution> optima;
    protected ViolationThresholdComparator<DoubleSolution> violationThresholdComparator ;
    protected NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

    public CUDEA(Problem<DoubleSolution> problem,
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
        archives = new ArrayList<>(populationSize);
        optima = new ArrayList<>(populationSize);
        violationThresholdComparator = new NormalizedOverallViolationThreholdComparator<>() ;
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<>();
    }

    public CUDEA(Measurable measureManager,
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

                if(updateExtremePoints(child,utopianPoint,idealPoint,nadirPoint,referencePoint)){
                    if(problem.getNumberOfObjectives() == 1)
                        updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
                    else
                        updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
                }

                SubRegion subRegion = locateSubRegion(child,utopianPoint,normIntercepts);

                boolean isUpdated = coneUpdate(child, subRegion,utopianPoint,normIntercepts);
                if(isFeasible(child)) {
                    isUpdated |=coneUpdateArchives(child, subRegion,utopianPoint,normIntercepts);
                }
                isUpdated |= coneUpdateOptima(child,subRegion,utopianPoint,normIntercepts);

                collectForAdaptiveCrossover(isUpdated);
            }
            gen++;
            updateAdaptiveCrossover();
            violationThresholdComparator.updateThreshold(population);

            population = collectPopulation();
            initializeNadirPoint(population,nadirPoint);
            updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);

        } while (evaluations < maxEvaluations);
    }

    public void measureRun() {
        //Start
        measureManager.durationMeasure.start();
        initializeSubRegions();
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

                if(updateExtremePoints(child,utopianPoint,idealPoint,nadirPoint,referencePoint)){
                    if(problem.getNumberOfObjectives() == 1)
                        updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
                    else
                        updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
                }

                SubRegion subRegion = locateSubRegion(child,utopianPoint,normIntercepts);
                boolean isUpdated = coneUpdate(child, subRegion,utopianPoint,normIntercepts);
                if(isFeasible(child)) {
                    isUpdated |=coneUpdateArchives(child, subRegion,utopianPoint,normIntercepts);
                }
                isUpdated |= coneUpdateOptima(child,subRegion,utopianPoint,normIntercepts);
                collectForAdaptiveCrossover(isUpdated);
            }
            gen++;
            updateAdaptiveCrossover();
            violationThresholdComparator.updateThreshold(population);

            population = collectPopulation();
            initializeNadirPoint(population,nadirPoint);
            updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);


            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }

    protected void initializeArchives(List<DoubleSolution> population){
        if(problem.getNumberOfObjectives() == 1){
            archives = new ArrayList<>(1);
            for (int i = 0; i < population.size(); i++) {
                if (isFeasible(population.get(i))) {
                    if(archives.isEmpty()){
                        archives.add(population.get(i));
                    }else if(archives.get(0).getObjective(0) > population.get(i).getObjective(0)){
                        archives.set(0,population.get(i));
                    }
                }
            }
        }else {
            archives = new ArrayList<>(subRegionManager.getSubRegionsNum());
            for (int i = 0; i < population.size(); i++) {
                if (isFeasible(population.get(i))) {
                    archives.add(population.get(i));
                }
            }
        }
    }

    protected void initializeOptima(List<DoubleSolution> population){
        if(problem.getNumberOfObjectives() == 1){
            optima = new ArrayList<>(1);
            for (int i = 0; i < population.size(); i++) {
                if (optima.isEmpty()) {
                    optima.add(population.get(i));
                } else if (optima.get(0).getObjective(0) > population.get(i).getObjective(0)) {
                    optima.set(0, population.get(i));
                }
            }
        }else {
            for (int i = 0; i < population.size(); i++) {
                optima.add(population.get(i));
            }
        }
    }

    //update the association between cone subregion and solution
    protected void associateSubRegionWithArchives(List<DoubleSolution> archives,double[] utopianPoint , double[] normIntercepts) {
        if(problem.getNumberOfObjectives() > 1) {
            //clearing the associate information
            for (int i = 0; i < subRegionManager.getSubRegionsNum(); ++i) {
                subRegionManager.getSubRegion(i).setArchive(null);
            }

            List<DoubleSolution> remainingArchives = new ArrayList<>(archives.size());
            for (int i = 0; i < archives.size(); ++i) {
                //find the subregion which the solution belongs to
                SubRegion subRegion = locateSubRegion(archives.get(i),utopianPoint,normIntercepts);
                if (subRegion.getArchive() == null) {//No solution has been bound to this subregion
                    //bind it
                    subRegion.setArchive(archives.get(i));
                } else {
                    DoubleSolution boundArchive = subRegion.getArchive();
                    DoubleSolution worserOne = archives.get(i);
                    //choose the better one for subregion by comparing their indicators using in the algorithm
                    DoubleSolution betterOne = getBetterSolutionByIndicatorUnConstraint(archives.get(i), boundArchive, subRegion,utopianPoint,normIntercepts);
                    if (betterOne == archives.get(i)) {
                        //replace the bound one
                        subRegion.setArchive(archives.get(i));
                        worserOne = boundArchive;
                    }
                    //record the worst one
                    remainingArchives.add(worserOne);
                }
            }

            List<SubRegion> unBoundArchivesSubRegion = new ArrayList<>(subRegionManager.getSubRegionsNum());
            for (int i = 0; i < subRegionManager.getSubRegionsNum(); i++) {
                SubRegion subRegion = subRegionManager.getSubRegion(i);
                if (subRegion.getArchive() == null) {
                    unBoundArchivesSubRegion.add(subRegion);
                }
            }

            Collections.shuffle(remainingArchives);

            for (int i = 0; i < remainingArchives.size() && (!unBoundArchivesSubRegion.isEmpty()); i++) {
                int selectedIdx = nearestUnboundSubRegionIndex(remainingArchives.get(i), unBoundArchivesSubRegion,utopianPoint,normIntercepts);
                SubRegion subRegion = unBoundArchivesSubRegion.get(selectedIdx);
                subRegion.setArchive(remainingArchives.get(i));
                unBoundArchivesSubRegion.remove(selectedIdx);
            }
        }
    }

    //update the association between cone subregion and solution
    protected void associateSubRegionWithOptima(List<DoubleSolution> optima,double[] utopianPoint, double[] normIntercepts) {
        if(problem.getNumberOfObjectives() > 1) {
            //clearing the associate information
            for (int i = 0; i < subRegionManager.getSubRegionsNum(); ++i) {
                subRegionManager.getSubRegion(i).setOptimum(null);
            }

            List<DoubleSolution> remainingOptima = new ArrayList<>(optima.size());
            for (int i = 0; i < optima.size(); ++i) {
                //find the subregion which the solution belongs to
                SubRegion subRegion = locateSubRegion(optima.get(i),utopianPoint,normIntercepts);
                if (subRegion.getOptimum() == null) {//No solution has been bound to this subregion
                    //bind it
                    subRegion.setOptimum(optima.get(i));
                } else {
                    DoubleSolution boundOptimum= subRegion.getOptimum();
                    DoubleSolution worserOne = optima.get(i);
                    //choose the better one for subregion by comparing their indicators using in the algorithm
                    DoubleSolution betterOne = getBetterSolutionByIndicatorUnConstraint(optima.get(i), boundOptimum, subRegion,utopianPoint,normIntercepts);
                    if (betterOne == optima.get(i)) {
                        //replace the bound one
                        subRegion.setOptimum(optima.get(i));
                        worserOne = boundOptimum;
                    }
                    //record the worst one
                    remainingOptima.add(worserOne);
                }
            }

            List<SubRegion> unboundSubregion = new ArrayList<>(subRegionManager.getSubRegionsNum());
            for (int i = 0; i < subRegionManager.getSubRegionsNum(); i++) {
                SubRegion subRegion = subRegionManager.getSubRegion(i);
                if (subRegion.getOptimum() == null) {
                    unboundSubregion.add(subRegion);
                }
            }

            Collections.shuffle(remainingOptima);

            for (int i = 0; i < remainingOptima.size() && (!unboundSubregion.isEmpty()); i++) {
                int selectedIdx = nearestUnboundSubRegionIndex(remainingOptima.get(i), unboundSubregion,utopianPoint,normIntercepts);
                SubRegion subRegion = unboundSubregion.get(selectedIdx);
                subRegion.setOptimum(remainingOptima.get(i));
                unboundSubregion.remove(selectedIdx);
            }
        }
    }

    protected  boolean coneUpdateArchives(DoubleSolution solution,SubRegion targetSubRegion,double[] utopianPoint, double[] normIntercepts) {
        if(problem.getNumberOfObjectives() == 1){
            if(archives.isEmpty()){
                archives.add(solution);
                return true;
            }else if(archives.get(0).getObjective(0) > solution.getObjective(0)){
                archives.set(0,solution);
                return true;
            }
            return false;
        }else {
            DoubleSolution storeSolution = targetSubRegion.getArchive();
            if (storeSolution == null) {
                targetSubRegion.setArchive(solution);
                return true;
            }

            SubRegion storeSubRegion = locateSubRegion(storeSolution,utopianPoint,normIntercepts);
            DoubleSolution betterOne = null;
            boolean isUpdated = false;
            if (targetSubRegion == storeSubRegion) {
                DoubleSolution worserOne = solution;
                betterOne = getBetterSolutionByIndicatorUnConstraint(solution, storeSolution, targetSubRegion,utopianPoint,normIntercepts);
                if (betterOne==solution) {
                    //has updated
                    isUpdated = true;
                    targetSubRegion.setArchive(solution);
                    worserOne = storeSolution;
                }
                isUpdated |= coneNeighborUpdateArchives(worserOne, targetSubRegion,utopianPoint,normIntercepts);
            } else {
                isUpdated = true;
                //cone update recursively
                targetSubRegion.setArchive(solution);
                coneUpdateArchives(storeSolution, storeSubRegion,utopianPoint,normIntercepts);
            }

            return isUpdated;
        }
    }

    protected  boolean coneNeighborUpdateArchives(DoubleSolution solution,SubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {
        List<Integer> neighbors = targetSubRegion.getNeighbors();
        int idxNeighborSubRegion = neighbors.get(randomGenerator.nextInt(0,neighbors.size()-1));
        SubRegion neighborSubRegion = subRegionManager.getSubRegion(idxNeighborSubRegion);

        DoubleSolution storeNeighbor = neighborSubRegion.getArchive();
        if(storeNeighbor == null){
            neighborSubRegion.setArchive(solution);
            return true;
        }

        boolean isUpdated = false;
        DoubleSolution discardedOne = solution;
        DoubleSolution betterOne = getBetterSolutionForNeighborUpdateUnConstraint(solution,storeNeighbor,neighborSubRegion,utopianPoint,normIntercepts);
        if(betterOne ==(solution)){
            neighborSubRegion.setArchive(solution);
            discardedOne = storeNeighbor;
            isUpdated = true;
        }

        SubRegion boundSubRegion = nearestUnBoundSubRegionWithArchives(discardedOne,utopianPoint,normIntercepts);
        if(boundSubRegion != null){
            boundSubRegion.setArchive(discardedOne);
            isUpdated = true;
        }

        return isUpdated;
    }

    protected SubRegion nearestUnBoundSubRegionWithArchives(DoubleSolution solution,double[] utopianPoint,double[] normIntercepts){
        double[] observation = Utils.calObservation(Utils.normalize(solution,utopianPoint,normIntercepts));
        double nearestDis = Double.POSITIVE_INFINITY;
        SubRegion nearestSubRegion = null;
        for (int i=0;i<subRegionManager.getSubRegionsNum();i++) {
            SubRegion subRegion = subRegionManager.getSubRegion(i);
            if (subRegion.getArchive() == null) {
                double dis = Utils.distance2(observation, subRegion.getDirection());
                if(dis < nearestDis){
                    nearestDis = dis;
                    nearestSubRegion = subRegion;
                }
            }
        }
        return nearestSubRegion;
    }

    protected  boolean coneUpdateOptima(DoubleSolution solution,SubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {
        if(problem.getNumberOfObjectives() == 1){
            if(optima.isEmpty()){
                optima.add(solution);
                return true;
            }else if(optima.get(0).getObjective(0) > solution.getObjective(0)){
                optima.set(0,solution);
                return true;
            }
            return false;
        }else {
            DoubleSolution storeSolution = targetSubRegion.getOptimum();
            SubRegion storeSubRegion = locateSubRegion(storeSolution,utopianPoint,normIntercepts);
            DoubleSolution betterOne = null;
            boolean isUpdated = false;
            if (targetSubRegion == (storeSubRegion)) {
                DoubleSolution worserOne = solution;
                betterOne = getBetterSolutionByIndicatorUnConstraint(solution, storeSolution, targetSubRegion,utopianPoint,normIntercepts);
                if (betterOne == (solution)) {
                    //has updated
                    isUpdated = true;
                    targetSubRegion.setOptimum(solution);
                    worserOne = storeSolution;
                }
                isUpdated |= coneNeighborUpdateOptima(worserOne, targetSubRegion,utopianPoint,normIntercepts);
            } else {
                isUpdated = true;
                //cone update recursively
                targetSubRegion.setOptimum(solution);
                coneUpdateOptima(storeSolution, storeSubRegion,utopianPoint,normIntercepts);
            }

            return isUpdated;
        }
    }



    protected  boolean coneNeighborUpdateOptima(DoubleSolution solution,SubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {
        List<Integer> neighbors = targetSubRegion.getNeighbors();
        int idxNeighborSubRegion = neighbors.get(randomGenerator.nextInt(0,neighbors.size()-1));
        SubRegion neighborSubRegion = subRegionManager.getSubRegion(idxNeighborSubRegion);

        DoubleSolution storeNeighbor = neighborSubRegion.getOptimum();

        DoubleSolution betterOne = getBetterSolutionForNeighborUpdateUnConstraint(solution,storeNeighbor,neighborSubRegion,utopianPoint,normIntercepts);
        if(betterOne == (solution)){
            neighborSubRegion.setOptimum(solution);
            return true;
        }

        return false;
    }

    protected DoubleSolution getBetterSolutionForNeighborUpdate(DoubleSolution newSolution,DoubleSolution storeSolution,SubRegion neighborSubRegion,double[] utopianPoint,double[] normIntercepts){
        SubRegion idealSubRegion = locateSubRegion(storeSolution,utopianPoint,normIntercepts);
        if((idealSubRegion != neighborSubRegion) || ((idealSubRegion == neighborSubRegion) && !violationThresholdComparator.underViolationEp(storeSolution))) {
            return getBetterSolutionByIndicator(newSolution,storeSolution,neighborSubRegion.getDirection(),utopianPoint,normIntercepts,beta_NeighborUpdate);
        }
        return storeSolution;
    }

    protected  DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storeSolution, SubRegion subRegion,double[] utopianPoint,double[] normIntercepts) {
        return this.getBetterSolutionByIndicator(newSolution,storeSolution,subRegion.getDirection(),utopianPoint,normIntercepts,beta_ConeUpdate);
    }

    protected  DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution,DoubleSolution storeSolution,double[] referenceVector,double[] utopianPoint,double[] normIntercepts,double beta) {
        if (violationThresholdComparator.needToCompare(storeSolution,newSolution)) {
            int flag = violationThresholdComparator.compare(storeSolution, newSolution);
            if (flag == 1) {
                return newSolution;
            } else if (flag == -1) {
                return storeSolution;
            }
        }

        return super.getBetterSolutionByIndicator(newSolution,storeSolution,referenceVector,utopianPoint,normIntercepts,beta);
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

        SubRegion subRegion = subRegionManager.getSubRegion(idxSubRegion);
        selectedPopType = randomSelectedPolutionType();
        if(selectedPopType == POPTYPE.POP){
            DoubleSolution solution = subRegion.getSolution();
            SubRegion targetSubRegion = locateSubRegion(solution,utopianPoint,normIntercepts);
            if(violationThresholdComparator.underViolationEp(solution) && targetSubRegion == (subRegion))
                parents.add(solution);
        }else if(selectedPopType == POPTYPE.ARCHIVE) {
            if(problem.getNumberOfObjectives() == 1){
                if(!archives.isEmpty())
                    parents.add(archives.get(0));
            }else {
                DoubleSolution archive = subRegion.getArchive();
                if (archive != null) {
                    SubRegion targetSubRegion = locateSubRegion(archive,utopianPoint,normIntercepts);
                    if (targetSubRegion == subRegion)
                        parents.add(archive);
                }
            }
        }else if(selectedPopType == POPTYPE.OPTIMUM) {
            if (problem.getNumberOfObjectives() == 1) {
                if (!optima.isEmpty() && violationThresholdComparator.underViolationEp(optima.get(0)))
                    parents.add(optima.get(0));
            } else {
                DoubleSolution optimum = subRegion.getOptimum();
                SubRegion targetSubRegion = locateSubRegion(optimum,utopianPoint,normIntercepts);
                if (/*violationThresholdComparator.underViolationEp(optimum) &&*/ targetSubRegion == subRegion)
                    parents.add(optimum);
            }
        }
        List<Integer> neighborsPOP = subRegion.getNeighbors();
        List<Integer> neighborARCHIVES = new ArrayList<>();
        List<Integer> neighborOPTIMA = new ArrayList<>();
        if(problem.getNumberOfObjectives() > 1 && matingType == MatingType.NEIGHBOR){
            for (int i=0;i<neighborsPOP.size();i++){
                if (subRegionManager.getSubRegion(neighborsPOP.get(i)).getArchive() != null)
                    neighborARCHIVES.add(neighborsPOP.get(i));
                neighborOPTIMA.add(neighborsPOP.get(i));
            }
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
                        neighbors = neighborsPOP;
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
        SubRegion subRegion1 = subRegionManager.getSubRegion(idx1);
        SubRegion subRegion2 = subRegionManager.getSubRegion(idx2);
//        selectedPopType = randomSelectedPolutionType();
        if(selectedPopType == POPTYPE.POP) {
            DoubleSolution solution1 = subRegion1.getSolution();
            DoubleSolution solution2 = subRegion2.getSolution();
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

            return tourmentSelectionUnConstraint(subRegion1, solution1, subRegion2, solution2,utopianPoint,normIntercepts);
//            }
        }else if (selectedPopType==POPTYPE.OPTIMUM){
            DoubleSolution solution1 = subRegion1.getOptimum();
            DoubleSolution solution2 = subRegion2.getOptimum();
            return tourmentSelectionUnConstraint(subRegion1, solution1, subRegion2, solution2,utopianPoint,normIntercepts);
//            }
        }
        return null;
    };


    protected DoubleSolution tourmentSelection(SubRegion subRegion1,DoubleSolution solution1, SubRegion subRegion2, DoubleSolution solution2,double[] utopianPoint,double[] normIntercepts) {

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



    public List<DoubleSolution> collectArchives() {
        if(problem.getNumberOfObjectives() > 1) {
            archives = new ArrayList<>(subRegionManager.getSubRegionsNum());
            List<SubRegion> subRegionsList = subRegionManager.getSubRegionsList();
            for (int i = 0; i < subRegionsList.size(); i++) {
                DoubleSolution solution = subRegionsList.get(i).getArchive();
                if (solution != null)
                    archives.add(solution);
            }
        }
        return archives;
    }

    public List<DoubleSolution> collectOptima() {
        if(problem.getNumberOfObjectives() > 1) {
            optima = new ArrayList<>(subRegionManager.getSubRegionsNum());
            List<SubRegion> subRegionsList = subRegionManager.getSubRegionsList();
            for (int i = 0; i < subRegionsList.size(); i++) {
                DoubleSolution solution = subRegionsList.get(i).getOptimum();
                if (solution != null)
                    optima.add(solution);
            }
        }
        return optima;
    }

    public List<DoubleSolution> getMeasurePopulation() {
        return collectArchives();
    }

    @Override public String getName() {
        return "CU-DEA" ;
    }

    @Override public String getDescription() {
        return "" ;
    }

}
