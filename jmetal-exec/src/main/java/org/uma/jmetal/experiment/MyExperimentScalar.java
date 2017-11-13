package org.uma.jmetal.experiment;

import org.uma.jmetal.experiment.MOEACD.*;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.hypervolume.ApproximateHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;
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
 * Created by X250 on 2016/4/27.
 */
public class MyExperimentScalar {

    public void execute() {
        String baseDir = "E://Results-Scalar000502/";
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes = 20;
        int neighborhoodSize = 20;
        double extremeRegionEnhancingSelectionProbability = 0.1;
        double neighborhoodSelectionProbability = 0.9;
        double pbi_theta = 5.0;
        int maxRun = 5;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new SDTLZ1(7, 3,10.0), new SDTLZ1(9, 5,10.0), new SDTLZ1(12, 8,3.0),
                new SDTLZ2(12, 3,10.0), new SDTLZ2(14, 5,10.0), new SDTLZ2(17, 8,3.0)
               );
        int[] popsList = {
                91, 210, 156,
                91, 210, 156
        };
        int[] maxIterationsList = {
                400, 600, 750,
                250, 350, 500
        };
        int[][] divisionConfigList = {
                {12}, {6}, {2, 3},
                {12}, {6}, {2, 3}
        };
        double[][] tauConfigList = {
                {1.0}, {1.0}, {0.5, 1.0},
                {1.0}, {1.0}, {0.5, 1.0}
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
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
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[4560]",
                frontDir + "DTLZ1.5D.pf[10626]",
                frontDir + "DTLZ1.8D.pf[11440]",
                frontDir + "DTLZ2.3D.pf[4560]",
                frontDir + "DTLZ2.5D.pf[10626]",
                frontDir + "DTLZ2.8D.pf[11440]"
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
                point3Dmin, point5Dmin, point8Dmin,
                point3D, point5D, point8D
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);


        NSGAIIIStudy nsgaiiiExperiment = new NSGAIIIStudy();
        try {
            nsgaiiiExperiment.execute(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndexNSGAIII,
                    mutationDistributionIndex,
                    indicatorEvaluatingTimes,
                    maxRun,
                    problemList,
                    popsList,
                    maxIterationsList,
                    divisionConfigList,
                    tauConfigList,
                    frontFileList,
                    hvRefPointList,
                    indicatorConfig);
        } catch (FileNotFoundException e) {
        }

//        MOEADStudy moeadExperiment = new MOEADStudy();
//        try {
//            moeadExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborSize,
//                    neighborhoodSelectionProbability,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }

