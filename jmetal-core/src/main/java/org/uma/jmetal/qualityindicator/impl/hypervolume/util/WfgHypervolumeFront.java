package org.uma.jmetal.qualityindicator.impl.hypervolume.util;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by ajnebro on 3/2/15.
 */
public class WfgHypervolumeFront extends ArrayFront {

  public WfgHypervolumeFront() {
    super();
  }

  public WfgHypervolumeFront(List<? extends Solution<?>> solutionList) {
    super(solutionList) ;
  }

  public WfgHypervolumeFront(int numberOfPoints, int dimensions) {
    super(numberOfPoints, dimensions) ;
  }

  public WfgHypervolumeFront(String fileName) throws FileNotFoundException {
    super(fileName);
  }

  public void setNumberOfPoints(int numberOfPoints) {
    this.numberOfPoints = numberOfPoints ;
  }

  public void ascendNumberOfPoints() {
    this.numberOfPoints++ ;
  }

  public void descendNumberOfPoints() {
    this.numberOfPoints-- ;
  }

  @Override public int getNumberOfPoints() {
    return numberOfPoints ;
  }

  @Override public Point getPoint(int index) {
    if (index < 0) {
      throw new JMetalException("The index value is negative") ;
    }

    return points[index];
  }

  @Override public void setPoint(int index, Point point) {
    if (index < 0) {
      throw new JMetalException("The index value is negative") ;
    } else if (point == null) {
      throw new JMetalException("The point is null") ;
    }
    points[index] = point ;
  }

}
