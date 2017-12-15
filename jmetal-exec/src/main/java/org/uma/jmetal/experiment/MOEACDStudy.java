package org.uma.jmetal.experiment;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;
import org.uma.jmetal.algorithm.multiobjective.moead.ConstraintMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIII;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.cdtlz.*;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.CombinationUtils;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;

import java.io.IOException;
import java.util.*;

public class MOEACDStudy {
    private static String experimentBaseDirectory = "jmetal-data";
    private static final int INDEPENDENT_RUNS = 5;

    private static Integer[] objectiveNumberConfig;
    private static int[][] numOfDivisionConfig;
    private static double[][] integratedTausConfig;
    private static int[] populationSizeConfig;
    private static Map<String, int[]> maxGenConfig;

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
                        .setIndicatorList(Arrays.asList(
                                new InvertedGenerationalDistance<DoubleSolution>(),
                                new InvertedGenerationalDistancePlus<DoubleSolution>())
                        )
                        .setIndependentRuns(INDEPENDENT_RUNS)
                        .setNumberOfCores(8)
                        .build();

        long startTime = System.currentTimeMillis();
        new ExecuteAlgorithms<>(experiment).run();
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) / 1000 + "s");
//        new ComputeQualityIndicators<>(experiment).run();
    }

    static public void initConfiguration() {
        objectiveNumberConfig = new Integer[]{
                3,
                5,
                8,
                10,
                15
        };
        numOfDivisionConfig = new int[][]{
                {12},
                {6},
                {3, 2},
                {3, 2},
                {2, 1}
        };
        integratedTausConfig = new double[][]{
                {1.0},
                {1.0},
                {1.0, 0.5},
                {1.0, 0.5},
                {1.0, 0.5}
        };
        populationSizeConfig = new int[objectiveNumberConfig.length];
        for (int j = 0; j < objectiveNumberConfig.length; j++) {
            int nObj = objectiveNumberConfig[j];
            int[] arrayH = numOfDivisionConfig[j];
            int nums = 0;
            for (int i = 0; i < arrayH.length; ++i)
                nums += CombinationUtils.compute(arrayH[i] + nObj - 1, nObj - 1);
            populationSizeConfig[j] = nums;
        }

        maxGenConfig = new HashMap<>();

        maxGenConfig.put("C1_DTLZ1", new int[]{500, 600, 800, 1000, 1500});
        maxGenConfig.put("C1_DTLZ3", new int[]{1000, 1500, 2500, 3500, 5000});
        maxGenConfig.put("C2_DTLZ2", new int[]{250, 350, 500, 750, 1000});
        maxGenConfig.put("ConvexC2_DTLZ2", new int[]{250, 750, 1500, 2500, 3500});
        maxGenConfig.put("C3_DTLZ1", new int[]{750, 1250, 2000, 3000, 4000});
        maxGenConfig.put("C3_DTLZ4", new int[]{750, 1250, 2000, 3000, 4000});
    }

    static public void initProblems() {
        problemList = new ArrayList<>();
        referenceFrontFileNames = new ArrayList<>();

        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new C1_DTLZ1(objectiveNumberConfig[i] + 4, objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("DTLZ1.%dD.pf", objectiveNumberConfig[i]));
        }
        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new C1_DTLZ3(objectiveNumberConfig[i] + 9, objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("DTLZ3.%dD.pf", objectiveNumberConfig[i]));
        }

        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new C2_DTLZ2(objectiveNumberConfig[i] + 9, objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C2_DTLZ2.%dD.pf", objectiveNumberConfig[i]));
        }
        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new ConvexC2_DTLZ2(objectiveNumberConfig[i] + 9, objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C2_Convex_DTLZ2.%dD.pf", objectiveNumberConfig[i]));
        }

        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new C3_DTLZ1(objectiveNumberConfig[i] + 4, objectiveNumberConfig[i], objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C3_DTLZ1.%dD.pf", objectiveNumberConfig[i]));
        }
        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new C3_DTLZ4(objectiveNumberConfig[i] + 4, objectiveNumberConfig[i], objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C3_DTLZ4.%dD.pf", objectiveNumberConfig[i]));
        }
    }


    static List<TaggedAlgorithm<List<DoubleSolution>>> configureAlgorithmList(List<Problem<DoubleSolution>> problemList,
                                                                              int independentRuns,
                                                                              Integer[] objectiveNumberConfig,
                                                                              int[][] numOfDivisionConfig,
                                                                              double[][] integratedTausConfig,
                                                                              int[] populationSizeConfig,
                                                                              Map<String, int[]> maxGenConfig) {
        List<TaggedAlgorithm<List<DoubleSolution>>> algorithms = new ArrayList<>();
        List<Integer> objectiveNumberList = Arrays.asList(objectiveNumberConfig);

        for (int run = 0; run < independentRuns; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                int configIndex = objectiveNumberList.indexOf(problemList.get(i).getNumberOfObjectives());
                String problemName = problemList.get(i).getName();
                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
                int maxGen = maxGenConfig.get(problemSeriesName)[configIndex];
                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.MOEACD))
                        .setNumOfDivision(numOfDivisionConfig[configIndex])
                        .setIntegratedTaus(integratedTausConfig[configIndex])
                        .setPopulationSize(populationSizeConfig[configIndex])
                        .setConstraintLayerSize(5)
                        .setMaxGen(maxGen)
                        .setDelta(new double[]{0.81, 0.09, 0.09, 0.01})
                        .build();
                algorithms.add(new TaggedAlgorithm<>(algorithm, "CMOEACD", problemList.get(i), run));
            }
        }

//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int configIndex = objectiveNumberList.indexOf(problemList.get(i).getNumberOfObjectives());
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName)[configIndex];
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEADBuilder(problemList.get(i), MOEADBuilder.Variant.CMOEADD))
//                        .setNumofDivision(numOfDivisionConfig[configIndex])
//                        .setIntegratedTau(integratedTausConfig[configIndex])
//                        .setPopulationSize(populationSizeConfig[configIndex])
//                        .setMaxGen(maxGen)
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "CMOEADD", problemList.get(i), run));
//            }
//        }

        for (int run = 0; run < independentRuns; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                int configIndex = objectiveNumberList.indexOf(problemList.get(i).getNumberOfObjectives());
                String problemName = problemList.get(i).getName();
                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
                int maxGen = maxGenConfig.get(problemSeriesName)[configIndex];
                Algorithm<List<DoubleSolution>> algorithm = (new MOEADBuilder(problemList.get(i), MOEADBuilder.Variant.ConstraintMOEAD))
                        .setNumofDivision(numOfDivisionConfig[configIndex])
                        .setIntegratedTau(integratedTausConfig[configIndex])
                        .setPopulationSize(populationSizeConfig[configIndex])
                        .setMaxGen(maxGen)
                        .build();
                algorithms.add(new TaggedAlgorithm<>(algorithm, "CMOEAD", problemList.get(i), run));
            }
        }
        return algorithms;
    }
}