        MOEADDStudy moeaddExperiment = new MOEADDStudy();
        try {
            moeaddExperiment.execute(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndex,
                    mutationDistributionIndex,
                    neighborSize,
                    neighborhoodSelectionProbability,
                    indicatorEvaluatingTimes,
                    maxRun,
                    problemList,
                    popsList,
                    maxIterationsList,
                    divisionConfigList,
                    tauConfigList,
                    frontFileList,
                    hvRefPointList,
                    indicatorConfig);
        } catch (FileNotFoundException e) {
        }

//
//        MOEACDPBIStudy moeacdpbiExperiment = new MOEACDPBIStudy();
//        try {
//            moeacdpbiExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    extremeRegionEnhancingSelectionProbability,
//                    neighborhoodSelectionProbability,
//                    pbi_theta,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }

//
//
//        MOEACDStudy moeacdExperiment = new MOEACDStudy();
//        try {
//            moeacdExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    extremeRegionEnhancingSelectionProbability,
//                    neighborhoodSelectionProbability,
//                    pbi_theta,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }


//        MOEACDNPBIStudy moeacdnnExperiment = new MOEACDNPBIStudy();
//        try {
//            moeacdnnExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    extremeRegionEnhancingSelectionProbability,
//                    neighborhoodSelectionProbability,
//                    pbi_theta,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }



//        MOEACDNStudy moeacdnExperiment = new MOEACDNStudy();
//        try {
//            moeacdnExperiment.execute(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    extremeRegionEnhancingSelectionProbability,
//                    neighborhoodSelectionProbability,
//                    pbi_theta,
//                    indicatorEvaluatingTimes,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    divisionConfigList,
//                    tauConfigList,
//                    frontFileList,
//                    hvRefPointList,
//                    indicatorConfig);
//        } catch (FileNotFoundException e) {
//        }

    }

    void analysis(){
        String[] problemNameList = {
                "SDTLZ1(3)","SDTLZ1(5)","SDTLZ1(8)","SDTLZ1(10)","SDTLZ1(15)",
                "SDTLZ2(3)","SDTLZ2(5)","SDTLZ2(8)","SDTLZ2(10)","SDTLZ2(15)"
        };

        int[] popsList = {
                109, 251, 165,286,136,
                109, 251, 165,286,136
        };
        int[] maxIterationsList = {
                400, 600, 750,1000,1500,
                250, 350, 500,750,1000
        };

        String[] algorithmNameList = {
//                "NSGAIII","MOEAD_PBI","MOEADDE_PBI","MOEADACD_PBI","MOEADD_PBI",

        };

        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";

        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[5050]",
                frontDir + "DTLZ1.5D.pf[14950]",
                frontDir + "DTLZ1.8D.pf[31824]",
                frontDir + "DTLZ1.10D.pf[43758]",
                frontDir + "DTLZ1.15D.pf[54264]",
                frontDir + "DTLZ2.3D.pf[5050]",
                frontDir + "DTLZ2.5D.pf[14950]",
                frontDir + "DTLZ2.8D.pf[31824]",
                frontDir + "DTLZ2.10D.pf[43758]",
                frontDir + "DTLZ2.15D.pf[54264]"
        };

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
                point3Dmin,point5Dmin, point8Dmin,point10Dmin,point15Dmin,
                point3D,point5D, point8D,point10D,point15D
        };

        Point IdealPoint3D = new ArrayPoint(new double[]{0.0,0.0,0.0});
        Point IdealPoint5D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint8D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint10D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});
        Point IdealPoint15D = new ArrayPoint(new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0});
        Point[] idealpointList = {
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
            NadirPoint10DMin.setDimensionValue(i,NadirPoint10DMin.getDimensionValue(i)*Math.pow(2.0,i));
            NadirPoint10D.setDimensionValue(i,NadirPoint10D.getDimensionValue(i)*Math.pow(3.0,i));
        }
        for(int i=0;i<15;i++){
            NadirPoint15DMin.setDimensionValue(i,NadirPoint15DMin.getDimensionValue(i)*Math.pow(1.2,i));
            NadirPoint15D.setDimensionValue(i,NadirPoint15D.getDimensionValue(i)*Math.pow(2.0,i));
        }

        Point[] NadirPointsList = {
            NadirPoint3DMin,NadirPoint5DMin,NadirPoint8DMin,NadirPoint10DMin,NadirPoint15DMin,
                NadirPoint3D,NadirPoint5D,NadirPoint8D,NadirPoint10D,NadirPoint15D
        };

        int[] objNumList = new int[]{
                3,5,8,10,15,
                3,5,8,10,15
        };

        Point[] boundPointList = {
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D,
                IdealPoint3D,IdealPoint5D,IdealPoint8D,IdealPoint10D,IdealPoint15D
        };
        double[] hvCubeList = {
                0.322166666666666601237523082091,
                0.167809583333333262045172773469,
                0.0576479131187995763596632059489,
                0.0282475246308855378052360407537,
                0.00474756150994297236589414268337,

                0.807401224401701478328163830156,
                1.44601659331517806705846851401,
                2.12773446575618585541178617859,
                2.59125206552980946739239698218,
                4.177236528690534374558916402748
        };
        int aDim  = 10;
        int numOfSample = 1000000;

        String baseDir = "E://ResultsMaOPScale000503/";
        String saveBaseDir = "E://ResultsMaOPScale000503/";
        int maxRun = 10;

        try {
            WFGHypervolume hvIndicator = new WFGHypervolume();
            ApproximateHypervolume ahvIndicator = new ApproximateHypervolume();
            ahvIndicator.setMiniming();
            MyExperimentAnalysis analysis = new MyExperimentAnalysis(baseDir,"");
            for (int iProblem = 0; iProblem < problemNameList.length; ++iProblem) {
                if(objNumList[iProblem] >= aDim){
                    ahvIndicator.setSamplePoints(numOfSample,hvRefPointList[iProblem],boundPointList[iProblem]);
                }
                Front referenceFront = new ArrayFront(frontFileList[iProblem]);
                for (int jAlg = 0; jAlg < algorithmNameList.length; ++jAlg) {
                    String instance = algorithmNameList[jAlg] + "_" + problemNameList[iProblem] + "_" + popsList[iProblem] + "_" + maxIterationsList[iProblem];
                    JMetalLogger.logger.info(instance);
                    Vector<Double> igd = new Vector<>(maxRun);
//                    Vector<Double> igdplus = new Vector<>(maxRun);
                    Vector<Double> hv = new Vector<>(maxRun);
                    for (int kRun = 0; kRun < maxRun; ++kRun) {
                        String solutionPF = baseDir+"/POF/" + instance + "R" + kRun + ".pof";
                        WfgHypervolumeFront solutionFront = new WfgHypervolumeFront(solutionPF);

                        for(int p=0;p< solutionFront.getNumberOfPoints();++p){
                            Point point = solutionFront.getPoint(p);
                            for(int q = 0;q<solutionFront.getPointDimensions();++q){
                                point.setDimensionValue(q,point.getDimensionValue(q)/NadirPointsList[iProblem].getDimensionValue(q));
                            }
                            solutionFront.setPoint(p,point);
                        }

                        InvertedGenerationalDistance igdIndicator = new InvertedGenerationalDistance();
                        double igdV = igdIndicator.invertedGenerationalDistance(solutionFront,referenceFront);
                        igd.add(igdV);

                        double hvV = 0.0;
                        if(objNumList[iProblem] < aDim) {
                            hvV = hvIndicator.evaluate(solutionFront, hvRefPointList[iProblem]);
                        }else{
//                                ahvIndicator = new ApproximateHypervolume();
//                                ahvIndicator.setSamplePoints(numOfSample,hvRefPointList[iProblem],boundPointList[iProblem]);
                            hvV = ahvIndicator.evaluate(solutionFront, hvRefPointList[iProblem]);
                        }
                        hv.add(hvV);
                        JMetalLogger.logger.info("["+kRun+"]  IGD : "+ igdV  + "   HV: "+hvV);
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
                    double median = ((Double)igd.get((igd.size()-1)/2)).doubleValue();
                    if(igd.size()%2 == 0) {
                        median += ((Double) igd.get(igd.size()/2));
                        median *= 0.5;
                    }
                    statIGD.add(median);


                    Vector statHV = new Vector();
                    minIdx = analysis.getMinRun(hv);
                    statHV.add(minIdx);
                    statHV.add(analysis.getMin(hv,minIdx));
                    maxIdx = analysis.getMaxRun(hv);
                    statHV.add(maxIdx);
                    statHV.add(analysis.getMax(hv,maxIdx));
                    statHV.add(analysis.getAverage(hv));
                    Collections.sort(hv);
                    median = ((Double)hv.get((hv.size()-1)/2)).doubleValue();
                    if(igd.size()%2 == 0) {
                        median += ((Double) hv.get(hv.size()/2));
                        median *= 0.5;
                    }
                    statHV.add(median);

                    Vector statHE= new Vector();
                    statHE.add(statHV.get(2));
                    statHE.add(hvCubeList[iProblem] - ((Double) statHV.get(3)).doubleValue());
                    statHE.add(statHE.get(0));
                    statHE.add(hvCubeList[iProblem] - ((Double) statHV.get(1)).doubleValue());
                    statHE.add(hvCubeList[iProblem] - ((Double)statHV.get(4)).doubleValue());
                    statHE.add(hvCubeList[iProblem] - ((Double) statHV.get(5)).doubleValue());

                    JMetalLogger.logger.info("IGD :\t[min  "+statIGD.get(0)+"]"+statIGD.get(1)+"\t[avg]"+statIGD.get(4)+"\t[median]"+statIGD.get(5)+"\t[max "+statIGD.get(2)+"]"+statIGD.get(3));
                    JMetalLogger.logger.info("HV :\t[min  "+statHV.get(0)+"]"+statHV.get(1)+"\t[avg]"+statHV.get(4)+"\t[median]"+statHV.get(5)+"\t[max "+statHV.get(2)+"]"+statHV.get(3));
                    JMetalLogger.logger.info("HE :\t[min  "+statHE.get(0)+"]"+statHE.get(1)+"\t[avg]"+statHE.get(4)+"\t[median]"+statHE.get(5)+"\t[max "+statHE.get(2)+"]"+statHE.get(3));

                    try {
                        Vector<Vector<Integer>> Generates = new Vector<Vector<Integer>>(maxRun);
                        Vector<Vector<Double>> totalIGD = new Vector<Vector<Double>>(maxRun);
                        Vector<Vector<Double>> totalHV = new Vector<>(maxRun);
                        Vector<Vector<Double>> totalHE = new Vector<>(maxRun);
                         for(int kRun = 0;kRun<maxRun;kRun++){
                            Vector<Integer> gen = new Vector();
                            gen.add(maxIterationsList[iProblem]);
                            Generates.add(gen);
                            Vector<Double> igdRun = new Vector();
                            igdRun.add(igd.get(kRun));
                            totalIGD.add(igdRun);
                             Vector<Double> hvRun = new Vector();
                             hvRun.add(hv.get(kRun));
                             totalHV.add(hvRun);
                             Vector<Double> heRun = new Vector();
                             heRun.add(hvCubeList[iProblem] - hv.get(kRun));
                             totalHE.add(heRun);
                        }
                        BufferedWriter writerIGD = new DefaultFileOutputContext(saveBaseDir+"/IGD/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,totalIGD,writerIGD);
                        writerIGD.close();
                        BufferedWriter writerStatIGD = new DefaultFileOutputContext(saveBaseDir+"/IGD/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statIGD, writerStatIGD);
                        writerStatIGD.close();
                        BufferedWriter writerHV = new DefaultFileOutputContext(saveBaseDir+"/HV/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,totalHV,writerHV);
                        writerHV.close();
                        BufferedWriter writerStatHV = new DefaultFileOutputContext(saveBaseDir+"/HV/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statHV, writerStatHV);
                        writerStatHV.close();
                        BufferedWriter writerHE = new DefaultFileOutputContext(saveBaseDir+"/HE/" + instance + ".csv").getFileWriter();
                        analysis.saveIndicators(Generates,totalHE,writerHE);
                        writerHE.close();
                        BufferedWriter writerStatHE = new DefaultFileOutputContext(saveBaseDir+"/HE/stat_" + instance + ".csv").getFileWriter();
                        analysis.saveIndicator(statHE, writerStatHE);
                        writerStatHE.close();
                    }catch (IOException e){}
                }
            }
        }catch(FileNotFoundException e){}

    }
    public static void main(String[] args) {
        MyExperimentScalar experiment = new MyExperimentScalar();
//        experiment.execute();
        experiment.analysis();
    }
}
