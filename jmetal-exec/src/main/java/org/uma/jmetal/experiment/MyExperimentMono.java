package org.uma.jmetal.experiment;

import org.uma.jmetal.experiment.MOEACD.CUCDEAIIStudy;
import org.uma.jmetal.experiment.MOEACD.CUCDEAStudy;
import org.uma.jmetal.experiment.MOEACD.UCDEAStudy;
import org.uma.jmetal.experiment.UDEA.CUDEAIIMonoStudy;
import org.uma.jmetal.experiment.UDEA.CUDEAMonoStudy;
import org.uma.jmetal.experiment.UDEA.UDEAMonoStudy;
import org.uma.jmetal.experiment.UDEA.UDEAStudy;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.CEC2005Problem;
import org.uma.jmetal.problem.singleobjective.G13.*;
import org.uma.jmetal.problem.singleobjective.Rastrigin;
import org.uma.jmetal.problem.singleobjective.Schwefel;
import org.uma.jmetal.problem.singleobjective.ShinnProblems.*;
import org.uma.jmetal.solution.DoubleSolution;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by X250 on 2017/1/1.
 */
public class MyExperimentMono {

    public void executeCEC2005Measure(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 5;
        int neighborhoodSize = 5;
        double neighborhoodSelectionProbability = 0.5;
        double pbi_theta = 5.0;
        double[] typicalX = null;

        List<Problem<DoubleSolution>> problemList = new ArrayList<>();
        int[] D = new int[]{10,30,50};
        int problemIDMin = 13;
        int problemIDMax = 17;
        //12 和 25 有问题
        for (int i=0;i<= problemIDMax  -problemIDMin;i++){
            for (int j=0;j<D.length;j++) {
                int problemID = problemIDMin+i;
                Problem<DoubleSolution> problem = new CEC2005Problem(problemID, D[j]);
                problem.setName("CEC" + problemID + "_" + D[j]);
                problemList.add(problem);
            }
        };

        int[] popsList = new int[problemList.size()];
        for (int i = 0;i<problemList.size();i++) {
            popsList[i] = 50;
        };

        int[] lambda = new int[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            lambda[i] = popsList[i];
        }

        int[] mu = new int[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            mu[i] = 10;
        }

        double[] sigma = new double[problemList.size()];
        for (int i =0;i<problemList.size();i++){
            sigma[i] = 0.3;
        }

        double[] c_uneven = new double[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            c_uneven[i] = 1.08;
        }

        int[] maxIterationsList = new int[problemList.size()];
        int c  = 0;
        for (int i=0;i<=problemIDMax - problemIDMin;i++){
            for (int j=0;j<D.length;j++){
                maxIterationsList[c] = D[j]*10000/popsList[c];
                c++;
            }
        };

        double[] allOptimum = new double[]{
                -450.0, //CEC1
                -450.0, //CEC2
                -450.0, //CEC3
                -450.0, //CEC4
                -310.0, //CEC5
                390.0,  //CEC6
                -180.0, //CEC7
                -140.0, //CEC8
                -330.0, //CEC9
                -330.0, //CEC10
                90.0,   //CEC11
                -460.0, //CEC12
                -130.0, //CEC13
                -300.0, //CEC14
                120.0, //CEC15
                120.0,  //CEC16
                120.0,  //CEC17
                10.0,   //CEC18
                10.0,   //CEC19
                10.0,   //CEC20
                360.0,  //CEC21
                360.0,  //CEC22
                360.0,  //CEC23
                260.0,  //CEC24
                260.0   //CEC25
        };

        double[] optimumList = new double[problemList.size()];
        c = 0;
        for (int i=0;i<problemIDMax - problemIDMin;i++){
            int problemID = problemIDMin + i;
            for (int j=0;j<D.length;j++){
                optimumList[c++] = allOptimum[problemID];
            }
        }
//

//        MonoDEStudy monoDEExperiment = new MonoDEStudy();
//        try {
//            monoDEExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    f,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        }catch (FileNotFoundException e){
//        }
//
//        MonoCMAESStudy monoCMAESExperiment = new MonoCMAESStudy();
//        try {
//            monoCMAESExperiment.executeMeasure(baseDir,
//                    lambda,
//                    typicalX,
//                    sigma,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        }catch (FileNotFoundException e){
//        }
//
//        MonoElitistESStudy monoElitistESExperiment = new MonoElitistESStudy();
//        try {
//            monoElitistESExperiment.executeMeasure(baseDir,
//                    mutationDistributionIndex,
//                    mu,
//                    lambda,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        }catch (FileNotFoundException e){
//        }
//        UDEAMonoStudy udeaMonoExperiment = new UDEAMonoStudy();
//        try {
//            udeaMonoExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        } catch (FileNotFoundException e) {
//        }
    }


