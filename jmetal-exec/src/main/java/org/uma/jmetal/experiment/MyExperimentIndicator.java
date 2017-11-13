package org.uma.jmetal.experiment;

import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Created by X250 on 2016/4/9.
 */
public class MyExperimentIndicator{

    Front referenceFront;

    Long computingTimes;
    Vector<Integer> generation;

    Vector<GenericIndicator> indicatorEvaluators;
    Vector<Vector<Double>> indicators;
    Vector<Vector<Point>> fronts;
//
//    PISAHypervolume hvIndicator;
//    Vector<Double> Hypervolume;
//    Epsilon epsIndicator;
//    Vector<Double> EPS;
//    GenerationalDistance gdIndicator;
//    Vector<Double> GD;
//    InvertedGenerationalDistance igdIndicator;
//    Vector<Double> IGD;
//    InvertedGenerationalDistancePlus igdplusIndicator;
//    Vector<Double> IGDPlus;
//
//    Spread spreadIndicator;
//    Vector<Double> spread;
//    ErrorRatio errorIndicator;
//    Vector<Double> error;

    public MyExperimentIndicator(Front referenceFront) {
        generation = new Vector();
        this.referenceFront = referenceFront;
        indicatorEvaluators = new Vector<GenericIndicator>();
        indicators = new Vector<Vector<Double>>();
        fronts = new Vector<Vector<Point>>();
//        hvIndicator = new PISAHypervolume(referenceFront);
//        Hypervolume = new Vector();
//        epsIndicator = new Epsilon(referenceFront);
//        EPS= new Vector();
//        gdIndicator = new GenerationalDistance(referenceFront);
//        GD= new Vector();
//        igdIndicator = new InvertedGenerationalDistance(referenceFront);
//        IGD= new Vector();
//        igdplusIndicator = new InvertedGenerationalDistancePlus(referenceFront);
//        IGDPlus= new Vector();
//        spreadIndicator = new Spread(referenceFront);
//        spread= new Vector<Double>();
//        errorIndicator = new ErrorRatio<List<? extends Solution<?>>>(referenceFront);
//        error= new Vector<Double>();
    }

    public void addIndicatorEvaluator(GenericIndicator evaluator){
        try {
            evaluator.setReferenceParetoFront(referenceFront);
        }catch (IOException e){}

        indicatorEvaluators.add(evaluator);
        indicators.add(new Vector<Double>());
    }

    public void computeQualityIndicators(int gen,List<? extends Solution<?>> population) {

        generation.add(gen);
        List<? extends Solution<?>> frontPopulation = SolutionListUtils.getNondominatedSolutions(population);
        saveFronts(frontPopulation);
        for(int i=0;i<indicatorEvaluators.size();++i){
//            JMetalLogger.logger.info(indicatorEvaluators.get(i).getName() + "  ... ");
            double result = (Double)indicatorEvaluators.get(i).evaluate(frontPopulation);
            indicators.get(i).add(result);
//            JMetalLogger.logger.info(indicatorEvaluators.get(i).getName() + "   " +result );
        }
//        Hypervolume.add(hvIndicator.evaluate(frontPopulation));
//
//        EPS.add(epsIndicator.evaluate(frontPopulation));
//
//        GD.add(gdIndicator.evaluate(frontPopulation));
//
//        IGD.add(igdIndicator.evaluate(frontPopulation));
//
//        IGDPlus.add(igdplusIndicator.evaluate(frontPopulation));
//
//        spread.add(spreadIndicator.evaluate(frontPopulation));
//
//        error.add(errorIndicator.evaluate(frontPopulation));
    }
    public void setComputingTime(Long time){computingTimes = time;}

    public Long getComputingTimes(){return computingTimes;}
    public Vector<Integer> getGeneration(){return generation;}
//    public Vector<Double> getHypervolume(){return Hypervolume;}
//    public Vector<Double> getEPS(){return EPS;}
//    public Vector<Double> getGD(){return GD;}
//    public Vector<Double> getIGD(){return IGD;}
//    public Vector<Double> getIGDPlus(){return IGDPlus;}
//    public Vector<Double> getSpread(){return spread;}
//    public Vector<Double> getErrorRatio(){return error;}

    public int getIndicatorNumer(){return indicatorEvaluators.size();}
    public Vector<Double> getIndicator(int idx){return indicators.get(idx);}
    public GenericIndicator getIndicatorEvaluator(int idx){return indicatorEvaluators.get(idx);}
    public Vector<Vector<Point>> getFronts(){return this.fronts;}
    public void saveFronts(List<? extends Solution<?>> frontPopulation){
        Vector<Point> front = new Vector<Point>();
        if(frontPopulation.isEmpty()) {
            fronts.add(front);
            return;
        }
        int nObj = frontPopulation.get(0).getNumberOfObjectives();

        for (Solution<?> solution: frontPopulation) {
            Point point = new ArrayPoint(nObj);
            for (int i =0;i<nObj;++i)
                point.setDimensionValue(i,solution.getObjective(i));
            front.add(point);
        }
        fronts.add(front);
    }
    public void printFinalIndicators(){

        int finalGen = generation.size()-1;
        String outputString = " ["+generation.get(finalGen)+"]\n" ;
        for(int i=0;i<indicatorEvaluators.size();++i){
            outputString += indicatorEvaluators.get(i).getName()+"\t: " + indicators.get(i).get(finalGen)+"\n";
        }
//        outputString += "Hypervolume     : " + Hypervolume.get(finalGen)+"\n";
//        outputString += "Epsilon         : " + EPS.get(finalGen) + "\n" ;
//        outputString += "GD              : " + GD.get(finalGen)+ "\n";
//        outputString += "IGD             : " + IGD.get(finalGen) + "\n";
//        outputString += "IGD+            : " + IGDPlus.get(finalGen) + "\n";
//        outputString += "Spread          : " + spread.get(finalGen)+ "\n";
//        outputString += "Error ratio     : " + error.get(finalGen) + "\n";
        JMetalLogger.logger.info(outputString);
    }
}

