package org.uma.jmetal.experiment;

import org.uma.jmetal.measure.MeasureListener;
import org.uma.jmetal.solution.Solution;

import java.util.List;

/**
 * Created by X250 on 2017/1/1.
 */
public class MonoIndicatorsListener <S extends Solution<?>> implements MeasureListener<List<S>> {
    private int gen;
    private int maxGen;
    private MyExperimentMonoIndicator experimentIndicator = null;

    private int interval;

    public MonoIndicatorsListener(int maxGen){
        this(maxGen,20);
    }
    public MonoIndicatorsListener(int maxGen,int interval){
        this.maxGen = maxGen;
        gen = 0;

        this.interval = interval;
    }

    public MonoIndicatorsListener(MyExperimentMonoIndicator myIndicator,int maxGen){
        this(myIndicator,maxGen,20);
    }
    public MonoIndicatorsListener(MyExperimentMonoIndicator myIndicator,int maxGen, int interval){
        this(maxGen,interval);
        experimentIndicator = myIndicator;
    }

    public void setExperimentIndicator(MyExperimentMonoIndicator myIndicator){experimentIndicator = myIndicator;}

    public MyExperimentMonoIndicator getExperimentIndicator(){
        return experimentIndicator;
    }

    @Override synchronized public void measureGenerated(List<S> solutions) {
        gen ++ ;
        if (gen == 1 || gen == maxGen || gen%interval == 0) {
            //calculating all indicatots
            experimentIndicator.computeQualityIndicators(gen,solutions);
        }
    }
}