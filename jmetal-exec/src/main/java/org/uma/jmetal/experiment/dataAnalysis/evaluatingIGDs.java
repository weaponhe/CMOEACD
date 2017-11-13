package org.uma.jmetal.experiment.dataAnalysis;

import org.uma.jmetal.experiment.MyExperimentAnalysis;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by X250 on 2016/4/18.
 */
public class evaluatingIGDs {
    public void executeTwo(){

    }
    public void executeMedium(){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
        };

        int[] popsList = {
                91, 210, 156,
                91, 210, 156,
                91, 210, 156,
                91, 210, 156,
                91, 210, 156
        };
        int[] maxIterationsList = {
                400, 600, 750,
                250, 350, 500,
                1000, 1000, 1000,
                600, 1000, 1250,
                250,750,2000
        };

        String[] algorithmNameList = {
                "MOEACDFPBI","MOEACDNFPBI"
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[4560]", frontDir + "DTLZ1.5D.pf[10626]",frontDir + "DTLZ1.8D.pf[11440]",
                frontDir + "DTLZ2.3D.pf[4560]",frontDir + "DTLZ2.5D.pf[10626]",frontDir + "DTLZ2.8D.pf[11440]",
                frontDir + "DTLZ3.3D.pf[4560]",frontDir + "DTLZ3.5D.pf[10626]",frontDir + "DTLZ3.8D.pf[11440]",
                frontDir + "DTLZ4.3D.pf[4560]",frontDir + "DTLZ4.5D.pf[10626]",frontDir + "DTLZ4.8D.pf[11440]",
                frontDir + "Convex_DTLZ2.3D.pf[4560]",frontDir + "Convex_DTLZ2.5D.pf[10626]",frontDir + "Convex_DTLZ2.8D.pf[11440]"
        };

        execute("E://Results-FPBI/","E://Results-FPBI/Others/",10,problemNameList,popsList,maxIterationsList,algorithmNameList,frontFileList);
    }

    public void executeSenior(){
        String[] problemNameList = {
                "DTLZ1(10)","DTLZ1(15)",
                "DTLZ2(10)","DTLZ2(15)",
                "DTLZ3(10)","DTLZ3(15)",
                "DTLZ4(10)","DTLZ4(15)",
                "Convex_DTLZ2(10)","Convex_DTLZ2(15)"
        };

        int[] popsList = {
                275, 135,
                275, 135,
                275, 135,
                275, 135,
                275, 135
        };
        int[] maxIterationsList = {
                1000, 1500,
                750, 1000,
                1000, 1000,
                2000, 3000,
                4000,4500
        };

        String[] algorithmNameList = {
                "NSGAIII",
                "MOEAD_TCH","MOEAD_PBI","MOEADDE_TCH","MOEADDE_PBI",
                "MOEADACD_TCH","MOEADACD_PBI","MOEADD_TCH","MOEADD_PBI",
                "CDEAD","CDEAPBI","CHEA",
                "CHEAIIa","CHEAIIb","CHEAIIc","CHEAIId","CHEAIIe","CHEAIIf"
        };


        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "DTLZ1.10D.pf[24310]",frontDir + "DTLZ1.15D.pf[38760]",
                frontDir + "DTLZ2.10D.pf[24310]", frontDir + "DTLZ2.15D.pf[38760]",
                frontDir + "DTLZ3.10D.pf[24310]",frontDir + "DTLZ3.15D.pf[38760]",
                frontDir + "DTLZ4.10D.pf[24310]",frontDir + "DTLZ4.15D.pf[38760]",
                frontDir + "Convex_DTLZ2.10D.pf[24310]",frontDir + "Convex_DTLZ2.15D.pf[38760]",
        };

        execute("E://Results/","E://Results/Others/",30,problemNameList,popsList,maxIterationsList,algorithmNameList,frontFileList);

    }



    public void executeMOEACDMedium(){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
        };

        int[] popsList = {
                91, 210, 156,
                91, 210, 156,
                91, 210, 156,
                91, 210, 156,
                91, 210, 156
        };
        int[] maxIterationsList = {
                400, 600, 750,
                250, 350, 500,
                1000, 1000, 1000,
                600, 1000, 1250,
                250,750,2000
        };

