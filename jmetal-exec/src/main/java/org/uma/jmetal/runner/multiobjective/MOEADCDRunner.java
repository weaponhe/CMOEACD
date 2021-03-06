package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by X250 on 2016/3/23.
 */
public class MOEADCDRunner extends AbstractAlgorithmRunner {
    /**
     * @param args Command line arguments.
     * @throws SecurityException
     * Invoking command:
     */
    public static void main(String[] args) throws FileNotFoundException {
        DoubleProblem problem;
        Algorithm<List<DoubleSolution>> algorithm;
        MutationOperator<DoubleSolution> mutation;
        DifferentialEvolutionCrossover crossover;

        String problemName ;
        String referenceParetoFront = "";
        if (args.length == 1) {
            problemName = args[0];
        } else if (args.length == 2) {
            problemName = args[0] ;
            referenceParetoFront = args[1] ;
        } else {
            problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1";//zdt.ZDT1";//lz09.LZ09F2";
            referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/DTLZ1.3D.pf";//ZDT1.pf";//LZ09_F2.pf";
        }

        problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);

        double cr = 1.0 ;
        double f = 0.5 ;
        crossover = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin");

        double mutationProbability = 1.0 / problem.getNumberOfVariables();
        double mutationDistributionIndex = 20.0;
        mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

        algorithm = new MOEADBuilder(problem, MOEADBuilder.Variant.MOEADCD)
                .setCrossover(crossover)
                .setMutation(mutation)
                .setMaxEvaluations(150000)
                .setPopulationSize(300)
                .setResultPopulationSize(300)
                .setNeighborhoodSelectionProbability(0.9)
                .setMaximumNumberOfReplacedSolutions(2)
                .setKIndex(5)
                .setNeighborSize(20)
                .setFunctionType(AbstractMOEAD.FunctionType.TCH)
                .setDataDirectory("MOEAD_Weights")
                .build() ;

        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
                .execute() ;

        List<DoubleSolution> population = algorithm.getResult() ;
        long computingTime = algorithmRunner.getComputingTime() ;

        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

        printFinalSolutionSet(population);
        if (!referenceParetoFront.equals("")) {
            printQualityIndicators(population, referenceParetoFront) ;
        }
    }
}