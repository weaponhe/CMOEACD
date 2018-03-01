package org.uma.jmetal.experiment.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by weaponhe on 2018/2/28.
 */
public class PracticalIndicatorsProcessor {
    static String basrDir = "../jmetal-data/MOEACDStudy/data";

    public static void main(String[] args) throws IOException {
        Map<String, Boolean> algorithms = new HashMap<>();
        Map<String, Boolean> problems = new HashMap<>();
        String[] indicators = {"IGD", "IGDPlus"};
        int runs = 20;
        String[] algorithmNames = {
                "C-MOEAD",
                "CNSGAIII",
                "C-MOEADD",
                "CMOEACD"
        };
        for (int i = 0; i < algorithmNames.length; i++) {
            algorithms.put(algorithmNames[i], true);
        }

        problems = new HashMap<>();
        String[] problemNames = {
                "Water",
        };
        for (int i = 0; i < problemNames.length; i++) {
            problems.put(problemNames[i], true);
        }

        Map<String, Map<String, Map<String, List<Double>>>> data = new HashMap<>();
        for (int k = 0; k < indicators.length; k++) {
            data.put(indicators[k], new HashMap<String, Map<String, List<Double>>>());
            for (int i = 0; i < algorithmNames.length; i++) {
                data.get(indicators[k]).put(algorithmNames[i], new HashMap<String, List<Double>>());
                String algorithm = algorithmNames[i];
                for (int j = 0; j < problemNames.length; j++) {
                    String problem = problemNames[j];
                    ArrayList<Double> raw = readFileData(indicators[k], algorithm, problem, runs);
                    data.get(indicators[k]).get(algorithm).put(problem, raw);
//                    double best = getBest(raw);
//                    double median = getMedian(raw);
//                    double worst = getWorst(raw);
//                    System.out.print(best + " " + median + " " + worst);
                }
//                System.out.print("\n");
            }
        }
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
                for (int count = 0; count <= 2; count++) {
                    if (newProblem) {
                        System.out.print(problem + "\t");
                        newProblem = false;
                    } else {
                        System.out.print("\t");
                    }

                    System.out.print("\t");

                    String bestKey = "";
                    Double bestValue = Double.MAX_VALUE;
                    for (String algorithm : algorithmNames) {
                        List<Double> array = data.get(indicators[indicator]).get(algorithm).get(problem);

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

    static ArrayList<Double> readFileData(String indicator, String algorithm, String problem, int runs) throws IOException {
        ArrayList<Double> data = new ArrayList<>();
        String path = basrDir + "/" + algorithm + "/" + problem + "/" + indicator;
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
