package org.uma.jmetal.measure.impl;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;

import java.util.List;

/**
 * Created by X250 on 2016/4/9.
 */
public  class MyAlgorithmMeasures<S extends Solution<?>> implements Measurable {
    public DurationMeasure durationMeasure ;
    protected SimpleMeasureManager measureManager ;

    protected BasicMeasure<List<S>> solutionListMeasure ;

    public void updateMeasureProgress(List<S> population) {
        durationMeasure.stop();
        solutionListMeasure.push(population);

        durationMeasure.start();
    }

    /* Measures code */
    public void initMeasures() {
        durationMeasure = new DurationMeasure() ;
        solutionListMeasure = new BasicMeasure<>() ;

        measureManager = new SimpleMeasureManager() ;
        measureManager.setPullMeasure("currentExecutionTime", durationMeasure);
        measureManager.setPushMeasure("currentPopulation", solutionListMeasure);
        durationMeasure.reset();
//        durationMeasure.start();
    }

    @Override
    public MeasureManager getMeasureManager() {
        return measureManager ;
    }

}