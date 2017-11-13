package org.uma.jmetal.experiment.dataAnalysis;

import org.apache.commons.math3.random.RandomGenerator;
import org.uma.jmetal.algorithm.multiobjective.moeacd.ConeSubRegion;
import org.uma.jmetal.experiment.MyExperimentAnalysis;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.*;
import org.uma.jmetal.problem.multiobjective.cdtlz.*;
import org.uma.jmetal.problem.multiobjective.dtlz.SDTLZ1;
import org.uma.jmetal.problem.multiobjective.mop.*;
import org.uma.jmetal.qualityindicator.impl.GeneralizedSpread;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.hypervolume.ApproximateHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.KDTree;
import org.uma.jmetal.util.fileinput.util.ReadDoubleDataFile;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.io.*;
import java.util.*;

/**
 * Created by X250 on 2016/5/6.
 */
public class analysisQuality {


    public void executeMOPMeasure(String dataDir,String statDir,int maxRun,String[] algorithmNameList,int approximateDim, int numOfSample){

        String[] problemNameList = {
            "MOP1(2)","MOP2(2)","MOP3(2)","MOP4(2)","MOP5(2)","MOP6(3)","MOP7(3)"
        };

        int[] objNumList = {2,2,2,2,2,3,3};
        int[] popsList = {
                100,100,100,100,100,300,300
        };

        int[] maxIterationsList = {
                3000,3000,3000,3000,3000,3000,3000
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "MOP1.2D.pf[4000]",
                frontDir + "MOP2.2D.pf[4000]",
                frontDir + "MOP3.2D.pf[4000]",
                frontDir + "MOP4.2D.pf[4000]",
                frontDir + "MOP5.2D.pf[4000]",
                frontDir + "MOP6.3D.pf[8001]",
                frontDir + "MOP7.3D.pf[8001]"
        };

        Point point2D = new ArrayPoint(new double[]{1.0,1.0});
        Point point3D = new ArrayPoint(new double[]{1.0,1.0,1.0});
        Point[] nadirPointList = {
                point2D,point2D,point2D,point2D,point2D,point3D,point3D
        };

        Point pointZero2D = new ArrayPoint(new double[]{0,0});
        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point[] boundPointList = {
                pointZero2D,pointZero2D,pointZero2D,pointZero2D,pointZero2D,pointZero3D,pointZero3D
        };

//        executeIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
//        executeIGDPlus(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
        executeNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);
    }

    public void executeMaOPMeasure(String dataDir,String statDir,int maxRun,String[] algorithmNameList,int approximateDim, int numOfSample){
//        String[] problemNameList = {
//                "DTLZ1(3)","DTLZ1(5)", "DTLZ1(8)","DTLZ1(10)","DTLZ1(15)"
//                ,
//                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)","DTLZ2(10)","DTLZ2(15)",
//                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)","DTLZ3(10)","DTLZ3(15)"
//                ,
//                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)", "DTLZ4(10)","DTLZ4(15)",
//                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)","Convex_DTLZ2(10)","Convex_DTLZ2(15)"
//        };
//
//        int[] objNumList = {
//                3,5,8,10,15
//                ,
//                3,5,8,10,15,
//                3,5,8,10,15,
//                3,5,8,10,15,
//                3,5,8,10,15
//        };
//        int[] popsList = {
//                91, 210, 156,275, 135
//                ,
//                91, 210, 156,275, 135,
//                91, 210, 156,275, 135,
//                91, 210, 156,275, 135,
//                91, 210, 156,275, 135
//        };
//
//        int[] maxIterationsList = {
//                400,600,750,1000,1500
//                ,
//                250,350,500,750,1000,
//                1000,1000,1000,1500,2000
//                ,
//                600,1000,1250,2000,3000,
//                250,750,2000,4000,4500
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[8001]",
//                frontDir + "DTLZ1.5D.pf[15981]",
//                frontDir + "DTLZ1.8D.pf[31824]",
//                frontDir + "DTLZ1.10D.pf[43758]",
//                frontDir + "DTLZ1.15D.pf[54264]",
//                frontDir + "DTLZ2.3D.pf[8001]",
//                frontDir + "DTLZ2.5D.pf[15981]",
//                frontDir + "DTLZ2.8D.pf[31824]",
//                frontDir + "DTLZ2.10D.pf[43758]",
//                frontDir + "DTLZ2.15D.pf[54264]",
//                frontDir + "DTLZ3.3D.pf[8001]",
//                frontDir + "DTLZ3.5D.pf[15981]",
//                frontDir + "DTLZ3.8D.pf[31824]",
//                frontDir + "DTLZ3.10D.pf[43758]",
//                frontDir + "DTLZ3.15D.pf[54264]",
//                frontDir + "DTLZ4.3D.pf[8001]",
//                frontDir + "DTLZ4.5D.pf[15981]",
//                frontDir + "DTLZ4.8D.pf[31824]",
//                frontDir + "DTLZ4.10D.pf[43758]",
//                frontDir + "DTLZ4.15D.pf[54264]",
//                frontDir + "Convex_DTLZ2.3D.pf[8001]",
//                frontDir + "Convex_DTLZ2.5D.pf[15981]",
//                frontDir + "Convex_DTLZ2.8D.pf[31824]",
//                frontDir + "Convex_DTLZ2.10D.pf[43758]",
//                frontDir + "Convex_DTLZ2.15D.pf[54264]"
//        };
//
//
//        Point point3Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5});
//        Point point3D = new ArrayPoint(new double[]{1.0, 1.0, 1.0});
//        Point point5Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5, 0.5, 0.5});
//        Point point5D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0});
//        Point point8Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5});
//        Point point8D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0});
//        Point point10Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5});
//        Point point10D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0});
//        Point point15Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5});
//        Point point15D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0});
//
//        Point[] nadirPointList = {
//                point3Dmin,point5Dmin, point8Dmin, point10Dmin,point15Dmin
//                ,
//                point3D,point5D,point8D,point10D,point15D,
//                point3D,point5D,point8D,point10D,point15D,
//                point3D,point5D,point8D,point10D,point15D,
//                point3D,point5D,point8D,point10D,point15D
//        };
//
//        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
//        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
//        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
//        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
//        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
//        Point[] boundPointList = {
//                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
//                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
//                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
//                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
//                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D
//        };

        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)",
                "DTLZ2(3)","DTLZ2(5)",
                "DTLZ3(3)","DTLZ3(5)",
                "DTLZ4(3)","DTLZ4(5)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)"
        };

        int[] objNumList = {
                3,5,
                3,5,
                3,5,
                3,5,
                3,5
        };
        int[] popsList = {
                91, 210,
                91, 210,
                91, 210,
                91, 210,
                91, 210
        };

        int[] maxIterationsList = {
                400,600,
                250,350,
                1000,1000,
                600,1000,
                250,750
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[8001]",
                frontDir + "DTLZ1.5D.pf[15981]",
                frontDir + "DTLZ2.3D.pf[8001]",
                frontDir + "DTLZ2.5D.pf[15981]",
                frontDir + "DTLZ3.3D.pf[8001]",
                frontDir + "DTLZ3.5D.pf[15981]",
                frontDir + "DTLZ4.3D.pf[8001]",
                frontDir + "DTLZ4.5D.pf[15981]",
                frontDir + "Convex_DTLZ2.3D.pf[8001]",
                frontDir + "Convex_DTLZ2.5D.pf[15981]"
        };


        Point point3Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5});
        Point point3D = new ArrayPoint(new double[]{1.0, 1.0, 1.0});
        Point point5Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5, 0.5, 0.5});
        Point point5D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0});
        Point point8Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5});
        Point point8D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0});
        Point point10Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5});
        Point point10D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0});
        Point point15Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5});
        Point point15D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0});

        Point[] nadirPointList = {
                point3Dmin,point5Dmin,
                point3D,point5D,
                point3D,point5D,
                point3D,point5D,
                point3D,point5D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero3D,pointZero5D,
                pointZero3D,pointZero5D,
                pointZero3D,pointZero5D,
                pointZero3D,pointZero5D,
                pointZero3D,pointZero5D
        };

//        executeIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
        executeNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);
    }

    public void executeMaOPMeasureIIa(String dataDir, String statDir,int maxRun,String[] algorithmNameList,int approximateDim, int numOfSample){

        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
        };

        int[] objNumList = {
                3,5,8,
                3,5,8,
                3,5,8,
                3,5,8,
                3,5,8
        };

        int[] popsList = {
                91, 210, 156,
                91, 210, 156,
                91, 210, 156,
                91, 210, 156,
                91, 210, 156
        };

//        int[] popsList = {
//                109, 251, 165,
//                109, 251, 165,
//                109, 251, 165,
//                109, 251, 165,
//                109, 251, 165
//        };

        int[] maxIterationsList = {
                400,600,750,
                250,350,500,
                1000,1000,1000,
                600,1000,1250,
                250,750,2000
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[8001]",
                frontDir + "DTLZ1.5D.pf[15981]",
                frontDir + "DTLZ1.8D.pf[31824]",
                frontDir + "DTLZ2.3D.pf[8001]",
                frontDir + "DTLZ2.5D.pf[15981]",
                frontDir + "DTLZ2.8D.pf[31824]",
                frontDir + "DTLZ3.3D.pf[8001]",
                frontDir + "DTLZ3.5D.pf[15981]",
                frontDir + "DTLZ3.8D.pf[31824]",
                frontDir + "DTLZ4.3D.pf[8001]",
                frontDir + "DTLZ4.5D.pf[15981]",
                frontDir + "DTLZ4.8D.pf[31824]",
                frontDir + "Convex_DTLZ2.3D.pf[8001]",
                frontDir + "Convex_DTLZ2.5D.pf[15981]",
                frontDir + "Convex_DTLZ2.8D.pf[31824]"
        };
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[91]",
//                frontDir + "DTLZ1.5D.pf[210]",
//                frontDir + "DTLZ1.8D.pf[156]",
//                frontDir + "DTLZ2.3D.pf[91]",
//                frontDir + "DTLZ2.5D.pf[210]",
//                frontDir + "DTLZ2.8D.pf[156]",
//                frontDir + "DTLZ3.3D.pf[91]",
//                frontDir + "DTLZ3.5D.pf[210]",
//                frontDir + "DTLZ3.8D.pf[156]",
//                frontDir + "DTLZ4.3D.pf[91]",
//                frontDir + "DTLZ4.5D.pf[210]",
//                frontDir + "DTLZ4.8D.pf[156]",
//                frontDir + "Convex_DTLZ2.3D.pf[91]",
//                frontDir + "Convex_DTLZ2.5D.pf[210]",
//                frontDir + "Convex_DTLZ2.8D.pf[156]"
//        };
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[109]",
//                frontDir + "DTLZ1.5D.pf[251]",
//                frontDir + "DTLZ1.8D.pf[165]",
//                frontDir + "DTLZ2.3D.pf[109]",
//                frontDir + "DTLZ2.5D.pf[251]",
//                frontDir + "DTLZ2.8D.pf[165]",
//                frontDir + "DTLZ2.3D.pf[109]",
//                frontDir + "DTLZ2.5D.pf[251]",
//                frontDir + "DTLZ2.8D.pf[165]",
//                frontDir + "DTLZ2.3D.pf[109]",
//                frontDir + "DTLZ2.5D.pf[251]",
//                frontDir + "DTLZ2.8D.pf[165]",
//                frontDir + "Convex_DTLZ2.3D.pf[109]",
//                frontDir + "Convex_DTLZ2.5D.pf[251]",
//                frontDir + "Convex_DTLZ2.8D.pf[165]"
//        };

//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[15151]",
//                frontDir + "DTLZ1.5D.pf[25081]",
//                frontDir + "DTLZ1.8D.pf[43713]",
//                frontDir + "DTLZ2.3D.pf[15151]",
//                frontDir + "DTLZ2.5D.pf[25081]",
//                frontDir + "DTLZ2.8D.pf[43713]",
//                frontDir + "DTLZ2.3D.pf[15151]",
//                frontDir + "DTLZ2.5D.pf[25081]",
//                frontDir + "DTLZ2.8D.pf[43713]",
//                frontDir + "DTLZ2.3D.pf[15151]",
//                frontDir + "DTLZ2.5D.pf[25081]",
//                frontDir + "DTLZ2.8D.pf[43713]",
//                frontDir + "Convex_DTLZ2.3D.pf[15151]",
//                frontDir + "Convex_DTLZ2.5D.pf[25081]",
//                frontDir + "Convex_DTLZ2.8D.pf[43713]"
//        };

//        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
//        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
//        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});

//        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
//        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
//        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
//        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
//        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
//        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});
        Point point3Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point5Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55, 0.55, 0.55});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point10Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point15Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});

        Point[] hvRefPointList = {
                point3Dmin,point5Dmin, point8Dmin,
                point3D,point5D, point8D,
                point3D,point5D, point8D,
                point3D,point5D, point8D,
                point3D,point5D, point8D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D
        };


//        double[] hvCubeList = {
//                1330.9791666666666666667,
//                0.99973958333333333333,
//                0.99999990311879960317460317460317,
//
////
//                1330.4764012244017011269228927694534,
//                31.835506593315177356352758483335,
//                255.9841456557561844991477147896,
//
//
//                1330.4764012244017011269228927694534,
//                31.835506593315177356352758483335,
//                255.9841456557561844991477147896,
//
//
//                1330.4764012244017011269228927694534,
//                31.835506593315177356352758483335,
//                255.9841456557561844991477147896,
//
//
//                1330.9666666666666666666666666666667,
//                31.999955908289241622574955908289,
//                255.99999997944442388886833331278
//
//        };
//
//        double[] hvCubeList = {
//                0.322166666666666601237523082091,
//                0.167809583333333262045172773469,
//                0.0576479131187995763596632059489,
////                0.0282475246308855378052360407537,
////                0.00474756150994297236589414268337,
//
//                0.807401224401701478328163830156,
//                1.44601659331517806705846851401,
//                2.12773446575618585541178617859,
////                2.59125206552980946739239698218,
////                4.177236528690534\374558916402748,
//
//                0.807401224401701478328163830156,
//                1.44601659331517806705846851401,
//                2.12773446575618585541178617859,
////                2.59125206552980946739239698218,
////                4.177236528690534\374558916402748,
//
//                0.807401224401701478328163830156,
//                1.44601659331517806705846851401,
//                2.12773446575618585541178617859,
////                2.59125206552980946739239698218,
////                4.177236528690534\374558916402748,
//
//                1.33100000000000040500935938326,
//                1.61051000000000077427841915778,
//                2.14358881000000156547002916341
////                ,
////                2.59374246010000231166259254678,
////                4.17724816941565624262011624523
//        };
        double[] hvCubeList = {
//                0.322166666666666601237523082091,
//                0.167809583333333262045172773469,
//                0.0576479131187995763596632059489,
//                0.0282475246308855378052360407537,
//                0.00474756150994297236589414268337,
                0.14554166666666670000,
                0.05006802083333336000,
                0.00837329690786210900,
//                0.00253295135207696300,
//                0.00012747949735763220,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
//                2.59125206552980946739,
//                4.17723652869053437456,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
//                2.59125206552980946739,
//                4.17723652869053437456,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
//                2.59125206552980946739,
//                4.17723652869053437456,

                1.33100000000000040501,
                1.61051000000000077428,
                2.14358881000000156547
//                ,
//                2.59374246010000231166,
//                4.17724816941565624262
        };

//        String[] problemNameList = {
//                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
//        };
//
//        int[] objNumList = {
//                3,5,8
//        };
//
//        int[] popsList = {
//                109, 251, 165
//        };
//        int[] maxIterationsList = {
//                250,750,2000
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//
//        String[] frontFileList = {
//
//                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//                frontDir + "Convex_DTLZ2.5D.pf[14950]",
//                frontDir + "Convex_DTLZ2.8D.pf[31824]"
//        };
////        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
////        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
////        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
////        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
////        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
////        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
////        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//
//        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
//        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
//        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
//        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
//        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
//        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});
//
//        Point[] hvRefPointList = {
//                point3D,point5D, point8D
//        };
//
//        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
//        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
//        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
//        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
//        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
//        Point[] boundPointList = {
//                pointZero3D,pointZero5D,pointZero8D
//        };
//
//
//        double[] hvCubeList = {
//
//
//                1.33100000000000040500935938326,
//                1.61051000000000077427841915778,
//                2.14358881000000156547002916341
////                ,
////                2.59374246010000231166259254678,
////                4.17724816941565624262011624523
//        };

//        String[] algorithmNameList = {
//                //"MOEACDPBI","MOEACDPBIS","MOEACDAPBI","MOEACDFPBI","MOEACDFAPBI",
////                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"
////                ,
////                "MOEACDPBI","MOEACDPBIS","MOEACDPBIAN","MOEACDAPBI","MOEACDFPBI","MOEACDFAPBI","MOEACDFBA","MOEACDLHV",
////                "MOEACD"
////                ,
////                "MOEACDN"
////                ,
//        };

//        String dataDir = "D://Experiments/ResultsMaOPMeasure11/";
//        String statDir = "D://Experiments/ResultsMaOPMeasure11/";
//        String dataDir = "E://ResultsMaOPMeasure15/";
//        String statDir = "E://ResultsMaOPMeasure15/";
//        int numOfSample = 1000000;
//        int approximateDim = 10;
//        int maxRun = 20;
//        executeIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
//        executeIGDPlus(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
        executeHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
    }

    public void executeMaOPMeasureIIb(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "DTLZ1(10)","DTLZ1(15)",
                "DTLZ2(10)","DTLZ2(15)",
                "DTLZ3(10)","DTLZ3(15)",
                "DTLZ4(10)","DTLZ4(15)",
                "Convex_DTLZ2(10)","Convex_DTLZ2(15)"
        };

        int[] objNumList = {
                10,15,
                10,15,
                10,15,
                10,15,
                10,15
        };

//        int[] popsList = {
//                286,136,
//                286,136,
//                286,136,
//                286,136,
//                286,136
//        };
        int[] popsList = {
                275, 135,
                275, 135,
                275, 135,
                275, 135,
                275, 135
        };

        int[] maxIterationsList = {
                1000,1500,
                750,1000,
                1500,2000,
                2000,3000,
                4000,4500
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////
        String[] frontFileList = {
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ1.15D.pf[54264]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ2.15D.pf[54264]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ3.15D.pf[54264]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "DTLZ4.15D.pf[54264]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.15D.pf[54264]"
        };
//        String[] frontFileList = {
//                frontDir + "DTLZ1.10D.pf[275]",
//                frontDir + "DTLZ1.15D.pf[135]",
//                frontDir + "DTLZ2.10D.pf[275]",
//                frontDir + "DTLZ2.15D.pf[135]",
//                frontDir + "DTLZ3.10D.pf[275]",
//                frontDir + "DTLZ3.15D.pf[135]",
//                frontDir + "DTLZ4.10D.pf[275]",
//                frontDir + "DTLZ4.15D.pf[135]",
//                frontDir + "Convex_DTLZ2.10D.pf[275]",
//                frontDir + "Convex_DTLZ2.15D.pf[135]"
//        };

//                String[] frontFileList = {
//                frontDir + "DTLZ1.10D.pf[286]",
//                frontDir + "DTLZ1.15D.pf[136]",
//                frontDir + "DTLZ2.10D.pf[286]",
//                frontDir + "DTLZ2.15D.pf[136]",
//                frontDir + "DTLZ2.10D.pf[286]",
//                frontDir + "DTLZ2.15D.pf[136]",
//                frontDir + "DTLZ2.10D.pf[286]",
//                frontDir + "DTLZ2.15D.pf[136]",
//                frontDir + "Convex_DTLZ2.10D.pf[286]",
//                frontDir + "Convex_DTLZ2.15D.pf[136]"
//        };
//        String[] frontFileList = {
//                frontDir + "DTLZ1.10D.pf[92378]",
//                frontDir + "DTLZ1.15D.pf[170544]",
//                frontDir + "DTLZ2.10D.pf[92378]",
//                frontDir + "DTLZ2.15D.pf[170544]",
//                frontDir + "DTLZ2.10D.pf[92378]",
//                frontDir + "DTLZ2.15D.pf[170544]",
//                frontDir + "DTLZ2.10D.pf[92378]",
//                frontDir + "DTLZ2.15D.pf[170544]",
//                frontDir + "Convex_DTLZ2.10D.pf[92378]",
//                frontDir + "Convex_DTLZ2.15D.pf[170544]"
//        };
//
//        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
//        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
//        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
//        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
//        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
//        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
//        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
//        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});
        Point point3Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point5Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55, 0.55, 0.55});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point10Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point15Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});

        Point[] hvRefPointList = {
                point10Dmin,point15Dmin,
                point10D,point15D,
                point10D,point15D,
                point10D,point15D,
                point10D,point15D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D
        };


//        double[] hvCubeList = {
////                0.99999999973088555445326278659612,
////                0.99999999999999997666270833795222,
////
////                1023.9975096054298072798399842016,
////                32767.999988359274877218491124947,
////
////                1023.9975096054298072798399842016,
////                32767.999988359274877218491124947,
////
////                1023.9975096054298072798399842016,
////                32767.999988359274877218491124947,
////
////
////                1023.9999999999999957910347537283,
////                32768 - 1.8530243193155939420665879882856e-27
//
//
//                0.0282475246308855378052360407537,
//                0.00474756150994297236589414268337,
//
//                2.59125206552980946739239698218,
//                4.177236528690534374558916402748,
//
//                2.59125206552980946739239698218,
//                4.177236528690534374558916402748,
//
//                2.59125206552980946739239698218,
//                4.177236528690534374558916402748,
//
//                2.59374246010000231166259254678,
//                4.17724816941565624262011624523
//        };
        double[] hvCubeList = {
//                0.322166666666666601237523082091,
//                0.167809583333333262045172773469,
//                0.0576479131187995763596632059489,
//                0.0282475246308855378052360407537,
//                0.00474756150994297236589414268337,
//                0.14554166666666670000,
//                0.05006802083333336000,
//                0.00837329690786210900,
                0.00253295135207696300,
                0.00012747949735763220,

//                0.80740122440170147833,
//                1.44601659331517806706,
//                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

//                0.80740122440170147833,
//                1.44601659331517806706,
//                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

//                0.80740122440170147833,
//                1.44601659331517806706,
//                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

//                1.33100000000000040501,
//                1.61051000000000077428,
//                2.14358881000000156547,
                2.59374246010000231166,
                4.17724816941565624262
        };
//        String[] problemNameList = {
//
//                "Convex_DTLZ2(10)","Convex_DTLZ2(15)"
//        };
//
//        int[] objNumList = {
//
//                10,15
//        };
//
//        int[] popsList = {
//
//                286,136
//        };
//        int[] maxIterationsList = {
//
//                4000,4500
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//
//        String[] frontFileList = {
//                frontDir + "Convex_DTLZ2.10D.pf[43758]",
//                frontDir + "Convex_DTLZ2.15D.pf[54264]"
//        };
//
//        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
//        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
//        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
//        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
//        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
//        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});
//
//        Point[] hvRefPointList = {
//                point10D,point15D
//        };
//
//        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
//        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
//        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
//        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
//        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
//        Point[] boundPointList = {
//                pointZero10D,pointZero15D
//        };
//
//
//        double[] hvCubeList = {
//                2.59374246010000231166259254678,
//                4.17724816941565624262011624523
//        };


//        String[] algorithmNameList = {
//                //"MOEACDPBI","MOEACDPBIS","MOEACDAPBI","MOEACDFPBI","MOEACDFAPBI",
////                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"
////                ,
////                "MOEACDPBI","MOEACDPBIS","MOEACDPBIAN","MOEACDAPBI","MOEACDFPBI","MOEACDFAPBI","MOEACDFBA","MOEACDLHV",
////                "MOEACD"
////                ,
////                "MOEACDN"
////                ,
//        };
//        String dataDir = "D://Experiments/ResultsMaOPMeasure11/";
//        String statDir = "D://Experiments/ResultsMaOPMeasure11/";
//        String dataDir = "E://ResultsMaOPMeasure15/";
//        String statDir = "E://ResultsMaOPMeasure15/";
//        int numOfSample = 1000000;
//        int approximateDim = 10;
//        int maxRun = 20;
//        executeIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
//        executeIGDPlus(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
        executeHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
    }

    public void executeMaOPScale(String dataDir,String statDir,int maxRun,String[] algorithmNameList,int approximateDim, int numOfSample){

        boolean isHV = true;
        boolean isIGD = true;
        boolean isIGDPlus = true;
        String[] problemNameList = {
                "SDTLZ1(3)","SDTLZ1(5)","SDTLZ1(8)",
                "SDTLZ1(10)","SDTLZ1(15)"
                ,
                "SDTLZ2(3)","SDTLZ2(5)","SDTLZ2(8)","SDTLZ2(10)","SDTLZ2(15)"
                ,
                "SDTLZ3(3)","SDTLZ3(5)","SDTLZ3(8)","SDTLZ3(10)","SDTLZ3(15)",
                "SDTLZ4(3)","SDTLZ4(5)","SDTLZ4(8)","SDTLZ4(10)","SDTLZ4(15)",
                "Convex_SDTLZ2(3)","Convex_SDTLZ2(5)","Convex_SDTLZ2(8)","Convex_SDTLZ2(10)","Convex_SDTLZ2(15)"
        };

        int[] popsList = {
                91, 210, 156,
                275,135,
                91, 210, 156, 275,135
                ,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135
        };

        int[] maxIterationsList = {
                400,600,750,
                1000,1500
                ,
                250,350,500,750,1000
                ,
                1000,1000,1000,1500,2000,
                600,1000,1250,2000,3000,
                250,750,2000,4000,4500
        };
//
//        String[] algorithmNameList = {
////                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI",

//        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";

//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[91]",
//                frontDir + "DTLZ1.5D.pf[210]",
//                frontDir + "DTLZ1.8D.pf[156]",
//                frontDir + "DTLZ1.10D.pf[275]",
//                frontDir + "DTLZ1.15D.pf[135]",
//                frontDir + "DTLZ2.3D.pf[91]",
//                frontDir + "DTLZ2.5D.pf[210]",
//                frontDir + "DTLZ2.8D.pf[156]",
//                frontDir + "DTLZ2.10D.pf[275]",
//                frontDir + "DTLZ2.15D.pf[135]"
////                ,
////                frontDir + "DTLZ3.3D.pf[91]",
////                frontDir + "DTLZ3.5D.pf[210]",
////                frontDir + "DTLZ3.8D.pf[156]",
////                frontDir + "DTLZ3.10D.pf[275]",
////                frontDir + "DTLZ3.15D.pf[135]",
////                frontDir + "DTLZ4.3D.pf[91]",
////                frontDir + "DTLZ4.5D.pf[210]",
////                frontDir + "DTLZ4.8D.pf[156]",
////                frontDir + "DTLZ4.10D.pf[275]",
////                frontDir + "DTLZ4.15D.pf[135]",
////                frontDir + "Convex_DTLZ2.3D.pf[91]",
////                frontDir + "Convex_DTLZ2.5D.pf[210]",
////                frontDir + "Convex_DTLZ2.8D.pf[156]",
////                frontDir + "Convex_DTLZ2.10D.pf[275]",
////                frontDir + "Convex_DTLZ2.15D.pf[135]"
//        };
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[8001]",
                frontDir + "DTLZ1.5D.pf[15981]",
                frontDir + "DTLZ1.8D.pf[31824]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ1.15D.pf[54264]"
                ,
                frontDir + "DTLZ2.3D.pf[8001]",
                frontDir + "DTLZ2.5D.pf[15981]",
                frontDir + "DTLZ2.8D.pf[31824]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ2.15D.pf[54264]"
                ,
                frontDir + "DTLZ3.3D.pf[8001]",
                frontDir + "DTLZ3.5D.pf[15981]",
                frontDir + "DTLZ3.8D.pf[31824]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ3.15D.pf[54264]",
                frontDir + "DTLZ4.3D.pf[8001]",
                frontDir + "DTLZ4.5D.pf[15981]",
                frontDir + "DTLZ4.8D.pf[31824]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "DTLZ4.15D.pf[54264]",
                frontDir + "Convex_DTLZ2.3D.pf[8001]",
                frontDir + "Convex_DTLZ2.5D.pf[15981]",
                frontDir + "Convex_DTLZ2.8D.pf[31824]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.15D.pf[54264]"
        };


//        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
//        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
//        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
//        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
//        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
//        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});
        Point point3Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point5Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55, 0.55, 0.55});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point10Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point15Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});


        Point[] hvRefPointList = {
//                point3Dmin,point5Dmin, point8Dmin,point10Dmin,point15Dmin,
                point3D,point5D, point8D,
                point10D,point15D
                ,

                point3D,point5D, point8D,point10D,point15D
                ,
                point3D,point5D, point8D,point10D,point15D,
                point3D,point5D, point8D,point10D,point15D,
                point3D,point5D, point8D,point10D,point15D
        };

        Point IdealPoint3D = new ArrayPoint(new double[]{0.0,0.0,0.0});
        Point IdealPoint5D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint8D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint10D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint15D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});
        Point[] idealpointList = {
                IdealPoint3D,IdealPoint5D,IdealPoint8D,
                IdealPoint10D,IdealPoint15D
                ,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D
                ,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D
        };

        Point NadirPoint3DMin = new ArrayPoint(new double[]{0.5,0.5,0.5});
        Point NadirPoint5DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5});
        Point NadirPoint8DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5});
        Point NadirPoint10DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5});
        Point NadirPoint15DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5});

        Point NadirPoint3D = new ArrayPoint(new double[]{1.0,1.0,1.0});
        Point NadirPoint5D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0});
        Point NadirPoint8D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0});
        Point NadirPoint10D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0});
        Point NadirPoint15D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0});

        for(int i=0;i<3;i++){
            NadirPoint3DMin.setDimensionValue(i,NadirPoint3DMin.getDimensionValue(i)*Math.pow(10.0,i));
            NadirPoint3D.setDimensionValue(i,NadirPoint3D.getDimensionValue(i)*Math.pow(10.0,i));
        }
        for(int i=0;i<5;i++){
            NadirPoint5DMin.setDimensionValue(i,NadirPoint5DMin.getDimensionValue(i)*Math.pow(10.0,i));
            NadirPoint5D.setDimensionValue(i,NadirPoint5D.getDimensionValue(i)*Math.pow(10.0,i));
        }
        for(int i=0;i<8;i++){
            NadirPoint8DMin.setDimensionValue(i,NadirPoint8DMin.getDimensionValue(i)*Math.pow(3.0,i));
            NadirPoint8D.setDimensionValue(i,NadirPoint8D.getDimensionValue(i)*Math.pow(3.0,i));
        }
        for(int i=0;i<10;i++){
//            NadirPoint10DMin.setDimensionValue(i,NadirPoint10DMin.getDimensionValue(i)*Math.pow(2.0,i));
            NadirPoint10DMin.setDimensionValue(i,NadirPoint10DMin.getDimensionValue(i)*Math.pow(3.0,i));

            NadirPoint10D.setDimensionValue(i,NadirPoint10D.getDimensionValue(i)*Math.pow(3.0,i));
        }
        for(int i=0;i<15;i++){
//            NadirPoint15DMin.setDimensionValue(i,NadirPoint15DMin.getDimensionValue(i)*Math.pow(1.2,i));
            NadirPoint15DMin.setDimensionValue(i,NadirPoint15DMin.getDimensionValue(i)*Math.pow(2.0,i));
            NadirPoint15D.setDimensionValue(i,NadirPoint15D.getDimensionValue(i)*Math.pow(2.0,i));
        }

        Point[] NadirPointsList = {
                NadirPoint3DMin,NadirPoint5DMin,NadirPoint8DMin,
                NadirPoint10DMin,NadirPoint15DMin
                ,
                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D
                ,
                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D,
                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D,
                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D
        };

        int[] objNumList = new int[]{
                3,5,8,
                10,15
                ,
                3,5,8,10,15
                ,
                3,5,8,10,15,
                3,5,8,10,15,
                3,5,8,10,15
        };

        Point[] boundPointList = {
                IdealPoint3D,IdealPoint5D,IdealPoint8D,
                IdealPoint10D,IdealPoint15D
                ,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D
                ,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D
        };
