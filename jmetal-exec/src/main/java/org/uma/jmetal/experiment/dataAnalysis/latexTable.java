package org.uma.jmetal.experiment.dataAnalysis;

import com.sun.deploy.security.ValidationState;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.IntegerDoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.StrengthFitnessComparator;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by X250 on 2016/8/31.
 */
public class latexTable {
//    private int SIGNSTATE_BEST = 2;
//    private int SIGNSTATE_SECONDBEST = 1;
//    private int SIGNSTATE_NONE = 0;
    private String precision = "#0.00000";
    private String precisionAttach = "#0";
    private DecimalFormat precisionControl = null;
    private DecimalFormat precisionControlAttach = null;

    private double[][][] mean;
    private double[][][] median;
    private double[][][] stdDeviation;
    private double[][][] iqr;
    private double[][][] max;
    private double[][][] min;
    private double[][][] numberOfValues;
    private double[][][] minRun;
    private double[][][] maxRun;
    private double[][][] medianRun;
    private double[][][] closestMeanRun;

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getPrecisionAttach() {
        return precisionAttach;
    }

    public void setPrecisionAttach(String precisionAttach) {
        this.precisionAttach = precisionAttach;
    }

    public void resetPrecisionControl() {
        precisionControl = new DecimalFormat(precision);
    }

    public void resetPrecisionControlAttach() {
        precisionControlAttach = new DecimalFormat(precisionAttach);
    }

//    private List<Integer> makeSignState(int sizeData, List<Integer> bestIdx, List<Integer> secondBestIdx) {
//        List<Integer> signState = new ArrayList<>(sizeData);
//        for (int i = 0; i < sizeData; i++)
//            signState.add(SIGNSTATE_NONE);
//        for (int i = 0; i < bestIdx.size(); i++)
//            signState.set(bestIdx.get(i), SIGNSTATE_BEST);
//        if (sizeData > 2 && bestIdx.size() == 1) {
//            for (int i = 0; i < secondBestIdx.size(); i++)
//                signState.set(secondBestIdx.get(i), SIGNSTATE_SECONDBEST);
//        }
//        return signState;
//    }

    private void computeDataStatistics(List<List<List<List<Double>>>> data) {

        JMetalLogger.logger.info("begin computeDataStatistics");

        resetPrecisionControl();
        resetPrecisionControlAttach();

        int sizeProblem = data.size();

        mean = new double[sizeProblem][][];
        median = new double[sizeProblem][][];
        stdDeviation = new double[sizeProblem][][];
        iqr = new double[sizeProblem][][];
        min = new double[sizeProblem][][];
        max = new double[sizeProblem][][];
        numberOfValues = new double[sizeProblem][][];
        minRun = new double[sizeProblem][][];
        maxRun = new double[sizeProblem][][];
        medianRun = new double[sizeProblem][][];
        closestMeanRun = new double[sizeProblem][][];


        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = data.get(iProblem).size();

            mean[iProblem] = new double[sizeProblemObj][];
            median[iProblem] = new double[sizeProblemObj][];
            stdDeviation[iProblem] = new double[sizeProblemObj][];
            iqr[iProblem] = new double[sizeProblemObj][];
            min[iProblem] = new double[sizeProblemObj][];
            max[iProblem] = new double[sizeProblemObj][];
            numberOfValues[iProblem] = new double[sizeProblemObj][];
            minRun[iProblem] = new double[sizeProblemObj][];
            maxRun[iProblem] = new double[sizeProblemObj][];
            medianRun[iProblem] = new double[sizeProblemObj][];
            closestMeanRun[iProblem] = new double[sizeProblemObj][];

            for (int problemObj = 0; problemObj < sizeProblemObj; problemObj++) {

                int sizeAlgorithm = data.get(iProblem).get(problemObj).size();

                mean[iProblem][problemObj] = new double[sizeAlgorithm];
                median[iProblem][problemObj] = new double[sizeAlgorithm];
                stdDeviation[iProblem][problemObj] = new double[sizeAlgorithm];
                iqr[iProblem][problemObj] = new double[sizeAlgorithm];
                min[iProblem][problemObj] = new double[sizeAlgorithm];
                max[iProblem][problemObj] = new double[sizeAlgorithm];
                numberOfValues[iProblem][problemObj] = new double[sizeAlgorithm];
                minRun[iProblem][problemObj] = new double[sizeAlgorithm];
                maxRun[iProblem][problemObj] = new double[sizeAlgorithm];
                medianRun[iProblem][problemObj] = new double[sizeAlgorithm];
                closestMeanRun[iProblem][problemObj] = new double[sizeAlgorithm];

                for (int algorithm = 0; algorithm < sizeAlgorithm; algorithm++) {
//                    Collections.sort(data.get(iProblem).get(problemObj).get(algorithm));
//
//                    Map<String, Double> statValues = computeStatistics(data.get(iProblem).get(problemObj).get(algorithm)) ;
//
//                    mean[iProblem][problemObj][algorithm] = statValues.get("mean");
//                    median[iProblem][problemObj][algorithm] = statValues.get("median");
//                    stdDeviation[iProblem][problemObj][algorithm] = statValues.get("stdDeviation");
//                    iqr[iProblem][problemObj][algorithm] = statValues.get("iqr");
//                    min[iProblem][problemObj][algorithm] = statValues.get("min");
//                    max[iProblem][problemObj][algorithm] = statValues.get("max");
//                    numberOfValues[iProblem][problemObj][algorithm] = statValues.get("numberOfElements").intValue();

                    List<Double> originalIndicatorData = data.get(iProblem).get(problemObj).get(algorithm);

                    List<Double> indicatorData = new ArrayList<>(originalIndicatorData.size());

                    //Control the precision of data
                    for (int q = 0; q < originalIndicatorData.size(); q++) {
                        indicatorData.add(Double.parseDouble(precisionControl.format(originalIndicatorData.get(q))));
                    }

                    List<Integer> idxRun = new ArrayList<>(indicatorData.size());
                    for (int q = 0; q < indicatorData.size(); q++)
                        idxRun.add(q);
                    sort(indicatorData, idxRun);

                    DescriptiveStatistics stats = new DescriptiveStatistics();
                    for (Double value : indicatorData) {
                        stats.addValue(value);
                    }

                    double meanValue = stats.getMean();
                    mean[iProblem][problemObj][algorithm] = meanValue;
                    mean[iProblem][problemObj][algorithm] = Double.parseDouble(precisionControl.format(mean[iProblem][problemObj][algorithm]));

                    double minDif = Double.MAX_VALUE;
                    int idxClosest = -1;
                    for (int i = 0; i < idxRun.size(); i++) {
                        double tmpDif = Math.abs(indicatorData.get(i) - meanValue);
                        if (tmpDif < minDif) {
                            minDif = tmpDif;
                            idxClosest = idxRun.get(i);
                        }
                    }
                    closestMeanRun[iProblem][problemObj][algorithm] = idxClosest;
                    median[iProblem][problemObj][algorithm] = stats.getPercentile(50.0);
                    median[iProblem][problemObj][algorithm] = Double.parseDouble(precisionControl.format(median[iProblem][problemObj][algorithm]));

                    stdDeviation[iProblem][problemObj][algorithm] = stats.getStandardDeviation();
                    stdDeviation[iProblem][problemObj][algorithm] = Double.parseDouble(precisionControl.format(stdDeviation[iProblem][problemObj][algorithm]));

                    iqr[iProblem][problemObj][algorithm] = stats.getPercentile(75) - stats.getPercentile(25);
                    iqr[iProblem][problemObj][algorithm] = Double.parseDouble(precisionControl.format(iqr[iProblem][problemObj][algorithm]));

                    min[iProblem][problemObj][algorithm] = stats.getMin();
                    max[iProblem][problemObj][algorithm] = stats.getMax();
                    numberOfValues[iProblem][problemObj][algorithm] = (double) indicatorData.size();
                    minRun[iProblem][problemObj][algorithm] = (double) idxRun.get(0);
                    maxRun[iProblem][problemObj][algorithm] = (double) idxRun.get(idxRun.size() - 1);
                    medianRun[iProblem][problemObj][algorithm] = (double) idxRun.get(idxRun.size() / 2);
                }
            }
        }
        JMetalLogger.logger.info("finish computeDataStatistics");
    }


    public void sort(List<Double> data, List<Integer> idx){
        sort(data,idx,true);
    }

    public void sort(List<Double> data, List<Integer> idx, boolean isMin2Max) {
        for (int i = 0; i < data.size(); i++) {
            int p = i;
            for (int j = i + 1; j < data.size(); j++) {
                if ((isMin2Max && data.get(j) < data.get(p)) || (!isMin2Max && data.get(j) > data.get(p)))
                    p = j;
            }
            if (p != i) {
                double tmp = data.get(i);
                data.set(i, data.get(p));
                data.set(p, tmp);
                int tmpIdx = idx.get(i);
                idx.set(i, idx.get(p));
                idx.set(p, tmpIdx);
            }
        }
    }

    private void generateLatexScript(List<List<List<List<Double>>>> data, String latexDirectoryName, String experimentName, String indicatorName, boolean isLowerTheBetter, String[] problemNameList, int[][] problemObjList, int[][] maxIterationsList, String[] algorithmNameList) throws IOException {

        resetPrecisionControl();
        resetPrecisionControlAttach();

        File latexOutput;
        latexOutput = new File(latexDirectoryName);
        if (!latexOutput.exists()) {
            boolean result = new File(latexDirectoryName).mkdirs();
            JMetalLogger.logger.info("Creating " + latexDirectoryName + " directory");
        }
        //System.out.println("Experiment name: " + experimentName_);
        String latexFile = latexDirectoryName + "/" + experimentName + ".tex";
        printHeaderLatexCommands(latexFile, experimentName);

//        for (int i = 0; i < data.size(); i++) {
//        printData(latexFile,indicatorName,isLowerTheBetter, mean, stdDeviation, "Mean and Standard Deviation",problemNameList, problemObjList, algorithmNameList);
//
//        printData(latexFile,indicatorName,isLowerTheBetter, median, iqr, "Median and Interquartile Range",problemNameList,problemObjList, algorithmNameList);
//        }

        printTableSelectedData(latexFile,indicatorName,isLowerTheBetter,experimentName,problemNameList,problemObjList,maxIterationsList,algorithmNameList);

        printSeperatedTableSelectedData(latexFile,indicatorName,isLowerTheBetter,experimentName,problemNameList,problemObjList,maxIterationsList,algorithmNameList);

//        printTableSelectedData(latexFile, indicatorName, isLowerTheBetter, experimentName, problemNameList, problemObjList, maxIterationsList, algorithmNameList);
//
//        printTableSelectedData2(latexFile, indicatorName, isLowerTheBetter, experimentName, problemNameList, problemObjList, maxIterationsList, algorithmNameList);
//
//        printTableSelectedData3(latexFile, indicatorName, isLowerTheBetter, experimentName, problemNameList, problemObjList, maxIterationsList, algorithmNameList);

        printFullData(latexFile, indicatorName, isLowerTheBetter, experimentName, problemNameList, problemObjList, maxIterationsList, algorithmNameList);


        printEndLatexCommands(latexFile);

    }
//    private void generateLatexScript(List<List<List<List<Double>>>> data,String latexDirectoryName,String experimentName,String indicatorName,boolean isLowerTheBetter , String[] problemNameList, int[][] problemObjList, String[] algorithmNameList) throws IOException {
//
//        File latexOutput;
//        latexOutput = new File(latexDirectoryName);
//        if (!latexOutput.exists()) {
//            boolean result = new File(latexDirectoryName).mkdirs();
//            JMetalLogger.logger.info("Creating " + latexDirectoryName + " directory");
//        }
//
//        //System.out.println("Experiment name: " + experimentName_);
//        String latexFile = latexDirectoryName + "/" + experimentName + ".tex";
//        printHeaderLatexCommands(latexFile,experimentName);
//
////        for (int i = 0; i < data.size(); i++) {
//            printData(latexFile,indicatorName,isLowerTheBetter, mean, stdDeviation, "Mean and Standard Deviation",problemNameList, problemObjList, algorithmNameList);
//
//        printData(latexFile,indicatorName,isLowerTheBetter, median, iqr, "Median and Interquartile Range",problemNameList,problemObjList, algorithmNameList);
////        }
//
//        printEndLatexCommands(latexFile);
//
//    }

    /**
     * Computes the statistical values
     *
     * @param values
     * @return
     */
    private Map<String, Double> computeStatistics(List<Double> values) {
        Map<String, Double> results = new HashMap<>();

        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Double value : values) {
            stats.addValue(value);
        }

        results.put("mean", stats.getMean());
        results.put("median", stats.getPercentile(50.0));
        results.put("stdDeviation", stats.getStandardDeviation());
        results.put("iqr", stats.getPercentile(75) - stats.getPercentile(25));
        results.put("max", stats.getMax());
        results.put("min", stats.getMean());
        results.put("numberOfElements", (double) values.size());

//        String text = "\n\nmean:  "+results.get("mean")+"\n" +
//                "median:  "+results.get("median")+"\n" +
//                "stdDeviation:  "+results.get("stdDeviation")+"\n" +
//                "iqr:  "+results.get("iqr")+"\n" +
//                "max:  "+results.get("max")+"\n" +
//                "min:  "+results.get("min")+"\n" +
//                "numberOfElements:  "+results.get("numberOfElements")+"\n" +
//                "\n\n";
//        JMetalLogger.logger.info(text);
        return results;
    }

    void printHeaderLatexCommands(String fileName, String experimentName) throws IOException {
        FileWriter os = new FileWriter(fileName, false);
        os.write("\\documentclass{article}" + "\n");
        os.write("\\title{" + experimentName + "}" + "\n");
        os.write("\\usepackage{colortbl}" + "\n");
        os.write("\\usepackage[table*]{xcolor}" + "\n");
        os.write("\\usepackage{multirow}" + "\n");
        os.write("\\usepackage{booktabs}" + "\n");
        os.write("\\usepackage{supertabular}" + "\n");
        os.write("\\usepackage{longtable}" + "\n");
        os.write("\\usepackage{tabu}" + "\n");
        os.write("\\usepackage{rotating}" + "\n");
//        os.write("\\usepackage[dvips]{graphicx}"+"\n");
        os.write("\\usepackage[caption=false,font=footnotesize]{subfig}" + "\n");
        os.write("\\usepackage{morefloats}" + "\n");
        os.write("\\usepackage[top=0.5in, bottom=0.5in, left=0.5in, right=0.5in]{geometry}\n");
        os.write("\\xdefinecolor{gray95}{gray}{0.65}" + "\n");
        os.write("\\xdefinecolor{gray25}{gray}{0.85}" + "\n");
        os.write("\\author{A.J. Nebro}" + "\n");
        os.write("\\begin{document}" + "\n");
//        os.write("\\maketitle" + "\n");
//        os.write("\\section{Tables}" + "\n");
        os.write("\\makeatletter " + "\n");
        os.write("\\def\\@cline#1-#2\\@nil{% " + "\n");
        os.write("\\omit " + "\n");
        os.write("\\@multicnt#1%" + "\n");
        os.write("\\advance\\@multispan\\m@ne" + "\n");
        os.write("\\ifnum\\@multicnt=\\@ne\\@firstofone{&\\omit}\\fi" + "\n");
        os.write("\\@multicnt#2%" + "\n");
        os.write("\\advance\\@multicnt-#1%" + "\n");
        os.write("\\advance\\@multispan\\@ne" + "\n");
        os.write("\\leaders\\hrule\\@height\\arrayrulewidth\\hfill" + "\n");
        os.write("\\cr" + "\n");
        os.write("\\noalign{\\nobreak\\vskip-\\arrayrulewidth}}" + "\n");
        os.write("\\makeatother" + "\n");
        os.close();
    }

    void printEndLatexCommands(String fileName) throws IOException {
        FileWriter os = new FileWriter(fileName, true);
        os.write("\\end{document}" + "\n");
        os.close();
    }

    private void printFullData(String latexFile, String indicatorName, boolean isLowerTheBetter, String caption, String[] problemNameList, int[][] problemObjList, int[][] maxIterationsList, String[] algorithmNameList) throws IOException {
        for (int i = 0; i < problemNameList.length; i++) {
            printProblemFullTable(latexFile, problemNameList[i], i, indicatorName, isLowerTheBetter, caption, problemObjList[i], maxIterationsList[i], algorithmNameList);
        }
    }

