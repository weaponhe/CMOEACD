package org.uma.jmetal.qualityindicator.impl.hypervolume.util;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;

import org.uma.jmetal.util.point.impl.ArrayPoint;
import org.uma.jmetal.util.point.util.comparator.PointComparator;


/**
 * Created by X250 on 2016/4/12.
 */
public class WfgHypervolumeVersion {
    protected int safe;
    WfgHypervolumeFront[] fs;
    protected int currentDeep;
    protected int currentDimension;
    protected int maxNumberOfPoints;
    protected int maxNumberOfObjectives;

    protected boolean beats(double x, double y) {
        return x > y;
    }

    protected double worse(double x, double y) {
        if (x > y)
            return y;
        else
            return x;
    }

    protected class PointComparator0 extends PointComparator{
        @Override
        public int compare(Point pointOne, Point pointTwo) {
            if (pointOne == null) {
                throw new JMetalException("PointOne is null");
            } else if (pointTwo == null) {
                throw new JMetalException("PointTwo is null");
            } else if (pointOne.getNumberOfDimensions() != pointTwo.getNumberOfDimensions()) {
                throw new JMetalException("Points have different size: "
                        + pointOne.getNumberOfDimensions() + " and "
                        + pointTwo.getNumberOfDimensions());
            }

                if (beats(pointOne.getDimensionValue(currentDimension -1), pointTwo.getDimensionValue(currentDimension -1))) {
                    return -1;
                } else if (beats(pointTwo.getDimensionValue(currentDimension -1), pointOne.getDimensionValue(currentDimension -1))) {
                    return 1;
                }
            return 0;
        }
    } ;
    PointComparator0 comparator0;

    protected  class PointComparator1 extends PointComparator{
        @Override
        public int compare(Point pointOne, Point pointTwo) {
            if (pointOne == null) {
                throw new JMetalException("PointOne is null");
            } else if (pointTwo == null) {
                throw new JMetalException("PointTwo is null");
            } else if (pointOne.getNumberOfDimensions() != pointTwo.getNumberOfDimensions()) {
                throw new JMetalException("Points have different size: "
                        + pointOne.getNumberOfDimensions() + " and "
                        + pointTwo.getNumberOfDimensions());
            }

            for (int i = currentDimension -1 ; i >= 0; i--) {
                if (beats(pointOne.getDimensionValue(i), pointTwo.getDimensionValue(i))) {
                    return -1;
                } else if (beats(pointTwo.getDimensionValue(i), pointOne.getDimensionValue(i))) {
                    return 1;
                }
            }
            return 0;
        }
    } ;
    PointComparator1 comparator1;

    protected  class PointComparator2 extends PointComparator{
        @Override
        public int compare(Point pointOne, Point pointTwo) {
            if (pointOne == null) {
                throw new JMetalException("PointOne is null");
            } else if (pointTwo == null) {
                throw new JMetalException("PointTwo is null");
            } else if (pointOne.getNumberOfDimensions() != pointTwo.getNumberOfDimensions()) {
                throw new JMetalException("Points have different size: "
                        + pointOne.getNumberOfDimensions() + " and "
                        + pointTwo.getNumberOfDimensions());
            }

            for (int i = currentDimension -2 ; i >= 0; i--) {
                if (beats(pointOne.getDimensionValue(i), pointTwo.getDimensionValue(i))) {
                    return -1;
                } else if (beats(pointTwo.getDimensionValue(i), pointOne.getDimensionValue(i))) {
                    return 1;
                }
            }
            return 0;
        }
    } ;
    PointComparator2 comparator2;

    public WfgHypervolumeVersion(int dimension, int maxNumberOfPoints) {

        safe = 0;
        currentDeep = 0;
        currentDimension = dimension;
        this.maxNumberOfPoints = maxNumberOfPoints;
        maxNumberOfObjectives = dimension;

        comparator0 = new PointComparator0();
        comparator1 = new PointComparator1();
        comparator2 = new PointComparator2();

        int maxd = this.maxNumberOfObjectives - 2;
        fs = new WfgHypervolumeFront[maxd];
        for (int i = 0; i < maxd; i++) {
            fs[i] = new WfgHypervolumeFront(maxNumberOfPoints, dimension - i - 1);
        }
    }

    public void statusReset(){
        safe = 0;
        currentDeep = 0;
        currentDimension = maxNumberOfObjectives;
    }

