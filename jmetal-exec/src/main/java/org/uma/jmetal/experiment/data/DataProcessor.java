package org.uma.jmetal.experiment.data;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.cdtlz.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.Point;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weaponhe on 2017/11/21.
 */
public class DataProcessor {
    public static void main(String[] args) throws IOException {
        int[] dimentions = {3, 5, 8, 10, 15};

//        for (int i = 0; i < dimentions.length; i++) {
//            String fileName = String.format("/pareto_fronts/DTLZ2.%dD.pf", dimentions[i]);
//            String outputFileName = String.format("jmetal-core/src/main/resources/pareto_fronts/C2_DTLZ2.%dD.pf", dimentions[i]);
//            ArrayFront front = new ArrayFront(fileName);
//            C2_DTLZ2 problem = new C2_DTLZ2(dimentions[i] + 9, dimentions[i]);
//            List<Point> res = getFessibleFront(front, problem);
//            writeFile(outputFileName, res);
//            System.out.println((double) res.size() / front.getNumberOfPoints());
//        }

//        for (int i = 0; i < dimentions.length; i++) {
//            String fileName = String.format("/pareto_fronts/Convex_DTLZ2.%dD.pf", dimentions[i]);
//            String outputFileName = String.format("jmetal-core/src/main/resources/pareto_fronts/C2_Convex_DTLZ2.%dD.pf", dimentions[i]);
//            ArrayFront front = new ArrayFront(fileName);
//            ConvexC2_DTLZ2 problem = new ConvexC2_DTLZ2(dimentions[i] + 9, dimentions[i]);
//            List<Point> res = getFessibleFront(front, problem);
//            writeFile(outputFileName, res);
//            System.out.println((double) res.size() / front.getNumberOfPoints());
//        }

        int M = 3;
        String fileName = "/pareto_fronts/DTLZ1.3D.pf";
        ArrayFront front = new ArrayFront(fileName);
        for (int i = 0; i < front.getNumberOfPoints(); i++) {
            Point point = front.getPoint(i);
            double sum = 0.0;
            for (int j = 0; j < point.getNumberOfDimensions() - 1; j++) {
                sum += point.getDimensionValue(j);
            }
            double lastValue = Double.MIN_VALUE;
            for (int k = 0; k < M; k++) {

            }
        }
    }

    static public List<Point> getFessibleFront(ArrayFront front, DoubleProblem problem) {
        List<Point> res = new ArrayList<>();
        for (int pi = 0; pi < front.getNumberOfPoints(); pi++) {
            Point point = front.getPoint(pi);
            DoubleSolution s = new DefaultDoubleSolution(problem);
            for (int i = 0; i < s.getNumberOfObjectives(); i++) {
                s.setObjective(i, point.getDimensionValue(i));
            }
            ((ConstrainedProblem) problem).evaluateConstraints(s);
            double cv = (double) s.getAttribute("overallConstraintViolationDegree");
            if (cv >= 0) {
                res.add(point);
            }
        }
        return res;
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