//        double[] hvCubeList = {
//                0.322166666666666601237523082091,
//                0.167809583333333262045172773469,
//                0.0576479131187995763596632059489,
//                0.0282475246308855378052360407537,
//                0.00474756150994297236589414268337,
//
//                0.807401224401701478328163830156,
//                1.44601659331517806705846851401,
//                2.12773446575618585541178617859,
//                2.59125206552980946739239698218,
//                4.177236528690534374558916402748
//        };
        double[] hvCubeList = {
//                0.322166666666666601237523082091,
//                0.167809583333333262045172773469,
//                0.0576479131187995763596632059489,
//                0.0282475246308855378052360407537,
//                0.00474756150994297236589414268337,
                0.14554166666666670000,
                0.05006802083333336000,
                0.00837329690786210900,
                0.00253295135207696300,
                0.00012747949735763220,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                1.33100000000000040501,
                1.61051000000000077428,
                2.14358881000000156547,
                2.59374246010000231166,
                4.17724816941565624262
        };

        try {
            WFGHypervolume hvIndicator = new WFGHypervolume();
            ApproximateHypervolume ahvIndicator = new ApproximateHypervolume();
            ahvIndicator.setMiniming();
            MyExperimentAnalysis analysis = new MyExperimentAnalysis(dataDir,"");
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                if(isHV) {
                    if (objNumList[iProblem] >= approximateDim) {
                        ahvIndicator.setSamplePoints(numOfSample, hvRefPointList[iProblem], boundPointList[iProblem]);
                    }
                }
                Front referenceFront = new ArrayFront(frontFileList[iProblem]);
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Double> igd = new Vector<>(maxRun);
                    Vector<Double> igdplus = new Vector<>(maxRun);
                    Vector<Double> hv = new Vector<>(maxRun);
                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        String solutionPF = dataDir+"/POF/" + instance + "R" + kRun + ".pof";
                        WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);

                        for(int p=0;p< solutionFront.getNumberOfPoints();++p){
                            Point point = solutionFront.getPoint(p);
                            for(int q = 0;q<solutionFront.getPointDimensions();++q){
                                point.setDimensionValue(q,(point.getDimensionValue(q) - idealpointList[iProblem].getDimensionValue(q))/(NadirPointsList[iProblem].getDimensionValue(q)-idealpointList[iProblem].getDimensionValue(q)));
                            }
                            solutionFront.setPoint(p,point);
                        }

                        if(isIGD) {
                            InvertedGenerationalDistance igdIndicator = new InvertedGenerationalDistance();
                            double igdV = igdIndicator.invertedGenerationalDistance(solutionFront, referenceFront);
                            igd.add(igdV);
                        }
                        if(isIGDPlus) {
                            InvertedGenerationalDistancePlus igdPlusIndicator = new InvertedGenerationalDistancePlus();
                            double igdPlusV = igdPlusIndicator.invertedGenerationalDistancePlus(solutionFront, referenceFront);
                            igdplus.add(igdPlusV);
                        }
                        if(isHV) {
                            double hvV = 0.0;
                            if (objNumList[iProblem] < approximateDim) {
                                hvV = hvIndicator.evaluate(solutionFront, hvRefPointList[iProblem]);
                            } else {
//                                ahvIndicator = new ApproximateHypervolume();
//                                ahvIndicator.setSamplePoints(numOfSample,hvRefPointList[iProblem],boundPointList[iProblem]);
                                hvV = ahvIndicator.evaluate(solutionFront, hvRefPointList[iProblem]);
                            }
                            hv.add(hvV);
                        }
//                        JMetalLogger.logger.info("["+kRun+"]  IGD : "+ igdV  + "   HV: "+hvV);
                    }
                    Vector<Double> finalIGD = new Vector();
                    if(isIGD) {
                        for (int kRun = 0; kRun < maxRun; ++kRun)
                            finalIGD.add(igd.get(kRun));
                    }
                    int minIdx;
                    int maxIdx;
                    double median;
                    Vector statIGD = new Vector();
                    if(isIGD) {
                        minIdx = analysis.getMinRun(igd);
                        statIGD.add(minIdx);
                        statIGD.add(analysis.getMin(igd, minIdx));
                        maxIdx = analysis.getMaxRun(igd);
                        statIGD.add(maxIdx);
                        statIGD.add(analysis.getMax(igd, maxIdx));
                        statIGD.add(analysis.getAverage(igd));
                        Collections.sort(igd);
                        median = ((Double) igd.get((igd.size() - 1) / 2)).doubleValue();
                        if (igd.size() % 2 == 0) {
                            median += ((Double) igd.get(igd.size() / 2));
                            median *= 0.5;
                        }
                        statIGD.add(median);
                    }

                    Vector<Double> finalIGDPlus = new Vector();
                    if(isIGDPlus) {
                        for (int kRun = 0; kRun < maxRun; ++kRun)
                            finalIGDPlus.add(igdplus.get(kRun));
                    }
                    Vector statIGDPlus = new Vector();
                    if(isIGDPlus) {
                        minIdx = analysis.getMinRun(igdplus);
                        statIGDPlus.add(minIdx);
                        statIGDPlus.add(analysis.getMin(igdplus, minIdx));
                        maxIdx = analysis.getMaxRun(igdplus);
                        statIGDPlus.add(maxIdx);
                        statIGDPlus.add(analysis.getMax(igdplus, maxIdx));
                        statIGDPlus.add(analysis.getAverage(igdplus));
                        Collections.sort(igdplus);
                        median = ((Double) igdplus.get((igdplus.size() - 1) / 2)).doubleValue();
                        if (igdplus.size() % 2 == 0) {
                            median += ((Double) igdplus.get(igdplus.size() / 2));
                            median *= 0.5;
                        }
                        statIGDPlus.add(median);
                    }
                    Vector<Double> finalHV = new Vector();
                    if(isHV) {
                        for (int kRun = 0; kRun < maxRun; ++kRun)
                            finalHV.add(hv.get(kRun));
                    }
                    Vector statHV = new Vector();
                    if(isHV) {
                        minIdx = analysis.getMinRun(hv);
                        statHV.add(minIdx);
                        statHV.add(analysis.getMin(hv, minIdx));
                        maxIdx = analysis.getMaxRun(hv);
                        statHV.add(maxIdx);
                        statHV.add(analysis.getMax(hv, maxIdx));
                        statHV.add(analysis.getAverage(hv));
                        Collections.sort(hv);
                        median = ((Double) hv.get((hv.size() - 1) / 2)).doubleValue();
                        if (igd.size() % 2 == 0) {
                            median += ((Double) hv.get(hv.size() / 2));
                            median *= 0.5;
                        }
                        statHV.add(median);
                    }
                    Vector<Double> finalHE = new Vector();
                    if(isHV) {
                        for (int kRun = 0; kRun < maxRun; ++kRun)
                            finalHE.add(hvCubeList[iProblem] - finalHV.get(kRun));
                    }
                    Vector statHE= new Vector();
                    if(isHV) {
                        statHE.add(statHV.get(2));
                        statHE.add(hvCubeList[iProblem] - ((Double) statHV.get(3)).doubleValue());
                        statHE.add(statHE.get(0));
                        statHE.add(hvCubeList[iProblem] - ((Double) statHV.get(1)).doubleValue());
                        statHE.add(hvCubeList[iProblem] - ((Double) statHV.get(4)).doubleValue());
                        statHE.add(hvCubeList[iProblem] - ((Double) statHV.get(5)).doubleValue());
                    }
                    if(isIGD)
                        JMetalLogger.logger.info("IGD :\t[min  "+statIGD.get(0)+"]"+statIGD.get(1)+"\t[avg]"+statIGD.get(4)+"\t[median]"+statIGD.get(5)+"\t[max "+statIGD.get(2)+"]"+statIGD.get(3));
                    if(isIGDPlus)
                        JMetalLogger.logger.info("IGDPlus :\t[min  "+statIGDPlus.get(0)+"]"+statIGDPlus.get(1)+"\t[avg]"+statIGDPlus.get(4)+"\t[median]"+statIGDPlus.get(5)+"\t[max "+statIGDPlus.get(2)+"]"+statIGDPlus.get(3));
                    if(isHV) {
                        JMetalLogger.logger.info("HV :\t[min  " + statHV.get(0) + "]" + statHV.get(1) + "\t[avg]" + statHV.get(4) + "\t[median]" + statHV.get(5) + "\t[max " + statHV.get(2) + "]" + statHV.get(3));
                        JMetalLogger.logger.info("HE :\t[min  " + statHE.get(0) + "]" + statHE.get(1) + "\t[avg]" + statHE.get(4) + "\t[median]" + statHE.get(5) + "\t[max " + statHE.get(2) + "]" + statHE.get(3));
                    }

                    try {
                        Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                        Vector<Vector<Double>> totalIGD = new Vector<Vector<Double>>(maxRun);

                        Vector<Vector<Double>> totalIGDPlus = new Vector<Vector<Double>>(maxRun);
                        Vector<Vector<Double>> totalHV = new Vector<>(maxRun);
                        Vector<Vector<Double>> totalHE = new Vector<>(maxRun);
                        for(int kRun = 0;kRun<maxRun;kRun++){
                            Vector<Integer> gen = new Vector();
                            gen.add(maxIterationsList[iProblem]);
                            Generates.add(gen);
                            if(isIGD) {
                                Vector<Double> igdRun = new Vector();
                                igdRun.add(finalIGD.get(kRun));
                                totalIGD.add(igdRun);
                            }
                            if(isIGDPlus) {
                                Vector<Double> igdPlusRun = new Vector();
                                igdPlusRun.add(finalIGDPlus.get(kRun));
                                totalIGDPlus.add(igdPlusRun);
                            }
                            if(isHV) {
                                Vector<Double> hvRun = new Vector();
                                hvRun.add(finalHV.get(kRun));
                                totalHV.add(hvRun);
                                Vector<Double> heRun = new Vector();
                                heRun.add(finalHE.get(kRun));
                                totalHE.add(heRun);
                            }
                        }
                        if(isIGD) {
                            BufferedWriter writerIGD = new DefaultFileOutputContext(statDir + "/IGD/" + instance + ".csv").getFileWriter();
                            analysis.saveIndicators(Generates, totalIGD, writerIGD);
                            writerIGD.close();
                            BufferedWriter writerStatIGD = new DefaultFileOutputContext(statDir + "/IGD/stat_" + instance + ".csv").getFileWriter();
                            analysis.saveIndicator(statIGD, writerStatIGD);
                            writerStatIGD.close();
                            BufferedWriter writerFinalIGD = new DefaultFileOutputContext(statDir + "/IGD/final_" + instance + ".csv").getFileWriter();
                            analysis.saveIndicator(finalIGD, writerFinalIGD);
                            writerFinalIGD.close();
                        }
                        if(isIGDPlus) {
                            BufferedWriter writerIGDPlus = new DefaultFileOutputContext(statDir + "/IGDPlus/" + instance + ".csv").getFileWriter();
                            analysis.saveIndicators(Generates, totalIGDPlus, writerIGDPlus);
                            writerIGDPlus.close();
                            BufferedWriter writerStatIGDPlus = new DefaultFileOutputContext(statDir + "/IGDPlus/stat_" + instance + ".csv").getFileWriter();
                            analysis.saveIndicator(statIGDPlus, writerStatIGDPlus);
                            writerStatIGDPlus.close();
                            BufferedWriter writerFinalIGDPlus = new DefaultFileOutputContext(statDir + "/IGDPlus/final_" + instance + ".csv").getFileWriter();
                            analysis.saveIndicator(finalIGDPlus, writerFinalIGDPlus);
                            writerFinalIGDPlus.close();
                        }
                        if(isHV) {
                            BufferedWriter writerHV = new DefaultFileOutputContext(statDir + "/HV/" + instance + ".csv").getFileWriter();
                            analysis.saveIndicators(Generates, totalHV, writerHV);
                            writerHV.close();
                            BufferedWriter writerStatHV = new DefaultFileOutputContext(statDir + "/HV/stat_" + instance + ".csv").getFileWriter();
                            analysis.saveIndicator(statHV, writerStatHV);
                            writerStatHV.close();
                            BufferedWriter writerFinalHV = new DefaultFileOutputContext(statDir + "/HV/final_" + instance + ".csv").getFileWriter();
                            analysis.saveIndicator(finalHV, writerFinalHV);
                            writerFinalHV.close();
                            BufferedWriter writerHE = new DefaultFileOutputContext(statDir + "/HE/" + instance + ".csv").getFileWriter();
                            analysis.saveIndicators(Generates, totalHE, writerHE);
                            writerHE.close();
                            BufferedWriter writerStatHE = new DefaultFileOutputContext(statDir + "/HE/stat_" + instance + ".csv").getFileWriter();
                            analysis.saveIndicator(statHE, writerStatHE);
                            writerStatHE.close();
                            BufferedWriter writerFinalHE = new DefaultFileOutputContext(statDir + "/HE/final_" + instance + ".csv").getFileWriter();
                            analysis.saveIndicator(finalHE, writerFinalHE);
                            writerFinalHE.close();
                        }
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }

    public void executeMaOPTaoMeasure(){

        String[] problemNameList = {
                "DTLZ1(8)",
                "DTLZ1(10)","DTLZ1(15)",
                "DTLZ2(8)",
                "DTLZ2(10)","DTLZ2(15)",
                "DTLZ3(8)",
                "DTLZ3(10)","DTLZ3(15)",
                "DTLZ4(8)",
                "DTLZ4(10)","DTLZ4(15)",
                "Convex_DTLZ2(8)",
                "Convex_DTLZ2(10)","Convex_DTLZ2(15)"
        };

        int[] objNumList = {
                8,
                10,15,
                8,10,15,
                8,10,15,
                8,10,15,
                8,10,15
        };

        int[] popsList = {
                156, 275,135,
                156, 275,135,
                156, 275,135,
                156, 275,135,
                156, 275,135
        };
//        int[] popsList = {
//                165, 286,136,
//                165, 286,136,
//                165, 286,136,
//                165, 286,136,
//                165, 286,136
//        };

        int[] maxIterationsList = {
                750,1000,1500,
                500,750,1000,
                1000,1500,2000,
                1250,2000,3000,
                2000,4000,4500
        };
        int[][] divisionConfigList = {
                {0,1,2,3},{0,1,2,3},{0,1,2},
                {0,1,2,3},{0,1,2,3},{0,1,2},
                {0,1,2,3},{0,1,2,3},{0,1,2},
                {0,1,2,3},{0,1,2,3},{0,1,2},
                {0,1,2,3},{0,1,2,3},{0,1,2},
        };

        double[][] tauConfigList = {
                {0.0, 1.0/3.0,2.0 / 3.0, 1.0}, {0.0, 1.0/3.0,2.0 / 3.0, 1.0},{0.0, 1.0/2.0, 1.0},
                {0.0, 1.0/3.0,2.0 / 3.0, 1.0}, {0.0, 1.0/3.0,2.0 / 3.0, 1.0},{0.0, 1.0/2.0, 1.0},
                {0.0, 1.0/3.0,2.0 / 3.0, 1.0}, {0.0, 1.0/3.0,2.0 / 3.0, 1.0},{0.0, 1.0/2.0, 1.0},
                {0.0, 1.0/3.0,2.0 / 3.0, 1.0}, {0.0, 1.0/3.0,2.0 / 3.0, 1.0},{0.0, 1.0/2.0, 1.0},
                {0.0, 1.0/3.0,2.0 / 3.0, 1.0}, {0.0, 1.0/3.0,2.0 / 3.0, 1.0},{0.0, 1.0/2.0, 1.0},
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";

        String[] frontFileList = {
                frontDir + "DTLZ1.8D.pf[31824]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ1.15D.pf[54264]",
                frontDir + "DTLZ2.8D.pf[54264]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ2.15D.pf[54264]",
                frontDir + "DTLZ3.8D.pf[31824]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ3.15D.pf[54264]",

                frontDir + "DTLZ4.8D.pf[31824]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "DTLZ4.15D.pf[54264]",

                frontDir + "Convex_DTLZ2.8D.pf[31824]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.15D.pf[54264]"
        };
        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});

        Point[] hvRefPointList = {
                point8Dmin, point10Dmin,point15Dmin,
                point8D, point10D,point15D,
                point8D, point10D,point15D,
                point8D, point10D,point15D,
                point8D, point10D,point15D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero3D,pointZero5D,pointZero8D,
                pointZero10D,pointZero15D,
                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D
        };


        double[] hvCubeList = {
                0.99999990311879960317460317460317,
                0.99999999973088555445326278659612,
                0.99999999999999997666270833795222,

                255.9841456557561844991477147896,
                1023.9975096054298072798399842016,
                32767.999988359274877218491124947,

                255.9841456557561844991477147896,
                1023.9975096054298072798399842016,
                32767.999988359274877218491124947,

                255.9841456557561844991477147896,
                1023.9975096054298072798399842016,
                32767.999988359274877218491124947,

                255.99999997944442388886833331278,
                1023.9999999999999957910347537283,
                32768 - 1.8530243193155939420665879882856e-27
        };


        String[] algorithmNameList = {
                //"MOEACDPBI","MOEACDPBIS","MOEACDAPBI","MOEACDFPBI","MOEACDFAPBI",
                //"NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"
                "MOEACDPBI","MOEACDPBIS","MOEACDPBIAN","MOEACDAPBI","MOEACDFPBI","MOEACDFAPBI","MOEACDFBA","MOEACDLHV","MOEACD","MOEACDN"
        };
        String dataDir = "E://ResultsMaOPMeasure7/";
        String statDir = "E://ResultsMaOPMeasure7/";
        int numOfSample = 10000;
        int approximateDim = 10;
        int maxRun = 20;
        executeIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
//        executeHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
    }

    public void executeMaOPThetaMeasure(){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
        };

        int[] objNumList = {
                3,5,8,
                3,5,8,
                3,5,8,
                3,5,8,
                3,5,8
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

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[5050]",
                frontDir + "DTLZ1.5D.pf[10626]",
                frontDir + "DTLZ1.8D.pf[11440]",
                frontDir + "DTLZ2.3D.pf[5050]",
                frontDir + "DTLZ2.5D.pf[10626]",
                frontDir + "DTLZ2.8D.pf[11440]",
                frontDir + "DTLZ3.3D.pf[5050]",
                frontDir + "DTLZ3.5D.pf[10626]",
                frontDir + "DTLZ3.8D.pf[11440]",
                frontDir + "DTLZ4.3D.pf[5050]",
                frontDir + "DTLZ4.5D.pf[10626]",
                frontDir + "DTLZ4.8D.pf[11440]",
                frontDir + "Convex_DTLZ2.3D.pf[5050]",
                frontDir + "Convex_DTLZ2.5D.pf[10626]",
                frontDir + "Convex_DTLZ2.8D.pf[11440]"
        };

        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});

        Point[] hvRefPointList = {
                point3Dmin,point5Dmin, point8Dmin,
                point3D,point5D, point8D,
                point3D,point5D, point8D,
                point3D,point5D, point8D,
                point3D,point5D, point8D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D
        };

        double[] hvCubeList = {
                1330.9791666666666666667,
                0.99973958333333333333,
                0.99999990311879960317460317460317,

                1330.4764012244017011269228927694534,
                31.835506593315177356352758483335,
                255.9841456557561844991477147896,

                1330.4764012244017011269228927694534,
                31.835506593315177356352758483335,
                255.9841456557561844991477147896,

                1330.4764012244017011269228927694534,
                31.835506593315177356352758483335,
                255.9841456557561844991477147896,

                1330.9666666666666666666666666666667,
                31.999955908289241622574955908289,
                255.99999997944442388886833331278
        };


        String[] algorithmNameList = {
            "MOEACDPBI","MOEACDFPBI"
        };

        int maxRun = 10;
        int numOfSample = 100000;
        int approximateDim = 10;
