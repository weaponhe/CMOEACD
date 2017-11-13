package org.uma.jmetal.problem.multiobjective.dtlz;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

/**
 * Created by X250 on 2016/4/27.
 */
public class SDTLZ2 extends DTLZ2{
    double factor;

    public SDTLZ2() {
        this(12, 3,10.0);
    }

    public SDTLZ2(Integer numberOfVariables, Integer numberOfObjectives) throws JMetalException {
        this(numberOfVariables,numberOfObjectives,10.0);
    }

    public SDTLZ2(Integer numberOfVariables, Integer numberOfObjectives,Double scalarFactor) throws JMetalException {
        super(numberOfVariables, numberOfObjectives);
        factor = scalarFactor;
        setName("SDTLZ2");
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
