package org.uma.jmetal.qualityindicator.impl.hypervolume.util;

import org.apache.commons.math3.util.Pair;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.point.util.comparator.PointComparator;
import org.uma.jmetal.util.point.util.comparator.PointDominationComparator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.ZipInputStream;

/**
 * Created by X250 on 2016/4/19.
 */
public class QuickHypervolumeVersion {

    PointDominationComparator dominationComparator;
    PointComparator pointComparator;
    Point pointZ;
    JMetalRandom randomGenerator;

    int[] region = null;
    int[] zRegion0 = null;
    int[] zRegion1 = null;
    int[] scale=null;
    int numParts;
    List<int[]> partRegions;
    List<Integer> dominatedC;
    int nondominatedNum;
    public QuickHypervolumeVersion(){
        dominationComparator = new PointDominationComparator();
        pointComparator = new PointComparator();
        randomGenerator = JMetalRandom.getInstance();
    }
    private boolean beats(double x, double y) {
        return x > y;
    }

    private double worse(double x, double y) {
        if (x > y)
            return y;
        else
            return x;
    }

    boolean isEqual(Point a,Point b){
        for(int i=0;i<a.getNumberOfDimensions();++i){
            if(a.getDimensionValue(i) > b.getDimensionValue(i))
                return false;
            if(a.getDimensionValue(i) < b.getDimensionValue(i))
             return false;
        }
        return  true;
    }

    Point transform(Point p,Point R){
        Point o = new ArrayPoint(p.getNumberOfDimensions());
        for(int i=0;i<p.getNumberOfDimensions();++i)
            o.setDimensionValue(i,p.getDimensionValue(i) - R.getDimensionValue(i));
        return o;
    }

    Point upperBoundPoint(ArrayFront front){
        Point pointO = new ArrayPoint(front.getPoint(0));
        for(int i=1;i<front.getNumberOfPoints();++i){
            for(int j=0;j<front.getPointDimensions();++j){
                if(pointO.getDimensionValue(j) < front.getPoint(i).getDimensionValue(j))
                    pointO.setDimensionValue(j,front.getPoint(i).getDimensionValue(j));
            }
        }
        return pointO;
    }
    Point lowerBoundPoint(ArrayFront front){
        Point pointO = new ArrayPoint(front.getPoint(0));
        for(int i=1;i<front.getNumberOfPoints();++i){
            for(int j=0;j<front.getPointDimensions();++j){
                if(pointO.getDimensionValue(j) > front.getPoint(i).getDimensionValue(j))
                    pointO.setDimensionValue(j,front.getPoint(i).getDimensionValue(j));
            }
        }
        return pointO;
    }

    public double get2DHV(ArrayFront front) {
        // returns the hypervolume  in 2D
        // assumes that ps is sorted improving
        double volume = 0.0;
        front.sort(new PointComparator());

        volume = front.getPoint(0).getDimensionValue(0) * front.getPoint(0).getDimensionValue(1);
        for (int i = 1; i < front.getNumberOfPoints(); i++) {
            volume += (front.getPoint(i).getDimensionValue(0) - front.getPoint(i - 1).getDimensionValue(0)) *
                    front.getPoint(i).getDimensionValue(1);
        }
        return volume;
    }


    public void sortPartRegions(List<int[]>partRegions){
        Collections.sort(partRegions,new Comparator<int[]>(){
            @Override
            public int compare(int[] region1, int[] region2) {
                int c1 = 0;
                int c2 = 0;
                for(int i=0;i<region1.length;++i){
                    if(region1[i] == 1) c1++;
                    if(region2[i] == 1) c2++;
                }
                if(c1 > c2) return -1;
                if(c1 < c2) return 1;
                return 0;
            }
        });
    }