//        executeIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
        executeHV("E://ResultsTheta1","E://ResultsTheta1",maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
        executeHV("E://ResultsTheta5","E://ResultsTheta5",maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
        executeHV("E://ResultsTheta10","E://ResultsTheta10",maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
        executeHV("E://ResultsTheta20","E://ResultsTheta20",maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
        executeHV("E://ResultsTheta50","E://ResultsTheta50",maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
        executeHV("E://ResultsTheta100","E://ResultsTheta100",maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);

    }
//
//    public void execute2(){
////        String[] problemNameList = {
////                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)","DTLZ1(10)",
////                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)","DTLZ2(10)",
////                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)","DTLZ3(10)",
////                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)","DTLZ4(10)",
////                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)","Convex_DTLZ2(10)"
////        };
////
////        int[] popsList = {
////                91, 210, 156,275,
////                91, 210, 156,275,
////                91, 210, 156,275,
////                91, 210, 156,275,
////                91, 210, 156,275
////        };
////        int[] maxIterationsList = {
////                400,600,750,1000,
////                250,350,500,750,
////                1000,1000,1000,1500,
////                600,1000,1250,2000,
////                250,750,2000,4000
////        };
////
////        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////        String[] frontFileList = {
////                frontDir + "DTLZ1.3D.pf[91]",
////                frontDir + "DTLZ1.5D.pf[210]",
////                frontDir + "DTLZ1.8D.pf[156]",
////                frontDir + "DTLZ1.10D.pf[275]",
////                frontDir + "DTLZ2.3D.pf[91]",
////                frontDir + "DTLZ2.5D.pf[210]",
////                frontDir + "DTLZ2.8D.pf[156]",
////                frontDir + "DTLZ2.10D.pf[275]",
////                frontDir + "DTLZ3.3D.pf[91]",
////                frontDir + "DTLZ3.5D.pf[210]",
////                frontDir + "DTLZ3.8D.pf[156]",
////                frontDir + "DTLZ3.10D.pf[275]",
////                frontDir + "DTLZ4.3D.pf[91]",
////                frontDir + "DTLZ4.5D.pf[210]",
////                frontDir + "DTLZ4.8D.pf[156]",
////                frontDir + "DTLZ4.10D.pf[275]",
////                frontDir + "Convex_DTLZ2.3D.pf[91]",
////                frontDir + "Convex_DTLZ2.5D.pf[210]",
////                frontDir + "Convex_DTLZ2.8D.pf[156]",
////                frontDir + "Convex_DTLZ2.10D.pf[275]"
////        };
////
//////        String[] frontFileList = {
//////                frontDir + "DTLZ1.3D.pf[5050]",
//////                frontDir + "DTLZ1.5D.pf[10626]",
//////                frontDir + "DTLZ1.8D.pf[11440]",
//////                frontDir + "DTLZ1.10D.pf[24310]",
//////                frontDir + "DTLZ2.3D.pf[5050]",
//////                frontDir + "DTLZ2.5D.pf[10626]",
//////                frontDir + "DTLZ2.8D.pf[11440]",
//////                frontDir + "DTLZ2.10D.pf[24310]",
//////                frontDir + "DTLZ3.3D.pf[5050]",
//////                frontDir + "DTLZ3.5D.pf[10626]",
//////                frontDir + "DTLZ3.8D.pf[11440]",
//////                frontDir + "DTLZ3.10D.pf[24310]",
//////                frontDir + "DTLZ4.3D.pf[5050]",
//////                frontDir + "DTLZ4.5D.pf[10626]",
//////                frontDir + "DTLZ4.8D.pf[11440]",
//////                frontDir + "DTLZ4.10D.pf[24310]",
//////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
//////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
//////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
//////        };
////
////        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
////        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
////        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
////        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
////        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
////        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
////        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////
////        Point[] hvRefPointList = {
////                point3Dmin,point5Dmin, point8Dmin, point10Dmin,
////                point3D,point5D, point8D, point10D,
////                point3D,point5D, point8D, point10D,
////                point3D,point5D, point8D, point10D,
////                point3D,point5D, point8D, point10D
////        };
////
////        double[] hvCubeList = {
////                0.9791666666666666667,
////                0.99973958333333333333,
////                0.99999990311879960317460317460317,
////                0.99999999973088555445326278659612,
////
////                7.4764012244017011269228927694534,
////                31.835506593315177356352758483335,
////                255.9841456557561844991477147896,
////                1023.9975096054298072798399842016,
////
////                7.4764012244017011269228927694534,
////                31.835506593315177356352758483335,
////                255.9841456557561844991477147896,
////                1023.9975096054298072798399842016,
////
////                7.4764012244017011269228927694534,
////                31.835506593315177356352758483335,
////                255.9841456557561844991477147896,
////                1023.9975096054298072798399842016,
////
////                7.9666666666666666666666666666667,
////                31.999955908289241622574955908289,
////                255.99999997944442388886833331278,
////                1023.9999999999999957910347537283
////
////        };
//////        String[] algorithmNameList = {
//////                "NSGAIII","MOEADDE_PBI","MOEADACD_PBI","MOEAD_PBI","MOEADD_PBI","MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
//////        };
////        String[] algorithmNameList = {
////                "MOEADACD_PBI"
////        };
////        String[] algorithmNameList = {
////                "MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
////        };
////        String[] problemNameList = {
////                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)",
////                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)",
////                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)",
////                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)",
////                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
////        };
////
////        int[] popsList = {
////                91, 210, 156,
////                91, 210, 156,
////                91, 210, 156,
////                91, 210, 156,
////                91, 210, 156
////        };
////        int[] maxIterationsList = {
////                400,600,750,
////                250,350,500,
////                1000,1000,1000,
////                600,1000,1250,
////                250,750,2000
////        };
////
////        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////        String[] frontFileList = {
////                frontDir + "DTLZ1.3D.pf[91]",
////                frontDir + "DTLZ1.5D.pf[210]",
////                frontDir + "DTLZ1.8D.pf[156]",
////                frontDir + "DTLZ2.3D.pf[91]",
////                frontDir + "DTLZ2.5D.pf[210]",
////                frontDir + "DTLZ2.8D.pf[156]",
////                frontDir + "DTLZ3.3D.pf[91]",
////                frontDir + "DTLZ3.5D.pf[210]",
////                frontDir + "DTLZ3.8D.pf[156]",
////                frontDir + "DTLZ4.3D.pf[91]",
////                frontDir + "DTLZ4.5D.pf[210]",
////                frontDir + "DTLZ4.8D.pf[156]",
////                frontDir + "Convex_DTLZ2.3D.pf[91]",
////                frontDir + "Convex_DTLZ2.5D.pf[210]",
////                frontDir + "Convex_DTLZ2.8D.pf[156]"
////        };
////
//////        String[] frontFileList = {
//////                frontDir + "DTLZ1.3D.pf[5050]",
//////                frontDir + "DTLZ1.5D.pf[10626]",
//////                frontDir + "DTLZ1.8D.pf[11440]",
//////                frontDir + "DTLZ1.10D.pf[24310]",
//////                frontDir + "DTLZ2.3D.pf[5050]",
//////                frontDir + "DTLZ2.5D.pf[10626]",
//////                frontDir + "DTLZ2.8D.pf[11440]",
//////                frontDir + "DTLZ2.10D.pf[24310]",
//////                frontDir + "DTLZ3.3D.pf[5050]",
//////                frontDir + "DTLZ3.5D.pf[10626]",
//////                frontDir + "DTLZ3.8D.pf[11440]",
//////                frontDir + "DTLZ3.10D.pf[24310]",
//////                frontDir + "DTLZ4.3D.pf[5050]",
//////                frontDir + "DTLZ4.5D.pf[10626]",
//////                frontDir + "DTLZ4.8D.pf[11440]",
//////                frontDir + "DTLZ4.10D.pf[24310]",
//////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
//////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
//////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
//////        };
////
////        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
////        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
////        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
////        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
////        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
////        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
////        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////
////        Point[] hvRefPointList = {
////                point3Dmin,point5Dmin, point8Dmin,
////                point3D,point5D, point8D,
////                point3D,point5D, point8D,
////                point3D,point5D, point8D,
////                point3D,point5D, point8D
////        };
////
////        double[] hvCubeList = {
////                1330.9791666666666666667,
////                0.99973958333333333333,
////                0.99999990311879960317460317460317,
////
////                1330.4764012244017011269228927694534,
////                31.835506593315177356352758483335,
////                255.9841456557561844991477147896,
////
////                1330.4764012244017011269228927694534,
////                31.835506593315177356352758483335,
////                255.9841456557561844991477147896,
////
////                1330.4764012244017011269228927694534,
////                31.835506593315177356352758483335,
////                255.9841456557561844991477147896,
////
////                1330.9666666666666666666666666666667,
////                31.999955908289241622574955908289,
////                255.99999997944442388886833331278
////
////        };
////        String[] algorithmNameList = {
////                "NSGAIII","MOEADDE_PBI","MOEADACD_PBI","MOEAD_PBI","MOEADD_PBI","MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
////        };
//        String[] problemNameList = {
//                "DTLZ1(10)",
//                "DTLZ2(10)",
//                "DTLZ3(10)",
//                "DTLZ4(10)",
//                "Convex_DTLZ2(10)"
//        };
//
//        int[] objNumList = {
//          10,10,10,10,10
//        };
//
//        int[] popsList = {
//                275,
//                275,
//                275,
//                275,
//                275
//        };
//        int[] maxIterationsList = {
//                1000,
//                750,
//                1500,
//                2000,
//                4000
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.10D.pf[275]",
//                frontDir + "DTLZ2.10D.pf[275]",
//                frontDir + "DTLZ3.10D.pf[275]",
//                frontDir + "DTLZ4.10D.pf[275]",
//                frontDir + "Convex_DTLZ2.10D.pf[275]"
//        };
//
////        String[] frontFileList = {
////                frontDir + "DTLZ1.3D.pf[5050]",
////                frontDir + "DTLZ1.5D.pf[10626]",
////                frontDir + "DTLZ1.8D.pf[11440]",
////                frontDir + "DTLZ1.10D.pf[24310]",
////                frontDir + "DTLZ2.3D.pf[5050]",
////                frontDir + "DTLZ2.5D.pf[10626]",
////                frontDir + "DTLZ2.8D.pf[11440]",
////                frontDir + "DTLZ2.10D.pf[24310]",
////                frontDir + "DTLZ3.3D.pf[5050]",
////                frontDir + "DTLZ3.5D.pf[10626]",
////                frontDir + "DTLZ3.8D.pf[11440]",
////                frontDir + "DTLZ3.10D.pf[24310]",
////                frontDir + "DTLZ4.3D.pf[5050]",
////                frontDir + "DTLZ4.5D.pf[10626]",
////                frontDir + "DTLZ4.8D.pf[11440]",
////                frontDir + "DTLZ4.10D.pf[24310]",
////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
////        };
//
//        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
//        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
//        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//
//        Point[] hvRefPointList = {
//                point10Dmin,
//                point10D,
//                point10D,
//                point10D,
//                point10D
//        };
//
//        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
//        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
//        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
//        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
//        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
//        Point[] boundPointList = {
//                pointZero10D,
//                pointZero10D,
//                pointZero10D,
//                pointZero10D,
//                pointZero10D
//        };
//
//        double[] hvCubeList = {
//
//                0.99999999973088555445326278659612,
//
//                1023.9975096054298072798399842016,
//
//                1023.9975096054298072798399842016,
//
//                1023.9975096054298072798399842016,
//
//                1023.9999999999999957910347537283
//
//        };
//        String[] algorithmNameList = {
//                "NSGAIII","MOEADDE_PBI","MOEADACD_PBI","MOEAD_PBI","MOEADD_PBI","MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
//        };
////        String[] algorithmNameList = {
////                "NSGAIII","MOEAD_PBI","MOEADD_PBI","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
////        };
////        String dataDir = "E://ResultsMaOPMeasure/";
////        String statDir = "E://ResultsMaOPMeasure/stat1/";
//        String dataDir = "E://ResultsMaOPT1/";
//        String statDir = "E://ResultsMaOPT1/stat/";
//        int maxRun = 20;
//        int numOfSample = 100000;
//        int approximateDim = 10;
////        executeFinalIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
//        executeFinalHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
//    }
//
//    public void execute3(){
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
//                750,1000,1250, 1500,
//                750,1000,1250, 1500,
//                1000,1250,1500, 2000,
//                1250,1500,2000, 3000,
//                1000,2000,3000, 5000
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[91]",
//                frontDir + "DTLZ1.5D.pf[210]",
//                frontDir + "DTLZ1.8D.pf[156]",
//                frontDir + "DTLZ1.10D.pf[275]",
//                frontDir + "DTLZ2.3D.pf[91]",
//                frontDir + "DTLZ2.5D.pf[210]",
//                frontDir + "DTLZ2.8D.pf[156]",
//                frontDir + "DTLZ2.10D.pf[275]",
//                frontDir + "DTLZ3.3D.pf[91]",
//                frontDir + "DTLZ3.5D.pf[210]",
//                frontDir + "DTLZ3.8D.pf[156]",
//                frontDir + "DTLZ3.10D.pf[275]",
//                frontDir + "DTLZ4.3D.pf[91]",
//                frontDir + "DTLZ4.5D.pf[210]",
//                frontDir + "DTLZ4.8D.pf[156]",
//                frontDir + "DTLZ4.10D.pf[275]",
//                frontDir + "Convex_DTLZ2.3D.pf[91]",
//                frontDir + "Convex_DTLZ2.5D.pf[210]",
//                frontDir + "Convex_DTLZ2.8D.pf[156]",
//                frontDir + "Convex_DTLZ2.10D.pf[275]"
//        };
//
////        String[] frontFileList = {
////                frontDir + "DTLZ1.3D.pf[5050]",
////                frontDir + "DTLZ1.5D.pf[10626]",
////                frontDir + "DTLZ1.8D.pf[11440]",
////                frontDir + "DTLZ1.10D.pf[24310]",
////                frontDir + "DTLZ2.3D.pf[5050]",
////                frontDir + "DTLZ2.5D.pf[10626]",
////                frontDir + "DTLZ2.8D.pf[11440]",
////                frontDir + "DTLZ2.10D.pf[24310]",
////                frontDir + "DTLZ3.3D.pf[5050]",
////                frontDir + "DTLZ3.5D.pf[10626]",
////                frontDir + "DTLZ3.8D.pf[11440]",
////                frontDir + "DTLZ3.10D.pf[24310]",
////                frontDir + "DTLZ4.3D.pf[5050]",
////                frontDir + "DTLZ4.5D.pf[10626]",
////                frontDir + "DTLZ4.8D.pf[11440]",
////                frontDir + "DTLZ4.10D.pf[24310]",
////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
////        };
//
//        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
//        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
//        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//
//        Point[] hvRefPointList = {
//                point3Dmin,point5Dmin, point8Dmin, point10Dmin,
//                point3D,point5D, point8D, point10D,
//                point3D,point5D, point8D, point10D,
//                point3D,point5D, point8D, point10D,
//                point3D,point5D, point8D, point10D
//        };
//
//        double[] hvCubeList = {
//                0.9791666666666666667,
//                0.99973958333333333333,
//                0.99999990311879960317460317460317,
//                0.99999999973088555445326278659612,
//
//                7.4764012244017011269228927694534,
//                31.835506593315177356352758483335,
//                255.9841456557561844991477147896,
//                1023.9975096054298072798399842016,
//
//                7.4764012244017011269228927694534,
//                31.835506593315177356352758483335,
//                255.9841456557561844991477147896,
//                1023.9975096054298072798399842016,
//
//                7.4764012244017011269228927694534,
//                31.835506593315177356352758483335,
//                255.9841456557561844991477147896,
//                1023.9975096054298072798399842016,
//
//                7.9666666666666666666666666666667,
//                31.999955908289241622574955908289,
//                255.99999997944442388886833331278,
//                1023.9999999999999957910347537283
//
//        };
////        String[] algorithmNameList = {
////                "NSGAIII","MOEAD_PBI","MOEADD_PBI","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
////        };
//        String[] algorithmNameList = {
//               "MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
//        };
//        int[][] evaluatingGenList = {
//                {400,25}, {600,25}, {750,50},{1000,50},
//                {250,25}, {350,25}, {500,25}, {750,50},
//                {1000,50}, {1000,50}, {1000,50},{1000,50},
//                {600,25}, {1000,50}, {1250,50},{2000,100},
//                {500,25}, {1000,50}, {2000,100},{2500,100}
//        };
//
////        String[] problemNameList = {
////                "DTLZ1(10)",
////                "DTLZ2(10)",
////                "DTLZ3(10)",
////                "DTLZ4(10)",
////                "Convex_DTLZ2(8)","Convex_DTLZ2(10)"
////        };
////
////        int[] popsList = {
////                275,
////               275,
////                275,
////                275,
////                156,275
////        };
////        int[] maxIterationsList = {
////                 1500,
////                1500,
////                 2000,
////                3000,
////                3000, 5000
////        };
////
////        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////        String[] frontFileList = {
////                frontDir + "DTLZ1.10D.pf[275]",
////                frontDir + "DTLZ2.10D.pf[275]",
////                frontDir + "DTLZ3.10D.pf[275]",
////                frontDir + "DTLZ4.10D.pf[275]",
////                frontDir + "Convex_DTLZ2.8D.pf[156]",
////                frontDir + "Convex_DTLZ2.10D.pf[275]"
////        };
////
//////        String[] frontFileList = {
//////                frontDir + "DTLZ1.3D.pf[5050]",
//////                frontDir + "DTLZ1.5D.pf[10626]",
//////                frontDir + "DTLZ1.8D.pf[11440]",
//////                frontDir + "DTLZ1.10D.pf[24310]",
//////                frontDir + "DTLZ2.3D.pf[5050]",
//////                frontDir + "DTLZ2.5D.pf[10626]",
//////                frontDir + "DTLZ2.8D.pf[11440]",
//////                frontDir + "DTLZ2.10D.pf[24310]",
//////                frontDir + "DTLZ3.3D.pf[5050]",
//////                frontDir + "DTLZ3.5D.pf[10626]",
//////                frontDir + "DTLZ3.8D.pf[11440]",
//////                frontDir + "DTLZ3.10D.pf[24310]",
//////                frontDir + "DTLZ4.3D.pf[5050]",
//////                frontDir + "DTLZ4.5D.pf[10626]",
//////                frontDir + "DTLZ4.8D.pf[11440]",
//////                frontDir + "DTLZ4.10D.pf[24310]",
//////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
//////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
//////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
//////        };
////
////        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
////        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
////        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
////        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
////        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
////        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
////        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////
////        Point[] hvRefPointList = {
////                 point10Dmin,
////                 point10D,
////                point10D,
////                 point10D,
////                point8D, point10D
////        };
////
////        double[] hvCubeList = {
////                0.99999999973088555445326278659612,
////
////                1023.9975096054298072798399842016,
////
////                1023.9975096054298072798399842016,
////
////                1023.9975096054298072798399842016,
////
////                255.99999997944442388886833331278,
////                1023.9999999999999957910347537283
////
////        };
////
////        String[] algorithmNameList = {
////                "NSGAIII","MOEAD_PBI","MOEADD_PBI","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
////        };
////        String dataDir = "E://ResultsMaOPMeasure/";
////        String statDir = "E://ResultsMaOPMeasure/stat1/";
//        String dataDir = "E://ResultsMaOP2-2/";
//        String statDir = "E://ResultsMaOP2-2/stat/";
//        int maxRun = 5;
//        executeFinalIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
////        executeFinalHV(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,hvRefPointList,hvCubeList,algorithmNameList);
//    }
//
//
//    public void execute4(){
////        String[] problemNameList = {
////                "DTLZ1(8)","DTLZ1(10)",
////                "DTLZ2(8)","DTLZ2(10)",
////                "DTLZ3(8)","DTLZ3(10)",
////                "DTLZ4(8)","DTLZ4(10)",
////                "Convex_DTLZ2(8)","Convex_DTLZ2(10)"
////        };
////
////        int[] popsList = {
////                156,275,
////                156,275,
////                156,275,
////                156,275,
////                156,275
////        };
////        int[] maxIterationsList = {
////                750, 1000,
////                500, 750,
////                1000, 1500,
////                1250, 2000,
////                2000, 4000
////        };
////
////        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////        String[] frontFileList = {
////                frontDir + "DTLZ1.8D.pf[156]",
////                frontDir + "DTLZ1.10D.pf[275]",
////                frontDir + "DTLZ2.8D.pf[156]",
////                frontDir + "DTLZ2.10D.pf[275]",
////                frontDir + "DTLZ3.8D.pf[156]",
////                frontDir + "DTLZ3.10D.pf[275]",
////                frontDir + "DTLZ4.8D.pf[156]",
////                frontDir + "DTLZ4.10D.pf[275]",
////                frontDir + "Convex_DTLZ2.8D.pf[156]",
////                frontDir + "Convex_DTLZ2.10D.pf[275]"
////        };
////
//////        String[] frontFileList = {
//////                frontDir + "DTLZ1.3D.pf[5050]",
//////                frontDir + "DTLZ1.5D.pf[10626]",
//////                frontDir + "DTLZ1.8D.pf[11440]",
//////                frontDir + "DTLZ1.10D.pf[24310]",
//////                frontDir + "DTLZ2.3D.pf[5050]",
//////                frontDir + "DTLZ2.5D.pf[10626]",
//////                frontDir + "DTLZ2.8D.pf[11440]",
//////                frontDir + "DTLZ2.10D.pf[24310]",
//////                frontDir + "DTLZ3.3D.pf[5050]",
//////                frontDir + "DTLZ3.5D.pf[10626]",
//////                frontDir + "DTLZ3.8D.pf[11440]",
//////                frontDir + "DTLZ3.10D.pf[24310]",
//////                frontDir + "DTLZ4.3D.pf[5050]",
//////                frontDir + "DTLZ4.5D.pf[10626]",
//////                frontDir + "DTLZ4.8D.pf[11440]",
//////                frontDir + "DTLZ4.10D.pf[24310]",
//////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
//////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
//////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
//////        };
////
////        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
////        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
////        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
////        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
////        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
////        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
////        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////
////        Point[] hvRefPointList = {
////                point8Dmin, point10Dmin,
////                point8D, point10D,
////                point8D, point10D,
////                point8D, point10D,
////                point8D, point10D
////        };
////
////        double[] hvCubeList = {
////                0.99999990311879960317460317460317,
////                0.99999999973088555445326278659612,
////
////                255.9841456557561844991477147896,
////                1023.9975096054298072798399842016,
////
////                255.9841456557561844991477147896,
////                1023.9975096054298072798399842016,
////
////                255.9841456557561844991477147896,
////                1023.9975096054298072798399842016,
////
////                255.99999997944442388886833331278,
////                1023.9999999999999957910347537283
////
////        };
//
////        String[] problemNameList = {
////                "DTLZ1(8)",
////                "DTLZ2(8)",
////                "DTLZ3(8)",
////                "DTLZ4(8)",
////                "Convex_DTLZ2(8)"
////        };
////
////        int[] popsList = {
////                156,
////                156,
////                156,
////                156,
////                156
////        };
////        int[] maxIterationsList = {
////                750,
////                500,
////                1000,
////                1250,
////                2000
////        };
////
////        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////        String[] frontFileList = {
////                frontDir + "DTLZ1.8D.pf[156]",
////                frontDir + "DTLZ2.8D.pf[156]",
////                frontDir + "DTLZ3.8D.pf[156]",
////                frontDir + "DTLZ4.8D.pf[156]",
////                frontDir + "Convex_DTLZ2.8D.pf[156]"
////        };
////
//////        String[] frontFileList = {
//////                frontDir + "DTLZ1.3D.pf[5050]",
//////                frontDir + "DTLZ1.5D.pf[10626]",
//////                frontDir + "DTLZ1.8D.pf[11440]",
//////                frontDir + "DTLZ1.10D.pf[24310]",
//////                frontDir + "DTLZ2.3D.pf[5050]",
//////                frontDir + "DTLZ2.5D.pf[10626]",
//////                frontDir + "DTLZ2.8D.pf[11440]",
//////                frontDir + "DTLZ2.10D.pf[24310]",
//////                frontDir + "DTLZ3.3D.pf[5050]",
//////                frontDir + "DTLZ3.5D.pf[10626]",
//////                frontDir + "DTLZ3.8D.pf[11440]",
//////                frontDir + "DTLZ3.10D.pf[24310]",
//////                frontDir + "DTLZ4.3D.pf[5050]",
//////                frontDir + "DTLZ4.5D.pf[10626]",
//////                frontDir + "DTLZ4.8D.pf[11440]",
//////                frontDir + "DTLZ4.10D.pf[24310]",
//////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
//////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
//////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
//////        };
////
////        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
////        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
////        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
////        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
////        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
////        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
////        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////
////        Point[] hvRefPointList = {
////                point8Dmin,
////                point8D,
////                point8D,
////                point8D,
////                point8D
////        };
////
////        double[] hvCubeList = {
////                0.99999990311879960317460317460317,
////
////                255.9841456557561844991477147896,
////
////                255.9841456557561844991477147896,
////
////                255.9841456557561844991477147896,
////
////                255.99999997944442388886833331278
////        };
//        String[] problemNameList = {
//                "DTLZ1(10)",
//                "DTLZ2(10)",
//                "DTLZ3(10)",
//                "DTLZ4(10)",
//                "Convex_DTLZ2(10)"
//        };
//
//        int[] popsList = {
//                275,
//                275,
//                275,
//                275,
//                275
//        };
//        int[] maxIterationsList = {
//                1000,
//                750,
//                1500,
//                2000,
//                4000
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.10D.pf[275]",
//                frontDir + "DTLZ2.10D.pf[275]",
//                frontDir + "DTLZ3.10D.pf[275]",
//                frontDir + "DTLZ4.10D.pf[275]",
//                frontDir + "Convex_DTLZ2.10D.pf[275]"
//        };
//
////        String[] frontFileList = {
////                frontDir + "DTLZ1.3D.pf[5050]",
////                frontDir + "DTLZ1.5D.pf[10626]",
////                frontDir + "DTLZ1.8D.pf[11440]",
////                frontDir + "DTLZ1.10D.pf[24310]",
////                frontDir + "DTLZ2.3D.pf[5050]",
////                frontDir + "DTLZ2.5D.pf[10626]",
////                frontDir + "DTLZ2.8D.pf[11440]",
////                frontDir + "DTLZ2.10D.pf[24310]",
////                frontDir + "DTLZ3.3D.pf[5050]",
////                frontDir + "DTLZ3.5D.pf[10626]",
////                frontDir + "DTLZ3.8D.pf[11440]",
////                frontDir + "DTLZ3.10D.pf[24310]",
////                frontDir + "DTLZ4.3D.pf[5050]",
////                frontDir + "DTLZ4.5D.pf[10626]",
////                frontDir + "DTLZ4.8D.pf[11440]",
////                frontDir + "DTLZ4.10D.pf[24310]",
////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
////        };
//
//        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
//        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
//        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//
//        Point[] hvRefPointList = {
//                point10Dmin,
//                point10D,
//                point10D,
//                point10D,
//                point10D
//        };
//
//        double[] hvCubeList = {
//                0.99999999973088555445326278659612,
//
//                1023.9975096054298072798399842016,
//
//                1023.9975096054298072798399842016,
//
//                1023.9975096054298072798399842016,
//
//                1023.9999999999999957910347537283
//
//        };
//
////        String[] problemNameList = {
////                "Convex_DTLZ2(8)"
////        };
////
////        int[] popsList = {
////                156
////        };
////        int[] maxIterationsList = {
////                2000
////        };
////
////        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////        String[] frontFileList = {
////                frontDir + "Convex_DTLZ2.8D.pf[156]"
////        };
//
////        String[] frontFileList = {
////                frontDir + "DTLZ1.3D.pf[5050]",
////                frontDir + "DTLZ1.5D.pf[10626]",
////                frontDir + "DTLZ1.8D.pf[11440]",
////                frontDir + "DTLZ1.10D.pf[24310]",
////                frontDir + "DTLZ2.3D.pf[5050]",
////                frontDir + "DTLZ2.5D.pf[10626]",
////                frontDir + "DTLZ2.8D.pf[11440]",
////                frontDir + "DTLZ2.10D.pf[24310]",
////                frontDir + "DTLZ3.3D.pf[5050]",
////                frontDir + "DTLZ3.5D.pf[10626]",
////                frontDir + "DTLZ3.8D.pf[11440]",
////                frontDir + "DTLZ3.10D.pf[24310]",
////                frontDir + "DTLZ4.3D.pf[5050]",
////                frontDir + "DTLZ4.5D.pf[10626]",
////                frontDir + "DTLZ4.8D.pf[11440]",
////                frontDir + "DTLZ4.10D.pf[24310]",
////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
////        };
//
////        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
////        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
////        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
////        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
////        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
////        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
////        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////
////        Point[] hvRefPointList = {
////                point8D
////        };
////
////        double[] hvCubeList = {
////                255.99999997944442388886833331278
////
////        };
//        String[] algorithmNameList = {
//                "MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
//        };
//
////        String dataDir = "E://ResultsMaOPMeasure/";
////        String statDir = "E://ResultsMaOPMeasure/stat1/";
//        String dataDir = "E://ResultsMaOPTaoT1/";
//        String statDir = "E://ResultsMaOPTaoT1/stat/";
//        int maxRun = 20;
//        executeFinalHV(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,hvRefPointList,hvCubeList,algorithmNameList);
//    }
//
//
//    public void execute5(){
////        String[] problemNameList = {
////                "DTLZ1(8)","DTLZ1(10)",
////                "DTLZ2(8)","DTLZ2(10)",
////                "DTLZ3(8)","DTLZ3(10)",
////                "DTLZ4(8)","DTLZ4(10)",
////                "Convex_DTLZ2(8)","Convex_DTLZ2(10)"
////        };
////
////        int[] popsList = {
////                156,275,
////                156,275,
////                156,275,
////                156,275,
////                156,275
////        };
////        int[] maxIterationsList = {
////                1250, 1500,
////                1250, 1500,
////                1500, 2000,
////                2000, 3000,
////                3000, 5000
////        };
////
////        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////        String[] frontFileList = {
////                frontDir + "DTLZ1.8D.pf[156]",
////                frontDir + "DTLZ1.10D.pf[275]",
////                frontDir + "DTLZ2.8D.pf[156]",
////                frontDir + "DTLZ2.10D.pf[275]",
////                frontDir + "DTLZ3.8D.pf[156]",
////                frontDir + "DTLZ3.10D.pf[275]",
////                frontDir + "DTLZ4.8D.pf[156]",
////                frontDir + "DTLZ4.10D.pf[275]",
////                frontDir + "Convex_DTLZ2.8D.pf[156]",
////                frontDir + "Convex_DTLZ2.10D.pf[275]"
////        };
////
//////        String[] frontFileList = {
//////                frontDir + "DTLZ1.3D.pf[5050]",
//////                frontDir + "DTLZ1.5D.pf[10626]",
//////                frontDir + "DTLZ1.8D.pf[11440]",
//////                frontDir + "DTLZ1.10D.pf[24310]",
//////                frontDir + "DTLZ2.3D.pf[5050]",
//////                frontDir + "DTLZ2.5D.pf[10626]",
//////                frontDir + "DTLZ2.8D.pf[11440]",
//////                frontDir + "DTLZ2.10D.pf[24310]",
//////                frontDir + "DTLZ3.3D.pf[5050]",
//////                frontDir + "DTLZ3.5D.pf[10626]",
//////                frontDir + "DTLZ3.8D.pf[11440]",
//////                frontDir + "DTLZ3.10D.pf[24310]",
//////                frontDir + "DTLZ4.3D.pf[5050]",
//////                frontDir + "DTLZ4.5D.pf[10626]",
//////                frontDir + "DTLZ4.8D.pf[11440]",
//////                frontDir + "DTLZ4.10D.pf[24310]",
//////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
//////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
//////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
//////        };
////
////        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
////        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
////        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
////        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
////        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
////        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
////        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////
////        Point[] hvRefPointList = {
////                point8Dmin, point10Dmin,
////                point8D, point10D,
////                point8D, point10D,
////                point8D, point10D,
////                point8D, point10D
////        };
////
////        double[] hvCubeList = {
////                0.99999990311879960317460317460317,
////                0.99999999973088555445326278659612,
////
////                255.9841456557561844991477147896,
////                1023.9975096054298072798399842016,
////
////                255.9841456557561844991477147896,
////                1023.9975096054298072798399842016,
////
////                255.9841456557561844991477147896,
////                1023.9975096054298072798399842016,
////
////                255.99999997944442388886833331278,
////                1023.9999999999999957910347537283
////
////        };
//
//        String[] problemNameList = {
//                "DTLZ1(8)",
//                "DTLZ2(8)",
//                "DTLZ3(8)",
//                "DTLZ4(8)",
//                "Convex_DTLZ2(8)"
//        };
//
//        int[] popsList = {
//                156,
//                156,
//                156,
//                156,
//                156
//        };
//        int[] maxIterationsList = {
//                1250,
//                1250,
//                1500,
//                2000,
//                3000
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.8D.pf[156]",
//                frontDir + "DTLZ2.8D.pf[156]",
//                frontDir + "DTLZ3.8D.pf[156]",
//                frontDir + "DTLZ4.8D.pf[156]",
//                frontDir + "Convex_DTLZ2.8D.pf[156]"
//        };
//
////        String[] frontFileList = {
////                frontDir + "DTLZ1.3D.pf[5050]",
////                frontDir + "DTLZ1.5D.pf[10626]",
////                frontDir + "DTLZ1.8D.pf[11440]",
////                frontDir + "DTLZ1.10D.pf[24310]",
////                frontDir + "DTLZ2.3D.pf[5050]",
////                frontDir + "DTLZ2.5D.pf[10626]",
////                frontDir + "DTLZ2.8D.pf[11440]",
////                frontDir + "DTLZ2.10D.pf[24310]",
////                frontDir + "DTLZ3.3D.pf[5050]",
////                frontDir + "DTLZ3.5D.pf[10626]",
////                frontDir + "DTLZ3.8D.pf[11440]",
////                frontDir + "DTLZ3.10D.pf[24310]",
////                frontDir + "DTLZ4.3D.pf[5050]",
////                frontDir + "DTLZ4.5D.pf[10626]",
////                frontDir + "DTLZ4.8D.pf[11440]",
////                frontDir + "DTLZ4.10D.pf[24310]",
////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
////        };
//
//        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
//        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
//        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//
//        Point[] hvRefPointList = {
//                point8Dmin,
//                point8D,
//                point8D,
//                point8D,
//                point8D
//        };
//
//        double[] hvCubeList = {
//                0.99999990311879960317460317460317,
//
//                255.9841456557561844991477147896,
//
//                255.9841456557561844991477147896,
//
//                255.9841456557561844991477147896,
//
//                255.99999997944442388886833331278
//        };
//
////        String[] problemNameList = {
////                "DTLZ1(10)",
////                "DTLZ2(10)",
////                "DTLZ3(10)",
////                "DTLZ4(10)",
////                "Convex_DTLZ2(10)"
////        };
////
////        int[] popsList = {
////                275,
////                275,
////                275,
////                275,
////                275
////        };
////        int[] maxIterationsList = {
////                1500,
////                1500,
////                2000,
////                3000,
////                 5000
////        };
////
////        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////        String[] frontFileList = {
////                frontDir + "DTLZ1.10D.pf[275]",
////                frontDir + "DTLZ2.10D.pf[275]",
////                frontDir + "DTLZ3.10D.pf[275]",
////                frontDir + "DTLZ4.10D.pf[275]",
////                frontDir + "Convex_DTLZ2.10D.pf[275]"
////        };
////
//////        String[] frontFileList = {
//////                frontDir + "DTLZ1.3D.pf[5050]",
//////                frontDir + "DTLZ1.5D.pf[10626]",
//////                frontDir + "DTLZ1.8D.pf[11440]",
//////                frontDir + "DTLZ1.10D.pf[24310]",
//////                frontDir + "DTLZ2.3D.pf[5050]",
//////                frontDir + "DTLZ2.5D.pf[10626]",
//////                frontDir + "DTLZ2.8D.pf[11440]",
//////                frontDir + "DTLZ2.10D.pf[24310]",
//////                frontDir + "DTLZ3.3D.pf[5050]",
//////                frontDir + "DTLZ3.5D.pf[10626]",
//////                frontDir + "DTLZ3.8D.pf[11440]",
//////                frontDir + "DTLZ3.10D.pf[24310]",
//////                frontDir + "DTLZ4.3D.pf[5050]",
//////                frontDir + "DTLZ4.5D.pf[10626]",
//////                frontDir + "DTLZ4.8D.pf[11440]",
//////                frontDir + "DTLZ4.10D.pf[24310]",
//////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
//////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
//////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
//////        };
////
////        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
////        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
////        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
////        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
////        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
////        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
////        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////
////        Point[] hvRefPointList = {
////                point10Dmin,
////                point10D,
////                point10D,
////                point10D,
////                point10D
////        };
////
////        double[] hvCubeList = {
////                0.99999999973088555445326278659612,
////
////                1023.9975096054298072798399842016,
////
////                1023.9975096054298072798399842016,
////
////                1023.9975096054298072798399842016,
////
////                1023.9999999999999957910347537283
////
////        };
//
////        String[] problemNameList = {
////                "Convex_DTLZ2(8)"
////        };
////
////        int[] popsList = {
////                156
////        };
////        int[] maxIterationsList = {
////                3000
////        };
////
////        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////        String[] frontFileList = {
////                frontDir + "Convex_DTLZ2.8D.pf[156]"
////        };
////
//////        String[] frontFileList = {
//////                frontDir + "DTLZ1.3D.pf[5050]",
//////                frontDir + "DTLZ1.5D.pf[10626]",
//////                frontDir + "DTLZ1.8D.pf[11440]",
//////                frontDir + "DTLZ1.10D.pf[24310]",
//////                frontDir + "DTLZ2.3D.pf[5050]",
//////                frontDir + "DTLZ2.5D.pf[10626]",
//////                frontDir + "DTLZ2.8D.pf[11440]",
//////                frontDir + "DTLZ2.10D.pf[24310]",
//////                frontDir + "DTLZ3.3D.pf[5050]",
//////                frontDir + "DTLZ3.5D.pf[10626]",
//////                frontDir + "DTLZ3.8D.pf[11440]",
//////                frontDir + "DTLZ3.10D.pf[24310]",
//////                frontDir + "DTLZ4.3D.pf[5050]",
//////                frontDir + "DTLZ4.5D.pf[10626]",
//////                frontDir + "DTLZ4.8D.pf[11440]",
//////                frontDir + "DTLZ4.10D.pf[24310]",
//////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
//////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
//////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
//////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
//////        };
////
////        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
////        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
////        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
////        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
////        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
////        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
////        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
////        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
////
////        Point[] hvRefPointList = {
////                point8D
////        };
////
////        double[] hvCubeList = {
////                255.99999997944442388886833331278
////
////        };
//
//        String[] algorithmNameList = {
//                "MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
//        };
//
////        String dataDir = "E://ResultsMaOPMeasure/";
////        String statDir = "E://ResultsMaOPMeasure/stat1/";
//        String dataDir = "E://ResultsMaOPTaoT2/";
//        String statDir = "E://ResultsMaOPTaoT2/";
//        int maxRun = 20;
//        executeFinalHV(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,hvRefPointList,hvCubeList,algorithmNameList);
//    }


    public void executeIGD(String dataDir,String statDir,int maxRun,String[] problemNameList,int[] popsList,int[] maxIterationsList,String[] frontFileList,String[] algorithmNameList){

        InvertedGenerationalDistance igdIndicator = new InvertedGenerationalDistance();
        MyExperimentAnalysis analysis = new MyExperimentAnalysis(dataDir,"");
        ReadDoubleDataFile loadData = new ReadDoubleDataFile();
        try {
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                Front referenceFront = new ArrayFront(frontFileList[iProblem]);
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                    Vector<Vector<Double>> igd = new Vector<Vector<Double>>();
                    Vector<Integer> Gen = new Vector<>();
                    String genFile = dataDir+"/GenData/gen_" + instance +".csv";
                    try{
                        double[] tmp = loadData.readFile(genFile);
                        for(int m = 0;m<tmp.length;++m)
                            Gen.add((int)tmp[m]);
                    }catch (FileNotFoundException e){
                        JMetalLogger.logger.info("Can not find file \""+genFile+"\"");
                    }
                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        Vector<Double> runIGD = new Vector<>();
                        for(int p = 0;p < Gen.size();p++){
                            String solutionPF = dataDir+"/GenData/" + instance + "R" + kRun + "_G_"+Gen.get(p)+".pof";
                            ArrayFront solutionFront = new ArrayFront(solutionPF);
                            runIGD.add(igdIndicator.invertedGenerationalDistance(solutionFront,referenceFront));
                        }
                        Generates.add(Gen);
                        igd.add(runIGD);
                    }

                    Vector<Double> statIGD = new Vector<>();
                    Vector<Double> trendIGD = new Vector<>();
                    analysis.analyzeIndicator(igd,statIGD,trendIGD);
                    Vector<Double> finalIGD = new Vector();
                    for(int kRun=0;kRun<maxRun;++kRun)
                        finalIGD.add(igd.get(kRun).get(Gen.size()-1));
                    JMetalLogger.logger.info("IGD :\t[min  "+statIGD.get(0)+"]"+statIGD.get(1)+"\t[avg]"+statIGD.get(4)+"\t[median]"+statIGD.get(5)+"\t[max "+statIGD.get(2)+"]"+statIGD.get(3));

                    try {

                        BufferedWriter writerIGD = new DefaultFileOutputContext(statDir+"/IGD/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,igd,writerIGD);
                        writerIGD.close();
                        BufferedWriter writerTrend = new DefaultFileOutputContext(statDir+"/IGD/trend_"+instance+".csv").getFileWriter();
                        analysis.saveIndicator(Generates.get(0),trendIGD,writerTrend);
                        writerTrend.close();
                        BufferedWriter writerStatIGD = new DefaultFileOutputContext(statDir+"/IGD/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statIGD, writerStatIGD);
                        writerStatIGD.close();
                        BufferedWriter writerFinalIGD = new DefaultFileOutputContext(statDir+"/IGD/final_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(finalIGD, writerFinalIGD);
                        writerFinalIGD.close();
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }

    public void executeFinalIGD(String dataDir,String statDir,int maxRun,String[] problemNameList,int[] popsList,int[] maxIterationsList,String[] frontFileList,String[] algorithmNameList){

        InvertedGenerationalDistance igdIndicator = new InvertedGenerationalDistance();
        MyExperimentAnalysis analysis = new MyExperimentAnalysis(dataDir,"");
        try {
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                Front referenceFront = new ArrayFront(frontFileList[iProblem]);
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                    Vector<Vector<Double>> igd = new Vector<Vector<Double>>();

                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        Vector<Double> runIGD = new Vector<>();
                        Vector<Integer> runGen = new Vector<>();
                        String solutionPF = dataDir+"/POF/" + instance + "R" + kRun + ".pof";
                        Front solutionFront = new ArrayFront(solutionPF);
                        runGen.add(maxIterationsList[iProblem]);
                        runIGD.add(igdIndicator.invertedGenerationalDistance(solutionFront,referenceFront));
                        Generates.add(runGen);
                        igd.add(runIGD);
                    }

                    Vector<Double> finalIGD = new Vector<>();
                    for (int i=0;i<igd.size();i++)
                        finalIGD.add(igd.get(i).get(igd.get(i).size() -1));

                    Vector<Double> statIGD = new Vector<>();
                    analysis.getStatistics(igd,statIGD);

                    JMetalLogger.logger.info("IGD :\t[min  "+statIGD.get(0)+"]"+statIGD.get(1)+"\t[avg]"+statIGD.get(4)+"\t[median]"+statIGD.get(5)+"\t[max "+statIGD.get(2)+"]"+statIGD.get(3));

                    try {
                        BufferedWriter writerIGD = new DefaultFileOutputContext(statDir+"/IGD/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,igd,writerIGD);
                        writerIGD.close();
                        BufferedWriter writerStatIGD = new DefaultFileOutputContext(statDir+"/IGD/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statIGD, writerStatIGD);
                        writerStatIGD.close();
                        BufferedWriter writerFinalIGD = new DefaultFileOutputContext(statDir+"/IGD/final_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(finalIGD, writerFinalIGD);
                        writerFinalIGD.close();
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }


    public void executeIGDPlus(String dataDir,String statDir,int maxRun,String[] problemNameList,int[] popsList,int[] maxIterationsList,String[] frontFileList,String[] algorithmNameList){

        InvertedGenerationalDistancePlus igdPlusIndicator = new InvertedGenerationalDistancePlus();

        MyExperimentAnalysis analysis = new MyExperimentAnalysis(dataDir,"");
        ReadDoubleDataFile loadData = new ReadDoubleDataFile();
        try {
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                Front referenceFront = new ArrayFront(frontFileList[iProblem]);
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                    Vector<Vector<Double>> igdPlus = new Vector<Vector<Double>>();
                    Vector<Integer> Gen = new Vector<>();
                    String genFile = dataDir+"/GenData/gen_" + instance +".csv";
                    try{
                        double[] tmp = loadData.readFile(genFile);
                        for(int m = 0;m<tmp.length;++m)
                            Gen.add((int)tmp[m]);
                    }catch (FileNotFoundException e){
                        JMetalLogger.logger.info("Can not find file \""+genFile+"\"");
                    }
                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        Vector<Double> runIGDPlus = new Vector<>();
                        for(int p = 0;p < Gen.size();p++){
                            String solutionPF = dataDir+"/GenData/" + instance + "R" + kRun + "_G_"+Gen.get(p)+".pof";
                            ArrayFront solutionFront = new ArrayFront(solutionPF);
                            runIGDPlus.add(igdPlusIndicator.invertedGenerationalDistancePlus(solutionFront,referenceFront));
                        }
                        Generates.add(Gen);
                        igdPlus.add(runIGDPlus);
                    }

                    Vector<Double> statIGDPlus= new Vector<>();
                    Vector<Double> trendIGDPlus = new Vector<>();
                    analysis.analyzeIndicator(igdPlus,statIGDPlus,trendIGDPlus);
                    Vector<Double> finalIGDPlus = new Vector();
                    for(int kRun=0;kRun<maxRun;++kRun)
                        finalIGDPlus.add(igdPlus.get(kRun).get(Gen.size()-1));
                    JMetalLogger.logger.info("IGDPlus :\t[min  "+statIGDPlus.get(0)+"]"+statIGDPlus.get(1)+"\t[avg]"+statIGDPlus.get(4)+"\t[median]"+statIGDPlus.get(5)+"\t[max "+statIGDPlus.get(2)+"]"+statIGDPlus.get(3));

                    try {

                        BufferedWriter writerIGDPlus = new DefaultFileOutputContext(statDir+"/IGDPlus/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,igdPlus,writerIGDPlus);
                        writerIGDPlus.close();
                        BufferedWriter writerTrend = new DefaultFileOutputContext(statDir+"/IGDPlus/trend_"+instance+".csv").getFileWriter();
                        analysis.saveIndicator(Generates.get(0),trendIGDPlus,writerTrend);
                        writerTrend.close();
                        BufferedWriter writerStatIGDPlus = new DefaultFileOutputContext(statDir+"/IGDPlus/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statIGDPlus, writerStatIGDPlus);
                        writerStatIGDPlus.close();
                        BufferedWriter writerFinalIGDPlus = new DefaultFileOutputContext(statDir+"/IGDPlus/final_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(finalIGDPlus, writerFinalIGDPlus);
                        writerFinalIGDPlus.close();
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }

    public void executeFinalIGDPlus(String dataDir,String statDir,int maxRun,String[] problemNameList,int[] popsList,int[] maxIterationsList,String[] frontFileList,String[] algorithmNameList){

        InvertedGenerationalDistancePlus igdPlusIndicator = new InvertedGenerationalDistancePlus();
        MyExperimentAnalysis analysis = new MyExperimentAnalysis(dataDir,"");
        try {
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                Front referenceFront = new ArrayFront(frontFileList[iProblem]);
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                    Vector<Vector<Double>> igdPlus = new Vector<Vector<Double>>();

                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        Vector<Double> runIGDPlus = new Vector<>();
                        Vector<Integer> runGen = new Vector<>();
                        String solutionPF = dataDir+"/POF/" + instance + "R" + kRun + ".pof";
                        Front solutionFront = new ArrayFront(solutionPF);
                        runGen.add(maxIterationsList[iProblem]);
                        runIGDPlus.add(igdPlusIndicator.invertedGenerationalDistancePlus(solutionFront,referenceFront));
                        Generates.add(runGen);
                        igdPlus.add(runIGDPlus);
                    }
                    Vector<Double> finalIGDPlus = new Vector<>();
                    for (int i=0;i<igdPlus.size();i++)
                        finalIGDPlus.add(igdPlus.get(i).get(igdPlus.get(i).size() -1));

                    Vector<Double> statIGDPlus = new Vector<>();
                    analysis.getStatistics(igdPlus,statIGDPlus);

                    JMetalLogger.logger.info("IGDPlus :\t[min  "+statIGDPlus.get(0)+"]"+statIGDPlus.get(1)+"\t[avg]"+statIGDPlus.get(4)+"\t[median]"+statIGDPlus.get(5)+"\t[max "+statIGDPlus.get(2)+"]"+statIGDPlus.get(3));

                    try {
                        BufferedWriter writerIGDPlus = new DefaultFileOutputContext(statDir+"/IGDPlus/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,igdPlus,writerIGDPlus);
                        writerIGDPlus.close();
                        BufferedWriter writerStatIGDPlus = new DefaultFileOutputContext(statDir+"/IGDPlus/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statIGDPlus, writerStatIGDPlus);
                        writerStatIGDPlus.close();
                        BufferedWriter writerFinalIGDPlus = new DefaultFileOutputContext(statDir+"/IGDPlus/final_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(finalIGDPlus, writerFinalIGDPlus);
                        writerFinalIGDPlus.close();
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }

    public void executeHV(String dataDir,String statDir,int maxRun,String[] problemNameList,int[] objNumList, int[] popsList,int[] maxIterationsList,Point[] hvRefPointList,Point[] boundPointList,double[] hvCube,int numOfSample,int aDim,String[] algorithmNameList){

        WFGHypervolume hvIndicator = new WFGHypervolume();
        ApproximateHypervolume ahvIndicator = null;
//        ahvIndicator.setMiniming();
        MyExperimentAnalysis analysis = new MyExperimentAnalysis(dataDir,"");
        ReadDoubleDataFile loadData = new ReadDoubleDataFile();
        try {
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
//                JMetalLogger.logger.info("hvCude : "+hvCube[iProblem]);
                if(objNumList[iProblem] >= aDim){
                    ahvIndicator = new ApproximateHypervolume();
                    ahvIndicator.setSamplePoints(numOfSample,hvRefPointList[iProblem],boundPointList[iProblem]);
                }
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                    Vector<Vector<Double>> HV = new Vector<Vector<Double>>();
                    Vector<Integer> Gen = new Vector<>();
                    String genFile = dataDir+"/GenData/gen_" + instance +".csv";
                    try{
                        double[] tmp = loadData.readFile(genFile);
                        for(int m = 0;m<tmp.length;++m)
                            Gen.add((int)tmp[m]);
                    }catch (FileNotFoundException e){
                        JMetalLogger.logger.info("Can not find file \""+genFile+"\"");
                    }
                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        Vector<Double> runHV = new Vector<>();
                        for(int p = 0;p < Gen.size();p++){
                            String solutionPF = dataDir+"/GenData/" + instance + "R" + kRun + "_G_"+Gen.get(p)+".pof";
                            WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                            if(objNumList[iProblem] < aDim) {
                                runHV.add(hvIndicator.evaluate(solutionFront, hvRefPointList[iProblem]));
                            }else{
//                                ahvIndicator = new ApproximateHypervolume();
//                                ahvIndicator.setSamplePoints(numOfSample,hvRefPointList[iProblem],boundPointList[iProblem]);
                                runHV.add(ahvIndicator.evaluate(solutionFront, hvRefPointList[iProblem]));
                            }
                        }
                        Generates.add(Gen);
                        HV.add(runHV);
                    }
                    Vector<Vector<Double>> HE = new Vector<Vector<Double>>();
                    for(int kRun = 0;kRun < maxRun;++kRun){
                        Vector<Double> runHE = new Vector<>();
                        for(int p = 0;p<HV.get(kRun).size();++p) {
                            double r = hvCube[iProblem] - HV.get(kRun).get(p);
                            runHE.add(r<0?0:r);
                        }
                        HE.add(runHE);
                    }
                    Vector<Double> statHV = new Vector<>();
                    Vector<Double> trendHV = new Vector<>();
                    analysis.analyzeIndicator(HV,statHV,trendHV);
                    Vector<Double> finalHV = new Vector();
                    for(int kRun=0;kRun<maxRun;++kRun)
                        finalHV.add(HV.get(kRun).get(Gen.size()-1));

                    Vector<Double> statHE = new Vector<>();
                    Vector<Double> trendHE = new Vector<>();
                    analysis.analyzeIndicator(HE,statHE,trendHE);
                    Vector<Double> finalHE = new Vector();
                    for(int kRun=0;kRun<maxRun;++kRun)
                        finalHE.add(HE.get(kRun).get(Gen.size()-1));
                    JMetalLogger.logger.info("HV :\t[min  "+statHV.get(0)+"]"+statHV.get(1)+"\t[avg]"+statHV.get(4)+"\t[median]"+statHV.get(5)+"\t[max "+statHV.get(2)+"]"+statHV.get(3));
                    JMetalLogger.logger.info("HE :\t[min  "+statHE.get(0)+"]"+statHE.get(1)+"\t[avg]"+statHE.get(4)+"\t[median]"+statHE.get(5)+"\t[max "+statHE.get(2)+"]"+statHE.get(3));

                    try {

                        BufferedWriter writerHV = new DefaultFileOutputContext(statDir+"/HV/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,HV,writerHV);
                        writerHV.close();
                        BufferedWriter writerTrendHV = new DefaultFileOutputContext(statDir+"/HV/trend_"+instance+".csv").getFileWriter();
                        analysis.saveIndicator(Generates.get(0),trendHV,writerTrendHV);
                        writerTrendHV.close();
                        BufferedWriter writerStatHV = new DefaultFileOutputContext(statDir+"/HV/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statHV, writerStatHV);
                        writerStatHV.close();
                        BufferedWriter writerFinalHV = new DefaultFileOutputContext(statDir+"/HV/final_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(finalHV, writerFinalHV);
                        writerFinalHV.close();

                        BufferedWriter writerHE = new DefaultFileOutputContext(statDir+"/HE/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,HE,writerHE);
                        writerHE.close();
                        BufferedWriter writerTrendHE = new DefaultFileOutputContext(statDir+"/HE/trend_"+instance+".csv").getFileWriter();
                        analysis.saveIndicator(Generates.get(0),trendHE,writerTrendHE);
                        writerTrendHE.close();
                        BufferedWriter writerStatHE = new DefaultFileOutputContext(statDir+"/HE/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statHE, writerStatHE);
                        writerStatHE.close();
                        BufferedWriter writerFinalHE = new DefaultFileOutputContext(statDir+"/HE/final_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(finalHE, writerFinalHE);
                        writerFinalHE.close();

                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }

    public void executeNormalizedHV(String dataDir,String statDir,int maxRun,String[] problemNameList,int[] objNumList, int[] popsList,int[] maxIterationsList,Point[] nadirPointList,Point[] boundPointList,int numOfSample,int aDim,String[] algorithmNameList){

        WFGHypervolume hvIndicator = new WFGHypervolume();
        ApproximateHypervolume ahvIndicator = null;
//        ahvIndicator.setMiniming();
        MyExperimentAnalysis analysis = new MyExperimentAnalysis(dataDir,"");
        ReadDoubleDataFile loadData = new ReadDoubleDataFile();
        try {
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                Point refPoint = new ArrayPoint(objNumList[iProblem]);
                Point zeroPoint = new ArrayPoint(objNumList[iProblem]);
                double hvCube = 1.0;
                for (int i=0;i<objNumList[iProblem];i++) {
                    refPoint.setDimensionValue(i, 1.1);
                    zeroPoint.setDimensionValue(i,0.0);
                    hvCube *= 1.1;
                }
                if(objNumList[iProblem] >= aDim){
                    ahvIndicator.setSamplePoints(numOfSample,refPoint,zeroPoint);
                }

                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                    Vector<Vector<Double>> HV = new Vector<Vector<Double>>();
                    Vector<Integer> Gen = new Vector<>();
                    String genFile = dataDir+"/GenData/gen_" + instance +".csv";
                    try{
                        double[] tmp = loadData.readFile(genFile);
                        for(int m = 0;m<tmp.length;++m)
                            Gen.add((int)tmp[m]);
                    }catch (FileNotFoundException e){
                        JMetalLogger.logger.info("Can not find file \""+genFile+"\"");
                    }
                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        Vector<Double> runHV = new Vector<>();
                        for(int p = 0;p < Gen.size();p++){
                            String solutionPF = dataDir+"/GenData/" + instance + "R" + kRun + "_G_"+Gen.get(p)+".pof";
                            WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                            for (int i=0;i<solutionFront.getNumberOfPoints();i++){
                                Point point = solutionFront.getPoint(i);
                                for (int j=0;j<solutionFront.getPointDimensions();j++){
                                    point.setDimensionValue(j,(point.getDimensionValue(j) - boundPointList[iProblem].getDimensionValue(j))/(nadirPointList[iProblem].getDimensionValue(j) - boundPointList[iProblem].getDimensionValue(j)));
                                }
                                solutionFront.setPoint(i,point);
                            }

                            if(objNumList[iProblem] < aDim) {
                                runHV.add(hvIndicator.evaluate(solutionFront, refPoint)/hvCube);
                            }else{
                                runHV.add(ahvIndicator.evaluate(solutionFront, refPoint)/hvCube);
                            }
                        }
                        Generates.add(Gen);
                        HV.add(runHV);
                    }

                    Vector<Double> statHV = new Vector<>();
                    Vector<Double> trendHV = new Vector<>();
                    analysis.analyzeIndicator(HV,statHV,trendHV);
                    Vector<Double> finalHV = new Vector();
                    for(int kRun=0;kRun<maxRun;++kRun)
                        finalHV.add(HV.get(kRun).get(Gen.size()-1));

                    JMetalLogger.logger.info("HV :\t[min  "+statHV.get(0)+"]"+statHV.get(1)+"\t[avg]"+statHV.get(4)+"\t[median]"+statHV.get(5)+"\t[max "+statHV.get(2)+"]"+statHV.get(3));

                    try {

                        BufferedWriter writerHV = new DefaultFileOutputContext(statDir+"/HV/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,HV,writerHV);
                        writerHV.close();
                        BufferedWriter writerTrendHV = new DefaultFileOutputContext(statDir+"/HV/trend_"+instance+".csv").getFileWriter();
                        analysis.saveIndicator(Generates.get(0),trendHV,writerTrendHV);
                        writerTrendHV.close();
                        BufferedWriter writerStatHV = new DefaultFileOutputContext(statDir+"/HV/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statHV, writerStatHV);
                        writerStatHV.close();
                        BufferedWriter writerFinalHV = new DefaultFileOutputContext(statDir+"/HV/final_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(finalHV, writerFinalHV);
                        writerFinalHV.close();

                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }

    public void executeFinalHV(String dataDir,String statDir,int maxRun,String[] problemNameList,int[] objNumList,int[] popsList,int[] maxIterationsList,Point[] hvRefPointList,Point[] boundPointList,double[] hvCube,int numOfSample,int aDim,String[] algorithmNameList){

        WFGHypervolume hvIndicator = new WFGHypervolume();
        ApproximateHypervolume ahvIndicator = new ApproximateHypervolume();
//        ahvIndicator.setMiniming();
        MyExperimentAnalysis analysis = new MyExperimentAnalysis(dataDir,"");
        try {
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                if(objNumList[iProblem] >= aDim){
                    ahvIndicator.setSamplePoints(numOfSample,hvRefPointList[iProblem],boundPointList[iProblem]);
                }
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                    Vector<Vector<Double>> HV = new Vector<Vector<Double>>();
                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        Vector<Double> runHV = new Vector<>();
                        Vector<Integer> runGen = new Vector<>();
                        runGen.add(maxIterationsList[iProblem]);
                        String solutionPF = dataDir+"/POF/" + instance + "R" + kRun + ".pof";
                        WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                        if(objNumList[iProblem] < aDim) {
                            runHV.add(hvIndicator.evaluate(solutionFront, hvRefPointList[iProblem]));
                        }else{
                            runHV.add(ahvIndicator.evaluate(solutionFront, hvRefPointList[iProblem]));
                        }
                        Generates.add(runGen);
                        HV.add(runHV);
                    }

                    Vector<Vector<Double>> HE = new Vector<Vector<Double>>();
                    for(int kRun = 0;kRun < maxRun;++kRun){
                        Vector<Double> runHE = new Vector<>();
                        for(int p = 0;p<HV.get(kRun).size();++p)
                            runHE.add(hvCube[iProblem] - HV.get(kRun).get(p));
                        HE.add(runHE);
                    }

                    Vector<Double> finalHV = new Vector<>();
                    for (int i=0;i<HV.size();i++)
                        finalHV.add(HV.get(i).get(HV.get(i).size() -1));
                    Vector<Double> statHV = new Vector<>();
                    analysis.getStatistics(HV,statHV);

                    Vector<Double> finalHE = new Vector<>();
                    for (int i=0;i<HE.size();i++)
                        finalHE.add(HE.get(i).get(HE.get(i).size() -1));
                    Vector<Double> statHE = new Vector<>();
                    analysis.getStatistics(HE,statHE);
                    JMetalLogger.logger.info("HV :\t[min  "+statHV.get(0)+"]"+statHV.get(1)+"\t[avg]"+statHV.get(4)+"\t[median]"+statHV.get(5)+"\t[max "+statHV.get(2)+"]"+statHV.get(3));
                    JMetalLogger.logger.info("HE :\t[min  "+statHE.get(0)+"]"+statHE.get(1)+"\t[avg]"+statHE.get(4)+"\t[median]"+statHE.get(5)+"\t[max "+statHE.get(2)+"]"+statHE.get(3));

                    try {

                        BufferedWriter writerHV = new DefaultFileOutputContext(statDir+"/HV/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,HV,writerHV);
                        writerHV.close();
                        BufferedWriter writerStatHV = new DefaultFileOutputContext(statDir+"/HV/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statHV, writerStatHV);
                        writerStatHV.close();
                        BufferedWriter writerFinalHV = new DefaultFileOutputContext(statDir+"/HV/final_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(finalHV, writerFinalHV);
                        writerFinalHV.close();

                        BufferedWriter writerHE = new DefaultFileOutputContext(statDir+"/HE/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,HE,writerHE);
                        writerHE.close();
                        BufferedWriter writerStatHE = new DefaultFileOutputContext(statDir+"/HE/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statHE, writerStatHE);
                        writerStatHE.close();
                        BufferedWriter writerFinalHE = new DefaultFileOutputContext(statDir+"/HE/final_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(finalHE, writerFinalHE);
                        writerFinalHE.close();
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }

    public void executeFinalNormalizedHV(String dataDir,String statDir,int maxRun,String[] problemNameList,int[] objNumList,int[] popsList,int[] maxIterationsList,Point[] nadirPointList,Point[] idealPointList,int numOfSample,int aDim,String[] algorithmNameList){

        WFGHypervolume hvIndicator = new WFGHypervolume();
        ApproximateHypervolume ahvIndicator = new ApproximateHypervolume();
        ahvIndicator.setMiniming();
        MyExperimentAnalysis analysis = new MyExperimentAnalysis(dataDir,"");
        try {
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                Point refPoint = new ArrayPoint(objNumList[iProblem]);
                Point zeroPoint = new ArrayPoint(objNumList[iProblem]);
                double hvCube = 1.0;
                for (int i=0;i<objNumList[iProblem];i++) {
                    refPoint.setDimensionValue(i, 1.1);
                    zeroPoint.setDimensionValue(i,0.0);
                    hvCube *= 1.1;
                }
                if(objNumList[iProblem] >= aDim){
                    ahvIndicator.setSamplePoints(numOfSample,refPoint,zeroPoint);
                }
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                    Vector<Vector<Double>> HV = new Vector<Vector<Double>>();
                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        Vector<Double> runHV = new Vector<>();
                        Vector<Integer> runGen = new Vector<>();
                        runGen.add(maxIterationsList[iProblem]);
                        String solutionPF = dataDir+"/POF/" + instance + "R" + kRun + ".pof";
                        WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                        for (int i=0;i<solutionFront.getNumberOfPoints();i++){
                            Point point = solutionFront.getPoint(i);
                            for (int j=0;j<solutionFront.getPointDimensions();j++){
                                point.setDimensionValue(j,(point.getDimensionValue(j) - idealPointList[iProblem].getDimensionValue(j))/(nadirPointList[iProblem].getDimensionValue(j) - idealPointList[iProblem].getDimensionValue(j)));
                            }
                            solutionFront.setPoint(i,point);
                        }

                        if(objNumList[iProblem] < aDim) {
                            runHV.add(hvIndicator.evaluate(solutionFront, refPoint)/hvCube);
                        }else{
                            runHV.add(ahvIndicator.evaluate(solutionFront, refPoint)/hvCube);
                        }
                        Generates.add(runGen);
                        HV.add(runHV);
                    }

                    Vector<Double> finalHV = new Vector<>();
                    for (int i=0;i<HV.size();i++)
                        finalHV.add(HV.get(i).get(HV.get(i).size() -1));
                    Vector<Double> statHV = new Vector<>();
                    analysis.getStatistics(HV,statHV);
                    JMetalLogger.logger.info("HV :\t[min  "+statHV.get(0)+"]"+statHV.get(1)+"\t[avg]"+statHV.get(4)+"\t[median]"+statHV.get(5)+"\t[max "+statHV.get(2)+"]"+statHV.get(3));

                    try {

                        BufferedWriter writerHV = new DefaultFileOutputContext(statDir+"/HV/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,HV,writerHV);
                        writerHV.close();
                        BufferedWriter writerStatHV = new DefaultFileOutputContext(statDir+"/HV/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statHV, writerStatHV);
                        writerStatHV.close();
                        BufferedWriter writerFinalHV = new DefaultFileOutputContext(statDir+"/HV/final_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(finalHV, writerFinalHV);
                        writerFinalHV.close();
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }

    public void executeFinalSpread(String dataDir,String statDir,int maxRun,String[] problemNameList,int[] popsList,int[] maxIterationsList,String[] frontFileList,String[] algorithmNameList){

        GeneralizedSpread spreadIndicator = new GeneralizedSpread();
        MyExperimentAnalysis analysis = new MyExperimentAnalysis(dataDir,"");
        try {
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                Front referenceFront = new ArrayFront(frontFileList[iProblem]);
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                    Vector<Vector<Double>> spread = new Vector<Vector<Double>>();

                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        Vector<Double> runSpread = new Vector<>();
                        Vector<Integer> runGen = new Vector<>();
                        String solutionPF = dataDir+"/POF/" + instance + "R" + kRun + ".pof";
                        Front solutionFront = new ArrayFront(solutionPF);
                        runGen.add(maxIterationsList[iProblem]);
                        runSpread.add(spreadIndicator.generalizedSpread(solutionFront,referenceFront));
                        Generates.add(runGen);
                        spread.add(runSpread);
                    }
                    Vector<Double> finalSpread= new Vector<>();
                    for (int i=0;i<spread.size();i++)
                        finalSpread.add(spread.get(i).get(spread.get(i).size() -1));

                    Vector<Double> statSpread = new Vector<>();
                    analysis.getStatistics(spread,statSpread);

                    JMetalLogger.logger.info("Spread :\t[min  "+statSpread.get(0)+"]"+statSpread.get(1)+"\t[avg]"+statSpread.get(4)+"\t[median]"+statSpread.get(5)+"\t[max "+statSpread.get(2)+"]"+statSpread.get(3));

                    try {
                        BufferedWriter writerSpread = new DefaultFileOutputContext(statDir+"/SPREAD/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,spread,writerSpread);
                        writerSpread.close();
                        BufferedWriter writerStatSpread = new DefaultFileOutputContext(statDir+"/SPREAD/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statSpread, writerStatSpread);
                        writerStatSpread.close();
                        BufferedWriter writerFinalSpread = new DefaultFileOutputContext(statDir+"/SPREAD/final_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(finalSpread, writerFinalSpread);
                        writerFinalSpread.close();
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}
    }


    public void loadStat(String dataFile,Vector<Vector<Integer>> Generates,Vector<Vector<Double>> STAT){
        try {
        InputStream inputStream = getClass().getResourceAsStream(dataFile);
        if (inputStream == null) {
            inputStream = new FileInputStream(dataFile);
        }
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(isr);

        String line ;

            line = br.readLine();

            while (line != null) {
                StringTokenizer tokenizer = new StringTokenizer(line,",");
                while (tokenizer.hasMoreTokens()) {
                    Vector<Double> runStat = new Vector<>();
                    Vector<Integer> runGen = new Vector<>();
                    runGen.add(new Integer(tokenizer.nextToken()));
                    runStat.add(new Double(tokenizer.nextToken()));
                    Generates.add(runGen);
                    STAT.add(runStat);
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            throw new JMetalException("Error reading file", e);
        } catch (NumberFormatException e) {
            throw new JMetalException("Format number exception when reading file", e);
        }
    }
    public void executeIGDStat(String statDir,int maxRun,String[] problemNameList,int[] popsList,int[] maxIterationsList,String[] algorithmNameList){
        MyExperimentAnalysis analysis = new MyExperimentAnalysis(statDir,"");
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                    Vector<Vector<Double>> IGD = new Vector<Vector<Double>>();
                    String dataFile = statDir+"/IGD/"+instance+".csv";
                    loadStat(dataFile,Generates,IGD);

                    Vector<Double> statIGD = new Vector<>();
                    analysis.getStatistics(IGD,statIGD);

                    JMetalLogger.logger.info("IGD :\t[min  "+statIGD.get(0)+"]"+statIGD.get(1)+"\t[avg]"+statIGD.get(4)+"\t[median]"+statIGD.get(5)+"\t[max "+statIGD.get(2)+"]"+statIGD.get(3));

                    try {
                        BufferedWriter writerStatIGD = new DefaultFileOutputContext(statDir+"/IGD/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statIGD, writerStatIGD);
                        writerStatIGD.close();
                    }catch (IOException e){}
                }
            }
    }



    public void executeHVStat(String statDir,int maxRun,String[] problemNameList,int[] popsList,int[] maxIterationsList,double[] hvCube,String[] algorithmNameList){
        MyExperimentAnalysis analysis = new MyExperimentAnalysis(statDir,"");
        for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
            for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                JMetalLogger.logger.info(instance);
                Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                Vector<Vector<Double>> HV = new Vector<Vector<Double>>();
                String dataFile = statDir+"/HV/"+instance+".csv";
                loadStat(dataFile,Generates,HV);

                Vector<Vector<Double>> HE = new Vector<Vector<Double>>();
                for(int kRun = 0;kRun < maxRun;++kRun){
                    Vector<Double> runHE = new Vector<>();
                    for(int p = 0;p<HV.get(kRun).size();++p)
                        runHE.add(hvCube[iProblem] - HV.get(kRun).get(p));
                    HE.add(runHE);
                }

                Vector<Double> statHV = new Vector<>();
                analysis.getStatistics(HV,statHV);

                Vector<Double> statHE = new Vector<>();
                analysis.getStatistics(HE,statHE);
                JMetalLogger.logger.info("HV :\t[min  "+statHV.get(0)+"]"+statHV.get(1)+"\t[avg]"+statHV.get(4)+"\t[median]"+statHV.get(5)+"\t[max "+statHV.get(2)+"]"+statHV.get(3));
                JMetalLogger.logger.info("HE :\t[min  "+statHE.get(0)+"]"+statHE.get(1)+"\t[avg]"+statHE.get(4)+"\t[median]"+statHE.get(5)+"\t[max "+statHE.get(2)+"]"+statHE.get(3));

                try {
                    BufferedWriter writerStatHV = new DefaultFileOutputContext(statDir+"/HV/stat_" + instance + ".csv").getFileWriter();
                    analysis.saveIndicator(statHV, writerStatHV);
                    writerStatHV.close();

                    BufferedWriter writerHE = new DefaultFileOutputContext(statDir+"/HE/" + instance + ".csv").getFileWriter();
                    analysis.saveIndicators(Generates,HE,writerHE);
                    writerHE.close();
                    BufferedWriter writerStatHE = new DefaultFileOutputContext(statDir+"/HE/stat_" + instance + ".csv").getFileWriter();
                    analysis.saveIndicator(statHE, writerStatHE);
                    writerStatHE.close();

                }catch (IOException e){}
            }
        }
    }



    public void execute6(){
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
//
//        double[] hvCubeList = {
//                1330.9791666666666666667,
//                0.99973958333333333333,
//                0.99999990311879960317460317460317,
//                0.99999999973088555445326278659612,
//
//                1330.4764012244017011269228927694534,
//                31.835506593315177356352758483335,
//                255.9841456557561844991477147896,
//                1023.9975096054298072798399842016,
//
//                1330.4764012244017011269228927694534,
//                31.835506593315177356352758483335,
//                255.9841456557561844991477147896,
//                1023.9975096054298072798399842016,
//
//                1330.4764012244017011269228927694534,
//                31.835506593315177356352758483335,
//                255.9841456557561844991477147896,
//                1023.9975096054298072798399842016,
//
//                1330.9666666666666666666666666666667,
//                31.999955908289241622574955908289,
//                255.99999997944442388886833331278,
//                1023.9999999999999957910347537283
//
//        };

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

        double[] hvCubeList = {
                1330.9791666666666666667,
                0.99973958333333333333,
                0.99999990311879960317460317460317,

                1330.4764012244017011269228927694534,
                31.835506593315177356352758483335,
                255.9841456557561844991477147896,

                1330.4764012244017011269228927694534,
                31.835506593315177356352758483335,
                255.9841456557561844991477147896,

                1330.4764012244017011269228927694534,
                31.835506593315177356352758483335,
                255.9841456557561844991477147896,

                1330.9666666666666666666666666666667,
                31.999955908289241622574955908289,
                255.99999997944442388886833331278
        };

        String[] algorithmNameList = {
                "NSGAIII","MOEADDE_PBI","MOEADACD_PBI","MOEAD_PBI","MOEADD_PBI","MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
        };

        String statDir = "E://ResultsMaOPT1/stat/";
        int maxRun = 20;
//        executeIGDStat(statDir,maxRun,problemNameList,popsList,maxIterationsList,algorithmNameList);
        executeHVStat(statDir,maxRun,problemNameList,popsList,maxIterationsList,hvCubeList,algorithmNameList);
    }

    public void execute7(){

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

        double[] hvCubeList = {
                0.99999990311879960317460317460317,

                255.9841456557561844991477147896,

                255.9841456557561844991477147896,

                255.9841456557561844991477147896,

                255.99999997944442388886833331278
        };
//        String[] problemNameList = {
//                "DTLZ1(10)",
//                "DTLZ2(10)",
//                "DTLZ3(10)",
//                "DTLZ4(10)",
//                "Convex_DTLZ2(10)"
//        };
//
//        int[] popsList = {
//                275,
//                275,
//                275,
//                275,
//                275
//        };
//        int[] maxIterationsList = {
//                1000,
//                750,
//                1500,
//                2000,
//                4000
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//        String[] frontFileList = {
//                frontDir + "DTLZ1.10D.pf[275]",
//                frontDir + "DTLZ2.10D.pf[275]",
//                frontDir + "DTLZ3.10D.pf[275]",
//                frontDir + "DTLZ4.10D.pf[275]",
//                frontDir + "Convex_DTLZ2.10D.pf[275]"
//        };
//
////        String[] frontFileList = {
////                frontDir + "DTLZ1.3D.pf[5050]",
////                frontDir + "DTLZ1.5D.pf[10626]",
////                frontDir + "DTLZ1.8D.pf[11440]",
////                frontDir + "DTLZ1.10D.pf[24310]",
////                frontDir + "DTLZ2.3D.pf[5050]",
////                frontDir + "DTLZ2.5D.pf[10626]",
////                frontDir + "DTLZ2.8D.pf[11440]",
////                frontDir + "DTLZ2.10D.pf[24310]",
////                frontDir + "DTLZ3.3D.pf[5050]",
////                frontDir + "DTLZ3.5D.pf[10626]",
////                frontDir + "DTLZ3.8D.pf[11440]",
////                frontDir + "DTLZ3.10D.pf[24310]",
////                frontDir + "DTLZ4.3D.pf[5050]",
////                frontDir + "DTLZ4.5D.pf[10626]",
////                frontDir + "DTLZ4.8D.pf[11440]",
////                frontDir + "DTLZ4.10D.pf[24310]",
////                frontDir + "Convex_DTLZ2.3D.pf[5050]",
////                frontDir + "Convex_DTLZ2.5D.pf[10626]",
////                frontDir + "Convex_DTLZ2.8D.pf[11440]",
////                frontDir + "Convex_DTLZ2.10D.pf[24310]",
////        };
//
//        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
//        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
//        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//
//        Point[] hvRefPointList = {
//                point10Dmin,
//                point10D,
//                point10D,
//                point10D,
//                point10D
//        };
//
//        double[] hvCubeList = {
//                0.99999999973088555445326278659612,
//
//                1023.9975096054298072798399842016,
//
//                1023.9975096054298072798399842016,
//
//                1023.9975096054298072798399842016,
//
//                1023.9999999999999957910347537283
//
//        };

        String[] algorithmNameList = {
                "MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
        };

        String statDir = "E://ResultsMaOPTaoT1/";
        int maxRun = 20;
        executeHVStat(statDir,maxRun,problemNameList,popsList,maxIterationsList,hvCubeList,algorithmNameList);
    }

    public void execute8(){

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


        double[] hvCubeList = {
                0.99999990311879960317460317460317,

                255.9841456557561844991477147896,

                255.9841456557561844991477147896,

                255.9841456557561844991477147896,

                255.99999997944442388886833331278
        };

//        String[] problemNameList = {
//                "DTLZ1(10)",
//                "DTLZ2(10)",
//                "DTLZ3(10)",
//                "DTLZ4(10)",
//                "Convex_DTLZ2(10)"
//        };
//
//        int[] popsList = {
//                275,
//                275,
//                275,
//                275,
//                275
//        };
//        int[] maxIterationsList = {
//                1500,
//                1500,
//                2000,
//                3000,
//                 5000
//        };
//
//        Point[] hvRefPointList = {
//                point10Dmin,
//                point10D,
//                point10D,
//                point10D,
//                point10D
//        };
//
//        double[] hvCubeList = {
//                0.99999999973088555445326278659612,
//
//                1023.9975096054298072798399842016,
//
//                1023.9975096054298072798399842016,
//
//                1023.9975096054298072798399842016,
//
//                1023.9999999999999957910347537283
//
//        };

        String[] algorithmNameList = {
                "MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
        };


        String statDir = "E://ResultsMaOPTaoT2/";
        int maxRun = 20;
        executeHVStat(statDir,maxRun,problemNameList,popsList,maxIterationsList,hvCubeList,algorithmNameList);
    }


    public void execute9(){
        String[] problemNameList = {
                "MOP1(2)","MOP2(2)","MOP3(2)","MOP4(2)","MOP5(2)","MOP6(3)","MOP7(3)"
        };

        int[] popsList = {
                200,200,200,200,200,300,300
        };
//        int[] maxIterationsList = {
//                500,500,500,500,500,750,750
//        };
        int[] maxIterationsList = {
                750,750,750,750,750,500,500
        };

        String[] algorithmNameList = {
                "NSGAIII","MOEADDE_PBI","MOEADACD_PBI","MOEAD_PBI","MOEADD_PBI","MOEACDCPBI2","MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
        };

        double[] hvCubeList = {
                120.666666666666667,

                120.3333333333333333333,

                120.2146018366025516903843391541801,

                120.51777015318746,

                120.666666666666667,

                1330.8333333333333333333333333333,

                1330.4764012244017011269228927694534
        };

        String baseDir2 = "E://ResultsMOPT1/";
        executeIGDStat(baseDir2,20,problemNameList,popsList,maxIterationsList,algorithmNameList);
        executeHVStat(baseDir2,20,problemNameList,popsList,maxIterationsList,hvCubeList,algorithmNameList);
    }


    public void execute10(){

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

        double[] hvCubeList = {
                0.99999999973088555445326278659612,

                1023.9975096054298072798399842016,

                1023.9975096054298072798399842016,

                1023.9975096054298072798399842016,

                1023.9999999999999957910347537283

        };

        String[] algorithmNameList = {
                "MOEACDCPBI","MOEACDCNPBI","MOEACDCMM","MOEACDCAPBI","MOEACDCMMAPBI"
        };


        String statDir = "E://HVTaoT1/";
        int maxRun = 20;
        executeHVStat(statDir,maxRun,problemNameList,popsList,maxIterationsList,hvCubeList,algorithmNameList);
    }

    public void executeMaOPIIaFinal(String dataDir,String statDir,int maxRun){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
        };

        int[] objNumList = {
                3,5,8,
                3,5,8,
                3,5,8,
                3,5,8,
                3,5,8
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

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";

        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[109]",
                frontDir + "DTLZ1.5D.pf[251]",
                frontDir + "DTLZ1.8D.pf[165]",
                frontDir + "DTLZ2.3D.pf[109]",
                frontDir + "DTLZ2.5D.pf[251]",
                frontDir + "DTLZ2.8D.pf[165]",
                frontDir + "DTLZ2.3D.pf[109]",
                frontDir + "DTLZ2.5D.pf[251]",
                frontDir + "DTLZ2.8D.pf[165]",
                frontDir + "DTLZ2.3D.pf[109]",
                frontDir + "DTLZ2.5D.pf[251]",
                frontDir + "DTLZ2.8D.pf[165]",
                frontDir + "Convex_DTLZ2.3D.pf[109]",
                frontDir + "Convex_DTLZ2.5D.pf[251]",
                frontDir + "Convex_DTLZ2.8D.pf[165]"
        };
//
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[15151]",
//                frontDir + "DTLZ1.5D.pf[25081]",
//                frontDir + "DTLZ1.8D.pf[43713]",
//                frontDir + "DTLZ2.3D.pf[15151]",
//                frontDir + "DTLZ2.5D.pf[25081]",
//                frontDir + "DTLZ2.8D.pf[43713]",
//                frontDir + "DTLZ2.3D.pf[15151]",
//                frontDir + "DTLZ2.5D.pf[25081]",
//                frontDir + "DTLZ2.8D.pf[43713]",
//                frontDir + "DTLZ2.3D.pf[15151]",
//                frontDir + "DTLZ2.5D.pf[25081]",
//                frontDir + "DTLZ2.8D.pf[43713]",
//                frontDir + "Convex_DTLZ2.3D.pf[15151]",
//                frontDir + "Convex_DTLZ2.5D.pf[25081]",
//                frontDir + "Convex_DTLZ2.8D.pf[43713]"
//        };

        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});

        Point[] hvRefPointList = {
                point3Dmin,point5Dmin, point8Dmin,
                point3D,point5D, point8D,
                point3D,point5D, point8D,
                point3D,point5D, point8D,
                point3D,point5D, point8D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D
        };

        double[] hvCubeList = {
                0.322166666666666601237523082091,
                0.167809583333333262045172773469,
                0.0576479131187995763596632059489,
//                0.0282475246308855378052360407537,
//                0.00474756150994297236589414268337,

                0.807401224401701478328163830156,
                1.44601659331517806705846851401,
                2.12773446575618585541178617859,
//                2.59125206552980946739239698218,
//                4.177236528690534\374558916402748,

                0.807401224401701478328163830156,
                1.44601659331517806705846851401,
                2.12773446575618585541178617859,
//                2.59125206552980946739239698218,
//                4.177236528690534\374558916402748,

                0.807401224401701478328163830156,
                1.44601659331517806705846851401,
                2.12773446575618585541178617859,
//                2.59125206552980946739239698218,
//                4.177236528690534\374558916402748,

                1.33100000000000040500935938326,
                1.61051000000000077427841915778,
                2.14358881000000156547002916341
//                ,
//                2.59374246010000231166259254678,
//                4.17724816941565624262011624523
        };
        String[] algorithmNameList = {
                "NSGAIII", "MOEAD_PBI", "MOEADDE_PBI", "MOEADACD_PBI", "MOEADD_PBI"
        };

        int numOfSample = 1000000;
        int approximateDim = 10;

        executeFinalIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList, frontFileList,algorithmNameList);
//        executeFinalHV(dataDir,statDir,maxRun,problemNameList, objNumList,popsList, maxIterationsList, hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
    }

    public void executeMaOPIIbFinal(String dataDir,String statDir,int maxRun) {
        String[] problemNameList = {
                "DTLZ1(10)","DTLZ1(15)",
                "DTLZ2(10)","DTLZ2(15)",
                "DTLZ3(10)","DTLZ3(15)",
                "DTLZ4(10)","DTLZ4(15)",
                "Convex_DTLZ2(10)","Convex_DTLZ2(15)"
        };

        int[] objNumList = {
                10,15,
                10,15,
                10,15,
                10,15,
                10,15
        };

        int[] popsList = {
                286,136,
                286,136,
                286,136,
                286,136,
                286,136
        };
        int[] maxIterationsList = {
                1000,1500,
                750,1000,
                1500,2000,
                2000,3000,
                4000,4500
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";

        String[] frontFileList = {
                frontDir + "DTLZ1.10D.pf[286]",
                frontDir + "DTLZ1.15D.pf[136]",
                frontDir + "DTLZ2.10D.pf[286]",
                frontDir + "DTLZ2.15D.pf[136]",
                frontDir + "DTLZ2.10D.pf[286]",
                frontDir + "DTLZ2.15D.pf[136]",
                frontDir + "DTLZ2.10D.pf[286]",
                frontDir + "DTLZ2.15D.pf[136]",
                frontDir + "Convex_DTLZ2.10D.pf[286]",
                frontDir + "Convex_DTLZ2.15D.pf[136]"
        };
//        String[] frontFileList = {
//                frontDir + "DTLZ1.10D.pf[92378]",
//                frontDir + "DTLZ1.15D.pf[170544]",
//                frontDir + "DTLZ2.10D.pf[92378]",
//                frontDir + "DTLZ2.15D.pf[170544]",
//                frontDir + "DTLZ2.10D.pf[92378]",
//                frontDir + "DTLZ2.15D.pf[170544]",
//                frontDir + "DTLZ2.10D.pf[92378]",
//                frontDir + "DTLZ2.15D.pf[170544]",
//                frontDir + "Convex_DTLZ2.10D.pf[92378]",
//                frontDir + "Convex_DTLZ2.15D.pf[170544]"
//        };
        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});

        Point[] hvRefPointList = {
                point10Dmin,point15Dmin,
                point10D,point15D,
                point10D,point15D,
                point10D,point15D,
                point10D,point15D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D
        };


        double[] hvCubeList = {
//                0.99999999973088555445326278659612,
//                0.99999999999999997666270833795222,
//
//                1023.9975096054298072798399842016,
//                32767.999988359274877218491124947,
//
//                1023.9975096054298072798399842016,
//                32767.999988359274877218491124947,
//
//                1023.9975096054298072798399842016,
//                32767.999988359274877218491124947,
//
//
//                1023.9999999999999957910347537283,
//                32768 - 1.8530243193155939420665879882856e-27


                0.0282475246308855378052360407537,
                0.00474756150994297236589414268337,

                2.59125206552980946739239698218,
                4.177236528690534374558916402748,

                2.59125206552980946739239698218,
                4.177236528690534374558916402748,

                2.59125206552980946739239698218,
                4.177236528690534374558916402748,

                2.59374246010000231166259254678,
                4.17724816941565624262011624523
        };


        String[] algorithmNameList = {
                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"

        };

        int numOfSample = 1000000;
        int approximateDim = 10;

        executeFinalIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList, frontFileList,algorithmNameList);
//        executeFinalHV(dataDir,statDir,maxRun,problemNameList, objNumList,popsList, maxIterationsList, hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);

    }

    public void executeMaOPSmallMeasure(String dataDir, String statDir,int maxRun,String[] algorithmNameList,int approximateDim, int numOfSample){

        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(10)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(10)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(10)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(10)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(10)"
        };

        int[] objNumList = {
                3,5,10,
                3,5,10,
                3,5,10,
                3,5,10,
                3,5,10
        };

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

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[10]",
//                frontDir + "DTLZ1.5D.pf[20]",
//                frontDir + "DTLZ1.10D.pf[65]",
//                frontDir + "DTLZ2.3D.pf[10]",
//                frontDir + "DTLZ2.5D.pf[20]",
//                frontDir + "DTLZ2.10D.pf[65]",
//                frontDir + "DTLZ3.3D.pf[10]",
//                frontDir + "DTLZ3.5D.pf[20]",
//                frontDir + "DTLZ3.10D.pf[65]",
//                frontDir + "DTLZ4.3D.pf[10]",
//                frontDir + "DTLZ4.5D.pf[20]",
//                frontDir + "DTLZ4.10D.pf[65]",
//                frontDir + "Convex_DTLZ2.3D.pf[10]",
//                frontDir + "Convex_DTLZ2.5D.pf[20]",
//                frontDir + "Convex_DTLZ2.10D.pf[65]"
//        };
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[8001]",
                frontDir + "DTLZ1.5D.pf[15981]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ2.3D.pf[8001]",
                frontDir + "DTLZ2.5D.pf[15981]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ3.3D.pf[8001]",
                frontDir + "DTLZ3.5D.pf[15981]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ4.3D.pf[8001]",
                frontDir + "DTLZ4.5D.pf[15981]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.3D.pf[8001]",
                frontDir + "Convex_DTLZ2.5D.pf[15981]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]"
        };
//        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
//        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
//        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
//        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
//        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
//        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});
        Point point3Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point5Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55, 0.55, 0.55});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point10Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point15Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});

        Point[] hvRefPointList = {
                point3Dmin,point5Dmin, point10Dmin,
                point3D,point5D, point10D,
                point3D,point5D, point10D,
                point3D,point5D, point10D,
                point3D,point5D, point10D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero3D,pointZero5D,pointZero10D,
                pointZero3D,pointZero5D,pointZero10D,
                pointZero3D,pointZero5D,pointZero10D,
                pointZero3D,pointZero5D,pointZero10D,
                pointZero3D,pointZero5D,pointZero10D
        };
//
//        double[] hvCubeList = {
//                0.322166666666666601237523082091,
//                0.167809583333333262045172773469,
//                0.0282475246308855378052360407537,
//
//                0.807401224401701478328163830156,
//                1.44601659331517806705846851401,
//                2.59125206552980946739239698218,
//
//                0.807401224401701478328163830156,
//                1.44601659331517806705846851401,
//                2.59125206552980946739239698218,
//
//                0.807401224401701478328163830156,
//                1.44601659331517806705846851401,
//                2.59125206552980946739239698218,
//
//                1.33100000000000040500935938326,
//                1.61051000000000077427841915778,
//                2.59374246010000231166259254678
//        };
        double[] hvCubeList = {
//                0.322166666666666601237523082091,
//                0.167809583333333262045172773469,
//                0.0576479131187995763596632059489,
//                0.0282475246308855378052360407537,
//                0.00474756150994297236589414268337,
                0.14554166666666670000,
                0.05006802083333336000,
                0.00253295135207696300,

                0.80740122440170147833,
                1.44601659331517806706,
                2.59125206552980946739,

                0.80740122440170147833,
                1.44601659331517806706,
                2.59125206552980946739,

                0.80740122440170147833,
                1.44601659331517806706,
                2.59125206552980946739,

                1.33100000000000040501,
                1.61051000000000077428,
                2.59374246010000231166
        };
        executeIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
        executeIGDPlus(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
        executeHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
    }


    public void executeConvex_DTLZ2Test(String dataDir,String statDir,int maxRun){
        String[] problemNameList = {
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
//                ,"Convex_DTLZ2(10)","Convex_DTLZ2(15)"
        };

        int[] objNumList = {
                3,5,8
//                ,10,15
        };

        int[] popsList = {
                91, 210, 156
//                , 275,135
        };
        int[] maxIterationsList = {
                250,750,2000
//                ,4000,4500
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";

//        String[] frontFileList = {
//                frontDir + "Convex_DTLZ2.3D.pf[15151]",
//                frontDir + "Convex_DTLZ2.5D.pf[25081]",
//                frontDir + "Convex_DTLZ2.8D.pf[43713]",
//                frontDir + "Convex_DTLZ2.10D.pf[92378]",
//                frontDir + "Convex_DTLZ2.15D.pf[170544]"
//        };
        String[] frontFileList = {
                frontDir + "Convex_DTLZ2.3D.pf[60301]",
                frontDir + "Convex_DTLZ2.5D.pf[372406]",
                frontDir + "Convex_DTLZ2.8D.pf[2982135]"
        };
//
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[15151]",
//                frontDir + "DTLZ1.5D.pf[25081]",
//                frontDir + "DTLZ1.8D.pf[43713]",
//                frontDir + "DTLZ2.3D.pf[15151]",
//                frontDir + "DTLZ2.5D.pf[25081]",
//                frontDir + "DTLZ2.8D.pf[43713]",
//                frontDir + "DTLZ2.3D.pf[15151]",
//                frontDir + "DTLZ2.5D.pf[25081]",
//                frontDir + "DTLZ2.8D.pf[43713]",
//                frontDir + "DTLZ2.3D.pf[15151]",
//                frontDir + "DTLZ2.5D.pf[25081]",
//                frontDir + "DTLZ2.8D.pf[43713]",
//                frontDir + "Convex_DTLZ2.3D.pf[15151]",
//                frontDir + "Convex_DTLZ2.5D.pf[25081]",
//                frontDir + "Convex_DTLZ2.8D.pf[43713]"
//        };

        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});

        Point[] hvRefPointList = {
                point3Dmin,point5Dmin, point8Dmin,
                point3D,point5D, point8D,
                point3D,point5D, point8D,
                point3D,point5D, point8D,
                point3D,point5D, point8D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D
        };

        double[] hvCubeList = {
                0.322166666666666601237523082091,
                0.167809583333333262045172773469,
                0.0576479131187995763596632059489,
//                0.0282475246308855378052360407537,
//                0.00474756150994297236589414268337,

                0.807401224401701478328163830156,
                1.44601659331517806705846851401,
                2.12773446575618585541178617859,
//                2.59125206552980946739239698218,
//                4.177236528690534\374558916402748,

                0.807401224401701478328163830156,
                1.44601659331517806705846851401,
                2.12773446575618585541178617859,
//                2.59125206552980946739239698218,
//                4.177236528690534\374558916402748,

                0.807401224401701478328163830156,
                1.44601659331517806705846851401,
                2.12773446575618585541178617859,
//                2.59125206552980946739239698218,
//                4.177236528690534\374558916402748,

                1.33100000000000040500935938326,
                1.61051000000000077427841915778,
                2.14358881000000156547002916341
//                ,
//                2.59374246010000231166259254678,
//                4.17724816941565624262011624523
        };
        String[] algorithmNameList = {
                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"

//                "MOEADGR_TCH"
//                "MOEACD"
        };

        int numOfSample = 1000000;
        int approximateDim = 10;

        executeFinalIGDPlus(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList, frontFileList,algorithmNameList);
    }


    public void executeMOPFinal(String dataDir,String statDir,int maxRun,String[] algorithmNameList,int approximateDim, int numOfSample){


        String[] problemNameList = {
                "MOP1(2)","MOP2(2)","MOP3(2)","MOP4(2)","MOP5(2)","MOP6(3)","MOP7(3)"
        };

        int[] objNumList = {2,2,2,2,2,3,3};
        int[] popsList = {
                100,100,100,100,100,300,300
        };

        int[] maxIterationsList = {
                3000,3000,3000,3000,3000,3000,3000
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "MOP1.2D.pf[4000]",
                frontDir + "MOP2.2D.pf[4000]",
                frontDir + "MOP3.2D.pf[4000]",
                frontDir + "MOP4.2D.pf[4000]",
                frontDir + "MOP5.2D.pf[4000]",
                frontDir + "MOP6.3D.pf[8001]",
                frontDir + "MOP7.3D.pf[8001]"
        };

        Point point2D = new ArrayPoint(new double[]{1.0,1.0});
        Point point3D = new ArrayPoint(new double[]{1.0,1.0,1.0});
        Point[] nadirPointList = {
                point2D,point2D,point2D,point2D,point2D,point3D,point3D
        };

        Point pointZero2D = new ArrayPoint(new double[]{0,0});
        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point[] boundPointList = {
                pointZero2D,pointZero2D,pointZero2D,pointZero2D,pointZero2D,pointZero3D,pointZero3D
        };

 //       executeFinalIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
  //      executeFinalIGDPlus(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
   //     executeFinalHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
        executeFinalNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);

    }


    public void executeMaOPFinal(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)","DTLZ1(10)","DTLZ1(15)"
                ,
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)","DTLZ2(10)","DTLZ2(15)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)","DTLZ3(10)","DTLZ3(15)"
                ,
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)",
                "DTLZ4(10)","DTLZ4(15)"
                ,
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)","Convex_DTLZ2(10)","Convex_DTLZ2(15)"
//                "DTLZ2(15)",
//                "DTLZ3(8)","DTLZ3(10)","DTLZ3(15)"
//                ,
//                "DTLZ4(8)",
//                "DTLZ4(10)","DTLZ4(15)"
        };

        int[] objNumList = {
                3,5,8,10,15
                ,
                3,5,8,10,15,
                3,5,8,10,15,
                3,5,8,10,15,
                3,5,8,
                10,15
//                15
//                ,
//                8,10,15,
//                8,
//                10,15
        };
        int[] popsList = {
                91, 210, 156,275, 135
                ,
                91, 210, 156,275, 135,
                91, 210, 156,275, 135,
                91, 210, 156,275, 135,
                91, 210, 156,
                275, 135
//                135,
//                156,275, 135,
//                156,
//                275, 135
        };

        int[] maxIterationsList = {
                400,600,750,1000,1500
                ,
                250,350,500,750,1000,
                1000,1000,
                1000,1500,2000
                ,
                600,1000,
                1250,
                2000,3000
                ,
                250,750,2000,4000,4500
//                1250,
//                1250,1750,2500
//                ,
//                1500,
//                2500,3500
        };

//        String[] problemNameList = {
//                "DTLZ1(3)","DTLZ1(5)",
//                "DTLZ2(3)","DTLZ2(5)",
//                "DTLZ3(3)","DTLZ3(5)",
//                "DTLZ4(3)","DTLZ4(5)",
//                "Convex_DTLZ2(3)","Convex_DTLZ2(5)"
//        };
//
//        int[] objNumList = {
//                3,5,
//                3,5,
//                3,5,
//                3,5,
//                3,5
//        };
//        int[] popsList = {
//                91, 210,
//                91, 210,
//                91, 210,
//                91, 210,
//                91, 210
//        };
//
//        int[] maxIterationsList = {
//                400,600,
//                250,350,
//                1000,1000,
//                600,1000,
//                250,750
//        };


        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[8001]",
                frontDir + "DTLZ1.5D.pf[15981]",
                frontDir + "DTLZ1.8D.pf[31824]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ1.15D.pf[54264]",
                frontDir + "DTLZ2.3D.pf[8001]",
                frontDir + "DTLZ2.5D.pf[15981]",
                frontDir + "DTLZ2.8D.pf[31824]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ2.15D.pf[54264]",
                frontDir + "DTLZ3.3D.pf[8001]",
                frontDir + "DTLZ3.5D.pf[15981]",
                frontDir + "DTLZ3.8D.pf[31824]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ3.15D.pf[54264]",
                frontDir + "DTLZ4.3D.pf[8001]",
                frontDir + "DTLZ4.5D.pf[15981]",
                frontDir + "DTLZ4.8D.pf[31824]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "DTLZ4.15D.pf[54264]",
                frontDir + "Convex_DTLZ2.3D.pf[8001]",
                frontDir + "Convex_DTLZ2.5D.pf[15981]",
                frontDir + "Convex_DTLZ2.8D.pf[31824]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.15D.pf[54264]"
        };
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[91]",
//                frontDir + "DTLZ1.5D.pf[210]",
//                frontDir + "DTLZ1.8D.pf[156]",
//                frontDir + "DTLZ1.10D.pf[275]",
//                frontDir + "DTLZ1.15D.pf[135]",
//                frontDir + "DTLZ2.3D.pf[91]",
//                frontDir + "DTLZ2.5D.pf[210]",
//                frontDir + "DTLZ2.8D.pf[156]",
//                frontDir + "DTLZ2.10D.pf[275]",
//                frontDir + "DTLZ2.15D.pf[135]",
//                frontDir + "DTLZ3.3D.pf[91]",
//                frontDir + "DTLZ3.5D.pf[210]",
//                frontDir + "DTLZ3.8D.pf[156]",
//                frontDir + "DTLZ3.10D.pf[275]",
//                frontDir + "DTLZ3.15D.pf[135]",
//                frontDir + "DTLZ4.3D.pf[91]",
//                frontDir + "DTLZ4.5D.pf[210]",
//                frontDir + "DTLZ4.8D.pf[156]",
//                frontDir + "DTLZ4.10D.pf[275]",
//                frontDir + "DTLZ4.15D.pf[135]",
//                frontDir + "Convex_DTLZ2.3D.pf[91]",
//                frontDir + "Convex_DTLZ2.5D.pf[210]",
//                frontDir + "Convex_DTLZ2.8D.pf[156]",
//                frontDir + "Convex_DTLZ2.10D.pf[275]",
//                frontDir + "Convex_DTLZ2.15D.pf[135]"
//        };

        Point point3Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5});
        Point point3D = new ArrayPoint(new double[]{1.0, 1.0, 1.0});
        Point point5Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5, 0.5, 0.5});
        Point point5D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0});
        Point point8Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5});
        Point point8D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0});
        Point point10Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5});
        Point point10D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0});
        Point point15Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5});
        Point point15D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0});



        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});

        Point[] nadirPointList = {
                point3Dmin,point5Dmin, point8Dmin, point10Dmin,point15Dmin
                ,
                point3D,point5D,point8D,point10D,point15D,
                point3D,point5D,point8D,point10D,point15D,
                point3D,point5D,point8D,point10D,point15D,
                point3D,point5D,point8D,
                point10D,point15D
//                point15D,
//                point8D,point10D,point15D,
//                point8D,point10D,point15D,
        };

        Point[] boundPointList = {
                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero10D,pointZero15D
//                pointZero15D,
//                pointZero8D,pointZero10D,pointZero15D,
//                pointZero8D,pointZero10D,pointZero15D,
        };

//        Point[] nadirPointList = {
//                point3Dmin,point5Dmin,
//                point3D,point5D,
//                point3D,point5D,
//                point3D,point5D,
//                point3D,point5D
//        };
//
//        Point[] boundPointList = {
//                pointZero3D,pointZero5D,
//                pointZero3D,pointZero5D,
//                pointZero3D,pointZero5D,
//                pointZero3D,pointZero5D,
//                pointZero3D,pointZero5D
//        };


//        String[] problemNameList = {
//                "DTLZ1(3)","DTLZ1(5)",
//                "DTLZ2(3)","DTLZ2(5)",
//                "DTLZ3(3)","DTLZ3(5)",
//                "DTLZ4(3)","DTLZ4(5)",
//                "Convex_DTLZ2(3)","Convex_DTLZ2(5)"
//        };
//
//        int[] objNumList = {
//                3,5,
//                3,5,
//                3,5,
//                3,5,
//                3,5
//        };
//        int[] popsList = {
//                91, 210,
//                91, 210,
//                91, 210,
//                91, 210,
//                91, 210
//        };
//
//        int[] maxIterationsList = {
//                400,600,
//                250,350,
//                1000,1000,
//                600,1000,
//                250,750
//        };
//
//        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
////
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[8001]",
//                frontDir + "DTLZ1.5D.pf[15981]",
//                frontDir + "DTLZ1.8D.pf[31824]",
//                frontDir + "DTLZ1.10D.pf[43758]",
//                frontDir + "DTLZ1.15D.pf[54264]",
//                frontDir + "DTLZ2.3D.pf[8001]",
//                frontDir + "DTLZ2.5D.pf[15981]",
//                frontDir + "DTLZ2.8D.pf[31824]",
//                frontDir + "DTLZ2.10D.pf[43758]",
//                frontDir + "DTLZ2.15D.pf[54264]",
//                frontDir + "DTLZ3.3D.pf[8001]",
//                frontDir + "DTLZ3.5D.pf[15981]",
//                frontDir + "DTLZ3.8D.pf[31824]",
//                frontDir + "DTLZ3.10D.pf[43758]",
//                frontDir + "DTLZ3.15D.pf[54264]",
//                frontDir + "DTLZ4.3D.pf[8001]",
//                frontDir + "DTLZ4.5D.pf[15981]",
//                frontDir + "DTLZ4.8D.pf[31824]",
//                frontDir + "DTLZ4.10D.pf[43758]",
//                frontDir + "DTLZ4.15D.pf[54264]",
//                frontDir + "Convex_DTLZ2.3D.pf[8001]",
//                frontDir + "Convex_DTLZ2.5D.pf[15981]",
//                frontDir + "Convex_DTLZ2.8D.pf[31824]",
//                frontDir + "Convex_DTLZ2.10D.pf[43758]",
//                frontDir + "Convex_DTLZ2.15D.pf[54264]"
//        };
//
//        Point point3Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5});
//        Point point3D = new ArrayPoint(new double[]{1.0, 1.0, 1.0});
//        Point point5Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5, 0.5, 0.5});
//        Point point5D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0});
//        Point point8Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5});
//        Point point8D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0});
//        Point point10Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5});
//        Point point10D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0});
//        Point point15Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5});
//        Point point15D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0});
//
//        Point[] nadirPointList = {
//                point3Dmin,point5Dmin,
//                point3D,point5D,
//                point3D,point5D,
//                point3D,point5D,
//                point3D,point5D
//        };
//
//        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
//        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
//        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
//        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
//        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
//        Point[] boundPointList = {
//                pointZero3D,pointZero5D,
//                pointZero3D,pointZero5D,
//                pointZero3D,pointZero5D,
//                pointZero3D,pointZero5D,
//                pointZero3D,pointZero5D
//        };


//        double[] hvCubeList = {
////                0.322166666666666601237523082091,
////                0.167809583333333262045172773469,
////                0.0576479131187995763596632059489,
////                0.0282475246308855378052360407537,
////                0.00474756150994297236589414268337,
//                0.14554166666666670000,
//                0.05006802083333336000,
//                0.00837329690786210900,
//                0.00253295135207696300,
//                0.00012747949735763220,
//
//                0.80740122440170147833,
//                1.44601659331517806706,
//                2.12773446575618585541,
//                2.59125206552980946739,
//                4.17723652869053437456,
//
//                0.80740122440170147833,
//                1.44601659331517806706,
//                2.12773446575618585541,
//                2.59125206552980946739,
//                4.17723652869053437456,
//
//                0.80740122440170147833,
//                1.44601659331517806706,
//                2.12773446575618585541,
//                2.59125206552980946739,
//                4.17723652869053437456,
//
//                1.33100000000000040501,
//                1.61051000000000077428,
//                2.14358881000000156547,
//                2.59374246010000231166,
//                4.17724816941565624262
//        };
////
//        executeFinalIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
//        executeFinalIGDPlus(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
//        executeFinalHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
        executeFinalNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);

    }


    public void executeMaOPMethodFinal(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){

        String[] problemNameList = {
                "DTLZ1(3)",
                "DTLZ2(3)",
                "DTLZ3(3)",
                "DTLZ4(3)",
                "Convex_DTLZ2(3)"
        };

        int[] objNumList = {
                3,
                3,
                3,
                3,
                3
        };
        int[] popsList = {
                91,
                91,
                91,
                91,
                91
        };

        int[] maxIterationsList = {
                400,
                250,
                1000,
                600,
                250
        };


        Point point3Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5});
        Point point3D = new ArrayPoint(new double[]{1.0, 1.0, 1.0});
        Point point5Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5, 0.5, 0.5});
        Point point5D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0});
        Point point8Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5});
        Point point8D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0});
        Point point10Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5});
        Point point10D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0});
        Point point15Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5});
        Point point15D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0});



        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});

        Point[] nadirPointList = {
                point3Dmin,
                point3D,
                point3D,
                point3D,
                point3D
        };

        Point[] boundPointList = {
                pointZero3D,
                pointZero3D,
                pointZero3D,
                pointZero3D,
                pointZero3D
        };


        executeFinalNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);

    }

    public void executeMaOPLowFinal(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)"
        };

        int[] objNumList = {
                3,5,8,
                3,5,8,
                3,5,8,
                3,5,8,
                3,5,8
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

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[8001]",
                frontDir + "DTLZ1.5D.pf[15981]",
                frontDir + "DTLZ1.8D.pf[31824]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ1.15D.pf[54264]",
                frontDir + "DTLZ2.3D.pf[8001]",
                frontDir + "DTLZ2.5D.pf[15981]",
                frontDir + "DTLZ2.8D.pf[31824]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ2.15D.pf[54264]",
                frontDir + "DTLZ3.3D.pf[8001]",
                frontDir + "DTLZ3.5D.pf[15981]",
                frontDir + "DTLZ3.8D.pf[31824]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ3.15D.pf[54264]",
                frontDir + "DTLZ4.3D.pf[8001]",
                frontDir + "DTLZ4.5D.pf[15981]",
                frontDir + "DTLZ4.8D.pf[31824]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "DTLZ4.15D.pf[54264]",
                frontDir + "Convex_DTLZ2.3D.pf[8001]",
                frontDir + "Convex_DTLZ2.5D.pf[15981]",
                frontDir + "Convex_DTLZ2.8D.pf[31824]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.15D.pf[54264]"
        };
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[91]",
//                frontDir + "DTLZ1.5D.pf[210]",
//                frontDir + "DTLZ1.8D.pf[156]",
//                frontDir + "DTLZ1.10D.pf[275]",
//                frontDir + "DTLZ1.15D.pf[135]",
//                frontDir + "DTLZ2.3D.pf[91]",
//                frontDir + "DTLZ2.5D.pf[210]",
//                frontDir + "DTLZ2.8D.pf[156]",
//                frontDir + "DTLZ2.10D.pf[275]",
//                frontDir + "DTLZ2.15D.pf[135]",
//                frontDir + "DTLZ3.3D.pf[91]",
//                frontDir + "DTLZ3.5D.pf[210]",
//                frontDir + "DTLZ3.8D.pf[156]",
//                frontDir + "DTLZ3.10D.pf[275]",
//                frontDir + "DTLZ3.15D.pf[135]",
//                frontDir + "DTLZ4.3D.pf[91]",
//                frontDir + "DTLZ4.5D.pf[210]",
//                frontDir + "DTLZ4.8D.pf[156]",
//                frontDir + "DTLZ4.10D.pf[275]",
//                frontDir + "DTLZ4.15D.pf[135]",
//                frontDir + "Convex_DTLZ2.3D.pf[91]",
//                frontDir + "Convex_DTLZ2.5D.pf[210]",
//                frontDir + "Convex_DTLZ2.8D.pf[156]",
//                frontDir + "Convex_DTLZ2.10D.pf[275]",
//                frontDir + "Convex_DTLZ2.15D.pf[135]"
//        };

