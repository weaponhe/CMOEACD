package org.uma.jmetal.qualityindicator.impl;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.point.util.comparator.LexicographicalPointComparator;
import org.uma.jmetal.util.point.util.comparator.PointDimensionComparator;
import org.uma.jmetal.util.point.util.distance.EuclideanDistance;
import org.uma.jmetal.util.point.util.distance.MinkowskiDistance;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by X250 on 2016/6/14.
 */
public class Space<S extends Solution<?>> extends GenericIndicator<S> {
    /**
     * Default constructor
     */
    public Space() {
        setName("SPACE");
        setDescription("Space quality indicator");
    }

    /**
     * Constructor
     *
     * @param referenceParetoFrontFile
     * @throws FileNotFoundException
     */
    public Space(String referenceParetoFrontFile) throws FileNotFoundException {
        super(referenceParetoFrontFile) ;
        setName("SPACE");
        setDescription("Space quality indicator");
    }

    /**
     * Constructor
     *
     * @param referenceParetoFront
     * @throws FileNotFoundException
     */
    public Space(Front referenceParetoFront) {
        super(referenceParetoFront) ;
        setName("SPACE");
        setDescription("Space quality indicator");
    }

    /**
     * Evaluate() method
     * @param solutionList
     * @return
     */
    @Override public Double evaluate(List<S> solutionList) {
        return spaceIndicator(new ArrayFront(solutionList));
    }

    public double spaceIndicator(Front front) {
        int numOfObjective = front.getPoint(0).getNumberOfDimensions();

        MinkowskiDistance distanceComputor = new MinkowskiDistance(1);
        double[] d = new double[front.getNumberOfPoints()];
        double dmean = 0.0;
        for (int i = 0 ; i < front.getNumberOfPoints(); i++) {
            d[i] = FrontUtils.distanceToNearestPoint(front.getPoint(i), front,distanceComputor);
            dmean += d[i];
        }
        dmean /= front.getNumberOfPoints();
        double result = 0.0;
        for(int i=0;i<front.getNumberOfPoints();i++){
            result += Math.pow(d[i] - dmean,2.0);
        }
        result /= front.getNumberOfPoints();
        return Math.sqrt(result);
    }

//  @Override public String getName() {
//    return "SPACE" ;
//  }
//
//  @Override public String getDescription() {
//    return "Space quality indicator" ;
//  }

    @Override
    public boolean isTheLowerTheIndicatorValueTheBetter() {
        return true ;
    }
}

