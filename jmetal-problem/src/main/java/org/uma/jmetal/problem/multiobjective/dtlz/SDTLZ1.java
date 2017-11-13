package org.uma.jmetal.problem.multiobjective.dtlz;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/4/27.
 */
public class SDTLZ1 extends DTLZ1 {
    double factor;

    public SDTLZ1() {
        this(7, 3,10.0);
    }

    public SDTLZ1(Integer numberOfVariables, Integer numberOfObjectives) throws JMetalException {
       this(numberOfVariables,numberOfObjectives,10.0);
    }

    public SDTLZ1(Integer numberOfVariables, Integer numberOfObjectives,Double scalarFactor) throws JMetalException {
        super(numberOfVariables, numberOfObjectives);
        factor = scalarFactor;
        setName("SDTLZ1");
    }

    /** Evaluate() method */
    public void evaluate(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables();
        int numberOfObjectives = getNumberOfObjectives() ;
        super.evaluate(solution);
        for (int i = 0; i < numberOfObjectives; i++) {
            solution.setObjective(i, solution.getObjective(i)*Math.pow(factor,i));
        }
    }
}
