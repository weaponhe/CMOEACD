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

import java.util.List;


/**
 * Created by weaponhe on 2017/11/28.
 */
public class CMOEACD_ADP extends MOEACD implements Measurable {
    protected double T0 = 1;
    protected double L = 1;
    protected double T = 1;
    protected int r = 0;

    public CMOEACD_ADP(Measurable measureManager,
                       Problem<DoubleSolution> problem,
                       int[] arrayH,
                       double[] integratedTaus,
                       int populationSize,
                       int maxEvaluations,
                       int maxGen,
                       int neighborhoodSize,
                       double neighborhoodSelectionProbability,
                       SBXCrossover sbxCrossoverOperator,
                       DifferentialEvolutionCrossover deCrossoverOperator,
                       MutationOperator<DoubleSolution> mutation,
                       AbstractMOEAD.FunctionType functionType
    ) {
        super(measureManager, problem, arrayH, integratedTaus,
                populationSize, maxEvaluations, maxGen, neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator, deCrossoverOperator, mutation, functionType);
    }

    @Override
    public void run() {
        measureManager.durationMeasure.start();
        initializeConeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population, utopianPoint, idealPoint, nadirPoint, referencePoint);
        initializeIntecepts(population, intercepts, utopianPoint, nadirPoint);
        initializeNormIntecepts(normIntercepts, utopianPoint, intercepts);

        associateSubRegion(population, utopianPoint, normIntercepts);

        do {
            calcEvolvingSubproblemList();
            updateTemperature(r);
            boolean isGenUpdated = false;
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
                isGenUpdated = isGenUpdated || isUpdated;

//
//                if(!isUpdated){
//                    isUpdated |= coneNeighborUpdate(child, subRegion, utopianPoint, normIntercepts);
//                }
                collectForAdaptiveCrossover(isUpdated);
            }
            r = isGenUpdated ? 0 : r + 1;
            gen++;

            initializeNadirPoint(population, nadirPoint);
            if (gen % updateInterval == 0)
                updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
            updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
//            associateSubRegion(population,utopianPoint,normIntercepts);

            updateAdaptiveCrossover();

        } while (gen < maxGen);
        measureManager.durationMeasure.stop();

    }

    @Override
    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion, double[] utopianPoint, double[] normIntercepts) {
        return getBetterSolutionByIndicator(newSolution, storedSolution, coneSubRegion);
    }

    protected DoubleSolution getBetterSolutionByIndicator(DoubleSolution newSolution, DoubleSolution storedSolution, ConeSubRegion coneSubRegion) {
        boolean newFessible = isFessible(newSolution);
        boolean storeFessible = isFessible(storedSolution);
        double newCV = getOverallConstraintViolationDegree(newSolution);
        double storedCV = getOverallConstraintViolationDegree(storedSolution);
        double newFun = fitnessFunction(newSolution, coneSubRegion.getRefDirection());
        double storeFun = fitnessFunction(storedSolution, coneSubRegion.getRefDirection());
        if (newFessible && storeFessible) {
            return newFun < storeFun ? newSolution : storedSolution;
        } else if (newCV < storedCV) {
            return newSolution;
        } else {
            double pf1 = Math.exp((storedCV - newCV) / T) / 2;
            double pf2 = newFun < storeFun ? Math.exp((newFun - storeFun) / T) / 2 : 0;
            double rand = randomGenerator.nextDouble(0, 1);
            if (rand < pf1) {
                return newSolution;
            } else if (rand < (pf1 + pf2)) {
                return newFun < storeFun ? newSolution : storedSolution;
            } else {
                return storedSolution;
            }
        }
    }

    protected void updateTemperature(int r) {
        T = T0 / (1 + (double) r / L);
    }

    @Override
    public MeasureManager getMeasureManager() {
        return measureManager.getMeasureManager();
    }

    @Override
    public String getName() {
        return "CMOEA/CD-ADP";
    }

    @Override
    public String getDescription() {
        return "MOEA/CD Constraint handle using Constraint Dominate Principle";
    }
}
