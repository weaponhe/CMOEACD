package org.uma.jmetal.algorithm.multiobjective.udea;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.Constant;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2017/1/3.
 */
public class Utils {

    public static double dot(double[] _vec1,double[] _vec2){
        double sum = 0.0;
        for(int i=0;i<_vec1.length;++i)
            sum += (_vec1[i] * _vec2[i]);
        return sum;
    }

    public  static double norm(double[] _vec){
        double sum = 0.0;
        for(int i=0;i<_vec.length;++i)
            sum += (_vec[i] * _vec[i]);
        return Math.sqrt(sum);
    }
    public static double distance2(double[] _v1,double[] _v2){
        int nobj = _v1.length;
        double sum =0;
        for(int i=0;i<nobj;++i)
            sum += (_v1[i] - _v2[i]) * (_v1[i] - _v2[i]);
        return sum;
    }

    public static double distance(double[] _v1,double[] _v2){
        return Math.sqrt(distance2(_v1,_v2));
    }


    public static void minFastSort(double x[], int idx[], int n, int m) {
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < n; j++) {
                if (x[i] > x[j]) {
                    double temp = x[i];
                    x[i] = x[j];
                    x[j] = temp;
                    int id = idx[i];
                    idx[i] = idx[j];
                    idx[j] = id;
                }
            }
        }
    }


    public static void sortIdxByData(List<Double> data, List<Integer> idx){
        sortIdxByData(data,idx,0,data.size()-1,true);
    }
    public static  void sortIdxByData(List<Double>data,List<Integer> idx,boolean isMin2Max){
        sortIdxByData(data,idx,0,data.size()-1,isMin2Max);
    }
    public static  void sortIdxByData(List<Double>data,List<Integer> idx,int ib,int ik){
        sortIdxByData(data,idx,ib,ik,true);
    }
    public static  void sortIdxByData(List<Double> data, List<Integer> idx, int ib, int ik,boolean isMin2Max){
        for (int i = ib;i<=ik;i++){
            int selected = i;
            double selectedData = data.get(i);
            int selectedIdx = idx.get(i);
            for (int j= i+1;j<=ik;j++){
                if((isMin2Max && data.get(j) < selectedData) || (!isMin2Max && data.get(j) > selectedData)){
                    selectedData = data.get(j);
                    selectedIdx = idx.get(j);
                    selected = j;
                }
            }
            if(selected != i){
                data.set(selected,data.get(i));
                idx.set(selected,idx.get(i));
                data.set(i,selectedData);
                idx.set(i,selectedIdx);
            }
        }
    }

    public static void invert(List<Integer> data) {
        int ib = 0;
        int ik = data.size() - 1;

        while (ib < ik) {
            int tmp = data.get(ib);
            data.set(ib,data.get(ik));
            data.set(ik,tmp);
            ib++;
            ik--;
        }
    }

    public static double[] translate(Solution<?> _individual, double[] _idealPoint){
        int nobj = _idealPoint.length;
        double[] translatedObjectives = new double[nobj];
        for(int i=0;i<nobj;++i)
            translatedObjectives[i] = _individual.getObjective(i) - _idealPoint[i];

        return translatedObjectives;
    }

    public static double[] normalize(Solution<?> _individual, double[] _idealPoint,double[] intercepts){
        return normalize(translate(_individual,_idealPoint),intercepts);
    }

    public static double[] normalize(double[] translatedObj,double[] intercepts){
        int nobj = translatedObj.length;
        double[] normObjectives = new double[nobj];
        for(int i=0;i<nobj;++i)
            normObjectives[i] = translatedObj[i] / intercepts[i];

        return normObjectives;
    }

    //calculate the observation vector
    public static double[] calObservation(double[] _normObjectives) {
        int nObj = _normObjectives.length;
        double[] observationV = new double[nObj];
        if(nObj == 1){
            observationV[0] = _normObjectives[0];
        }else {
            for (int i = 0; i < nObj; ++i) observationV[i] = 0;
            double sum = 0.0;
            for (double n : _normObjectives)
                sum += n;

            if (sum <= Constant.TOLERATION) {
                observationV[0] = 1.0;
            } else {
                for (int i = 0; i < nObj; ++i)
                    observationV[i] = _normObjectives[i] / sum;
            }
        }
        return observationV;
    }
    public static int dominateCompare(DoubleSolution ind1, DoubleSolution ind2){

        for(int i=0;i<ind1.getNumberOfObjectives();++i){
            if(ind1.getObjective(i) < ind2.getObjective(i)){
                for(int j=i+1;j<ind1.getNumberOfObjectives();++j)
                    if(ind1.getObjective(j) > ind2.getObjective(j))
                        return 0;
                return -1;
            }
            else if(ind1.getObjective(i) > ind2.getObjective(i)){
                for (int j=i+1;j<ind1.getNumberOfObjectives();++j)
                    if (ind1.getObjective(j) < ind2.getObjective(j))
                        return 0;
                return 1;
            }
        }
        return 2;
    }

    public static List<Double> guassianElimination(List<List<Double>> A, List<Double> b) {
        List<Double> x = new ArrayList<>();

        int N = A.size();
        for (int i=0; i<N; i+=1)
        {
            A.get(i).add(b.get(i));
        }

        for (int base=0; base<N-1; base+=1)
        {
            //swap line
            double maxV = Math.abs(A.get(base).get(base));
            int iMax = base;
            for (int k = base + 1;k < A.get(base).size() - 1;k++){
                if(Math.abs(A.get(k).get(base)) > maxV){
                    iMax = k;
                    maxV = Math.abs(A.get(k).get(base));
                }
            }
            if(iMax != base){
                for (int k = base;k<A.get(base).size();k++){
                    double tmp = A.get(base).get(k);
                    A.get(base).set(k,A.get(iMax).get(k));
                    A.get(iMax).set(k,tmp);
                }
            }

            for (int target=base+1; target<N; target+=1)
            {
                double ratio = A.get(target).get(base)/A.get(base).get(base);
                for (int term=base; term<A.get(base).size(); term+=1)
                {
                    A.get(target).set(term, A.get(target).get(term) - A.get(base).get(term)*ratio);
                }

            }
        }

        for (int i = 0; i < N; i++)
            x.add(0.0);

        for (int i=N-1; i>=0; i-=1)
        {
            for (int known=i+1; known<N; known+=1)
            {
                A.get(i).set(N, A.get(i).get(N) - A.get(i).get(known)*x.get(known));
            }
            x.set(i, A.get(i).get(N)/A.get(i).get(i));
        }
        return x;
    }


    public static void randomPermutation(int[] perm, int size) {
        JMetalRandom randomGenerator = JMetalRandom.getInstance() ;
        int[] index = new int[size];
        boolean[] flag = new boolean[size];

        for (int n = 0; n < size; n++) {
            index[n] = n;
            flag[n] = true;
        }

        int num = 0;
        while (num < size) {
            int start = randomGenerator.nextInt(0, size - 1);
            while (true) {
                if (flag[start]) {
                    perm[num] = index[start];
                    flag[start] = false;
                    num++;
                    break;
                }
                if (start == (size - 1)) {
                    start = 0;
                } else {
                    start++;
                }
            }
        }
    }

    public static double leftGaussian( double progress,double alpha,double pivot){
//        double c = 0;
//        double alpha = 0.25;
        return Math.exp(-0.5*Math.pow((progress - pivot)/alpha,2.0));
    }


    public static double sigmoid(double progress, double shrink, double pivot) {
        return 1.0 / (1.0 + Math.exp(shrink * (progress - pivot)));
    }

}
