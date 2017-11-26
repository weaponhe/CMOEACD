package org.uma.jmetal.algorithm.multiobjective.moeacd;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.math3.stat.descriptive.summary.SumOfLogs;
import org.jcp.xml.dsig.internal.dom.DOMX509Data;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolutionAtBounds;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.KDTree;
import org.uma.jmetal.util.comparator.impl.NormallizedMaximumViolationThresholdComparator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by X250 on 2016/4/26.
 */
public class MOEACD extends AbstractMOEACD {

    protected int extraEvolvingSize;
    protected List<Integer> evolvingIdxList = null;
    protected double D0Mean = 0;
    protected double D0Min = 0;

    protected double chooseR;
    protected double Rde;
    protected double Rsbx;
    protected int Cde;
    protected int Sde;
    protected int Csbx;
    protected int Ssbx;

    protected double beta_ConeUpdate = 3.0;
    protected double beta_NeighborUpdate = 3.0;
    protected int idxSR;

    protected enum ComparisonMethod {
        CDP,
        CORE_AREA
    }

    ;

    public MOEACD(Problem<DoubleSolution> problem,
                  int[] arrayH,
                  double[] integratedTaus,
                  int populationSize,
                  int constraintLayerSize,
                  int maxEvaluations,
                  int maxGen,
                  int neighborhoodSize,
                  double neighborhoodSelectionProbability,
                  AbstractMOEAD.FunctionType functionType,
                  SBXCrossover sbxCrossoverOperator,
                  DifferentialEvolutionCrossover deCrossoverOperator,
                  MutationOperator<DoubleSolution> mutation,
                  double[] delta
    ) {
        super(problem, arrayH, integratedTaus,
                populationSize, constraintLayerSize, maxEvaluations, maxGen, neighborhoodSize,
                neighborhoodSelectionProbability, functionType,
                sbxCrossoverOperator, deCrossoverOperator, mutation, delta);
        evolvingIdxList = new ArrayList<>(2 * populationSize);

        chooseR = 0.5;
        Rde = 0.5;
        Rsbx = 0.5;
        Cde = 0;
        Sde = 0;
        Csbx = 0;
        Ssbx = 0;
    }

    public MOEACD(Measurable measureManager, Problem<DoubleSolution> problem,
                  int[] arrayH,
                  double[] integratedTaus,
                  int populationSize,
                  int constraintLayerSize,
                  int maxEvaluations,
                  int maxGen,
                  int neighborhoodSize,
                  double neighborhoodSelectionProbability,
                  AbstractMOEAD.FunctionType functionType,
                  SBXCrossover sbxCrossoverOperator,
                  DifferentialEvolutionCrossover deCrossoverOperator,
                  MutationOperator<DoubleSolution> mutation,
                  double[] delta
    ) {
        super(measureManager, problem, arrayH, integratedTaus,
                populationSize, constraintLayerSize, maxEvaluations, maxGen, neighborhoodSize,
                neighborhoodSelectionProbability, functionType,
                sbxCrossoverOperator, deCrossoverOperator, mutation, delta);
        evolvingIdxList = new ArrayList<>(2 * populationSize);

        chooseR = 0.5;
        Rde = 0.5;
        Rsbx = 0.5;
        Cde = 0;
        Sde = 0;
        Csbx = 0;
        Ssbx = 0;
    }

    @Override
    public void run() {
        measureManager.durationMeasure.start();
        initializeConeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 0;

        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        associateSubRegion(population, utopianPoint, normIntercepts);

        //calculate measure
        measureManager.updateMeasureProgress(getMeasurePopulation());
        do {
            monitor(gen);
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
                    updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                    //当更新了理想点时，重新绑定
                    associateSubRegion(population, utopianPoint, normIntercepts);
                }

                updateSubIdealPoint2(child);

                boolean isUpdated = updatePopulation(child, idealPoint, utopianPoint, normIntercepts);
                collectForAdaptiveCrossover(isUpdated);
            }

//            initializeNadirPoint(population, nadirPoint);
//            if (gen % updateInterval == 0)
//                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
//            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);

            updateAdaptiveCrossover();
            measureManager.updateMeasureProgress(getMeasurePopulation());

            gen++;
        } while (gen < maxGen);
        measureManager.durationMeasure.stop();
    }

    public void monitor(int gen) {
        int fessibleCount = 0;
        for (int i = 0; i < population.size(); i++) {
            DoubleSolution solution = population.get(i);
            double cv = (double) solution.getAttribute("overallConstraintViolationDegree");
            if (cv >= 0) {
                fessibleCount++;
            }
        }

        double sum = 0.0;
        for (int i = 0; i < population.size(); i++) {
            DoubleSolution solution = population.get(i);
            double cv = (double) solution.getAttribute("overallConstraintViolationDegree");
            if (cv < 0) {
                sum += cv;
            }
        }

        double aver_cv = sum / population.size();
        JMetalLogger.logger.info("[gen=" + gen + "]" + "fessibleCount: " + fessibleCount + "; " + "average cv: " + aver_cv);
    }

    public void measureRun() {
        //Start
        measureManager.durationMeasure.start();
        initializeConeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        associateSubRegion(population, utopianPoint, normIntercepts);


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
                    updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                }

                ConeSubRegion subRegion = locateConeSubRegion(child, utopianPoint, normIntercepts);
                boolean isUpdated = coneUpdate(child, subRegion, utopianPoint, normIntercepts);
