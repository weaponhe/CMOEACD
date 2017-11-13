package org.uma.jmetal.experiment;

import org.uma.jmetal.experiment.MyExperimentIndicator;
import org.uma.jmetal.measure.MeasureListener;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.front.Front;

import java.util.List;

/**
 * Created by X250 on 2016/4/9.
 */
public class IndicatorsListener<S extends Solution<?>> implements MeasureListener<List<S>> {
    private int gen;
    private int maxGen;
    private MyExperimentIndicator experimentIndicator = null;
//    private List<S> lastGenPopulation;
//    private int lastGen;
//    private int phase;
//    private int limitGen;
    private int interval;
    private int collectTimes;
//    private int half ;
//    private int nextCollectGen;
    public IndicatorsListener(int maxGen){
        this(maxGen,20);
    }
    public IndicatorsListener(int maxGen,int collectTimes/*interval*/){
        this.maxGen = maxGen;
        gen = 0;
//        nextCollectGen = 1;
//        lastGen = gen;
//        phase = 1;
//        limitGen = maxGen/4;
//        half = (collectTimes/2);
//        if(half < 1)
//            half = 1;
        interval = maxGen/collectTimes;
        this.collectTimes = collectTimes;
    }

    public IndicatorsListener(MyExperimentIndicator myIndicator,int maxGen){
        this(myIndicator,maxGen,20);
    }
    public IndicatorsListener(MyExperimentIndicator myIndicator,int maxGen, int collectTimes/*interval*/){
        this(maxGen,collectTimes/*interval*/);
        experimentIndicator = myIndicator;
    }
//
//    private void measureLastGenPopulation(){
//        int num = experimentIndicator.getGeneration().size();
//        if(experimentIndicator.getGeneration().get(num-1) != lastGen){
//            //calculating all indicatots
//            experimentIndicator.computeQualityIndicators(lastGen,lastGenPopulation);
////            experimentIndicator.printFinalIndicators();
//        }
//    }
    public void setExperimentIndicator(MyExperimentIndicator myIndicator){experimentIndicator = myIndicator;}

    public MyExperimentIndicator getExperimentIndicator(){
//        measureLastGenPopulation();
        return experimentIndicator;
    }

    @Override synchronized public void measureGenerated(List<S> solutions) {
//        lastGenPopulation = solutions;
//        lastGen = gen;
        gen ++ ;
        if (gen == 1 || gen == maxGen || gen%interval == 0) {
            //calculating all indicatots
            experimentIndicator.computeQualityIndicators(gen,solutions);
//            experimentIndicator.printFinalIndicators();
//            if(gen >= limitGen){
//                phase++;
//                limitGen = maxGen;
//                interval = (maxGen*3/4)/half;
//            }
//            nextCollectGen += interval;
        }
    }
}