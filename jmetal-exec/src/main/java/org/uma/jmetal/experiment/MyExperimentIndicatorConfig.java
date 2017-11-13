package org.uma.jmetal.experiment;

import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.point.Point;


/**
 * Created by X250 on 2016/4/12.
 */
public class MyExperimentIndicatorConfig {
    private boolean isEP = false;
    private boolean isHV = false;
    private boolean isGD = false;
    private boolean isIGD = false;
    private boolean isIGDPLUS = false;
    private boolean isSPREAD = false;

    public static enum INDICATORTYPE {EP, HV, GD, IGD, IGDPLUS, SPREAD};

    public MyExperimentIndicatorConfig() {
    }

    public boolean check(INDICATORTYPE indType){
        switch (indType){
            case EP:
            case HV:
            case GD:
            case IGD:
            case IGDPLUS:
            case SPREAD:
                return true;
            default: return false;
        }
    }
    public void addIndicatorType(INDICATORTYPE indType){
        switch (indType) {
            case EP:isEP = true;break;
            case HV:isHV = true;break;
            case GD:isGD = true;break;
            case IGD:isIGD = true;break;
            case IGDPLUS:isIGDPLUS = true;break;
            case SPREAD:isSPREAD = true;break;
        }
    }
    public MyExperimentIndicator generate(Front referenceFront, Point referencepoint) {
        MyExperimentIndicator experimentIndicator = new MyExperimentIndicator(referenceFront);
        if(isEP)
            experimentIndicator.addIndicatorEvaluator(new Epsilon());
        if(isHV) {
            WFGHypervolume hvEvaluator = new WFGHypervolume();
            hvEvaluator.setReferencePoint(referencepoint);
            hvEvaluator.setMiniming();
            experimentIndicator.addIndicatorEvaluator(hvEvaluator);
        }
        if(isGD)
            experimentIndicator.addIndicatorEvaluator(new GenerationalDistance());
        if(isIGD)
            experimentIndicator.addIndicatorEvaluator(new InvertedGenerationalDistance());
        if (isIGDPLUS) {
            InvertedGenerationalDistancePlus IGDPlusEvaluator = new InvertedGenerationalDistancePlus();
            experimentIndicator.addIndicatorEvaluator(IGDPlusEvaluator);
        }
        if (isSPREAD)
            experimentIndicator.addIndicatorEvaluator(new Spread());

        return experimentIndicator;
    }
}
