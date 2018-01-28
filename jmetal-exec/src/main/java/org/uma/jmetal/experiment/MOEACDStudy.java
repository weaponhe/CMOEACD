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
import org.uma.jmetal.problem.multiobjective.cdtlz.*;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
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
import java.util.*;

public class MOEACDStudy {
    private static String experimentBaseDirectory = "jmetal-data";
    private static final int INDEPENDENT_RUNS = 5;

    private static Integer[] objectiveNumberConfig;
    private static Integer[] paretoPointCoontConfig;
    private static int[][] numOfDivisionConfig;
    private static double[][] integratedTausConfig;
    private static int[] populationSizeConfig;
    private static Map<String, int[]> maxGenConfig;

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
        new ComputeQualityIndicators<>(experiment).run();
//        computeRuningTime(algorithmList, experiment.getExperimentBaseDirectory());
    }

    static public void initConfiguration() {
        objectiveNumberConfig = new Integer[]{
                3,
                5,
                8,
                10,
                15
        };

        paretoPointCoontConfig = new Integer[]{
                91,
                210,
                156,
                275,
                135
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
            referenceFrontFileNames.add(String.format("DTLZ1.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
        }
        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new C1_DTLZ3(objectiveNumberConfig[i] + 9, objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("DTLZ3.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
        }

        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new C2_DTLZ2(objectiveNumberConfig[i] + 9, objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C2_DTLZ2.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
        }
        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new ConvexC2_DTLZ2(objectiveNumberConfig[i] + 9, objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C2_Convex_DTLZ2.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
        }

        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new C3_DTLZ1(objectiveNumberConfig[i] + 4, objectiveNumberConfig[i], objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C3_DTLZ1.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
        }
        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new C3_DTLZ4(objectiveNumberConfig[i] + 4, objectiveNumberConfig[i], objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C3_DTLZ4.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
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

////        CMOEACDMeasure
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
//                        .setDelta(new double[]{0.81, 0.09, 0.09, 0.01})
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "CMOEACDMeasure", problemList.get(i), run));
//            }
//        }


//        C-MOEAD
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int configIndex = objectiveNumberList.indexOf(problemList.get(i).getNumberOfObjectives());
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName)[configIndex];
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEADBuilder(problemList.get(i), MOEADBuilder.Variant.ConstraintMOEAD))
//                        .setNumofDivision(numOfDivisionConfig[configIndex])
//                        .setIntegratedTau(integratedTausConfig[configIndex])
//                        .setPopulationSize(populationSizeConfig[configIndex])
//                        .setMaxGen(maxGen)
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEAD", problemList.get(i), run));
//            }
//        }
//
//        CMOEACD2Measure-PBI
        for (int run = 0; run < independentRuns; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                int configIndex = objectiveNumberList.indexOf(problemList.get(i).getNumberOfObjectives());
                String problemName = problemList.get(i).getName();
                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                maxGen=1000
                int maxGen = maxGenConfig.get(problemSeriesName)[configIndex];
                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACD2Measure))
                        .setNumOfDivision(numOfDivisionConfig[configIndex])
                        .setIntegratedTaus(integratedTausConfig[configIndex])
                        .setPopulationSize(populationSizeConfig[configIndex])
                        //设为1之后的启示：确定是交叉生成的个体太差？选择时增加竞标赛？更新和选择的目标层挂钩？
//                        选择的影响非常大？感觉影响不大。
//                        感觉更新的影响更大
//                        更新优先更新首层？对头！！！！！
                        .setConstraintLayerSize(5)
                        .setMaxGen(maxGen)
                        .setFunctionType(AbstractMOEAD.FunctionType.PBI)
                        .setDelta(new double[]{0.81, 0.09, 0.09, 0.01})
                        .build();
                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEACD(PBI)", problemList.get(i), run));
            }
        }
//
//        //        CMOEACD2Measure-TCH
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int configIndex = objectiveNumberList.indexOf(problemList.get(i).getNumberOfObjectives());
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName)[configIndex];
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACD2Measure))
//                        .setNumOfDivision(numOfDivisionConfig[configIndex])
//                        .setIntegratedTaus(integratedTausConfig[configIndex])
//                        .setPopulationSize(populationSizeConfig[configIndex])
//                        .setConstraintLayerSize(5)
//                        .setMaxGen(maxGen)
//                        .setFunctionType(AbstractMOEAD.FunctionType.TCH)
//                        .setDelta(new double[]{0.81, 0.09, 0.09, 0.01})
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEACD(TCH)", problemList.get(i), run));
//            }
//        }
//
//        //        CMOEACD2Measure-LP2
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int configIndex = objectiveNumberList.indexOf(problemList.get(i).getNumberOfObjectives());
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName)[configIndex];
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACD2Measure))
//                        .setNumOfDivision(numOfDivisionConfig[configIndex])
//                        .setIntegratedTaus(integratedTausConfig[configIndex])
//                        .setPopulationSize(populationSizeConfig[configIndex])
//                        .setConstraintLayerSize(5)
//                        .setMaxGen(maxGen)
//                        .setFunctionType(AbstractMOEAD.FunctionType.LP)
//                        .setDelta(new double[]{0.81, 0.09, 0.09, 0.01})
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEACD(LP2)", problemList.get(i), run));
//            }
//        }
//
////        NSGAIIIMeasure
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
//
////        CMOEACD-CDP
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int configIndex = objectiveNumberList.indexOf(problemList.get(i).getNumberOfObjectives());
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName)[configIndex];
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACD_CDP))
//                        .setNumOfDivision(numOfDivisionConfig[configIndex])
//                        .setIntegratedTaus(integratedTausConfig[configIndex])
//                        .setPopulationSize(populationSizeConfig[configIndex])
//                        .setMaxGen(maxGen)
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEACD-CDP", problemList.get(i), run));
//            }
//        }
//
////        C-MOEACD-SR
//        for (int run = 0; run < independentRuns; run++) {
//            for (int i = 0; i < problemList.size(); i++) {
//                int configIndex = objectiveNumberList.indexOf(problemList.get(i).getNumberOfObjectives());
//                String problemName = problemList.get(i).getName();
//                String problemSeriesName = problemName.substring(0, problemName.lastIndexOf("_"));
//                int maxGen = maxGenConfig.get(problemSeriesName)[configIndex];
//                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACD_SR))
//                        .setNumOfDivision(numOfDivisionConfig[configIndex])
//                        .setIntegratedTaus(integratedTausConfig[configIndex])
//                        .setPopulationSize(populationSizeConfig[configIndex])
//                        .setMaxGen(maxGen)
//                        .build();
//                algorithms.add(new TaggedAlgorithm<>(algorithm, "C-MOEACD-SR", problemList.get(i), run));
//            }
//        }


////        CMOEADD
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

        return algorithms;
    }
}