//    private void printTableSelectedData(String latexFile, String indicatorName, boolean isLowerTheBetter, String caption, String[] problemNameList, int[][] problemObjList, int[][] maxIterationsList, String[] algorithmNameList) throws IOException {
//        // Generate header of the table
//        FileWriter os = new FileWriter(latexFile, true);
//        os.write("\n");
////        os.write("\\begin{table}" + "\n");
////        os.write("\\renewcommand{\\arraystretch}{1.2} \n");
////        os.write("\\caption{" +  indicatorName + ". " + caption + "}" + "\n");
////        os.write("\\label{table: " + indicatorName + "}" + "\n");
////        os.write("\\centering" + "\n");
////        os.write("\\begin{scriptsize}" + "\n");
//
//
////        int len = 3 + algorithmNameList.length;
////        os.write("\\begin{center}" + "\n");
////        os.write("\\tablecaption{" +  indicatorName + ". " + caption + "}" + "\n");
////        os.write("\\label{table:" + indicatorName + "}" + "\n");
////        os.write("\\tablefirsthead{"+"\n");
////        os.write("\\toprule" + "\n");
////        os.write("Test Instance & m  & Gen ");
////        for (int i = 0; i < algorithmNameList.length; i++) {
////            os.write(" & "+ algorithmNameList[i].replace("_", "\\_") );
////        }
////        os.write("\\\\ \\midrule"+"\n");
////        os.write("}" + "\n");
////
////        os.write("\\tablehead{"+"\n");
////        os.write("\\multicolumn{"+len+"}{c}{\\small Table \\ref{table:" + indicatorName+"} continue...}\\\\" + "\n");
////        os.write("\\toprule" + "\n");
////        os.write("Test Instance & m ");
////        for (int i = 0; i < algorithmNameList.length; i++) {
////            os.write(" & "+ algorithmNameList[i].replace("_", "\\_") );
////        }
////        os.write("\\\\ \\midrule"+"\n");
////        os.write("}" + "\n");
////
////        os.write("\\tabletail{"+"\n");
////        os.write("\\bottomrule"+"\n");
////        os.write("\\multicolumn{"+len+"}{c}{continue next page ...}\\\\" + "\n");
////        os.write("}" + "\n");
////
////        os.write("\\tablelasttail{\\bottomrule}"+"\n");
////
////
////        os.write("\\begin{supertabular}{c|c|c"+"\n");
////        // calculate the number of columns
////        for (int i = 0; i < algorithmNameList.length; i++) {
////            os.write("|c");
////        }
////        os.write("}" + "\n");
////
////        os.write("\\toprule");
////
//
//        os.write("\\begin{center}" + "\n");
//        os.write("\\begin{longtabu} to\\linewidth{X[3,c]|X[1,c]");
//        // calculate the number of columns
//        for (int i = 0; i < algorithmNameList.length; i++) {
//            os.write("|X[5,c]");
//        }
////        os.write("\\begin{center}" + "\n");
////        os.write("\\begin{longtabu} {c|c|c");
////        // calculate the number of columns
////        for (int i = 0; i < algorithmNameList.length; i++) {
////            os.write("|c");
////        }
//        os.write("}" + "\n");
//        String compareText = "(best, mean, median, worst)";
//        os.write("\\caption{" + indicatorName + ". " + caption + compareText + "}" + "\n");
//        String labelText = "table:" + indicatorName;
//        os.write("\\label{" + labelText + "}\\\\" + "\n");
//
//        // write table head
//        int len = 2 + algorithmNameList.length;
//        String tableHeadText = "";
//        for (int i = -2; i < algorithmNameList.length; i++) {
//            if (i == -2) {
//                tableHeadText += " \\textbf{Test Instance} & ";
//            } else if (i == -1) {
//                tableHeadText += " \\textbf{m} & ";
//            } else if (i == (algorithmNameList.length - 1)) {
//                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "}\\\\" + "\n";
//            } else {
//                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "} & ";
//            }
//        }
//
//        os.write("\\toprule" + "\n");
//        os.write(tableHeadText);
//        os.write("\\endfirsthead" + "\n");
//
//        os.write("\\multicolumn{" + len + "}{c}{\\small Table. \\ref{" + labelText + "} continue ... }\\\\" + "\n");
//        os.write("\\toprule" + "\n");
//        os.write(tableHeadText);
//        os.write("\\hline" + "\n");
//        os.write("\\endhead" + "\n");
//
//        os.write("\\bottomrule" + "\n");
//        os.write("\\multicolumn{" + len + "}{c}{\\small continue next page ...  }\\\\" + "\n");
//        os.write("\\endfoot" + "\n");
//
////        os.write("\\bottomrule"+"\n");
//        os.write("\\endlastfoot" + "\n");
//
//
//        for (int i = 0; i < problemNameList.length; i++) {
//            os.write("\\hline" + "\n");
//            os.write(printProblemSelectedData(indicatorName, isLowerTheBetter, problemNameList[i], i, problemObjList[i], maxIterationsList[i], algorithmNameList.length));
//        }
//        // close table
//        os.write("\\bottomrule" + "\n");
////
//        os.write("\\end{longtabu}" + "\n");
////        os.write("\\end{scriptsize}" + "\n");
////        os.write("\\end{table}" + "\n");
//        os.write("\\end{center}" + "\n");
////        os.write("\\end{supertabular}" + "\n");
////        os.write("\\end{center}"+"\n");
//        os.close();
//    }
//
//
//    private void printTableSelectedData2(String latexFile, String indicatorName, boolean isLowerTheBetter, String caption, String[] problemNameList, int[][] problemObjList, int[][] maxIterationsList, String[] algorithmNameList) throws IOException {
//        // Generate header of the table
//        FileWriter os = new FileWriter(latexFile, true);
//        os.write("\n");
//
//
//        os.write("\\begin{center}" + "\n");
//        os.write("\\begin{longtabu} to\\linewidth{X[3,c]|X[1,c]");
//        // calculate the number of columns
//        for (int i = 0; i < algorithmNameList.length; i++) {
//            os.write("|X[5,c]");
//        }
//
//        os.write("}" + "\n");
//        String compareText = "(best, mean, std)";
//        os.write("\\caption{" + indicatorName + ". " + caption + compareText + "}" + "\n");
//        String labelText = "table:" + indicatorName;
//        os.write("\\label{" + labelText + "}\\\\" + "\n");
//
//        // write table head
//        int len = 2 + algorithmNameList.length;
//        String tableHeadText = "";
//        for (int i = -2; i < algorithmNameList.length; i++) {
//            if (i == -2) {
//                tableHeadText += " \\textbf{Test Instance} & ";
//            } else if (i == -1) {
//                tableHeadText += " \\textbf{m} & ";
//            } else if (i == (algorithmNameList.length - 1)) {
//                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "}\\\\" + "\n";
//            } else {
//                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "} & ";
//            }
//        }
//
//        os.write("\\toprule" + "\n");
//        os.write(tableHeadText);
//        os.write("\\endfirsthead" + "\n");
//
//        os.write("\\multicolumn{" + len + "}{c}{\\small Table. \\ref{" + labelText + "} continue ... }\\\\" + "\n");
//        os.write("\\toprule" + "\n");
//        os.write(tableHeadText);
//        os.write("\\hline" + "\n");
//        os.write("\\endhead" + "\n");
//
//        os.write("\\bottomrule" + "\n");
//        os.write("\\multicolumn{" + len + "}{c}{\\small continue next page ...  }\\\\" + "\n");
//        os.write("\\endfoot" + "\n");
//
////        os.write("\\bottomrule"+"\n");
//        os.write("\\endlastfoot" + "\n");
//
//        for (int i = 0; i < problemNameList.length; i++) {
//            os.write("\\hline" + "\n");
//            os.write(printProblemSelectedData2(indicatorName, isLowerTheBetter, problemNameList[i], i, problemObjList[i], maxIterationsList[i], algorithmNameList.length));
//        }
//
//        os.write("\\bottomrule" + "\n");
//        os.write("\\end{longtabu}" + "\n");
//
//        os.write("\\end{center}" + "\n");
//
//        os.close();
//    }
//
//    private void printTableSelectedData3(String latexFile, String indicatorName, boolean isLowerTheBetter, String caption, String[] problemNameList, int[][] problemObjList, int[][] maxIterationsList, String[] algorithmNameList) throws IOException {
//        // Generate header of the table
//        FileWriter os = new FileWriter(latexFile, true);
//        os.write("\n");
//
//
//        os.write("\\begin{center}" + "\n");
//        os.write("\\begin{longtabu} to\\linewidth{X[3,c]|X[1,c]");
//        // calculate the number of columns
//        for (int i = 0; i < algorithmNameList.length; i++) {
//            os.write("|X[5,c]");
//        }
//
//        os.write("}" + "\n");
//        String compareText = "(best, median, worst)";
//        os.write("\\caption{" + indicatorName + ". " + caption + compareText + "}" + "\n");
//        String labelText = "table:" + indicatorName;
//        os.write("\\label{" + labelText + "}\\\\" + "\n");
//
//        // write table head
//        int len = 2 + algorithmNameList.length;
//        String tableHeadText = "";
//        for (int i = -2; i < algorithmNameList.length; i++) {
//            if (i == -2) {
//                tableHeadText += " \\textbf{Test Instance} & ";
//            } else if (i == -1) {
//                tableHeadText += " \\textbf{m} & ";
//
//            } else if (i == (algorithmNameList.length - 1)) {
//                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "}\\\\" + "\n";
//            } else {
//                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "} & ";
//            }
//        }
//
//        os.write("\\toprule" + "\n");
//        os.write(tableHeadText);
//        os.write("\\endfirsthead" + "\n");
//
//        os.write("\\multicolumn{" + len + "}{c}{\\small Table. \\ref{" + labelText + "} continue ... }\\\\" + "\n");
//        os.write("\\toprule" + "\n");
//        os.write(tableHeadText);
//        os.write("\\hline" + "\n");
//        os.write("\\endhead" + "\n");
//
//        os.write("\\bottomrule" + "\n");
//        os.write("\\multicolumn{" + len + "}{c}{\\small continue next page ...  }\\\\" + "\n");
//        os.write("\\endfoot" + "\n");
//
////        os.write("\\bottomrule"+"\n");
//        os.write("\\endlastfoot" + "\n");
//
//        for (int i = 0; i < problemNameList.length; i++) {
//            os.write("\\hline" + "\n");
//            os.write(printProblemSelectedData3(indicatorName, isLowerTheBetter, problemNameList[i], i, problemObjList[i], maxIterationsList[i], algorithmNameList.length));
//        }
//
//        os.write("\\bottomrule" + "\n");
//        os.write("\\end{longtabu}" + "\n");
//
//        os.write("\\end{center}" + "\n");
//
//        os.close();
//    }

    private void printProblemFullTable(String latexFile, String problemName, int problemIdx, String indicatorName, boolean isLowerTheBetter, String caption, int[] problemObjList, int[] maxIterationsList, String[] algorithmNameList) throws IOException {
        // Generate header of the table
        FileWriter os = new FileWriter(latexFile, true);
        os.write("\n");
//        os.write("\\begin{table}" + "\n");
//        os.write("\\renewcommand{\\arraystretch}{1.2} \n");
//        os.write("\\caption{" + problemName.replace("_","") + "." + indicatorName.replace("_","") + ". " + caption + "}" + "\n");
//        os.write("\\label{table:" + problemName.replace("_","") + ":" + indicatorName.replace("_","") + "}" + "\n");
//        os.write("\\centering" + "\n");
//        os.write("\\begin{scriptsize}" + "\n");
        os.write("\\begin{center}" + "\n");
        os.write("\\begin{longtabu} to\\linewidth{X[3,c]|X[1,c]");
        // calculate the number of columns
        for (int i = 0; i < algorithmNameList.length; i++) {
            os.write("|X[5,c]");
        }
//        os.write("\\begin{longtabu} {c|c|c");
//        // calculate the number of columns
//        for (int i = 0; i < algorithmNameList.length; i++) {
//            os.write("|c");
//        }
        os.write("}" + "\n");
        String compareText = "(best, mean, std, median, iqr, worst)";
        os.write("\\caption{" + problemName.replace("_", "") + "." + indicatorName.replace("_", "") + compareText + "}" + "\n");
        String labelText = "table:" + problemName.replace("_", "") + ":" + indicatorName.replace("_", "");
        os.write("\\label{" + labelText + "}\\\\" + "\n");

        // write table head
        int len = 2 + algorithmNameList.length;
        String tableHeadText = "";
        for (int i = -2; i < algorithmNameList.length; i++) {
            if (i == -2) {
                tableHeadText += " \\textbf{Test Instance}& ";
            } else if (i == -1) {
                tableHeadText += " \\textbf{m} & ";

            } else if (i == (algorithmNameList.length - 1)) {
                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "}\\\\" + "\n";
            } else {
                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "} & ";
            }
        }

        os.write("\\toprule" + "\n");
        os.write(tableHeadText);
        os.write("\\endfirsthead" + "\n");

        os.write("\\multicolumn{" + len + "}{c}{\\small Table. \\ref{" + labelText + "} continue ... }\\\\" + "\n");
        os.write("\\toprule" + "\n");
        os.write(tableHeadText);
        os.write("\\hline" + "\n");
        os.write("\\endhead" + "\n");

        os.write("\\bottomrule" + "\n");
        os.write("\\multicolumn{" + len + "}{c}{\\small continue next page ...  }\\\\" + "\n");
        os.write("\\endfoot" + "\n");

//        os.write("\\bottomrule"+"\n");
        os.write("\\endlastfoot" + "\n");

//        // calculate the number of columns
//        for (int i = 0; i < algorithmNameList.length; i++) {
//            os.write("|X[4]");
//        }
//        os.write("}" + "\n");
//
//        os.write("\\toprule" + "\n");
//        // write table head
//        for (int i = -3; i < algorithmNameList.length; i++) {
//            if (i == -3) {
//                os.write(" Test Instance & ");
//            }else if(i == -2){
//                os.write(" m & ");
//            } else if(i == -1){
//                os.write(" Gen & ");
//            }else if (i == (algorithmNameList.length - 1)) {
//                os.write(" " + algorithmNameList[i].replace("_", "\\_") + "\\\\" + "\n");
//            } else {
//                os.write("" + algorithmNameList[i].replace("_", "\\_") + " & ");
//            }
//        }


//        int len = 3 + algorithmNameList.length;
//        os.write("\\begin{center}" + "\n");
//        os.write("\\tablecaption{" + problemName.replace("_","") + "." + indicatorName.replace("_","") + ". " + caption + "}" + "\n");
//        os.write("\\label{table:" + problemName.replace("_","") + ":" + indicatorName.replace("_","") + "}" + "\n");
//        os.write("\\tablefirsthead{"+"\n");
//        os.write("\\toprule" + "\n");
//        os.write("Test Instance & m ");
//        for (int i = 0; i < algorithmNameList.length; i++) {
//            os.write(" & "+ algorithmNameList[i].replace("_", "\\_") );
//        }
//        os.write("\\\\ \\midrule"+"\n");
//        os.write("}" + "\n");
//
//        os.write("\\tablehead{"+"\n");
//        os.write("\\multicolumn{"+len+"}{c}{\\small Table \\ref{table:" + problemName.replace("_","") + ":" + indicatorName.replace("_","") +"} continue...}\\\\" + "\n");
//        os.write("\\toprule" + "\n");
//        os.write("Test Instance & m ");
//        for (int i = 0; i < algorithmNameList.length; i++) {
//            os.write(" & "+ algorithmNameList[i].replace("_", "\\_") );
//        }
//        os.write("\\\\ \\midrule"+"\n");
//        os.write("}" + "\n");
//
//        os.write("\\tabletail{"+"\n");
//        os.write("\\bottomrule"+"\n");
//        os.write("\\multicolumn{"+len+"}{c}{continue next page ...}\\\\" + "\n");
//        os.write("}" + "\n");
//
//        os.write("\\tablelasttail{\\bottomrule}"+"\n");
//
//
//        os.write("\\begin{supertabular}{c|c|c"+"\n");
//        // calculate the number of columns
//        for (int i = 0; i < algorithmNameList.length; i++) {
//            os.write("|c");
//        }
//        os.write("}" + "\n");

        os.write("\\hline" + "\n");
        os.write(printProblemFullData(indicatorName, isLowerTheBetter, problemName, problemIdx, problemObjList,maxIterationsList, algorithmNameList.length));

        // close table
        os.write("\\bottomrule" + "\n");

        os.write("\\end{longtabu}" + "\n");
//        os.write("\\end{scriptsize}" + "\n");
//        os.write("\\end{table}" + "\n");
        os.write("\\end{center}" + "\n");
//        os.write("\\end{supertabular}" + "\n");
//        os.write("\\end{center}"+"\n");
        os.close();
    }

    private String printProblemFullData(String indicatorName, boolean isLowerTheBetter, String problemName, int problemIdx, int[] problemObjList, int[] maxIterationsList, int algorithmNum) throws IOException {
        String outputText = "";

//        outputText += "\\midrule"+"\n";
        // write lines
        int sizeProblemObj = problemObjList.length;
        int l = sizeProblemObj * 6;
        outputText += "\\multirow{" + l + "}*{\\begin{sideways} \\textbf{" + problemName.replace("_", "\\_") + "}\\end{sideways}} \n";

        List<Integer> ranks = null;
        int len = 2 + algorithmNum;
        for (int i = 0; i < sizeProblemObj; i++) {
            List<Integer> bestOrderIdx = null;

            outputText += "& \\multirow{" + 6 + "}*{" + problemObjList[i] + "} & " + "\n";
            //best
            if (isLowerTheBetter) {
                ranks = calcComparasionRanks(min[problemIdx][i], isLowerTheBetter);
                outputText += printLineWithRank(min[problemIdx][i], minRun[problemIdx][i], ranks);

            } else {
                ranks = calcComparasionRanks(max[problemIdx][i],isLowerTheBetter);
                outputText += printLineWithRank(max[problemIdx][i], maxRun[problemIdx][i], ranks);

            }
            outputText += " \\nopagebreak" + "\n";
//            outputText += "\\cline{4-" + len + "}";
            //mean
            outputText += "&  & ";
            ranks = calcComparasionRanks(mean[problemIdx][i], isLowerTheBetter);
            outputText += printLineWithRank(mean[problemIdx][i], closestMeanRun[problemIdx][i], ranks);
            outputText += " \\nopagebreak" + "\n";
//            outputText += "\\cline{4-" + len + "}";
            //stdDeviation
            outputText += "&  & ";
            ranks = calcComparasionRanks(stdDeviation[problemIdx][i],true);
            outputText += printLineWithRank(stdDeviation[problemIdx][i], ranks);
            outputText += " \\nopagebreak" + "\n";
//            outputText += "\\cline{4-" + len + "}";
            //median
            outputText += "& & ";
            ranks = calcComparasionRanks(median[problemIdx][i], isLowerTheBetter);
            outputText += printLineWithRank(median[problemIdx][i], medianRun[problemIdx][i],ranks);
            outputText += " \\nopagebreak" + "\n";
//            outputText += "\\cline{4-" + len + "}";
            //iqr
            outputText += "&  & ";
            ranks = calcComparasionRanks(iqr[problemIdx][i],true);
            outputText += printLineWithRank(iqr[problemIdx][i], ranks);
            outputText += " \\nopagebreak" + "\n";
//            outputText += "\\cline{4-" + len + "}";
            //worst
            outputText += "&  & ";
            if (isLowerTheBetter) {
                ranks = calcComparasionRanks(max[problemIdx][i], isLowerTheBetter);
                outputText += printLineWithRank(max[problemIdx][i], maxRun[problemIdx][i], ranks);
            } else {
                ranks = calcComparasionRanks(min[problemIdx][i],isLowerTheBetter);
                outputText += printLineWithRank(min[problemIdx][i], minRun[problemIdx][i], ranks);
            }
            if (i != sizeProblemObj - 1)
//                outputText += " \\nopagebreak" + "\n";
                outputText += "\\cline{2-" + len + "}";

        }
//        outputText += "\\midrule" + "\n";
        return outputText;
    }

    private void printTableSelectedData(String latexFile, String indicatorName, boolean isLowerTheBetter, String caption, String[] problemNameList, int[][] problemObjList, int[][] maxIterationsList, String[] algorithmNameList) throws IOException {
        // Generate header of the table
        FileWriter os = new FileWriter(latexFile, true);
        os.write("\n");

        os.write("\\begin{center}" + "\n");
        os.write("\\begin{longtabu} to\\linewidth{X[3,c]|X[1,c]");
        // calculate the number of columns
        for (int i = 0; i < algorithmNameList.length; i++) {
            os.write("|X[5,c]");
        }

        os.write("}" + "\n");
        String compareText = "(best, mean, std)";
        os.write("\\caption{" + indicatorName + ". " + caption + compareText + "}" + "\n");
        String labelText = "table:" + indicatorName;
        os.write("\\label{" + labelText + "}\\\\" + "\n");

        // write table head
        int len = 2 + algorithmNameList.length;
        String tableHeadText = "";
        for (int i = -2; i < algorithmNameList.length; i++) {
            if (i == -2) {
                tableHeadText += " \\textbf{Test Instance} & ";
            } else if (i == -1) {
                tableHeadText += " \\textbf{m} & ";
            } else if (i == (algorithmNameList.length - 1)) {
                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "}\\\\" + "\n";
            } else {
                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "} & ";
            }
        }

        os.write("\\toprule" + "\n");
        os.write(tableHeadText);
        os.write("\\endfirsthead" + "\n");

        os.write("\\multicolumn{" + len + "}{c}{\\small Table. \\ref{" + labelText + "} continue ... }\\\\" + "\n");
        os.write("\\toprule" + "\n");
        os.write(tableHeadText);
        os.write("\\hline" + "\n");
        os.write("\\endhead" + "\n");

        os.write("\\bottomrule" + "\n");
        os.write("\\multicolumn{" + len + "}{c}{\\small continue next page ...  }\\\\" + "\n");
        os.write("\\endfoot" + "\n");

//        os.write("\\bottomrule"+"\n");
        os.write("\\endlastfoot" + "\n");

        for (int i = 0; i < problemNameList.length; i++) {
            os.write("\\hline" + "\n");
            os.write(printProblemSelectedData(indicatorName, isLowerTheBetter, problemNameList[i], i, problemObjList[i], maxIterationsList[i], algorithmNameList.length));
        }

        os.write("\\bottomrule" + "\n");
        os.write("\\end{longtabu}" + "\n");

        os.write("\\end{center}" + "\n");

        os.close();
    }

    private void printSeperatedTableSelectedData(String latexFile, String indicatorName, boolean isLowerTheBetter, String caption, String[] problemNameList, int[][] problemObjList, int[][] maxIterationsList, String[] algorithmNameList) throws IOException {
        for (int i = 0; i < problemNameList.length; i++) {
            printProblemTableSelectedData(latexFile, problemNameList[i], i, indicatorName, isLowerTheBetter, caption, problemObjList[i], maxIterationsList[i], algorithmNameList);
        }
    }

    private void printProblemTableSelectedData(String latexFile, String problemName, int problemIdx, String indicatorName, boolean isLowerTheBetter, String caption, int[] problemObjList, int[] maxIterationsList, String[] algorithmNameList) throws IOException {
        // Generate header of the table
        FileWriter os = new FileWriter(latexFile, true);
        os.write("\n");
//        os.write("\\begin{table}" + "\n");
//        os.write("\\renewcommand{\\arraystretch}{1.2} \n");
//        os.write("\\caption{" + problemName.replace("_","") + "." + indicatorName.replace("_","") + ". " + caption + "}" + "\n");
//        os.write("\\label{table:" + problemName.replace("_","") + ":" + indicatorName.replace("_","") + "}" + "\n");
//        os.write("\\centering" + "\n");
//        os.write("\\begin{scriptsize}" + "\n");
        os.write("\\begin{center}" + "\n");
        os.write("\\begin{longtabu} to\\linewidth{X[3,c]|X[1,c]");
        // calculate the number of columns
        for (int i = 0; i < algorithmNameList.length; i++) {
            os.write("|X[5,c]");
        }
//        os.write("\\begin{longtabu} {c|c|c");
//        // calculate the number of columns
//        for (int i = 0; i < algorithmNameList.length; i++) {
//            os.write("|c");
//        }
        os.write("}" + "\n");
        String compareText = "(best, mean, std)";
        os.write("\\caption{" + problemName.replace("_", "") + "." + indicatorName.replace("_", "") + compareText + "}" + "\n");
        String labelText = "table:" + problemName.replace("_", "") + ":" + indicatorName.replace("_", "");
        os.write("\\label{" + labelText + "}\\\\" + "\n");

        // write table head
        int len = 2 + algorithmNameList.length;
        String tableHeadText = "";
        for (int i = -2; i < algorithmNameList.length; i++) {
            if (i == -2) {
                tableHeadText += " \\textbf{Test Instance}& ";
            } else if (i == -1) {
                tableHeadText += " \\textbf{m} & ";

            } else if (i == (algorithmNameList.length - 1)) {
                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "}\\\\" + "\n";
            } else {
                tableHeadText += " \\textbf{" + algorithmNameList[i].replace("_", "\\_") + "} & ";
            }
        }

        os.write("\\toprule" + "\n");
        os.write(tableHeadText);
        os.write("\\endfirsthead" + "\n");

        os.write("\\multicolumn{" + len + "}{c}{\\small Table. \\ref{" + labelText + "} continue ... }\\\\" + "\n");
        os.write("\\toprule" + "\n");
        os.write(tableHeadText);
        os.write("\\hline" + "\n");
        os.write("\\endhead" + "\n");

        os.write("\\bottomrule" + "\n");
        os.write("\\multicolumn{" + len + "}{c}{\\small continue next page ...  }\\\\" + "\n");
        os.write("\\endfoot" + "\n");

//        os.write("\\bottomrule"+"\n");
        os.write("\\endlastfoot" + "\n");

        os.write("\\hline" + "\n");
        os.write(printProblemSelectedData(indicatorName, isLowerTheBetter, problemName, problemIdx, problemObjList,maxIterationsList, algorithmNameList.length));

        // close table
        os.write("\\bottomrule" + "\n");

        os.write("\\end{longtabu}" + "\n");
//        os.write("\\end{scriptsize}" + "\n");
//        os.write("\\end{table}" + "\n");
        os.write("\\end{center}" + "\n");
//        os.write("\\end{supertabular}" + "\n");
//        os.write("\\end{center}"+"\n");
        os.close();
    }

    private String printProblemSelectedData(String indicatorName, boolean isLowerTheBetter, String problemName, int problemIdx, int[] problemObjList, int[] maxIterationsList, int algorithmNum) throws IOException {
        String outputText = "";

//        outputText += "\\midrule" + "\n";

        // write lines
        int sizeProblemObj = problemObjList.length;
        int l = sizeProblemObj * 3;
        outputText += "\\multirow{" + l + "}*{\\begin{sideways} \\textbf{" + problemName.replace("_", "\\_") + "}\\end{sideways}} \n";

        List<Integer> ranks = null;
        int len = 2 + algorithmNum;
        for (int i = 0; i < sizeProblemObj; i++) {
            List<Integer> bestOrderIdx = null;

            outputText += "& \\multirow{" + 3 + "}*{" + problemObjList[i] + "} & " + "\n";
            //best
            if (isLowerTheBetter) {
                ranks = calcComparasionRanks(min[problemIdx][i],isLowerTheBetter);
                outputText += printLineWithRank(min[problemIdx][i], ranks);
            } else {
                ranks = calcComparasionRanks(max[problemIdx][i],isLowerTheBetter);
                outputText += printLineWithRank(max[problemIdx][i], ranks);
            }

            outputText += " \\nopagebreak" + "\n";
//            outputText += "\\cline{4-" + len + "}";
            //mean
            outputText += "&  & ";
            ranks = calcComparasionRanks(mean[problemIdx][i],isLowerTheBetter);
            outputText += printLineWithRank(mean[problemIdx][i], ranks);
            outputText += " \\nopagebreak" + "\n";
//            outputText += "\\cline{4-" + len + "}";
//            //stdDeviation
//            outputText += "&  & ";
//            ranks = calcComparasionRanks(stdDeviation[problemIdx][i],true);
//            outputText += printLineWithRank(stdDeviation[problemIdx][i], ranks);
////            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
            //worst
            outputText += "&  & ";
            if (isLowerTheBetter) {
                ranks = calcComparasionRanks(max[problemIdx][i],isLowerTheBetter);
                outputText += printLineWithRank(max[problemIdx][i], ranks);
            } else {
                ranks = calcComparasionRanks(min[problemIdx][i],isLowerTheBetter);
                outputText += printLineWithRank(min[problemIdx][i], ranks);
            }
            if (i != sizeProblemObj - 1)
//                outputText += " \\nopagebreak" + "\n";
                outputText += "\\cline{2-" + len + "}";
        }
//        outputText += "\\midrule" + "\n";

        return outputText;
    }

//    private String printProblemSelectedData(String indicatorName, boolean isLowerTheBetter, String problemName, int problemIdx, int[] problemObjList, int[] maxIterationsList, int algorithmNum) throws IOException {
//        String outputText = "";
//
////        outputText += "\\midrule" + "\n";
//
//        // write lines
//        int sizeProblemObj = problemObjList.length;
//        int l = sizeProblemObj * 4;
//        outputText += "\\multirow{" + l + "}*{\\begin{sideways} \\textbf{" + problemName.replace("_", "\\_") + "}\\end{sideways}} \n";
//
//        List<Integer> bestIdx = new ArrayList<>(1);
//        List<Integer> secondBestIdx = new ArrayList<>(1);
//        List<Integer> signState = null;
//        int len = 2 + algorithmNum;
//        for (int i = 0; i < sizeProblemObj; i++) {
//            List<Integer> bestOrderIdx = null;
//
//            outputText += "& \\multirow{" + 4 + "}*{" + problemObjList[i] + "} & " + "\n";
//            //best
//            if (isLowerTheBetter) {
////                bestOrderIdx = findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(min[problemIdx][i], minRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
//                //findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter,mean[problemIdx][i],isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(min[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(min[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(min[problemIdx][i], signState);
//
//            } else {
////                bestOrderIdx = findBestAndSecondBestIdx(max[problemIdx][i],isLowerTheBetter,mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(max[problemIdx][i], maxRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
//                //findBestAndSecondBestIdx(max[problemIdx][i],isLowerTheBetter,mean[problemIdx][i],isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(max[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(max[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(max[problemIdx][i], signState);
//
//            }
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//            //mean
//            outputText += "&  & ";
////            bestOrderIdx = findBestAndSecondBestIdx(mean[problemIdx][i],isLowerTheBetter,stdDeviation[problemIdx][i],true);
////            outputText += printLine(mean[problemIdx][i],closestMeanRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
//            //findBestAndSecondBestIdx(mean[problemIdx][i],isLowerTheBetter, stdDeviation[problemIdx][i],true,bestIdx,secondBestIdx);
//            findBestAndSecondBestIdx(mean[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//            signState = makeSignState(mean[problemIdx][i].length, bestIdx, secondBestIdx);
//            outputText += printLine(mean[problemIdx][i], signState);
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//
//            //median
//            outputText += "& & ";
////            bestOrderIdx = findBestAndSecondBestIdx(median[problemIdx][i], isLowerTheBetter,iqr[problemIdx][i],true);
////            outputText += printLine(median[problemIdx][i],medianRun[problemIdx][i],bestOrderIdx.get(0), bestOrderIdx.get(1));
//            //findBestAndSecondBestIdx(median[problemIdx][i], isLowerTheBetter,iqr[problemIdx][i],true,bestIdx,secondBestIdx);
//            findBestAndSecondBestIdx(median[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//            signState = makeSignState(median[problemIdx][i].length, bestIdx, secondBestIdx);
//            outputText += printLine(median[problemIdx][i], signState);
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//
//            //worst
//            outputText += "&  & ";
//            if (isLowerTheBetter) {
////                bestOrderIdx = findBestAndSecondBestIdx(max[problemIdx][i], mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(max[problemIdx][i], maxRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
//                //findBestAndSecondBestIdx(max[problemIdx][i], isLowerTheBetter,mean[problemIdx][i], isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(max[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(max[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(max[problemIdx][i], signState);
//            } else {
////                bestOrderIdx = findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(min[problemIdx][i], minRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
//                //findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(min[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(min[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(min[problemIdx][i], signState);
//            }
//            if (i != sizeProblemObj - 1)
////                outputText += " \\nopagebreak" + "\n";
//                outputText += "\\cline{2-" + len + "}";
//
//        }
////        outputText += "\\midrule" + "\n";
//
//        return outputText;
//    }
//
//
//    private String printProblemSelectedData2(String indicatorName, boolean isLowerTheBetter, String problemName, int problemIdx, int[] problemObjList, int[] maxIterationsList, int algorithmNum) throws IOException {
//        String outputText = "";
//
////        outputText += "\\midrule" + "\n";
//
//        // write lines
//        int sizeProblemObj = problemObjList.length;
//        int l = sizeProblemObj * 3;
//        outputText += "\\multirow{" + l + "}*{\\begin{sideways} \\textbf{" + problemName.replace("_", "\\_") + "}\\end{sideways}} \n";
//
//        List<Integer> bestIdx = new ArrayList<>(1);
//        List<Integer> secondBestIdx = new ArrayList<>(1);
//        List<Integer> signState = null;
//        int len = 2 + algorithmNum;
//        for (int i = 0; i < sizeProblemObj; i++) {
//            List<Integer> bestOrderIdx = null;
//
//            outputText += "& \\multirow{" + 3 + "}*{" + problemObjList[i] + "} & " + "\n";
//            //best
//            if (isLowerTheBetter) {
////                bestOrderIdx = findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(min[problemIdx][i], minRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
////                findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter,mean[problemIdx][i],isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(min[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(min[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(min[problemIdx][i], signState);
//
//            } else {
////                bestOrderIdx = findBestAndSecondBestIdx(max[problemIdx][i],isLowerTheBetter,mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(max[problemIdx][i], maxRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
////                findBestAndSecondBestIdx(max[problemIdx][i],isLowerTheBetter,mean[problemIdx][i],isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(max[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(max[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(max[problemIdx][i], signState);
//
//            }
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//            //mean
//            outputText += "&  & ";
////            bestOrderIdx = findBestAndSecondBestIdx(mean[problemIdx][i],isLowerTheBetter,stdDeviation[problemIdx][i],true);
////            outputText += printLine(mean[problemIdx][i],closestMeanRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
////            findBestAndSecondBestIdx(mean[problemIdx][i],isLowerTheBetter, stdDeviation[problemIdx][i],true,bestIdx,secondBestIdx);
//            findBestAndSecondBestIdx(mean[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//            signState = makeSignState(mean[problemIdx][i].length, bestIdx, secondBestIdx);
//            outputText += printLine(mean[problemIdx][i], signState);
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//            //stdDeviation
//            outputText += "&  & ";
////            bestOrderIdx = findBestAndSecondBestIdx(stdDeviation[problemIdx][i],true);
////            outputText += printLine(stdDeviation[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
//            findBestAndSecondBestIdx(stdDeviation[problemIdx][i], true, bestIdx, secondBestIdx);
//            signState = makeSignState(stdDeviation[problemIdx][i].length, bestIdx, secondBestIdx);
//            outputText += printLine(stdDeviation[problemIdx][i], signState);
////            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//
//            if (i != sizeProblemObj - 1)
////                outputText += " \\nopagebreak" + "\n";
//                outputText += "\\cline{2-" + len + "}";
//
//        }
////        outputText += "\\midrule" + "\n";
//
//        return outputText;
//    }
//
//    private String printProblemSelectedData3(String indicatorName, boolean isLowerTheBetter, String problemName, int problemIdx, int[] problemObjList, int[] maxIterationsList, int algorithmNum) throws IOException {
//        String outputText = "";
//
////        outputText += "\\midrule" + "\n";
//
//        // write lines
//        int sizeProblemObj = problemObjList.length;
//        int l = sizeProblemObj * 3;
//        outputText += "\\multirow{" + l + "}*{\\begin{sideways} \\textbf{" + problemName.replace("_", "\\_") + "}\\end{sideways}} \n";
//
//        List<Integer> bestIdx = new ArrayList<>(1);
//        List<Integer> secondBestIdx = new ArrayList<>(1);
//        List<Integer> signState = null;
//        int len = 2 + algorithmNum;
//        for (int i = 0; i < sizeProblemObj; i++) {
//            List<Integer> bestOrderIdx = null;
//
//            outputText += "& \\multirow{" + 3 + "}*{" + problemObjList[i] + "} & " + "\n";
//            //best
//            if (isLowerTheBetter) {
////                bestOrderIdx = findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(min[problemIdx][i], minRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
////                findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter,mean[problemIdx][i],isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(min[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(min[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(min[problemIdx][i], signState);
//
//            } else {
////                bestOrderIdx = findBestAndSecondBestIdx(max[problemIdx][i],isLowerTheBetter,mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(max[problemIdx][i], maxRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
////                findBestAndSecondBestIdx(max[problemIdx][i],isLowerTheBetter,mean[problemIdx][i],isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(max[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(max[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(max[problemIdx][i], signState);
//
//            }
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//
//            //median
//            outputText += "& & ";
////            bestOrderIdx = findBestAndSecondBestIdx(median[problemIdx][i], isLowerTheBetter,iqr[problemIdx][i],true);
////            outputText += printLine(median[problemIdx][i],medianRun[problemIdx][i],bestOrderIdx.get(0), bestOrderIdx.get(1));
////            findBestAndSecondBestIdx(median[problemIdx][i],isLowerTheBetter, iqr[problemIdx][i],true,bestIdx,secondBestIdx);
//            findBestAndSecondBestIdx(median[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//            signState = makeSignState(median[problemIdx][i].length, bestIdx, secondBestIdx);
//            outputText += printLine(median[problemIdx][i], signState);
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//
//            //worst
//            outputText += "&  & ";
//            if (isLowerTheBetter) {
////                bestOrderIdx = findBestAndSecondBestIdx(max[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(max[problemIdx][i], maxRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
////                findBestAndSecondBestIdx(max[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(max[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(max[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(max[problemIdx][i], signState);
//            } else {
////                bestOrderIdx = findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(min[problemIdx][i], minRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
////                findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(min[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(min[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(min[problemIdx][i], signState);
//            }
//            if (i != sizeProblemObj - 1)
////                outputText += " \\nopagebreak" + "\n";
//                outputText += "\\cline{2-" + len + "}";
//
//        }
//
//        return outputText;
//    }
//
//
//    private String printProblemFullData(String indicatorName, boolean isLowerTheBetter, String problemName, int problemIdx, int[] problemObjList, int[] maxIterationsList, int algorithmNum) throws IOException {
//        String outputText = "";
//
////        outputText += "\\midrule"+"\n";
//        // write lines
//        int sizeProblemObj = problemObjList.length;
//        int l = sizeProblemObj * 6;
//        outputText += "\\multirow{" + l + "}*{\\begin{sideways} \\textbf{" + problemName.replace("_", "\\_") + "}\\end{sideways}} \n";
//
//        List<Integer> bestIdx = new ArrayList<>(1);
//        List<Integer> secondBestIdx = new ArrayList<>(1);
//        List<Integer> signState = null;
//        int len = 2 + algorithmNum;
//        for (int i = 0; i < sizeProblemObj; i++) {
//            List<Integer> bestOrderIdx = null;
//
//            outputText += "& \\multirow{" + 6 + "}*{" + problemObjList[i] + "} & " + "\n";
//            //best
//            if (isLowerTheBetter) {
////                bestOrderIdx = findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(min[problemIdx][i], minRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
////                findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter,mean[problemIdx][i],isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(min[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(min[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(min[problemIdx][i], minRun[problemIdx][i], signState);
//
//            } else {
////                bestOrderIdx = findBestAndSecondBestIdx(max[problemIdx][i],isLowerTheBetter,mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(max[problemIdx][i], maxRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
////                findBestAndSecondBestIdx(max[problemIdx][i],isLowerTheBetter,mean[problemIdx][i],isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(max[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(max[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(max[problemIdx][i], maxRun[problemIdx][i], signState);
//
//            }
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//            //mean
//            outputText += "&  & ";
////            bestOrderIdx = findBestAndSecondBestIdx(mean[problemIdx][i],isLowerTheBetter,stdDeviation[problemIdx][i],true);
////            outputText += printLine(mean[problemIdx][i],closestMeanRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
//            //findBestAndSecondBestIdx(mean[problemIdx][i],isLowerTheBetter, stdDeviation[problemIdx][i],true,bestIdx,secondBestIdx);
//            findBestAndSecondBestIdx(mean[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//            signState = makeSignState(mean[problemIdx][i].length, bestIdx, secondBestIdx);
//            outputText += printLine(mean[problemIdx][i], closestMeanRun[problemIdx][i], signState);
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//            //stdDeviation
//            outputText += "&  & ";
////            bestOrderIdx = findBestAndSecondBestIdx(stdDeviation[problemIdx][i],true);
////            outputText += printLine(stdDeviation[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
//            findBestAndSecondBestIdx(stdDeviation[problemIdx][i], true, bestIdx, secondBestIdx);
//            signState = makeSignState(stdDeviation[problemIdx][i].length, bestIdx, secondBestIdx);
//            outputText += printLine(stdDeviation[problemIdx][i], signState);
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//            //median
//            outputText += "& & ";
////            bestOrderIdx = findBestAndSecondBestIdx(median[problemIdx][i],isLowerTheBetter, iqr[problemIdx][i],true);
////            outputText += printLine(median[problemIdx][i],medianRun[problemIdx][i],bestOrderIdx.get(0), bestOrderIdx.get(1));
////            findBestAndSecondBestIdx(median[problemIdx][i],isLowerTheBetter, iqr[problemIdx][i],true,bestIdx,secondBestIdx);
//            findBestAndSecondBestIdx(median[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//            signState = makeSignState(median[problemIdx][i].length, bestIdx, secondBestIdx);
//            outputText += printLine(median[problemIdx][i], medianRun[problemIdx][i], signState);
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//            //iqr
//            outputText += "&  & ";
////            bestOrderIdx = findBestAndSecondBestIdx(iqr[problemIdx][i], true);
////            outputText += printLine(iqr[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
//            findBestAndSecondBestIdx(iqr[problemIdx][i], true, bestIdx, secondBestIdx);
//            signState = makeSignState(iqr[problemIdx][i].length, bestIdx, secondBestIdx);
//            outputText += printLine(iqr[problemIdx][i], signState);
//            outputText += " \\nopagebreak" + "\n";
////            outputText += "\\cline{4-" + len + "}";
//            //worst
//            outputText += "&  & ";
//            if (isLowerTheBetter) {
////                bestOrderIdx = findBestAndSecondBestIdx(max[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(max[problemIdx][i], maxRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
////                findBestAndSecondBestIdx(max[problemIdx][i], isLowerTheBetter,mean[problemIdx][i], isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(max[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(max[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(max[problemIdx][i], maxRun[problemIdx][i], signState);
//            } else {
////                bestOrderIdx = findBestAndSecondBestIdx(min[problemIdx][i],isLowerTheBetter, mean[problemIdx][i], isLowerTheBetter);
////                outputText += printLine(min[problemIdx][i], minRun[problemIdx][i], bestOrderIdx.get(0), bestOrderIdx.get(1));
////                findBestAndSecondBestIdx(min[problemIdx][i], isLowerTheBetter,mean[problemIdx][i], isLowerTheBetter,bestIdx,secondBestIdx);
//                findBestAndSecondBestIdx(min[problemIdx][i], isLowerTheBetter, bestIdx, secondBestIdx);
//                signState = makeSignState(min[problemIdx][i].length, bestIdx, secondBestIdx);
//                outputText += printLine(min[problemIdx][i], minRun[problemIdx][i], signState);
//            }
//            if (i != sizeProblemObj - 1)
////                outputText += " \\nopagebreak" + "\n";
//                outputText += "\\cline{2-" + len + "}";
//
//        }
////        outputText += "\\midrule" + "\n";
//        return outputText;
//    }



