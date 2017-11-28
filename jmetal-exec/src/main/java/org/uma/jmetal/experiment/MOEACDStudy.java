package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDBuilder;
import org.uma.jmetal.algorithm.multiobjective.moead.ConstraintMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.cdtlz.*;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.CombinationUtils;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;

import java.io.IOException;
import java.util.*;

/**
 * Created by weaponhe on 2017/11/17.
 */
public class MOEACDStudy {
    private static final int INDEPENDENT_RUNS = 5;

    public static void main(String[] args) throws IOException {
        String experimentBaseDirectory = "jmetal-data";

        int[] objectiveNumber = {3, 5, 8, 10, 15};

        Map<Integer, int[]> numOfDivisionMap = new HashMap<>();
        numOfDivisionMap.put(3, new int[]{12});
        numOfDivisionMap.put(5, new int[]{6});
        numOfDivisionMap.put(8, new int[]{3, 2});
        numOfDivisionMap.put(10, new int[]{3, 2});
        numOfDivisionMap.put(15, new int[]{2, 1});
        Map<Integer, double[]> integratedTausMap = new HashMap<>();
        integratedTausMap.put(3, new double[]{1.0});
        integratedTausMap.put(5, new double[]{1.0});
        integratedTausMap.put(8, new double[]{1.0, 0.5});
        integratedTausMap.put(10, new double[]{1.0, 0.5});
        integratedTausMap.put(15, new double[]{1.0, 0.5});
        Map<Integer, Integer> populationSizeMap = new HashMap<>();
        for (int j = 0; j < objectiveNumber.length; j++) {
            int _nObj = objectiveNumber[j];
            int[] _arrayH = numOfDivisionMap.get(_nObj);
            int nums = 0;
            for (int i = 0; i < _arrayH.length; ++i)
                nums += CombinationUtils.compute(_arrayH[i] + _nObj - 1, _nObj - 1);
            populationSizeMap.put(_nObj, nums);
        }


        List<Problem<DoubleSolution>> problemList = new ArrayList<>();
        List<String> referenceFrontFileNames = new ArrayList<>();
//        for (int i = 0; i < objectiveNumber.length; i++) {
//            Problem p = new C1_DTLZ1(objectiveNumber[i] + 4, objectiveNumber[i]);
//            p.setName(String.format("%s_%dD", p.getName(), objectiveNumber[i]));
//            problemList.add(p);
//            referenceFrontFileNames.add(String.format("DTLZ1.%dD.pf", objectiveNumber[i]));
//        }
//        for (int i = 0; i < objectiveNumber.length; i++) {
//            Problem p = new C1_DTLZ3(objectiveNumber[i] + 9, objectiveNumber[i]);
//            p.setName(String.format("%s_%dD", p.getName(), objectiveNumber[i]));
//            problemList.add(p);
//            referenceFrontFileNames.add(String.format("DTLZ3.%dD.pf", objectiveNumber[i]));
//        }

        for (int i = 0; i < objectiveNumber.length; i++) {
            Problem p = new C2_DTLZ2(objectiveNumber[i] + 9, objectiveNumber[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumber[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C2_DTLZ2.%dD.pf", objectiveNumber[i]));
        }
        for (int i = 0; i < objectiveNumber.length; i++) {
            Problem p = new ConvexC2_DTLZ2(objectiveNumber[i] + 9, objectiveNumber[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumber[i]));
            problemList.add(p);
            referenceFrontFileNames.add(String.format("C2_Convex_DTLZ2.%dD.pf", objectiveNumber[i]));
        }

        List<TaggedAlgorithm<List<DoubleSolution>>> algorithmList =
                configureAlgorithmList(
                        problemList,
                        INDEPENDENT_RUNS,
                        numOfDivisionMap,
                        integratedTausMap,
                        populationSizeMap
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
                                new GenerationalDistance<DoubleSolution>(),
                                new InvertedGenerationalDistance<DoubleSolution>()))
                        .setIndependentRuns(INDEPENDENT_RUNS)
                        .setNumberOfCores(8)
                        .build();
        new ExecuteAlgorithms<>(experiment).run();
        new ComputeQualityIndicators<>(experiment).run();
        new GenerateLatexTablesWithStatistics(experiment).run();
    }

    static List<TaggedAlgorithm<List<DoubleSolution>>> configureAlgorithmList(List<Problem<DoubleSolution>> problemList,
                                                                              int independentRuns,
                                                                              Map<Integer, int[]> numOfDivisionMap,
                                                                              Map<Integer, double[]> integratedTausMap,
                                                                              Map<Integer, Integer> populationSizeMap) {
        List<TaggedAlgorithm<List<DoubleSolution>>> algorithms = new ArrayList<>();


        for (int run = 0; run < independentRuns; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                Algorithm<List<DoubleSolution>> algorithm = (new MOEACDBuilder(problemList.get(i), MOEACDBuilder.Variant.MOEACD))
                        .setNumOfDivision(numOfDivisionMap.get(problemList.get(i).getNumberOfObjectives()))
                        .setIntegratedTaus(integratedTausMap.get(problemList.get(i).getNumberOfObjectives()))
                        .setPopulationSize(populationSizeMap.get(problemList.get(i).getNumberOfObjectives()))
                        .setConstraintLayerSize(5)
                        .setMaxGen(1000)
                        .setDelta(new double[]{0.81, 0.09, 0.09, 0.01})
                        .build();
                algorithms.add(new TaggedAlgorithm<List<DoubleSolution>>(algorithm, "CMOEACD", problemList.get(i), run));
            }
        }

        for (int run = 0; run < independentRuns; run++) {
            for (int i = 0; i < problemList.size(); i++) {
                Algorithm<List<DoubleSolution>> algorithm = (new MOEADBuilder(problemList.get(i), MOEADBuilder.Variant.ConstraintMOEAD))
                        .setNumofDivision(numOfDivisionMap.get(problemList.get(i).getNumberOfObjectives()))
                        .setIntegratedTau(integratedTausMap.get(problemList.get(i).getNumberOfObjectives()))
                        .setPopulationSize(populationSizeMap.get(problemList.get(i).getNumberOfObjectives()))
                        .setMaxGen(1000)
                        .build();
                algorithms.add(new TaggedAlgorithm<List<DoubleSolution>>(algorithm, "CMOEAD", problemList.get(i), run));
            }
        }
        return algorithms;
    }
}