    public void initSetting(int dimension,int pointSize){
        pointZ = new ArrayPoint(dimension);
        zRegion0 = new int[dimension];
        for(int i = 0;i<dimension;++i) {
            pointZ.setDimensionValue(i,0.0);
            zRegion0[i] = 0;
        }
        zRegion1 = new int[dimension];
        for(int i=0;i<dimension;++i)
            zRegion1[i] = 1;

        scale = new int[dimension];
        for(int i=0;i<dimension;++i) {
            scale[i] = (int) Math.pow(2, i);
        }

        region = new int[dimension];
        numParts = (int)Math.pow(2,dimension)-2;
        partRegions = new ArrayList<>();

        for(int i=0;i<numParts;++i){
            int[] t = new int[dimension];
            int idx = i+1;
            for(int j=0;j<dimension;++j){
                if((idx & scale[j])>0)
                    t[j] = 1;
                else
                    t[j] = 0;
            }
            partRegions.add(t);
        }
        sortPartRegions(partRegions);

        dominatedC = new ArrayList<>(pointSize);
        for(int i=0;i<pointSize;++i){
            dominatedC.add(0);
        }
    }
    public double getHV(ArrayFront front) {
        if(front.getNumberOfPoints() == 0) return 0;
//        if (front.getPointDimensions() == 2) return get2DHV(front);
//        else

        initSetting(front.getPointDimensions(),front.getNumberOfPoints());

        List<Point> frontPoints = new ArrayList<>(front.getNumberOfPoints());
        for(int i=0;i<front.getNumberOfPoints();++i)
            frontPoints.add(front.getPoint(i));

        removeDominatedPoints(frontPoints);

        return getQHV(frontPoints,front.getPointDimensions());
    }

    public double getHV(ArrayFront front,int dimension) {
        List<Point> frontPoints = new ArrayList<>(front.getNumberOfPoints());
        for(int i=0;i<front.getNumberOfPoints();++i) {
            Point p = new ArrayPoint(dimension);
            for(int j=0;j<dimension;++j)
                p.setDimensionValue(j,front.getPoint(i).getDimensionValue(j));
            frontPoints.add(p);
        }

        removeDominatedPoints(frontPoints);

        return getQHV(frontPoints,dimension);
    }

    public void removeDominatedPoints(List<Point> points){
        if(points.size() > dominatedC.size()) {
            int size = 2* dominatedC.size();
            while(points.size()> size)
                size += dominatedC.size();
            dominatedC = new ArrayList<>(size);
            for(int i=0;i<size;++i)
                dominatedC.add(0);
        }else {
            for (int i = 0; i < points.size(); ++i) {
                dominatedC.set(i, 0);
            }
        }
        for(int i=0;i<points.size();++i){
            for(int j=i+1;j<points.size();++j){
                int result = dominationComparator.compare(points.get(i),points.get(j));
                if(result == -1 || result == 2)
                    dominatedC.set(j,dominatedC.get(j)+1);
                else if(result == 1)
                    dominatedC.set(i,dominatedC.get(i)+1);
            }
        }
//        nondominatedNum=0;
//        for(int i=0;i<points.size();++i){
//            if(dominatedC.get(i) == 0)
//                nondominatedNum++;
//        }
//        if(nondominatedNum < points.size()){
           for(int i = points.size()-1;i>=0;i--){
               if(dominatedC.get(i)> 0)
                   points.remove(i);
           }
//        }
    }
    boolean regionEqual(int[] regionA,int[] regionB){
        int nObj = regionA.length;
        for(int i=0;i<nObj;++i){
            if(regionA[i] != regionB[i])
                return false;
        }
        return true;
    }

    int region2Idx(int[] region){
        int idx = -1;
        for(int j=0;j<region.length;++j){
            idx += region[j]*scale[j];
        }
        return idx;
    }

    void topSort(List<Point> points, int topK){
        if(points.size() < topK)
            topK = points.size();
        int last = points.size()-1;
        for(int i=0;i<topK;++i){
            int idx = last-i;
            for(int j=idx - 1;j>=0 ;--j){
                if(-1 == pointComparator.compare(points.get(j),points.get(idx))){
                    idx = j;
                }
            }
            if(idx != last - i){
                Point tmpP = points.get(idx);
                points.set(idx,points.get(last - i));
                points.set(last-i,tmpP);
            }
        }
    }