//    public String printLine(double[] data, List<Integer> signState) {
//        String outputText = "  ";
//
//        for (int j = 0; j < (data.length - 1); j++) {
//            if (signState.get(j) == SIGNSTATE_BEST) {
//                outputText += "\\cellcolor{gray95}";
//                outputText += "\\textbf{";
//            }
//            if (signState.get(j) == SIGNSTATE_SECONDBEST) {
//                outputText += "\\cellcolor{gray25}";
//            }
//
//            outputText += "" + precisionControl.format(data[j]);
//            //String.format(Locale.ENGLISH, precision, data[j]) ;
//            if (signState.get(j) == SIGNSTATE_BEST) {
//                outputText += "}";
//            }
//            outputText += "& ";
//        }
//        if (SIGNSTATE_BEST == signState.get(data.length - 1)) {
//            outputText += "\\cellcolor{gray95}";
//            outputText += "\\textbf{";
//        }
//        if (SIGNSTATE_SECONDBEST == signState.get(data.length - 1)) {
//            outputText += "\\cellcolor{gray25}";
//        }
//        outputText += "" + precisionControl.format(data[data.length - 1]);
//        //String.format(Locale.ENGLISH, precision, data[data.length - 1]);
//
//        if (SIGNSTATE_BEST == signState.get(data.length - 1)) {
//            outputText += "}";
//        }
//        outputText += "\\\\" + "\n";
//        return outputText;
//    }
//
//    public String printLine(double[] data, double[] attach, List<Integer> signState) {
//        String outputText = " 　";
//        for (int j = 0; j < (data.length - 1); j++) {
//            if (signState.get(j) == SIGNSTATE_BEST) {
//                outputText += "\\cellcolor{gray95}";
//                outputText += "\\textbf{";
//            }
//            if (signState.get(j) == SIGNSTATE_SECONDBEST) {
//                outputText += "\\cellcolor{gray25}";
//            }
//
//            String m = precisionControl.format(data[j]);
//            //String.format(Locale.ENGLISH, precision, data[j]) ;
//            String s = precisionControlAttach.format(attach[j]);
//            //String.format(Locale.ENGLISH, precisionAttach, attach[j]);
//            outputText += "$" + m + "_{(" + s + ")}$";
//            if (signState.get(j) == SIGNSTATE_BEST) {
//                outputText += "}";
//            }
//            outputText += "& ";
//        }
//        if (signState.get(data.length - 1) == SIGNSTATE_BEST) {
//            outputText += "\\cellcolor{gray95}";
//            outputText += "\\textbf{";
//        }
//        if (signState.get(data.length - 1) == SIGNSTATE_SECONDBEST) {
//            outputText += "\\cellcolor{gray25}";
//        }
//        String m = precisionControl.format(data[data.length - 1]);
//        //String.format(Locale.ENGLISH, ,precision data[data.length - 1]) ;
//        String s = precisionControlAttach.format(attach[attach.length - 1]);
//        //String.format(Locale.ENGLISH, precisionAttach, attach[attach.length - 1]);
//        outputText += "$" + m + "_{(" + s + ")}$";
//
//        if (SIGNSTATE_BEST == signState.get(data.length - 1)) {
//            outputText += "}";
//        }
//        outputText += "\\\\" + "\n";
//        return outputText;
//    }

    public String printLineWithRank(double[] data, List<Integer> ranks) {
        String outputText = "  ";

        for (int j = 0; j < (data.length - 1); j++) {
            if (1 == ranks.get(j)) {
                outputText += "\\cellcolor{gray95}";
            }
            if (data.length > 2 && 2 == ranks.get(j)) {
                outputText += "\\cellcolor{gray25}";
            }

            outputText += "$";
            if(1 == ranks.get(j))
                outputText += "\\mathbf";
            outputText += "{" + precisionControl.format(data[j]) + "_{(" + ranks.get(j) + ")}}$";

            outputText += "& ";
        }
        if (1 == ranks.get(data.length - 1)) {
            outputText += "\\cellcolor{gray95}";
        }
        if (data.length > 2 && 2 == ranks.get(data.length - 1)) {
            outputText += "\\cellcolor{gray25}";
        }

        outputText += "$";
        if(1 == ranks.get(data.length - 1))
            outputText += "\\mathbf";
        outputText +="{"+ precisionControl.format(data[data.length - 1]) + "_{(" + ranks.get(data.length  - 1 ) + ")}}$";

        outputText += "\\\\" + "\n";
        return outputText;
    }


    public String printLine(double[] data, List<Integer> ranks) {
        String outputText = "  ";

        for (int j = 0; j < (data.length - 1); j++) {
            if (1 == ranks.get(j)) {
                outputText += "\\cellcolor{gray95}";
            }
            if (data.length > 2 && 2 == ranks.get(j)) {
                outputText += "\\cellcolor{gray25}";
            }

            outputText += "$";
            if(1 == ranks.get(j))
                outputText += "\\mathbf";
            outputText += "{" + precisionControl.format(data[j]) + "}$";

            outputText += "& ";
        }
        if (1 == ranks.get(data.length - 1)) {
            outputText += "\\cellcolor{gray95}";
        }
        if (data.length > 2 && 2 == ranks.get(data.length - 1)) {
            outputText += "\\cellcolor{gray25}";
        }

        outputText += "$";
        if(1 == ranks.get(data.length - 1))
            outputText += "\\mathbf";
        outputText +="{"+ precisionControl.format(data[data.length - 1]) + "}$";

        outputText += "\\\\" + "\n";
        return outputText;
    }

    public String printLineWithRank(double[] data, double[] attach, List<Integer> ranks) {
        String outputText = "  ";

        for (int j = 0; j < (data.length - 1); j++) {
            if (1 == ranks.get(j)) {
                outputText += "\\cellcolor{gray95}";
            }
            if (data.length > 2 && 2 == ranks.get(j)) {
                outputText += "\\cellcolor{gray25}";
            }

            outputText += "$";
            if(1 == ranks.get(j))
                outputText += "\\mathbf";
            String m = precisionControl.format(data[j]);
            String s = precisionControlAttach.format(attach[j]);
            outputText += "{" + m + "^{<" + s + ">}_{("+ranks.get(data.length  -1)+")}}$";

            outputText += "& ";
        }
        if (1 == ranks.get(data.length - 1)) {
            outputText += "\\cellcolor{gray95}";
        }
        if (data.length > 2 && 2 == ranks.get(data.length - 1)) {
            outputText += "\\cellcolor{gray25}";
        }

        outputText += "$";
        if(1 == ranks.get(data.length - 1))
            outputText += "\\mathbf";
        String m = precisionControl.format(data[data.length - 1]);
        String s = precisionControlAttach.format(attach[attach.length - 1]);
        outputText +="{"+ m + "^{(" + s + ")}_{("+ranks.get(data.length - 1)+")}}$";

        outputText += "\\\\" + "\n";
        return outputText;
    }

    public String printLine(double[] data, double[] attach, List<Integer> ranks) {
        String outputText = "  ";

        for (int j = 0; j < (data.length - 1); j++) {
            if (1 == ranks.get(j)) {
                outputText += "\\cellcolor{gray95}";
            }
            if (data.length > 2 && 2 == ranks.get(j)) {
                outputText += "\\cellcolor{gray25}";
            }

            outputText += "$";
            if(1 == ranks.get(j))
                outputText += "\\mathbf";
            String m = precisionControl.format(data[j]);
            String s = precisionControlAttach.format(attach[j]);
            outputText += "{" + m + "^{<" + s + ">}}$";

            outputText += "& ";
        }
        if (1 == ranks.get(data.length - 1)) {
            outputText += "\\cellcolor{gray95}";
        }
        if (data.length > 2 && 2 == ranks.get(data.length - 1)) {
            outputText += "\\cellcolor{gray25}";
        }

        outputText += "$";
        if(1 == ranks.get(data.length - 1))
            outputText += "\\mathbf";
        String m = precisionControl.format(data[data.length - 1]);
        String s = precisionControlAttach.format(attach[attach.length - 1]);
        outputText +="{"+ m + "^{<" + s + ">}}$";

        outputText += "\\\\" + "\n";
        return outputText;
    }

//    public String printLine(double[] data, int bestIndex, int secondBestIndex) {
//        String outputText = "  ";
//
//        for (int j = 0; j < (data.length - 1); j++) {
//            if (j == bestIndex) {
//                outputText += "\\cellcolor{gray95}";
//                outputText += "\\textbf{";
//            }
//            if (data.length > 2 && j == secondBestIndex) {
//                outputText += "\\cellcolor{gray25}";
//            }
//
//            outputText += "" + precisionControl.format(data[j]);
//            //String.format(Locale.ENGLISH, precision, data[j]) ;
//            if (j == bestIndex) {
//                outputText += "}";
//            }
//            outputText += "& ";
//        }
//        if (bestIndex == (data.length - 1)) {
//            outputText += "\\cellcolor{gray95}";
//            outputText += "\\textbf{";
//        }
//        if (data.length > 2 && secondBestIndex == (data.length - 1)) {
//            outputText += "\\cellcolor{gray25}";
//        }
//        outputText += "" + precisionControl.format(data[data.length - 1]);
//        //String.format(Locale.ENGLISH, precision, data[data.length - 1]);
//
//        if (bestIndex == (data.length - 1)) {
//            outputText += "}";
//        }
//        outputText += "\\\\" + "\n";
//        return outputText;
//    }
//
//    public String printLine(double[] data, double[] attach, int bestIndex, int secondBestIndex) {
//        String outputText = " 　";
//        for (int j = 0; j < (data.length - 1); j++) {
//            if (j == bestIndex) {
//                outputText += "\\cellcolor{gray95}";
//                outputText += "\\textbf{";
//            }
//            if (data.length > 2 && j == secondBestIndex) {
//                outputText += "\\cellcolor{gray25}";
//            }
//
//            String m = precisionControl.format(data[j]);
//            //String.format(Locale.ENGLISH, precision, data[j]) ;
//            String s = precisionControlAttach.format(attach[j]);
//            //String.format(Locale.ENGLISH, precisionAttach, attach[j]);
//            outputText += "$" + m + "_{(" + s + ")}$";
//            if (j == bestIndex) {
//                outputText += "}";
//            }
//            outputText += "& ";
//        }
//        if (bestIndex == (data.length - 1)) {
//            outputText += "\\cellcolor{gray95}";
//            outputText += "\\textbf{";
//        }
//        if (data.length > 2 && secondBestIndex == (data.length - 1)) {
//            outputText += "\\cellcolor{gray25}";
//        }
//        String m = precisionControl.format(data[data.length - 1]);
//        //String.format(Locale.ENGLISH, ,precision data[data.length - 1]) ;
//        String s = precisionControlAttach.format(attach[attach.length - 1]);
//        //String.format(Locale.ENGLISH, precisionAttach, attach[attach.length - 1]);
//        outputText += "$" + m + "_{(" + s + ")}$";
//
//        if (bestIndex == (data.length - 1)) {
//            outputText += "}";
//        }
//        outputText += "\\\\" + "\n";
//        return outputText;
//    }
//
//    public String printLine(double[] data) {
//        String outputText = "  ";
//        for (int j = 0; j < (data.length - 1); j++) {
//            outputText += "" + precisionControl.format(data[j]) + "& ";
//            //String.format(Locale.ENGLISH, precision, data[j]) + "& ";
//        }
//        outputText += "" + precisionControl.format(data[data.length - 1]) + "\\\\" + "\n";
//        //String.format(Locale.ENGLISH, precision, data[data.length - 1])+ "\\\\" + "\n";
//        return outputText;
//    }
//
//    public String printLine(double[] data, double[] attach) {
//        String outputText = "  ";
//        for (int j = 0; j < (data.length - 1); j++) {
//            String m = precisionControl.format(data[j]);
//            // String.format(Locale.ENGLISH, precision, data[j]) ;
//            String s = precisionControlAttach.format(attach[j]);
//            //String.format(Locale.ENGLISH, precisionAttach, attach[j]);
//            outputText += "$" + m + "_{(" + s + ")}$" + "& ";
//        }
//        String m = precisionControl.format(data[data.length - 1]);
//        //String.format(Locale.ENGLISH, precision, data[data.length - 1]) ;
//        String s = precisionControlAttach.format(attach[attach.length - 1]);
//        //String.format(Locale.ENGLISH, precisionAttach, attach[attach.length - 1]);
//        outputText += "$" + m + "_{(" + s + ")}$" + "\\\\" + "\n";
//        return outputText;
//    }
//
//    public void findBestAndSecondBestIdx(double[] indicatorData, boolean isLowerTheBetter, List<Integer> bestIdx, List<Integer> secondBestIdx) {
//        // find the best value and second best value
//        double bestValue;
//        double secondBestValue;
////        int bestIndex = -1;
////        int secondBestIndex = -1;
//        bestIdx.clear();
//        secondBestIdx.clear();
//
//        double signData = 1.0;
//        if (isLowerTheBetter) {
//            bestValue = Double.MAX_VALUE;
//            secondBestValue = Double.MAX_VALUE;
//            signData = 1.0;
//        } else {
//            bestValue = Double.MIN_VALUE;
//            secondBestValue = Double.MIN_VALUE;
//            signData = -1.0;
//        }
//
//        for (int j = indicatorData.length - 1; j >= 0; j--) {
//            if (signData * (indicatorData[j] - bestValue) <= 0.0) {
//                if (signData * (indicatorData[j] - bestValue) < 0.0) {
//                    secondBestIdx.clear();
//                    for (int k = 0; k < bestIdx.size(); k++) {
//                        secondBestIdx.add(bestIdx.get(k));
//                    }
//                    secondBestValue = bestValue;
//                    bestValue = indicatorData[j];
//                    bestIdx.clear();
//                }
//                bestIdx.add(j);
//            } else if (signData * (indicatorData[j] - secondBestValue) <= 0.0) {
//                if (signData * (indicatorData[j] - secondBestValue) < 0.0) {
//                    secondBestIdx.clear();
//                    secondBestValue = indicatorData[j];
//                }
//                secondBestIdx.add(j);
//            }
//        }
//
////    else {
////            bestValue = Double.MIN_VALUE;
////            secondBestValue = Double.MIN_VALUE;
////            secondBestValue = Double.MIN_VALUE;
////            for (int j = indicatorData.length - 1; j >= 0; j--) {
////                if (indicatorData[j] >= bestValue) {
////                    if(indicatorData[j] > bestValue) {
////                        secondBestIdx.clear();
////                        for (int k = 0; k < bestIdx.size(); k++) {
////                            secondBestIdx.add(bestIdx.get(k));
////                        }
////                        secondBestValue = bestValue;
////                        bestValue = indicatorData[j];
////                        bestIdx.clear();
////                    }
////                    bestIdx.add(j);
////                } else if (indicatorData[j] > secondBestValue) {
////                    if(indicatorData[j] < secondBestValue) {
////                        secondBestIdx.clear();
////                        secondBestValue = indicatorData[j];
////                    }
////                    secondBestIdx.add(j);
////                }
////            }
////        }
//    }
//
//    public void findBestAndSecondBestIdx(double[] centralTendencyData, boolean isLowerTheBetterCentral, double[] dispersionData, boolean isLowerTheBetterDispersion, List<Integer> bestIdx, List<Integer> secondBestIdx) {
//        // find the best value and second best value
//        double bestCentralTendencyValue;
//        double bestDispersionValue;
//        double secondBestCentralTendencyValue;
//        double secondBestDispersionValue;
//
//        bestIdx.clear();
//        secondBestIdx.clear();
//
//        double signCentral = 1.0;
//        double signDispersion = 1.0;
//        if (isLowerTheBetterCentral) {
//            bestCentralTendencyValue = Double.MAX_VALUE;
//            secondBestCentralTendencyValue = Double.MAX_VALUE;
//            signCentral = 1.0;
//        } else {
//            bestCentralTendencyValue = Double.MIN_VALUE;
//            secondBestCentralTendencyValue = Double.MIN_VALUE;
//            signCentral = -1.0;
//        }
//        if (isLowerTheBetterDispersion) {
//            bestDispersionValue = Double.MAX_VALUE;
//            secondBestDispersionValue = Double.MAX_VALUE;
//            signDispersion = 1.0;
//        } else {
//            bestDispersionValue = Double.MIN_VALUE;
//            secondBestDispersionValue = Double.MIN_VALUE;
//            signDispersion = -1.0;
//        }
//
//        for (int j = centralTendencyData.length - 1; j >= 0; j--) {
//            if (signCentral * (centralTendencyData[j] - bestCentralTendencyValue) < 0.0) {
//                secondBestIdx.clear();
//                for (int k = 0; k < bestIdx.size(); k++) {
//                    secondBestIdx.add(bestIdx.get(k));
//                }
//                secondBestCentralTendencyValue = bestCentralTendencyValue;
//                bestCentralTendencyValue = centralTendencyData[j];
//                bestIdx.clear();
//                bestIdx.add(j);
//            } else if (signCentral * (centralTendencyData[j] - bestCentralTendencyValue) > 0.0 || signDispersion * (dispersionData[j] - bestDispersionValue) >= 0.0) {
//                if (signCentral * (centralTendencyData[j] - secondBestCentralTendencyValue) < 0.0) {
//                    secondBestIdx.clear();
//                    secondBestCentralTendencyValue = centralTendencyData[j];
//                    secondBestIdx.add(j);
//                } else if (signCentral * (centralTendencyData[j] - secondBestCentralTendencyValue) > 0.0) {
//
//                } else if (signDispersion * (dispersionData[j] - secondBestDispersionValue) < 0.0) {
//                    secondBestIdx.clear();
//                    secondBestCentralTendencyValue = centralTendencyData[j];
//                    secondBestIdx.add(j);
//                }
//            } else if (signDispersion * (dispersionData[j] - bestDispersionValue) < 0.0) {
//                secondBestIdx = new ArrayList<>(1);
//                for (int k = 0; k < bestIdx.size(); k++) {
//                    secondBestIdx.add(bestIdx.get(k));
//                }
//                secondBestCentralTendencyValue = bestCentralTendencyValue;
//                bestCentralTendencyValue = centralTendencyData[j];
//                bestIdx.clear();
//                bestIdx.add(j);
//            }
//        }
////        } else {
////            bestCentralTendencyValue = Double.MIN_VALUE;
////            bestDispersionValue = Double.MIN_VALUE;
////            secondBestCentralTendencyValue = Double.MIN_VALUE;
////            secondBestDispersionValue = Double.MIN_VALUE;
////            for (int j = centralTendencyData.length-1; j >= 0; j--) {
////                if (centralTendencyData[j] >= bestCentralTendencyValue){
////                    if(centralTendencyData[j] > bestCentralTendencyValue){
////                        secondBestIdx.clear();
////                        for (int k = 0; k < bestIdx.size(); k++) {
////                            secondBestIdx.add(bestIdx.get(k));
////                        }
////                        secondBestCentralTendencyValue = bestCentralTendencyValue;
////                        bestCentralTendencyValue = centralTendencyData[j];
////                        bestIdx.clear();
////                        bestIdx.add(j);
////                    } else if(dispersionData[j] <= bestDispersionValue){
////                        if(dispersionData[j] < bestDispersionValue){
////                            secondBestIdx.clear();
////                            for (int k = 0; k < bestIdx.size(); k++) {
////                                secondBestIdx.add(bestIdx.get(k));
////                            }
////                            secondBestCentralTendencyValue = bestCentralTendencyValue;
////                            bestCentralTendencyValue = centralTendencyData[j];
////                            bestIdx.clear();
////                        }
////                        bestIdx.add(j);
////                    }
////                } else if (centralTendencyData[j] >= secondBestCentralTendencyValue){
////                    if(centralTendencyData[j] > secondBestCentralTendencyValue){
////                        secondBestIdx.clear();
////                        secondBestCentralTendencyValue = centralTendencyData[j];
////                        secondBestIdx.add(j);
////                    }else if(dispersionData[j] <= secondBestDispersionValue){
////                        if(dispersionData[j] < secondBestDispersionValue){
////                            secondBestIdx.clear();
////                            secondBestCentralTendencyValue = centralTendencyData[j];
////                            secondBestIdx.add(j);
////                        }
////                        secondBestIdx.add(j);
////                    }
////                }
////            }
////        }
//    }

    public List<Integer> calcComparasionRanks(double[] indicatorData, boolean isLowerTheBetter) {
        List<Integer> ranks = new ArrayList<>(indicatorData.length);
        List<Double> dataCopy = new ArrayList<>(indicatorData.length);
        List<Integer> idx = new ArrayList<>(indicatorData.length);
        for (int i=0;i<indicatorData.length;i++){
            ranks.add(i+1);
            dataCopy.add(indicatorData[i]);
            idx.add(i);
        }
        sort(dataCopy,idx,isLowerTheBetter);

        int currentRank = 1;
        int numSameRank = 0;
        double currentData = dataCopy.get(0);
        ranks.set(idx.get(0),currentRank);
        numSameRank++;
        for (int i =1;i<indicatorData.length;i++){
            if((isLowerTheBetter && dataCopy.get(i) > currentData) || (!isLowerTheBetter && dataCopy.get(i) < currentData) ){
                currentRank+= numSameRank;
                numSameRank = 0;
                currentData = dataCopy.get(i);
            }
            ranks.set(idx.get(i),currentRank);
            numSameRank++;
        }
        return ranks;
    }
//
//    public List<Integer> findBestAndSecondBestIdx(double[] indicatorData, boolean isLowerTheBetter) {
//        // find the best value and second best value
//        double bestValue;
//        double secondBestValue;
//        int bestIndex = -1;
//        int secondBestIndex = -1;
//
//        double signData = 1.0;
//        if (isLowerTheBetter) {
//            bestValue = Double.MAX_VALUE;
//            secondBestValue = Double.MAX_VALUE;
//            signData = 1.0;
//        } else {
//            bestValue = Double.MIN_VALUE;
//            secondBestValue = Double.MIN_VALUE;
//            signData = -1.0;
//        }
//
//        for (int j = 0; j < indicatorData.length; j++) {
//            if (signData * (indicatorData[j] - bestValue) < 0.0) {
//                secondBestIndex = bestIndex;
//                secondBestValue = bestValue;
//                bestValue = indicatorData[j];
//                bestIndex = j;
//            } else if (signData * (indicatorData[j] - secondBestValue) < 0.0) {
//                secondBestIndex = j;
//                secondBestValue = indicatorData[j];
//            }
//        }
////        } else {
////            bestValue = Double.MIN_VALUE;
////            secondBestValue = Double.MIN_VALUE;
////            secondBestValue = Double.MIN_VALUE;
////            for (int j = 0; j < indicatorData.length; j++) {
////                if (indicatorData[j] > bestValue) {
////                    secondBestIndex = bestIndex;
////                    secondBestValue = bestValue;
////                    bestValue = indicatorData[j];
////                    bestIndex = j;
////                } else if (indicatorData[j] > secondBestValue) {
////                    secondBestIndex = j;
////                    secondBestValue = indicatorData[j];
////                }
////            }
////        }
//        List<Integer> result = new ArrayList<>(2);
//        result.add(bestIndex);
//        result.add(secondBestIndex);
//        return result;
//    }
//
//    public List<Integer> findBestAndSecondBestIdx(double[] centralTendencyData, boolean isLowerTheBetterCentral, double[] dispersionData, boolean isLowerTheBetterDispersion) {
//        // find the best value and second best value
//        double bestCentralTendencyValue;
//        double bestDispersionValue;
//        double secondBestCentralTendencyValue;
//        double secondBestDispersionValue;
//        int bestIndex = -1;
//        int secondBestIndex = -1;
//
//        double signCentral = 1.0;
//        double signDispersion = 1.0;
//        if (isLowerTheBetterCentral) {
//            bestCentralTendencyValue = Double.MAX_VALUE;
//            secondBestCentralTendencyValue = Double.MAX_VALUE;
//            signCentral = 1.0;
//        } else {
//            bestCentralTendencyValue = Double.MIN_VALUE;
//            secondBestCentralTendencyValue = Double.MIN_VALUE;
//            signCentral = -1.0;
//        }
//
//        if (isLowerTheBetterDispersion) {
//            bestDispersionValue = Double.MAX_VALUE;
//            secondBestDispersionValue = Double.MAX_VALUE;
//            signDispersion = 1.0;
//        } else {
//            bestDispersionValue = Double.MIN_VALUE;
//            secondBestDispersionValue = Double.MIN_VALUE;
//            signDispersion = -1.0;
//        }
//
//        for (int j = 0; j < centralTendencyData.length; j++) {
//            if (signCentral * (centralTendencyData[j] - bestCentralTendencyValue) < 0.0) {
//                secondBestIndex = bestIndex;
//                secondBestCentralTendencyValue = bestCentralTendencyValue;
//                secondBestDispersionValue = bestDispersionValue;
//                bestCentralTendencyValue = centralTendencyData[j];
//                bestDispersionValue = dispersionData[j];
//                bestIndex = j;
//            } else if (signCentral * (centralTendencyData[j] - bestCentralTendencyValue) > 0.0 || signDispersion * (dispersionData[j] - bestDispersionValue) >= 0.0) {
//                if (signCentral * (centralTendencyData[j] - secondBestCentralTendencyValue) < 0.0) {
//                    secondBestIndex = j;
//                    secondBestCentralTendencyValue = centralTendencyData[j];
//                    secondBestDispersionValue = dispersionData[j];
//                } else if (signCentral * (centralTendencyData[j] - secondBestCentralTendencyValue) > 0.0) {
//                    //
//                } else if (signDispersion * (dispersionData[j] - secondBestDispersionValue) < 0.0) {
//                    secondBestIndex = j;
//                    secondBestCentralTendencyValue = centralTendencyData[j];
//                    secondBestDispersionValue = dispersionData[j];
//                }
//            } else if (signDispersion * (dispersionData[j] - bestDispersionValue) < 0.0) {
//                secondBestIndex = bestIndex;
//                secondBestCentralTendencyValue = bestCentralTendencyValue;
//                secondBestDispersionValue = bestDispersionValue;
//                bestCentralTendencyValue = centralTendencyData[j];
//                bestDispersionValue = dispersionData[j];
//                bestIndex = j;
//            }
//        }
////        } else {
////            bestCentralTendencyValue = Double.MIN_VALUE;
////            bestDispersionValue = Double.MIN_VALUE;
////            secondBestCentralTendencyValue = Double.MIN_VALUE;
////            secondBestDispersionValue = Double.MIN_VALUE;
////            for (int j = 0; j < centralTendencyData.length; j++) {
////                if ((centralTendencyData[j] > bestCentralTendencyValue) ||
////                        ((centralTendencyData[j] ==
////                                bestCentralTendencyValue) && (dispersionData[j] < bestDispersionValue))) {
////                    secondBestIndex = bestIndex;
////                    secondBestCentralTendencyValue = bestCentralTendencyValue;
////                    secondBestDispersionValue = bestDispersionValue;
////                    bestCentralTendencyValue = centralTendencyData[j];
////                    bestDispersionValue = dispersionData[j];
////                    bestIndex = j;
////                } else if ((centralTendencyData[j] > secondBestCentralTendencyValue) ||
////                        ((centralTendencyData[j] ==
////                                secondBestCentralTendencyValue) && (dispersionData[j] < secondBestDispersionValue))) {
////                    secondBestIndex = j;
////                    secondBestCentralTendencyValue = centralTendencyData[j];
////                    secondBestDispersionValue = dispersionData[j];
////                }
////            }
////        }
//        List<Integer> result = new ArrayList<>(2);
//        result.add(bestIndex);
//        result.add(secondBestIndex);
//        return result;
//    }

