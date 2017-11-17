package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.multiobjective.moeacd.AbstractMOEACD;
import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;
import org.uma.jmetal.measure.MeasureListener;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by weaponhe on 2017/10/25.
 */
public class MOEACDRunner extends AbstractAlgorithmRunner {
    static public String referenceParetoFront;

    public static void main(String[] args) throws FileNotFoundException {
        DoubleProblem problem;
        AbstractMOEACD algorithm = null;
        MutationOperator<DoubleSolution> mutation;
        DifferentialEvolutionCrossover crossover;

        String problemName;
        referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/DTLZ1.3D.pf[91]";
        problemName = "org.uma.jmetal.problem.multiobjective.cdtlz.C1_DTLZ1";

        problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

        double cr = 1.0;
        double f = 0.5;
        crossover = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin");

        double mutationProbability = 1.0 / problem.getNumberOfVariables();
        double mutationDistributionIndex = 20.0;
        mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

        int[] numOfDivision = {12};
        double[] integratedTaus = {1.0};
        algorithm = (new MOEACDBuilder(problem, MOEACDBuilder.Variant.MOEACD))
                .setIntegratedTaus(integratedTaus)
                .setPopulationSize(91)
                .setConstraintLayerSize(5)
                .setNumOfDivision(numOfDivision)
                .build();

        MeasureManager measureManager = algorithm.getMeasureManager();
        BasicMeasure<List<DoubleSolution>> solutionListMeasure =
                (BasicMeasure<List<DoubleSolution>>) measureManager.<List<DoubleSolution>>getPushMeasure("currentPopulation");
        solutionListMeasure.register(new Listener());

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute();
        List<DoubleSolution> population = algorithm.getResult();
//        printFinalSolutionSet(population);
//        if (!referenceParetoFront.equals("")) {
//            printHypervolume(population, referenceParetoFront);
//        }
    }


    private static class Listener implements MeasureListener<List<DoubleSolution>> {
        private int counter = 0;

        @Override
        synchronized public void measureGenerated(List<DoubleSolution> solutions) {
            if ((counter % 10 == 0)) {
                if (solutions.size() > 0) {
                    try {
                        printHypervolume(solutions, referenceParetoFront);
                    } catch (Exception e) {
                    }
                }
            }
            counter++;
        }
    }

}
