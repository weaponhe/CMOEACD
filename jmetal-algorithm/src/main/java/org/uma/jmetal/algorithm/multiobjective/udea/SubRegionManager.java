package org.uma.jmetal.algorithm.multiobjective.udea;

import org.uma.jmetal.algorithm.multiobjective.moeacd.ConeSubRegion;
import org.uma.jmetal.algorithm.multiobjective.moeacd.MOEACDUtils;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.KDTree;
import org.uma.jmetal.util.UniformWeightUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class SubRegionManager {
    protected int[] H;
    protected double[] Tau;
    protected int nObj;

    //list of sub-regions
    protected List<SubRegion> subRegionList;
    //list of idxes of extreme sub-regions
    protected List<Integer> extremeSRIdxList;
    //list of idxes of marginal sub-regions
    protected List<Integer> marginalSRIdxList;
    protected KDTree kdTree = null;
    protected int originalSubRegionsNum  = 0;

    protected double c_uneven = 1.3;
    protected boolean isAssistantManager = false;

    public SubRegionManager(){}
    public SubRegionManager(int nObj,int[] H,double[] Tau){
        this.nObj = nObj;
        this.H = H;
        this.Tau = Tau;
    }

    public KDTree getKdTree(){return  kdTree;}

    public List<SubRegion> getSubRegionsList(){return subRegionList;}

    public void generateSubRegionList(List<double[]> predefinedDirections) {
        subRegionList = new ArrayList<>();

        for (int i=0;i<predefinedDirections.size();i++) {
            SubRegion subRegion = new SubRegion();
            subRegion.setDirection(predefinedDirections.get(i));
            subRegion.setIndex(i);
            subRegionList.add(subRegion);
        }

        kdTree =  KDTree.build(predefinedDirections);

        extremeSRIdxList = calcExtremeSubRegionIdx();
        marginalSRIdxList = calcMarginalSubRegionIdx();
    }

    public void generateSubRegionList() {
        if(nObj > 1) {
            List<double[]> uniformDirections = UniformWeightUtils.generateArrayList(H, Tau, nObj);

            generateConeSubRegionList(uniformDirections);
        }else{
            List<double[]> unEvenDirections = generateUnEvenDirectionsBasedOnGeometric(H[0]+1);
            generateConeSubRegionList(unEvenDirections);
            subRegionList.get(0).setDirection(new double[]{0});
            for (int i=1;i<getSubRegionsNum();++i){
                subRegionList.get(i).setDirection(new double[]{0.5*(subRegionList.get(i-1).getDirection()[0] + subRegionList.get(i).getDirection()[0])});
            }
        }
    }

    protected double[][] generateUnEvenDirectionsBasedOnArithmetic(int num){
        double[][] directions = new double[num][nObj];
        double k = 2.0/(num*(num - 1));
        for (int i=1;i<=num;i++){
            double l = k * i * (i-1)/2.0;
            directions[i-1][0] = l;
        }
        return directions;
    }

    protected List<double[]> generateUnEvenDirectionsBasedOnGeometric(int num){
        List<double[]> directions = new ArrayList<>();
        double k = 1.0/(Math.pow(c_uneven,num - 1) - 1.0);
        for (int i=1;i<=num;i++){
            double[] direction = new double[nObj];
            double l = k * (Math.pow(c_uneven,i -1) - 1.0);
            direction[0] = l;
            directions.add(direction);
        }
        return directions;
    }

    public void generateAssistantSubRegionList(List<double[]> predefinedDirections) {
        isAssistantManager = true;
        generateSubRegionList(predefinedDirections);
    }
    public void generateAssistantSubRegionList(int num, int nObj,double c_uneven) {
        List<double[]> unEvenDirections = generateAssistantDirections(num,nObj,c_uneven);
        generateAssistantSubRegionList(unEvenDirections);
    }

    public void generateAssistantSubRegionList(int num, int nObj) {
        List<double[]> unEvenDirections = generateAssistantDirections(num,nObj,c_uneven);
        generateAssistantSubRegionList(unEvenDirections);
    }


    protected List<double[]> generateAssistantDirections(int num, int nObj,double c_uneven){
        this.c_uneven = c_uneven;
        this.nObj = nObj;
        this.isAssistantManager = true;
        List<double[]> directions = new ArrayList<>();
        double k = 1.0/(Math.pow(c_uneven,num - 1) - 1.0);
        for (int i=1;i<=num;i++){
            double[] direction = new double[nObj];
            double l = k * (Math.pow(c_uneven,i -1) - 1.0);
            if(nObj == 2){
                direction[0] = l;
            }
            direction[nObj - 1] = 1.0 - l;
            directions.add(direction);
        }
        return directions;
    }

    public void generateConeSubRegionList(List<double[]> predefinedDirections) {
        subRegionList = new ArrayList<>();

        for (int i=0;i<predefinedDirections.size();i++) {
            SubRegion subRegion = new SubRegion();
            subRegion.setDirection(predefinedDirections.get(i));
            subRegion.setIndex(i);
            subRegionList.add(subRegion);
        }

        kdTree =  KDTree.build(predefinedDirections);

        extremeSRIdxList = calcExtremeSubRegionIdx();
        marginalSRIdxList = calcMarginalSubRegionIdx();
    }

    protected ArrayList<Integer> calcExtremeSubRegionIdx() {
        ArrayList<Integer> idxList = new ArrayList<>(nObj);
        double[] v = new double[nObj];
        for (int i=0;i<nObj;i++)
            v[i] = 0.0;
        if(nObj == 1){
            int index = kdTree.queryIndex(v);
            idxList.add(index);
        }else {
            for (int i = 0; i < nObj; i++) {
                v[i] = 1.0;
                int index = kdTree.queryIndex(v);
                idxList.add(index);
                v[i] = 0.0;
            }
        }
        return idxList;
    }

    protected ArrayList<Integer> calcMarginalSubRegionIdx() {
        ArrayList<Integer> idxList = new ArrayList<>(nObj);
        for (int i=0;i<getSubRegionsNum();i++){
            double[] refDirection = getSubRegion(i).getDirection();
            for (int j = 0;j<nObj;j++){
                if(refDirection[j] < Constant.TOLERATION){
                    idxList.add(i);
                    break;
                }
            }
        }
        return idxList;
    }

    public List<Integer> getExtremeSubRegionIdxList() {
        return extremeSRIdxList;
    }

    public List<Integer> getMarginalSubRegionIdxList() {
        return marginalSRIdxList;
    }


    public void reconstructSubRegionList(List<double[]> refDirections) {
        generateSubRegionList(refDirections);
    }


    public SubRegion getSubRegion(int idxSubRegion) {
        return subRegionList.get(idxSubRegion);
    }

    public void setOriginalSubRegionsNum(int num){originalSubRegionsNum = num;}
    public int getOriginalSubRegionsNum(){return originalSubRegionsNum;}
    public int getSubRegionsNum() {
        return subRegionList.size();
    }

    /**
     * Initialize neighborhoods
     */
    public void initializeSubRegionsNeighbors(int neighborhoodSize) {
//        double[] x = new double[getSubRegionsNum()];
//        int[] idx = new int[getSubRegionsNum()];
//
//        for (int i = 0; i < getSubRegionsNum(); i++) {
//            // calculate the distances based on central reference observation vector
//            for (int j = 0; j < getSubRegionsNum(); j++) {
//                x[j] = MOEACDUtils.distance2(getSubRegion(i).getDirection(),getSubRegion(j).getDirection());
//                idx[j] = j;
//            }
//
//            // find 'neighborhoodSize' nearest neighboring subregions
//            Utils.minFastSort(x, idx, getSubRegionsNum(), neighborhoodSize);
//            for(int k=0;k<neighborhoodSize;++k)
//                getSubRegion(i).addNeighbor(idx[k]);
//        }
        for(int i=0;i<getSubRegionsNum();i++){
            SubRegion subRegion = getSubRegion(i);
            List<Integer> neighbors = kdTree.queryKNearestIndexes(subRegion.getDirection(),neighborhoodSize);
            subRegion.setNeighbors(neighbors);
        }
    }


    public int indexing(double[] _normObjective) {

        double[] observationV = Utils.calObservation(_normObjective);

        int index = kdTree.queryIndex(observationV);

        return index;
    }

    public int indexing(double[] _normObjective,KDTree kdTree) {

        double[] observationV = Utils.calObservation(_normObjective);

        int index = 0;

        if(nObj == 1) {
            index = fasterIndexingForMono(observationV);
        }
        else
            index = kdTree.queryIndex(observationV);
        return index;
    }

    protected int fasterIndexingForMono(double[] observation){
//        if(observation[0] > 1.0)
//            JMetalLogger.logger.info(observation[0]+"\n");
        double tmp = (2.0*Math.pow(c_uneven,getSubRegionsNum()-1)*observation[0] + 2)/(1.0 + c_uneven);
        return (int)Math.floor(Math.log(tmp)/Math.log(c_uneven)) + 1;
    }

    public SubRegion locateSubRegion(double[] _normObjective) {
        return getSubRegion(indexing(_normObjective));
    }
}