//
//  private void printData(String latexFile, String indicatorName, boolean isLowerTheBetter, double[][][] centralTendency, double[][][] dispersion, String caption,String[] problemNameList, int[][] problemObjList, String[] algorithmNameList) throws IOException {
//        // Generate header of the table
//        FileWriter os = new FileWriter(latexFile, true);
//        os.write("\n");
//        os.write("\\begin{table}" + "\n");
//        os.write("\\caption{" + indicatorName+ ". " + caption + "}" + "\n");
//        os.write("\\label{table: " + indicatorName + "}" + "\n");
//        os.write("\\centering" + "\n");
//        os.write("\\begin{scriptsize}" + "\n");
//        os.write("\\begin{tabular}{ll");
//
//        // calculate the number of columns
//
//        for (int i =0;i<algorithmNameList.length;i++) {
//            os.write("l");
//        }
//        os.write("}\n");
//        os.write("\\hline");
//
//        // write table head
//        for (int i = -2; i < algorithmNameList.length; i++) {
//            if (i < 0) {
//                os.write(" & ");
//            } else if (i == (algorithmNameList.length - 1)) {
//                os.write(" " + algorithmNameList[i] + "\\\\" + "\n");
//            } else {
//                os.write("" + algorithmNameList[i] + " & ");
//            }
//        }
//        os.write("\\hline \n");
//
//        // write lines
//        for(int iProblem=0;iProblem<problemNameList.length;iProblem++) {
//            int sizeProblemObj = problemObjList[iProblem].length;
//            os.write("\\multirow{" + sizeProblemObj + "}*{" + problemNameList[iProblem].replace("_", "\\_") + "} \n");
//
//            for (int i = 0; i < sizeProblemObj; i++) {
//                // find the best value and second best value
//                double bestCentralTendencyValue;
//                double bestDispersionValue;
//                double secondBestCentralTendencyValue;
//                double secondBestDispersionValue;
//                int bestIndex = -1;
//                int secondBestIndex = -1;
//
//                if (isLowerTheBetter) {
//                    bestCentralTendencyValue = Double.MAX_VALUE;
//                    bestDispersionValue = Double.MAX_VALUE;
//                    secondBestCentralTendencyValue = Double.MAX_VALUE;
//                    secondBestDispersionValue = Double.MAX_VALUE;
//                    for (int j = 0; j < algorithmNameList.length; j++) {
//                        if ((centralTendency[iProblem][i][j] < bestCentralTendencyValue) ||
//                                ((centralTendency[iProblem][i][j] ==
//                                        bestCentralTendencyValue) && (dispersion[iProblem][i][j] < bestDispersionValue))) {
//                            secondBestIndex = bestIndex;
//                            secondBestCentralTendencyValue = bestCentralTendencyValue;
//                            secondBestDispersionValue = bestDispersionValue;
//                            bestCentralTendencyValue = centralTendency[iProblem][i][j];
//                            bestDispersionValue = dispersion[iProblem][i][j];
//                            bestIndex = j;
//                        } else if ((centralTendency[iProblem][i][j] < secondBestCentralTendencyValue) ||
//                                ((centralTendency[iProblem][i][j] ==
//                                        secondBestCentralTendencyValue) && (dispersion[iProblem][i][j] < secondBestDispersionValue))) {
//                            secondBestIndex = j;
//                            secondBestCentralTendencyValue = centralTendency[iProblem][i][j];
//                            secondBestDispersionValue = dispersion[iProblem][i][j];
//                        }
//                    }
//                } else {
//                    bestCentralTendencyValue = Double.MIN_VALUE;
//                    bestDispersionValue = Double.MIN_VALUE;
//                    secondBestCentralTendencyValue = Double.MIN_VALUE;
//                    secondBestDispersionValue = Double.MIN_VALUE;
//                    for (int j = 0; j < algorithmNameList.length; j++) {
//                        if ((centralTendency[iProblem][i][j] > bestCentralTendencyValue) ||
//                                ((centralTendency[iProblem][i][j] ==
//                                        bestCentralTendencyValue) && (dispersion[iProblem][i][j] < bestDispersionValue))) {
//                            secondBestIndex = bestIndex;
//                            secondBestCentralTendencyValue = bestCentralTendencyValue;
//                            secondBestDispersionValue = bestDispersionValue;
//                            bestCentralTendencyValue = centralTendency[iProblem][i][j];
//                            bestDispersionValue = dispersion[iProblem][i][j];
//                            bestIndex = j;
//                        } else if ((centralTendency[iProblem][i][j] > secondBestCentralTendencyValue) ||
//                                ((centralTendency[iProblem][i][j] ==
//                                        secondBestCentralTendencyValue) && (dispersion[iProblem][i][j] < secondBestDispersionValue))) {
//                            secondBestIndex = j;
//                            secondBestCentralTendencyValue = centralTendency[iProblem][i][j];
//                            secondBestDispersionValue = dispersion[iProblem][i][j];
//                        }
//                    }
//                }
//
//                os.write("& " + problemObjList[iProblem][i] + " & ");
//                for (int j = 0; j < (algorithmNameList.length - 1); j++) {
//                    if (j == bestIndex) {
//                        os.write("\\cellcolor{gray95}");
//                    }
//                    if (j == secondBestIndex) {
//                        os.write("\\cellcolor{gray25}");
//                    }
//
//                    String m = String.format(Locale.ENGLISH, "%10.2e", centralTendency[iProblem][i][j]);
//                    String s = String.format(Locale.ENGLISH, "%8.1e", dispersion[iProblem][i][j]);
//                    os.write("$" + m + "_{" + s + "}$ & ");
//                }
//                if (bestIndex == (algorithmNameList.length - 1)) {
//                    os.write("\\cellcolor{gray95}");
//                }
//                if (secondBestIndex == (algorithmNameList.length - 1)) {
//                    os.write("\\cellcolor{gray25}");
//                }
//                String m = String.format(Locale.ENGLISH, "%10.2e",
//                        centralTendency[iProblem][i][algorithmNameList.length - 1]);
//                String s = String.format(Locale.ENGLISH, "%8.1e",
//                        dispersion[iProblem][i][algorithmNameList.length - 1]);
//                os.write("$" + m + "_{" + s + "}$ \\\\" + "\n");
//                if (i != sizeProblemObj - 1) {
//                    int len = (algorithmNameList.length + 2);
//                    os.write("\\cline{2-" + len + "}");
//                }
//            }
//            // close table
//            os.write("\\hline" + "\n");
//        }
//        os.write("\\end{tabular}" + "\n");
//        os.write("\\end{scriptsize}" + "\n");
//        os.write("\\end{table}" + "\n");
//        os.close();
//    }


    public List<List<List<List<Double>>>> collectData(int maxRun, String indicator, String[][] problemNameList, String[][] algorithmNameList, int[][] popsList, int[][] maxIterationsList, String[] dirs, String beginStr) {

        List<List<List<List<Double>>>> data = new ArrayList<>();

        analysisQuality quality = new analysisQuality();

        for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {

            List<List<List<Double>>> problemData = new ArrayList<>();

            for (int iProblemObj = 0; iProblemObj < problemNameList[iProblem].length; iProblemObj++) {
                List<List<Double>> problemObjData = new ArrayList<>();

                for (int k = 0; k < algorithmNameList.length; k++) {
                    for (int jAlg = 0; jAlg < algorithmNameList[k].length; ++jAlg) {
                        String instance = algorithmNameList[k][jAlg] + "_" + problemNameList[iProblem][iProblemObj] + "_" + popsList[iProblem][iProblemObj] + "_" + maxIterationsList[iProblem][iProblemObj];
                        JMetalLogger.logger.info(instance);

                        List<Double> indicatorData = new ArrayList<>(maxRun);
                        try {
                            String dataFile = dirs[k] + "/" + indicator + "/" + beginStr + instance + ".csv";

                            InputStream inputStream = getClass().getResourceAsStream(dataFile);
                            if (inputStream == null) {
                                inputStream = new FileInputStream(dataFile);
                            }
                            InputStreamReader isr = new InputStreamReader(inputStream);
                            BufferedReader br = new BufferedReader(isr);

                            String line;

                            line = br.readLine();

                            while (line != null) {
                                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                                while (tokenizer.hasMoreTokens()) {
                                    indicatorData.add(new Double(tokenizer.nextToken()));
                                }
                                line = br.readLine();
                            }
                            br.close();
                        } catch (IOException e) {
                            throw new JMetalException("Error reading file", e);
                        } catch (NumberFormatException e) {
                            throw new JMetalException("Format number exception when reading file", e);
                        }

                        if (indicator.equals("HV")) {//normalize
                            for (int p = 0; p < maxRun; p++) {
                                if (indicatorData.get(p) - 0.99999 > 0.0)
                                    indicatorData.set(p, 0.99999);
                            }
                        }

                        problemObjData.add(indicatorData);

                    }
                }
                problemData.add(problemObjData);
            }
            data.add(problemData);
        }

        return data;
    }

    public void generateLatexMaOP() {


        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
                "HV"
        };

        boolean[] isLowerTheBetter = {
                false
        };

        String[] baseDirs = {
                "D://Experiments/ExperimentDataThesis/DTLZ/compare/",
//                "D://Experiments/ExperimentDataThesis/DTLZ/MOEACD-old/",
//                "D://Experiments/ExperimentDataThesis/DTLZ/MOEACD/"
//                ,
//                "E://ResultsMaOPMOEACD40/"
//                ,"E://ResultsMaOPMOEACD44/"
//                ,
//                "E://ResultsMaOPMOEACD60/"
//                "E://ResultsMaOPUEACD/"
//                "E://ResultsMaOPMOEACDWithoutEvolving/"
//                ,
//                "E://ResultsMaOPMOEACDAll/",
//                "E://ResultsMaOPMOEACDNoDomination/"
//                ,
//                "E://ResultsMaOPMOEACDNoDominationWithoutEvolving"
//                ,
//                "E://ResultsMaOPMOEACDNoDominationWithoutEvolving1",
//                "E://ResultsMaOPMOEACDNoDominationWithoutEvolving2"
//                "E://ResultsMaOPMOEACDBeta2_2/",
//                "E://ResultsMaOPMOEACDBeta2_0/",
//                "E://ResultsMaOPMOEACDBeta3_3/"
//                ,
//                "E://ResultsMaOPMOEACDWithoutNeighbor/"
//                "E://ResultsMaOPMOEACD80/",
//                "E://ResultsMaOPMOEACD81/"
//                "E://ResultsMaOPMOEACDB2_0/",
//                "E://ResultsMaOPMOEACDB3_0/"
//                "E://ResultsMaOPMOEACD97/",
//                "E://ResultsMaOPMOEACD110/",
//                "E://ResultsMaOPMOEACD111/",
//                "E://ResultsMaOPMOEACD112/",
//                "E://ResultsMaOPMOEACD120/"
//                ,
//                "E://ResultsMaOPMOEACDND_N_B3_0/",
//                "E://ResultsMaOPMOEACDND_NR_B3_0/",
//                "E://ResultsMaOPMOEACDND_N_B2_0/"
//                ,
//                "E://ResultsMaOPMOEACDND_N_NM_B3_0/"
//                ,
//                "E://ResultsMaOPMOEACDND_NGR_B3_0/",
//                "E://ResultsMaOPMOEACDND_NG_B3_0/"
//                "E://ResultsMaOPMOEACD_ND_NR_W6_B3_0/"
//                "E://ResultsMaOPMOEACD_ND_NGRD_B3_0/"
//                "E://ResultsMaOPMOEACD_D_NRU_B3_0_F/",
//                "E://ResultsMaOPMOEACD_ND_B3_0_F/",
//                "E://ResultsMaOPMOEACD_ND_NR_B3_0_F/",
//                "E://ResultsMaOPMOEACD_ND_GRU_B3_0_F/"
//                "E://ResultsMaOPMOEACD_D_B3_0_F/",
//                "E://ResultsMaOPMOEACD_D_NA_B3_0_Final/",
//                "E://ResultsMaOPMOEACD_ND_NRU_B3_0_F/",
//                "E://ResultsMaOPMOEACD_ND_NRSU_B3_0_F/"
//                "E://ResultsMaOPMOEACD_ND_GR_B3_0_F/",
//                "E://ResultsMaOPMOEACD_D_GR_B3_0_F/"
//                "E://ResultsMaOPMOEACDACV/"
//                "E://ResultsMaOPMOEACD_D_N_W17_B3_0_Final/",
//                "E://ResultsMaOPMOEACD_D_N_B3_0_Final/",
////              "E://ResultsMaOPMOEACD_ND_N_B3_0_Final/",
////              "E://ResultsMaOPMOEACD_ND_N_W17_B3_0_Final/"
////              "E://ResultsMaOPMOEACD_D_N_B3_3_FF/",
////              "E://ResultsMaOPMOEACD_D_N_B2_2_FF/"
//                "E://ResultsMaOPMOEACD_D_N_B3_0_FFF/",
//                "E://ResultsMaOPMOEACD_D_NW_B3_0_FFF/",
//                "E://ResultsMaOPMOEACDFFFF/"
//                ,
//                "E://ResultsMaOPMOEACDFFFF2/",
//                "E://ResultsMaOPMOEACDFFFF3/",
//                "E://ResultsMaOPMOEACDFFFF4/"
//                ,"E://ResultsMaOPMOEACDFFFFW17/"
//                ,"E://ResultsMaOPMOEACDFFFFNW17/"
//                "E://ResultsMaOPMOEACD_W47_B3_0_SB20",
//                "E://ResultsMaOPMOEACD_W47_B2_0_SB20",
//                "E://ResultsMaOPMOEACDFFFFW20_B3_0"
                "E://ResultsMaOPMOEACD_FFF1/",
                "E://ResultsMaOPMOEACD_FFF22/"
        };

        String[][] dirs = {
                new String[]{
                        baseDirs[0],
                        baseDirs[1]
                        ,
                        baseDirs[2]
//                        ,
//                        baseDirs[3]
//                        ,
//                        baseDirs[4]
//                        ,
//                        baseDirs[5]
                }
        };

        String[] experimentName = {
//                "DTLZ(IGDPlus)",
                "DTLZ(HV)"
//                "DTLZ(HV)Compare"
        };

        String[][] algorithmNameList = {
                new String[]{
                        "MOEAD_PBI",
//                        "MOEADDE_PBI",
                        "MOEADACDSBX_PBI",
//                        "MOEADACD_PBI",
                        "MOEADAGRSBX_PBI",
//                        "MOEADAGR_PBI",
                        "NSGAIII",
                        "MOEADD_PBI"
                },
//                new String[]{
//                        "MOEACD"
//                }
//                ,
//                new String[]{
////                        "MOEACD-SBX"
////                        ,"MOEACD-DE"
////                        ,
////                        "MOEACD-F"
////                        ,
//                        "MOEACD-SBX"
//                }
//                ,
//                new String[]{
////                        "MOEACD",
////                        "MOEACD-SBX",
////                        "MOEACD-DE"
////                        "MOEACD-V",
////                        "MOEACD-PEH",
////                        "MOEACD-ACV",
////                        "MOEACD-AECV"
////                        "UDEA",
//                        "MOEACD-SBX"
////                        ,
////                        "U-CDEA"
//                }
//                ,
//                new String[]{
//                        "MOEACD-SBX"
//                }
//                ,
//                new String[]{
//                        "MOEACD-SBX"
//                } ,
//                new String[]{
//                        "MOEACD-SBX"
//                } ,
                new String[]{
                        "MOEACD-SBX"
                } ,
                new String[]{
                        "MOEACD-SBX"
                }
//                new String[]{
//                        "MOEACD-ACV",
//                        "MOEACD-AECV"
//                }
//                ,
//                new String[]{
//                        "U-EACD",
//                        "U-EACD-ACV",
//                        "U-EACD-AECV"
//                }
        };

        String[] algorithms = {
                "MOEA/D",
//                "MOEA/D-DE",
                "MOEA/D-ACD",
//                "MOEA/D-ACD-DE",
                "MOEA/D-AGR",
//                "MOEA/D-AGR-DE",
                "NSGAIII",
                "MOEA/DD",
//                "MOEA/CD-O",
//                "MOEA/CD-SBX"
//                ,
////                "MOEA/CD-DE",
//                "MOEA/CD-F"
//                ,
//                "MOEA/CD"
//                ,
//                "MOEA/CD"
//                ,
//                "MOEA/CD-SBX"
//                ,
//                "MOEA/CD-DE"
//                ,"MOEA/CD-V"
//                ,
//                "MOEA/CD-PEH",
//                ,
//                "MOEA/CD-2"
//                ,
//                "MOEA/CD-3"
//                "U-EA/CD",
//                "U-EA/CD-ACV",
//                "U-EA/CD-AECV"
//                "MOEA/CD-ACV",
//                "MOEA/CD-AECV"
//                "UDEA",
//                "MOEACD",
//                "U-CDEA"
//                "MOEA/CD",
//                "MOEA/CD",
//                "MOEA/CD",
                "MOEA/CD",
                "MOEA/CD"
//                "MOEA/CD-ACV",
//                "MOEA/CD-AECV"
        };

        String[][] problemNameList = {
                new String[]{
                        "DTLZ1(3)", "DTLZ1(5)", "DTLZ1(8)", "DTLZ1(10)", "DTLZ1(15)"
                },
                new String[]{
                        "DTLZ2(3)", "DTLZ2(5)", "DTLZ2(8)", "DTLZ2(10)", "DTLZ2(15)"
                },
                new String[]{
                        "DTLZ3(3)", "DTLZ3(5)",
                        "DTLZ3(8)", "DTLZ3(10)", "DTLZ3(15)"
                },
                new String[]{
                        "DTLZ4(3)", "DTLZ4(5)",
                        "DTLZ4(8)", "DTLZ4(10)", "DTLZ4(15)"
                }
                ,
                new String[]{
                        "Convex_DTLZ2(3)", "Convex_DTLZ2(5)", "Convex_DTLZ2(8)", "Convex_DTLZ2(10)", "Convex_DTLZ2(15)"
                }
        };
        int[][] popsList = {
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210,
                        156, 275, 135
                },
                new int[]{
                        91, 210,
                        156, 275, 135
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        400, 600, 750, 1000, 1500
                },
                new int[]{
                        250, 350, 500, 750, 1000
                },
                new int[]{
                        1000, 1000,
                        1000, 1500, 2000
                },
                new int[]{
                        600, 1000,
                        1250, 2000, 3000
                }
                ,
                new int[]{
                        250, 750, 2000, 4000, 4500
                }
        };


        String[] probelms = {
                "DTLZ1",
                "DTLZ2",
                "DTLZ3",
                "DTLZ4"
                ,
                "Convex_DTLZ2"
        };
        int[][] problemObj = {
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5,
                        8, 10, 15
                },
                new int[]{
                        3, 5,
                        8, 10, 15
                }
        };
//
//        String[][] problemNameList = {
//                new String[]{
//                        "DTLZ1(3)", "DTLZ1(5)"
//                },
//                new String[]{
//                        "DTLZ2(3)", "DTLZ2(5)"
//                },
//                new String[]{
//                        "DTLZ3(3)", "DTLZ3(5)"
//                },
//                new String[]{
//                        "DTLZ4(3)", "DTLZ4(5)"
//                },
//                new String[]{
//                        "Convex_DTLZ2(3)", "Convex_DTLZ2(5)"
//                }
//        };
//        int[][] popsList = {
//                new int[]{
//                        91, 210
//                },
//                new int[]{
//                        91, 210
//                },
//                new int[]{
//                        91, 210
//                },
//                new int[]{
//                        91, 210
//                },
//                new int[]{
//                        91, 210
//                }
//        };
//        int[][] maxIterationsList = {
//                new int[]{
//                        400, 600
//                },
//                new int[]{
//                        250, 350
//                },
//                new int[]{
//                        1000, 1000
//                },
//                new int[]{
//                        600, 1000
//                },
//                new int[]{
//                        250, 750
//                }
//        };
//
//
//        String[] probelms = {
//                "DTLZ1",
//                "DTLZ2",
//                "DTLZ3",
//                "DTLZ4",
//                "Convex_DTLZ2"
//        };
//        int[][] problemObj = {
//                new int[]{
//                        3, 5
//                },
//                new int[]{
//                        3, 5
//                },
//                new int[]{
//                        3, 5
//                },
//                new int[]{
//                        3, 5
//                },
//                new int[]{
//                        3, 5
//                }
//        };


        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }

    public void generateLatexTestDTLZ3() {


        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
                "HV"
        };

        boolean[] isLowerTheBetter = {
                false
        };

        String[] baseDirs = {
                "D://Experiments/ExperimentDataThesis/DTLZ/compare/",
//                "D://Experiments/ExperimentDataThesis/DTLZ/MOEACD-old/",
                "D://Experiments/ExperimentDataThesis/DTLZ/MOEACD/"
                ,
                "E://ResultsMaOPMOEACD31/"
        };

        String[][] dirs = {
                new String[]{
                        baseDirs[0],
                        baseDirs[1]
                        ,
                        baseDirs[2]
                }
        };

        String[] experimentName = {
//                "DTLZ(IGDPlus)",
                "DTLZ3(HV)"
//                "DTLZ(HV)Compare"
        };

        String[][] algorithmNameList = {
                new String[]{
                        "MOEAD_PBI", "MOEADDE_PBI",
//                        "MOEADACDSBX_PBI",
                        "MOEADACD_PBI",
//                        "MOEADAGRSBX_PBI",
                        "MOEADAGR_PBI",
                        "NSGAIII", "MOEADD_PBI"
                },
//                new String[]{
////                        "MOEACD"
//                }
//                ,
                new String[]{
//                        "MOEACD-SBX","MOEACD-DE",
//                        "MOEACD-F",
                        "MOEACD"
                }
                ,
                new String[]{
                        "MOEACD"
//                        "MOEACD-V",
//                        "MOEACD-EHA"
                }
        };

        String[] algorithms = {
                "MOEA/D", "MOEA/D-DE",
                "MOEA/D-ACD",
//                "MOEA/D-ACD-DE",
                "MOEA/D-AGR",
//                "MOEA/D-AGR-DE",
                "NSGA-III", "MOEA/DD",
//                "MOEA/CD-O",
//                "MOEA/CD-SBX","MOEA/CD-DE",
//                "MOEA/CD-F",
                "MOEA/CD"
                ,
                "MOEA/CD"
//                ,"MOEA/CD-V"
//                ,"MOEA/CD-EHA"
        };

        String[][] problemNameList = {
                new String[]{
                        "DTLZ3(3)", "DTLZ3(5)", "DTLZ3(8)", "DTLZ3(10)", "DTLZ3(15)"
                }
        };
        int[][] popsList = {
                new int[]{
                        91, 210, 156, 275, 135
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        1000, 1000, 1000, 1500, 2000
                }
        };


        String[] probelms = {
                "DTLZ3"
        };
        int[][] problemObj = {
                new int[]{
                        3, 5, 8, 10, 15
                }

        };


        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }

    public void generateLatexMOP() {


        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
                "HV"
        };
        boolean[] isLowerTheBetter = {
                false
        };
        String[] experimentName = {
//                "MOP(IGDPlus)",
                "MOP(HV)"
        };
        String[][] algorithmNameList = {
                new String[]{
                        "MOEAD_PBI",
//                        "MOEADDE_PBI",
                        "MOEADACDSBX_PBI",
//                        "MOEADACD_PBI",
                        "MOEADAGRSBX_PBI",
//                        "MOEADAGR_PBI",
                        "NSGAIII", "MOEADD_PBI"
                },
//                new String[]{
//                        "MOEACD"
//                },
                new String[]{
//                        "MOEACD-SBX"
//                        , "MOEACD-DE"
//                        ,
//                        "MOEACD-F"
//                        ,
                        "MOEACD"
                }
                ,
                new String[]{
//                        "MOEACD-DE",
                        "MOEACD"
//                        ,"MOEACD-SBX"
//                        ,"MOEACD-DE"
                        ,"MOEACD-ACV"
                        ,"MOEACD-AECV"
                }
//                ,
//                new String[]{
//                        "MOEACD-V"
//                },
//                new String[]{
//                        "MOEACD-EHA"
//                }
//                ,
//                new String[]{
//                        "MOEACD"
//                }
//                ,
//                new String[]{
//                        "U-EACD",
//                        "U-EACD-ACV",
//                        "U-EACD-AECV"
//                }

        };
        String[] algorithms = {
                "MOEA/D",
//                "MOEA/D-DE",
                "MOEA/D-ACD",
//                "MOEA/D-ACD-DE",
                "MOEA/D-AGR",
//                "MOEA/D-AGR-DE",
                "NSGAIII", "MOEA/DD",
//                "MOEA/CD-O",
//                "MOEA/CD-SBX",
//                "MOEA/CD-DE",
////                "MOEA/CD-F",
                "MOEA/CD"
//                ,
////                "MOEA/CD-DE",
                ,"MOEA/CD",
//                ,"MOEA/CD-SBX"
//                ,"MOEA/CD-DE"
//                ,"MOEA/CD-DE","MEOA/CD"
//                ,"MOEA/CD-V"
//                ,"MOEA/CD-EHA"
//                ,
//                "MOEA/CD-2"
//                ,
//                "MOEA/CD-3"
//                "U-EA/CD",
//                "U-EA/CD-ACV",
//                "U-EA/CD-AECV"
                "MOEA/CD-ACV",
                "MOEA/CD-AECV"
        };
        String[] baseDirs = {
                "D://Experiments/ExperimentDataThesis/MOP/compare/",
//                "D://Experiments/ExperimentDataThesis/MOP/MOEACD-old/",
                "D://Experiments/ExperimentDataThesis/MOP/MOEACD/"
                ,
//                ,
//                "E://ResultsMOPMOEACD3/",
//                "E://ResultsMOPMOEACD4/"
//                ,"E://ResultsMOPMOEACD8/"
//                ,
//                "E://ResultsMOPMOEACD40/"
//                "E://ResultsMOPMOEACD44/"
//                ,
                "E://ResultsMOPMOEACD54/"
//                "E://ResultsMOPUEACD/"
        };

        String[][] dirs = {
                new String[]{
                        baseDirs[0]
                        ,
                        baseDirs[1]
                        ,
                        baseDirs[2]
//                        ,
//                        baseDirs[3]
                }
        };

        String[][] problemNameList = {
                new String[]{
                        "MOP1(2)"
                },
                new String[]{
                        "MOP2(2)"
                },
                new String[]{
                        "MOP3(2)"
                },
                new String[]{
                        "MOP4(2)"
                },
                new String[]{
                        "MOP5(2)"
                },
                new String[]{
                        "MOP6(3)"
                },
                new String[]{
                        "MOP7(3)"
                }
        };

        int[][] popsList = {
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        300
                },
                new int[]{
                        300
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                }
        };


        String[] probelms = {
                "MOP1",
                "MOP2",
                "MOP3",
                "MOP4",
                "MOP5",
                "MOP6",
                "MOP7"
        };
        int[][] problemObj = {
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                }
        };

        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }


    public void generateLatexMaOPScale() {

        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
//                "IGDPlus",
                "HV"
        };
        boolean[] isLowerTheBetter = {
//                true,
                false
        };
        String[] experimentName = {
//                "ScaleDTLZ(IGDPlus)",
                "ScaleDTLZ(HV)"
        };


        String[][] algorithmNameList = {
                new String[]{
                        "MOEADN_PBI",
//                        "NSGAIII",
                        "MOEADDN_PBI"
                },
//                new String[]{
//                        "MOEACD","MOEACD-N"
//                },
//                new String[]{
//                        "MOEACD","MOEACD-N"
//                },
                new String[]{
//                        "MOEACD",
                        "MOEACD-N-SBX"
//                        ,
//                        "MOEACD-N-F"
//                        ,
//                        "MOEACD-N"

                }
//                ,
//                new String[]{
//                        "MOEACD-N-SBX"
//                }
//                ,
//                new String[]{
//                        "MOEACD-N"
//                }
//                ,
//                new String[]{
//                        "MOEACD-N"
//                }
        };
        String[] algorithms = {
                "MOEA/D-N",
//                "NSGAIII",
                "MOEA/DD-N",
//                "MOEA/CD-O","MOEA/CD-O-N",
//                "MOEA/CD","MOEA/CD-N",
//                "MOEA/CD",
//                "MOEA/CD-N-SBX"
//                ,
//                "MOEA/CD-N-F"
//                ,
//                "MOEA/CD-N"
//                ,
                "MOEA/CD-N"
//                ,
//                "MOEA/CD-N"
        };

        String[] baseDirs = {
                "D://Experiments/ExperimentDataThesis/ScaledDTLZ/compare/",
//                "D://Experiments/ExperimentDataThesis/ScaledDTLZ/MOEACD-old/",
//                "D://Experiments/ExperimentDataThesis/ScaledDTLZ/MOEACD/"
//                ,
//                "E://ResultsScaleDTLZMOEACD44/"
//                ,
//                "E://ResultsScaleDTLZMOEACD46/"
                "E://ResultsScaleDTLZMOEACDWithoutEvolving/"
//                ,
//                "E://ResultsScaleDTLZMOEACDWithoutNeighbor/"
//                "E://ResultsScaleDTLZMOEACD80/",
//                "E://ResultsScaleDTLZMOEACD81/"
//                "E://ResultsScaleDTLZMOEACD90/"
        };

        String[][] dirs = {

                new String[]{
                        baseDirs[0],
                        baseDirs[1]
//                        ,
//                        baseDirs[2]
//                        ,baseDirs[3]
                }
        };

        String[][] problemNameList = {
                new String[]{
                        "SDTLZ1(3)", "SDTLZ1(5)", "SDTLZ1(8)", "SDTLZ1(10)", "SDTLZ1(15)"
                },
                new String[]{
                        "SDTLZ2(3)", "SDTLZ2(5)", "SDTLZ2(8)", "SDTLZ2(10)", "SDTLZ2(15)"
                },
                new String[]{
                        "SDTLZ3(3)", "SDTLZ3(5)", "SDTLZ3(8)", "SDTLZ3(10)", "SDTLZ3(15)"
                },
                new String[]{
                        "SDTLZ4(3)", "SDTLZ4(5)", "SDTLZ4(8)", "SDTLZ4(10)", "SDTLZ4(15)"
                },
                new String[]{
                        "Convex_SDTLZ2(3)", "Convex_SDTLZ2(5)", "Convex_SDTLZ2(8)", "Convex_SDTLZ2(10)", "Convex_SDTLZ2(15)"
                }
        };
        int[][] popsList = {
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                }

        };
        int[][] maxIterationsList = {
                new int[]{
                        400, 600, 750, 1000, 1500
                },
                new int[]{
                        250, 350, 500, 750, 1000
                },
                new int[]{
                        1000, 1000, 1000, 1500, 2000
                },
                new int[]{
                        600, 1000, 1250, 2000, 3000
                },
                new int[]{
                        250, 750, 2000, 4000, 4500
                }
        };


        String[] probelms = {
                "SDTLZ1",
                "SDTLZ2",
                "SDTLZ3",
                "SDTLZ4",
                "Convex_SDTLZ2"
        };

        int[][] problemObj = {
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                }
        };

        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }

    public void generateLatexConstraints() {

        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
                "HV"
        };
        boolean[] isLowerTheBetter = {
                false
        };
        String[] experimentName = {
                "ConstrainedDTLZ(HV)"
        };
        String[] baseDirs = {
//                "D://Experiments/ExperimentDataThesis/ConstrainedDTLZ/compare/",
//                "D://Experiments/ExperimentDataThesis/ConstrainedDTLZ/MOEACD/"
//                ,
//                "E://ResultsConstraintsMOEACD37/"
//                ,
//                "E://ResultsConstraintsMOEACD38/"
//                ,
//                "E://ResultsConstraintsMOEACD39/"
//                "E://ResultsConstraintsMOEACD60/",
//                "E://ResultsConstraintsMOEACD61/"
                "E://ResultsConstraintsMeasureMOEACD70/"
        };
        String[][] dirs = {
                new String[]{
                        baseDirs[0]
//                        ,
//                        baseDirs[1]
//                        ,
//                        baseDirs[2]
//                        ,
//                        baseDirs[3]
                }
        };

        String[][] algorithmNameList = {
//                new String[]{
//                        "CMOEAD_PBI",
//                        "CMOEADD_PBI"
//                },
//                new String[]{
//                        "C-MOEACD"
////                        ,
////                        "C-MOEACD-A"
//                },
                new String[]{
//                        "C-MOEACD-A",
//                        "C-MOEACD-AII",
//                        "C-MOEACD-AIII",
//                        "C-MOEACD-AIV",
//                        "C-MOEACD-AV",
////                        "C-MOEACD-AO",
//                        "C-MOEACD-AOII",
////                        "CUDEA",
////                        "CUDEA-II"
//                        "C-MOEACD-AOD"
////                        ,
////                        "CU-CDEA",
////                        "CU-CDEA-II"
                        "C-MOEACD",
                        "C-MOEACD-II",
                        "C-MOEACD-III",
                        "C-MOEACD-IV",
                        "C-MOEACD-V",
                        "C-MOEACD-VI",
                        "C-MOEACD-VII",
                        "C-MOEACD-VIII",
                        "C-MOEACD-A",
                        "C-MOEACD-AO",
                        "C-MOEACD-AOD"
                }
//                ,
//                new String[]{
//                        "CMOEAD_PBI"
//                        ,"C-MOEACD"
//                }
//                ,
//                new String[]{
//                        "C-MOEACD-0",
//                        "C-MOEACD-III"
//                        ,
//                        "C-MOEACD-II"
//                }
//                ,
//                new String[]{
//                        "C-MOEACD-II"
//                }
        };

        String[] algorithms = {
//                "C-MOEA/D",
//                "C-MOEA/DD",
//                "C-MOEA/CD"
//                ,
////                "C-MOEA/CD-A"
////                ,
//                "C-MOEA/CD-A",
//                "C-MOEA/CD-AII",
//                "C-MOEA/CD-AIII",
//                "C-MOEA/CD-AIV",
//                "C-MOEA/CD-AV",
////                "C-MOEA/CD-AO",
//                "C-MOEA/CD-AOII",
////                "CUDEA",
////                "CUDEA-II"
//                "C-MOEA/CD-AOD"
////                ,
////                "CU-CDEA",
////                "CU-CDEA-II"
////                ,
////                "C-MOEA/D"
////                ,
////                "C-MOEA/CD"
////                ,
////                "C-MOEA/CD-0"
////                ,
////                "C-MOEA/CD-III"
////                ,
////                "C-MOEA/CD-II"
////                ,
////                "C-MOEA/CD-II"
                "C-MOEACD",
                "C-MOEACD-II",
                "C-MOEACD-III",
                "C-MOEACD-IV",
                "C-MOEACD-V",
                "C-MOEACD-VI",
                "C-MOEACD-VII",
                "C-MOEACD-VIII",
                "C-MOEACD-A",
                "C-MOEACD-AO",
                "C-MOEACD-AOD"
        };