    public double get2DHV(WfgHypervolumeFront front,int calcSize) {
        // returns the hypervolume of ps[0 .. k-1] in 2D
        // assumes that ps is sorted improving
        double volume = 0.0;
        //if(calcSize > 4)
          //  front.sort(4,calcSize,comparator1);
//        front.sort(0,safe,comparator2);
//        if(calcSize > safe)
//            front.sort(safe,calcSize,comparator1);

        volume = front.getPoint(0).getDimensionValue(0) * front.getPoint(0).getDimensionValue(1);
        for (int i = 1; i < calcSize; i++) {
            volume += (front.getPoint(i).getDimensionValue(0) - front.getPoint(i - 1).getDimensionValue(0)) *
                    front.getPoint(i).getDimensionValue(1);
        }
        return volume;
    }

    public double getExclusiveHV(WfgHypervolumeFront front, int point) {
        makeDominatedBit(front, point);
        double volume = getInclHV(front.getPoint(point)) - getHV(fs[currentDeep - 1]);
        currentDeep--;
        return volume;
    }

    void topSort(WfgHypervolumeFront front,int topK){
        if(front.getNumberOfPoints() < topK)
            topK = front.getNumberOfPoints();
        int firstS = topK;
        if(safe < topK)
            firstS = safe;

        for(int i=0;i<firstS;++i){
            int idx = i;
            for(int j=i+1;j<safe;++j){
                if(-1 == comparator2.compare(front.getPoint(j),front.getPoint(idx))){
                    idx = j;
                }
            }
            if(idx != i){
                front.swap(idx,i);
            }
        }
        for(int i=0;i<topK;++i){
            int idx = i;
            for(int j=i+1;j<front.getNumberOfPoints();++j){
                if(-1 == comparator1.compare(front.getPoint(j),front.getPoint(idx))){
                    idx = j;
                }
            }
            if(idx != i){
                front.swap(idx,i);
            }
        }
    }

    public double directHV(ArrayFront front, int idxBegin, int calSize){
        switch (calSize) {
            case 1:
                return getInclHV(front.getPoint(idxBegin));
            case 2:
                return getInclHV2(front.getPoint(idxBegin), front.getPoint(idxBegin+1));
            case 3:
                return getInclHV3(front.getPoint(idxBegin), front.getPoint(idxBegin+1), front.getPoint(idxBegin+2));
            case 4:
                return getInclHV4(front.getPoint(idxBegin), front.getPoint(idxBegin+1), front.getPoint(idxBegin+2), front.getPoint(idxBegin+3));
            case 5:
                return getInclHV5(front.getPoint(idxBegin), front.getPoint(idxBegin+1), front.getPoint(idxBegin+2), front.getPoint(idxBegin+3),front.getPoint(idxBegin+4));
            default:break;
        }
        return 0.0;
    }

    public double getHV(WfgHypervolumeFront front) {
        // process small fronts with the IEA
//        JMetalLogger.logger.info("[num of points] "+ front.getNumberOfPoints());

//        switch (front.getNumberOfPoints()) {
//            case 0:
//                return 0;
//            case 1:
//                return getInclHV(front.getPoint(0));
//            case 2:
//                return getInclHV2(front.getPoint(0), front.getPoint(1));
//            case 3:
//                return getInclHV3(front.getPoint(0), front.getPoint(1), front.getPoint(2));
//            case 4:
//                return getInclHV4(front.getPoint(0), front.getPoint(1), front.getPoint(2), front.getPoint(3));
//            default:break;
//        }
        if(front.getNumberOfPoints()<=5)
            return directHV(front,0,front.getNumberOfPoints());

        // these points need sorting
        front.sort(safe,front.getNumberOfPoints(),comparator1);//new PointComparator2(currentDimension-1));
        // n = 2 implies that safe = 0
        if (currentDimension == 2) return get2DHV(front,front.getNumberOfPoints());
        // these points don't NEED sorting, but it helps
        front.sort(0, safe,comparator2);//new PointComparator2(currentDimension -2));
        //front.sort(4, front.getNumberOfPoints(),comparator0);//new PointComparator2(currentDimension -2));

        if (currentDimension == 3 && safe > 0) {
//            JMetalLogger.logger.info("["+currentDeep+"  -  "+currentDimension+"]");
            double volume = front.getPoint(0).getDimensionValue(2) * get2DHV(front, safe);
//            front.sort(safe,front.getNumberOfPoints(),comparator0);

            currentDimension--;
            for (int i = safe; i <front.getNumberOfPoints(); i++)
                // we can ditch dominated points here, but they will be ditched anyway in makeDominatedBit
                volume += front.getPoint(i).getDimensionValue(currentDimension) * getExclusiveHV(front,i);
            currentDimension++;
//            JMetalLogger.logger.info("["+currentDeep+"  -  "+currentDimension+"]  HV :"+volume);
            return volume;
        } else
            {
//            JMetalLogger.logger.info("["+currentDeep+"  -  "+currentDimension+"]");
//            topSort(front,4);
            double volume = getInclHV5(front.getPoint(0), front.getPoint(1), front.getPoint(2),front.getPoint(3),front.getPoint(4));

//            front.sort(safe > 4 ? safe : 4, front.getNumberOfPoints(), comparator0);

            currentDimension--;
            for (int i = 5; i < front.getNumberOfPoints(); i++)
                // we can ditch dominated points here, but they will be ditched anyway in makeDominatedBit
                volume += front.getPoint(i).getDimensionValue(currentDimension) * getExclusiveHV(front,i);
            currentDimension++;
//            JMetalLogger.logger.info("["+currentDeep+"  -  "+currentDimension+"]  HV :"+volume);
            return volume;
        }
    }

