package org.uma.jmetal.experiment.dataAnalysis;

import org.uma.jmetal.experiment.*;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.zdt.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;
import org.uma.jmetal.util.fileinput.util.ReadDoubleDataFile;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by X250 on 2016/4/17.
 */
public class emergeStat {
    int numStat = 6;
    enum STATTYPE{MINRUN,MIN,MAXRUN,MAX,AVG,MEDIAM};
    String[] statName = {"MinRun","Min","MaxRun","Max","Avg","Median"};

    public void executeTWO(){
        String[] problemNameList = {
                "ZDT1(2)","ZDT2(2)","ZDT3(2)","ZDT4(2)","ZDT6(2)"
        };
        int[] popsList = {
                100,100,100,100,100
        };
        int[] maxIterationsList = {
                200,200,200,1000,200
        };

        String[] algorithmNameList = {
                "NSGAIII",
                "MOEAD_TCH","MOEAD_PBI","MOEADDE_TCH","MOEADDE_PBI",
                "MOEADACD_TCH","MOEADACD_PBI","MOEADD_TCH","MOEADD_PBI",
                "CAEA","CDEAD","CDEAPBI","CHEA",
                "CHEAIIa","CHEAIIb","CHEAIIc","CHEAIId","CHEAIIe","CHEAIIf"
        };

        String[] indicatorNameList = {
                "HV","IGD","IGDPlus"
        };

        int[] statList = {
                STATTYPE.MINRUN.ordinal(),STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAXRUN.ordinal(),STATTYPE.MAX.ordinal()
        };
        String baseDir = "E://Results/";
        execute(baseDir,"Two","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }

    public void executeTimeTWO(){
        String[] problemNameList = {
                "ZDT1(2)","ZDT2(2)","ZDT3(2)","ZDT4(2)","ZDT6(2)"
        };
        int[] popsList = {
                100,100,100,100,100
        };
        int[] maxIterationsList = {
                200,200,200,1000,200
        };

        String[] algorithmNameList = {
                "NSGAIII",
                "MOEAD_TCH","MOEAD_PBI","MOEADDE_TCH","MOEADDE_PBI",
                "MOEADACD_TCH","MOEADACD_PBI","MOEADD_TCH","MOEADD_PBI",
                "CAEA","CDEAD","CDEAPBI","CHEA",
                "CHEAIIa","CHEAIIb","CHEAIIc","CHEAIId","CHEAIIe","CHEAIIf"
        };

        String[] indicatorNameList = {
                "Time"
        };

        int[] statList = {
                0
        };
        String baseDir = "E://Results/";
        execute(baseDir,"Two","avg",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
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
                "NSGAIII",
                "MOEAD_TCH","MOEAD_PBI","MOEADDE_TCH","MOEADDE_PBI",
                "MOEADACD_TCH","MOEADACD_PBI","MOEADD_TCH","MOEADD_PBI",
                "CDEAD","CDEAPBI","CHEA",
                "CHEAIIa","CHEAIIb","CHEAIIc","CHEAIId","CHEAIIe","CHEAIIf"
        };

//        String[] indicatorNameList = {
//                "HV","IGD","IGDPlus"
//        };
        String[] indicatorNameList = {
               "IGD","IGDPlus"
        };


        int[] statList = {
                STATTYPE.MINRUN.ordinal(),STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAXRUN.ordinal(),STATTYPE.MAX.ordinal()
        };
//        String baseDir = "E://Results/";
        String baseDir = "E://Results/Others/";
        execute(baseDir,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }

    public void executeTimeMedium(){
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
                "NSGAIII",
                "MOEAD_TCH","MOEAD_PBI","MOEADDE_TCH","MOEADDE_PBI",
                "MOEADACD_TCH","MOEADACD_PBI","MOEADD_TCH","MOEADD_PBI",
                "CDEAD","CDEAPBI","CHEA",
                "CHEAIIa","CHEAIIb","CHEAIIc","CHEAIId","CHEAIIe","CHEAIIf"
        };

        String[] indicatorNameList = {
                "Time"
        };

        int[] statList = {
                0
        };
        String baseDir = "E://Results/";
        execute(baseDir,"Medium","avg",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
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

        String[] indicatorNameList = {
                "IGD","IGDPlus"
        };

        int[] statList = {
                STATTYPE.MINRUN.ordinal(),STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAXRUN.ordinal(),STATTYPE.MAX.ordinal()
        };
        String baseDir = "E://Results/";
        execute(baseDir,"Senior","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }

    public void executeTimeSenior(){
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

        String[] indicatorNameList = {
                "Time"
        };

        int[] statList = {
                0
        };
        String baseDir = "E://Results/";
        execute(baseDir,"Senior","avg",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }


    public void executeMOEACDMedium(){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)"
//                ,
//                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
        };

        int[] popsList = {
                91, 210, 156,
                91, 210, 156,
                91, 210, 156,
                91, 210, 156
//                ,
//                91, 210, 156
        };
        int[] maxIterationsList = {
                400, 600, 750,
                250, 350, 500,
                1000, 1000, 1000,
                600, 1000, 1250
//                ,
//                250,750,2000
        };
//        int[] maxIterationsList = {
//                500, 750, 1000,
//                500, 750, 1000,
//                1000, 1250, 1500,
//                1000, 1250, 1500
////                ,
////                500, 1000, 2000
//        };
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
                "MOEACDCMM","MOEACDCMMAPBI"
        };

//        String[] indicatorNameList = {
//                "HV"
//        };
        String[] indicatorNameList = {
               "IGD"
        };

        int[] statList = {
                STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };
//        String baseDir = "E://Results/";
        String baseDir = "E://Results/";
        execute(baseDir,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }

    public void executeTimeMOEACDMedium(){
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
                "MOEADDE_TCH","MOEADDE_PBI",
                "MOEADACD_TCH","MOEADACD_PBI",
                "MOEAD_TCH","MOEAD_PBI",
                "MOEADD_TCH","MOEADD_PBI",
                "NSGAIII",
                "MOEACDPBI","MOEACDNPBI",
                "MOEACDDE"
        };

        String[] indicatorNameList = {
                "Time"
        };

        int[] statList = {
                0
        };
        String baseDir = "E://Results-4-27-compare/";
        execute(baseDir,"Medium","avg",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }



    public void executeScalarMedium(){
        String[] problemNameList = {
                "SDTLZ1(3)","SDTLZ1(5)","SDTLZ1(8)","SDTLZ1(10)","SDTLZ1(15)",
                "SDTLZ2(3)","SDTLZ2(5)","SDTLZ2(8)","SDTLZ2(10)","SDTLZ2(15)"
        };

        int[] popsList = {
                91, 210, 156,275,135,
                91, 210, 156,275,135
        };

//        int[] popsList = {
//                109, 251, 165,286,136,
//                109, 251, 165,286,136
//        };
        int[] maxIterationsList = {
                400, 600, 750,1000,1500,
                250, 350, 500,750,1000
        };

        String[] algorithmNameList = {
//                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"
//                ,

        };

        String[] indicatorNameList = {
//                "HV",
                "IGD","IGDPlus"
//                ,"HE"
        };
//        String[] indicatorNameList = {
//                "IGD","IGDPlus"
//        };


        int[] statList = {
                STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };
//        String baseDir = "E://ResultsMaOPScale000012/";
        String baseDir = "D://Experiments//ResultsMaOPScale000024/stat/";
        execute(baseDir,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }

    public void executeWFGMedium(){
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

        String[] algorithmNameList = {
                "NSGAIII",
                "MOEAD_TCH", "MOEAD_PBI", "MOEADDE_TCH", "MOEADDE_PBI",
                "MOEADACD_TCH", "MOEADACD_PBI", "MOEADD_TCH", "MOEADD_PBI",
                "MOEACDPBI","MOEACDNPBI"
        };

        String[] indicatorNameList = {
                "HV"
        };


        int[] statList = {
               STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };
//        String baseDir = "E://Results/";
        String baseDir = "E://Results4-28-WFGCompare/";
        execute(baseDir,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }

    public void executeMaOP(){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)","DTLZ1(10)","DTLZ1(15)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)","DTLZ2(10)","DTLZ2(15)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)","DTLZ3(10)","DTLZ3(15)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)","DTLZ4(10)","DTLZ4(15)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)","Convex_DTLZ2(10)","Convex_DTLZ2(15)"
        };
//
        int[] popsList = {
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135
        };
//        int[] popsList = {
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136
//        };
        int[] maxIterationsList = {
                400,600,750,1000,1500,
                250,350,500,750,1000,
                1000,1000,1000,1500,2000,
                600,1000,1250,2000,3000,
                250,750,2000,4000,4500
        };

//        String[] problemNameList = {
//                "DTLZ1(8)","DTLZ1(10)","DTLZ1(15)",
//                "DTLZ2(8)","DTLZ2(10)","DTLZ2(15)",
//                "DTLZ3(8)","DTLZ3(10)","DTLZ3(15)",
//                "DTLZ4(8)","DTLZ4(10)","DTLZ4(15)",
//                "Convex_DTLZ2(8)","Convex_DTLZ2(10)","Convex_DTLZ2(15)"
//        };
//
//        int[] popsList = {
//                156, 275,135,
//                156, 275,135,
//                156, 275,135,
//                156, 275,135,
//                156, 275,135
//        };
//        int[] maxIterationsList = {
//                750,1000,1500,
//                500,750,1000,
//                1000,1500,2000,
//                1250,2000,3000,
//                2000,4000,4500
//        };
        String[] algorithmNameList = {
//                "NSGAIII"
// ,"MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"

        };

        int[] statList = {
                STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };

        String[] indicatorNameList2 = {
                "IGD"
                ,"IGDPlus"
//                ,
//                "HV",
//                "HE"
        };

        String baseDir2 = "D://Experiments/ResultsMaOPMeasure000024/stat/";
//        String baseDir2 = "E://ResultsMaOPMeasure000012/";

        execute(baseDir2,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList2,statList);
    }