//        String[][] problemNameList = {
//                new String[]{
//                        "C1_DTLZ1(3)", "C1_DTLZ1(5)", "C1_DTLZ1(8)", "C1_DTLZ1(10)", "C1_DTLZ1(15)"
//                },
//                new String[]{
//                        "C1_DTLZ3(3)", "C1_DTLZ3(5)", "C1_DTLZ3(8)", "C1_DTLZ3(10)", "C1_DTLZ3(15)"
//                },
//                new String[]{
//                        "C2_DTLZ2(3)", "C2_DTLZ2(5)", "C2_DTLZ2(8)", "C2_DTLZ2(10)", "C2_DTLZ2(15)"
//                },
//                new String[]{
//                        "ConvexC2_DTLZ2(3)", "ConvexC2_DTLZ2(5)", "ConvexC2_DTLZ2(8)", "ConvexC2_DTLZ2(10)", "ConvexC2_DTLZ2(15)"
//                },
//                new String[]{
//                        "C3_DTLZ1(3)", "C3_DTLZ1(5)", "C3_DTLZ1(8)", "C3_DTLZ1(10)", "C3_DTLZ1(15)"
//                },
//                new String[]{
//                        "C3_DTLZ4(3)", "C3_DTLZ4(5)", "C3_DTLZ4(8)", "C3_DTLZ4(10)", "C3_DTLZ4(15)"
//                }
//        };
//
//        int[][] popsList = {
//                new int[]{
//                        91, 210, 156, 275, 135,
//                },
//                new int[]{
//                        91, 210, 156, 275, 135,
//                },
//                new int[]{
//                        91, 210, 156, 275, 135,
//                },
//                new int[]{
//                        91, 210, 156, 275, 135,
//                },
//                new int[]{
//                        91, 210, 156, 275, 135,
//                },
//                new int[]{
//                        91, 210, 156, 275, 135,
//                }
//        };
//
//        int[][] maxIterationsList = {
//                new int[]{
//                        500, 600, 800, 1000, 1500
//                },
//                new int[]{
//                        1000, 1500, 2500, 3500, 5000
//                },
//                new int[]{
//                        250, 350, 500, 750, 1000
//                },
//                new int[]{
//                        250, 750, 1500, 2500, 3500
//                },
//                new int[]{
//                        750, 1250, 2000, 3000, 4000
//                },
//                new int[]{
//                        750, 1250, 2000, 3000, 4000
//                }
//        };
//
//        int[][] problemObj = {
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                }
//        };
//        String[] probelms = {
//                "C1_DTLZ1",
//                "C1_DTLZ3",
//                "C2_DTLZ2",
//                "ConvexC2_DTLZ2",
//                "C3_DTLZ1",
//                "C3_DTLZ4"
//        };

        String[][] problemNameList = {
                new String[]{
                        "C1_DTLZ1(3)"
//                        , "C1_DTLZ1(5)"
                },
                new String[]{
                        "C1_DTLZ3(3)"
//                        , "C1_DTLZ3(5)"
                },
                new String[]{
                        "C2_DTLZ2(3)"
//                        , "C2_DTLZ2(5)"
                },
                new String[]{
                        "ConvexC2_DTLZ2(3)"
//                        , "ConvexC2_DTLZ2(5)"
                },
                new String[]{
                        "C3_DTLZ1(3)"
//                        , "C3_DTLZ1(5)"
                },
                new String[]{
                        "C3_DTLZ4(3)"
//                        , "C3_DTLZ4(5)"
                }
        };

        int[][] popsList = {
                new int[]{
                        91
//                        , 210
                },
                new int[]{
                        91
//                        , 210
                },
                new int[]{
                        91
//                        , 210
                },
                new int[]{
                        91
//                        , 210
                },
                new int[]{
                        91
//                        , 210
                },
                new int[]{
                        91
//                        , 210
                }
        };

        int[][] maxIterationsList = {
                new int[]{
                        500
//                        , 600
                },
                new int[]{
                        1000
//                        , 1500
                },
                new int[]{
                        250
//                        , 350
                },
                new int[]{
                        250
//                        , 750
                },
                new int[]{
                        750
//                        , 1250
                },
                new int[]{
                        750
//                        , 1250
                }
        };

        int[][] problemObj = {
                new int[]{
                        3
//                        , 5
                },
                new int[]{
                        3
//                        , 5
                },
                new int[]{
                        3
//                        , 5
                },
                new int[]{
                        3
//                        , 5
                },
                new int[]{
                        3
//                        , 5
                },
                new int[]{
                        3
//                        , 5
                }
        };

        String[] probelms = {
                "C1_DTLZ1",
                "C1_DTLZ3",
                "C2_DTLZ2",
                "ConvexC2_DTLZ2",
                "C3_DTLZ1",
                "C3_DTLZ4"
        };

        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }


    public void generateLatexConstrainedEnegineer() {

        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
                "HV"
        };
        boolean[] isLowerTheBetter = {
                false
        };
        String[] experimentName = {
                "ConstrainedEngineer(HV)"
        };
        String[] baseDirs = {
                "D://Experiments/ExperimentDataThesis/Engineer/Constraints/compare/",
                "D://Experiments/ExperimentDataThesis/Engineer/Constraints/MOEACD/"
//                ,
//                "E://ResultsEngineerConstraintsMOEACD4/"
//                ,
//                "E://ResultsEngineerConstraintsMOEACD3/"
//                ,
//                "E://ResultsEngineerConstraintsMOEACD31/"
        };
        String[][] dirs = {
                new String[]{
                        baseDirs[0]
                        ,
                        baseDirs[1]
//                        ,
//                        baseDirs[2]
//                        ,
//                        baseDirs[3]
                }
        };
        String[][] problemNameList = {
                new String[]{
                        "CarSideImpact(3)"
                },
//                new String[]{
//                        "NCarSideImpact(3)"
//                },
                new String[]{
                        "Machining(4)"
                },
                new String[]{
                        "Water(5)"
                }
//                ,
//                new String[]{
//                        "NWater(5)"
//                }
        };

        int[][] popsList = {
                new int[]{
                        91
                },
//                new int[]{
//                        91
//                },
                new int[]{
                        165
                },
//                new int[]{
//                        210
//                },
                new int[]{
                        210
                }
        };

        int[][] maxIterationsList = {
                new int[]{
                        500
                },
//                new int[]{
//                        500
//                },
                new int[]{
                        750
                },
//                new int[]{
//                        1000
//                },
                new int[]{
                        1000
                }
        };


        String[][] algorithmNameList = {
                new String[]{
//                        "CMOEAD_PBI",


                        "CMOEADN_PBI",
                        "CMOEADDN_PBI"
                },
                new String[]{
//                        "C-MOEACD",
                        "C-MOEACD-N",
                        "C-MOEACD-NA"
                        ,
                        "C-MOEACD-ND",
                        "C-MOEACD-NAD"
                }
//                ,
//                new String[]{
//                        "C-MOEACD-N",
//                        "C-MOEACD-NA"
////                        ,
////                        "C-MOEACD-ND",
////                        "C-MOEACD-NAD"
//                }
//                  ,
//                new String[]{
//                        "C-MOEACD-N",
//                        "C-MOEACD-NA"
//                        ,
//                        "C-MOEACD-ND",
//                        "C-MOEACD-NAD"
//                }

        };


        String[] probelms = {
                "CarSideImpact",
//                "NCarSideImpact"
//                ,
                "Machining",
                "Water"
//                , "NWater"
        };

        int[][] problemObj = {
                new int[]{
                        3
                },
//                new int[]{
//                        3
//                },
                new int[]{
                        4
                },
//                new int[]{
//                        5
//                },
                new int[]{
                        5
                }
        };
        String[] algorithms = {
//                "C-MOEA/D",


                "C-MOEA/D-N",
                "C-MOEA/DD-N",


//                "C-MOEA/CD",


                "C-MOEA/D-N",
                "C-MOEA/CD-NA",
                "C-MOEA/CD-ND",
                "C-MOEA/CD-NAD"
//                ,
//                "C-MOEA/D-N",
//                "C-MOEA/CD-NA"
////                ,
////                "C-MOEA/CD-ND",
////                "C-MOEA/CD-NAD"
//                , "C-MOEA/D-N",
//                "C-MOEA/CD-NA"
//                ,
//                "C-MOEA/CD-ND",
//                "C-MOEA/CD-NAD"

        };

        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }

    public void generateLatexUnConstrainedEngineer() {

        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
                "HV"
        };
        boolean[] isLowerTheBetter = {
                false
        };
        String[] experimentName = {
                "Engineer(HV)"
        };
        String[] baseDirs = {
                "D://Experiments/ExperimentDataThesis/Engineer/UnConstraints/compare/",
//                "D://Experiments/ExperimentDataThesis/Engineer/UnConstraints/MOEACD-old/",
//                "D://Experiments/ExperimentDataThesis/Engineer/UnConstraints/MOEACD/"
//                "E://ResultsEngineerUnConstraintsCompare7/",
//                "E://ResultsEngineerUnConstraintsMOEACD7/"
//                "E://ResultsEngineerUnConstraintsMOEACD8/",
//                "E://ResultsEngineerUnConstraintsMOEACD8/"
//                "E://ResultsEngineerUnConstraintsMOEACD12/",
//                "E://ResultsEngineerUnConstraintsMOEACD12/"
//                ,
//                "E://ResultsEngineerUnConstraintsMOEACD46/"
                "E://ResultsEngineerUnConstraintsMOEACDWithoutEvolving/"
//                ,
//                "E://ResultsEngineerUnConstraintsMOEACDWithoutNeighbor/"
//                "E://ResultsEngineerUnConstraintsMOEACD90/"
        };
        String[][] dirs = {
                new String[]{
                        baseDirs[0]
                        ,
                        baseDirs[1]
//                        ,
//                        baseDirs[2]
                }
        };

        String[][] problemNameList = {
                new String[]{
                        "CrashWorthiness(3)"
                }
                ,
                new String[]{
                        "UCarSideImpact(9)"
                }
                ,
////                new String[]{
//                        "F_UCarSideImpact(9)"
//                }
                new String[]{
                        "CarCabDesign(9)"
                }
        };

        int[][] popsList = {
                new int[]{
                        153
                }
                ,
                new int[]{
                        210
                },
                new int[]{
                        210
                }
        };

        int[][] maxIterationsList = {
                new int[]{
                        200
                }
                ,
                new int[]{
                        2000
                } ,
                new int[]{
                        2000
                }
        };


        String[][] algorithmNameList = {
                new String[]{
//                        "MOEAD_PBI",
                        "MOEADN_PBI",
//                        "MOEADD_PBI",
                        "MOEADDN_PBI"
                }
                ,
//                new String[]{
//                        "MOEACD-N"
//                },
//                new String[]{
//                        "MOEACD",
//                        "MOEACD-N",
////                        "MOEACD-D",
//                        "MOEACD-ND"
//                }
//                ,
//                new String[]{
////                        "MOEACD",
////                        "MOEACD-N"
////                        ,
//                        "MOEACD-N-SBX"
////                        ,
////                        "MOEACD-N-F"
////                        ,
////                        "MOEACD-ND"
//                }
//                ,
                new String[]{
                        "MOEACD-N-SBX",
//                        "MOEACD-ND"
                }
        };


        String[] probelms = {
                "CrashWorthiness"
                ,
                "UCarSideImpact"
                ,
//                "F_UCarSideImpact(9)"
                "CarCabDesign"
        };


        int[][] problemObj = {
                new int[]{
                        3
                }
                ,
                new int[]{
                        9
                }
                ,
                new int[]{
                        9
                }
        };

        String[] algorithms = {
//                "MOEA/D",


                "MOEA/D-N",
//                "MOEA/DD",
                "MOEA/DD-N",
//                "MOEA/CD-N",


//                "MOEA/CD",
//
//
//                "MOEA/CD-N",
////                "MOEA/CD-D",
//                "MOEA/CD-ND"
//
//                ,
//                "MOEA/CD",
//                "MOEA/CD-N"
//                ,
//                "MOEA/CD-N-SBX"
//                ,
//                "MOEA/CD-N-F"
//                ,
//                "MOEA/CD-ND"
//                ,
                "MOEA/CD-N"
//                ,
//                "MOEA/CD-ND"
        };


        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }

    public void generateTimeMatlabMaOP(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";

        String indicator = "Time";

        String experimentName = "TimeDTLZ";

        String[] dirs = {
                "D://Experiments/ExperimentDataThesis/DTLZ/compare/",
//                "D://Experiments/ExperimentDataThesis/DTLZ/MOEACD-old/",
//                "D://Experiments/ExperimentDataThesis/DTLZ/MOEACD/",
                "E://ResultsMaOPMOEACDWithoutEvolving/"
//                ,
//                "E://ResultsMaOPMOEACDWithoutNeighbor/"
        };

        String[][] problemNameList = {
                new String[]{
                        "DTLZ1(3)", "DTLZ1(5)", "DTLZ1(8)", "DTLZ1(10)", "DTLZ1(15)"
                },
                new String[]{
                        "DTLZ2(3)", "DTLZ2(5)", "DTLZ2(8)", "DTLZ2(10)", "DTLZ2(15)"
                },
                new String[]{
                        "DTLZ3(3)", "DTLZ3(5)", "DTLZ3(8)", "DTLZ3(10)", "DTLZ3(15)"
                },
                new String[]{
                        "DTLZ4(3)", "DTLZ4(5)", "DTLZ4(8)", "DTLZ4(10)", "DTLZ4(15)"
                },
                new String[]{
                        "Convex_DTLZ2(3)", "Convex_DTLZ2(5)", "Convex_DTLZ2(8)", "Convex_DTLZ2(10)", "Convex_DTLZ2(15)"
                }
        };
        int[][] popsList = {
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        400, 600, 750, 1000, 1500
                },
                new int[]{
                        250, 350, 500, 750, 1000
                },
                new int[]{
                        1000, 1000, 1000, 1500, 2000
                },
                new int[]{
                        600, 1000, 1250, 2000, 3000
                },
                new int[]{
                        250, 750, 2000, 4000, 4500
                }
        };

        String[][] algorithmNameList = {

                new String[]{
                        "MOEADD_PBI", "NSGAIII",
//                        "MOEADACDSBX_PBI",
                        "MOEADACD_PBI",
//                        "MOEADAGRSBX_PBI",
                        "MOEADAGR_PBI",
                        "MOEAD_PBI"
//                        , "MOEADDE_PBI"
                },
//                new String[]{
//                        "MOEACD"
//                },
//                new String[]{
//                        "MOEACD-SBX"
////                        , "MOEACD-DE",
//////                        "MOEACD-F",
////                        "MOEACD"
//                },
                new String[]{
                        "MOEACD-SBX"
                }

        };


        String[] probelms = {
                "DTLZ1",
                "DTLZ2",
                "DTLZ3",
                "DTLZ4",
                "Convex\\_DTLZ2"
        };

        int[][] problemObj = {
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                }
        };

        int[] nObj = {
                3, 5, 8, 10, 15
        };
        String[] algorithms = {
                "MOEA/DD", "NSGAIII",
                "MOEA/D-ACD",
//                "MOEA/D-ACD-DE",
                "MOEA/D-AGR",
//                "MOEA/D-AGR-DE",
                "MOEA/D",
//                "MOEA/D-DE",
//                "MOEA/CD",
////                "MOEA/CD-SBX",
////                "MOEA/CD-DE",
////                "MOEA/CD-F",
//                "MOEA/CD",
                "MOEA/CD"
        };

        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "");

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

        mean = new double[sizeProblem][][];

        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

            mean[iProblem] = new double[sizeProblemObj][];

            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

                mean[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    double sum = 0.0;
                    for (int i = 0; i < indicatorData.size(); i++)
                        sum += indicatorData.get(i);

                    mean[iProblem][jObj][kAlgorithm] = sum / indicatorData.size();

                }
            }
        }
        JMetalLogger.logger.info("finish computeDataStatistics");

        try {
            File matlabOutput;
            matlabOutput = new File(matlabDir);
            if (!matlabOutput.exists()) {
                boolean result = new File(matlabDir).mkdirs();
                JMetalLogger.logger.info("Creating " + matlabDir + " directory");
            }
            //System.out.println("Experiment name: " + experimentName_);
            String matlabFile = matlabDir + "/" + experimentName + ".m";

            FileWriter os = new FileWriter(matlabFile, false);
            for (int iObj = 0; iObj < nObj.length; iObj++) {
                os.write("\n %%%%%%%%%%%%%%%%%%%%%%%%%%\n ");
                os.write("% " + nObj[iObj] + "\n");
                os.write("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n");
                os.write("figure;" + "\n");
                os.write("z = [" + "\n");
                for (int jAlgorithm = 0; jAlgorithm < algorithms.length; jAlgorithm++) {
                    for (int kProblem = 0; kProblem < probelms.length; kProblem++) {
                        os.write(" " + mean[kProblem][iObj][jAlgorithm] + " ");
                        if (kProblem != probelms.length - 1)
                            os.write(",");
                    }
                    if (jAlgorithm != algorithms.length - 1)
                        os.write(";");
                    os.write("\n");
                }
                os.write("];\n");

                os.write("bar3(z);" + "\n");
                if (isChinese) {
                    os.write("set(gca,'FontSize',12,'FontName','宋体','FontWeight','Bold');" + "\n");
                    os.write("title('" + nObj[iObj] + "目标DTLZ测试例的平均运行时间');" + "\n");
                } else {
                    os.write("set(gca,'FontSize',12,'FontName','Arial','FontWeight','Bold');" + "\n");
                    os.write("title('The running time for " + nObj[iObj] + "-objective DTLZ');" + "\n");
                }
                os.write("set(gca,'xticklabel',{");
                for (int kProblem = 0; kProblem < probelms.length; kProblem++) {
                    os.write(" '" + probelms[kProblem] + "' ");
                }
                os.write("});" + "\n");

//                os.write("xlabel('Test Instance');"+"\n");
                os.write("set(gca,'yticklabel',{");
                for (int jAlgorithm = 0; jAlgorithm < algorithms.length; jAlgorithm++)
                    os.write(" '" + algorithms[jAlgorithm] + "' ");
                os.write("});" + "\n");
//                os.write("ylabel('Algorithm');"+"\n");
                if (isChinese)
                    os.write("zlabel('平均运行时间(ms)');" + "\n");
                else
                    os.write("zlabel('Time(ms)');" + "\n");
                os.write("\n\n");
            }
            os.close();
        } catch (IOException e) {
        }
    }

    public void generateTimeMatlabMOP2D(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";


        String indicator = "Time";

        String experimentName = "TimeMOP2Objective";

        String[] dirs = {
                "D://Experiments/ExperimentDataThesis/MOP/compare/",
//                "D://Experiments/ExperimentDataThesis/MOP/MOEACD-old/",
                "D://Experiments/ExperimentDataThesis/MOP/MOEACD/"
        };
        String[][] problemNameList = {
                new String[]{
                        "MOP1(2)"
                },
                new String[]{
                        "MOP2(2)"
                },
                new String[]{
                        "MOP3(2)"
                },
                new String[]{
                        "MOP4(2)"
                },
                new String[]{
                        "MOP5(2)"
                }
        };

        int[][] popsList = {
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                }
        };


        String[][] algorithmNameList = {

                new String[]{
                        "MOEADD_PBI", "NSGAIII",
//                        "MOEADACDSBX_PBI",
                        "MOEADACD_PBI",
//                        "MOEADAGRSBX_PBI",
                        "MOEADAGR_PBI",
                        "MOEAD_PBI"
//                        , "MOEADDE_PBI"
                },
//                new String[]{
//                        "MOEACD"
//                },
                new String[]{
                        "MOEACD-SBX", "MOEACD-DE",
//                        "MOEACD-F",
                        "MOEACD"
                }
        };


        String[] probelms = {
                "MOP1",
                "MOP2",
                "MOP3",
                "MOP4",
                "MOP5"
        };
        int[][] problemObj = {
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                }
        };


        String[] algorithms = {
                "MOEA/DD", "NSGAIII",
                "MOEA/D-ACD",
//                "MOEA/D-ACD-DE",
                "MOEA/D-AGR",
//                "MOEA/D-AGR-DE",
                "MOEA/D",
//                "MOEA/D-DE",
//                "MOEA/CD",
                "MOEA/CD-SBX", "MOEA/CD-DE",
//                "MOEA/CD-F",
                "MOEA/CD"
        };

        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "");

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

        mean = new double[sizeProblem][][];

        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

            mean[iProblem] = new double[sizeProblemObj][];

            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

                mean[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    double sum = 0.0;
                    for (int i = 0; i < indicatorData.size(); i++)
                        sum += indicatorData.get(i);

                    mean[iProblem][jObj][kAlgorithm] = sum / indicatorData.size();

                }
            }
        }
        JMetalLogger.logger.info("finish computeDataStatistics");

        try {
            File matlabOutput;
            matlabOutput = new File(matlabDir);
            if (!matlabOutput.exists()) {
                boolean result = new File(matlabDir).mkdirs();
                JMetalLogger.logger.info("Creating " + matlabDir + " directory");
            }
            //System.out.println("Experiment name: " + experimentName_);
            String matlabFile = matlabDir + "/" + experimentName + ".m";

            FileWriter os = new FileWriter(matlabFile, false);

            os.write("figure;" + "\n");
            os.write("z = [" + "\n");
            for (int jAlgorithm = 0; jAlgorithm < algorithms.length; jAlgorithm++) {
                for (int kProblem = 0; kProblem < probelms.length; kProblem++) {
                    os.write(" " + mean[kProblem][0][jAlgorithm] + " ");
                    if (kProblem != probelms.length - 1)
                        os.write(",");
                }
                if (jAlgorithm == algorithms.length - 1)
                    os.write(";");
                os.write("\n");
            }
            os.write("];\n");

            os.write("bar3(z);" + "\n");

            if (isChinese) {
                os.write("set(gca,'FontSize',12,'FontName','宋体','FontWeight','Bold');" + "\n");
                os.write("title('2目标MOP测试例的平均运行时间')" + "\n");
            } else {
                os.write("set(gca,'FontSize',12,'FontName','Arial','FontWeight','Bold');" + "\n");
                os.write("title('The running time for 2-objective MOP')" + "\n");
            }
            os.write("set(gca,'xticklabel',{");
            for (int kProblem = 0; kProblem < probelms.length; kProblem++) {
                os.write(" '" + probelms[kProblem] + "' ");
            }
            os.write("});" + "\n");

//                os.write("xlabel('Test Instance');"+"\n");
            os.write("set(gca,'yticklabel',{");
            for (int jAlgorithm = 0; jAlgorithm < algorithms.length; jAlgorithm++)
                os.write(" '" + algorithms[jAlgorithm] + "' ");
            os.write("});" + "\n");
//                os.write("ylabel('Algorithm');"+"\n");
            if (isChinese)
                os.write("zlabel('平均运行时间(ms)');" + "\n");
            else
                os.write("zlabel('Time(ms)');" + "\n");
            os.write("\n\n");

            os.close();
        } catch (IOException e) {
        }
    }


    public void generateTimeMatlabMOP3D(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";

        String indicator = "Time";

        String experimentName = "TimeMOP3Objective";

        String[] dirs = {
                "D://Experiments/ExperimentDataThesis/MOP/compare/",
//                "D://Experiments/ExperimentDataThesis/MOP/MOEACD-old/",
                "D://Experiments/ExperimentDataThesis/MOP/MOEACD/"
        };
        String[][] problemNameList = {
                new String[]{
                        "MOP6(3)"
                },
                new String[]{
                        "MOP7(3)"
                }
        };

        int[][] popsList = {
                new int[]{
                        300
                },
                new int[]{
                        300
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        3000
                },
                new int[]{
                        3000
                }
        };


        String[][] algorithmNameList = {

                new String[]{
                        "MOEADD_PBI", "NSGAIII",
//                        "MOEADACDSBX_PBI",
                        "MOEADACD_PBI",
//                        "MOEADAGRSBX_PBI",
                        "MOEADAGR_PBI",
                        "MOEAD_PBI"
//                        , "MOEADDE_PBI"
                },
//                new String[]{
//                        "MOEACD"
//                },
                new String[]{
                        "MOEACD-SBX", "MOEACD-DE",
//                        "MOEACD-F",
                        "MOEACD"
                }
        };


        String[] probelms = {
                "MOP6",
                "MOP7"
        };
        int[][] problemObj = {
                new int[]{
                        3
                },
                new int[]{
                        3
                }
        };


        String[] algorithms = {
                "MOEA/DD", "NSGAIII",
                "MOEA/D-ACD",
//                "MOEA/D-ACD-DE",
                "MOEA/D-AGR",
//                "MOEA/D-AGR-DE",
                "MOEA/D",
//                "MOEA/D-DE",
//                "MOEA/CD",
                "MOEA/CD-SBX", "MOEA/CD-DE",
//                "MOEA/CD-F",
                "MOEA/CD"
        };

        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "");

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

        mean = new double[sizeProblem][][];

        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

            mean[iProblem] = new double[sizeProblemObj][];

            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

                mean[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    double sum = 0.0;
                    for (int i = 0; i < indicatorData.size(); i++)
                        sum += indicatorData.get(i);

                    mean[iProblem][jObj][kAlgorithm] = sum / indicatorData.size();

                }
            }
        }
        JMetalLogger.logger.info("finish computeDataStatistics");

        try {
            File matlabOutput;
            matlabOutput = new File(matlabDir);
            if (!matlabOutput.exists()) {
                boolean result = new File(matlabDir).mkdirs();
                JMetalLogger.logger.info("Creating " + matlabDir + " directory");
            }
            //System.out.println("Experiment name: " + experimentName_);
            String matlabFile = matlabDir + "/" + experimentName + ".m";

            FileWriter os = new FileWriter(matlabFile, false);

            os.write("figure;" + "\n");
            os.write("z = [" + "\n");
            for (int jAlgorithm = 0; jAlgorithm < algorithms.length; jAlgorithm++) {
                for (int kProblem = 0; kProblem < probelms.length; kProblem++) {
                    os.write(" " + mean[kProblem][0][jAlgorithm] + " ");
                    if (kProblem != probelms.length - 1)
                        os.write(",");
                }
                if (jAlgorithm == algorithms.length - 1)
                    os.write(";");
                os.write("\n");
            }
            os.write("];\n");

            os.write("bar3(z);" + "\n");
            if (isChinese) {
                os.write("set(gca,'FontSize',12,'FontName','宋体','FontWeight','Bold');" + "\n");
                os.write("title('3目标MOP测试例的平均运行时间')" + "\n");
            } else {
                os.write("set(gca,'FontSize',12,'FontName','Arial','FontWeight','Bold');" + "\n");
                os.write("title('The running time for 3-objective MOP')" + "\n");
            }
            os.write("set(gca,'xticklabel',{");
            for (int kProblem = 0; kProblem < probelms.length; kProblem++) {
                os.write(" '" + probelms[kProblem] + "' ");
            }
            os.write("});" + "\n");

//                os.write("xlabel('Test Instance');"+"\n");
            os.write("set(gca,'yticklabel',{");
            for (int jAlgorithm = 0; jAlgorithm < algorithms.length; jAlgorithm++)
                os.write(" '" + algorithms[jAlgorithm] + "' ");
            os.write("});" + "\n");
//                os.write("ylabel('Algorithm');"+"\n");
            if (isChinese)
                os.write("zlabel('平均运行时间(ms)');" + "\n");
            else
                os.write("zlabel('Time(ms)');" + "\n");
            os.write("\n\n");

            os.close();
        } catch (IOException e) {
        }
    }

    public void generateMedianPFMatlabMOP(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";
        String saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/Figure/MOP/";
        String latexDir = "D://Experiments/ExperimentDataThesis/Figure/MOP/";
        if (isChinese) {
            saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/FigureZH/MOP/";
            latexDir = "D://Experiments/ExperimentDataThesis/FigureZH/MOP/";
        }

        String indicator = "HV";

        String experimentName = "PF4MOP";

        String[] dirs = {
                "D://Experiments/ExperimentDataThesis/MOP/compare/",
//                "D://Experiments/ExperimentDataThesis/MOP/MOEACD-old/",
                "D://Experiments/ExperimentDataThesis/MOP/MOEACD/"
                ,"E://ResultsMOPMOEACD54/"
        };
        String[][] problemNameList = {
                new String[]{
                        "MOP1(2)"
                },
                new String[]{
                        "MOP2(2)"
                },
                new String[]{
                        "MOP3(2)"
                },
                new String[]{
                        "MOP4(2)"
                },
                new String[]{
                        "MOP5(2)"
                }
                ,
                new String[]{
                        "MOP6(3)"
                },
                new String[]{
                        "MOP7(3)"
                }
        };

        int[][] popsList = {
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                }
                ,
                new int[]{
                        300
                },
                new int[]{
                        300
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                }
                ,
                new int[]{
                        3000
                },
                new int[]{
                        3000
                }
        };


        String[][] algorithmNameList = {

                new String[]{
                        "MOEADD_PBI", "NSGAIII",
//                        "MOEADACDSBX_PBI",
                        "MOEADACD_PBI",
//                        "MOEADAGRSBX_PBI",
                        "MOEADACD_PBI",
                        "MOEAD_PBI", "MOEADDE_PBI"
                },
//                new String[]{
//                       "MOEACD"
//                },
                new String[]{
//                        "MOEACD-SBX", "MOEACD-DE", "MOEACD-F",
                        "MOEACD"
                }
                ,
                new String[]{
                        "MOEACD-ACV",
                        "MOEACD-AECV"
                }

        };


        String[] probelms = {
                "MOP1",
                "MOP2",
                "MOP3",
                "MOP4",
                "MOP5"
                ,
                "MOP6",
                "MOP7"
        };
        int[][] problemObj = {
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                }
                ,
                new int[]{
                        3
                },
                new int[]{
                        3
                }
        };


        String[] algorithms = {
                "MOEA/DD", "NSGA-III",
//                "MOEA/D-ACD-SBX",
                "MOEA/D-ACD",
//                "MOEA/D-AGR-SBX",
                "MOEA/D-AGR",
                "MOEA/D", "MOEA/D-DE",
//                "MOEA/CD-O",
//                "MOEA/CD-SBX", "MOEA/CD-DE", "MOEA/CD-F",
                "MOEA/CD"
                ,
                "MOEA/CD-ACV",
                "MOEA/CD-AECV"

        };


        String[] pfDir = new String[algorithms.length];
        int c = 0;
        for (int i = 0; i < algorithmNameList.length; i++) {
            for (int j = 0; j < algorithmNameList[i].length; j++) {
                String pf = dirs[i] + "POF/" + algorithmNameList[i][j];
                pfDir[c] = pf;
                c++;
            }
        }


        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "final_");

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