    public void makeDominatedBit(WfgHypervolumeFront front, int p) {
// creates the front ps[0 .. p-1] in fs[fr], with each point bounded by ps[p] and dominated points removed

        int l = 0;
        int u = p - 1;
        for (int i = p - 1; i >= 0; i--)
            if (beats(front.getPoint(p).getDimensionValue(currentDimension - 1), front.getPoint(i).getDimensionValue(currentDimension - 1))) {
                Point tmpPoint = new ArrayPoint(currentDimension);
                tmpPoint.setDimensionValue(currentDimension -1, front.getPoint(i).getDimensionValue(currentDimension - 1));
                for (int j = 0; j < currentDimension - 1; j++)
                    tmpPoint.setDimensionValue(j , worse(front.getPoint(p).getDimensionValue(j), front.getPoint(i).getDimensionValue(j)));
                fs[currentDeep].setPoint(u,tmpPoint);
//                fs[currentDeep].getPoint(u).setDimensionValue(currentDimension -1 , front.getPoint(i).getDimensionValue(currentDimension -1));
//                for (int j=0;j<currentDimension -1;j++)
//                    fs[currentDeep].getPoint(u).setDimensionValue(j , worse(front.getPoint(p).getDimensionValue(j) , front.getPoint(i).getDimensionValue(j)));
                u--;
            } else {
                Point tmpPoint = new ArrayPoint(currentDimension);
                tmpPoint.setDimensionValue(currentDimension -1, front.getPoint(p).getDimensionValue(currentDimension - 1));
                for (int j = 0; j < currentDimension - 1; j++)
                    tmpPoint.setDimensionValue(j , worse(front.getPoint(p).getDimensionValue(j), front.getPoint(i).getDimensionValue(j)));

                fs[currentDeep].setPoint(l,tmpPoint);
//                fs[currentDeep].getPoint(l).setDimensionValue(currentDimension -1 , front.getPoint(i).getDimensionValue(currentDimension -1));
//                for (int j=0;j<currentDimension -1;j++)
//                    fs[currentDeep].getPoint(l).setDimensionValue(j , worse(front.getPoint(p).getDimensionValue(j) , front.getPoint(i).getDimensionValue(j)));
                l++;
            }

        // points below l are all equal in the last objective; points above l are all worse
        // points below l can dominate each other, and we don't need to compare the last objective
        // points above l cannot dominate points that start below l, and we don't need to compare the last objective
        fs[currentDeep].setNumberOfPoints(1);
        for (int i = 1; i < l; i++) {
            int j = 0;
            while (j < fs[currentDeep].getNumberOfPoints())
                switch (dominates2way(fs[currentDeep].getPoint(i), fs[currentDeep].getPoint(j), currentDimension - 2)) {
                    case 0:
                        j++;
                        break;
                    case -1: // AT THIS POINT WE KNOW THAT i CANNOT BE DOMINATED BY ANY OTHER PROMOTED POINT j
                        // SWAP i INTO j, AND 1-WAY DOM FOR THE REST OF THE js
                        fs[currentDeep].swap(i,j);

                        while (j < fs[currentDeep].getNumberOfPoints() - 1 && dominates1way(fs[currentDeep].getPoint(j), fs[currentDeep].getPoint(fs[currentDeep].getNumberOfPoints() - 1), currentDimension - 1))
                            fs[currentDeep].descendNumberOfPoints();//nPoints--

                        int k = j + 1;

                        while (k < fs[currentDeep].getNumberOfPoints())
                            if (dominates1way(fs[currentDeep].getPoint(j), fs[currentDeep].getPoint(k), currentDimension - 2)) {
                                fs[currentDeep].swap(k,fs[currentDeep].getNumberOfPoints() - 1);
                                fs[currentDeep].descendNumberOfPoints();
                            } else
                                k++;
                    default:
                        j = fs[currentDeep].getNumberOfPoints() + 1;
                }

            if (j == fs[currentDeep].getNumberOfPoints()) {
                fs[currentDeep].swap(fs[currentDeep].getNumberOfPoints(),i);
                fs[currentDeep].ascendNumberOfPoints();//nPoints++
            }
        }

        safe =(int) worse(l, fs[currentDeep].getNumberOfPoints());

        for (int i = l; i < p; i++) {
            int j = 0;
            while (j < safe)
                if (dominates1way(fs[currentDeep].getPoint(j), fs[currentDeep].getPoint(i), currentDimension - 2))
                    j = fs[currentDeep].getNumberOfPoints() + 1;
                else
                    j++;
            while (j < fs[currentDeep].getNumberOfPoints())
                switch (dominates2way(fs[currentDeep].getPoint(i), fs[currentDeep].getPoint(j), currentDimension - 1)) {
                    case 0:
                        j++;
                        break;
                    case -1: // AT THIS POINT WE KNOW THAT i CANNOT BE DOMINATED BY ANY OTHER PROMOTED POINT j
                        // SWAP i INTO j, AND 1-WAY DOM FOR THE REST OF THE js
                        fs[currentDeep].swap(j,i);
                        while (j < fs[currentDeep].getNumberOfPoints() - 1 && dominates1way(fs[currentDeep].getPoint(j),fs[currentDeep].getPoint(fs[currentDeep].getNumberOfPoints() - 1), currentDimension - 1))
                            fs[currentDeep].descendNumberOfPoints();
                        int k = j + 1;
                        while (k < fs[currentDeep].getNumberOfPoints())
                            if (dominates1way(fs[currentDeep].getPoint(j), fs[currentDeep].getPoint(k), currentDimension - 1)) {
                                fs[currentDeep].swap(k,fs[currentDeep].getNumberOfPoints()-1);
                                fs[currentDeep].descendNumberOfPoints();
                            } else
                                k++;
                    default:
                        j = fs[currentDeep].getNumberOfPoints() + 1;
                }
            if (j == fs[currentDeep].getNumberOfPoints()) {
                fs[currentDeep].swap(fs[currentDeep].getNumberOfPoints(),i);
                fs[currentDeep].ascendNumberOfPoints();
            }
        }
       // fs[currentDeep].sort(safe,fs[ currentDeep].getNumberOfPoints(),comparator0);
        currentDeep++;
    }
//    public void makeDominatedBit(WfgHypervolumeFront front, int p) {
//// creates the front ps[0 .. p-1] in fs[fr], with each point bounded by ps[p] and dominated points removed
//        int l = 0;
//        int u = p - 1;
//        for (int i = p - 1; i >= 0; i--)
//            if (beats(front.getPoint(p).getDimensionValue(currentDimension - 1), front.getPoint(i).getDimensionValue(currentDimension - 1))) {
//                Point tmpPoint = new ArrayPoint(currentDimension);
//                tmpPoint.setDimensionValue(currentDimension -1, front.getPoint(i).getDimensionValue(currentDimension - 1));
//                for (int j = 0; j < currentDimension - 1; j++)
//                    tmpPoint.setDimensionValue(j , worse(front.getPoint(p).getDimensionValue(j), front.getPoint(i).getDimensionValue(j)));
//                fs[currentDeep].setPoint(u,tmpPoint);
////                fs[currentDeep].getPoint(u).setDimensionValue(currentDimension -1 , front.getPoint(i).getDimensionValue(currentDimension -1));
////                for (int j=0;j<currentDimension -1;j++)
////                    fs[currentDeep].getPoint(u).setDimensionValue(j , worse(front.getPoint(p).getDimensionValue(j) , front.getPoint(i).getDimensionValue(j)));
//                u--;
//            } else {
//                Point tmpPoint = new ArrayPoint(currentDimension);
//                tmpPoint.setDimensionValue(currentDimension -1, front.getPoint(p).getDimensionValue(currentDimension - 1));
//                for (int j = 0; j < currentDimension - 1; j++)
//                    tmpPoint.setDimensionValue(j , worse(front.getPoint(p).getDimensionValue(j), front.getPoint(i).getDimensionValue(j)));
//
//                fs[currentDeep].setPoint(l,tmpPoint);
////                fs[currentDeep].getPoint(l).setDimensionValue(currentDimension -1 , front.getPoint(i).getDimensionValue(currentDimension -1));
////                for (int j=0;j<currentDimension -1;j++)
////                    fs[currentDeep].getPoint(l).setDimensionValue(j , worse(front.getPoint(p).getDimensionValue(j) , front.getPoint(i).getDimensionValue(j)));
//                l++;
//            }
//        // points below l are all equal in the last objective; points above l are all worse
//        // points below l can dominate each other, and we don't need to compare the last objective
//        // points above l cannot dominate points that start below l, and we don't need to compare the last objective
//        fs[currentDeep].setNumberOfPoints(l);
//        for(int i=0;i<fs[currentDeep].getNumberOfPoints();++i){
//            for(int j=i+1;j<fs[currentDeep].getNumberOfPoints();++j){
//                switch (dominates2way(fs[currentDeep].getPoint(i), fs[currentDeep].getPoint(j), currentDimension - 2)) {
//                    case 0:
//                        break;
//                    case -1:
//                        //fs[currentDeep].setPoint(j,fs[currentDeep].getPoint(fs[currentDeep].getNumberOfPoints() -1));
//                        fs[currentDeep].swap(j,fs[currentDeep].getNumberOfPoints() -1);
//                        fs[currentDeep].descendNumberOfPoints();
//                        --j;
//                        break;
//                    case 1:
//                        //fs[currentDeep].setPoint(i,fs[currentDeep].getPoint(j));
//                        //fs[currentDeep].setPoint(j,fs[currentDeep].getPoint(fs[currentDeep].getNumberOfPoints() -1));
//                        fs[currentDeep].swap(i,j);
//                        fs[currentDeep].swap(j,fs[currentDeep].getNumberOfPoints() -1);
//                        fs[currentDeep].descendNumberOfPoints();
//                        j=i;
//                        break;
//                    case 2:
//                        //fs[currentDeep].setPoint(j,fs[currentDeep].getPoint(fs[currentDeep].getNumberOfPoints() -1));
//                        fs[currentDeep].swap(j,fs[currentDeep].getNumberOfPoints()-1);
//                        fs[currentDeep].descendNumberOfPoints();
//                        --j;
//                        break;
//                }
//            }
//        }
//
//        safe =  fs[currentDeep].getNumberOfPoints();
//
//        fs[currentDeep].setNumberOfPoints(p);
//        for(int i = l;i<fs[currentDeep].getNumberOfPoints();++i){
//            for(int j = i+1 ; j < fs[currentDeep].getNumberOfPoints();++j){
//                switch (dominates2way(fs[currentDeep].getPoint(i), fs[currentDeep].getPoint(j), currentDimension - 1)) {
//                    case 0:
//                        break;
//                    case -1:
//                        //fs[currentDeep].setPoint(j,fs[currentDeep].getPoint(fs[currentDeep].getNumberOfPoints() -1));
//                        fs[currentDeep].swap(j,fs[currentDeep].getNumberOfPoints() -1);
//                        fs[currentDeep].descendNumberOfPoints();
//                        --j;
//                        break;
//                    case 1:
//                        //fs[currentDeep].setPoint(i,fs[currentDeep].getPoint(j));
//                        //fs[currentDeep].setPoint(j,fs[currentDeep].getPoint(fs[currentDeep].getNumberOfPoints() -1));
//                        fs[currentDeep].swap(i,j);
//                        fs[currentDeep].swap(j,fs[currentDeep].getNumberOfPoints() -1);
//                        fs[currentDeep].descendNumberOfPoints();
//                        j=i;
//                        break;
//                    case 2:
//                        //fs[currentDeep].setPoint(j,fs[currentDeep].getPoint(fs[currentDeep].getNumberOfPoints() -1));
//                        fs[currentDeep].swap(j,fs[currentDeep].getNumberOfPoints()-1);
//                        fs[currentDeep].descendNumberOfPoints();
//                        --j;
//                        break;
//                }
//            }
//        }
//
//        for(int i=0;i<safe;++i){
//            for(int j=l;j<fs[currentDeep].getNumberOfPoints();++j){
//                if(dominates1way(fs[currentDeep].getPoint(i),fs[currentDeep].getPoint(j),currentDimension-2)){
//                    fs[currentDeep].swap(j,fs[currentDeep].getNumberOfPoints()-1);
//                    fs[currentDeep].descendNumberOfPoints();
//                    --j;
//                }
//            }
//        }
//
//        int idxAdd = safe;
//        for(int i = l;i<fs[currentDeep].getNumberOfPoints();++i)
//            fs[currentDeep].swap(idxAdd++,i);
//
//        fs[currentDeep].setNumberOfPoints(idxAdd);
//
//        currentDeep++;
//    }

