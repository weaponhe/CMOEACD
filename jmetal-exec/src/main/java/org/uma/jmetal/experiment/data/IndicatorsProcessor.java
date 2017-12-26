package org.uma.jmetal.experiment.data;

import org.uma.jmetal.solution.DoubleSolution;

import java.io.*;
import java.util.*;

/**
 * Created by weaponhe on 2017/11/27.
 */
public class IndicatorsProcessor {
    public static void main(String[] args) throws IOException {
        int runs = 5;
        String basrDir = "jmetal-data/MOEACDStudy/data";
        File file = new File(basrDir);
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
                "CMOEACD",
//                "CMOEADD",
                "CNSGAIII"
//                "CMOEACD-CL-CDP",
//                "CMOEACD-CDP",
//                "CMOEACD-CDP2",
//                "CMOEACD-SR"
//                "CMOEACD-PF(1)",
//                "CMOEACD-PF(2)",
//                "CMOEACD-PF(3)",
//                "CMOEACD-PF(4)",
//                "CMOEACD-PF(5)",
//                "CMOEACD-PF(6)",
//                "CMOEACD-PF(7)",
//                "CMOEACD-PF(8)",
//                "CMOEACD-PF(9)",
//                "CMOEACD-PF(10)",
//                "CMOEACD(3)",
//                "CMOEACD(4)",
//                "CMOEACD(5)",
//                "CMOEACD(6)",
//                "CMOEACD(7)",
//                "CMOEACD(8)",
//                "CMOEACD(9)",
//                "CMOEACD(10)",
//                "CMOEACD(11)"
        };
        for (int i = 0; i < algorithmNames.length; i++) {
            algorithms.put(algorithmNames[i], true);
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
                            array = readFileData(indicators[indicator], algorithm, problem, dimention);
                        } catch (FileNotFoundException e) {
                            System.out.println("[" + algorithm + " " + problem + " " + dimention + "]: all run no result");
                        }
                        if (array.size() < runs && array.size() > 0) {
                            System.out.println("[" + algorithm + " " + problem + " " + dimention + "]: some run no result");
                        }
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