//        medianRun = new double[sizeProblem][][];
        File matlabOutput;
        matlabOutput = new File(matlabDir);
        if (!matlabOutput.exists()) {
            boolean result = new File(matlabDir).mkdirs();
            JMetalLogger.logger.info("Creating " + matlabDir + " directory");
        }
        //System.out.println("Experiment name: " + experimentName_);
        String matlabFile = matlabDir + "/" + experimentName + ".m";
        try {
            FileWriter os = new FileWriter(matlabFile, false);
            os.close();
        } catch (IOException e) {
        }


        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

//            medianRun[iProblem] = new double[sizeProblemObj][];
            String latexFile = latexDir + "/" + experimentName + "_" + probelms[iProblem] + ".tex";

            try {
                printHeaderLatexCommands(latexFile, experimentName);
            } catch (IOException e) {
            }
            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

//                medianRun[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    List<Integer> idxRun = new ArrayList<>(indicatorData.size());
                    for (int q = 0; q < indicatorData.size(); q++)
                        idxRun.add(q);
                    sort(indicatorData, idxRun);
//                    medianRun[iProblem][jObj][kAlgorithm] = (double)idxRun.get(idxRun.size()/2);
                    int medianRun = idxRun.get(idxRun.size() / 2);
                    String instance = problemNameList[iProblem][jObj] + "_" + popsList[iProblem][jObj] + "_" + maxIterationsList[iProblem][jObj];
                    String saveInstanceName = instance.replace("(", "[").replace(")", "]");

                    String pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + medianRun + ".pof";
                    String saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem],problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);


                    int bestRun = idxRun.get(idxRun.size() - 1);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + bestRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (isChinese)
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);


                    int worstRun = idxRun.get(0);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + worstRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);

                    String preStr = "pf_";
                    if (problemObj[iProblem][jObj] > 3)
                        preStr = "pc_";
                    pfOrPcLatexScript(latexFile, preStr, algorithms[kAlgorithm], saveInstanceName);
                }
            }
            try {
                printEndLatexCommands(latexFile);
            } catch (IOException e) {
            }


        }


    }


    public void generateMedianPFMatlabDTLZ(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";
        String saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/Figure/DTLZ/";
        String latexDir = "D://Experiments/ExperimentDataThesis/Figure/DTLZ/";
//        String saveMatlabFigureDir = "D://DTLZ/";
//        String latexDir = "D://DTLZ/";
        if (isChinese) {
            saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/FigureZH/DTLZ/";
            latexDir = "D://Experiments/ExperimentDataThesis/FigureZH/DTLZ/";
        }

        String indicator = "HV";

        String experimentName = "PF4DTLZ";


        String[] dirs = {
//                "D://Experiments/ExperimentDataThesis/DTLZ/compare/",
//                "D://Experiments/ExperimentDataThesis/DTLZ/MOEACD-old/",
//                "D://Experiments/ExperimentDataThesis/DTLZ/MOEACD/"
//                ,
//                "E://ResultsMaOPMOEACDNoDomination/",
//                "E://ResultsMaOPMOEACDWithoutEvolving/",
//                "E://ResultsMaOPMOEACDNoDominationWithoutEvolving1/",
//                "E://ResultsMaOPMOEACDNoDominationWithoutEvolving2/"
//                "E://ResultsMaOPMOEACD123/"
                "E://ResultsMaOPMOEACDND_N_B3_0/",
                "E://ResultsMaOPMOEACDND_NR_B3_0/",
                "E://ResultsMaOPMOEACDND_N_B2_0/",
                "E://ResultsMaOPMOEACDND_N_NM_B3_0/",
                "E://ResultsMaOPMOEACDND_N_NM_B2_0/"
        };

        String[][] problemNameList = {
//                new String[]{
//                        "DTLZ1(3)", "DTLZ1(5)", "DTLZ1(8)", "DTLZ1(10)", "DTLZ1(15)"
//                },
//                new String[]{
//                        "DTLZ2(3)", "DTLZ2(5)", "DTLZ2(8)", "DTLZ2(10)", "DTLZ2(15)"
//                },
//                new String[]{
//                        "DTLZ3(3)", "DTLZ3(5)", "DTLZ3(8)", "DTLZ3(10)", "DTLZ3(15)"
//                },
                new String[]{
                        "DTLZ4(3)", "DTLZ4(5)", "DTLZ4(8)", "DTLZ4(10)", "DTLZ4(15)"
                }
//                ,
//                new String[]{
//                        "Convex_DTLZ2(3)", "Convex_DTLZ2(5)", "Convex_DTLZ2(8)", "Convex_DTLZ2(10)", "Convex_DTLZ2(15)"
//                }
        };
        int[][] popsList = {
//                new int[]{
//                        91, 210, 156, 275, 135
//                },
//                new int[]{
//                        91, 210, 156, 275, 135
//                },
//                new int[]{
//                        91, 210, 156, 275, 135
//                },
//                new int[]{
//                        91, 210, 156, 275, 135
//                },
                new int[]{
                        91, 210, 156, 275, 135
                }
        };
        int[][] maxIterationsList = {
//                new int[]{
//                        400, 600, 750, 1000, 1500
//                },
//                new int[]{
//                        250, 350, 500, 750, 1000
//                },
//                new int[]{
//                        1000, 1000, 1000, 1500, 2000
//                },
                new int[]{
                        600, 1000, 1250, 2000, 3000
                }
//                ,
//                new int[]{
//                        250, 750, 2000, 4000, 4500
//                }
        };

        String[][] algorithmNameList = {

//                new String[]{
//                        "MOEADD_PBI",
//                        "NSGAIII",
//                        "MOEADACDSBX_PBI",
////                        "MOEADACD_PBI",
//                        "MOEADAGRSBX_PBI",
////                        "MOEADAGR_PBI",
//                        "MOEAD_PBI", "MOEADDE_PBI"
//                },
//                new String[]{
//                        "MOEACD"
//                },
                new String[]{
                        "MOEACD-SBX"
//                        , "MOEACD-DE",
//                        "MOEACD-F",
//                        "MOEACD"
                }
//                ,
//                new String[]{
//                        "MOEACD-V"
//                        ,
//                        "MOEACD-EHA"
//                }
//                ,
//                new String[]{
//                        "MOEACD-SBX"
//                },
//                new String[]{
//                        "MOEACD-SBX"
//                },
//                new String[]{
//                        "MOEACD-SBX"
//                }
        };


        String[] probelms = {
//                "DTLZ1",
//                "DTLZ2",
//                "DTLZ3",
                "DTLZ4"
//                ,
//                "Convex\\_DTLZ2"
        };

        int[][] problemObj = {
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
                new int[]{
                        3, 5, 8, 10, 15
                }
        };

        int[] nObj = {
                3, 5, 8, 10, 15
        };
        String[] algorithms = {
//                "MOEA/DD",
//                "NSGAIII",
////                "MOEA/D-ACD-SBX",
//                "MOEA/D-ACD",
////                "MOEA/D-AGR-SBX",
//                "MOEA/D-AGR",
//                "MOEA/D", "MOEA/D-DE",
////                "MOEA/CD-O",
                "MOEA/CD-SBX"
//                , "MOEA/CD-DE",
//                "MOEA/CD-F",
//                "MOEA/CD"
//                ,
//                "MOEA/CD-V"
//                ,
//                "MEOA/CD-EHA"
//                ,
//                "MOEA/CD-SBX1"
//                ,
//                "MOEA/CD-SBX2"
//                ,
//                "MOEA/CD-SBX3"
        };


        int maxRun = 20;


        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "final_");

        String[] pfDir = new String[algorithms.length];
        int c = 0;
        for (int i = 0; i < algorithmNameList.length; i++) {
            for (int j = 0; j < algorithmNameList[i].length; j++) {
                pfDir[c] = dirs[i] + "POF/" + algorithmNameList[i][j];
                c++;
            }
        }
//        int maxRun = 20;
//
//        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, problemObj, hvRefPointList, dirs,"");

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

//        medianRun = new double[sizeProblem][][];
        File matlabOutput;
        matlabOutput = new File(matlabDir);
        if (!matlabOutput.exists()) {
            boolean result = new File(matlabDir).mkdirs();
            JMetalLogger.logger.info("Creating " + matlabDir + " directory");
        }
        //System.out.println("Experiment name: " + experimentName_);
        String matlabFile = matlabDir + "/" + experimentName + ".m";

        try {
            FileWriter os = new FileWriter(matlabFile, false);
            os.close();
        } catch (IOException e) {
        }


        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

//            medianRun[iProblem] = new double[sizeProblemObj][];
            String latexFile = latexDir + "/" + experimentName + "_" + probelms[iProblem].replace("\\", "") + ".tex";
            try {
                printHeaderLatexCommands(latexFile, experimentName);
            } catch (IOException e) {
            }

            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

//                medianRun[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    List<Integer> idxRun = new ArrayList<>(indicatorData.size());

                    for (int q = 0; q < indicatorData.size(); q++)
                        idxRun.add(q);

                    sort(indicatorData, idxRun);
//                    medianRun[iProblem][jObj][kAlgorithm] = (double)idxRun.get(idxRun.size()/2);
                    int medianRun = idxRun.get(idxRun.size() / 2);
                    String instance = problemNameList[iProblem][jObj] + "_" + popsList[iProblem][jObj] + "_" + maxIterationsList[iProblem][jObj];
                    String saveInstanceName = instance.replace("(", "[").replace(")", "]");

                    String pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + medianRun + ".pof";
                    String saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj]);


                    int bestRun = idxRun.get(idxRun.size() - 1);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + bestRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);

                    int worstRun = idxRun.get(0);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + worstRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);


                    String preStr = "pf_";
                    if (problemObj[iProblem][jObj] > 3)
                        preStr = "pc_";
                    pfOrPcLatexScript(latexFile, preStr, algorithms[kAlgorithm], saveInstanceName);

                }
            }
            try {
                printEndLatexCommands(latexFile);
            } catch (IOException e) {
            }
        }


    }


    public void generateMedianPFMatlabScaledDTLZ(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";
        String saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/Figure/ScaledDTLZ/";
        String latexDir = "D://Experiments/ExperimentDataThesis/Figure/ScaledDTLZ/";
        if (isChinese) {
            saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/FigureZH/ScaledDTLZ/";
            latexDir = "D://Experiments/ExperimentDataThesis/FigureZH/ScaledDTLZ/";
        }

        String indicator = "HV";

        String experimentName = "PF4ScaledDTLZ";


        String[][] algorithmNameList = {
                new String[]{
                        "MOEADN_PBI", "NSGAIII", "MOEADDN_PBI"
                },
//                new String[]{
//                        "MOEACD","MOEACD-N"
//                },
                new String[]{
                        "MOEACD",
                        "MOEACD-N-SBX"
                        ,
                        "MOEACD-N-F"
                        ,
                        "MOEACD-N"
                }
        };

        String[] algorithms = {
                "MOEA/D-N", "NSGAIII-N", "MOEA/DD-N",
//                "MOEA/CD-O","MOEA/CD-O-N",
                "MOEA/CD",
                "MOEA/CD-N-SBX"
                ,
                "MOEA/CD-N-F"
                ,
                "MOEA/CD-N"
        };


        String[] dirs = {
                "D://Experiments/ExperimentDataThesis/ScaledDTLZ/compare/",
//                "D://Experiments/ExperimentDataThesis/ScaledDTLZ/MOEACD-old/",
                "D://Experiments/ExperimentDataThesis/ScaledDTLZ/MOEACD/"
        };


        String[][] problemNameList = {
                new String[]{
                        "SDTLZ1(3)", "SDTLZ1(5)", "SDTLZ1(8)", "SDTLZ1(10)", "SDTLZ1(15)"
                },
                new String[]{
                        "SDTLZ2(3)", "SDTLZ2(5)", "SDTLZ2(8)", "SDTLZ2(10)", "SDTLZ2(15)"
                },
                new String[]{
                        "SDTLZ3(3)", "SDTLZ3(5)", "SDTLZ3(8)", "SDTLZ3(10)", "SDTLZ3(15)"
                },
                new String[]{
                        "SDTLZ4(3)", "SDTLZ4(5)", "SDTLZ4(8)", "SDTLZ4(10)", "SDTLZ4(15)"
                },
                new String[]{
                        "Convex_SDTLZ2(3)", "Convex_SDTLZ2(5)", "Convex_SDTLZ2(8)", "Convex_SDTLZ2(10)", "Convex_SDTLZ2(15)"
                }
        };

        boolean[][] isNormlizedList = {
                new boolean[]{
                        false, true, true, true, true
                },
                new boolean[]{
                        false, true, true, true, true
                },
                new boolean[]{
                        false, true, true, true, true
                },
                new boolean[]{
                        false, true, true, true, true
                },
                new boolean[]{
                        false, true, true, true, true
                }
        };

        int[][] popsList = {
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                }

        };
        int[][] maxIterationsList = {
                new int[]{
                        400, 600, 750, 1000, 1500
                },
                new int[]{
                        250, 350, 500, 750, 1000
                },
                new int[]{
                        1000, 1000, 1000, 1500, 2000
                },
                new int[]{
                        600, 1000, 1250, 2000, 3000
                },
                new int[]{
                        250, 750, 2000, 4000, 4500
                }
        };


        String[] probelms = {
                "SDTLZ1",
                "SDTLZ2",
                "SDTLZ3",
                "SDTLZ4",
                "Convex_SDTLZ2"
        };



        int[][] problemObj = {
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                }
        };

        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "final_");

        String[] pfDir = new String[algorithms.length];
        int c = 0;
        for (int i = 0; i < algorithmNameList.length; i++) {
            for (int j = 0; j < algorithmNameList[i].length; j++) {
                pfDir[c] = dirs[i] + "POF/" + algorithmNameList[i][j];
                c++;
            }
        }
//        int maxRun = 20;
//
//        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, problemObj, hvRefPointList, dirs,"");

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

//        medianRun = new double[sizeProblem][][];
        File matlabOutput;
        matlabOutput = new File(matlabDir);
        if (!matlabOutput.exists()) {
            boolean result = new File(matlabDir).mkdirs();
            JMetalLogger.logger.info("Creating " + matlabDir + " directory");
        }
        //System.out.println("Experiment name: " + experimentName_);
        String matlabFile = matlabDir + "/" + experimentName + ".m";

        try {
            FileWriter os = new FileWriter(matlabFile, false);
            os.close();
        } catch (IOException e) {
        }


        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

//            medianRun[iProblem] = new double[sizeProblemObj][];
            String latexFile = latexDir + "/" + experimentName + "_" + probelms[iProblem] + ".tex";
            try {
                printHeaderLatexCommands(latexFile, experimentName);

            } catch (IOException e) {
            }
            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

//                medianRun[iProblem][jObj] = new double[sizeAlgorithm];


                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    List<Integer> idxRun = new ArrayList<>(indicatorData.size());

                    for (int q = 0; q < indicatorData.size(); q++)
                        idxRun.add(q);

                    sort(indicatorData, idxRun);
//                    medianRun[iProblem][jObj][kAlgorithm] = (double)idxRun.get(idxRun.size()/2);
                    int medianRun = idxRun.get(idxRun.size() / 2);
                    String instance = "";
                    if(isNormlizedList[iProblem][jObj])
                        instance += "N_";
                    instance += problemNameList[iProblem][jObj] + "_" + popsList[iProblem][jObj] + "_" + maxIterationsList[iProblem][jObj];
                    String saveInstanceName = instance.replace("(", "[").replace(")", "]");

                    String pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + medianRun + ".pof";
                    String saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);


                    int bestRun = idxRun.get(idxRun.size() - 1);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + bestRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);

                    int worstRun = idxRun.get(0);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + worstRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);


                    String preStr = "pf_";
                    if (problemObj[iProblem][jObj] > 3)
                        preStr = "pc_";
                    pfOrPcLatexScript(latexFile, preStr, algorithms[kAlgorithm], saveInstanceName);
                }
            }
            try {
                printEndLatexCommands(latexFile);
            } catch (IOException e) {
            }

        }


    }

    public void generateMedianPFMatlabConstrainedDTLZ(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";
        String saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/Figure/ConstrainedDTLZ/";
        String latexDir = "D://Experiments/ExperimentDataThesis/Figure/ConstrainedDTLZ/";
        if (isChinese) {
            saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/FigureZH/ConstrainedDTLZ/";
            latexDir = "D://Experiments/ExperimentDataThesis/FigureZH/ConstrainedDTLZ/";
        }

        String indicator = "HV";

        String experimentName = "PF4ConstrainedDTLZ";

        String[][] algorithmNameList = {
                new String[]{
                        "CMOEAD_PBI", "CMOEADD_PBI"
                },
                new String[]{
                        "C-MOEACD"
//                        , "C-MOEACD-A"
                }
                ,
                new String[]{
                         "C-MOEACD-A"
                        ,
                        "C-MOEACD-AO"
                        ,
                        "C-MOEACD-AOII"
                }
        };

        String[] algorithms = {
                "C-MOEA/D", "C-MOEA/DD",
                "C-MOEA/CD",

                "C-MOEA/CD-A"
                ,
                "C-MOEA/CD-AO"
                ,
                "C-MOEA/CD-AOII"
        };


        String[] dirs = {
                "D://Experiments/ExperimentDataThesis/ConstrainedDTLZ/compare/",
//                "D://Experiments/ExperimentDataThesis/ConstrainedDTLZ/MOEACD/"
//                ,
                "E://ResultsConstraintsMOEACD60/"
                ,
                "E://ResultsConstraintsMOEACD61/"
        };


//        String[][] problemNameList = {
//                new String[]{
//                        "C1_DTLZ1(3)", "C1_DTLZ1(5)", "C1_DTLZ1(8)", "C1_DTLZ1(10)", "C1_DTLZ1(15)"
//                },
//                new String[]{
//                        "C1_DTLZ3(3)", "C1_DTLZ3(5)", "C1_DTLZ3(8)", "C1_DTLZ3(10)", "C1_DTLZ3(15)"
//                },
//                new String[]{
//                        "C2_DTLZ2(3)", "C2_DTLZ2(5)", "C2_DTLZ2(8)", "C2_DTLZ2(10)", "C2_DTLZ2(15)"
//                },
//                new String[]{
//                        "ConvexC2_DTLZ2(3)", "ConvexC2_DTLZ2(5)", "ConvexC2_DTLZ2(8)", "ConvexC2_DTLZ2(10)", "ConvexC2_DTLZ2(15)"
//                },
//                new String[]{
//                        "C3_DTLZ1(3)", "C3_DTLZ1(5)", "C3_DTLZ1(8)", "C3_DTLZ1(10)", "C3_DTLZ1(15)"
//                },
//                new String[]{
//                        "C3_DTLZ4(3)", "C3_DTLZ4(5)", "C3_DTLZ4(8)", "C3_DTLZ4(10)", "C3_DTLZ4(15)"
//                }
//        };
//
//
//        int[][] problemObj = {
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                },
//                new int[]{
//                        3, 5, 8, 10, 15
//                }
//        };
//        int[][] popsList = {
//                new int[]{
//                        91, 210, 156, 275, 135
//                },
//                new int[]{
//                        91, 210, 156, 275, 135
//                },
//                new int[]{
//                        91, 210, 156, 275, 135
//                },
//                new int[]{
//                        91, 210, 156, 275, 135
//                },
//                new int[]{
//                        91, 210, 156, 275, 135
//                },
//                new int[]{
//                        91, 210, 156, 275, 135
//                }
//        };
//        int[][] maxIterationsList = {
//                new int[]{
//                        500, 600, 800, 1000, 1500
//                },
//                new int[]{
//                        1000, 1500, 2500, 3500, 5000
//                },
//                new int[]{
//                        250, 350, 500, 750, 1000
//                },
//                new int[]{
//                        250, 750, 1500, 2500, 3500
//                },
//                new int[]{
//                        750, 1250, 2000, 3000, 4000
//                },
//                new int[]{
//                        750, 1250, 2000, 3000, 4000
//                }
//        };
        String[][] problemNameList = {
                new String[]{
                        "C1_DTLZ1(3)", "C1_DTLZ1(5)"
                },
                new String[]{
                        "C1_DTLZ3(3)", "C1_DTLZ3(5)"
                },
                new String[]{
                        "C2_DTLZ2(3)", "C2_DTLZ2(5)"
                },
                new String[]{
                        "ConvexC2_DTLZ2(3)", "ConvexC2_DTLZ2(5)"
                },
                new String[]{
                        "C3_DTLZ1(3)", "C3_DTLZ1(5)"
                },
                new String[]{
                        "C3_DTLZ4(3)", "C3_DTLZ4(5)"
                }
        };


        int[][] problemObj = {
                new int[]{
                        3, 5
                },
                new int[]{
                        3, 5
                },
                new int[]{
                        3, 5
                },
                new int[]{
                        3, 5
                },
                new int[]{
                        3, 5
                },
                new int[]{
                        3, 5
                }
        };
        int[][] popsList = {
                new int[]{
                        91, 210
                },
                new int[]{
                        91, 210
                },
                new int[]{
                        91, 210
                },
                new int[]{
                        91, 210
                },
                new int[]{
                        91, 210
                },
                new int[]{
                        91, 210
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        500, 600
                },
                new int[]{
                        1000, 1500
                },
                new int[]{
                        250, 350
                },
                new int[]{
                        250, 750
                },
                new int[]{
                        750, 1250
                },
                new int[]{
                        750, 1250
                }
        };



        String[] problemName = {
                "C1_DTLZ1",
                "C1_DTLZ3",
                "C2_DTLZ2",
                "ConvexC2_DTLZ2",
                "C3_DTLZ1",
                "C3_DTLZ4"
        };

        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "final_");

        String[] pfDir = new String[algorithms.length];
        int c = 0;
        for (int i = 0; i < algorithmNameList.length; i++) {
            for (int j = 0; j < algorithmNameList[i].length; j++) {
                pfDir[c] = dirs[i] + "POF/" + algorithmNameList[i][j];
                c++;
            }
        }

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

//        medianRun = new double[sizeProblem][][];
        File matlabOutput;
        matlabOutput = new File(matlabDir);
        if (!matlabOutput.exists()) {
            boolean result = new File(matlabDir).mkdirs();
            JMetalLogger.logger.info("Creating " + matlabDir + " directory");
        }
        //System.out.println("Experiment name: " + experimentName_);
        String matlabFile = matlabDir + "/" + experimentName + ".m";

        try {
            FileWriter os = new FileWriter(matlabFile, false);
            os.close();
        } catch (IOException e) {
        }


        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

//            medianRun[iProblem] = new double[sizeProblemObj][];
            String latexFile = latexDir + "/" + experimentName + "_" + problemName[iProblem] + ".tex";
            try {
                printHeaderLatexCommands(latexFile, experimentName);
            } catch (IOException e) {
            }

            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

//                medianRun[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    List<Integer> idxRun = new ArrayList<>(indicatorData.size());

                    for (int q = 0; q < indicatorData.size(); q++)
                        idxRun.add(q);

                    sort(indicatorData, idxRun);
//                    medianRun[iProblem][jObj][kAlgorithm] = (double)idxRun.get(idxRun.size()/2);
                    int medianRun = idxRun.get(idxRun.size() / 2);
                    String instance = problemNameList[iProblem][jObj] + "_" + popsList[iProblem][jObj] + "_" + maxIterationsList[iProblem][jObj];
                    String saveInstanceName = instance.replace("(", "[").replace(")", "]");

                    String pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + medianRun + ".pof";
                    String saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, problemName[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, problemName[iProblem], problemObj[iProblem][jObj]);


                    int bestRun = idxRun.get(idxRun.size() - 1);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + bestRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, problemName[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, problemName[iProblem], problemObj[iProblem][jObj]);

                    int worstRun = idxRun.get(0);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + worstRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, problemName[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, problemName[iProblem], problemObj[iProblem][jObj]);

                    String preStr = "pf_";
                    if (problemObj[iProblem][jObj] > 3)
                        preStr = "pc_";
                    pfOrPcLatexScript(latexFile, preStr, algorithms[kAlgorithm], saveInstanceName);
                }
            }
            try {
                printEndLatexCommands(latexFile);
            } catch (IOException e) {
            }
        }

    }

    public void generateMedianPFMatlabConstrainedEngineer(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";
        String saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/Figure/Engineer/Constraints/";
        String latexDir = "D://Experiments/ExperimentDataThesis/Figure/Engineer/Constraints";
        if (isChinese) {
            saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/FigureZH/Engineer/Constraints/";
            latexDir = "D://Experiments/ExperimentDataThesis/FigureZH/Engineer/Constraints/";
        }

        String indicator = "HV";

        String experimentName = "PF4ConstrainedEngineer";

        String[] dirs = {
                "D://Experiments/ExperimentDataThesis/Engineer/Constraints/compare/",
                "D://Experiments/ExperimentDataThesis/Engineer/Constraints/MOEACD/"
//                ,
//                "E://ResultsEngineerConstraintsMOEACD4/",
//                "E://ResultsEngineerConstraintsMOEACD3/",
//                "E://ResultsEngineerConstraintsMOEACD0/"
        };
        String[][] problemNameList = {
                new String[]{
                        "CarSideImpact(3)"
                },
//                new String[]{
//                        "NCarSideImpact(3)"
//                },
                new String[]{
                        "Machining(4)"
                },
                new String[]{
                        "Water(5)"
                }
//                ,
//                new String[]{
//                        "NWater(5)"
//                }
        };

        boolean[][] isNormalizedList = {
                new boolean[]{
                        false
                },
                new boolean[]{
                        true
                },
                new boolean[]{
                        true
                }
        };


        int[][] popsList = {
                new int[]{
                        91
                },
//                new int[]{
//                        91
//                },
                new int[]{
                        165
                },
//                new int[]{
//                        210
//                },
                new int[]{
                        210
                }
        };

        int[][] maxIterationsList = {
                new int[]{
                        500
                },
//                new int[]{
//                        500
//                }
//                ,
                new int[]{
                        750
                },
//                new int[]{
//                        1000
//                },
                new int[]{
                        1000
                }
        };


        String[][] algorithmNameList = {
                new String[]{
//                        "CMOEAD_PBI",


                        "CMOEADN_PBI",
                        "CMOEADDN_PBI"
                },
                new String[]{
//                        "C-MOEACD",


                        "C-MOEACD-N",
                        "C-MOEACD-NA"
                        ,
                        "C-MOEACD-ND",
                        "C-MOEACD-NAD"
                }
//                ,
//                new String[]{
//                        "C-MOEACD-N",
//                        "C-MOEACD-NA"
////                        ,
////                        "C-MOEACD-ND",
////                        "C-MOEACD-NAD"
//                },
//                new String[]{
//                        "C-MOEACD-N",
//                        "C-MOEACD-NA"
//                        ,
//                        "C-MOEACD-ND",
//                        "C-MOEACD-NAD"
//                }

        };


        String[] probelms = {
                "CarSideImpact"
//                ,"NCarSideImpact"
                , "Machining"
                , "Water"
//                , "NWater"
        };


        int[][] problemObj = {
                new int[]{
                        3
                },
//                new int[]{
//                        3
//                },
                new int[]{
                        4
                },
//                new int[]{
//                        5
//                },
                new int[]{
                        5
                }
        };

        String[] algorithms = {
//                "C-MOEA/D",


                "C-MOEA/D-N",
                "C-MOEA/DD-N",


//                "C-MOEA/CD",


                "C-MOEA/CD-N",
                "C-MOEA/CD-NA"
                ,
                "C-MOEA/CD-ND",
                "C-MOEA/CD-NAD"
//                ,
//                "C-MOEA/CD-N2",
//                "C-MOEA/CD-NA2"
////                ,
////                "C-MOEA/CD-ND2",
////                "C-MOEA/CD-NAD2"
//                ,
//                "C-MOEA/CD-N3",
//                "C-MOEA/CD-NA3"
//                ,
//                "C-MOEA/CD-ND3",
//                "C-MOEA/CD-NAD3"
        };

        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "final_");

        String[] pfDir = new String[algorithms.length];
        int c = 0;
        for (int i = 0; i < algorithmNameList.length; i++) {
            for (int j = 0; j < algorithmNameList[i].length; j++) {
                pfDir[c] = dirs[i] + "POF/" + algorithmNameList[i][j];
                c++;
            }
        }

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

//        medianRun = new double[sizeProblem][][];
        File matlabOutput;
        matlabOutput = new File(matlabDir);
        if (!matlabOutput.exists()) {
            boolean result = new File(matlabDir).mkdirs();
            JMetalLogger.logger.info("Creating " + matlabDir + " directory");
        }
        //System.out.println("Experiment name: " + experimentName_);
        String matlabFile = matlabDir + "/" + experimentName + ".m";
        try {
            FileWriter os = new FileWriter(matlabFile, false);
            os.close();
        } catch (IOException e) {
        }

        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

//            medianRun[iProblem] = new double[sizeProblemObj][];

            String latexFile = latexDir + "/" + experimentName + "_" + probelms[iProblem] + ".tex";
            try {
                printHeaderLatexCommands(latexFile, experimentName);
            } catch (IOException e) {
            }

            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

//                medianRun[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    List<Integer> idxRun = new ArrayList<>(indicatorData.size());

                    for (int q = 0; q < indicatorData.size(); q++)
                        idxRun.add(q);

                    sort(indicatorData, idxRun);