    int dominates2way(Point p, Point q,int idxBegin) {
        // returns -1 if p dominates q, 1 if q dominates p, 2 if p == q, 0 otherwise
        // ASSUMING MINIMIZATION

        // domination could be checked in either order

        for (int i = idxBegin ; i >= 0; i--) {
            if (beats(p.getDimensionValue(i),q.getDimensionValue(i))) {
                for (int j = i - 1; j >= 0; j--) {
                    if (beats(q.getDimensionValue(j) , p.getDimensionValue(j))) {
                        return 0;
                    }
                }
                return -1;
            } else if (beats(q.getDimensionValue(i) , p.getDimensionValue(i))) {
                for (int j = i - 1; j >= 0; j--) {
                    if (beats(p.getDimensionValue(j) , q.getDimensionValue(j))) {
                        return 0;
                    }
                }
                return 1;
            }
        }
        return 2;
    }

    boolean dominates1way(Point p, Point q, int idxBegin)
// returns true if p dominates q or p == q, false otherwise
// the assumption is that q doesn't dominate p
    {
        for (int i = idxBegin; i >= 0; i--)
            if(beats(q.getDimensionValue(i),p.getDimensionValue(i)))
                return false;
        return true;
    }


    public double getInclHV(Point p)
// returns the inclusive hypervolume of p
    {
//        JMetalLogger.logger.info("<getInclHV>");
        double volume = 1;
        for (int i = 0; i < currentDimension; i++)
            volume *= p.getDimensionValue(i);
        return volume;
    }


