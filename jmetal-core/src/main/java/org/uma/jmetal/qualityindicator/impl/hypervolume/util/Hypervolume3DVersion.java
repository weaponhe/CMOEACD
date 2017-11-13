package org.uma.jmetal.qualityindicator.impl.hypervolume.util;

import org.uma.jmetal.qualityindicator.impl.Hypervolume;
import org.uma.jmetal.util.point.util.comparator.PointComparator;

/**
 * Created by X250 on 2016/5/28.
 */
public class Hypervolume3DVersion {
    public Hypervolume3DVersion(){
    }

    public double getHV(WfgHypervolumeFront front) {
        double volume = 0.0;

//        if (m_initial_sorting) {
//            sort(points.begin(), points.end(), fitness_vector_cmp(2,'<'));
//        }
//        double V = 0.0; // hypervolume
//        double A = 0.0; // area of the sweeping plane
//        std::multiset<fitness_vector, fitness_vector_cmp> T(fitness_vector_cmp(0, '>'));
//
//        // sentinel points (r_point[0], -INF, r_point[2]) and (-INF, r_point[1], r_point[2])
//        const double INF = std::numeric_limits<double>::max();
//        fitness_vector sA(r_point.begin(), r_point.end()); sA[1] = -INF;
//        fitness_vector sB(r_point.begin(), r_point.end()); sB[0] = -INF;
//
//        T.insert(sA);
//        T.insert(sB);
//        double z3 = points[0][2];
//        T.insert(points[0]);
//        A = fabs((points[0][0] - r_point[0]) * (points[0][1] - r_point[1]));
//
//        std::multiset<fitness_vector>::iterator p;
//        std::multiset<fitness_vector>::iterator q;
//        for(std::vector<fitness_vector>::size_type idx = 1 ; idx < points.size() ; ++idx) {
//            p = T.insert(points[idx]);
//            q = (p);
//            ++q; //setup q to be a successor of p
//            if ( (*q)[1] <= (*p)[1] ) { // current point is dominated
//                T.erase(p); // disregard the point from further calculation
//            } else {
//                V += A * fabs(z3 - (*p)[2]);
//                z3 = (*p)[2];
//                std::multiset<fitness_vector>::reverse_iterator rev_it(q);
//                ++rev_it;
//
//                std::multiset<fitness_vector>::reverse_iterator erase_begin (rev_it);
//                std::multiset<fitness_vector>::reverse_iterator rev_it_pred;
//                while((*rev_it)[1] >= (*p)[1] ) {
//                    rev_it_pred = rev_it;
//                    ++rev_it_pred;
//                    A -= fabs(((*rev_it)[0] - (*rev_it_pred)[0])*((*rev_it)[1] - (*q)[1]));
//                    ++rev_it;
//                }
//                A += fabs(((*p)[0] - (*(rev_it))[0])*((*p)[1] - (*q)[1]));
//                T.erase(rev_it.base(),erase_begin.base());
//            }
//        }
//        V += A * fabs(z3 - r_point[2]);

        return volume;
    }
}