    public double getQHV(List<Point> front,int nObj) {

        switch (front.size()) {
            case 0:
                return 0.0;
            case 1:
                return getInclHV(front.get(0));
            case 2:
                return getInclHV2(front.get(0), front.get(1));
            case 3:
                return getInclHV3(front.get(0), front.get(1), front.get(2));
            case 4:
                return getInclHV4(front.get(0), front.get(1), front.get(2), front.get(3));
            default:
                break;
        }

//        topSort(front,4);
//        int last = front.size()-1;
//        double volume = getInclHV4(front.get(last), front.get(last-1), front.get(last - 2),front.get(last - 3));
//        for(int i=0;i<4;++i)
//            front.remove(front.size()-1);

        double volume = 0.0;
        Point pivot;   /* Pivot */
        ///1. Select
//        int idxPivot = -1;
//        double pivotHV = Double.NEGATIVE_INFINITY;
//        double tmpHV = 0.0;
//        for (int i = 0; i < front.size(); ++i) {
//            tmpHV = getInclHV(front.get(i));
//            if (tmpHV > pivotHV) {
//                /* New Pivot Candidate */
//                pivotHV = tmpHV;
//                idxPivot = i;
//            }
//        }
        int idxPivot = randomGenerator.nextInt(0,front.size()-1);//(front.size()-1)/2;
        pivot = front.get(idxPivot);
        double pivotHV = getInclHV(pivot);

        volume += pivotHV;
//        JMetalLogger.logger.info("HV pivot : " + volume);
        if (volume < 0)
            JMetalLogger.logger.info("Error HV pivot");
//        JMetalLogger.logger.info("Pivot : ["+idxPivot+"]<"+pivot.getDimensionValue(0)+" , "+pivot.getDimensionValue(1)+" , "+pivot.getDimensionValue(2)+">"+pivotHV);

        /// Divide and Conquer
        List<List<Point>> seperatedFrontPoints = new ArrayList<>();
        List<Point> seperatedZ = new ArrayList<>();
        for(int i=0;i<numParts;++i){
            List<Point> f = new ArrayList<>();
            seperatedFrontPoints.add(f);
            Point p = new ArrayPoint(nObj);
            seperatedZ.add(p);
        }

        for(int i=0;i<partRegions.size();++i){
            int idx = region2Idx(partRegions.get(i));
            Point p = seperatedZ.get(idx);
            for(int j=0;j<nObj;++j){
                if(partRegions.get(i)[j] == 0)
                    p.setDimensionValue(j,pointZ.getDimensionValue(j));
                else
                    p.setDimensionValue(j,pivot.getDimensionValue(j));
            }
            seperatedZ.set(idx,p);
        }

        for(int i=0;i<front.size();++i){
            if(i == idxPivot)
                continue;
            for(int j=0;j<nObj;++j){
                if(front.get(i).getDimensionValue(j) > pivot.getDimensionValue(j))
                    region[j] = 1;
                else
                    region[j] = 0;
            }
            int idx = region2Idx(region);
            if(idx<0 || idx >= numParts)
                continue;
            seperatedFrontPoints.get(idx).add(front.get(i));
        }

        for(int i=0;i<partRegions.size();++i){
            int idxC = region2Idx(partRegions.get(i));
            if(seperatedFrontPoints.get(idxC).isEmpty())
                continue;

            removeDominatedPoints(seperatedFrontPoints.get(idxC));

            System.arraycopy(partRegions.get(i),0,region,0,nObj);
            for(int j=0;j<nObj;++j){
                if(region[j] == 0)
                    continue;
                region[j] = 0;
                int idxN = region2Idx(region);
                if(idxN >= 0){
                    for(int k = 0;k<seperatedFrontPoints.get(idxC).size();++k) {
                        Point np = new ArrayPoint(seperatedFrontPoints.get(idxC).get(k));
                        np.setDimensionValue(j,seperatedZ.get(idxC).getDimensionValue(j));
                        seperatedFrontPoints.get(idxN).add(np);
                    }
                }
                region[j] = 1;
            }

//            for(int j=0;j<seperatedFrontPoints.get(idxC).size();++j){
//                Point p = seperatedFrontPoints.get(idxC).get(j);
//                for(int k=0;k<nObj;++k)
//                    p.setDimensionValue(k,p.getDimensionValue(k) - seperatedZ.get(idxC).getDimensionValue(k));
//                seperatedFrontPoints.get(idxC).set(j,p);
//            }
//
//            double tmp = getQHV(seperatedFrontPoints.get(idxC),nObj);
//            if (tmp < 0)
//                JMetalLogger.logger.info("Error HV");
////            if(seperatedFronts.get(i).getNumberOfPoints() <= 1)
////                JMetalLogger.logger.info("HV point:" + tmp);
//            volume += tmp;
//            seperatedFrontPoints.get(idxC).clear();
        }

//        String outputStr = "["+front.size()+"]  ";
        for (int i=0;i<seperatedFrontPoints.size();++i)
        {
            if(seperatedFrontPoints.get(i).isEmpty())
                continue;
            for(int j=0;j<seperatedFrontPoints.get(i).size();++j){
                Point p = seperatedFrontPoints.get(i).get(j);
                for(int k=0;k<nObj;++k)
                    p.setDimensionValue(k,p.getDimensionValue(k) - seperatedZ.get(i).getDimensionValue(k));
                seperatedFrontPoints.get(i).set(j,p);
            }
//            outputStr += seperatedFrontPoints.get(i).size()+"\t";
            double tmp = getQHV(seperatedFrontPoints.get(i),nObj);
            if (tmp < 0)
                JMetalLogger.logger.info("Error HV");
//            if(seperatedFronts.get(i).getNumberOfPoints() <= 1)
//                JMetalLogger.logger.info("HV point:" + tmp);
            volume += tmp;
            seperatedFrontPoints.get(i).clear();
        }
//        JMetalLogger.logger.info(outputStr);
//
//        for (int i = 0; i < seperatedFrontPoints.size(); ++i) {
//            if(seperatedFrontPoints.get(i).isEmpty())
//                continue;
//            ArrayFront nFront = new ArrayFront(seperatedFrontPoints.get(i).size(),front.getPointDimensions());
//            for(int j=0;j<seperatedFrontPoints.get(i).size();++j) {
//                Point p = nFront.getPoint(j);
//                for(int k=0;k<front.getPointDimensions();++k)
//                    p.setDimensionValue(k,Math.max(0,seperatedFrontPoints.get(i).get(j).getDimensionValue(k) - seperatedZ.get(i).getDimensionValue(k)));
//                nFront.setPoint(j, p);
//            }
//
//            seperatedFrontPoints.get(i).clear();
//            double tmp = getQHV(nFront);
//            if (tmp < 0)
//                JMetalLogger.logger.info("Error HV");
////            if(seperatedFronts.get(i).getNumberOfPoints() <= 1)
////                JMetalLogger.logger.info("HV point:" + tmp);
//            volume += tmp;
//        }
        seperatedFrontPoints.clear();
        seperatedZ.clear();
        return volume;
    }