    public void executeMaOPIIa(){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
        };

        int[] popsList = {
                109, 251, 165,
                109, 251, 165,
                109, 251, 165,
                109, 251, 165,
                109, 251, 165
        };
        int[] maxIterationsList = {
                400,600,750,
                250,350,500,
                1000,1000,1000,
                600,1000,1250,
                250,750,2000
        };

        String[] algorithmNameList = {
                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"
//                ,
//                "MOEACDPBI","MOEACDPBIS","MOEACDPBIAN","MOEACDAPBI","MOEACDFPBI","MOEACDFAPBI","MOEACDFBA","MOEACDLHV",
//                "MOEACD","MOEACDN"
        };


        int[] statList = {
                STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };

        String[] indicatorNameList2 = {
                "IGD"
                ,
                "HV",
                "HE"
        };

        String baseDir2 = "D://Experiments/ResultsMaOPMeasure11/stat2/";
        execute(baseDir2,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList2,statList);
    }
    public void execute1(){
//        String[] problemNameList = {
//                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)","DTLZ1(10)",
//                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)","DTLZ2(10)",
//                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)","DTLZ3(10)",
//                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)","DTLZ4(10)",
//                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)","Convex_DTLZ2(10)"
//        };
//
//        int[] popsList = {
//                91, 210, 156,275,
//                91, 210, 156,275,
//                91, 210, 156,275,
//                91, 210, 156,275,
//                91, 210, 156,275
//        };
//        int[] maxIterationsList = {
//                400,600,750,1000,
//                250,350,500,750,
//                1000,1000,1000,1500,
//                600,1000,1250,2000,
//                250,750,2000,4000
//        };
        String[] problemNameList = {
                "DTLZ1(10)",
                "DTLZ2(10)",
                "DTLZ3(10)",
                "DTLZ4(10)",
                "Convex_DTLZ2(10)"
        };

        int[] popsList = {
                275,
                275,
                275,
                275,
                275
        };
        int[] maxIterationsList = {
                1000,
                750,
                1500,
                2000,
                4000
        };
//        String[] problemNameList = {
//                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)",
//                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)",
//                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)",
//                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)",
//                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
//        };
//
//        int[] popsList = {
//                91, 210, 156,
//                91, 210, 156,
//                91, 210, 156,
//                91, 210, 156,
//                91, 210, 156
//        };
//        int[] maxIterationsList = {
//                400,600,750,
//                250,350,500,
//                1000,1000,1000,
//                600,1000,1250,
//                250,750,2000
//        };
        // "NSGAIII","MOEADDE_PBI","MOEADACD_PBI","MOEAD_PBI","MOEADD_PBI",
        String[] algorithmNameList = {
               "MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
        };
//        String[] algorithmNameList = {
//               "MOEADACD_PBI"
//        };

//        String[] algorithmNameList = {
//               "MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
//        };

        int[] statList = {
                STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };

//        String[] indicatorNameList1 = {
//                "IGD"
//        };
//
//        String baseDir1 = "E://ResultsMaOP1/";
//        execute(baseDir1,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList1,statList);


        String[] indicatorNameList2 = {
//                "IGD"
//                ,
                "HV","HE"
        };

        String baseDir2 = "E://HVTaoT1//";
        execute(baseDir2,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList2,statList);

    }


    public void execute2(){
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
                400,600,750,
                250,350,500,
                1000,1000,1000,
                600,1000,1250,
                250,750,2000
        };

        String[] algorithmNameList = {
                "NSGAIII","MOEADDE_PBI","MOEADACD_PBI","MOEAD_PBI","MOEADD_PBI","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
        };

