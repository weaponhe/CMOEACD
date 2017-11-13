package org.uma.jmetal.experiment;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;

import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;


import java.util.List;
import java.util.Vector;

/**
 * Created by X250 on 2017/1/1.
 */
public class MyExperimentMonoIndicator {
    double optimum;

    Long computingTimes;
    Vector<Integer> generation;

    Vector<Double> optimumList;
    Vector<Double> errorValueList;
    Vector<Vector<Point>> fronts;

    public MyExperimentMonoIndicator(double optimum) {
        generation = new Vector();
        this.optimum = optimum;
        optimumList = new Vector();
        errorValueList = new Vector<>();
        fronts = new Vector<>();

    }

    public Vector<Vector<Point>> getFronts(){return this.fronts;}


    public Vector<Double> getOptimumList(){return optimumList;}

    public Vector<Double> getErrorValueList(){return errorValueList;}

    public void computeQualityIndicators(int gen,List<? extends Solution<?>> population) {

        generation.add(gen);
        double currentOptimum = Double.MAX_VALUE;
        Vector<Point> optimumPop = new Vector<>();
        for (int i=0;i<population.size();i++){
            currentOptimum = Math.min(currentOptimum, population.get(i).getObjective(0));
        }
        double error = currentOptimum - optimum;
        optimumList.add(currentOptimum);
        errorValueList.add(error);
        Point point = new ArrayPoint(1);
        point.setDimensionValue(0,currentOptimum);
        optimumPop.add(point);
        fronts.add(optimumPop);
    }
    public void setComputingTime(Long time){computingTimes = time;}

    public Long getComputingTimes(){return computingTimes;}
    public Vector<Integer> getGeneration(){return generation;}

    public void printFinalIndicators(){
        int finalGen = generation.size()-1;
        String outputString = " ["+generation.get(finalGen)+"]\n" ;
        outputString += "Global Optimum    : " +"\n";
        outputString += "Error Value   : " +"\n";
        JMetalLogger.logger.info(outputString);
    }
}

