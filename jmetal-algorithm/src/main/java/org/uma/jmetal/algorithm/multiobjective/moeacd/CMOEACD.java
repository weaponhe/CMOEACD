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

public class CMOEACD extends MOEACD {
    protected List<List<Double>> subPlaneIdealPointList;
    protected List<List<Double>> subPlaneNadirPointList;
    protected int constraintLayerSize;
    protected double[] delta;
    protected int maximumNumberOfReplacedSolutions;
    protected List<DoubleSolution> eliteArchivePop;
    protected double headOrTailRatio = 0.5;

    public CMOEACD(Problem<DoubleSolution> problem,
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
                   int maximumNumberOfReplacedSolutions,
                   double headOrTailRatio
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
        for (int i = 0; i < populationSize; i++) {
            eliteArchivePop.add(null);
        }
        this.headOrTailRatio = headOrTailRatio;
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
                   SBXCrossover sbxCrossoverOperator,
                   DifferentialEvolutionCrossover deCrossoverOperator,
                   MutationOperator<DoubleSolution> mutation,
                   AbstractMOEAD.FunctionType functionType,
                   double[] delta,
                   int maximumNumberOfReplacedSolutions,
                   double headOrTailRatio
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
        this.headOrTailRatio = headOrTailRatio;
    }

//    @Override
//    public void run() {
//        initializeConeSubRegions();
//        initializePopulation(this.populationSize * this.constraintLayerSize);
//        int gen = 0;
//
//        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
//        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
//        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);
//
//        associateSubRegion();
//        initializeSubExtremePoints();
//
//        do {
//            calcEvolvingSubproblemList();
//            for (int i = 0; i < populationSize; i++) {
//                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
//                DoubleSolution child = children.get(0);
//                problem.evaluate(child);
//                if (problem instanceof ConstrainedProblem) {
//                    ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child);
//                }
//                if (updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)) {
//                    updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
//                }
//                boolean isUpdated = updatePopulation(child, idealPoint, utopianPoint, normIntercepts);
//                collectForAdaptiveCrossover(isUpdated);
//            }
//            initializeNadirPoint(population, nadirPoint);
//            if (gen % updateInterval == 0)
//                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
//            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
//            updateAdaptiveCrossover();
//
//            gen++;
//
//        } while (gen < maxGen);
//    }

