package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.impl.NormalizedOverallViolationThreholdComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2017/1/5.
 */
public class CUCDEA extends CMOEACDAOII {
    public CUCDEA(Problem<DoubleSolution> problem,
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
                sbxCrossoverOperator, deCrossoverOperator, mutation);
        subRegionManager = new ConeSubRegionManagerUnified(problem.getNumberOfObjectives(), arrayH, integratedTaus);
    }

    public CUCDEA(Measurable measureManager, Problem<DoubleSolution> problem,
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
                sbxCrossoverOperator, deCrossoverOperator, mutation);
        subRegionManager = new ConeSubRegionManagerUnified(problem.getNumberOfObjectives(), arrayH, integratedTaus);
    }

    public CUCDEA(Problem<DoubleSolution> problem,
                 int[] arrayH,
                 double[] integratedTaus,
                 int populationSize,
                 int maxEvaluations,
                 int neighborhoodSize,
                 double neighborhoodSelectionProbability,
                 SBXCrossover sbxCrossoverOperator,
                 DifferentialEvolutionCrossover deCrossoverOperator,
                 MutationOperator<DoubleSolution> mutation,
                 double c_uneven
    ) {
        super(problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
        subRegionManager = new ConeSubRegionManagerUnified(problem.getNumberOfObjectives(),arrayH,integratedTaus,c_uneven);
    }

    public CUCDEA(Measurable measureManager, Problem<DoubleSolution> problem,
                 int[] arrayH,
                 double[] integratedTaus,
                 int populationSize,
                 int maxEvaluations,
                 int neighborhoodSize,
                 double neighborhoodSelectionProbability,
                 SBXCrossover sbxCrossoverOperator,
                 DifferentialEvolutionCrossover deCrossoverOperator,
                 MutationOperator<DoubleSolution> mutation,
                 double c_uneven
    ) {
        super(measureManager,problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
        subRegionManager = new ConeSubRegionManagerUnified(problem.getNumberOfObjectives(),arrayH,integratedTaus,c_uneven);

    }

    @Override
    public void run() {

        initializeConeSubRegions();
        initializePopulation();
        initializeArchives(population);
        initializeOptima(population);
        evaluations = populationSize;
        int gen = 1;
        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        violationThresholdComparator.updateThreshold(population);

        associateSubRegion(population, utopianPoint, normIntercepts);
        associateSubRegionWithArchives(archives, utopianPoint, normIntercepts);
        associateSubRegionWithOptima(optima, utopianPoint, normIntercepts);

        do {
            calcEvolvingSubproblemList();

            for (int i = 0; i < populationSize; i++) {

                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
                DoubleSolution child = children.get(0);

                problem.evaluate(child);
                if (problem instanceof ConstrainedProblem) {
                    ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child);
                }

                evaluations += 1;

                if (updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)) {
                    if (problem.getNumberOfObjectives() == 1)
                        updateNormIntercepts(normIntercepts, utopianPoint, nadirPoint);
                    else
                        updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                }

                ConeSubRegion subRegion = locateConeSubRegion(child, utopianPoint, normIntercepts);

                boolean isUpdated = coneUpdate(child, subRegion, utopianPoint, normIntercepts);
                if (isFeasible(child)) {
                    isUpdated |= coneUpdateArchives(child, subRegion, utopianPoint, normIntercepts);
                }
                isUpdated |= coneUpdateOptimum(child, subRegion, utopianPoint, normIntercepts);
                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();
            violationThresholdComparator.updateThreshold(population);

            initializeNadirPoint(population, nadirPoint);
            if(problem.getNumberOfObjectives() == 1){
                updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
            }else {
                if(gen%updateInterval==0) {
                    updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
                }
                updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
            }
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

        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        violationThresholdComparator.updateThreshold(population);

        associateSubRegion(population, utopianPoint, normIntercepts);
        associateSubRegionWithArchives(archives, utopianPoint, normIntercepts);
        associateSubRegionWithOptima(optima, utopianPoint, normIntercepts);


        //calculate measure
        measureManager.updateMeasureProgress(getMeasurePopulation());
        do {
            calcEvolvingSubproblemList();

            for (int i = 0; i < populationSize; i++) {

                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
                DoubleSolution child = children.get(0);

                problem.evaluate(child);
                if (problem instanceof ConstrainedProblem) {
                    ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child);
                }

                evaluations += 1;

                if (updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)) {
                    if (problem.getNumberOfObjectives() == 1)
                        updateNormIntercepts(normIntercepts, utopianPoint, nadirPoint);
                    else
                        updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                }

                ConeSubRegion subRegion = locateConeSubRegion(child, utopianPoint, normIntercepts);

                boolean isUpdated = coneUpdate(child, subRegion, utopianPoint, normIntercepts);
                if (isFeasible(child)) {
                    isUpdated |= coneUpdateArchives(child, subRegion, utopianPoint, normIntercepts);
                }
                isUpdated |= coneUpdateOptimum(child, subRegion, utopianPoint, normIntercepts);
                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();
            violationThresholdComparator.updateThreshold(population);

            initializeNadirPoint(population, nadirPoint);
            if(problem.getNumberOfObjectives() == 1){
                updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
            }else {
                if(gen%updateInterval==0)
                    updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
                updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
            }
//            associateSubRegion(population,utopianPoint,normIntercepts);
//            associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
//            associateSubRegionWithOptima(optima,utopianPoint,normIntercepts);

            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }


    public void updateNormIntercepts(double[] normIntercepts , double[] utopianPoint, double[] intercepts){
        if(problem.getNumberOfObjectives() == 1){
            for(int i=0;i<problem.getNumberOfObjectives();++i){
                normIntercepts[i] = intercepts[i] - utopianPoint[i];
                if(normIntercepts[i] < Constant.TOLERATION)
                    normIntercepts[i] = Constant.TOLERATION;
            }
        }else
            super.updateNormIntercepts(normIntercepts,utopianPoint,intercepts);
    }

    //use for normalization procedure for multi-, many-objective optimization
    protected void updateIntercepts(List<DoubleSolution> population, double[] intercepts, double[] utopianPoint, double[] nadirPoint) {
        if(problem.getNumberOfObjectives() == 1) {
            for (int i=0;i<problem.getNumberOfObjectives();i++)
                intercepts[i] = nadirPoint[i];
        }else
            super.updateIntercepts(population,intercepts,utopianPoint,nadirPoint);
    }

    protected void calcEvolvingSubproblemList() {
        if (problem.getNumberOfObjectives() == 1) {
            evolvingIdxList = new ArrayList<>(subRegionManager.getConeSubRegionsNum());
            double[] observation = new double[]{0.0};
            int index = subRegionManager.indexing(observation);
            for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
                evolvingIdxList.add(index);
            }
        } else {
            super.calcEvolvingSubproblemList();
        }
    }

    protected void initializeArchives(List<DoubleSolution> population) {
        if (problem.getNumberOfObjectives() == 1) {
            archives = new ArrayList<>(1);
            for (int i = 0; i < population.size(); i++) {
                if (isFeasible(population.get(i))) {
                    if (archives.isEmpty()) {
                        archives.add(population.get(i));
                    } else if (archives.get(0).getObjective(0) > population.get(i).getObjective(0)) {
                        archives.set(0, population.get(i));
                    }
                }
            }
        } else {
            super.initializeArchives(population);
        }
    }

    protected void initializeOptima(List<DoubleSolution> population) {
        if (problem.getNumberOfObjectives() == 1) {
            optima = new ArrayList<>(1);
            for (int i = 0; i < population.size(); i++) {
                if (optima.isEmpty()) {
                    optima.add(population.get(i));
                } else if (optima.get(0).getObjective(0) > population.get(i).getObjective(0)) {
                    optima.set(0, population.get(i));
                }
            }
        } else {
            super.initializeOptima(population);
        }
    }

    //update the association between cone subregion and solution
    protected void associateSubRegionWithArchives(List<DoubleSolution> archives, double[] utopianPoint, double[] normIntercepts) {
        if (problem.getNumberOfObjectives() > 1) {
            super.associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
        }
    }

    //update the association between cone subregion and solution
    protected void associateSubRegionWithOptima(List<DoubleSolution> optima,double[] utopianPoint, double[] normIntercepts) {
        if (problem.getNumberOfObjectives() > 1) {
            super.associateSubRegionWithOptima(optima,utopianPoint,normIntercepts);
        }
    }

    protected  boolean coneUpdateArchives(DoubleSolution solution, ConeSubRegion targetSubRegion, double[] utopianPoint, double[] normIntercepts) {
        if (problem.getNumberOfObjectives() == 1) {
            if (archives.isEmpty()) {
                archives.add(solution);
                return true;
            } else if (archives.get(0).getObjective(0) > solution.getObjective(0)) {
                archives.set(0, solution);
                return true;
            }
            return false;
        } else {
            return super.coneUpdateArchives(solution,targetSubRegion,utopianPoint,normIntercepts);
        }
    }
    protected  boolean coneUpdateOptimum(DoubleSolution solution,ConeSubRegion targetSubRegion,double[] utopianPoint,double[] normIntercepts) {
        if (problem.getNumberOfObjectives() == 1) {
            if (optima.isEmpty()) {
                optima.add(solution);
                return true;
            } else if (optima.get(0).getObjective(0) > solution.getObjective(0)) {
                optima.set(0, solution);
                return true;
            }
            return false;
        } else {
            return super.coneUpdateOptimum(solution,targetSubRegion,utopianPoint,normIntercepts);
        }
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
            if(problem.getNumberOfObjectives() == 1){
                if(!archives.isEmpty())
                    parents.add(archives.get(0));
            }else {
                int index = coneSubRegion.getIdxArchive();
                if (index >= 0) {
                    DoubleSolution archive = archives.get(index);
                    ConeSubRegion targetSubRegion = locateConeSubRegion(archive, utopianPoint, normIntercepts);
                    if (targetSubRegion == coneSubRegion)
                        parents.add(archive);
                }
            }
        }else if(selectedPopType == POPTYPE.OPTIMUM){
            if (problem.getNumberOfObjectives() == 1) {
                if (!optima.isEmpty() /*&& violationThresholdComparator.underViolationEp(optima.get(0))*/)
                    parents.add(optima.get(0));
            } else {
                DoubleSolution optimum = optima.get(coneSubRegion.getIdxOptimum());
                ConeSubRegion targetSubRegion = locateConeSubRegion(optimum, utopianPoint, normIntercepts);
                if (targetSubRegion == coneSubRegion)
                    parents.add(optimum);
            }
        }

        List<Integer> neighborsPOP = coneSubRegion.getNeighbors();
        List<Integer> neighborARCHIVES = new ArrayList<>();
        List<Integer> neighborOPTIMA = coneSubRegion.getNeighbors();
        if(problem.getNumberOfObjectives() > 1 && (matingType == MatingType.NEIGHBOR)){
            for (int i=0;i<neighborsPOP.size();i++){
                if (subRegionManager.getConeSubRegion(neighborsPOP.get(i)).getIdxArchive() >= 0)
                    neighborARCHIVES.add(neighborsPOP.get(i));
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
            }


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


}
