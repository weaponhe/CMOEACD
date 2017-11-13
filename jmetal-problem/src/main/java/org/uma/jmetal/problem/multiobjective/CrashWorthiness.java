package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/8/12.
 */
public class CrashWorthiness extends AbstractDoubleProblem {
        /**
         * Constructor
         */
        public CrashWorthiness() {
            setNumberOfVariables(5);
            setNumberOfObjectives(3);
            setNumberOfConstraints(0);
            setName("CrashWorthiness");
            List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
            List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

            for (int i=0;i<getNumberOfVariables();i++){
                lowerLimit.add(1.0);
                upperLimit.add(3.0);
            }
            setLowerLimit(lowerLimit);
            setUpperLimit(upperLimit);
        }

        /** Evaluate() method */
        @Override
        public void evaluate(DoubleSolution solution) {
            double [] fx = new double[3] ;
            double [] x = new double[getNumberOfVariables()] ;
            for (int i = 0 ; i < getNumberOfVariables(); i++)
                x[i] = solution.getVariableValue(i) ;

            fx[0] = 1640.2823 + 2.3573285*x[0] + 2.3220035*x[1] + 4.5688768*x[2] + 7.7213633*x[3] + 4.4559504*x[4];
            fx[1] = 6.5856 + 1.15*x[0] - 1.0427*x[1] + 0.9738*x[2] + 0.8364*x[3] - 0.3695*x[0]*x[3] + 0.0861*x[0]*x[4] + 0.3628*x[1]*x[3] - 0.1106*x[0]*x[0] - 0.3437*x[2]*x[2] + 0.1764*x[3]*x[3];
            fx[2] = -0.0551 + 0.0181*x[0] + 0.1024*x[1] + 0.0421*x[2] - 0.0073*x[0]*x[1] + 0.024*x[1]*x[2] - 0.0118*x[1]*x[3] - 0.0204*x[2]*x[3] - 0.008*x[2]*x[4] - 0.0241*x[1]*x[1] + 0.0109*x[3]*x[3];

            solution.setObjective(0,fx[0]);
            solution.setObjective(1,fx[1]);
            solution.setObjective(2,fx[2]);
        }
    }
