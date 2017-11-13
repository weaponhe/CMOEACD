package org.uma.jmetal.experiment;

import org.uma.jmetal.qualityindicator.impl.GeneralizedSpread;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.Space;
import org.uma.jmetal.qualityindicator.impl.hypervolume.ApproximateHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.util.WfgHypervolumeFront;
import org.uma.jmetal.solution.SolutionEvaluator;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.UniformSimplexCentroidWeightsUtils;
import org.uma.jmetal.util.UniformWeightUtils;
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
 * Created by X250 on 2016/6/14.
 */
public class TestUniformWeights {

    public static void main(String []argv){

//        int nObj = 3;
//        int[][] H1 = {{12},{13},{22},{23},{24}};
//        double[][] tao = {{1.0},{1.0},{1.0},{1.0},{1.0}};
//        int[] H2 = {7,8,13,14};

//        int nObj = 5;
//        int[][] H1 = {{5},{6},{7},{8},{3,4},{4,4}};
//        double[][] tao = {{1.0},{1.0},{1.0},{1.0},{0.5,1.0},{0.5,1.0}};
//        int[] H2 = {4,5,6};

//        int nObj = 8;
//        int[][] H1 = {{8},{2,3},{3,4},{3,3}};
//        double[][] tao = {{1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0}};
//        int[] H2 = {2,3,4,7};

//        int nObj = 10;
//        int[][] H1 = {{1,2},{2,2},{2,3},{3,3},{3,4},{4,4}};
//        double[][] tao = {{0.5,1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0}};
//        int[] H2 = {2,3,4};
//        int nObj = 15;
//        int[][] H1 = {{1,2},{2,2},{2,3},{3,3}};
//        double[][] tao = {{0.5,1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0}};
//        int[] H2 = {2,3,4};
//
//        for(int i=0;i<H1.length;++i){
//            double[][] weights = UniformWeightUtils.generateArray(H1[i],tao[i],nObj);
//            int popsize =weights.length;
//            String instance = "simplexWeights."+nObj+"D[";
//            for(int j=0;j<H1[i].length;j++)
//                instance+=H1[i][j]+"-";
//            instance += popsize;
//            instance += "].dat";
//            BufferedWriter writer = new DefaultFileOutputContext("D:\\Experiments\\UniformWeights\\"+instance).getFileWriter();
//            try {
//                for (int m = 0; m < weights.length; ++m) {
//                    for (int n = 0; n < nObj; n++) {
//                        writer.write("" + weights[m][n] + " ");
//                    }
//                    writer.newLine();
//                }
//                writer.close();
//            }catch (IOException e){}
//        }
//
//        for(int i=0;i<H2.length;++i){
//            UniformSimplexCentroidWeightsUtils uniformSimplexCentroidWeightsUtils = new UniformSimplexCentroidWeightsUtils(nObj,H2[i]);
//            ArrayList<double[]> weights = uniformSimplexCentroidWeightsUtils.getUniformWeights();
//            int popsize =weights.size();
//            String instance = "centriodWeights."+nObj+"D["+H2[i]+"-";
//            instance += popsize;
//            instance += "].dat";
//            BufferedWriter writer = new DefaultFileOutputContext("D:\\Experiments\\UniformWeights\\"+instance).getFileWriter();
//            try {
//                for (int m = 0; m < weights.size(); ++m) {
//                    for (int n = 0; n < nObj; n++) {
//                        writer.write("" + weights.get(m)[n] + " ");
//                    }
//                    writer.newLine();
//                }
//                writer.close();
//            }catch (IOException e){}
//        }

//
//        int[] obj = {3,5,8,10,15};
//        int[] H = {58,16,9,8,6};
//        for(int i=0;i<H.length;++i){
//            UniformSimplexCentroidWeightsUtils uniformSimplexCentroidWeightsUtils = new UniformSimplexCentroidWeightsUtils(obj[i],H[i]);
//            ArrayList<double[]> weights = uniformSimplexCentroidWeightsUtils.getUniformWeights();
//            int popsize =weights.size();
//            String instance = "ref."+obj[i]+"D["+H[i]+"-";
//            instance += popsize;
//            instance += "].dat";
//            BufferedWriter writer = new DefaultFileOutputContext("D:\\Experiments\\UniformWeights\\"+instance).getFileWriter();
//            try {
//                for (int m = 0; m < weights.size(); ++m) {
//                    for (int n = 0; n < obj[i]; n++) {
//                        writer.write("" + weights.get(m)[n] + " ");
//                    }
//                    writer.newLine();
//                }
//                writer.close();
//            }catch (IOException e){}
//        }
//        String dir = "D:\\Experiments\\UniformWeights\\";
//        String[] instance = {
//                dir +"simplexWeights.3D[12-91].dat",
//                dir +"simplexWeights.3D[13-105].dat",
//                dir +"simplexWeights.3D[22-276].dat",
//                dir +"simplexWeights.3D[23-300].dat",
//                dir +"simplexWeights.3D[24-325].dat",
//                dir +"simplexWeights.5D[3-4-105].dat",
//                dir +"simplexWeights.5D[4-4-140].dat",
//                dir +"simplexWeights.5D[5-126].dat",
//                dir +"simplexWeights.5D[6-210].dat",
//                dir +"simplexWeights.5D[7-330].dat",
//                dir +"simplexWeights.5D[8-495].dat",
//                dir +"simplexWeights.8D[8-6435].dat",
//                dir +"simplexWeights.8D[2-3-156].dat",
//                dir +"simplexWeights.8D[3-3-240].dat",
//                dir +"simplexWeights.8D[3-4-450].dat",
//                dir +"simplexWeights.10D[1-2-65].dat",
//                dir +"simplexWeights.10D[2-2-110].dat",
//                dir +"simplexWeights.10D[3-3-440].dat",
//                dir +"simplexWeights.10D[3-4-935].dat",
//                dir +"simplexWeights.10D[4-4-1430].dat",
//                dir +"simplexWeights.15D[1-2-135].dat",
//                dir +"simplexWeights.15D[2-2-240].dat",
//                dir +"simplexWeights.15D[2-3-800].dat",
//                dir +"simplexWeights.15D[3-3-1360].dat",
//
//                dir +"centriodWeights.3D[7-85].dat",
//                dir +"centriodWeights.3D[8-109].dat",
//                dir +"centriodWeights.3D[13-274].dat",
//                dir +"centriodWeights.3D[14-316].dat",
//                dir +"centriodWeights.5D[4-126].dat",
//                dir +"centriodWeights.5D[5-251].dat",
//                dir +"centriodWeights.5D[6-456].dat",
//                dir +"centriodWeights.8D[2-45].dat",
//                dir +"centriodWeights.8D[3-165].dat",
//                dir +"centriodWeights.8D[4-495].dat",
//                dir +"centriodWeights.8D[7-6435].dat",
//                dir +"centriodWeights.10D[2-66].dat",
//                dir +"centriodWeights.10D[3-286].dat",
//                dir +"centriodWeights.10D[4-1001].dat",
//                dir +"centriodWeights.15D[2-136].dat",
//                dir +"centriodWeights.15D[3-816].dat",
//                dir +"centriodWeights.15D[4-3876].dat"
//        };
//
//        String[] refFile = {
//                dir+"ref.3D[58-5134].dat",
//                dir+"ref.3D[58-5134].dat",
//                dir+"ref.3D[58-5134].dat",
//                dir+"ref.3D[58-5134].dat",
//                dir+"ref.3D[58-5134].dat",
//                dir+"ref.5D[16-15981].dat",
//                dir+"ref.5D[16-15981].dat",
//                dir+"ref.5D[16-15981].dat",
//                dir+"ref.5D[16-15981].dat",
//                dir+"ref.5D[16-15981].dat",
//                dir+"ref.5D[16-15981].dat",
//                dir+"ref.8D[9-24301].dat",
//                dir+"ref.8D[9-24301].dat",
//                dir+"ref.8D[9-24301].dat",
//                dir+"ref.8D[9-24301].dat",
//                dir+"ref.10D[8-43758].dat",
//                dir+"ref.10D[8-43758].dat",
//                dir+"ref.10D[8-43758].dat",
//                dir+"ref.10D[8-43758].dat",
//                dir+"ref.10D[8-43758].dat",
//                dir+"ref.15D[6-54264].dat",
//                dir+"ref.15D[6-54264].dat",
//                dir+"ref.15D[6-54264].dat",
//                dir+"ref.15D[6-54264].dat",
//
//                dir+"ref.3D[58-5134].dat",
//                dir+"ref.3D[58-5134].dat",
//                dir+"ref.3D[58-5134].dat",
//                dir+"ref.3D[58-5134].dat",
//                dir+"ref.5D[16-15981].dat",
//                dir+"ref.5D[16-15981].dat",
//                dir+"ref.5D[16-15981].dat",
//                dir+"ref.8D[9-24301].dat",
//                dir+"ref.8D[9-24301].dat",
//                dir+"ref.8D[9-24301].dat",
//                dir+"ref.8D[9-24301].dat",
//                dir+"ref.10D[8-43758].dat",
//                dir+"ref.10D[8-43758].dat",
//                dir+"ref.10D[8-43758].dat",
//                dir+"ref.15D[6-54264].dat",
//                dir+"ref.15D[6-54264].dat",
//                dir+"ref.15D[6-54264].dat",
//        };
//
//        try {
//
//            BufferedWriter writer = new DefaultFileOutputContext("D:\\Experiments\\UniformWeights\\analysis.dat").getFileWriter();
//
//
//            for (int i = 0; i < instance.length; i++) {
//                writer.write("<"+instance[i]+">");
//                writer.newLine();
//                Front referenceFront = null;
//                Front weights = null;
//                try {
//                    referenceFront = new ArrayFront(refFile[i]);
//                    weights = new ArrayFront(instance[i]);
//                }catch (FileNotFoundException e){}
//                GeneralizedSpread spread = new GeneralizedSpread();
//                double spd = spread.generalizedSpread(weights,referenceFront);
//                writer.write("spread = "+spd);
//                writer.newLine();
//                Space space = new Space();
//                double sp = space.spaceIndicator(weights);
//                writer.write("space = "+sp);
//                writer.newLine();
//                InvertedGenerationalDistance igdIndicator = new InvertedGenerationalDistance();
//                double igd = igdIndicator.invertedGenerationalDistance(weights,referenceFront);
//                writer.write("IGD = "+igd);
//                writer.newLine();
//                InvertedGenerationalDistancePlus igdPlusIndicator = new InvertedGenerationalDistancePlus();
//                double igdPlus = igdPlusIndicator.invertedGenerationalDistancePlus(weights,referenceFront);
//                writer.write("IGDPlus = "+igdPlus);
//                writer.newLine();
//                JMetalLogger.logger.info("\n"+instance[i]+"\n[spread]"+spd+"\n[space]"+sp+"\n[IGD]"+igd+"\n[IGDPlus]"+igdPlus+"\n");
//            }
//                writer.close();
//            }catch (IOException e){}



        String dir = "D:\\Experiments\\UniformWeights\\";
        String centroid = "simplexCentroid";
        String simplex = "simplex";
        String problem = "Convex_DTLZ2";
        String[] instance = {
                dir +centroid+"\\"+problem+".3D.pf[85]",
                dir +centroid+"\\"+problem+".3D.pf[109]",
                dir +centroid+"\\"+problem+".3D.pf[274]",
                dir +centroid+"\\"+problem+".3D.pf[316]",
                dir +centroid+"\\"+problem+".5D.pf[126]",
                dir +centroid+"\\"+problem+".5D.pf[251]",
                dir +centroid+"\\"+problem+".5D.pf[456]",
                dir +centroid+"\\"+problem+".8D.pf[45]",
                dir +centroid+"\\"+problem+".8D.pf[165]",
                dir +centroid+"\\"+problem+".8D.pf[495]",
                dir +centroid+"\\"+problem+".8D.pf[6435]",
                dir +centroid+"\\"+problem+".10D.pf[66]",
                dir +centroid+"\\"+problem+".10D.pf[286]",
                dir +centroid+"\\"+problem+".10D.pf[1001]",
                dir +centroid+"\\"+problem+".15D.pf[136]",
                dir +centroid+"\\"+problem+".15D.pf[816]",
                dir +centroid+"\\"+problem+".15D.pf[3876]",

                dir +simplex+"\\"+problem+".3D.pf[91]",
                dir +simplex+"\\"+problem+".3D.pf[105]",
                dir +simplex+"\\"+problem+".3D.pf[276]",
                dir +simplex+"\\"+problem+".3D.pf[300]",
                dir +simplex+"\\"+problem+".3D.pf[325]",
                dir +simplex+"\\"+problem+".5D.pf[105]",
                dir +simplex+"\\"+problem+".5D.pf[126]",
                dir +simplex+"\\"+problem+".5D.pf[140]",
                dir +simplex+"\\"+problem+".5D.pf[210]",
                dir +simplex+"\\"+problem+".5D.pf[330]",
                dir +simplex+"\\"+problem+".5D.pf[495]",
                dir +simplex+"\\"+problem+".8D.pf[156]",
                dir +simplex+"\\"+problem+".8D.pf[240]",
                dir +simplex+"\\"+problem+".8D.pf[450]",
                dir +simplex+"\\"+problem+".8D.pf[6435]",
                dir +simplex+"\\"+problem+".10D.pf[65]",
                dir +simplex+"\\"+problem+".10D.pf[110]",
                dir +simplex+"\\"+problem+".10D.pf[275]",
                dir +simplex+"\\"+problem+".10D.pf[440]",
                dir +simplex+"\\"+problem+".10D.pf[935]",
                dir +simplex+"\\"+problem+".10D.pf[1430]",
                dir +simplex+"\\"+problem+".15D.pf[135]",
                dir +simplex+"\\"+problem+".15D.pf[240]",
                dir +simplex+"\\"+problem+".15D.pf[800]",
                dir +simplex+"\\"+problem+".15D.pf[1360]"
        };

        String[] refFile = {
                dir + problem+".3D.pf[5050]",
                dir + problem+".3D.pf[5050]",
                dir + problem+".3D.pf[5050]",
                dir + problem+".3D.pf[5050]",
                dir + problem+".5D.pf[14950]",
                dir + problem+".5D.pf[14950]",
                dir + problem+".5D.pf[14950]",
                dir + problem+".8D.pf[31824]",
                dir + problem+".8D.pf[31824]",
                dir + problem+".8D.pf[31824]",
                dir + problem+".8D.pf[31824]",
                dir + problem+".10D.pf[43758]",
                dir + problem+".10D.pf[43758]",
                dir + problem+".10D.pf[43758]",
                dir + problem+".15D.pf[54264]",
                dir + problem+".15D.pf[54264]",
                dir + problem+".15D.pf[54264]",


                dir + problem+".3D.pf[5050]",
                dir + problem+".3D.pf[5050]",
                dir + problem+".3D.pf[5050]",
                dir + problem+".3D.pf[5050]",
                dir + problem+".3D.pf[5050]",
                dir + problem+".5D.pf[14950]",
                dir + problem+".5D.pf[14950]",
                dir + problem+".5D.pf[14950]",
                dir + problem+".5D.pf[14950]",
                dir + problem+".5D.pf[14950]",
                dir + problem+".5D.pf[14950]",
                dir + problem+".8D.pf[31824]",
                dir + problem+".8D.pf[31824]",
                dir + problem+".8D.pf[31824]",
                dir + problem+".8D.pf[31824]",
                dir + problem+".10D.pf[43758]",
                dir + problem+".10D.pf[43758]",
                dir + problem+".10D.pf[43758]",
                dir + problem+".10D.pf[43758]",
                dir + problem+".10D.pf[43758]",
                dir + problem+".10D.pf[43758]",
                dir + problem+".15D.pf[54264]",
                dir + problem+".15D.pf[54264]",
                dir + problem+".15D.pf[54264]",
                dir + problem+".15D.pf[54264]"
        };


        Point point3Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7});
        Point point3D = new ArrayPoint(new double[]{1.1, 1.1, 1.1});
        Point point5Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7, 0.7, 0.7});
        Point point5D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1});
        Point point8Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7});
        Point point8D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1});
        Point point10Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7});
        Point point10D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1});
        Point point15Dmin = new ArrayPoint(new double[]{0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7,0.7, 0.7, 0.7});
        Point point15D = new ArrayPoint(new double[]{1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1,1.1, 1.1, 1.1});


        Point[] hvRefPointList = {
                point3Dmin, point3Dmin, point3Dmin, point3Dmin,
                point5Dmin, point5Dmin, point5Dmin,
                point8Dmin, point8Dmin, point8Dmin, point8Dmin,
                point10Dmin, point10Dmin, point10Dmin,
                point15Dmin, point15Dmin, point15Dmin,

                point3Dmin, point3Dmin, point3Dmin, point3Dmin,point3Dmin,
                point5Dmin, point5Dmin, point5Dmin, point5Dmin, point5Dmin, point5Dmin,
                point8Dmin, point8Dmin, point8Dmin, point8Dmin,
                point10Dmin, point10Dmin, point10Dmin, point10Dmin, point10Dmin, point10Dmin,
                point15Dmin, point15Dmin, point15Dmin, point15Dmin,
        };

        Point pointZero3D = new ArrayPoint(new double[]{0,0,0});
        Point pointZero5D = new ArrayPoint(new double[]{0,0,0,0,0});
        Point pointZero8D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0});
        Point pointZero10D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0});
        Point pointZero15D = new ArrayPoint(new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0});
        Point[] boundPointList = {
                pointZero3D, pointZero3D, pointZero3D, pointZero3D,
                pointZero5D, pointZero5D, pointZero5D,
                pointZero8D, pointZero8D, pointZero8D, pointZero8D,
                pointZero10D, pointZero10D, pointZero10D,
                pointZero15D, pointZero15D, pointZero15D,

                pointZero3D, pointZero3D, pointZero3D, pointZero3D, pointZero3D,
                pointZero5D, pointZero5D, pointZero5D, pointZero5D, pointZero5D, pointZero5D,
                pointZero8D, pointZero8D, pointZero8D, pointZero8D,
                pointZero10D, pointZero10D, pointZero10D, pointZero10D, pointZero10D, pointZero10D,
                pointZero15D, pointZero15D, pointZero15D, pointZero15D
        };

        int[] nobjList = {
                3, 3, 3, 3,
                5, 5, 5,
                8, 8, 8, 8,
                10,10,10,
                15,15,15,

                3, 3, 3, 3, 3,
                5, 5, 5, 5, 5, 5,
                8, 8, 8, 8,
                10,10,10,10,10,10,
                15,15,15,15
        };

