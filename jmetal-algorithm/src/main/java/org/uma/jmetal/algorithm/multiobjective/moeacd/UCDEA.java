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
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.comparator.impl.OverallConstraintViolationComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by X250 on 2017/1/5.
 */
public class UCDEA extends MOEACD {
    public UCDEA(Problem<DoubleSolution> problem,
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
        super(problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
        subRegionManager = new ConeSubRegionManagerUnified(problem.getNumberOfObjectives(),arrayH,integratedTaus);
    }

    public UCDEA(Measurable measureManager, Problem<DoubleSolution> problem,
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
        super(measureManager,problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,deCrossoverOperator, mutation);
        subRegionManager = new ConeSubRegionManagerUnified(problem.getNumberOfObjectives(),arrayH,integratedTaus);

    }

    public UCDEA(Problem<DoubleSolution> problem,
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

    public UCDEA(Measurable measureManager, Problem<DoubleSolution> problem,
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
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        associateSubRegion(population,utopianPoint,normIntercepts);

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

                if(updateExtremePoints(child,utopianPoint,idealPoint,nadirPoint,referencePoint)) {
                    if(problem.getNumberOfObjectives() == 1)
                        updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
                    else
                        updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                }

                ConeSubRegion subRegion = locateConeSubRegion(child,utopianPoint,normIntercepts);

                boolean isUpdated = coneUpdate(child, subRegion,utopianPoint,normIntercepts);

                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();

            initializeNadirPoint(population,nadirPoint);
            if(problem.getNumberOfObjectives() == 1){
                updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
            }else {
                if(gen%updateInterval==0)
                    updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
                updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
            }
//            associateSubRegion(population,utopianPoint,normIntercepts);


        } while (evaluations < maxEvaluations);
    }

    public void measureRun() {
        //Start
        measureManager.durationMeasure.start();
        initializeConeSubRegions();
        initializePopulation();
        evaluations = populationSize;
        int gen = 1;

        initializeExtremePoints(population,utopianPoint,idealPoint,nadirPoint,referencePoint);
        initializeIntecepts(population,intercepts,utopianPoint,nadirPoint);
        initializeNormIntecepts(normIntercepts,utopianPoint,intercepts);

        associateSubRegion(population,utopianPoint,normIntercepts);

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

                if(updateExtremePoints(child,utopianPoint,idealPoint,nadirPoint,referencePoint)) {
                    if(problem.getNumberOfObjectives() == 1)
                        updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
                    else
                        updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
                }

                ConeSubRegion subRegion = locateConeSubRegion(child,utopianPoint,normIntercepts);
                boolean isUpdated = coneUpdate(child, subRegion,utopianPoint,normIntercepts);

                collectForAdaptiveCrossover(isUpdated);
            }

            gen++;
            updateAdaptiveCrossover();

            initializeNadirPoint(population,nadirPoint);
            if(problem.getNumberOfObjectives() == 1){
                updateNormIntercepts(normIntercepts,utopianPoint,nadirPoint);
            }else {
                if(gen%updateInterval==0)
                    updateIntercepts(population, intercepts, utopianPoint, nadirPoint);
                updateNormIntercepts(normIntercepts, utopianPoint, intercepts);
            }
//            associateSubRegion(population,utopianPoint,normIntercepts);

//            JMetalLogger.logger.info("\nI: "+idealPoint[0]+"\t N: "+nadirPoint[0]+"\n");
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

}