//        String[] algorithmNameList = {
//                "MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
//        };

        int[] statList = {
                STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };

//        String[] indicatorNameList1 = {
//                "IGD"
//        };
//
//        String baseDir1 = "E://ResultsMaOP2/";
//        execute(baseDir1,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList1,statList);


        String[] indicatorNameList2 = {
//                "IGD"
//                ,
//                "HV",
                "HE"
        };

        String baseDir2 = "E://ResultsDataAnalysis/";
        execute(baseDir2,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList2,statList);

    }


    public void execute3(){
//        String[] problemNameList = {
//                "DTLZ1(8)","DTLZ1(10)",
//                "DTLZ2(8)","DTLZ2(10)",
//                "DTLZ3(8)","DTLZ3(10)",
//                "DTLZ4(8)","DTLZ4(10)",
//                "Convex_DTLZ2(8)","Convex_DTLZ2(10)"
//        };
//
//        int[] popsList = {
//                156,275,
//                156,275,
//                156,275,
//                156,275,
//                156,275
//        };
//        int[] maxIterationsList = {
//                750, 1000,
//                500, 750,
//                1000, 1500,
//                1250, 2000,
//                2000, 4000
//        };

        String[] problemNameList = {
                "DTLZ1(8)",
                "DTLZ2(8)",
                "DTLZ3(8)",
                "DTLZ4(8)",
                "Convex_DTLZ2(8)"
        };

        int[] popsList = {
                156,
                156,
                156,
                156,
                156
        };
        int[] maxIterationsList = {
                750,
                500,
                1000,
                1250,
                2000
        };
        String[] algorithmNameList = {
                "MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
        };


        int[] statList = {
                STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };

        String[] indicatorNameList1 = {
                "IGD"
        };

        String baseDir1 = "E://ResultsMaOPTaoT1/";
        execute(baseDir1,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList1,statList);

//
//        String[] indicatorNameList2 = {
//                "HV","HE"
//        };
//
//        String baseDir2 = "E://ResultsMaOPTaoT1/";
//        execute(baseDir2,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList2,statList);

    }

    public void execute4(){
//        String[] problemNameList = {
//                "DTLZ1(8)","DTLZ1(10)",
//                "DTLZ2(8)","DTLZ2(10)",
//                "DTLZ3(8)","DTLZ3(10)",
//                "DTLZ4(8)","DTLZ4(10)",
//                "Convex_DTLZ2(8)","Convex_DTLZ2(10)"
//        };
//
//        int[] popsList = {
//                156,275,
//                156,275,
//                156,275,
//                156,275,
//                156,275
//        };
//        int[] maxIterationsList = {
//                1250, 1500,
//                1250, 1500,
//                1500, 2000,
//                2000, 3000,
//                3000, 5000
//        };
        String[] problemNameList = {
                "DTLZ1(8)",
                "DTLZ2(8)",
                "DTLZ3(8)",
                "DTLZ4(8)",
                "Convex_DTLZ2(8)"
        };

        int[] popsList = {
                156,
                156,
                156,
                156,
                156
        };
        int[] maxIterationsList = {
                1250,
                1250,
                1500,
                2000,
                3000
        };

        String[] algorithmNameList = {
                "MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
        };


        int[] statList = {
                STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };

        String[] indicatorNameList1 = {
                "IGD"
        };

        String baseDir1 = "E://ResultsMaOPTaoT2/";
        execute(baseDir1,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList1,statList);


//        String[] indicatorNameList2 = {
//                "HV","HE"
//        };
//
//        String baseDir2 = "E://ResultsMaOPTaoT2/";
//        execute(baseDir2,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList2,statList);
    }

    public void executeMOP(){
        String[] problemNameList = {
                "MOP1(2)","MOP2(2)","MOP3(2)","MOP4(2)","MOP5(2)","MOP6(3)","MOP7(3)"
        };

//        int[] popsList = {
//                200,200,200,200,200,300,300
//        };
////        int[] maxIterationsList = {
////                500,500,500,500,500,750,750
////        };
//        int[] maxIterationsList = {
//                750,750,750,750,750,500,500
//        };
        int[] popsList = {
                100,100,100,100,100,300,300
        };
//
//        int[] popsList = {
//                101,101,101,101,101,316,316
//        };
        int[] maxIterationsList = {
                3000,3000,3000,3000,3000,3000,3000
        };
        String[] algorithmNameList = {
//                "NSGAIII"
// ,"MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"
//                ,
//                "MOEACDPBI","MOEACDPBIS","MOEACDPBIAN","MOEACDAPBI","MOEACDFPBI","MOEACDFAPBI","MOEACDFBA","MOEACDLHV",

        };

//        String[] algorithmNameList = {
//               "MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
//        };

        int[] statList = {
                STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };

//        String[] indicatorNameList1 = {
//                "IGD"
//        };
//
//        String baseDir1 = "E://ResultsMaOP1/";
//        execute(baseDir1,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList1,statList);


        String[] indicatorNameList2 = {
                "IGD"
                ,
                "IGDPlus"
                ,
                "HV"
                ,"HE"
        };

//        String baseDir2 = "E://ResultsMOPMeasure000012/";

        String baseDir2 = "D://Experiments/ResultsMOPMeasure000023/";
        execute(baseDir2,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList2,statList);
    }

    public void executeTimeMOP(){
        String[] problemNameList = {
                "MOP1(2)","MOP2(2)","MOP3(2)","MOP4(2)","MOP5(2)","MOP6(3)","MOP7(3)"
        };

//        int[] popsList = {
//                200,200,200,200,200,300,300
//        };
////        int[] maxIterationsList = {
////                500,500,500,500,500,750,750
////        };
//        int[] maxIterationsList = {
//                750,750,750,750,750,500,500
//        };
        int[] popsList = {
                100,100,100,100,100,300,300
        };
//
//        int[] popsList = {
//                101,101,101,101,101,316,316
//        };
        int[] maxIterationsList = {
                3000,3000,3000,3000,3000,3000,3000
        };

        String[] algorithmNameList = {
//                "NSGAIII"
// ,
// "MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"

        };
        String[] indicatorNameList = {
                "Time"
        };

        int[] statList = {
                0
        };
        String baseDir = "D://Experiments/ResultsMOPMeasure000023/";
//        String baseDir = "E://ResultsMOPMeasure00002/";
        execute(baseDir,"Medium","avg",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }

    public void executeTimeMaOP(){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)","DTLZ1(10)","DTLZ1(15)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)","DTLZ2(10)","DTLZ2(15)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)","DTLZ3(10)","DTLZ3(15)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)","DTLZ4(10)","DTLZ4(15)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)","Convex_DTLZ2(10)","Convex_DTLZ2(15)"
        };
