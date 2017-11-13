package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.algorithm.multiobjective.udea.Utils;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.util.*;
import org.uma.jmetal.util.front.imp.ArrayFront;

import java.util.*;

/**
 * Created by X250 on 2016/4/22.
 */
public class ConeSubRegionManager {
    //For reducing the populationsize when the objective of problem is high
    protected int[] arrayH;
    protected double[] integratedTaus;
    protected int nObj;
    protected int[] numsOfLayers;
    protected int[] startIdxLayers;
    protected ArrayList<ConeSubRegion> coneSubRegions;
    protected ArrayList<Integer> extremeConeSRIdxList;
    protected ArrayList<Integer> marginalConeSRIdxList;
    protected KDTree kdTree = null;
    protected int originalSubRegionsNum  = 0;
    public ConeSubRegionManager() {}
    public ConeSubRegionManager(int _nObj, int[] _arrayH, double[] _integratedTaus) {
        this.nObj = _nObj;
        this.arrayH = _arrayH;
        this.integratedTaus = _integratedTaus;

//        numsOfLayers = new int[this.arrayH.length];
//        startIdxLayers = new int[this.arrayH.length];
//        coneSubRegions = new ArrayList<>();
//        extremeConeSRIdxList = new ArrayList<>();
//        marginalConeSRIdxList = new ArrayList<>();
    }

    public KDTree getKdTree(){return  kdTree;}

    public List<ConeSubRegion> getConeSubRegionsList(){return coneSubRegions;}

    public void generateConeSubRegionList() {
//        numsOfLayers = new int[this.arrayH.length];
//        startIdxLayers = new int[this.arrayH.length];
//        startIdxLayers[0] = 0;
//        coneSubRegions = new ArrayList<>();
//
//        for (int i = 0; i < this.arrayH.length; ++i) {
//            if (i > 0)
//                startIdxLayers[i] = startIdxLayers[i - 1] + numsOfLayers[i - 1];
//            ArrayList<ConeSubRegion> layerConeSubRegions = genConeSubRegions(arrayH[i], integratedTaus[i], startIdxLayers[i]);
//            numsOfLayers[i] = layerConeSubRegions.size();
//            coneSubRegions.addAll(layerConeSubRegions);
//        }
//
//
//        ArrayList<double[]> uniformWeights = new ArrayList<>(getConeSubRegionsNum());
//        for (int i=0;i<getConeSubRegionsNum();++i){
//            uniformWeights.add(getConeSubRegion(i).getRefObservation());
//        }
//        kdTree =  KDTree.build(uniformWeights);
//
//        extremeConeSRIdxList = calcExtremeConeSubRegionIdx();
//        marginalConeSRIdxList = calcMarginalConeSubRegionIdx();
        List<double[]> uniformDirections = UniformWeightUtils.generateArrayList(arrayH, integratedTaus, nObj);

        generateConeSubRegionList(uniformDirections);
    }


    public void generateConeSubRegionList(List<double[]> predefinedDirections) {
        coneSubRegions = new ArrayList<>();

        for (int i=0;i<predefinedDirections.size();i++) {
            ConeSubRegion coneSR = new ConeSubRegion();
            coneSR.setRefDirection(predefinedDirections.get(i));
            coneSR.setIdxConeSubRegion(i);
            coneSubRegions.add(coneSR);
        }

        kdTree =  KDTree.build(predefinedDirections);

        extremeConeSRIdxList = calcExtremeConeSubRegionIdx();
        marginalConeSRIdxList = calcMarginalConeSubRegionIdx();

    }

    public void reconstructConeSubRegionList(List<double[]> refDirections) {
        generateConeSubRegionList(refDirections);
    }

    /**
     * recalculate neighborhoods
     */
    public void recalculateSubRegionsNeighbors(int neighborhoodSize) {
        initializingSubRegionsNeighbors(neighborhoodSize);
    }

