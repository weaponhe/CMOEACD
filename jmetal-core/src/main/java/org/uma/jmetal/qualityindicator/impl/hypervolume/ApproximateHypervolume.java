package org.uma.jmetal.qualityindicator.impl.hypervolume;

import org.uma.jmetal.qualityindicator.impl.Hypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeVersion;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.HypervolumeContributionComparator;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.solutionattribute.impl.HypervolumeContributionAttribute;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2016/6/2.
 */
public class ApproximateHypervolume<S extends Solution<?>> extends Hypervolume<S> {

    private Point referencePoint;
    private int numberOfObjectives;

    private static final double DEFAULT_OFFSET = 100.0;
    private double offset = DEFAULT_OFFSET;
    private boolean maximizing;
    JMetalRandom randomGenerator;

    private List<Point> samplePoints;
    Point boxPoint = null;

    /**
     * Default constructor
     */
    public ApproximateHypervolume() {
        setMiniming();
        setDescription("Approximate hypervolume quality indicator");
        randomGenerator = JMetalRandom.getInstance();
    }


    //modified by Yuehong Xie
    public void setMaximizing() {
        maximizing = true;
    }

    public void setMiniming() {
        maximizing = false;
    }


    boolean dominatesValue(double v1, double v2) {
        if (maximizing) {
            return v1 > v2;
        } else
            return v2 > v1;
    }

    public void setSamplePoints(List<Point> samplePoints,Point referencePoint, Point boundPoint){
        this.samplePoints = samplePoints;
        numberOfObjectives = referencePoint.getNumberOfDimensions();
        boxPoint = new ArrayPoint(numberOfObjectives);
        for(int i=0;i<numberOfObjectives;++i){
            double tmp = boundPoint.getDimensionValue(i) - referencePoint.getDimensionValue(i);
            if(!maximizing)
                tmp = -tmp;
            boxPoint.setDimensionValue(i,tmp < 0 ? 0 : tmp);
        }
    }

    public void setSamplePoints(int numOfSample,Point referencePoint, Point boundPoint){
        numberOfObjectives = referencePoint.getNumberOfDimensions();
        boxPoint = new ArrayPoint(numberOfObjectives);
//        String boxStr = "boxPoint : ";
        for(int i=0;i<numberOfObjectives;++i){
            double tmp = boundPoint.getDimensionValue(i) - referencePoint.getDimensionValue(i);
            if(!maximizing)
                tmp = -tmp;
            boxPoint.setDimensionValue(i,tmp < 0 ? 0 : tmp);
//            boxStr += (boxPoint.getDimensionValue(i)+",");
        }
//        JMetalLogger.logger.info(boxStr);
        samplePoints = new ArrayList<>(numOfSample);
        for(int i=0;i<numOfSample;++i){
            Point point = new ArrayPoint(numberOfObjectives);
            for(int j=0;j<numberOfObjectives;j++)
                point.setDimensionValue(j,randomGenerator.nextDouble(0.0,boxPoint.getDimensionValue(j)));
            samplePoints.add(point);
        }
    }

    public List<Point> getSamplePoints(){
        return samplePoints;
    }

    boolean dominateOrEqual(Point p, Point q) {
        for (int i = p.getNumberOfDimensions() - 1; i >= 0; i--) {
            if (p.getDimensionValue(i)> q.getDimensionValue(i)) {
                for (int j = i - 1; j >= 0; j--) {
                    if (q.getDimensionValue(j)> p.getDimensionValue(j)) {
                        return false;
                    }
                }
                return true;
            } else if (q.getDimensionValue(i)> p.getDimensionValue(i)) {
               return false;
            }
        }
        return true;
    }

    private  ArrayFront transformAndFliter(List<S> solutionList, Point referencePoint){
        numberOfObjectives = referencePoint.getNumberOfDimensions();
        ArrayFront solutionfront = new ArrayFront(solutionList.size(),numberOfObjectives);
        for(int i=0;i<solutionList.size();++i){
            Point point = new ArrayPoint(numberOfObjectives);
            for(int j=0;j<numberOfObjectives;++j){
                double tmp = solutionList.get(i).getObjective(j) - referencePoint.getDimensionValue(j);
                if(!maximizing)
                    tmp = -tmp;
                point.setDimensionValue(j,tmp < 0 ? 0 : tmp);
            }
            solutionfront.setPoint(i,point);
        }
        return solutionfront;
    }

    int getHVCount(ArrayFront front, List<Point> samplePoints){
        int numOfSample = samplePoints.size();
        int countInBound = 0;
        for(int i=0;i<numOfSample;++i){
            for(int j=0;j<front.getNumberOfPoints();++j) {
                if (dominateOrEqual(front.getPoint(j),samplePoints.get(i))){
//                    String outStr = "\nfront("+front.getPoint(j)+")\nsample("+samplePoints.get(i)+")\n";
//                    JMetalLogger.logger.info(outStr);
                    countInBound+=1;
                    break;
                }
            }
        }
        return countInBound;
    }


    public double[] hypeFitness(ArrayFront front, List<Point> samplePoints, final int k) {
        int nrOfSamples = samplePoints.size();
        int nrOfPoints = front.getNumberOfPoints();
        int dim = front.getPointDimensions();	// Number of objectives

        // compute alpha factor of HypE fitness:
        double[] alpha = new double[nrOfPoints];
        for (int i = 1; i<=k; i++) {
            double alphai = 1;
            for (int l = 1; l <= i-1; l++) {
                alphai = alphai * (k-l) / (nrOfPoints - l );
            }
            alpha[i-1] = alphai / i;
        }

        double[] f = new double[nrOfPoints];

        // compute amount of dominators in front for each sample:
        int[] dominated = new int[nrOfSamples];
        for (int s=0; s<samplePoints.size(); s++) {
            // compute amount of dominators in front for each sample:
            for (int j = 0; j<nrOfPoints; j++) {
                if(dominateOrEqual(front.getPoint(j),samplePoints.get(s))){
                    dominated[s] = dominated[s] + 1;
                }
            }
        }

        for (int s=0; s<samplePoints.size(); s++) {
            // sum up alpha values of each dominated sample:
            for (int j = 0; j<nrOfPoints; j++) {
                if(dominateOrEqual(front.getPoint(j),samplePoints.get(s))){
                    f[j] = f[j] + alpha[dominated[s]-1];
                }
            }
        }

        return f;
    }