    double getInclHV2(Point p, Point q)
// returns the hypervolume of {p, q}
    {
        double vp  = 1; double vq  = 1;
        double vpq = 1;
        for (int i = 0; i < currentDimension; i++)
        {
            vp  *= p.getDimensionValue(i);
            vq  *= q.getDimensionValue(i);
            vpq *= worse(p.getDimensionValue(i),q.getDimensionValue(i));
        }
        return vp + vq - vpq;
    }


    double getInclHV3(Point p, Point q, Point r)
// returns the hypervolume of {p, q, r}
    {
        double vp   = 1; double vq   = 1; double vr   = 1;
        double vpq  = 1; double vpr  = 1; double vqr  = 1;
        double vpqr = 1;
        for (int i = 0; i < currentDimension; i++)
        {
            vp *= p.getDimensionValue(i);
            vq *= q.getDimensionValue(i);
            vr *= r.getDimensionValue(i);
            if (beats(p.getDimensionValue(i),q.getDimensionValue(i))){
                if (beats(q.getDimensionValue(i),r.getDimensionValue(i)))
                {
                    vpq  *= q.getDimensionValue(i);
                    vpr  *= r.getDimensionValue(i);
                    vqr  *= r.getDimensionValue(i);
                    vpqr *= r.getDimensionValue(i);
                }
                else
                {
                    vpq  *= q.getDimensionValue(i);
                    vpr  *= worse(p.getDimensionValue(i),r.getDimensionValue(i));
                    vqr  *= q.getDimensionValue(i);
                    vpqr *= q.getDimensionValue(i);
                }
            }
            else if (beats(p.getDimensionValue(i),r.getDimensionValue(i)))
            {
                vpq  *= p.getDimensionValue(i);
                vpr  *= r.getDimensionValue(i);
                vqr  *= r.getDimensionValue(i);
                vpqr *= r.getDimensionValue(i);
            } else {
                vpq  *= p.getDimensionValue(i);
                vpr  *= p.getDimensionValue(i);
                vqr  *= worse(q.getDimensionValue(i),r.getDimensionValue(i));
                vpqr *= p.getDimensionValue(i);
            }
        }
        return vp + vq + vr - vpq - vpr - vqr + vpqr;
    }