//        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
//        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{2, 2, 2, 2, 2});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1});
//        Point point8D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point10D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2});
//        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
//        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
//        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
//        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
//        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
//        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});
//        Point point3Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55});
//        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
//        Point point5Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55, 0.55, 0.55});
//        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
//        Point point8Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55});
//        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
//        Point point10Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55});
//        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
//        Point point15Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55});
//        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});
//        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
//        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{2, 2, 2,2, 2});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1,1, 1, 1,1, 1});
//        Point point8D = new ArrayPoint(new double[]{2, 2, 2,2, 2, 2,2, 2});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1,1, 1, 1,1, 1, 1,1});
//        Point point10D = new ArrayPoint(new double[]{2, 2, 2,2, 2, 2,2, 2, 2,2});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1,1, 1, 1,1, 1, 1,1, 1, 1,1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{2, 2, 2,2, 2, 2,2, 2, 2,2, 2, 2,2, 2, 2});
        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
        Point point5Dmin = new ArrayPoint(new double[]{11, 11, 11, 11, 11});
        Point point5D = new ArrayPoint(new double[]{11, 11, 11, 11, 11});
        Point point8Dmin = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11});
        Point point8D = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11});
        Point point10Dmin = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11, 11,11});
        Point point10D = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11, 11,11});
        Point point15Dmin = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11, 11,11, 11, 11,11, 11, 11});
        Point point15D = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11, 11,11, 11, 11,11, 11, 11});

        Point[] hvRefPointList = {
                point3Dmin,point5Dmin, point8Dmin,
                point3D,point5D,point8D,
                point3D,point5D,point8D,
                point3D,point5D,point8D,
                point3D,point5D,point8D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D,
                pointZero3D,pointZero5D,pointZero8D
        };


        double[] hvCubeList = {
//                0.322166666666666601237523082091,
//                0.167809583333333262045172773469,
//                0.0576479131187995763596632059489,
//                0.0282475246308855378052360407537,
//                0.00474756150994297236589414268337,
                0.14554166666666670000,
                0.05006802083333336000,
                0.00837329690786210900,
                0.00253295135207696300,
                0.00012747949735763220,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                1.33100000000000040501,
                1.61051000000000077428,
                2.14358881000000156547,
                2.59374246010000231166,
                4.17724816941565624262
        };

