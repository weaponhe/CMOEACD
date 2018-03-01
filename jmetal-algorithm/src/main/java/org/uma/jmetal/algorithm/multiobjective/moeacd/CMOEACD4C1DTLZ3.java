//package org.uma.jmetal.algorithm.multiobjective.moeacd;
//
//import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
//import org.uma.jmetal.measure.Measurable;
//import org.uma.jmetal.measure.MeasureManager;
//import org.uma.jmetal.operator.MutationOperator;
//import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
//import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
//import org.uma.jmetal.problem.Problem;
//import org.uma.jmetal.solution.DoubleSolution;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by weaponhe on 2018/1/29.
// */
//public class CMOEACD4C1DTLZ3 extends CMOEACD4Selection {
//    public CMOEACD4C1DTLZ3(Problem<DoubleSolution> problem,
//                           int[] arrayH,
//                           double[] integratedTaus,
//                           int populationSize,
//                           int constraintLayerSize,
//                           int maxEvaluations,
//                           int maxGen,
//                           int neighborhoodSize,
//                           double neighborhoodSelectionProbability,
//                           SBXCrossover sbxCrossoverOperator,
//                           DifferentialEvolutionCrossover deCrossoverOperator,
//                           MutationOperator<DoubleSolution> mutation,
//                           AbstractMOEAD.FunctionType functionType,
//                           double[] delta,
//                           int maximumNumberOfReplacedSolutions,
//                           SELECTION_TYPE slectionType,
//                           double headOrTailRatio
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
//                maximumNumberOfReplacedSolutions,
//                slectionType,
//                headOrTailRatio);
//    }
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
//
//
////    protected List<DoubleSolution> parentSelection(int idxSubRegion, int parentPoolSize) {
////        int tourmentSize = 2;
////        List<Integer> waitingSolutionIndexes = new ArrayList<>();
////        List<Integer> waitingSubRegionIndexes = new ArrayList<>();
////
////        ConeSubRegion coneSubRegion = subRegionManager.getConeSubRegion(idxSubRegion);
////        List<Integer> neighbors = coneSubRegion.getNeighbors();
////
////
////        double[] delta = computeDelta();
////        double firstLayerRatio = 0.5;
////        //各种组合！！！
//////        放在里面还是外面
////        while (waitingSolutionIndexes.size() < parentPoolSize * tourmentSize) {
////            double rand = randomGenerator.nextDouble(0, 1);
////            double randLayer = randomGenerator.nextDouble(0, 1);
////            int idxTargetSubRegion = 0;
////            int idxTargetLayer = 0;
////            if (rand < delta[0]) {
////                //neighbors子问题的第一个约束子层中找
////                idxTargetSubRegion = neighbors.get(randomGenerator.nextInt(0, neighbors.size() - 1));
////                idxTargetLayer = (randLayer <= firstLayerRatio) ? 0 : (constraintLayerSize - 1);
////            } else if (rand < delta[1]) {
////                //neighbors子问题的所有约束子层中找
////                idxTargetSubRegion = neighbors.get(randomGenerator.nextInt(0, neighbors.size() - 1));
////                idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
////            } else if (rand < delta[2]) {
////                //所有子问题的第一个约束子层中找
////                idxTargetSubRegion = randomGenerator.nextInt(0, populationSize - 1);
////                idxTargetLayer = (randLayer <= firstLayerRatio) ? 0 : (constraintLayerSize - 1);
////            } else {
////                //所有子问题的所有约束子层
////                idxTargetSubRegion = randomGenerator.nextInt(0, populationSize - 1);
////                idxTargetLayer = randomGenerator.nextInt(0, constraintLayerSize - 1);
////            }
////            int idxTargetSolution = subRegionManager.getConeSubRegion(idxTargetSubRegion).getSubPopulation().get(idxTargetLayer);
////            boolean flag = true;
////            for (Integer index : waitingSolutionIndexes) {
////                if (index == idxTargetSolution) {
////                    flag = false;
////                    break;
////                }
////            }
////            if (flag) {
////                waitingSolutionIndexes.add(idxTargetSolution);
////                waitingSubRegionIndexes.add(idxTargetSubRegion);
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
////    @Override
////    public List<DoubleSolution> getResult() {
////        return historyPop;
////    }
//}