    @Override
    public Double evaluate(List<S> solutionList) {
        ArrayFront solutionfront = transformAndFliter(solutionList,referencePoint);
        return evaluate(solutionfront);
    }

    public Double evaluate(ArrayFront front) {
        numberOfObjectives = front.getPointDimensions();
        int inNum =  getHVCount(front,samplePoints);
////        if(inNum==samplePoints.size())
////            JMetalLogger.logger.info("all in");

        double hv = inNum;
        hv /= samplePoints.size();
        for(int i=0;i<numberOfObjectives;++i){
            hv *= boxPoint.getDimensionValue(i);
        }
//        double[] f = hypeFitness(front,samplePoints,1);
//        double hv = 0;//
//        for (int i=0;i<f.length;i++)
//            hv += f[i];
//        hv /= samplePoints.size();
//        for(int i=0;i<numberOfObjectives;++i){
//            hv *= boxPoint.getDimensionValue(i);
//        }
        return hv;
    }

    public Double evaluate(ArrayFront front, Point referencePoint) {
        for(int i=0;i<front.getNumberOfPoints();++i){
            Point point = front.getPoint(i);
            for(int j=0;j<front.getPointDimensions();++j){
                point.setDimensionValue(j, referencePoint.getDimensionValue(j) - point.getDimensionValue(j));
                if(maximizing)
                    point.setDimensionValue(j, - point.getDimensionValue(j));
                if(point.getDimensionValue(j) < 0.0)
                    point.setDimensionValue(j,0.0);
            }
            front.setPoint(i,point);
        }
        return evaluate(front);
    }

    public double computeHypervolume(List<S> solutionList) {
        ArrayFront solutionfront = transformAndFliter(solutionList,referencePoint);
        return evaluate(solutionfront);
    }

    /**
     * Updates the reference point
     */
    private void updateReferencePoint(List<? extends Solution<?>> solutionList) {
        double[] maxObjectives = new double[numberOfObjectives];
        for (int i = 0; i < numberOfObjectives; i++) {
            maxObjectives[i] = 0;
        }

        for (int i = 0; i < solutionList.size(); i++) {
            for (int j = 0; j < numberOfObjectives; j++) {
                if ((!maximizing && maxObjectives[j] < solutionList.get(i).getObjective(j)) || (maximizing && maxObjectives[j] > solutionList.get(i).getObjective(j))) {
                    maxObjectives[j] = solutionList.get(i).getObjective(j);
                }
            }
        }

        if (referencePoint == null) {
            referencePoint = new ArrayPoint(numberOfObjectives);
            for (int i = 0; i < numberOfObjectives; i++) {
                referencePoint.setDimensionValue(i, Double.MAX_VALUE);
            }
        }

        for (int i = 0; i < referencePoint.getNumberOfDimensions(); i++) {
            referencePoint.setDimensionValue(i, maximizing ? maxObjectives[i] - offset : maxObjectives[i] + offset);
        }
    }

    /**
     * Updates the reference point
     */
    private void updateReferencePoint(Front front) {
        double[] maxObjectives = new double[numberOfObjectives];
        for (int i = 0; i < numberOfObjectives; i++) {
            maxObjectives[i] = 0;
        }

        for (int i = 0; i < front.getNumberOfPoints(); i++) {
            for (int j = 0; j < numberOfObjectives; j++) {
                if ((!maximizing && maxObjectives[j] < front.getPoint(i).getDimensionValue(j)) || (maximizing && maxObjectives[j] > front.getPoint(i).getDimensionValue(j))) {
                    maxObjectives[j] = front.getPoint(i).getDimensionValue(j);
                }
            }
        }

        for (int i = 0; i < referencePoint.getNumberOfDimensions(); i++) {
            referencePoint.setDimensionValue(i, maximizing ? maxObjectives[i] - offset : maxObjectives[i] + offset);
        }
    }

    @Override
    public List<S> computeHypervolumeContribution(List<S> solutionList, List<S> referenceFrontList) {

        numberOfObjectives = solutionList.get(0).getNumberOfObjectives();
        updateReferencePoint(referenceFrontList);

        if (solutionList.size() > 1) {
            double[] contributions = new double[solutionList.size()];
            double solutionSetHV = 0;

            solutionSetHV = evaluate(solutionList);

            for (int i = 0; i < solutionList.size(); i++) {
                S currentPoint = solutionList.get(i);
                solutionList.remove(i);

                contributions[i] = solutionSetHV - evaluate(solutionList);

                solutionList.add(i, currentPoint);
            }

            HypervolumeContributionAttribute<Solution<?>> hvContribution = new HypervolumeContributionAttribute<Solution<?>>();
            for (int i = 0; i < solutionList.size(); i++) {
                hvContribution.setAttribute(solutionList.get(i), contributions[i]);
            }

            Collections.sort(solutionList, new HypervolumeContributionComparator<S>());
        }

        return solutionList;
    }

    @Override
    public double getOffset() {
        return offset;
    }

    @Override
    public void setOffset(double offset) {
        this.offset = offset;
    }

    public void setReferencePoint(Point refPoint) {
        referencePoint = refPoint;
    }

}
