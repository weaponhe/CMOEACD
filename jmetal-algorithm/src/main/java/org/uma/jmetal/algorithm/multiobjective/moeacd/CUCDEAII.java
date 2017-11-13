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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/6.
 */
public class CUCDEAII extends CMOEACDAOD {
    public CUCDEAII(Problem<DoubleSolution> problem,
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

    public CUCDEAII(Measurable measureManager, Problem<DoubleSolution> problem,
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
    public CUCDEAII(Problem<DoubleSolution> problem,
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

    public CUCDEAII(Measurable measureManager, Problem<DoubleSolution> problem,
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

    @Override public void run() {

        initializeConeSubRegions();

        generatePopulation();

        initializeArchives(initialPop);
        initializeOptima(initialPop);
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(optima,utopianPointU,idealPointU,nadirPointU,referencePointU);

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
                    if(updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)){
                        if(problem.getNumberOfObjectives() == 1)
                            updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
                        else
                            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                    }
                    ConeSubRegion subRegion = locateConeSubRegion(child, utopianPoint, normIntercepts);

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
                ConeSubRegion subRegion = locateConeSubRegion(child, utopianPointU, normInterceptsU);

                isUpdated |= coneUpdateOptimum(child,subRegion,utopianPoint,normIntercepts);


                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();
            violationThresholdComparator.updateThreshold(population);

            if(problem.getNumberOfObjectives() == 1) {
                initializeNadirPoint(population, nadirPoint);
                updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
            }else {
                initializeNadirPoint(population, nadirPoint);
                initializeNadirPoint(optima,nadirPointU);
                if(gen%updateInterval==0) {
                    updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
                    updateIntercepts(optima,interceptsU,utopianPointU,nadirPointU);
                }
                updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                updateNormIntercepts(normInterceptsU,utopianPointU,interceptsU);
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

        generatePopulation();

        initializeArchives(initialPop);
        initializeOptima(initialPop);
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(optima,utopianPointU,idealPointU,nadirPointU,referencePointU);
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
                    if(updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)){
                        if(problem.getNumberOfObjectives() == 1)
                            updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
                        else
                            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                    }
                    ConeSubRegion subRegion = locateConeSubRegion(child, utopianPoint, normIntercepts);

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
                ConeSubRegion subRegion = locateConeSubRegion(child, utopianPointU, normInterceptsU);

                isUpdated |= coneUpdateOptimum(child,subRegion,utopianPoint,normIntercepts);

                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();
            violationThresholdComparator.updateThreshold(population);

            if(problem.getNumberOfObjectives() == 1) {
                initializeNadirPoint(population, nadirPoint);
                updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
            }else {
                initializeNadirPoint(population, nadirPoint);
                initializeNadirPoint(optima,nadirPointU);
                if(gen%updateInterval==0) {
                    updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
                    updateIntercepts(optima,interceptsU,utopianPointU,nadirPointU);
                }
                updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                updateNormIntercepts(normInterceptsU,utopianPointU,interceptsU);
            }
//            associateSubRegion(population,utopianPoint,normIntercepts);
//            associateSubRegionWithArchives(archives,utopianPoint,normIntercepts);
//            associateSubRegionWithOptima(optima,utopianPoint,normIntercepts);

//            JMetalLogger.logger.info("\nOptima: "+ idealPointU[0]+"\t"+" feasible: "+ idealPoint[0]+"\n");
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
        ConeSubRegion subRegion = subRegionManager.getConeSubRegion(idxSubRegion);
        ConeSubRegion optimumSubRegion = subRegion;
        int solutionIdx = subRegion.getIdxSolution();
        if(solutionIdx >= 0 && problem.getNumberOfObjectives() > 1) {
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
            if (problem.getNumberOfObjectives() == 1) {
                if (!archives.isEmpty())
                    parents.add(archives.get(0));
            } else {
                int archiveIdx = subRegion.getIdxArchive();
                if (archiveIdx >= 0) {
                    DoubleSolution archive = archives.get(archiveIdx);
                    ConeSubRegion targetSubRegion = locateConeSubRegion(archive, utopianPoint, normIntercepts);
                    if (targetSubRegion == (subRegion))
                        parents.add(archive);
                }
            }
        }else if(selectedPopType == POPTYPE.OPTIMUM) {
            if (problem.getNumberOfObjectives() == 1) {
                if (!optima.isEmpty() /*&& violationThresholdComparator.underViolationEp(optima.get(0))*/)
                    parents.add(optima.get(0));
            } else {
                DoubleSolution optimum = optima.get(optimumSubRegion.getIdxOptimum());
                ConeSubRegion targetSubRegion = locateConeSubRegion(optimum, utopianPointU, normInterceptsU);
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
                if (subRegionManager.getConeSubRegion(neighborsOrignal.get(i)).getIdxSolution() >= 0)
                    neighborPOP.add(neighborsOrignal.get(i));
            }
            if (problem.getNumberOfObjectives() > 1) {
                for (int i = 0; i < neighborsOrignal.size(); i++) {
                    if (subRegionManager.getConeSubRegion(neighborsOrignal.get(i)).getIdxArchive() >= 0)
                        neighborARCHIVES.add(neighborsOrignal.get(i));
                }
                neighborOPTIMA = optimumSubRegion.getNeighbors();
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
}
