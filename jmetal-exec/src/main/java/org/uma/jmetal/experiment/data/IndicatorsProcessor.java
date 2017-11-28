package org.uma.jmetal.experiment.data;

import org.uma.jmetal.solution.DoubleSolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by weaponhe on 2017/11/27.
 */
public class IndicatorsProcessor {
    public static void main(String[] args) throws IOException {
        String basrDir = "jmetal-data/MOEACDStudy/data";
        File file = new File(basrDir);
        File[] algorithmList = file.listFiles();

        String[] indicators = {"GD", "IGD"};
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

        //数据读取
        Map<String, Map<String, Map<String, Map<Integer, List<Double>>>>> data = new HashMap<>();
        for (int indicator = 0; indicator < indicators.length; indicator++) {
            data.put(indicators[indicator], new HashMap<String, Map<String, Map<Integer, List<Double>>>>());
            for (String algorithm : algorithms.keySet()) {
                data.get(indicators[indicator]).put(algorithm, new HashMap<String, Map<Integer, List<Double>>>());
                for (String problem : problems.keySet()) {
                    data.get(indicators[indicator]).get(algorithm).put(problem, new HashMap<Integer, List<Double>>());
                    for (Integer dimention : dimentions.keySet()) {
                        ArrayList<Double> array = readFileData(indicators[indicator], algorithm, problem, dimention);
                        data.get(indicators[indicator]).get(algorithm).get(problem).put(dimention, array);
                    }
                }
            }
        }

        //数据处理

        for (int indicator = 0; indicator < indicators.length; indicator++) {
            System.out.println(indicators[indicator]);
            System.out.print("Test Instance\t");
            System.out.print("m\t");
            for (String algorithm : algorithms.keySet()) {
                System.out.print(String.format("%s\t", algorithm));
            }
            System.out.print("\n");
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
                        for (String algorithm : algorithms.keySet()) {
                            List<Double> array = data.get(indicators[indicator]).get(algorithm).get(problem).get(dimention);
                            Double value = 0.0;
                            if (count == 0) {
                                value = getBest(array);
                            } else if (count == 1) {
                                value = getMedian(array);
                            } else if (count == 2) {
                                value = getWorst(array);
                            }
                            System.out.print(value + "\t");
                        }
                        System.out.print("\n");
                    }

                }
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


    static ArrayList<Double> readFileData(String indicator, String algorithm, String problem, Integer dimention) throws IOException {
        ArrayList<Double> data = new ArrayList<>();
        String path = "jmetal-data/MOEACDStudy/data/" + algorithm + "/" + problem + "_" + dimention + "D" + "/" + indicator;
        File file = new File(path);
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        while ((tempString = reader.readLine()) != null) {
            data.add(Double.parseDouble(tempString));
        }
        reader.close();
        return data;
    }
}
