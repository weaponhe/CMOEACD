package org.uma.jmetal.algorithm.multiobjective.udea;

import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class SubRegion {
    //reference direction vector
    private double[] direction;
    //the index in all sub-regions
    private int idxSR;
    //associated solution
    private DoubleSolution solution;
    //associated archive
    private DoubleSolution archive;
    //associated optimum
    private DoubleSolution optimum;
    //associated solution set
    private List<DoubleSolution> solutionSet;
    //associated archive set
    private List<DoubleSolution> archiveSet;
    //associated optimum set
    private List<DoubleSolution> optimumSet;
    //the index of neighbor sub-regions
    private List<Integer> neighbors;

    public SubRegion(){
        direction = null;
        idxSR = -1;
        solution = null;
        archive = null;
        optimum = null;
        solutionSet = null;
        archiveSet = null;
        optimumSet = null;
        neighbors = null;
    }

    public SubRegion(double[] direction,int idxSR){
        this();
        this.direction = direction;
    }

    public void setDirection(double[] direction){
        this.direction =  direction;
    }

    public double[] getDirection(){
        return this.direction;
    }

    public void setIndex(int idxSR){
        this.idxSR = idxSR;
    }

    public int getIndex(){
        return idxSR;
    }

    public void setSolution(DoubleSolution solution){
        this.solution = solution;
    }

    public DoubleSolution getSolution(){
        return solution;
    }

    public void setSolutionSet(List<DoubleSolution> solutionSet){
        this.solutionSet = solutionSet;
    }

    public void addSolution(DoubleSolution solution){
        if(this.solutionSet ==null){
            this.solutionSet = new ArrayList<>();
        }
        this.solutionSet.add(solution);
    }

    public List<DoubleSolution> getSolutionSet(){
        return this.solutionSet;
    }

    public DoubleSolution getSolution(int idx){
        if(this.solutionSet == null || idx >= this.solutionSet.size()){
            return null;
        }
        return this.solutionSet.get(idx);
    }

    public void setArchive(DoubleSolution archive){
        this.archive = archive;
    }

    public DoubleSolution getArchive(){
        return archive;
    }

    public void setArchiveSet(List<DoubleSolution> archiveSet){
        this.archiveSet = archiveSet;
    }

    public void addArchive(DoubleSolution archive){
        if(this.archiveSet ==null){
            this.archiveSet = new ArrayList<>();
        }
        this.archiveSet.add(archive);
    }

    public List<DoubleSolution> getArchiveSet(){
        return this.archiveSet;
    }

    public DoubleSolution getArchive(int idx){
        if(this.archiveSet == null || idx >= this.archiveSet.size()){
            return null;
        }
        return this.archiveSet.get(idx);
    }

    public void setOptimum(DoubleSolution optimum){
        this.optimum = optimum;
    }

    public DoubleSolution getOptimum(){
        return optimum;
    }

    public void setOptimumSet(List<DoubleSolution> optimumSet){
        this.optimumSet = optimumSet;
    }

    public void addOptimum(DoubleSolution optimum){
        if(this.optimumSet ==null){
            this.optimumSet = new ArrayList<>();
        }
        this.optimumSet.add(optimum);
    }

    public List<DoubleSolution> getOptimumSet(){
        return this.optimumSet;
    }

    public DoubleSolution getOptimum(int idx){
        if(this.optimumSet == null || idx >= this.optimumSet.size()){
            return null;
        }
        return this.optimumSet.get(idx);
    }


    public void setNeighbors(List<Integer> neighbors){
        this.neighbors = neighbors;
    }

    public void addNeighbor(int neighbor){
        if(this.neighbors ==null){
            this.neighbors = new ArrayList<>();
        }
        int i;
        for(i=0;i<neighbors.size();i++) {
            if (neighbors.get(i) == neighbor)
                break;
        }
        if(i == neighbors.size())
            neighbors.add(neighbor);
    }

    public void addNeighbors(List<Integer> neighbors){
        Iterator it = neighbors.iterator();
        while (it.hasNext()) {
            addNeighbor((int) it.next());
        }
    }

    public List<Integer> getNeighbors(){
        if(this.neighbors==null)
            this.neighbors = new ArrayList<>();
        return this.neighbors;
    }
}