//        String[] algorithmNameList = {
//                "MOEADDE_TCH","MOEADDE_PBI",
//                "MOEADACD_TCH","MOEADACD_PBI",
//                "MOEAD_TCH","MOEAD_PBI",
//                "MOEADD_TCH","MOEADD_PBI",
//                "NSGAIII",
//                "MOEACDPBI","MOEACDNPBI",
//                "MOEACDDE"
//        };
        String[] algorithmNameList = {
                "MOEACDAPBI","MOEACDNAPBI"
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[4560]", frontDir + "DTLZ1.5D.pf[10626]",frontDir + "DTLZ1.8D.pf[11440]",
//                frontDir + "DTLZ2.3D.pf[4560]",frontDir + "DTLZ2.5D.pf[10626]",frontDir + "DTLZ2.8D.pf[11440]",
//                frontDir + "DTLZ3.3D.pf[4560]",frontDir + "DTLZ3.5D.pf[10626]",frontDir + "DTLZ3.8D.pf[11440]",
//                frontDir + "DTLZ4.3D.pf[4560]",frontDir + "DTLZ4.5D.pf[10626]",frontDir + "DTLZ4.8D.pf[11440]",
//                frontDir + "Convex_DTLZ2.3D.pf[4560]",frontDir + "Convex_DTLZ2.5D.pf[10626]",frontDir + "Convex_DTLZ2.8D.pf[11440]"
//        };

        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[91]",
                frontDir + "DTLZ1.5D.pf[210]",
                frontDir + "DTLZ1.8D.pf[156]",
                frontDir + "DTLZ2.3D.pf[91]",
                frontDir + "DTLZ2.5D.pf[210]",
                frontDir + "DTLZ2.8D.pf[156]",
                frontDir + "DTLZ3.3D.pf[91]",
                frontDir + "DTLZ3.5D.pf[210]",
                frontDir + "DTLZ3.8D.pf[156]",
                frontDir + "DTLZ4.3D.pf[91]",
                frontDir + "DTLZ4.5D.pf[210]",
                frontDir + "DTLZ4.8D.pf[156]",
                frontDir + "Convex_DTLZ2.3D.pf[91]",
                frontDir + "Convex_DTLZ2.5D.pf[210]",
                frontDir + "Convex_DTLZ2.8D.pf[156]"
        };
        execute("E://ResultsThetaAdaptive/","E://ResultsThetaAdaptive/Others/",10,problemNameList,popsList,maxIterationsList,algorithmNameList,frontFileList);
    }


    public void execute(String baseDir,String saveBaseDir,int maxRun,String[] problemNameList,int[] popsList,int[] maxIterationsList,String[] algorithmNameList,String[] frontFileList){

        try {
            MyExperimentAnalysis analysis = new MyExperimentAnalysis(baseDir,"");
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                Front referenceFront = new ArrayFront(frontFileList[iProblem]);
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Double> igd = new Vector<>(maxRun);
                    Vector<Double> igdplus = new Vector<>(maxRun);
                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        String solutionPF = baseDir+"/POF/" + instance + "R" + kRun + ".pof";
                        Front solutionFront = new ArrayFront(solutionPF);

                        InvertedGenerationalDistance igdIndicator = new InvertedGenerationalDistance();
                        double igdV = igdIndicator.invertedGenerationalDistance(solutionFront,referenceFront);
                        igd.add(igdV);
                        InvertedGenerationalDistancePlus igdplusIndicator = new InvertedGenerationalDistancePlus();
                        double igdplusV = igdplusIndicator.invertedGenerationalDistancePlus(solutionFront,referenceFront);
                        igdplus.add(igdplusV);
                        JMetalLogger.logger.info("["+kRun+"]  IGD : "+ igdV + "  IGDPlus : " + igdplusV );
                    }
                    Vector statIGD = new Vector();
                    int minIdx = analysis.getMinRun(igd);
                    statIGD.add(minIdx);
                    statIGD.add(analysis.getMin(igd,minIdx));
                    int maxIdx = analysis.getMaxRun(igd);
                    statIGD.add(maxIdx);
                    statIGD.add(analysis.getMax(igd,maxIdx));
                    statIGD.add(analysis.getAverage(igd));
                    Collections.sort(igd);
                    statIGD.add(igd.get((igd.size()+1)/2));

                    Vector statIGDPlus = new Vector();
                    minIdx = analysis.getMinRun(igdplus);
                    statIGDPlus.add(minIdx);
                    statIGDPlus.add(analysis.getMin(igdplus,minIdx));
                    maxIdx = analysis.getMaxRun(igdplus);
                    statIGDPlus.add(maxIdx);
                    statIGDPlus.add(analysis.getMax(igdplus,maxIdx));
                    statIGDPlus.add(analysis.getAverage(igdplus));
                    Collections.sort(igdplus);
                    statIGDPlus.add(igdplus.get((igdplus.size()+1)/2));

                    JMetalLogger.logger.info("IGD :\t[min  "+statIGD.get(0)+"]"+statIGD.get(1)+"\t[avg]"+statIGD.get(4)+"\t[median]"+statIGD.get(5)+"\t[max "+statIGD.get(2)+"]"+statIGD.get(3));
                    JMetalLogger.logger.info("IGDPlus :\t[min  "+statIGDPlus.get(0)+"]"+statIGDPlus.get(1)+"\t[avg]"+statIGDPlus.get(4)+"\t[median]"+statIGDPlus.get(5)+"\t[max "+statIGDPlus.get(2)+"]"+statIGDPlus.get(3));

                    try {
                        Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                        Vector<Vector<Double>> totalIGD = new Vector<Vector<Double>>(maxRun);
                        Vector<Vector<Double>> totalIGDPlus = new Vector<Vector<Double>>(maxRun);
                        for(int kRun = 0;kRun<maxRun;kRun++){
                            Vector<Integer> gen = new Vector();
                            gen.add(maxIterationsList[iProblem]);
                            Generates.add(gen);
                            Vector<Double> igdRun = new Vector();
                            igdRun.add(igd.get(kRun));
                            totalIGD.add(igdRun);
                            Vector<Double> igdplusRun = new Vector();
                            igdplusRun.add(igdplus.get(kRun));
                            totalIGDPlus.add(igdplusRun);
                        }
                        BufferedWriter writerIGD = new DefaultFileOutputContext(saveBaseDir+"/IGD/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,totalIGD,writerIGD);
                        writerIGD.close();
                        BufferedWriter writerStatIGD = new DefaultFileOutputContext(saveBaseDir+"/IGD/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statIGD, writerStatIGD);
                        writerStatIGD.close();
                        BufferedWriter writerIGDPlus = new DefaultFileOutputContext(saveBaseDir+"/IGDPlus/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,totalIGDPlus,writerIGDPlus);
                        writerIGDPlus.close();
                        BufferedWriter writerStatIGDPlus = new DefaultFileOutputContext(saveBaseDir+"/IGDPlus/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statIGDPlus, writerStatIGDPlus);
                        writerStatIGDPlus.close();
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }
    public static void main(String[] argc){
        evaluatingIGDs computor = new evaluatingIGDs();
        computor.executeMedium();
//        computor.executeMOEACDMedium();
    }
}
