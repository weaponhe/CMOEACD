package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moeacd.AbstractMOEACD;
import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
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
    public static void main(String[] args) throws FileNotFoundException {
        DoubleProblem problem;
        Algorithm<List<DoubleSolution>> algorithm;
        MutationOperator<DoubleSolution> mutation;
        DifferentialEvolutionCrossover crossover;

        String problemName ;
//        String referenceParetoFront = "";
//        if (args.length == 1) {
//            problemName = args[0];
//        } else if (args.length == 2) {
//            problemName = args[0] ;
//            referenceParetoFront = args[1] ;
//        } else {
//        problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1";//zdt.ZDT1";//lz09.LZ09F2";
        problemName = "org.uma.jmetal.problem.multiobjective.cdtlz.C1_DTLZ1";
//        referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/DTLZ1.3D.pf";//ZDT1.pf";//LZ09_F2.pf";
//        }

        problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);

        double cr = 1.0 ;
        double f = 0.5 ;
        crossover = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin");

        double mutationProbability = 1.0 / problem.getNumberOfVariables();
        double mutationDistributionIndex = 20.0;
        mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

        int[] numOfDivision = {12};
        double[] integratedTaus = {1.0};
        algorithm = (new MOEACDBuilder(problem, MOEACDBuilder.Variant.MOEACD))
                .setIntegratedTaus(integratedTaus)
                .setPopulationSize(91)
//                .setConstraintLayerSize(5)
                .setNumOfDivision(numOfDivision)
                .build();

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute() ;
        List<DoubleSolution> population = algorithm.getResult() ;
        int fessibleCount = 0;
//        for (DoubleSolution ind : population) {
//            int numberOfConstrait = ind.getNumberOfConstraints();
//            double cv = 0;
//            for (int i = 0; i < numberOfConstrait; i++) {
//                cv += ind.getConstraintViolation(i);
//                if (cv > 0)
//            }
//        }
    }
}