//        executeFinalIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
//        executeFinalIGDPlus(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
//        executeFinalHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
//        executeFinalNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
    }

    public void executeMaOPScaleHVFinal(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "SDTLZ1(3)","SDTLZ1(5)","SDTLZ1(8)",
                "SDTLZ1(10)","SDTLZ1(15)"
                ,
                "SDTLZ2(3)","SDTLZ2(5)","SDTLZ2(8)","SDTLZ2(10)","SDTLZ2(15)"
                ,
                "SDTLZ3(3)","SDTLZ3(5)","SDTLZ3(8)","SDTLZ3(10)","SDTLZ3(15)",
                "SDTLZ4(3)","SDTLZ4(5)","SDTLZ4(8)","SDTLZ4(10)","SDTLZ4(15)",
                "Convex_SDTLZ2(3)","Convex_SDTLZ2(5)","Convex_SDTLZ2(8)","Convex_SDTLZ2(10)","Convex_SDTLZ2(15)"
        };

        int[] popsList = {
                91, 210, 156,
                275,135
                ,
                91, 210, 156, 275,135
                ,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135
        };

        int[] maxIterationsList = {
                400,600,750,
                1000,1500
                ,
                250,350,500,750,1000
                ,
                1000,1000,1000,1500,2000,
                600,1000,1250,2000,3000,
                250,750,2000,4000,4500
        };


        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";