//        int[] popsList = {
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136
//        };
        int[] popsList = {
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135
        };
        int[] maxIterationsList = {
                400,600,750,1000,1500,
                250,350,500,750,1000,
                1000,1000,1000,1500,2000,
                600,1000,1250,2000,3000,
                250,750,2000,4000,4500
        };
        String[] algorithmNameList = {
//                "NSGAIII"
// ,"MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"
//                "MOEACDPBI","MOEACDPBIS","MOEACDPBIAN","MOEACDAPBI","MOEACDFPBI","MOEACDFAPBI","MOEACDFBA","MOEACDLHV",
//"MOEADGR_TCH","MOEADGR_PBI"

                "MOEACD"

        };
        String[] indicatorNameList = {
                "Time"
        };

        int[] statList = {
                0
        };
        String baseDir = "D://Experiments/ResultsMaOPMeasure0000317/";
//        String baseDir = "E://ResultsMaOPMeasure0000305/";
        execute(baseDir,"Medium","avg",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }


    public void executeTimeMaOPIIa(){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)",
//                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
        };

        int[] objNumList = {
                3,5,8,
//                3,5,8,
                3,5,8,
                3,5,8,
                3,5,8
        };

        int[] popsList = {
                109, 126, 165,
//                109, 126, 165,
                109, 126, 165,
                109, 126, 165,
                109, 126, 165
        };
        int[] maxIterationsList = {
                400,600,750,
//                250,350,500,
                1000,1000,1000,
                600,1000,1250,
                250,750,2000
        };
        String[] algorithmNameList = {
                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI",
//                "MOEACDPBI","MOEACDPBIS","MOEACDPBIAN","MOEACDAPBI","MOEACDFPBI","MOEACDFAPBI","MOEACDFBA","MOEACDLHV",
                "MOEACD","MOEACDN"
        };
        String[] indicatorNameList = {
                "Time"
        };

        int[] statList = {
                0
        };
        String baseDir = "D://Experiments/ResultsMaOPMeasure11/";
        execute(baseDir,"Medium","avg",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }


    public void executeTimeMaOPTao(){
                String[] problemNameList = {
                "DTLZ1(8)","DTLZ1(10)",
                "DTLZ2(8)","DTLZ2(10)",
                "DTLZ3(8)","DTLZ3(10)",
                "DTLZ4(8)","DTLZ4(10)",
                "Convex_DTLZ2(8)","Convex_DTLZ2(10)"
        };

        int[] popsList = {
                156,275,
                156,275,
                156,275,
                156,275,
                156,275
        };
        int[] maxIterationsList = {
                750, 1000,
                500, 750,
                1000, 1500,
                1250, 2000,
                2000, 4000
        };
        String[] algorithmNameList = {
               "MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
        };
        String[] indicatorNameList = {
                "Time"
        };

        int[] statList = {
                0
        };
        String baseDir = "E://ResultsMaOPTaoT1/";
        execute(baseDir,"Medium","avg",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList,statList);
    }
    public void execute(String baseDir,String saveFilenameTag, String tag,String[] problemNameList,int[] popsList,int[] maxIterationsList,String[] algorithmNameList,String[] indicatorNameList,int[] statList){

        try {

            for (int iIndicator = 0; iIndicator < indicatorNameList.length; ++iIndicator) {
                BufferedWriter writer = new DefaultFileOutputContext(baseDir + "/" + indicatorNameList[iIndicator] + "/emerge"+saveFilenameTag+".csv").getFileWriter();
                writer.write("Problem,Popsize,MaxIterations,StatisticsType,");
                for(int jAlg = 0 ; jAlg < algorithmNameList.length -1 ;++jAlg)
                    writer.write(algorithmNameList[jAlg]+",");
                writer.write(algorithmNameList[algorithmNameList.length-1]);
                writer.newLine();

                for (int kProblem = 0; kProblem < problemNameList.length; ++kProblem) {
                    String problemConfig = problemNameList[kProblem] + "_" + popsList[kProblem] + "_" + maxIterationsList[kProblem];
                    double[][] emergeData = emergeStatOnProblem(baseDir, indicatorNameList[iIndicator],tag, problemConfig, algorithmNameList, statList);
                    writer.write(toCSVString(emergeData,problemNameList[kProblem],popsList[kProblem],maxIterationsList[kProblem],statList));
                }
                writer.close();
            }
        }catch (IOException e){}
    }
    public double[][] emergeStatOnProblem(String baseDir,String indicatorName,String tag,String problemConfig,String[] algorithmNameList,int[] emergeStatList){
        double[][] result = new double[algorithmNameList.length][emergeStatList.length];
        ReadDoubleDataFile loadData = new ReadDoubleDataFile();
        for(int i=0;i<algorithmNameList.length;++i){
            String filename = baseDir+"/"+indicatorName+"/"+tag+"_"+algorithmNameList[i]+"_"+problemConfig+".csv";
            try{
                double[] tmp = loadData.readFile(filename);
                for(int j=0;j<emergeStatList.length;++j)
                    result[i][j] = tmp[emergeStatList[j]];
            }catch (FileNotFoundException e){
                JMetalLogger.logger.info("Can not find file \""+filename+"\"");
            }
        }
        return result;
    }

    public String toCSVString(double[][] data,String problemName,int pops,int iterations,int[] statList){
        String outputString = "";

        for(int iStat=0;iStat<statList.length;++iStat){
            if(iStat==0)
                outputString += problemName + "," + pops + "," + iterations + ",";
            else
                outputString += " , , ,";
            outputString += statName[statList[iStat]]+",";

            for(int jAlg=0;jAlg<data.length-1;++jAlg){
                outputString += data[jAlg][iStat]+",";
            }
            outputString += data[data.length-1][iStat];
            outputString += "\n";
        }
        return outputString;
    }
