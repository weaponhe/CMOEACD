package org.uma.jmetal.experiment;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ3;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ4;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by X250 on 2016/3/24.
 */
public class MyExperiment {

    public static void main(String[] args) {

        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 20;
        int indicatorEvaluatingTimes = 20;
        int maxRun = 30;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(7, 3),
                new DTLZ1(7, 3), new DTLZ1(9, 5), new DTLZ1(12, 8), new DTLZ1(14, 10), new DTLZ1(19, 15),
                new DTLZ2(12, 3), new DTLZ2(14, 5), new DTLZ2(17, 8), new DTLZ2(19, 10), new DTLZ2(24, 15),
                new DTLZ3(12, 3), new DTLZ3(14, 5), new DTLZ3(17, 8), new DTLZ3(19, 10), new DTLZ3(24, 15),
                new DTLZ4(12, 3), new DTLZ4(14, 5), new DTLZ4(17, 8), new DTLZ4(19, 10), new DTLZ4(24, 15)
        );
        int[] popsList = {
                91,
                91, 210, 156, 275, 135,
                91, 210, 156, 275, 135,
                91, 210, 156, 275, 135,
                91, 210, 156, 275, 135,
        };
        int[] maxIterationsList = {
                400,
                400, 600, 750, 1000, 1500,
                250, 350, 500, 750, 1000,
                1000, 1000, 1000, 1000, 1000,
                600, 1000, 1250, 2000, 3000
        };
        int[][] divisionConfigList = {
                {12},
                {12}, {6}, {2, 3}, {2, 3},{1, 2},
                {12}, {6}, {2, 3}, {2, 3}, {1, 2},
                {12}, {6}, {2, 3}, {2, 3}, {1, 2},
                {12}, {6}, {2, 3}, {2, 3}, {1, 2}
        };
        double[][] tauConfigList = {
                {1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0}, {0.5, 1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0}, {0.5, 1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0}, {0.5, 1.0},
                {1.0}, {1.0}, {0.5, 1.0}, {0.5, 1.0}, {0.5, 1.0}
        };
        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "DTLZ1.3D.pf[91]",
                frontDir + "DTLZ1.3D.pf[91]",
                frontDir + "DTLZ1.5D.pf[210]",
                frontDir + "DTLZ1.8D.pf[156]",
                frontDir + "DTLZ1.10D.pf[275]",
                frontDir + "DTLZ1.15D.pf[135]",
                frontDir + "DTLZ2.3D.pf[91]",
                frontDir + "DTLZ2.5D.pf[210]",
                frontDir + "DTLZ2.8D.pf[156]",
                frontDir + "DTLZ2.10D.pf[275]",
                frontDir + "DTLZ2.15D.pf[135]",
                frontDir + "DTLZ3.3D.pf[91]",
                frontDir + "DTLZ3.5D.pf[210]",
                frontDir + "DTLZ3.8D.pf[156]",
                frontDir + "DTLZ3.10D.pf[275]",
                frontDir + "DTLZ3.15D.pf[135]",
                frontDir + "DTLZ4.3D.pf[91]",
                frontDir + "DTLZ4.5D.pf[210]",
                frontDir + "DTLZ4.8D.pf[156]",
                frontDir + "DTLZ4.10D.pf[275]",
                frontDir + "DTLZ4.15D.pf[135]"
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
                point3Dmin,
                point3Dmin, point5Dmin, point8Dmin, point10Dmin, point15Dmin,
                point3D, point5D, point8D, point10D, point15D,
                point3D, point5D, point8D, point10D, point15D,
                point3D, point5D, point8D, point10D, point15D,
        };

        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);

//        MOEADStudy moeadExperiment = new MOEADStudy();
//        try {
//            moeadExperiment.execute("E:/Results/",
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    mutationDistributionIndex,
//                    neighborSize,
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
//        MOEADDEStudy moeaddeExperiment = new MOEADDEStudy();
//        try {
//            moeaddeExperiment.execute("E:/Results/",
//                    crossoverProbability,
//                    f,
//                    mutationDistributionIndex,
//                    neighborSize,
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
//        NSGAIIIStudy nsgaiiiExperiment = new NSGAIIIStudy();
//        try {
//            nsgaiiiExperiment.execute("E:/Results/",
//                    crossoverProbability,
//                    crossoverDistributionIndexNSGAIII,
//                    mutationDistributionIndex,
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
}
