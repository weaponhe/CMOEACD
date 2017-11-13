package org.uma.jmetal.problem.multiobjective.dtlz;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

/**
 * Created by X250 on 2016/9/3.
 */
public class Convex_SDTLZ2 extends Convex_DTLZ2{
    double factor;

    public Convex_SDTLZ2() {
        this(12, 3,10.0);
    }

    public Convex_SDTLZ2(Integer numberOfVariables, Integer numberOfObjectives) throws JMetalException {
        this(numberOfVariables,numberOfObjectives,10.0);
    }

    public Convex_SDTLZ2(Integer numberOfVariables, Integer numberOfObjectives,Double scalarFactor) throws JMetalException {
        super(numberOfVariables, numberOfObjectives);
        factor = scalarFactor;
        setName("Convex_SDTLZ2");
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
