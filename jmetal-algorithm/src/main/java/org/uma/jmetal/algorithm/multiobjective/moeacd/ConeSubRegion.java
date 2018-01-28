package org.uma.jmetal.algorithm.multiobjective.moeacd;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.Constant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by X250 on 2016/4/22.
 */
public class ConeSubRegion {
    //Reference Observation Vector, direction of the cone sub region
    double[] refDirection;
    double[] normalizedWeights;

    //m-dimensional index
    int[] mIndex;
    //the index in all cone subregions
    int idxConeSR;
    //the index of individual in the whole population
    int idxSolution;
    //
    List<Integer> subPopulation;
    List<Integer> idxMultiSolutions;
    //the index of individual in the archieves
    int idxArchive;
    //the index of optimum without considering constraints
    int idxOptimum;
    //the set of index of neighbors
    List<Integer> neighbors;
    //extreme subregion
    boolean isExtremeConeSR = false;
    //marginal subregion in each layer
    boolean isMarginalConeSR = false;
    //boundary marginal subregion
    boolean isBoundaryMarginalConeSR = false;

    public void setSubPopulation(int size) {
        this.subPopulation = new ArrayList<>(5);
        for (int i = 0; i < size; i++) {
            this.subPopulation.add(-1);
        }
    }

    public List<Integer> getSubPopulation() {
        return this.subPopulation;
    }

    public void setRefDirection(int _H, int[] _mIndex, double _tau) {
        int nobj = _mIndex.length;
        this.mIndex = _mIndex;
        this.refDirection = new double[nobj];

        for (int i = 0; i < nobj; ++i) {
            refDirection[i] = 1.0 * mIndex[i] / _H;
            if (refDirection[i] < Constant.TOLERATION)
                refDirection[i] = Constant.TOLERATION;
            if (mIndex[i] == 0)
                isMarginalConeSR = true;
        }

        if (Math.abs(_tau - 1.0) > Constant.TOLERATION) {//inner layer
            refDirection = MOEACDUtils.encode(_tau, refDirection);
        } else
            isBoundaryMarginalConeSR = isMarginalConeSR;//boundary layer

        neighbors = new ArrayList<>();
    }

    public void setRefDirection(double[] direction) {
        this.refDirection = direction;
        int nobj = direction.length;


        for (int i = 0; i < nobj; i++) {
            if (refDirection[i] <= Constant.TOLERATION) {
                isMarginalConeSR = true;
                isBoundaryMarginalConeSR = true;
                break;
            }
        }

        neighbors = new ArrayList<>();
    }

    public double[] getRefDirection() {
        return refDirection;
    }

    public double getRefDirection(int _idx) {
        return refDirection[_idx];
    }

    public void setNormalizedWeights(double[] normalizedWeights) {
        this.normalizedWeights = normalizedWeights;
    }

    public double[] getNormalizedWeights() {
        return this.normalizedWeights;
    }

    public void setIdxConeSubRegion(int _idxSR) {
        this.idxConeSR = _idxSR;
    }

    public int getIdxConeSubRegion() {
        return idxConeSR;
    }

    public void setIdxSolution(int _idxSolution) {
        this.idxSolution = _idxSolution;
    }

    public int getIdxSolution() {
        return idxSolution;
    }

    public void addIdxMultiSolutions(int idx) {
        idxMultiSolutions.add(idx);
    }

    public void setIdxMultiSolutions(List<Integer> idxes) {
        idxMultiSolutions = idxes;
    }

    public List<Integer> getIdxMultiSolutions() {
        return idxMultiSolutions;
    }

    public void setIdxArchive(int _idxArchive) {
        this.idxArchive = _idxArchive;
    }

    public int getIdxArchive() {
        return idxArchive;
    }

    public void setIdxOptimum(int idxOptimum) {
        this.idxOptimum = idxOptimum;
    }

    public int getIdxOptimum() {
        return this.idxOptimum;
    }

    public int[] getmIndex() {
        return this.mIndex;
    }

    public void setNeighbors(Set<Integer> neighbors) {
        addNeighbors(neighbors);
    }

    public void setNeighbors(List<Integer> neighbors) {
        this.neighbors = neighbors;
    }

    public void addNeighbor(int idxSop) {
        int i;
        for (i = 0; i < neighbors.size(); i++) {
            if (neighbors.get(i) == idxSop)
                break;
        }
        if (i == neighbors.size())
            neighbors.add(idxSop);
    }

    public void addNeighbors(Set<Integer> neighbors) {
        Iterator it = neighbors.iterator();
        while (it.hasNext()) {
            addNeighbor((int) it.next());
        }
    }

    public void addNeighbors(ArrayList<Integer> neighbors) {
        this.neighbors.addAll(neighbors);
    }

    public List<Integer> getNeighbors() {
        return this.neighbors;
    }

    public void setExtremeConeSubRegion() {
        this.isExtremeConeSR = true;
    }

    public boolean isExtremeConeSubRegion() {
        return isExtremeConeSR;
    }

    public void setMarginalConeSubRegion() {
        this.isMarginalConeSR = true;
    }

    public boolean isMarginalConeSubRegion() {
        return isMarginalConeSR;
    }

    public void setBoundaryMarginalConeSubRegion() {
        this.isBoundaryMarginalConeSR = true;
    }

    public boolean isBoundaryMarginalConeSubRegion() {
        return isBoundaryMarginalConeSR;
    }
}
