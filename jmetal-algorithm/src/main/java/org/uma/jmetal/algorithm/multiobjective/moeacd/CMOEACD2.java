package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weaponhe on 2018/1/18.
 */

/**
 * CMOEA/CD2在初始化时将个体分配给子问题，且不会随着进化过程而重新分配，类似于MOEA/D。
 * 在MOEA/D中，一个个体负责一个子问题的搜索；在CMOEA/CD2中，几个个体负责一个子问题的搜索。
 */
public class CMOEACD2 extends MOEACD {
    protected List<List<Double>> subPlaneIdealPointList;
    protected List<List<Double>> subPlaneNadirPointList;
    protected int constraintLayerSize;
    protected double[] delta;
    protected int maximumNumberOfReplacedSolutions;
    protected List<DoubleSolution> eliteArchivePop;

    public CMOEACD2(Problem<DoubleSolution> problem,
                    int[] arrayH,
                    double[] integratedTaus,
                    int populationSize,
                    int constraintLayerSize,
                    int maxEvaluations,
                    int maxGen,
                    int neighborhoodSize,
                    double neighborhoodSelectionProbability,
                    SBXCrossover sbxCrossoverOperator,
                    DifferentialEvolutionCrossover deCrossoverOperator,
                    MutationOperator<DoubleSolution> mutation,
                    AbstractMOEAD.FunctionType functionType,
                    double[] delta,
                    int maximumNumberOfReplacedSolutions
    ) {
        super(problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, maxGen, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, deCrossoverOperator, mutation, functionType);
        evolvingIdxList = new ArrayList<>(2 * populationSize);
        this.constraintLayerSize = constraintLayerSize;
        this.delta = delta;
        this.maximumNumberOfReplacedSolutions = maximumNumberOfReplacedSolutions;

        subPlaneIdealPointList = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            subPlaneIdealPointList.add(new ArrayList<Double>(2));
        }
        subPlaneNadirPointList = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            subPlaneNadirPointList.add(new ArrayList<Double>(2));
        }
        eliteArchivePop = new ArrayList<>(populationSize);
//        promisingArchivePop = new ArrayList<>(populationSize);
//        for (int i = 0; i < populationSize; i++) {
//            eliteArchivePop.add(null);
//            promisingArchivePop.add(null);
//        }
    }

    public CMOEACD2(Measurable measureManager, Problem<DoubleSolution> problem,
                    int[] arrayH,
                    double[] integratedTaus,
                    int populationSize,
                    int constraintLayerSize,
                    int maxEvaluations,
                    int maxGen,
                    int neighborhoodSize,
                    double neighborhoodSelectionProbability,
                    SBXCrossover sbxCrossoverOperator,
                    DifferentialEvolutionCrossover deCrossoverOperator,
                    MutationOperator<DoubleSolution> mutation,
                    AbstractMOEAD.FunctionType functionType,
                    double[] delta,
                    int maximumNumberOfReplacedSolutions
    ) {
        super(measureManager, problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, maxGen, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, deCrossoverOperator, mutation, functionType);
        evolvingIdxList = new ArrayList<>(2 * populationSize);
        this.maxGen = maxGen;
        this.constraintLayerSize = constraintLayerSize;
        this.delta = delta;
        this.maximumNumberOfReplacedSolutions = maximumNumberOfReplacedSolutions;

        subPlaneIdealPointList = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            subPlaneIdealPointList.add(new ArrayList<Double>(2));
        }
        subPlaneNadirPointList = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            subPlaneNadirPointList.add(new ArrayList<Double>(2));
        }
        eliteArchivePop = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            eliteArchivePop.add(null);
        }
    }

    @Override
    public void run() {
        initializeConeSubRegions();
        initializePopulation(this.populationSize * this.constraintLayerSize);
        int gen = 0;

        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

//        associateSubRegion(population, utopianPoint, normIntercepts);
        associateSubRegionRandomly();
        initializeSubExtremePoints();

        do {
            calcEvolvingSubproblemList();
            for (int i = 0; i < populationSize; i++) {
                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
                DoubleSolution child = children.get(0);
                problem.evaluate(child);
                if (problem instanceof ConstrainedProblem) {
                    ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child);
                }
                if (updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)) {
                    updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                }
                boolean isUpdated = updatePopulation(child, idealPoint, utopianPoint, normIntercepts);
                collectForAdaptiveCrossover(isUpdated);
            }
            initializeNadirPoint(population, nadirPoint);
            if (gen % updateInterval == 0)
                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
            updateAdaptiveCrossover();

            gen++;

        } while (gen < maxGen);
    }

    protected void initializeConeSubRegions() {
        subRegionManager.generateConeSubRegionList(constraintLayerSize);
        subRegionManager.initializingSubRegionsNeighbors(neighborhoodSize);
        findD0();
    }


    protected void associateSubRegionRandomly() {
        for (int i = 0; i < populationSize; i++) {
            ConeSubRegion subRegion = subRegionManager.getConeSubRegion(i);
            List<Integer> subPopulation = subRegion.getSubPopulation();
            int beginIndex = i * constraintLayerSize;
            for (int t = 0; t < constraintLayerSize; t++) {
                subPopulation.set(t, beginIndex + t);
                updateEliteArchivePop(subRegion, population.get(beginIndex + t));
            }
        }
    }


    protected void updateEliteArchivePop(ConeSubRegion subRegion, DoubleSolution newSolution) {
        int subRegionIndex = subRegion.getIdxConeSubRegion();
        DoubleSolution eliteSolution = eliteArchivePop.get(subRegionIndex);
        if (eliteSolution == null) {
            eliteArchivePop.set(subRegionIndex, newSolution);
            return;
        }
        if (!isFeasible(eliteSolution) && isFeasible(newSolution)) {
            eliteArchivePop.set(subRegionIndex, newSolution);
        } else if (isFeasible(eliteSolution) && isFeasible(newSolution)) {
            double fitnessElite = fitnessFunction(eliteSolution, getLambda(subRegion));
            double fitnessNew = fitnessFunction(newSolution, getLambda(subRegion));
            if (fitnessNew < fitnessElite) {
                eliteArchivePop.set(subRegionIndex, newSolution);
            }
        }
    }