    double getInclHV4(Point p, Point q, Point r, Point s)
// returns the hypervolume of {p, q, r, s}
    {
        double vp    = 1; double vq   = 1; double vr   = 1; double vs   = 1;
        double vpq   = 1; double vpr  = 1; double vps  = 1; double vqr  = 1; double vqs = 1; double vrs = 1;
        double vpqr  = 1; double vpqs = 1; double vprs = 1; double vqrs = 1;
        double vpqrs = 1;
        for (int i = 0; i < currentDimension; i++)
        {
            vp *= p.getDimensionValue(i);
            vq *= q.getDimensionValue(i);
            vr *= r.getDimensionValue(i);
            vs *= s.getDimensionValue(i);
            if (beats(p.getDimensionValue(i),q.getDimensionValue(i)))
                if (beats(q.getDimensionValue(i),r.getDimensionValue(i)))
                    if (beats(r.getDimensionValue(i),s.getDimensionValue(i)))
                    {
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
                    }
                    else
                    {
                        double z1 = worse(q.getDimensionValue(i),s.getDimensionValue(i));
                        vpq *= q.getDimensionValue(i);
                        vpr *= r.getDimensionValue(i);
                        vps *= worse(p.getDimensionValue(i),s.getDimensionValue(i));
                        vqr *= r.getDimensionValue(i);
                        vqs *= z1;
                        vrs *= r.getDimensionValue(i);
                        vpqr *= r.getDimensionValue(i);
                        vpqs *= z1;
                        vprs *= r.getDimensionValue(i);
                        vqrs *= r.getDimensionValue(i);
                        vpqrs *= r.getDimensionValue(i);
                    }
                else
                if (beats(q.getDimensionValue(i),s.getDimensionValue(i)))
                {
                    vpq *= q.getDimensionValue(i);
                    vpr *= worse(p.getDimensionValue(i),r.getDimensionValue(i));
                    vps *= s.getDimensionValue(i);
                    vqr *= q.getDimensionValue(i);
                    vqs *= s.getDimensionValue(i);
                    vrs *= s.getDimensionValue(i);
                    vpqr *= q.getDimensionValue(i);
                    vpqs *= s.getDimensionValue(i);
                    vprs *= s.getDimensionValue(i);
                    vqrs *= s.getDimensionValue(i);
                    vpqrs *= s.getDimensionValue(i);
                }
                else
                {
                    double z1 = worse(p.getDimensionValue(i),r.getDimensionValue(i));
                    vpq *= q.getDimensionValue(i);
                    vpr *= z1;
                    vps *= worse(p.getDimensionValue(i),s.getDimensionValue(i));
                    vqr *= q.getDimensionValue(i);
                    vqs *= q.getDimensionValue(i);
                    vrs *= worse(r.getDimensionValue(i),s.getDimensionValue(i));
                    vpqr *= q.getDimensionValue(i);
                    vpqs *= q.getDimensionValue(i);
                    vprs *= worse(z1,s.getDimensionValue(i));
                    vqrs *= q.getDimensionValue(i);
                    vpqrs *= q.getDimensionValue(i);
                }
            else
            if (beats(q.getDimensionValue(i),r.getDimensionValue(i)))
                if (beats(p.getDimensionValue(i),s.getDimensionValue(i)))
                {
                    double z1 = worse(p.getDimensionValue(i),r.getDimensionValue(i));
                    double z2 = worse(r.getDimensionValue(i),s.getDimensionValue(i));
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
                }
                else
                {
                    double z1 = worse(p.getDimensionValue(i),r.getDimensionValue(i));
                    double z2 = worse(r.getDimensionValue(i),s.getDimensionValue(i));
                    vpq *= p.getDimensionValue(i);
                    vpr *= z1;
                    vps *= p.getDimensionValue(i);
                    vqr *= r.getDimensionValue(i);
                    vqs *= worse(q.getDimensionValue(i),s.getDimensionValue(i));
                    vrs *= z2;
                    vpqr *= z1;
                    vpqs *= p.getDimensionValue(i);
                    vprs *= z1;
                    vqrs *= z2;
                    vpqrs *= z1;
                }
            else
            if (beats(p.getDimensionValue(i),s.getDimensionValue(i)))
            {
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
            }
            else
            {
                double z1 = worse(q.getDimensionValue(i),s.getDimensionValue(i));
                vpq *= p.getDimensionValue(i);
                vpr *= p.getDimensionValue(i);
                vps *= p.getDimensionValue(i);
                vqr *= q.getDimensionValue(i);
                vqs *= z1;
                vrs *= worse(r.getDimensionValue(i),s.getDimensionValue(i));
                vpqr *= p.getDimensionValue(i);
                vpqs *= p.getDimensionValue(i);
                vprs *= p.getDimensionValue(i);
                vqrs *= z1;
                vpqrs *= p.getDimensionValue(i);
            }
        }
        return vp + vq + vr + vs - vpq - vpr - vps - vqr - vqs - vrs + vpqr + vpqs + vprs + vqrs - vpqrs;
    }