        //generate subproblems
    protected ArrayList<ConeSubRegion> genConeSubRegions(int _H, double _tau, int _startIndex) {
        int[][] mIndexes = UniformWeightUtils.generateMIndexesArray(_H, nObj);

        ArrayList<ConeSubRegion> coneSubRegions = new ArrayList<>(mIndexes.length);
        for (int[] mIndex : mIndexes) {
            ConeSubRegion coneSR = new ConeSubRegion();
            coneSR.setRefDirection(_H, mIndex, _tau);
            coneSR.setIdxConeSubRegion(_startIndex++);
            coneSubRegions.add(coneSR);
        }
        return coneSubRegions;
    }

    public ConeSubRegion getConeSubRegion(int _indexConeSR) {
        return coneSubRegions.get(_indexConeSR);
    }

    public void setOriginalSubRegionsNum(int num){originalSubRegionsNum = num;}
    public int getOriginalSubRegionsNum(){return originalSubRegionsNum;}
    public int getConeSubRegionsNum() {
        return coneSubRegions.size();
    }

    protected ArrayList<Integer> genExtremeConeSubRegionIdx(int _H, int _startIndex) {
        ArrayList<Integer> idxList = new ArrayList<>();

        idxList.ensureCapacity(nObj);
        int[] mIndex = new int[nObj];
        for (int i = 0; i < nObj; ++i) mIndex[i] = 0;
        for (int i = 0; i < nObj; ++i) {
            mIndex[i] = _H;
            idxList.add(_startIndex + calIndexFromMIndex(_H, nObj, mIndex));
            mIndex[i] = 0;
        }
        return idxList;
    }

    protected ArrayList<Integer> genMarginalConeSubRegionIdx(List<ConeSubRegion> coneSubRegions) {
        ArrayList<Integer> marginalConeSRIdxList = new ArrayList<>();
        for (int i = 0; i < coneSubRegions.size(); ++i) {
            if (coneSubRegions.get(i).isBoundaryMarginalConeSubRegion())
                marginalConeSRIdxList.add(coneSubRegions.get(i).getIdxConeSubRegion());
        }
        return marginalConeSRIdxList;
    }

    protected ArrayList<Integer> calcExtremeConeSubRegionIdx() {
        ArrayList<Integer> idxList = new ArrayList<>(nObj);
        double[] v = new double[nObj];
        for (int i=0;i<nObj;i++)
            v[i] = 0.0;
        for (int i=0;i<nObj;i++){
            v[i] = 1.0;
            int index = kdTree.queryIndex(v);
            getConeSubRegion(index).setExtremeConeSubRegion();
            idxList.add(index);
            v[i] = 0.0;
        }
        return idxList;
    }

    protected ArrayList<Integer> calcMarginalConeSubRegionIdx() {
        ArrayList<Integer> idxList = new ArrayList<>(nObj);
        for (int i=0;i<getConeSubRegionsNum();i++){
            double[] refDirection = getConeSubRegion(i).getRefDirection();
            for (int j = 0;j<nObj;j++){
                if(refDirection[j] < Constant.TOLERATION){
                    idxList.add(i);
                    getConeSubRegion(i).setBoundaryMarginalConeSubRegion();
                    break;
                }
            }
        }
        return idxList;
    }

    public ArrayList<Integer> getExtremeConeSubRegionIdxList() {
        return extremeConeSRIdxList;
    }

    public ArrayList<Integer> getMarginalSubRegionIdxList() {
        return marginalConeSRIdxList;
    }

    /**
     * Initialize neighborhoods
     */
    public void initializingSubRegionsNeighbors(int neighborhoodSize) {
//        double[] x = new double[getConeSubRegionsNum()];
//        int[] idx = new int[getConeSubRegionsNum()];
//
//        for (int i = 0; i < getConeSubRegionsNum(); i++) {
//            // calculate the distances based on central reference observation vector
//            for (int j = 0; j < getConeSubRegionsNum(); j++) {
//                x[j] = MOEACDUtils.distance2(getConeSubRegion(i).getRefObservation(),getConeSubRegion(j).getRefObservation());
//                idx[j] = j;
//            }
//
//            // find 'neighborhoodSize' nearest neighboring subregions
//            MOEADUtils.minFastSort(x, idx, getConeSubRegionsNum(), neighborhoodSize);
//            for(int k=0;k<neighborhoodSize;++k)
//                getConeSubRegion(i).addNeighbor(idx[k]);
//        }
        for(int i=0;i<getConeSubRegionsNum();i++){
            ConeSubRegion subRegion = getConeSubRegion(i);
            List<Integer> neighbors = kdTree.queryKNearestIndexes(subRegion.getRefDirection(),neighborhoodSize);
            subRegion.setNeighbors(neighbors);
        }
    }

