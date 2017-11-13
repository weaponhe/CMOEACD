package org.uma.jmetal.qualityindicator.impl.hypervolume;

import org.uma.jmetal.qualityindicator.impl.Hypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeVersion;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.HypervolumeContributionComparator;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.point.util.comparator.PointComparator;
import org.uma.jmetal.util.solutionattribute.impl.HypervolumeContributionAttribute;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ajnebro on 2/2/15.
 */
public class WFGHypervolume<S extends Solution<?>> extends Hypervolume<S> {

  private Point referencePoint;
  private int numberOfObjectives;

  private static final double DEFAULT_OFFSET = 100.0 ;
  private double offset = DEFAULT_OFFSET ;
  //modified by Yuehong Xie
  private boolean maximizing;

  /**
   * Default constructor
   */
  public WFGHypervolume() {
    setDescription("WFG implementation of the hypervolume quality indicator");
    setMiniming();
  }

  /**
   * Constructor
   *
   * @param referenceParetoFrontFile
   * @throws FileNotFoundException
   */
  public WFGHypervolume(String referenceParetoFrontFile) throws FileNotFoundException {
    super(referenceParetoFrontFile) ;
    setMiniming();
    numberOfObjectives = referenceParetoFront.getPointDimensions() ;
    referencePoint = null ;
    updateReferencePoint(referenceParetoFront);
    setDescription("WFG implementation of the hypervolume quality indicator");
  }

  /**
   * Constructor
   *
   * @param referenceParetoFront
   * @throws FileNotFoundException
   */
  public WFGHypervolume(Front referenceParetoFront) {
    super(referenceParetoFront) ;
    setMiniming();
    numberOfObjectives = referenceParetoFront.getPointDimensions() ;
    referencePoint = null ;
    updateReferencePoint(referenceParetoFront);
    setDescription("WFG implementation of the hypervolume quality indicator");
  }

  //modified by Yuehong Xie
  public void setMaximizing(){
    maximizing = true;
  }
  public void setMiniming(){
    maximizing = false;
  }

  boolean dominatesValue(double v1,double v2){
    if (maximizing){
      return v1 > v2;
    }
    else
      return v2 > v1;
  }
  int dominates(Point p, Point q) {
    // returns -1 if p dominates q, 1 if q dominates p, 2 if p == q, 0 otherwise
    // ASSUMING MINIMIZATION

    // domination could be checked in either order

    for (int i = p.getNumberOfDimensions()-1 ; i >= 0; i--) {
      if (dominatesValue(p.getDimensionValue(i),q.getDimensionValue(i))) {
        for (int j = i - 1; j >= 0; j--) {
          if (dominatesValue(q.getDimensionValue(j) , p.getDimensionValue(j))) {
            return 0;
          }
        }
        return -1;
      } else if (dominatesValue(q.getDimensionValue(i) , p.getDimensionValue(i))) {
        for (int j = i - 1; j >= 0; j--) {
          if (dominatesValue(p.getDimensionValue(j) , q.getDimensionValue(j))) {
            return 0;
          }
        }
        return 1;
      }
    }
    return 2;
  }

    private  WfgHypervolumeFront transformAndFliter(List<S> solutionList){
      return transformAndFliter(solutionList,referencePoint);
    }

  private void updateInvertedRefPoint(Point invertedRefPoint,S solution){
    for(int i=0;i<solution.getNumberOfObjectives();i++)
    {
      if ((maximizing && solution.getObjective(i) < invertedRefPoint.getDimensionValue(i)) || (!maximizing && solution.getObjective(i) > invertedRefPoint.getDimensionValue(i)))
        invertedRefPoint.setDimensionValue(i,solution.getObjective(i));
    }
  }
    private  WfgHypervolumeFront transformAndFliter(List<S> solutionList, Point referencePoint){
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
        point.setDimensionValue(j,tmp < 0 ? 0 : tmp);
        if(point.getDimensionValue(j) > 0)
          inBound = true;
      }
      if(inBound)
        pointList.add(point);
    }

//      Point intercept = new ArrayPoint(numberOfObjectives);
//  for(int i=0;i<numberOfObjectives;i++){
//    intercept.setDimensionValue(i,Math.abs(referencePoint.getDimensionValue(i) - invertedReferencePoint.getDimensionValue(i)));
//    if(intercept.getDimensionValue(i) < Constant.TOLERATION)
//      return new WfgHypervolumeFront(0,numberOfObjectives);
//  }
//
//    for(int i=0;i<pointList.size();++i) {
//      for (int j = i + 1; j < pointList.size(); ++j) {
//        switch (dominates(pointList.get(i), pointList.get(j))) {
//          case 0:
//            break;]
//          case -1:
//            pointList.set(j, pointList.get(pointList.size() - 1));
//            pointList.remove(pointList.size() - 1);
//            --j;
//            break;
//          case 1:
//            pointList.set(i, pointList.get(j));
//            pointList.set(j, pointList.get(pointList.size() - 1));
//            pointList.remove(pointList.size() - 1);
//            j = i;
//            break;
//          case 2:
//            pointList.set(j, pointList.get(pointList.size() - 1));
//            pointList.remove(pointList.size() - 1);
//            --j;
//            break;
//        }
//      }
//    }

    WfgHypervolumeFront front = new WfgHypervolumeFront(pointList.size(),numberOfObjectives);
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
    WfgHypervolumeFront solutionfront = transformAndFliter(solutionList);
    return evaluate(solutionfront);
//    double hv;
//    if (solutionfront.getNumberOfPoints() == 0) {
//      hv = 0.0;
//    } else {
//      numberOfObjectives = solutionfront.getPointDimensions();
//
//      WfgHypervolumeVersion wfgHv = new WfgHypervolumeVersion(numberOfObjectives, solutionfront.getNumberOfPoints());
//      if(solutionfront.getPointDimensions() > 2)
//          solutionfront.sort(0,solutionfront.getNumberOfPoints(),new PointComparator());//new PointComparator2(currentDimension-1));
//      hv = wfgHv.getHV(solutionfront);
//    }
//    return hv;
  }


  public Double evaluate(WfgHypervolumeFront front) {
    double hv;
    if (front.getNumberOfPoints() == 0) {
      hv = 0.0;
    } else {
      numberOfObjectives = front.getPointDimensions();

      WfgHypervolumeVersion wfgHv = new WfgHypervolumeVersion(numberOfObjectives, front.getNumberOfPoints());
//      if (front.getPointDimensions() > 2)
//        front.sort(0, front.getNumberOfPoints(), new PointComparator());
      hv = wfgHv.getHV(front);
    }
    return hv;
  }


  public Double evaluate(WfgHypervolumeFront front, Point referencePoint) {
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
    WfgHypervolumeFront solutionfront = transformAndFliter(solutionList,referencePoint);

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
