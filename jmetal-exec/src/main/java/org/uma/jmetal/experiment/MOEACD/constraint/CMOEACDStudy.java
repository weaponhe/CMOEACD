package org.uma.jmetal.experiment.MOEACD.constraint;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moeacd.AbstractMOEACD;
import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADCDMeasure;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.DurationMeasure;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.solution.DoubleSolution;
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
 * Created by weaponhe on 2018/1/19.
 */
public class CMOEACDStudy {
    static public void excute() throws IOException {
        Configurator config = new Configurator();
        String experimentBaseDirectory = config.getExperimentBaseDirectory();
        List<String> referenceFrontFileNames = config.getReferenceFrontFileNames();
        List<Problem<DoubleSolution>> problemList = config.getProblemList();
        int INDEPENDENT_RUNS = config.getIndependentRuns();
        Integer[] objectiveNumberConfig = config.getObjectiveNumberConfig();
        int[][] numOfDivisionConfig = config.getNumOfDivisionConfig();
        double[][] integratedTausConfig = config.getIntegratedTausConfig();
        int[] populationSizeConfig = config.getPopulationSizeConfig();
        Map<String, int[]> maxGenConfig = config.getMaxGenConfig();
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
        computeRuningTime(algorithmList, experiment.getExperimentBaseDirectory());
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
                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.CMOEACD2Measure))
                        .setNumOfDivision(numOfDivisionConfig[configIndex])
                        .setIntegratedTaus(integratedTausConfig[configIndex])
                        .setPopulationSize(populationSizeConfig[configIndex])
                        .setConstraintLayerSize(5)
                        .setMaxGen(maxGen)
                        .setDelta(new double[]{0.81, 0.09, 0.09, 0.01})
                        .build();
                algorithms.add(new TaggedAlgorithm<>(algorithm, "CMOEACD2Measure", problemList.get(i), run));
            }
        }
        return algorithms;
    }

    static void computeRuningTime(List<TaggedAlgorithm<List<DoubleSolution>>> algorithmList, String experimentBaseDirectory) {
        Map<String, List<Long>> runingTimeMap = new HashMap<>();
        for (int i = 0; i < algorithmList.size(); i++) {
            TaggedAlgorithm taggedAlgorithm = algorithmList.get(i);
            Problem problem = taggedAlgorithm.getProblem();
            String algorithmName = taggedAlgorithm.getTag();
            String problemName = problem.getName();
            Algorithm algorithm = taggedAlgorithm.getAlgorithm();
            MeasureManager measureManager = ((AbstractMOEACD) algorithm).getMeasureManager();
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
}