//                    medianRun[iProblem][jObj][kAlgorithm] = (double)idxRun.get(idxRun.size()/2);
                    int medianRun = idxRun.get(idxRun.size() / 2);
                    String instance = "";
                    if(isNormalizedList[iProblem][jObj])
                        instance += "N_";
                    instance += problemNameList[iProblem][jObj] + "_" + popsList[iProblem][jObj] + "_" + maxIterationsList[iProblem][jObj];
                    String saveInstanceName = instance.replace("(", "[").replace(")", "]");

                    String pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + medianRun + ".pof";
                    String saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);


                    int bestRun = idxRun.get(idxRun.size() - 1);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + bestRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);

                    int worstRun = idxRun.get(0);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + worstRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);

                    String preStr = "pf_";
                    if (problemObj[iProblem][jObj] > 3)
                        preStr = "pc_";
                    pfOrPcLatexScript(latexFile, preStr, algorithms[kAlgorithm], saveInstanceName);
                }
            }
            try {
                printEndLatexCommands(latexFile);
            } catch (IOException e) {
            }
        }

    }


    public void generateMedianPFMatlabUnConstrainedEngineer(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";
        String saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/Figure/Engineer/UnConstraints/";
        String latexDir = "D://Experiments/ExperimentDataThesis/Figure/Engineer/UnConstraints/";
        if (isChinese) {
            saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/FigureZH/Engineer/UnConstraints/";
            latexDir = "D://Experiments/ExperimentDataThesis/FigureZH/Engineer/UnConstraints/";
        }


        String indicator = "HV";

        String experimentName = "PF4Engineer";

        String[] dirs = {
//                "D://Experiments/ExperimentDataThesis/Engineer/UnConstraints/compare/",
//                "D://Experiments/ExperimentDataThesis/Engineer/UnConstraints/MOEACD-old/",
//                "D://Experiments/ExperimentDataThesis/Engineer/UnConstraints/MOEACD/"
//                "E://ResultsEngineerUnConstraintsCompare7/",
//                "E://ResultsEngineerUnConstraintsMOEACD7/"
//                "E://ResultsEngineerUnConstraintsMOEACD12/"
//                ,
//                "E://ResultsEngineerUnConstraintsMOEACD38/"
                "E://ResultsEngineerUnConstraintsMOEACDWithoutEvolving/"

        };
        String[][] problemNameList = {
                new String[]{
                        "CrashWorthiness(3)"
                }
                ,
                new String[]{
                        "UCarSideImpact(9)"
                },
                new String[]{
                        "CarCabDesign(9)"
                }
        };

        boolean[][] isNormlizedList = {
                new boolean[]{
                        false
                },
                new boolean[]{
                        true
                },
                new boolean[]{
                        true
                }
        };

        int[][] popsList = {
                new int[]{
                        153
                }
                ,
                new int[]{
                        210
                },
                new int[]{
                        210
                }
        };

        int[][] maxIterationsList = {
                new int[]{
                        200
                }
                ,
                new int[]{
                        2000
                } ,
                new int[]{
                        2000
                }
        };


        String[][] algorithmNameList = {
//                new String[]{
////                        "MOEAD_PBI",
//                        "MOEADN_PBI",
////                        "MOEADD_PBI",
//                        "MOEADDN_PBI"
//
//                },
//                new String[]{
//                        "MOEACD-N"
//                },
//                new String[]{
//                        "MOEACD",
//                        "MOEACD-N",
////                        "MOEACD-D",
//                        "MOEACD-ND"
//                },
                new String[]{
//                        "MOEACD",
//                        "MOEACD-N"
//                        ,
                        "MOEACD-N-SBX"
//                        ,
//                        "MOEACD-N-F"
//                        ,
//                        "MOEACD-ND"
                }
//                ,
//                new String[]{
//                        "MOEACD-ND"
//                }
        };


        String[] probelms = {
                "CrashWorthiness"
                ,
                "UCarSideImpact",
                "CarCabDesign"
        };


        int[][] problemObj = {
                new int[]{
                        3
                }
                ,
                new int[]{
                        9
                },
                new int[]{
                        9
                }
        };

        String[] algorithms = {
//                "MOEA/D",
//

//                "MOEA/D-N",
////                "MOEA/DD",
//                "MOEA/DD-N",
//                "MOEA/CD-N",


//                "MOEA/CD",
//
//
//                "MOEA/CD-N",
////                "MOEA/CD-D",
//                "MOEA/CD-ND"
//                ,
//                "MOEA/CD",
//                "MOEA/CD-N"
//                ,
                "MOEA/CD-N-SBX"
//                ,
//                "MOEA/CD-N-F"
//                ,"MOEA/CD-ND"

//                ,
//                "MOEA/CD-ND"
        };


        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "final_");

        String[] pfDir = new String[algorithms.length];
        int c = 0;
        for (int i = 0; i < algorithmNameList.length; i++) {
            for (int j = 0; j < algorithmNameList[i].length; j++) {
                pfDir[c] = dirs[i] + "POF/" + algorithmNameList[i][j];
                c++;
            }
        }

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

//        medianRun = new double[sizeProblem][][];
        File matlabOutput;
        matlabOutput = new File(matlabDir);
        if (!matlabOutput.exists()) {
            boolean result = new File(matlabDir).mkdirs();
            JMetalLogger.logger.info("Creating " + matlabDir + " directory");
        }
        //System.out.println("Experiment name: " + experimentName_);
        String matlabFile = matlabDir + "/" + experimentName + ".m";
        try {
            FileWriter os = new FileWriter(matlabFile, false);
            os.close();
        } catch (IOException e) {
        }

        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

//            medianRun[iProblem] = new double[sizeProblemObj][];

            String latexFile = latexDir + "/" + experimentName + "_" + probelms[iProblem] + ".tex";
            try {
                printHeaderLatexCommands(latexFile, experimentName);
            } catch (IOException e) {
            }

            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

//                medianRun[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    List<Integer> idxRun = new ArrayList<>(indicatorData.size());

                    for (int q = 0; q < indicatorData.size(); q++)
                        idxRun.add(q);

                    sort(indicatorData, idxRun);
//                    medianRun[iProblem][jObj][kAlgorithm] = (double)idxRun.get(idxRun.size()/2);
                    int medianRun = idxRun.get(idxRun.size() / 2);
                    String instance = "";
                    if(isNormlizedList[iProblem][jObj])
                        instance += "N_";
                    instance += problemNameList[iProblem][jObj] + "_" + popsList[iProblem][jObj] + "_" + maxIterationsList[iProblem][jObj];
                    String saveInstanceName = instance.replace("(", "[").replace(")", "]");

                    String pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + medianRun + ".pof";
                    String saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);


                    int bestRun = idxRun.get(idxRun.size() - 1);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + bestRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);

                    int worstRun = idxRun.get(0);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + worstRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormlizedList[iProblem][jObj]);

                    String preStr = "pf_";
                    if (problemObj[iProblem][jObj] > 3)
                        preStr = "pc_";
                    pfOrPcLatexScript(latexFile, preStr, algorithms[kAlgorithm], saveInstanceName);
                }
            }
            try {
                printEndLatexCommands(latexFile);
            } catch (IOException e) {
            }
        }
    }

    public void pfOrPcMatlabScript(String matlabFile, String pfFile, String saveFile, String problemName, int obj) {
        pfOrPcMatlabScript(matlabFile,pfFile,saveFile,problemName, obj,false);
    }

    public void pfOrPcMatlabScript(String matlabFile, String pfFile, String saveFile, String problemName, int obj, boolean isNormalized) {
        try {
            FileWriter os = new FileWriter(matlabFile, true);

            os.write("%figure;" + "\n");
            os.write("pfFile = '" + pfFile + "';" + "\n");
            os.write("saveFile = '" + saveFile + "'" + "\n");
            if (obj == 2) {
                os.write("[f1,f2] = textread(pfFile,'%f %f');" + "\n");
                os.write("plot(f1,f2,'mo','markersize',6);" + "\n");
                os.write(" set(gca, 'Fontname', 'Arial', 'Fontsize', 28,'FontWeight','bold');" + "\n");
                os.write(" set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',1.5);" + "\n");
                os.write("title('"+problemName+"["+obj+"]')" + "\n");
                os.write(" xlabel('f1')" + "\n");
                os.write("ylabel('f2')" + "\n");
            } else if (obj == 3) {
                os.write("[f1,f2,f3] = textread(pfFile,'%f %f %f');" + "\n");
                os.write("plot3(f1,f2,f3,'mo','markersize',6);" + "\n");
                os.write("set(gca, 'Fontname', 'Arial', 'Fontsize', 28,'FontWeight','bold'); " + "\n");
                os.write("set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',1.5);" + "\n");
                os.write("title('"+problemName+"["+obj+"]')" + "\n");
                os.write("xlabel('f1')" + "\n");
                os.write("ylabel('f2')" + "\n");
                os.write("zlabel('f3')" + "\n");
                os.write("view(-45,-20)" + "\n");
                os.write("grid off;" + "\n");
                os.write("box on;" + "\n");
                os.write("axis square;" + "\n");
            } else {
                os.write("[F] = load(pfFile);" + "\n");
                os.write("[m,n] = size(F);" + "\n");
                os.write("for k=1:m" + "\n");
                os.write("   V = F(k,:);" + "\n");
                os.write("X = 1:1:n;" + "\n");
                os.write("plot(X,V);hold on;" + "\n");
                os.write("end" + "\n");
                os.write("set(gca, 'Fontname', 'Arial', 'Fontsize', 28,'FontWeight','bold');" + "\n");
                os.write("set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',1.5);" + "\n");
                os.write("title('"+problemName+"["+obj+"]')" + "\n");
                os.write("xlim([1 n]);" + "\n");
                os.write("xlabel('Objective No.')" + "\n");
                if(isNormalized)
                    os.write("ylabel('Normalized Objective Value')" + "\n");
                else
                    os.write("ylabel('Objective Value')" + "\n");
            }
            os.write("print('-depsc','-painters',saveFile);" + "\n");
            os.write("clf;" + "\n");
            os.write("\n\n\n");

            os.close();
        } catch (IOException e) {
        }
    }

    public void pfOrPcMatlabScriptZH(String matlabFile, String pfFile, String saveFile,String problemName, int obj) {
        pfOrPcMatlabScriptZH(matlabFile,pfFile,saveFile, problemName ,obj);
    }

    public void pfOrPcMatlabScriptZH(String matlabFile, String pfFile, String saveFile,String problemName, int obj, boolean isNormalized) {
        try {
            FileWriter os = new FileWriter(matlabFile, true);

            os.write("%figure;" + "\n");
            os.write("pfFile = '" + pfFile + "';" + "\n");
            os.write("saveFile = '" + saveFile + "'" + "\n");
            if (obj == 2) {
                os.write("[f1,f2] = textread(pfFile,'%f %f');" + "\n");
                os.write("plot(f1,f2,'mo','markersize',6);" + "\n");
                os.write(" set(gca, 'Fontname', 'Arial', 'Fontsize', 28,'FontWeight','bold');" + "\n");
                os.write(" set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',1.5);" + "\n");
                os.write("title('"+problemName+"["+obj+"]')" + "\n");
                os.write(" xlabel('f1')" + "\n");
                os.write("ylabel('f2')" + "\n");
            } else if (obj == 3) {
                os.write("[f1,f2,f3] = textread(pfFile,'%f %f %f');" + "\n");
                os.write("plot3(f1,f2,f3,'mo','markersize',6);" + "\n");
                os.write("set(gca, 'Fontname', 'Arial', 'Fontsize', 28,'FontWeight','bold'); " + "\n");
                os.write("set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',1.5);" + "\n");
                os.write("title('"+problemName+"["+obj+"]')" + "\n");
                os.write("xlabel('f1')" + "\n");
                os.write("ylabel('f2')" + "\n");
                os.write("zlabel('f3')" + "\n");
                os.write("view(-45,-20)" + "\n");
                os.write("grid off;" + "\n");
                os.write("box on;" + "\n");
                os.write("axis square;" + "\n");
            } else {
                os.write("[F] = load(pfFile);" + "\n");
                os.write("[m,n] = size(F);" + "\n");
                os.write("for k=1:m" + "\n");
                os.write("   V = F(k,:);" + "\n");
                os.write("X = 1:1:n;" + "\n");
                os.write("plot(X,V);hold on;" + "\n");
                os.write("end" + "\n");
                os.write("set(gca, 'Fontname', '宋体', 'Fontsize', 28,'FontWeight','bold');" + "\n");
                os.write("set(findobj(get(gca,'Children'),'LineWidth',0.5),'LineWidth',1.5);" + "\n");
                os.write("title('"+problemName+"["+obj+"]')" + "\n");
                os.write("xlim([1 n]);" + "\n");
                os.write("xlabel('目标序号')" + "\n");
                if(isNormalized)
                    os.write("ylabel('标准化目标值')" + "\n");
                else
                    os.write("ylabel('目标值')" + "\n");
            }
            os.write("print('-depsc','-painters',saveFile);" + "\n");
            os.write("clf;" + "\n");
            os.write("\n\n\n");

            os.close();
        } catch (IOException e) {
        }
    }

    public void pfOrPcLatexScript(String latexFile, String preStr, String algorithmName, String instanceName) {
        try {
            FileWriter os = new FileWriter(latexFile, true);
            os.write("\\begin{figure}" + "\n");
            os.write("\\centering" + "\n");
            os.write("\\subfloat[]{" + "\n");
            os.write("\\includegraphics[width = 0.30\\linewidth]{./" + preStr + algorithmName.replace("/", "") + "_" + instanceName + "_B.eps" + "}" + "\n");
            os.write("}" + "\n");
            os.write("\\subfloat[]{" + "\n");
            os.write("\\includegraphics[width = 0.30\\linewidth]{./" + preStr + algorithmName.replace("/", "") + "_" + instanceName + ".eps" + "}" + "\n");
            os.write("}" + "\n");
            os.write("\\subfloat[]{" + "\n");
            os.write("\\includegraphics[width = 0.30\\linewidth]{./" + preStr + algorithmName.replace("/", "") + "_" + instanceName + "_W.eps" + "}" + "\n");
            os.write("}" + "\n");
            os.write("\\caption{The Pareto Front of Parallel Coordiates Plots for " + algorithmName.replace("_", "\\_") + " on " + instanceName.replace("_", "\\_") + " .(a)Best,(b)Median,(c)Worst}" + "\n");
            os.write("\\end{figure}" + "\n");
            os.close();
        } catch (IOException e) {
        }
    }


    public void generateLatexIrregular() {

        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
                "HV"
        };

        boolean[] isLowerTheBetter = {
                false
        };

        String[] baseDirs = {
                "D://Experiments/ExperimentDataThesis/Irregular/compare/",
                "D://Experiments/ExperimentDataThesis/Irregular/MOEACD/"
        };

        String[][] dirs = {
                new String[]{
                        baseDirs[0]
                        ,
                        baseDirs[1]
                }
        };

        String[] experimentName = {
                "Irregular(HV)"
        };

        String[][] algorithmNameList = {
                new String[]{
                        "MOEAD_PBI",
                        "MOEADD_PBI"
                }
                ,
                new String[]{
                        "MOEACD",
                        "MOEACD-D"
                }
        };

        String[] algorithms = {
                "MOEA/D",
                "MOEA/DD", "MOEA/CD", "MOEA/CD-D"
        };

        String[][] problemNameList = {
                new String[]{
                        "DTLZ5(3)", "DTLZ5(5)", "DTLZ5(8)", "DTLZ5(10)", "DTLZ5(15)"
                },
                new String[]{
                        "DTLZ6(3)", "DTLZ6(5)", "DTLZ6(8)", "DTLZ6(10)", "DTLZ6(15)"
                },
                new String[]{
                        "DTLZ7(3)", "DTLZ7(5)", "DTLZ7(8)", "DTLZ7(10)", "DTLZ7(15)"
                },
                new String[]{
                        "InvertedDTLZ1(3)", "InvertedDTLZ1(5)", "InvertedDTLZ1(8)", "InvertedDTLZ1(10)", "InvertedDTLZ1(15)"
                }
        };
        int[][] popsList = {
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        500, 750, 1000, 1500, 2000
                },
                new int[]{
                        500, 750, 1000, 1500, 2000
                },
                new int[]{
                        500, 750, 1000, 1500, 2000
                },
                new int[]{
                        500, 750, 1000, 1500, 2000
                }
        };


        String[] probelms = {
                "DTLZ5",
                "DTLZ6",
                "DTLZ7",
                "InvertedDTLZ1"
        };
        int[][] problemObj = {
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                }
        };


        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }


    public void generateMedianPFMatlabIrregular(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";
        String saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/Figure/Irregular/";
        String latexDir = "D://Experiments/ExperimentDataThesis/Figure/Irregular/";
        if (isChinese) {
            saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/FigureZH/Irregular/";
            latexDir = "D://Experiments/ExperimentDataThesis/FigureZH/Irregular/";
        }


        String indicator = "HV";

        String experimentName = "PF4Irregular";


        String[] dirs = {
                "D://Experiments/ExperimentDataThesis/Irregular/compare/",
                "D://Experiments/ExperimentDataThesis/Irregular/MOEACD/"
        };

        String[][] problemNameList = {
                new String[]{
                        "DTLZ5(3)", "DTLZ5(5)", "DTLZ5(8)", "DTLZ5(10)", "DTLZ5(15)"
                },
                new String[]{
                        "DTLZ6(3)", "DTLZ6(5)", "DTLZ6(8)", "DTLZ6(10)", "DTLZ6(15)"
                },
                new String[]{
                        "DTLZ7(3)", "DTLZ7(5)", "DTLZ7(8)", "DTLZ7(10)", "DTLZ7(15)"
                },
                new String[]{
                        "InvertedDTLZ1(3)", "InvertedDTLZ1(5)", "InvertedDTLZ1(8)", "InvertedDTLZ1(10)", "InvertedDTLZ1(15)"
                }
        };

        boolean[][] isNormalizedList = {
                new boolean[]{
                        false, true, true, true, true
                },
                new boolean[]{
                        false, true, true, true, true
                },
                new boolean[]{
                        false, true, true, true, true
                },
                new boolean[]{
                        false, true, true, true, true
                }
        };

        int[][] popsList = {
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        500, 750, 1000, 1500, 2000
                },
                new int[]{
                        500, 750, 1000, 1500, 2000
                },
                new int[]{
                        500, 750, 1000, 1500, 2000
                },
                new int[]{
                        500, 750, 1000, 1500, 2000
                }
        };

        String[][] algorithmNameList = {

                new String[]{
                        "MOEAD_PBI", "MOEADD_PBI"
                }
                , new String[]{
                    "MOEACD",
                    "MOEACD-D"
                }
        };


        String[] probelms = {
                "DTLZ_5",
                "DTLZ_6",
                "DTLZ_7",
                "Inverted\\_DTLZ_1"
        };

        int[][] problemObj = {
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                }
        };

        int[] nObj = {
                3, 5, 8, 10, 15
        };
        String[] algorithms = {
                "MOEA/D", "MOEA/DD", "MOEA/CD", "MOEA/CD-D"
        };


        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "final_");

        String[] pfDir = new String[algorithms.length];
        int c = 0;
        for (int i = 0; i < algorithmNameList.length; i++) {
            for (int j = 0; j < algorithmNameList[i].length; j++) {
                pfDir[c] = dirs[i] + "POF/" + algorithmNameList[i][j];
                c++;
            }
        }
//        int maxRun = 20;
//
//        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, problemObj, hvRefPointList, dirs,"");

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

//        medianRun = new double[sizeProblem][][];
        File matlabOutput;
        matlabOutput = new File(matlabDir);
        if (!matlabOutput.exists()) {
            boolean result = new File(matlabDir).mkdirs();
            JMetalLogger.logger.info("Creating " + matlabDir + " directory");
        }
        //System.out.println("Experiment name: " + experimentName_);
        String matlabFile = matlabDir + "/" + experimentName + ".m";

        try {
            FileWriter os = new FileWriter(matlabFile, false);
            os.close();
        } catch (IOException e) {
        }


        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

//            medianRun[iProblem] = new double[sizeProblemObj][];
            String latexFile = latexDir + "/" + experimentName + "_" + probelms[iProblem].replace("\\", "") + ".tex";
            try {
                printHeaderLatexCommands(latexFile, experimentName);
            } catch (IOException e) {
            }

            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

//                medianRun[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    List<Integer> idxRun = new ArrayList<>(indicatorData.size());

                    for (int q = 0; q < indicatorData.size(); q++)
                        idxRun.add(q);

                    sort(indicatorData, idxRun);
//                    medianRun[iProblem][jObj][kAlgorithm] = (double)idxRun.get(idxRun.size()/2);
                    int medianRun = idxRun.get(idxRun.size() / 2);
                    String instance = "";
                    if(isNormalizedList[iProblem][jObj])
                        instance += "N_";
                    instance += problemNameList[iProblem][jObj] + "_" + popsList[iProblem][jObj] + "_" + maxIterationsList[iProblem][jObj];
                    String saveInstanceName = instance.replace("(", "[").replace(")", "]");

                    String pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + medianRun + ".pof";
                    String saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);


                    int bestRun = idxRun.get(idxRun.size() - 1);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + bestRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);

                    int worstRun = idxRun.get(0);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + worstRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj],isNormalizedList[iProblem][jObj]);


                    String preStr = "pf_";
                    if (problemObj[iProblem][jObj] > 3)
                        preStr = "pc_";
                    pfOrPcLatexScript(latexFile, preStr, algorithms[kAlgorithm], saveInstanceName);

                }
            }
            try {
                printEndLatexCommands(latexFile);
            } catch (IOException e) {
            }
        }

    }

    public void generateLatexIrregularConstraints() {

        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
                "HV"
        };
        boolean[] isLowerTheBetter = {
                false
        };
        String[] experimentName = {
                "IrregularConstraints(HV)"
        };
        String[] baseDirs = {
                "D://Experiments/ExperimentDataThesis/ConstrainedDTLZ/compare/",
                "D://Experiments/ExperimentDataThesis/ConstrainedDTLZ/MOEACD/",
                "D://Experiments/ExperimentDataThesis/Irregular/CMOEACD/"
        };
        String[][] dirs = {
                new String[]{
                        baseDirs[0],
                        baseDirs[1],
                        baseDirs[2]
                }
        };
        String[][] problemNameList = {
                new String[]{
                        "C2_DTLZ2(3)", "C2_DTLZ2(5)", "C2_DTLZ2(8)", "C2_DTLZ2(10)", "C2_DTLZ2(15)"
                }
                ,
                new String[]{
                        "ConvexC2_DTLZ2(3)", "ConvexC2_DTLZ2(5)", "ConvexC2_DTLZ2(8)", "ConvexC2_DTLZ2(10)", "ConvexC2_DTLZ2(15)"
                },
        };

        int[][] popsList = {
                new int[]{
                        91, 210, 156, 275, 135,
                },
                new int[]{
                        91, 210, 156, 275, 135,
                }
        };

        int[][] maxIterationsList = {

                new int[]{
                        250, 350, 500, 750, 1000
                },
                new int[]{
                        250, 750, 1500, 2500, 3500
                }
        };


        String[][] algorithmNameList = {
                new String[]{
//                        "CMOEAD_PBI" ,
                        "CMOEADD_PBI"
                },
                new String[]{
//                        "C-MOEACD",
                        "C-MOEACD-A"
                }
                ,
                new String[]{
//                        "C-MOEACD-D",
                        "C-MOEACD-AD"
                }
        };

        String[] probelms = {
                "C2_DTLZ2",
                "ConvexC2_DTLZ2"
        };

        int[][] problemObj = {

                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                }
        };
        String[] algorithms = {
//                "C-MOEA/D",
                "C-MOEA/DD",
//                "C-MOEA/CD",
                "C-MOEA/CD-A",
//                "C-MOEA/CD-D",
                "C-MOEA/CD-AD"
        };

        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }

    public void generateMedianPFMatlabIrregularConstraints(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";
        String saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/Figure/Irregular/";
        String latexDir = "D://Experiments/ExperimentDataThesis/Figure/Irregular/";
        if (isChinese) {
            saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/FigureZH/Irregular/";
            latexDir = "D://Experiments/ExperimentDataThesis/FigureZH/Irregular/";
        }


        String indicator = "HV";

        String experimentName = "PF4IrregularConstraints";


        String[] dirs = {
                "D://Experiments/ExperimentDataThesis/ConstrainedDTLZ/compare/",
                "D://Experiments/ExperimentDataThesis/ConstrainedDTLZ/MOEACD/",
                "D://Experiments/ExperimentDataThesis/Irregular/CMOEACD/"
        };

        String[][] problemNameList = {
                new String[]{
                        "C2_DTLZ2(3)", "C2_DTLZ2(5)", "C2_DTLZ2(8)", "C2_DTLZ2(10)", "C2_DTLZ2(15)"
                },
                new String[]{
                        "ConvexC2_DTLZ2(3)", "ConvexC2_DTLZ2(5)", "ConvexC2_DTLZ2(8)", "ConvexC2_DTLZ2(10)", "ConvexC2_DTLZ2(15)"
                },
        };

        int[][] popsList = {
                new int[]{
                        91, 210, 156, 275, 135,
                },
                new int[]{
                        91, 210, 156, 275, 135,
                }
        };

        int[][] maxIterationsList = {

                new int[]{
                        250, 350, 500, 750, 1000
                },
                new int[]{
                        250, 750, 1500, 2500, 3500
                }
        };


        String[][] algorithmNameList = {
                new String[]{
                        "CMOEAD_PBI", "CMOEADD_PBI"
                },
                new String[]{
                        "C-MOEACD",
                        "C-MOEACD-A"
                },
                new String[]{
                        "C-MOEACD-D",
                        "C-MOEACD-AD"
                }
        };

        String[] probelms = {
                "C2_DTLZ2",
                "ConvexC2_DTLZ2"
        };

        int[][] problemObj = {

                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                }
        };
        String[] algorithms = {
                "C-MOEA/D", "C-MOEA/DD", "C-MOEA/CD", "C-MOEA/CD-A", "C-MOEA/CD-D", "C-MOEA/CD-AD"
        };


        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "final_");

        String[] pfDir = new String[algorithms.length];
        int c = 0;
        for (int i = 0; i < algorithmNameList.length; i++) {
            for (int j = 0; j < algorithmNameList[i].length; j++) {
                pfDir[c] = dirs[i] + "POF/" + algorithmNameList[i][j];
                c++;
            }
        }
//        int maxRun = 20;
//        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, problemObj, hvRefPointList, dirs,"");

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

//        medianRun = new double[sizeProblem][][];
        File matlabOutput;
        matlabOutput = new File(matlabDir);
        if (!matlabOutput.exists()) {
            boolean result = new File(matlabDir).mkdirs();
            JMetalLogger.logger.info("Creating " + matlabDir + " directory");
        }
        //System.out.println("Experiment name: " + experimentName_);
        String matlabFile = matlabDir + "/" + experimentName + ".m";

        try {
            FileWriter os = new FileWriter(matlabFile, false);
            os.close();
        } catch (IOException e) {
        }


        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

//            medianRun[iProblem] = new double[sizeProblemObj][];
            String latexFile = latexDir + "/" + experimentName + "_" + probelms[iProblem].replace("\\", "") + ".tex";
            try {
                printHeaderLatexCommands(latexFile, experimentName);
            } catch (IOException e) {
            }

            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

//                medianRun[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    List<Integer> idxRun = new ArrayList<>(indicatorData.size());

                    for (int q = 0; q < indicatorData.size(); q++)
                        idxRun.add(q);

                    sort(indicatorData, idxRun);
