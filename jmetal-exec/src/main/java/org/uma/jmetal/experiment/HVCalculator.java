package org.uma.jmetal.experiment;

import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by X250 on 2016/6/15.
 */
public class HVCalculator {
    public static double DTLZ1(double[] ref,int nObj,double r){
        double cube = 1.0;
        double v = 1.0;
        for(int i =0;i<nObj;i++){
            cube *= ref[i];
            v *= (i+1);
        }
        v = Math.pow(r,nObj)/v;
        return cube - v;
    }
    public static double DTLZ2(double[] ref,int nObj,double r){
        double cube = 1.0;
        double v = 1.0;
        for(int i =0;i<nObj;i++){
            cube *= ref[i];
        }
        if(nObj%2 == 0){
            for (int i=1;i<= nObj/2;i++)
                v *= i;
        }else{
            for(int i=1;i<=nObj+1;i++)
                v*=i;
            for (int i=1;i<= (nObj+1)/2;i++)
                v /= i;
            v *= Math.sqrt(Math.PI);
            v /= Math.pow(2,nObj+1);
        }
        v = 1.0/v * Math.pow(Math.PI,(1.0*nObj)/2)*Math.pow(r,nObj)/Math.pow(2,nObj);

        return cube - v;
    }

    public static double Convex_DTLZ2(double[] ref,int nObj,double r){
        double cube = 1.0;
        double v = 1.0;
        for(int i=0;i<nObj;i++)
            cube *= ref[i];
        for(int i=2*nObj-1;i>= 1;i++){
            v *= i;
        }
        v = Math.pow(2,nObj-1)*Math.pow(r,1.0*nObj-0.5)/v;
        return cube - v;
    }

    public static double MOP4(double[] ref) {
        int K = 20000;

        double a1 = 0.0764094027926891659096589633;
        double a2 = 0.353377632511718642617795481;
        double a3 = 0.512335450555583331928235361;
        double a4 = 0.915494919683592913101560384;
        ArrayList<double[]> pf = new ArrayList<>(K);
        for (int i = 0; i < K - 1; i++) {
            double[] p = new double[2];
            p[0] = (1.0 * i) / (K - 1);
            p[1] = 1.0 - Math.sqrt(p[0]) * Math.pow(Math.cos(2 * Math.PI * p[0]), 2.0);
            if (p[0] <= a1 || (p[0] >= a2 && p[0] <= a3) || p[0] >= a4)
                pf.add(p);
        }

        BufferedWriter writer = new DefaultFileOutputContext("D:\\Experiments\\UniformWeights\\MOP4.pf").getFileWriter();
        try {
            for (int m = 0; m < pf.size(); ++m) {
                for (int n = 0; n < 2; n++) {
                    writer.write("" + pf.get(m)[n] + " ");
                }
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
        }
        Point refPoint = new ArrayPoint(2);
        refPoint.setDimensionValue(0,ref[0]);
        refPoint.setDimensionValue(1,ref[1]);

        WfgHypervolumeFront front = null;
        try {

            front = new WfgHypervolumeFront("D:\\Experiments\\UniformWeights\\MOP4.pf");
        }catch (FileNotFoundException e){}

        WFGHypervolume hvIndicator = new WFGHypervolume();
        hvIndicator.setMiniming();
        return  hvIndicator.evaluate(front,refPoint);
    }

    public static void main(String[] argv){
        int[] obj = {
                3,
                5,
                8,
                10,
                15
        };
        {
            double[][] ref = {
//                    {0.7, 0.7, 0.7},
//                    {0.7, 0.7, 0.7, 0.7, 0.7},
//                    {0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7},
//                    {0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7},
//                    {0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7}
                    {0.55, 0.55, 0.55},
                    {0.55, 0.55, 0.55, 0.55, 0.55},
                    {0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55},
                    {0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55},
                    {0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55, 0.55}
            };
            for (int i = 0; i < obj.length; i++) {
                double hv = HVCalculator.DTLZ1(ref[i],obj[i],0.5);
                System.out.printf("DTLZ1<%d>\t%5.30f\n",obj[i], hv);
                double normS = 1.0;
                for (int j=0;j<obj[i];j++)
                    normS *= 1.0/ref[i][j];
                System.out.printf("Normlized HV\t%5.30f\n",hv*normS);
            }
        }
        {
//            double[][] ref = {
//                   {1.1,1.1,1.1},
//                   {1.1,1.1,1.1,1.1,1.1},
//                    {1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1},
//                    {1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1},
//                    {1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1}
//            };
//            for (int i = 0; i < obj.length; i++) {
//                double hv = HVCalculator.DTLZ2(ref[i],obj[i],1.0);
//                System.out.printf("DTLZ2(3,4)<%d>\t%5.20f\n",obj[i], hv);
//            }
        }

        {
//            double[][] ref = {
//                    {1.1,1.1,1.1},
//                    {1.1,1.1,1.1,1.1,1.1},
//                    {1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1},
//                    {1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1},
//                    {1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1,1.1}
//            };
//            for (int i = 0; i < obj.length; i++) {
//                double hv = HVCalculator.Convex_DTLZ2(ref[i],obj[i],1.0);
//                System.out.printf("ConvexDTLZ2<%d>\t%5.20f\n",obj[i], hv);
//            }
        }
        {
//            double[] ref = {1.1, 1.1};
//
//            System.out.printf("MOP1(5)<%d>\t%5.20f\n", 2, HVCalculator.Convex_DTLZ2(ref, 2,1.0));
//
//            System.out.printf("MOP3<%d>\t%5.20f\n", 2, HVCalculator.DTLZ2(ref, 2,1.0));
//
//            System.out.printf("MOP4<%d>\t%5.20f\n", 2,  HVCalculator.MOP4(ref));

//            double[] ref1 = {1.1,1.1,1.1};
//            double[] ref2 = {0.7,0.7,0.7};
//            System.out.printf("MOP6<%d>\t%5.20f\n", 3, HVCalculator.DTLZ1(ref1, 3,1.0));
//            System.out.printf("MOP7<%d>\t%5.20f\n", 3, HVCalculator.DTLZ1(ref2, 3,0.5));
        }
    }
}