//    public String tooLatexTableString(double[][] data,String problemName,int pops,int iterations){
//
//        for (int i = 0; i < experiment.getIndicatorList().size(); i++) {
//            printData(latexFile, i, mean, stdDeviation, "Mean and Standard Deviation");
//            printData(latexFile, i, median, iqr, "Median and Interquartile Range");
//        }
//    }
//
//    public String headerLatexString(String titleName,String indicatorName, String caption, String[] algrithmList) {
//        String outputString = "";
//        outputString += "\\documentclass{article}" + "\n";
//        outputString += "\\title{" + titleName + "}" + "\n";
//        outputString += "\\usepackage{colortbl}" + "\n";
//        outputString += "\\usepackage[table*]{xcolor}" + "\n";
//        outputString += "\\xdefinecolor{gray95}{gray}{0.65}" + "\n";
//        outputString += "\\xdefinecolor{gray25}{gray}{0.8}" + "\n";
//        outputString += "\\author{Yuehong Xie}" + "\n";
//        outputString += "\\begin{document}" + "\n";
//        outputString += "\\maketitle" + "\n";
//        outputString += "\\section{Tables}" + "\n";
//
//        outputString +="\n";
//        outputString += "\\begin{table}" + "\n";
//        outputString += "\\caption{" + indicatorName + ". " + caption + "}" + "\n";
//        outputString += "\\label{table: " + indicatorName + "}" + "\n";
//        outputString += "\\centering" + "\n";
//        outputString += "\\begin{scriptsize}" + "\n";
//        outputString += "\\begin{tabular}{l";
//
//        // calculate the number of columns
//        for (int i=0;i<algrithmList.length;++i)
//            outputString += "l";
//        outputString += "}\n";
//        outputString += "\\hline";
//
//        // write table head
//        for (int i = -1; i < algrithmList.length; i++) {
//            if (i == -1) {
//                outputString += " & ";
//            } else if (i == (algrithmList.length - 1)) {
//                outputString += " " + algrithmList[i] + "\\\\" + "\n";
//            } else {
//                outputString += "" + algrithmList[i] + " & ";
//            }
//        }
//        outputString += "\\hline \n";
//        return outputString;
//    }
//
//    String endLatexString() {
//        String outputString = "";
//        // close table
//        outputString += "\\hline" + "\n";
//        outputString += "\\end{tabular}" + "\n";
//        outputString += "\\end{scriptsize}" + "\n";
//        outputString += "\\end{table}" + "\n";
//        outputString += "\\end{document}" + "\n";
//        return  outputString;
//    }
//
//    String dataToString(double[][] data, STATTYPE[] statList,String indicatorName,String caption,String[] algrithmList,boolean isLowerTheBest) {
//        String outputString = "";
//        // write lines
//        for (int i = 0; i < algrithmList.length; i++) {
//            // find the best value and second best value
//            double bestValue[] = new double[statList.length];
//            double secondValue[] = new double[statList.length];
//
//            double bestCentralTendencyValue;
//            double bestDispersionValue;
//            double secondBestCentralTendencyValue;
//            double secondBestDispersionValue;
//            int bestIndex = -1;
//            int secondBestIndex = -1;
//
//            if (isLowerTheBest) {
//                bestCentralTendencyValue = Double.MAX_VALUE;
//                bestDispersionValue = Double.MAX_VALUE;
//                secondBestCentralTendencyValue = Double.MAX_VALUE;
//                secondBestDispersionValue = Double.MAX_VALUE;
//                for (int j = 0; j < (algrithmList.length); j++) {
//                    if ((centralTendency[indicatorIndex][i][j] < bestCentralTendencyValue) ||
//                            ((centralTendency[indicatorIndex][i][j] ==
//                                    bestCentralTendencyValue) && (dispersion[indicatorIndex][i][j] < bestDispersionValue))) {
//                        secondBestIndex = bestIndex;
//                        secondBestCentralTendencyValue = bestCentralTendencyValue;
//                        secondBestDispersionValue = bestDispersionValue;
//                        bestCentralTendencyValue = centralTendency[indicatorIndex][i][j];
//                        bestDispersionValue = dispersion[indicatorIndex][i][j];
//                        bestIndex = j;
//                    } else if ((centralTendency[indicatorIndex][i][j] < secondBestCentralTendencyValue) ||
//                            ((centralTendency[indicatorIndex][i][j] ==
//                                    secondBestCentralTendencyValue) && (dispersion[indicatorIndex][i][j] < secondBestDispersionValue))) {
//                        secondBestIndex = j;
//                        secondBestCentralTendencyValue = centralTendency[indicatorIndex][i][j];
//                        secondBestDispersionValue = dispersion[indicatorIndex][i][j];
//                    }
//                }
//            } else {
//                bestCentralTendencyValue = Double.MIN_VALUE;
//                bestDispersionValue = Double.MIN_VALUE;
//                secondBestCentralTendencyValue = Double.MIN_VALUE;
//                secondBestDispersionValue = Double.MIN_VALUE;
//                for (int j = 0; j < (experiment.getAlgorithmList().size()); j++) {
//                    if ((centralTendency[indicatorIndex][i][j] > bestCentralTendencyValue) ||
//                            ((centralTendency[indicatorIndex][i][j] ==
//                                    bestCentralTendencyValue) && (dispersion[indicatorIndex][i][j] < bestDispersionValue))) {
//                        secondBestIndex = bestIndex;
//                        secondBestCentralTendencyValue = bestCentralTendencyValue;
//                        secondBestDispersionValue = bestDispersionValue;
//                        bestCentralTendencyValue = centralTendency[indicatorIndex][i][j];
//                        bestDispersionValue = dispersion[indicatorIndex][i][j];
//                        bestIndex = j;
//                    } else if ((centralTendency[indicatorIndex][i][j] > secondBestCentralTendencyValue) ||
//                            ((centralTendency[indicatorIndex][i][j] ==
//                                    secondBestCentralTendencyValue) && (dispersion[indicatorIndex][i][j] < secondBestDispersionValue))) {
//                        secondBestIndex = j;
//                        secondBestCentralTendencyValue = centralTendency[indicatorIndex][i][j];
//                        secondBestDispersionValue = dispersion[indicatorIndex][i][j];
//                    }
//                }
//            }
//
//            outputString += algrithmList[i].replace("_", "\\_") + " & ";
//            for (int j = 0; j < (algrithmList.length - 1); j++) {
//                if (j == bestIndex) {
//                    outputString += "\\cellcolor{gray95}";
//                }
//                if (j == secondBestIndex) {
//                    outputString += "\\cellcolor{gray25}";
//                }
//
//                String m = String.format(Locale.ENGLISH, "%10.2e", data[i][j]);
//                String s = String.format(Locale.ENGLISH, "%8.1e", data[i][j]);
//                outputString += "$" + m + "_{" + s + "}$ & ";
//            }
//            if (bestIndex == (algrithmList.length - 1)) {
//                outputString += "\\cellcolor{gray95}";
//            }
//            if (secondBestIndex == (algrithmList.length - 1)) {
//                outputString += "\\cellcolor{gray25}";
//            }
//            String m = String.format(Locale.ENGLISH, "%10.2e",
//                    data[i][algrithmList.length - 1]);
//            String s = String.format(Locale.ENGLISH, "%8.1e",
//                    data[i][algrithmList.length - 1]);
//            outputString += "$" + m + "_{" + s + "}$ \\\\" + "\n";
//        }
//
//        return outputString;
//    }


    public void executeMaOPSmall(){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(10)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(10)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(10)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(10)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(10)"
        };