    public void initializingSubRegionsNeighbors() {
        ArrayList<Set<Integer>> srNeighbors = new ArrayList();
        //init neighbors' indexes for each subregion in its belonging layer
        for (int i = 0; i < arrayH.length; ++i) {
            int c = 0;
            for (int j = startIdxLayers[i]; c < numsOfLayers[i]; j++, c++) {
                Set<Integer> neighbors;
                if(nObj == 2)
                    neighbors = calcNeighbors(arrayH[i], coneSubRegions.get(j).getmIndex(),startIdxLayers[i],j,5);
                else if(nObj == 3)
                    neighbors = calcNeighbors(arrayH[i], coneSubRegions.get(j).getmIndex(),startIdxLayers[i],j,2);
                else
                    neighbors = calcNeighbors(arrayH[i], coneSubRegions.get(j).getmIndex(),startIdxLayers[i],j,1);

                srNeighbors.add(neighbors);
            }
        }

        double[] x = new double[getConeSubRegionsNum()];
        int[] idx = new int[getConeSubRegionsNum()];

        for (int i = 0; i < arrayH.length; ++i) {
            int c = 0;
            for (int j = startIdxLayers[i]; c < numsOfLayers[i]; j++, c++) {
                ConeSubRegion subregion = coneSubRegions.get(j);
                int maxNeighborSize = (int)(srNeighbors.get(j).size())+nObj;//(int)(srNeighbors.get(j).size()*1.5);
                Set<Integer> candidate = new HashSet<>();
                candidate.addAll(srNeighbors.get(j));
                for (int k = 0; k < arrayH.length; ++k) {
                    if (k == i) continue;
                    int idxSR = indexing(i,arrayH[k],integratedTaus[k], subregion.getRefDirection());
                    candidate.add(idxSR);
                    candidate.addAll(srNeighbors.get(idxSR));
                }
                if(candidate.size() <= maxNeighborSize){
                    subregion.setNeighbors(candidate);
                }else {
                    Iterator it = candidate.iterator();
                    int p = 0;
                    while (it.hasNext()) {
                        idx[p] = (int) it.next();
                        x[p] = MOEACDUtils.distance2(getConeSubRegion(idx[p]).getRefDirection(), subregion.getRefDirection());
                        p++;
                    }
                    MOEADUtils.minFastSort(x, idx, p, maxNeighborSize);
                    for (int k = 0; k < maxNeighborSize ; ++k)
                        subregion.addNeighbor(idx[k]);
                }
            }
        }
    }

    private Set<Integer> calcNeighbors(int _H, int[] mIndex,int startIdx) {
        Set<Integer> neighbors = new HashSet<>();
        int[] tmpIndex = new int[nObj];
        for (int i = 0; i < nObj; ++i) tmpIndex[i] = mIndex[i];
        for (int i = 0; i < nObj; ++i) {
            tmpIndex[i]++;
            if (tmpIndex[i] <= _H) {
                for (int j = i + 1; j < nObj; ++j) {
                    tmpIndex[j]--;
                    if (tmpIndex[j] >= 0) {
                        neighbors.add(calIndexFromMIndex(_H, nObj, tmpIndex));
                    }
                    tmpIndex[j]++;
                }
            }
            tmpIndex[i]--;
            tmpIndex[i]--;
            if (tmpIndex[i] >= 0) {
                for (int j = i + 1; j < nObj; ++j) {
                    tmpIndex[j]++;
                    if (tmpIndex[j] <= _H) {
                        neighbors.add(calIndexFromMIndex(_H, nObj, tmpIndex));
                    }
                    tmpIndex[j]--;
                }
            }
            tmpIndex[i]++;
        }
        Set<Integer> result = new HashSet<>();
        Iterator it = neighbors.iterator();
        while(it.hasNext()){
            result.add(startIdx+(int)it.next());
        }
        return result;
    }


