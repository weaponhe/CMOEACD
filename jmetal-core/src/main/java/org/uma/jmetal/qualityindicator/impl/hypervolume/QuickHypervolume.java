package org.uma.jmetal.qualityindicator.impl.hypervolume;

import org.uma.jmetal.qualityindicator.impl.Hypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.QuickHypervolumeVersion;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeVersion;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.HypervolumeContributionComparator;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.point.util.comparator.PointComparator;
import org.uma.jmetal.util.solutionattribute.impl.HypervolumeContributionAttribute;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by X250 on 2016/4/19.
 */
public class QuickHypervolume<S extends Solution<?>> extends Hypervolume<S> {

    private Point referencePoint;
    private int numberOfObjectives;

    private static final double DEFAULT_OFFSET = 100.0 ;
    private double offset = DEFAULT_OFFSET ;
    //modified by Yuehong Xie
    private boolean maximizing;

    /**
     * Default constructor
     */
    public QuickHypervolume() {
        setDescription("QHV implementation of the hypervolume quality indicator");
    }

    /**
     * Constructor
     *
     * @param referenceParetoFrontFile
     * @throws FileNotFoundException
     */
    public QuickHypervolume(String referenceParetoFrontFile) throws FileNotFoundException {
        super(referenceParetoFrontFile) ;
        setMiniming();
        numberOfObjectives = referenceParetoFront.getPointDimensions() ;
        referencePoint = null ;
        updateReferencePoint(referenceParetoFront);
        setDescription("QHV implementation of the hypervolume quality indicator");
    }

    /**
     * Constructor
     *
     * @param referenceParetoFront
     * @throws FileNotFoundException
     */
    public QuickHypervolume(Front referenceParetoFront) {
        super(referenceParetoFront) ;
        setMiniming();
        numberOfObjectives = referenceParetoFront.getPointDimensions() ;
        referencePoint = null ;
        updateReferencePoint(referenceParetoFront);
        setDescription("QHV implementation of the hypervolume quality indicator");
    }

    //modified by Yuehong Xie
    public void setMaximizing(){
        maximizing = true;
    }
    public void setMiniming(){
        maximizing = false;
    }

    private ArrayFront transformAndFliter(List<S> solutionList){
        return transformAndFliter(solutionList,referencePoint);
    }

    private  ArrayFront transformAndFliter(List<S> solutionList, Point referencePoint){
        List<Point> pointList = new ArrayList<Point>();
        numberOfObjectives = solutionList.get(0).getNumberOfObjectives();
//      Point invertedReferencePoint = new ArrayPoint(numberOfObjectives);
//      if(solutionList.size() > 0)
//        for (int i=0;i<numberOfObjectives;++i)
//          invertedReferencePoint.setDimensionValue(i,solutionList.get(0).getObjective(i));

        for (int i = 0; i < solutionList.size(); i++) {
//      updateInvertedRefPoint(invertedReferencePoint,solutionList.get(i));
            Point point = new ArrayPoint(numberOfObjectives);
            boolean inBound = false;
            for(int j=0;j<numberOfObjectives;++j){
                double tmp = solutionList.get(i).getObjective(j) - referencePoint.getDimensionValue(j);
                if(!maximizing)
                    tmp = -tmp;
                point.setDimensionValue(j,tmp < 0.0 ? 0.0 : tmp);
                if(point.getDimensionValue(j) > 0.0)
                    inBound = true;
            }
            if(inBound)
                pointList.add(point);
        }


        ArrayFront front = new ArrayFront(pointList.size(),numberOfObjectives);
        for(int i=0;i<pointList.size();++i){
//      Point normPoint = new ArrayPoint(numberOfObjectives);
//      for (int j=0;j<numberOfObjectives;++j){
//        normPoint.setDimensionValue(j,pointList.get(i).getDimensionValue(j)/intercept.getDimensionValue(j));
//      }
            front.setPoint(i,pointList.get(i));
        }
        return front;
    }

    @Override
    public Double evaluate(List<S> solutionList) {
        ArrayFront solutionfront = transformAndFliter(solutionList);
        return evaluate(solutionfront);

    }

    public Double evaluate(ArrayFront front) {
        double hv;
        if (front.getNumberOfPoints() == 0) {
            hv = 0.0;
        } else {
            numberOfObjectives = front.getPointDimensions();
            QuickHypervolumeVersion QHVComputor = new QuickHypervolumeVersion();
            hv = QHVComputor.getHV(front);
        }
        return hv;
    }

    public Double evaluate(ArrayFront front, Point referencePoint) {
        for(int i=0;i<front.getNumberOfPoints();++i){
            Point point = front.getPoint(i);
            for(int j=0;j<front.getPointDimensions();++j){
                point.setDimensionValue(j,point.getDimensionValue(j) - referencePoint.getDimensionValue(j));
                if(!maximizing)
                    point.setDimensionValue(j, -point.getDimensionValue(j));
                if(point.getDimensionValue(j) < 0)
                    point.setDimensionValue(j,0);
            }
            front.setPoint(i,point);
        }
        return evaluate(front);
    }

    public double computeHypervolume(List<S> solutionList, Point referencePoint) {
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
                if ((!maximizing && maxObjectives[j] < solutionList.get(i).getObjective(j))||(maximizing && maxObjectives[j] > solutionList.get(i).getObjective(j))) {
                    maxObjectives[j] = solutionList.get(i).getObjective(j) ;
                }
            }
        }

        if (referencePoint == null) {
            referencePoint = new ArrayPoint(numberOfObjectives) ;
            for (int i = 0; i < numberOfObjectives ; i++) {
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
                    maxObjectives[j] = front.getPoint(i).getDimensionValue(j) ;
                }
            }
        }

        for (int i = 0; i < referencePoint.getNumberOfDimensions(); i++) {
            referencePoint.setDimensionValue(i, maximizing ? maxObjectives[i] - offset : maxObjectives[i] + offset);
        }
    }

    @Override
    public List<S> computeHypervolumeContribution(List<S> solutionList, List<S> referenceFrontList) {

        numberOfObjectives = solutionList.get(0).getNumberOfObjectives() ;
        updateReferencePoint(referenceFrontList);

        if (solutionList.size() > 1) {
            double[] contributions = new double[solutionList.size()];
            double solutionSetHV = 0;

            solutionSetHV = evaluate(solutionList);

            for (int i = 0; i < solutionList.size(); i++) {
                S currentPoint = solutionList.get(i);
                solutionList.remove(i);

                contributions[i] = solutionSetHV - evaluate(solutionList) ;

                solutionList.add(i, currentPoint);
            }

            HypervolumeContributionAttribute<Solution<?>> hvContribution = new HypervolumeContributionAttribute<Solution<?>>();
            for (int i = 0; i < solutionList.size(); i++) {
                hvContribution.setAttribute(solutionList.get(i), contributions[i]);
            }

            Collections.sort(solutionList, new HypervolumeContributionComparator<S>());
        }

        return solutionList ;
    }

    @Override
    public double getOffset() {
        return offset;
    }

    @Override
    public void setOffset(double offset) {
        this.offset = offset ;
    }

//  @Override public String getDescription() {
//    return "WFG implementation of the hypervolume quality indicator" ;
//  }

    public void setReferencePoint(Point refPoint){referencePoint = refPoint;}

}