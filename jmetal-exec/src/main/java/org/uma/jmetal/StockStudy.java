package org.uma.jmetal;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.DurationMeasure;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.StockInvestment;
import org.uma.jmetal.problem.multiobjective.cdtlz.*;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.CombinationUtils;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.ComputeQualityIndicators;
import org.uma.jmetal.util.experiment.component.ExecuteAlgorithms;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by weaponhe on 2018/3/28.
 */
public class StockStudy {
    private static String experimentBaseDirectory = "../jmetal-data";
    private static final int INDEPENDENT_RUNS = 1;

    private static Integer[] objectiveNumberConfig;
    private static Map<Integer, Integer> paretoPointCoontConfig;
    private static Map<Integer, int[]> numOfDivisionConfig;
    private static Map<Integer, double[]> integratedTausConfig;
    private static Map<Integer, Integer> populationSizeConfig;
    private static Map<String, Map<Integer, Integer>> maxGenConfig;

    private static List<Problem<DoubleSolution>> problemList;
    private static List<String> referenceFrontFileNames = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        initConfiguration();
        initProblems();
        List<TaggedAlgorithm<List<DoubleSolution>>> algorithmList = configureAlgorithmList(
                problemList,
                INDEPENDENT_RUNS,
                objectiveNumberConfig,
                numOfDivisionConfig,
                integratedTausConfig,
                populationSizeConfig,
                maxGenConfig
        );

        Experiment<DoubleSolution, List<DoubleSolution>> experiment =
                new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("MOEACDStudy")
                        .setAlgorithmList(algorithmList)
                        .setProblemList(problemList)
                        .setExperimentBaseDirectory(experimentBaseDirectory)
                        .setOutputParetoFrontFileName("FUN")
                        .setOutputParetoSetFileName("VAR")
                        //jmetal-core/src/main/resources/pareto_fronts
                        .setReferenceFrontDirectory("/pareto_fronts")
                        .setReferenceFrontFileNames(referenceFrontFileNames)
                        .setIndependentRuns(INDEPENDENT_RUNS)
                        .setNumberOfCores(8)
                        .build();
        new ExecuteAlgorithms<>(experiment).run();
    }

    static public void initConfiguration() {
        objectiveNumberConfig = new Integer[]{2};

        paretoPointCoontConfig = new HashMap<>();
        paretoPointCoontConfig.put(2, 200);

        numOfDivisionConfig = new HashMap<>();
        numOfDivisionConfig.put(2, new int[]{199});

        integratedTausConfig = new HashMap<>();
        integratedTausConfig.put(2, new double[]{1.0});

        populationSizeConfig = new HashMap<>();
        for (int j = 0; j < objectiveNumberConfig.length; j++) {
            int nObj = objectiveNumberConfig[j];
            int[] arrayH = numOfDivisionConfig.get(nObj);
            int nums = 0;
            for (int i = 0; i < arrayH.length; ++i)
                nums += CombinationUtils.compute(arrayH[i] + nObj - 1, nObj - 1);
            populationSizeConfig.put(nObj, nums);
        }

        maxGenConfig = new HashMap<>();
        maxGenConfig.put("StockInvestment", new HashMap<Integer, Integer>());
        maxGenConfig.get("StockInvestment").put(2, 500);

    }


    static public void initProblems() {
        problemList = new ArrayList<>();
        referenceFrontFileNames = new ArrayList<>();
        int stockCount = 9;
        int totalVolume = 500000;
        int[] stockPrices = new int[]{271,
                664,
                519,
                280,
                412,
                618,
                975,
                448,
                434};
        double k = 0.005;
        double[] expectedReturnRates = new double[]{0.351111111,
                0.715555556,
                0.121111111,
                0.206666667,
                0.493333333,
                0.204444444,
                0.273333333,
                0.104444444,
                0.046666667
        };

        double[][] returnVariances = new double[][]{
                {0.037, 0.008, -0.002, 0.001, 0.03, 0.01, 0.021, 0.004, 0},
                {0.008, 0.218, 0.07, 0.052, 0.051, 0.036, 0.029, 0.013, 0.001},
                {-0.002, 0.07, 0.034, 0.028, 0.017, 0.015, 0.012, 0.007, 0.001},
                {0.001, 0.052, 0.028, 0.028, 0.014, 0.014, 0.012, 0.006, 0.001},
                {0.03, 0.051, 0.017, 0.014, 0.038, 0.018, 0.026, 0.008, 0},
                {0.01, 0.036, 0.015, 0.014, 0.018, 0.012, 0.013, 0.005, 0},
                {0.021, 0.029, 0.012, 0.012, 0.026, 0.013, 0.019, 0.006, 0},
                {0.004, 0.013, 0.007, 0.006, 0.008, 0.005, 0.006, 0.003, 0},
                {0, 0.001, 0.001, 0.001, 0, 0, 0, 0, 0}
        };
        int[] initStocks = {
                35,
                50,
                0,
                30,
                10,
                35,
                65,
                0,
                70
        };

        Problem p = new StockInvestment(stockCount, totalVolume, stockPrices, initStocks, k, expectedReturnRates, returnVariances);
        problemList.add(p);

    }

    static List<TaggedAlgorithm<List<DoubleSolution>>> configureAlgorithmList(List<Problem<DoubleSolution>> problemList,
                                                                              int independentRuns,
                                                                              Integer[] objectiveNumberConfig,
                                                                              Map<Integer, int[]> numOfDivisionConfig,
                                                                              Map<Integer, double[]> integratedTausConfig,
                                                                              Map<Integer, Integer> populationSizeConfig,
                                                                              Map<String, Map<Integer, Integer>> maxGenConfig) {
        List<TaggedAlgorithm<List<DoubleSolution>>> algorithms = new ArrayList<>();
        List<Integer> objectiveNumberList = Arrays.asList(objectiveNumberConfig);

        //      C-MOEAD
        for (int run = 0; run < independentRuns; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                int nObj = problemList.get(i).getNumberOfObjectives();
                String problemName = problemList.get(i).getName();
                int maxGen = maxGenConfig.get(problemName).get(nObj);
                Algorithm<List<DoubleSolution>> algorithm = (new MOEADBuilder(problemList.get(i), MOEADBuilder.Variant.CMOEADCDP))
                        .setNumofDivision(numOfDivisionConfig.get(nObj))
                        .setIntegratedTau(integratedTausConfig.get(nObj))
                        .setPopulationSize(populationSizeConfig.get(nObj))
                        .setMaxGen(maxGen)
                        .build();
                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEAD-CDP", problemList.get(i), run));
            }
        }

        //      C-MOEAD
        for (int run = 0; run < independentRuns; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                int nObj = problemList.get(i).getNumberOfObjectives();
                String problemName = problemList.get(i).getName();
                int maxGen = maxGenConfig.get(problemName).get(nObj);
                Algorithm<List<DoubleSolution>> algorithm = (new MOEADBuilder(problemList.get(i), MOEADBuilder.Variant.CMOEADSR))
                        .setNumofDivision(numOfDivisionConfig.get(nObj))
                        .setIntegratedTau(integratedTausConfig.get(nObj))
                        .setPopulationSize(populationSizeConfig.get(nObj))
                        .setMaxGen(maxGen)
                        .build();
                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEAD-SR", problemList.get(i), run));
            }
        }


        return algorithms;
    }
}


