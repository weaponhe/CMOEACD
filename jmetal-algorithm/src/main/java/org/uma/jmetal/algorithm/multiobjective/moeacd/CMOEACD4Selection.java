//package org.uma.jmetal.algorithm.multiobjective.moeacd;
//
//import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
//import org.uma.jmetal.operator.MutationOperator;
//import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
//import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
//import org.uma.jmetal.problem.ConstrainedProblem;
//import org.uma.jmetal.problem.Problem;
//import org.uma.jmetal.solution.DoubleSolution;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by weaponhe on 2018/1/29.
// */
//public class CMOEACD4Selection extends CMOEACD {
//    protected double headOrTailRatio = 0.5;
//
//    public CMOEACD4Selection(Problem<DoubleSolution> problem,
//                             int[] arrayH,
//                             double[] integratedTaus,
//                             int populationSize,
//                             int constraintLayerSize,
//                             int maxEvaluations,
//                             int maxGen,
//                             int neighborhoodSize,
//                             double neighborhoodSelectionProbability,
//                             SBXCrossover sbxCrossoverOperator,
//                             DifferentialEvolutionCrossover deCrossoverOperator,
//                             MutationOperator<DoubleSolution> mutation,
//                             AbstractMOEAD.FunctionType functionType,
//                             double[] delta,
//                             int maximumNumberOfReplacedSolutions,
//                             double headOrTailRatio
//    ) {
//        super(problem,
//                arrayH,
//                integratedTaus,
//                populationSize,
//                constraintLayerSize,
//                maxEvaluations,
//                maxGen,
//                neighborhoodSize,
//                neighborhoodSelectionProbability,
//                sbxCrossoverOperator,
//                deCrossoverOperator,
//                mutation,
//                functionType,
//                delta,
//                maximumNumberOfReplacedSolutions);
//        this.headOrTailRatio = headOrTailRatio;
//    }
//
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
//
//
//    protected List<DoubleSolution> parentSelection(int idxSubRegion, int parentPoolSize) {
////        switch (slectionType) {
////            case randout:
////            case randout_dual:
//        return parentSelection_rand_out(idxSubRegion, parentPoolSize);
////            case randin:
////            case randin_dual:
////                return parentSelection_rand_in(idxSubRegion, parentPoolSize);
////            case randout_tour:
////            case randout_tour_dual:
////                return parentSelection_rand_out_tour(idxSubRegion, parentPoolSize);
//////            case randin_tour:
////            case randin_tour_dual:
////                return parentSelection_rand_in_tour(idxSubRegion, parentPoolSize);
////            default:
////                return super.parentSelection(idxSubRegion, parentPoolSize);
////        }
//    }
//
//
//    protected List<DoubleSolution> parentSelection_rand_out(int idxSubRegion, int parentPoolSize) {
//        List<Integer> waitingSolutionIndexes = new ArrayList<>();
//        List<Integer> waitingSubRegionIndexes = new ArrayList<>();
//        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
//        List<Integer> neighbors = coneSubRegion.getNeighbors();
//
//        double[] delta = computeDelta();
//        double rand = randomGenerator.nextDouble(0, 1);
//        while (waitingSolutionIndexes.size() < parentPoolSize) {
////            if (isDual) {
//            double randHeadOrTail = randomGenerator.nextDouble(0, 1);
//            addOneNeighbor(neighbors, rand, randHeadOrTail, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
////            } else {
////                addOneNeighbor(neighbors, rand, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
////            }
//        }
//        List<DoubleSolution> selectedSolutions = new ArrayList<>(parentPoolSize);
//        for (int i = 0; i < waitingSolutionIndexes.size(); i++) {
//            selectedSolutions.add(population.get(waitingSolutionIndexes.get(i)));
//        }
//
//        return selectedSolutions;
//    }
//
//
////    protected List<DoubleSolution> parentSelection_rand_in(int idxSubRegion, int parentPoolSize) {
////        List<Integer> waitingSolutionIndexes = new ArrayList<>();
////        List<Integer> waitingSubRegionIndexes = new ArrayList<>();
////        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
////        List<Integer> neighbors = coneSubRegion.getNeighbors();
////
////        double[] delta = computeDelta();
////        while (waitingSolutionIndexes.size() < parentPoolSize) {
////            double rand = randomGenerator.nextDouble(0, 1);
////            if (isDual) {
////                double randHeadOrTail = randomGenerator.nextDouble(0, 1);
////                addOneNeighbor(neighbors, rand, randHeadOrTail, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
////            } else {
////                addOneNeighbor(neighbors, rand, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
////            }
////        }
////        List<DoubleSolution> selectedSolutions = new ArrayList<>(parentPoolSize);
////        for (int i = 0; i < waitingSolutionIndexes.size(); i++) {
////            selectedSolutions.add(population.get(waitingSolutionIndexes.get(i)));
////        }
////
////        return selectedSolutions;
////    }
////
////    protected List<DoubleSolution> parentSelection_rand_in_tour(int idxSubRegion, int parentPoolSize) {
////        int tourmentSize = 2;
////        List<Integer> waitingSolutionIndexes = new ArrayList<>();
////        List<Integer> waitingSubRegionIndexes = new ArrayList<>();
////
////        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
////        List<Integer> neighbors = coneSubRegion.getNeighbors();
////
////        double[] delta = computeDelta();
////        while (waitingSolutionIndexes.size() < parentPoolSize * tourmentSize) {
////            double rand = randomGenerator.nextDouble(0, 1);
////            if (isDual) {
////                double randHeadOrTail = randomGenerator.nextDouble(0, 1);
////                addOneNeighbor(neighbors, rand, randHeadOrTail, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
////            } else {
////                addOneNeighbor(neighbors, rand, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
////            }
////        }
////        List<DoubleSolution> selectedSolutions = new ArrayList<>(parentPoolSize);
////        for (int i = 0; i < waitingSolutionIndexes.size(); i += tourmentSize) {
////            selectedSolutions.add(tourmentSelection(
////                    population.get(waitingSolutionIndexes.get(i)),
////                    population.get(waitingSolutionIndexes.get(i + 1)),
////                    subRegionManager.getConeSubRegion(waitingSubRegionIndexes.get(i)),
////                    subRegionManager.getConeSubRegion(waitingSubRegionIndexes.get(i + 1))
////            ));
////        }
////
////
////        return selectedSolutions;
////    }
////
////    protected List<DoubleSolution> parentSelection_rand_out_tour(int idxSubRegion, int parentPoolSize) {
////        int tourmentSize = 2;
////        List<Integer> waitingSolutionIndexes = new ArrayList<>();
////        List<Integer> waitingSubRegionIndexes = new ArrayList<>();
////        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
////        List<Integer> neighbors = coneSubRegion.getNeighbors();
////        double[] delta = computeDelta();
////        double rand = randomGenerator.nextDouble(0, 1);
////        while (waitingSolutionIndexes.size() < parentPoolSize * tourmentSize) {
////            if (isDual) {
////                double randHeadOrTail = randomGenerator.nextDouble(0, 1);
////                addOneNeighbor(neighbors, rand, randHeadOrTail, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
////            } else {
////                addOneNeighbor(neighbors, rand, delta, waitingSolutionIndexes, waitingSubRegionIndexes);
////            }
////        }
////        List<DoubleSolution> selectedSolutions = new ArrayList<>(parentPoolSize);
////        for (int i = 0; i < waitingSolutionIndexes.size(); i += tourmentSize) {
////            selectedSolutions.add(tourmentSelection(
////                    population.get(waitingSolutionIndexes.get(i)),
////                    population.get(waitingSolutionIndexes.get(i + 1)),
////                    subRegionManager.getConeSubRegion(waitingSubRegionIndexes.get(i)),
////                    subRegionManager.getConeSubRegion(waitingSubRegionIndexes.get(i + 1))
////            ));
////        }
////
////
////        return selectedSolutions;
////    }
//
//    protected int getTargetLayerIndex(double rand, double[] delta, double randHeadOrTail) {
//        int idxTargetLayer;
//        if (rand < delta[0]) {
//            //neighbors子问题的第一个约束子层中找
////            idxTargetLayer = 0;
////            idxTargetLayer = (!isDual || randHeadOrTail < headOrTailRatio) ? 0 : constraintLayerSize - 1;
//            idxTargetLayer = randHeadOrTail < headOrTailRatio ? 0 : constraintLayerSize - 1;
//        } else if (rand < delta[1]) {
//            //neighbors子问题的所有约束子层中找
//            idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
//        } else if (rand < delta[2]) {
//            //所有子问题的第一个约束子层中找
////            idxTargetLayer = 0;
////            idxTargetLayer = (!isDual || randHeadOrTail < headOrTailRatio) ? 0 : constraintLayerSize - 1;
//            idxTargetLayer = randHeadOrTail < headOrTailRatio ? 0 : constraintLayerSize - 1;
//        } else {
//            //所有子问题的所有约束子层
//            idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
//        }
//        return idxTargetLayer;
//    }
//
//    protected int getTargetSubRegionIndex(List<Integer> neighbors, double rand, double[] delta) {
//        int idxTargetSubRegion;
//        if (rand < delta[0]) {
//            //neighbors子问题的第一个约束子层中找
//            idxTargetSubRegion = neighbors.get(randomGenerator.nextInt(0, neighbors.size() - 1));
//        } else if (rand < delta[1]) {
//            //neighbors子问题的所有约束子层中找
//            idxTargetSubRegion = neighbors.get(randomGenerator.nextInt(0, neighbors.size() - 1));
//        } else if (rand < delta[2]) {
//            //所有子问题的第一个约束子层中找
//            idxTargetSubRegion = randomGenerator.nextInt(0, populationSize - 1);
//        } else {
//            //所有子问题的所有约束子层
//            idxTargetSubRegion = randomGenerator.nextInt(0, populationSize - 1);
//        }
//        return idxTargetSubRegion;
//    }
//
//    protected boolean addOneNeighbor(List<Integer> neighbors, double rand, double randHeadOrTail, double[] delta,
//                                     List<Integer> waitingSolutionIndexes,
//                                     List<Integer> waitingSubRegionIndexes) {
//        int idxTargetSubRegion = getTargetSubRegionIndex(neighbors, rand, delta);
//        int idxTargetLayer = getTargetLayerIndex(rand, delta, randHeadOrTail);
//        int idxTargetSolution = subRegionManager.getConeSubRegion(idxTargetSubRegion).getSubPopulation().get(idxTargetLayer);
//        boolean flag = true;
//        for (Integer index : waitingSolutionIndexes) {
//            if (index == idxTargetSolution) {
//                flag = false;
//                break;
//            }
//        }
//        if (flag) {
//            waitingSolutionIndexes.add(idxTargetSolution);
//            waitingSubRegionIndexes.add(idxTargetSubRegion);
//        }
//        return flag;
//    }
//
////    protected boolean addOneNeighbor(List<Integer> neighbors, double rand, double[] delta,
////                                     List<Integer> waitingSolutionIndexes,
////                                     List<Integer> waitingSubRegionIndexes) {
////        int idxTargetSubRegion = getTargetSubRegionIndex(neighbors, rand, delta);
////        int idxTargetLayer = getTargetLayerIndex(rand, delta, 0);
////        int idxTargetSolution = subRegionManager.getConeSubRegion(idxTargetSubRegion).getSubPopulation().get(idxTargetLayer);
////        boolean flag = true;
////        for (Integer index : waitingSolutionIndexes) {
////            if (index == idxTargetSolution) {
////                flag = false;
////                break;
////            }
////        }
////        if (flag) {
////            waitingSolutionIndexes.add(idxTargetSolution);
////            waitingSubRegionIndexes.add(idxTargetSubRegion);
////        }
////        return flag;
////    }
//
//
////    protected DoubleSolution tourmentSelection(DoubleSolution solution1, DoubleSolution solution2, ConeSubRegion targetSubRegion1, ConeSubRegion targetSubRegion2) {
////        //不考虑约束
////        //适应度值
////        //占优关系
//////        int domination = MOEACDUtils.dominateCompare(solution1, solution2);
//////        if (domination == -1) {
//////            return solution1;
//////        } else if (domination == 1) {
//////            return solution2;
//////        } else {
//////            return solution1;
//////        }
////        double f1 = fitnessFunction(solution1, getLambda(targetSubRegion1));
////        double f2 = fitnessFunction(solution2, getLambda(targetSubRegion2));
////
////        if (f1 < f2)
////            return solution1;
////        else
////            return solution2;
////    }
//
//
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
//            MOEACD.ComparisonMethod comparisonMethod;
//            if (targetLayer == 0) {
//                comparisonMethod = MOEACD.ComparisonMethod.FEASIBILITY_FIRST;
//            } else if (targetLayer == constraintLayerSize - 1) {
//                comparisonMethod = MOEACD.ComparisonMethod.FITNESS_FIRST;
//            } else {
//                comparisonMethod = MOEACD.ComparisonMethod.CORE_AREA;
//            }
//            DoubleSolution betterSolution = getBetterSolution(solution, storedSolution, neighborSubRegion, comparisonMethod);
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
//
//    protected DoubleSolution getBetterSolution(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion targetSubRegion, MOEACD.ComparisonMethod method) {
//        if (method == MOEACD.ComparisonMethod.CORE_AREA) {
//            double newArea = calcConicalArea(constraintLayerSize, newSolution, getLambda(targetSubRegion));
//            double oldArea = calcConicalArea(constraintLayerSize, storedSolution, getLambda(targetSubRegion));
//            if (newArea < oldArea) {
//                return newSolution;
//            } else {
//                return storedSolution;
//            }
//        } else {
//            double newFitness = fitnessFunction(newSolution, getLambda(targetSubRegion));
//            double storedFitness = fitnessFunction(storedSolution, getLambda(targetSubRegion));
//            double newCV = getOverallConstraintViolationDegree(newSolution);
//            double storedCV = getOverallConstraintViolationDegree(storedSolution);
//            if (method == MOEACD.ComparisonMethod.FEASIBILITY_FIRST) {
//                if (newCV < storedCV) {
//                    return newSolution;
//                } else if (newCV > storedCV) {
//                    return storedSolution;
//                } else {
//                    if (newFitness < storedFitness) {
//                        return newSolution;
//                    } else {
//                        return storedSolution;
//                    }
//                }
//            } else if (method == MOEACD.ComparisonMethod.FITNESS_FIRST) {
//                if (newFitness < storedFitness) {
//                    return newSolution;
//                } else if (newFitness > storedFitness) {
//                    return storedSolution;
//                } else {
//                    if (newCV < storedCV) {
//                        return newSolution;
//                    } else {
//                        return storedSolution;
//                    }
//                }
//            }
//        }
//        //其他未知情况
//        return storedSolution;
//    }
//}