//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[91]",
//                frontDir + "DTLZ1.5D.pf[210]",
//                frontDir + "DTLZ1.8D.pf[156]",
//                frontDir + "DTLZ1.10D.pf[275]",
//                frontDir + "DTLZ1.15D.pf[135]",
//                frontDir + "DTLZ2.3D.pf[91]",
//                frontDir + "DTLZ2.5D.pf[210]",
//                frontDir + "DTLZ2.8D.pf[156]",
//                frontDir + "DTLZ2.10D.pf[275]",
//                frontDir + "DTLZ2.15D.pf[135]"
////                ,
////                frontDir + "DTLZ3.3D.pf[91]",
////                frontDir + "DTLZ3.5D.pf[210]",
////                frontDir + "DTLZ3.8D.pf[156]",
////                frontDir + "DTLZ3.10D.pf[275]",
////                frontDir + "DTLZ3.15D.pf[135]",
////                frontDir + "DTLZ4.3D.pf[91]",
////                frontDir + "DTLZ4.5D.pf[210]",
////                frontDir + "DTLZ4.8D.pf[156]",
////                frontDir + "DTLZ4.10D.pf[275]",
////                frontDir + "DTLZ4.15D.pf[135]",
////                frontDir + "Convex_DTLZ2.3D.pf[91]",
////                frontDir + "Convex_DTLZ2.5D.pf[210]",
////                frontDir + "Convex_DTLZ2.8D.pf[156]",
////                frontDir + "Convex_DTLZ2.10D.pf[275]",
////                frontDir + "Convex_DTLZ2.15D.pf[135]"
//        };
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[8001]",
                frontDir + "DTLZ1.5D.pf[15981]",
                frontDir + "DTLZ1.8D.pf[31824]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ1.15D.pf[54264]",
                frontDir + "DTLZ2.3D.pf[8001]",
                frontDir + "DTLZ2.5D.pf[15981]",
                frontDir + "DTLZ2.8D.pf[31824]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ2.15D.pf[54264]"
                ,
                frontDir + "DTLZ3.3D.pf[8001]",
                frontDir + "DTLZ3.5D.pf[15981]",
                frontDir + "DTLZ3.8D.pf[31824]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ3.15D.pf[54264]",
                frontDir + "DTLZ4.3D.pf[8001]",
                frontDir + "DTLZ4.5D.pf[15981]",
                frontDir + "DTLZ4.8D.pf[31824]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "DTLZ4.15D.pf[54264]",
                frontDir + "Convex_DTLZ2.3D.pf[8001]",
                frontDir + "Convex_DTLZ2.5D.pf[15981]",
                frontDir + "Convex_DTLZ2.8D.pf[31824]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.15D.pf[54264]"
        };


        Point IdealPoint3D = new ArrayPoint(new double[]{0.0,0.0,0.0});
        Point IdealPoint5D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint8D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint10D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint15D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});
        Point[] idealpointList = {
                IdealPoint3D,IdealPoint5D,IdealPoint8D,
                IdealPoint10D,IdealPoint15D
                ,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D
                ,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D
        };

        Point NadirPoint3DMin = new ArrayPoint(new double[]{0.5,0.5,0.5});
        Point NadirPoint5DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5});
        Point NadirPoint8DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5});
        Point NadirPoint10DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5});
        Point NadirPoint15DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5});

        Point NadirPoint3D = new ArrayPoint(new double[]{1.0,1.0,1.0});
        Point NadirPoint5D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0});
        Point NadirPoint8D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0});
        Point NadirPoint10D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0});
        Point NadirPoint15D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0});

        for(int i=0;i<3;i++){
            NadirPoint3DMin.setDimensionValue(i,NadirPoint3DMin.getDimensionValue(i)*Math.pow(10.0,i));
            NadirPoint3D.setDimensionValue(i,NadirPoint3D.getDimensionValue(i)*Math.pow(10.0,i));
        }
        for(int i=0;i<5;i++){
            NadirPoint5DMin.setDimensionValue(i,NadirPoint5DMin.getDimensionValue(i)*Math.pow(10.0,i));
            NadirPoint5D.setDimensionValue(i,NadirPoint5D.getDimensionValue(i)*Math.pow(10.0,i));
        }
        for(int i=0;i<8;i++){
            NadirPoint8DMin.setDimensionValue(i,NadirPoint8DMin.getDimensionValue(i)*Math.pow(3.0,i));
            NadirPoint8D.setDimensionValue(i,NadirPoint8D.getDimensionValue(i)*Math.pow(3.0,i));
        }
        for(int i=0;i<10;i++){
//            NadirPoint10DMin.setDimensionValue(i,NadirPoint10DMin.getDimensionValue(i)*Math.pow(3.0,i));
            NadirPoint10DMin.setDimensionValue(i,NadirPoint10DMin.getDimensionValue(i)*Math.pow(2.0,i));
            NadirPoint10D.setDimensionValue(i,NadirPoint10D.getDimensionValue(i)*Math.pow(3.0,i));
        }
        for(int i=0;i<15;i++){
//            NadirPoint15DMin.setDimensionValue(i,NadirPoint15DMin.getDimensionValue(i)*Math.pow(2.0,i));
            NadirPoint15DMin.setDimensionValue(i,NadirPoint15DMin.getDimensionValue(i)*Math.pow(1.2,i));
            NadirPoint15D.setDimensionValue(i,NadirPoint15D.getDimensionValue(i)*Math.pow(2.0,i));
        }

        Point[] NadirPointsList = {
                NadirPoint3DMin,NadirPoint5DMin,NadirPoint8DMin,
                NadirPoint10DMin,NadirPoint15DMin
                ,
                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D
                ,
                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D,
                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D,
                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D
        };

        int[] objNumList = new int[]{
                3,5,8,
                10,15
                ,
                3,5,8,10,15
                ,
                3,5,8,10,15,
                3,5,8,10,15,
                3,5,8,10,15
        };

        Point[] boundPointList = {
                IdealPoint3D,IdealPoint5D,IdealPoint8D,
                IdealPoint10D,IdealPoint15D
                ,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D
                ,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D
        };

        double[] hvCubeList = {
                0.14554166666666670000,
                0.05006802083333336000,
                0.00837329690786210900,
                0.00253295135207696300,
                0.00012747949735763220,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                1.33100000000000040501,
                1.61051000000000077428,
                2.14358881000000156547,
                2.59374246010000231166,
                4.17724816941565624262
        };

        executeFinalNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,NadirPointsList,boundPointList,numOfSample,approximateDim,algorithmNameList);
    }

    public void executeMaOPSmallFinal(String dataDir, String statDir,int maxRun,String[] algorithmNameList,int approximateDim, int numOfSample){

        String[] problemNameList = {
                "DTLZ1(3)","DTLZ1(5)","DTLZ1(10)",
                "DTLZ2(3)","DTLZ2(5)","DTLZ2(10)",
                "DTLZ3(3)","DTLZ3(5)","DTLZ3(10)",
                "DTLZ4(3)","DTLZ4(5)","DTLZ4(10)",
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(10)"
        };

        int[] objNumList = {
                3,5,10,
                3,5,10,
                3,5,10,
                3,5,10,
                3,5,10
        };

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

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[10]",
//                frontDir + "DTLZ1.5D.pf[20]",
//                frontDir + "DTLZ1.10D.pf[65]",
//                frontDir + "DTLZ2.3D.pf[10]",
//                frontDir + "DTLZ2.5D.pf[20]",
//                frontDir + "DTLZ2.10D.pf[65]",
//                frontDir + "DTLZ3.3D.pf[10]",
//                frontDir + "DTLZ3.5D.pf[20]",
//                frontDir + "DTLZ3.10D.pf[65]",
//                frontDir + "DTLZ4.3D.pf[10]",
//                frontDir + "DTLZ4.5D.pf[20]",
//                frontDir + "DTLZ4.10D.pf[65]",
//                frontDir + "Convex_DTLZ2.3D.pf[10]",
//                frontDir + "Convex_DTLZ2.5D.pf[20]",
//                frontDir + "Convex_DTLZ2.10D.pf[65]"
//        };
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[8001]",
                frontDir + "DTLZ1.5D.pf[15981]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ2.3D.pf[8001]",
                frontDir + "DTLZ2.5D.pf[15981]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ3.3D.pf[8001]",
                frontDir + "DTLZ3.5D.pf[15981]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ4.3D.pf[8001]",
                frontDir + "DTLZ4.5D.pf[15981]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.3D.pf[8001]",
                frontDir + "Convex_DTLZ2.5D.pf[15981]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]"
        };
        Point point3Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point5Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55, 0.55, 0.55});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point10Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point15Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});

        Point[] hvRefPointList = {
                point3Dmin,point5Dmin, point10Dmin,
                point3D,point5D, point10D,
                point3D,point5D, point10D,
                point3D,point5D, point10D,
                point3D,point5D, point10D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero3D,pointZero5D,pointZero10D,
                pointZero3D,pointZero5D,pointZero10D,
                pointZero3D,pointZero5D,pointZero10D,
                pointZero3D,pointZero5D,pointZero10D,
                pointZero3D,pointZero5D,pointZero10D
        };

        double[] hvCubeList = {
                0.14554166666666670000,
                0.05006802083333336000,
                0.00253295135207696300,

                0.80740122440170147833,
                1.44601659331517806706,
                2.59125206552980946739,

                0.80740122440170147833,
                1.44601659331517806706,
                2.59125206552980946739,

                0.80740122440170147833,
                1.44601659331517806706,
                2.59125206552980946739,

                1.33100000000000040501,
                1.61051000000000077428,
                2.59374246010000231166
        };

        executeFinalIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
        executeFinalIGDPlus(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
        executeFinalHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
    }



    public void executeSpreadTestFinal(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
//                "DTLZ1(3)","DTLZ1(5)","DTLZ1(8)","DTLZ1(10)","DTLZ1(15)",
//                "DTLZ2(3)","DTLZ2(5)","DTLZ2(8)","DTLZ2(10)","DTLZ2(15)",
//                "DTLZ3(3)","DTLZ3(5)","DTLZ3(8)","DTLZ3(10)","DTLZ3(15)",
//                "DTLZ4(3)","DTLZ4(5)","DTLZ4(8)", "DTLZ4(10)","DTLZ4(15)"
//                ,
                "Convex_DTLZ2(3)","Convex_DTLZ2(5)","Convex_DTLZ2(8)","Convex_DTLZ2(10)","Convex_DTLZ2(15)"
        };

        int[] objNumList = {
//                3,5,8,10,15,
//                3,5,8,10,15,
//                3,5,8,10,15,
//                3,5,8,10,15
//                ,
                3,5,8,10,15
        };
        int[] popsList = {
//                91, 210, 156,275, 135,
//                91, 210, 156,275, 135,
//                91, 210, 156,275, 135,
//                91, 210, 156,275, 135
//                ,
                91, 210, 156,275, 135
        };

        int[] maxIterationsList = {
//                400,600,750,1000,1500,
//                250,350,500,750,1000,
//                1000,1000,1000,1500,2000,
//                600,1000,1250,2000,3000
//                ,
                250,750,2000,4000,4500
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//
        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[8001]",
//                frontDir + "DTLZ1.5D.pf[15981]",
//                frontDir + "DTLZ1.8D.pf[31824]",
//                frontDir + "DTLZ1.10D.pf[43758]",
//                frontDir + "DTLZ1.15D.pf[54264]",
//                frontDir + "DTLZ2.3D.pf[8001]",
//                frontDir + "DTLZ2.5D.pf[15981]",
//                frontDir + "DTLZ2.8D.pf[31824]",
//                frontDir + "DTLZ2.10D.pf[43758]",
//                frontDir + "DTLZ2.15D.pf[54264]",
//                frontDir + "DTLZ3.3D.pf[8001]",
//                frontDir + "DTLZ3.5D.pf[15981]",
//                frontDir + "DTLZ3.8D.pf[31824]",
//                frontDir + "DTLZ3.10D.pf[43758]",
//                frontDir + "DTLZ3.15D.pf[54264]",
//                frontDir + "DTLZ4.3D.pf[8001]",
//                frontDir + "DTLZ4.5D.pf[15981]",
//                frontDir + "DTLZ4.8D.pf[31824]",
//                frontDir + "DTLZ4.10D.pf[43758]",
//                frontDir + "DTLZ4.15D.pf[54264]"
//                ,
                frontDir + "Convex_DTLZ2.3D.pf[8001]",
                frontDir + "Convex_DTLZ2.5D.pf[15981]",
                frontDir + "Convex_DTLZ2.8D.pf[31824]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.15D.pf[54264]"
        };
//        String[] frontFileList = {
////                frontDir + "DTLZ1.3D.pf[91]",
////                frontDir + "DTLZ1.5D.pf[210]",
////                frontDir + "DTLZ1.8D.pf[156]",
////                frontDir + "DTLZ1.10D.pf[275]",
////                frontDir + "DTLZ1.15D.pf[135]",
////                frontDir + "DTLZ2.3D.pf[91]",
////                frontDir + "DTLZ2.5D.pf[210]",
////                frontDir + "DTLZ2.8D.pf[156]",
////                frontDir + "DTLZ2.10D.pf[275]",
////                frontDir + "DTLZ2.15D.pf[135]",
////                frontDir + "DTLZ3.3D.pf[91]",
////                frontDir + "DTLZ3.5D.pf[210]",
////                frontDir + "DTLZ3.8D.pf[156]",
////                frontDir + "DTLZ3.10D.pf[275]",
////                frontDir + "DTLZ3.15D.pf[135]",
////                frontDir + "DTLZ4.3D.pf[91]",
////                frontDir + "DTLZ4.5D.pf[210]",
////                frontDir + "DTLZ4.8D.pf[156]",
////                frontDir + "DTLZ4.10D.pf[275]",
////                frontDir + "DTLZ4.15D.pf[135]",
//                frontDir + "Convex_DTLZ2.3D.pf[91]",
//                frontDir + "Convex_DTLZ2.5D.pf[210]",
//                frontDir + "Convex_DTLZ2.8D.pf[156]",
//                frontDir + "Convex_DTLZ2.10D.pf[275]",
//                frontDir + "Convex_DTLZ2.15D.pf[135]"
//        };

        executeFinalSpread(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
    }


    public void executeConstraintsFinal(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "C1_DTLZ1(3)", "C1_DTLZ1(5)", "C1_DTLZ1(8)", "C1_DTLZ1(10)", "C1_DTLZ1(15)",
                "C1_DTLZ3(3)", "C1_DTLZ3(5)", "C1_DTLZ3(8)", "C1_DTLZ3(10)", "C1_DTLZ3(15)"
                ,
                "C2_DTLZ2(3)",
                "C2_DTLZ2(5)", "C2_DTLZ2(8)", "C2_DTLZ2(10)", "C2_DTLZ2(15)",
                "ConvexC2_DTLZ2(3)"
                , "ConvexC2_DTLZ2(5)", "ConvexC2_DTLZ2(8)", "ConvexC2_DTLZ2(10)", "ConvexC2_DTLZ2(15)"
                ,
                "C3_DTLZ1(3)", "C3_DTLZ1(5)", "C3_DTLZ1(8)", "C3_DTLZ1(10)", "C3_DTLZ1(15)"
                ,
                "C3_DTLZ4(3)", "C3_DTLZ4(5)", "C3_DTLZ4(8)", "C3_DTLZ4(10)", "C3_DTLZ4(15)"
        };


        int[] objNumList = {
                3,5,8,10,15,
                3,5,8,10,15,
                3,5,8,10,15,
                3,5,8,10,15,
                3,
                5,8,10,15,
                3
                ,5,8,10,15
        };
        int[] popsList = {
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156, 275,135,
                91, 210, 156,275,135,
                91,
                210, 156, 275,135,
                91
                , 210, 156, 275,135
        };
        int[] maxIterationsList = {
                500,600,800,1000,1500,
                1000,1500,2500,3500,5000
                ,
                250,
                350,500,750,1000,
                250
                ,750,1500,2500,3500
                ,
                750,1250,2000,3000,4000
                ,
                750,1250,2000,3000,4000
        };

        Point npoint3Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5});
        Point npoint3D = new ArrayPoint(new double[]{1.0, 1.0, 1.0});
        Point npoint3Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0});
        Point npoint5Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5, 0.5, 0.5});
        Point npoint5D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0});
        Point npoint5Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0});
        Point npoint8Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5});
        Point npoint8D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0});
        Point npoint8Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0});
        Point npoint10Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5});
        Point npoint10D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0});
        Point npoint10Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0});
        Point npoint15Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5});
        Point npoint15D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0});
        Point npoint15Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0});

        Point npoint11_3D = new ArrayPoint(new double[]{11.0,11.0,11.0});
        Point npoint11_5D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0});
        Point npoint11_8D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0});
        Point npoint11_10D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0});
        Point npoint11_15D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0});

        Point[] nadirPointList = {
                npoint3Dmin,npoint5Dmin,npoint8Dmin,npoint10Dmin,npoint15Dmin,
                npoint3D,npoint5D,npoint8D,npoint10D,npoint15D,
//                npoint11_3D,npoint11_5D,npoint11_8D,npoint11_10D,npoint11_15D,
                npoint3D,
                npoint5D,npoint8D,npoint10D,npoint15D,
                npoint3D
                ,npoint5D,npoint8D,npoint10D,npoint15D
                ,
                npoint3D,npoint5D,npoint8D,npoint10D,npoint15D
                ,
                npoint3Dmax,npoint5Dmax,npoint8Dmax,npoint10Dmax,npoint15Dmax
        };


        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});


        Point[] boundPointList = {
                pointZero3D, pointZero5D, pointZero8D, pointZero10D, pointZero15D,
                pointZero3D, pointZero5D, pointZero8D, pointZero10D, pointZero15D,
                pointZero3D, pointZero5D, pointZero8D, pointZero10D, pointZero15D,
                pointZero3D, pointZero5D, pointZero8D, pointZero10D, pointZero15D
                ,
                pointZero3D,
                pointZero5D, pointZero8D, pointZero10D, pointZero15D,
                pointZero3D
                , pointZero5D, pointZero8D, pointZero10D, pointZero15D
        };

        double[] hvCubeList = {
//                0.14554166666666670000,
//                0.05006802083333336000,
//
//                0.80740122440170147833,
//                1.44601659331517806706,
//
//                0.80740122440170147833,
//                1.44601659331517806706,
//
//                0.80740122440170147833,
//                1.44601659331517806706
                0.14554166666666670000,

                0.80740122440170147833,
                0.14554166666666670000,

                0.80740122440170147833,


                0.80740122440170147833,

                0.80740122440170147833,
                0.14554166666666670000,

                0.80740122440170147833,
                0.14554166666666670000,

                0.80740122440170147833,


                0.80740122440170147833,

                0.80740122440170147833,
                0.14554166666666670000,

                0.80740122440170147833,
                0.14554166666666670000,

                0.80740122440170147833,


                0.80740122440170147833,

                0.80740122440170147833,
                0.14554166666666670000,

                0.80740122440170147833,
                0.14554166666666670000
                ,
                0.80740122440170147833,


                0.80740122440170147833,

                0.80740122440170147833,0.14554166666666670000,

                0.80740122440170147833,
                0.14554166666666670000,

                0.80740122440170147833,


                0.80740122440170147833,

                0.80740122440170147833
                ,0.14554166666666670000,

                0.80740122440170147833,
                0.14554166666666670000,

                0.80740122440170147833,


                0.80740122440170147833,

                0.80740122440170147833
        };

        executeFinalNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);
    }

    public void executeDTLZConstraintsTest(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "C1_DTLZ1(3)",
                "C1_DTLZ3(3)",
                "C2_DTLZ2(3)",
                "ConvexC2_DTLZ2(3)",
                "C3_DTLZ1(3)",
                "C3_DTLZ4(3)"

                ,

                "C1_DTLZ1(5)",
                "C1_DTLZ3(5)",
                "C2_DTLZ2(5)",
                "ConvexC2_DTLZ2(5)",
                "C3_DTLZ1(5)",
                "C3_DTLZ4(5)"
        };


        int[] objNumList = {
                3,
                3,
                3,
                3,
                3,
                3

                ,

                5,
                5,
                5,
                5,
                5,
                5
        };
        int[] popsList = {
                91,
                91,
                91,
                91,
                91,
                91
//
                ,

                210,
                210,
                210,
                210,
                210,
                210
        };
        int[] maxIterationsList = {
                500,
                1000,
                250,
                250,
                750,
                750

                ,

                600,
                1500,
                350,
                750,
                1250,
                1250
        };

        Point npoint3Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5});
        Point npoint3D = new ArrayPoint(new double[]{1.0, 1.0, 1.0});
        Point npoint3Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0});
        Point npoint5Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5, 0.5, 0.5});
        Point npoint5D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0});
        Point npoint5Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0});
        Point npoint8Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5});
        Point npoint8D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0});
        Point npoint8Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0});
        Point npoint10Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5});
        Point npoint10D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0});
        Point npoint10Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0});
        Point npoint15Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5});
        Point npoint15D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0});
        Point npoint15Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0});

        Point npoint11_3D = new ArrayPoint(new double[]{11.0,11.0,11.0});
        Point npoint11_5D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0});
        Point npoint11_8D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0});
        Point npoint11_10D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0});
        Point npoint11_15D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0});

        Point[] nadirPointList = {
                npoint3Dmin,
                npoint3D,
//                npoint11_3D,
                npoint3D,
                npoint3D,
                npoint3D,
                npoint3Dmax

                ,

                npoint5Dmin,
                npoint5D,
//                npoint11_5D,
                npoint5D,
                npoint5D,
                npoint5D,
                npoint5Dmax
        };


        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});


        Point[] boundPointList = {
                pointZero3D,
                pointZero3D,
                pointZero3D,
                pointZero3D,
                pointZero3D,
                pointZero3D

                ,

                pointZero5D,
                pointZero5D,
                pointZero5D,
                pointZero5D,
                pointZero5D,
                pointZero5D



        };

        executeFinalNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);
    }


    public void executeDTLZConstraintsMeasureTest(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "C1_DTLZ1(3)",
                "C1_DTLZ3(3)",
                "C2_DTLZ2(3)",
                "ConvexC2_DTLZ2(3)",
                "C3_DTLZ1(3)",
                "C3_DTLZ4(3)"

//                ,
//
//                "C1_DTLZ1(5)",
//                "C1_DTLZ3(5)",
//                "C2_DTLZ2(5)",
//                "ConvexC2_DTLZ2(5)",
//                "C3_DTLZ1(5)",
//                "C3_DTLZ4(5)"
        };


        int[] objNumList = {
                3,
                3,
                3,
                3,
                3,
                3

//                ,
//
//                5,
//                5,
//                5,
//                5,
//                5,
//                5
        };
        int[] popsList = {
                91,
                91,
                91,
                91,
                91,
                91
//
//                ,
//
//                210,
//                210,
//                210,
//                210,
//                210,
//                210
        };
        int[] maxIterationsList = {
                500,
                1000,
                250,
                250,
                750,
                750

//                ,
//
//                600,
//                1500,
//                350,
//                750,
//                1250,
//                1250
        };

        Point npoint3Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5});
        Point npoint3D = new ArrayPoint(new double[]{1.0, 1.0, 1.0});
        Point npoint3Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0});
        Point npoint5Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5, 0.5, 0.5});
        Point npoint5D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0});
        Point npoint5Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0});
        Point npoint8Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5});
        Point npoint8D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0});
        Point npoint8Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0});
        Point npoint10Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5});
        Point npoint10D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0});
        Point npoint10Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0});
        Point npoint15Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5});
        Point npoint15D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0});
        Point npoint15Dmax = new ArrayPoint(new double[]{2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0});

        Point npoint11_3D = new ArrayPoint(new double[]{11.0,11.0,11.0});
        Point npoint11_5D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0});
        Point npoint11_8D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0});
        Point npoint11_10D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0});
        Point npoint11_15D = new ArrayPoint(new double[]{11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0,11.0});



        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});

        Point[] nadirPointList = {
                npoint3Dmin,
                npoint3D,
//                npoint11_3D,
                npoint3D,
                npoint3D,
                npoint3D,
                npoint3Dmax

//                ,
//
//                npoint5Dmin,
//                npoint5D,
////                npoint11_5D,
//                npoint5D,
//                npoint5D,
//                npoint5D,
//                npoint5Dmax
        };


        Point[] boundPointList = {
                pointZero3D,
                pointZero3D,
                pointZero3D,
                pointZero3D,
                pointZero3D,
                pointZero3D

//                ,
//
//                pointZero5D,
//                pointZero5D,
//                pointZero5D,
//                pointZero5D,
//                pointZero5D,
//                pointZero5D
        };

        executeNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);
    }


    public void executeCFConstraintsMeasure(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "CF1(2)",
                "CF2(2)",
                "CF3(2)",
                "CF4(2)",
                "CF5(2)",
                "CF6(2)",
                "CF7(2)",
                "CF8(2)",
                "CF9(2)",
                "CF10(3)"
        };


        int[] objNumList = {
                2,
                2,
                2,
                2,
                2,
                2,
                2,
                3,
                3,
                3
        };
        int[] popsList = {
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                300,
                300,
                300
        };
        int[] maxIterationsList = {
                1000,
                1000,
                1000,
                1000,
                1000,
                1000,
                1000,
                1000,
                1000,
                1000
        };


        Point nadir2D = new ArrayPoint(new double[]{1.0,1.0});
        Point nadir3D = new ArrayPoint(new double[]{1.0,1.0,1.0});

        Point[] nadirPointList = {
                nadir2D,
                nadir2D,
                nadir2D,
                nadir2D,
                nadir2D,
                nadir2D,
                nadir2D,
                nadir3D,
                nadir3D,
                nadir3D
        };


        Point ideal2D = new ArrayPoint(new double[]{1.0,1.0});
        Point ideal3D = new ArrayPoint(new double[]{1.0,1.0,1.0});

        Point[] boundPointList = {
                ideal2D,
                ideal2D,
                ideal2D,
                ideal2D,
                ideal2D,
                ideal2D,
                ideal2D,
                ideal3D,
                ideal3D,
                ideal3D
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "CF1.2D.pf",
                frontDir + "CF2.2D.pf",
                frontDir + "CF3.2D.pf",
                frontDir + "CF4.2D.pf",
                frontDir + "CF5.2D.pf",
                frontDir + "CF6.2D.pf",
                frontDir + "CF7.2D.pf",
                frontDir + "CF8.3D.pf",
                frontDir + "CF9.3D.pf",
                frontDir + "CF10.3D.pf"
        };

        executeIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
        executeNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);
    }


    public void executeConstrainedEngineerFinal(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "CarSideImpact(3)"
                ,
//                "NCarSideImpact(3)",
                "Machining(4)",
                "Water(5)"
//                ,
//                "NWater(5)"
        };


        int[] objNumList = {
                3
//                ,
//                3,
                ,4
//                5
                ,5
        };
        int[] popsList = {
                91
//                ,
//                 91,
                ,165
//                210
                ,210
        };
        int[] maxIterationsList = {
                500
//                ,
//                500,
                ,750
//                1000
                ,1000
        };

        Point point3Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point3Dmax = new ArrayPoint(new double[]{2.2,2.2,2.2});
        Point point5Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55, 0.55, 0.55});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point5Dmax = new ArrayPoint(new double[]{2.2, 2.2, 2.2,2.2, 2.2});
        Point point8Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmax = new ArrayPoint(new double[]{2.2, 2.2, 2.2,2.2, 2.2, 2.2,2.2, 2.2});
        Point point10Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point10Dmax = new ArrayPoint(new double[]{2.2, 2.2, 2.2,2.2, 2.2, 2.2,2.2, 2.2, 2.2,2.2});
        Point point15Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});
        Point point15Dmax = new ArrayPoint(new double[]{2.2, 2.2, 2.2,2.2, 2.2, 2.2,2.2, 2.2, 2.2,2.2, 2.2, 2.2,2.2, 2.2, 2.2});

//        Point pointCar = new ArrayPoint(new double[]{42.768, 4, 12.453});
//        Point pointCar1_1 = new ArrayPoint(new double[]{1.1*42.768, 1.1*4, 1.1*12.453});
//        Point pointWater = new ArrayPoint(new double[]{73451, 1350, 2.8535e+06, 6.5753e+06, 24779});
//        Point pointWater1_1 = new ArrayPoint(new double[]{1.1*73451, 1.1*1350, 1.1*2.8535e+06, 1.1*6.5753e+06, 1.1*24779});

//        Point pointCar = new ArrayPoint(new double[]{42.768011990946945 , 4.0 , 12.521158393308081});
//        Point pointMachining = new ArrayPoint(new double[]{ 3.571283006634979 , -3.7299999999999986 , -3.399999999999996 , -0.30063044650522697});
//        Point pointWater = new ArrayPoint(new double[]{77282.0969033014 , 1350.0 , 2853468.96494178 , 7885544.778071534 , 24999.924323437346});

        Point pointCar = new ArrayPoint(new double[]{43.72712968627474 , 4.0207375 , 12.616684125473485 });
        Point pointMachining = new ArrayPoint(new double[]{ 3.430753084909151 , -3.717557307721173 , -3.370738153311003 , -0.23668639783606066 });
        Point pointWater = new ArrayPoint(new double[]{ 76391.36637750018 , 1415.2598524805635 , 2981875.06836416 , 7567977.289295986 , 26249.5594284981 });


        Point[] nadirPointList = {
                pointCar
                ,
//                pointCar
                pointMachining,
//                ,
//                pointWater,
                pointWater
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});

//        Point pointIdealCar = new ArrayPoint(new double[]{23.586, 3.5852, 10.611});
//        Point pointIdealWater = new ArrayPoint(new double[]{63840, 56.4, 2.8535e+05, 1.8375e+05, 7.2222});

//        Point pointIdealCar = new ArrayPoint(new double[]{23.58565808439098 , 3.58525 , 10.61064375});
//        Point pointIdealMachining = new ArrayPoint(new double[]{2.4167618558793054 , -3.9788538455765083 , -3.9852369337798583 , -1.579511419888553});
//        Point pointIdealWater = new ArrayPoint(new double[]{63840.2774 , 44.80295038873002 , 285346.896494178 , 183749.96706092838 , 7.22222222222193 });

        Point pointIdealCar = new ArrayPoint(new double[]{  22.626540389063184 , 3.5645124999999998 , 10.515118017834595 });
        Point pointIdealMachining = new ArrayPoint(new double[]{  2.3684765592588364 , -3.991296537855334 , -4.014498780468852 , -1.6434554685577192 });
        Point pointIdealWater = new ArrayPoint(new double[]{  63242.60649630951 , -20.456902091833477 , 156940.79307179787 , -167879.90542645534 , -1242.4128828385344 });

        Point[] boundPointList = {
                pointIdealCar
//                ,
//                pointIdealCar,
                ,pointIdealMachining
//                pointIdealWater
                ,pointIdealWater
        };
//
//        for (int i=0;i<3;i++){
//            pointCar1_1.setDimensionValue(i,pointIdealCar.getDimensionValue(i)+1.1*(pointCar.getDimensionValue(i) - pointIdealCar.getDimensionValue(i)));
//        }
//        for (int i=0;i<5;i++){
//            pointWater1_1.setDimensionValue(i,pointIdealWater.getDimensionValue(i)+1.1*pointWater.getDimensionValue(i) - pointIdealWater.getDimensionValue(i));
//        }

        executeFinalNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);
    }


    public void executeUnConstrainedEngineerFinal(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "CrashWorthiness(3)"
                ,
                "UCarSideImpact(9)"
                ,
//                "F_UCarSideImpact(9)"
                "CarCabDesign(9)"
        };

        int[] objNumList = {
                3
                ,
                9
                ,
                9
        };
        int[] popsList = {
                153
                ,
                210
                ,
                210
        };
        int[] maxIterationsList = {
                200
                ,
                2000
                ,
                2000
        };

//        Point pointUCar = new ArrayPoint(new double[]{26.05153, 0.07172,0.01,0.01,0.16307,2.74974,0.42725,0.22561,0.35715});
//        Point pointUCar1_1 = new ArrayPoint(new double[]{1.1*42.768, 1.1, 1.1,1.1,1.1,1.1,5.5,1.1,1.1});
//        Point pointCrash = new ArrayPoint(new double[]{1695.2002, 10.7454,0.26400});
//        Point pointCrash1_1 = new ArrayPoint(new double[]{1.1*26.05153, 1.1*0.07172,1.1*0.01,1.1*0.01,1.1*0.16307,1.1*2.74974,1.1*0.42725,1.1*0.22561,1.1*0.35715});

//        Point pointUCar = new ArrayPoint(new double[]{42.5170522401881 , -0.06696880223325141 , -0.07264283306703384 , -0.09801355894196942 , -0.010377742816105873 , -2.689108372855639 , 0.3509644836244803 , -0.6523454415340666 , 0.35714999996868});
        Point pointUCar = new ArrayPoint(new double[]{ 44.127612395693475 , 0.1056387399997471 , -0.024999195143077163 , -0.0674647127157054 , 0.174973843105539 , 3.8488146046989367 , 0.46934999999567373 , 0.3165462500000005 , 0.5142900000000008 });

//        Point pointCrash = new ArrayPoint(new double[]{1695.2002034931022 , 10.74539999986672 , 0.26399999998519663});
        Point pointCrash = new ArrayPoint(new double[]{ 1696.874822542648 , 10.975529999823332 , 0.2752299999825989 });

        Point pointFUCar = new ArrayPoint(new double[]{42.5170522401881 , -0.06696880223325141 , -0.07264283306703384 , -0.09801355894196942 , -0.010377742816105873 , -2.689108372855639 , 0.0 , -0.6523454415340666 , 0.0});


        Point pointUCar2 = new ArrayPoint(new double[]{42.5170522401881 , 1.0 , 1.0 , 1.0 , 1.0 , 1.0 , 0.3509644836244803 , - 0.6523454415340666 , 0.35714999996868});
        Point pointUCar3 = new ArrayPoint(new double[]{42.76801199531525 , 0.07172109999999998 , -0.03487982440567505 , -0.07556587876345999 , 0.16307069999999996 , 3.1914307031822275 , 0.4272499999999999 , 0.22561250000000044 , 0.35715000000000074 });

//        Point pointCarCab = new ArrayPoint(new double[]{ -1345.9479185614514 , -95.3332544375 , -1022.8869498418009 , 178278.53421301444 , -29.357032822925728 , 1.8652415443765662 , -74.91081243128417 , 72.85544051050297 , -100.98664514564578 });
        Point pointCarCab = new ArrayPoint(new double[]{ -1342.739505924534 , -94.5289769375 , -1020.4975813474825 , 179914.04643270464 , -29.250417897542313 , 1.8790622376961845 , -74.83952348595844 , 72.95703193663532 , -98.44584453475915 });

        Point[] nadirPointList = {
                pointCrash,
                pointUCar
                ,
//                pointFUCar
//                pointUCar2
//                pointUCar3
                pointCarCab
        };

