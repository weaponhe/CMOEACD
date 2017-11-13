package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.impl.ViolationThresholdComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/9/4.
 */
public class CMOEACDN extends CMOEACD{
    public CMOEACDN(Problem<DoubleSolution> problem,
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
    }

    public CMOEACDN(Measurable measureManager, Problem<DoubleSolution> problem,
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
    }

    //use for normalization procedure for multi-, many-objective optimization
    protected void updateIntercepts(List<DoubleSolution>population,double[] intercepts, double[] utopianPoint,double[] nadirPoint) {

//        normalization procedure in multi-, many-objective optimization
        List<List<Double>> extremePoints = findExtremePoints(population, utopianPoint);
        constructHyperplaneAndUpdateIntercepts(intercepts, extremePoints, utopianPoint, nadirPoint);

    }

    public void updateNormIntercepts(double[] normIntercepts , double[] utopianPoint, double[] intercepts) {

//        for (int i = 0; i < problem.getNumberOfObjectives(); ++i) {
//            normIntercepts[i] = 1.0;
//        }
        //normalization procedure in multi-, many-objective optimization
        for (int i = 0; i < problem.getNumberOfObjectives(); ++i) {
            normIntercepts[i] = intercepts[i] - utopianPoint[i];
            if (normIntercepts[i] < Constant.TOLERATION)
                normIntercepts[i] = Constant.TOLERATION;
        }

    }


    @Override public String getName() {
        return "C-MOEA/CD-N" ;
    }

    @Override public String getDescription() {
        return "MOEA/CD with constraints handling and normalization procedure" ;
    }

}