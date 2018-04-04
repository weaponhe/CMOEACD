package org.uma.jmetal.experiment;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moeacd.*;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.ConstraintMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADCDMeasure;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIII;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.DurationMeasure;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.*;
import org.uma.jmetal.problem.multiobjective.cdtlz.*;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.QuickHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.CombinationUtils;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class MOEACDStudy {
    private static String experimentBaseDirectory = "../jmetal-data";
    private static final int INDEPENDENT_RUNS = 5;

    private static Integer[] objectiveNumberConfig;
    private static Map<Integer, Integer> paretoPointCoontConfig;
    private static Map<Integer, int[]> numOfDivisionConfig;
    private static Map<Integer, double[]> integratedTausConfig;
    private static Map<Integer, Integer> populationSizeConfig;
    private static Map<String, Map<Integer, Integer>> maxGenConfig;

    private static List<Problem<DoubleSolution>> problemList;
    private static List<String> referenceFrontFileNames = new ArrayList<>();

    static void computeRuningTime(List<TaggedAlgorithm<List<DoubleSolution>>> algorithmList, String experimentBaseDirectory) {
        Map<String, List<Long>> runingTimeMap = new HashMap<>();
        for (int i = 0; i < algorithmList.size(); i++) {
            TaggedAlgorithm taggedAlgorithm = algorithmList.get(i);
            Problem problem = taggedAlgorithm.getProblem();
            String algorithmName = taggedAlgorithm.getTag();
            String problemName = problem.getName();
            Algorithm algorithm = taggedAlgorithm.getAlgorithm();
            MeasureManager measureManager = ((Measurable) algorithm).getMeasureManager();
            DurationMeasure currentComputingTime =
                    (DurationMeasure) measureManager.<Long>getPullMeasure("currentExecutionTime");
            String outputFilePath = String.format("%s/data/%s/%s/Time",
                    experimentBaseDirectory, algorithmName, problemName);
            if (!runingTimeMap.containsKey(outputFilePath)) {
                runingTimeMap.put(outputFilePath, new ArrayList<Long>());
            }
            runingTimeMap.get(outputFilePath).add(currentComputingTime.get());
        }

        Iterator iter = runingTimeMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String path = (String) entry.getKey();
            List<Long> runingTimes = (List<Long>) entry.getValue();
            resetFile(path);
            for (int i = 0; i < runingTimes.size(); i++) {
                writeQualityIndicatorValueToFile(runingTimes.get(i), path);
            }
        }

    }

    static public void resetFile(String file) {
        File f = new File(file);
        if (f.exists()) {
            JMetalLogger.logger.info("File " + file + " exist.");

            if (f.isDirectory()) {
                JMetalLogger.logger.info("File " + file + " is a directory. Deleting directory.");
                if (f.delete()) {
                    JMetalLogger.logger.info("Directory successfully deleted.");
                } else {
                    JMetalLogger.logger.info("Error deleting directory.");
                }
            } else {
                JMetalLogger.logger.info("File " + file + " is a file. Deleting file.");
                if (f.delete()) {
                    JMetalLogger.logger.info("File succesfully deleted.");
                } else {
                    JMetalLogger.logger.info("Error deleting file.");
                }
            }
        } else {
            JMetalLogger.logger.info("File " + file + " does NOT exist.");
        }
    }

    static public void writeQualityIndicatorValueToFile(Long indicatorValue, String qualityIndicatorFile) {
        FileWriter os;
        try {
            os = new FileWriter(qualityIndicatorFile, true);
            os.write("" + indicatorValue + "\n");
            os.close();
        } catch (IOException ex) {
            throw new JMetalException("Error writing indicator file" + ex);
        }
    }


    public static void main(String[] args) throws IOException {
        initConfiguration();
//        initProblems();
//        List<TaggedAlgorithm<List<DoubleSolution>>> algorithmList = configureAlgorithmList(
//                problemList,
//                INDEPENDENT_RUNS,
//                objectiveNumberConfig,
//                numOfDivisionConfig,
//                integratedTausConfig,
//                populationSizeConfig,
//                maxGenConfig
//        );
        initPracticalProblems();
        List<TaggedAlgorithm<List<DoubleSolution>>> algorithmList = configurePracticalAlgorithmList(
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
                                new InvertedGenerationalDistancePlus<DoubleSolution>(),
                                new QuickHypervolume<DoubleSolution>()
                        ))
                        .setIndependentRuns(INDEPENDENT_RUNS)
                        .setNumberOfCores(8)
                        .build();

        long startTime = System.currentTimeMillis();
        new ExecuteAlgorithms<>(experiment).run();
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) / 1000 + "s");
        new ComputeQualityIndicators<>(experiment).run();
        computeRuningTime(algorithmList, experiment.getExperimentBaseDirectory());
    }

    static public void initConfiguration() {
        objectiveNumberConfig = new Integer[]{
//                2,
                3,
                4,
                5,
                8,
                10,
                15
        };

        paretoPointCoontConfig = new HashMap<>();
        paretoPointCoontConfig.put(2, 200);
        paretoPointCoontConfig.put(3, 91);
        paretoPointCoontConfig.put(4, 165);
        paretoPointCoontConfig.put(5, 210);
        paretoPointCoontConfig.put(8, 156);
        paretoPointCoontConfig.put(10, 275);
        paretoPointCoontConfig.put(15, 135);


        numOfDivisionConfig = new HashMap<>();
        numOfDivisionConfig.put(2, new int[]{199});
        numOfDivisionConfig.put(3, new int[]{12});
        numOfDivisionConfig.put(4, new int[]{8});
        numOfDivisionConfig.put(5, new int[]{6});
        numOfDivisionConfig.put(8, new int[]{3, 2});
        numOfDivisionConfig.put(10, new int[]{3, 2});
        numOfDivisionConfig.put(15, new int[]{2, 1});

        integratedTausConfig = new HashMap<>();
        integratedTausConfig.put(2, new double[]{1.0});
        integratedTausConfig.put(3, new double[]{1.0});
        integratedTausConfig.put(4, new double[]{1.0});
        integratedTausConfig.put(5, new double[]{1.0});
        integratedTausConfig.put(8, new double[]{1.0, 0.5});
        integratedTausConfig.put(10, new double[]{1.0, 0.5});
        integratedTausConfig.put(15, new double[]{1.0, 0.5});

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

        maxGenConfig.put("C1_DTLZ1", new HashMap<Integer, Integer>());
        maxGenConfig.get("C1_DTLZ1").put(3, 500);
        maxGenConfig.get("C1_DTLZ1").put(5, 600);
        maxGenConfig.get("C1_DTLZ1").put(8, 800);
        maxGenConfig.get("C1_DTLZ1").put(10, 1000);
        maxGenConfig.get("C1_DTLZ1").put(15, 1500);
        maxGenConfig.put("C1_DTLZ3", new HashMap<Integer, Integer>());
        maxGenConfig.get("C1_DTLZ3").put(3, 1000);
        maxGenConfig.get("C1_DTLZ3").put(5, 1500);
        maxGenConfig.get("C1_DTLZ3").put(8, 2500);
        maxGenConfig.get("C1_DTLZ3").put(10, 3500);
        maxGenConfig.get("C1_DTLZ3").put(15, 5000);
        maxGenConfig.put("C2_DTLZ2", new HashMap<Integer, Integer>());
        maxGenConfig.get("C2_DTLZ2").put(3, 250);
        maxGenConfig.get("C2_DTLZ2").put(5, 350);
        maxGenConfig.get("C2_DTLZ2").put(8, 500);
        maxGenConfig.get("C2_DTLZ2").put(10, 750);
        maxGenConfig.get("C2_DTLZ2").put(15, 1000);
        maxGenConfig.put("ConvexC2_DTLZ2", new HashMap<Integer, Integer>());
        maxGenConfig.get("ConvexC2_DTLZ2").put(3, 250);
        maxGenConfig.get("ConvexC2_DTLZ2").put(5, 750);
        maxGenConfig.get("ConvexC2_DTLZ2").put(8, 1500);
        maxGenConfig.get("ConvexC2_DTLZ2").put(10, 2500);
        maxGenConfig.get("ConvexC2_DTLZ2").put(15, 3500);
        maxGenConfig.put("C3_DTLZ1", new HashMap<Integer, Integer>());
        maxGenConfig.get("C3_DTLZ1").put(3, 750);
        maxGenConfig.get("C3_DTLZ1").put(5, 1250);
        maxGenConfig.get("C3_DTLZ1").put(8, 2000);
        maxGenConfig.get("C3_DTLZ1").put(10, 3000);
        maxGenConfig.get("C3_DTLZ1").put(15, 4000);
        maxGenConfig.put("C3_DTLZ4", new HashMap<Integer, Integer>());
        maxGenConfig.get("C3_DTLZ4").put(3, 750);
        maxGenConfig.get("C3_DTLZ4").put(5, 1250);
        maxGenConfig.get("C3_DTLZ4").put(8, 2000);
        maxGenConfig.get("C3_DTLZ4").put(10, 3000);
        maxGenConfig.get("C3_DTLZ4").put(15, 4000);

        maxGenConfig.put("Water", new HashMap<Integer, Integer>());
        maxGenConfig.get("Water").put(5, 500);
//        maxGenConfig.put("Golinski", new HashMap<Integer, Integer>());
//        maxGenConfig.get("Golinski").put(2, 1000);
//        maxGenConfig.put("Binh2", new HashMap<Integer, Integer>());
//        maxGenConfig.get("Binh2").put(2, 1000);
//        maxGenConfig.put("Tanaka", new HashMap<Integer, Integer>());
//        maxGenConfig.get("Tanaka").put(2, 1000);
//        maxGenConfig.put("Srinivas", new HashMap<Integer, Integer>());
//        maxGenConfig.get("Srinivas").put(2, 1000);
//        maxGenConfig.put("Osyczka2", new HashMap<Integer, Integer>());
//        maxGenConfig.get("Osyczka2").put(2, 1000);
//        maxGenConfig.put("CarSideImpact", new HashMap<Integer, Integer>());
//        maxGenConfig.get("CarSideImpact").put(3, 1000);
//        maxGenConfig.put("ThreeTruss", new HashMap<Integer, Integer>());
//        maxGenConfig.get("ThreeTruss").put(2, 1000);
//        maxGenConfig.get("ThreeTruss").put(3, 1000);

//        maxGenConfig.put("CarSideImpact", new HashMap<Integer, Integer>());
//        maxGenConfig.get("CarSideImpact").put(3, 1000);
        maxGenConfig.put("Machining", new HashMap<Integer, Integer>());
        maxGenConfig.get("Machining").put(4, 750);
    }

    static public void initPracticalProblems() {
        problemList = new ArrayList<>();
//        referenceFrontFileNames = new ArrayList<>();
//        problemList.add(new Water());
//        referenceFrontFileNames.add("Water.pf");
//        problemList.add(new Golinski());
//        referenceFrontFileNames.add("Golinski.pf");
//        problemList.add(new Binh2());
//        referenceFrontFileNames.add("Binh2.pf");
//        problemList.add(new Tanaka());
//        referenceFrontFileNames.add("Tanaka.pf");
//        problemList.add(new Srinivas());
//        referenceFrontFileNames.add("Srinivas.pf");
//        problemList.add(new Osyczka2());
//        referenceFrontFileNames.add("Osyczka2.pf");
//        problemList.add(new CarSideImpact());
//        referenceFrontFileNames.add("Osyczka2.pf");
//        problemList.add(new ThreeTruss());
//        referenceFrontFileNames.add("ThreeTruss");
//        problemList.add(new CarSideImpact());
//        referenceFrontFileNames.add("CarSideImpact");
        problemList.add(new Machining());
        referenceFrontFileNames.add("Machining");
    }

    static public void initProblems() {
        problemList = new ArrayList<>();
        referenceFrontFileNames = new ArrayList<>();


        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            int n = objectiveNumberConfig[i];
            Problem p = new C1_DTLZ1(n + 4, n);
            p.setName(String.format("%s_%dD", p.getName(), n));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("DTLZ1.%dD.pf[%d]", n, paretoPointCoontConfig.get(n)));
        }
        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            int n = objectiveNumberConfig[i];
            Problem p = new C1_DTLZ3(n + 9, n);
            p.setName(String.format("%s_%dD", p.getName(), n));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("DTLZ3.%dD.pf[%d]", n, paretoPointCoontConfig.get(n)));
        }

        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            int n = objectiveNumberConfig[i];
            Problem p = new C2_DTLZ2(n + 9, n);
            p.setName(String.format("%s_%dD", p.getName(), n));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C2_DTLZ2.%dD.pf[%d]", n, paretoPointCoontConfig.get(n)));
        }
        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            int n = objectiveNumberConfig[i];
            Problem p = new ConvexC2_DTLZ2(n + 9, n);
            p.setName(String.format("%s_%dD", p.getName(), n));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C2_Convex_DTLZ2.%dD.pf[%d]", n, paretoPointCoontConfig.get(n)));
        }

        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            int n = objectiveNumberConfig[i];
            Problem p = new C3_DTLZ1(n + 4, n, n);
            p.setName(String.format("%s_%dD", p.getName(), n));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C3_DTLZ1.%dD.pf[%d]", n, paretoPointCoontConfig.get(n)));
        }
        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            int n = objectiveNumberConfig[i];
            Problem p = new C3_DTLZ4(n + 4, n, n);
            p.setName(String.format("%s_%dD", p.getName(), n));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C3_DTLZ4.%dD.pf[%d]", n, paretoPointCoontConfig.get(n)));
        }
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
                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
                int maxGen = maxGenConfig.get(problemSeriesName).get(nObj);
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
                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
                int maxGen = maxGenConfig.get(problemSeriesName).get(nObj);
                Algorithm<List<DoubleSolution>> algorithm = (new MOEADBuilder(problemList.get(i), MOEADBuilder.Variant.CMOEADSR))
                        .setNumofDivision(numOfDivisionConfig.get(nObj))
                        .setIntegratedTau(integratedTausConfig.get(nObj))
                        .setPopulationSize(populationSizeConfig.get(nObj))
                        .setMaxGen(maxGen)
                        .build();
                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEAD-SR", problemList.get(i), run));
            }
        }