    public void executeShinnProblemMeasure(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 20.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborhoodSize = 30;
        double neighborhoodSelectionProbability = 0.8;
        double[] typicalX = null;

        int D = 10;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new ShinnF1(D),new ShinnF2(D),new ShinnF3(D),
                new ShinnF4(D),new ShinnF5(D),new ShinnF6(D),
                new ShinnF7(D),new ShinnF8(D),new ShinnF9(D),
                new ShinnF10(D),new ShinnF11(D),new ShinnF12(D)
        );

        int[] popsList = new int[problemList.size()];
        for (int i = 0;i<problemList.size();i++) {
            popsList[i] = D*10;
        };

        int[] lambda = new int[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            lambda[i] = popsList[i];
        }

        int[] mu = new int[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            mu[i] = 15*D/10;
        }

        double[] sigma = new double[problemList.size()];
        for (int i =0;i<problemList.size();i++){
            sigma[i] = 0.3;
        }

        double[] c_uneven = new double[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            c_uneven[i] = 1.08;
        }


        int[] maxIterationsList = new int[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            maxIterationsList[i] = D*5000/popsList[i];
        };

        double[] optimumList = new double[]{
                -1.21598*D, //F1
                -2.0*D, //F2
                0.0, //F3
                0.0, //F4
                0.0, //F5
                -1.85*D, //F6
                0.0, //F7
                0.0, //F8
                0.0, //F9
                0.0, //F10
                0.0, //F11
                0.0 //F12
        };
//
//        MonoDEStudy monoDEExperiment = new MonoDEStudy();
//        try {
//            monoDEExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    f,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        }catch (FileNotFoundException e){
//        }
//
//        MonoCMAESStudy monoCMAESExperiment = new MonoCMAESStudy();
//        try {
//            monoCMAESExperiment.executeMeasure(baseDir,
//                    lambda,
//                    typicalX,
//                    sigma,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        }catch (FileNotFoundException e){
//        }
//
//        MonoElitistESStudy monoElitistESExperiment = new MonoElitistESStudy();
//        try {
//            monoElitistESExperiment.executeMeasure(baseDir,
//                    mutationDistributionIndex,
//                    mu,
//                    lambda,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        }catch (FileNotFoundException e){
//        }
//        UDEAMonoStudy udeaMonoExperiment = new UDEAMonoStudy();
//        try {
//            udeaMonoExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        } catch (FileNotFoundException e) {
//        }

        UCDEAStudy UCDEAExperiment = new UCDEAStudy();
        try {
            UCDEAExperiment.executeMeasure(baseDir,
                    c_uneven,
                    crossoverProbability,
                    crossoverDistributionIndex,
                    f,
                    mutationDistributionIndex,
                    neighborhoodSize,
                    neighborhoodSelectionProbability,
                    maxRun,
                    problemList,
                    popsList,
                    maxIterationsList,
                    optimumList);
        } catch (FileNotFoundException e) {
        }
    }

    public void executeUnConstrainedMonoMeasure(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 20.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborhoodSize = 30;
        double neighborhoodSelectionProbability = 0.8;

        double[] typicalX = null;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new Rastrigin(10), new Rastrigin(20),
                new Schwefel(10),new Schwefel(20)
        );

        int[] popsList = new int[]{100,200,100,200};

        int[] lambda = new int[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            lambda[i] = popsList[i];
        }

        int[] mu = new int[]{15,30,15,30};

        double[] sigma = new double[problemList.size()];
        for (int i =0;i<problemList.size();i++){
            sigma[i] = 0.3;
        }

        double[] c_uneven = new double[]{1.08,1.04,1.08,1.04};

        int[] maxIterationsList = new int[]{10*5000/100,20*5000/200,10*5000/100,20*5000/200};

        double[] optimumList = new double[]{
                0.0,
                0.0,
                0.0,
                0.0
        };

//        MonoDEStudy monoDEExperiment = new MonoDEStudy();
//        try {
//            monoDEExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    f,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        }catch (FileNotFoundException e){
//        }
//
//        MonoCMAESStudy monoCMAESExperiment = new MonoCMAESStudy();
//        try {
//            monoCMAESExperiment.executeMeasure(baseDir,
//                    lambda,
//                    typicalX,
//                    sigma,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        }catch (FileNotFoundException e){
//        }
//
//        MonoElitistESStudy monoElitistESExperiment = new MonoElitistESStudy();
//        try {
//            monoElitistESExperiment.executeMeasure(baseDir,
//                    mutationDistributionIndex,
//                    mu,
//                    lambda,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        }catch (FileNotFoundException e){
//        }

