package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.KDTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/5.
 */
public class ConeSubRegionManagerUnified extends ConeSubRegionManager {
    protected double c_uneven = 1.3;
    public ConeSubRegionManagerUnified(int _nObj, int[] _arrayH, double[] _integratedTaus) {
        super(_nObj,_arrayH,_integratedTaus);
    }

    public ConeSubRegionManagerUnified(int _nObj, int[] _arrayH, double[] _integratedTaus,double c_uneven) {
        super(_nObj,_arrayH,_integratedTaus);
        this.c_uneven = c_uneven;
    }


    public void generateConeSubRegionList() {
        if (nObj > 1) {
            super.generateConeSubRegionList();
        } else {
//            List<double[]> unEvenDirections = generateEvenDirections(arrayH[0] + 1);
            List<double[]> unEvenDirections = generateUnEvenDirectionsBasedOnGeometric(arrayH[0] + 1);
//            List<double[]> unEvenDirections = generateUnEvenDirectionsBasedOnArithmetic(arrayH[0] + 1);
            generateConeSubRegionList(unEvenDirections);
            coneSubRegions.get(0).setRefDirection(new double[]{0});
            for (int i = 1; i < getConeSubRegionsNum(); ++i) {
                coneSubRegions.get(i).setRefDirection(new double[]{0.5 * (coneSubRegions.get(i - 1).getRefDirection()[0] + coneSubRegions.get(i).getRefDirection()[0])});
            }
        }
    }

    protected List<double[]> generateUnEvenDirectionsBasedOnArithmetic(int num) {
        List<double[]> directions = new ArrayList<>();
        double k = 2.0 / (num * (num - 1));
        for (int i = 1; i <= num; i++) {
            double[] direction = new double[nObj];
            double l = k * i * (i - 1) / 2.0;
            direction[0] = l;
            directions.add(direction);
        }
        return directions;
    }

    protected List<double[]> generateEvenDirections(int num) {
        List<double[]> directions = new ArrayList<>();
        double k = 1.0 / (num - 1) ;
        for (int i = 1; i <= num; i++) {
            double[] direction = new double[nObj];
            double l = k * (i-1);
            direction[0] = l;
            directions.add(direction);
        }
        return directions;
    }

    protected List<double[]> generateUnEvenDirectionsBasedOnGeometric(int num) {
        List<double[]> directions = new ArrayList<>();
//        double k = 1.0 / (Math.pow(c_uneven, num - 1) - 1.0);
//        for (int i = 1; i <= num; i++) {
//            double[] direction = new double[nObj];
//            double l = k * (Math.pow(c_uneven, i - 1) - 1.0);
//            direction[0] = l;
//            directions.add(direction);
//        }
//        c_uneven = Math.pow(2.0,1.0/(num -1));
        for (int i = 1; i <= num; i++) {
            double[] direction = new double[nObj];
            double l =  0.25*(i-1)/(1.0*num - 0.75*i - 0.25);
            direction[0] = l;
            directions.add(direction);
        }
        return directions;
    }

    protected ArrayList<Integer> calcExtremeConeSubRegionIdx() {
        if (nObj > 1) {
            return super.calcExtremeConeSubRegionIdx();
        } else {
            ArrayList<Integer> idxList = new ArrayList<>(nObj);
            int index = kdTree.queryIndex(new double[]{0.0});
            idxList.add(index);
//            index = kdTree.queryIndex(new double[]{1.0});
//            idxList.add(index);
            return idxList;
        }
    }

    protected ArrayList<Integer> calcMarginalConeSubRegionIdx() {
        if (nObj > 1) {
            return super.calcExtremeConeSubRegionIdx();
        } else {
            ArrayList<Integer> idxList = new ArrayList<>(nObj);
            int index = kdTree.queryIndex(new double[]{0.0});
            idxList.add(index);
//            index = kdTree.queryIndex(new double[]{1.0});
//            idxList.add(index);
            return idxList;
        }
    }

    public int indexing(double[] _normObjective) {

        double[] observationV = MOEACDUtils.calObservation(_normObjective);

        int index = 0;
//        if(nObj == 1) {
//            index = fasterIndexingForMono(observationV);
//        }
//        else
            index = kdTree.queryIndex(observationV);

        return index;
    }

    protected int fasterIndexingForMono(double[] observation){
        double tmp = (2.0*Math.pow(c_uneven,getConeSubRegionsNum()-1)*observation[0] + 2)/(1.0 + c_uneven);
        return (int)Math.floor(Math.log(tmp)/Math.log(c_uneven)) + 1;
    }
}