////      C-MOEAD
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int nObj = problemList.get(i).getNumberOfObjectives();
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName).get(nObj);
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEADBuilder(problemList.get(i), MOEADBuilder.Variant.ConstraintMOEAD))
//                        .setNumofDivision(numOfDivisionConfig.get(nObj))
//                        .setIntegratedTau(integratedTausConfig.get(nObj))
//                        .setPopulationSize(populationSizeConfig.get(nObj))
//                        .setMaxGen(maxGen)
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEAD-ACV", problemList.get(i), run));
//            }
//        }

////      C-MOEACD
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int configIndex = objectiveNumberList.indexOf(problemList.get(i).getNumberOfObjectives());
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName)[configIndex];
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACDMeasure))
//                        .setNumOfDivision(numOfDivisionConfig[configIndex])
//                        .setIntegratedTaus(integratedTausConfig[configIndex])
//                        .setPopulationSize(populationSizeConfig[configIndex])
//                        .setConstraintLayerSize(5)
//                        .setMaxGen(maxGen)
//                        .setFunctionType(AbstractMOEAD.FunctionType.PBI)
//                        .setDelta(new double[]{0.81, 0.09, 0.09, 0.01})
//                        .setHeadOrTailRatio(0.8)
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEACD", problemList.get(i), run));
//            }
//        }
//
////      C-NSGAIII
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int configIndex = objectiveNumberList.indexOf(problemList.get(i).getNumberOfObjectives());
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName)[configIndex];
//
//                double crossoverProbability = 1.0;
//                double crossoverDistributionIndex = 30.0;
//                SBXCrossover sbxCrossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);
//
//                double mutationProbability = 1.0 / problemList.get(i).getNumberOfVariables();
//                double mutationDistributionIndex = 20.0;
//                MutationOperator<DoubleSolution> mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);
//
//                SelectionOperator<List<DoubleSolution>, DoubleSolution> selection = new BinaryTournamentSelection<>();
//
//                Algorithm<List<DoubleSolution>> algorithm = new NSGAIIIBuilder<>(problemList.get(i), NSGAIIIBuilder.Variant.NSGAIIIMeasure)
//                        .setCrossoverOperator(sbxCrossover)
//                        .setMutationOperator(mutation)
//                        .setSelectionOperator(selection)
//                        .setMaxIterations(maxGen)
//                        .setNumberOfDivisions(numOfDivisionConfig[configIndex])
//                        .setIntergratedTaus(integratedTausConfig[configIndex])
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-NSGAIII", problemList.get(i), run));
//            }
//        }
//
//        //C-MOEADD
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
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEADD", problemList.get(i), run));
//            }
//        }

