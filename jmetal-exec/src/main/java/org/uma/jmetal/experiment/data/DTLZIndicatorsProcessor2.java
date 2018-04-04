package org.uma.jmetal.experiment.data;

import org.uma.jmetal.problem.multiobjective.cdtlz.C3_DTLZ1;
import org.uma.jmetal.solution.DoubleSolution;

import java.io.*;
import java.util.*;

/**
 * Created by weaponhe on 2017/11/27.
 */
public class DTLZIndicatorsProcessor2 {
    static String basrDir = "../jmetal-data/MOEACDStudy/data";

    public static void main(String[] args) throws IOException {
        int runs = 5;
        File file = new File(basrDir);
        File[] algorithmList = file.listFiles();


        String[] indicators = {"IGD"};
        Map<String, Boolean> algorithms = new HashMap<>();
        Map<String, Boolean> problems = new HashMap<>();
        Map<Integer, Boolean> dimentions = new HashMap<>();

        //自定义算法列表
        algorithms = new HashMap<>();
        String[] algorithmNames = {
                "C-MOEAD-SR",
                "C-MOEAD-CDP",
                "C-MOEAD-ACV",
                "C-NSGAIII",
                "C-MOEADD",
                "C-MOEACD"

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

        int[] dimentionArray = {
                3, 5, 8, 10, 15
        };
        for (int i = 0; i < dimentionArray.length; i++) {
            dimentions.put(dimentionArray[i], true);
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

        //print header

//        System.out.println("\\begin{table}[H]");
        System.out.println("\\begin{center}");
        System.out.println("\\renewcommand{\\arraystretch}{0.6}");
        System.out.println("\\scriptsize");

        System.out.print("\\begin{longtabu} to \\linewidth{X[1,c]|X[1,c]");
        for (int i = 0; i < algorithmNames.length; i++) {
            System.out.print("|X[5,c]");
        }
        System.out.print("}\n");

        System.out.println(String.format("\\caption{各个对比算法在CDTLZ系列测试例的实验结果(最佳值,中位值,最差值)}"));
        System.out.println(String.format("\\label{table:result:%s}\\\\", "exp2"));
        System.out.println("\\nopagebreak");
        System.out.println("\\toprule");

        System.out.print("\\textbf{测试例} &  \\textbf{目标数m} ");
        for (int i = 0; i < algorithmNames.length; i++) {
            System.out.print(String.format("&   \\textbf{%s}", algorithmNames[i]));
        }
        System.out.print("\\\\\n");

        System.out.println("\\endfirsthead");
        System.out.println(String.format("\\multicolumn{%d}{c}{\\small 表 \\ref{table:result:%s}\\ 各个对比算法在CDTLZ系列测试例的实验结果(续)}\\\\",
                2 + algorithmNames.length,
                "exp2"
        ));


        System.out.println("\\toprule");
        System.out.print("\\textbf{测试例} &  \\textbf{目标数m} ");
        for (int i = 0; i < algorithmNames.length; i++) {
            System.out.print(String.format("&   \\textbf{%s}", algorithmNames[i]));
        }
        System.out.print("\\\\\n");
        System.out.println("\\hline");
        System.out.println("\\endhead");
        System.out.println("\\bottomrule");
        System.out.println(String.format("\\multicolumn{%d}{c}{\\small 表格接下页  }\\\\", 2 + algorithmNames.length));
        System.out.println("\\endfoot");
        System.out.println("\\endlastfoot");
        System.out.println("\\nopagebreak");
        Map<String, Integer> bestCountMap = new HashMap<>();
        Map<String, Integer> secondCountMap = new HashMap<>();
        for (String algorithm : algorithmNames) {
            bestCountMap.put(algorithm, 0);
            secondCountMap.put(algorithm, 0);
        }


        for (int p = 0; p < problemNames.length; p++) {
            String problem = problemNames[p];


            for (int indicator = 0; indicator < indicators.length; indicator++) {
                boolean newProblem = true;
                for (Integer dimention : dimentions.keySet()) {
                    for (int count = 0; count <= 2; count++) {
                        if (newProblem) {
                            System.out.print("\n\\hline\n");
                            System.out.print(String.format("\\multirow{15}*{\\begin{sideways} \\textbf{%s}\\end{sideways}}", recombineString(problem)) + "\n");
                            newProblem = false;
                        } else {
//                            System.out.print("\t");
                        }
                        if (count == 0) {
                            if (dimention != dimentionArray[0]) {
                                System.out.print(String.format("\\cline{2-%d}", 2 + algorithmNames.length));
                            }
                            System.out.print(String.format("& \\multirow{3}*{%d}", dimention) + "\n");
                        } else {
//                            System.out.print("\t");
                        }

                        String bestKey = "";
                        Double bestValue = Double.MAX_VALUE;
                        List<Double> oneLineData = new ArrayList<>();
                        for (String algorithm : algorithmNames) {
                            List<Double> array = data.get(indicators[indicator]).get(algorithm).get(problem).get(dimention);
                            if (array.size() == 0) {
                                //Best/Median/Worst都是—
                                oneLineData.add(Double.POSITIVE_INFINITY);
                            } else {
                                if (count == 0) {
                                    oneLineData.add(getBest(array));
                                } else if (count == 1) {
                                    oneLineData.add(getMedian(array));
                                } else if (count == 2) {
                                    if (array.size() < runs) {
                                        oneLineData.add(Double.POSITIVE_INFINITY);
                                    } else {
                                        oneLineData.add(getWorst(array));
                                    }
                                }
                            }
                        }
                        //对oneLineData排序，得到序号数组
                        List<Integer> oneLineRank = new ArrayList<>();
                        for (int col = 0; col < oneLineData.size(); col++) {
                            oneLineRank.add(1);
                        }
                        for (int i1 = 0; i1 < oneLineData.size(); i1++) {
                            for (int i2 = 0; i2 < oneLineData.size(); i2++) {
                                if (oneLineData.get(i2) < oneLineData.get(i1)) {
                                    oneLineRank.set(i1, oneLineRank.get(i1) + 1);
                                }
                            }
                        }
                        for (int col = 0; col < oneLineData.size(); col++) {
                            if (count != 0 && col == 0) {
                                System.out.print("& ");
                            }
                            System.out.print("& ");
                            if (oneLineRank.get(col) == 1) {
                                System.out.print("\\cellcolor{gray95}");
                            }
                            if (oneLineRank.get(col) == 2) {
                                System.out.print("\\cellcolor{gray25}");
                            }
                            if (oneLineData.get(col) == Double.POSITIVE_INFINITY) {
                                System.out.print("${-}$ ");
                            } else {
                                System.out.print(String.format("${%f_{(%d)}}$ ", oneLineData.get(col), oneLineRank.get(col)));
                            }
                            if (col == oneLineData.size() - 1) {
                                System.out.print("\\\\");
                            }
                        }
                        System.out.print("\n");
                        System.out.print("\\nopagebreak\n");
                        String bestAlgorithmName = algorithmNames[oneLineRank.indexOf(1)];
                        String secondAlgorithmName = algorithmNames[oneLineRank.indexOf(2)];
                        bestCountMap.put(bestAlgorithmName, bestCountMap.get(bestAlgorithmName) + 1);
                        secondCountMap.put(secondAlgorithmName, secondCountMap.get(secondAlgorithmName) + 1);

//                        for (String algorithm : algorithmNames) {
//                            List<Double> array = data.get(indicators[indicator]).get(algorithm).get(problem).get(dimention);
//
//                            //处理不完整数据
//                            if (array.size() == 0) {
//                                //Best/Median/Worst都是—
//                                System.out.print("-" + "\t");
//                                continue;
//                            } else if (array.size() < runs && count == 2) {
//                                //Worst为-
//                                System.out.print("-" + "\t");
//                                continue;
//                            }
//
//                            Double value = 0.0;
//                            if (count == 0) {
//                                value = getBest(array);
//                            } else if (count == 1) {
//                                value = getMedian(array);
//                            } else if (count == 2) {
//                                value = getWorst(array);
//                            }
//                            if (value < bestValue) {
//                                bestValue = value;
//                                bestKey = algorithm;
//                            }
//                            if (count != 0 && algorithm != algorithmNames[algorithmNames.length - 1]) {
//                                System.out.print("& ");
//                            }
//                            System.out.print(String.format("& ${%f}$", value) + "\t");
//                            if (algorithm == algorithmNames[algorithmNames.length - 1]) {
//                                //last one
//                                System.out.print("\\\\");
//                            }
//                        }
//                        System.out.print("\n");
                    }
                }
            }

        }
        //print footer
        System.out.println("\\bottomrule");
        System.out.println("\\end{longtabu}");
        System.out.println("\\begin{minipage}{\\linewidth}");
        System.out.println("\\flushleft \\scriptsize\\ \\textit{注：} 对于每一个具体的数据项，在所有算法中的排序以下标的形式标识，其中如果是最佳的就为以深灰色为底、加粗字体进行标记，而次优的只用浅灰色为底进行标记。");
        System.out.println("\\end{minipage}");
        System.out.println("\\end{center}");
//        System.out.println("\\end{table}");

        for (String algorithm : algorithmNames) {
            System.out.print(bestCountMap.get(algorithm) + "\t");
        }
        System.out.print("\n");
        for (String algorithm : algorithmNames) {
            System.out.print(secondCountMap.get(algorithm) + "\t");
        }
        System.out.print("\n");
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

    static String recombineString(String oldString) {
        int splitPoint = oldString.indexOf('_');
        String newString = oldString.substring(0, splitPoint) + '-' + oldString.substring(splitPoint + 1);
        return newString;
    }
}
