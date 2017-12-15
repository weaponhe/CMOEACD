package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import java.util.*;


public class CMOEACD extends MOEACD {
    protected enum ComparisonMethod {
        CDP,
        CORE_AREA
    };

    public CMOEACD(Problem<DoubleSolution> problem,
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

    public CMOEACD(Measurable measureManager, Problem<DoubleSolution> problem,
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
        initializeConeSubRegions();
        initializePopulation();
        int gen = 0;

        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        associateSubRegion(population, utopianPoint, normIntercepts);

        do {
            updateFR();
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
                    associateSubRegion(population, utopianPoint, normIntercepts);
                }
                boolean isUpdated = updatePopulation(child, idealPoint, utopianPoint, normIntercepts);
                collectForAdaptiveCrossover(isUpdated);
            }
//            initializeNadirPoint(population, nadirPoint);
//            if (gen % updateInterval == 0)
//                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
//            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
            updateAdaptiveCrossover();

            gen++;
        } while (gen < maxGen);
    }

    protected void associateSubRegion(List<DoubleSolution> population, double[] utopianPoint, double[] normIntercepts) {
        resetSubRegion();
        classifiedPopulation = classifyPopulation();
        initializeSubExtremePoints(classifiedPopulation);
        List<Integer> globalRemainedIndexList = new ArrayList<>();
        //第一轮
        for (int i = 0; i < subRegionManager.getConeSubRegionsNum(); ++i) {
            ConeSubRegion subproblem = subRegionManager.getConeSubRegion(i);
            List<Integer> subPopulation = subproblem.getSubPopulation();
            List<Integer> candidateIndexList = classifiedPopulation.get(i);
            List<Integer> remainedIndexList = new ArrayList<>();

            for (int j = 0; j < candidateIndexList.size(); j++) {
                int candidateIndex = candidateIndexList.get(j);
                int layerIndex = queryConstraitLayerWithNormalization(population.get(candidateIndex), utopianPoint, normIntercepts);
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

    protected void initializeSubExtremePoints(List<List<Integer>> classifiedPopulation) {
        for (int i = 0; i < classifiedPopulation.size(); i++) {
            List<Integer> pop = classifiedPopulation.get(i);
            double xMin = Double.POSITIVE_INFINITY;
            double yMin = Double.POSITIVE_INFINITY;
            double xMax = Double.NEGATIVE_INFINITY;
            double yMax = Double.NEGATIVE_INFINITY;

            for (int j = 0; j < pop.size(); j++) {
                int index = pop.get(j);
                Solution solution = this.population.get(index);
                double x = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
                double y = fitnessFunction(solution, subRegionManager.getConeSubRegion(i).getRefDirection());
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

    protected boolean isSubproblemFilled(ConeSubRegion subproblem) {
        for (int i = 0; i < this.constraintLayerSize; i++) {
            if (subproblem.subPopulation.get(i) == -1) {
                return false;
            }
        }
        return true;
    }

    protected int queryConstraitLayerWithNormalization(DoubleSolution solution, double[] utopianPoint, double[] normIntercepts) {
        ConeSubRegion subproblem = locateConeSubRegion(solution, utopianPoint, normIntercepts);
        int subproblemIndex = subproblem.getIdxConeSubRegion();
        double x = Math.abs((double) solution.getAttribute("overallConstraintViolationDegree"));
        double y = fitnessFunction(solution, subproblem.getRefDirection());
        List<Double> subIdealPoint = this.subPlaneIdealPointList.get(subproblemIndex);
        List<Double> subNadirPoint = this.subPlaneNadirPointList.get(subproblemIndex);
        double idealX = Math.min(subIdealPoint.get(0), x);
        double idealY = Math.min(subIdealPoint.get(1), y);
        double nadirX = Math.max(subNadirPoint.get(0), x);
        double nadirY = Math.max(subNadirPoint.get(1), y);

        double xNorm = (x - idealX) / (nadirX - idealX);
        double yNorm = (y - idealY) / (nadirY - idealY);
        int k = (int) Math.floor(((constraintLayerSize - 1) * xNorm / (xNorm + yNorm)) + 0.5);
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

    protected void updateFR() {
        int count = 0;
        for (int i = 0; i < population.size(); i++) {
            if (isFessible(population.get(i))) {
                count++;
            }
        }
        this.FR = (double) count / population.size();

        count = 0;
        for (int i = 0; i < populationSize; i++) {
            int index = subRegionManager.getConeSubRegion(i).getSubPopulation().get(0);
            if (isFessible(population.get(index))) {
                count++;
            }
        }
        this.firstLayerFR = (double) count / populationSize;
    }

    protected boolean updatePopulation(DoubleSolution solution, double[] idealPoint, double[] utopianPoint, double[] normIntercepts) {
        boolean isUpdated = false;
        ConeSubRegion targetSubRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
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
            int idxTargetLayer = queryConstraitLayerWithNormalization(solution, utopianPoint, normIntercepts);
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
                isUpdated = true;
            }
        } else {
            int idxTargetLayer = waitingReplacedLayers.get(randomGenerator.nextInt(0, waitingReplacedLayers.size() - 1));
            remainedSolution = population.get(pop.get(idxTargetLayer));
            population.set(pop.get(idxTargetLayer), solution);
            isUpdated = true;
        }

        if (isUpdated) {
            classifiedPopulation = classifyPopulation();
            initializeSubExtremePoints(classifiedPopulation);
        }

        if (remainedSolution != null) {
            updatePopulation(remainedSolution, idealPoint, utopianPoint, normIntercepts);
        }

        return isUpdated;
    }


    protected boolean isFessible(DoubleSolution solution) {
        return (double) solution.getAttribute("overallConstraintViolationDegree") >= 0;
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

    protected List<DoubleSolution> parentSelection(int idxSubRegion, int parentPoolSize) {
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

    protected double[] computeDelta() {
        double[] ret = new double[3];
        ret[0] = this.delta[0];
        ret[1] = this.delta[0] + delta[1];
        ret[2] = this.delta[0] + delta[1] + delta[2];
        return ret;
    }

    @Override
    public String getName() {
        return "CMOEA/CD";
    }

    @Override
    public String getDescription() {
        return "Constraint CMOEA/CD";
    }
}