//                if(!isUpdated){
//                    isUpdated |= coneNeighborUpdate(child, subRegion, utopianPoint, normIntercepts);
//                }
                collectForAdaptiveCrossover(isUpdated);
            }
            gen++;

            initializeNadirPoint(population, nadirPoint);
            if (gen % updateInterval == 0)
                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
//            associateSubRegion(population,utopianPoint,normIntercepts);

            updateAdaptiveCrossover();
            //calculate measure
            measureManager.updateMeasureProgress(getMeasurePopulation());

        } while (evaluations < maxEvaluations);
        measureManager.durationMeasure.stop();
    }


    /**
     * Initialize subproblems
     */

    protected void initializeConeSubRegions() {
        subRegionManager.generateConeSubRegionList(constraintLayerSize);
        subRegionManager.initializingSubRegionsNeighbors(neighborhoodSize);
        findD0();
    }

    protected int updateExtraEvolvingSize() {
        double maxES = Math.min(subRegionManager.getConeSubRegionsNum() / 5.0, 5.0 * subRegionManager.getMarginalSubRegionIdxList().size());
        return extraEvolvingSize = (int) Math.ceil(MOEACDUtils.sigmoid(1.0 * evaluations / maxEvaluations, 15.0, 0.382) * maxES);

    }

    protected void calcEvolvingSubproblemList() {

        extraEvolvingSize = 0;//updateExtraEvolvingSize();

        int evolvingSize = subRegionManager.getConeSubRegionsNum() + extraEvolvingSize;

        evolvingIdxList = new ArrayList<>(evolvingSize);

        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
            evolvingIdxList.add(i);
        }

        if (extraEvolvingSize > 0) {
            List<Integer> marginalIdx = subRegionManager.getMarginalSubRegionIdxList();
            Collections.shuffle(marginalIdx);
            int idx = 0;
            while (evolvingIdxList.size() < evolvingSize) {
                if (idx == marginalIdx.size())
                    idx = 0;
                evolvingIdxList.add(marginalIdx.get(idx));
                idx++;
            }
        }

        Collections.shuffle(evolvingIdxList);
    }

    //update the association between cone subregion and solution
    protected void associateSubRegion(List<DoubleSolution> population, double[] utopianPoint, double[] normIntercepts) {
        resetSubRegion();
        List<List<Integer>> classifiedPopulation = classifyPopulation();
        updateSubIdealPoint(classifiedPopulation);
        List<Integer> globalRemainedIndexList = new ArrayList<>();
        //第一轮
        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); ++i) {
            ConeSubRegion subproblem = subRegionManager.getConeSubRegion(i);
            List<Integer> subPopulation = subproblem.getSubPopulation();
            List<Integer> candidateIndexList = classifiedPopulation.get(i);
            List<Integer> remainedIndexList = new ArrayList<>();

            for (int j = 0; j < candidateIndexList.size(); j++) {
                int candidateIndex = candidateIndexList.get(j);
                int layerIndex = queryConstraitLayer(population.get(candidateIndex), utopianPoint, normIntercepts);
                if (subPopulation.get(layerIndex) == -1) {
                    subPopulation.set(layerIndex, candidateIndex);
                } else {
                    DoubleSolution newSolution = population.get(candidateIndex);
                    DoubleSolution storedSolution = population.get(subPopulation.get(layerIndex));
                    ComparisonMethod method = layerIndex == 0 ? ComparisonMethod.CDP : ComparisonMethod.CORE_AREA;
                    DoubleSolution betterSolution = getBetterSolution(newSolution, storedSolution, subproblem, method);
                    if (betterSolution == newSolution) {
                        remainedIndexList.add(subPopulation.get(layerIndex));
                        subPopulation.set(layerIndex, candidateIndex);
                    } else {
                        remainedIndexList.add(candidateIndex);
                    }
                }
            }

            for (int layerIndex = 0; layerIndex < constraintLayerSize; layerIndex++) {
                if (subPopulation.get(layerIndex) == -1) {
                    int index = findNearestSolutionForLayer(remainedIndexList, layerIndex);
                    if (index >= 0) {
                        remainedIndexList.remove(remainedIndexList.indexOf(index));
                        subPopulation.set(layerIndex, index);
                    }
                }
            }

            for (int solutionIndex : remainedIndexList) {
                globalRemainedIndexList.add(solutionIndex);
            }
        }

        //第二轮
        for (int index : globalRemainedIndexList) {
            int subproblemIndex = findNearestUnfilledSubproblemForSolution(population.get(index));
            ConeSubRegion targetSubproblem = subRegionManager.getConeSubRegion(subproblemIndex);
            for (int i = 0; i < this.constraintLayerSize; i++) {
                if (targetSubproblem.subPopulation.get(i) == -1) {
                    targetSubproblem.subPopulation.set(i, index);
                    break;
                }
            }
        }
    }

    protected void resetSubRegion() {
        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
            for (int j = 0; j < constraintLayerSize; j++) {
                subRegionManager.getConeSubRegion(i).getSubPopulation().set(j, -1);
            }
        }
    }

    protected int findNearestUnfilledSubproblemForSolution(Solution solution) {
        double minDis = Double.MAX_VALUE;
        int subproblemIndex = -1;
        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
            ConeSubRegion subproblem = subRegionManager.getConeSubRegion(i);
            if (!isSubproblemFilled(subproblem)) {
                double[] refs = subproblem.getRefDirection();
                List<Double> objectives = new ArrayList<>(solution.getNumberOfObjectives());
                for (int j = 0; j < solution.getNumberOfObjectives(); j++) {
                    objectives.add(solution.getObjective(j));
                }
                double sum = 0;
                for (int j = 0; j < objectives.size(); j++) {
                    sum += objectives.get(j);
                }
                double dis = 0;
                for (int j = 0; j < objectives.size(); j++) {
                    dis += Math.pow(refs[j] - objectives.get(j) / sum, 2);
                }
                if (dis < minDis) {
                    minDis = dis;
                    subproblemIndex = i;
                }
            }
        }
        return subproblemIndex;
    }

    protected int findNearestSolutionForLayer(List<Integer> solutionList, int layerIndex) {
        double minDis = Double.MAX_VALUE;
        int index = -1;
        for (int solutionIndex : solutionList) {
            DoubleSolution solution = population.get(solutionIndex);
            ConeSubRegion subRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
            double y1 = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
            double y2 = fitnessFunction(solution, subRegion.getRefDirection());
            double v1 = y1 / (y1 + y2);
            double r1 = layerIndex / (constraintLayerSize - 1);
            double dis = Math.pow((r1 - v1), 2);
            if (dis < minDis) {
                minDis = dis;
                index = solutionIndex;
            }
        }
        return index;
    }

