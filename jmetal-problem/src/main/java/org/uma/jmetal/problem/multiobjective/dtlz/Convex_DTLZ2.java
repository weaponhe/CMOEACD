package org.uma.jmetal.problem.multiobjective.dtlz;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X250 on 2016/4/17.
 */
public class Convex_DTLZ2 extends DTLZ2{
    public Convex_DTLZ2() throws ClassNotFoundException, JMetalException {
        this(12, 3);
    }
    public Convex_DTLZ2(Integer numberOfVariables, Integer numberOfObjectives) throws JMetalException {
        super(numberOfVariables, numberOfObjectives);
        setName("Convex_DTLZ2");
    }
    public void evaluate(DoubleSolution solution) {
        super.evaluate(solution);
        for(int i=0;i<getNumberOfObjectives()-1;++i){
            solution.setObjective(i , Math.pow(solution.getObjective(i),4));
        }
        solution.setObjective(getNumberOfObjectives()-1,Math.pow(solution.getObjective(getNumberOfObjectives()-1),2));
    }
}