    @Override
    public void run() {
        initializeConeSubRegions();
        initializePopulation(this.populationSize * this.constraintLayerSize);
        int gen = 0;

        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        associateSubRegion();
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

    @Override
    public void measureRun() {
        measureManager.durationMeasure.start();
        initializeConeSubRegions();
        initializePopulation(this.populationSize * this.constraintLayerSize);
        int gen = 0;

//        for (int i = 0; i < populationSize * constraintLayerSize; i++) {
//            historyPop.add(population.get(i));
//        }

        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        associateSubRegion();
        initializeSubExtremePoints();

        do {
            calcEvolvingSubproblemList();
            for (int i = 0; i < populationSize; i++) {
                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
                DoubleSolution child = children.get(0);
                problem.evaluate(child);
//                historyPop.add(child);
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
        measureManager.durationMeasure.stop();
    }

    protected void initializeConeSubRegions() {
        subRegionManager.generateConeSubRegionList(constraintLayerSize);
        subRegionManager.initializingSubRegionsNeighbors(neighborhoodSize);
        findD0();
    }


    protected void associateSubRegion() {
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

//    protected DoubleSolution getBetterSolution(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion targetSubRegion, ComparisonMethod method) {
//        if (method == ComparisonMethod.CDP) {
//            return getBetterSolutionByIndicatorUnConstraint(newSolution, storedSolution, targetSubRegion.getRefDirection(), utopianPoint, normIntercepts, beta_ConeUpdate);
//        } else {
//            double newArea = calcConicalArea(constraintLayerSize, newSolution, getLambda(targetSubRegion));
//            double oldArea = calcConicalArea(constraintLayerSize, storedSolution, getLambda(targetSubRegion));
//            if (newArea < oldArea) {
//                return newSolution;
//            } else {
//                return storedSolution;
//            }
//        }
//    }


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

//    protected boolean updatePopulation(DoubleSolution solution, double[] idealPoint, double[] utopianPoint, double[] normIntercepts) {
//        boolean isUpdated = false;
//        ConeSubRegion targetSubRegion = locateConeSubRegion(solution, utopianPoint, normIntercepts);
//        List<Integer> neighborIndexes = new ArrayList<>(targetSubRegion.getNeighbors());
//        int time = 0;
//        int size = neighborIndexes.size();
//
//        for (int i = 0; i < size; i++) {
//            int neighborIndex = neighborIndexes.get(i);
//            ConeSubRegion neighborSubRegion = subRegionManager.getConeSubRegion(neighborIndex);
//            int targetLayer = queryConstraitLayer(solution, neighborSubRegion);
//            int targetSolutionIndex = neighborSubRegion.getSubPopulation().get(targetLayer);
//            DoubleSolution storedSolution = population.get(targetSolutionIndex);
//            ComparisonMethod method = targetLayer == 0 ? ComparisonMethod.CDP : ComparisonMethod.CORE_AREA;
//            DoubleSolution betterSolution = getBetterSolution(solution, storedSolution, neighborSubRegion, method);
//            if (betterSolution == solution) {
//                population.set(targetSolutionIndex, solution);
//                updateSubExtremePoints(neighborIndex, solution);
//                updateEliteArchivePop(neighborSubRegion, solution);
//                isUpdated = true;
//                time++;
//            }
//            if (time >= maximumNumberOfReplacedSolutions) {
//                break;
//            }
//        }
//        return isUpdated;
//    }

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
            MOEACD.ComparisonMethod comparisonMethod;
            if (targetLayer == 0) {
                comparisonMethod = MOEACD.ComparisonMethod.FEASIBILITY_FIRST;
            } else if (targetLayer == constraintLayerSize - 1) {
                comparisonMethod = MOEACD.ComparisonMethod.FITNESS_FIRST;
            } else {
                comparisonMethod = MOEACD.ComparisonMethod.CORE_AREA;
            }
            DoubleSolution betterSolution = getBetterSolution(solution, storedSolution, neighborSubRegion, comparisonMethod);
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

    protected DoubleSolution getBetterSolution(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion targetSubRegion, MOEACD.ComparisonMethod method) {
        if (method == MOEACD.ComparisonMethod.CORE_AREA) {
            double newArea = calcConicalArea(constraintLayerSize, newSolution, getLambda(targetSubRegion));
            double oldArea = calcConicalArea(constraintLayerSize, storedSolution, getLambda(targetSubRegion));
            if (newArea < oldArea) {
                return newSolution;
            } else {
                return storedSolution;
            }
        } else {
            double newFitness = fitnessFunction(newSolution, getLambda(targetSubRegion));
            double storedFitness = fitnessFunction(storedSolution, getLambda(targetSubRegion));
            double newCV = getOverallConstraintViolationDegree(newSolution);
            double storedCV = getOverallConstraintViolationDegree(storedSolution);
            if (method == MOEACD.ComparisonMethod.FEASIBILITY_FIRST) {
                if (newCV < storedCV) {
                    return newSolution;
                } else if (newCV > storedCV) {
                    return storedSolution;
                } else {
                    if (newFitness < storedFitness) {
                        return newSolution;
                    } else {
                        return storedSolution;
                    }
                }
            } else if (method == MOEACD.ComparisonMethod.FITNESS_FIRST) {
                if (newFitness < storedFitness) {
                    return newSolution;
                } else if (newFitness > storedFitness) {
                    return storedSolution;
                } else {
                    if (newCV < storedCV) {
                        return newSolution;
                    } else {
                        return storedSolution;
                    }
                }
            }
        }
        //其他未知情况
        return storedSolution;
    }

//    protected List<DoubleSolution> parentSelection(int idxSubRegion, int parentPoolSize) {
//        List<Integer> waitingSolutionIndexes = new ArrayList<>();
//        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
//        List<Integer> neighbors = coneSubRegion.getNeighbors();
//
//        double[] delta = computeDelta();
//        while (waitingSolutionIndexes.size() < parentPoolSize) {
//            double rand = randomGenerator.nextDouble(0, 1);
//            int idxTargetSubRegion = 0;
//            int idxTargetLayer = 0;
//            if (rand < delta[0]) {
//                //neighbors子问题的第一个约束子层中找
//                idxTargetSubRegion = neighbors.get(randomGenerator.nextInt(0, neighbors.size() - 1));
//                idxTargetLayer = 0;
//            } else if (rand < delta[1]) {
//                //neighbors子问题的所有约束子层中找
//                idxTargetSubRegion = neighbors.get(randomGenerator.nextInt(0, neighbors.size() - 1));
//                idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
//            } else if (rand < delta[2]) {
//                //所有子问题的第一个约束子层中找
//                idxTargetSubRegion = randomGenerator.nextInt(0, populationSize - 1);
//                idxTargetLayer = 0;
//            } else {
//                //所有子问题的所有约束子层
//                idxTargetSubRegion = randomGenerator.nextInt(0, populationSize - 1);
//                idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
//            }
//            int idxTargetSolution = subRegionManager.getConeSubRegion(idxTargetSubRegion).getSubPopulation().get(idxTargetLayer);
//            boolean flag = true;
//            for (Integer index : waitingSolutionIndexes) {
//                if (index == idxTargetSolution) {
//                    flag = false;
//                    break;
//                }
//            }
//            if (flag) {
//                waitingSolutionIndexes.add(idxTargetSolution);
//            }
//        }
//        List<DoubleSolution> selectedSolutions = new ArrayList<>(parentPoolSize);
//        for (int i = 0; i < waitingSolutionIndexes.size(); i++) {
//            selectedSolutions.add(population.get(waitingSolutionIndexes.get(i)));
//        }
//
//        return selectedSolutions;
//    }
    protected List<DoubleSolution> parentSelection(int idxSubRegion, int parentPoolSize) {
//        switch (slectionType) {
//            case randout:
//            case randout_dual:
        return parentSelection_rand_out(idxSubRegion, parentPoolSize);
//            case randin:
//            case randin_dual:
//                return parentSelection_rand_in(idxSubRegion, parentPoolSize);
//            case randout_tour:
//            case randout_tour_dual:
//                return parentSelection_rand_out_tour(idxSubRegion, parentPoolSize);
////            case randin_tour:
//            case randin_tour_dual:
//                return parentSelection_rand_in_tour(idxSubRegion, parentPoolSize);
//            default:
//                return super.parentSelection(idxSubRegion, parentPoolSize);
//        }
    }


    protected List<DoubleSolution> parentSelection_rand_out(int idxSubRegion, int parentPoolSize) {
        List<Integer> waitingSolutionIndexes = new ArrayList<>();
        List<Integer> waitingSubRegionIndexes = new ArrayList<>();
        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
        List<Integer> neighbors = coneSubRegion.getNeighbors();

        double[] delta = computeDelta();
        double rand = randomGenerator.nextDouble(0, 1);
        while (waitingSolutionIndexes.size() < parentPoolSize) {
//            if (isDual) {
            double randHeadOrTail = randomGenerator.nextDouble(0, 1);
            addOneNeighbor(neighbors, rand, randHeadOrTail, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
//            } else {
//                addOneNeighbor(neighbors, rand, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
//            }
        }
        List<DoubleSolution> selectedSolutions = new ArrayList<>(parentPoolSize);
        for (int i = 0; i < waitingSolutionIndexes.size(); i++) {
            selectedSolutions.add(population.get(waitingSolutionIndexes.get(i)));
        }

        return selectedSolutions;
    }


//    protected List<DoubleSolution> parentSelection_rand_in(int idxSubRegion, int parentPoolSize) {
//        List<Integer> waitingSolutionIndexes = new ArrayList<>();
//        List<Integer> waitingSubRegionIndexes = new ArrayList<>();
//        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
//        List<Integer> neighbors = coneSubRegion.getNeighbors();
//
//        double[] delta = computeDelta();
//        while (waitingSolutionIndexes.size() < parentPoolSize) {
//            double rand = randomGenerator.nextDouble(0, 1);
//            if (isDual) {
//                double randHeadOrTail = randomGenerator.nextDouble(0, 1);
//                addOneNeighbor(neighbors, rand, randHeadOrTail, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
//            } else {
//                addOneNeighbor(neighbors, rand, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
//            }
//        }
//        List<DoubleSolution> selectedSolutions = new ArrayList<>(parentPoolSize);
//        for (int i = 0; i < waitingSolutionIndexes.size(); i++) {
//            selectedSolutions.add(population.get(waitingSolutionIndexes.get(i)));
//        }
//
//        return selectedSolutions;
//    }
//
//    protected List<DoubleSolution> parentSelection_rand_in_tour(int idxSubRegion, int parentPoolSize) {
//        int tourmentSize = 2;
//        List<Integer> waitingSolutionIndexes = new ArrayList<>();
//        List<Integer> waitingSubRegionIndexes = new ArrayList<>();
//
//        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
//        List<Integer> neighbors = coneSubRegion.getNeighbors();
//
//        double[] delta = computeDelta();
//        while (waitingSolutionIndexes.size() < parentPoolSize * tourmentSize) {
//            double rand = randomGenerator.nextDouble(0, 1);
//            if (isDual) {
//                double randHeadOrTail = randomGenerator.nextDouble(0, 1);
//                addOneNeighbor(neighbors, rand, randHeadOrTail, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
//            } else {
//                addOneNeighbor(neighbors, rand, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
//            }
//        }
//        List<DoubleSolution> selectedSolutions = new ArrayList<>(parentPoolSize);
//        for (int i = 0; i < waitingSolutionIndexes.size(); i += tourmentSize) {
//            selectedSolutions.add(tourmentSelection(
//                    population.get(waitingSolutionIndexes.get(i)),
//                    population.get(waitingSolutionIndexes.get(i + 1)),
//                    subRegionManager.getConeSubRegion(waitingSubRegionIndexes.get(i)),
//                    subRegionManager.getConeSubRegion(waitingSubRegionIndexes.get(i + 1))
//            ));
//        }
//
//
//        return selectedSolutions;
//    }
//
//    protected List<DoubleSolution> parentSelection_rand_out_tour(int idxSubRegion, int parentPoolSize) {
//        int tourmentSize = 2;
//        List<Integer> waitingSolutionIndexes = new ArrayList<>();
//        List<Integer> waitingSubRegionIndexes = new ArrayList<>();
//        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
//        List<Integer> neighbors = coneSubRegion.getNeighbors();
//        double[] delta = computeDelta();
//        double rand = randomGenerator.nextDouble(0, 1);
//        while (waitingSolutionIndexes.size() < parentPoolSize * tourmentSize) {
//            if (isDual) {
//                double randHeadOrTail = randomGenerator.nextDouble(0, 1);
//                addOneNeighbor(neighbors, rand, randHeadOrTail, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
//            } else {
//                addOneNeighbor(neighbors, rand, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
//            }
//        }
//        List<DoubleSolution> selectedSolutions = new ArrayList<>(parentPoolSize);
//        for (int i = 0; i < waitingSolutionIndexes.size(); i += tourmentSize) {
//            selectedSolutions.add(tourmentSelection(
//                    population.get(waitingSolutionIndexes.get(i)),
//                    population.get(waitingSolutionIndexes.get(i + 1)),
//                    subRegionManager.getConeSubRegion(waitingSubRegionIndexes.get(i)),
//                    subRegionManager.getConeSubRegion(waitingSubRegionIndexes.get(i + 1))
//            ));
//        }
//
//
//        return selectedSolutions;
//    }

    protected int getTargetLayerIndex(double rand, double[] delta, double randHeadOrTail) {
        int idxTargetLayer;
        if (rand < delta[0]) {
            //neighbors子问题的第一个约束子层中找
//            idxTargetLayer = 0;
//            idxTargetLayer = (!isDual || randHeadOrTail < headOrTailRatio) ? 0 : constraintLayerSize - 1;
            idxTargetLayer = randHeadOrTail < headOrTailRatio ? 0 : constraintLayerSize - 1;
        } else if (rand < delta[1]) {
            //neighbors子问题的所有约束子层中找
            idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
        } else if (rand < delta[2]) {
            //所有子问题的第一个约束子层中找
//            idxTargetLayer = 0;
//            idxTargetLayer = (!isDual || randHeadOrTail < headOrTailRatio) ? 0 : constraintLayerSize - 1;
            idxTargetLayer = randHeadOrTail < headOrTailRatio ? 0 : constraintLayerSize - 1;
        } else {
            //所有子问题的所有约束子层
            idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
        }
        return idxTargetLayer;
    }

    protected int getTargetSubRegionIndex(List<Integer> neighbors, double rand, double[] delta) {
        int idxTargetSubRegion;
        if (rand < delta[0]) {
            //neighbors子问题的第一个约束子层中找
            idxTargetSubRegion = neighbors.get(randomGenerator.nextInt(0, neighbors.size() - 1));
        } else if (rand < delta[1]) {
            //neighbors子问题的所有约束子层中找
            idxTargetSubRegion = neighbors.get(randomGenerator.nextInt(0, neighbors.size() - 1));
        } else if (rand < delta[2]) {
            //所有子问题的第一个约束子层中找
            idxTargetSubRegion = randomGenerator.nextInt(0, populationSize - 1);
        } else {
            //所有子问题的所有约束子层
            idxTargetSubRegion = randomGenerator.nextInt(0, populationSize - 1);
        }
        return idxTargetSubRegion;
    }

    protected boolean addOneNeighbor(List<Integer> neighbors, double rand, double randHeadOrTail, double[] delta,
                                     List<Integer> waitingSolutionIndexes,
                                     List<Integer> waitingSubRegionIndexes) {
        int idxTargetSubRegion = getTargetSubRegionIndex(neighbors, rand, delta);
        int idxTargetLayer = getTargetLayerIndex(rand, delta, randHeadOrTail);
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
        return flag;
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

//    @Override
//    public List<DoubleSolution> getResult() {
//        List<DoubleSolution> pop = eliteArchivePop;
//        if (pop.isEmpty())
//            return pop;
//        else
//            return SolutionListUtils.getNondominatedSolutions(pop);
//    }
}
