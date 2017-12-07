package org.uma.jmetal.experiment.data;

/**
 * Created by weaponhe on 2017/12/4.
 */
public class CombinComputer {
    //nObj H size
    //3 140 10011
    //5 12 1820
    //8 9 11440
    //10 7 11440
    //15 5 11628
    public static void main(String[] args) {
        int nObj = 3;
        for (int H = 3; H < 20; H++) {
            int size = compute(H + nObj - 1, nObj - 1);
            System.out.println("nObj=" + nObj + "; H=" + H + "; size=" + size);
        }
    }

    static public int compute(int m, int n) {
        n = Math.min(n, m - n);
        int denominator = 1;//分母
        int numerator = 1;//分子
        while (n >= 1) {
            denominator *= n--;
            numerator *= m--;
        }
        return numerator / denominator;
    }
}