////        CMOEACD-CDP
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int nObj = problemList.get(i).getNumberOfObjectives();
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName).get(nObj);
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACD_CDP))
//                        .setNumOfDivision(numOfDivisionConfig.get(nObj))
//                        .setIntegratedTaus(integratedTausConfig.get(nObj))
//                        .setPopulationSize(populationSizeConfig.get(nObj))
//                        .setMaxGen(maxGen)
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEACD-CDP", problemList.get(i), run));
//            }
//        }
//        //        CMOEACD-ADP
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int nObj = problemList.get(i).getNumberOfObjectives();
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName).get(nObj);
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACD_ADP))
//                        .setNumOfDivision(numOfDivisionConfig.get(nObj))
//                        .setIntegratedTaus(integratedTausConfig.get(nObj))
//                        .setPopulationSize(populationSizeConfig.get(nObj))
//                        .setMaxGen(maxGen)
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEACD-ADP", problemList.get(i), run));
//            }
//        }
////
////        C-MOEACD-SR
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int nObj = problemList.get(i).getNumberOfObjectives();
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName).get(nObj);
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACD_SR))
//                        .setNumOfDivision(numOfDivisionConfig.get(nObj))
//                        .setIntegratedTaus(integratedTausConfig.get(nObj))
//                        .setPopulationSize(populationSizeConfig.get(nObj))
//                        .setMaxGen(maxGen)
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEACD-SR", problemList.get(i), run));
//            }
//        }
//        //        C-MOEACD-ASR
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int nObj = problemList.get(i).getNumberOfObjectives();
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName).get(nObj);
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACD_ASR))
//                        .setNumOfDivision(numOfDivisionConfig.get(nObj))
//                        .setIntegratedTaus(integratedTausConfig.get(nObj))
//                        .setPopulationSize(populationSizeConfig.get(nObj))
//                        .setMaxGen(maxGen)
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEACD-ASR", problemList.get(i), run));
//            }
//        }

        return algorithms;
    }


    static List<TaggedAlgorithm<List<DoubleSolution>>> configurePracticalAlgorithmList(List<Problem<DoubleSolution>> problemList,
                                                                                       int independentRuns,
                                                                                       Integer[] objectiveNumberConfig,
                                                                                       Map<Integer, int[]> numOfDivisionConfig,
                                                                                       Map<Integer, double[]> integratedTausConfig,
                                                                                       Map<Integer, Integer> populationSizeConfig,
                                                                                       Map<String, Map<Integer, Integer>> maxGenConfig) {

        List<TaggedAlgorithm<List<DoubleSolution>>> algorithms = new ArrayList<>();
        List<Integer> objectiveNumberList = Arrays.asList(objectiveNumberConfig);

        //        C-MOEAD
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

        //        C-MOEAD
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

        //        C-MOEAD
        for (int run = 0; run < independentRuns; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                int nObj = problemList.get(i).getNumberOfObjectives();
                String problemName = problemList.get(i).getName();
                int maxGen = maxGenConfig.get(problemName).get(nObj);
                Algorithm<List<DoubleSolution>> algorithm = (new MOEADBuilder(problemList.get(i), MOEADBuilder.Variant.ConstraintMOEAD))
                        .setNumofDivision(numOfDivisionConfig.get(nObj))
                        .setIntegratedTau(integratedTausConfig.get(nObj))
                        .setPopulationSize(populationSizeConfig.get(nObj))
                        .setMaxGen(maxGen)
                        .build();
                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEAD-ACV", problemList.get(i), run));
            }
        }




        for (int run = 0; run < independentRuns; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                int nObj = problemList.get(i).getNumberOfObjectives();
                String problemName = problemList.get(i).getName();
                int maxGen = maxGenConfig.get(problemName).get(nObj);
                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACDMeasure))
                        .setNumOfDivision(numOfDivisionConfig.get(nObj))
                        .setIntegratedTaus(integratedTausConfig.get(nObj))
                        .setPopulationSize(populationSizeConfig.get(nObj))
                        .setConstraintLayerSize(5)
                        .setMaxGen(maxGen)
                        .setFunctionType(AbstractMOEAD.FunctionType.PBI)
                        .setDelta(new double[]{1, 0.09, 0.09, 0.01})
                        .setHeadOrTailRatio(0.5)
                        .build();
                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEACD", problemList.get(i), run));
            }
        }

        //NSGAIII
        for (int run = 0; run < independentRuns; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                int nObj = problemList.get(i).getNumberOfObjectives();
                String problemName = problemList.get(i).getName();
                int maxGen = maxGenConfig.get(problemName).get(nObj);
                double crossoverProbability = 1.0;
                double crossoverDistributionIndex = 30.0;
                SBXCrossover sbxCrossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

                double mutationProbability = 1.0 / problemList.get(i).getNumberOfVariables();
                double mutationDistributionIndex = 20.0;
                MutationOperator<DoubleSolution> mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

                SelectionOperator<List<DoubleSolution>, DoubleSolution> selection = new BinaryTournamentSelection<>();

                Algorithm<List<DoubleSolution>> algorithm = new NSGAIIIBuilder<>(problemList.get(i), NSGAIIIBuilder.Variant.NSGAIIIMeasure)
                        .setCrossoverOperator(sbxCrossover)
                        .setMutationOperator(mutation)
                        .setSelectionOperator(selection)
                        .setMaxIterations(maxGen)
                        .setNumberOfDivisions(numOfDivisionConfig.get(nObj))
                        .setIntergratedTaus(integratedTausConfig.get(nObj))
                        .build();
                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-NSGAIII", problemList.get(i), run));
            }
        }
        //        CMOEADD
        for (int run = 0; run < independentRuns; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                int nObj = problemList.get(i).getNumberOfObjectives();
                String problemName = problemList.get(i).getName();
                int maxGen = maxGenConfig.get(problemName).get(nObj);
                Algorithm<List<DoubleSolution>> algorithm = (new MOEADBuilder(problemList.get(i), MOEADBuilder.Variant.CMOEADD))
                        .setNumofDivision(numOfDivisionConfig.get(nObj))
                        .setIntegratedTau(integratedTausConfig.get(nObj))
                        .setPopulationSize(populationSizeConfig.get(nObj))
                        .setMaxGen(maxGen)
                        .build();
                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEADD", problemList.get(i), run));
            }
        }

        return algorithms;
    }
}
