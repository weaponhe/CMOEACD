package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.util.*;
import org.uma.jmetal.util.front.imp.ArrayFront;

import java.util.*;

/**
 * Created by X250 on 2016/6/9.
 */
public class ConeSubRegionManagerII extends ConeSubRegionManager {

    UniformSimplexCentroidWeightsUtils uniformWeightsManager;
    public ConeSubRegionManagerII(int _nObj, int _H) {
        this.nObj = _nObj;
        this.arrayH = new int[1];
        this.arrayH[0] = _H;

        coneSubRegions = new ArrayList<>();
        uniformWeightsManager = new UniformSimplexCentroidWeightsUtils(this.nObj,this.arrayH[0]);
    }

    public void generateConeSubRegionList() {
        ArrayList<double[]> uniformWeights = uniformWeightsManager.getUniformWeights();
        kdTree =  KDTree.build(uniformWeights);

        for (int i=0;i<uniformWeights.size();++i) {
            ConeSubRegion coneSR = new ConeSubRegion();
            coneSR.setRefDirection(uniformWeights.get(i));
//            coneSR.setAdaptiveD(1.0*(nObj -1)/(2.0*nObj*arrayH[0]));
            coneSR.setIdxConeSubRegion(i);
            coneSubRegions.add(coneSR);
        }
        extremeConeSRIdxList = uniformWeightsManager.getExtremeIdxes();
        for(int i =0;i<extremeConeSRIdxList.size();++i)
            getConeSubRegion(extremeConeSRIdxList.get(i)).setExtremeConeSubRegion();
        marginalConeSRIdxList = uniformWeightsManager.getMarginalIdxes();
        for(int i=0;i<marginalConeSRIdxList.size();++i){
            getConeSubRegion(marginalConeSRIdxList.get(i)).setBoundaryMarginalConeSubRegion();
            getConeSubRegion(marginalConeSRIdxList.get(i)).setMarginalConeSubRegion();
        }
    }

    public void initializingSubRegionsNeighbors() {
        ArrayList<ArrayList<Integer>> innerNeighbors = uniformWeightsManager.getInnerNeighbors();
        ArrayList<ArrayList<Integer>> outerNeighbors = uniformWeightsManager.getOuterNeighbors();
        for(int i=0;i<getConeSubRegionsNum();i++){
            ConeSubRegion coneSubRegion = getConeSubRegion(i);

            coneSubRegion.setNeighbors(innerNeighbors.get(i));

            coneSubRegion.addNeighbors(outerNeighbors.get(i));
        }

        uniformWeightsManager.free();
    }


}