//    protected void updatePromisingArchivePop(ConeSubRegion subRegion, DoubleSolution newSolution) {
//        int subRegionIndex = subRegion.getIdxConeSubRegion();
//        DoubleSolution eliteSolution = eliteArchivePop.get(subRegionIndex);
//        if (eliteSolution == null) {
//            promisingArchivePop.set(subRegionIndex, newSolution);
//            return;
//        }
//        if (!isFeasible(eliteSolution) && isFeasible(newSolution)) {
//            promisingArchivePop.set(subRegionIndex, newSolution);
//        } else if (isFeasible(eliteSolution) && isFeasible(newSolution)) {
//            double fitnessElite = fitnessFunction(eliteSolution, getLambda(subRegion));
//            double fitnessNew = fitnessFunction(newSolution, getLambda(subRegion));
//            if (fitnessNew < fitnessElite) {
//                promisingArchivePop.set(subRegionIndex, newSolution);
//            }
//        }
//    }

//    associateSubRegion
//    protected void associateSubRegion(List<DoubleSolution> population, double[] utopianPoint, double[] normIntercepts) {
//        List<List<Integer>> classifiedPopulation = classifyPopulation();
//        List<Integer> globalRemainedIndexList = new ArrayList<>();
//        //第一轮
//        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); ++i) {
//            ConeSubRegion subproblem = subRegionManager.getConeSubRegion(i);
//            List<Integer> subPopulation = subproblem.getSubPopulation();
//            List<Integer> candidateIndexList = classifiedPopulation.get(i);
//            List<Integer> remainedIndexList = new ArrayList<>();
//
//            for (int j = 0; j < candidateIndexList.size(); j++) {
//                int candidateIndex = candidateIndexList.get(j);
//                int layerIndex = queryConstraitLayerWithNormalization(population.get(candidateIndex), utopianPoint, normIntercepts);
//                if (subPopulation.get(layerIndex) == -1) {
//                    subPopulation.set(layerIndex, candidateIndex);
//                } else {
//                    DoubleSolution newSolution = population.get(candidateIndex);
//                    DoubleSolution storedSolution = population.get(subPopulation.get(layerIndex));
//                    ComparisonMethod method = layerIndex == 0 ? ComparisonMethod.CDP : ComparisonMethod.CORE_AREA;
//                    DoubleSolution betterSolution = getBetterSolution(newSolution, storedSolution, subproblem, method);
//                    if (betterSolution == newSolution) {
//                        remainedIndexList.add(subPopulation.get(layerIndex));
//                        subPopulation.set(layerIndex, candidateIndex);
//                    } else {
//                        remainedIndexList.add(candidateIndex);
//                    }
//                }
//            }
//
//            for (int layerIndex = 0; layerIndex < constraintLayerSize; layerIndex++) {
//                if (subPopulation.get(layerIndex) == -1) {
//                    int index = findNearestSolutionForLayer(remainedIndexList, layerIndex);
//                    if (index >= 0) {
//                        remainedIndexList.remove(remainedIndexList.indexOf(index));
//                        subPopulation.set(layerIndex, index);
//                    }
//                }
//            }
//
//            for (int solutionIndex : remainedIndexList) {
//                globalRemainedIndexList.add(solutionIndex);
//            }
//        }
//
//        //第二轮
//        for (int index : globalRemainedIndexList) {
//            int subproblemIndex = findNearestUnfilledSubproblemForSolution(population.get(index));
//            ConeSubRegion targetSubproblem = subRegionManager.getConeSubRegion(subproblemIndex);
//            for (int i = 0; i < this.constraintLayerSize; i++) {
//                if (targetSubproblem.subPopulation.get(i) == -1) {
//                    targetSubproblem.subPopulation.set(i, index);
//                    break;
//                }
//            }
//        }
//    }
//    protected List<List<Integer>> classifyPopulation() {
//        List<List<Integer>> classifiedPopulation = new ArrayList<>(populationSize);
//        for (int i = 0; i < populationSize; ++i) {
//            classifiedPopulation.add(new ArrayList<Integer>());
//        }
//        for (int i = 0; i < population.size(); ++i) {
//            DoubleSolution solution = population.get(i);
//            ConeSubRegion subproblem = locateConeSubRegion(solution, utopianPoint, normIntercepts);
//            int subproblemIndex = subproblem.getIdxConeSubRegion();
//            classifiedPopulation.get(subproblemIndex).add(i);
//        }
//        return classifiedPopulation;
//    }
//protected int findNearestUnfilledSubproblemForSolution(Solution solution) {
//    double minDis = Double.MAX_VALUE;
//    int subproblemIndex = -1;
//    for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); i++) {
//        ConeSubRegion subproblem = subRegionManager.getConeSubRegion(i);
//        if (!isSubproblemFilled(subproblem)) {
//            double[] refs = subproblem.getRefDirection();
//            List<Double> objectives = new ArrayList<>(solution.getNumberOfObjectives());
//            for (int j = 0; j < solution.getNumberOfObjectives(); j++) {
//                objectives.add(solution.getObjective(j));
//            }
//            double sum = 0;
//            for (int j = 0; j < objectives.size(); j++) {
//                sum += objectives.get(j);
//            }
//            double dis = 0;
//            for (int j = 0; j < objectives.size(); j++) {
//                dis += Math.pow(refs[j] - objectives.get(j) / sum, 2);
//            }
//            if (dis < minDis) {
//                minDis = dis;
//                subproblemIndex = i;
//            }
//        }
//    }
//    return subproblemIndex;
//}
//protected boolean isSubproblemFilled(ConeSubRegion subproblem) {
//    for (int i = 0; i < this.constraintLayerSize; i++) {
//        if (subproblem.subPopulation.get(i) == -1) {
//            return false;
//        }
//    }
//    return true;
//}
//
//    protected int findNearestSolutionForLayer(List<Integer> solutionList, int layerIndex) {
//        double minDis = Double.MAX_VALUE;
//        int index = -1;
//        for (int solutionIndex : solutionList) {
//            DoubleSolution solution = population.get(solutionIndex);
//            ConeSubRegion subRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
//            double y1 = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
////            double y2 = fitnessFunction(solution, subRegion.getRefDirection());
//            double y2 = fitnessFunction(solution, subRegion.getNormalizedWeights());
//            double v1 = y1 / (y1 + y2);
//            double r1 = layerIndex / (constraintLayerSize - 1);
//            double dis = Math.pow((r1 - v1), 2);
//            if (dis < minDis) {
//                minDis = dis;
//                index = solutionIndex;
//            }
//        }
//        return index;
//    }

    protected int queryConstraitLayer(DoubleSolution solution, ConeSubRegion subRegion) {
        int subRegionIndex = subRegion.getIdxConeSubRegion();
        double x = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
        double y = fitnessFunction(solution, getLambda(subRegion));
        List<Double> subIdealPoint = this.subPlaneIdealPointList.get(subRegionIndex);
        List<Double> subNadirPoint = this.subPlaneNadirPointList.get(subRegionIndex);
        double idealX = Math.min(subIdealPoint.get(0), x);
        double idealY = Math.min(subIdealPoint.get(1), y);
        double nadirX = Math.max(subNadirPoint.get(0), x);
        double nadirY = Math.max(subNadirPoint.get(1), y);

        double xNorm = (x - idealX) / (nadirX - idealX);
        double yNorm = (y - idealY) / (nadirY - idealY);
        int k = (int) Math.floor(((constraintLayerSize - 1) * xNorm / (xNorm + yNorm)) + 0.5);
        return k;
    }

    protected DoubleSolution getBetterSolution(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion targetSubRegion, ComparisonMethod method) {
        if (method == ComparisonMethod.CDP) {
            return getBetterSolutionByIndicatorUnConstraint(newSolution, storedSolution, targetSubRegion.getRefDirection(), utopianPoint, normIntercepts, beta_ConeUpdate);
        } else {
            double newArea = calcConicalArea(constraintLayerSize, newSolution, getLambda(targetSubRegion));
            double oldArea = calcConicalArea(constraintLayerSize, storedSolution, getLambda(targetSubRegion));
            if (newArea < oldArea) {
                return newSolution;
            } else {
                return storedSolution;
            }
        }
    }


    protected double calcConicalArea(int N, DoubleSolution solution, double[] refDirection) {
        //Normalize!!
        double y1 = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
        double y2 = fitnessFunction(solution, refDirection);
        int k = (int) Math.floor(((constraintLayerSize - 1) * y1 / (y1 + y2)) + 0.5);
        double a = (k - 0.5) / (N - k - 0.5);
        double b = (k + 0.5) / (N - k - 1.5);
        return 0.5 * (b - a) * Math.pow(y2, 2) + 0.5 * (1 / a) * Math.pow(y1 - a * y2, 2);
    }

    protected void initializeSubExtremePoints() {
        for (int i = 0; i < populationSize; i++) {
            List<Integer> pop = subRegionManager.getConeSubRegion(i).getSubPopulation();
            double xMin = Double.POSITIVE_INFINITY;
            double yMin = Double.POSITIVE_INFINITY;
            double xMax = Double.NEGATIVE_INFINITY;
            double yMax = Double.NEGATIVE_INFINITY;

            for (int j = 0; j < pop.size(); j++) {
                int index = pop.get(j);
                Solution solution = this.population.get(index);
                double x = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
                double y = fitnessFunction(solution, getLambda(subRegionManager.getConeSubRegion(i)));
                xMin = Math.min(xMin, x);
                yMin = Math.min(yMin, y);
                xMax = Math.max(xMax, x);
                yMax = Math.max(yMax, y);
            }

            List<Double> subIdealPoint = new ArrayList<>();
            subIdealPoint.add(xMin);
            subIdealPoint.add(yMin);
            this.subPlaneIdealPointList.set(i, subIdealPoint);

            List<Double> subNadirPoint = new ArrayList<>();
            subNadirPoint.add(xMax);
            subNadirPoint.add(yMax);
            this.subPlaneNadirPointList.set(i, subNadirPoint);
        }
    }

    protected void updateSubExtremePoints(int index, DoubleSolution solution) {
        ConeSubRegion targetSubRegion = subRegionManager.getConeSubRegion(index);
        double x = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
        double y = fitnessFunction(solution, getLambda(targetSubRegion));

        List<Double> subIdealPoint = this.subPlaneIdealPointList.get(index);
        subIdealPoint.set(0, Math.min(x, subIdealPoint.get(0)));
        subIdealPoint.set(1, Math.min(y, subIdealPoint.get(1)));
        List<Double> subNadirPoint = this.subPlaneNadirPointList.get(index);
        subNadirPoint.set(0, Math.max(x, subNadirPoint.get(0)));
        subNadirPoint.set(1, Math.max(y, subNadirPoint.get(1)));
    }

    protected boolean updatePopulation(DoubleSolution solution, double[] idealPoint, double[] utopianPoint, double[] normIntercepts) {
        boolean isUpdated = false;
        ConeSubRegion targetSubRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
        List<Integer> neighborIndexes = new ArrayList<>(targetSubRegion.getNeighbors());
        int time = 0;
        int size = neighborIndexes.size();

        for (int i = 0; i < size; i++) {
            int neighborIndex = neighborIndexes.get(i);
            ConeSubRegion neighborSubRegion = subRegionManager.getConeSubRegion(neighborIndex);
            int targetLayer = queryConstraitLayer(solution, neighborSubRegion);
            int targetSolutionIndex = neighborSubRegion.getSubPopulation().get(targetLayer);
            DoubleSolution storedSolution = population.get(targetSolutionIndex);
            ComparisonMethod method = targetLayer == 0 ? ComparisonMethod.CDP : ComparisonMethod.CORE_AREA;
            DoubleSolution betterSolution = getBetterSolution(solution, storedSolution, neighborSubRegion, method);
            if (betterSolution == solution) {
                population.set(targetSolutionIndex, solution);
                updateSubExtremePoints(neighborIndex, solution);
                updateEliteArchivePop(neighborSubRegion, solution);
                isUpdated = true;
                time++;
            }
            if (time >= maximumNumberOfReplacedSolutions) {
                break;
            }
        }
        return isUpdated;
    }

    protected List<DoubleSolution> parentSelection(int idxSubRegion, int parentPoolSize) {
        int tourmentSize = 2;
        List<Integer> waitingSolutionIndexes = new ArrayList<>();
        List<Integer> waitingSubRegionIndexes = new ArrayList<>();

        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
        List<Integer> neighbors = coneSubRegion.getNeighbors();

        double rand = randomGenerator.nextDouble(0, 1);
        double[] delta = computeDelta();
        while (waitingSolutionIndexes.size() < parentPoolSize * tourmentSize) {
            int idxTargetSubRegion = 0;
            int idxTargetLayer = 0;
            if (rand < delta[0]) {
                //neighbors子问题的第一个约束子层中找
                idxTargetSubRegion = neighbors.get(randomGenerator.nextInt(0, neighbors.size() - 1));
                idxTargetLayer = 0;
            } else if (rand < delta[1]) {
                //neighbors子问题的所有约束子层中找
                idxTargetSubRegion = neighbors.get(randomGenerator.nextInt(0, neighbors.size() - 1));
                idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
            } else if (rand < delta[2]) {
                //所有子问题的第一个约束子层中找
                idxTargetSubRegion = randomGenerator.nextInt(0, populationSize - 1);
                idxTargetLayer = 0;
            } else {
                //所有子问题的所有约束子层
                idxTargetSubRegion = randomGenerator.nextInt(0, populationSize - 1);
                idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
            }
            int idxTargetSolution = subRegionManager.getConeSubRegion(idxTargetSubRegion).getSubPopulation().get(idxTargetLayer);
            boolean flag = true;
            for (Integer index : waitingSolutionIndexes) {
                if (index == idxTargetSolution) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                waitingSolutionIndexes.add(idxTargetSolution);
                waitingSubRegionIndexes.add(idxTargetSubRegion);
            }
        }
        List<DoubleSolution> selectedSolutions = new ArrayList<>(parentPoolSize);
        for (int i = 0; i < waitingSolutionIndexes.size(); i += tourmentSize) {
            selectedSolutions.add(tourmentSelection(
                    population.get(waitingSolutionIndexes.get(i)),
                    population.get(waitingSolutionIndexes.get(i + 1)),
                    subRegionManager.getConeSubRegion(waitingSubRegionIndexes.get(i)),
                    subRegionManager.getConeSubRegion(waitingSubRegionIndexes.get(i + 1))
            ));
        }

        return selectedSolutions;
    }

    protected double[] computeDelta() {
        double[] ret = new double[3];
        ret[0] = this.delta[0];
        ret[1] = this.delta[0] + delta[1];
        ret[2] = this.delta[0] + delta[1] + delta[2];
        return ret;
    }

    public double[] getLambda(ConeSubRegion subRegion) {
        if (functionType == AbstractMOEAD.FunctionType.TCH || functionType == AbstractMOEAD.FunctionType.LP) {
            return subRegion.getNormalizedWeights();
        } else {
            return subRegion.getRefDirection();
        }
    }

    @Override
    public List<DoubleSolution> getResult() {
        List<DoubleSolution> pop = eliteArchivePop;
        if (pop.isEmpty())
            return pop;
        else
            return SolutionListUtils.getNondominatedSolutions(pop);
    }

    protected DoubleSolution tourmentSelection(DoubleSolution solution1, DoubleSolution solution2, ConeSubRegion targetSubRegion1, ConeSubRegion targetSubRegion2) {
        //不考虑约束
        //适应度值
        //占优关系
//        int domination = MOEACDUtils.dominateCompare(solution1, solution2);
//        if (domination == -1) {
//            return solution1;
//        } else if (domination == 1) {
//            return solution2;
//        } else {
//            return solution1;
//        }
        double f1 = fitnessFunction(solution1, getLambda(targetSubRegion1));
        double f2 = fitnessFunction(solution2, getLambda(targetSubRegion2));

        if (f1 < f2)
            return solution1;
        else
            return solution2;
    }
}