    double getInclHV5(Point p, Point q, Point r, Point s, Point t)
// returns the hypervolume of {p, q, r, s, t}
    {
        double vp = 1;
        double vq = 1;
        double vr = 1;
        double vs = 1;
        double vt = 1;

        double vpq = 1;
        double vpr = 1;
        double vps = 1;
        double vpt = 1;
        double vqr = 1;
        double vqs = 1;
        double vqt = 1;
        double vrs = 1;
        double vrt = 1;
        double vst = 1;

        double vpqr = 1;
        double vpqs = 1;
        double vpqt = 1;
        double vprs = 1;
        double vprt = 1;
        double vpst = 1;
        double vqrs = 1;
        double vqrt = 1;
        double vqst = 1;
        double vrst = 1;

        double vpqrs = 1;
        double vpqrt = 1;
        double vpqst = 1;
        double vprst = 1;
        double vqrst = 1;

        double vpqrst = 1;
        for (int i = 0; i < currentDimension; i++) {
            vp *= p.getDimensionValue(i);
            vq *= q.getDimensionValue(i);
            vr *= r.getDimensionValue(i);
            vs *= s.getDimensionValue(i);
            vt *= t.getDimensionValue(i);
            vpq *= worse(p.getDimensionValue(i),q.getDimensionValue(i));
            vpr *= worse(p.getDimensionValue(i),r.getDimensionValue(i));
            vps *= worse(p.getDimensionValue(i),s.getDimensionValue(i));
            vpt *= worse(p.getDimensionValue(i),t.getDimensionValue(i));
            vqr *= worse(q.getDimensionValue(i),r.getDimensionValue(i));
            vqs *= worse(q.getDimensionValue(i),s.getDimensionValue(i));
            vqt *= worse(q.getDimensionValue(i),t.getDimensionValue(i));
            vrs *= worse(r.getDimensionValue(i),s.getDimensionValue(i));
            vrt *= worse(r.getDimensionValue(i),t.getDimensionValue(i));
            vst *= worse(s.getDimensionValue(i),t.getDimensionValue(i));
            vpqr *= worse(p.getDimensionValue(i),
                    worse(q.getDimensionValue(i),r.getDimensionValue(i)));
            vpqs *= worse(p.getDimensionValue(i),
                    worse(q.getDimensionValue(i),s.getDimensionValue(i)));
            vpqt *= worse(p.getDimensionValue(i),
                    worse(q.getDimensionValue(i),t.getDimensionValue(i)));
            vprs *= worse(p.getDimensionValue(i),
                    worse(r.getDimensionValue(i),s.getDimensionValue(i)));
            vprt *= worse(p.getDimensionValue(i),
                    worse(r.getDimensionValue(i),t.getDimensionValue(i)));
            vpst *= worse(p.getDimensionValue(i),
                    worse(s.getDimensionValue(i),t.getDimensionValue(i)));
            vqrs *= worse(q.getDimensionValue(i),
                    worse(r.getDimensionValue(i),s.getDimensionValue(i)));
            vqrt *= worse(q.getDimensionValue(i),
                    worse(r.getDimensionValue(i),t.getDimensionValue(i)));
            vqst *= worse(q.getDimensionValue(i),
                    worse(s.getDimensionValue(i),t.getDimensionValue(i)));
            vrst *= worse(r.getDimensionValue(i),
                    worse(s.getDimensionValue(i),t.getDimensionValue(i)));
            vpqrs *= worse(worse(p.getDimensionValue(i),q.getDimensionValue(i)),
                    worse(r.getDimensionValue(i),s.getDimensionValue(i)));
            vpqrt *= worse(worse(p.getDimensionValue(i),q.getDimensionValue(i)),
                    worse(r.getDimensionValue(i),t.getDimensionValue(i)));
            vpqst *= worse(worse(p.getDimensionValue(i),q.getDimensionValue(i)),
                    worse(s.getDimensionValue(i),t.getDimensionValue(i)));
            vprst *= worse(worse(p.getDimensionValue(i),r.getDimensionValue(i)),
                    worse(s.getDimensionValue(i),t.getDimensionValue(i)));
            vqrst *= worse(worse(q.getDimensionValue(i),r.getDimensionValue(i)),
                    worse(s.getDimensionValue(i),t.getDimensionValue(i)));
            vpqrst *= worse(worse(p.getDimensionValue(i),
                    worse(q.getDimensionValue(i),r.getDimensionValue(i))),
                    worse(s.getDimensionValue(i),t.getDimensionValue(i)));
        }
        return vp + vq + vr + vs + vt
                - vpq  - vpr  - vps  - vpt  - vqr  - vqs  - vqt  - vrs  - vrt  - vst
                + vpqr + vpqs + vpqt + vprs + vprt + vpst + vqrs + vqrt + vqst + vrst
                - vpqrs - vpqrt - vpqst - vprst - vqrst
                + vpqrst;
    }

}
