package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDUtils;
import org.uma.jmetal.util.CombinationUtils;
import org.uma.jmetal.util.JMetalLogger;

/**
 * Created by X250 on 2016/6/9.
 */
public class popsizeCalculator {

    public int calcPopSize(int _nObj,int _H){
        int popSize = 0;
        if(_H < _nObj){
            for(int i=1;i<=_H;i++)
                popSize +=  CombinationUtils.compute(i+_nObj-1,_nObj-1);
            popSize += 1;
        }
        else{
            popSize += 1;
            for (int i=1;i<=_nObj-1;i++)
                popSize += CombinationUtils.compute(i+_nObj-1,_nObj-1);
            for(int i = _nObj;i<=_H;i++)
                popSize += (CombinationUtils.compute(i+_nObj-1,_nObj-1) - CombinationUtils.compute(i-1,_nObj-1));
        }
        return popSize;
    }

    public static void main(String[] argv){
        popsizeCalculator calculator = new popsizeCalculator();
        for(int nObj =3;nObj<=3;nObj++){
            for (int H = 150;H>=100;H--){
                JMetalLogger.logger.info("\n["+nObj+"]\t<"+H+">\t"+calculator.calcPopSize(nObj,H)+"\n"+CombinationUtils.compute(H+nObj-1,nObj-1)+"\n");
            }
        }
    }
}
