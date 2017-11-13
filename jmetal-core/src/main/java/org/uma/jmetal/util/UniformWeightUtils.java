package org.uma.jmetal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by X250 on 2016/4/10.
 */
public class UniformWeightUtils {

    public static double[][] generateArray(int[] _arrayH,double[] _integratedTau,int _nObj){

        Vector<Vector<Double>> resultV = generateVector(_arrayH,_integratedTau,_nObj);
        double[][] resultA = new double[resultV.size()][_nObj];
        for(int i=0;i< resultV.size();++i)
            for(int j=0;j<_nObj;++j)
                resultA[i][j] = resultV.get(i).get(j);
        return resultA;
    }

    public static List<double[]> generateArrayList(int[] _arrayH, double[] _integratedTau, int _nObj){

        Vector<Vector<Double>> resultV = generateVector(_arrayH,_integratedTau,_nObj);
        List<double[]> resultA = new ArrayList<>(resultV.size());
        for(int i=0;i< resultV.size();++i) {
            double[] weight = new double[_nObj];
            for (int j = 0; j < _nObj; ++j)
                weight[j] = resultV.get(i).get(j);
            resultA.add(weight);
        }
        return resultA;
    }

    public static int[][] generateMIndexesArray(int _H, int _nObj){

        Vector<Vector<Integer>> resultV = generateMIndexesVector(_H,_nObj);
        int[][] resultA = new int[resultV.size()][_nObj];
        for(int i=0;i< resultV.size();++i)
            for(int j=0;j<_nObj;++j)
                resultA[i][j] = resultV.get(i).get(j);
        return resultA;
    }

    public static Vector<Vector<Double>> generateVector(int[] _arrayH,double[] _integratedTau,int _nObj){
        int nums = 0;
        for(int i =0;i<_arrayH.length;++i)
            nums += CombinationUtils.compute(_arrayH[i]+_nObj-1,_nObj-1);

        Vector<Vector<Double>> uniformWeights = new Vector<Vector<Double>>(nums);
        for(int i=0;i<_arrayH.length;++i) {
            Vector<Integer> mIndex = new Vector<Integer>();
            for(int j=0;j<_nObj;++j) mIndex.add(0);
            generateWeightRecursion(_arrayH[i], _integratedTau[i], _nObj, _nObj-1, _arrayH[i], mIndex,uniformWeights);
        }
        return uniformWeights;
    }

    public static Vector<Vector<Integer>> generateMIndexesVector(int _H,int _nObj){
        int nums = CombinationUtils.compute(_H +_nObj-1,_nObj-1);
        Vector<Vector<Integer>> uniformMIndexes = new Vector<Vector<Integer>>(nums);
        Vector<Integer> mIndex = new Vector<Integer>();
        for(int j=0;j<_nObj;++j) mIndex.add(0);
        generateMIndexRecursion(_H, _nObj, _nObj-1, _H, mIndex,uniformMIndexes);
        return uniformMIndexes;
    }
    private static void generateWeightRecursion(Integer _H,Double _tau, Integer _nObj, Integer _idxStart,Integer _leftMax, Vector<Integer> _mIndex,Vector<Vector<Double>> _uniformWeights){
        if (0 == _idxStart || 0 == _leftMax)
        {
            //got one
            _mIndex.set(_idxStart, _leftMax);
            for(int i=0;i< _idxStart;++i)
                _mIndex.set(i , 0);

            //transform mIndex to weight
            Vector<Double> weight = new Vector<Double>();
            for(int i=0;i<_nObj;i++) {
                if(_H.equals(0))
                    weight.add((1.0 - _tau) / _nObj + _tau * _mIndex.get(i));
                else
                    weight.add((1.0 - _tau) / _nObj + _tau * _mIndex.get(i) / _H);
            }
            _uniformWeights.add(weight);
            return;
        }
        for (int i = _leftMax; i >= 0; i--)
        {
            _mIndex.set(_idxStart,i);
            generateWeightRecursion(_H,_tau,_nObj, _idxStart - 1, _leftMax - i, _mIndex, _uniformWeights);
        }
    }

    private static void generateMIndexRecursion(Integer _H, Integer _nObj, Integer _idxStart,Integer _leftMax, Vector<Integer> _mIndex,Vector<Vector<Integer>> _uniformMIndexes){
        if (0 == _idxStart || 0 == _leftMax)
        {
            //got one
            _mIndex.set(_idxStart, _leftMax);
            for(int i=0;i< _idxStart;++i)
                _mIndex.set(i , 0);

            Vector<Integer> copyMIndex = new Vector<>();
            for(int i=0;i<_nObj;++i) copyMIndex.add(_mIndex.get(i));
            _uniformMIndexes.add(copyMIndex);
            return;
        }
        for (int i = _leftMax; i >= 0; i--)
        {
            _mIndex.set(_idxStart,i);
            generateMIndexRecursion(_H,_nObj, _idxStart - 1, _leftMax - i, _mIndex, _uniformMIndexes);
        }
    }

    private static Vector<Double> mIndex2Weight(Vector<Integer> _mIndex,Integer _H,Double _tau,int _nObj){
        Vector<Double> weight = new Vector<>(_nObj);
        for(int i=0;i<_nObj;++i){
            if(_H.equals(0))
                weight.set(i,(1.0 - _tau) / _nObj + _tau * _mIndex.get(i));
            else
                weight.set(i,(1.0 - _tau) / _nObj + _tau * _mIndex.get(i) / _H);
        }
        return weight;
    }

    public static Vector<Vector<Double>> mIndexes2Weights(Vector<Vector<Integer>> _uniformMIndexes,Integer _H,Double _tau,int _nObj) {
        Vector<Vector<Double>> uniformWeights = new Vector<Vector<Double>>(_uniformMIndexes.size());
        for (int i = 0; i < _uniformMIndexes.size(); ++i) {
            uniformWeights.set(i, mIndex2Weight(_uniformMIndexes.get(i), _H, _tau, _nObj));
        }
        return uniformWeights;
    }
}