//        Point pointIdealUCar = new ArrayPoint(new double[]{26.473725439753455 , -0.6065090379980109 , -0.20575823598963872 , -0.21751834232301726 , -0.07499189548328733 , -10.84248000288643 , -0.41183427718929844 , -1.5930625000000003 , -2.7443827938042613});
//        Point pointIdealCrash = new ArrayPoint(new double[]{1661.707822502186 , 6.142800000734479 , 0.03940000003715144});
        Point pointIdealUCar = new ArrayPoint(new double[]{14.216403600205076 , -0.6405493399946892 , -0.2154455944808858 , -0.22539814222077087 , -0.0868953052163203 , -11.604929931813963 , -0.4568499999091499 , -1.6839962500000003 , -2.9427900000000005});
        Point pointIdealCrash = new ArrayPoint(new double[]{ 1660.0332034526402 , 5.912670000777867 , 0.028170000039749177  });


        Point pointIdealUCar2 = new ArrayPoint(new double[]{26.473725439753455 , 0 , 0 , 0 , 0 , 0 , -0.41183427718929844 , -1.5930625000000003 , -2.7443827938042613});
        Point pointIdealUCar3 = new ArrayPoint(new double[]{15.576004000000003 , -0.6066317000000001 , -0.20678893999999998 , -0.21821934999999998 , -0.07499216197804007 , -10.902487 , -0.4147500000000002 , -1.5930625000000003 , -2.7856500000000004});
//        Point pointIdealCarCab  = new ArrayPoint(new double[]{-1410.1161712998005 , -111.4188044375 , -1070.6743197281676 , 145568.28981921045 , -31.489331330594005 , 1.5888276779842 , -76.33659133779895 , 70.82361198785604 , -151.80265736337833});
        Point pointIdealCarCab  = new ArrayPoint(new double[]{ -1413.324583936718 , -112.2230819375 , -1073.063688222486 , 143932.77759952025 , -31.59594625597742 , 1.5750069846645818 , -76.40788028312468 , 70.7220205617237 , -154.34345797426496 });

        Point[] boundPointList = {
                pointIdealCrash
                ,
                pointIdealUCar
                ,
//                pointIdealUCar
//                pointIdealUCar2
//                pointIdealUCar3
                pointIdealCarCab
        };

//        for (int i=0;i<3;i++){
//            pointCrash1_1.setDimensionValue(i,pointIdealCrash.getDimensionValue(i)+1.1*(pointCrash.getDimensionValue(i) - pointIdealCrash.getDimensionValue(i)));
//        }
//        for (int i=0;i<9;i++){
//            pointUCar1_1.setDimensionValue(i,pointIdealUCar.getDimensionValue(i)+1.1*(pointUCar.getDimensionValue(i) - pointIdealUCar.getDimensionValue(i)));
//        }
//
//        Point[] hvRefPointList = {
//              pointCrash1_1
//                ,pointUCar1_1
//        };

        double[] hvCubeList = {

                0.14554166666666670000,

                0.80740122440170147833,


                0.80740122440170147833,

                0.80740122440170147833
        };

//        executeFinalHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
        executeFinalNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);
    }


    public void executeMaOPHighFinal(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "DTLZ1(10)","DTLZ1(15)",
                "DTLZ2(10)","DTLZ2(15)",
                "DTLZ3(10)","DTLZ3(15)",
                "DTLZ4(10)","DTLZ4(15)",
                "Convex_DTLZ2(10)","Convex_DTLZ2(15)"
        };

        int[] objNumList = {
                10,15,
                10,15,
                10,15,
                10,15,
                10,15
        };
        int[] popsList = {
                275, 135,
                275, 135,
                275, 135,
                275, 135,
                275, 135
        };

        int[] maxIterationsList = {
                1000,1500,
                750,1000,
                1500,2000,
                2000,3000,
                4000,4500
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//
        String[] frontFileList = {

                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ1.15D.pf[54264]",

                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ2.15D.pf[54264]",

                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ3.15D.pf[54264]",

                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "DTLZ4.15D.pf[54264]",

                frontDir + "Convex_DTLZ2.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.15D.pf[54264]"
        };

        Point point3Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point5Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55, 0.55, 0.55});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point10Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point15Dmin = new ArrayPoint(new double[]{0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55,0.55, 0.55, 0.55});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});
//        Point point3Dmin = new ArrayPoint(new double[]{1, 1, 1});
//        Point point3D = new ArrayPoint(new double[]{2, 2, 2});
//        Point point5Dmin = new ArrayPoint(new double[]{1, 1, 1, 1, 1});
//        Point point5D = new ArrayPoint(new double[]{2, 2, 2,2, 2});
//        Point point8Dmin = new ArrayPoint(new double[]{1, 1, 1,1, 1, 1,1, 1});
//        Point point8D = new ArrayPoint(new double[]{2, 2, 2,2, 2, 2,2, 2});
//        Point point10Dmin = new ArrayPoint(new double[]{1, 1, 1,1, 1, 1,1, 1, 1,1});
//        Point point10D = new ArrayPoint(new double[]{2, 2, 2,2, 2, 2,2, 2, 2,2});
//        Point point15Dmin = new ArrayPoint(new double[]{1, 1, 1,1, 1, 1,1, 1, 1,1, 1, 1,1, 1, 1});
//        Point point15D = new ArrayPoint(new double[]{2, 2, 2,2, 2, 2,2, 2, 2,2, 2, 2,2, 2, 2});
//        Point point3Dmin = new ArrayPoint(new double[]{11, 11, 11});
//        Point point3D = new ArrayPoint(new double[]{11, 11, 11});
//        Point point5Dmin = new ArrayPoint(new double[]{11, 11, 11, 11, 11});
//        Point point5D = new ArrayPoint(new double[]{11, 11, 11, 11, 11});
//        Point point8Dmin = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11});
//        Point point8D = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11});
//        Point point10Dmin = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11, 11,11});
//        Point point10D = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11, 11,11});
//        Point point15Dmin = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11, 11,11, 11, 11,11, 11, 11});
//        Point point15D = new ArrayPoint(new double[]{11, 11, 11,11, 11, 11,11, 11, 11,11, 11, 11,11, 11, 11});

        Point[] hvRefPointList = {
                point10Dmin,point15Dmin,
                point10D,point15D,
                point10D,point15D,
                point10D,point15D,
                point10D,point15D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D,
                pointZero10D,pointZero15D
        };


        double[] hvCubeList = {
                0.00253295135207696300,
                0.00012747949735763220,

                2.59125206552980946739,
                4.17723652869053437456,

                2.59125206552980946739,
                4.17723652869053437456,

                2.59125206552980946739,
                4.17723652869053437456,

                2.59374246010000231166,
                4.17724816941565624262
        };

//        executeFinalIGD(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
//        executeFinalIGDPlus(dataDir,statDir,maxRun,problemNameList,popsList,maxIterationsList,frontFileList,algorithmNameList);
        executeFinalHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,hvRefPointList,boundPointList,hvCubeList,numOfSample,approximateDim,algorithmNameList);
    }


    public void executeIrregularFinal(String dataDir,String statDir,int maxRun, String[] algorithmNameList,int approximateDim, int numOfSample){
        String[] problemNameList = {
                "DTLZ5(3)"
                ,"DTLZ5(5)","DTLZ5(8)","DTLZ5(10)","DTLZ5(15)"
                ,
                "DTLZ6(3)"
                ,"DTLZ6(5)","DTLZ6(8)","DTLZ6(10)","DTLZ6(15)"
                ,
                "DTLZ7(3)"
                ,"DTLZ7(5)","DTLZ7(8)","DTLZ7(10)","DTLZ7(15)"
                ,
                "InvertedDTLZ1(3)"
                ,"InvertedDTLZ1(5)","InvertedDTLZ1(8)", "InvertedDTLZ1(10)","InvertedDTLZ1(15)"
        };

        int[] objNumList = {
                3
                ,5,8,10,15
                ,
                3
                ,5,8,10,15
                ,
                3
                ,5,8,10,15
                ,
                3
                ,5,8,10,15
        };
        int[] popsList = {
                91
                , 210, 156,275, 135
                ,
                91
                , 210, 156,275, 135
                ,
                91
                , 210, 156,275, 135
                ,
                91
                , 210, 156,275, 135
        };

        int[] maxIterationsList = {
                500
                ,750,1000,1500,2000
                ,
                500
                ,750,1000,1500,2000
                ,
                500
                ,750,1000,1500,2000
                ,
                500
                ,750,1000,1500,2000
        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
//
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[8001]",
                frontDir + "DTLZ1.5D.pf[15981]",
                frontDir + "DTLZ1.8D.pf[31824]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ1.15D.pf[54264]",
                frontDir + "DTLZ2.3D.pf[8001]",
                frontDir + "DTLZ2.5D.pf[15981]",
                frontDir + "DTLZ2.8D.pf[31824]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ2.15D.pf[54264]",
                frontDir + "DTLZ3.3D.pf[8001]",
                frontDir + "DTLZ3.5D.pf[15981]",
                frontDir + "DTLZ3.8D.pf[31824]",
                frontDir + "DTLZ3.10D.pf[43758]",
                frontDir + "DTLZ3.15D.pf[54264]",
                frontDir + "DTLZ4.3D.pf[8001]",
                frontDir + "DTLZ4.5D.pf[15981]",
                frontDir + "DTLZ4.8D.pf[31824]",
                frontDir + "DTLZ4.10D.pf[43758]",
                frontDir + "DTLZ4.15D.pf[54264]",
                frontDir + "Convex_DTLZ2.3D.pf[8001]",
                frontDir + "Convex_DTLZ2.5D.pf[15981]",
                frontDir + "Convex_DTLZ2.8D.pf[31824]",
                frontDir + "Convex_DTLZ2.10D.pf[43758]",
                frontDir + "Convex_DTLZ2.15D.pf[54264]"
        };
//        String[] frontFileList = {
//                frontDir + "DTLZ1.3D.pf[91]",
//                frontDir + "DTLZ1.5D.pf[210]",
//                frontDir + "DTLZ1.8D.pf[156]",
//                frontDir + "DTLZ1.10D.pf[275]",
//                frontDir + "DTLZ1.15D.pf[135]",
//                frontDir + "DTLZ2.3D.pf[91]",
//                frontDir + "DTLZ2.5D.pf[210]",
//                frontDir + "DTLZ2.8D.pf[156]",
//                frontDir + "DTLZ2.10D.pf[275]",
//                frontDir + "DTLZ2.15D.pf[135]",
//                frontDir + "DTLZ3.3D.pf[91]",
//                frontDir + "DTLZ3.5D.pf[210]",
//                frontDir + "DTLZ3.8D.pf[156]",
//                frontDir + "DTLZ3.10D.pf[275]",
//                frontDir + "DTLZ3.15D.pf[135]",
//                frontDir + "DTLZ4.3D.pf[91]",
//                frontDir + "DTLZ4.5D.pf[210]",
//                frontDir + "DTLZ4.8D.pf[156]",
//                frontDir + "DTLZ4.10D.pf[275]",
//                frontDir + "DTLZ4.15D.pf[135]",
//                frontDir + "Convex_DTLZ2.3D.pf[91]",
//                frontDir + "Convex_DTLZ2.5D.pf[210]",
//                frontDir + "Convex_DTLZ2.8D.pf[156]",
//                frontDir + "Convex_DTLZ2.10D.pf[275]",
//                frontDir + "Convex_DTLZ2.15D.pf[135]"
//        };


        Point point3Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5});
        Point point3D = new ArrayPoint(new double[]{1.0, 1.0, 1.0});
        Point point5Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5, 0.5, 0.5});
        Point point5D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0});
        Point point8Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5});
        Point point8D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0});
        Point point10Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5});
        Point point10D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0});
        Point point15Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5});
        Point point15D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0});

//        Point p5_3D = new ArrayPoint(new double[]{0.7186755941911256,0.7188216378970089, 1.0000000003573788 });
//        Point p5_5D = new ArrayPoint(new double[]{0.9418631576802451, 1.0216784976191902,3.315698255115983,3.412246812764775,1.000530337981155});
//        Point p5_8D = new ArrayPoint(new double[]{ 1.6497050219425389, 1.399449275416734,3.0690038911478505 ,3.1619718958122034 ,3.2432883164731603,3.326695500549648,3.4122460054758554 ,1.0004331959827706});
//        Point p5_10D = new ArrayPoint(new double[]{ 1.0561691008834828 ,0.8421615908703666 ,2.8307836509840087 ,2.8705530108897848,2.855375493736204,3.116568268990962,3.2432881925047936 , 3.3266832213709905,3.41224731094647 ,1.0002802902235108 });
//        Point p5_15D = new ArrayPoint(new double[]{ 0.9894535935606902,1.0150012611210153,1.070459962666478,1.1190580575635487,0.9919296451011165 , 1.4081943542568183,1.2460794116244116,2.9201224116929105,1.5620609918547361,2.3672236851246704, 2.7741865310033074,3.2418103511526457,3.3081566059211442, 3.4122473330575205,3.425826025481662});
        Point p5_3D = new ArrayPoint(new double[]{1.0,1.0, 1.0 });
        Point p5_5D = new ArrayPoint(new double[]{1.0, 1.0,4.0,4.0,1.0});
        Point p5_8D = new ArrayPoint(new double[]{ 2.0,2.0,3.0 ,4.0 ,4.0,4.0,4.0 ,1.0});
        Point p5_10D = new ArrayPoint(new double[]{ 1.0 ,1.0 ,3.0 ,3.0,3.0,4.0,4.0 ,4.0,4.0 ,1.0 });
        Point p5_15D = new ArrayPoint(new double[]{ 1.0,1.0,1.0,2.0,1.0 ,2.0,2.0,3.0,2.0,3.0, 3.0,4.0,4.0,4.0,4.0});

//        Point p6_3D = new ArrayPoint(new double[]{ 0.7244899733349346, 0.7499497706738386 ,  1.0});
//        Point p6_5D = new ArrayPoint(new double[]{2.456470211921555 , 1.967073672527626 , 10.94401793034341 ,  10.971972140091358 , 1.297300313249616 });
//        Point p6_8D = new ArrayPoint(new double[]{10.416663898852184  , 10.536399582490407 , 10.839460125150373 , 10.842603804668716 , 10.83317254418388 , 10.943255103769177 , 10.91936728229286 , 1.6453077708648474   });
//        Point p6_10D = new ArrayPoint(new double[]{ 9.380800554983894 , 9.383310948934938 , 10.211315732044625 , 10.81632460686074 , 10.682865474272688  ,10.883885417923986 , 10.911984015339565,  10.924311566735126 , 10.97169137960642 , 10.890969195798073     });
//        Point p6_15D = new ArrayPoint(new double[]{9.192510059617222  , 9.630438605942016 ,  9.108543060611531 , 10.006678513930455 ,  10.053953518845994 , 10.149322036424381 , 9.201525800656006  ,10.105821750804791 , 10.203679189460239 , 10.855010450441284 , 10.872805556227863 ,  10.888226792875326 , 10.943961333867845 , 10.971766718166414 , 10.757487484976629 });
        Point p6_3D = new ArrayPoint(new double[]{ 1.0, 1.0 ,  1.0});
        Point p6_5D = new ArrayPoint(new double[]{3.0, 2.0 , 11.0 , 11.0 ,2.0 });
        Point p6_8D = new ArrayPoint(new double[]{11.0 , 11.0 ,11.0 , 11.0 , 11.0 , 11.0 , 11.0 , 11.0  });
        Point p6_10D = new ArrayPoint(new double[]{11.0 , 11.0 ,11.0 , 11.0 , 11.0 , 11.0 , 11.0 , 11.0 ,11.0 , 11.0 });
        Point p6_15D = new ArrayPoint(new double[]{11.0 , 11.0 ,11.0 , 11.0 , 11.0 , 11.0 , 11.0 , 11.0 ,11.0 , 11.0 , 11.0 ,11.0 , 11.0 , 11.0 , 11.0});

