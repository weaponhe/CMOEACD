package org.uma.jmetal.problem.multiobjective;

import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.problem.impl.AbstractIntegerPermutationProblem;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.solutionattribute.impl.MaximumConstraintViolation;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weaponhe on 2018/3/28.
 */
public class StockInvestment extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;
    public MaximumConstraintViolation<DoubleSolution> maximumConstraintViolationDegree;

    private int totalVolume;//可用于投资的总金额
    private int[] stockPrices;//股票价格数组，维度为n
    private int[] initStocks;//已持有股票数组，维度为n
    private double k;//交易费用比例
    private double[] expectedReturnRates;//期望收益率数组，维度为n
    private double[][] returnVariances;//收益率协方差，维度为n*n

    public StockInvestment(int n,
                           int totalVolume,
                           int[] stockPrices,
                           int[] initStocks,
                           double k,
                           double[] expectedReturnRates,
                           double[][] returnVariances) {
        setNumberOfVariables(n);
        setNumberOfObjectives(2);
        setNumberOfConstraints(1);
        setName("StockInvestment");

        overallConstraintViolationDegree = new OverallConstraintViolation<>();
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<>();
        maximumConstraintViolationDegree = new MaximumConstraintViolation<>();

        this.totalVolume = totalVolume;
        this.stockPrices = stockPrices;
        this.initStocks = initStocks;
        this.k = k;
        this.expectedReturnRates = expectedReturnRates;
        this.returnVariances = returnVariances;

        List<Double> lowerLimit = new ArrayList<>();
        List<Double> upperLimit = new ArrayList<>();
        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0.0);
            upperLimit.add((double) totalVolume / stockPrices[i]);
        }
        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
    }

    /**
     * Evaluate() method
     */
    public void evaluate(DoubleSolution solution) {
        int numberOfVariables = getNumberOfVariables();
        int numberOfObjectives = getNumberOfObjectives();

        double[] f = new double[numberOfObjectives];
        double[] x = new double[numberOfVariables];

        //对决策变量进行四舍五入
        for (int i = 0; i < numberOfVariables; i++) {
            solution.setVariableValue(i, (double) Math.round(solution.getVariableValue(i)));
            x[i] = solution.getVariableValue(i);
        }

        f[0] = 0;
        for (int i = 0; i < numberOfVariables; i++) {
            for (int j = 0; j < numberOfVariables; j++) {
                f[0] += (x[i] * stockPrices[i] / totalVolume) * (x[j] * stockPrices[j] / totalVolume) * returnVariances[i][j];
            }
        }

        for (int i = 0; i < numberOfVariables; i++) {
            f[1] += (x[i] * stockPrices[i] / totalVolume) * expectedReturnRates[i] - k * Math.abs(x[i] - initStocks[i]) * stockPrices[i] / totalVolume;
        }
        f[1] = -f[1];

        for (int i = 0; i < numberOfObjectives; i++) {
            solution.setObjective(i, f[i]);
        }
    }

    public void evaluateConstraints(DoubleSolution solution) {
        double[] constraint = new double[this.getNumberOfConstraints()];
        double volume = 0;
        for (int i = 0; i < getNumberOfVariables(); i++) {
            volume += solution.getVariableValue(i) * stockPrices[i];
        }
        constraint[0] = totalVolume - volume;

        solution.setConstraintViolation(0, constraint[0]);

        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;
        double maximumConstraintViolation = 0.0;
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            if (constraint[i] < 0.0) {
                overallConstraintViolation += constraint[i];
                violatedConstraints++;
                maximumConstraintViolation = Math.min(maximumConstraintViolation, constraint[i]);
            }
        }

        solution.setAttribute("overallConstraintViolationDegree", overallConstraintViolation);
        overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
        maximumConstraintViolationDegree.setAttribute(solution, maximumConstraintViolation);
    }

}
