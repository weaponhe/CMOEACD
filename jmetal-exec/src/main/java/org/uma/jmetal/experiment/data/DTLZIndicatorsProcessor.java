package org.uma.jmetal.experiment.data;

import org.uma.jmetal.problem.multiobjective.cdtlz.C3_DTLZ1;
import org.uma.jmetal.solution.DoubleSolution;

import java.io.*;
import java.util.*;

/**
 * Created by weaponhe on 2017/11/27.
 */
public class DTLZIndicatorsProcessor {
    static String basrDir = "../jmetal-data/MOEACDStudy/data";

    public static void main(String[] args) throws IOException {
        int runs = 5;
        File file = new File(basrDir);
        System.out.println(file.getCanonicalPath());//获取标准的路径
        System.out.println(file.getAbsolutePath());//获取绝对路径
        File[] algorithmList = file.listFiles();


        String[] indicators = {"IGD", "IGDPlus"};
        Map<String, Boolean> algorithms = new HashMap<>();
        Map<String, Boolean> problems = new HashMap<>();
        Map<Integer, Boolean> dimentions = new HashMap<>();


        //数据预处理
        for (int indicator = 0; indicator < indicators.length; indicator++) {
            for (int i = 0; i < algorithmList.length; i++) {
                if (algorithmList[i].isDirectory()) {
                    String algorithmName = algorithmList[i].getName();
                    algorithms.put(algorithmName, true);
                    File[] problemList = algorithmList[i].listFiles();
                    for (int j = 0; j < problemList.length; j++) {
                        if (problemList[j].isDirectory()) {
                            String temp = problemList[j].getName();
                            int splitPoint = temp.lastIndexOf("_");
                            String problemName = temp.substring(0, splitPoint);
                            String D = temp.substring(splitPoint + 1);
                            Integer dimention = Integer.parseInt(D.substring(0, D.length() - 1));
                            problems.put(problemName, true);
                            dimentions.put(dimention, true);
                        }
                    }
                }
            }
            break;
        }

        //自定义算法列表
        algorithms = new HashMap<>();
        String[] algorithmNames = {
//                "C-MOEACD(PBI)",
//                "CMOEACD4Selection_randout",
//                "CMOEACD4Selection_randin",
//                "CMOEACD4Selection_randout_tour",
//                "CMOEACD4Selection_randin_tour",
//                "CMOEACD4C1DTLZ3_randout",
//                "CMOEACD4C1DTLZ3_randin",
//                "CMOEACD4C1DTLZ3_randout_tour",
//                "CMOEACD4C1DTLZ3(1)",
//                "CMOEACD4C1DTLZ3(2)",
//                "CMOEACD4C1DTLZ3(3)",
//                "CMOEACD4C1DTLZ3(4)",
//                "CMOEACD4C1DTLZ3(5)",
//                "CMOEACD4C1DTLZ3(6)",
//                "CMOEACD4C1DTLZ3(7)",
                "CMOEACD",
//                "CMOEACD4C1DTLZ3(9)",
//                "CMOEACD4C1DTLZ3(10)",
//                "CMOEACD4C1DTLZ3(Adaptive)",
//                "CMOEACD4C1DTLZ3_randin_tour",
//                "CMOEACD4C1DTLZ3_randout_noelite",
//                "CMOEACD4C1DTLZ3_randin_noelite",
//                "CMOEACD4C1DTLZ3_randout_tour_noelite",
//                "CMOEACD4C1DTLZ3_randin_tour_noelite",
//                "C-MOEACD-C1DTLZ3",
//                "C-MOEACD(TCH)",
//                "C-MOEACD(LP2)",
//                "C-MOEAD",
                "CNSGAIII",
//                "C-MOEADD"
//                "C-MOEACD-CDP",
//                "C-MOEACD-SR"
        };
        for (int i = 0; i < algorithmNames.length; i++) {
            algorithms.put(algorithmNames[i], true);
        }
//
        problems = new HashMap<>();
        String[] problemNames = {
                "C1_DTLZ1",
                "C1_DTLZ3",
                "C2_DTLZ2",
                "ConvexC2_DTLZ2",
                "C3_DTLZ1",
                "C3_DTLZ4"
        };
        for (int i = 0; i < problemNames.length; i++) {
            problems.put(problemNames[i], true);
        }


        //数据读取
        Map<String, Map<String, Map<String, Map<Integer, List<Double>>>>> data = new HashMap<>();
        for (int indicator = 0; indicator < indicators.length; indicator++) {
            data.put(indicators[indicator], new HashMap<String, Map<String, Map<Integer, List<Double>>>>());
            for (String algorithm : algorithms.keySet()) {
                data.get(indicators[indicator]).put(algorithm, new HashMap<String, Map<Integer, List<Double>>>());
                for (String problem : problems.keySet()) {
                    data.get(indicators[indicator]).get(algorithm).put(problem, new HashMap<Integer, List<Double>>());
                    for (Integer dimention : dimentions.keySet()) {
                        ArrayList<Double> array = new ArrayList<>();
                        try {
                            array = readFileData(indicators[indicator], algorithm, problem, dimention, runs);
                            data.get(indicators[indicator]).get(algorithm).get(problem).put(dimention, array);
                            if (array.size() < runs && array.size() > 0) {
                                System.out.println("[" + algorithm + " " + problem + " " + dimention + "]: some run no result");
                            }
                        } catch (FileNotFoundException e) {
                            System.out.println("[" + algorithm + " " + problem + " " + dimention + "]: all run no result");
                        }
                    }
                }
            }
        }

        //数据处理

        for (int indicator = 0; indicator < indicators.length; indicator++) {
            System.out.println(indicators[indicator]);
            System.out.print("Test Instance\t");
            System.out.print("m\t");
//            for (String algorithm : algorithms.keySet()) {
            for (String algorithm : algorithmNames) {
                System.out.print(String.format("%s\t", algorithm));
            }
            System.out.print("\n");

            Map<String, Integer> bestCountMap = new HashMap<>();
//            for (String algorithm : algorithms.keySet()) {
            for (String algorithm : algorithmNames) {
                bestCountMap.put(algorithm, 0);
            }
            for (String problem : problems.keySet()) {
                boolean newProblem = true;
                for (Integer dimention : dimentions.keySet()) {
                    for (int count = 0; count <= 2; count++) {
                        if (newProblem) {
                            System.out.print(problem + "\t");
                            newProblem = false;
                        } else {
                            System.out.print("\t");
                        }
                        if (count == 0) {
                            System.out.print(dimention + "\t");
                        } else {
                            System.out.print("\t");
                        }

                        String bestKey = "";
                        Double bestValue = Double.MAX_VALUE;
                        for (String algorithm : algorithmNames) {
                            List<Double> array = data.get(indicators[indicator]).get(algorithm).get(problem).get(dimention);

                            //处理不完整数据
                            if (array.size() == 0) {
                                //Best/Median/Worst都是—
                                System.out.print("-" + "\t");
                                continue;
                            } else if (array.size() < runs && count == 2) {
                                //Worst为-
                                System.out.print("-" + "\t");
                                continue;
                            }

                            Double value = 0.0;
                            if (count == 0) {
                                value = getBest(array);
                            } else if (count == 1) {
                                value = getMedian(array);
                            } else if (count == 2) {
                                value = getWorst(array);
                            }
                            if (value < bestValue) {
                                bestValue = value;
                                bestKey = algorithm;
                            }
                            System.out.print(value + "\t");
                        }

                        bestCountMap.put(bestKey, bestCountMap.get(bestKey) + 1);
                        System.out.print("\n");
                    }

                }
            }
            System.out.print("\t\t");
//            for (String algorithm : algorithms.keySet()) {
            for (String algorithm : algorithmNames) {
                System.out.print(bestCountMap.get(algorithm) + "\t");
            }
            System.out.print("\n");
        }
    }


    static Double getBest(List<Double> array) {
        Collections.sort(array);
        return array.get(0);
    }

    static Double getMedian(List<Double> array) {
        Collections.sort(array);
        if (array.size() % 2 == 0) {
            int index1 = array.size() / 2 - 1;
            int index2 = array.size() / 2;
            return (array.get(index1) + array.get(index2)) / 2;

        } else {
            return array.get(array.size() / 2);
        }
    }

    static Double getWorst(List<Double> array) {
        Collections.sort(array);
        return array.get(array.size() - 1);
    }


    static ArrayList<Double> readFileData(String indicator, String algorithm, String problem, Integer dimention, int runs) throws IOException {
        ArrayList<Double> data = new ArrayList<>();
        String path = basrDir + "/" + algorithm + "/" + problem + "_" + dimention + "D" + "/" + indicator;
        File file = new File(path);
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        int i = 0;
        while ((tempString = reader.readLine()) != null && i < runs) {
            data.add(Double.parseDouble(tempString));
            i++;
        }
        reader.close();
        return data;
    }
}