//        Point p7_3D = new ArrayPoint(new double[]{0.8823031256779991,0.8839524066121905,5.999999799352565});
//        Point p7_5D = new ArrayPoint(new double[]{0.9434981541057926,0.9752310452615462,0.9568700693100819,0.9654750895728885,9.999999946437562});
//        Point p7_8D = new ArrayPoint(new double[]{0.9550531981507313,0.9553867831492282,0.9627410284941865,0.9504314897329504,0.9564406577592235,0.9932180938987621,0.9977520044672878,15.9999990351596});
//        Point p7_10D = new ArrayPoint(new double[]{0.9994766114113571,0.999945548715627,0.9998858743468757,0.9999094525396945,0.9999480834956957,0.9993350045956148,0.9999250157481507,0.9998987508679884,0.9995799430791926,75.71678519299999});
//        Point p7_15D = new ArrayPoint(new double[]{0.9999749679538876,0.9999813967120404,0.9999863837504789,0.9999376744968368,0.9999590163019647, 0.9999916485999341,0.999979859126295, 0.9999817885250342, 0.9999994976814529,0.9999853461518721, 0.999993189913248,0.99998745194808,0.9999883754481094, 0.99994054904864,118.25354894825256});
        Point p7_3D = new ArrayPoint(new double[]{1.0,1.0,6.0});
        Point p7_5D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,10.0});
        Point p7_8D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,16.0});
        Point p7_10D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,76.0});
        Point p7_15D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0, 1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,120.0});

        Point[] nadirPointList = {
//                point3D,point5D,point8D,point10D,point15D,
//                point3D,point5D,point8D,point10D,point15D,


                p5_3D
                ,p5_5D,p5_8D,p5_10D,p5_15D
                ,
                p6_3D
                ,p6_5D,p6_8D,p6_10D,p6_15D
                ,
                p7_3D
                ,p7_5D,p7_8D,p7_10D,p7_15D
                ,
                point3D
                ,point5D,point8D,point10D,point15D
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
//        Point pZ7_3D = new ArrayPoint(new double[]{5.74511715562493E-16,0,2.6140199319402986});
//        Point pZ7_5D = new ArrayPoint(new double[]{0,4.628333199129033E-16,2.1892565388802256E-15, 2.5459234210915353E-15,3.228115949685426});
//        Point pZ7_8D = new ArrayPoint(new double[]{1.4755074371584203E-15,3.098117664899809E-14,2.0894127030611863E-17,1.4029667232567789E-14,1.3302862326140923E-13,7.613488951330515E-16,1.792710717343876E-15,4.149035905977044});
//        Point pZ7_10D = new ArrayPoint(new double[]{1.0177030687826027E-5,1.7893882634544855E-6,4.358643858693892E-5,3.98877388474439E-6,1.7187020679008246E-5,1.0199503718520686E-6, 2.112695790525941E-6,2.4836833610191825E-6,3.0731067861666794E-5,5.169153687267617});
//        Point pZ7_15D = new ArrayPoint(new double[]{1.0657181079731792E-5,8.169466643394155E-6,1.7393477121596668E-4,2.3335723577349004E-5,2.390638006316559E-5, 5.555893455293226E-6,6.181711572961213E-6,4.192951552850488E-6,8.803048941357196E-6,2.8530937141169055E-5,1.6642845916792215E-4,5.032096329471436E-5,2.1037111000044018E-4,3.646154929919099E-5, 6.907673733019576});
        Point pZ7_3D = new ArrayPoint(new double[]{0,0,2.0});
        Point pZ7_5D = new ArrayPoint(new double[]{0,0,0,0,3.0});
        Point pZ7_8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,4.0});
        Point pZ7_10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,5.0});
        Point pZ7_15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,6.0});

        Point[] boundPointList = {
                pointZero3D
                ,pointZero5D,pointZero8D,pointZero10D,pointZero15D
                ,
                pointZero3D
                ,pointZero5D,pointZero8D,pointZero10D,pointZero15D
                ,
                pZ7_3D
                ,pZ7_5D,pZ7_8D,pZ7_10D,pZ7_15D
                ,
                pointZero3D
                ,pointZero5D,pointZero8D,pointZero10D,pointZero15D
//                ,
//                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D
        };

        double[] hvCubeList = {
//                0.322166666666666601237523082091,
//                0.167809583333333262045172773469,
//                0.0576479131187995763596632059489,
//                0.0282475246308855378052360407537,
//                0.00474756150994297236589414268337,
                0.14554166666666670000,
                0.05006802083333336000,
                0.00837329690786210900,
                0.00253295135207696300,
                0.00012747949735763220,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                0.80740122440170147833,
                1.44601659331517806706,
                2.12773446575618585541,
                2.59125206552980946739,
                4.17723652869053437456,

                1.33100000000000040501,
                1.61051000000000077428,
                2.14358881000000156547,
                2.59374246010000231166,
                4.17724816941565624262
        };
        executeFinalNormalizedHV(dataDir,statDir,maxRun,problemNameList,objNumList,popsList,maxIterationsList,nadirPointList,boundPointList,numOfSample,approximateDim,algorithmNameList);
    }

    public void findExtremePoints(){
        String dataDir = "E://ResultsApproximateEngineer/";
//        String dataDir = "E://ResultsEngineerUnConstraintsPoint/";
        String savePath = dataDir+"extremePoints6.txt";
        String[] instances = {
                "CrashWorthiness(3)_10011_2000",
                "UCarSideImpact(9)_43758_3000",
                "CarCabDesign(9)_43758_3000"
                ,
                "CarSideImpact(3)_10011_2000",
                "Machining(4)_10660_2500",
                "Water(5)_10626_3000"

        };
        int[] nObjs = {
                3,
                9,
                9,
                3,
                4,
                5
        };
        String[][] algorithmNames = {
                new String[]{
                        "MOEADN_PBI",
                        "MOEACD-N"
                }
                ,
                new String[]{
                        "MOEADN_PBI"
                        ,
                        "MOEACD-N"
                },
                new String[]{
                        "MOEADN_PBI",
                        "MOEACD-N"
                }
                ,
                new String[]{
                        "CMOEADN_PBI",
                        "C-MOEACD-N",
                        "C-MOEACD-NAD"
                },
                new String[]{
                        "CMOEADN_PBI",
                        "C-MOEACD-N",
                        "C-MOEACD-ND",
                        "C-MOEACD-NAD"
                },
                new String[]{
                        "CMOEADN_PBI",
                        "C-MOEACD-N",
                        "C-MOEACD-NAD"
                }

        };

        MyExperimentAnalysis tool = new MyExperimentAnalysis("","");
        try {
            BufferedWriter writer = new DefaultFileOutputContext(savePath).getFileWriter();
            for (int i = 0; i < instances.length; i++) {
                String outputStr = "[ "+instances[i]+" ]\n";
                JMetalLogger.logger.info(outputStr);

                Vector<Point> newFront = new Vector<>();
                for (int j = 0; j < algorithmNames[i].length; j++) {
                    outputStr += "< "+algorithmNames[i][j]+" >\n";
                    String pfPath = dataDir + "/POF/" + algorithmNames[i][j] + "_" + instances[i] + "R0.pof";
                    JMetalLogger.logger.info(pfPath+"\n");
                    WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(pfPath);
                    double[] nadirPoint = new double[nObjs[i]];
                    for (int k = 0; k < nObjs[i]; k++) {
                        nadirPoint[k] = Double.NEGATIVE_INFINITY;
                    }
                    double[] idealPoint = new double[nObjs[i]];
                    for (int k = 0; k < nObjs[i]; k++) {
                        idealPoint[k] = Double.POSITIVE_INFINITY;
                    }
                    for (int p = 0; p < solutionFront.getNumberOfPoints(); p++) {
                        Point point = solutionFront.getPoint(p);
                        newFront.add(point);
                        for (int k = 0; k < nObjs[i]; k++) {
                            nadirPoint[k] = Math.max(nadirPoint[k], point.getDimensionValue(k));
                            idealPoint[k] = Math.min(idealPoint[k], point.getDimensionValue(k));
                        }
                    }
                    outputStr += "Ideal Point   \t( ";
                    for (int k=0;k<nObjs[i];k++){
                        outputStr += idealPoint[k];
                        if(k != nObjs[i]-1)
                            outputStr += " , ";
                    }
                    outputStr += " )\n";

                    outputStr += "Nadir Point   \t( ";
                    for (int k=0;k<nObjs[i];k++){
                        outputStr += nadirPoint[k];
                        if(k != nObjs[i]-1)
                            outputStr += " , ";
                    }
                    outputStr += " )\n";
                }

                JMetalLogger.logger.info("Merge Size : "+newFront.size()+"\n");
                int[] dominationCount = new int[newFront.size()];
                for (int j=0;j<newFront.size();j++){
                    dominationCount[j] = 0;
                }

                for (int j=0;j<newFront.size();j++){
                    for(int k=j+1;k<newFront.size();k++) {
                        int domination = dominates(newFront.get(j),newFront.get(k));
                        if(domination == -1 || domination == 2){
                            dominationCount[k]++;
                        }else if(domination == 1){
                            dominationCount[j]++;
                        }
                    }
                }

                for (int j=newFront.size()-1;j>=0;j--){
                    if(dominationCount[j]>0){
                        newFront.remove(j);
                    }
                }

                JMetalLogger.logger.info("Merge Filter Size : "+newFront.size()+"\n");

                String newPFName = dataDir + "/POF/" + algorithmNames[i][0] + "_M_" + instances[i] + "R0.pof";
                BufferedWriter newFrontWriter = new DefaultFileOutputContext(newPFName).getFileWriter();
                tool.saveFront(newFront, newFrontWriter);
                newFrontWriter.close();

                double[] nadirPointMerge = new double[nObjs[i]];
                for (int k = 0; k < nObjs[i]; k++) {
                    nadirPointMerge[k] = Double.NEGATIVE_INFINITY;
                }
                double[] idealPointMerge = new double[nObjs[i]];
                for (int k = 0; k < nObjs[i]; k++) {
                    idealPointMerge[k] = Double.POSITIVE_INFINITY;
                }

                for (int p = 0; p < newFront.size(); p++) {
                    Point point = newFront.get(p);
                    for (int k = 0; k < nObjs[i]; k++) {
                        nadirPointMerge[k] = Math.max(nadirPointMerge[k], point.getDimensionValue(k));
                        idealPointMerge[k] = Math.min(idealPointMerge[k], point.getDimensionValue(k));
                    }
                }

                outputStr += "\n< Merge >\n";
                outputStr += "Ideal Point   \t( ";
                for (int k=0;k<nObjs[i];k++){
                    outputStr += idealPointMerge[k];
                    if(k != nObjs[i]-1)
                        outputStr += " , ";
                }
                outputStr += " )\n";

                outputStr += "Nadir Point   \t( ";
                for (int k=0;k<nObjs[i];k++){
                    outputStr += nadirPointMerge[k];
                    if(k != nObjs[i]-1)
                        outputStr += " , ";
                }
                outputStr += " )\n";

                double[] interval = new double[nObjs[i]];
                for (int k = 0; k < nObjs[i]; k++) {
                    interval[k] =  (nadirPointMerge[k] - idealPointMerge[k]);
                }

                for (int k = 0; k < nObjs[i]; k++) {
                    nadirPointMerge[k] = nadirPointMerge[k] + 0.05 * interval[k];
                    idealPointMerge[k] = idealPointMerge[k] - 0.05 * interval[k];
                }

                outputStr += "\n< Final >\n";
                outputStr += "Ideal Point   \t( ";
                for (int k=0;k<nObjs[i];k++){
                    outputStr += idealPointMerge[k];
                    if(k != nObjs[i]-1)
                        outputStr += " , ";
                }
                outputStr += " )\n";

                outputStr += "Nadir Point   \t( ";
                for (int k=0;k<nObjs[i];k++){
                    outputStr += nadirPointMerge[k];
                    if(k != nObjs[i]-1)
                        outputStr += " , ";
                }
                outputStr += " )\n";

                outputStr += "\n\n";
                JMetalLogger.logger.info(outputStr);
                writer.write(outputStr);
            }
            writer.close();
        }catch (IOException e){}

    }


    public void filterUCarSideImpact(){
        int run = 20;
        String[] dirs = {
                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\UnConstraints\\MOEACD\\",
                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\UnConstraints\\MOEACD\\",
                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\UnConstraints\\MOEACD\\",
                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\UnConstraints\\compare\\",
                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\UnConstraints\\compare\\"
        };
        String[] algorithmNames = {
                "MOEACD",
                "MOEACD-N",
                "MOEACD-ND",
                "MOEADN_PBI",
                "MOEADDN_PBI"
        };;
        MyExperimentAnalysis tool = new MyExperimentAnalysis("","");
        try {
            for (int i = 0; i < dirs.length; i++) {
                for (int r = 0; r < run; r++) {
                    String solutionPF = dirs[i] + "POF\\" + algorithmNames[i] + "_UCarSideImpact(9)_210_2000R" + r + ".pof";
                    WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                    Vector<Point> newFront = new Vector<>(solutionFront.getNumberOfPoints());
                    for (int j = 0; j < solutionFront.getNumberOfPoints(); j++) {
                        boolean isFeasible = true;
                        for (int k = 1; k <= 8; k++) {
                            if (solutionFront.getPoint(j).getDimensionValue(k) > 0.0) {
                                isFeasible = false;
                                break;
                            }
                        }
                        if (isFeasible) {
                            newFront.add(solutionFront.getPoint(j));
                        }
                    }
                    String newPFName = dirs[i] + "POF\\" + algorithmNames[i] + "_F_UCarSideImpact(9)_210_2000R" + r + ".pof";
                    BufferedWriter writer = new DefaultFileOutputContext(newPFName).getFileWriter();
                    tool.saveFront(newFront, writer);
                    writer.close();
                }
            }
        }catch (IOException e){}
    }

    public void transformUCarSideImpact(){
        int run = 20;
        String[] dirs = {
                "E:\\ResultsEngineerUnConstraintsMOEACD6\\",
                "E:\\ResultsEngineerUnConstraintsMOEACD6\\",
                "E:\\ResultsEngineerUnConstraintsMOEACD6\\",
                "E:\\ResultsEngineerUnConstraintsCompare6\\",
                "E:\\ResultsEngineerUnConstraintsCompare6\\"
        };
        String[] algorithmNames = {
                "MOEACD",
                "MOEACD-N",
                "MOEACD-ND",
                "MOEADN_PBI",
                "MOEADDN_PBI"
        };;
        MyExperimentAnalysis tool = new MyExperimentAnalysis("","");
        try {
            for (int i = 0; i < dirs.length; i++) {
                for (int r = 0; r < run; r++) {
                    String solutionPF = dirs[i] + "POF\\" + algorithmNames[i] + "_UCarSideImpact(9)_210_2000R" + r + ".pof";
                    WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                    Vector<Point> newFront = new Vector<>(solutionFront.getNumberOfPoints());
                    for (int j = 0; j < solutionFront.getNumberOfPoints(); j++) {
                        boolean isFeasible = true;
                        for (int k = 1; k <= 8; k++) {
                            if (solutionFront.getPoint(j).getDimensionValue(k) > 0.0) {
                                isFeasible = false;
                                break;
                            }
                        }
                        if (isFeasible) {
                            Point p = new ArrayPoint(3);
                            p.setDimensionValue(0, solutionFront.getPoint(j).getDimensionValue(0));
                            p.setDimensionValue(1, solutionFront.getPoint(j).getDimensionValue(6) + 4.0);
                            p.setDimensionValue(2, 0.5 * ((solutionFront.getPoint(j).getDimensionValue(7) + 9.9) + (solutionFront.getPoint(j).getDimensionValue(8) + 15.7)));
                            newFront.add(p);
                        }
                    }
                    String newPFName = dirs[i] + "POF\\" + algorithmNames[i] + "_T_UCarSideImpact(9)_210_2000R" + r + ".pof";
                    BufferedWriter writer = new DefaultFileOutputContext(newPFName).getFileWriter();
                    tool.saveFront(newFront, writer);
                    writer.close();
                }
            }
        }catch (IOException e){}
    }

    public void normalizeEngineerPF(){
        int run = 20;
        String[] instances = {
                "CrashWorthiness(3)_153_200",
                "UCarSideImpact(9)_210_2000"
                ,
                "CarCabDesign(9)_210_2000"
//                ,
//                "T_UCarSideImpact(9)_210_2000"
//                ,

//                "CarSideImpact(3)_91_500",
//                "Machining(4)_165_750",
//                "Water(5)_210_1000"
//                ,

        };
        String[] dirs = {
//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\UnConstraints\\MOEACD\\"
//
//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\UnConstraints\\MOEACD\\",
//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\UnConstraints\\MOEACD\\",
//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\UnConstraints\\MOEACD\\",
//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\UnConstraints\\compare\\",
//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\UnConstraints\\compare\\"
////                ,
//
//                "E:\\ResultsEngineerUnConstraintsMOEACD7\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD7\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD7\\",
//                "E:\\ResultsEngineerUnConstraintsCompare7\\",
//                "E:\\ResultsEngineerUnConstraintsCompare7\\"


//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\Constraints\\MOEACD\\",
//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\Constraints\\MOEACD\\",
//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\Constraints\\MOEACD\\",
//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\Constraints\\MOEACD\\",
//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\Constraints\\compare\\",
//                "D:\\Experiments\\ExperimentDataThesis\\Engineer\\Constraints\\compare\\"
//                "E:\\ResultsEngineerUnConstraintsMOEACD8\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD8\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD8\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD8\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD8\\"
//                "E:\\ResultsEngineerUnConstraintsMOEACD12\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD12\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD12\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD12\\"
//                "E:\\ResultsEngineerUnConstraintsMOEACD13\\"

//                "E:\\ResultsEngineerUnConstraintsMOEACD15\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD16\\"
//                ,
//                "E:\\ResultsEngineerUnConstraintsMOEACD16\\"

//                "E:\\ResultsEngineerUnConstraintsMOEACD17\\",
//                "E:\\ResultsEngineerUnConstraintsCompare17\\"
//                ,
//                "E:\\ResultsEngineerUnConstraintsCompare15\\"

//                "E:\\ResultsEngineerConstraintsMOEACD15\\",
//                "E:\\ResultsEngineerConstraintsMOEACD15\\",
//                "E:\\ResultsEngineerConstraintsMOEACD15\\",
//                "E:\\ResultsEngineerConstraintsMOEACD17\\",
//                "E:\\ResultsEngineerConstraintsCompare17\\"
//                ,
//                "E:\\ResultsEngineerConstraintsCompare15\\"


//                "E:\\ResultsEngineerUnConstraintsMOEACD20\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD20\\",
//                "E:\\ResultsEngineerUnConstraintsMOEACD22\\"
                "E:\\ResultsEngineerUnConstraintsMOEACDWithoutEvolving\\"
        };
        String[] algorithmNames = {
//                "MOEACD",
////                "MOEAD_PBI"
//                "MOEACD-N",
//                "MOEACD-ND"


//                "MOEACD-N"
//                ,
                "MOEACD-N-SBX"
//                ,
//                "MOEACD-N-F"
//                ,
//                "MOEACD-ND",
//                "MOEADN_PBI",
//                "MOEADDN_PBI"
////                ,
//
//                "C-MOEACD-N",
//                "C-MOEACD-NA",
//                "C-MOEACD-ND",
//                "C-MOEACD-NAD",
//                "CMOEADN_PBI",
//                "CMOEADDN_PBI"
//                "C-MOEACD",
//                "CMOEAD_PBI"
        };

//        Point pointUCar = new ArrayPoint(new double[]{42.5170522401881 , -0.06696880223325141 , -0.07264283306703384 , -0.09801355894196942 , -0.010377742816105873 , -2.689108372855639 , 0.3509644836244803 , -0.6523454415340666 , 0.35714999996868});
//        Point pointCrash = new ArrayPoint(new double[]{1695.2002034931022 , 10.74539999986672 , 0.26399999998519663});
        Point pointUCar2 = new ArrayPoint(new double[]{42.5170522401881 , 1.0 , 1.0 , 1.0 , 1.0 , 1.0 , 0.3509644836244803 , - 0.6523454415340666 , 0.35714999996868});
        Point pointUCar3 = new ArrayPoint(new double[]{42.76801199531525 , 0.07172109999999998 , -0.03487982440567505 , -0.07556587876345999 , 0.16307069999999996 , 3.1914307031822275 , 0.4272499999999999 , 0.22561250000000044 , 0.35715000000000074 });


//        Point pointIdealUCar = new ArrayPoint(new double[]{26.473725439753455 , -0.6065090379980109 , -0.20575823598963872 , -0.21751834232301726 , -0.07499189548328733 , -10.84248000288643 , -0.41183427718929844 , -1.5930625000000003 , -2.7443827938042613});
//        Point pointIdealCrash = new ArrayPoint(new double[]{1661.707822502186 , 6.142800000734479 , 0.03940000003715144});

        Point pointIdealUCar2 = new ArrayPoint(new double[]{26.473725439753455 , 0 , 0 , 0 , 0 , 0 , -0.41183427718929844 , -1.5930625000000003 , -2.7443827938042613});
        Point pointIdealUCar3 = new ArrayPoint(new double[]{15.576004000000003 , -0.6066317000000001 , -0.20678893999999998 , -0.21821934999999998 , -0.07499216197804007 , -10.902487 , -0.4147500000000002 , -1.5930625000000003 , -2.7856500000000004});

        Point pointUCar = new ArrayPoint(new double[]{ 44.127612395693475 , 0.1056387399997471 , -0.024999195143077163 , -0.0674647127157054 , 0.174973843105539 , 3.8488146046989367 , 0.46934999999567373 , 0.3165462500000005 , 0.5142900000000008 });
        Point pointCrash = new ArrayPoint(new double[]{ 1696.874822542648 , 10.975529999823332 , 0.2752299999825989 });
        Point pointIdealUCar = new ArrayPoint(new double[]{14.216403600205076 , -0.6405493399946892 , -0.2154455944808858 , -0.22539814222077087 , -0.0868953052163203 , -11.604929931813963 , -0.4568499999091499 , -1.6839962500000003 , -2.9427900000000005});
        Point pointIdealCrash = new ArrayPoint(new double[]{ 1660.0332034526402 , 5.912670000777867 , 0.028170000039749177  });

//        Point pointCar = new ArrayPoint(new double[]{42.768011990946945 , 4.0 , 12.521158393308081});
//        Point pointMachining = new ArrayPoint(new double[]{ 3.571283006634979 , -3.7299999999999986 , -3.399999999999996 , -0.30063044650522697});
//        Point pointWater = new ArrayPoint(new double[]{77282.0969033014 , 1350.0 , 2853468.96494178 , 7885544.778071534 , 24999.924323437346});
//
//        Point pointIdealCar = new ArrayPoint(new double[]{23.58565808439098 , 3.58525 , 10.61064375});
//        Point pointIdealMachining = new ArrayPoint(new double[]{2.4167618558793054 , -3.9788538455765083 , -3.9852369337798583 , -1.579511419888553});
//        Point pointIdealWater = new ArrayPoint(new double[]{63840.2774 , 44.80295038873002 , 285346.896494178 , 183749.96706092838 , 7.22222222222193 });

        Point pointCar = new ArrayPoint(new double[]{43.72712968627474 , 4.0207375 , 12.616684125473485 });
        Point pointMachining = new ArrayPoint(new double[]{ 3.430753084909151 , -3.717557307721173 , -3.370738153311003 , -0.23668639783606066 });
        Point pointWater = new ArrayPoint(new double[]{ 76391.36637750018 , 1415.2598524805635 , 2981875.06836416 , 7567977.289295986 , 26249.5594284981 });

        Point pointIdealCar = new ArrayPoint(new double[]{  22.626540389063184 , 3.5645124999999998 , 10.515118017834595 });
        Point pointIdealMachining = new ArrayPoint(new double[]{  2.3684765592588364 , -3.991296537855334 , -4.014498780468852 , -1.6434554685577192 });
        Point pointIdealWater = new ArrayPoint(new double[]{  63242.60649630951 , -20.456902091833477 , 156940.79307179787 , -167879.90542645534 , -1242.4128828385344 });

//               Point pointCarCab = new ArrayPoint(new double[]{-1345.9479185614514 , -95.3332544375 , -1022.8869498418009 , 178278.53421301444 , -29.357032822925728 , 1.8652415443765662 , -74.91081243128417 , 72.85544051050297 , -100.98664514564578});
//        Point pointIdealCarCab = new ArrayPoint(new double[]{-1410.1161712998005 , -111.4188044375 , -1070.6743197281676 , 145568.28981921045 , -31.489331330594005 , 1.5888276779842 , -76.33659133779895 , 70.82361198785604 , -151.80265736337833});
        Point pointCarCab = new ArrayPoint(new double[]{ -1342.739505924534 , -94.5289769375 , -1020.4975813474825 , 179914.04643270464 , -29.250417897542313 , 1.8790622376961845 , -74.83952348595844 , 72.95703193663532 , -98.44584453475915 });
        Point pointIdealCarCab = new ArrayPoint(new double[]{ -1413.324583936718 , -112.2230819375 , -1073.063688222486 , 143932.77759952025 , -31.59594625597742 , 1.5750069846645818 , -76.40788028312468 , 70.7220205617237 , -154.34345797426496 });

        Point[] nadarPointList = {
                pointCrash,
                pointUCar
                ,
                pointCarCab
////                pointCar,

//                pointUCar2
//                pointUCar3
//                pointCar,
//                pointMachining,
//                pointWater

        };

        Point[] idealPointList = {
                pointIdealCrash,
                pointIdealUCar
                ,
                pointIdealCarCab
////                pointIdealCar
////                ,
//                pointIdealUCar2
//                pointIdealUCar3
//                pointIdealCar,
//                pointIdealMachining,
//                pointIdealWater

        };


        MyExperimentAnalysis tool = new MyExperimentAnalysis("","");
        try {
            for (int i = 0; i < dirs.length; i++) {
                for (int j = 0; j < instances.length;j++) {
                    for (int r = 0; r < run; r++) {
                        String solutionPF = dirs[i] + "POF\\" + algorithmNames[i] + "_" + instances[j] + "R" + r + ".pof";
                        WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                        Vector<Point> newFront = new Vector<>(solutionFront.getNumberOfPoints());
                        for (int p = 0; p < solutionFront.getNumberOfPoints(); p++) {
                            Point point = new ArrayPoint(solutionFront.getPointDimensions());
                            for (int k = 0; k < solutionFront.getPointDimensions(); k++) {
                                point.setDimensionValue(k,(solutionFront.getPoint(p).getDimensionValue(k) - idealPointList[j].getDimensionValue(k))/(nadarPointList[j].getDimensionValue(k) - idealPointList[j].getDimensionValue(k)));
                            }
                            newFront.add(point);
                        }
                        String newPFName = dirs[i] + "POF\\" + algorithmNames[i] + "_N_"+instances[j]+"R" + r + ".pof";
                        BufferedWriter writer = new DefaultFileOutputContext(newPFName).getFileWriter();
                        tool.saveFront(newFront, writer);
                        writer.close();
                    }
                }
            }
        }catch (IOException e){}
    }


    boolean dominatesValue(double v1,double v2){
        boolean maximizing = false;
        if (maximizing){
            return v1 > v2;
        }
        else
            return v2 > v1;
    }

    int dominates(Point p, Point q) {
        // returns -1 if p dominates q, 1 if q dominates p, 2 if p == q, 0 otherwise
        // ASSUMING MINIMIZATION

        // domination could be checked in either order

        for (int i = p.getNumberOfDimensions()-1 ; i >= 0; i--) {
            if (dominatesValue(p.getDimensionValue(i),q.getDimensionValue(i))) {
                for (int j = i - 1; j >= 0; j--) {
                    if (dominatesValue(q.getDimensionValue(j) , p.getDimensionValue(j))) {
                        return 0;
                    }
                }
                return -1;
            } else if (dominatesValue(q.getDimensionValue(i) , p.getDimensionValue(i))) {
                for (int j = i - 1; j >= 0; j--) {
                    if (dominatesValue(p.getDimensionValue(j) , q.getDimensionValue(j))) {
                        return 0;
                    }
                }
                return 1;
            }
        }
        return 2;
    }

    public void MergeFront(){
        String instance = "CarCabDesign(9)_43758_3000R0";
        String[] dirs = {
                "E://ResultsEngineerUnConstraintsPoint/",
                "E://ResultsEngineerUnConstraintsPoint/"
        };
        String[] algorithmNames = {
                "MOEACD-N",
                "MOEADN_PBI"
        };
        MyExperimentAnalysis tool = new MyExperimentAnalysis("","");
        try {
            Vector<Point> newFront = new Vector<>();
            for (int i = 0; i < dirs.length; i++) {
                String solutionPF = dirs[i] + "POF\\" + algorithmNames[i] + "_"+instance + ".pof";
                WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                for (int j = 0; j < solutionFront.getNumberOfPoints(); j++) {
                    newFront.add(solutionFront.getPoint(j));
                }
            }

            int[] dominationCount = new int[newFront.size()];
            for (int i=0;i<newFront.size();i++){
                dominationCount[i] = 0;
            }

            for (int i=0;i<newFront.size();i++){
                for(int j=i+1;j<newFront.size();j++) {
                    int domination = dominates(newFront.get(i),newFront.get(j));
                    if(domination == -1 || domination == 2){
                        dominationCount[j]++;
                    }else if(domination == 1){
                        dominationCount[i]++;
                    }
                }
            }

            for (int i=newFront.size()-1;i>=0;i--){
                if(dominationCount[i]>0){
                    newFront.remove(i);
                }
            }

            String newPFName = dirs[0] + "POF\\" + algorithmNames[0] + "_M_"+instance+".pof";
            BufferedWriter writer = new DefaultFileOutputContext(newPFName).getFileWriter();
            tool.saveFront(newFront, writer);
            writer.close();

        }catch (IOException e){}
    }


    public void normalizeScalePF(){
        int run = 20;
        String[] instances = {
                "SDTLZ1(3)_91_400","SDTLZ1(5)_210_600","SDTLZ1(8)_156_750","SDTLZ1(10)_275_1000","SDTLZ1(15)_135_1500",
                "SDTLZ2(3)_91_250","SDTLZ2(5)_210_350","SDTLZ2(8)_156_500","SDTLZ2(10)_275_750","SDTLZ2(15)_135_1000"
                ,
                "SDTLZ3(3)_91_1000","SDTLZ3(5)_210_1000","SDTLZ3(8)_156_1000","SDTLZ3(10)_275_1500","SDTLZ3(15)_135_2000",
                "SDTLZ4(3)_91_600","SDTLZ4(5)_210_1000","SDTLZ4(8)_156_1250","SDTLZ4(10)_275_2000","SDTLZ4(15)_135_3000",
                "Convex_SDTLZ2(3)_91_250","Convex_SDTLZ2(5)_210_750","Convex_SDTLZ2(8)_156_2000","Convex_SDTLZ2(10)_275_4000","Convex_SDTLZ2(15)_135_4500"
        };

        String[] dirs = {
                "D:\\Experiments\\ExperimentDataThesis\\ScaledDTLZ\\compare\\",
                "D:\\Experiments\\ExperimentDataThesis\\ScaledDTLZ\\compare\\",
                "D:\\Experiments\\ExperimentDataThesis\\ScaledDTLZ\\compare\\",
                "D:\\Experiments\\ExperimentDataThesis\\ScaledDTLZ\\MOEACD\\",
                "D:\\Experiments\\ExperimentDataThesis\\ScaledDTLZ\\MOEACD\\",
                "D:\\Experiments\\ExperimentDataThesis\\ScaledDTLZ\\MOEACD\\",
                "D:\\Experiments\\ExperimentDataThesis\\ScaledDTLZ\\MOEACD\\"
        };
        String[] algorithmNames = {
                "NSGAIII",
                "MOEADN_PBI",
                "MOEADDN_PBI",
                "MOEACD",
                "MOEACD-N"
                ,"MOEACD-N-SBX"
                ,"MOEACD-N-F"
        };


        Point IdealPoint3D = new ArrayPoint(new double[]{0.0,0.0,0.0});
        Point IdealPoint5D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint8D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint10D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint15D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});

        Point NadirPoint3DMin = new ArrayPoint(new double[]{0.5,0.5,0.5});
        Point NadirPoint5DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5});
        Point NadirPoint8DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5});
        Point NadirPoint10DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5});
        Point NadirPoint15DMin = new ArrayPoint(new double[]{0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5});

        Point NadirPoint3D = new ArrayPoint(new double[]{1.0,1.0,1.0});
        Point NadirPoint5D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0});
        Point NadirPoint8D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0});
        Point NadirPoint10D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0});
        Point NadirPoint15D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0});

        for(int i=0;i<3;i++){
            NadirPoint3DMin.setDimensionValue(i,NadirPoint3DMin.getDimensionValue(i)*Math.pow(10.0,i));
            NadirPoint3D.setDimensionValue(i,NadirPoint3D.getDimensionValue(i)*Math.pow(10.0,i));
        }
        for(int i=0;i<5;i++){
            NadirPoint5DMin.setDimensionValue(i,NadirPoint5DMin.getDimensionValue(i)*Math.pow(10.0,i));
            NadirPoint5D.setDimensionValue(i,NadirPoint5D.getDimensionValue(i)*Math.pow(10.0,i));
        }
        for(int i=0;i<8;i++){
            NadirPoint8DMin.setDimensionValue(i,NadirPoint8DMin.getDimensionValue(i)*Math.pow(3.0,i));
            NadirPoint8D.setDimensionValue(i,NadirPoint8D.getDimensionValue(i)*Math.pow(3.0,i));
        }
        for(int i=0;i<10;i++){
//            NadirPoint10DMin.setDimensionValue(i,NadirPoint10DMin.getDimensionValue(i)*Math.pow(3.0,i));
            NadirPoint10DMin.setDimensionValue(i,NadirPoint10DMin.getDimensionValue(i)*Math.pow(2.0,i));
            NadirPoint10D.setDimensionValue(i,NadirPoint10D.getDimensionValue(i)*Math.pow(3.0,i));
        }
        for(int i=0;i<15;i++){
//            NadirPoint15DMin.setDimensionValue(i,NadirPoint15DMin.getDimensionValue(i)*Math.pow(2.0,i));
            NadirPoint15DMin.setDimensionValue(i,NadirPoint15DMin.getDimensionValue(i)*Math.pow(1.2,i));
            NadirPoint15D.setDimensionValue(i,NadirPoint15D.getDimensionValue(i)*Math.pow(2.0,i));
        }

        Point[] nadarPointList = {
//                NadirPoint3DMin,NadirPoint5DMin,NadirPoint8DMin,
                NadirPoint10DMin,NadirPoint15DMin
//                ,
//                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D
//                ,
//                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D,
//                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D,
//                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D
        };

        Point[] idealPointList = {
//                IdealPoint3D,IdealPoint5D,IdealPoint8D,
                IdealPoint10D,IdealPoint15D
//                ,
//                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D
//                ,
//                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D,
//                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D,
//                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D
        };


        MyExperimentAnalysis tool = new MyExperimentAnalysis("","");
        try {
            for (int i = 0; i < dirs.length; i++) {
                for (int j = 0; j < instances.length;j++) {
                    for (int r = 0; r < run; r++) {
                        String solutionPF = dirs[i] + "POF\\" + algorithmNames[i] + "_" + instances[j] + "R" + r + ".pof";
                        WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                        Vector<Point> newFront = new Vector<>(solutionFront.getNumberOfPoints());
                        for (int p = 0; p < solutionFront.getNumberOfPoints(); p++) {
                            Point point = new ArrayPoint(solutionFront.getPointDimensions());
                            for (int k = 0; k < solutionFront.getPointDimensions(); k++) {
                                point.setDimensionValue(k,(solutionFront.getPoint(p).getDimensionValue(k) - idealPointList[j].getDimensionValue(k))/(nadarPointList[j].getDimensionValue(k) - idealPointList[j].getDimensionValue(k)));
                            }
                            newFront.add(point);
                        }
                        String newPFName = dirs[i] + "POF\\" + algorithmNames[i] + "_N_"+instances[j]+"R" + r + ".pof";
                        BufferedWriter writer = new DefaultFileOutputContext(newPFName).getFileWriter();
                        tool.saveFront(newFront, writer);
                        writer.close();
                    }
                }
            }
        }catch (IOException e){}
    }



    public void normalizeIrregularPF(){
        int run = 20;
        String[] instances = {
                "DTLZ5(3)_91_500","DTLZ5(5)_210_750","DTLZ5(8)_156_1000","DTLZ5(10)_275_1500","DTLZ5(15)_135_2000",
                "DTLZ6(3)_91_500","DTLZ6(5)_210_750","DTLZ6(8)_156_1000","DTLZ6(10)_275_1500","DTLZ6(15)_135_2000",
                "DTLZ7(3)_91_500","DTLZ7(5)_210_750","DTLZ7(8)_156_1000","DTLZ7(10)_275_1500","DTLZ7(15)_135_2000",
                "InvertedDTLZ1(3)_91_500","InvertedDTLZ1(5)_210_750","InvertedDTLZ1(8)_156_1000", "InvertedDTLZ1(10)_275_1500","InvertedDTLZ1(15)_135_2000"
        };



        String[] dirs = {
                "D:\\Experiments\\ExperimentDataThesis\\Irregular\\compare\\",
                "D:\\Experiments\\ExperimentDataThesis\\Irregular\\compare\\",
                "D:\\Experiments\\ExperimentDataThesis\\Irregular\\MOEACD\\",
                "D:\\Experiments\\ExperimentDataThesis\\Irregular\\MOEACD\\"
        };
        String[] algorithmNames = {
                "MOEAD_PBI",
                "MOEADD_PBI",
                "MOEACD",
                "MOEACD-D"
        };

        Point point3Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5});
        Point point3D = new ArrayPoint(new double[]{1.0, 1.0, 1.0});
        Point point5Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5, 0.5, 0.5});
        Point point5D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0});
        Point point8Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5});
        Point point8D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0});
        Point point10Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5});
        Point point10D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0});
        Point point15Dmin = new ArrayPoint(new double[]{0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5,0.5, 0.5, 0.5});
        Point point15D = new ArrayPoint(new double[]{1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0,1.0, 1.0, 1.0});
        Point p5_3D = new ArrayPoint(new double[]{1.0,1.0, 1.0 });
        Point p5_5D = new ArrayPoint(new double[]{1.0, 1.0,4.0,4.0,1.0});
        Point p5_8D = new ArrayPoint(new double[]{ 2.0,2.0,3.0 ,4.0 ,4.0,4.0,4.0 ,1.0});
        Point p5_10D = new ArrayPoint(new double[]{ 1.0 ,1.0 ,3.0 ,3.0,3.0,4.0,4.0 ,4.0,4.0 ,1.0 });
        Point p5_15D = new ArrayPoint(new double[]{ 1.0,1.0,1.0,2.0,1.0 ,2.0,2.0,3.0,2.0,3.0, 3.0,4.0,4.0,4.0,4.0});

        Point p6_3D = new ArrayPoint(new double[]{ 1.0, 1.0 ,  1.0});
        Point p6_5D = new ArrayPoint(new double[]{3.0, 2.0 , 11.0 , 11.0 ,2.0 });
        Point p6_8D = new ArrayPoint(new double[]{11.0 , 11.0 ,11.0 , 11.0 , 11.0 , 11.0 , 11.0 , 11.0  });
        Point p6_10D = new ArrayPoint(new double[]{11.0 , 11.0 ,11.0 , 11.0 , 11.0 , 11.0 , 11.0 , 11.0 ,11.0 , 11.0 });
        Point p6_15D = new ArrayPoint(new double[]{11.0 , 11.0 ,11.0 , 11.0 , 11.0 , 11.0 , 11.0 , 11.0 ,11.0 , 11.0 , 11.0 ,11.0 , 11.0 , 11.0 , 11.0});

        Point p7_3D = new ArrayPoint(new double[]{1.0,1.0,6.0});
        Point p7_5D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,10.0});
        Point p7_8D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,16.0});
        Point p7_10D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,76.0});
        Point p7_15D = new ArrayPoint(new double[]{1.0,1.0,1.0,1.0,1.0, 1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,120.0});


        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
//        Point pZ7_3D = new ArrayPoint(new double[]{5.74511715562493E-16,0,2.6140199319402986});
//        Point pZ7_5D = new ArrayPoint(new double[]{0,4.628333199129033E-16,2.1892565388802256E-15, 2.5459234210915353E-15,3.228115949685426});
//        Point pZ7_8D = new ArrayPoint(new double[]{1.4755074371584203E-15,3.098117664899809E-14,2.0894127030611863E-17,1.4029667232567789E-14,1.3302862326140923E-13,7.613488951330515E-16,1.792710717343876E-15,4.149035905977044});
//        Point pZ7_10D = new ArrayPoint(new double[]{1.0177030687826027E-5,1.7893882634544855E-6,4.358643858693892E-5,3.98877388474439E-6,1.7187020679008246E-5,1.0199503718520686E-6, 2.112695790525941E-6,2.4836833610191825E-6,3.0731067861666794E-5,5.169153687267617});
//        Point pZ7_15D = new ArrayPoint(new double[]{1.0657181079731792E-5,8.169466643394155E-6,1.7393477121596668E-4,2.3335723577349004E-5,2.390638006316559E-5, 5.555893455293226E-6,6.181711572961213E-6,4.192951552850488E-6,8.803048941357196E-6,2.8530937141169055E-5,1.6642845916792215E-4,5.032096329471436E-5,2.1037111000044018E-4,3.646154929919099E-5, 6.907673733019576});
        Point pZ7_3D = new ArrayPoint(new double[]{0,0,2.0});
        Point pZ7_5D = new ArrayPoint(new double[]{0,0,0,0,3.0});
        Point pZ7_8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,4.0});
        Point pZ7_10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,5.0});
        Point pZ7_15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,6.0});


        Point[] nadarPointList = {
                p5_3D,p5_5D,p5_8D,p5_10D,p5_15D,
                p6_3D,p6_5D,p6_8D,p6_10D,p6_15D,
                p7_3D,p7_5D,p7_8D,p7_10D,p7_15D,
                point3D,point5D,point8D,point10D,point15D
        };

        Point[] idealPointList = {
                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D,
                pZ7_3D,pZ7_5D,pZ7_8D,pZ7_10D,pZ7_15D,
                pointZero3D,pointZero5D,pointZero8D,pointZero10D,pointZero15D
        };


        MyExperimentAnalysis tool = new MyExperimentAnalysis("","");
        try {
            for (int i = 0; i < dirs.length; i++) {
                for (int j = 0; j < instances.length;j++) {
                    for (int r = 0; r < run; r++) {
                        String solutionPF = dirs[i] + "POF\\" + algorithmNames[i] + "_" + instances[j] + "R" + r + ".pof";
                        WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);
                        Vector<Point> newFront = new Vector<>(solutionFront.getNumberOfPoints());
                        for (int p = 0; p < solutionFront.getNumberOfPoints(); p++) {
                            Point point = new ArrayPoint(solutionFront.getPointDimensions());
                            for (int k = 0; k < solutionFront.getPointDimensions(); k++) {
                                point.setDimensionValue(k,(solutionFront.getPoint(p).getDimensionValue(k) - idealPointList[j].getDimensionValue(k))/(nadarPointList[j].getDimensionValue(k) - idealPointList[j].getDimensionValue(k)));
                            }
                            newFront.add(point);
                        }
                        String newPFName = dirs[i] + "POF\\" + algorithmNames[i] + "_N_"+instances[j]+"R" + r + ".pof";
                        BufferedWriter writer = new DefaultFileOutputContext(newPFName).getFileWriter();
                        tool.saveFront(newFront, writer);
                        writer.close();
                    }
                }
            }
        }catch (IOException e){}
    }

    public void testRandomDirections() {
        JMetalRandom randomGenerator = JMetalRandom.getInstance();
        int[] nObj = new int[]{1, 2, 3, 5, 8, 10, 15};
        int[] popSize = new int[]{30, 40, 50, 60, 70, 80, 90};
        int e = 5;
        String dir = "D://";
        try {
            for (int k = 0; k < nObj.length; k++) {
                int numberOfDirectionsForACV = e * popSize[k] * nObj[k];

                List<Integer> idx = new ArrayList<>();
                for(int p = 0;p<nObj[k];p++){
                    idx.add(p);
                }
                String file = dir + "w[" + nObj[k] + "].txt";
                BufferedWriter writer = new DefaultFileOutputContext(file).getFileWriter();
                String outStr = "";
                for (int i = 0; i < numberOfDirectionsForACV; i++) {
                    double[] direction = new double[nObj[k]];
                    Collections.shuffle(idx);
                    double left = 1.0;
                    for (int j = 0; j < nObj[k] - 1; j++) {
                        direction[idx.get(j)] = randomGenerator.nextDouble(0.0, left);
                        left -= direction[idx.get(j)];
                    }
                    if (nObj[k] == 1)
                        direction[idx.get(0)] = randomGenerator.nextDouble(0.0, left);
                    else
                        direction[idx.get(nObj[k] - 1)] = left;

                    for (int p = 0; p < nObj[k]; p++) {
                        outStr += (direction[p] + " ");
                    }
                    outStr += "\n";
                }
                writer.write(outStr);
                writer.close();
            }
        }catch (IOException da){

        }
    }
    public static void main(String []argc) {
        analysisQuality computor = new analysisQuality();

//        String[] algorithmNameList = {
////                "NSGAIII",
////                "MOEAD_PBI",
////                "MOEADDE_PBI",
//////                ,"MOEADACD_PBI",
////                "MOEADD_PBI",
////                ,"MOEADGR_TCH"
////                ,"MOEADGR_PBI"
////                "MOEADAGR_TCH",
////                "MOEADAGR_PBI"
////                ,
//                "MOEACD"
////                "MOEADACDSBX_PBI",
////                "MOEADAGRSBX_PBI"
//        };
//        int approximateDim = 10;
//        int numOfSample = 1000000;

//        int maxRunMOP = 20;
//        String baseDirMOP = "D:\\Experiments\\ExperimentData\\MOP\\compare\\";
//        String statDirMOP = baseDirMOP + "stat2\\";
//        computor.executeMOPFinal(baseDirMOP,baseDirMOP,maxRunMOP,algorithmNameList,approximateDim,numOfSample);

//        int maxRunMaOP = 20;
//
////      String baseDirMaOP = "D:\\Experiments\\ExperimentDataFinal\\DTLZ\\MOEACD\\";
//        String baseDirMaOP = "D:\\Experiments\\ResultsMaOPMeasure0000040\\";

//        String baseDirMaOP = "E:\\ResultsMaOPMeasure0000026\\";
//        String statDirMaOP = baseDirMaOP + "stat2\\";
//        computor.executeMaOPFinal(baseDirMaOP,baseDirMaOP,maxRunMaOP,algorithmNameList,approximateDim,numOfSample);

//        computor.executeMaOPLowFinal(baseDirMaOP,statDirMaOP,maxRunMaOP,algorithmNameList,approximateDim,numOfSample);
//        computor.executeMaOPHighFinal(baseDirMaOP,statDirMaOP,maxRunMaOP,algorithmNameList,approximateDim,numOfSample);
//
//        computor.executeSpreadTestFinal(baseDirMaOP,baseDirMaOP,maxRunMaOP,algorithmNameList,approximateDim,numOfSample);
////
//        String[] algorithmNameList1 = {
//                "NSGAIII",
//                "MOEAD_PBI",
//////                "MOEADDE_PBI","MOEADACD_PBI",
//                "MOEADD_PBI"
////                ,"MOEADGR_TCH"
////                ,
////                "MOEADAGR_TCH",
////                "MOEADAGR_PBI"
////                ,
////                "MOEACDN"
//        };
////
//        int maxRunMaOPScale = 20;
//        String baseDirMaOPScale = "D:\\Experiments\\ExperimentDataFinal\\ScaleDTLZ\\compare\\";

//        String statDirMaOPScale = baseDirMaOPScale+"stat2\\";
//        computor.executeMaOPScale(baseDirMaOPScale,baseDirMaOPScale,maxRunMaOPScale,algorithmNameList1,approximateDim,numOfSample);
////
//        computor.executeMaOPScaleHVFinal(baseDirMaOPScale,baseDirMaOPScale,maxRunMaOPScale,algorithmNameList1,approximateDim,numOfSample);
//        String[] algorithmNameList2 = {
////                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI"
////                ,
////                "MOEADGR_TCH"
//                "MOEADAGR_TCH",
//                "MOEADAGR_PBI"
////                "MOEACD"
//        };
//
//        int maxRunMaOPSmall =20;
//        String baseDirMaOPSmall = "D:\\Experiments\\ExperimentData\\SmallDTLZ\\compare\\";
//
//        String statDirMaOPSmall = baseDirMaOPSmall+"stat2\\";
//        computor.executeMaOPSmallFinal(baseDirMaOPSmall,baseDirMaOPSmall,maxRunMaOPSmall,algorithmNameList2,approximateDim,numOfSample);

//        computor.findExtremePoints();
//        computor.filterUCarSideImpact();
//        computor.normalizeEngineerPF();
//        computor.normalizeScalePF();
//        computor.normalizeIrregularPF();
//        computor.MergeFront();
//        computor.testRandomDirections();
//
        int num = 20;
        double c_uneven  =1.5;
        String file = "D://tt.txt";
//        ArrayList<double[]> uniformWeights = new ArrayList<>(num);
        try {
            BufferedWriter writer = new DefaultFileOutputContext(file).getFileWriter();
            String outStr = "";
//            c_uneven = Math.pow(2.0,1.0/(num -1));
//            JMetalLogger.logger.info("\n"+c_uneven+"\n");
            for (int i = 1; i <= num; i++) {
                double l = 0.25*(i-1)/(1.0*num - 0.75*i - 0.25);
                outStr += ("0 "+l+'\n');
//                uniformWeights.add(new double[]{l});
             }

//            double k = 1.0 / (Math.pow(c_uneven, num - 1) - 1.0);
//            for (int i = 1; i <= num; i++) {
//                double l = k * (Math.pow(c_uneven, i - 1) - 1.0);
////                outStr += (""+i+" "+l+'\n');
//                uniformWeights.add(new double[]{l});
//            }
//            double k = 2.0/(num*(num - 1));
//            for (int i=1;i<=num;i++){
//                double l = k * i * (i-1)/2.0;
//                outStr += (""+i+" "+l+'\n');
//            }
            writer.write(outStr);
            writer.close();
        }catch(IOException e){}

//        KDTree kdTree =  KDTree.build(uniformWeights);
//
//        double[] test = new double[]{0.001,0.01,0.15,0.2,0.2444,0.45,0.6,0.8,1.0};
//        for (int i=0;i<test.length;i++) {
//            double tmp = (2.0 * Math.pow(c_uneven, num - 1) * test[i] + 2) / (1.0 + c_uneven);
//            int index1 = (int) Math.floor(Math.log(tmp) / Math.log(c_uneven)) + 1;
//
//            int index2 = kdTree.queryIndex(new double[]{test[i]});
//            JMetalLogger.logger.info("\n["+i+"]<"+test[i]+">\t"+index1+"\t"+index2+"\n");
//        }
    }
}