    private Set<Integer> calcNeighbors(int _H, int[] mIndex,int startIdx,int idx,int range) {
        Set<Integer> neighbors = calcNeighbors(_H, mIndex,startIdx);
        if(range>1){
            neighbors.add(idx);
            Set<Integer> cLayerNeighbors = neighbors;
            for(int i=1;i<range;++i){
                Set<Integer> tmp = new HashSet<>();
                Iterator it = cLayerNeighbors.iterator();
                while(it.hasNext()){
                    tmp.addAll(calcNeighbors(_H,coneSubRegions.get((int)it.next()).getmIndex(),startIdx));
                }
                Iterator itTmp = tmp.iterator();
                Set<Integer> nLayerNeighbors = new HashSet<>();
                while(itTmp.hasNext()){
                    int idxTmp = (Integer) itTmp.next();
                    if(!neighbors.contains(idxTmp)) {
                        nLayerNeighbors.add(idxTmp);
                        neighbors.add(idxTmp);
                    }
                }
                cLayerNeighbors = nLayerNeighbors;
            }
            neighbors.remove(idx);
        }
        return neighbors;
    }

    public int indexing(double[] _normObjective) {

        return indexing(_normObjective,kdTree);
    }
    public int indexing(double[] _normObjective,KDTree kdTree) {

        double[] observationV = Utils.calObservation(_normObjective);

        int index = kdTree.queryIndex(observationV);
        return index;
    }

    public ConeSubRegion locateSubRegion(double[] _normObjective) {
        return getConeSubRegion(indexing(_normObjective));
    }


    protected int indexing(int layer,int _H, double _tau, double[] observationV) {
        if(Math.abs(_tau - 1.0) > Constant.TOLERATION)
        {
            double[] decodedObservationV = MOEACDUtils.decode(_tau, observationV);
            double lowest = Double.POSITIVE_INFINITY;
            int idxLowest = -1;
            for (int i = 0; i < decodedObservationV.length; ++i) {
                if (observationV[i] < lowest) {
                    lowest = decodedObservationV[i];
                    idxLowest = i;
                }
            }

            if (lowest < 0) {//out of the bound of inner layer
                int selectedIdx = -1;
                double selectedDist = Double.POSITIVE_INFINITY;
                int c = 0;
                for (int i = startIdxLayers[layer]; c < numsOfLayers[layer]; ++i, ++c) {
                    ConeSubRegion subRegion = getConeSubRegion(i);
                    if(subRegion.isMarginalConeSubRegion()) {
                        double tmpDist = MOEACDUtils.distance2(subRegion.getRefDirection(), observationV);
                        if (tmpDist < selectedDist)
                            selectedIdx = i;
                    }
                }
                return selectedIdx ;
            }
            observationV = decodedObservationV;
        }
        double[] hPlus = convert2IndexLike(_H, observationV);
        int[] mIndex = nearestMIndex(_H,hPlus);
        return startIdxLayers[layer] + calIndexFromMIndex(_H, observationV.length,mIndex );
    }

    //convert m-1 dimensional index to m-dimensional index
    public int[] m_1Index2MIndex(int _H, int _nObj, int[] _m_1Index) {
        int[] mIndex = new int[_nObj];
        mIndex[_nObj - 1] = _H - _m_1Index[_nObj - 2];
        for (int i = _nObj - 2; i > 0; --i)
            mIndex[i] = _m_1Index[i] - _m_1Index[i - 1];
        mIndex[0] = _m_1Index[0];
        return mIndex;
    }

    //convert m-dimensional index to m-1 dimensional index
    protected int[] mIndex2M_1Index(int _H, int _nObj, int[] _mIndex) {
        int[] m_1Index = new int[_nObj - 1];
        m_1Index[_nObj - 2] = _H - _mIndex[_nObj - 1];
        for (int i = _nObj - 2; i > 0; --i)
            m_1Index[i - 1] = m_1Index[i] - _mIndex[i];

        return m_1Index;
    }