//        UDEAMonoStudy udeaMonoExperiment = new UDEAMonoStudy();
//        try {
//            udeaMonoExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        } catch (FileNotFoundException e) {
//        }

        UCDEAStudy UCDEAExperiment = new UCDEAStudy();
        try {
            UCDEAExperiment.executeMeasure(baseDir,
                    c_uneven,
                    crossoverProbability,
                    crossoverDistributionIndex,
                    f,
                    mutationDistributionIndex,
                    neighborhoodSize,
                    neighborhoodSelectionProbability,
                    maxRun,
                    problemList,
                    popsList,
                    maxIterationsList,
                    optimumList);
        } catch (FileNotFoundException e) {
        }
    }


    public void executeG13Measure(String baseDir,int maxRun) {
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 30.0;
        double crossoverDistributionIndexNSGAIII = 30.0;
        double f = 0.5;
        double mutationDistributionIndex = 20.0;
        int neighborSize = 5;
        int neighborhoodSize = 5;
        double neighborhoodSelectionProbability = 0.5;
        double pbi_theta = 5.0;
        double[] typicalX = null;
        double Pf = 0.45;

        List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
                new g01() , new g02(), new g03(), new g04(),
                new g05(), new g06(), new g07(), new g08(),
                new g09(), new g10(),new g11(),new g12(),new g13()
        );

        int[] popsList = new int[problemList.size()];
        for (int i = 0;i<problemList.size();i++) {
            popsList[i] = 200;
        };

        int[] lambda = new int[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            lambda[i] = popsList[i];
        }

        int[] mu = new int[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            mu[i] = 30;
        }

        double[] sigma = new double[problemList.size()];
        for (int i =0;i<problemList.size();i++){
            sigma[i] = 0.3;
        }

        double[] c_uneven = new double[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            c_uneven[i] = 1.04;
        }


        int[] maxIterationsList = new int[problemList.size()];
        for (int i=0;i<problemList.size();i++){
            maxIterationsList[i] = 500000/popsList[i];
        };

        double[] optimumList = new double[]{
                -15.0, //g01
                -0.80361910412559,//g02
                -1.00050010001000, //g03
                -30665.53867178332, //g04
                5126.4967140071, //g05
                -6961.81387558015, //g06
                24.30620906818, //g07
                -0.0958250414180359, //g08
                680.630057374402, //g09
                7049.24802052867, //g10
                0.7499, //g11
                -1.0, //g12
                0.053941514041898//g13
        };

//        MonoSRElitistESStudy monoSRElitistESExperiment = new MonoSRElitistESStudy();
//        try {
//            monoSRElitistESExperiment.executeMeasure(baseDir,
//                    mutationDistributionIndex,
//                    mu,
//                    lambda,
//                    Pf,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        }catch (FileNotFoundException e){
//        }

//        CUDEAMonoStudy CudeaMonoExperiment = new CUDEAMonoStudy();
//        try {
//            CudeaMonoExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        } catch (FileNotFoundException e) {
//        }
//
//        CUDEAIIMonoStudy CudeaIIMonoExperiment = new CUDEAIIMonoStudy();
//        try {
//            CudeaIIMonoExperiment.executeMeasure(baseDir,
//                    crossoverProbability,
//                    crossoverDistributionIndex,
//                    f,
//                    mutationDistributionIndex,
//                    neighborhoodSize,
//                    neighborhoodSelectionProbability,
//                    maxRun,
//                    problemList,
//                    popsList,
//                    maxIterationsList,
//                    optimumList);
//        } catch (FileNotFoundException e) {
//        }
        CUCDEAStudy CUCDEAExperiment = new CUCDEAStudy();
        try {
            CUCDEAExperiment.executeMeasure(baseDir,
                    c_uneven,
                    crossoverProbability,
                    crossoverDistributionIndex,
                    f,
                    mutationDistributionIndex,
                    neighborhoodSize,
                    neighborhoodSelectionProbability,
                    maxRun,
                    problemList,
                    popsList,
                    maxIterationsList,
                    optimumList);
        } catch (FileNotFoundException e) {
        }

        CUCDEAIIStudy CUCDEAIIExperiment = new CUCDEAIIStudy();
        try {
            CUCDEAIIExperiment.executeMeasure(baseDir,
                    c_uneven,
                    crossoverProbability,
                    crossoverDistributionIndex,
                    f,
                    mutationDistributionIndex,
                    neighborhoodSize,
                    neighborhoodSelectionProbability,
                    maxRun,
                    problemList,
                    popsList,
                    maxIterationsList,
                    optimumList);
        } catch (FileNotFoundException e) {
        }
    }

    public static void main(String[] args) {
        int maxRun = 20;
        MyExperimentMono experiment = new MyExperimentMono();
//        String baseDir = "E:\\ResultsMonoCompare1\\";
//        String baseDir = "E:\\ResultsMonoCUDEA\\";
//        String baseDir = "E:\\ResultsMonoUCDEANei0_9\\";
//        String baseDir = "E:\\ResultsMonoConstraintsCompare\\";
//        String baseDir = "E:\\ResultsMonoConstraintsCUCDEA\\";
        String baseDir = "E://ResultsMonoUnConstraintsUCDEA/";
//        String baseDir = "E://ResultsMonoUnConstraintsCompare/";

//        experiment.executeCEC2005Measure(baseDir,maxRun);
        experiment.executeShinnProblemMeasure(baseDir,maxRun);
        experiment.executeUnConstrainedMonoMeasure(baseDir,maxRun);
//        experiment.executeG13Measure(baseDir,maxRun);
    }
}
