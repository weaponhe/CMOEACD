package org.uma.jmetal.experiment.PFGeneration;

import org.uma.jmetal.experiment.PFGeneration.DTLZ.*;
import org.uma.jmetal.experiment.PFGeneration.MOP.*;
import org.uma.jmetal.util.JMetalLogger;

/**
 * Created by X250 on 2016/7/24.
 */
public class PFGeneration {
    public static void main(String[] argv) {
        PFGenerator pfGenerator = null;

//        int[] nobjList = {
//                3, 5, 8, 10, 15
//        };
//
////        int[] popsList = {
////                109, 251, 165, 286, 136
////        };
////        int[] HList = {
////                8, 4, 3, 3, 2
////        };
//
//        int[] popsList = {
//                15151, 25081, 43713, 92378, 170544
//        };
//
//        int[] HList = {
////                100, 18, 10, 9, 7
//                200,36,20,18,14
//        };
//        int[] nobjList = {
//                5
//        };
//
//
//        int[] popsList = {
//                15981
//        };
//
//        int[] HList = {
//              16
//        };
//
//        int[] nobjList = {
//                3, 3, 3, 3,
//                5, 5, 5,
//                8, 8, 8, 8,
//                10,10,10,
//                15,15,15
//        };
//        int[] HList = {
//                7,8,13,14,
//                4,5,6,
//                2,3,4,7,
//                2,3,4,
//                2,3,4
//        };
//        int[] popsList = {
//                85, 109, 274, 316,
//                126, 251, 456,
//                45, 165, 495, 6435,
//                66, 286, 1001,
//                136, 816, 3876
//        };
//


//        String saveDir = "D://Experiments\\UniformWeights\\";
//        for (int i = 0; i < popsList.length; i++) {
//
//            JMetalLogger.logger.info("/nDTLZ1\tpops: " + popsList[i] + "\tnObj: " + nobjList[i] + "\tH: " + HList[i] + "\n");
//            pfGenerator = new DTLZ1PFGenerator(popsList[i], nobjList[i], HList[i]);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//
//            JMetalLogger.logger.info("/nDTLZ2\tpops: " + popsList[i] + "\tnObj: " + nobjList[i] + "\tH: " + HList[i] + "\n");
//            pfGenerator = new DTLZ2PFGenerator(popsList[i], nobjList[i], HList[i]);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//
//            JMetalLogger.logger.info("/nDTLZ3\tpops: " + popsList[i] + "\tnObj: " + nobjList[i] + "\tH: " + HList[i] + "\n");
//            pfGenerator = new DTLZ3PFGenerator(popsList[i], nobjList[i], HList[i]);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//
//            JMetalLogger.logger.info("/nDTLZ4\tpops: " + popsList[i] + "\tnObj: " + nobjList[i] + "\tH: " + HList[i] + "\n");
//            pfGenerator = new DTLZ4PFGenerator(popsList[i], nobjList[i], HList[i]);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//
//            JMetalLogger.logger.info("/nConvex_DTLZ2\tpops: " + popsList[i] + "\tnObj: " + nobjList[i] + "\tH: " + HList[i] + "\n");
//            pfGenerator = new ConvexDTLZ2PFGenerator(popsList[i], nobjList[i], HList[i]);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//        }


//        int[] nobjList = {
//                3, 3, 3, 3, 3,
//                5, 5, 5, 5, 5, 5,
//                8, 8, 8, 8,
//                10,10,10,10,10,10,
//                15,15,15,15
//        };
//        int[][] arrayHList = {
//                {12},{13},{22},{23},{24},
//                {5},{6},{7},{8},{3,4},{4,4},
//                {8},{2,3},{3,4},{3,3},
//                {1,2},{2,2},{2,3},{3,3},{3,4},{4,4},
//                {1,2},{2,2},{2,3},{3,3}
//        };
//        double[][] arrayTaoList = {
//                {1.0},{1.0},{1.0},{1.0},{1.0},
//                {1.0},{1.0},{1.0},{1.0},{0.5,1.0},{0.5,1.0},
//                {1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0},
//                {0.5,1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0},
//                {0.5,1.0},{0.5,1.0},{0.5,1.0},{0.5,1.0}
//        };
//        int[] popsList = {
//                91, 105, 276, 300, 325,
//                105, 140, 126, 210, 330 ,495,
//                156, 240, 450, 6435,
//                65, 110, 275, 440, 935, 1430,
//                135, 240, 800, 1360
//        };
//        int[] nobjList = {
//                3,
//                5,
//                8,
//                10,
//                15
//        };
//        int[][] arrayHList = {
//                {12},
//                {6},
//                {2,3},
//                {2,3},
//                {1,2}
//        };
//        double[][] arrayTaoList = {
//                {1.0},
//                {1.0},
//                {0.5,1.0},
//                {0.5,1.0},
//                {0.5,1.0}
//        };
//        int[] popsList = {
//                91,
//                210,
//                156,
//                275,
//                135
//        };

//        int[] nobjList = {
//                3,
//                5,
//                10
//        };
//        int[][] arrayHList = {
//                {3},
//                {1,2},
//                {1,2}
//        };
//        double[][] arrayTaoList = {
//                {1.0},
//                {0.5,1.0},
//                {0.5,1.0}
//        };
//        int[] popsList = {
//                10,
//                20,
//                65
//        };
//        int[] nobjList = {
//                3
//        };
//        int[][] arrayHList = {
//                {125}
//        };
//        double[][] arrayTaoList = {
//                {1.0}
//        };
//        int[] popsList = {
//                8001
//        };
//        String saveDir = "D://Experiments\\UniformWeights\\";
//        for (int i = 0; i < popsList.length; i++) {
//
//            JMetalLogger.logger.info("/nDTLZ1\tpops: " + popsList[i] + "\tnObj: " + nobjList[i] + "\n");
//            pfGenerator = new DTLZ1PFGenerator(popsList[i], nobjList[i], arrayHList[i],arrayTaoList[i]);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//
//            JMetalLogger.logger.info("/nDTLZ2\tpops: " + popsList[i] + "\tnObj: " + nobjList[i] + "\t\n");
//            pfGenerator = new DTLZ2PFGenerator(popsList[i], nobjList[i], arrayHList[i],arrayTaoList[i]);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//
//            JMetalLogger.logger.info("/nConvex_DTLZ2\tpops: " + popsList[i] + "\tnObj: " + nobjList[i] + "\t\n");
//            pfGenerator = new ConvexDTLZ2PFGenerator(popsList[i], nobjList[i], arrayHList[i],arrayTaoList[i]);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//        }
//


//        int[] nobjList = {
//                3
//        };
//        int[][] arrayHList = {
//                {125}
//        };
//        double[][] arrayTaoList = {
//                {1.0}
//        };
//        int[] popsList = {
//                8001
//        };
//        String saveDir = "D://Experiments\\UniformWeights\\";
//        for (int i = 0; i < popsList.length; i++) {
//
//            JMetalLogger.logger.info("/nMOP6\tpops: " + popsList[i] + "\tnObj: " + nobjList[i] + "\n");
//            pfGenerator = new MOP6PFGenerator(popsList[i], arrayHList[i],arrayTaoList[i]);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//
//            JMetalLogger.logger.info("/nMOP7\tpops: " + popsList[i] + "\tnObj: " + nobjList[i] + "\n");
//            pfGenerator = new MOP7PFGenerator(popsList[i], arrayHList[i],arrayTaoList[i]);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//        }

//        String saveDir = "D://Experiments\\UniformWeights\\";

//            JMetalLogger.logger.info("/nMOP1\n");
//            pfGenerator = new MOP1PFGenerator(4000,3999);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//
//        JMetalLogger.logger.info("/nMOP2\n");
//        pfGenerator = new MOP2PFGenerator(4000,3999);
//        pfGenerator.generatePF();
//        pfGenerator.printPF(saveDir);
//
//        JMetalLogger.logger.info("/nMOP3\n");
//        pfGenerator = new MOP3PFGenerator(4000,3999);
//        pfGenerator.generatePF();
//        pfGenerator.printPF(saveDir);

        //for (int i=12000;i<13000;i++) {
//        {
//        int i = 12498;
//            JMetalLogger.logger.info("/nMOP4\n");
//            pfGenerator = new MOP4PFGenerator(i, i - 1);
//            pfGenerator.generatePF();
//            pfGenerator.printPF(saveDir);
//        }
//        JMetalLogger.logger.info("/nMOP5\n");
//        pfGenerator = new MOP5PFGenerator(4000,3999);
//        pfGenerator.generatePF();
//        pfGenerator.printPF(saveDir);
    }
}
