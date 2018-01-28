package org.uma.jmetal.experiment.MOEACD.constraint;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.cdtlz.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.CombinationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weaponhe on 2018/1/19.
 */
public class Configurator {
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

    public Configurator() {
        initConfiguration();
        initProblems();
    }

    static public void initConfiguration() {
        objectiveNumberConfig = new Integer[]{
                3,
                5,
//                8,
//                10,
//                15
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
//            referenceFrontFileNames.add(String.format("DTLZ1.%dD.pf", objectiveNumberConfig[i]));
            referenceFrontFileNames.add(String.format("DTLZ1.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
        }
        for (int i = 0; i < objectiveNumberConfig.length; i++) {
            Problem p = new C1_DTLZ3(objectiveNumberConfig[i] + 9, objectiveNumberConfig[i]);
            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
            problemList.add(p);
//            referenceFrontFileNames.add(String.format("DTLZ3.%dD.pf", objectiveNumberConfig[i]));
            referenceFrontFileNames.add(String.format("DTLZ3.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
        }
////
//        for (int i = 0; i < objectiveNumberConfig.length; i++) {
//            Problem p = new C2_DTLZ2(objectiveNumberConfig[i] + 9, objectiveNumberConfig[i]);
//            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
//            problemList.add(p);
////            referenceFrontFileNames.add(String.format("C2_DTLZ2.%dD.pf", objectiveNumberConfig[i]));
//            referenceFrontFileNames.add(String.format("C2_DTLZ2.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
//        }
//        for (int i = 0; i < objectiveNumberConfig.length; i++) {
//            Problem p = new ConvexC2_DTLZ2(objectiveNumberConfig[i] + 9, objectiveNumberConfig[i]);
//            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
//            problemList.add(p);
////            referenceFrontFileNames.add(String.format("C2_Convex_DTLZ2.%dD.pf", objectiveNumberConfig[i]));
//            referenceFrontFileNames.add(String.format("C2_Convex_DTLZ2.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
//        }
//
//        for (int i = 0; i < objectiveNumberConfig.length; i++) {
//            Problem p = new C3_DTLZ1(objectiveNumberConfig[i] + 4, objectiveNumberConfig[i], objectiveNumberConfig[i]);
//            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
//            problemList.add(p);
////            referenceFrontFileNames.add(String.format("C3_DTLZ1.%dD.pf", objectiveNumberConfig[i]));
//            referenceFrontFileNames.add(String.format("C3_DTLZ1.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
//        }
//        for (int i = 0; i < objectiveNumberConfig.length; i++) {
//            Problem p = new C3_DTLZ4(objectiveNumberConfig[i] + 4, objectiveNumberConfig[i], objectiveNumberConfig[i]);
//            p.setName(String.format("%s_%dD", p.getName(), objectiveNumberConfig[i]));
//            problemList.add(p);
////            referenceFrontFileNames.add(String.format("C3_DTLZ4.%dD.pf", objectiveNumberConfig[i]));
//            referenceFrontFileNames.add(String.format("C3_DTLZ4.%dD.pf[%d]", objectiveNumberConfig[i], paretoPointCoontConfig[i]));
//        }
    }

    public static String getExperimentBaseDirectory() {
        return experimentBaseDirectory;
    }

    public static void setExperimentBaseDirectory(String experimentBaseDirectory) {
        Configurator.experimentBaseDirectory = experimentBaseDirectory;
    }

    public static int getIndependentRuns() {
        return INDEPENDENT_RUNS;
    }

    public static Integer[] getObjectiveNumberConfig() {
        return objectiveNumberConfig;
    }

    public static void setObjectiveNumberConfig(Integer[] objectiveNumberConfig) {
        Configurator.objectiveNumberConfig = objectiveNumberConfig;
    }

    public static Integer[] getParetoPointCoontConfig() {
        return paretoPointCoontConfig;
    }

    public static void setParetoPointCoontConfig(Integer[] paretoPointCoontConfig) {
        Configurator.paretoPointCoontConfig = paretoPointCoontConfig;
    }

    public static int[][] getNumOfDivisionConfig() {
        return numOfDivisionConfig;
    }

    public static void setNumOfDivisionConfig(int[][] numOfDivisionConfig) {
        Configurator.numOfDivisionConfig = numOfDivisionConfig;
    }

    public static double[][] getIntegratedTausConfig() {
        return integratedTausConfig;
    }

    public static void setIntegratedTausConfig(double[][] integratedTausConfig) {
        Configurator.integratedTausConfig = integratedTausConfig;
    }

    public static int[] getPopulationSizeConfig() {
        return populationSizeConfig;
    }

    public static void setPopulationSizeConfig(int[] populationSizeConfig) {
        Configurator.populationSizeConfig = populationSizeConfig;
    }

    public static Map<String, int[]> getMaxGenConfig() {
        return maxGenConfig;
    }

    public static void setMaxGenConfig(Map<String, int[]> maxGenConfig) {
        Configurator.maxGenConfig = maxGenConfig;
    }

    public static List<Problem<DoubleSolution>> getProblemList() {
        return problemList;
    }

    public static void setProblemList(List<Problem<DoubleSolution>> problemList) {
        Configurator.problemList = problemList;
    }

    public static List<String> getReferenceFrontFileNames() {
        return referenceFrontFileNames;
    }

    public static void setReferenceFrontFileNames(List<String> referenceFrontFileNames) {
        Configurator.referenceFrontFileNames = referenceFrontFileNames;
    }
}
