package org.uma.jmetal.util;

/**
 * Created by X250 on 2016/4/10.
 */
public class CombinationUtils {

    private static double lnCombination(int _n, int _m) {
        if (_m > _n)
            return 0;
        if (_m < _n/2.0)
            _m = _n-_m;
        double s1 = 0;
        for (int i=_m+1; i<=_n; i++)
            s1 += Math.log((double)i);
        double s2 = 0;
        int ub = _n-_m;
        for (int i=2; i<=ub; i++)
            s2 += Math.log((double)i);
        return s1-s2;
    }
    //Calculating Combination Number
    public static int compute(int _n, int _m) {
        /*
        int numerator = 1;
        int denominator = 1;
        for (int i = _n; i > _n - _m; i--)
            numerator *= i;
        for (int i = _m; i > 1; i--)
            denominator *= i;
        return numerator / denominator;
        */
        if (_m > _n)
            return 0;
        return (int)Math.round(Math.exp(lnCombination(_n, _m)));
    }
}