    double getInclHV(Point p)
// returns the inclusive hypervolume of p
    {
        double volume = 1.0;
        for (int i = 0; i < p.getNumberOfDimensions(); i++)
            volume *= p.getDimensionValue(i);
        return volume;
//        BigDecimal volume = new BigDecimal("1.0");
//        for (int i = 0; i < p.getNumberOfDimensions(); i++)
//            volume = volume.multiply(new BigDecimal(Double.toString(p.getDimensionValue(i))));
//        return volume.doubleValue();
    }

    double getInclHV2(Point p, Point q)
// returns the hypervolume of {p, q}
    {
        double vp = 1;
        double vq = 1;
        double vpq = 1;
        for (int i = 0; i < p.getNumberOfDimensions(); i++) {
            vp *= p.getDimensionValue(i);
            vq *= q.getDimensionValue(i);
            vpq *= worse(p.getDimensionValue(i), q.getDimensionValue(i));
        }
        return vp + vq - vpq;
    }


    double getInclHV3(Point p, Point q, Point r)
// returns the hypervolume of {p, q, r}
    {
        double vp = 1;
        double vq = 1;
        double vr = 1;
        double vpq = 1;
        double vpr = 1;
        double vqr = 1;
        double vpqr = 1;
        for (int i = 0; i < p.getNumberOfDimensions(); i++) {
            vp *= p.getDimensionValue(i);
            vq *= q.getDimensionValue(i);
            vr *= r.getDimensionValue(i);
            if (beats(p.getDimensionValue(i), q.getDimensionValue(i))) {
                if (beats(q.getDimensionValue(i), r.getDimensionValue(i))) {
                    vpq *= q.getDimensionValue(i);
                    vpr *= r.getDimensionValue(i);
                    vqr *= r.getDimensionValue(i);
                    vpqr *= r.getDimensionValue(i);
                } else {
                    vpq *= q.getDimensionValue(i);
                    vpr *= worse(p.getDimensionValue(i), r.getDimensionValue(i));
                    vqr *= q.getDimensionValue(i);
                    vpqr *= q.getDimensionValue(i);
                }
            } else if (beats(p.getDimensionValue(i), r.getDimensionValue(i))) {
                vpq *= p.getDimensionValue(i);
                vpr *= r.getDimensionValue(i);
                vqr *= r.getDimensionValue(i);
                vpqr *= r.getDimensionValue(i);
            } else {
                vpq *= p.getDimensionValue(i);
                vpr *= p.getDimensionValue(i);
                vqr *= worse(q.getDimensionValue(i), r.getDimensionValue(i));
                vpqr *= p.getDimensionValue(i);
            }
        }
        return vp + vq + vr - vpq - vpr - vqr + vpqr;
    }


