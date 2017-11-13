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
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.comparator.impl.ViolationThresholdComparator;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2016/7/20.
 */
public class CMOEACD extends MOEACD {
    enum POPTYPE{POP,ARCHIVE,OPTIMUM};
    protected POPTYPE selectedPopType = POPTYPE.POP;
    protected ViolationThresholdComparator<DoubleSolution> violationThresholdComparator ;
    protected NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;

    public CMOEACD(Problem<DoubleSolution> problem,
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

    public CMOEACD(Measurable measureManager, Problem<DoubleSolution> problem,
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
        violationThresholdComparator = new ViolationThresholdComparator<DoubleSolution>() ;
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<>();
    }

    @Override public void run() {

        initializeConeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;


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
            violationThresholdComparator.updateThreshold(population);

            initializeNadirPoint(population,nadirPoint);
            if(gen%updateInterval==0)
                updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
//            associateSubRegion(population,utopianPoint,normIntercepts);


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
            violationThresholdComparator.updateThreshold(population);

            initializeNadirPoint(population,nadirPoint);
            if(gen%updateInterval==0)
                updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
            updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
//            associateSubRegion(population,utopianPoint,normIntercepts);

            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }

    protected DoubleSolution getBetterSolutionForNeighborUpdate(DoubleSolution newSolution,DoubleSolution storedSolution,ConeSubRegion neighborSubRegion,double[] utopianPoint,double[] normIntercepts){
        ConeSubRegion idealSubRegion = locateConeSubRegion(storedSolution,utopianPoint,normIntercepts);
        if(idealSubRegion != neighborSubRegion || (idealSubRegion == neighborSubRegion && !violationThresholdComparator.underViolationEp(storedSolution))) {
            return getBetterSolutionByIndicator(newSolution,storedSolution,neighborSubRegion.getRefDirection(),utopianPoint,normIntercepts,beta_NeighborUpdate);
        }
        return storedSolution;
    }

    protected  DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution,DoubleSolution storedSolution,double[] referenceVector,double[] utopianPoint,double[] normIntercepts,double beta) {
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
//        for (int i=0;i<subRegionManager.getConeSubRegionsNum();i++) {
//            evolvingIdxList.add(i);
//        }
//
//        if(extraEvolvingSize > 0) {
//            List<Integer> marginalIdx = subRegionManager.getMarginalSubRegionIdxList();
//            List<Integer> availableExtraIdx = new ArrayList<>(marginalIdx.size());
//            for (int i=0;i< marginalIdx.size() ;i++){
//                DoubleSolution ind = population.get(subRegionManager.getConeSubRegion(marginalIdx.get(i)).getIdxSolution());
//                if(/*isFeasible(ind)*/ violationThresholdComparator.underViolationEp(ind)) {
//                    availableExtraIdx.add(marginalIdx.get(i));
//                }
//            }
////            List<Integer> extremeIdx = subRegionManager.getExtremeConeSubRegionIdxList();
////            for (int i=0;i<extremeIdx.size();i++){
////                List<Integer> neighbor = subRegionManager.getConeSubRegion(extremeIdx.get(i)).getNeighbors();
////                for (int j = 0;j<neighbor.size();j++){
////                    if(!subRegionManager.getConeSubRegion(neighbor.get(j)).isBoundaryMarginalConeSubRegion()){
////                        DoubleSolution solution = population.get(subRegionManager.getConeSubRegion(neighbor.get(j)).getIdxSolution());
////                        if(/*isFeasible(solution) */violationThresholdComparator.underViolationEp(solution)) {
////                            availableExtraIdx.add(neighbor.get(j));
////                        }
////                    }
////                }
////            }
//
//            Collections.shuffle(availableExtraIdx);
//
//            if(availableExtraIdx.size()>0) {
//                int idx = 0;
//                while (evolvingIdxList.size() < evolvingSize) {
//                    if (idx == availableExtraIdx.size())
//                        idx = 0;
//                    evolvingIdxList.add(availableExtraIdx.get(idx));
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
        DoubleSolution solution = population.get(coneSubRegion.getIdxSolution());
        ConeSubRegion targetSubRegion = locateConeSubRegion(solution,utopianPoint,normIntercepts);
        if(/*isFeasible(solution) */violationThresholdComparator.underViolationEp(solution) && targetSubRegion == coneSubRegion)
            parents.add(solution);

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


    @Override public String getName() {
        return "C-MOEA/CD" ;
    }

    @Override public String getDescription() {
        return "MOEA/CD with constraints handling" ;
    }

}