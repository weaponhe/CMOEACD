package org.uma.jmetal.experiment.dataAnalysis;

import org.uma.jmetal.experiment.MyExperimentAnalysis;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ3;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ4;
import org.uma.jmetal.problem.multiobjective.wfg.*;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by X250 on 2016/4/15.
 */
public class evaluatingHypervolume {
    void execute(){
        int maxRun = 30;
        String[] problemNameList = {
                "DTLZ1(10)", "DTLZ2(10)", "DTLZ3(10)", "DTLZ4(10)", "Convex_DTLZ2(10)",
                "DTLZ1(15)", "DTLZ2(15)", "DTLZ3(15)", "DTLZ4(15)", "Convex_DTLZ2(15)"
        };
        int[] popsList = {
                275,275,275,275,275,
                135,135,135,135,135
        };
        int[] maxIterationsList = {
                1000,750,1000,2000,4000,
                1500,1000,1000,3000,4500
        };

        String[] algorithmNameList = {
                "NSGAIII",
                "MOEAD_TCH","MOEAD_PBI","MOEADDE_TCH","MOEADDE_PBI",
                "MOEADACD_TCH","MOEADACD_PBI","MOEADD_TCH","MOEADD_PBI",
                "CDEAD","CDEAPBI","CHEA",
                "CHEAIIa","CHEAIIb","CHEAIIc","CHEAIId","CHEAIIe","CHEAIIf"
        };

//        Point point3Dmin = new ArrayPoint(new double[]{0.6, 0.6, 0.6});
//        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.6, 0.6, 0.6, 0.6, 0.6});
//        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1, 1.1, 1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6});
//        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6});
//        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6});
//        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1, 1.1});

        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});

        Point[] hvRefPointList = {
                point10Dmin, point10D,point10D,point10D,point10D,
                point15Dmin,point15D,point15D,point15D,point15D
        };

        String baseDir = "E://Results/";
        String saveBaseDir = baseDir;
        try {
            MyExperimentAnalysis analysis = new MyExperimentAnalysis(baseDir,"");
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Double> hvs = new Vector<>(maxRun);
                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        String solutionPF = baseDir+"/POF/" + instance + "R" + kRun + ".pof";
                        WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                        WFGHypervolume hvIndicator = new WFGHypervolume();
                        double hv = hvIndicator.evaluate(solutionFront,hvRefPointList[iProblem]);
                        hvs.add(hv);
                        JMetalLogger.logger.info("["+kRun+"]  HV : "+ hv );
                    }
                    Vector statHV = new Vector();
                    int minIdx = analysis.getMinRun(hvs);
                    statHV.add(minIdx);
                    statHV.add(analysis.getMin(hvs,minIdx));
                    int maxIdx = analysis.getMaxRun(hvs);
                    statHV.add(maxIdx);
                    statHV.add(analysis.getMax(hvs,maxIdx));
                    statHV.add(analysis.getAverage(hvs));
                    Collections.sort(hvs);
                    statHV.add(hvs.get((hvs.size()+1)/2));

                    JMetalLogger.logger.info("HV :\t[min  "+statHV.get(0)+"]"+statHV.get(1)+"\t[avg]"+statHV.get(4)+"\t[median]"+statHV.get(5)+"\t[max "+statHV.get(2)+"]"+statHV.get(3));

                    try {
                        Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                        Vector<Vector<Double>> totalHV = new Vector<Vector<Double>>(maxRun);
                        for(int kRun = 0;kRun<maxRun;kRun++){
                            Vector<Integer> gen = new Vector();
                            gen.add(maxIterationsList[iProblem]);
                            Generates.add(gen);
                            Vector<Double> hvRun = new Vector();
                            hvRun.add(hvs.get(kRun));
                            totalHV.add(hvRun);
                        }
                        BufferedWriter writer = new DefaultFileOutputContext(saveBaseDir+"/HV/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,totalHV,writer);
                        writer.close();
                        BufferedWriter writerStat = new DefaultFileOutputContext(saveBaseDir+"/HV/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statHV, writerStat);
                        writerStat.close();
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }

    void execute2(){
        int maxRun = 30;

    int[] popsList = {
            91, 210, 156,
            91, 210, 156,
            91, 210, 156,
            91, 210, 156,
            91, 210, 156,
            91, 210, 156,
            91, 210, 156,
            91, 210, 156,
            91, 210, 156
    };
    int[] maxIterationsList = {
            400, 600, 750,
            400, 600, 750,
            400, 600, 750,
            400, 600, 750,
            400, 600, 750,
            400, 600, 750,
            400, 600, 750,
            400, 600, 750,
            400, 600, 750

    };

    Point point3D = new ArrayPoint(new double[]{3.0, 3.0, 7.0});
    Point point5D = new ArrayPoint(new double[]{3.0, 3.0, 3.0, 3.0,11.0 });
    Point point8D = new ArrayPoint(new double[]{3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 3.0, 17.0});

    Point[] hvRefPointList = {
            point3D, point5D, point8D,
            point3D, point5D, point8D,
            point3D, point5D, point8D,
            point3D, point5D, point8D,
            point3D, point5D, point8D,
            point3D, point5D, point8D,
            point3D, point5D, point8D,
            point3D, point5D, point8D,
            point3D, point5D, point8D
    };

        String[] problemNameList = {
               "WFG1(3)","WFG1(5)","WFG1(8)",
                "WFG2(3)","WFG2(5)","WFG2(8)",
                "WFG3(3)","WFG3(5)","WFG3(8)",
                "WFG4(3)","WFG4(5)","WFG4(8)",
                "WFG5(3)","WFG5(5)","WFG5(8)",
                "WFG6(3)","WFG6(5)","WFG6(8)",
                "WFG7(3)","WFG7(5)","WFG7(8)",
                "WFG8(3)","WFG8(5)","WFG8(8)",
                "WFG9(3)","WFG9(5)","WFG9(8)"
        };

        String[] algorithmNameList = {
                "NSGAIII",
                "MOEAD_TCH", "MOEAD_PBI", "MOEADDE_TCH", "MOEADDE_PBI",
                "MOEADACD_TCH", "MOEADACD_PBI", "MOEADD_TCH", "MOEADD_PBI",
                "MOEACDPBI","MOEACDNPBI"
        };


        String baseDir = "E://Results4-28-WFGCompare/";
        String saveBaseDir = "E://Results4-28-WFGCompare/Others/";
        try {
            MyExperimentAnalysis analysis = new MyExperimentAnalysis(baseDir,"");
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {

                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Double> hvs = new Vector<>(maxRun);
                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        String solutionPF = baseDir+"/POF/" + instance + "R" + kRun + ".pof";
                        WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
//                        double cubeHV = 1.0;
//                        for(int i=0;i<solutionFront.getPointDimensions();++i)
//                            cubeHV *= hvRefPointList[iProblem].getDimensionValue(i);

                        WFGHypervolume hvIndicator = new WFGHypervolume();
                        double hv = hvIndicator.evaluate(solutionFront,hvRefPointList[iProblem]);
                        hvs.add(hv);
                        JMetalLogger.logger.info("["+kRun+"]  HV : "+ hv );
                    }
                    Vector statHV = new Vector();
                    int minIdx = analysis.getMinRun(hvs);
                    statHV.add(minIdx);
                    statHV.add(analysis.getMin(hvs,minIdx));
                    int maxIdx = analysis.getMaxRun(hvs);
                    statHV.add(maxIdx);
                    statHV.add(analysis.getMax(hvs,maxIdx));
                    statHV.add(analysis.getAverage(hvs));
                    Collections.sort(hvs);
                    statHV.add(hvs.get((hvs.size()+1)/2));

                    JMetalLogger.logger.info("HV :\t[min  "+statHV.get(0)+"]"+statHV.get(1)+"\t[avg]"+statHV.get(4)+"\t[median]"+statHV.get(5)+"\t[max "+statHV.get(2)+"]"+statHV.get(3));

                    try {
                        Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                        Vector<Vector<Double>> totalHV = new Vector<Vector<Double>>(maxRun);
                        for(int kRun = 0;kRun<maxRun;kRun++){
                            Vector<Integer> gen = new Vector();
                            gen.add(maxIterationsList[iProblem]);
                            Generates.add(gen);
                            Vector<Double> hvRun = new Vector();
                            hvRun.add(hvs.get(kRun));
                            totalHV.add(hvRun);
                        }
                        BufferedWriter writer = new DefaultFileOutputContext(saveBaseDir+"/HV/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,totalHV,writer);
                        writer.close();
                        BufferedWriter writerStat = new DefaultFileOutputContext(saveBaseDir+"/HV/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statHV, writerStat);
                        writerStat.close();
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }
    public static void main(String[] argc){
        evaluatingHypervolume tmp = new evaluatingHypervolume();
        tmp.execute2();
    }
}