    //calculate the index of subproblem in the certain decompositional level
    protected int calIndexFromMIndex(int _H, int _nObj, int[] _mIndex) {
        int[] m_1Index = mIndex2M_1Index(_H, _nObj, _mIndex);

        int index = 0;
        for (int i = 0; i < _nObj - 1; i++)
            index += CombinationUtils.compute(m_1Index[i] + i, i + 1);
        return index;
    }

    //convert  the normalied objectives into a index like vector
    protected double[] convert2IndexLike(int _H, double[] _Observation) {
        int nObj = _Observation.length;
        double[] hPlus = new double[nObj];
        for (int i = 0; i < nObj; ++i) {
            hPlus[i] = _Observation[i] * _H;
        }
        return hPlus;
    }

    //calculate the nearest m-dimensional index of the normalized objectives
    protected int[] nearestMIndex(int _H,double[] _hPlus) {
        int nObj = _hPlus.length;
        int[] mIndex = new int[nObj];

        //(1)initialization
        boolean[] sign = new boolean[nObj];
        for (int i = 0; i < nObj; ++i)
            sign[i] = false;

        int b = nObj;
        int minIdx = 0;
        double minAbsDif;
        double fixDif;
        double[] dif = new double[nObj];
        double tmpDif;
        double tmpAbsDif;
        double shareFixDif;
        do {
            //(2)find the nearest pos
            minAbsDif = Double.POSITIVE_INFINITY;
            tmpAbsDif = Double.POSITIVE_INFINITY;
            fixDif = Double.POSITIVE_INFINITY;
            minIdx = -1;
            for (int i = 0; i < nObj; ++i) {
                if (!sign[i]) {
                    tmpDif = Math.round(_hPlus[i]) - _hPlus[i];
                    tmpAbsDif = Math.abs(tmpDif);
                    if (tmpAbsDif < minAbsDif) {
                        minAbsDif = tmpAbsDif;
                        fixDif = tmpDif;
                        minIdx = i;
                    }
                }
            }
            //replacement the old vector with the new one
            //(3)
            mIndex[minIdx] = (int) Math.round(_hPlus[minIdx]);
            sign[minIdx] = true;
            b--;
            //(4)
            shareFixDif = fixDif / b;
            for (int i = 0; i < nObj; ++i)
                if (!sign[i])
                    _hPlus[i] -= shareFixDif;

        } while (b > 1);

        int lastIdx = 0;
        int leftV = _H;
        for (int i = 0; i < nObj; ++i) {
            if (!sign[i])
                lastIdx = i;
            else
                leftV -= mIndex[i];
        }
        mIndex[lastIdx] = leftV;

        return mIndex;
    }

    public static void main(String[] argc){
        int[] arrayH = new int[1];
        arrayH[0] = 12;
        double[] tau = new double[1];
        tau[0] = 1.0;
        ConeSubRegionManager coneSubRegionManager = new ConeSubRegionManager(3,arrayH,tau);
        coneSubRegionManager.generateConeSubRegionList();
        coneSubRegionManager.initializingSubRegionsNeighbors(20);
        for(int i=0;i<coneSubRegionManager.getConeSubRegionsNum();++i){
            int[] mIndex = coneSubRegionManager.getConeSubRegion(i).getmIndex();
            double[] refObservation = coneSubRegionManager.getConeSubRegion(i).getRefDirection();
            String outputStr = "["+i+"] ("+coneSubRegionManager.getConeSubRegion(i).getIdxConeSubRegion()+")<";
            for(int j=0;j<mIndex.length;++j)
                outputStr += mIndex[j]+",";
            outputStr+="> <";
            for (int j = 0;j<refObservation.length;++j)
                outputStr += refObservation[j]+",";
            outputStr += "> \nNei:  ";
//            List<Integer> neighbors = coneSubRegionManager.getConeSubRegion(i).getNeighbors();
//            for(int j=0;j<neighbors.size();++j)
//                outputStr += neighbors.get(j)+"\t";
            JMetalLogger.logger.info(outputStr);
        }
    }
}