//                    medianRun[iProblem][jObj][kAlgorithm] = (double)idxRun.get(idxRun.size()/2);
                    int medianRun = idxRun.get(idxRun.size() / 2);
                    String instance = problemNameList[iProblem][jObj] + "_" + popsList[iProblem][jObj] + "_" + maxIterationsList[iProblem][jObj];
                    String saveInstanceName = instance.replace("(", "[").replace(")", "]");

                    String pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + medianRun + ".pof";
                    String saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);


                    int bestRun = idxRun.get(idxRun.size() - 1);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + bestRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj]);

                    int worstRun = idxRun.get(0);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + worstRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);


                    String preStr = "pf_";
                    if (problemObj[iProblem][jObj] > 3)
                        preStr = "pc_";
                    pfOrPcLatexScript(latexFile, preStr, algorithms[kAlgorithm], saveInstanceName);

                }
            }
            try {
                printEndLatexCommands(latexFile);
            } catch (IOException e) {
            }
        }

    }


    public void generateLatexIrregularTest() {

        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
                "HV"
        };

        boolean[] isLowerTheBetter = {
                false
        };

        String[] baseDirs = {
                "D://Experiments/ExperimentDataThesis/Irregular/compare/",
                "D://Experiments/ExperimentDataThesis/Irregular/MOEACD/",
//                "E://ResultsIrregularMOEACDtest1/"
//                ,
//                "E://ResultsIrregularMOEACDtest2/"
//                ,
//                "E://ResultsIrregularMOEACDtest3/"
//                ,
//                "E://ResultsIrregularMOEACDtest4/"
//                ,
//                "E://ResultsIrregularMOEACDtest5/"
                "E://ResultsIrregularMOEACD21/"
        };

        String[][] dirs = {
                new String[]{
                        baseDirs[0]
                        ,
                        baseDirs[1]
                        ,
                        baseDirs[2]
//                        ,
//                        baseDirs[3]
//                        ,
//                        baseDirs[4]
//                        ,
//                        baseDirs[5]
//                        ,
//                        baseDirs[6]
                }
        };

        String[] experimentName = {
                "Irregular(HV)"
        };

        String[][] algorithmNameList = {
                new String[]{
                        "MOEAD_PBI", "MOEADD_PBI"
                }
                ,
                new String[]{
                        "MOEACD",
                        "MOEACD-D"
                }
                ,
                new String[]{
                        "MOEACD",
                        "MOEACD-D"
                }
//                ,
//                new String[]{
//                        "MOEACD-D"
//                }
//                ,
//                new String[]{
//                        "MOEACD-D"
//                },
//                new String[]{
//                        "MOEACD-D"
//                }
//                ,
//                new String[]{
//                        "MOEACD-D"
//                }
        };

        String[] algorithms = {
                "MOEA/D", "MOEA/DD", "MOEA/CD", "MOEA/CD-D"
                ,"MOEA/CD-t1"
                , "MOEA/CD-Dt1"
//                , "MOEA/CD-Dt2"
//                ,"MOEA/CD-Dt3","MOEA/CD-Dt4"
//                ,"MOEA/CD-Dt5"
        };

        String[][] problemNameList = {
                new String[]{
                        "DTLZ5(3)"
                },
                new String[]{
                        "DTLZ6(3)"
                },
                new String[]{
                        "DTLZ7(3)"
                },
                new String[]{
                        "InvertedDTLZ1(3)"
                }
        };
        int[][] popsList = {
                new int[]{
                        91
                },
                new int[]{
                        91
                },
                new int[]{
                        91
                },
                new int[]{
                        91
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        500
                },
                new int[]{
                        500
                },
                new int[]{
                        500
                },
                new int[]{
                        500
                }
        };


        String[] probelms = {
                "DTLZ5",
                "DTLZ6",
                "DTLZ7",
                "InvertedDTLZ1"
        };
        int[][] problemObj = {
                new int[]{
                        3
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                }
        };


        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }


    public void generateMedianPFMatlabIrregularTest(boolean isChinese) {

        String matlabDir = "D://Experiments/ExperimentDataThesis/Matlab/";
        String saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/Figure/Irregular/";
        String latexDir = "D://Experiments/ExperimentDataThesis/Figure/Irregular/";
        if (isChinese) {
            saveMatlabFigureDir = "D://Experiments/ExperimentDataThesis/FigureZH/Irregular/";
            latexDir = "D://Experiments/ExperimentDataThesis/FigureZH/Irregular/";
        }


        String indicator = "HV";

        String experimentName = "PF4Irregular";


        String[] dirs = {
                "D://Experiments/ExperimentDataThesis/Irregular/compare/",
                "D://Experiments/ExperimentDataThesis/Irregular/MOEACD/",
//                "E://ResultsIrregularMOEACDtest1/"
//                ,
//                "E://ResultsIrregularMOEACDtest2/"
//                ,
//                "E://ResultsIrregularMOEACDtest3/"
//                ,
//                "E://ResultsIrregularMOEACDtest4/"
//                ,
//                "E://ResultsIrregularMOEACDtest5/"
                "E://ResultsIrregularMOEACD21/"
        };

        String[][] problemNameList = {
                new String[]{
                        "DTLZ5(3)"
                },
                new String[]{
                        "DTLZ6(3)"
                },
                new String[]{
                        "DTLZ7(3)"
                },
                new String[]{
                        "InvertedDTLZ1(3)"
                }
        };
        int[][] popsList = {
                new int[]{
                        91
                },
                new int[]{
                        91
                },
                new int[]{
                        91
                },
                new int[]{
                        91
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        500
                },
                new int[]{
                        500
                },
                new int[]{
                        500
                },
                new int[]{
                        500
                }
        };

        String[][] algorithmNameList = {

                new String[]{
                        "MOEAD_PBI", "MOEADD_PBI"
                }
                , new String[]{
                    "MOEACD",
                    "MOEACD-D"
                }
                ,
                new String[]{
                        "MOEACD",
                        "MOEACD-D"
                }
//                ,
//                new String[]{
//                        "MOEACD-D"
//                }
//                ,
//                new String[]{
//                        "MOEACD-D"
//                },
//                new String[]{
//                        "MOEACD-D"
//                }
//                ,
//                new String[]{
//                        "MOEACD-D"
//                }
        };


        String[] probelms = {
                "DTLZ_5",
                "DTLZ_6",
                "DTLZ_7",
                "Inverted\\_DTLZ_1"
        };

        int[][] problemObj = {
                new int[]{
                        3
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                }
        };

        int[] nObj = {
                3
        };

        String[] algorithms = {
                "MOEA/D", "MOEA/DD", "MOEA/CD", "MOEA/CD-D"
                ,"MOEA/CD-t1"
                , "MOEA/CD-Dt1"
//                , "MOEA/CD-Dt2"
//                ,"MOEA/CD-Dt3","MOEA/CD-Dt4"
//                ,"MOEA/CD-Dt5"
        };


        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "final_");

        String[] pfDir = new String[algorithms.length];
        int c = 0;
        for (int i = 0; i < algorithmNameList.length; i++) {
            for (int j = 0; j < algorithmNameList[i].length; j++) {
                pfDir[c] = dirs[i] + "POF/" + algorithmNameList[i][j];
                c++;
            }
        }
//        int maxRun = 20;
//
//        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, problemObj, hvRefPointList, dirs,"");

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

//        medianRun = new double[sizeProblem][][];
        File matlabOutput;
        matlabOutput = new File(matlabDir);
        if (!matlabOutput.exists()) {
            boolean result = new File(matlabDir).mkdirs();
            JMetalLogger.logger.info("Creating " + matlabDir + " directory");
        }
        //System.out.println("Experiment name: " + experimentName_);
        String matlabFile = matlabDir + "/" + experimentName + ".m";

        try {
            FileWriter os = new FileWriter(matlabFile, false);
            os.close();
        } catch (IOException e) {
        }


        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

//            medianRun[iProblem] = new double[sizeProblemObj][];
            String latexFile = latexDir + "/" + experimentName + "_" + probelms[iProblem].replace("\\", "") + ".tex";
            try {
                printHeaderLatexCommands(latexFile, experimentName);
            } catch (IOException e) {
            }

            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

//                medianRun[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    List<Integer> idxRun = new ArrayList<>(indicatorData.size());

                    for (int q = 0; q < indicatorData.size(); q++)
                        idxRun.add(q);

                    sort(indicatorData, idxRun);
//                    medianRun[iProblem][jObj][kAlgorithm] = (double)idxRun.get(idxRun.size()/2);
                    int medianRun = idxRun.get(idxRun.size() / 2);
                    String instance = problemNameList[iProblem][jObj] + "_" + popsList[iProblem][jObj] + "_" + maxIterationsList[iProblem][jObj];
                    String saveInstanceName = instance.replace("(", "[").replace(")", "]");

                    String pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + medianRun + ".pof";
                    String saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);


                    int bestRun = idxRun.get(idxRun.size() - 1);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + bestRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj]);

                    int worstRun = idxRun.get(0);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + worstRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);


                    String preStr = "pf_";
                    if (problemObj[iProblem][jObj] > 3)
                        preStr = "pc_";
                    pfOrPcLatexScript(latexFile, preStr, algorithms[kAlgorithm], saveInstanceName);

                }
            }
            try {
                printEndLatexCommands(latexFile);
            } catch (IOException e) {
            }
        }

    }


    public void generateLatexMaOPTest() {


        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
                "HV"
        };

        boolean[] isLowerTheBetter = {
                false
        };

        String[] baseDirs = {
                "D://Experiments/ExperimentDataThesis/DTLZ/MOEACD/",
                "E://ResultsMaOPMOEACDtestP1/",
                "E://ResultsMaOPMOEACDtestP2/",
                "E://ResultsMaOPMOEACDtestP3/"
        };

        String[][] dirs = {
                new String[]{
                        baseDirs[0],
                        baseDirs[1],
                        baseDirs[2]
                        ,
                        baseDirs[3]
                }
        };

        String[] experimentName = {
//                "DTLZ(IGDPlus)",
                "DTLZ(HV)Test"
        };

        String[][] algorithmNameList = {
                new String[]{
                        "MOEACD-SBX", "MOEACD-DE", "MOEACD-F", "MOEACD"
                },
                new String[]{
                        "MOEACD"
                },
                new String[]{
                        "MOEACD"
                },
                new String[]{
                        "MOEACD"
                }
        };

        String[] algorithms = {
                "MOEA/CD-SBX", "MOEA/CD-DE", "MOEA/CD-F", "MOEA/CD"
                , "MOEA/CD-P1", "MOEA/CD-P2", "MOEA/CD-P3"
        };

        String[][] problemNameList = {
                new String[]{
                        "DTLZ1(3)", "DTLZ1(5)", "DTLZ1(8)", "DTLZ1(10)", "DTLZ1(15)"
                },
                new String[]{
                        "DTLZ2(3)", "DTLZ2(5)", "DTLZ2(8)", "DTLZ2(10)", "DTLZ2(15)"
                },
                new String[]{
                        "DTLZ3(3)", "DTLZ3(5)", "DTLZ3(8)", "DTLZ3(10)", "DTLZ3(15)"
                },
                new String[]{
                        "DTLZ4(3)", "DTLZ4(5)", "DTLZ4(8)", "DTLZ4(10)", "DTLZ4(15)"
                },
                new String[]{
                        "Convex_DTLZ2(3)", "Convex_DTLZ2(5)", "Convex_DTLZ2(8)", "Convex_DTLZ2(10)", "Convex_DTLZ2(15)"
                }
        };
        int[][] popsList = {
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                },
                new int[]{
                        91, 210, 156, 275, 135
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        400, 600, 750, 1000, 1500
                },
                new int[]{
                        250, 350, 500, 750, 1000
                },
                new int[]{
                        1000, 1000, 1000, 1500, 2000
                },
                new int[]{
                        600, 1000, 1250, 2000, 3000
                },
                new int[]{
                        250, 750, 2000, 4000, 4500
                }
        };


        String[] probelms = {
                "DTLZ1",
                "DTLZ2",
                "DTLZ3",
                "DTLZ4",
                "Convex_DTLZ2"
        };
        int[][] problemObj = {
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                },
                new int[]{
                        3, 5, 8, 10, 15
                }
        };


        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }


    public void generateLatexMOPTest() {


        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String[] indicator = {
                "HV"
        };
        boolean[] isLowerTheBetter = {
                false
        };
        String[] experimentName = {
//                "MOP(IGDPlus)",
                "MOP(HV)Test"
        };
        String[][] algorithmNameList = {
                new String[]{
                        "MOEACD-SBX", "MOEACD-DE", "MOEACD-F", "MOEACD"
                },
                new String[]{
                        "MOEACD"
                },
                new String[]{
                        "MOEACD"
                },
                new String[]{
                        "MOEACD"
                }

        };
        String[] algorithms = {
                "MOEA/CD-SBX", "MOEA/CD-DE", "MOEA/CD-F", "MOEA/CD"
                , "MOEA/CD-P1", "MOEA/CD-P2", "MOEA/CD-P3"
        };
        String[] baseDirs = {
                "D://Experiments/ExperimentDataThesis/MOP/MOEACD/",
                "E://ResultsMOPMOEACDtestP1/",
                "E://ResultsMOPMOEACDtestP2/",
                "E://ResultsMOPMOEACDtestP3/"
        };

        String[][] dirs = {
                new String[]{
                        baseDirs[0],
                        baseDirs[1]
                        ,
                        baseDirs[2]
                        ,
                        baseDirs[3]
                }
        };

        String[][] problemNameList = {
                new String[]{
                        "MOP1(2)"
                },
                new String[]{
                        "MOP2(2)"
                },
                new String[]{
                        "MOP3(2)"
                },
                new String[]{
                        "MOP4(2)"
                },
                new String[]{
                        "MOP5(2)"
                },
                new String[]{
                        "MOP6(3)"
                },
                new String[]{
                        "MOP7(3)"
                }
        };

        int[][] popsList = {
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        100
                },
                new int[]{
                        300
                },
                new int[]{
                        300
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                },
                new int[]{
                        3000
                }
        };


        String[] probelms = {
                "MOP1",
                "MOP2",
                "MOP3",
                "MOP4",
                "MOP5",
                "MOP6",
                "MOP7"
        };
        int[][] problemObj = {
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        2
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                }
        };

        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }

    public void generateBestInObjectivesLatexScript() {
        String latexDir = "D://Experiments/ExperimentDataThesis/LatexTable/";

        String indicator = "HV";

        String experimentName = "BestInObjective";

        String[] dirs = {
//                "D://Experiments/ExperimentDataThesis/Engineer/UnConstraints/compare/",
//                "D://Experiments/ExperimentDataThesis/Engineer/UnConstraints/MOEACD/"
                "E://ResultsEngineerUnConstraintsCompare7/",
                "E://ResultsEngineerUnConstraintsMOEACD7/"
        };
        String[][] problemNameList = {
//                new String[]{
//                        "CrashWorthiness(3)"
//                }
//                ,
                new String[]{
                        "UCarSideImpact(9)"
                }
        };

        int[][] popsList = {
//                new int[]{
//                        153
//                }
//                ,
                new int[]{
                        210
                }
        };

        int[][] maxIterationsList = {
//                new int[]{
//                        200
//                }
//                ,
                new int[]{
                        2000
                }
        };

        String[][] algorithmNameList = {
                new String[]{
//                        "MOEAD_PBI",
//                        "MOEADN_PBI",
//                        "MOEADD_PBI",
                        "MOEADDN_PBI"

                },
//                new String[]{
//                        "MOEACD-N"
//                },
                new String[]{
//                        "MOEACD",
//                        "MOEACD-N",
//                        "MOEACD-D",
                        "MOEACD-ND"
                }
        };


        String[] probelms = {
//                "CrashWorthiness(3)"
//                ,
                "UCarSideImpact(9)"
        };


        int[][] problemObj = {
//                new int[]{
//                        3
//                }
//                ,
                new int[]{
                        9
                }
        };

        String[] algorithms = {
//                "MOEA/D",
//                "MOEA/D-N",
//                "MOEA/DD",
                "MOEA/DD-N",
//                "MOEA/CD-N",
//                "MOEA/CD",
//                "MOEA/CD-N",
//                "MOEA/CD-D",
                "MOEA/CD-ND"
        };




//        String[] dirs = {
//                "D://Experiments/ExperimentDataThesis/Engineer/Constraints/compare/",
//                "D://Experiments/ExperimentDataThesis/Engineer/Constraints/MOEACD/"
//
//        };
//        String[][] problemNameList = {
//                new String[]{
//                        "CarSideImpact(3)"
//                },
////                new String[]{
////                        "NCarSideImpact(3)"
////                },
//                new String[]{
//                        "Machining(4)"
//                },
//                new String[]{
//                        "Water(5)"
//                }
////                ,
////                new String[]{
////                        "NWater(5)"
////                }
//        };
//
//
//        int[][] popsList = {
//                new int[]{
//                        91
//                },
////                new int[]{
////                        91
////                },
//                new int[]{
//                        165
//                },
////                new int[]{
////                        210
////                },
//                new int[]{
//                        210
//                }
//        };
//
//        int[][] maxIterationsList = {
//                new int[]{
//                        500
//                },
////                new int[]{
////                        500
////                }
////                ,
//                new int[]{
//                        750
//                },
////                new int[]{
////                        1000
////                },
//                new int[]{
//                        1000
//                }
//        };
//
//
//        String[][] algorithmNameList = {
//                new String[]{
////                        "CMOEADN_PBI",
//                        "CMOEADDN_PBI"
//                },
//                new String[]{
////                        "C-MOEACD-N",
////                        "C-MOEACD-NA"
////                        ,
////                        "C-MOEACD-ND",
//                        "C-MOEACD-NAD"
//                }
//
//
//        };
//
//
//        String[] probelms = {
//                "CarSideImpact"
////                ,"NCarSideImpact"
//                , "Machining"
//                , "Water"
////                , "NWater"
//        };
//
//
//        int[][] problemObj = {
//                new int[]{
//                        3
//                },
////                new int[]{
////                        3
////                },
//                new int[]{
//                        4
//                },
////                new int[]{
////                        5
////                },
//                new int[]{
//                        5
//                }
//        };
//
//        String[] algorithms = {
////                "C-MOEA/D-N",
//                "C-MOEA/DD-N",
////                "C-MOEA/CD-N",
////                "C-MOEA/CD-NA"
////                ,
////                "C-MOEA/CD-ND",
//                "C-MOEA/CD-NAD"
////
//        };

        int maxRun = 20;

        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "final_");

        String[] pfDir = new String[algorithms.length];
        int c = 0;
        for (int i = 0; i < algorithmNameList.length; i++) {
            for (int j = 0; j < algorithmNameList[i].length; j++) {
                pfDir[c] = dirs[i] + "POF/" + algorithmNameList[i][j];
                c++;
            }
        }

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

            String latexFile = latexDir + "/" + experimentName + "_" + probelms[iProblem] + ".tex";
            try {
                printHeaderLatexCommands(latexFile, experimentName);

                FileWriter os = new FileWriter(latexFile, true);
                os.write("\n");
                os.write("\\newcommand{\\tabincell}[2]{\\begin{tabular}{@{}#1@{}}#2\\end{tabular}}"+"\n");

                os.write("\\begin{center}" + "\n");
                os.write("\\begin{longtabu} to\\linewidth{X[3,c]");
                // calculate the number of columns
                for (int i = 0; i < algorithms.length; i++) {
                    os.write("|X[5,c]");
                }

                os.write("}" + "\n");
                os.write("\\caption{Best in Objectives " + probelms[iProblem] + "}" + "\n");
                String labelText = "table:bestInObjectives:" + probelms[iProblem];
                os.write("\\label{" + labelText + "}\\\\" + "\n");

                // write table head
                int len = 1 + algorithms.length;
                String tableHeadText = "";
                for (int i = -1; i < algorithms.length; i++) {
                    if (i == -1) {
                        tableHeadText += " \\textbf{Objective} & ";
                    } else if (i == (algorithms.length - 1)) {
                        tableHeadText += " \\textbf{" + algorithms[i].replace("_", "\\_") + "}\\\\" + "\n";
                    } else {
                        tableHeadText += " \\textbf{" + algorithms[i].replace("_", "\\_") + "} & ";
                    }
                }

                os.write("\\toprule" + "\n");
                os.write(tableHeadText);
                os.write("\\endfirsthead" + "\n");

                os.write("\\multicolumn{" + len + "}{c}{\\small Table. \\ref{" + labelText + "} continue ... }\\\\" + "\n");
                os.write("\\toprule" + "\n");
                os.write(tableHeadText);
                os.write("\\hline" + "\n");
                os.write("\\endhead" + "\n");

                os.write("\\bottomrule" + "\n");
                os.write("\\multicolumn{" + len + "}{c}{\\small continue next page ...  }\\\\" + "\n");
                os.write("\\endfoot" + "\n");

                os.write("\\endlastfoot" + "\n");

                for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                    int sizeAlgorithm = data.get(iProblem).get(jObj).size();

                    List<List<Point>> bestInObjectivesComparing = new ArrayList<>();
                    for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                        List<Point> bestInObjectives = new ArrayList<>();
                        List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                        List<Integer> idxRun = new ArrayList<>(indicatorData.size());

                        for (int q = 0; q < indicatorData.size(); q++)
                            idxRun.add(q);

                        sort(indicatorData, idxRun);

                        String instance = problemNameList[iProblem][jObj] + "_" + popsList[iProblem][jObj] + "_" + maxIterationsList[iProblem][jObj];

                        int bestRun = idxRun.get(idxRun.size() - 1);
                        String solutionPF = pfDir[kAlgorithm] + "_" + instance + "R" + bestRun + ".pof";
                        WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);

                        for (int i = 0; i < solutionFront.getPointDimensions(); i++) {
                            double vmin = Double.POSITIVE_INFINITY;
                            int idx = -1;
                            for (int j = 0; j < solutionFront.getNumberOfPoints(); j++) {
                                if (solutionFront.getPoint(j).getDimensionValue(i) < vmin) {
                                    vmin = solutionFront.getPoint(j).getDimensionValue(i);
                                    idx = j;
                                }
                            }

                            Point p = new ArrayPoint(solutionFront.getPointDimensions());
                            for (int j = 0; j < solutionFront.getPointDimensions(); j++) {
                                p.setDimensionValue(j, solutionFront.getPoint(idx).getDimensionValue(j));
                            }
                            bestInObjectives.add(p);
                        }
                        bestInObjectivesComparing.add(bestInObjectives);
                    }



                    for (int k = 0; k < problemObj[iProblem][jObj]; k++) {
                        os.write("\\hline" + "\n");
                        os.write("" + (k + 1) + "\t");
                        int bestIdx = -1;
                        double bestObjective = Double.POSITIVE_INFINITY;
                        for (int alg = 0; alg < algorithms.length; alg++) {
                            if (bestInObjectivesComparing.get(alg).get(k).getDimensionValue(k) < bestObjective) {
                                bestObjective = bestInObjectivesComparing.get(alg).get(k).getDimensionValue(k);
                                bestIdx = alg;
                            }
                        }
                        for (int alg = 0; alg < algorithms.length; alg++) {
                            os.write(" & ");
                            if (alg == bestIdx) {
                                os.write("\\cellcolor{gray95}");
                            }
                            os.write("\\tabincell{c}{(\\ ");

                            for (int p = 0; p < problemObj[iProblem][jObj]; p++) {
                                if (p == k) {
                                    os.write("\\textbf{");
                                }

                                os.write("" + bestInObjectivesComparing.get(alg).get(k).getDimensionValue(p));

                                if (p == k) {
                                    os.write("}");
                                }

                                if (p != problemObj[iProblem][jObj] - 1) {
                                    os.write(" , ");
                                    os.write(" \\\\ ");
                                }
                            }

                            os.write("\\ )} "+"\n");
                        }
                        os.write("\\\\"+"\n");
                    }

                }
                // close table
                os.write("\\bottomrule" + "\n");
//
                os.write("\\end{longtabu}" + "\n");
//        os.write("\\end{scriptsize}" + "\n");
//        os.write("\\end{table}" + "\n");
                os.write("\\end{center}" + "\n");
//        os.write("\\end{supertabular}" + "\n");
//        os.write("\\end{center}"+"\n");
                os.close();
                printEndLatexCommands(latexFile);
            } catch (IOException e) {
            }
        }
}


    public void generateLatexMaOPMethod() {


        String latexDir = "E://ResultsMaOPMethod/LatexTable/";

        String[] indicator = {
                "HV"
        };

        boolean[] isLowerTheBetter = {
                false
        };

        String[] baseDirs = {
                "E://ResultsMaOPMethod/"
        };

        String[][] dirs = {
                new String[]{
                        baseDirs[0]
                }
        };

        String[] experimentName = {
                "DTLZMethod"
        };

        String[][] algorithmNameList = {
                new String[]{
                        "MOEACD-V",
                        "MOEACD-PEH",
                        "MOEACD-ACV",
                        "MOEACD-AECV"
                }
        };

        String[] algorithms = {
                "MOEA/CD-V",
                "MOEA/CD-PEH",
                "MOEA/CD-ACV",
                "MOEA/CD-AECV"
        };


        String[][] problemNameList = {
                new String[]{
                        "DTLZ1(3)"
                },
                new String[]{
                        "DTLZ2(3)"
                },
                new String[]{
                        "DTLZ3(3)"
                },
                new String[]{
                        "DTLZ4(3)"
                },
                new String[]{
                        "Convex_DTLZ2(3)"
                }
        };
        int[][] popsList = {
                new int[]{
                        91
                },
                new int[]{
                        91
                },
                new int[]{
                        91
                },
                new int[]{
                        91
                },
                new int[]{
                        91
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        400
                },
                new int[]{
                        250
                },
                new int[]{
                        1000
                },
                new int[]{
                        600
                },
                new int[]{
                        250
                }
        };


        String[] probelms = {
                "DTLZ1",
                "DTLZ2",
                "DTLZ3",
                "DTLZ4",
                "Convex_DTLZ2"
        };
        int[][] problemObj = {
                new int[]{
                        3
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                }
        };


        int maxRun = 20;
        for (int type = 0; type < indicator.length; type++) {
            List<List<List<List<Double>>>> data = collectData(maxRun, indicator[type], problemNameList, algorithmNameList, popsList, maxIterationsList, dirs[type], "final_");
            latexTable table = new latexTable();
            table.computeDataStatistics(data);
            try {
                table.generateLatexScript(data, latexDir, experimentName[type], indicator[type], isLowerTheBetter[type], probelms, problemObj, maxIterationsList, algorithms);
            } catch (IOException e) {
            }
        }
    }

    public void generateMedianPFMatlabDTLZMethod(boolean isChinese) {

        String matlabDir = "E://ResultsMaOPMethod/Matlab/";
        String saveMatlabFigureDir = "E://ResultsMaOPMethod/Figure/";
        String latexDir = "E://ResultsMaOPMethod/Figure/";

        String indicator = "HV";

        String experimentName = "PF4DTLZMethod";


        String[] dirs = {
                "E://ResultsMaOPMethod/"
        };

        String[][] problemNameList = {
                new String[]{
                        "DTLZ1(3)"
                },
                new String[]{
                        "DTLZ2(3)"
                },
                new String[]{
                        "DTLZ3(3)"
                },
                new String[]{
                        "DTLZ4(3)"
                }
                ,
                new String[]{
                        "Convex_DTLZ2(3)"
                }
        };
        int[][] popsList = {
                new int[]{
                        91
                },
                new int[]{
                        91
                },
                new int[]{
                        91
                },
                new int[]{
                        91
                },
                new int[]{
                        91
                }
        };
        int[][] maxIterationsList = {
                new int[]{
                        400
                },
                new int[]{
                        250
                },
                new int[]{
                        1000
                },
                new int[]{
                        600
                }
                ,
                new int[]{
                        250
                }
        };

        String[][] algorithmNameList = {
                new String[]{
                        "MOEACD-V",
                        "MOEACD-PEH",
                        "MOEACD-ACV",
                        "MOEACD-AECV"
                }
        };


        String[] probelms = {
                "DTLZ1",
                "DTLZ2",
                "DTLZ3",
                "DTLZ4"
                ,
                "Convex\\_DTLZ2"
        };

        int[][] problemObj = {
                new int[]{
                        3
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                },
                new int[]{
                        3
                }
        };

        int[] nObj = {
                3
        };
        String[] algorithms = {
                "MOEA/CD-V",
                "MOEA/CD-PEH",
                "MOEA/CD-ACV",
                "MOEA/CD-AECV"
        };


        int maxRun = 20;


        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, dirs, "final_");

        String[] pfDir = new String[algorithms.length];
        int c = 0;
        for (int i = 0; i < algorithmNameList.length; i++) {
            for (int j = 0; j < algorithmNameList[i].length; j++) {
                pfDir[c] = dirs[i] + "POF/" + algorithmNameList[i][j];
                c++;
            }
        }
//        int maxRun = 20;
//
//        List<List<List<List<Double>>>> data = collectData(maxRun, indicator, problemNameList, algorithmNameList, popsList, maxIterationsList, problemObj, hvRefPointList, dirs,"");

        JMetalLogger.logger.info("begin computeDataStatistics");
        int sizeProblem = problemNameList.length;

//        medianRun = new double[sizeProblem][][];
        File matlabOutput;
        matlabOutput = new File(matlabDir);
        if (!matlabOutput.exists()) {
            boolean result = new File(matlabDir).mkdirs();
            JMetalLogger.logger.info("Creating " + matlabDir + " directory");
        }
        //System.out.println("Experiment name: " + experimentName_);
        String matlabFile = matlabDir + "/" + experimentName + ".m";

        try {
            FileWriter os = new FileWriter(matlabFile, false);
            os.close();
        } catch (IOException e) {
        }


        for (int iProblem = 0; iProblem < sizeProblem; iProblem++) {

            int sizeProblemObj = problemObj[iProblem].length;

//            medianRun[iProblem] = new double[sizeProblemObj][];
            String latexFile = latexDir + "/" + experimentName + "_" + probelms[iProblem].replace("\\", "") + ".tex";
            try {
                printHeaderLatexCommands(latexFile, experimentName);
            } catch (IOException e) {
            }

            for (int jObj = 0; jObj < sizeProblemObj; jObj++) {

                int sizeAlgorithm = data.get(iProblem).get(jObj).size();

//                medianRun[iProblem][jObj] = new double[sizeAlgorithm];

                for (int kAlgorithm = 0; kAlgorithm < sizeAlgorithm; kAlgorithm++) {
                    List<Double> indicatorData = data.get(iProblem).get(jObj).get(kAlgorithm);
                    List<Integer> idxRun = new ArrayList<>(indicatorData.size());

                    for (int q = 0; q < indicatorData.size(); q++)
                        idxRun.add(q);

                    sort(indicatorData, idxRun);
//                    medianRun[iProblem][jObj][kAlgorithm] = (double)idxRun.get(idxRun.size()/2);
                    int medianRun = idxRun.get(idxRun.size() / 2);
                    String instance = problemNameList[iProblem][jObj] + "_" + popsList[iProblem][jObj] + "_" + maxIterationsList[iProblem][jObj];
                    String saveInstanceName = instance.replace("(", "[").replace(")", "]");

                    String pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + medianRun + ".pof";
                    String saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + ".eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile,  probelms[iProblem],problemObj[iProblem][jObj]);


                    int bestRun = idxRun.get(idxRun.size() - 1);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + bestRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_B.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);

                    int worstRun = idxRun.get(0);
                    pfFile = pfDir[kAlgorithm] + "_" + instance + "R" + worstRun + ".pof";
                    saveFile = saveMatlabFigureDir + "/pf_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (problemObj[iProblem][jObj] > 3)
                        saveFile = saveMatlabFigureDir + "/pc_" + algorithms[kAlgorithm].replace("/", "") + "_" + saveInstanceName + "_W.eps";
                    if (isChinese)
                        pfOrPcMatlabScriptZH(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);
                    else
                        pfOrPcMatlabScript(matlabFile, pfFile, saveFile, probelms[iProblem], problemObj[iProblem][jObj]);


                    String preStr = "pf_";
                    if (problemObj[iProblem][jObj] > 3)
                        preStr = "pc_";
                    pfOrPcLatexScript(latexFile, preStr, algorithms[kAlgorithm], saveInstanceName);

                }
            }
            try {
                printEndLatexCommands(latexFile);
            } catch (IOException e) {
            }
        }


    }

    public static void main(String[] argv){
        boolean isChinese = false;
        latexTable table = new latexTable();
//
//        table.generateTimeMatlabMaOP(isChinese);
//
//        table.generateTimeMatlabMOP2D(isChinese);
//        table.generateTimeMatlabMOP3D(isChinese);
//
        table.generateLatexMaOP();
//        table.generateMedianPFMatlabDTLZ(isChinese);

//        table.generateLatexMaOPMethod();
//        table.generateMedianPFMatlabDTLZMethod(isChinese);
//
////
//        table.generateLatexMOP();
//        table.generateMedianPFMatlabMOP(isChinese);
//
//        table.generateLatexMaOPScale();
//        table.generateMedianPFMatlabScaledDTLZ(isChinese);
//
//        table.generateLatexConstraints();
//        table.generateMedianPFMatlabConstrainedDTLZ(isChinese);
//
//        table.generateLatexIrregular();
//        table.generateMedianPFMatlabIrregular(isChinese);

//        table.generateLatexIrregularConstraints();
//        table.generateMedianPFMatlabIrregularConstraints(isChinese);
//
//        table.generateLatexUnConstrainedEngineer();
//        table.generateMedianPFMatlabUnConstrainedEngineer(isChinese);
//
//        table.generateLatexConstrainedEnegineer();
//        table.generateMedianPFMatlabConstrainedEngineer(isChinese);
//
//        table.generateLatexIrregularTest();
//        table.generateMedianPFMatlabIrregularTest(isChinese);

//        table.generateLatexMaOPTest();
//        table.generateLatexMOPTest();

//        table.generateBestInObjectivesLatexScript();
//          table.generateLatexTestDTLZ3();
    }
}
