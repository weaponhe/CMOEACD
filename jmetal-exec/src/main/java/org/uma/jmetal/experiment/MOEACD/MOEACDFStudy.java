package org.uma.jmetal.experiment.MOEACD;

import org.uma.jmetal.algorithm.multiobjective.moeacd.AbstractMOEACD;
import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;
import org.uma.jmetal.experiment.IndicatorsListener;
import org.uma.jmetal.experiment.MyExperimentAnalysis;
import org.uma.jmetal.experiment.MyExperimentIndicator;
import org.uma.jmetal.experiment.MyExperimentIndicatorConfig;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.measure.impl.DurationMeasure;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolution;
import org.uma.jmetal.solution.util.RepairDoubleSolutionAtBounds;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by X250 on 2016/11/28.
 */
public class MOEACDFStudy extends AbstractMOEACDStudy{
    public MOEACDFStudy() {
        super("MOEACD-F");
        this.algType = MOEACDBuilder.Variant.MOEACDF;
        this.measureAlgType = MOEACDBuilder.Variant.MOEACDFMeasure;
    }
}