//    protected void initIdealSubproblemIndex() {
//        for (int i = 0; i < population.size(); ++i) {
//            DoubleSolution solution = population.get(i);
//            ConeSubRegion subproblem = locateConeSubRegion(solution, utopianPoint, normIntercepts);
//            int subproblemIndex = subproblem.getIdxConeSubRegion();
//            solution.setAttribute("idealSubproblemIndex", subproblemIndex);
//        }
//    }
//
//    protected void initIdealFitness() {
//        for (int i = 0; i < population.size(); ++i) {
//            DoubleSolution solution = population.get(i);
//            ConeSubRegion subproblem = locateConeSubRegion(solution, utopianPoint, normIntercepts);
//            solution.setAttribute("idealFitness", fitnessFunction(solution, subproblem.getRefDirection()));
//        }
//    }
//
//    protected void initIdealConstraintLayerIndex() {
//        for (int i = 0; i < population.size(); ++i) {
//            DoubleSolution solution = population.get(i);
//            ConeSubRegion subproblem = locateConeSubRegion(solution, utopianPoint, normIntercepts);
//            solution.setAttribute("idealConstraintLayerIndex", queryConstraitLayer(solution));
//        }
//    }

    protected List<List<Integer>> classifyPopulation() {
        List<List<Integer>> classifiedPopulation = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; ++i) {
            classifiedPopulation.add(new ArrayList<Integer>());
        }
        for (int i = 0; i < population.size(); ++i) {
            DoubleSolution solution = population.get(i);
            ConeSubRegion subproblem = locateConeSubRegion(solution, utopianPoint, normIntercepts);
            int subproblemIndex = subproblem.getIdxConeSubRegion();
            classifiedPopulation.get(subproblemIndex).add(i);
        }
        return classifiedPopulation;
    }

    protected void updateSubIdealPoint(List<List<Integer>> classifiedPopulation) {
        for (int i = 0; i < classifiedPopulation.size(); i++) {
            List<Integer> pop = classifiedPopulation.get(i);
            List<Double> subIdealPoint = new ArrayList<>();
            double xMin = Double.MAX_VALUE;
            double yMin = Double.MAX_VALUE;

            for (int j = 0; j < pop.size(); j++) {
                int index = pop.get(j);
                Solution solution = this.population.get(index);
                double x = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
                double y = fitnessFunction(solution, subRegionManager.getConeSubRegion(i).getRefDirection());
                xMin = Math.min(xMin, x);
                yMin = Math.min(yMin, y);
            }

            subIdealPoint.add(xMin);
            subIdealPoint.add(yMin);

            this.subPlaneUtopianPointList.set(i, subIdealPoint);
        }
    }

    protected void updateSubIdealPoint2(DoubleSolution solution) {
        ConeSubRegion subproblem = locateConeSubRegion(solution, utopianPoint, normIntercepts);
        int subproblemIndex = subproblem.getIdxConeSubRegion();
        double x = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
        double y = fitnessFunction(solution, subRegionManager.getConeSubRegion(subproblemIndex).getRefDirection());
        List<Double> subIdealPoint = this.subPlaneUtopianPointList.get(subproblemIndex);
        subIdealPoint.set(0, Math.min(subIdealPoint.get(0), x));
        subIdealPoint.set(1, Math.min(subIdealPoint.get(1), y));
    }


    protected boolean isSubproblemFilled(ConeSubRegion subproblem) {
        for (int i = 0; i < this.constraintLayerSize; i++) {
            if (subproblem.subPopulation.get(i) == -1) {
                return false;
            }
        }
        return true;
    }

    protected int queryConstraitLayer(DoubleSolution solution, double[] utopianPoint, double[] normIntercepts) {
        ConeSubRegion subproblem = locateConeSubRegion(solution, utopianPoint, normIntercepts);
        int subproblemIndex = subproblem.getIdxConeSubRegion();
        double x = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
        double y = fitnessFunction(solution, subproblem.getRefDirection());
        List<Double> subUtopianPoint = this.subPlaneUtopianPointList.get(subproblemIndex);
        double xTrans = x - subUtopianPoint.get(0);
        double yTrans = y - subUtopianPoint.get(1);
        int k = (int) Math.floor(((constraintLayerSize - 1) * xTrans / (xTrans + yTrans)) + 0.5);
        return k;
    }

    protected double calcConicalArea(int N, DoubleSolution solution, double[] refDirection) {
        double y1 = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
        double y2 = fitnessFunction(solution, refDirection);
        int k = (int) Math.floor(((constraintLayerSize - 1) * y1 / (y1 + y2)) + 0.5);
        double a = (k - 0.5) / (N - k - 0.5);
        double b = (k + 0.5) / (N - k - 1.5);
        return 0.5 * (b - a) * Math.pow(y2, 2) + 0.5 * (1 / a) * Math.pow(y1 - a * y2, 2);
    }

    protected int nearestUnboundSubRegionIdx(DoubleSolution solution, List<Integer> unboundSubRegion, double[] utopianPoint, double[] normIntercepts) {
        int minIdx = 0;
        double[] observation = MOEACDUtils.calObservation(MOEACDUtils.normalize(solution, utopianPoint, normIntercepts));
        ConeSubRegion subRegion = subRegionManager.getConeSubRegion(unboundSubRegion.get(0));
        double minDis = MOEACDUtils.distance2(observation, subRegion.getRefDirection());
        for (int i = 1; i < unboundSubRegion.size(); ++i) {
            subRegion = subRegionManager.getConeSubRegion(unboundSubRegion.get(i));
            double tmp = MOEACDUtils.distance2(observation, subRegion.getRefDirection());
            if (tmp < minDis) {
                minDis = tmp;
                minIdx = i;
            }
        }
        return minIdx;
    }

    protected double calcConvergence(DoubleSolution solution, double[] utopianPoint, double[] normIntercepts) {
        double[] normObjectives = MOEACDUtils.normalize(solution, utopianPoint, normIntercepts);
        return MOEACDUtils.norm(normObjectives);
    }

    protected double calcConvergence(double[] normObjectives) {
        return MOEACDUtils.norm(normObjectives);
    }

    protected double calcDirectivity(DoubleSolution solution, double[] direction, double[] utopianPoint, double[] normIntercepts) {
        double[] normObjectives = MOEACDUtils.normalize(solution, utopianPoint, normIntercepts);
        double[] observation = MOEACDUtils.calObservation(normObjectives);
        return directivity(observation, direction);
    }

    protected double calcDirectivity(double[] normObjectives, double[] direction) {
        double[] observation = MOEACDUtils.calObservation(normObjectives);

        return directivity(observation, direction);
    }

    public void findD0() {
        D0Min = Double.POSITIVE_INFINITY;
        D0Mean = 0.0;
//        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
//            double minDist = Double.POSITIVE_INFINITY;
//            double[] w1 = subRegionManager.getConeSubRegion(i).getRefObservation();
//            for (int j = 0; j < subRegionManager.getConeSubRegionsNum(); j++) {
//                if (i == j) continue;
//
//                double[] w2 = subRegionManager.getConeSubRegion(j).getRefObservation();
//                double tmp = MOEACDUtils.distance(w1, w2);
//                minDist = Math.min(tmp, minDist);
//            }
//            D0Min = Math.min(D0Min, minDist);
//            D0Mean += minDist;
//        }

        KDTree kdTree = subRegionManager.getKdTree();
        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
            double[] refD = subRegionManager.getConeSubRegion(i).getRefDirection();
            List<double[]> nearestCoordinates = kdTree.queryKNearestCoordinates(refD, 2);
            double minDist = MOEACDUtils.distance(refD, nearestCoordinates.get(1));
            D0Min = Math.min(D0Min, minDist);
            D0Mean += minDist;
        }
        D0Mean /= subRegionManager.getConeSubRegionsNum();
    }

    protected boolean updatePopulation(DoubleSolution solution, double[] idealPoint, double[] utopianPoint, double[] normIntercepts) {
        ConeSubRegion targetSubRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
        boolean isUpdated = false;
        List<Integer> pop = targetSubRegion.getSubPopulation();
        List<Integer> waitingReplacedLayers = new ArrayList<>();
        int idxTargetSubRegion = targetSubRegion.getIdxConeSubRegion();
        for (int i = 0; i < constraintLayerSize; i++) {
            int idxStoredSolution = pop.get(i);
            int idealSubproblemIndex = locateConeSubRegion(population.get(idxStoredSolution), utopianPoint, normIntercepts).getIdxConeSubRegion();
            if (idealSubproblemIndex != idxTargetSubRegion) {
                waitingReplacedLayers.add(i);
            }
        }
        DoubleSolution remainedSolution = null;
        if (waitingReplacedLayers.size() == 0) {
            int idxTargetLayer = queryConstraitLayer(solution, utopianPoint, normIntercepts);
            if (idxTargetLayer >= constraintLayerSize || idxTargetLayer < 0) {
                idxTargetLayer = 0;
            }
            int idxOldSolution = pop.get(idxTargetLayer);
            DoubleSolution newSolution = solution;
            DoubleSolution storedSolution = population.get(idxOldSolution);
            ComparisonMethod method = idxTargetLayer == 0 ? ComparisonMethod.CDP : ComparisonMethod.CORE_AREA;
            DoubleSolution betterSolution = getBetterSolution(newSolution, storedSolution, targetSubRegion, method);
            if (betterSolution == newSolution) {
                remainedSolution = population.get(pop.get(idxTargetLayer));
                population.set(pop.get(idxTargetLayer), solution);
            }
        } else {
            int idxTargetLayer = waitingReplacedLayers.get(randomGenerator.nextInt(0, waitingReplacedLayers.size() - 1));
            remainedSolution = population.get(pop.get(idxTargetLayer));
            population.set(pop.get(idxTargetLayer), solution);
        }

        if (remainedSolution != null) {
            updatePopulation(remainedSolution, idealPoint, utopianPoint, normIntercepts);
        }

        return isUpdated;
    }

    protected boolean updatePopulation2(DoubleSolution solution, double[] idealPoint, double[] utopianPoint, double[] normIntercepts) {
        ConeSubRegion targetSubRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
        boolean isUpdated = false;
        List<Integer> subPop = targetSubRegion.getSubPopulation();
        for (int i = 0; i < constraintLayerSize; i++) {
            int index = subPop.get(i);
            DoubleSolution oldSolution = population.get(index);
            if (!isFessible(oldSolution) && isFessible(solution)) {
                population.set(index, solution);
                isUpdated = true;
            } else if (!isFessible(oldSolution) && !isFessible(solution)) {
                double oldCv = (double) oldSolution.getAttribute("overallConstraintViolationDegree");
                double cv = (double) solution.getAttribute("overallConstraintViolationDegree");
                if (Math.abs(cv) < Math.abs(oldCv)) {
                    population.set(index, solution);
                    isUpdated = true;
                }
            } else if (isFessible(oldSolution) && isFessible(solution)) {
                int domination = MOEACDUtils.constraintDominateCompare(solution, oldSolution);
                if (domination != -1) {
                    population.set(index, solution);
                    isUpdated = true;
                }
            }
        }
        return isUpdated;
    }


    protected boolean updatePopulation3(DoubleSolution newSolution, double[] idealPoint, double[] utopianPoint, double[] normIntercepts) {
        boolean isUpdated = false;
        ConeSubRegion targetSubRegion = locateConeSubRegion(newSolution, utopianPoint, normIntercepts);
        List<Integer> subPop = targetSubRegion.getSubPopulation();
        DoubleSolution firstLayerStoredSolution = population.get(subPop.get(0));
        DoubleSolution betterSolution = getBetterSolution(newSolution, firstLayerStoredSolution, targetSubRegion, ComparisonMethod.CDP);
        if (betterSolution == newSolution) {
            population.set(subPop.get(0), newSolution);
            isUpdated = true;
        }

        List<Integer> waitingReplacedLayers = new ArrayList<>();
        int idxTargetSubRegion = targetSubRegion.getIdxConeSubRegion();
        for (int i = 1; i < constraintLayerSize; i++) {
            int idxStoredSolution = subPop.get(i);
            int idealSubproblemIndex = locateConeSubRegion(population.get(idxStoredSolution), utopianPoint, normIntercepts).getIdxConeSubRegion();
            if (idealSubproblemIndex != idxTargetSubRegion) {
                waitingReplacedLayers.add(i);
            }
        }

        if (waitingReplacedLayers.size() == 0) {
            int idxTargetLayer = queryConstraitLayer(newSolution, utopianPoint, normIntercepts);
            if (idxTargetLayer != 0) {
                int idxOldSolution = subPop.get(idxTargetLayer);
                DoubleSolution storedSolution = population.get(idxOldSolution);
                DoubleSolution betterSolution1 = getBetterSolution(newSolution, storedSolution, targetSubRegion, ComparisonMethod.CORE_AREA);
                if (betterSolution1 == newSolution) {
                    population.set(subPop.get(idxTargetLayer), newSolution);
                    isUpdated = true;
                }
            }
        } else {
            for (int i = 0; i < waitingReplacedLayers.size(); i++) {
                int idxTargetLayer = waitingReplacedLayers.get(i);
                population.set(subPop.get(idxTargetLayer), newSolution);
                isUpdated = true;
            }
        }

        return isUpdated;
    }

    protected boolean isFessible(DoubleSolution solution) {
        return (double) solution.getAttribute("overallConstraintViolationDegree") >= 0;
    }

    protected boolean coneUpdate(DoubleSolution _solution, ConeSubRegion targetSubRegion, double[] utopianPoint, double[] normIntercepts) {
        int idxStoreInPop = targetSubRegion.getIdxSolution();
        DoubleSolution storedSolution = population.get(idxStoreInPop);
        ConeSubRegion storedSubRegion = locateConeSubRegion(storedSolution, utopianPoint, normIntercepts);
        boolean isUpdated = false;
        if (targetSubRegion == storedSubRegion) {
            DoubleSolution worseS = _solution;
            DoubleSolution betterS = getBetterSolutionByIndicator(_solution, storedSolution, targetSubRegion, utopianPoint, normIntercepts);
            if (betterS == _solution) {
                //has updated
                isUpdated = true;
                population.set(idxStoreInPop, _solution);
                worseS = storedSolution;
            }

            isUpdated |= coneNeighborUpdate(worseS, storedSubRegion, utopianPoint, normIntercepts);
        } else {
            isUpdated = true;
            population.set(idxStoreInPop, _solution);
            //cone update recursively
            coneUpdate(storedSolution, storedSubRegion, utopianPoint, normIntercepts);
        }

        return isUpdated;
    }

    protected boolean coneNeighborUpdate(DoubleSolution _solution, ConeSubRegion targetSubRegion, double[] utopianPoint, double[] normIntercepts) {
        List<Integer> neighbors = targetSubRegion.getNeighbors();
        int idxNeighborSubRegion = neighbors.get(randomGenerator.nextInt(1, neighbors.size() - 1));
//        int idxNeighborSubRegion = randomGenerator.nextInt(0,subRegionManager.getConeSubRegionsNum() - 1);

        ConeSubRegion neighborSubRegion = subRegionManager.getConeSubRegion(idxNeighborSubRegion);

        int idxNeighbor = neighborSubRegion.getIdxSolution();
        DoubleSolution storedSolution = population.get(idxNeighbor);
        DoubleSolution betterOne = getBetterSolutionForNeighborUpdate(_solution, storedSolution, neighborSubRegion, utopianPoint, normIntercepts);

        if (betterOne == _solution) {
            population.set(idxNeighbor, _solution);

            return true;
        }
        return false;
    }

    protected DoubleSolution getBetterSolutionForNeighborUpdate(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion neighborSubRegion, double[] utopianPoint, double[] normIntercepts) {
        return getBetterSolutionForNeighborUpdateUnConstraint(newSolution, storedSolution, neighborSubRegion, utopianPoint, normIntercepts);
    }

    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion, double[] utopianPoint, double[] normIntercepts) {
        return this.getBetterSolutionByIndicatorUnConstraint(newSolution, storedSolution, coneSubRegion.getRefDirection(), utopianPoint, normIntercepts, beta_ConeUpdate);
    }

    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, double[] refDirection, double[] utopianPoint, double[] normIntercepts, double beta) {
        return getBetterSolutionByIndicatorUnConstraint(newSolution, storedSolution, refDirection, utopianPoint, normIntercepts, beta);
    }

    protected DoubleSolution getBetterSolutionForNeighborUpdateUnConstraint(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion neighborSubRegion, double[] utopianPoint, double[] normIntercepts) {
        ConeSubRegion idealSubRegion = locateConeSubRegion(storedSolution, utopianPoint, normIntercepts);
        if (idealSubRegion != neighborSubRegion) {
            return getBetterSolutionByIndicatorUnConstraint(newSolution, storedSolution, neighborSubRegion.getRefDirection(), utopianPoint, normIntercepts, beta_NeighborUpdate);
        }
        return storedSolution;
    }

    protected DoubleSolution getBetterSolutionByIndicatorUnConstraint(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion, double[] utopianPoint, double[] normIntercepts) {
        return this.getBetterSolutionByIndicatorUnConstraint(newSolution, storedSolution, coneSubRegion.getRefDirection(), utopianPoint, normIntercepts, beta_ConeUpdate);
    }

    protected DoubleSolution getBetterSolutionByIndicatorUnConstraint(DoubleSolution newSolution, DoubleSolution storedSolution, double[] refDirection, double[] utopianPoint, double[] normIntercepts, double beta) {
        int domination = MOEACDUtils.dominateCompare(newSolution, storedSolution);
        if (domination == -1) {
            return newSolution;
        } else if (domination == 1 || domination == 2) {
            return storedSolution;
        } else {
            double PDDNewSolution = PDD(newSolution, refDirection, utopianPoint, normIntercepts, beta);
            double PDDStoreSolution = PDD(storedSolution, refDirection, utopianPoint, normIntercepts, beta);

            if (PDDNewSolution < PDDStoreSolution)
                return newSolution;
            else
                return storedSolution;
        }
    }

    protected DoubleSolution getBetterSolution(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion targetSubRegion, ComparisonMethod method) {
        if (method == ComparisonMethod.CDP) {
            int domination = MOEACDUtils.constraintDominateCompare(newSolution, storedSolution);
            if (domination == -1) {
                return newSolution;
            } else {
                return storedSolution;
            }
        } else {
            double newArea = calcConicalArea(constraintLayerSize, newSolution, targetSubRegion.getRefDirection());
            double oldArea = calcConicalArea(constraintLayerSize, storedSolution, targetSubRegion.getRefDirection());
            if (newArea < oldArea) {
                return newSolution;
            } else {
                return storedSolution;
            }
        }
    }


    protected double PDD(DoubleSolution solution, double[] direction, double[] utopianPoint, double[] normIntercepts, double beta) {
        double p = 1.0 * evaluations / maxEvaluations;
        double[] normObjectives = MOEACDUtils.normalize(solution, utopianPoint, normIntercepts);
        double Dc = calcConvergence(normObjectives);
        double[] observation = MOEACDUtils.calObservation(normObjectives);

        double directivityCrowdness = directivity(observation, direction);

        return Dc * (1.0 + Math.pow(p, beta) * /*problem.getNumberOfObjectives() * */directivityCrowdness);
    }


    protected double directivity(double[] observation, double[] direction) {
        int nObj = problem.getNumberOfObjectives();
        double lenLambda = 0.0;
        double crowdness = 0.0;
        double weightedCrowdness = 0.0;
        for (int i = 0; i < nObj; i++) {
            double w = direction[i];
            if (w < 0.0001)
                w = 0.0001;
            double tmp = Math.abs(observation[i] - direction[i]);
            crowdness += tmp;
//            crowdness += Math.pow(tmp,2.0);
            weightedCrowdness += (Math.pow(tmp, 2.0) / w);
//            weightedCrowdness += (tmp/w );
            lenLambda += Math.pow(direction[i], 2.0);
        }
        lenLambda = Math.sqrt(lenLambda);
//        double preK = D0Mean/2.0*Math.sqrt(1.0*(nObj-1)/nObj);
//        return Math.sqrt(lenLambda*weightedCrowdness/Math.pow(preK,2.0));
        return Math.sqrt(lenLambda * weightedCrowdness) / D0Mean;
//        return lenLambda  * weightedCrowdness/D0Mean;
//        return lenLambda  * weightedCrowdness/nObj/D0Mean;
//        return crowdness/D0Mean * lenLambda * weightedCrowdness ;
    }

    protected List<DoubleSolution> reproduction(int idxSubRegion) {
        chooseMatingType();

        chooseCrossoverType();

        int parentPoolSize = 2;
        if (crossoverType == CrossoverType.DE)
            parentPoolSize = 3;

//        List<DoubleSolution> parents = parentSelection(idxSubRegion, parentPoolSize);
        List<DoubleSolution> parents = selectParent(idxSubRegion, parentPoolSize);

        if (CrossoverType.DE == crossoverType) {
            DoubleSolution tmp = parents.get(0);
            parents.set(0, parents.get(parents.size() - 1));
            parents.set(parents.size() - 1, tmp);
        }

        List<DoubleSolution> children = null;
        if (CrossoverType.SBX == crossoverType)
            children = sbxCrossoverOperator.execute(parents);
        else if (CrossoverType.DE == crossoverType) {
            deCrossoverOperator.setCurrentSolution(parents.get(2));
            children = deCrossoverOperator.execute(parents);
        }

        mutationOperator.execute(children.get(0));

        return children;
    }

    protected List<DoubleSolution> selectParent(int idxSubRegion, int parentPoolSize) {
        List<DoubleSolution> parents = new ArrayList<>(parentPoolSize);
        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
        List<Integer> neighbors = coneSubRegion.getNeighbors();

        double[] delta = computeDelta();
        double rand = randomGenerator.nextDouble(0, 1);

        while (parents.size() < parentPoolSize) {
            int idxTargetSubproblem = 0;
            int idxTargetLayer = 0;
            if (rand < delta[0]) {
                //neighbors子问题的第一个约束子层中找
                idxTargetSubproblem = randomGenerator.nextInt(0, neighbors.size() - 1);
                idxTargetLayer = 0;
            } else if (rand < delta[1]) {
                //neighbors子问题的所有约束子层中找
                idxTargetSubproblem = randomGenerator.nextInt(0, neighbors.size() - 1);
                idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
            } else if (rand < delta[2]) {
                //所有子问题的第一个约束子层中找
                idxTargetSubproblem = randomGenerator.nextInt(0, populationSize - 1);
                idxTargetLayer = 0;
            } else {
                //所有子问题的所有约束子层
                idxTargetSubproblem = randomGenerator.nextInt(0, populationSize - 1);
                idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
            }
            int idxTargetSolution = subRegionManager.getConeSubRegion(idxTargetSubproblem).getSubPopulation().get(idxTargetLayer);
            DoubleSolution targetSolution = population.get(idxTargetSolution);
            if (!parents.contains(targetSolution)) {
                parents.add(targetSolution);
            }
        }
        return parents;
    }

    protected List<DoubleSolution> parentSelection(int idxSubRegion, int parentPoolSize) {
        List<DoubleSolution> parents = new ArrayList<>(parentPoolSize);

        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
        DoubleSolution solution = population.get(coneSubRegion.getIdxSolution());
        ConeSubRegion targetSubRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
        if (targetSubRegion == coneSubRegion)
            parents.add(solution);

        List<Integer> neighbors = coneSubRegion.getNeighbors();
        if (neighbors.size() < parentPoolSize + 1)
            matingType = MatingType.GLOBAL;

        while (parents.size() < parentPoolSize) {

            int idxSubRegion1;
            int idxSubRegion2;
            if (matingType == MatingType.NEIGHBOR) {
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

            DoubleSolution selectedSolution = tourmentSelection(idxSubRegion1, idxSubRegion2);

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
        return parents;
    }

    protected double[] computeDelta() {
        double[] ret = new double[3];
        ret[0] = this.delta[0];
        ret[1] = this.delta[0] + delta[1];
        ret[2] = this.delta[0] + delta[1] + delta[2];
        return ret;
    }

    protected DoubleSolution tourmentSelection(int idx1, int idx2) {
        ConeSubRegion subRegion1 = subRegionManager.getConeSubRegion(idx1);
        ConeSubRegion subRegion2 = subRegionManager.getConeSubRegion(idx2);
        int idxSolution1 = subRegion1.getIdxSolution();
        int idxSolution2 = subRegion2.getIdxSolution();

        DoubleSolution solution1 = population.get(idxSolution1);
        DoubleSolution solution2 = population.get(idxSolution2);
        return tourmentSelection(subRegion1, solution1, subRegion2, solution2, utopianPoint, normIntercepts);
    }

    ;

    protected DoubleSolution tourmentSelection(ConeSubRegion subRegion1, DoubleSolution solution1, ConeSubRegion subRegion2, DoubleSolution solution2, double[] utopianPoint, double[] normIntercepts) {
        return tourmentSelectionUnConstraint(subRegion1, solution1, subRegion2, solution2, utopianPoint, normIntercepts);
    }

    ;

    protected DoubleSolution tourmentSelectionUnConstraint(ConeSubRegion subRegion1, DoubleSolution solution1, ConeSubRegion subRegion2, DoubleSolution solution2, double[] utopianPoint, double[] normIntercepts) {
        int domination = MOEACDUtils.dominateCompare(solution1, solution2);
        if (domination == -1 || (domination == 2 && randomGenerator.nextDouble(0.0, 1.0) < 0.5))
            return solution1;
        else if (domination == 1)
            return solution2;
        else {
            ConeSubRegion idealSubregion1 = locateConeSubRegion(solution1, utopianPoint, normIntercepts);
            boolean isInPlace1 = (idealSubregion1 == subRegion1);
            ConeSubRegion idealSubregion2 = locateConeSubRegion(solution2, utopianPoint, normIntercepts);
            boolean isInPlace2 = (idealSubregion2 == subRegion2);
            if (isInPlace1 && !isInPlace2)
                return solution1;
            else if (!isInPlace1 && isInPlace2)
                return solution2;
        }

        if (randomGenerator.nextDouble(0.0, 1.0) < 0.5)
            return solution1;
        else
            return solution2;
    }

    ;

    protected void chooseMatingType() {
        if (randomGenerator.nextDouble() < neighborhoodSelectionProbability)
            matingType = MatingType.NEIGHBOR;
        else
            matingType = MatingType.GLOBAL;
    }

    protected void chooseCrossoverType() {
        if (randomGenerator.nextDouble() < chooseR)
            crossoverType = CrossoverType.SBX;
        else {
            crossoverType = CrossoverType.DE;
        }
    }

    protected void collectForAdaptiveCrossover(boolean isUpdated) {
        if (crossoverType == CrossoverType.SBX) {
            Csbx++;
            if (isUpdated)
                Ssbx++;
        } else {
            Cde++;
            if (isUpdated)
                Sde++;
        }
    }

    protected void updateAdaptiveCrossover() {
        double rde = 1.0 * Sde / Cde;
        double rsbx = 1.0 * Ssbx / Csbx;
        if (!(Double.isNaN(rde) || Double.isNaN(rsbx) || rde + rsbx < Constant.TOLERATION)) {
            Rde = 0.5 * Rde + 0.5 * rde / (rde + rsbx);
            double upperBound = 0.9;
            double lowerBound = 0.1;
            Rde = Math.max(Rde, lowerBound);
            Rde = Math.min(Rde, upperBound);

            Rsbx = 0.5 * Rsbx + 0.5 * rsbx / (rde + rsbx);
            Rsbx = Math.max(Rsbx, lowerBound);
            Rsbx = Math.min(Rsbx, upperBound);

            chooseR = Rsbx;
        }

        Cde = 0;
        Sde = 0;
        Csbx = 0;
        Ssbx = 0;
    }

    @Override
    public String getName() {
        return "MOEA/CD";
    }

    @Override
    public String getDescription() {
        return "Cone Decomposition based Evolutionary Algorithm (Dynamic SBX and DE)";
    }
}
