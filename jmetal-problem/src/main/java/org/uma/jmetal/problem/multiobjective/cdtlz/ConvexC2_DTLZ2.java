//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal.problem.multiobjective.cdtlz;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.multiobjective.dtlz.Convex_DTLZ2;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.MaximumConstraintViolation;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem ConvexC2-DTLZ2, defined in:
 * Jain, H. and K. Deb.  "An Evolutionary Many-Objective Optimization Algorithm Using Reference-Point-Based
 * Nondominated Sorting Approach, Part II: Handling Constraints and Extending to an Adaptive Approach."
 * EEE Transactions on Evolutionary Computation, 18(4):602-622, 2014.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ConvexC2_DTLZ2 extends Convex_DTLZ2 implements ConstrainedProblem<DoubleSolution> {
  public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree ;
  public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints ;
  public MaximumConstraintViolation<DoubleSolution> maximumConstraintViolationDegree;

  private static Map<Integer, Double> rValue;

  static {
    rValue = new HashMap<Integer, Double>() ;
    rValue.put(3, 0.225) ;
    rValue.put(5, 0.225) ;
    rValue.put(8, 0.26) ;
    rValue.put(10, 0.26) ;
    rValue.put(15, 0.27) ;
  }

  /**
   * Constructor
   * @param numberOfVariables
   * @param numberOfObjectives
   */
  public ConvexC2_DTLZ2(int numberOfVariables, int numberOfObjectives) {
    super(numberOfVariables, numberOfObjectives) ;

    setNumberOfConstraints(1);
    setName("ConvexC2_DTLZ2");

    overallConstraintViolationDegree = new OverallConstraintViolation<DoubleSolution>() ;
    numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>() ;
    maximumConstraintViolationDegree = new MaximumConstraintViolation<>();
  }

  @Override
  public void evaluateConstraints(DoubleSolution solution) {
    double[] constraint = new double[getNumberOfConstraints()];


    double sum = 0 ;
    for (int i = 0; i < getNumberOfObjectives(); i++) {
      sum += solution.getObjective(i) ;
    }

    double lambda = sum / getNumberOfObjectives() ;

    sum = 0 ;
    for (int i = 0; i < getNumberOfObjectives(); i++) {
      sum += Math.pow(solution.getObjective(i) - lambda, 2.0) ;
    }

    constraint[0] = sum - Math.pow(rValue.get(getNumberOfObjectives()), 2.0) ;

    solution.setConstraintViolation(0,constraint[0]);

    double overallConstraintViolation = 0.0;
    int violatedConstraints = 0;
    double maximumConstraintViolation = 0.0;
    for (int i = 0; i < getNumberOfConstraints(); i++) {
      if (constraint[i]<0.0){
        overallConstraintViolation+=constraint[i];
        violatedConstraints++;
        maximumConstraintViolation = Math.min(maximumConstraintViolation,constraint[i]);
      }
    }

    solution.setAttribute("overallConstraintViolationDegree", overallConstraintViolation);
    overallConstraintViolationDegree.setAttribute(solution,overallConstraintViolation);
    numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
    maximumConstraintViolationDegree.setAttribute(solution,maximumConstraintViolation);
  }
}
