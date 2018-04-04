package org.uma.jmetal.experiment.data;

import org.uma.jmetal.solution.DoubleSolution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by weaponhe on 2018/3/22.
 */
public class analyzeWater {
    static String basrDir = "../jmetal-data/MOEACDStudy/data";

    public static void main(String[] args) throws IOException {
        String[] indicators = {"IGD"};
        Map<String, ArrayList<ArrayList<Double>>> anchorMap = new HashMap<>();
        int runs = 5;
        String[] algorithmNames = {
                "C-MOEAD-SR",
                "C-MOEAD-CDP",
                "C-MOEAD-ACV",
                "C-NSGAIII",
                "C-MOEADD",
                "C-MOEACD"
        };

        int[] winArray = new int[algorithmNames.length];

        String problem = "Machining";
        int nObj = 4;

        for (int j = 0; j < algorithmNames.length; j++) {
            String algorithm = algorithmNames[j];
//            ArrayList<Double> IGD = readFileData("IGD", algorithm, problem, runs);
//            int index = getMedianIndex((ArrayList<Double>) IGD.clone());
            int index = 0;

            //找到每一个algorithm对应index的那次前沿中，每个维度最小对应的个体
            ArrayList<ArrayList<Double>> front = readFront(algorithm, problem, index);
            ArrayList<ArrayList<Double>> anchorSolutions = getAnchorSolutions(front);
            anchorMap.put(algorithm, anchorSolutions);
        }

        //横向为算法，纵向为目标
        for (int i = 0; i < nObj; i++) {
            int minIndex = 0;
            double minValue = Double.MAX_VALUE;
            for (int j = 0; j < algorithmNames.length; j++) {
                String algorithm = algorithmNames[j];
                double value = anchorMap.get(algorithm).get(i).get(i);
                if (value < minValue) {
                    minValue = value;
                    minIndex = j;
                }
            }
            winArray[minIndex]++;
        }

        for (int j = 0; j < algorithmNames.length; j++) {
            System.out.println(winArray[j]);
        }
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

    static ArrayList<ArrayList<Double>> readFront(String algorithm, String problem, int index) throws IOException {
        ArrayList<ArrayList<Double>> front = new ArrayList<>();
//        String path = basrDir + "/" + algorithm + "/" + problem + "/FUN" +;
        String path = String.format("%s/%s/%s/FUN%d.tsv", basrDir, algorithm, problem, index);
        File file = new File(path);
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        while ((tempString = reader.readLine()) != null) {
            String[] strArray = tempString.split(" ");
            ArrayList<Double> solution = new ArrayList<>();
            for (int i = 0; i < strArray.length; i++) {
                solution.add(Double.parseDouble(strArray[i]));
            }
            front.add(solution);
        }
        reader.close();
        return front;
    }


    static int getMedianIndex(ArrayList<Double> array) {
        ArrayList<Double> tempArray = (ArrayList<Double>) array.clone();
        Collections.sort(tempArray);
        Double median;

        if (tempArray.size() % 2 == 0) {
            int index1 = tempArray.size() / 2 - 1;
            int index2 = tempArray.size() / 2;
            median = (tempArray.get(index1) + tempArray.get(index2)) / 2;

        } else {
            median = tempArray.get(tempArray.size() / 2);
        }
        double minDis = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < array.size(); i++) {
            if (Math.abs(array.get(i) - median) < minDis) {
                minDis = Math.abs(array.get(i) - median);
                index = i;
            }
        }
        return index;
    }


    static ArrayList<ArrayList<Double>> getAnchorSolutions(ArrayList<ArrayList<Double>> front) {
        ArrayList<ArrayList<Double>> anchorSolutions = new ArrayList<>();
        int nNbj = front.get(0).size();
        for (int i = 0; i < nNbj; i++) {
            int minIndex = 0;
            double minValue = Double.MAX_VALUE;
            for (int j = 0; j < front.size(); j++) {
                double value = front.get(j).get(i);
                if (value < minValue) {
                    minValue = value;
                    minIndex = j;
                }
            }
            anchorSolutions.add(front.get(minIndex));
        }
        return anchorSolutions;
    }
}
