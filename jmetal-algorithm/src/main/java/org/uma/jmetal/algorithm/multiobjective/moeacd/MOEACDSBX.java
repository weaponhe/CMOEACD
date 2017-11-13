package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/11/28.
 */
public class MOEACDSBX extends MOEACD {
    public MOEACDSBX(Problem<DoubleSolution> problem,
                   int[] arrayH,
                   double[] integratedTaus,
                   int populationSize,
                   int maxEvaluations,
                   int neighborhoodSize,
                   double neighborhoodSelectionProbability,
                   SBXCrossover sbxCrossoverOperator,
                   MutationOperator<DoubleSolution> mutation
    ) {
        super(problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,null, mutation);
    }

    public MOEACDSBX(Measurable measureManager,
                   Problem<DoubleSolution> problem,
                   int[] arrayH,
                   double[] integratedTaus,
                   int populationSize,
                   int maxEvaluations,
                   int neighborhoodSize,
                   double neighborhoodSelectionProbability,
                   SBXCrossover sbxCrossoverOperator,
                   MutationOperator<DoubleSolution> mutation) {
        super(measureManager,problem,arrayH, integratedTaus,
                populationSize,maxEvaluations,neighborhoodSize,
                neighborhoodSelectionProbability,
                sbxCrossoverOperator,null, mutation);
    }

    protected void chooseCrossoverType(){
        crossoverType = CrossoverType.SBX;
    }

    @Override public String getName() {
        return "MOEA/CD-SBX" ;
    }

    @Override public String getDescription() {
        return "MOEA/CD with SBX only" ;
    }
}
