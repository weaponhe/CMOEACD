package org.uma.jmetal.experiment;

import org.uma.jmetal.experiment.MOEACD.MOEACDPStudy;
import org.uma.jmetal.experiment.MOEACD.MOEACDStudy;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.problem.multiobjective.mop.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.UniformWeightUtils;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by X250 on 2016/9/24.
 */
public class MyExperimentPreference {
    public void executePreference(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 5;
        int indicatorEvaluatingTimes = 20;
        int neighborhoodSize = 5;
        double neighborhoodSelectionProbability = 0.9;
        int extraExtremeSize = 2;
        double pbi_theta = 5.0;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new DTLZ1(7,3),new DTLZ1(7,3),
                new DTLZ2(12,3),new DTLZ2(12,3)
        );

        int[] popsList = {
                13,30,
                13,30
        };
        int[] maxIterationsList = {
                2000,1000,
                2000,1000
        };

        double tau = 0.2;
        int nObj = 3;
        int innerH = 3;

        List<double[]> directions1 = UniformWeightUtils.generateArrayList(new int[]{1,innerH},new double[]{1.0,tau},nObj);

        List<double[]> baseDirection = UniformWeightUtils.generateArrayList(new int[]{innerH},new double[]{tau},nObj);

        List<double[]> moveVectors = new ArrayList<>(nObj);
        for (int i=0;i<nObj;i++){
            double[]  w1 = new double[nObj];
            for (int j=0;j<nObj;j++){
                if(j == i)
                    w1[j] = 1.0;
                else
                    w1[j] = 0.0;
            }
            double[] w2 = new double[nObj];
            for (int j=0;j<nObj;j++)
                w2[j] = (1.0 - tau)/nObj + tau*w1[j];
            double[] mw = new double[nObj];
            for (int j=0;j<nObj;j++)
                mw[j] = w1[j] - w2[j];
            moveVectors.add(mw);
        }

        List<double[]> directions2 = new ArrayList<>();
        for (int i=0;i<nObj;i++){
            for (int j=0;j<baseDirection.size();j++){
                double[] w = new double[nObj];
                for (int k =0;k<nObj;k++){
                    w[k] = baseDirection.get(j)[k] + moveVectors.get(i)[k];
                }
                directions2.add(w);
            }
        }



        List<List<double[]>> predefineDirections = new ArrayList<>();
        predefineDirections.add(directions1);
        predefineDirections.add(directions2);
        predefineDirections.add(directions1);
        predefineDirections.add(directions2);


        String frontDir = "jmetal-problem/src/test/resources/pareto_fronts/";
        String[] frontFileList = {
                frontDir + "MOP1.2D.pf[2000]",
                frontDir + "MOP2.2D.pf[2000]",
                frontDir + "MOP3.2D.pf[2000]",
                frontDir + "MOP4.2D.pf[2000]",
                frontDir + "MOP5.2D.pf[2000]",
                frontDir + "MOP6.3D.pf[5050]",
                frontDir + "MOP7.3D.pf[5050]"
        };
        Point refP2D = new ArrayPoint(new double[]{11, 11 });
        Point refP3D = new ArrayPoint(new double[]{11, 11, 11});
        Point[] hvRefPointList = {
                refP2D, refP2D, refP2D, refP2D, refP2D,refP3D,refP3D
        };


        MyExperimentIndicatorConfig indicatorConfig = new MyExperimentIndicatorConfig();
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.HV);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGD);
//        indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.IGDPLUS);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.EP);
        //indicatorConfig.addIndicatorType(MyExperimentIndicatorConfig.INDICATORTYPE.SPREAD);



        MOEACDPStudy moeacdPExperiment = new MOEACDPStudy();
        try {
            moeacdPExperiment.execute(baseDir,
                    crossoverProbability,
                    crossoverDistributionIndex,
                    f,
                    mutationDistributionIndex,
                    neighborhoodSize,
                    neighborhoodSelectionProbability,
                    indicatorEvaluatingTimes,
                    maxRun,
                    problemList,
                    popsList,
                    maxIterationsList,
                    predefineDirections,
                    frontFileList,
                    hvRefPointList,
                    indicatorConfig);
        } catch (FileNotFoundException e) {
        }

    }

    public static void main(String[] argv){
        int maxRunPreference = 1;

        MyExperimentPreference experimentPreference = new MyExperimentPreference();
        String baseDirPreference = "E:\\ResultsPreference1\\";
        experimentPreference.executePreference(baseDirPreference,maxRunPreference);

    }
}
