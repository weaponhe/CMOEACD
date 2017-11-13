package org.uma.jmetal.problem.multiobjective.dtlz;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

/**
 * Created by X250 on 2016/9/3.
 */
public class SDTLZ3 extends DTLZ3{
    double factor;

    public SDTLZ3() {
        this(12, 3,10.0);
    }

    public SDTLZ3(Integer numberOfVariables, Integer numberOfObjectives) throws JMetalException {
        this(numberOfVariables,numberOfObjectives,10.0);
    }

    public SDTLZ3(Integer numberOfVariables, Integer numberOfObjectives,Double scalarFactor) throws JMetalException {
        super(numberOfVariables, numberOfObjectives);
        factor = scalarFactor;
        setName("SDTLZ3");
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