//        Point[] hvRefPointList = {
//                point3D, point3D, point3D, point3D,
//                point5D, point5D, point5D,
//                point8D, point8D, point8D, point8D,
//                point10D, point10D, point10D,
//                point15D, point15D, point15D,
//
//                point3D, point3D, point3D, point3D,point3D,
//                point5D, point5D, point5D, point5D, point5D, point5D,
//                point8D, point8D, point8D, point8D,
//                point10D, point10D, point10D, point10D, point10D, point10D,
//                point15D, point15D, point15D, point15D,
//        };

        int aDim = 8;
        int numOfSample = 1000000;
        try {
            BufferedWriter writer = new DefaultFileOutputContext("D:\\Experiments\\UniformWeights\\analysis_"+problem+".dat").getFileWriter();

            for (int i = 0; i < instance.length; i++) {
                writer.write("<"+instance[i]+">");
                writer.newLine();
                Front referenceFront = null;
                WfgHypervolumeFront pf = null;
                try {
                    referenceFront = new ArrayFront(refFile[i]);
                    pf = new WfgHypervolumeFront(instance[i]);
                }catch (FileNotFoundException e){}
//                GeneralizedSpread spread = new GeneralizedSpread();
//                double spd = spread.generalizedSpread(pf,referenceFront);
//                writer.write("spread = "+spd);
//                writer.newLine();
//                Space space = new Space();
//                double sp = space.spaceIndicator(pf);
//                writer.write("space = "+sp);
//                writer.newLine();
                InvertedGenerationalDistance igdIndicator = new InvertedGenerationalDistance();
                double igd = igdIndicator.invertedGenerationalDistance(pf,referenceFront);
                writer.write("IGD = "+igd);
                writer.newLine();
                InvertedGenerationalDistancePlus igdPlusIndicator = new InvertedGenerationalDistancePlus();
                double igdPlus = igdPlusIndicator.invertedGenerationalDistancePlus(pf,referenceFront);
                writer.write("IGDPlus = "+igdPlus);
                writer.newLine();

                double hv = -1.0;
                if(nobjList[i] < aDim) {
                    WFGHypervolume hvIndicator = new WFGHypervolume();
                    hv = hvIndicator.evaluate(pf, hvRefPointList[i]);
                }else{
                    ApproximateHypervolume ahvIndicator = new ApproximateHypervolume();
                    ahvIndicator.setSamplePoints(numOfSample,hvRefPointList[i],boundPointList[i]);
                    hv = ahvIndicator.evaluate(pf, hvRefPointList[i]);
                }
                writer.write("HV = "+hv);
                writer.newLine();

                //[spread]"+spd+"\n[space]"+sp+"\n
                JMetalLogger.logger.info("\n"+instance[i]+"\n[IGD]"+igd+"\n[IGDPlus]"+igdPlus+"\n[HV]"+hv+"\n");
            }
            writer.close();
        }catch (IOException e){}

    }
}