//
        int[] popsList = {
                10, 20, 65,
                10, 20, 65,
                10, 20, 65,
                10, 20, 65,
                10, 20, 65
        };

        int[] maxIterationsList = {
                400,600,1000,
                250,350,750,
                1000,1000,1500,
                600,1000,2000,
                250,750,4000
        };

        String[] algorithmNameList = {
//                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"

        };

        int[] statList = {
                STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };

        String[] indicatorNameList2 = {
                "IGD"
                ,"IGDPlus"
//                ,
//                "HV",
//                "HE"
        };

        String baseDir2 = "D://Experiments/ResultsMaOPSmall000024/stat/";
//        String baseDir2 = "E://ResultsMaOPMeasure000012/";

        execute(baseDir2,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList2,statList);
    }


    public void executeConvexDTLZ2Test(){
        String[] problemNameList = {
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
//                ,"Convex_DTLZ2(10)","Convex_DTLZ2(15)"
        };
//
        int[] popsList = {
                91, 210, 156
//                , 275,135
        };
//        int[] popsList = {
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136,
//                109, 251, 165, 286,136
//        };
        int[] maxIterationsList = {
                250,750,2000
//                ,4000,4500
        };

//        String[] problemNameList = {
//                "DTLZ1(8)","DTLZ1(10)","DTLZ1(15)",
//                "DTLZ2(8)","DTLZ2(10)","DTLZ2(15)",
//                "DTLZ3(8)","DTLZ3(10)","DTLZ3(15)",
//                "DTLZ4(8)","DTLZ4(10)","DTLZ4(15)",
//                "Convex_DTLZ2(8)","Convex_DTLZ2(10)","Convex_DTLZ2(15)"
//        };
//
//        int[] popsList = {
//                156, 275,135,
//                156, 275,135,
//                156, 275,135,
//                156, 275,135,
//                156, 275,135
//        };
//        int[] maxIterationsList = {
//                750,1000,1500,
//                500,750,1000,
//                1000,1500,2000,
//                1250,2000,3000,
//                2000,4000,4500
//        };
        String[] algorithmNameList = {
                "NSGAIII"
 ,"MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"

//                "MOEADGR_TCH"
//                "MOEACD"
        };

        int[] statList = {
                STATTYPE.MIN.ordinal(),STATTYPE.AVG.ordinal(),STATTYPE.MEDIAM.ordinal(),STATTYPE.MAX.ordinal()
        };

        String[] indicatorNameList2 = {
//                "IGD"
//                ,
                "IGDPlus"
//                ,
//                "HV",
//                "HE"
        };

        String baseDir2 = "D://Experiments/ConvexDTLZ2Test/";

        execute(baseDir2,"Medium","stat",problemNameList,popsList,maxIterationsList,algorithmNameList,indicatorNameList2,statList);
    }


    public static void main(String[] argc){
        emergeStat computor = new emergeStat();
        //computor.executeMedium();
//        computor.executeTimeMedium();
//        computor.executeMOEACDMedium();
//        computor.executeTimeMOEACDMedium();
//        computor.executeScalarMedium();
//        computor.executeWFGMedium();
//        computor.execute1();
//        computor.execute2();
//        computor.execute3();
//        computor.execute4();
//        computor.executeMOP();
        computor.executeTimeMOP();
//        computor.executeTimeMaOP();
//        computor.executeTimeMaOPTao();
//        computor.executeMaOP();
//        computor.executeMaOPSmall();
//        computor.executeMaOPIIa();

//        computor.executeTimeMaOPIIa();
//        computor.executeConvexDTLZ2Test();
    }
}
