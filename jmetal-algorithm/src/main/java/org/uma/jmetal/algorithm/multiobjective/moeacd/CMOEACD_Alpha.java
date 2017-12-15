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
//import java.util.List;
//
///**
// * Created by weaponhe on 2017/11/28.
// */
//public class CMOEACD_Alpha extends AbstractMOEACD {
//    public CMOEACD_Alpha(Problem<DoubleSolution> problem,
//                         int[] arrayH,
//                         double[] integratedTaus,
//                         int populationSize,
//                         int constraintLayerSize,
//                         int maxEvaluations,
//                         int maxGen,
//                         int neighborhoodSize,
//                         double neighborhoodSelectionProbability,
//                         AbstractMOEAD.FunctionType functionType,
//                         SBXCrossover sbxCrossoverOperator,
//                         DifferentialEvolutionCrossover deCrossoverOperator,
//                         MutationOperator<DoubleSolution> mutation,
//                         double[] delta
//    ) {
//        super(problem, arrayH, integratedTaus,
//                populationSize, constraintLayerSize, maxEvaluations, maxGen, neighborhoodSize,
//                neighborhoodSelectionProbability, functionType,
//                sbxCrossoverOperator, deCrossoverOperator, mutation, delta);
//    }
//
//    @Override
//    public void run() {
////        measureManager.durationMeasure.start();
//        initializeConeSubRegions();
//        initializePopulation();
//        int gen = 0;
//
//        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
//        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
//        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);
//
//        associateSubRegion(population, utopianPoint, normIntercepts);
//
//        //calculate measure
////        measureManager.updateMeasureProgress(getMeasurePopulation());
//        do {
//            updateFR();
//            calcEvolvingSubproblemList();
////            monitor(gen);
//            for (int i = 0; i < populationSize; i++) {
//                List<DoubleSolution> children = reproduction(evolvingIdxList.get(i));
//                DoubleSolution child = children.get(0);
//                problem.evaluate(child);
//                if (problem instanceof ConstrainedProblem) {
//                    ((ConstrainedProblem<DoubleSolution>) problem).evaluateConstraints(child);
//                }
//                if (updateExtremePoints(child, utopianPoint, idealPoint, nadirPoint, referencePoint)) {
//                    updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
//                    associateSubRegion(population, utopianPoint, normIntercepts);
//                }
//                boolean isUpdated = updatePopulation(child, idealPoint, utopianPoint, normIntercepts);
//                collectForAdaptiveCrossover(isUpdated);
//            }
////            initializeNadirPoint(population, nadirPoint);
////            if (gen % updateInterval == 0)
////                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
////            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
//            updateAdaptiveCrossover();
////            measureManager.updateMeasureProgress(getMeasurePopulation());
//
//            gen++;
//        } while (gen < maxGen);
////        measureManager.durationMeasure.stop();
//    }
//
//    @Override
//    protected List<DoubleSolution> reproduction(int idxSubRegion) {
//        return null;
//    }
//
//    @Override
//    public String getName() {
//        return "MOEA/CD-CLN";
//    }
//
//    @Override
//    public String getDescription() {
//        return "MOEA/CD Variant: Constraint Layer Normalization";
//    }
//}
