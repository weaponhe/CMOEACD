package org.uma.jmetal.experiment.data;

import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.UniformWeightUtils;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;
import org.uma.jmetal.util.point.impl.ArrayPoint;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weaponhe on 2017/11/22.
 */
public class C3ParetoGenerator {
    public static void main(String[] args) {

        //nObj H size
        //3 140 10011
        //5 12 1820
        //8 9 11440
        //10 7 11440
        //15 5 11628

//        int[] objectives = {2};
        int[] objectives = {2, 3, 5, 8, 10, 15};
        int[][] arrayHs = {{10000}, {140}, {12}, {9}, {7}, {5}};
        double[] integratedTaus = {1.0};

        for (int idx = 0; idx < objectives.length; idx++) {
            int nObj = objectives[idx];
            int[] arrayH = arrayHs[idx];
            String outputFileName = String.format("jmetal-core/src/main/resources/pareto_fronts/C3_DTLZ1.%dD.pf", nObj);
            List<double[]> uniformDirections = UniformWeightUtils.generateArrayList(arrayH, integratedTaus, nObj);
            List<Point> front = generateC3DTLZ1(uniformDirections);
            writeFile(outputFileName, front);
        }

        for (int idx = 0; idx < objectives.length; idx++) {
            int nObj = objectives[idx];
            int[] arrayH = arrayHs[idx];
            String outputFileName = String.format("jmetal-core/src/main/resources/pareto_fronts/C3_DTLZ4.%dD.pf", nObj);
            List<double[]> uniformDirections = UniformWeightUtils.generateArrayList(arrayH, integratedTaus, nObj);
            List<Point> front = generateC3DTLZ4(uniformDirections);
            writeFile(outputFileName, front);
        }

    }

    static public List<Point> generateC3DTLZ1(List<double[]> uniformDirections) {
        int nObj = uniformDirections.get(0).length;
        List<Point> front = new ArrayList<>();
        for (int i = 0; i < uniformDirections.size(); i++) {
            double[] d = uniformDirections.get(i);
            double best = Double.MAX_VALUE;
            for (int j = 0; j < nObj; j++) {
                double co = 1.0 - d[j] + d[j] / 0.5;
                if (co < 0) {
                    System.out.println("!!!");
                }
                best = Math.min(best, co);
            }
            double a = 1.0 / best;
            double[] p = new double[nObj];
            for (int j = 0; j < nObj; j++) {
                p[j] = d[j] * a;
            }
            Point point = new ArrayPoint(p);
            front.add(point);
        }
        return front;
    }

    static public List<Point> generateC3DTLZ4(List<double[]> uniformDirections) {
        int nObj = uniformDirections.get(0).length;
        List<Point> front = new ArrayList<>();
        for (int i = 0; i < uniformDirections.size(); i++) {
            double[] d = uniformDirections.get(i);
            double best = Double.MAX_VALUE;
            double sum = 0;
            for (int j = 0; j < nObj; j++) {
                sum += Math.pow(d[j], 2.0);
            }
            for (int j = 0; j < nObj; j++) {
                double co = Math.pow(d[j], 2.0) / 4.0 + sum - Math.pow(d[j], 2.0);
                if (co < 0) {
                    System.out.println("!!!");
                }
                best = Math.min(best, co);
            }
            double a = Math.sqrt(1.0 / best);
            double[] p = new double[nObj];
            for (int j = 0; j < nObj; j++) {
                p[j] = d[j] * a;
            }
            Point point = new ArrayPoint(p);
            front.add(point);
        }
        return front;
    }

    static public void writeFile(String file, List<Point> front) {
        resetFile(file);
        FileWriter os;
        try {
            os = new FileWriter(file, true);
            for (int i = 0; i < front.size(); i++) {
                String str = "";
                Point point = front.get(i);
                for (int j = 0; j < point.getNumberOfDimensions(); j++) {
                    str += point.getDimensionValue(j) + "\t";

                }
                str += "\n";
                os.write(str);
            }
            os.close();
        } catch (IOException ex) {
            throw new JMetalException("Error writing indicator file" + ex);
        }
    }

    static public void resetFile(String file) {
        File f = new File(file);
        if (f.exists()) {
            JMetalLogger.logger.info("File " + file + " exist.");

            if (f.isDirectory()) {
                JMetalLogger.logger.info("File " + file + " is a directory. Deleting directory.");
                if (f.delete()) {
                    JMetalLogger.logger.info("Directory successfully deleted.");
                } else {
                    JMetalLogger.logger.info("Error deleting directory.");
                }
            } else {
                JMetalLogger.logger.info("File " + file + " is a file. Deleting file.");
                if (f.delete()) {
                    JMetalLogger.logger.info("File succesfully deleted.");
                } else {
                    JMetalLogger.logger.info("Error deleting file.");
                }
            }
        } else {
            JMetalLogger.logger.info("File " + file + " does NOT exist.");
        }
    }
}