    double getInclHV4(Point p, Point q, Point r, Point s)
// returns the hypervolume of {p, q, r, s}
    {
        double vp = 1;
        double vq = 1;
        double vr = 1;
        double vs = 1;
        double vpq = 1;
        double vpr = 1;
        double vps = 1;
        double vqr = 1;
        double vqs = 1;
        double vrs = 1;
        double vpqr = 1;
        double vpqs = 1;
        double vprs = 1;
        double vqrs = 1;
        double vpqrs = 1;
        for (int i = 0; i < p.getNumberOfDimensions(); i++) {
            vp *= p.getDimensionValue(i);
            vq *= q.getDimensionValue(i);
            vr *= r.getDimensionValue(i);
            vs *= s.getDimensionValue(i);
            if (beats(p.getDimensionValue(i), q.getDimensionValue(i)))
                if (beats(q.getDimensionValue(i), r.getDimensionValue(i)))
                    if (beats(r.getDimensionValue(i), s.getDimensionValue(i))) {
                        vpq *= q.getDimensionValue(i);
                        vpr *= r.getDimensionValue(i);
                        vps *= s.getDimensionValue(i);
                        vqr *= r.getDimensionValue(i);
                        vqs *= s.getDimensionValue(i);
                        vrs *= s.getDimensionValue(i);
                        vpqr *= r.getDimensionValue(i);
                        vpqs *= s.getDimensionValue(i);
                        vprs *= s.getDimensionValue(i);
                        vqrs *= s.getDimensionValue(i);
                        vpqrs *= s.getDimensionValue(i);
                    } else {
                        double z1 = worse(q.getDimensionValue(i), s.getDimensionValue(i));
                        vpq *= q.getDimensionValue(i);
                        vpr *= r.getDimensionValue(i);
                        vps *= worse(p.getDimensionValue(i), s.getDimensionValue(i));
                        vqr *= r.getDimensionValue(i);
                        vqs *= z1;
                        vrs *= r.getDimensionValue(i);
                        vpqr *= r.getDimensionValue(i);
                        vpqs *= z1;
                        vprs *= r.getDimensionValue(i);
                        vqrs *= r.getDimensionValue(i);
                        vpqrs *= r.getDimensionValue(i);
                    }
                else if (beats(q.getDimensionValue(i), s.getDimensionValue(i))) {
                    vpq *= q.getDimensionValue(i);
                    vpr *= worse(p.getDimensionValue(i), r.getDimensionValue(i));
                    vps *= s.getDimensionValue(i);
                    vqr *= q.getDimensionValue(i);
                    vqs *= s.getDimensionValue(i);
                    vrs *= s.getDimensionValue(i);
                    vpqr *= q.getDimensionValue(i);
                    vpqs *= s.getDimensionValue(i);
                    vprs *= s.getDimensionValue(i);
                    vqrs *= s.getDimensionValue(i);
                    vpqrs *= s.getDimensionValue(i);
                } else {
                    double z1 = worse(p.getDimensionValue(i), r.getDimensionValue(i));
                    vpq *= q.getDimensionValue(i);
                    vpr *= z1;
                    vps *= worse(p.getDimensionValue(i), s.getDimensionValue(i));
                    vqr *= q.getDimensionValue(i);
                    vqs *= q.getDimensionValue(i);
                    vrs *= worse(r.getDimensionValue(i), s.getDimensionValue(i));
                    vpqr *= q.getDimensionValue(i);
                    vpqs *= q.getDimensionValue(i);
                    vprs *= worse(z1, s.getDimensionValue(i));
                    vqrs *= q.getDimensionValue(i);
                    vpqrs *= q.getDimensionValue(i);
                }
            else if (beats(q.getDimensionValue(i), r.getDimensionValue(i)))
                if (beats(p.getDimensionValue(i), s.getDimensionValue(i))) {
                    double z1 = worse(p.getDimensionValue(i), r.getDimensionValue(i));
                    double z2 = worse(r.getDimensionValue(i), s.getDimensionValue(i));
                    vpq *= p.getDimensionValue(i);
                    vpr *= z1;
                    vps *= s.getDimensionValue(i);
                    vqr *= r.getDimensionValue(i);
                    vqs *= s.getDimensionValue(i);
                    vrs *= z2;
                    vpqr *= z1;
                    vpqs *= s.getDimensionValue(i);
                    vprs *= z2;
                    vqrs *= z2;
                    vpqrs *= z2;
                } else {
                    double z1 = worse(p.getDimensionValue(i), r.getDimensionValue(i));
                    double z2 = worse(r.getDimensionValue(i), s.getDimensionValue(i));
                    vpq *= p.getDimensionValue(i);
                    vpr *= z1;
                    vps *= p.getDimensionValue(i);
                    vqr *= r.getDimensionValue(i);
                    vqs *= worse(q.getDimensionValue(i), s.getDimensionValue(i));
                    vrs *= z2;
                    vpqr *= z1;
                    vpqs *= p.getDimensionValue(i);
                    vprs *= z1;
                    vqrs *= z2;
                    vpqrs *= z1;
                }
            else if (beats(p.getDimensionValue(i), s.getDimensionValue(i))) {
                vpq *= p.getDimensionValue(i);
                vpr *= p.getDimensionValue(i);
                vps *= s.getDimensionValue(i);
                vqr *= q.getDimensionValue(i);
                vqs *= s.getDimensionValue(i);
                vrs *= s.getDimensionValue(i);
                vpqr *= p.getDimensionValue(i);
                vpqs *= s.getDimensionValue(i);
                vprs *= s.getDimensionValue(i);
                vqrs *= s.getDimensionValue(i);
                vpqrs *= s.getDimensionValue(i);
            } else {
                double z1 = worse(q.getDimensionValue(i), s.getDimensionValue(i));
                vpq *= p.getDimensionValue(i);
                vpr *= p.getDimensionValue(i);
                vps *= p.getDimensionValue(i);
                vqr *= q.getDimensionValue(i);
                vqs *= z1;
                vrs *= worse(r.getDimensionValue(i), s.getDimensionValue(i));
                vpqr *= p.getDimensionValue(i);
                vpqs *= p.getDimensionValue(i);
                vprs *= p.getDimensionValue(i);
                vqrs *= z1;
                vpqrs *= p.getDimensionValue(i);
            }
        }
        return vp + vq + vr + vs - vpq - vpr - vps - vqr - vqs - vrs + vpqr + vpqs + vprs + vqrs - vpqrs;
    }
}